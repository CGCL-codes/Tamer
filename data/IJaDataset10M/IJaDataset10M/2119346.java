package de.sicari.starter.gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import de.sicari.starter.parameters.JavaAuthLoginConfigFile;
import de.sicari.starter.parameters.JavaBinaryFile;
import de.sicari.starter.parameters.JavaClassPath;
import de.sicari.starter.parameters.JavaSecurityPolicyFile;
import de.sicari.starter.parameters.JceLibraryFile;
import de.sicari.starter.parameters.SicariBaseDirectory;
import de.sicari.starter.parameters.SicariLogDirectory;
import de.sicari.starter.parameters.SicariLoggingConfigFile;
import de.sicari.starter.parameters.SicariScriptFile;
import de.sicari.starter.parameters.SicariScriptParameters;
import de.sicari.starter.util.Common;
import de.sicari.starter.util.Utils;

/**
 * This frame is supposed to show detailed informations about the parameters
 * that would be used for starting <i>SicAri</i> with the current user input.
 *
 * @author Matthias Pressfreund
 * @version "$Id: InfoFrame.java 335 2007-09-06 19:20:10Z jpeters $"
 */
public class InfoFrame extends JFrame implements ChangeListener {

    private static final long serialVersionUID = -818939064702463538L;

    /**
     * Text output for (yet) undefined parameters
     */
    protected static final String UNDEFINED_ = "*** UNDEFINED ***";

    /**
     * The parent <code>StarterFrame</code>
     */
    protected StarterFrame sframe_;

    /**
     * The button to dismiss the info window
     */
    protected JButton dismissButton_;

    /**
     * The text area
     */
    protected JTextArea textArea_;

    /**
     * The user defined system properties
     */
    protected Map<String, String> userprp_;

    /**
     * Create the <code>InfoFrame</code>.
     *
     * @param sframe The parent <code>StarterFrame</code>
     * @param userprp The user defined system properties
     */
    public InfoFrame(StarterFrame sframe, Map<String, String> userprp) {
        super("Detailed SicAri Startup Info");
        TitledBorder border;
        JPanel infoPanel;
        Container cp;
        sframe_ = sframe;
        userprp_ = userprp;
        setIconImage(sframe_.getIconImage());
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        infoPanel = new JPanel(new GridBagLayout());
        border = BorderFactory.createTitledBorder("Parameters");
        border.setTitleJustification(TitledBorder.CENTER);
        infoPanel.setBorder(border);
        Utils.addConstrained(infoPanel, new JScrollPane(textArea_ = new JTextArea()), 0, 0, 1, 1, GridBagConstraints.BOTH, GridBagConstraints.CENTER, 1, 1, new Insets(5, 5, 5, 5));
        textArea_.setEditable(false);
        textArea_.setTabSize(2);
        textArea_.setBackground(Color.lightGray.brighter());
        cp = getContentPane();
        cp.setLayout(new GridBagLayout());
        Utils.addConstrained(cp, infoPanel, 0, 0, 1, 1, GridBagConstraints.BOTH, GridBagConstraints.SOUTH, 1, 1, new Insets(10, 10, 5, 10));
        Utils.addConstrained(cp, dismissButton_ = new JButton("Dismiss"), 0, 1, 1, 1, GridBagConstraints.NONE, GridBagConstraints.NORTH, 1, 0, new Insets(5, 5, 5, 5));
        dismissButton_.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        addComponentListener(new ComponentAdapter() {

            @Override
            public void componentShown(ComponentEvent e) {
                updateInfoPanel();
            }
        });
        updateInfoPanel();
        Utils.center(this);
    }

