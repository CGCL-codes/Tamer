package joelib.gui.render;

import wsi.ra.tool.ResourceLoader;
import java.awt.Dimension;
import java.awt.Graphics;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.EventObject;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.apache.log4j.Category;
import joelib.io.IOType;
import joelib.io.SimpleReader;
import joelib.molecule.JOEMol;
import joelib.smarts.JOESmartsPattern;

/**
 *  A 2D renderer for viewing molecules.
 *
 * @author     steinbeck
 * @author     wegnerj
 * @license    LGPL
 * @cvsversion    $Revision: 1.11 $, $Date: 2004/08/31 14:23:21 $
 */
public class MoleculeViewer2D extends JPanel implements Renderer2DChangeListener {

    private static Category logger = Category.getInstance("joelib.gui.render.MoleculeViewer2D");

    protected Renderer2D renderer;

    protected Renderer2DModel r2dm;

    protected RenderingAtoms atomContainer;

    private JFrame frame = null;

    private String title = "Molecule Viewer";

    /**
     *  Constructs a MoleculeViewer with a molecule to display and a Renderer2DModel containing the information on how to display it.
     *
     * @param  r2dm           The rendere settings determining how the molecule is displayed
     */
    public MoleculeViewer2D(RenderingAtoms atomContainer, Renderer2DModel r2dm) {
        this.atomContainer = atomContainer;
        this.r2dm = r2dm;
        r2dm.addChangeListener(this);
        renderer = new Renderer2D(r2dm);
        frame = new JFrame();
    }

    /**
     *  Constructs a MoleculeViewer with a molecule to display
     */
    public MoleculeViewer2D(RenderingAtoms atomContainer) {
        this(atomContainer, new Renderer2DModel());
    }

    /**
     *  Constructs a MoleculeViewer with a molecule to display
     */
    public MoleculeViewer2D() {
        this(null, new Renderer2DModel());
    }

    /**
     *  Sets the AtomContainer to be displayed
     *
     * @param  atomContainer  The AtomContainer to be displayed
     */
    public void setAtomContainer(RenderingAtoms atomContainer) {
        this.atomContainer = atomContainer;
    }

    /**
     *  Returns the AtomContainer which is being displayed
     *
     * @return    The AtomContainer which is being displayed
     */
    public RenderingAtoms getAtomContainer() {
        return this.atomContainer;
    }

    /**
     *  Sets the Frame attribute of the MoleculeViewer2D object
     *
     * @param  frame  The new Frame value
     */
    public void setFrame(JFrame frame) {
        this.frame = frame;
    }

    /**
     *  Gets the Frame attribute of the MoleculeViewer2D object
     *
     * @return    The Frame value
     */
    public JFrame getFrame() {
        return frame;
    }

    /**
     *  Sets a Renderer2DModel which determins the way a molecule is displayed
     *
     * @param  r2dm  The Renderer2DModel
     */
    public void setRenderer2DModel(Renderer2DModel r2dm) {
        this.r2dm = r2dm;
        r2dm.addChangeListener(this);
        renderer = new Renderer2D(r2dm);
    }

    /**
     *  Gets the Renderer2DModel which determins the way a molecule is displayed
     *
     * @return    The Renderer2DModel value
     */
    public Renderer2DModel getRenderer2DModel() {
        return renderer.getRenderer2DModel();
    }

    /**
     *  Contructs a JFrame into which this JPanel is put and displays the frame with
     *  the molecule.
     */
    public void display() {
        setPreferredSize(new Dimension(Mol2Image.instance().getDefaultWidth(), Mol2Image.instance().getDefaultHeight()));
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.getContentPane().add(this);
        frame.setTitle(title);
        frame.pack();
        frame.setVisible(true);
    }

    public static void display(JOEMol molecule) {
        display(molecule, molecule.getTitle(), null, null, null, null, null);
    }

    public static void display(JOEMol molecule, JOESmartsPattern smarts, String eTransfer, String retroSynth, String conjRing, String labels) {
        display(molecule, molecule.getTitle(), smarts, eTransfer, retroSynth, conjRing, labels);
    }

    public static void display(JOEMol molecule, String _title, JOESmartsPattern smarts, String eTransfer, String retroSynth, String conjRing, String labels) {
        MoleculeViewer2D mv = new MoleculeViewer2D();
        mv.title = _title;
        mv.getFrame().setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        try {
            RenderingAtoms container = new RenderingAtoms();
            container.add(molecule);
            if (smarts != null) {
                logger.info("Select SMARTS pattern: " + smarts.getSMARTS());
                mv.renderer.selectSMARTSPatterns(molecule, container, smarts);
            }
            if (eTransfer != null) {
                Arrows arrows = new Arrows(molecule, eTransfer);
                mv.getRenderer2DModel().setArrows(arrows);
            }
            if (retroSynth != null) {
                OrthoLines oLines = new OrthoLines(molecule, retroSynth);
                mv.getRenderer2DModel().setOLines(oLines);
            }
            if (conjRing != null) {
                ConjugatedRings cRings = new ConjugatedRings(molecule, conjRing);
                mv.getRenderer2DModel().setCRings(cRings);
            }
            if (labels != null) {
                container.setRenderAtomLabels(molecule, labels, ";", "=");
            }
            mv.setAtomContainer(container);
            mv.display();
        } catch (Exception exc) {
            logger.error("Exit due to an unexpected error during coordinate generation");
            exc.printStackTrace();
        }
    }

