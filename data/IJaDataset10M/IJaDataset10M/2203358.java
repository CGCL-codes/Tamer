package com.loribel.commons.swing.panel;

import javax.swing.Icon;
import javax.swing.JComponent;
import com.loribel.commons.abstraction.GB_LabelIcon;
import com.loribel.commons.abstraction.GB_SwingDemo;
import com.loribel.commons.swing.tools.GB_SwingDemoTools;
import com.loribel.commons.util.GB_LabelIconTools;

/**
 * Demo class.
 *
 * @author Gregory Borelli
 */
public class GB_LabelIconPanelDemo implements GB_SwingDemo {

    public Class[] getDemoClasses() {
        return new Class[] { GB_LabelIconPanel.class };
    }

    public JComponent getDemoComponent() {
        String l_desc = "Description of the labelIcon... Description of the labelIcon..." + "Description of the labelIcon... Description of the labelIcon..." + "Description of the labelIcon... Description of the labelIcon..." + "Description of the labelIcon... Description of the labelIcon...";
        GB_LabelIcon l_labelIcon = GB_LabelIconTools.newLabelIcon("Label", (Icon) null, l_desc);
        GB_LabelIconPanel retour = new GB_LabelIconPanel(l_labelIcon);
        return retour;
    }

    public GB_LabelIcon getShortTitle() {
        return getTitle();
    }

    public GB_LabelIcon getTitle() {
        return GB_LabelIconTools.newLabelIcon("GB_LabelIconPanel");
    }

    public static void main(String[] p) {
        GB_SwingDemo l_demo = new GB_LabelIconPanelDemo();
        GB_SwingDemoTools.showIntoFrame(l_demo);
    }
}
