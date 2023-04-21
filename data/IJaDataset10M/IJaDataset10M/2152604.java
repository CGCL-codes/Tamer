package fr.emn.easymol.main;

import javax.swing.JFrame;
import fr.emn.easymol.ui.components.Molecule3DPane;

/**
 * @author avaughan
 */
public class Molecule3DWindow extends JFrame {

    private Molecule3DPane pane = null;

    public Molecule3DWindow(Molecule3DPane myPane) {
        pane = myPane;
        this.setTitle(pane.getMolecule().getName() + " 3D View");
        this.setSize(300, 300);
        this.getContentPane().add(pane);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
}
