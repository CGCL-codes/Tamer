package de.kout.wlFxp.view.queue;

import de.kout.wlFxp.Configuration;
import de.kout.wlFxp.Transfer;
import de.kout.wlFxp.wlFxp;
import de.kout.wlFxp.ftp.FtpServer;
import de.kout.wlFxp.ftp.Site;
import de.kout.wlFxp.view.custom.MyTextField;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Panel;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Enumeration;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * Edit Dialog for the queueList
 *
 * @author Alexander Kout
 *
 * 4. Oktober 2003
 */
public class QueueEditDialog extends JDialog implements ActionListener, KeyListener, ItemListener {

    QueueList q;

    MyTextField sName;

    MyTextField sPath;

    MyTextField sServer;

    MyTextField tName;

    MyTextField tPath;

    MyTextField tServer;

    int[] rows;

    FtpServer tmp_sServer;

    FtpServer tmp_tServer;

    Configuration cfg;

    /**
         * Constructor for the QueueEditDialog object
         * 
         * @param q
         *            Description of the Parameter
         * @param rows
         *            Description of the Parameter
         */
    public QueueEditDialog(QueueList q, int[] rows) {
        super(q.frame, "Edit", true);
        this.q = q;
        cfg = wlFxp.getConfig();
        if (rows.length == 0) {
            exit();
            return;
        }
        this.rows = rows;
        JTabbedPane tpane = new JTabbedPane();
        tpane.addKeyListener(this);
        tpane.add("Source", buildSource());
        tpane.add("Target", buildTarget());
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(tpane, BorderLayout.CENTER);
        JButton ok = new JButton("Ok");
        ok.addActionListener(this);
        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(this);
        Panel p1 = new Panel();
        p1.setLayout(new FlowLayout(FlowLayout.RIGHT));
        p1.add(ok);
        p1.add(cancel);
        getContentPane().add(p1, BorderLayout.SOUTH);
        loadValues();
        setLocationRelativeTo(q.frame);
        pack();
        Point p = getLocation();
        p.setLocation(p.getX() - (getWidth() / 2), p.getY() - (getHeight() / 2));
        setLocation(p);
        setVisible(true);
    }

    /**
	 * loads the values / checks for multiple Selected
	 */
    public void loadValues() {
        Transfer t;
        for (int i = 0; i < rows.length; i++) {
            t = q.elementAt(rows[i]);
            if (sName.getText().equals("") || sName.getText().equals(t.getSource().getName())) {
                sName.setText(t.getSource().getName());
            } else {
                sName.setText("multiple Selected");
                sName.setEnabled(false);
            }
            if (sPath.getText().equals("") || sPath.getText().equals(t.getSource().getParent())) {
                sPath.setText(t.getSource().getParent());
            } else {
                sPath.setText("multiple Selected");
                sPath.setEnabled(false);
            }
            if (t.getSourceServer() != null) {
                if (sServer.getText().equals("") || sServer.getText().equals(t.getSourceServer().toString())) {
                    sServer.setText(t.getSourceServer().toString());
                    tmp_sServer = new FtpServer(t.getSourceServer().toStringLong(), true);
                } else {
                    sServer.setText("multiple Selected");
                    tmp_sServer = null;
                }
            } else {
                sServer.setText("local");
            }
            if (tName.getText().equals("") || tName.getText().equals(t.getDest().getName())) {
                tName.setText(t.getDest().getName());
            } else {
                tName.setText("multiple Selected");
                tName.setEnabled(false);
            }
            if (tPath.getText().equals("") || tPath.getText().equals(t.getDest().getParent())) {
                tPath.setText(t.getDest().getParent());
            } else {
                tPath.setText("multiple Selected");
                tPath.setEnabled(false);
            }
            if (t.getDestServer() != null) {
                if (tServer.getText().equals("") || tServer.getText().equals(t.getDestServer().toString())) {
                    tServer.setText(t.getDestServer().toString());
                    tmp_tServer = new FtpServer(t.getDestServer().toStringLong(), true);
                } else {
                    tServer.setText("multiple Selected");
                    tmp_tServer = null;
                }
            } else {
                tServer.setText("local");
            }
        }
    }