    /**
     *  The main method.
     *
     * @param  args  An MDL molfile
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            StringBuffer sb = new StringBuffer();
            String programName = MoleculeViewer2D.class.getClass().getName();
            sb.append("Usage is : ");
            sb.append("java -cp . ");
            sb.append(programName);
            sb.append(" <file>");
            sb.append(" [<SMARTS matching patter>]");
            sb.append(" [<electron transfer pattern>]");
            sb.append(" [<orthogonal line pattern>]");
            sb.append(" [<ring pattern>]");
            sb.append(" [<atom label patterns>]");
            sb.append("\n\nThis is version $Revision: 1.11 $ ($Date: 2004/08/31 14:23:21 $)\n\n");
            sb.append("Examples:\n");
            sb.append("java -cp . ");
            sb.append(programName);
            sb.append("sh viewer2D.sh joelib/test/hexamethylbenzen.mol '[D1]' ';' ';' ';' '12=draussen;5=imRing'\n");
            sb.append("java -cp . ");
            sb.append(programName);
            sb.append(" joelib/test/contigous.mol");
            sb.append(" '[*;r]'");
            sb.append(" '11,r,13;1,2,l,2,7;6,7,l,6,8'");
            sb.append(" '1,l,sr1,2;8,l,sr2,11'");
            sb.append(" '40,47,55,59,58,52,c-'");
            sb.append(" '40=abc;50=*;12=*'");
            System.out.println(sb.toString());
            System.exit(1);
        }
        byte[] bytes = ResourceLoader.instance().getBytesFromResourceLocation(args[0]);
        if (bytes == null) {
            logger.error("Molecule can't be loaded at \"" + args[0] + "\".");
            System.exit(1);
        }
        ByteArrayInputStream sReader = new ByteArrayInputStream(bytes);
        SimpleReader reader = null;
        IOType inType = MoleculeViewer2D.checkInputType(args[0]);
        if (inType == null) {
            System.err.println("Input type could not be estimated from file extension.");
        }
        try {
            reader = new SimpleReader(sReader, inType);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        JOEMol mol = new JOEMol(inType, inType);
        for (; ; ) {
            try {
                if (!reader.readNext(mol)) {
                    break;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                System.exit(1);
            }
            break;
        }
        JOESmartsPattern smarts = null;
        if (args.length >= 2) {
            smarts = new JOESmartsPattern();
            if (!args[1].equals(";")) {
                if (!smarts.init(args[1])) {
                    System.err.println("Invalid SMARTS pattern: " + args[1]);
                }
            }
        }
        String eTransfer = null;
        if (args.length >= 3) {
            eTransfer = args[2];
        }
        String retroSynth = null;
        if (args.length >= 4) {
            retroSynth = args[3];
        }
        String cRings = null;
        if (args.length >= 5) {
            cRings = args[4];
        }
        String labels = null;
        if (args.length >= 6) {
            labels = args[5];
        }
        MoleculeViewer2D.display(mol, smarts, eTransfer, retroSynth, cRings, labels);
    }

    /**
     *  Paints the molecule onto the JPanel
     *
     * @param  g  The graphics used to paint with.
     */
    public void paint(Graphics g) {
        super.paint(g);
        if (atomContainer != null) {
            setBackground(r2dm.getBackColor());
            RenderHelper.translateAllPositive(atomContainer);
            RenderHelper.scaleMolecule(atomContainer, getSize(), 0.8);
            RenderHelper.center(atomContainer, getSize());
            renderer.paintBoundingBox(atomContainer, title, 20, g);
            renderer.paintMolecule(atomContainer, g);
        }
    }

    /**
     *  Method to notify this CDKChangeListener if something has changed in another object
     *
     * @param  e  The EventObject containing information on the nature and source of the event
     */
    public void stateChanged(EventObject e) {
        repaint();
    }

    private static IOType checkInputType(String inputFile) {
        IOType inType = null;
        if (inType == null) {
            try {
                inType = SimpleReader.checkGetInputType(inputFile);
            } catch (IOException e) {
                System.out.println(e.getMessage());
                return inType;
            }
        }
        return inType;
    }
}
