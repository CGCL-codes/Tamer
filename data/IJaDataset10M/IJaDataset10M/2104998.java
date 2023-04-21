package com.emental.mindraider.ui;

import java.awt.BorderLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.List;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.apache.log4j.Category;
import com.emental.mindraider.MindRaiderConstants;
import com.emental.mindraider.MindRaiderVocabulary;
import com.emental.mindraider.concept.ui.OpenConceptJDialog;
import com.emental.mindraider.desktop.VirtualDesktopJPanel;
import com.emental.mindraider.facet.BriefFacet;
import com.emental.mindraider.facet.FacetCustodian;
import com.emental.mindraider.folder.ui.NewFolderJDialog;
import com.emental.mindraider.gfx.Gfx;
import com.emental.mindraider.gfx.IconsRegistry;
import com.emental.mindraider.gnowsis.ui.ConnectUriqaServerJDialog;
import com.emental.mindraider.graph.spiders.SpidersGraph;
import com.emental.mindraider.install.Installer;
import com.emental.mindraider.install.ui.PreferencesJDialog;
import com.emental.mindraider.kernel.MindRaider;
import com.emental.mindraider.model.ui.DownloadModelJDialog;
import com.emental.mindraider.model.ui.NewRdfModelJDialog;
import com.emental.mindraider.model.ui.RdfModelDetailsJDialog;
import com.emental.mindraider.notebook.NotebookCustodian;
import com.emental.mindraider.notebook.ui.NewNotebookJDialog;
import com.emental.mindraider.notebook.ui.OpenNotebookJDialog;
import com.emental.mindraider.outline.NotebookOutlineJPanel;
import com.emental.mindraider.profile.color.ColorProfileSpidersBlack;
import com.emental.mindraider.profile.color.ColorProfileSpidersWhite;
import com.emental.mindraider.search.SearchCommander;
import com.emental.mindraider.trash.ui.TrashJPanel;
import com.emental.mindraider.utils.Launcher;
import com.emental.mindraider.utils.Utils;

/**
 * Application main window.
 * 
 * @author Martin.Dvorak
 */
public class MindRaiderJFrame extends JFrame implements MindRaiderConstants, DropTargetListener {

    private static final Category cat = Category.getInstance("com.emental.mindraider.ui.MindRaiderJFrame");

    /**
     * serial version UID
     */
    private static final long serialVersionUID = 2882895236794565156L;

    /**
     * <code>true</code> if in applet
     */
    public boolean isApplet;

    /**
     * instance of this class
     */
    private static MindRaiderJFrame singleton;

    public JSplitPane leftSidebarSplitPane;

    /**
     * Get instance of this class.
     * 
     * @return
     */
    public static MindRaiderJFrame getInstance(boolean isApplet) {
        if (singleton == null) {
            singleton = new MindRaiderJFrame(isApplet);
        }
        return singleton;
    }

    /**
     * Constructor.
     */
    private MindRaiderJFrame() {
    }

