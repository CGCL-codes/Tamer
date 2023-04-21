package at.filemonkey.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import at.filemonkey.controller.MonkeyMainController;
import at.filemonkey.model.DirectoryNavigator;

/**
 * UI Element for file browsing contains a JTextField for the current directory
 * (path), a JList for files/directories in the current directory. A realization
 * class of DirectoryNavigator provides List/TreeModel, CellRenderers
 * 
 * @author Markus Skergeth
 * 
 */
public class FileListPanel extends JPanel {

    private static final long serialVersionUID = 373174034648660692L;

    protected MonkeyMainController mc;

    protected DirectoryNavigator nav;

    protected JTextField uri;

    protected JList list;

    protected JButton up;

    protected JPopupMenu filePopup;

    protected JPopupMenu listPopup;

    protected JMenuItem rename;

    protected JMenuItem delete;

    protected JMenuItem newFolder;

    protected JLabel nameSpace;

    public FileListPanel(MonkeyMainController controller) {
        super();
        if (controller == null) throw new IllegalArgumentException("controller is null");
        mc = controller;
        initComponents();
        initActions();
        layoutComponents();
    }

    /**
	 * initialize all GUI components
	 */
    protected void initComponents() {
        list = new JList();
        list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        filePopup = new JPopupMenu();
        rename = new JMenuItem("Rename");
        filePopup.add(rename);
        delete = new JMenuItem("Delete");
        filePopup.add(delete);
        listPopup = new JPopupMenu();
        newFolder = new JMenuItem("Create new folder");
        listPopup.add(newFolder);
        nameSpace = new JLabel("FTP:");
        uri = new JTextField();
        up = new JButton("up");
        up.setText("");
        up.setIcon(new ImageIcon(mc.getProperty("up")));
        up.setPreferredSize(new Dimension(20, 20));
    }