    /**
	 * Description of the Method
	 *
	 * @return Description of the Return Value
	 */
    public JPanel buildSource() {
        JLabel label;
        JPanel pane = new JPanel();
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc;
        pane.setLayout(gbl);
        label = new JLabel("name: ");
        label.setHorizontalAlignment(JLabel.RIGHT);
        gbc = makegbc(0, 0, 1, 1);
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0;
        gbl.setConstraints(label, gbc);
        pane.add(label);
        sName = new MyTextField();
        sName.addKeyListener(this);
        gbc = makegbc(1, 0, 1, 1);
        gbl.setConstraints(sName, gbc);
        pane.add(sName);
        label = new JLabel("path: ");
        label.setHorizontalAlignment(JLabel.RIGHT);
        gbc = makegbc(0, 1, 1, 1);
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0;
        gbl.setConstraints(label, gbc);
        pane.add(label);
        sPath = new MyTextField();
        sPath.addKeyListener(this);
        gbc = makegbc(1, 1, 1, 1);
        gbl.setConstraints(sPath, gbc);
        pane.add(sPath);
        label = new JLabel("server: ");
        label.setHorizontalAlignment(JLabel.RIGHT);
        gbc = makegbc(0, 2, 1, 1);
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0;
        gbl.setConstraints(label, gbc);
        pane.add(label);
        sServer = new MyTextField(20);
        sServer.setEnabled(false);
        sServer.addKeyListener(this);
        gbc = makegbc(1, 2, 1, 1);
        gbl.setConstraints(sServer, gbc);
        pane.add(sServer);
        JButton b = new JButton("...");
        b.setActionCommand("changeSourceServer");
        b.addActionListener(this);
        b.setMaximumSize(new Dimension(20, 10));
        gbc = makegbc(2, 2, 1, 1);
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbl.setConstraints(b, gbc);
        pane.add(b);
        JPanel p = new JPanel();
        gbc = makegbc(0, 3, 2, 1);
        gbc.weighty = 100;
        gbl.setConstraints(p, gbc);
        pane.add(p);
        return pane;
    }

    /**
	 * Description of the Method
	 *
	 * @return Description of the Return Value
	 */
    public JPanel buildTarget() {
        JLabel label;
        JPanel pane = new JPanel();
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc;
        pane.setLayout(gbl);
        label = new JLabel("name: ");
        label.setHorizontalAlignment(JLabel.RIGHT);
        gbc = makegbc(0, 0, 1, 1);
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0;
        gbl.setConstraints(label, gbc);
        pane.add(label);
        tName = new MyTextField();
        tName.addKeyListener(this);
        gbc = makegbc(1, 0, 1, 1);
        gbl.setConstraints(tName, gbc);
        pane.add(tName);
        label = new JLabel("path: ");
        label.setHorizontalAlignment(JLabel.RIGHT);
        gbc = makegbc(0, 1, 1, 1);
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0;
        gbl.setConstraints(label, gbc);
        pane.add(label);
        tPath = new MyTextField();
        tPath.addKeyListener(this);
        gbc = makegbc(1, 1, 1, 1);
        gbl.setConstraints(tPath, gbc);
        pane.add(tPath);
        label = new JLabel("server: ");
        label.setHorizontalAlignment(JLabel.RIGHT);
        gbc = makegbc(0, 2, 1, 1);
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.weightx = 0;
        gbl.setConstraints(label, gbc);
        pane.add(label);
        tServer = new MyTextField(20);
        tServer.setEnabled(false);
        tServer.addKeyListener(this);
        gbc = makegbc(1, 2, 1, 1);
        gbl.setConstraints(tServer, gbc);
        pane.add(tServer);
        JButton b = new JButton("...");
        b.setActionCommand("changeTargetServer");
        b.addActionListener(this);
        b.setMaximumSize(new Dimension(20, 10));
        gbc = makegbc(2, 2, 1, 1);
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;
        gbl.setConstraints(b, gbc);
        pane.add(b);
        JPanel p = new JPanel();
        gbc = makegbc(0, 3, 2, 1);
        gbc.weighty = 100;
        gbl.setConstraints(p, gbc);
        pane.add(p);
        return pane;
    }

