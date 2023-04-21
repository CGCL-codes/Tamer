package org.openscience.cdk.applications;

import org.openscience.cdk.*;
import org.openscience.cdk.io.*;
import org.openscience.cdk.applications.swing.*;
import org.openscience.cdk.renderer.*;
import org.openscience.cdk.tools.*;
import org.openscience.cdk.geometry.*;
import org.openscience.cdk.smiles.*;
import org.openscience.cdk.layout.*;
import java.util.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.vecmath.*;

/**
 * Command line utility for viewing chemical information from files.
 *
 * @cdkPackage applications
 *
 * @author     steinbeck
 * @author     egonw
 * @created    2002-10-03
 *
 * @keyword    command line util
 */
public class Viewer {

    private org.openscience.cdk.tools.LoggingTool logger;

    private boolean useJava3D;

    private boolean use3D;

    private boolean useJmol;

    private boolean showCOT;

    /**
     *  Constructs a CDK structure viewer.
     */
    public Viewer() {
        logger = new org.openscience.cdk.tools.LoggingTool(this.getClass().getName(), true);
        logger.dumpSystemProperties();
        logger.dumpClasspath();
        useJava3D = false;
        use3D = true;
        showCOT = false;
        useJmol = false;
    }

    /**
     * Sets a flag value for viewing 3D if available.
     *
     * @param  b  value for use3D flag
     */
    public void setUse3D(boolean b) {
        this.use3D = b;
    }

    /**
     * Sets a flag value for using Jmol for viewing 3D.
     *
     * @param  b  value for using Jmol
     */
    public void setUseJmol(boolean b) {
        this.useJmol = b;
    }

    /**
     * Sets a flag value for using Java3D for viewing 3D.
     *
     * @param  b  value for using Java3D
     */
    public void setUseJava3D(boolean b) {
        this.useJava3D = b;
    }

    /**
     * Sets a flag value for using Java3D for viewing 3D.
     *
     * @param  b  value for using Java3D
     */
    public void setShowChemObjectTree(boolean b) {
        this.showCOT = b;
    }

    /**
     * Views the contents of an <code>AtomContainer</code>.
     *
     * @param  m  AtomContainer to view
     */
    private void view(AtomContainer m) {
        if (showCOT) {
            logger.info("Viewing ChemObjectTree...");
            JFrame frame = new JFrame("CDK Molecule Viewer");
            ChemObjectTree cot = new ChemObjectTree();
            frame.getContentPane().add(new JScrollPane(cot), BorderLayout.CENTER);
            cot.paintChemObject(m);
            frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            frame.setSize(500, 500);
            frame.setVisible(true);
            frame.addWindowListener(new AppCloser());
        } else if (GeometryTools.has2DCoordinates(m) || GeometryTools.has3DCoordinates(m)) {
            logger.info("Viewing molecule...");
            JFrame frame = new JFrame("CDK Molecule Viewer");
            frame.getContentPane().setLayout(new BorderLayout());
            if (GeometryTools.has3DCoordinates(m) && use3D) {
                logger.info("Viewing with 3D viewer");
                boolean viewed = false;
                if (useJmol) {
                    logger.debug(".. trying Jmol viewer");
                    try {
                        frame.getContentPane().setLayout(new BorderLayout());
                        org.openscience.jmol.app.Jmol jmol = org.openscience.jmol.app.Jmol.getJmol(frame);
                        frame.getContentPane().add(jmol, BorderLayout.CENTER);
                        logger.debug(".. done");
                        viewed = true;
                    } catch (Exception e) {
                        logger.error("Viewing did not succeed!");
                        logger.error(e.toString());
                        e.printStackTrace();
                    }
                }
                if (useJava3D && !viewed) {
                    logger.debug(".. trying Java3D viewer");
                    try {
                        AcceleratedRenderer3D renderer = new AcceleratedRenderer3D(new AcceleratedRenderer3DModel(m));
                        frame.getContentPane().add(renderer, BorderLayout.CENTER);
                        logger.debug(".. done");
                        viewed = true;
                    } catch (Exception e) {
                        logger.error("Viewing did not succeed!");
                        logger.error(e.toString());
                        e.printStackTrace();
                    }
                }
            } else if (GeometryTools.has2DCoordinates(m)) {
                logger.info("Viewing with 2D viewer");
                MoleculeViewer2D mv = new MoleculeViewer2D(m);
                logger.debug(m.toString());
                frame.getContentPane().add(mv, BorderLayout.CENTER);
            }
            frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            frame.setSize(500, 500);
            frame.setVisible(true);
            frame.addWindowListener(new AppCloser());
            frame.show();
        } else {
            logger.info("Generating 2D coordinates");
            StructureDiagramGenerator sdg = new StructureDiagramGenerator();
            try {
                sdg.setMolecule(new Molecule(m));
                sdg.generateCoordinates(new Vector2d(0, 1));
                view(sdg.getMolecule());
            } catch (Exception exc) {
                System.out.println("Molecule has no coordinates and cannot generate those.");
                System.exit(1);
            }
        }
    }

