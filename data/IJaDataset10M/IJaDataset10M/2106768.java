package com.googlecode.simpleret.viewer.ui;

import static java.awt.event.InputEvent.CTRL_MASK;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import com.googlecode.simpleret.Constants;
import com.googlecode.simpleret.viewer.FrameConstants;
import com.googlecode.simpleret.viewer.Viewer;

public class FrameMainMenu {

    private JMenuBar jJMenuBar = null;

    private JMenu jMenuFile = null;

    private JMenu jMenuEdit = null;

    private JMenuItem jMenuItemOpenProj = null;

    private JMenuItem jMenuItemImportNewProj = null;

    private JMenuItem jMenuItemExportHTML = null;

    private JMenuItem jMenuItemExportAUML = null;

    private JMenuItem jMenuItemInstrBox = null;

    private JMenuItem jMenuItemSignat1 = null;

    private JMenuItem jMenuItemSignat2 = null;

    private JMenuItem jMenuItemSignat3 = null;

    private JMenu jMenuView = null;

    private JMenuItem jMenuItemSetRange = null;

    private JMenuItem jMenuItemDeepness = null;

    private JMenu jMenuHelp = null;

    private JMenuItem jMenuItemHelp = null;

    private JMenuItem jMenuItemExit = null;

    private JCheckBoxMenuItem jCheckBoxMenuItemShowColor = null;

    private JCheckBoxMenuItem jCheckBoxMenuItemShowRange = null;

    private Viewer viewer = null;

    public FrameMainMenu(Viewer viewer) {
        this.viewer = viewer;
    }

    /**
	 * This method initializes jJMenuBar
	 * 
	 * @return javax.swing.JMenuBar
	 */
    public JMenuBar getJJMenuBar() {
        if (jJMenuBar == null) {
            jJMenuBar = new JMenuBar();
            jJMenuBar.add(getJMenuFile());
            jJMenuBar.add(getJMenuEdit());
            jJMenuBar.add(getJMenuView());
            jJMenuBar.add(getJMenuHelp());
        }
        return jJMenuBar;
    }

    /**
	 * This method initializes jMenuFile
	 * 
	 * @return javax.swing.JMenu
	 */
    private JMenu getJMenuFile() {
        if (jMenuFile == null) {
            jMenuFile = new JMenu();
            jMenuFile.setName("");
            jMenuFile.setText("File");
            jMenuFile.setHorizontalTextPosition(SwingConstants.TRAILING);
            jMenuFile.setHorizontalAlignment(SwingConstants.LEADING);
            jMenuFile.add(getJMenuItemExportHTML());
            jMenuFile.add(getJMenuItemExportAUML());
            jMenuFile.add(getJMenuItemExit());
        }
        return jMenuFile;
    }

    /**
	 * This method initializes jMenuEdit
	 * 
	 * @return javax.swing.JMenu
	 */
    private JMenu getJMenuEdit() {
        if (jMenuEdit == null) {
            jMenuEdit = new JMenu();
            jMenuEdit.setText("Edit");
            jMenuEdit.add(getJMenuItemInstrBox());
            jMenuEdit.add(getJMenuItemSignat1());
            jMenuEdit.add(getJMenuItemSignat2());
            jMenuEdit.add(getJMenuItemSignat3());
        }
        return jMenuEdit;
    }

