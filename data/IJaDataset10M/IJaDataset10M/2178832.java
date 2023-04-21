package com.loribel.commons.swing.panel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import com.loribel.commons.abstraction.GB_LabelIcon;
import com.loribel.commons.abstraction.GB_SwingDemo;
import com.loribel.commons.gui.demo.GB_SwingDemoAbstract;
import com.loribel.commons.swing.GB_PanelRowsTitle;
import com.loribel.commons.swing.tools.GB_SwingDemoTools;
import com.loribel.commons.util.GB_LabelIconTools;

/**
 * Demo class.
 *
 * @author Gregory Borelli
 */
public class GB_LabelIconGuiToolsDemo extends GB_SwingDemoAbstract {

    private GB_LabelIcon labelIcon;

    private GB_LabelIconPanel panelLabelIcon;

    public GB_LabelIconGuiToolsDemo() {
        labelIcon = GB_LabelIconTools.newLabelIcon("Label", (Icon) null, "Description");
    }

    public static void main(String[] p) {
        GB_SwingDemo l_demo = new GB_LabelIconGuiToolsDemo();
        GB_SwingDemoTools.showIntoFrame(l_demo);
    }

    public Class getDemoClass() {
        return GB_LabelIconGuiTools.class;
    }

    public JComponent getDemoComponent() {
        GB_PanelRowsTitle retour = new GB_PanelRowsTitle();
        panelLabelIcon = new GB_LabelIconPanel(labelIcon);
        retour.addRow(panelLabelIcon);
        retour.addRow(new MyButtonEdit());
        return retour;
    }

    /**
     * Inner class.
     */
    private class MyButtonEdit extends JButton implements ActionListener {

        MyButtonEdit() {
            super("Edit LabelIcon...");
            this.addActionListener(this);
        }

        public void actionPerformed(ActionEvent a_event) {
            boolean r = GB_LabelIconGuiTools.showEdit(this, labelIcon, "Edit label icon", JOptionPane.QUESTION_MESSAGE, null);
            if (r) {
                panelLabelIcon.refresh();
            }
        }
    }
}
