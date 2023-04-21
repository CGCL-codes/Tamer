package net.sourceforge.texlipse.actions;

import java.io.File;
import java.text.MessageFormat;
import net.sourceforge.texlipse.TexlipsePlugin;
import net.sourceforge.texlipse.bibeditor.BibEditor;
import net.sourceforge.texlipse.builder.KpsewhichRunner;
import net.sourceforge.texlipse.editor.TexEditor;
import net.sourceforge.texlipse.model.AbstractEntry;
import net.sourceforge.texlipse.model.TexCommandEntry;
import net.sourceforge.texlipse.properties.TexlipseProperties;
import net.sourceforge.texlipse.texparser.LatexParserUtils;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.SubStatusLineManager;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.texteditor.AbstractTextEditor;
import org.eclipse.ui.ide.IDE;

/**
 * This action opens the declaration of
 * <ul><li>References</li>
 * <li>Citations</li>
 * <li>Custom commands (/newcommand)</li>
 * <li>include, input and bibliography</li>
 * </ul>
 * 
 * @author Boris von Loesch
 */
public class OpenDeclarationAction implements IEditorActionDelegate {

    private IEditorPart targetEditor;

    /**
	 * Creates new action.
	 */
    public OpenDeclarationAction() {
        super();
    }

    public void setActiveEditor(IAction action, IEditorPart targetEditor) {
        this.targetEditor = targetEditor;
    }

    /**
     * Prints an error message on the status line and make a beep.
     * @param message   The error message
	 */
    private void createStatusLineErrorMessage(String message) {
        TexEditor editor;
        if (targetEditor instanceof TexEditor) {
            editor = (TexEditor) targetEditor;
            SubStatusLineManager slm = (SubStatusLineManager) targetEditor.getEditorSite().getActionBars().getStatusLineManager();
            slm.setErrorMessage(message);
            slm.setVisible(true);
            editor.getViewer().getTextWidget().getDisplay().beep();
        }
    }

