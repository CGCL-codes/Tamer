package au.gov.naa.digipres.xena.plugin.csv;

import java.util.Date;
import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 * A simple application which uses the generated JReleaseInfo file as
 * argument. It creates a table with the properties in the JReleaseInfo
 * file. Properties are read with introspection.<br />
 * You can add this viewer to your java library jar-file, setting
 * the Main-Class Manifest-Attribute to this viewer and providing
 * a "double-click view" of relevant build-data.
 *
 * @author Thomas Cotting, Open Source Competence Group, www.oscg.ch
 */
class JReleaseInfoViewer extends JFrame {

    /** ContentPanel. */
    private JPanel pnlContent = null;

    /** ScrollPanel holding table. */
    private JScrollPane scrlPane = null;

    /** Table showing properties. */
    private JTable tblProps = null;

    /** Status line. */
    private JLabel lblStatus = null;

    /** JReleaseInfo class for introspection. */
    private Class c = null;

    /** Projectname in title */
    private String project = null;

    /** Version in title */
    private String version = null;

    /**
    * Constructor.
    * @param c Class 
    */
    public JReleaseInfoViewer(Class c) {
        super();
        this.c = c;
        enableEvents(AWTEvent.WINDOW_EVENT_MASK);
        initialize();
    }

    /**
    * Center the frame in the middle of the screen.
    * @param frame of window
    */
    private void centerFrame(JFrame frame) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = frame.getSize();
        frame.setLocation((screenSize.width / 2) - (frameSize.width / 2), (screenSize.height / 2) - (frameSize.height / 2));
    }

    /**
    * This method initializes the frame.
    *
    * @return void
    */
    private void initialize() {
        setSize(500, 200);
        centerFrame(this);
        scrlPane = new JScrollPane();
        tblProps = getTable();
        scrlPane.setViewportView(tblProps);
        lblStatus = new JLabel();
        lblStatus.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblStatus.setText("JReleaseInfo Viewer by Open Source Competence Group, www.oscg.ch");
        pnlContent = new JPanel();
        pnlContent.setLayout(new BorderLayout());
        pnlContent.add(scrlPane, BorderLayout.CENTER);
        pnlContent.add(lblStatus, BorderLayout.SOUTH);
        this.setContentPane(pnlContent);
        String title = "Release Information";
        if (project != null) {
            String prefix = project;
            if (version != null) {
                prefix = prefix + " " + version;
            }
            this.setTitle(prefix + " - " + title);
        } else {
            this.setTitle(title);
        }
    }

    /**
    * Create a table with two columns.
    * @return table
    */
    private JTable getTable() {
        JTable tbl = null;
        try {
            Vector cols = new Vector();
            cols.add("Property");
            cols.add("Value");
            Vector rows = new Vector();
            Object obj = c.newInstance();
            Method[] methods = c.getDeclaredMethods();
            Map sortedProps = new TreeMap();
            for (int i = 0; i < methods.length; i++) {
                Vector v = new Vector();
                Method method = methods[i];
                String methodName = method.getName();
                if (methodName.equalsIgnoreCase("getProject")) {
                    Object objP = method.invoke(obj, null);
                    if (objP instanceof String) {
                        project = (String) objP;
                    }
                }
                if (methodName.equalsIgnoreCase("getVersion")) {
                    Object objV = method.invoke(obj, null);
                    if (objV instanceof String) {
                        version = (String) objV;
                    } else {
                        version = objV.toString();
                    }
                }
                if (methodName.startsWith("get")) {
                    String field = methodName.substring(3);
                    v.add(field);
                    v.add(method.invoke(obj, null));
                    sortedProps.put(field, v);
                } else if (methodName.startsWith("is")) {
                    String field = methodName.substring(2);
                    v.add(field);
                    v.add(method.invoke(obj, null));
                    sortedProps.put(field, v);
                }
            }
            rows.addAll(sortedProps.values());
            tbl = new JTable(rows, cols);
            tbl.getColumn("Property").setPreferredWidth(150);
            tbl.getColumn("Value").setPreferredWidth(350);
            tbl.setEnabled(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tbl;
    }

    /**
    * Process Window close event.
    * @param e WindowEvent
    */
    protected void processWindowEvent(WindowEvent e) {
        super.processWindowEvent(e);
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            System.exit(0);
        }
    }
}

/**
 * This class provides information gathered from the build environment.
 * 
 * @author JReleaseInfo AntTask
 */
public class ReleaseInfo {

    /** buildDate (set during build process to 1314923713564L). */
    private static Date buildDate = new Date(1314923713564L);

    /**
    * Get buildDate (set during build process to Fri Sep 02 10:35:13 EST 2011).
    * @return Date buildDate
    */
    public static final Date getBuildDate() {
        return buildDate;
    }

    /**
    * Get release (set during build process to Boolean.FALSE).
    * @return Boolean release
    */
    public static final Boolean isRelease() {
        return Boolean.FALSE;
    }

    /**
    * Get buildNumber (set during build process to 1).
    * @return int buildNumber
    */
    public static final int getBuildNumber() {
        return 1;
    }

    /** project (set during build process to "csv"). */
    private static String project = new String("csv");

    /**
    * Get project (set during build process to "csv").
    * @return String project
    */
    public static final String getProject() {
        return project;
    }

    /** version (set during build process to "2.3.0"). */
    private static String version = new String("2.3.0");

    /**
    * Get version (set during build process to "2.3.0").
    * @return String version
    */
    public static final String getVersion() {
        return version;
    }

    public static void main(String[] args) throws Exception {
        JReleaseInfoViewer frame = new JReleaseInfoViewer(ReleaseInfo.class);
        frame.setVisible(true);
    }
}
