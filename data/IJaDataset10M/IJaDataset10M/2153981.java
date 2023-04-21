package org.dengues.core.actions;

import java.util.ArrayList;
import java.util.List;
import org.dengues.commons.utils.ImageUtil;
import org.dengues.core.ExceptionOperation;
import org.dengues.core.warehouse.IWarehouseNode;
import org.dengues.core.warehouse.IWarehouseView;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.cheatsheets.ICheatSheetAction;
import org.eclipse.ui.cheatsheets.ICheatSheetManager;

/**
 * Qiang.Zhang.Adolf@gmail.com class global comment. Detailled comment <br/>
 * 
 * $Id: Dengues.epf 2007-12-10 qiang.zhang $
 * 
 */
public abstract class AbstractAction extends Action implements ICheatSheetAction {

    protected static final int WIZARD_WIDTH = 600;

    protected static final int WIZARD_HEIGHT = 395;

    public static final String EXTENSION_ID = "org.dengues.core.warehouseContextAction";

    public static final String ATTRI_LABEL = "label";

    public static final String ATTRI_IMAGEKEY = "imageKey";

    public static final String ATTRI_CLASS = "class";

    public static final String ATTRI_DEFN_ID = "definitionId";

    public static final String ATTRI_LEVEL = "level";

    public static final String ATTRI_DOUBLECLICK = "doubleClick";

    public static final String ATTRI_SUBITEM = "subItem";

    public static final String ATTRI_CREATEACTION = "isCreateAction";

    protected List<IWarehouseNode> pnodes;

    protected IWarehouseView view;

    private int level;

    private boolean isDoubleClick;

    private String subItem;

    /**
     * Qiang.Zhang.Adolf@gmail.com AbstractAction constructor comment.
     */
    public AbstractAction() {
        super();
    }

    /**
     * Qiang.Zhang.Adolf@gmail.com Comment method "handleWizard".
     * 
     * @param node
     * @param wizardDialog
     */
    protected void handleWizard(IWarehouseNode node, WizardDialog wizardDialog) {
        wizardDialog.setPageSize(WIZARD_WIDTH, WIZARD_HEIGHT);
        wizardDialog.create();
        wizardDialog.open();
        getWarehouseView().expand(node, true);
        refresh(node);
    }

    /**
     * Qiang.Zhang.Adolf@gmail.com Comment method "getAllActionsFromExtension".
     * 
     * @return
     */
    public static List<AbstractAction> getAllActionsFromExtension() {
        List<AbstractAction> actions = new ArrayList<AbstractAction>();
        IConfigurationElement[] configElements = Platform.getExtensionRegistry().getConfigurationElementsFor(EXTENSION_ID);
        for (IConfigurationElement configElement : configElements) {
            try {
                Object execExtension = configElement.createExecutableExtension(ATTRI_CLASS);
                if (execExtension instanceof AbstractAction) {
                    AbstractAction action = (AbstractAction) execExtension;
                    action.setText(configElement.getAttribute(ATTRI_LABEL));
                    String attribute = configElement.getAttribute(ATTRI_IMAGEKEY);
                    String caction = configElement.getAttribute(ATTRI_CREATEACTION);
                    if ("true".equals(caction)) {
                        action.setImageDescriptor(ImageUtil.getDescriptorAction(attribute));
                    } else {
                        action.setImageDescriptor(ImageUtil.getDescriptor(attribute));
                    }
                    attribute = configElement.getAttribute(ATTRI_DEFN_ID);
                    if (attribute != null && attribute.length() > 0) {
                        action.setActionDefinitionId(attribute);
                    }
                    attribute = configElement.getAttribute(ATTRI_LEVEL);
                    try {
                        if (attribute != null && attribute.length() > 0) {
                            int attrlevel = Integer.parseInt(attribute);
                            if (attrlevel > 0) {
                                action.setLevel(attrlevel);
                            }
                        }
                    } catch (Exception e) {
                        ExceptionOperation.operate(e);
                    }
                    String attribute2 = configElement.getAttribute(ATTRI_DOUBLECLICK);
                    action.isDoubleClick = "true".equals(attribute2);
                    attribute2 = configElement.getAttribute(ATTRI_SUBITEM);
                    action.setSubItem(attribute2);
                    actions.add(action);
                }
            } catch (CoreException e) {
                ExceptionOperation.operate(e);
            }
        }
        return actions;
    }