    public void run(IAction action) {
        TexEditor editor;
        if (targetEditor instanceof TexEditor) {
            editor = (TexEditor) targetEditor;
        } else {
            throw new RuntimeException("Expecting text editor. Found: " + targetEditor.getClass().getName());
        }
        IProject project = editor.getProject();
        if (project == null) return;
        ITextSelection selection = (ITextSelection) editor.getSelectionProvider().getSelection();
        IDocument doc = editor.getDocumentProvider().getDocument(editor.getEditorInput());
        String docString = doc.get();
        IRegion comRegion = LatexParserUtils.getCommand(docString, selection.getOffset());
        if (comRegion == null) {
            createStatusLineErrorMessage(TexlipsePlugin.getResourceString("gotoDeclarationNoCommandFound"));
            return;
        }
        String command = docString.substring(comRegion.getOffset(), comRegion.getOffset() + comRegion.getLength());
        AbstractEntry refEntry = null;
        if (selection.getOffset() < comRegion.getOffset() + comRegion.getLength()) {
            AbstractEntry[] entries = editor.getDocumentModel().getRefMana().getCompletionsCom(command.substring(1), TexCommandEntry.NORMAL_CONTEXT);
            if (entries != null && entries.length > 0 && entries[0].fileName != null) {
                refEntry = entries[0];
            }
        }
        if (refEntry == null && (command.indexOf("ref") >= 0 || command.indexOf("cite") >= 0 || command.equals("\\input") || command.equals("\\include")) || command.equals("\\bibliography")) {
            IRegion region = null;
            region = LatexParserUtils.getCommandArgument(docString, comRegion.getOffset());
            if (region == null) {
                createStatusLineErrorMessage(TexlipsePlugin.getResourceString("gotoDeclarationNoArgumentFound"));
                return;
            }
            String ref = docString.substring(region.getOffset(), region.getOffset() + region.getLength());
            if (command.indexOf("ref") >= 0) {
                refEntry = editor.getDocumentModel().getRefMana().getLabel(ref);
            } else if (command.indexOf("cite") >= 0) {
                if (ref.indexOf(',') > 0) {
                    int cIndex = selection.getOffset() - region.getOffset();
                    if (cIndex < 0) {
                        createStatusLineErrorMessage(TexlipsePlugin.getResourceString("gotoDeclarationNoArgumentFound"));
                        return;
                    }
                    if (ref.charAt(cIndex) == ',') cIndex--;
                    int start = ref.lastIndexOf(',', cIndex) + 1;
                    if (start < 0) start = 0;
                    int end = ref.indexOf(',', cIndex);
                    if (end < 0) end = ref.length();
                    ref = ref.substring(start, end);
                }
                refEntry = editor.getDocumentModel().getRefMana().getBib(ref.trim());
            } else if (command.equals("\\include") || command.equals("\\input") || command.equals("\\bibliography")) {
                if (command.equals("\\bibliography")) {
                    if (!ref.toLowerCase().endsWith(".bib")) {
                        ref = ref + ".bib";
                    }
                } else {
                    if (!ref.toLowerCase().endsWith(".tex")) {
                        ref = ref + ".tex";
                    }
                }
                IContainer dir = TexlipseProperties.getProjectSourceDir(project);
                IResource file = dir.findMember(ref);
                if (file == null) {
                    IFile refFile = editor.getDocumentModel().getFile();
                    if (refFile != null) {
                        dir = refFile.getParent();
                        file = dir.findMember(ref);
                    }
                }
                if (file == null) {
                    createStatusLineErrorMessage(MessageFormat.format(TexlipsePlugin.getResourceString("gotoDeclarationNoFileFound"), new Object[] { ref }));
                    return;
                }
                try {
                    IDE.openEditor(editor.getEditorSite().getPage(), (IFile) file.getAdapter(IFile.class));
                } catch (PartInitException e) {
                    TexlipsePlugin.log("Open declaration:", e);
                }
                return;
            }
        }
        if (refEntry == null || refEntry.fileName == null) {
            createStatusLineErrorMessage(TexlipsePlugin.getResourceString("gotoDeclarationNoDeclarationFound"));
            return;
        }
        IFile file = project.getFile(refEntry.fileName);
        try {
            AbstractTextEditor part;
            if (!file.exists()) {
                KpsewhichRunner filesearch = new KpsewhichRunner();
                String filepath = filesearch.getFile(editor.getDocumentModel().getFile(), refEntry.fileName, "bibtex");
                if ("".equals(filepath)) {
                    createStatusLineErrorMessage(TexlipsePlugin.getResourceString("gotoDeclarationNoDeclarationFound"));
                    return;
                }
                File f = new File(filepath);
                part = (AbstractTextEditor) IDE.openEditor(editor.getEditorSite().getPage(), f.toURI(), BibEditor.ID, true);
            } else {
                part = (AbstractTextEditor) IDE.openEditor(editor.getEditorSite().getPage(), file);
            }
            IDocument doc2 = part.getDocumentProvider().getDocument(part.getEditorInput());
            int lineOffset = doc2.getLineOffset(refEntry.startLine - 1);
            int offset = 0;
            if (command.indexOf("ref") >= 0 && refEntry.position != null) {
                offset = refEntry.position.offset;
            }
            part.getEditorSite().getSelectionProvider().setSelection(new TextSelection(lineOffset + offset, 0));
        } catch (PartInitException e) {
            TexlipsePlugin.log("Jump2Label PartInitException", e);
        } catch (BadLocationException e) {
            TexlipsePlugin.log("Jump2Label BadLocationException", e);
        } catch (CoreException ce) {
            TexlipsePlugin.log("Can't run Kpathsea", ce);
        }
    }

    public void selectionChanged(IAction action, ISelection selection) {
        action.setEnabled(targetEditor instanceof TexEditor);
    }
}