    /**
	 * This method initializes jMenuItemOpenProj
	 * 
	 * @return javax.swing.JMenuItem
	 */
    private JMenuItem getJMenuItemOpenProj() {
        if (jMenuItemOpenProj == null) {
            jMenuItemOpenProj = new JMenuItem();
            jMenuItemOpenProj.setPreferredSize(new Dimension(150, 20));
            jMenuItemOpenProj.setText("Open Project ...");
            jMenuItemOpenProj.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, CTRL_MASK));
            jMenuItemOpenProj.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    viewer.firePropertyChange(FrameConstants.EVENT_OPEN_PROJECT, 0, 1);
                }
            });
        }
        return jMenuItemOpenProj;
    }

    /**
	 * This method initializes jMenuItemImportNewProj
	 * 
	 * @return javax.swing.JMenuItem
	 */
    private JMenuItem getJMenuItemImportNewProj() {
        if (jMenuItemImportNewProj == null) {
            jMenuItemImportNewProj = new JMenuItem();
            jMenuItemImportNewProj.setPreferredSize(new Dimension(200, 20));
            jMenuItemImportNewProj.setText("Import New Project ...");
            jMenuItemImportNewProj.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F3, CTRL_MASK));
            jMenuItemImportNewProj.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    viewer.firePropertyChange(FrameConstants.EVENT_IMPORT_PROJECT, 0, 1);
                }
            });
        }
        return jMenuItemImportNewProj;
    }

    /**
	 * This method initializes jMenuItemExportHTML
	 * 
	 * @return javax.swing.JMenuItem
	 */
    private JMenuItem getJMenuItemExportHTML() {
        if (jMenuItemExportHTML == null) {
            jMenuItemExportHTML = new JMenuItem();
            if (Constants.isWebStartMode()) {
                jMenuItemExportHTML.setEnabled(false);
                jMenuItemExportHTML.setToolTipText(Constants.DISABLED_IN_WEB_START_MODE);
            }
            jMenuItemExportHTML.setPreferredSize(new Dimension(150, 20));
            jMenuItemExportHTML.setText("Export to HTML ...");
            jMenuItemExportHTML.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, CTRL_MASK));
            jMenuItemExportHTML.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    viewer.firePropertyChange(FrameConstants.EVENT_EXPORT_HTML, 0, 1);
                }
            });
        }
        return jMenuItemExportHTML;
    }

    /**
	 * This method initializes jMenuItemExportAUML
	 * 
	 * @return javax.swing.JMenuItem
	 */
    private JMenuItem getJMenuItemExportAUML() {
        if (jMenuItemExportAUML == null) {
            jMenuItemExportAUML = new JMenuItem();
            if (Constants.isWebStartMode()) {
                jMenuItemExportAUML.setEnabled(false);
                jMenuItemExportAUML.setToolTipText(Constants.DISABLED_IN_WEB_START_MODE);
            }
            jMenuItemExportAUML.setPreferredSize(new Dimension(200, 20));
            jMenuItemExportAUML.setText("Export to AmaterasUML ...");
            jMenuItemExportAUML.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, CTRL_MASK));
            jMenuItemExportAUML.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    viewer.firePropertyChange(FrameConstants.EVENT_EXPORT_AMATERAS_UML, 0, 1);
                }
            });
        }
        return jMenuItemExportAUML;
    }

    /**
	 * This method initializes jMenuItemInstrBox
	 * 
	 * @return javax.swing.JMenuItem
	 */
    private JMenuItem getJMenuItemInstrBox() {
        if (jMenuItemInstrBox == null) {
            jMenuItemInstrBox = new JMenuItem();
            jMenuItemInstrBox.setPreferredSize(new Dimension(200, 20));
            jMenuItemInstrBox.setText("Instrumental Box ...");
            jMenuItemInstrBox.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_0, 0));
            jMenuItemInstrBox.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    viewer.firePropertyChange(FrameConstants.EVENT_INSTRUMENTAL_BOX, 0, 1);
                }
            });
        }
        return jMenuItemInstrBox;
    }

    /**
	 * This method initializes jMenuItemSignat1
	 * 
	 * @return javax.swing.JMenuItem
	 */
    private JMenuItem getJMenuItemSignat1() {
        if (jMenuItemSignat1 == null) {
            jMenuItemSignat1 = new JMenuItem();
            jMenuItemSignat1.setPreferredSize(new Dimension(200, 20));
            jMenuItemSignat1.setText("List of Signatures N1");
            jMenuItemSignat1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, 0));
            jMenuItemSignat1.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    viewer.firePropertyChange(FrameConstants.EVENT_EDIT_SIGNATURES, 0, 1);
                }
            });
        }
        return jMenuItemSignat1;
    }

    /**
	 * This method initializes jMenuItemSignat2
	 * 
	 * @return javax.swing.JMenuItem
	 */
    private JMenuItem getJMenuItemSignat2() {
        if (jMenuItemSignat2 == null) {
            jMenuItemSignat2 = new JMenuItem();
            jMenuItemSignat2.setPreferredSize(new Dimension(200, 20));
            jMenuItemSignat2.setText("List of Signatures N2");
            jMenuItemSignat2.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, 0));
            jMenuItemSignat2.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    viewer.firePropertyChange(FrameConstants.EVENT_EDIT_SIGNATURES, 0, 2);
                }
            });
        }
        return jMenuItemSignat2;
    }

    /**
	 * This method initializes jMenuItemSignat3
	 * 
	 * @return javax.swing.JMenuItem
	 */
    private JMenuItem getJMenuItemSignat3() {
        if (jMenuItemSignat3 == null) {
            jMenuItemSignat3 = new JMenuItem();
            jMenuItemSignat3.setPreferredSize(new Dimension(200, 20));
            jMenuItemSignat3.setText("List of Signatures N3");
            jMenuItemSignat3.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3, 0));
            jMenuItemSignat3.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    viewer.firePropertyChange(FrameConstants.EVENT_EDIT_SIGNATURES, 0, 3);
                }
            });
        }
        return jMenuItemSignat3;
    }

    /**
	 * This method initializes jMenuView
	 * 
	 * @return javax.swing.JMenu
	 */
    private JMenu getJMenuView() {
        if (jMenuView == null) {
            jMenuView = new JMenu();
            jMenuView.setText("View");
            jMenuView.add(getJCheckBoxMenuItemShowColor());
            jMenuView.add(new JSeparator());
            jMenuView.add(getJCheckBoxMenuItemShowRange());
            jMenuView.add(getJMenuItemSetRange());
            jMenuView.add(new JSeparator());
            jMenuView.add(getJMenuItemDeepness());
        }
        return jMenuView;
    }

    /**
	 * This method initializes jMenuItemSetRange
	 * 
	 * @return javax.swing.JMenuItem
	 */
    private JMenuItem getJMenuItemSetRange() {
        if (jMenuItemSetRange == null) {
            jMenuItemSetRange = new JMenuItem();
            jMenuItemSetRange.setPreferredSize(new Dimension(200, 20));
            jMenuItemSetRange.setText("Set Range ...");
            jMenuItemSetRange.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_9, CTRL_MASK));
            jMenuItemSetRange.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    viewer.firePropertyChange(FrameConstants.EVENT_INPUT_RANGE, 0, 1);
                }
            });
        }
        return jMenuItemSetRange;
    }

    /**
	 * This method initializes jMenuItemDeepness
	 * 
	 * @return javax.swing.JMenuItem
	 */
    private JMenuItem getJMenuItemDeepness() {
        if (jMenuItemDeepness == null) {
            jMenuItemDeepness = new JMenuItem();
            jMenuItemDeepness.setPreferredSize(new Dimension(200, 20));
            jMenuItemDeepness.setText("Deepness ...");
            jMenuItemDeepness.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, 0));
            jMenuItemDeepness.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    viewer.firePropertyChange(FrameConstants.EVENT_INPUT_DEEPNESS, 0, 1);
                }
            });
        }
        return jMenuItemDeepness;
    }

    /**
	 * This method initializes jMenuHelp
	 * 
	 * @return javax.swing.JMenu
	 */
    private JMenu getJMenuHelp() {
        if (jMenuHelp == null) {
            jMenuHelp = new JMenu();
            jMenuHelp.setText("Help");
            jMenuHelp.add(getJMenuItemHelp());
        }
        return jMenuHelp;
    }

    /**
	 * This method initializes jMenuItemHelp
	 * 
	 * @return javax.swing.JMenuItem
	 */
    private JMenuItem getJMenuItemHelp() {
        if (jMenuItemHelp == null) {
            jMenuItemHelp = new JMenuItem();
            jMenuItemHelp.setPreferredSize(new Dimension(100, 20));
            jMenuItemHelp.setHorizontalAlignment(SwingConstants.LEADING);
            jMenuItemHelp.setHorizontalTextPosition(SwingConstants.TRAILING);
            jMenuItemHelp.setText("About");
            jMenuItemHelp.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
            jMenuItemHelp.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    viewer.firePropertyChange(FrameConstants.EVENT_HELP, 0, 1);
                }
            });
        }
        return jMenuItemHelp;
    }

    /**
	 * This method initializes jMenuItemExit
	 * 
	 * @return javax.swing.JMenuItem
	 */
    private JMenuItem getJMenuItemExit() {
        if (jMenuItemExit == null) {
            jMenuItemExit = new JMenuItem();
            jMenuItemExit.setPreferredSize(new Dimension(50, 20));
            jMenuItemExit.setText("Exit");
            jMenuItemExit.setToolTipText("");
            jMenuItemExit.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    viewer.firePropertyChange(FrameConstants.EVENT_EXIT, 0, 1);
                }
            });
        }
        return jMenuItemExit;
    }

    /**
	 * This method initializes jCheckBoxMenuItemShowColor
	 * 
	 * @return javax.swing.JCheckBoxMenuItem
	 */
    private JCheckBoxMenuItem getJCheckBoxMenuItemShowColor() {
        if (jCheckBoxMenuItemShowColor == null) {
            jCheckBoxMenuItemShowColor = new JCheckBoxMenuItem();
            jCheckBoxMenuItemShowColor.setText("Show Colorized");
            jCheckBoxMenuItemShowColor.setPreferredSize(new Dimension(200, 20));
            jCheckBoxMenuItemShowColor.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, 0));
            jCheckBoxMenuItemShowColor.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    viewer.firePropertyChange(FrameConstants.EVENT_SHOW_COLORIZED, 0, 1);
                }
            });
        }
        return jCheckBoxMenuItemShowColor;
    }

    /**
	 * This method initializes jCheckBoxMenuItemShowRange
	 * 
	 * @return javax.swing.JCheckBoxMenuItem
	 */
    private JCheckBoxMenuItem getJCheckBoxMenuItemShowRange() {
        if (jCheckBoxMenuItemShowRange == null) {
            jCheckBoxMenuItemShowRange = new JCheckBoxMenuItem();
            jCheckBoxMenuItemShowRange.setText("Show Range");
            jCheckBoxMenuItemShowRange.setPreferredSize(new Dimension(200, 20));
            jCheckBoxMenuItemShowRange.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, 0));
            jCheckBoxMenuItemShowRange.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    viewer.firePropertyChange(FrameConstants.EVENT_SHOW_RANGE, 0, 1);
                }
            });
        }
        return jCheckBoxMenuItemShowRange;
    }
}
