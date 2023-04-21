package com.aptana.ide.editors.wizards;

import java.io.File;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorRegistry;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.internal.editors.text.EditorsPlugin;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;
import com.aptana.ide.core.IdeLog;
import com.aptana.ide.core.StringUtils;
import com.aptana.ide.core.ui.CoreUIPlugin;
import com.aptana.ide.core.ui.CoreUIUtils;
import com.aptana.ide.editors.UnifiedEditorsPlugin;
import com.aptana.ide.editors.unified.IUnifiedEditor;

/**
 * A wizard for creating an untitled text file
 * 
 * @author Ingo Muschenetz
 */
public class UntitledTextFileWizard extends Wizard implements INewWizard {

    private IWorkbenchWindow fWindow;

    /**
	 * Constructor
	 */
    public UntitledTextFileWizard() {
    }

    /**
	 * Returns the "friendly name" to use when naming the file
	 * 
	 * @return The friendly name
	 */
    protected String getFriendlyName() {
        return StringUtils.EMPTY;
    }

    /**
	 * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#dispose()
	 */
    public void dispose() {
        fWindow = null;
    }

    /**
	 * Returns the file extension. "" in this case.
	 * 
	 * @return The string representing the extension.
	 */
    protected String getFileExtension() {
        return StringUtils.EMPTY;
    }

    private File queryFile() {
        IPath stateLocation = EditorsPlugin.getDefault().getStateLocation();
        IPath path = stateLocation.append("/_" + new Object().hashCode() + getFileExtension());
        return new File(path.toOSString());
    }

    /**
	 * Returns the ID of the default associated editor
	 * 
	 * @param file
	 *            The file to get the editor for
	 * @return The string ID of the associated editor
	 */
    protected String getEditorId(File file) {
        IWorkbench workbench = fWindow.getWorkbench();
        IEditorRegistry editorRegistry = workbench.getEditorRegistry();
        IEditorDescriptor descriptor = editorRegistry.getDefaultEditor(file.getName());
        if (descriptor != null) {
            return descriptor.getId();
        }
        return "com.aptana.ide.ui.UntitledTextEditor";
    }

    private IEditorInput createEditorInput(File file) {
        return CoreUIUtils.createNonExistingFileEditorInput(file, StringUtils.format(Messages.UntitledTextFileWizard_Untitled, getFriendlyName()));
    }

    /**
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
    public boolean performFinish() {
        File file = queryFile();
        IEditorInput input = createEditorInput(file);
        String editorId = getEditorId(file);
        IWorkbenchPage page = fWindow.getActivePage();
        try {
            IEditorPart part = page.openEditor(input, editorId);
            if (part instanceof ITextEditor) {
                ITextEditor editor = (ITextEditor) part;
                IDocumentProvider dp = editor.getDocumentProvider();
                IDocument doc = dp.getDocument(editor.getEditorInput());
                try {
                    String fileContents = getInitialFileContents();
                    if (fileContents != null) {
                        doc.replace(0, 0, fileContents);
                    }
                } catch (BadLocationException e) {
                    IdeLog.logError(UnifiedEditorsPlugin.getDefault(), Messages.UntitledTextFileWizard_Error, e);
                }
            }
            if (part instanceof IUnifiedEditor) {
                IUnifiedEditor te = (IUnifiedEditor) part;
                te.getViewer().getTextWidget().setCaretOffset(getInitialCaretOffset());
            }
        } catch (PartInitException e) {
            IdeLog.logError(CoreUIPlugin.getDefault(), Messages.UntitledTextFileWizard_UnableToInitializePart, e);
            return false;
        }
        return true;
    }

    /**
	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench,
	 *      org.eclipse.jface.viewers.IStructuredSelection)
	 */
    public void init(IWorkbench workbench, IStructuredSelection selection) {
        fWindow = workbench.getActiveWorkbenchWindow();
    }

    /**
	 * getInitialFileContents
	 * 
	 * @return Returns the initial file contents. Null by default.
	 */
    protected String getInitialFileContents() {
        return StringUtils.EMPTY;
    }

    /**
	 * getInitialCaretOffset
	 * 
	 * @return int
	 */
    protected int getInitialCaretOffset() {
        return getInitialFileContents().length();
    }
}
