package com.emental.mindraider.model.ui;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import javax.swing.JLabel;
import javax.swing.JPanel;
import com.emental.mindraider.kernel.MindRaider;
import com.emental.mindraider.ui.ProgramIconJDialog;

/**
 * RDF Model details frame.
 * 
 * @author Martin.Dvorak
 */
public class RdfModelDetailsJDialog extends ProgramIconJDialog {

    /**
     * serial version uid
     */
    private static final long serialVersionUID = -6287045711732276750L;

    public RdfModelDetailsJDialog() {
        super("RDF Model Details");
        JPanel p = new JPanel();
        p.add(new JLabel("   Triplets: " + MindRaider.spidersGraph.getNumberOfTriplets() + "   "));
        setContentPane(p);
        pack();
        Dimension ddww = getSize();
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(new Point((screen.width - ddww.width) / 2, (screen.height - ddww.height) / 2));
        setVisible(true);
    }
}
