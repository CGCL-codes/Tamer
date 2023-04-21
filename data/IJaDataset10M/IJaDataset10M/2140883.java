package joelib.gui.render;

import wsi.ra.tool.PropertyHolder;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Vector;
import javax.vecmath.Point2d;
import org.apache.log4j.Category;
import joelib.molecule.JOEMol;
import joelib.smarts.JOESmartsPattern;
import joelib.util.JHM;
import joelib.util.types.StringString;

/**
 * Image creation with options.
 *
 * @author     wegnerj
 * @license    GPL
 * @cvsversion    $Revision: 1.5 $, $Date: 2004/08/31 14:23:21 $
 */
public class Mol2Image {

    private static Category logger = Category.getInstance("joelib.gui.render.Mol2Image");

    private static final String SHOW_NUMBERS = "shownumbers";

    private static final String HIDE_NUMBERS = "hidenumbers";

    private static final String SMARTS = "smarts";

    private static final String ARROWS = "arrows";

    private static final String ORTHO_LINES = "ortholines";

    private static final String CONJ_RING = "conjring";

    private static final String LABELS = "labels";

    private static final String WIDTH = "width";

    private static final String HEIGHT = "height";

    private static final String TITLE = "title";

    private static final String SHOW_TITLE = "showtitle";

    private static final String HIDE_TITLE = "hidetitle";

    private static final String ROTATE = "rotate";

    private static final String SHOW_END_CARBON = "showendcarbon";

    private static final String HIDE_END_CARBON = "hideendcarbon";

    private static int DEFAULT_WIDTH = 600;

    private static int DEFAULT_HEIGHT = 400;

    private static Mol2Image instance;

    private int defaultHeight;

    private int defaultWidth;

    private Mol2Image() {
        defaultWidth = PropertyHolder.instance().getInt(this, "defaultWidth", DEFAULT_WIDTH);
        defaultHeight = PropertyHolder.instance().getInt(this, "defaultHeight", DEFAULT_HEIGHT);
    }

    /**
     *  Description of the Method
     *
     * @return   Description of the Return Value
     */
    public static synchronized Mol2Image instance() {
        if (instance == null) {
            instance = new Mol2Image();
        }
        return instance;
    }

    public static StringString[] parseOptions(String options) {
        Vector singleOpts = new Vector();
        JHM.tokenize(singleOpts, options, " \t\n\r");
        Vector validOpts = new Vector(singleOpts.size());
        String opt;
        for (int i = 0; i < singleOpts.size(); i++) {
            opt = ((String) singleOpts.get(i)).toLowerCase();
            if (opt.startsWith(SHOW_NUMBERS)) {
                validOpts.add(new StringString(SHOW_NUMBERS, ""));
            } else if (opt.startsWith(HIDE_NUMBERS)) {
                validOpts.add(new StringString(HIDE_NUMBERS, ""));
            } else if (opt.startsWith(SHOW_TITLE)) {
                validOpts.add(new StringString(SHOW_TITLE, ""));
            } else if (opt.startsWith(HIDE_TITLE)) {
                validOpts.add(new StringString(HIDE_TITLE, ""));
            } else if (opt.startsWith(SHOW_END_CARBON)) {
                validOpts.add(new StringString(SHOW_END_CARBON, ""));
            } else if (opt.startsWith(HIDE_END_CARBON)) {
                validOpts.add(new StringString(HIDE_END_CARBON, ""));
            } else if (opt.startsWith(TITLE)) {
                validOpts.add(new StringString(TITLE, opt.substring(TITLE.length() + 1)));
            } else if (opt.startsWith(SMARTS)) {
                validOpts.add(new StringString(SMARTS, opt.substring(SMARTS.length() + 1)));
            } else if (opt.startsWith(ARROWS)) {
                validOpts.add(new StringString(ARROWS, opt.substring(ARROWS.length() + 1)));
            } else if (opt.startsWith(ORTHO_LINES)) {
                validOpts.add(new StringString(ORTHO_LINES, opt.substring(ORTHO_LINES.length() + 1)));
            } else if (opt.startsWith(CONJ_RING)) {
                validOpts.add(new StringString(CONJ_RING, opt.substring(CONJ_RING.length() + 1)));
            } else if (opt.startsWith(LABELS)) {
                validOpts.add(new StringString(LABELS, opt.substring(LABELS.length() + 1)));
            } else if (opt.startsWith(WIDTH)) {
                validOpts.add(new StringString(WIDTH, opt.substring(WIDTH.length() + 1)));
            } else if (opt.startsWith(HEIGHT)) {
                validOpts.add(new StringString(HEIGHT, opt.substring(HEIGHT.length() + 1)));
            } else if (opt.startsWith(ROTATE)) {
                validOpts.add(new StringString(ROTATE, opt.substring(ROTATE.length() + 1)));
            }
        }
        if (validOpts.size() == 0) {
            return null;
        }
        StringString[] vOptsSS = new StringString[validOpts.size()];
        for (int i = 0; i < validOpts.size(); i++) {
            vOptsSS[i] = (StringString) validOpts.get(i);
        }
        return vOptsSS;
    }

