package net.sourceforge.keepassj2me.packer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.net.URL;
import java.util.Enumeration;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

/**
 * MainWindow - GUI for packing custom KeePassJ2ME midlet
 * 
 * @author Stepan Strelets
 *
 */
public class MainWindow extends JFrame implements ActionListener, WindowListener, ItemListener {

    private static final long serialVersionUID = -62505571827133047L;

    private JTextField srcJar = null;

    private JButton srcJarBrowse = null;

    private JList srcKdb = null;

    private DefaultListModel srcKdbModel = null;

    private JButton srcKdbAdd = null;

    private JButton srcKdbRemove = null;

    private String srcKbdLastDir = null;

    private JTextField dstJar = null;

    private JButton dstJarBrowse = null;

    private JCheckBox resPackEnable = null;

    private JComboBox resIconsPackName = null;

    private JComboBox resLogoPackName = null;

    private JButton info = null;

    private JButton ok = null;

    private JButton cancel = null;

    private Config conf = null;

    MainWindow() {
        super();
        int x = 0, y = 0;
        this.setIconImage(getImage("icon.png"));
        this.setTitle("KeePass J2ME Packer");
        this.setSize(600, 440);
        this.setMinimumSize(new Dimension(440, 320));
        this.setLocationRelativeTo(null);
        this.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(this);
        x = 0;
        y = 0;
        JLabel caption = new JLabel("Source JAR - KeePassJ2ME Midlet");
        caption.setIcon(new ImageIcon(getImage("compress.png")));
        constraints.anchor = GridBagConstraints.WEST;
        constraints.fill = GridBagConstraints.NONE;
        constraints.gridheight = 1;
        constraints.gridwidth = 2;
        constraints.gridx = x;
        constraints.gridy = y;
        constraints.insets = new Insets(20, 20, 0, 20);
        constraints.weightx = 0;
        constraints.weighty = 0;
        this.add(caption, constraints);
        srcJar = new JTextField("");
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridheight = 1;
        constraints.gridwidth = 1;
        constraints.gridx = x;
        constraints.gridy = y + 1;
        constraints.insets = new Insets(5, 20, 5, 5);
        constraints.weightx = 1;
        constraints.weighty = 0;
        this.add(srcJar, constraints);
        srcJarBrowse = new JButton("Browse");
        srcJarBrowse.setHorizontalAlignment(JButton.LEFT);
        srcJarBrowse.setIcon(new ImageIcon(getImage("folder.png")));
        srcJarBrowse.addActionListener(this);
        constraints.anchor = GridBagConstraints.WEST;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridheight = 1;
        constraints.gridwidth = 1;
        constraints.gridx = x + 1;
        constraints.gridy = y + 1;
        constraints.insets = new Insets(5, 5, 5, 20);
        constraints.weightx = 0;
        constraints.weighty = 0;
        this.add(srcJarBrowse, constraints);
        y = 2;
        JLabel caption2 = new JLabel("Source KDB - KeePass Key Databases (and key files)");
        caption2.setIcon(new ImageIcon(getImage("database.png")));
        constraints.anchor = GridBagConstraints.WEST;
        constraints.fill = GridBagConstraints.NONE;
        constraints.gridheight = 1;
        constraints.gridwidth = 2;
        constraints.gridx = x;
        constraints.gridy = y;
        constraints.insets = new Insets(5, 20, 0, 20);
        constraints.weightx = 0;
        constraints.weighty = 0;
        this.add(caption2, constraints);
        srcKdbModel = new DefaultListModel();
        srcKdb = new JList(srcKdbModel);
        srcKdb.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        srcKdb.setLayoutOrientation(JList.VERTICAL);
        srcKdb.setVisibleRowCount(-1);
        JScrollPane listScroller = new JScrollPane(srcKdb);
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridheight = 2;
        constraints.gridwidth = 1;
        constraints.gridx = x;
        constraints.gridy = y + 1;
        constraints.insets = new Insets(5, 20, 5, 5);
        constraints.weightx = 1;
        constraints.weighty = 1;
        this.add(listScroller, constraints);
        srcKdbAdd = new JButton("Add");
        srcKdbAdd.setHorizontalAlignment(JButton.LEFT);
        srcKdbAdd.setIcon(new ImageIcon(getImage("add.png")));
        srcKdbAdd.addActionListener(this);
        constraints.anchor = GridBagConstraints.SOUTHWEST;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridheight = 1;
        constraints.gridwidth = 1;
        constraints.gridx = x + 1;
        constraints.gridy = y + 1;
        constraints.insets = new Insets(5, 5, 5, 20);
        constraints.weightx = 0;
        constraints.weighty = 0;
        this.add(srcKdbAdd, constraints);
        srcKdbRemove = new JButton("Remove");
        srcKdbRemove.setHorizontalAlignment(JButton.LEFT);
        srcKdbRemove.setIcon(new ImageIcon(getImage("delete.png")));
        srcKdbRemove.addActionListener(this);
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridheight = 1;
        constraints.gridwidth = 1;
        constraints.gridx = x + 1;
        constraints.gridy = y + 2;
        constraints.insets = new Insets(5, 5, 5, 20);
        constraints.weightx = 0;
        constraints.weighty = 0;
        this.add(srcKdbRemove, constraints);
        y = 5;
        JLabel caption3 = new JLabel("Target JAR - KeePassJ2ME Midlet with KDB");
        caption3.setIcon(new ImageIcon(getImage("compress.png")));
        constraints.anchor = GridBagConstraints.WEST;
        constraints.fill = GridBagConstraints.NONE;
        constraints.gridheight = 1;
        constraints.gridwidth = 2;
        constraints.gridx = x;
        constraints.gridy = y;
        constraints.insets = new Insets(5, 20, 0, 20);
        constraints.weightx = 0;
        constraints.weighty = 0;
        this.add(caption3, constraints);
        dstJar = new JTextField("");
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridheight = 1;
        constraints.gridwidth = 1;
        constraints.gridx = x;
        constraints.gridy = y + 1;
        constraints.insets = new Insets(5, 20, 5, 5);
        constraints.weightx = 1;
        constraints.weighty = 0;
        this.add(dstJar, constraints);
        dstJarBrowse = new JButton("Browse");
        dstJarBrowse.setHorizontalAlignment(JButton.LEFT);
        dstJarBrowse.setIcon(new ImageIcon(getImage("folder.png")));
        dstJarBrowse.addActionListener(this);
        constraints.anchor = GridBagConstraints.WEST;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridheight = 1;
        constraints.gridwidth = 1;
        constraints.gridx = x + 1;
        constraints.gridy = y + 1;
        constraints.insets = new Insets(5, 5, 5, 20);
        constraints.weightx = 0;
        constraints.weighty = 0;
        this.add(dstJarBrowse, constraints);
        x = 0;
        y = 7;
        JLabel caption4 = new JLabel("Resource pack");
        caption4.setIcon(new ImageIcon(getImage("compress.png")));
        constraints.anchor = GridBagConstraints.WEST;
        constraints.fill = GridBagConstraints.NONE;
        constraints.gridheight = 1;
        constraints.gridwidth = 2;
        constraints.gridx = x;
        constraints.gridy = y;
        constraints.insets = new Insets(5, 20, 0, 20);
        constraints.weightx = 0;
        constraints.weighty = 0;
        this.add(caption4, constraints);
        JPanel resPack = new JPanel();
        resPack.setLayout(new FlowLayout(FlowLayout.LEFT));
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridheight = 1;
        constraints.gridwidth = 1;
        constraints.gridx = x;
        constraints.gridy = y + 1;
        constraints.insets = new Insets(5, 20, 5, 5);
        constraints.weightx = 1;
        constraints.weighty = 0;
        this.add(resPack, constraints);
        resPackEnable = new JCheckBox("Enable");
        resPackEnable.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent event) {
                if (event.getStateChange() == ItemEvent.SELECTED) {
                    resIconsPackName.setEnabled(true);
                    resLogoPackName.setEnabled(true);
                    conf.setResourcesPackEnable(true);
                    conf.setIconsPackName(resIconsPackName.getSelectedItem().toString());
                    conf.setLogoPackName(resLogoPackName.getSelectedItem().toString());
                } else {
                    resIconsPackName.setEnabled(false);
                    resLogoPackName.setEnabled(false);
                    conf.setResourcesPackEnable(false);
                }
            }
        });
        resPack.add(resPackEnable);
        JLabel caption41 = new JLabel("Icons:");
        resPack.add(caption41);
        resIconsPackName = new JComboBox();
        resIconsPackName.setEditable(false);
        resIconsPackName.setEnabled(false);
        resIconsPackName.addItem("16x16");
        resIconsPackName.addItem("22x22");
        resIconsPackName.addItem("32x32");
        resIconsPackName.addItem("48x48");
        resIconsPackName.addItemListener(this);
        resPack.add(resIconsPackName);
        JLabel caption42 = new JLabel("Logo:");
        resPack.add(caption42);
        resLogoPackName = new JComboBox();
        resLogoPackName.setEditable(false);
        resLogoPackName.setEnabled(false);
        resLogoPackName.addItem("16x16");
        resLogoPackName.addItem("22x22");
        resLogoPackName.addItem("32x32");
        resLogoPackName.addItem("48x48");
        resLogoPackName.addItem("64x64");
        resLogoPackName.addItemListener(this);
        resPack.add(resLogoPackName);
        x = 0;
        y = 9;
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        JPanel lpanel = new JPanel();
        FlowLayout llayout = new FlowLayout();
        llayout.setHgap(0);
        llayout.setVgap(0);
        lpanel.setLayout(llayout);
        info = new JButton("About");
        info.setHorizontalAlignment(JButton.LEFT);
        info.setIcon(new ImageIcon(getImage("information.png")));
        info.addActionListener(this);
        lpanel.add(info);
        JPanel rpanel = new JPanel();
        FlowLayout rlayout = new FlowLayout();
        rlayout.setHgap(0);
        rlayout.setVgap(0);
        rpanel.setLayout(rlayout);
        ok = new JButton("Pack");
        ok.setHorizontalAlignment(JButton.LEFT);
        ok.setIcon(new ImageIcon(getImage("tick.png")));
        ok.addActionListener(this);
        rpanel.add(ok);
        cancel = new JButton("Exit");
        cancel.setHorizontalAlignment(JButton.LEFT);
        cancel.setIcon(new ImageIcon(getImage("cancel.png")));
        cancel.addActionListener(this);
        rpanel.add(cancel);
        panel.add(lpanel, BorderLayout.WEST);
        panel.add(rpanel, BorderLayout.EAST);
        constraints.anchor = GridBagConstraints.EAST;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridheight = 1;
        constraints.gridwidth = 3;
        constraints.gridx = x;
        constraints.gridy = y;
        constraints.insets = new Insets(20, 20, 20, 20);
        constraints.weightx = 1;
        constraints.weighty = 0;
        this.add(panel, constraints);
        cancel.requestFocusInWindow();
        conf = new Config(null);
        if (conf.Load()) {
            srcJar.setText(conf.getSourceJar());
            String kdb;
            int i = 0;
            while ((kdb = conf.getSourceKdb(i++)) != null) srcKdbModel.addElement(kdb);
            dstJar.setText(conf.getTargetJar());
            srcKbdLastDir = conf.getKdbLastDir();
            String iconsPackName = conf.getIconsPackName();
            resIconsPackName.setSelectedItem(iconsPackName);
            String logoPackName = conf.getLogoPackName();
            resLogoPackName.setSelectedItem(logoPackName);
            boolean resourcePackEnabled = conf.getResourcesPackEnable();
            if (resourcePackEnabled) resPackEnable.setSelected(true);
        }
        ;
    }

    public Image getImage(String name) {
        URL url = getClass().getResource("/res/images/" + name);
        if (url != null) return Toolkit.getDefaultToolkit().getImage(url); else return Toolkit.getDefaultToolkit().getImage("res/images/" + name);
    }

    void exit() {
        this.setVisible(false);
        getConfig().Save();
        System.exit(0);
    }

    private boolean inSrcKdb(String path) {
        for (Enumeration<?> e = srcKdbModel.elements(); e.hasMoreElements(); ) {
            if (e.nextElement().toString().compareTo(path) == 0) return true;
        }
        ;
        return false;
    }

    public Config getConfig() {
        conf.setSourceJar(srcJar.getText());
        conf.clearSourceKdb();
        int i = 0;
        for (Enumeration<?> e = srcKdbModel.elements(); e.hasMoreElements(); ) {
            conf.setSourceKdb(i++, e.nextElement().toString());
        }
        ;
        conf.setTargetJar(dstJar.getText());
        if (srcKbdLastDir != null) conf.setKdbLastDir(srcKbdLastDir);
        return conf;
    }

    public void actionPerformed(ActionEvent arg0) {
        JButton button = (JButton) arg0.getSource();
        if (button == srcJarBrowse) {
            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("Open source Midlet");
            File f = new File(srcJar.getText());
            fc.setCurrentDirectory(f.getParentFile());
            fc.addChoosableFileFilter(new FilterExt("jar", "Midlet (*.jar)"));
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                srcJar.setText(fc.getSelectedFile().getAbsolutePath());
            }
            ;
        } else if (button == srcKdbAdd) {
            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("Open source KDB");
            fc.setMultiSelectionEnabled(true);
            if (srcKbdLastDir != null) fc.setCurrentDirectory(new File(srcKbdLastDir));
            fc.addChoosableFileFilter(new FilterExt("kdb", "KeePass Key Database (*.kdb)"));
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                File[] files = fc.getSelectedFiles();
                int i;
                for (i = 0; i < files.length; ++i) {
                    if (i == 0) srcKbdLastDir = files[i].getParent();
                    if (!inSrcKdb(files[i].getAbsolutePath())) srcKdbModel.addElement(files[i].getAbsolutePath());
                }
                ;
            }
            ;
        } else if (button == srcKdbRemove) {
            int sel[] = srcKdb.getSelectedIndices();
            for (int i = sel.length - 1; i >= 0; --i) {
                srcKdbModel.remove(sel[i]);
            }
            ;
        } else if (button == dstJarBrowse) {
            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("Save taget Midlet");
            File f = new File(dstJar.getText());
            fc.setCurrentDirectory(f.getParentFile());
            fc.addChoosableFileFilter(new FilterExt("jar", "Midlet (*.jar)"));
            if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                String path = fc.getSelectedFile().getAbsolutePath();
                if (!path.substring(path.length() - 4).equalsIgnoreCase(".jar")) path += ".jar";
                dstJar.setText(path);
            }
            ;
        } else if (button == info) {
            JOptionPane.showMessageDialog(this, "Version: 1.3.1\r\n\r\n" + "Project page:\r\nhttp://keepassj2me.sourceforge.net/\r\n\r\n" + "License:\r\n" + "GNU GPL v2 http://www.gnu.org/licenses/gpl-2.0.html\r\n\r\n" + "Authors (In alphabetic order):\r\n" + "Naomaru Itoi\r\n" + "Stepan Strelets\r\n\r\n" + "Thanks to:\r\n" + "Mark James (silk icons) http://famfamfam.com\r\n\r\n" + "KeePass J2ME Packer comes with ABSOLUTELY NO WARRANTY.\r\n" + "This is free software, and you are welcome to redistribute it\r\n" + "under certain conditions; for details visit:\r\n" + "http://www.gnu.org/licenses/gpl-2.0.html", "About KeePass J2ME Packer", JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getImage("logo.png")));
        } else if (button == ok) {
            MidletPacker packer = new MidletPacker(getConfig());
            try {
                packer.pack();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (button == cancel) {
            this.exit();
        }
    }

    public void windowActivated(WindowEvent e) {
    }

    public void windowClosed(WindowEvent e) {
    }

    public void windowClosing(WindowEvent e) {
        this.exit();
    }

    public void windowDeactivated(WindowEvent e) {
    }

    public void windowDeiconified(WindowEvent e) {
    }

    public void windowIconified(WindowEvent e) {
    }

    public void windowOpened(WindowEvent e) {
    }

    public void itemStateChanged(ItemEvent event) {
        JComboBox box = (JComboBox) event.getSource();
        if (box == resIconsPackName) {
            conf.setIconsPackName(event.getItem().toString());
        } else if (box == resLogoPackName) {
            conf.setLogoPackName(event.getItem().toString());
        }
    }
}
