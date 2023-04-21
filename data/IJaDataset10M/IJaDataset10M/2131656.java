package com.loribel.commons.gui.bo.tabbed;

import java.util.List;
import javax.swing.JComponent;
import javax.swing.JPanel;
import com.loribel.commons.abstraction.GB_ComponentBuilder;
import com.loribel.commons.abstraction.swing.GB_ViewManager;
import com.loribel.commons.business.GB_BOFactoryTools;
import com.loribel.commons.business.abstraction.GB_SimpleBusinessObject;
import com.loribel.commons.business.abstraction.GB_SimpleBusinessObjectSet;
import com.loribel.commons.gui.GB_VMTools;
import com.loribel.commons.gui.abstraction.GB_VMFactory;
import com.loribel.commons.gui.factory.GB_VMFactoryTools;
import com.loribel.commons.swing.GB_ButtonAction;
import com.loribel.commons.swing.GB_LinkAction;
import com.loribel.commons.swing.GB_PanelCols;
import com.loribel.commons.swing.GB_PanelRows;
import com.loribel.commons.swing.tools.GB_ButtonTools;
import com.loribel.commons.swing.tools.GB_ContainerTools;
import com.loribel.commons.swing.tools.GB_MessageDialogTools;
import com.loribel.commons.swing.tools.GB_SwingTools;
import com.loribel.commons.util.CTools;
import com.loribel.commons.util.GB_IconTools;

/**
 * Build a tabbed pane to represent a property multi of businessObject.
 *
 * @author Gregory Borelli
 */
public class GB_BOPropertyTabbed implements GB_ComponentBuilder {

    /**
     * The Business object witch contains property values to show in the table.
     */
    private GB_SimpleBusinessObjectSet bo;

    /**
     * The BO name of the property.
     */
    private String boNameForProperty;

    private MyComp comp;

    /**
     * The name of the property.
     */
    private String propertyName;

    private GB_ViewManager vm;

    private GB_VMFactory vmFactory;

    private JPanel vmPanel;

    private GB_LinkAction[] tabLabels;

    /**
     * Default constructor.
     */
    public GB_BOPropertyTabbed(GB_SimpleBusinessObjectSet a_bo, String a_propertyName) {
        super();
        bo = a_bo;
        propertyName = a_propertyName;
        boNameForProperty = bo.getProperty(a_propertyName).getBoType();
    }

    public JComponent buildComponent() {
        if (comp == null) {
            comp = new MyComp();
            comp.update();
            selectTabIndex(0);
        }
        return comp;
    }

    private boolean unselectTab() {
        if (vm != null) {
            if (!vm.stop(true)) {
                return false;
            }
        }
        int len = CTools.getSize(tabLabels);
        for (int i = 0; i < len; i++) {
            tabLabels[i].setSelected(false);
        }
        return true;
    }

    private void selectTabIndex(int a_index) {
        if (a_index < 0) {
            return;
        }
        List l_values = (List) bo.getPropertyValue(propertyName);
        int len = CTools.getSize(l_values);
        if (a_index >= len) {
            return;
        }
        if (!unselectTab()) {
            return;
        }
        Object l_value = l_values.get(a_index);
        if (vmFactory == null) {
            vmFactory = GB_VMFactoryTools.getBOVMFactory();
        }
        vm = vmFactory.newViewManager(l_value, false);
        JComponent l_view = GB_VMTools.getViewSafe(vm, true);
        GB_ContainerTools.setMainPanel(vmPanel, l_view);
        tabLabels[a_index].setSelected(true);
        GB_SwingTools.forceRepaint(comp, true);
    }

    public void setVmFactory(GB_VMFactory a_vmFactory) {
        vmFactory = a_vmFactory;
    }

    private class MyButtonAdd extends GB_ButtonAction {

        MyButtonAdd() {
            super();
            this.setIcon(GB_IconTools.get(AA.ICON.X16_ADD));
            GB_ButtonTools.decoreIcon(this);
        }

        public void doAction() {
            GB_SimpleBusinessObject l_newValue = GB_BOFactoryTools.getFactory().newBusinessObject(boNameForProperty);
            bo.addPropertyValue(propertyName, l_newValue);
            comp.update();
            List l_values = (List) bo.getPropertyValue(propertyName);
            selectTabIndex(CTools.getSize(l_values) - 1);
        }
    }

    private class MyButtonDelete extends GB_ButtonAction {

        private Object value;

        private int index;

        MyButtonDelete(int a_index, Object a_value) {
            super();
            index = a_index;
            value = a_value;
            this.setIcon(GB_IconTools.get(AA.ICON.X16_DELETE));
            GB_ButtonTools.decoreIcon(this);
        }

        public void doAction() {
            boolean r = GB_MessageDialogTools.showConfirmDelete(this);
            if (!r) {
                return;
            }
            bo.removePropertyValue(propertyName, value);
            comp.update();
            int l_index = index;
            if (l_index == tabLabels.length) {
                l_index--;
            }
            selectTabIndex(l_index);
        }
    }

    private class MyTabLabel extends GB_LinkAction {

        private int index;

        MyTabLabel(Object a_value, int a_index) {
            super(propertyName);
            if (a_index > 0) {
                setText(propertyName + " [" + (a_index + 1) + "]");
            }
            index = a_index;
        }

        public void doAction() {
            selectTabIndex(index);
        }
    }

    private class MyComp extends GB_PanelRows {

        MyComp() {
        }

        private void update() {
            removeAll();
            List l_values = (List) bo.getPropertyValue(propertyName);
            int len = CTools.getSize(l_values);
            GB_PanelCols l_header = new GB_PanelCols();
            l_header.addSpace(5);
            tabLabels = new GB_LinkAction[len];
            for (int i = 0; i < len; i++) {
                Object l_value = l_values.get(i);
                GB_LinkAction l_tabLabel = new MyTabLabel(l_value, i);
                tabLabels[i] = l_tabLabel;
                l_header.addCol(l_tabLabel);
                l_header.addCol(new MyButtonDelete(i, l_value));
                l_header.addSpace(5);
            }
            l_header.addCol(new MyButtonAdd());
            l_header.addColEnd();
            this.addRowFill(l_header);
            vmPanel = new JPanel();
            this.addRowFill2(vmPanel);
            forceRepaint();
        }
    }
}
