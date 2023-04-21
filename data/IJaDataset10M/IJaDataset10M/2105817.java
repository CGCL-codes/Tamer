package maltcms.ui.wizard;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.filechooser.FileFilter;
import maltcms.runtime.LocalHostLauncher;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.netbeans.api.wizard.WizardDisplayer;
import org.netbeans.spi.wizard.Wizard;
import org.netbeans.spi.wizard.WizardException;
import org.netbeans.spi.wizard.WizardPage;
import org.netbeans.spi.wizard.WizardPage.WizardResultProducer;
import cross.Logging;
import cross.event.AEvent;
import cross.event.EventSource;
import cross.event.IEvent;
import cross.event.IEventSource;
import cross.event.IListener;
import cross.ui.ConfigurationEditor;

/**
 * 
 * @author Nils.Hoffmann@CeBiTec.Uni-Bielefeld.DE
 * 
 * 
 */
public class PipelineWizard implements WizardResultProducer, HyperlinkListener, IEventSource<Configuration> {

    private PropertiesConfiguration cfg = null;

    private RunConfigurationAction runConfig = null;

    private LoadConfigurationAction loadConfig = null;

    private EditConfigurationAction editConfig = null;

    private CreateConfigurationAction createConfig = null;

    private EventSource<Configuration> es = new EventSource<Configuration>();

    private JFrame cfgEditorFrame = null;

    private ConfigurationEditor pe = null;

    private JLabel jl = null;