    /**
     * @param defaultHeight
     */
    public void setDefaultHeight(int defaultHeight) {
        this.defaultHeight = defaultHeight;
    }

    /**
     * @return
     */
    public int getDefaultHeight() {
        return defaultHeight;
    }

    /**
     * @param defaultWidth
     */
    public void setDefaultWidth(int defaultWidth) {
        this.defaultWidth = defaultWidth;
    }

    /**
     * @return
     */
    public int getDefaultWidth() {
        return defaultWidth;
    }

    public BufferedImage mol2image(JOEMol mol) {
        return mol2image(mol, null, null, null, null, null, null);
    }

    public BufferedImage mol2image(JOEMol mol, StringString[] options) {
        int width = defaultWidth;
        int height = defaultHeight;
        JOESmartsPattern smarts = null;
        String arrows = null;
        String orthoLines = null;
        String conjRing = null;
        String labels = null;
        int showNumbers = 0;
        int showTitle = 0;
        String title = mol.getTitle();
        String rotate = null;
        int showEndCarbon = 0;
        String opt;
        String val;
        if (options != null) {
            for (int i = 0; i < options.length; i++) {
                opt = options[i].s1.toLowerCase();
                val = options[i].s2.trim();
                if (opt.startsWith(SHOW_NUMBERS)) {
                    showNumbers = 1;
                } else if (opt.startsWith(HIDE_NUMBERS)) {
                    showNumbers = -1;
                } else if (opt.startsWith(SHOW_END_CARBON)) {
                    showEndCarbon = 1;
                } else if (opt.startsWith(HIDE_END_CARBON)) {
                    showEndCarbon = -1;
                } else if (opt.startsWith(SMARTS)) {
                    smarts = new JOESmartsPattern();
                    if (!val.equals(";")) {
                        if (!smarts.init(val)) {
                            logger.error("Invalid SMARTS pattern: " + val);
                            smarts = null;
                        }
                    }
                } else if (opt.startsWith(ARROWS)) {
                    arrows = val;
                } else if (opt.startsWith(ORTHO_LINES)) {
                    orthoLines = val;
                } else if (opt.startsWith(CONJ_RING)) {
                    conjRing = val;
                } else if (opt.startsWith(LABELS)) {
                    labels = val;
                } else if (opt.startsWith(WIDTH)) {
                    width = Integer.parseInt(val);
                } else if (opt.startsWith(HEIGHT)) {
                    height = Integer.parseInt(val);
                } else if (opt.startsWith(SHOW_TITLE)) {
                    showTitle = 1;
                } else if (opt.startsWith(HIDE_TITLE)) {
                    showTitle = -1;
                } else if (opt.startsWith(TITLE)) {
                    title = val;
                } else if (opt.startsWith(ROTATE)) {
                    rotate = val;
                }
            }
        }
        Dimension d = new Dimension(width, height);
        BufferedImage image = new BufferedImage(d.width, d.height, BufferedImage.TYPE_INT_ARGB);
        RenderingAtoms container = new RenderingAtoms();
        container.add(mol);
        if (rotate != null) {
            double deg = Double.parseDouble(rotate);
            if (deg < 0) {
                while (deg < 0) {
                    deg += 360;
                }
            }
            double rad = (deg * Math.PI) / 180;
            Point2d centre = RenderHelper.get2DCentreOfMass(container);
            RenderHelper.rotate(container, centre, rad);
        }
        RenderHelper.translateAllPositive(container);
        RenderHelper.scaleMolecule(container, d, 0.8);
        RenderHelper.center(container, d);
        Graphics graphics = image.getGraphics();
        Renderer2D renderer = new Renderer2D();
        if (smarts != null) {
            renderer.selectSMARTSPatterns(container, smarts);
        }
        if (arrows != null) {
            Arrows myArrows = new Arrows(mol, arrows);
            renderer.getRenderer2DModel().setArrows(myArrows);
        }
        if (orthoLines != null) {
            OrthoLines oLines = new OrthoLines(mol, orthoLines);
            renderer.getRenderer2DModel().setOLines(oLines);
        }
        if (conjRing != null) {
            ConjugatedRings cRings = new ConjugatedRings(mol, conjRing);
            renderer.getRenderer2DModel().setCRings(cRings);
        }
        if (labels != null) {
            container.setRenderAtomLabels(mol, labels, ";", "=");
        }
        if (showNumbers == -1) {
            renderer.getRenderer2DModel().setDrawNumbers(false);
        } else if (showNumbers == 1) {
            renderer.getRenderer2DModel().setDrawNumbers(true);
        }
        if (showEndCarbon == -1) {
            renderer.getRenderer2DModel().setShowEndCarbons(false);
        } else if (showEndCarbon == 1) {
            renderer.getRenderer2DModel().setShowEndCarbons(true);
        }
        graphics.setColor(renderer.getRenderer2DModel().getBackColor());
        graphics.fillRect(0, 0, d.width, d.height);
        renderer.paintMolecule(container, graphics);
        if ((title != null) && (showTitle != -1)) {
            renderer.paintBoundingBox(container, title, 20, graphics, false, true);
        }
        return image;
    }

