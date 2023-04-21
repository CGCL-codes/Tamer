package com.loribel.commons.module.debug.gui;

import java.util.ArrayList;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.tree.DefaultTreeModel;
import com.loribel.commons.abstraction.GB_LabelIcon;
import com.loribel.commons.business.GB_BusinessObjectDefault;
import com.loribel.commons.gui.GB_ViewManagerAbstract;
import com.loribel.commons.module.debug.gui.tree.GB_DebuggableByBONamesTN;
import com.loribel.commons.swing.GB_CheckBox;
import com.loribel.commons.swing.GB_Panel;
import com.loribel.commons.swing.GB_PanelRowsTitle;
import com.loribel.commons.swing.GB_Tree;
import com.loribel.commons.swing.tree.GB_DefaultTreeModel;
import com.loribel.commons.util.CTools;
import com.loribel.commons.util.GB_LabelIconTools;

/**
 * View to presents a tree for Business Object instance.
 * <p>
 * Available Filter :
 * <ul>
 * <li>all
 * <li>with listeners
 * </ul>
 */
public class GB_BOTreeVM extends GB_ViewManagerAbstract {

    java.util.List list;

    JCheckBox checkListener;

    GB_Panel panelTree;

    public GB_BOTreeVM(java.util.List a_list) {
        list = a_list;
    }

    protected java.util.List getFilterList() {
        if (list == null) {
            return null;
        }
        if (!checkListener.isSelected()) {
            return list;
        }
        int len = list.size();
        Object l_item;
        GB_BusinessObjectDefault l_bo;
        ArrayList retour = new ArrayList();
        for (int i = 0; i < len; i++) {
            l_item = list.get(i);
            if (l_item instanceof GB_BusinessObjectDefault) {
                l_bo = (GB_BusinessObjectDefault) l_item;
                if (CTools.getSize(l_bo.getAllListeners()) > 0) {
                    retour.add(l_bo);
                }
            }
        }
        return retour;
    }

    protected JComponent buildView() {
        GB_PanelRowsTitle retour = new GB_PanelRowsTitle();
        retour.addRowFill(buildFilterComponent());
        panelTree = new GB_Panel();
        retour.addRowFill2Scroll(panelTree);
        updateTree();
        return retour;
    }

    protected void updateTree() {
        panelTree.removeAll();
        panelTree.setMainPanel(buildTree());
        panelTree.forceRepaint();
    }

    protected JComponent buildFilterComponent() {
        checkListener = new ListenerCheckBox();
        return checkListener;
    }

    protected JComponent buildTree() {
        GB_DebuggableByBONamesTN l_root = new GB_DebuggableByBONamesTN(getFilterList());
        DefaultTreeModel l_model = new GB_DefaultTreeModel(l_root);
        GB_Tree retour = new GB_Tree(l_model);
        return retour;
    }

    public GB_LabelIcon getLabelIcon() {
        GB_LabelIcon retour = GB_LabelIconTools.newLabelIcon("Tree for Business Object");
        return retour;
    }

    private class ListenerCheckBox extends GB_CheckBox implements ChangeListener {

        public ListenerCheckBox() {
            super("with listeners");
            this.addChangeListener(this);
        }

        public void stateChanged(ChangeEvent a_changeEvent) {
            updateTree();
        }
    }
}