    /**
     * Qiang.Zhang.Adolf@gmail.com AbstractAction constructor comment.
     * 
     * @param name
     */
    public AbstractAction(String name) {
        super(name);
    }

    /**
     * Qiang.Zhang.Adolf@gmail.com Comment method "getSelection".
     * 
     * @return
     */
    protected final ISelection getSelection() {
        IWorkbenchPartSite site = getActivePage().getActivePart().getSite();
        if (site == null) {
            return null;
        }
        return site.getSelectionProvider().getSelection();
    }

    /**
     * Qiang.Zhang.Adolf@gmail.com Comment method "getActivePage".
     * 
     * @return
     */
    protected final IWorkbenchPage getActivePage() {
        return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
    }

    /**
     * Qiang.Zhang.Adolf@gmail.com Comment method "setEnable".
     */
    public void setEnable() {
        boolean isEnable = false;
        ISelection selection = getSelection();
        if (selection != null) {
            if (selection instanceof IStructuredSelection) {
                IStructuredSelection selection2 = (IStructuredSelection) selection;
                if (!selection2.isEmpty()) {
                    pnodes = new ArrayList<IWarehouseNode>();
                    for (Object e : selection2.toList()) {
                        pnodes.add((IWarehouseNode) e);
                    }
                    isEnable = true;
                }
            }
        }
        if (isEnable) {
            isEnable = computeEnable();
        }
        setEnabled(isEnable);
    }

    /**
     * Qiang.Zhang.Adolf@gmail.com Comment method "computeEnable".
     */
    protected abstract boolean computeEnable();

    /**
     * Qiang.Zhang.Adolf@gmail.com Comment method "refresh".
     * 
     * @param node
     */
    public void refresh(Object node) {
        refresh();
        getWarehouseView().refresh(node);
    }

    /**
     * Qiang.Zhang.Adolf@gmail.com Comment method "refresh".
     * 
     * @param node
     */
    public void refresh() {
        getWarehouseView().refresh();
    }

    /**
     * Qiang.Zhang.Adolf@gmail.com Comment method "getWarehouseView".
     * 
     * @return
     */
    protected IWarehouseView getWarehouseView() {
        if (view == null) {
            IViewPart findView = getActivePage().findView(IWarehouseView.VIEW_ID);
            view = (IWarehouseView) findView;
        }
        return view;
    }

    /**
     * Qiang.Zhang.Adolf@gmail.com Comment method "getPath".
     * 
     * @return
     */
    protected IPath getPath() {
        return pnodes.get(0).getWarehousePath();
    }

    /**
     * Qiang.Zhang.Adolf@gmail.com Comment method "getPath".
     * 
     * @param node
     * @return
     */
    protected IPath getPath(IWarehouseNode node) {
        return node.getWarehousePath();
    }

    /**
     * Qiang.Zhang.Adolf@gmail.com Comment method "getLevel".
     * 
     * @return
     */
    public int getLevel() {
        return this.level;
    }

    /**
     * Sets the level.
     * 
     * @param level the level to set
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * Getter for isDoubleClick.
     * 
     * @return the isDoubleClick
     */
    public boolean isDoubleClick() {
        return this.isDoubleClick;
    }

    /**
     * Sets the isDoubleClick.
     * 
     * @param isDoubleClick the isDoubleClick to set
     */
    public void setDoubleClick(boolean isDoubleClick) {
        this.isDoubleClick = isDoubleClick;
    }

    /**
     * Getter for doubleClass.
     * 
     * @return the doubleClass
     */
    public Class getDoubleClickClass() {
        return null;
    }

    /**
     * Getter for subItem.
     * 
     * @return the subItem
     */
    public String getSubItem() {
        return this.subItem;
    }

    /**
     * Sets the subItem.
     * 
     * @param subItem the subItem to set
     */
    public void setSubItem(String subItem) {
        this.subItem = subItem;
    }

    public void run(String[] params, ICheatSheetManager manager) {
        run();
    }
}
