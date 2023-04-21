package org.openscience.cdk.applications.swing;

import java.awt.Dimension;
import java.awt.Graphics;
import java.util.EventObject;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.event.CDKChangeListener;
import org.openscience.cdk.geometry.GeometryTools;
import org.openscience.cdk.renderer.Renderer3D;
import org.openscience.cdk.renderer.Renderer3DModel;

/**
 * A Swing-based implementation of Renderer3D for viewing molecules.
 *
 * @cdk.module java3d
 * @cdk.require swing
 */
public class MoleculeViewer3D extends JPanel implements CDKChangeListener {

    private org.openscience.cdk.tools.LoggingTool logger;

    public AtomContainer atomContainer;

    public Renderer3DModel r3dm;

    public Renderer3D renderer;

    public String title = "Molecule Viewer";

    /**
	 * Constructs a MoleculeViewer with a molecule to display
	 * and a Renderer3DModel containing the information on how to display it.
	 *
	 * @param atomContainer The molecule to be displayed
	 * @param r3dm          The renderer settings determining how the molecule is displayed
	 */
    public MoleculeViewer3D(AtomContainer atomContainer, Renderer3DModel r3dm) {
        logger = new org.openscience.cdk.tools.LoggingTool(this);
        this.atomContainer = atomContainer;
        this.r3dm = r3dm;
        r3dm.addCDKChangeListener(this);
        renderer = new Renderer3D(r3dm);
    }

    /**
	 * Constructs a MoleculeViewer with a molecule to display
	 *
	 * @param   atomContainer  The molecule to be displayed
	 */
    public MoleculeViewer3D(AtomContainer atomContainer) {
        logger = new org.openscience.cdk.tools.LoggingTool(this);
        setAtomContainer(atomContainer);
        r3dm = new Renderer3DModel();
        r3dm.addCDKChangeListener(this);
        renderer = new Renderer3D(r3dm);
    }

    /**
	 * Constructs a MoleculeViewer with a molecule to display
	 *
	 */
    public MoleculeViewer3D() {
        logger = new org.openscience.cdk.tools.LoggingTool(this);
        r3dm = new Renderer3DModel();
        r3dm.addCDKChangeListener(this);
        renderer = new Renderer3D(r3dm);
    }

    /**
	 * Contructs a JFrame into which this JPanel is
	 * put and displays the frame with the molecule
	 */
    public void display() {
        setPreferredSize(new Dimension(600, 400));
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.getContentPane().add(this);
        frame.setTitle(title);
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Paints the molecule onto the JPanel
     *
     * @param   g  The graphics used to paint with.
     */
    public void paint(Graphics g) {
        super.paint(g);
        logger.debug("Called super.paint()");
        if (atomContainer != null) {
            logger.debug("Painting structure...");
            setBackground(r3dm.getBackColor());
            GeometryTools.translateAllPositive(atomContainer);
            GeometryTools.scaleMolecule(atomContainer, getSize(), 0.8);
            GeometryTools.center(atomContainer, getSize());
            renderer.paintMolecule(atomContainer, g);
        } else {
            logger.warn("Nothing to draw: atomContainer == null!");
        }
    }

    /**
	 * Sets a Renderer3DModel which determins the way a molecule is displayed
	 *
	 * @param   r3dm  The Renderer3DModel
	 */
    public void setRenderer3DModel(Renderer3DModel r3dm) {
        this.r3dm = r3dm;
        r3dm.addCDKChangeListener(this);
        renderer = new Renderer3D(r3dm);
    }

    /**
	 * Method to notify this CDKChangeListener if something
	 * has changed in another object
	 *
	 * @param   e  The EventObject containing information on the nature and source of the event
	 */
    public void stateChanged(EventObject e) {
        repaint();
    }

    /**
	 * Returns the AtomContainer which is being displayed
	 *
	 * @return The AtomContainer which is being displayed
	 */
    public AtomContainer getAtomContainer() {
        return this.atomContainer;
    }

    /**
	 * Sets the AtomContainer to be displayed
	 *
	 * @param   atomContainer The AtomContainer to be displayed
	 */
    public void setAtomContainer(AtomContainer atomContainer) {
        this.atomContainer = atomContainer;
    }
}
