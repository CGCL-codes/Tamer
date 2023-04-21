package org.xulbooster.eclipse.xb.ui.editors.xul.views.properties;

import java.util.HashSet;
import java.util.Set;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.progress.UIJob;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.IPropertySourceProvider;
import org.eclipse.ui.views.properties.PropertySheetPage;
import org.eclipse.wst.sse.core.internal.provisional.INodeAdapter;
import org.eclipse.wst.sse.core.internal.provisional.INodeNotifier;
import org.eclipse.wst.sse.ui.views.properties.PropertySheetConfiguration;
import org.eclipse.wst.xml.core.internal.contentmodel.CMDocument;
import org.eclipse.wst.xml.core.internal.contentmodel.modelquery.CMDocumentManager;
import org.eclipse.wst.xml.core.internal.contentmodel.modelquery.CMDocumentManagerListener;
import org.eclipse.wst.xml.core.internal.contentmodel.modelquery.ModelQuery;
import org.eclipse.wst.xml.core.internal.contentmodel.util.CMDocumentCache;
import org.eclipse.wst.xml.core.internal.modelquery.ModelQueryUtil;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;
import org.eclipse.wst.xml.ui.internal.XMLUIMessages;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xulbooster.eclipse.xb.ui.editors.xul.internal.properties.XULPropertySource;

/**
 * Configuration for property sheet page which shows XML content.
 * 
 * @see org.eclipse.wst.sse.ui.views.properties.PropertySheetConfiguration
 * @since 1.0
 */
public class XULPropertySheetConfiguration extends PropertySheetConfiguration {

    private class CMDocumentManagerListenerImpl implements CMDocumentManagerListener {

        public void cacheCleared(CMDocumentCache cache) {
        }

        public void cacheUpdated(CMDocumentCache cache, final String uri, int oldStatus, int newStatus, CMDocument cmDocument) {
            if ((newStatus == CMDocumentCache.STATUS_LOADED) || (newStatus == CMDocumentCache.STATUS_ERROR)) {
                refreshPages();
            }
        }

        public void propertyChanged(CMDocumentManager cmDocumentManager, String propertyName) {
            if (cmDocumentManager.getPropertyEnabled(CMDocumentManager.PROPERTY_AUTO_LOAD)) {
                refreshPages();
            }
        }

        private void refreshPages() {
            getPropertiesRefreshJob().addPropertySheetPage(fPropertySheetPage);
            getPropertiesRefreshJob().schedule(PropertiesRefreshJob.UPDATE_DELAY);
        }
    }

    private class PropertiesRefreshJob extends UIJob {

        public static final int UPDATE_DELAY = 200;

        private Set propertySheetPages = null;

        public PropertiesRefreshJob() {
            super(XMLUIMessages.JFaceNodeAdapter_1);
            setSystem(true);
            setPriority(Job.SHORT);
            propertySheetPages = new HashSet(1);
        }

        void addPropertySheetPage(IPropertySheetPage page) {
            if (page != null) {
                propertySheetPages.add(page);
                schedule(UPDATE_DELAY);
            }
        }

        public IStatus runInUIThread(IProgressMonitor monitor) {
            Object[] pages = propertySheetPages.toArray();
            propertySheetPages.clear();
            for (int i = 0; i < pages.length; i++) {
                PropertySheetPage page = (PropertySheetPage) pages[i];
                if ((page != null) && (page.getControl() != null) && !page.getControl().isDisposed()) {
                    page.refresh();
                }
            }
            return Status.OK_STATUS;
        }
    }

    private class XMLPropertySheetRefreshAdapter implements INodeAdapter {

        public boolean isAdapterForType(Object type) {
            return false;
        }

        public void notifyChanged(INodeNotifier notifier, int eventType, Object changedFeature, Object oldValue, Object newValue, int pos) {
            if (fPropertySheetPage != null) {
                getPropertiesRefreshJob().addPropertySheetPage(fPropertySheetPage);
            }
        }
    }

    private class XULPropertySourceProvider implements IPropertySourceProvider {

        private IPropertySource fPropertySource = null;

        private INodeNotifier fSource = null;

        public IPropertySource getPropertySource(Object object) {
            if ((fSource != null) && object.equals(fSource)) {
                return fPropertySource;
            }
            if (object instanceof IDOMNode) {
                fSource = (INodeNotifier) object;
                fPropertySource = (IPropertySource) fSource.getAdapterFor(IPropertySource.class);
                if (fPropertySource == null) {
                    fPropertySource = new XULPropertySource((INodeNotifier) object);
                }
            } else {
                fSource = null;
                fPropertySource = null;
            }
            return fPropertySource;
        }
    }

