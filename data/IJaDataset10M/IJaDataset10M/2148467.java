package net.sf.greengary.util;

import java.awt.Component;
import java.awt.FileDialog;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.image.renderable.ParameterBlock;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

/** This abstract application provides some features, that can be
 * used in different applications.
*	Supported features are: 
*	<ul>
*	<li>System.out redirection to a JTextArea.
*	<li>Saving and restoring the content of (public) 
*	members of the panels</li>
*	<li>Running scripts read from a config-file</li>
*	<li>Displaying the progress of a long operation by a panel in a ProgressBar.</li>
*	</ul>
*	@author <a href="mailto:rlop@gmx.de">Robert Lopuszanski FB07/IF</a> 
*	@since 1.06-2003-08-31
*	@version 1.07-2003-09-05
*/
public abstract class AbstractApplication extends JFrame implements ActionListener {

    /** Contains all Panels with several tools. */
    protected final JTabbedPane m_TabbedPane = new JTabbedPane();

    /** Default-constraints for GUI. */
    private final GridBagConstraints m_Constraints;

    private static final DateFormat DF_YYYYMMDD_HHMMSS = new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss");

    /** TextArea contains all output written to System.out. 
	*	(System.out is redirected). 
	*	@see #m_Out */
    private final JTextArea m_LoggingTextArea = new JTextArea("Program started " + DF_YYYYMMDD_HHMMSS.format(new Date()) + ".\n");

    /** Logging-textarea for System.out redirection. 
	*	@see net.sf.greengary.util.JTextAreaPrintStream */
    private JTextAreaPrintStream m_Out;

    /** Progressbar for long jobs. 
	*	If a panel supports interface Progress, 
	*	status of progress is displayed.
	*	@see net.sf.greengary.util.Progress */
    private final JProgressBar m_Progress = new JProgressBar(0, 1);

    /** The Thread, currently doing work in Background. 
	*	If null, no thread is active. 
	*	Threads are created from panels. */
    private Thread m_WorkingThread = null;

    /** All properties for this application 
	*	(including those for the panels). */
    protected Properties m_Properties = new Properties();

