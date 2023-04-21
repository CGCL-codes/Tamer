package org.openscience.cdk.applications.demo;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.TextField;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.applications.swing.MoleculeListViewer;
import org.openscience.cdk.applications.swing.MoleculeViewer2D;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.layout.StructureDiagramGenerator;
import org.openscience.cdk.smiles.SmilesParser;

/**
 * @cdk.module  applications
 * @cdk.require swing
 */
public class SmilesViewerforDeterministicGenerator extends JApplet implements ActionListener {

    static final long serialVersionUID = 3968754411807170822L;

    String s = "";

    TextField textField = null;

    String smilesString = "";

    Button drawButton = null;

    Checkbox toggleSymbols = null;

    Checkbox toggleHydrogens = null;

    Checkbox toggleNumbers = null;

    static boolean isApplication = false;

    public static void main(String[] args) {
        isApplication = true;
        JFrame frame = new JFrame();
        SmilesViewerforDeterministicGenerator sv = new SmilesViewerforDeterministicGenerator();
        sv.init();
        frame.addWindowListener(sv.getAWindowAdapter());
        frame.getContentPane().add(sv);
        frame.pack();
        frame.setVisible(true);
    }

    public void init() {
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        textField = new TextField(30);
        textField.addActionListener(this);
        JPanel northPanel = new JPanel();
        JPanel southPanel = new JPanel();
        northPanel.setLayout(new FlowLayout());
        southPanel.setLayout(new FlowLayout());
        drawButton = new Button("Draw");
        drawButton.addActionListener(this);
        northPanel.add(textField);
        northPanel.add(drawButton);
        contentPane.add("North", northPanel);
        toggleSymbols = new Checkbox("Draw Element Symbols");
        toggleHydrogens = new Checkbox("Draw Hydrogens");
        toggleNumbers = new Checkbox("Draw Numbers");
        toggleSymbols.setState(false);
        toggleHydrogens.setState(false);
        toggleNumbers.setState(false);
        southPanel.add(toggleSymbols);
        southPanel.add(toggleHydrogens);
        southPanel.add(toggleNumbers);
        contentPane.add("South", southPanel);
    }

    public WindowAdapter getAWindowAdapter() {
        return new AWindowAdapter();
    }

    public void actionPerformed(java.awt.event.ActionEvent e) {
        if (e.getSource() == drawButton || e.getSource() == textField) {
            smilesString = textField.getText();
            JFrame frame = new JFrame(smilesString);
            frame.getContentPane().setLayout(new BorderLayout());
            try {
                MoleculeListViewer mlv;
                mlv = new MoleculeListViewer();
                mlv.setMolViewDim(new Dimension(400, 600));
                SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());
                IMolecule mol = sp.parseSmiles(smilesString);
                StructureDiagramGenerator sdg = new StructureDiagramGenerator();
                MoleculeViewer2D mv = new MoleculeViewer2D();
                mv.getRenderer2DModel().setDrawNumbers(toggleNumbers.getState());
                mv.getRenderer2DModel().setKekuleStructure(toggleSymbols.getState());
                mv.getRenderer2DModel().setShowImplicitHydrogens(toggleHydrogens.getState());
                sdg.setMolecule((Molecule) mol.clone());
                sdg.generateCoordinates();
                mv.setAtomContainer(sdg.getMolecule());
                mlv.addStructure(mv);
            } catch (Exception exc) {
                System.out.println(exc);
            }
        }
    }

    class AWindowAdapter extends java.awt.event.WindowAdapter {

        public void windowClosing(java.awt.event.WindowEvent we) {
            JFrame frame = (JFrame) we.getWindow();
            frame.setVisible(false);
            frame.dispose();
            if (isApplication) System.exit(0);
        }
    }
}
