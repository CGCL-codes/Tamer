package jcd2.view;

import jcd2.view.charts.ChartsPanel;
import jcd2.model.LogEntry;
import jcd2.model.Cyclist;
import java.awt.*;
import java.awt.event.WindowAdapter;
import javax.swing.*;

/**
 *  Display: creates the main display for JCycleData. This creates a component
 *  that can be placed in any Container like Frames or Panels.
 *
 * @author     Manfred Crumbach
 * @created    21. November 2001
 */
public class Display extends JPanel {

    private static final long serialVersionUID = 5728076650541417855L;

    private Cyclist cyclist;

    private Container topPanel = null;

    private ChartsPanel chartsPanel = null;

    private JTabbedPane jtp = new JTabbedPane();

    /**
    *  Creates and initialises a display of the cyclist's information. This
    *  constructor will open a new JFrame containing the display.
    */
    public Display() {
        super();
        final JFrame newWindow = new JFrame("JCycleData");
        int x, y;
        int w, h;
        initialise(newWindow.getContentPane());
        newWindow.setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        newWindow.addWindowListener(new WindowAdapter() {

            /**
          *  perform orderly shutdown when window is closed
          *
          * @param  e  WindowEvent
          */
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                shutdown(newWindow);
            }
        });
        x = 15;
        y = 15;
        w = 800;
        h = 600;
        SwingUtilities.updateComponentTreeUI(newWindow);
        newWindow.pack();
        newWindow.setBounds(x, y, w, h);
        newWindow.setVisible(true);
    }

    /**
    *  Creates and initialises a display of the cyclist's information.
    *
    * @param  topContainer  Container to place the Display in
    * @param  cFile         File that contains the Cyclist's data or null
    */
    public Display(Container topContainer) {
        super();
        initialise(topContainer);
    }

    /**
    *  Display the given Cyclist. This results in a refresh of all display
    *  components like log and stats. If called for the first time (e.g. at
    *  start up) the display components are created.
    *
    * @param  c  The new Cyclist value
    */
    public void setCyclist(Cyclist c) {
        cyclist = c;
        if (chartsPanel == null) {
            chartsPanel = new ChartsPanel(cyclist);
        } else {
            chartsPanel.setCyclist(cyclist);
        }
        this.repaint();
    }

    /**
    *  Gets the selectedLogEntry attribute of the Display object
    *
    * @return    The selectedLogEntry value
    */
    public LogEntry getSelectedLogEntry() {
        LogEntry selected = null;
        return selected;
    }

    /**
    *  in order to perform an orderly shutdown this function checks for unsaved
    *  data and asks wether the data should be saved before leaving.
    *
    * @param  currentWindow  window which requested to be closed
    */
    public void shutdown(JFrame currentWindow) {
        if (cyclist.hasChanged()) {
            int answer = -1;
            switch(answer) {
                case JOptionPane.YES_OPTION:
                    break;
                case JOptionPane.NO_OPTION:
                    break;
                case JOptionPane.CANCEL_OPTION:
                default:
                    break;
            }
        }
    }

    public void selectLogEntry(LogEntry entry) {
    }

    /**
    *  Display consists of a panel with basic header information and a tabbed
    *  panel which contains a table with all activitess, an overview of the
    *  average heart beats and a graphical display of all distances summed up on
    *  a weekly base.
    *
    * @param  topContainer  Container in which Display will be placed
    * @param  cFile         file to initialise Cyclist
    */
    private void initialise(Container topContainer) {
        topPanel = topContainer;
        cyclist = new Cyclist();
        setCyclist(cyclist);
        JPanel helpPanel = new JPanel();
        JPanel toolPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        toolPanel.setLayout(new BorderLayout());
        helpPanel.setLayout(new BorderLayout());
        helpPanel.add(toolPanel, BorderLayout.CENTER);
        topPanel.add(helpPanel, BorderLayout.NORTH);
        JPanel centrePanel = new JPanel();
        showCyclist(centrePanel);
        topPanel.add(centrePanel, BorderLayout.CENTER);
    }

    /**
    *  initialises and provides display for the Cyclist. 
    *  In a header part basic information like cyclist's name, 
    *  average distance, average speed, etc. are displayed. 
    *  This display is provided by the Cyclist! 
    * 
    *  Per LogEntry information is displayed in a tabbed JPanel. 
    *  This per LogEntry information can be a simple list of all 
    *  LogEntries, an overview of the average heart beats or the 
    *  distance on a per week basis.
    * 
    * @param container  Canvas on which the information will be displayed
    */
    private void showCyclist(Container container) {
        JComponent heading = new Header(cyclist);
        if (chartsPanel == null) {
            chartsPanel = new ChartsPanel(cyclist);
        }
        container.setLayout(new BorderLayout());
        container.add(heading, BorderLayout.NORTH);
        container.add(jtp, BorderLayout.CENTER);
    }
}
