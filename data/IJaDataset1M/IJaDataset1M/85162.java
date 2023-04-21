package mvt.tools;

import java.awt.*;
import java.awt.event.*;
import java.awt.print.*;
import java.util.*;
import java.io.File;
import java.io.FileOutputStream;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.filechooser.*;
import mvt.*;
import mvt.graphics.*;
import mvt.util.*;
import math.objects.*;
import math.exceptions.*;

/** GraphicsPanel is an abstract class that 
 * takes much of the similarities of Graphics2DPanel and
 * Graphics3DPanel an puts them in one place. 
 * It holds onto the "only" copies of input and plot
 * panels, holds the listeners for plot and clear
 * buttons, as well as takes * care of saving GIF images 
 * and printing.
 * 
 * @author Darin Gillis
 * @author for the Mathematical Visualization Project for 
 *    the Department of Applied Mathematics, University of Colorado 
 *    at Boulder 
 *
 *   @since JDK1.2   
 */
public abstract class GraphicsPanel extends ToolPanel implements Printable {

    /** The plotPanel is where the plotting is performed  */
    private PlotPanel plotPanel = null;

    /** inputPanel is where the user inputs functions and variables */
    private InputPanel inputPanel = null;

    /** the print function needs this for temporary storage */
    private Image temp = null;

    /** Constructor for this graphics panel, the superclass of any
     * 3D or 2D plotting tool.  If there is one plotpanel, inputpanel
     * and options panel, use this constructor.  
     */
    public GraphicsPanel(String toolTitle, PlotPanel plotPanel, InputPanel inputPanel, OptionsPanel optionsPanel) {
        super(toolTitle, optionsPanel);
        setLayout(new BorderLayout());
        inputPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        add(new JScrollPane(inputPanel), BorderLayout.SOUTH);
        add(plotPanel, BorderLayout.CENTER);
        this.plotPanel = plotPanel;
        this.inputPanel = inputPanel;
    }

    /** Overriding this method will give the that object control
     * over the layout of the menu bar.  If you would like to add
     * an option, override buildMenu(), call super.buildMenu()
     * and then access the menubar with frame.getJMenuBar().
     */
    public void buildMenu(JInternalFrame owner) {
        super.buildMenu(owner);
        JMenuBar mb = owner.getJMenuBar();
        JMenuItem printItem = new JMenuItem("Print");
        printItem.setMnemonic('p');
        printItem.addActionListener(new PrintHandler());
        JMenuItem printPreviewItem = new JMenuItem("Print Preview...");
        printPreviewItem.setMnemonic('v');
        printPreviewItem.addActionListener(new PrintPreviewHandler());
        JMenuItem saveItem = new JMenuItem("Save as GIF...");
        saveItem.setMnemonic('p');
        saveItem.addActionListener(new SaveHandler());
        JMenu fileMenu = mb.getMenu(0);
        fileMenu.insertSeparator(0);
        fileMenu.insert(printItem, 0);
        fileMenu.insert(printPreviewItem, 0);
        if (MVT.isApplication()) {
            fileMenu.insertSeparator(0);
            fileMenu.insert(saveItem, 0);
        }
    }

    /** Get the PlotPanel for this tool.
     */
    public PlotPanel getPlotPanel() {
        GraphicsPanel gp;
        try {
            gp = ((GraphicsPanel) getActiveToolPanel());
        } catch (ClassCastException e) {
            return null;
        }
        return gp.plotPanel;
    }

    /** Get the InputPanel for this tool.
     */
    public InputPanel getInputPanel() {
        GraphicsPanel gp;
        try {
            gp = ((GraphicsPanel) getActiveToolPanel());
        } catch (ClassCastException e) {
            return null;
        }
        return gp.inputPanel;
    }

    public Map collectInput() throws SyntaxException {
        Map inputData = getInputPanel().getInput();
        Map optionsData = getOptionsPanel().getInput();
        inputData.putAll(optionsData);
        return inputData;
    }

    /** Predcondition: verifyInput() has been called before this
     *   function gets called.  This gives the tool the most current
     *   information from the InputPanel & OptionsPanel to work from.
     * 
     *  This function will create a GraphicsComponent from the data 
     *  in the tool's private attributes; the attributes are filled
     *  in by the verifyInput() method.  
     */
    public abstract GraphicsComponent createGraphicsComponent();

    /** This function parses all of the data collected from
     *  the input panel, checks the validity of the input, and
     *  then stores the data in this tool's private attributes.
     *  The createGraphicsObject() method uses the data stored
     *  in the private attributes.
     */
    public abstract Map verifyInput() throws Exception;

    /** This function is called after the GraphicsComponents has
     *  been constructed; in order to set the correct default range. 
     *  If and explicit range has been set, then it is used.  
     *  By default, it takes care of setting the one-to-one ratio
     *  found in many 2D tools. (3D tools have no such option) 
     */
    public void calculatePlotBounds(Map data) {
        Object one = data.get("ONE_TO_ONE");
        if ((one != null) && (((Boolean) one).booleanValue())) ((Plot2DPanel) getPlotPanel()).oneToOne();
    }