    /** ctor sets up all GUI-elements. 
	*	@param sPropertiesfile File containing the properties for 
	*	the application. */
    public AbstractApplication(String sPropertiesfile) {
        setTitle(getFrameTitle());
        final Toolkit tk = Toolkit.getDefaultToolkit();
        this.setIconImage(ToolIcon.getIcon());
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                close();
            }
        });
        setSize(getInitialWidth(), getInitialHeight());
        setLocation((int) ((tk.getScreenSize().getWidth() - getInitialWidth()) / 2), (int) ((tk.getScreenSize().getHeight() - getInitialHeight()) / 2));
        getContentPane().setLayout(new GridBagLayout());
        m_Constraints = new GridBagConstraints();
        m_Constraints.insets = new Insets(3, 3, 3, 3);
        m_Constraints.fill = GridBagConstraints.BOTH;
        m_Constraints.anchor = GridBagConstraints.NORTH;
        m_Constraints.gridwidth = GridBagConstraints.REMAINDER;
        m_Constraints.weighty = 1.0;
        m_Constraints.weightx = 1.0;
        loadPropertiesWithoutSet(sPropertiesfile);
        addPanels();
        addComponents();
        loadProperties(sPropertiesfile);
        startGUI();
    }

    /** Returns the version of the application as a string. 
	*	@return the version of the application as a string. */
    public abstract String getVersion();

    /** The title of the application-frame.
	*	@return The title of the application-frame. */
    public abstract String getFrameTitle();

    /** Returns initial width of window. 
	*	@return initial width of window.*/
    public abstract int getInitialWidth();

    /** Returns initial height of window.
	*	@return initial height of window.*/
    public abstract int getInitialHeight();

    /** Called to add panels to tabbed pane. */
    protected abstract void addPanels();

    /** Handle a standard event. Called by actionPerformed(ActionEvent ae).
	*	@param sActionCommand The Action command of the event. 
	*	@see #actionPerformed(ActionEvent ae) */
    protected abstract void actionPerformed(String sActionCommand);

    /** Close application. */
    protected abstract void close();

    /** Whether commands loaded in config-file.
	*	This method can use properties from the 
	*	file or whatever, to decide.
	*	should be started.
	*	@return true, if commands should be started, else false. */
    protected abstract boolean autorunCommands();

    /**	Handle actionevents for logging, program exit etc..
	*	This functions calls actionPerformed(String) implemented
	*	in the classes derived from this class.
	*	@see #actionPerformed(String)
	*	@param ae ActionEvent-object generated by calling object. */
    public void actionPerformed(ActionEvent ae) {
        if (m_WorkingThread == null) {
            actionPerformed(ae.getActionCommand());
        } else {
            System.out.println("Thread running");
            System.err.println("Thread running");
        }
    }

    /** Clear the content of the logging textfield. */
    public void clearLog() {
        this.m_LoggingTextArea.setText("");
    }

    /** Display open-file-dialog and save content of log into a file.
	*	@return true, if a file with log has been written. 
	*	false if action cancelled by user
	*	or exception occured. */
    public boolean savelog() {
        try {
            FileDialog fd = new FileDialog(this, "Select file to save log", FileDialog.SAVE);
            fd.setVisible(true);
            String sFilename = fd.getDirectory() + File.separator + fd.getFile();
            if (fd.getFile() == null || fd.getDirectory() == null) {
                return false;
            } else {
                FileWriter fw = new FileWriter(sFilename, false);
                fw.write("Date: " + DF_YYYYMMDD_HHMMSS.format(new Date()) + "\n\r");
                fw.write(this.m_LoggingTextArea.getText());
                fw.write("\n\r");
                fw.flush();
                fw.close();
                return true;
            }
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
            ex.printStackTrace(System.err);
            new ExceptionDialog(this, "Error saving logfile", ex);
            return false;
        }
    }

    /** Run a job with the
	*	given AbstractPanel-object. AbstractPanel implements Runnable.
	*	@param run Start a thread from this object or simpy call run().
	*	(asynchronous)
	*	@param startAsThread true = start the action as a thread,
	*	false = run action within this thread (synchronous).
	*	@return true, if job could be executed. */
    public boolean startJob(AbstractPanel run, boolean startAsThread) {
        if (m_WorkingThread == null) {
            if ((this.m_TabbedPane.getSelectedComponent()) instanceof Progress) {
                this.m_Progress.setMaximum(((Progress) (this.m_TabbedPane.getSelectedComponent())).getProgressMax());
                this.m_Progress.setMinimum(((Progress) (this.m_TabbedPane.getSelectedComponent())).getProgressMin());
            }
            if (startAsThread) {
                m_WorkingThread = new Thread(run);
                m_WorkingThread.start();
            } else {
                run.run();
            }
            return true;
        } else {
            System.out.println("Tried to start a new thread, but another thread is already running!");
            System.err.println("Tried to start a new thread, but another thread is already running!");
            return false;
        }
    }

    /** Wrapper to make paint( this.getGraphics() ) accessable from inner class.
	*	Also enables GUI-elements after a thread has finished. */
    public void paintGUI() {
        if (this.m_WorkingThread != null && !this.m_WorkingThread.isAlive()) {
            System.out.println("Thread finished.");
            this.m_TabbedPane.getSelectedComponent().setEnabled(true);
            m_WorkingThread = null;
            final Component[] comps = this.getContentPane().getComponents();
            for (int i = 0; i < comps.length; ++i) {
                comps[i].setEnabled(true);
            }
            this.m_Progress.setValue(0);
        } else if (this.m_WorkingThread != null) {
            if ((this.m_TabbedPane.getSelectedComponent()) instanceof Progress) {
                this.m_Progress.setValue(((Progress) (this.m_TabbedPane.getSelectedComponent())).getProgressValue());
            }
            this.validate();
            this.doLayout();
            this.paint(this.getGraphics());
        }
        Component[] comps = this.m_TabbedPane.getComponents();
        for (int i = 0; i < comps.length; ++i) {
            AbstractPanel pb = (AbstractPanel) (comps[i]);
            if (pb.isInitialized()) {
                pb.getTabIcon().setOn();
            } else {
                pb.getTabIcon().setOff();
            }
        }
    }

    /** Adds all components to frame. */
    private void addComponents() {
        m_Constraints.fill = GridBagConstraints.BOTH;
        m_Constraints.gridwidth = GridBagConstraints.REMAINDER;
        m_Constraints.weightx = 1.0;
        m_Constraints.weighty = 1.0;
        m_Constraints.weighty = 1.0;
        m_LoggingTextArea.setAutoscrolls(false);
        m_LoggingTextArea.setEditable(false);
        JScrollPane sp = new JScrollPane(m_LoggingTextArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        this.getContentPane().add(sp, this.m_Constraints);
        this.getContentPane().add(new JSplitPane(JSplitPane.VERTICAL_SPLIT, this.m_TabbedPane, sp), this.m_Constraints);
        m_Constraints.weighty = 0.0;
        m_Constraints.gridwidth = 1;
        JButton b = new JButton("Clear log");
        b.setActionCommand("clearlog");
        b.addActionListener(this);
        this.getContentPane().add(b, this.m_Constraints);
        m_Constraints.weighty = 0.0;
        m_Constraints.gridwidth = GridBagConstraints.REMAINDER;
        b = new JButton("Save log");
        b.setActionCommand("savelog");
        b.addActionListener(this);
        this.getContentPane().add(b, this.m_Constraints);
        m_Constraints.weighty = 0.0;
        m_Constraints.gridwidth = GridBagConstraints.REMAINDER;
        this.getContentPane().add(m_Progress, this.m_Constraints);
    }

    /** Add a panel to gui.
	*	@param pb panel to add. */
    public void addPanel(AbstractPanel pb) {
        this.m_TabbedPane.addTab(pb.getTabbedPaneName(), pb.getTabIcon(), pb);
    }

    /** Display GUI, set System.out and start update-thread for GUI. 
	*	Runs commands in properties, if wanted.*/
    private void startGUI() {
        m_Out = new JTextAreaPrintStream(this.m_LoggingTextArea, true);
        m_Out.setOut();
        Thread t = new Thread(new Runnable() {

            public void run() {
                paintGUI();
                if (autorunCommands()) {
                    runProperties();
                }
                while (true) {
                    try {
                        Thread.sleep(1000);
                        paintGUI();
                    } catch (Throwable t) {
                        ;
                    }
                }
            }
        });
        t.start();
        setVisible(true);
    }

    /** Make a screenshot of every panel and save in
	*	files in the current directory. 
	*	@since 0.07-2003-08-31 */
    protected void makePanelScreenshots() {
        final Component[] panels = m_TabbedPane.getComponents();
        for (int i = 0; i < panels.length; ++i) {
            final AbstractPanel p = (AbstractPanel) (panels[i]);
            final String sFilename = p.getTabbedPaneName().replace('/', '_').replace(' ', '_') + ".png";
            final BufferedImage bufImg = new BufferedImage(p.getWidth(), p.getHeight(), BufferedImage.TYPE_INT_RGB);
            p.paint(bufImg.getGraphics());
            ParameterBlock pb = new ParameterBlock();
            pb.add(bufImg);
            PlanarImage rImage = (PlanarImage) JAI.create("awtImage", pb);
            JAI.create("filestore", rImage, sFilename, "PNG").createInstance();
            System.out.println("Saved " + sFilename);
        }
    }

    /** Load the properties from choosen file. 
	*	Uses a file-dialog to get filename. */
    protected void loadProperties() {
        FileDialog fd = new FileDialog(this, "Select file to load settings", FileDialog.LOAD);
        fd.setVisible(true);
        if (fd.getFile() == null || fd.getDirectory() == null) {
            return;
        }
        final String sFilename = fd.getDirectory() + File.separator + fd.getFile();
        loadProperties(sFilename);
    }

    /** Load the properties from specified file.
	*	@param sPropertiesfile File containing the 
	*	properties for the application. */
    protected void loadPropertiesWithoutSet(String sPropertiesfile) {
        if (sPropertiesfile == null) {
            System.out.println("No property-file specified. Using default values.");
            return;
        } else {
            try {
                FileInputStream fis = new FileInputStream(sPropertiesfile);
                m_Properties.clear();
                m_Properties.load(fis);
                fis.close();
            } catch (Exception ex) {
                System.err.println(ex);
                System.err.println("Error while loading property-file. Using default values.");
            }
        }
    }

    /** Load the properties from specified file
	*	and set values for panels.
	*	@param sPropertiesfile File containing the 
	*	properties for the application. */
    protected void loadProperties(String sPropertiesfile) {
        System.out.println("Loading properties from '" + sPropertiesfile + "'.");
        loadPropertiesWithoutSet(sPropertiesfile);
        final Component[] panels = m_TabbedPane.getComponents();
        for (int i = 0; i < panels.length; ++i) {
            final AbstractPanel p = (AbstractPanel) (panels[i]);
            p.loadProperties(m_Properties);
        }
    }

    /** Save the properties from choosen file. 
	*	Uses a file-dialog to get filename. */
    protected void saveProperties() {
        if (m_Properties == null) {
            System.out.println("No properties loaded.");
            System.err.println("No properties loaded.");
            return;
        }
        FileDialog fd = new FileDialog(this, "Select file to save settings", FileDialog.SAVE);
        fd.setVisible(true);
        if (fd.getFile() == null || fd.getDirectory() == null) {
            return;
        }
        final String sFilename = fd.getDirectory() + File.separator + fd.getFile();
        saveProperties(sFilename);
    }

    /** Save the properties from specified file. 
	*	@param sPropertiesfile File containing the properties 
	*	for the application. */
    protected void saveProperties(String sPropertiesfile) {
        if (m_Properties == null) {
            System.out.println("No properties loaded.");
            System.err.println("No properties loaded.");
            return;
        }
        if (sPropertiesfile == null) {
            throw new IllegalArgumentException("No file specified. sPropertiesfile was sPropertiesfile");
        } else {
            final Component[] panels = m_TabbedPane.getComponents();
            for (int i = 0; i < panels.length; ++i) {
                final AbstractPanel p = (AbstractPanel) (panels[i]);
                p.saveProperties(m_Properties);
            }
            try {
                final FileOutputStream fos = new FileOutputStream(sPropertiesfile);
                m_Properties.store(fos, "LegoCCApplication " + getVersion() + "\n# (use bash command sort to resort)");
                fos.close();
            } catch (Exception ex) {
                System.err.println(ex);
                System.err.println("Error while loading property-file. Using default values.");
            }
        }
    }

    /** List the content of 
	*	the currently loaded properties file. */
    public void listProperties() {
        if (m_Properties != null) {
            m_Properties.list(System.out);
        } else {
            System.out.println("No properties loaded.");
        }
    }

    /** Fire a list of events given in property-file. 
	*	Special properties contain information,
	*	which event should be fired to which panel. */
    protected void runProperties() {
        if (m_Properties == null) {
            System.out.println("No properties loaded.");
            System.err.println("No properties loaded.");
            return;
        }
        Enumeration names = m_Properties.propertyNames();
        Vector<String> events = new Vector<String>();
        while (names.hasMoreElements()) {
            String sName = (String) (names.nextElement());
            if (Character.isDigit(sName.substring(0, 1).charAt(0)) && sName.length() == 5) {
                events.add(sName);
            }
        }
        String[] eventStrings = new String[events.size()];
        eventStrings = (String[]) events.toArray(eventStrings);
        Arrays.sort(eventStrings);
        final Hashtable<String, AbstractPanel> panelHash = new Hashtable<String, AbstractPanel>();
        final Component[] panels = m_TabbedPane.getComponents();
        for (int i = 0; i < panels.length; ++i) {
            final AbstractPanel p = (AbstractPanel) (panels[i]);
            final String sClass = p.getClass().getName().substring(p.getClass().getName().lastIndexOf(".") + 1);
            panelHash.put(sClass, p);
        }
        for (int i = 0; i < eventStrings.length; ++i) {
            String sValue = m_Properties.getProperty(eventStrings[i]);
            int nSub = sValue.indexOf('_');
            if (nSub < 0) {
                System.out.println("Invalid entry '" + eventStrings[i] + "'.");
                continue;
            }
            String sPanel = sValue.substring(0, nSub);
            String sCommand = sValue.substring(nSub + 1);
            System.out.println("Fire command '" + sCommand + "' on Panel '" + sPanel + "'.");
            AbstractPanel p = (AbstractPanel) (panelHash.get(sPanel));
            if (p == null) {
                System.out.println("No Panel named '" + sPanel + "'.");
                continue;
            } else {
                m_TabbedPane.setSelectedComponent(p);
                paint(getGraphics());
                p.actionPerformed(new ApplicationActionEvent(this, 0, sCommand));
                System.out.println("Finished command '" + sCommand + "' on Panel '" + sPanel + "'.");
            }
        }
    }
}