    /**
     * Create the info text.
     */
    protected String createInfo() {
        Iterator<Map.Entry<String, String>> meitr;
        Map.Entry<String, String> prp;
        JavaAuthLoginConfigFile jauthcfg;
        SicariLoggingConfigFile slogcfg;
        JavaSecurityPolicyFile jsecpol;
        SicariScriptParameters sparams;
        SicariLogDirectory slogdir;
        SicariBaseDirectory sbase;
        SicariScriptFile sscript;
        StringTokenizer toki;
        Iterator<String> sitr;
        JavaBinaryFile jbin;
        JceLibraryFile sjce;
        JceLibraryFile jce;
        Iterator<File> fitr;
        JavaClassPath jcp;
        StringBuffer text;
        boolean daemon;
        String dlogf;
        List<File> cp;
        daemon = sframe_.isDaemonChecked();
        jbin = sframe_.getJavaBinaryFile();
        jcp = sframe_.getJavaClassPath();
        sbase = sframe_.getSicariBaseDirectory();
        jsecpol = sframe_.getJavaSecurityPolicyFile();
        jauthcfg = sframe_.getJavaAuthLoginConfigFile();
        slogcfg = sframe_.getSicariLoggingConfigFile();
        sscript = sframe_.getSicariScriptFile();
        sparams = sframe_.getSicariScriptParameters();
        slogdir = sframe_.getSicariLogDirectory();
        text = new StringBuffer();
        text.append("Java RE Binary:\n");
        text.append("\t" + ((jbin != null) ? jbin.getAbsolutePath() + " (Version " + jbin.getVersion() + ")" : UNDEFINED_) + "\n");
        text.append("\n");
        text.append("JCE library");
        sjce = JceLibraryFile.findPatch(sbase);
        if (sjce == null) {
            jce = JceLibraryFile.findDefault(jbin);
        } else {
            jce = sjce;
        }
        if (jce != null) {
            if (!jce.isRecommended()) {
                text.append(" (not recommended)");
            }
            text.append(":\n");
            text.append("\tVendor: " + jce.getVendor() + "\n");
            text.append("\tTitle: " + jce.getTitle() + "\n");
            text.append("\tVersion: " + jce.getVersion() + "\n");
        } else {
            text.append(" not found\n");
        }
        text.append("\n");
        text.append("SicAri startup class:\n");
        text.append("\t" + Common.STARTUP_CLASS + "\n");
        text.append("\n");
        text.append("Java Options:\n");
        if (sjce != null) {
            text.append("\t" + Common.JAVA_OPTION_XBOOTCLASSPATHP + sjce.getAbsolutePath() + "\n");
        }
        if (jbin != null && jbin.isServerSupported()) {
            text.append("\t" + Common.JAVA_OPTION_SERVER + "\n");
        }
        text.append("\t" + Common.JAVA_OPTION_CLASSPATH + "\n");
        if (jcp == null) {
            jcp = JavaClassPath.createEmpty();
        }
        cp = jcp.toCompleteList(sbase, jbin);
        if (!cp.isEmpty()) {
            for (fitr = cp.iterator(); fitr.hasNext(); ) {
                text.append("\t\t" + fitr.next());
                text.append(fitr.hasNext() ? File.pathSeparator + "\n" : "\n");
            }
        } else {
            text.append("\t\t" + UNDEFINED_ + "\n");
        }
        text.append("\t" + Common.PROPERTY_ID + Common.JAVA_PROPERTY_EXTDIRS + "=\n");
        if (sbase != null) {
            toki = new StringTokenizer(sbase.completeExtensionDirectories((jbin != null) ? jbin.getExtensionDirectories() : null), File.pathSeparator);
            if (toki.hasMoreTokens()) {
                while (toki.hasMoreTokens()) {
                    text.append("\t\t" + toki.nextToken());
                    text.append(toki.hasMoreTokens() ? File.pathSeparator + "\n" : "\n");
                }
            } else {
                text.append("\t\t" + UNDEFINED_ + "\n");
            }
        } else {
            text.append("\t\t" + UNDEFINED_ + "\n");
        }
        text.append("\t" + Common.PROPERTY_ID + JavaSecurityPolicyFile.PROPERTY + "=\n");
        text.append("\t\t" + ((jsecpol != null) ? jsecpol.getAbsolutePath() : UNDEFINED_) + "\n");
        text.append("\t" + Common.PROPERTY_ID + JavaAuthLoginConfigFile.PROPERTY + "=\n");
        text.append("\t\t" + ((jauthcfg != null) ? jauthcfg.getAbsolutePath() : UNDEFINED_) + "\n");
        text.append("\t" + Common.PROPERTY_ID + SicariBaseDirectory.PROPERTY + "=\n");
        text.append("\t\t" + ((sbase != null) ? sbase.getAbsolutePath() : UNDEFINED_) + "\n");
        text.append("\t" + Common.PROPERTY_ID + Common.JAVA_PROPERTY_SICARI_ETC + "=\n");
        text.append("\t\t" + ((sbase != null) ? new File(sbase.getAbsolutePath(), "etc").getAbsolutePath() : UNDEFINED_) + "\n");
        text.append("\t" + Common.PROPERTY_ID + SicariLoggingConfigFile.PROPERTY + "=\n");
        text.append("\t\t" + ((slogcfg != null) ? slogcfg.getAbsolutePath() : UNDEFINED_) + "\n");
        text.append("\t" + Common.PROPERTY_ID + SicariLogDirectory.PROPERTY + "=\n");
        text.append("\t\t" + ((slogdir != null) ? slogdir.getAbsolutePath() : UNDEFINED_) + "\n");
        for (meitr = userprp_.entrySet().iterator(); meitr.hasNext(); ) {
            prp = meitr.next();
            text.append("\t" + Common.PROPERTY_ID + prp.getKey() + "=\n");
            text.append("\t\t" + prp.getValue() + "\n");
        }
        text.append("\n");
        text.append("SicAri Options:\n");
        if (daemon) {
            text.append("\t" + Common.SICARI_OPTION_DAEMON + "\n");
            dlogf = (sbase != null) ? new File(sbase, Common.DAEMON_LOG_FILE_PREFIX + Common.DAEMON_LOG_FILE_DATE_FORMAT + ".log").getAbsolutePath() : UNDEFINED_;
            text.append("\t" + Common.SICARI_OPTION_DAEMON_OUT + "\n" + "\t\t" + dlogf + "\n");
            text.append("\t" + Common.SICARI_OPTION_DAEMON_ERR + "\n" + "\t\t" + dlogf + "\n");
        }
        text.append("\t" + Common.SICARI_OPTION_F + "\n");
        if (sscript != null) {
            text.append("\t\t" + sscript.getAbsolutePath() + "\n");
            if (sparams != null) {
                for (sitr = sparams.getList().iterator(); sitr.hasNext(); ) {
                    text.append("\t\t" + sitr.next() + "\n");
                }
            }
        } else {
            text.append("\t\t" + UNDEFINED_ + "\n");
        }
        return text.toString();
    }

    /**
     * Update and resize the info panel.
     */
    protected void updateInfoPanel() {
        textArea_.setText(createInfo());
        pack();
    }

    public void stateChanged(ChangeEvent e) {
        if (isVisible()) {
            updateInfoPanel();
        }
    }
}
