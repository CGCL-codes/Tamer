package de.denkselbst.sentrick.sbeditor.fileselection;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class FileList extends JPanel {

    private static final long serialVersionUID = 1L;

    private List<FileSelectionListener> listeners = new CopyOnWriteArrayList<FileSelectionListener>();

    private File currentDir;

    private File lastSelected;

    private JList listing;

    private JLabel dir;

    public FileList() {
        super(new BorderLayout());
        dir = new JLabel();
        add(dir, BorderLayout.NORTH);
        listing = new JList();
        listing.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(listing), BorderLayout.CENTER);
        currentDir = new File(".").getAbsoluteFile();
        populateCurrentDir();
        listing.addMouseListener(new MouseListener() {

            public void mouseReleased(MouseEvent e) {
            }

            public void mousePressed(MouseEvent e) {
            }

            public void mouseExited(MouseEvent e) {
            }

            public void mouseEntered(MouseEvent e) {
            }

            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() > 1) cd();
            }
        });
        listing.addKeyListener(new KeyListener() {

            public void keyReleased(KeyEvent e) {
            }

            public void keyPressed(KeyEvent e) {
            }

            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == 10) cd();
            }
        });
        listing.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                select();
            }
        });
    }

    private void cd() {
        String filename = (String) listing.getSelectedValue();
        File f = new File(currentDir, filename);
        if (f.isDirectory()) {
            currentDir = f;
            populateCurrentDir();
        }
    }

    private void populateCurrentDir() {
        try {
            dir.setText(currentDir.getCanonicalFile().getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        DefaultListModel m = new DefaultListModel();
        listing.setModel(m);
        if (currentDir.getParent() != null) m.addElement("..");
        for (File f : currentDir.listFiles()) {
            String fn = f.getName();
            if (!fn.startsWith(".") && (f.isDirectory() || fn.endsWith(".txt"))) m.addElement(fn);
        }
        listing.setSelectedIndex(0);
    }

    private void select() {
        String filename = (String) listing.getSelectedValue();
        if (filename == null) {
            dispatch(null);
            return;
        }
        File f = new File(currentDir, filename);
        if (f.getName().endsWith(".txt") && !f.equals(lastSelected)) {
            lastSelected = f;
            dispatch(f);
        }
    }

    public void addListener(FileSelectionListener l) {
        listeners.add(l);
    }

    public void removeListener(FileSelectionListener l) {
        listeners.remove(l);
    }

    private void dispatch(File f) {
        for (FileSelectionListener l : listeners) l.fileSelected(f);
    }

    public static void main(String[] args) {
        FileList n = new FileList();
        n.addListener(new FileSelectionListener() {

            public void fileSelected(File f) {
                System.err.println("selected: " + f);
            }
        });
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.getContentPane().add(n);
        f.pack();
        f.setVisible(true);
    }
}
