package org.hoydaa.cs4eclipse.html.ui.actions;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import org.eclipse.core.filebuffers.FileBuffers;
import org.eclipse.core.filebuffers.ITextFileBuffer;
import org.eclipse.core.filebuffers.ITextFileBufferManager;
import org.eclipse.core.filebuffers.LocationKind;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.internal.WorkbenchPlugin;
import org.eclipse.ui.internal.editors.text.EditorsPlugin;
import org.eclipse.ui.texteditor.AbstractDecoratedTextEditorPreferenceConstants;
import org.hoydaa.cs4eclipse.core.generator.AbstractCodeSnippetGenerator;
import org.hoydaa.cs4eclipse.ui.Activator;
import org.hoydaa.cs4eclipse.ui.preferences.PreferenceConstants;
import org.hoydaa.cs4eclipse.html.core.generator.HTMLCodeSnippetGenerator;

/**
 * 
 * @author Utku Utkan
 * 
 */
@SuppressWarnings("restriction")
public class CopyToClipboardHTMLFileAction implements IObjectActionDelegate {

    private ISelection selection;

    @Override
    public void setActivePart(IAction action, IWorkbenchPart targetPart) {
    }

    @Override
    public void run(IAction action) {
        if (!(selection instanceof IStructuredSelection)) {
            return;
        }
        IStructuredSelection structuredSelection = (IStructuredSelection) selection;
        IFile file = (IFile) structuredSelection.getFirstElement();
        ITextFileBufferManager manager = FileBuffers.getTextFileBufferManager();
        try {
            manager.connect(file.getFullPath(), LocationKind.IFILE, null);
        } catch (CoreException e1) {
            e1.printStackTrace();
        }
        ITextFileBuffer fileBuffer = manager.getTextFileBuffer(file.getFullPath(), LocationKind.IFILE);
        IDocument document = fileBuffer.getDocument();
        IRegion region = new Region(0, document.getLength());
        IPreferenceStore ps = Activator.getDefault().getPreferenceStore();
        IPreferenceStore editorsPs = EditorsPlugin.getDefault().getPreferenceStore();
        IPreferenceStore workbenchPs = WorkbenchPlugin.getDefault().getPreferenceStore();
        AbstractCodeSnippetGenerator generator = new HTMLCodeSnippetGenerator();
        generator.setWhitespaceAllowed(ps.getBoolean(PreferenceConstants.ALLOW_WHITESPACES));
        generator.setTabWidth(editorsPs.getInt(AbstractDecoratedTextEditorPreferenceConstants.EDITOR_TAB_WIDTH));
        generator.setFont(PreferenceConverter.getFontData(workbenchPs, "org.eclipse.wst.sse.ui.textfont"));
        String codeSnippet = generator.generateCodeSnippet(document, region);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection copy = new StringSelection(codeSnippet);
        clipboard.setContents(copy, copy);
    }

    @Override
    public void selectionChanged(IAction action, ISelection selection) {
        this.selection = selection;
    }
}