    public BufferedImage mol2image(JOEMol mol, String title) {
        return mol2image(mol, title, null, null, null, null, null);
    }

    public BufferedImage mol2image(JOEMol mol, JOESmartsPattern smarts) {
        return mol2image(mol, null, smarts, null, null, null, null);
    }

    public BufferedImage mol2image(JOEMol mol, String title, JOESmartsPattern smarts, String eTransfer, String retroSynth, String conjRing, String labels) {
        Dimension d = new Dimension(defaultWidth, defaultHeight);
        BufferedImage image = new BufferedImage(d.width, d.height, BufferedImage.TYPE_INT_ARGB);
        RenderingAtoms container = new RenderingAtoms();
        container.add(mol);
        RenderHelper.translateAllPositive(container);
        RenderHelper.scaleMolecule(container, d, 0.8);
        RenderHelper.center(container, d);
        Graphics graphics = image.getGraphics();
        Renderer2D renderer = new Renderer2D();
        renderer.selectSMARTSPatterns(container, smarts);
        if (eTransfer != null) {
            Arrows arrows = new Arrows(mol, eTransfer);
            renderer.getRenderer2DModel().setArrows(arrows);
        }
        if (retroSynth != null) {
            OrthoLines oLines = new OrthoLines(mol, retroSynth);
            renderer.getRenderer2DModel().setOLines(oLines);
        }
        if (conjRing != null) {
            ConjugatedRings cRings = new ConjugatedRings(mol, conjRing);
            renderer.getRenderer2DModel().setCRings(cRings);
        }
        if (labels != null) {
            container.setRenderAtomLabels(mol, labels, ";", "=");
        }
        graphics.setColor(renderer.getRenderer2DModel().getBackColor());
        graphics.fillRect(0, 0, d.width, d.height);
        renderer.paintMolecule(container, graphics);
        if (title != null) {
            renderer.paintBoundingBox(container, title, 20, graphics, false, true);
        }
        return image;
    }
}
