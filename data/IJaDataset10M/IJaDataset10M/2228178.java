package org.jhotdraw.samples.draw;

import java.awt.Dimension;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

/**
 * A TerraER drawing project.
 * 
 * @author Henrique Santos
 * @version 1.0 2009-04-08
 */
public class TerraProject extends DrawProject {

    private JPanel terrawrapcomponent = null;

    private org.jhotdraw.draw.TerraFigureTree terraobjectinspector = null;

    public void init() {
        super.init();
    }

    public JComponent getComponent() {
        if (terrawrapcomponent == null) {
            terraobjectinspector = org.jhotdraw.draw.TerraFigureTree.getInstance();
            terraobjectinspector.setView(getEditor().getActiveView());
            terraobjectinspector.setPreferredSize(new Dimension(200, 100));
            JScrollPane scr = new JScrollPane(terraobjectinspector);
            scr.setPreferredSize(new Dimension(200, 100));
            terraobjectinspector.setBorder(new javax.swing.border.TitledBorder("Inspetor de Objetos"));
            terrawrapcomponent = new JPanel(new java.awt.BorderLayout());
            JSplitPane spt = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scr, this);
            JPanel LeftFiller = new JPanel();
            LeftFiller.setPreferredSize(new Dimension(5, 10));
            terrawrapcomponent.add(spt, java.awt.BorderLayout.CENTER);
            terrawrapcomponent.add(LeftFiller, java.awt.BorderLayout.WEST);
        }
        return terrawrapcomponent;
    }
}
