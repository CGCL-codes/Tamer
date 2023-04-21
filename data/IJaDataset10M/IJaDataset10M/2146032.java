package org.dengues.warehouse.viewers.action;

import org.dengues.commons.IDenguesCoreContants;
import org.dengues.commons.IDenguesSharedImage;
import org.dengues.commons.utils.ImageUtil;
import org.dengues.core.actions.AbstractAction;
import org.dengues.core.resource.WarehouseResourceFactory;
import org.dengues.core.warehouse.ENavNodeType;
import org.dengues.core.warehouse.ENodeCategoryName;
import org.dengues.core.warehouse.IWarehouseNode;
import org.dengues.core.warehouse.IWarehouseView;
import org.dengues.warehouse.i18n.Messages;
import org.dengues.warehouse.models.FolderWarehouseNode;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;

/**
 * Qiang.Zhang.Adolf@gmail.com class global comment. Detailled comment <br/>
 * 
 * $Id: Dengues.epf 2008-1-7 qiang.zhang $
 * 
 */
public class DeleteAction extends AbstractAction {

    /**
     * Qiang.Zhang.Adolf@gmail.com DeleteAction constructor comment.
     */
    public DeleteAction() {
        super(Messages.getString("DeleteAction.Label"));
        setImageDescriptor(ImageUtil.getDescriptor(IDenguesSharedImage.ACTION_DBREMOVE));
        setActionDefinitionId("deleteStorge");
    }

    @Override
    public void run() {
        if (pnodes != null) {
            for (IWarehouseNode node : pnodes) {
                if (computeEnable()) {
                    if (node instanceof FolderWarehouseNode) {
                        IPath path = getPath(node);
                        IFolder folder = WarehouseResourceFactory.getFolder(node.getCategoryName(), path);
                        IContainer parent = folder.getParent();
                        folder.getLocation().toFile().delete();
                        try {
                            parent.refreshLocal(IResource.DEPTH_ONE, null);
                        } catch (CoreException ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        node.setBin(true);
                        WarehouseResourceFactory.deleteObjectLogical(node.getObject());
                    }
                    refresh(node);
                } else if (node.isBin()) {
                    DeleteForeverAction foreverAction = new DeleteForeverAction(node);
                    if (foreverAction.computeEnable()) {
                        foreverAction.run();
                    }
                }
            }
            pnodes = null;
        } else {
            IViewPart findView = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(IDenguesCoreContants.WAREHOUSE_VIEW_ID);
            if (findView instanceof IWarehouseView) {
                IWarehouseView view = (IWarehouseView) findView;
                pnodes = ((StructuredSelection) view.getTreeView().getSelection()).toList();
                run();
            }
        }
    }

    @Override
    public int getLevel() {
        return 999;
    }

    @Override
    protected boolean computeEnable() {
        boolean isEnable = true;
        if (pnodes != null) {
            boolean emptyFolder = true;
            for (IWarehouseNode node : pnodes) {
                boolean c = ENavNodeType.SYSTEM_FOLDER != node.getNodeType() && !node.isBin();
                if (node.getCategoryName() == ENodeCategoryName.TABLE || node.getCategoryName() == ENodeCategoryName.COLUMN) {
                    isEnable = false;
                    break;
                } else if (node.getCategoryName() == ENodeCategoryName.BLOCKS) {
                    if (!node.getChildren().isEmpty()) {
                        isEnable = false;
                        break;
                    }
                }
                if (!c) {
                    isEnable = false;
                    break;
                } else {
                    if (ENavNodeType.SIMPLE_FOLDER == node.getNodeType()) {
                        emptyFolder = emptyFolder && node.getChildren().isEmpty();
                        if (!emptyFolder) {
                            isEnable = false;
                            break;
                        }
                    } else if (ENavNodeType.CDC == node.getNodeType() || ENavNodeType.BLOCK_REF == node.getNodeType()) {
                        isEnable = false;
                        break;
                    }
                }
            }
        }
        return isEnable;
    }
}
