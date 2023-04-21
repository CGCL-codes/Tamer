package karto.views;

import karto.datamodels.Messages;
import karto.datamodels.RCListenerFactory;
import javax.swing.*;

public class RCMenuBar extends JMenuBar {

    /**
	 * 
	 */
    private static final long serialVersionUID = 2103658983214470132L;

    private final RCMainframe rcMainframe;

    private JMenu FileMenu;

    private JMenu WindowMenu;

    private JMenu HelpMenu;

    private JMenuItem OpenShpMenuItem;

    private JMenuItem OpenOwnMenuItem;

    private JMenuItem OpenSpeckMenuItem;

    private JMenuItem ExitMenuItem;

    private JMenuItem HelpMenuItem;

    private JMenuItem ArrangeMenuItem;

    private JMenuItem CascadeMenuItem;

    public RCMenuBar(RCMainframe rcMainframe) {
        super();
        this.rcMainframe = rcMainframe;
        init();
        addComp();
    }

    private void init() {
        RCListenerFactory listenerF = new RCListenerFactory(rcMainframe);
        FileMenu = new JMenu(Messages.getString("RCMenuBar.File"));
        WindowMenu = new JMenu(Messages.getString("RCMenuBar.Window"));
        HelpMenu = new JMenu(Messages.getString("RCMenuBar.HelpMenu"));
        HelpMenuItem = new JMenuItem(Messages.getString("RCMenuBar.HelpMenuItem"));
        HelpMenuItem.addActionListener(listenerF.getHelpL());
        HelpMenuItem.setIcon(Utils.loadIcon(this, "images/VDhelp.gif"));
        OpenOwnMenuItem = new JMenuItem(Messages.getString("RCMenuBar.OpenOwn"));
        OpenOwnMenuItem.setIcon(Utils.loadIcon(this, "images/VDopen.gif"));
        OpenOwnMenuItem.addActionListener(listenerF.getOpenOwnFileActionListener());
        OpenShpMenuItem = new JMenuItem(Messages.getString("RCMenuBar.OpenShp"));
        OpenShpMenuItem.setIcon(Utils.loadIcon(this, "images/VDopen.gif"));
        OpenShpMenuItem.addActionListener(listenerF.getOpenShapeFileActionListener());
        OpenSpeckMenuItem = new JMenuItem(Messages.getString("RCMenuBar.OpenSpeck"));
        OpenSpeckMenuItem.setIcon(Utils.loadIcon(this, "images/VDopen.gif"));
        OpenSpeckMenuItem.addActionListener(listenerF.getOpenSpeckmanActionListener());
        ExitMenuItem = new JMenuItem(Messages.getString("RCMenuBar.Exit"));
        ExitMenuItem.addActionListener(listenerF.getExitL());
        ArrangeMenuItem = new JMenuItem(Messages.getString("RCMenuBar.Arrange_Windows"));
        ArrangeMenuItem.addActionListener(listenerF.getArrangeL());
        CascadeMenuItem = new JMenuItem(Messages.getString("RCMenuBar.Cascade_Windows"));
        CascadeMenuItem.addActionListener(listenerF.getCascadeL());
    }

    private void addComp() {
        this.add(FileMenu);
        this.add(WindowMenu);
        this.add(HelpMenu);
        FileMenu.add(OpenShpMenuItem);
        FileMenu.add(OpenOwnMenuItem);
        FileMenu.addSeparator();
        FileMenu.add(OpenSpeckMenuItem);
        FileMenu.addSeparator();
        FileMenu.addSeparator();
        FileMenu.add(ExitMenuItem);
        WindowMenu.add(ArrangeMenuItem);
        WindowMenu.add(CascadeMenuItem);
        HelpMenu.add(HelpMenuItem);
    }
}