    /** This function retrieves the variables from the variable
     *  name boxes in either the InputPanel or OptionsPanel.
     *  This will return null if there is a bad variable in one
     *  of the boxes.  On success, it will return a list of the 
     *  variables used by the tool. 
     */
    public VariableSet getStatedVariables() {
        VariableSet vars = getInputPanel().getStatedVariables();
        if (vars == null) vars = getOptionsPanel().getStatedVariables();
        return vars;
    }

    /** This function updates all the labels in each panel, 
     * reflecting a change in the variable name.
     */
    public void setVariables(VariableSet vars) {
        getInputPanel().setVariables(vars);
        getOptionsPanel().setVariables(vars);
    }

    /**
     * Adds the tool's generated object to the PlotPanel, as long
     * as there were no difficulties in creating the GraphicsComponent.  
     * Primarily this code originates from a Plot Button Click.
     */
    public void updatePlot() {
        Map data;
        try {
            data = verifyInput();
            Object olay = data.get("OVERLAY");
            if ((olay != null) && (!(((Boolean) olay).booleanValue()))) getPlotPanel().clearGraphicsList();
        } catch (Exception e) {
            errorMessage(e.getMessage());
            e.printStackTrace();
            return;
        }
        GraphicsComponent obj = createGraphicsComponent();
        if (obj != null) getPlotPanel().addGraphicsObject(obj);
        calculatePlotBounds(data);
        getPlotPanel().repaint();
    }

    /** Called when the user requests a print
     */
    public void printPlot() {
        try {
            PrinterJob pj = PrinterJob.getPrinterJob();
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            PageFormat pageFormat = pj.defaultPage();
            pageFormat.setOrientation(PageFormat.LANDSCAPE);
            pj.setPrintable(this, pageFormat);
            pj.print();
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        } catch (PrinterException e) {
            errorMessage("Printing Error: " + e.toString());
        }
    }

    /** Called by Java internally to print the graphics context
     */
    public int print(Graphics pg, PageFormat pageFormat, int pageIndex) throws PrinterException {
        if (pageIndex >= 1) return NO_SUCH_PAGE;
        Image temp = snapshot();
        pg.drawImage(temp, (int) pageFormat.getImageableX(), (int) pageFormat.getImageableY(), (int) pageFormat.getImageableWidth(), (int) pageFormat.getImageableHeight(), 0, 0, getPlotPanel().getWidth(), getPlotPanel().getHeight(), this);
        System.gc();
        return PAGE_EXISTS;
    }

    /** Get an image "snapshot" of the graphics currently on the panel.
     */
    private Image snapshot() {
        if (temp == null) temp = getPlotPanel().createImage(getPlotPanel().getWidth(), getPlotPanel().getHeight());
        getPlotPanel().paint(temp.getGraphics());
        return temp;
    }

    /**  Performs the following:
   *      <LI>  creates a gif image
   *      <LI>  attempts to save the gif to a file
   *  @param img        the image to be enocded into GIF format
   *                    and transmitted
   *  @param filename   the destination filename
   */
    public void savePicture() {
        try {
            String filename = "/home/student/gillisd/mvt/saved.gif";
            JFileChooser dialog = new JFileChooser();
            dialog.addChoosableFileFilter(new ImageFilter());
            int returnVal = dialog.showDialog(this, "Save GIF");
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = dialog.getSelectedFile();
                FileOutputStream out = new java.io.FileOutputStream(file);
                GifEncoder gif = new GifEncoder(snapshot(), out);
                gif.encode();
                out.flush();
                out.close();
            }
        } catch (java.io.IOException e) {
            errorMessage("I/O Exception in GIF Save: " + e);
        } catch (Exception ex) {
            errorMessage("GIF Save failed: " + ex);
        }
    }

    /** This class that gets the *.gif option to pop up in the 
    * JFileChooser Dialog.
    */
    public class ImageFilter extends FileFilter {

        public final String gif = "gif";

        /** Get the extension of a file.
     */
        public String getExtension(File f) {
            String ext = null;
            String s = f.getName();
            int i = s.lastIndexOf('.');
            if (i > 0 && i < s.length() - 1) ext = s.substring(i + 1).toLowerCase();
            return ext;
        }

        /** Accept all directories and all gif files.
     */
        public boolean accept(File f) {
            if (f.isDirectory()) return true;
            String extension = getExtension(f);
            if (extension != null) {
                if (extension.equals(gif)) {
                    return true;
                } else {
                    return false;
                }
            }
            return false;
        }

        public String getDescription() {
            return "*.gif";
        }
    }

    public class SaveHandler implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            savePicture();
        }
    }

    public class PrintHandler implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            Thread runner = new Thread() {

                public void run() {
                    try {
                        GraphicsPanel gp = (GraphicsPanel) getActiveToolPanel();
                        if (gp != null) gp.printPlot();
                    } catch (ClassCastException e) {
                    }
                }
            };
            runner.start();
        }
    }

    public class PrintPreviewHandler implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            Thread runner = new Thread() {

                public void run() {
                    Cursor oldc = getCursor();
                    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    try {
                        GraphicsPanel gp = (GraphicsPanel) getActiveToolPanel();
                        if (gp != null) new PrintPreview(gp, PageFormat.LANDSCAPE, "Print Preview");
                    } catch (ClassCastException e) {
                    }
                    setCursor(oldc);
                }
            };
            runner.start();
        }
    }
}
