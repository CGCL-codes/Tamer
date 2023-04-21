package org.nexopenframework.ide.eclipse.editors;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.part.MultiPageEditorSite;
import org.eclipse.wst.sse.ui.StructuredTextEditor;
import org.eclipse.wst.xml.core.internal.provisional.contenttype.ContentTypeIdForXML;
import org.eclipse.wst.xml.ui.internal.tabletree.XMLMultiPageEditorPart;
import org.nexopenframework.ide.eclipse.ServiceComponentUIPlugin;
import org.nexopenframework.ide.eclipse.model.IApplication;
import org.nexopenframework.ide.eclipse.model.IModelListener;
import org.nexopenframework.ide.eclipse.util.IProjectAware;

/**
 * <p>NexOpen Framework</p>
 *  
 * <p></p>
 * 
 * @see org.eclipse.wst.xml.ui.internal.tabletree.XMLMultiPageEditorPart
 * @author Francesc Xavier Magdaleno
 * @version 1.0
 * @since 1.0
 */
public class WSTFormPage extends XMLMultiPageEditorPart implements IModelListener {

    /**Handle of our project*/
    private IProjectAware aware;

    private String id;

    private FormEditor formEditor;

    /**
	 * @param formEditor
	 * @param id
	 */
    public WSTFormPage(final FormEditor formEditor, final String id, final IProjectAware aware) {
        this.formEditor = formEditor;
        this.id = id;
        this.aware = aware;
    }

    public void doSave(IProgressMonitor progressMonitor) {
        super.doSave(progressMonitor);
        IApplication application = ApplicationRegistry.getApplication(aware.getProject());
        application.clearModules();
        application.fireModelChanged();
    }

    public String getId() {
        return id;
    }

    /**
	 * 
	 * @see org.nexopenframework.ide.eclipse.model.IModelListener#modelChanged(java.lang.Object[], java.lang.String, java.lang.String)
	 */
    public void modelChanged(Object[] objects, String type, String property) {
        try {
            final Field fieldFTextEditor = this.getClass().getSuperclass().getDeclaredField("fTextEditor");
            fieldFTextEditor.setAccessible(true);
            final StructuredTextEditor fTextEditor = (StructuredTextEditor) fieldFTextEditor.get(this);
            Method m = fTextEditor.getClass().getDeclaredMethod("doSetInput", new Class[] { IEditorInput.class });
            m.setAccessible(true);
            m.invoke(fTextEditor, new Object[] { this.formEditor.getEditorInput() });
        } catch (final NoSuchFieldException e) {
            ILog log = ServiceComponentUIPlugin.getDefault().getLog();
            Status status = new Status(Status.ERROR, ServiceComponentUIPlugin.PLUGIN_ID, 0, "Unexpected exception [NoSuchFieldException] in WSTFormPage", e);
            log.log(status);
            throw new RuntimeException(e);
        } catch (final NoSuchMethodException e) {
            ILog log = ServiceComponentUIPlugin.getDefault().getLog();
            Status status = new Status(Status.ERROR, ServiceComponentUIPlugin.PLUGIN_ID, 0, "Unexpected exception [NoSuchMethodException] in WSTFormPage", e);
            log.log(status);
            throw new RuntimeException(e);
        } catch (SecurityException e) {
            ILog log = ServiceComponentUIPlugin.getDefault().getLog();
            Status status = new Status(Status.ERROR, ServiceComponentUIPlugin.PLUGIN_ID, 0, "Security Exception in WSTFormPage", e);
            log.log(status);
            throw e;
        } catch (Throwable e) {
            throw (e instanceof RuntimeException) ? (RuntimeException) e : new RuntimeException(e);
        }
    }

    protected IEditorSite createSite(final IEditorPart editor) {
        final IEditorSite site = new MultiPageEditorSite(this, editor) {

            public String getId() {
                return ContentTypeIdForXML.ContentTypeID_XML + ".source";
            }
        };
        return site;
    }
}