    /**
     * Constructor.
     * 
     * @param profileLocation
     * @param isApplet
     */
    private MindRaiderJFrame(final boolean isApplet) {
        super(MindRaider.getTitle(), Gfx.getGraphicsConfiguration());
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                cat.debug("Frame got quit!");
                if (isApplet) {
                    dispose();
                } else {
                    System.exit(0);
                }
            }
        });
        DropTarget dropTarget = new DropTarget(this, (DropTargetListener) this);
        this.setDropTarget(dropTarget);
        String javaVersion = System.getProperty("java.version");
        if (!javaVersion.startsWith("1.4.2")) {
            JOptionPane.showMessageDialog(this, "To run MindRaider you should use JRE 1.4.2!  " + "\nYour current version is " + javaVersion, "Warning: Java Version", JOptionPane.WARNING_MESSAGE);
        }
        this.isApplet = isApplet;
        singleton = this;
        System.out.println("Booting kernel...");
        setIconImage(IconsRegistry.getImage("programIcon.gif"));
        SplashScreen splash = new SplashScreen(this, false);
        splash.showSplashScreen();
        MindRaider.preSetProfiles();
        MindRaider.setMasterToolBar(new MasterToolBar());
        getContentPane().add(MindRaider.masterToolBar, BorderLayout.NORTH);
        getContentPane().add(StatusBar.getStatusBar(), BorderLayout.SOUTH);
        buildMenu(MindRaider.spidersGraph);
        MindRaider.setProfiles();
        final JTabbedPane leftSidebar = new JTabbedPane(JTabbedPane.BOTTOM);
        leftSidebar.setTabPlacement(JTabbedPane.TOP);
        leftSidebar.addTab("Explorer", ExplorerJPanel.getInstance());
        leftSidebar.addTab("Trash", TrashJPanel.getInstance());
        if (MindRaider.EXPERIMENTAL_PERSPECTIVE.equals(MindRaider.profile.getUiPerspective())) {
            leftSidebar.addTab("Desktops", new VirtualDesktopJPanel());
        }
        leftSidebar.setSelectedIndex(0);
        leftSidebar.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent arg0) {
                if (arg0.getSource() instanceof JTabbedPane) {
                    if (leftSidebar.getSelectedIndex() == 1) {
                        TrashJPanel.getInstance().refresh();
                    }
                }
            }
        });
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(NotebookOutlineJPanel.getInstance().mainPanelControlsBar, BorderLayout.NORTH);
        mainPanel.add(NotebookOutlineJPanel.getInstance(), BorderLayout.CENTER);
        leftSidebarSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftSidebar, mainPanel);
        leftSidebarSplitPane.setOneTouchExpandable(true);
        leftSidebarSplitPane.setDividerLocation(200);
        leftSidebarSplitPane.setLastDividerLocation(200);
        leftSidebarSplitPane.setDividerSize(6);
        leftSidebarSplitPane.setContinuousLayout(true);
        getContentPane().add(leftSidebarSplitPane, BorderLayout.CENTER);
        Gfx.centerAndShowWindow(this, 1024, 768);
        MindRaider.postSetProfiles();
        splash.hideSplash();
    }

    /**
     * Build main menu.
     * 
     * @param spiders
     */
    private void buildMenu(final SpidersGraph spiders) {
        JMenuBar menuBar;
        JMenu menu, submenu;
        JMenuItem menuItem, subMenuItem;
        JRadioButtonMenuItem rbMenuItem;
        menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        menu = new JMenu(MR_TITLE);
        menu.setMnemonic(KeyEvent.VK_M);
        ButtonGroup perspectiveGroup = new ButtonGroup();
        submenu = new JMenu("Set Perspective... ");
        submenu.setMnemonic(KeyEvent.VK_P);
        subMenuItem = new JRadioButtonMenuItem("Outliner");
        subMenuItem.setEnabled(true);
        if (MindRaider.OUTLINER_PERSPECTIVE.equals(MindRaider.profile.getUiPerspective())) {
            subMenuItem.setSelected(true);
        }
        subMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setPerspective(MindRaider.OUTLINER_PERSPECTIVE);
            }
        });
        submenu.add(subMenuItem);
        perspectiveGroup.add(subMenuItem);
        subMenuItem = new JRadioButtonMenuItem("Semantic Web");
        subMenuItem.setEnabled(true);
        if (MindRaider.SW_PERSPECTIVE.equals(MindRaider.profile.getUiPerspective())) {
            subMenuItem.setSelected(true);
        }
        subMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setPerspective(MindRaider.SW_PERSPECTIVE);
            }
        });
        submenu.add(subMenuItem);
        perspectiveGroup.add(subMenuItem);
        subMenuItem = new JRadioButtonMenuItem("Experimental Features");
        if (MindRaider.EXPERIMENTAL_PERSPECTIVE.equals(MindRaider.profile.getUiPerspective())) {
            subMenuItem.setSelected(true);
        }
        subMenuItem.setEnabled(true);
        subMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setPerspective(MindRaider.EXPERIMENTAL_PERSPECTIVE);
            }
        });
        submenu.add(subMenuItem);
        perspectiveGroup.add(subMenuItem);
        menu.add(submenu);
        menuItem = new JMenuItem("Preferences...");
        menuItem.setMnemonic(KeyEvent.VK_P);
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                new PreferencesJDialog();
            }
        });
        menu.add(menuItem);
        if (MindRaider.EXPERIMENTAL_PERSPECTIVE.equals(MindRaider.profile.getUiPerspective())) {
            menu.addSeparator();
            menuItem = new JMenuItem("Folders...");
            menuItem.setMnemonic(KeyEvent.VK_F);
            menuItem.setEnabled(false);
            menuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                }
            });
            menu.add(menuItem);
            menuItem = new JMenuItem("Notebooks...");
            menuItem.setMnemonic(KeyEvent.VK_N);
            menuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                }
            });
            menu.add(menuItem);
            menuItem = new JMenuItem("Facets...");
            menuItem.setMnemonic(KeyEvent.VK_A);
            menuItem.setEnabled(false);
            menuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                }
            });
            menu.add(menuItem);
            menuItem = new JMenuItem("Attachments...");
            menuItem.setMnemonic(KeyEvent.VK_A);
            menuItem.setEnabled(false);
            menuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                }
            });
            menu.add(menuItem);
            menuItem = new JMenuItem("Channels");
            menuItem.setMnemonic(KeyEvent.VK_A);
            menuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    try {
                        MindRaider.notebookCustodian.loadNotebook(new URI("Channels"));
                    } catch (URISyntaxException e1) {
                        e1.printStackTrace();
                    }
                }
            });
            menu.add(menuItem);
            menuItem = new JMenuItem("Bookmarks");
            menuItem.setMnemonic(KeyEvent.VK_A);
            menuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    try {
                        MindRaider.notebookCustodian.loadNotebook(new URI("Bookmarks"));
                    } catch (URISyntaxException e1) {
                        e1.printStackTrace();
                    }
                }
            });
            menu.add(menuItem);
            menuItem = new JMenuItem("Associations...");
            menuItem.setMnemonic(KeyEvent.VK_A);
            menuItem.setEnabled(false);
            menuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                }
            });
            menu.add(menuItem);
            menuItem = new JMenuItem("Taxonomies...");
            menuItem.setMnemonic(KeyEvent.VK_A);
            menuItem.setEnabled(false);
            menuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                }
            });
            menu.add(menuItem);
            menuBar.add(menu);
        }
        menu.addSeparator();
        menuItem = new JMenuItem("Set Active Notebook as Home");
        menuItem.setMnemonic(KeyEvent.VK_H);
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                MindRaider.profile.setHomeNotebook();
            }
        });
        menu.add(menuItem);
        menu.add(menuItem);
        if (!MindRaider.OUTLINER_PERSPECTIVE.equals(MindRaider.profile.getUiPerspective())) {
            menuItem = new JMenuItem("My Profile");
            menuItem.setMnemonic(KeyEvent.VK_M);
            menuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    MindRaider.notebookCustodian.close();
                    NotebookOutlineJPanel.getInstance().clear();
                    String myProfile = MindRaider.profile.getProfileLocation();
                    StatusBar.show("Loading profile " + myProfile);
                    try {
                        MindRaider.spidersGraph.load(myProfile);
                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(MindRaiderJFrame.this, "Unable to load profile: " + e1.getMessage(), "Load Model Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            menu.add(menuItem);
        }
        menu.addSeparator();
        menuItem = new JMenuItem("Exit", KeyEvent.VK_X);
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                System.out.println("Exiting " + MR_TITLE + "...");
                System.exit(0);
            }
        });
        menu.add(menuItem);
        menuBar.add(menu);
        menu = new JMenu("View");
        menu.setMnemonic(KeyEvent.VK_V);
        ButtonGroup lfGroup = new ButtonGroup();
        submenu = new JMenu("Look&Feel... ");
        cat.debug("Look and feel is: " + MindRaider.profile.getLookAndFeel());
        submenu.setMnemonic(KeyEvent.VK_P);
        subMenuItem = new JRadioButtonMenuItem("Native");
        if (MindRaider.LF_NATIVE.equals(MindRaider.profile.getLookAndFeel())) {
            subMenuItem.setSelected(true);
        }
        subMenuItem.setEnabled(true);
        subMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setLookAndFeel(MindRaider.LF_NATIVE);
            }
        });
        submenu.add(subMenuItem);
        lfGroup.add(subMenuItem);
        subMenuItem = new JRadioButtonMenuItem("Java");
        if (MindRaider.LF_JAVA_DEFAULT.equals(MindRaider.profile.getLookAndFeel())) {
            subMenuItem.setSelected(true);
        }
        subMenuItem.setEnabled(true);
        subMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setLookAndFeel(MindRaider.LF_JAVA_DEFAULT);
            }
        });
        submenu.add(subMenuItem);
        lfGroup.add(subMenuItem);
        subMenuItem = new JRadioButtonMenuItem("Dark Java");
        if (MindRaider.LF_JAVA_BLACK.equals(MindRaider.profile.getLookAndFeel())) {
            subMenuItem.setSelected(true);
        }
        subMenuItem.setEnabled(true);
        subMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setLookAndFeel(MindRaider.LF_JAVA_BLACK);
            }
        });
        submenu.add(subMenuItem);
        lfGroup.add(subMenuItem);
        menu.add(submenu);
        menu.addSeparator();
        menuItem = new JMenuItem("Left Sidebar");
        menuItem.setMnemonic(KeyEvent.VK_L);
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (leftSidebarSplitPane.getDividerLocation() == 1) {
                    leftSidebarSplitPane.resetToPreferredSizes();
                } else {
                    closeLeftSidebar();
                }
            }
        });
        menu.add(menuItem);
        menuItem = new JMenuItem("Right Sidebar");
        menuItem.setMnemonic(KeyEvent.VK_R);
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                NotebookOutlineJPanel.getInstance().toggleRightSidebar();
            }
        });
        menu.add(menuItem);
        menuItem = new JMenuItem("Toolbar");
        menuItem.setMnemonic(KeyEvent.VK_T);
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                MindRaider.masterToolBar.toggleVisibility();
            }
        });
        menu.add(menuItem);
        menuItem = new JMenuItem("Graph Navigator Dashboard");
        menuItem.setMnemonic(KeyEvent.VK_D);
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                MindRaider.spidersGraph.getGlPanel().toggleControlPanel();
            }
        });
        menu.add(menuItem);
        JCheckBoxMenuItem checkboxMenuItem;
        ButtonGroup colorSchemeGroup;
        if (MindRaider.EXPERIMENTAL_PERSPECTIVE.equals(MindRaider.profile.getUiPerspective())) {
            menu.addSeparator();
            submenu = new JMenu("Facet... ");
            submenu.setMnemonic(KeyEvent.VK_F);
            colorSchemeGroup = new ButtonGroup();
            String[] facetLabels = FacetCustodian.getInstance().getFacetLabels();
            if (facetLabels != null && facetLabels.length > 0) {
                for (int i = 0; i < facetLabels.length; i++) {
                    rbMenuItem = new JRadioButtonMenuItem(facetLabels[i]);
                    rbMenuItem.addActionListener(new FacetActionListener(facetLabels[i]));
                    colorSchemeGroup.add(rbMenuItem);
                    submenu.add(rbMenuItem);
                    if (BriefFacet.LABEL.equals(facetLabels[i])) {
                        rbMenuItem.setSelected(true);
                    }
                }
            }
            menu.add(submenu);
            checkboxMenuItem = new JCheckBoxMenuItem("Graph Label as URI");
            checkboxMenuItem.setMnemonic(KeyEvent.VK_G);
            checkboxMenuItem.setState(MindRaider.spidersGraph.isUriLabels());
            checkboxMenuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    if (e.getSource() instanceof JCheckBoxMenuItem) {
                        JCheckBoxMenuItem j = (JCheckBoxMenuItem) e.getSource();
                        MindRaider.spidersGraph.setUriLabels(j.getState());
                        MindRaider.spidersGraph.renderModel();
                        MindRaider.profile.graphShowLabelsAsUris = j.getState();
                        MindRaider.profile.save();
                    }
                }
            });
            menu.add(checkboxMenuItem);
            checkboxMenuItem = new JCheckBoxMenuItem("Predicate nodes");
            checkboxMenuItem.setMnemonic(KeyEvent.VK_P);
            checkboxMenuItem.setState(!MindRaider.spidersGraph.getHidePredicates());
            checkboxMenuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    if (e.getSource() instanceof JCheckBoxMenuItem) {
                        JCheckBoxMenuItem j = (JCheckBoxMenuItem) e.getSource();
                        MindRaider.spidersGraph.hidePredicates(!j.getState());
                        MindRaider.spidersGraph.renderModel();
                        MindRaider.profile.graphHidePredicates = !j.getState();
                        MindRaider.profile.save();
                    }
                }
            });
            menu.add(checkboxMenuItem);
            checkboxMenuItem = new JCheckBoxMenuItem("Multiline labels");
            checkboxMenuItem.setMnemonic(KeyEvent.VK_M);
            checkboxMenuItem.setState(MindRaider.spidersGraph.isMultilineNodes());
            checkboxMenuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    if (e.getSource() instanceof JCheckBoxMenuItem) {
                        JCheckBoxMenuItem j = (JCheckBoxMenuItem) e.getSource();
                        MindRaider.spidersGraph.setMultilineNodes(j.getState());
                        MindRaider.spidersGraph.renderModel();
                        MindRaider.profile.graphMultilineLabels = j.getState();
                        MindRaider.profile.save();
                    }
                }
            });
            menu.add(checkboxMenuItem);
        }
        menu.addSeparator();
        checkboxMenuItem = new JCheckBoxMenuItem("Antialiased", true);
        checkboxMenuItem.setMnemonic(KeyEvent.VK_A);
        checkboxMenuItem.setState(SpidersGraph.antialiased);
        checkboxMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (e.getSource() instanceof JCheckBoxMenuItem) {
                    JCheckBoxMenuItem j = (JCheckBoxMenuItem) e.getSource();
                    SpidersGraph.antialiased = j.getState();
                    MindRaider.spidersGraph.renderModel();
                }
            }
        });
        menu.add(checkboxMenuItem);
        checkboxMenuItem = new JCheckBoxMenuItem("Hyperbolic", true);
        checkboxMenuItem.setMnemonic(KeyEvent.VK_H);
        checkboxMenuItem.setState(SpidersGraph.hyperbolic);
        checkboxMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (e.getSource() instanceof JCheckBoxMenuItem) {
                    JCheckBoxMenuItem j = (JCheckBoxMenuItem) e.getSource();
                    SpidersGraph.hyperbolic = j.getState();
                    MindRaider.spidersGraph.renderModel();
                }
            }
        });
        menu.add(checkboxMenuItem);
        checkboxMenuItem = new JCheckBoxMenuItem("FPS", true);
        checkboxMenuItem.setMnemonic(KeyEvent.VK_F);
        checkboxMenuItem.setState(SpidersGraph.fps);
        checkboxMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (e.getSource() instanceof JCheckBoxMenuItem) {
                    JCheckBoxMenuItem j = (JCheckBoxMenuItem) e.getSource();
                    SpidersGraph.fps = j.getState();
                    MindRaider.spidersGraph.renderModel();
                }
            }
        });
        menu.add(checkboxMenuItem);
        submenu = new JMenu("Color Scheme... ");
        submenu.setMnemonic(KeyEvent.VK_C);
        colorSchemeGroup = new ButtonGroup();
        rbMenuItem = new JRadioButtonMenuItem("White");
        rbMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                MindRaider.spidersGraph.setRenderingProfile(ColorProfileSpidersWhite.getInstance());
                MindRaider.spidersGraph.renderModel();
            }
        });
        colorSchemeGroup.add(rbMenuItem);
        submenu.add(rbMenuItem);
        rbMenuItem = new JRadioButtonMenuItem("Black");
        rbMenuItem.setSelected(true);
        rbMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                MindRaider.spidersGraph.setRenderingProfile(ColorProfileSpidersBlack.getInstance());
                MindRaider.spidersGraph.renderModel();
            }
        });
        colorSchemeGroup.add(rbMenuItem);
        submenu.add(rbMenuItem);
        menu.add(submenu);
        menu.addSeparator();
        checkboxMenuItem = new JCheckBoxMenuItem("Full screen");
        checkboxMenuItem.setMnemonic(KeyEvent.VK_U);
        checkboxMenuItem.setState(false);
        checkboxMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (e.getSource() instanceof JCheckBoxMenuItem) {
                    JCheckBoxMenuItem j = (JCheckBoxMenuItem) e.getSource();
                    if (j.getState()) {
                        Gfx.toggleFullScreen(MindRaiderJFrame.this);
                    } else {
                        Gfx.toggleFullScreen(null);
                    }
                }
            }
        });
        menu.add(checkboxMenuItem);
        menuBar.add(menu);
        menu = new JMenu("Folder");
        menu.setMnemonic(KeyEvent.VK_F);
        menuItem = new JMenuItem("New...");
        menuItem.setMnemonic(KeyEvent.VK_N);
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                new NewFolderJDialog();
            }
        });
        menu.add(menuItem);
        menuItem = new JMenuItem("Discard");
        menuItem.setMnemonic(KeyEvent.VK_D);
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(MindRaiderJFrame.this, "Do you really want to discard folder " + MindRaider.profile.getActiveNotebook() + "?");
                if (result == JOptionPane.YES_OPTION) {
                }
            }
        });
        menu.add(menuItem);
        menuBar.add(menu);
        menu = new JMenu("Notebook");
        menu.setMnemonic(KeyEvent.VK_N);
        menuItem = new JMenuItem("New...");
        menuItem.setMnemonic(KeyEvent.VK_N);
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                new NewNotebookJDialog();
            }
        });
        menu.add(menuItem);
        menuItem = new JMenuItem("Open...");
        menuItem.setMnemonic(KeyEvent.VK_O);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK | ActionEvent.SHIFT_MASK));
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                new OpenNotebookJDialog();
            }
        });
        menu.add(menuItem);
        menuItem = new JMenuItem("Close");
        menuItem.setMnemonic(KeyEvent.VK_C);
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                MindRaider.notebookCustodian.close();
                NotebookOutlineJPanel.getInstance().refresh();
                MindRaider.spidersGraph.renderModel();
            }
        });
        menu.add(menuItem);
        menuItem = new JMenuItem("Discard");
        menuItem.setMnemonic(KeyEvent.VK_D);
        menuItem.setEnabled(false);
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(MindRaiderJFrame.this, "Do you really want to discard notebook " + MindRaider.profile.getActiveNotebook() + "?");
                if (result == JOptionPane.YES_OPTION) {
                    if (MindRaider.profile.activeNotebookUri != null) {
                        try {
                            MindRaider.folderCustodian.discardNotebook(MindRaider.profile.activeNotebookUri.toString());
                            MindRaider.notebookCustodian.close();
                        } catch (Exception e1) {
                            cat.error("Unable to discard notebook!", e1);
                        }
                    }
                }
            }
        });
        menu.add(menuItem);
        menu.addSeparator();
        menuItem = new JMenuItem("Up");
        menuItem.setMnemonic(KeyEvent.VK_U);
        menuItem.setEnabled(false);
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
            }
        });
        menu.add(menuItem);
        menuItem = new JMenuItem("Down");
        menuItem.setMnemonic(KeyEvent.VK_D);
        menuItem.setEnabled(false);
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
            }
        });
        menu.add(menuItem);
        menu.addSeparator();
        submenu = new JMenu("Export... ");
        submenu.setMnemonic(KeyEvent.VK_E);
        subMenuItem = new JMenuItem("OPML");
        subMenuItem.setSelected(true);
        subMenuItem.setEnabled(true);
        subMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (MindRaider.profile.getActiveNotebook() == null) {
                    JOptionPane.showMessageDialog(MindRaiderJFrame.this, "To export a notebook it must be loaded!", "Export Error", JOptionPane.ERROR_MESSAGE);
                    return;
                } else {
                    JFileChooser fc = new JFileChooser();
                    fc.setApproveButtonText("Export");
                    fc.setControlButtonsAreShown(true);
                    fc.setDialogTitle("Choose Export Directory");
                    fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    String exportDirectory = MindRaider.profile.getHomeDirectory() + File.separator + "export" + File.separator + "opml";
                    Utils.createDirectory(exportDirectory);
                    fc.setCurrentDirectory(new File(exportDirectory));
                    int returnVal = fc.showOpenDialog(MindRaiderJFrame.this);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        String dstFileName = fc.getSelectedFile().getAbsolutePath() + File.separator + "OPML-EXPORT-" + MindRaider.notebookCustodian.getActiveNotebookNcName() + ".xml";
                        System.out.println("Exporting to file: " + dstFileName);
                        MindRaider.notebookCustodian.exportNotebook(NotebookCustodian.FORMAT_OPML, dstFileName);
                        Launcher.launchViaStart(dstFileName);
                    } else {
                        cat.debug("Export command cancelled by user.");
                    }
                }
            }
        });
        submenu.add(subMenuItem);
        subMenuItem = new JMenuItem("TWiki");
        subMenuItem.setSelected(true);
        subMenuItem.setEnabled(true);
        subMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (MindRaider.profile.getActiveNotebook() == null) {
                    JOptionPane.showMessageDialog(MindRaiderJFrame.this, "To export a notebook it must be loaded!", "Export Error", JOptionPane.ERROR_MESSAGE);
                    return;
                } else {
                    JFileChooser fc = new JFileChooser();
                    fc.setApproveButtonText("Export");
                    fc.setControlButtonsAreShown(true);
                    fc.setDialogTitle("Choose Export Directory");
                    fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    String exportDirectory = MindRaider.profile.getHomeDirectory() + File.separator + "export" + File.separator + "twiki";
                    Utils.createDirectory(exportDirectory);
                    fc.setCurrentDirectory(new File(exportDirectory));
                    int returnVal = fc.showOpenDialog(MindRaiderJFrame.this);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        final String dstFileName = fc.getSelectedFile().getAbsolutePath() + File.separator + "TWIKI-EXPORT-" + MindRaider.notebookCustodian.getActiveNotebookNcName() + ".txt";
                        System.out.println("Exporting to file: " + dstFileName);
                        MindRaider.notebookCustodian.exportNotebook(NotebookCustodian.FORMAT_TWIKI, dstFileName);
                    } else {
                        cat.debug("Export command cancelled by user.");
                    }
                }
            }
        });
        submenu.add(subMenuItem);
        menu.add(submenu);
        submenu = new JMenu("Import... ");
        submenu.setMnemonic(KeyEvent.VK_I);
        subMenuItem = new JMenuItem("TWiki");
        subMenuItem.setSelected(true);
        subMenuItem.setEnabled(true);
        subMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                NotebookOutlineJPanel.getInstance().clear();
                MindRaider.profile.activeNotebookUri = null;
                JFileChooser fc = new JFileChooser();
                int returnVal = fc.showOpenDialog(MindRaiderJFrame.this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    final File file = fc.getSelectedFile();
                    MindRaider.profile.deleteActiveModel();
                    System.out.println("Importing TWiki topic: " + file.getAbsolutePath());
                    final SwingWorker worker = new SwingWorker() {

                        public Object construct() {
                            ProgressDialogJFrame progressDialogJFrame = new ProgressDialogJFrame("TWiki Import", "<html>&nbsp;&nbsp;<b>Processing topic:</b>&nbsp;&nbsp;</html>");
                            try {
                                MindRaider.notebookCustodian.importNotebook(NotebookCustodian.FORMAT_TWIKI, (file != null ? file.getAbsolutePath() : null), progressDialogJFrame);
                            } finally {
                                if (progressDialogJFrame != null) {
                                    progressDialogJFrame.dispose();
                                }
                            }
                            return null;
                        }
                    };
                    worker.start();
                } else {
                    System.out.println("Open command cancelled by user.");
                }
            }
        });
        submenu.add(subMenuItem);
        menu.add(submenu);
        menu.addSeparator();
        menuItem = new JMenuItem("Details...");
        menuItem.setMnemonic(KeyEvent.VK_E);
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                new RdfModelDetailsJDialog();
            }
        });
        menu.add(menuItem);
        menuBar.add(menu);
        menu = new JMenu("Concept");
        menu.setMnemonic(KeyEvent.VK_C);
        menuItem = new JMenuItem("New...");
        menuItem.setMnemonic(KeyEvent.VK_N);
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                NotebookOutlineJPanel.getInstance().conceptNew();
            }
        });
        menu.add(menuItem);
        menuItem = new JMenuItem("Open...");
        menuItem.setMnemonic(KeyEvent.VK_O);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK | ActionEvent.SHIFT_MASK));
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (MindRaider.profile.activeNotebookUri != null) {
                    new OpenConceptJDialog();
                }
            }
        });
        menu.add(menuItem);
        menuItem = new JMenuItem("Discard");
        menuItem.setMnemonic(KeyEvent.VK_D);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                NotebookOutlineJPanel.getInstance().conceptDiscard();
            }
        });
        menu.add(menuItem);
        menu.addSeparator();
        menuItem = new JMenuItem("Up");
        menuItem.setMnemonic(KeyEvent.VK_U);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_UP, ActionEvent.CTRL_MASK));
        menuItem.setEnabled(true);
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                NotebookOutlineJPanel.getInstance().conceptUp();
            }
        });
        menu.add(menuItem);
        menuItem = new JMenuItem("Promote");
        menuItem.setMnemonic(KeyEvent.VK_P);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, ActionEvent.CTRL_MASK));
        menuItem.setEnabled(true);
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                NotebookOutlineJPanel.getInstance().conceptPromote();
            }
        });
        menu.add(menuItem);
        menuItem = new JMenuItem("Demote");
        menuItem.setMnemonic(KeyEvent.VK_D);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, ActionEvent.CTRL_MASK));
        menuItem.setEnabled(true);
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                NotebookOutlineJPanel.getInstance().conceptDemote();
            }
        });
        menu.add(menuItem);
        menuItem = new JMenuItem("Down");
        menuItem.setMnemonic(KeyEvent.VK_O);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, ActionEvent.CTRL_MASK));
        menuItem.setEnabled(true);
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                NotebookOutlineJPanel.getInstance().conceptDown();
            }
        });
        menu.add(menuItem);
        menuBar.add(menu);
        if (!MindRaider.OUTLINER_PERSPECTIVE.equals(MindRaider.profile.getUiPerspective())) {
            menu = new JMenu("Model");
            menu.setMnemonic(KeyEvent.VK_O);
            menu.getAccessibleContext().setAccessibleDescription("RDF Model");
            menuItem = new JMenuItem("New...", KeyEvent.VK_N);
            menuItem.setMnemonic(KeyEvent.VK_N);
            menuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    MindRaider.profile.activeNotebookUri = null;
                    NotebookOutlineJPanel.getInstance().clear();
                    MindRaider.profile.deleteActiveModel();
                    spiders.clear();
                    showSpidersGraphOnly();
                    MindRaider.setModeRdf();
                    new NewRdfModelJDialog();
                }
            });
            menu.add(menuItem);
            menuItem = new JMenu("Open from");
            menuItem.setMnemonic(KeyEvent.VK_O);
            subMenuItem = new JMenuItem("File...");
            subMenuItem.setMnemonic(KeyEvent.VK_F);
            subMenuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    NotebookOutlineJPanel.getInstance().clear();
                    MindRaider.profile.activeNotebookUri = null;
                    spiders.clear();
                    showSpidersGraphOnly();
                    MindRaider.setModeRdf();
                    JFileChooser fc = new JFileChooser();
                    fc.setCurrentDirectory(new File(MindRaider.modelCustodian.getModelsNest()));
                    fc.setDialogTitle("Open Model");
                    int returnVal = fc.showOpenDialog(MindRaiderJFrame.this);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        File file = fc.getSelectedFile();
                        MindRaider.profile.deleteActiveModel();
                        cat.debug("Opening model: " + file.getAbsolutePath());
                        try {
                            spiders.load(file.getAbsolutePath());
                        } catch (Exception e1) {
                            JOptionPane.showMessageDialog(MindRaiderJFrame.this, "Unable to load RDF model: " + e1.getMessage(), "Load Model Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        StatusBar.show("Loaded model " + file.getAbsolutePath());
                    } else {
                        System.out.println("Open command cancelled by user.");
                    }
                }
            });
            menuItem.add(subMenuItem);
            subMenuItem = new JMenuItem("URL...");
            subMenuItem.setMnemonic(KeyEvent.VK_U);
            subMenuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    NotebookOutlineJPanel.getInstance().clear();
                    MindRaider.profile.activeNotebookUri = null;
                    MindRaider.profile.deleteActiveModel();
                    spiders.clear();
                    showSpidersGraphOnly();
                    MindRaider.setModeRdf();
                    new DownloadModelJDialog(false);
                }
            });
            menuItem.add(subMenuItem);
            menu.add(menuItem);
            menuItem = new JMenu("Add from");
            menuItem.setMnemonic(KeyEvent.VK_O);
            subMenuItem = new JMenuItem("File...");
            subMenuItem.setMnemonic(KeyEvent.VK_F);
            subMenuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    NotebookOutlineJPanel.getInstance().clear();
                    MindRaider.profile.activeNotebookUri = null;
                    showSpidersGraphOnly();
                    MindRaider.setModeRdf();
                    JFileChooser fc = new JFileChooser();
                    int returnVal = fc.showOpenDialog(MindRaiderJFrame.this);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        File file = fc.getSelectedFile();
                        MindRaider.profile.deleteActiveModel();
                        System.out.println("Opening model: " + file.getAbsolutePath());
                        spiders.addModel(file.getAbsolutePath());
                        StatusBar.show("Added model " + file.getAbsolutePath());
                    } else {
                        System.out.println("Open command cancelled by user.");
                    }
                }
            });
            menuItem.add(subMenuItem);
            subMenuItem = new JMenuItem("URL...");
            subMenuItem.setMnemonic(KeyEvent.VK_U);
            subMenuItem.setMnemonic(KeyEvent.VK_U);
            subMenuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    NotebookOutlineJPanel.getInstance().clear();
                    MindRaider.profile.activeNotebookUri = null;
                    showSpidersGraphOnly();
                    MindRaider.setModeRdf();
                    new DownloadModelJDialog(true);
                }
            });
            menuItem.add(subMenuItem);
            menu.add(menuItem);
            menuItem = new JMenuItem("Save");
            menuItem.setMnemonic(KeyEvent.VK_S);
            menuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    if (spiders.save()) {
                        StatusBar.show("Model " + spiders.getRdfModel() + " saved!");
                    } else {
                        StatusBar.show("Model not saved.");
                    }
                }
            });
            menu.add(menuItem);
            menuItem = new JMenuItem("Save as...");
            menuItem.setMnemonic(KeyEvent.VK_A);
            menu.add(menuItem);
            menuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    String filename = null;
                    JFileChooser fc = new JFileChooser();
                    fc.setCurrentDirectory(new File(MindRaider.modelCustodian.getModelsNest()));
                    fc.setDialogTitle("Save Model As...");
                    int returnVal = fc.showSaveDialog(MindRaiderJFrame.this);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        File file = fc.getSelectedFile();
                        System.out.println("Saving model: " + file.getAbsolutePath());
                        MindRaider.spidersGraph.saveAs(file.getAbsolutePath());
                    }
                    if (spiders.saveAs(filename)) {
                        StatusBar.show("Model " + spiders.getRdfModel() + " saved!");
                    } else {
                        StatusBar.show("Model not saved.");
                    }
                }
            });
            menuItem = new JMenuItem("Dump");
            menuItem.setMnemonic(KeyEvent.VK_D);
            menu.add(menuItem);
            menuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    spiders.getRdfModel().show();
                }
            });
            menuItem = new JMenuItem("Close", KeyEvent.VK_C);
            menuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    NotebookOutlineJPanel.getInstance().clear();
                    spiders.clear();
                }
            });
            menuItem.setMnemonic(KeyEvent.VK_C);
            menu.add(menuItem);
            menu.addSeparator();
            if (MindRaider.EXPERIMENTAL_PERSPECTIVE.equals(MindRaider.profile.getUiPerspective())) {
                submenu = new JMenu("Serialization");
                submenu.setMnemonic(KeyEvent.VK_D);
                ButtonGroup serializationGroup = new ButtonGroup();
                rbMenuItem = new JRadioButtonMenuItem("RDF XML");
                rbMenuItem.setSelected(true);
                rbMenuItem.setMnemonic(KeyEvent.VK_R);
                serializationGroup.add(rbMenuItem);
                submenu.add(rbMenuItem);
                rbMenuItem = new JRadioButtonMenuItem("SiRS");
                rbMenuItem.setSelected(true);
                rbMenuItem.setMnemonic(KeyEvent.VK_S);
                serializationGroup.add(rbMenuItem);
                submenu.add(rbMenuItem);
                rbMenuItem = new JRadioButtonMenuItem("Triple");
                rbMenuItem.setSelected(true);
                rbMenuItem.setMnemonic(KeyEvent.VK_T);
                serializationGroup.add(rbMenuItem);
                submenu.add(rbMenuItem);
                menu.add(submenu);
            }
            menuItem = new JMenuItem("Details...");
            menuItem.setMnemonic(KeyEvent.VK_D);
            menuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    new RdfModelDetailsJDialog();
                }
            });
            menu.add(menuItem);
            menuBar.add(menu);
        }
        if (MindRaider.EXPERIMENTAL_PERSPECTIVE.equals(MindRaider.profile.getUiPerspective())) {
            menu = new JMenu("MEP");
            menu.setMnemonic(KeyEvent.VK_E);
            menu.getAccessibleContext().setAccessibleDescription("Model Exposer Plugin");
            menuItem = new JMenuItem("Install...", KeyEvent.VK_I);
            menuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                }
            });
            menuItem.setMnemonic(KeyEvent.VK_I);
            menu.add(menuItem);
            menuItem = new JMenuItem("Configure...", KeyEvent.VK_C);
            menuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                }
            });
            menuItem.setMnemonic(KeyEvent.VK_C);
            menu.add(menuItem);
            menuItem = new JMenuItem("Remove...", KeyEvent.VK_R);
            menuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                }
            });
            menuItem.setMnemonic(KeyEvent.VK_R);
            menu.add(menuItem);
            menuBar.add(menu);
        }
        if (MindRaider.EXPERIMENTAL_PERSPECTIVE.equals(MindRaider.profile.getUiPerspective())) {
            menu = new JMenu("P2P");
            menu.setMnemonic(KeyEvent.VK_P);
            menu.getAccessibleContext().setAccessibleDescription("Shared mind");
            menuItem = new JMenuItem("Attach Mind...", KeyEvent.VK_I);
            menuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                }
            });
            menuItem.setMnemonic(KeyEvent.VK_I);
            menu.add(menuItem);
            menuItem = new JMenuItem("Synchronize", KeyEvent.VK_C);
            menuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                }
            });
            menuItem.setMnemonic(KeyEvent.VK_C);
            menu.add(menuItem);
            menuItem = new JMenuItem("Configure...", KeyEvent.VK_R);
            menuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                }
            });
            menuItem.setMnemonic(KeyEvent.VK_R);
            menu.add(menuItem);
            menuBar.add(menu);
        }
        menu = new JMenu("Search");
        menu.setMnemonic(KeyEvent.VK_S);
        menuItem = new JMenuItem("Search...", KeyEvent.VK_S);
        menuItem.setEnabled(false);
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.CTRL_MASK));
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
            }
        });
        menu.add(menuItem);
        menuItem = new JMenuItem("Rebuild Search Index");
        menuItem.setMnemonic(KeyEvent.VK_R);
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                SearchCommander.rebuildSearchIndex();
            }
        });
        menu.add(menuItem);
        menuBar.add(menu);
        menu = new JMenu("Tools");
        menu.setMnemonic(KeyEvent.VK_T);
        menuItem = new JMenuItem("Backup Repository");
        menuItem.setMnemonic(KeyEvent.VK_B);
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Installer.backupRepositoryAsync();
            }
        });
        menu.add(menuItem);
        if (!MindRaider.OUTLINER_PERSPECTIVE.equals(MindRaider.profile.getUiPerspective())) {
            menu.addSeparator();
            submenu = new JMenu("URIQA Browser");
            submenu.setMnemonic(KeyEvent.VK_U);
            menuItem = new JMenuItem("Connect to server...");
            menuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    MindRaider.profile.activeNotebookUri = null;
                    NotebookOutlineJPanel.getInstance().clear();
                    MindRaider.profile.deleteActiveModel();
                    spiders.clear();
                    showSpidersGraphOnly();
                    MindRaider.setModeUriqa();
                    new ConnectUriqaServerJDialog();
                }
            });
            submenu.add(menuItem);
            menuItem = new JMenuItem("Disconnect");
            menuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                }
            });
            submenu.add(menuItem);
            menu.add(submenu);
        }
        menuBar.add(menu);
        if (MindRaider.EXPERIMENTAL_PERSPECTIVE.equals(MindRaider.profile.getUiPerspective())) {
            menu = new JMenu("Window");
            menu.setMnemonic(KeyEvent.VK_W);
            menuBar.add(menu);
        }
        menu = new JMenu("Help");
        menu.setMnemonic(KeyEvent.VK_H);
        menuItem = new JMenuItem("Documentation");
        menuItem.setMnemonic(KeyEvent.VK_D);
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    MindRaider.notebookCustodian.loadNotebook(new URI(MindRaiderVocabulary.getNotebookUri(NotebookCustodian.MR_DOC_NOTEBOOK_DOCUMENTATION_LOCAL_NAME)));
                    NotebookOutlineJPanel.getInstance().refresh();
                } catch (Exception e1) {
                    cat.error("Error: unable to load 'MR help' notebook!", e1);
                }
            }
        });
        menu.add(menuItem);
        menu.addSeparator();
        menuItem = new JMenuItem("Homepage");
        menuItem.setMnemonic(KeyEvent.VK_H);
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Launcher.launchInBrowser("http://mindraider.sourceforge.net");
            }
        });
        menu.add(menuItem);
        menuItem = new JMenuItem("Check for " + MR_TITLE + " Updates");
        menuItem.setMnemonic(KeyEvent.VK_F);
        menuItem.setEnabled(false);
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
            }
        });
        menu.add(menuItem);
        menu.addSeparator();
        menuItem = new JMenuItem("About " + MR_TITLE);
        menuItem.setMnemonic(KeyEvent.VK_A);
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                new AboutJDialog();
            }
        });
        menu.add(menuItem);
        menuBar.add(menu);
    }

    private void showSpidersGraphOnly() {
        if (leftSidebarSplitPane.getDividerLocation() > 1) {
            closeLeftSidebar();
        }
        NotebookOutlineJPanel.getInstance().hideRightSiderbar();
        NotebookOutlineJPanel.getInstance().hideConceptTree();
        NotebookOutlineJPanel.getInstance().disableAllToolbarButtons();
    }

    public void closeLeftSidebar() {
        leftSidebarSplitPane.setDividerLocation(0);
    }

    private boolean latelyMaximized = false;

    public void maximizeLeftSidebar(JButton maximizeButton) {
        if (latelyMaximized) {
            restoreLeftSidebar();
            if (maximizeButton != null) {
                maximizeButton.setIcon(IconsRegistry.getImageIcon("explorerMaximizeRight.png"));
                maximizeButton.setToolTipText("Maximize Explorer siderbar");
            }
            latelyMaximized = false;
        } else {
            leftSidebarSplitPane.setDividerLocation(2000);
            if (maximizeButton != null) {
                maximizeButton.setIcon(IconsRegistry.getImageIcon("explorerRestoreLeft.png"));
                maximizeButton.setToolTipText("Restore Explorer siderbar");
            }
            latelyMaximized = true;
        }
    }

    public void restoreLeftSidebar() {
        leftSidebarSplitPane.setDividerLocation(leftSidebarSplitPane.getLastDividerLocation());
    }

    private static class FacetActionListener implements ActionListener {

        private String facetLabel;

        /**
         * Constructor.
         * 
         * @param facetLabel
         */
        public FacetActionListener(String facetLabel) {
            this.facetLabel = facetLabel;
        }

        public void actionPerformed(ActionEvent arg0) {
            MindRaider.spidersGraph.setFacet(FacetCustodian.getInstance().getFacet(facetLabel));
            MindRaider.spidersGraph.renderModel();
        }
    }

    public DragAndDropReference dragAndDropReference = null;

    public void dragEnter(DropTargetDragEvent arg0) {
    }

    public void dragOver(DropTargetDragEvent arg0) {
    }

    public void dropActionChanged(DropTargetDragEvent arg0) {
    }

    public void drop(DropTargetDropEvent evt) {
        cat.debug("=-> drop");
        try {
            Transferable t = evt.getTransferable();
            if (t.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                cat.debug(" Accepting 'string' data flavor...");
                evt.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                String s = (String) t.getTransferData(DataFlavor.stringFlavor);
                evt.getDropTargetContext().dropComplete(true);
                cat.debug("DnD: '" + s + "'");
                if (s != null) {
                    int indexOf = s.indexOf("\n");
                    if (indexOf != -1) {
                        dragAndDropReference = new DragAndDropReference(s.substring(indexOf + 1), s.substring(0, indexOf), DragAndDropReference.BROWSER_LINK);
                    } else {
                        dragAndDropReference = new DragAndDropReference(s, DragAndDropReference.BROWSER_LINK);
                    }
                }
            } else {
                if (t.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                    cat.debug(" Accepting 'file list' data flavor...");
                    evt.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
                    List list = (List) t.getTransferData(DataFlavor.javaFileListFlavor);
                    if (list != null) {
                        Iterator iterator = list.iterator();
                        while (iterator.hasNext()) {
                            Object next = iterator.next();
                            if (next instanceof File) {
                                cat.debug(" DnD file: " + next);
                                dragAndDropReference = new DragAndDropReference(((File) next).getAbsolutePath(), DragAndDropReference.EXPLORER_LINK);
                            }
                        }
                    }
                } else {
                    cat.debug("DnD rejected! ");
                    dragAndDropReference = null;
                }
            }
        } catch (Exception e) {
            cat.debug("Drag&Drop error:", e);
            dragAndDropReference = null;
        }
        NotebookOutlineJPanel.getInstance().enableDisableAttachToolbarButton();
        if (dragAndDropReference != null) {
            JOptionPane.showMessageDialog(this, "Dropped local/web resource reference stored! Use \n'clip' icon from Notebook outline toolbar to attach it.", "Drag&Drop Info", JOptionPane.INFORMATION_MESSAGE);
            dragAndDropReference.debug();
        }
        cat.debug("<-= drop");
    }

    public void dragExit(DropTargetEvent arg0) {
    }

    /**
     * Set look and feel.
     * 
     * @param lookAndFeel look and feel to be set.
     */
    private void setLookAndFeel(String lookAndFeel) {
        MindRaider.profile.setLookAndFeel(lookAndFeel);
        MindRaider.profile.save();
        JOptionPane.showMessageDialog(MindRaiderJFrame.this, "To apply new L&F please restart " + MindRaiderConstants.MR_TITLE + "!");
    }

    /**
     * Set perspective.
     * 
     * @param perspective perspective to be set.
     */
    private void setPerspective(String perspective) {
        MindRaider.profile.setUiPerspective(perspective);
        MindRaider.profile.save();
        JOptionPane.showMessageDialog(MindRaiderJFrame.this, "To change perspective please restart " + MindRaiderConstants.MR_TITLE + "!");
    }
}
