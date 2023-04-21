package com.loribel.commons.gui.bo.metamodel.tree;

import java.awt.Component;
import javax.swing.JMenu;
import com.loribel.commons.abstraction.GB_IdOwner;
import com.loribel.commons.abstraction.GB_LabelIcon;
import com.loribel.commons.abstraction.GB_MenusOwner;
import com.loribel.commons.abstraction.swing.GB_ViewManager;
import com.loribel.commons.abstraction.swing.GB_ViewManagerOwner;
import com.loribel.commons.business.GB_BOLabelIconTools;
import com.loribel.commons.business.GB_BOTools;
import com.loribel.commons.business.impl.bo.GB_BOMetaDataBO;
import com.loribel.commons.business.metamodel.GB_BOBOMetaDataContainer;
import com.loribel.commons.business.metamodel.GB_BOBOMetaDataContainerSet;
import com.loribel.commons.gui.bo.metamodel.options.GB_BOBOMetaDataGuiOptions;
import com.loribel.commons.gui.bo.metamodel.vm.GB_BOBOMetaDataVM;
import com.loribel.commons.gui.bo.tree.GB_BOTNAbstract;
import com.loribel.commons.swing.GB_MenuItemAction;
import com.loribel.commons.swing.GB_MenuItemLongAction;
import com.loribel.commons.swing.abstraction.GB_TreeNode;
import com.loribel.commons.swing.tools.GB_DialogTools;
import com.loribel.commons.swing.tools.GB_MessageDialogTools;
import com.loribel.commons.swing.tools.GB_TreeTools;

/**
 * TN.
 *
 * @author Gregory Borelli
 */
public class GB_BOBOMetaDataTN extends GB_BOTNAbstract implements GB_ViewManagerOwner, GB_IdOwner, GB_MenusOwner {

    /**
     * inner class.
     */
    private class MyMenuClone extends GB_MenuItemAction {

        MyMenuClone() {
            super("Cloner...");
            this.setIcon(AA.ICON.X16_ADD);
        }

        public void doAction() throws Exception {
            GB_BOBOMetaDataContainerSet l_container = getContainerSet();
            if (l_container == null) {
                return;
            }
            String l_id = GB_DialogTools.showEditString(this, "Nouvel identifiant", "");
            if (l_id == null) {
                return;
            }
            GB_BOMetaDataBO l_metaData = new GB_BOMetaDataBO();
            GB_BOTools.copyValues(l_metaData, bo, true);
            l_metaData.setId(l_id);
            l_container.addMetaData(l_metaData);
            GB_TreeNode l_node = (GB_TreeNode) GB_BOBOMetaDataTN.this.getParent();
            GB_TreeTools.rebuildChildren(l_node);
            GB_TreeTools.selectChildById(l_node, l_id, false);
        }
    }

    /**
     * inner class.
     */
    private class MyMenuDelete extends GB_MenuItemAction {

        MyMenuDelete() {
            super(AA.BUTTON_DELETE);
            this.setIcon(AA.ICON.X16_DELETE);
        }

        public void doAction() throws Exception {
            boolean r = GB_MessageDialogTools.showConfirmDelete(this);
            if (!r) {
                return;
            }
            GB_BOBOMetaDataContainerSet l_container = getContainerSet();
            if (l_container == null) {
                return;
            }
            l_container.removeMetaData(bo);
            GB_TreeNode l_node = (GB_TreeNode) GB_BOBOMetaDataTN.this.getParent();
            GB_TreeTools.rebuildChildren(l_node);
            GB_TreeTools.select(l_node, true);
        }
    }

    /**
     * inner class.
     */
    private class MyMenuSave extends GB_MenuItemLongAction {

        MyMenuSave() {
            super(AA.BUTTON_SAVE);
            this.setIcon(AA.ICON.X16_SAVE);
        }

        public Object doAction() throws Exception {
            GB_BOBOMetaDataContainerSet l_container = getContainerSet();
            if (l_container == null) {
                return null;
            }
            l_container.save();
            return Boolean.TRUE;
        }

        public void doGuiAfter(Component a_parent, Object a_value) {
        }

        public boolean doGuiBefore(Component a_parent) {
            return true;
        }
    }

    private GB_BOMetaDataBO bo;

    private GB_BOBOMetaDataGuiOptions options;

    public GB_BOBOMetaDataTN(GB_BOMetaDataBO a_bo, GB_BOBOMetaDataGuiOptions a_options) {
        super(a_bo);
        bo = a_bo;
        options = a_options;
    }

    public boolean buildChildren2(boolean a_flagDeep) throws Exception {
        GB_BOBOMetaDataPropertyTableTN l_nodeProperties = new GB_BOBOMetaDataPropertyTableTN(bo, options);
        add(l_nodeProperties);
        addNodePropertyTable(GB_BOMetaDataBO.BO_PROPERTY.EXTENSION);
        return true;
    }

    private GB_BOBOMetaDataContainerSet getContainerSet() {
        GB_BOBOMetaDataContainer retour = bo.getContainer();
        if (retour instanceof GB_BOBOMetaDataContainerSet) {
            return (GB_BOBOMetaDataContainerSet) retour;
        }
        return null;
    }

    public String getId() {
        return bo.getId();
    }

    public GB_LabelIcon getLabelIcon() {
        return GB_BOLabelIconTools.newLabelIcon(bo);
    }

    public JMenu[] getMenus() {
        JMenu l_menu = new JMenu();
        l_menu.add(new MyMenuDelete());
        l_menu.add(new MyMenuClone());
        l_menu.addSeparator();
        l_menu.add(new MyMenuSave());
        JMenu[] l_menus = new JMenu[1];
        l_menus[0] = l_menu;
        return l_menus;
    }

    public GB_ViewManager getViewManager() {
        GB_BOBOMetaDataVM retour = new GB_BOBOMetaDataVM(bo, true, options);
        return retour;
    }
}