    private CMDocumentManagerListenerImpl fCMDocumentManagerListener = new CMDocumentManagerListenerImpl();

    private PropertiesRefreshJob fPropertiesRefreshJob = null;

    IPropertySheetPage fPropertySheetPage = null;

    private IPropertySourceProvider fPropertySourceProvider = null;

    private INodeAdapter fRefreshAdapter = new XMLPropertySheetRefreshAdapter();

    private CMDocumentManager[] fSelectedCMDocumentManagers = new CMDocumentManager[0];

    private INodeNotifier[] fSelectedNotifiers = new INodeNotifier[0];

    /**
	 * Create new instance of XMLPropertySheetConfiguration
	 */
    public XULPropertySheetConfiguration() {
        super();
        System.out.println("XULPropertySheetConfiguration");
    }

    public ISelection getInputSelection(IWorkbenchPart selectingPart, ISelection selection) {
        if (fSelectedNotifiers != null) {
            for (int i = 0; i < fSelectedNotifiers.length; i++) {
                fSelectedNotifiers[i].removeAdapter(fRefreshAdapter);
            }
            fSelectedNotifiers = null;
        }
        for (int i = 0; i < fSelectedCMDocumentManagers.length; i++) {
            fSelectedCMDocumentManagers[i].removeListener(fCMDocumentManagerListener);
        }
        ISelection preferredSelection = selection;
        if (selection instanceof IStructuredSelection) {
            IStructuredSelection structuredSel = (IStructuredSelection) selection;
            Object[] selectedObjects = new Object[structuredSel.size()];
            System.arraycopy(structuredSel.toArray(), 0, selectedObjects, 0, selectedObjects.length);
            for (int i = 0; i < selectedObjects.length; i++) {
                Object inode = selectedObjects[i];
                if (inode instanceof Node) {
                    Node node = (Node) inode;
                    Node parentNode = node.getParentNode();
                    if (node.getNodeType() == Node.ATTRIBUTE_NODE) {
                        Element ownerElement = ((Attr) node).getOwnerElement();
                        selectedObjects[i] = ownerElement;
                    } else if (((node.getNodeType() == Node.TEXT_NODE) || (node.getNodeType() == Node.CDATA_SECTION_NODE)) && (parentNode != null)) {
                        selectedObjects[i] = parentNode;
                    }
                }
            }
            if (selectedObjects.length > 0) {
                Set managers = new HashSet(1);
                Set selectedNotifiers = new HashSet(1);
                for (int i = 0; i < selectedObjects.length; i++) {
                    if (selectedObjects[i] instanceof Node) {
                        ModelQuery query = ModelQueryUtil.getModelQuery(((Node) selectedObjects[i]).getOwnerDocument());
                        if (query != null) {
                            CMDocumentManager mgr = query.getCMDocumentManager();
                            if (mgr != null) {
                                managers.add(mgr);
                                mgr.addListener(fCMDocumentManagerListener);
                            }
                        }
                    }
                    if (selectedObjects[i] instanceof INodeNotifier) {
                        selectedNotifiers.add(selectedObjects[i]);
                        ((INodeNotifier) selectedObjects[i]).addAdapter(fRefreshAdapter);
                    }
                }
                fSelectedCMDocumentManagers = (CMDocumentManager[]) managers.toArray(new CMDocumentManager[managers.size()]);
                fSelectedNotifiers = (INodeNotifier[]) selectedNotifiers.toArray(new INodeNotifier[selectedNotifiers.size()]);
            }
            preferredSelection = new StructuredSelection(selectedObjects);
        }
        return preferredSelection;
    }

    PropertiesRefreshJob getPropertiesRefreshJob() {
        if (fPropertiesRefreshJob == null) {
            fPropertiesRefreshJob = new PropertiesRefreshJob();
        }
        return fPropertiesRefreshJob;
    }

    public IPropertySourceProvider getPropertySourceProvider(IPropertySheetPage page) {
        if (fPropertySourceProvider == null) {
            fPropertySheetPage = page;
            fPropertySourceProvider = new XULPropertySourceProvider();
        }
        return fPropertySourceProvider;
    }

    public void unconfigure() {
        super.unconfigure();
        for (int i = 0; i < fSelectedCMDocumentManagers.length; i++) {
            fSelectedCMDocumentManagers[i].removeListener(fCMDocumentManagerListener);
        }
        fPropertySheetPage = null;
    }
}