    /**
	 * Description of the Method
	 *
	 * @param s Description of the Parameter
	 *
	 * @return Description of the Return Value
	 */
    public JPopupMenu buildServerMenu(String s) {
        JPopupMenu pm = new JPopupMenu();
        JMenu m = new JMenu("sites");
        pm.add(m);
        JMenuItem mi;
        JMenu mm;
        Enumeration e = cfg.getSitesRoot().breadthFirstEnumeration();
        int k = 0;
        while (e.hasMoreElements()) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.nextElement();
            if (!node.getAllowsChildren()) {
                mi = new JMenuItem(((Site) node.getUserObject()).toString());
                mi.addActionListener(this);
                mi.setActionCommand("sites" + s + k);
                if (node.getParent() == cfg.getSitesRoot()) {
                    m.add(mi);
                } else if (node.getParent() != null) {
                    ((Site) ((DefaultMutableTreeNode) node.getParent()).getUserObject()).getMenu().add(mi);
                }
            } else {
                mm = ((Site) node.getUserObject()).getMenu();
                mm.removeAll();
                SwingUtilities.updateComponentTreeUI(mm);
                if (node.getParent() == cfg.getSitesRoot()) {
                    m.add(mm);
                } else if (node.getParent() != null) {
                    ((Site) ((DefaultMutableTreeNode) node.getParent()).getUserObject()).getMenu().add(mm);
                }
            }
            k++;
        }
        m = new JMenu("history");
        pm.add(m);
        for (int i = 0; i < cfg.getHistory().size(); i++) {
            mi = new JMenuItem(((FtpServer) cfg.getHistory().elementAt(i)).toString());
            mi.addActionListener(this);
            mi.setActionCommand("history" + s + i);
            m.add(mi);
        }
        return pm;
    }

    /**
	 * Description of the Method
	 */
    public void write() {
        Transfer transfer;
        for (int i = 0; i < rows.length; i++) {
            transfer = q.elementAt(rows[i]);
            q.removeElementAt(rows[i]);
            if (!sName.getText().equals("multiple Selected")) {
                transfer.getSource().setName(sName.getText());
            }
            if (!tName.getText().equals("multiple Selected")) {
                transfer.getDest().setName(tName.getText());
            }
            if (!sPath.getText().equals("multiple Selected")) {
                transfer.getSource().setParent(sPath.getText());
            }
            if (!tPath.getText().equals("multiple Selected")) {
                transfer.getDest().setParent(tPath.getText());
            }
            if (tmp_sServer != null) {
                transfer.setSourceServer(tmp_sServer);
            }
            if (tmp_tServer != null) {
                transfer.setDestServer(tmp_tServer);
            }
            q.insertElementAt(transfer, rows[i]);
        }
        exit();
    }

    /**
	 * disposes the Dialog
	 */
    private void exit() {
        setVisible(false);
        dispose();
    }

    /**
	 * the method that is called by the ActionListener
	 *
	 * @param e the ActionEvent
	 */
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd.equals("Ok")) {
            write();
        } else if (cmd.equals("Cancel")) {
            exit();
        } else if (cmd.equals("changeSourceServer")) {
            buildServerMenu("s").show((JButton) e.getSource(), 0, ((JButton) e.getSource()).getHeight());
        } else if (cmd.equals("changeTargetServer")) {
            buildServerMenu("t").show((JButton) e.getSource(), 0, ((JButton) e.getSource()).getHeight());
        } else if (cmd.startsWith("history")) {
            if (cmd.startsWith("historys") && !sServer.getText().equals("local") && !sServer.getText().equals("multiple Selected")) {
                tmp_sServer = (FtpServer) cfg.getHistory().elementAt(Integer.parseInt(cmd.substring(8, 9)));
                sServer.setText(tmp_sServer.toString());
            } else if (!tServer.getText().equals("local") && !tServer.getText().equals("multiple Selected")) {
                tmp_tServer = (FtpServer) cfg.getHistory().elementAt(Integer.parseInt(cmd.substring(8, 9)));
                tServer.setText(tmp_tServer.toString());
            }
        } else if (cmd.startsWith("sites")) {
            if (cmd.startsWith("sitess") && !sServer.getText().equals("local") && !sServer.getText().equals("multiple Selected")) {
                tmp_sServer = (FtpServer) getSiteFromRoot(Integer.parseInt(cmd.substring(6, 7))).getServer();
                sServer.setText(tmp_sServer.toString());
            } else if (!tServer.getText().equals("local") && !tServer.getText().equals("multiple Selected")) {
                tmp_tServer = (FtpServer) getSiteFromRoot(Integer.parseInt(cmd.substring(6, 7))).getServer();
                tServer.setText(tmp_tServer.toString());
            }
        }
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @param pos DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
    private Site getSiteFromRoot(int pos) {
        Enumeration e = cfg.getSitesRoot().breadthFirstEnumeration();
        int i = 0;
        while (e.hasMoreElements()) {
            if (i == pos) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.nextElement();
                return (Site) node.getUserObject();
            }
            e.nextElement();
            i++;
        }
        return null;
    }

    /**
	 * this method is called by the KeyListener
	 *
	 * @param e the KeyEvent
	 */
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            write();
        } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            exit();
        }
    }

    /**
	 * another mothod of the KeyListener
	 *
	 * @param event KeyEvent
	 */
    public void keyReleased(KeyEvent event) {
    }

    /**
	 * another mothod of the KeyListener
	 *
	 * @param event KeyEvent
	 */
    public void keyTyped(KeyEvent event) {
    }

    /**
	 * the method of the ItemListener
	 *
	 * @param e ItemEvent
	 */
    public void itemStateChanged(ItemEvent e) {
        Object source = e.getItemSelectable();
        if (source == null) {
        }
    }

    /**
	 * method to help making GridBagConstraints
	 *
	 * @param x gridx
	 * @param y gridy
	 * @param width gridwidth
	 * @param height gridheight
	 *
	 * @return returns the GridBagConstraints
	 */
    private GridBagConstraints makegbc(int x, int y, int width, int height) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = width;
        gbc.gridheight = height;
        gbc.weightx = 100;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(1, 1, 1, 1);
        return gbc;
    }
}
