package com.loribel.commons.swing.panel;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import com.loribel.commons.GB_DemoInitializer;
import com.loribel.commons.abstraction.GB_LabelIcon;
import com.loribel.commons.abstraction.GB_SwingDemo;
import com.loribel.commons.swing.GB_PanelRowsTitle;
import com.loribel.commons.swing.tools.GB_SwingDemoTools;
import com.loribel.commons.util.GB_LabelIconTools;

/**
 * Demo.
 *
 * @author Gregory Borelli
 */
public class GB_ItemsCheckboxPanelBuilderDemo implements GB_SwingDemo {

    public GB_ItemsCheckboxPanelBuilderDemo() {
    }

    public JComponent getDemoComponent() {
        GB_PanelRowsTitle retour = new GB_PanelRowsTitle();
        retour.setScrollable(true);
        retour.setTitle(getTitle());
        int len = 20;
        List l_list = new ArrayList(len);
        for (int i = 0; i < len; i++) {
            l_list.add("item " + i);
        }
        GB_ItemsCheckboxPanelBuilder l_demo = new GB_ItemsCheckboxPanelBuilder(l_list);
        JComponent l_panel = l_demo.buildComponent();
        l_demo.selectAll(true);
        retour.addRowFill(l_panel);
        retour.addSeparator();
        List l_listS = new ArrayList();
        for (int i = 0; i < 5; i++) {
            l_listS.add(l_list.get(i));
        }
        l_demo = new GB_ItemsCheckboxPanelBuilder(l_list);
        l_demo.setOrderLR(false);
        l_demo.selectItems(l_listS);
        retour.addRowFill(l_demo.buildComponent());
        retour.addRowEnd();
        return retour;
    }

    public Class[] getDemoClasses() {
        return new Class[] { GB_ItemsCheckboxPanelBuilder.class };
    }

    public GB_LabelIcon getTitle() {
        GB_LabelIcon retour = GB_LabelIconTools.newLabelIcon("GB_ItemsSelectPanel");
        return retour;
    }

    public GB_LabelIcon getShortTitle() {
        return getTitle();
    }

    public static void main(String[] p) {
        GB_DemoInitializer.initAll();
        GB_SwingDemo l_demo = new GB_ItemsCheckboxPanelBuilderDemo();
        GB_SwingDemoTools.showIntoFrame(l_demo);
    }
}
