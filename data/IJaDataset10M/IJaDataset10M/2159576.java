package de.fu_berlin.inf.dpp.whiteboard.view;

import org.apache.log4j.Logger;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.actions.ZoomComboContributionItem;
import org.eclipse.gef.ui.parts.GraphicalEditorWithPalette;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.ViewPart;
import de.fu_berlin.inf.dpp.whiteboard.gef.editor.WhiteboardEditor;

public class SarosWhiteboardView extends ViewPart {

    protected static Logger log = Logger.getLogger(SarosWhiteboardView.class);

    private final EmbeddedWhiteboardEditor gEditor;

    public SarosWhiteboardView() {
        gEditor = new EmbeddedWhiteboardEditor();
    }

    public WhiteboardEditor getEditor() {
        return gEditor;
    }

    /**
	 * Initializes the WhiteboardEditor embedded in this ViewPart
	 * 
	 */
    @Override
    public void createPartControl(Composite arg0) {
        gEditor.createPartControl(arg0);
        try {
            gEditor.init(getViewSite(), new DummyEditorInput());
        } catch (PartInitException e) {
            log.error("Could not initialize Whiteboard part", e);
            return;
        }
        contributeToToolBar(getViewSite().getActionBars().getToolBarManager());
    }

    @Override
    @SuppressWarnings("rawtypes")
    public Object getAdapter(Class type) {
        if (type == ZoomManager.class) return gEditor.getAdapter(type);
        return super.getAdapter(type);
    }

    protected IAction getAction(String id) {
        return gEditor.getActionRegistry().getAction(id);
    }

    /**
	 * Add all whiteboard actions to the toolbar
	 * 
	 * @param toolBarManager
	 */
    public void contributeToToolBar(IToolBarManager toolBarManager) {
        toolBarManager.add(getAction(ActionFactory.UNDO.getId()));
        toolBarManager.add(getAction(ActionFactory.REDO.getId()));
        toolBarManager.add(new Separator());
        toolBarManager.add(getAction(ActionFactory.SELECT_ALL.getId()));
        toolBarManager.add(getAction(ActionFactory.COPY.getId()));
        toolBarManager.add(getAction(ActionFactory.PASTE.getId()));
        toolBarManager.add(getAction(ActionFactory.DELETE.getId()));
        toolBarManager.add(new Separator());
        toolBarManager.add(getAction(GEFActionConstants.ZOOM_IN));
        toolBarManager.add(getAction(GEFActionConstants.ZOOM_OUT));
        ZoomComboContributionItem zoomItem = new ZoomComboContributionItem(getViewSite().getPage());
        toolBarManager.add(zoomItem);
    }

    @Override
    public void setFocus() {
        gEditor.setFocus();
    }

    protected class DummyEditorInput implements IEditorInput {

        @Override
        public boolean exists() {
            return false;
        }

        @Override
        public ImageDescriptor getImageDescriptor() {
            return null;
        }

        @Override
        public String getName() {
            return "dummy";
        }

        @Override
        public IPersistableElement getPersistable() {
            return null;
        }

        @Override
        public String getToolTipText() {
            return null;
        }

        @SuppressWarnings("rawtypes")
        @Override
        public Object getAdapter(Class adapter) {
            return null;
        }
    }

    /**
	 * This extension of {@link WhiteboardEditor} is used to embed the Viewer of
	 * a GEF {@link GraphicalEditorWithPalette} inside a ViewPart (instead of an
	 * EditorPart). </br>
	 * 
	 * Therefore it hooks the GraphicalViewer of this ViewPart, maintains its
	 * own IWorkbenchPartSite reference and offers a init()-method to initialize
	 * it to a IWorkbenchPartSite instead of an IEditorSite.
	 * 
	 * @author jurke
	 */
    protected class EmbeddedWhiteboardEditor extends WhiteboardEditor {

        private IWorkbenchPartSite site = null;

        @Override
        protected void hookGraphicalViewer() {
            getSelectionSynchronizer().addViewer(getGraphicalViewer());
            SarosWhiteboardView.this.getViewSite().setSelectionProvider(getGraphicalViewer());
        }

        @Override
        public void setSite(IWorkbenchPartSite site) {
            this.site = site;
        }

        @Override
        public IWorkbenchPartSite getSite() {
            return site;
        }

        @Override
        public void selectionChanged(IWorkbenchPart part, ISelection selection) {
            if (SarosWhiteboardView.this.equals(getSite().getPage().getActivePart())) {
                updateActions(getSelectionActions());
            }
        }

        public void init(IWorkbenchPartSite site, IEditorInput input) throws PartInitException {
            setSite(site);
            setInput(input);
            getCommandStack().addCommandStackListener(this);
            site.getWorkbenchWindow().getSelectionService().addSelectionListener(this);
            initializeActionRegistry();
        }

        @Override
        protected ActionRegistry getActionRegistry() {
            return super.getActionRegistry();
        }
    }
}
