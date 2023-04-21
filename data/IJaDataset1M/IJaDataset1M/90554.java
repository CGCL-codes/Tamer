package org.openscience.cdk.test.renderer;

import java.awt.BorderLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.ChemFile;
import org.openscience.cdk.ChemModel;
import org.openscience.cdk.ChemObject;
import org.openscience.cdk.ChemSequence;
import org.openscience.cdk.graph.rebond.RebondTool;
import org.openscience.cdk.io.CMLReader;
import org.openscience.cdk.io.ChemObjectReader;
import org.openscience.cdk.io.MDLReader;
import org.openscience.cdk.io.XYZReader;
import org.openscience.cdk.renderer.AcceleratedRenderer3D;
import org.openscience.cdk.renderer.AcceleratedRenderer3DModel;
import org.openscience.cdk.tools.manipulator.ChemModelManipulator;

/**
 * @cdk.module test-java3d
 */
public class AcceleratedRenderer3DTest {

    public AcceleratedRenderer3DTest(String inFile) {
        try {
            ChemObjectReader reader;
            System.out.println("Loading: " + inFile);
            if (inFile.endsWith(".xyz")) {
                reader = new XYZReader(new FileReader(inFile));
                System.out.println("Expecting XYZ format...");
            } else if (inFile.endsWith(".cml")) {
                reader = new CMLReader(new FileReader(inFile));
                System.out.println("Expecting CML format...");
            } else {
                reader = new MDLReader(new FileInputStream(inFile));
                System.out.println("Expecting MDL MolFile format...");
            }
            ChemFile chemFile = (ChemFile) reader.read((ChemObject) new ChemFile());
            ChemSequence[] chemSequence = chemFile.getChemSequences();
            ChemModel[] chemModels = chemSequence[0].getChemModels();
            AtomContainer atomContainer = ChemModelManipulator.getAllInOneContainer(chemModels[0]);
            RebondTool rebonder = new RebondTool(2.0, 0.5, 0.5);
            rebonder.rebond(atomContainer);
            JFrame frame = new JFrame("AcceleratedRenderer3DTest");
            frame.getContentPane().setLayout(new BorderLayout());
            AcceleratedRenderer3D renderer = new AcceleratedRenderer3D(new AcceleratedRenderer3DModel(atomContainer));
            frame.getContentPane().add(renderer, BorderLayout.CENTER);
            frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            frame.setSize(500, 500);
            frame.setVisible(true);
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    public static void main(String[] args) {
        if (args.length == 1) {
            String filename = args[0];
            if (new File(filename).canRead()) {
                new AcceleratedRenderer3DTest(filename);
            } else {
                System.out.println("File " + filename + " does not exist!");
            }
        } else {
            System.out.println("Syntax: AcceleratedRenderer3DTest <inputfile>");
        }
    }
}
