package org.wsmostudio.ui.views.navigator;

import java.util.*;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.*;
import org.wsmostudio.ui.GUIHelper;
import org.wsmostudio.ui.Utils;
import org.wsmostudio.ui.editors.WSMOEditorInput;
import org.wsmostudio.ui.editors.model.ObservableModel;

/**
 * A class which listens for certain events in the workbench and dynamically
 * updates the content of the <i>WSMO Navigator</i> view if this is needed.
 * Such events are: activating/closing an editor, modifications in the active 
 * editor.
 *
 * @author not attributable
 * @version $Revision: 1227 $ $Date: 2007-07-19 09:08:32 -0400 (Thu, 19 Jul 2007) $
 */
public class WSMONavigatorUpdater implements IPartListener, IPropertyListener {

    private WSMONavigator navigator;

    public WSMONavigatorUpdater(WSMONavigator navi) {
        this.navigator = navi;
    }

    public void registerInOpenedEditors() {
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        if (page == null) {
            return;
        }
        IEditorReference[] editors = page.getEditorReferences();
        if (editors == null) {
            return;
        }
        for (int i = 0; i < editors.length; i++) {
            IEditorPart editor = editors[i].getEditor(true);
            if (editor != null) {
                editor.addPropertyListener(this);
            }
        }
    }

    public void unregisterFromOpenedEditors() {
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        if (page == null) {
            return;
        }
        IEditorReference[] editors = page.getEditorReferences();
        for (int i = 0; i < editors.length; i++) {
            IEditorPart editor = editors[i].getEditor(true);
            if (editor != null) {
                editor.removePropertyListener(this);
            }
        }
    }

    public void partActivated(IWorkbenchPart part) {
        part.addPropertyListener(this);
        notifyNavigator(part);
    }

    public void partBroughtToTop(IWorkbenchPart part) {
        notifyNavigator(part);
    }

    public void partClosed(IWorkbenchPart part) {
        part.removePropertyListener(this);
        GUIHelper.closeDependentEditors(part);
        clearNavigator(part);
    }

    public void partDeactivated(IWorkbenchPart part) {
    }

    public void partOpened(IWorkbenchPart part) {
        part.addPropertyListener(this);
        notifyNavigator(part);
    }

    public void propertyChanged(Object source, int propId) {
        if (propId == IEditorPart.PROP_DIRTY) {
            navigator.getTree().refresh(true);
        }
    }

    private void notifyNavigator(IWorkbenchPart part) {
        if (false == part instanceof IEditorPart) {
            return;
        }
        ObservableModel tmpModel = (ObservableModel) ((IEditorPart) part).getEditorInput().getAdapter(ObservableModel.class);
        if (tmpModel == null) {
            return;
        }
        while (tmpModel.getMasterModel() != null) {
            tmpModel = tmpModel.getMasterModel();
        }
        IEditorInput newInput = Utils.findInputForModel(tmpModel);
        if (newInput != null && false == newInput.equals(this.navigator.getWsmoInput())) {
            this.navigator.setWsmoInput(newInput);
        }
    }

    private void clearNavigator(IWorkbenchPart part) {
        if (false == part instanceof IEditorPart) {
            return;
        }
        IEditorInput input = ((IEditorPart) part).getEditorInput();
        if (input != null && input.equals(this.navigator.getWsmoInput())) {
            this.navigator.setWsmoInput(null);
        }
    }

    public void saveExpansionInfo(IEditorInput oldInput) {
        if (false == oldInput instanceof WSMOEditorInput) {
            return;
        }
        WeakHashMap<Object, Object> infoMap = new WeakHashMap<Object, Object>();
        TreeItem root = this.navigator.getTree().getTree().getTopItem();
        if (root != null) {
            while (root.getParentItem() != null) {
                root = root.getParentItem();
            }
            collectExpansionInfo(infoMap, root);
        }
        ((WSMOEditorInput) oldInput).setTreeExpansionInfo(infoMap);
        ((WSMOEditorInput) oldInput).setFocus(((IStructuredSelection) this.navigator.getTree().getSelection()).getFirstElement());
    }

    public void applyExpansionInfo(IEditorInput newInput) {
        if (false == newInput instanceof WSMOEditorInput) {
            this.navigator.getTree().expandToLevel(2);
            return;
        }
        WSMOEditorInput wsmoInput = (WSMOEditorInput) newInput;
        WeakHashMap<Object, Object> infoMap = wsmoInput.getTreeExpansionInfo();
        if (infoMap == null || infoMap.size() == 0) {
            this.navigator.getTree().expandToLevel(2);
            return;
        }
        this.navigator.getTree().setExpandedElements(infoMap.keySet().toArray());
        if (wsmoInput.getFocuus() != null) {
            this.navigator.getTree().setSelection(new StructuredSelection(wsmoInput.getFocuus()));
        }
    }

    private void collectExpansionInfo(WeakHashMap<Object, Object> result, TreeItem root) {
        if (root.getExpanded() == false) {
            return;
        }
        TreeItem[] items = root.getItems();
        if (items == null || items.length == 0) {
            return;
        }
        Set<TreeItem> toDo = new HashSet<TreeItem>();
        for (int i = 0; i < items.length; i++) {
            if (items[i].getExpanded() == true) {
                toDo.add(items[i]);
            }
        }
        result.put(root.getData(), null);
        for (TreeItem subItem : toDo) {
            collectExpansionInfo(result, subItem);
        }
    }
}
