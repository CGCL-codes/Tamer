package gnu.rhuelga.cirl;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.filechooser.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.lang.*;
import gnu.rhuelga.cirl.htmllib.*;
import gnu.rhuelga.cirl.cirllib.*;
import gnu.regexp.*;

class cirl implements ActionListener, MouseListener {

    JButton btnGo;

    JButton btnToDown;

    JButton btnRemove;

    JButton btnToTop;

    JToggleButton tbtnFilter;

    JTextField txtAddress;

    JComboBox cmbFilter;

    JTabbedPane tab;

    JTable tables[];

    JMenuItem mnuOpen;

    JMenuItem mnuSave;

    JMenuItem mnuQuit;

    JMenuItem mnuPreference;

    JMenuItem mnuAbout;

    JFrame frame;

    gnu.rhuelga.cirl.cirllib.DownControl control;

    static final String WINDOW_NAME = "CIRL";

    private static final String defaultAddress = "http://www.debian.org";

    static final String[] LIST_OF_FILTERS = { ".*(gif|GIF|jpeg|JPEG|jpg|JPG)", ".*(html|HTML|htm|HTM)" };

    public cirl() {
        Preference.setPreference();
        control = new gnu.rhuelga.cirl.cirllib.DownControl();
        frame = new JFrame(WINDOW_NAME);
        frame.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent we) {
                quit();
            }
        });
        frame.setSize(300, 300);
        Container content = frame.getContentPane();
        JPanel topPanel = new JPanel(new BorderLayout());
        btnGo = new JButton("Go");
        btnGo.addActionListener(this);
        topPanel.add(btnGo, BorderLayout.WEST);
        txtAddress = new JTextField();
        txtAddress.setText(defaultAddress);
        topPanel.add(txtAddress, BorderLayout.CENTER);
        txtAddress.setPreferredSize(new Dimension(400, 30));
        content.add(topPanel, BorderLayout.NORTH);
        tab = createTab();
        content.add(tab);
        JPanel botPanel = new JPanel(new BorderLayout());
        JPanel filPanel = new JPanel(new BorderLayout());
        tbtnFilter = new JToggleButton("Filter");
        tbtnFilter.addActionListener(this);
        filPanel.add(tbtnFilter, BorderLayout.WEST);
        cmbFilter = new JComboBox(LIST_OF_FILTERS);
        cmbFilter.setEditable(true);
        filPanel.add(cmbFilter, BorderLayout.CENTER);
        botPanel.add(filPanel, BorderLayout.NORTH);
        JComponent butPanel = new JPanel(new FlowLayout());
        btnToDown = new JButton("To Download");
        btnToDown.addActionListener(this);
        butPanel.add(btnToDown, FlowLayout.LEFT);
        btnRemove = new JButton("Remove");
        btnRemove.addActionListener(this);
        butPanel.add(btnRemove, FlowLayout.LEFT);
        btnToTop = new JButton("To Top");
        btnToTop.addActionListener(this);
        butPanel.add(btnToTop, FlowLayout.LEFT);
        botPanel.add(butPanel, BorderLayout.SOUTH);
        content.add(botPanel, BorderLayout.SOUTH);
        frame.setJMenuBar(createMenuBar());
        frame.pack();
        frame.setVisible(true);
        control.setPriority(Thread.currentThread().getPriority() - 2);
        control.start();
    }

    JTable setUpTable(JTabbedPane tab, String name, ResourceList resl) {
        JTable table = new JTable(resl);
        ResourceList.setUpColumns(table);
        tab.addTab(name, null, new JScrollPane(table));
        return table;
    }

    JTabbedPane createTab() {
        JTabbedPane t = new JTabbedPane();
        tables = new JTable[5];
        tables[0] = setUpTable(t, "downloading", control.downloading);
        tables[1] = setUpTable(t, "to download", control.toDownload);
        tables[2] = setUpTable(t, "downloaded", control.downloaded);
        tables[3] = setUpTable(t, "not downloaded", control.notDownloaded);
        tables[4] = setUpTable(t, "new downloads", control.newDownloads);
        return t;
    }

    JMenuBar createMenuBar() {
        JMenuBar menubar = new JMenuBar();
        JMenu menu = new JMenu("Archives");
        mnuOpen = new JMenuItem("Open");
        mnuOpen.addActionListener(this);
        menu.add(mnuOpen);
        mnuSave = new JMenuItem("Save");
        mnuSave.addActionListener(this);
        menu.add(mnuSave);
        menu.addSeparator();
        mnuQuit = new JMenuItem("Quit");
        mnuQuit.addActionListener(this);
        menu.add(mnuQuit);
        menubar.add(menu);
        menu = new JMenu("Edit");
        mnuPreference = new JMenuItem("Preference");
        mnuPreference.addActionListener(this);
        menu.add(mnuPreference);
        menubar.add(menu);
        menu = new JMenu("Help");
        mnuAbout = new JMenuItem("About");
        mnuAbout.addActionListener(this);
        menu.add(mnuAbout);
        menubar.add(menu);
        return menubar;
    }

    protected Component makeTextPanel(String text) {
        JPanel panel = new JPanel(false);
        JLabel filler = new JLabel(text);
        filler.setHorizontalAlignment(JLabel.CENTER);
        panel.setLayout(new GridLayout(1, 1));
        panel.add(filler);
        return panel;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnGo) {
            goAddress();
        }
        if (e.getSource() == btnToDown) {
            moveResourceToDownload();
        }
        if (e.getSource() == btnRemove) {
            removeResources();
        }
        if (e.getSource() == btnToTop) {
            moveResourceToTop();
        }
        if (e.getSource() == mnuOpen) {
            openFile();
        }
        if (e.getSource() == mnuSave) {
            saveFile();
        }
        if (e.getSource() == mnuQuit) {
            quit();
        }
        if (e.getSource() == mnuAbout) {
            about();
        }
        if (e.getSource() == mnuPreference) {
            Preference.showDialog(frame);
        }
        if (e.getSource() == tbtnFilter) {
            if (tbtnFilter.isSelected()) {
                try {
                    setFilter((String) cmbFilter.getSelectedItem());
                } catch (Exception excep) {
                    System.out.println("Error la expresion no es valida");
                    System.out.println(excep);
                }
            } else {
                removeFilter();
            }
        }
    }

    public void mouseClicked(MouseEvent e) {
        if ((e.getModifiers() & InputEvent.BUTTON3_MASK) == InputEvent.BUTTON3_MASK) {
            if (e.getSource() == tables[0]) {
                Resource r;
                r = ((ResourceList) (tables[0].getModel())).getResourceAt(tables[0].getSelectedRow());
                System.out.println("filename :" + r.getFileName());
                System.out.println("status: " + r.getStatus());
                System.out.println("url: " + r.getURL());
            }
        }
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    synchronized Collection getSelection() {
        Collection col;
        gnu.rhuelga.cirl.cirllib.ResourceList rl;
        int[] sel;
        sel = tables[tab.getSelectedIndex()].getSelectedRows();
        rl = (gnu.rhuelga.cirl.cirllib.ResourceList) tables[tab.getSelectedIndex()].getModel();
        col = rl.remove(sel);
        tables[tab.getSelectedIndex()].clearSelection();
        return col;
    }

    synchronized void removeResources() {
        getSelection();
    }

    synchronized void moveResourceToDownload() {
        control.download(getSelection());
    }

    synchronized void moveResourceToTop() {
        Collection col;
        gnu.rhuelga.cirl.cirllib.ResourceList rl;
        col = getSelection();
        rl = (gnu.rhuelga.cirl.cirllib.ResourceList) tables[tab.getSelectedIndex()].getModel();
        rl.pushOnTop(col);
    }

    public static void main(String[] argv) {
        cirl t = new cirl();
        if (!t.existREClass()) {
            t.disableFilter();
        }
    }

    void goAddress() {
        URL url;
        String address;
        try {
            url = new URL(txtAddress.getText());
            control.download(url);
            return;
        } catch (IOException e) {
            System.out.println("Error: opening the socket");
            System.out.println(e);
        }
    }

    void openFile() {
        JFileChooser chooser = new JFileChooser();
        int result = chooser.showOpenDialog(mnuOpen);
        if (result == JFileChooser.CANCEL_OPTION) return;
        try {
            FileInputStream filein = new FileInputStream(chooser.getSelectedFile());
            ObjectInputStream in = new ObjectInputStream(filein);
            control = (gnu.rhuelga.cirl.cirllib.DownControl) in.readObject();
            frame.getContentPane().remove(tab);
            tab = createTab();
            frame.getContentPane().add(tab);
            tab.setVisible(false);
            frame.repaint();
            control.start();
            control.reactiveDownloadings();
            removeFilter();
            tab.setVisible(true);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void saveFile() {
        int result;
        JFileChooser chooser = new JFileChooser();
        javax.swing.filechooser.FileFilter f = new javax.swing.filechooser.FileFilter() {

            public boolean accept(File f) {
                if (f.getName().endsWith(".cirl")) return true;
                return false;
            }

            public String getDescription() {
                return "CIRL file";
            }
        };
        result = chooser.showSaveDialog(mnuSave);
        if (result == JFileChooser.CANCEL_OPTION) return;
        try {
            FileOutputStream fileout = new FileOutputStream(chooser.getSelectedFile());
            ObjectOutputStream out = new ObjectOutputStream(fileout);
            out.writeObject(control);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    void about() {
        JOptionPane.showMessageDialog(frame, "CIRL, is cat internet | relative link > \n" + "Version 0.5\n\n" + "Author: Roberto Huelga Diaz \n\n" + "http://cirl.sourceforge.net\n\n" + "cirl is free software; you can redistribute it and/or modify\n" + "it under the terms of the GNU General Public License as published by\n" + "the Free Software Foundation; either version 2 of the License or\n" + "(at your option) any later version.\n\n" + "View the COPYING file for more info");
    }

    void setFilter(String strFilter) throws Exception {
        gnu.regexp.RE reg = new gnu.regexp.RE(strFilter);
        System.out.println(" setting filter: " + strFilter);
        control.setFilter(reg);
    }

    void removeFilter() {
        control.removeFilter();
        tbtnFilter.setSelected(false);
    }

    void quit() {
        control.finish();
        System.exit(0);
    }

    boolean existREClass() {
        Class c;
        try {
            c = Class.forName("gnu.regexp.RE");
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    void disableFilter() {
        tbtnFilter.setEnabled(false);
        JOptionPane.showMessageDialog(frame, "I can't locate the gnu.regexp library, \n" + "to use the filter option, it must be \n" + "installed, and in the classpath, please \n" + "download it from \n" + "http://www.cacas.org/java/gnu/regexp/");
    }
}