    /**
     * Views the structures represented by the SMILES string in 2D.
     *
     * @param  SMILES  SMILES string representing the molecule to view
     */
    public void viewSMILES(String SMILES) {
        logger.info("Viewing SMILES...");
        SmilesParser sp = new SmilesParser();
        Molecule mol = null;
        try {
            mol = sp.parseSmiles(SMILES);
        } catch (Exception exc) {
            System.out.println("Problem parsing SMILES: " + exc.toString());
        }
        if (mol != null) {
            view(mol);
        }
    }

    /**
     * Shows the structures in the file.
     *
     * @param  inFile  name of the file to show
     */
    public void viewFile(String inFile) {
        logger.info("Viewing file...");
        ChemFile chemFile = new ChemFile();
        try {
            ChemObjectReader reader = new ReaderFactory().createReader(new FileReader(inFile));
            chemFile = (ChemFile) reader.read((ChemObject) new ChemFile());
        } catch (Exception exc) {
            logger.error("Error while reading file");
            logger.error(exc.toString());
            exc.printStackTrace();
            System.exit(1);
        }
        ChemSequence chemSequence;
        ChemModel chemModel;
        SetOfMolecules setOfMolecules;
        Crystal crystal;
        logger.info("  number of sequences: " + chemFile.getChemSequenceCount());
        for (int sequence = 0; sequence < chemFile.getChemSequenceCount(); sequence++) {
            chemSequence = chemFile.getChemSequence(sequence);
            logger.info("  number of models in sequence " + sequence + ": " + chemSequence.getChemModelCount());
            for (int model = 0; model < chemSequence.getChemModelCount(); model++) {
                chemModel = chemSequence.getChemModel(model);
                setOfMolecules = chemModel.getSetOfMolecules();
                if (setOfMolecules != null) {
                    logger.info("  number of molecules in model " + model + ": " + setOfMolecules.getMoleculeCount());
                    for (int i = 0; i < setOfMolecules.getMoleculeCount(); i++) {
                        logger.info("model: " + i);
                        Molecule m = setOfMolecules.getMolecule(i);
                        view(m);
                    }
                }
                crystal = chemModel.getCrystal();
                if (crystal != null) {
                    logger.info("Found crystal!");
                    view(crystal);
                }
            }
        }
    }

    /**
     * Runs the program from the command line.
     *
     * @param  args  command line arguments.
     */
    public static void main(String[] args) {
        boolean showSmiles = false;
        String filename = "";
        Viewer viewer = new Viewer();
        if (args.length == 1) {
            filename = args[0];
        } else if (args.length > 1) {
            showSmiles = viewer.parseCommandLineOptions(args);
            filename = args[args.length - 1];
        } else {
            System.out.println("Syntax : Viewer [options] <inputfile>");
            System.out.println();
            System.out.println("         Viewer --smiles <SMILES>");
            System.out.println();
            System.out.println("options: --nojava3D    Disable Java3D support");
            System.out.println("         --no3D        View only 2D info");
            System.out.println("         --useJmol     Use Jmol for 3D (if available)");
            System.out.println("         --useTree     Show ChemObject Tree");
            System.exit(0);
        }
        if (showSmiles) {
            viewer.viewSMILES(filename);
        } else {
            if (new File(filename).canRead()) {
                viewer.viewFile(filename);
            } else {
                System.out.println("File " + filename + " does not exist!");
            }
        }
    }

    private boolean parseCommandLineOptions(String[] args) {
        boolean showSmiles = false;
        for (int i = 1; i < args.length; i++) {
            String opt = args[i - 1];
            if ("--nojava3d".equalsIgnoreCase(opt)) {
                this.setUseJava3D(false);
            } else if ("--no3d".equalsIgnoreCase(opt)) {
                this.setUse3D(false);
            } else if ("--useJmol".equalsIgnoreCase(opt)) {
                this.setUseJmol(true);
            } else if ("--smiles".equalsIgnoreCase(opt)) {
                showSmiles = true;
            } else if ("--useTree".equalsIgnoreCase(opt)) {
                this.setShowChemObjectTree(true);
            } else {
                System.err.println("Unknown option: " + opt);
                System.exit(1);
            }
        }
        return showSmiles;
    }

    /**
     * Class used to end the program when the viewing window is closed.
     */
    protected static final class AppCloser extends WindowAdapter {

        public void windowClosing(WindowEvent e) {
            System.exit(0);
        }
    }
}
