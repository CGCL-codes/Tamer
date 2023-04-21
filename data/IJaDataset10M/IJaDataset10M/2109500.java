package com.googlecode.gwt.test.plugin.editors;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.internal.Workbench;
import org.eclipse.ui.texteditor.ITextEditor;

public class MacroHyperLink implements IHyperlink {

    private String macroName;

    private IRegion fRegion;

    private IProject currentProject;

    public MacroHyperLink(IRegion macroRegion, String macroName, IProject currentProject) {
        this.fRegion = macroRegion;
        this.macroName = macroName;
        this.currentProject = currentProject;
    }

    @Override
    public IRegion getHyperlinkRegion() {
        return this.fRegion;
    }

    @Override
    public String getHyperlinkText() {
        return this.macroName;
    }

    @Override
    public String getTypeLabel() {
        return null;
    }

    @Override
    public void open() {
        String filpath = ProjectPluginProperty.getInstance().getMacroResource(currentProject).getFileOfMacro(macroName).toString();
        String ProjectPath = currentProject.getLocation().toFile().toString();
        filpath = filpath.replace(ProjectPath, "");
        IFile file = currentProject.getFile(filpath);
        try {
            IEditorPart editorPart = IDE.openEditor(Workbench.getInstance().getActiveWorkbenchWindow().getActivePage(), file, true);
            this.goToLine(editorPart, ProjectPluginProperty.getInstance().getMacroResource(currentProject).getMacrosLine(macroName));
        } catch (PartInitException e) {
            e.printStackTrace();
        }
    }

    private void goToLine(IEditorPart editorPart, int lineNumber) {
        if (!(editorPart instanceof ITextEditor) || lineNumber <= 0) {
            return;
        }
        ITextEditor editor = (ITextEditor) editorPart;
        IDocument document = editor.getDocumentProvider().getDocument(editor.getEditorInput());
        if (document != null) {
            IRegion lineInfo = null;
            try {
                lineInfo = document.getLineInformation(lineNumber - 1);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
            if (lineInfo != null) {
                editor.selectAndReveal(lineInfo.getOffset(), 0);
            }
        }
    }
}
