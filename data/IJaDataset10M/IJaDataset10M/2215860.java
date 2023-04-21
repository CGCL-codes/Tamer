package links.gui;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import links.LinkTopology;
import links.comm.Sender;

public class GUI extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1026620658585504985L;

    private static final String PROGRAM_TITLE = "Link properties GUI";

    private JMenu file_menu;

    private JMenuItem exit_menuitem;

    private JMenu options_menu;

    private JMenuItem configuration_menuitem;

    private JMenuItem load_menuitem;

    private JMenuItem createdefault_menuitem;

    private LinkTopology g;

    private JMenuItem save_menuitem;

    private File graph_file;

    private JMenuItem saveas_menuitem;

    private TopologyGUI tg;

    private VisualizationGUI vg;

    public GUI() {
        super(PROGRAM_TITLE);
        g = new LinkTopology();
        Sender s = new Sender();
        g.addGraphModelListener(s);
        addMenuBar();
        JTabbedPane p = new JTabbedPane();
        tg = new TopologyGUI(g);
        p.addTab("Direct links", tg);
        add(p);
        vg = new VisualizationGUI(g);
        p.addTab("Link visualization", vg);
        add(p);
        p.setSelectedIndex(1);
        initialize();
    }

    private void addMenuBar() {
        JMenuBar bar = new JMenuBar();
        setJMenuBar(bar);
        file_menu = new JMenu("File");
        file_menu.setMnemonic('F');
        createdefault_menuitem = new JMenuItem("New");
        createdefault_menuitem.setAccelerator(KeyStroke.getKeyStroke('N', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask(), false));
        createdefault_menuitem.addActionListener(this);
        file_menu.add(createdefault_menuitem);
        load_menuitem = new JMenuItem("Load");
        load_menuitem.setAccelerator(KeyStroke.getKeyStroke('O', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask(), false));
        load_menuitem.addActionListener(this);
        file_menu.add(load_menuitem);
        save_menuitem = new JMenuItem("Save");
        save_menuitem.setAccelerator(KeyStroke.getKeyStroke('S', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask(), false));
        save_menuitem.addActionListener(this);
        file_menu.add(save_menuitem);
        saveas_menuitem = new JMenuItem("Save as...");
        saveas_menuitem.addActionListener(this);
        file_menu.add(saveas_menuitem);
        file_menu.addSeparator();
        exit_menuitem = new JMenuItem("Exit");
        exit_menuitem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        file_menu.add(exit_menuitem);
        bar.add(file_menu);
        options_menu = new JMenu("Options");
        options_menu.setMnemonic('O');
        configuration_menuitem = new JMenuItem("Configuration");
        configuration_menuitem.setAccelerator(KeyStroke.getKeyStroke('T', Toolkit.getDefaultToolkit().getMenuShortcutKeyMask(), false));
        configuration_menuitem.addActionListener(this);
        options_menu.add(configuration_menuitem);
        bar.add(options_menu);
    }

    private void initialize() {
        setSize(300, 200);
        setVisible(true);
        pack();
    }

    public static void main(String args[]) {
        try {
            GUI gui = new GUI();
            gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            int i = 0;
            for (String arg : args) {
                if (arg.equals("-l") && args.length >= i + 1) gui.loadGraphFile(new File(args[i + 1]));
                if (arg.equals("-c")) System.exit(0);
                i++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void actionPerformed(ActionEvent e) {
        JMenuItem source = (JMenuItem) (e.getSource());
        if (source.equals(configuration_menuitem)) {
            new ConfigurationDialog(this);
        } else if (source.equals(load_menuitem)) {
            JFileChooser fc = new JFileChooser();
            fc.setCurrentDirectory(graph_file);
            int returnVal = fc.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                loadGraphFile(fc.getSelectedFile());
            }
        } else if (source.equals(createdefault_menuitem)) {
            int devices = Integer.parseInt(JOptionPane.showInputDialog("Enter Number of devices", "8"));
            g.create(devices);
            graph_file = null;
            setTitle(PROGRAM_TITLE + ": Unsaved topology");
            tg.redraw();
            pack();
        } else if (source.equals(save_menuitem)) {
            if (graph_file == null) {
                chooseSaveFile();
            }
            saveTopology();
        } else if (source.equals(saveas_menuitem)) {
            chooseSaveFile();
            saveTopology();
        }
    }

    public void loadGraphFile(File file) {
        graph_file = file;
        g.loadLinkTopology(graph_file);
        setTitle(PROGRAM_TITLE + ": " + graph_file.getName());
        tg.redraw();
        pack();
    }

    private void saveTopology() {
        if (g.saveLinkTopology(graph_file)) {
            JOptionPane.showMessageDialog(this, "Topology saved.");
        } else {
            JOptionPane.showMessageDialog(this, "Unable to save topology.");
        }
    }

    private void chooseSaveFile() {
        JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(graph_file);
        int returnVal = fc.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            graph_file = fc.getSelectedFile();
            setTitle(PROGRAM_TITLE + ": " + graph_file.getName());
        }
    }
}
