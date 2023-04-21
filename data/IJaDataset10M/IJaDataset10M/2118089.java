package swing;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class MenuBar extends JMenuBar {

    private JMenu file = new JMenu("File");

    private JMenu tools = new JMenu("Tools");

    private JMenu activity = new JMenu("Activity");

    private JMenu advanced = new JMenu("Advanced");

    private JMenu help = new JMenu("Help");

    public JMenuItem quit = new JMenuItem("Quit");

    public JMenuItem attachToProject = new JMenuItem("Attach to project");

    public JMenuItem accountManager = new JMenuItem("Account manager");

    public JMenuItem runAlways = new JMenuItem("Run always");

    public JMenuItem runBasedOnPreferences = new JMenuItem("Run based on preferences");

    public JMenuItem suspend = new JMenuItem("Suspend");

    public JMenuItem networkActivityAlwaysAvailable = new JMenuItem("Network activity always available");

    public JMenuItem networkActivityBasedOnPreferences = new JMenuItem("Network activity based on preferences");

    public JMenuItem networkActivitySuspended = new JMenuItem("Network activity suspended");

    public JMenuItem options = new JMenuItem("Options");

    public JMenuItem selectComputer = new JMenuItem("Select computer...");

    public JMenuItem runCPUBenchmarks = new JMenuItem("Run CPU benchmark");

    public JMenuItem retryCommunication = new JMenuItem("Retry communication");

    public JMenuItem jBoincManager = new JMenuItem("JBoinc Manager");

    public JMenuItem jBoincWebsite = new JMenuItem("JBoinc Website");

    public JMenuItem boincWebsite = new JMenuItem("BOINC Website");

    public JMenuItem about = new JMenuItem("About JBoinc Manager");

    private MainWindow m;

    public MenuBar(MainWindow main) {
        m = main;
        add(file);
        add(tools);
        add(activity);
        add(advanced);
        add(help);
        file.add(quit);
        tools.add(attachToProject);
        tools.add(accountManager);
        activity.add(runAlways);
        activity.add(runBasedOnPreferences);
        activity.add(suspend);
        activity.addSeparator();
        activity.add(networkActivityAlwaysAvailable);
        activity.add(networkActivityBasedOnPreferences);
        activity.add(networkActivitySuspended);
        advanced.add(options);
        advanced.add(selectComputer);
        advanced.add(runCPUBenchmarks);
        advanced.add(retryCommunication);
        help.add(jBoincManager);
        help.add(jBoincWebsite);
        help.add(boincWebsite);
        help.addSeparator();
        help.add(about);
        quit.addActionListener(m);
        attachToProject.addActionListener(m);
        accountManager.addActionListener(m);
        runAlways.addActionListener(m);
        runBasedOnPreferences.addActionListener(m);
        suspend.addActionListener(m);
        networkActivityAlwaysAvailable.addActionListener(m);
        networkActivityBasedOnPreferences.addActionListener(m);
        networkActivitySuspended.addActionListener(m);
        options.addActionListener(m);
        selectComputer.addActionListener(m);
        runCPUBenchmarks.addActionListener(m);
        retryCommunication.addActionListener(m);
        jBoincManager.addActionListener(m);
        jBoincWebsite.addActionListener(m);
        boincWebsite.addActionListener(m);
        about.addActionListener(m);
    }
}
