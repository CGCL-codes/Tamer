package de.beas.explicanto.client.rcp.editor;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.MessageFormat;
import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IPropertyListener;
import org.eclipse.ui.ISaveablePart2;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.MultiPageEditorPart;
import de.beas.explicanto.client.I18N;
import de.beas.explicanto.client.model.Document;
import de.beas.explicanto.client.model.Node;
import de.beas.explicanto.client.rcp.dialogs.ExplicantoMessageDialog;

/**
 * 
 * MainEditor
 * 
 * @author alexandru.georgescu
 * @version 1.0
 * 
 */
public class MainEditor extends MultiPageEditorPart implements PropertyChangeListener, ISaveablePart2 {

    private static final Logger log = Logger.getLogger(MainEditor.class);

    public static final String ID = "de.beas.explicanto.client.rcp.editor.MainEditor";

    private CsdeEditor csdeEditor;

    public void init(IEditorSite site, IEditorInput input) throws PartInitException {
        super.init(site, input);
        setPartName(input.getName());
        Document doc = (Document) getEditorInput().getAdapter(Document.class);
        doc.getRoot().addPropertyChangeListener(this);
        this.addPropertyListener(new IPropertyListener() {

            public void propertyChanged(Object source, int propId) {
                if (log.isDebugEnabled()) {
                    log.debug("propertyChanged");
                    log.debug(source);
                    log.debug("" + propId);
                }
            }
        });
    }

    protected void createPages() {
        CTabFolder tabFolder = (CTabFolder) getContainer();
        tabFolder.setSimple(false);
        createCSDEPage();
        createOutlinePage();
    }

    private void createCSDEPage() {
        try {
            csdeEditor = new CsdeEditor();
            int index = addPage(csdeEditor, getEditorInput());
            setPageText(index, I18N.translate("csdeEditor.title"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void createOutlinePage() {
        try {
            addPage(0, (IEditorPart) (csdeEditor.getAdapter(CsdeEditor.TreeEditor.class)), getEditorInput());
            setPageText(0, I18N.translate("outline.title"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Object getAdapter(Class adapter) {
        return csdeEditor.getAdapter(adapter);
    }

    public void doSave(IProgressMonitor monitor) {
        csdeEditor.doSave(monitor);
    }

    public void doSaveAs() {
    }

    public boolean isSaveAsAllowed() {
        return false;
    }

    public boolean isDirty() {
        return csdeEditor.isDirty();
    }

    public IEditorPart getActiveEditor() {
        return super.getActiveEditor();
    }

    public void propertyChange(PropertyChangeEvent arg0) {
        if (arg0.getPropertyName().equals(Node.EDITOR_TITLE)) {
            String str = arg0.getNewValue().toString();
            setPartName(str);
        }
    }

    public int promptToSaveOnClose() {
        int result = ExplicantoMessageDialog.openYesNoCancel(getSite().getShell(), I18N.translate("actions.saveRequest.title"), MessageFormat.format(I18N.translate("actions.saveRequest.msg"), new String[] { getPartName() }));
        if (result == 1) csdeEditor.unlockAll();
        if ((result == 2) || (result == -1)) return ISaveablePart2.CANCEL;
        return result;
    }

    public ImageData getJPEGDiagram() {
        return csdeEditor.getJPEGDiagram();
    }
}