    /**
	 * Goals for v0.1 Configure a pipeline for
	 * <ul>
	 * <li>anchor finding/loading</li>
	 * <li>binning, preprocessing</li>
	 * <li>alignment</li>
	 * <li>visualization</li>
	 * </ul>
	 * and allow the user to decide on the first pane, whether an existing
	 * pipeline configuration should be loaded, or if a new one should be
	 * created. After configuration and validation, the user should be asked,
	 * whether he wants to save his configuration.
	 * 
	 * After finishing of the wizard, maltcms would be started with the
	 * corresponding configuration. User selectable, whether within the same VM
	 * (not recommended for clients) or as a job submitted to the SGE or as a
	 * stand alone java process.
	 * 
	 * @param args
	 */
    public static void main(String[] args) {
        System.setProperty("log4j.configuration", "file://${user.dir}/cfg/log4j.properties");
        System.setSecurityManager(null);
        Runnable r = new Runnable() {

            @Override
            public void run() {
                createChromAFrame();
            }

            private void createChromAFrame() {
                final JFrame jf = new JFrame("ChromA");
                jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                final JSplitPane jsp = new javax.swing.JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
                final JEditorPane jep = new JEditorPane("text/html", "");
                StringBuilder sb = new StringBuilder();
                sb.append("<h1 style=\"font-family:Arial,Tahoma,sans-serif;\">Welcome to ChromA</h1>");
                sb.append("<h2 style=\"font-family:Arial,Tahoma,sans-serif;\">Getting Started</h2>");
                sb.append("<p style=\"font-family:Arial,Tahoma,sans-serif;\">If you do not have any data in netcdf or mzXML format at hand, please <a href=\"http://bibiserv.techfak.uni-bielefeld.de/chroma/download.html\">download some example files</a></p>");
                sb.append("<p style=\"font-family:Arial,Tahoma,sans-serif;\">To <a href=\"http://bibiserv.techfak.uni-bielefeld.de/chroma/createConfiguration\">create a new configuration</a> for ChromA, select <em>File->Create config</em></p>");
                sb.append("<p style=\"font-family:Arial,Tahoma,sans-serif;\">To <a href=\"http://bibiserv.techfak.uni-bielefeld.de/chroma/loadConfiguration\">load an existing configuration</a> for ChromA, select <em>File->Load config</em></p>");
                sb.append("<h2 style=\"font-family:Arial,Tahoma,sans-serif;\">Running a configuration</h2>");
                sb.append("<p style=\"font-family:Arial,Tahoma,sans-serif;\">After loading or creation of a new configuration, you can <a href=\"http://bibiserv.techfak.uni-bielefeld.de/chroma/runConfiguration\">run the active configuration</a> by selecting <em>Run->Run configuration</em> in the menu");
                sb.append("<p style=\"font-family:Arial,Tahoma,sans-serif;\">As soon as configuration is executed, the right hand side of this window will show a progress view. After reaching 100%, the results will be presented either by opening your system's default file browser, or by showing a dialog, which gives the URL on your computer under which the results have been saved.</p>");
                jep.setText(sb.toString());
                final PipelineWizard pw = new PipelineWizard();
                jep.addHyperlinkListener(pw);
                jep.setEditable(false);
                jsp.add(new JScrollPane(jep));
                jsp.setDividerLocation(-1);
                jsp.setOneTouchExpandable(true);
                LoadConfigurationAction loadConfigAction = (LoadConfigurationAction) pw.getLoadConfigurationAction();
                loadConfigAction.setContainer(jsp);
                CreateConfigurationAction createConfigAction = (CreateConfigurationAction) pw.getCreateConfigurationAction();
                createConfigAction.setPipelineWizard(pw);
                AbstractAction aboutAction = new AbstractAction("About") {

                    private static final long serialVersionUID = -5667519619218922784L;

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JEditorPane jep = new JEditorPane("text/html", "<p style=\"font-family:Arial,Tahoma,sans-serif;\">ChromA version 0.9, April 2009<br />by Nils Hoffmann &lt;Nils.Hoffmann@CeBiTec.Uni-Bielefeld.DE&gt;</p><p style=\"font-family:Arial,Tahoma,sans-serif;\"><a href=\"http://bibiserv.techfak.uni-bielefeld.de/chroma/\">Visit ChromA on the web</a></p><p style=\"font-family:Arial,Tahoma,sans-serif;\">Do you have any ideas on how to improve ChromA?<br /><a href=\"https://sourceforge.net/tracker/?func=add&group_id=251287&atid=1126546\">Visit our feature request tracker</a></p>");
                        jep.setEditable(false);
                        jep.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
                        jep.addHyperlinkListener(pw);
                        JOptionPane.showMessageDialog(jf, jep, "About ChromA", JOptionPane.INFORMATION_MESSAGE);
                    }
                };
                JMenu jm = new JMenu("File");
                JMenuItem jmi1 = new JMenuItem(loadConfigAction);
                JMenuItem jmi2 = new JMenuItem(createConfigAction);
                jm.add(jmi1);
                jm.add(jmi2);
                jm.add(new JSeparator(JSeparator.HORIZONTAL));
                jm.add(new JMenuItem(new AbstractAction("Quit") {

                    private static final long serialVersionUID = -2780507053390931185L;

                    public void actionPerformed(ActionEvent e) {
                        jf.setVisible(false);
                        System.exit(0);
                    }

                    ;
                }));
                JMenu jm2 = new JMenu("Run");
                RunConfigurationAction rca = (RunConfigurationAction) pw.getRunConfigurationAction();
                rca.setContainer(jsp);
                rca.setEnabled(false);
                JMenuItem jmi4 = new JMenuItem(rca);
                jm2.add(jmi4);
                JMenu jm1 = new JMenu("More");
                JMenuItem jmi3 = new JMenuItem(aboutAction);
                jm1.add(jmi3);
                JMenuBar jmb = new JMenuBar();
                jmb.add(jm);
                jmb.add(jm2);
                jmb.add(jm1);
                jf.setJMenuBar(jmb);
                jf.add(jsp, BorderLayout.CENTER);
                pw.getMessageArea().setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
                jf.add(pw.getMessageArea(), BorderLayout.SOUTH);
                jf.setSize(800, 600);
                jf.setLocationRelativeTo(null);
                jf.setVisible(true);
            }
        };
        SwingUtilities.invokeLater(r);
    }

    /**
	 * @return
	 */
    protected AbstractAction getRunConfigurationAction() {
        if (this.runConfig == null) {
            this.runConfig = new RunConfigurationAction("Run configuration");
        }
        return this.runConfig;
    }

    /**
	 * @return
	 */
    protected AbstractAction getLoadConfigurationAction() {
        if (this.loadConfig == null) {
            this.loadConfig = new LoadConfigurationAction("Load config");
        }
        return this.loadConfig;
    }

    /**
	 * @return
	 */
    protected AbstractAction getEditConfigurationAction() {
        if (this.editConfig == null) {
            this.editConfig = new EditConfigurationAction("Edit config");
            this.editConfig.setPipelineWizard(this);
        }
        return this.editConfig;
    }

    /**
	 * @return
	 */
    protected AbstractAction getCreateConfigurationAction() {
        if (this.createConfig == null) {
            this.createConfig = new CreateConfigurationAction("Create config");
        }
        return this.createConfig;
    }

    public JLabel getMessageArea() {
        if (this.jl == null) {
            this.jl = new JLabel("Loaded configuration: none");
        }
        return this.jl;
    }

    public JFrame getConfigurationEditor() {
        JFrame cfgEditorFrame = new JFrame();
        cfgEditorFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        if (this.pe == null) {
            this.pe = new ConfigurationEditor();
            addListener(this.pe);
            cfgEditorFrame.add(new JScrollPane(this.pe));
        }
        return cfgEditorFrame;
    }

    public void execChroma(final PropertiesConfiguration cfg, final Container jf) {
        LocalHostLauncher lhl = new LocalHostLauncher(cfg, jf);
        Thread t = new Thread(lhl);
        t.start();
    }

    public class CreateConfigurationAction extends AbstractAction {

        private PipelineWizard pw = null;

        /**
         * 
         */
        public CreateConfigurationAction() {
            super();
        }

        /**
		 * @param name
		 * @param icon
		 */
        public CreateConfigurationAction(String name, Icon icon) {
            super(name, icon);
        }

        /**
		 * @param name
		 */
        public CreateConfigurationAction(String name) {
            super(name);
        }

        /**
		 * @param pw1
		 */
        public void setPipelineWizard(PipelineWizard pw1) {
            this.pw = pw1;
        }

        /**
         * 
         */
        private static final long serialVersionUID = -6216223747360194482L;

        @Override
        public void actionPerformed(ActionEvent e) {
            Runnable r1 = new Runnable() {

                @Override
                public void run() {
                    Wizard wiz = WizardPage.createWizard(new Class[] { FileInputOutputPane.class, AnchorDefinitionPane.class, PreprocessingPane.class, AlignmentPane.class, VisualizationPane.class }, pw);
                    File cfg = (File) WizardDisplayer.showWizard(wiz);
                    if (cfg != null) {
                        try {
                            PropertiesConfiguration pc = new PropertiesConfiguration(cfg);
                            setConfiguration(pc);
                            RunConfigurationAction rca = (RunConfigurationAction) getRunConfigurationAction();
                            rca.setConfiguration(pc);
                            rca.setEnabled(true);
                            getMessageArea().setText("Created configuration " + pc.getFileName());
                        } catch (ConfigurationException ce) {
                            JOptionPane.showMessageDialog(pw.getMessageArea().getTopLevelAncestor(), "Loading of file " + cfg.getAbsolutePath() + " failed:\n" + ce.getLocalizedMessage());
                        }
                    } else {
                        return;
                    }
                }
            };
            SwingUtilities.invokeLater(r1);
        }
    }

    public void setConfiguration(PropertiesConfiguration cfg1) {
        this.cfg = cfg1;
    }

    public class EditConfigurationAction extends AbstractAction {

        private PipelineWizard pw;

        /**
         * 
         */
        private static final long serialVersionUID = -4941454703967935783L;

        /**
         * 
         */
        public EditConfigurationAction() {
            super();
        }

        /**
		 * @param name
		 * @param icon
		 */
        public EditConfigurationAction(String name, Icon icon) {
            super(name, icon);
        }

        /**
		 * @param name
		 */
        public EditConfigurationAction(String name) {
            super(name);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (cfg != null) {
                Runnable r = new Runnable() {

                    @Override
                    public void run() {
                        getMessageArea().setText("Editing config: " + cfg.getPath());
                        if (pw != null) {
                            pw.fireEvent(new AEvent<Configuration>(cfg, pw));
                            getConfigurationEditor().setVisible(true);
                            getConfigurationEditor().pack();
                        } else {
                        }
                    }

                    ;
                };
                SwingUtilities.invokeLater(r);
            }
        }

        /**
		 * @param pw1
		 */
        public void setPipelineWizard(PipelineWizard pw1) {
            this.pw = pw1;
        }
    }

    public class RunConfigurationAction extends AbstractAction {

        /**
         * 
         */
        private static final long serialVersionUID = 7614497705493470346L;

        /**
         * 
         */
        public RunConfigurationAction() {
            super();
        }

        /**
		 * @param name
		 * @param icon
		 */
        public RunConfigurationAction(String name, Icon icon) {
            super(name, icon);
        }

        /**
		 * @param name
		 */
        public RunConfigurationAction(String name) {
            super(name);
        }

        private Container c = null;

        private PropertiesConfiguration cfg = null;

        public void setConfiguration(PropertiesConfiguration cfg1) {
            this.cfg = cfg1;
        }

        public void setContainer(Container jf) {
            this.c = jf;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (cfg != null && c != null) {
                Runnable r = new Runnable() {

                    @Override
                    public void run() {
                        getMessageArea().setText("Executing config: " + cfg.getPath());
                        execChroma(cfg, c);
                    }
                };
                SwingUtilities.invokeLater(r);
            }
        }
    }

    public class LoadConfigurationAction extends AbstractAction {

        private Container jf = null;

        /**
         * 
         */
        public LoadConfigurationAction() {
            super();
        }

        /**
		 * @param name
		 * @param icon
		 */
        public LoadConfigurationAction(String name, Icon icon) {
            super(name, icon);
        }

        /**
		 * @param name
		 */
        public LoadConfigurationAction(String name) {
            super(name);
        }

        public void setContainer(Container c) {
            this.jf = c;
        }

        /**
         * 
         */
        private static final long serialVersionUID = -3371831501616427849L;

        @Override
        public void actionPerformed(ActionEvent e) {
            Runnable r2 = new Runnable() {

                @Override
                public void run() {
                    JFileChooser jfc = new JFileChooser();
                    jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                    jfc.setMultiSelectionEnabled(false);
                    jfc.setFileFilter(new FileFilter() {

                        @Override
                        public String getDescription() {
                            return ".properties";
                        }

                        @Override
                        public boolean accept(File f) {
                            if (f.isDirectory()) {
                                return true;
                            }
                            if (f.getAbsolutePath().endsWith(".properties")) {
                                return true;
                            }
                            return false;
                        }
                    });
                    int ret = jfc.showOpenDialog(jf);
                    if (ret == JFileChooser.APPROVE_OPTION) {
                        try {
                            PropertiesConfiguration pc = new PropertiesConfiguration(jfc.getSelectedFile());
                            setConfiguration(pc);
                            RunConfigurationAction rca = (RunConfigurationAction) getRunConfigurationAction();
                            rca.setConfiguration(pc);
                            rca.setEnabled(true);
                            getMessageArea().setText("Loaded configuration: " + jfc.getSelectedFile().getAbsolutePath());
                        } catch (ConfigurationException e1) {
                            JOptionPane.showMessageDialog(jf, "Loading of file " + jfc.getSelectedFile().getAbsolutePath() + " failed:\n" + e1.getLocalizedMessage());
                        }
                    }
                }
            };
            SwingUtilities.invokeLater(r2);
        }
    }

    ;

    @Override
    public boolean cancel(Map arg0) {
        return true;
    }

    @Override
    public Object finish(Map arg0) throws WizardException {
        Map gatheredSettings = (Map) arg0;
        if (gatheredSettings != null) {
            PropertiesConfiguration cfg = new PropertiesConfiguration();
            for (Object key : gatheredSettings.keySet()) {
                if (key.equals("alignment.algorithm.distance")) {
                    String value = (String) gatheredSettings.get(key);
                    if (value.equals("Time Penalized Dot")) {
                        cfg.setProperty((String) key, "maltcms.commands.distances.ArrayCos");
                        cfg.setProperty("alignment.algorithm", "maltcms.commands.distances.dtw.MZIDynamicTimeWarp");
                    } else if (value.equals("Cosine Similarity")) {
                        cfg.setProperty((String) key, "maltcms.commands.distances.ArrayCos");
                        cfg.setProperty("alignment.algorithm", "maltcms.commands.distances.dtw.MZIDynamicTimeWarp");
                    } else if (value.equals("Linear Correlation Similarity")) {
                        cfg.setProperty((String) key, "maltcms.commands.distances.ArrayCorr");
                        cfg.setProperty("alignment.algorithm", "maltcms.commands.distances.dtw.MZIDynamicTimeWarp");
                    } else if (value.equals("Euclidean Distance")) {
                        cfg.setProperty((String) key, "maltcms.commands.distances.ArrayLp");
                        cfg.setProperty("alignment.algorithm", "maltcms.commands.distances.dtw.MZIDynamicTimeWarp");
                    } else if (value.equals("Hamming Distance")) {
                        cfg.setProperty((String) key, "maltcms.commands.distances.ArrayHamming");
                        cfg.setProperty("alignment.algorithm", "maltcms.commands.distances.dtw.MZIDynamicTimeWarp");
                    } else if (value.equals("TIC Squared Distance")) {
                        cfg.setProperty((String) key, "maltcms.commands.distances.ArrayLp");
                        cfg.setProperty("alignment.algorithm", "maltcms.commands.distances.dtw.TICDynamicTimeWarp");
                    } else {
                        System.err.println("Unknown type " + value + ", setting default");
                        cfg.setProperty((String) key, "maltcms.commands.distances.ArrayCos");
                        cfg.setProperty("alignment.algorithm", "maltcms.commands.distances.dtw.MZIDynamicTimeWarp");
                    }
                } else if (key.equals("input.dataInfo")) {
                    cfg.setProperty("input.dataInfo", gatheredSettings.get(key));
                } else if (key.equals("maltcms.commands.fragments.DenseArrayProducer.maskMasses")) {
                    if (gatheredSettings.get("maltcms.commands.fragments.DenseArrayProducer.maskMasses") == null) {
                        cfg.setProperty("maltcms.commands.fragments.DenseArrayProducer.maskMasses", "");
                    } else {
                        Object o = gatheredSettings.get("maltcms.commands.fragments.DenseArrayProducer.maskMasses");
                        if (o instanceof String) {
                            String s = (String) o;
                            s.replaceAll(" ", ",");
                            cfg.setProperty("maltcms.commands.fragments.DenseArrayProducer.maskMasses", s);
                        }
                    }
                } else {
                    cfg.setProperty((String) key, gatheredSettings.get(key));
                }
            }
            cfg.setProperty("maltcms.ui.charts.PlotRunner.saveGraphics", true);
            cfg.setProperty("cross.datastructures.workflow.DefaultWorkflow.saveHTML", true);
            File f = checkOverwrite();
            if (f != null) {
                try {
                    cfg.save(f);
                } catch (ConfigurationException e) {
                    Logging.getLogger(this.getClass()).warn("{}", e.getLocalizedMessage());
                }
            }
            return f;
        }
        return null;
    }

    public File checkOverwrite() {
        JFileChooser jfc = new JFileChooser(cwd);
        jfc.setDialogTitle("Select existing or create new file to save properties");
        jfc.setMultiSelectionEnabled(false);
        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int ret = jfc.showSaveDialog(null);
        if (ret == JFileChooser.APPROVE_OPTION) {
            if (jfc.getSelectedFile().exists()) {
                int opRet = JOptionPane.showOptionDialog(null, "File " + jfc.getSelectedFile().getAbsolutePath() + "\nalready exists, overwrite?", "Overwrite existing file?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[] { "Overwrite", "Cancel" }, "Cancel");
                if (opRet == JOptionPane.YES_OPTION) {
                    return jfc.getSelectedFile();
                } else {
                    cwd = jfc.getSelectedFile().getParentFile();
                    return checkOverwrite();
                }
            }
            return jfc.getSelectedFile();
        }
        return null;
    }

    private File cwd = null;

    @Override
    public void hyperlinkUpdate(HyperlinkEvent e) {
        if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            if (e.getURL().getPath().endsWith("loadConfiguration")) {
                getLoadConfigurationAction().actionPerformed(new ActionEvent(this, 1, "loadConfiguration"));
            } else if (e.getURL().getPath().endsWith("createConfiguration")) {
                getCreateConfigurationAction().actionPerformed(new ActionEvent(this, 1, "createConfiguration"));
            } else if (e.getURL().getPath().endsWith("editConfiguration")) {
                getEditConfigurationAction().actionPerformed(new ActionEvent(this, 1, "editConfiguration"));
            } else if (e.getURL().getPath().endsWith("runConfiguration")) {
                getRunConfigurationAction().actionPerformed(new ActionEvent(this, 1, "runConfiguration"));
            } else {
                if (Desktop.isDesktopSupported()) {
                    try {
                        Desktop.getDesktop().browse(e.getURL().toURI());
                    } catch (IOException e1) {
                        Logging.getLogger(this.getClass()).warn("{}", e1.getLocalizedMessage());
                    } catch (URISyntaxException e1) {
                        Logging.getLogger(this.getClass()).warn("{}", e1.getLocalizedMessage());
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please view your results at " + e.getURL().toString());
                }
            }
        }
    }

    @Override
    public void addListener(IListener<IEvent<Configuration>> l) {
        es.addListener(l);
    }

    @Override
    public void fireEvent(IEvent<Configuration> e) {
        es.fireEvent(e);
    }

    @Override
    public void removeListener(IListener<IEvent<Configuration>> l) {
        es.addListener(l);
    }
}
