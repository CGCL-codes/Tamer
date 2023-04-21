package net.coljac.pirates.gui;

import net.coljac.pirates.CardDatabase;
import net.coljac.pirates.Card;
import net.coljac.pirates.gui.helper.Exporter;
import net.coljac.pirates.gui.helper.Importer;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * By Colin Jacobs, colin@q9software.com
 * Date: Mar 11, 2006
 */
public class ManagerMain extends JFrame {

    public static final String VERSION = "1.1.1";

    public static ManagerMain instance;

    public ShipsPanel shipsPanel;

    public FleetsPanel fleetsPanel;

    public OtherPanel otherPanel;

    public OtherPanel allPanel;

    public CrewPanel crewPanel;

    public CardDatabase db;

    public TabPanel tabPanel;

    private JMenu fileMenu = new JMenu("File");

    private JMenu helpMenu = new JMenu("Help");

    private AboutPanel aboutPanel;

    public ManagerMain() throws HeadlessException {
        super("The Admiral");
        String cardDB = "cards.db";
        if (System.getProperty("card.db") != null) {
            cardDB = System.getProperty("card.db");
        }
        if (new File(cardDB).exists()) {
            try {
                db = CardDatabase.init(cardDB);
            } catch (IOException e) {
                e.printStackTrace();
                db = new CardDatabase(cardDB);
            }
        } else {
            try {
                InputStream is = ManagerMain.class.getResourceAsStream("/cards.db");
                if (is != null) {
                    db = CardDatabase.init(is, cardDB);
                } else {
                    db = new CardDatabase(cardDB);
                }
            } catch (Exception e) {
                db = new CardDatabase(cardDB);
            }
        }
        init();
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        setSize((int) (screen.getWidth() * .95), (int) (screen.getHeight() / 2));
        setLocation((int) (screen.getWidth() * .025), 200);
        this.setIconImage(Icons.ICON_ADMIRAL_32.getImage());
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                shutdown();
                System.exit(0);
            }
        });
        setVisible(true);
        aboutPanel = new AboutPanel();
    }

    private void init() {
        if (this.getJMenuBar() == null) {
            JMenuBar mb = new JMenuBar();
            initializeFileMenu();
            initializeHelpMenu();
            mb.add(fileMenu);
            mb.add(helpMenu);
            this.setJMenuBar(mb);
        }
        ToolTipManager.sharedInstance().setInitialDelay(0);
        instance = this;
        this.shipsPanel = new ShipsPanel();
        this.otherPanel = new OtherPanel();
        this.allPanel = new OtherPanel(true);
        this.fleetsPanel = new FleetsPanel();
        this.crewPanel = new CrewPanel();
        shipsPanel.setFleetsPanel(fleetsPanel);
        crewPanel.setFleetsPanel(fleetsPanel);
        otherPanel.setFleetsPanel(fleetsPanel);
        allPanel.setFleetsPanel(fleetsPanel);
        if (tabPanel == null) {
            tabPanel = new TabPanel();
            this.add(tabPanel);
        }
        tabPanel.init();
        repaint();
    }

    private void initializeFileMenu() {
        JMenu exportSubMenu = new JMenu("Export");
        fileMenu.add(exportSubMenu);
        JMenu importSubMenu = new JMenu("Import");
        fileMenu.add(importSubMenu);
        JMenuItem exportMy = new JMenuItem("Export my cards");
        exportMy.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String file = getFile("txt", false);
                if (file != null) {
                    try {
                        Exporter.exportCardList(file, true);
                    } catch (IOException e1) {
                        showError(e1.getMessage());
                    }
                }
            }
        });
        exportSubMenu.add(exportMy);
        JMenuItem exportAll = new JMenuItem("Export all cards");
        exportAll.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String file = getFile("txt", false);
                if (file != null) {
                    try {
                        Exporter.exportCardList(file, false);
                    } catch (IOException e1) {
                        showError(e1.getMessage());
                    }
                }
            }
        });
        exportSubMenu.add(exportAll);
        JMenuItem exportChecklist = new JMenuItem("Export checklist");
        exportChecklist.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String file = getFile("txt", false);
                if (file != null) {
                    try {
                        Exporter.exportChecklist(file);
                    } catch (IOException e1) {
                        showError(e1.getMessage());
                    }
                }
            }
        });
        exportSubMenu.add(exportChecklist);
        JMenu minis = new JMenu("Trading");
        exportSubMenu.add(minis);
        JMenuItem haves = new JMenuItem("Haves");
        haves.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String file = getFile("txt", true);
                try {
                    Exporter.exportMT(file, getCommentString(), true);
                } catch (IOException ioe) {
                    showError("Problem with the export: " + ioe.getMessage());
                }
            }
        });
        minis.add(haves);
        JMenuItem wants = new JMenuItem("Wants");
        wants.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String file = getFile("txt", true);
                try {
                    Exporter.exportMT(file, getCommentString(), false);
                } catch (IOException ioe) {
                    showError("Problem with the export: " + ioe.getMessage());
                }
            }
        });
        minis.add(wants);
        JMenuItem importCards = new JMenuItem("Import cards");
        importCards.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                final String file = getFile("txt", true);
                if (file != null) {
                    final JProgressBar progress = new JProgressBar(0, 100);
                    progress.setIndeterminate(true);
                    final JDialog progressDialog = new JDialog(ManagerMain.instance, "Importing...", false);
                    progressDialog.setUndecorated(true);
                    progressDialog.setLocation((int) (ManagerMain.instance.getLocation().getX() + instance.getWidth()) / 2, (int) (ManagerMain.instance.getLocation().getY() + instance.getHeight()) / 2);
                    JPanel panel = new JPanel();
                    panel.setBorder(BorderFactory.createTitledBorder("Importing..."));
                    panel.add(progress);
                    progressDialog.add(panel);
                    progressDialog.pack();
                    progressDialog.setVisible(true);
                    new Thread() {

                        public void run() {
                            String result = null;
                            try {
                                result = Importer.importCards(file);
                            } catch (IOException e1) {
                                showError(e1.getMessage());
                            } finally {
                                progressDialog.setVisible(false);
                                progressDialog.dispose();
                                dbChanged();
                                if (result != null) {
                                    showMessage(result);
                                }
                            }
                        }
                    }.start();
                }
            }
        });
        importSubMenu.add(importCards);
        JMenuItem importMine = new JMenuItem("Import checklist");
        importMine.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                final String file = getFile("txt", true);
                if (file != null) {
                    JProgressBar progress = new JProgressBar(0, 100);
                    JDialog progressDialog = new JDialog(ManagerMain.instance, "Importing...", true);
                    progressDialog.setLocation((int) ManagerMain.instance.getLocation().getX() / 2, (int) ManagerMain.instance.getLocation().getY() / 2);
                    progressDialog.add(progress);
                    progressDialog.pack();
                    progress.setVisible(true);
                    new Thread() {

                        public void run() {
                            try {
                                String result = Importer.importMyCards(file);
                                if (result != null) {
                                    showError(result);
                                }
                                showMessage(result);
                                dbChanged();
                            } catch (IOException e1) {
                                showError(e1.getMessage());
                            }
                        }
                    };
                }
            }
        });
        importSubMenu.add(importMine);
        JMenuItem clear = new JMenuItem("Clear Cards");
        clear.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int option = JOptionPane.showConfirmDialog(ManagerMain.instance, "This will remove all cards! Are you sure?", "Remove all cards?", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    db.clear();
                    db.save();
                    dbChanged();
                }
            }
        });
        fileMenu.add(clear);
        fileMenu.addSeparator();
        JMenuItem exit = new JMenuItem("Exit", KeyEvent.VK_X);
        exit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                shutdown();
                System.exit(0);
            }
        });
        fileMenu.add(exit);
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Message", JOptionPane.INFORMATION_MESSAGE);
    }

    private String getCommentString() {
        String inputValue = JOptionPane.showInputDialog(this, "Enter comment:", "Comment?", JOptionPane.QUESTION_MESSAGE);
        return inputValue;
    }

    public String getFile(String extension, boolean open) {
        final String ext = extension;
        JFileChooser chooser = new JFileChooser(new File("."));
        chooser.setFileFilter(new FileFilter() {

            public boolean accept(File pathname) {
                return pathname.getAbsolutePath().toUpperCase().endsWith(ext.toUpperCase());
            }

            public String getDescription() {
                return "Tab-delimited text";
            }
        });
        int returnVal = open ? chooser.showOpenDialog(this) : chooser.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile().getAbsolutePath();
        }
        return null;
    }

    private void initializeHelpMenu() {
        JMenuItem help = new JMenuItem("Help");
        help.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JDialog dialog = new JDialog(instance, "Admiral Help", false);
                dialog.add(new HelpPanel());
                dialog.setLocation((int) instance.getLocation().getX() + 200, (int) instance.getLocation().getY() + 100);
                dialog.setSize(700, 500);
                dialog.setVisible(true);
            }
        });
        JMenuItem about = new JMenuItem("About");
        about.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                JDialog aboutDialog = new JDialog(ManagerMain.instance, "About", false);
                aboutDialog.add(aboutPanel);
                aboutDialog.pack();
                aboutDialog.setLocation((int) (ManagerMain.instance.getLocation().getX() + 100), (int) (ManagerMain.instance.getLocation().getY()) + 100);
                aboutDialog.setVisible(true);
            }
        });
        helpMenu.add(help);
        helpMenu.add(about);
    }

    public static void main(String[] args) {
        new ManagerMain();
    }

    public boolean showConfirm(String message) {
        int i = JOptionPane.showConfirmDialog(this, message, "Are you sure?", JOptionPane.YES_NO_OPTION);
        return i == JOptionPane.YES_OPTION;
    }

    public void showAlert(String message) {
        JOptionPane.showMessageDialog(this, message, "Are you sure?", JOptionPane.YES_NO_OPTION);
    }

    public void dbChanged() {
        init();
    }

    private void shutdown() {
        try {
            ManagerMain.instance.db.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