    protected void initActions() {
        uri.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent a) {
                uriPathChanged(a);
            }
        });
        list.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent m) {
                if (SwingUtilities.isLeftMouseButton(m) && m.getClickCount() == 2) {
                    listDoubleClicked(m);
                } else if (SwingUtilities.isRightMouseButton(m) && !list.isSelectionEmpty() && list.locationToIndex(m.getPoint()) == list.getSelectedIndex()) {
                    filePopup.show(list, m.getX(), m.getY());
                } else if (SwingUtilities.isRightMouseButton(m)) {
                    listPopup.show(list, m.getX(), m.getY());
                }
            }

            ;
        });
        list.addKeyListener(new KeyListener() {

            @Override
            public void keyPressed(KeyEvent arg0) {
            }

            @Override
            public void keyReleased(KeyEvent k) {
                listKeyReleased(k);
            }

            @Override
            public void keyTyped(KeyEvent k) {
            }
        });
        up.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                upButtonClicked(arg0);
            }
        });
        rename.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                renameClicked(arg0);
            }
        });
        delete.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelected();
            }
        });
        newFolder.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                createFolderClicked(arg0);
            }
        });
    }

    protected void layoutComponents() {
        setLayout(new BorderLayout());
        JPanel helpPanel = new JPanel();
        helpPanel.setLayout(new FlowLayout());
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        helpPanel.add(nameSpace);
        topPanel.add(helpPanel, BorderLayout.WEST);
        helpPanel.add(up);
        topPanel.add(uri, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(list), BorderLayout.CENTER);
    }

    protected void updateUI(String dir) {
        list.updateUI();
        uri.setText(dir);
    }

    /**
	 * update JList and JTextField
	 */
    public void updateListAndURI() {
        try {
            updateUI(nav.getCurrentPath());
        } catch (IOException e) {
            mc.setStatus("Could not update UI elements", StatusBar.WARNING);
        }
    }

    /**
	 * calls navigator.cd() and updates UI elements
	 * 
	 * @param a
	 */
    protected void uriPathChanged(ActionEvent a) {
        try {
            if (!nav.cd(uri.getText())) {
                mc.setStatus("The entered path does not exist", StatusBar.WARNING);
            }
        } catch (IOException e) {
            mc.setStatus("The entered path does not exist", StatusBar.WARNING);
        }
        updateUI(uri.getText());
    }

    /**
	 * listDoubleClick-Event calls navigator.cd() and updates UI elements
	 * 
	 * @param m
	 */
    protected void listDoubleClicked(MouseEvent m) {
        Object file = list.getSelectedValue();
        if (file != null) {
            String currentPath = "";
            try {
                currentPath = nav.getCurrentPath() + nav.getFilename(file);
                nav.cd(currentPath);
                updateUI(nav.getCurrentPath());
            } catch (IOException e) {
                mc.setStatus(e.getClass().getName() + ": " + e.getMessage(), StatusBar.ERROR);
            }
        }
    }

    protected void deleteSelected() {
        if (JOptionPane.showConfirmDialog(this, "Do you really want to delete the selected files?") == JOptionPane.YES_OPTION) {
            Object[] fileObjects = list.getSelectedValues();
            for (int i = 0; i < fileObjects.length; i++) {
                try {
                    String filename = nav.getFilename(fileObjects[i]);
                    nav.delete(filename);
                    mc.setStatus("File deleted: " + filename, StatusBar.MESSAGE);
                    updateUI(nav.getCurrentPath());
                } catch (IOException e) {
                    mc.setStatus(e.getClass().getName() + ": " + e.getMessage(), StatusBar.ERROR);
                }
            }
        }
    }

    protected void listKeyReleased(KeyEvent k) {
        if (k.getKeyCode() == KeyEvent.VK_DELETE) {
            deleteSelected();
        }
    }

    /**
	 * button-event for going to the parent directory
	 * 
	 * @param a
	 */
    protected void upButtonClicked(ActionEvent a) {
        try {
            if (!nav.up()) {
                mc.setStatus("Could not change path to parent directory", StatusBar.ERROR);
            }
            updateUI(nav.getCurrentPath());
        } catch (IOException e) {
            mc.setStatus("Could not change path to parent directory", StatusBar.ERROR);
        }
    }

    /**
	 * event for renaming a file/folder
	 * 
	 * @param a
	 */
    protected void renameClicked(ActionEvent a) {
        Object file = list.getSelectedValue();
        String filename = nav.getFilename(file);
        String newName = JOptionPane.showInputDialog(this, "Rename " + filename + " to:", "Rename File/Folder", JOptionPane.QUESTION_MESSAGE);
        if (newName != null && newName.trim().length() > 0) {
            try {
                if (nav.rename(filename, newName)) {
                    mc.setStatus(filename + " renamed to: " + newName, StatusBar.MESSAGE);
                } else {
                    mc.setStatus(filename + " could not be renamed to: " + newName, StatusBar.ERROR);
                }
                updateUI(nav.getCurrentPath());
            } catch (IOException e) {
                mc.setStatus(filename + " could not be renamed to: " + newName, StatusBar.ERROR);
            }
        }
    }

    /**
	 * event for creating a new folder
	 * 
	 * @param a
	 */
    protected void createFolderClicked(ActionEvent a) {
        String newFolder = JOptionPane.showInputDialog(this, "Name of the new folder:", "Create Folder", JOptionPane.QUESTION_MESSAGE);
        if (newFolder != null && newFolder.trim().length() > 0) {
            try {
                if (nav.createFolder(newFolder)) {
                    mc.setStatus("Folder created: " + newFolder, StatusBar.MESSAGE);
                } else {
                    mc.setStatus("Folder could not be created: " + newFolder, StatusBar.ERROR);
                }
                updateUI(nav.getCurrentPath());
            } catch (IOException e) {
                mc.setStatus("Folder could not be created: " + newFolder, StatusBar.ERROR);
            }
        }
    }

    /**
	 * set TransferHandler for the list
	 */
    public void setTransferHandler(TransferHandler handler) {
        if (handler == null) {
            throw new IllegalArgumentException("handler is null");
        }
        list.setTransferHandler(handler);
        list.setDragEnabled(true);
    }

    /**
	 * set navigator for list and update uri
	 * 
	 * @param navigator
	 */
    public void setDirectoryNavigator(DirectoryNavigator navigator) {
        if (navigator == null) {
            throw new IllegalArgumentException("navigator is null");
        }
        list.setModel(navigator);
        try {
            uri.setText(navigator.getCurrentPath());
        } catch (IOException e) {
            mc.setStatus(e.getClass().getName() + ": " + e.getMessage(), StatusBar.ERROR);
        }
        list.setCellRenderer(navigator.getListCellRenderer());
        list.setIgnoreRepaint(true);
        nameSpace.setText(navigator.getName() + ": ");
        nav = navigator;
    }
}
