package planning.editor;

import java.awt.GridBagLayout;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JSplitPane;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import java.awt.GridBagConstraints;

public class PlannerEditor extends JPanel {

    private static final long serialVersionUID = 1L;

    public IPlannerEditorController listener;

    private JPanel jPanel = null;

    private JPanel jPanel3 = null;

    private JPanel jPanel11 = null;

    private JButton jButtonAdd = null;

    private JButton jButtonRemove = null;

    private JButton jButtonClear = null;

    private JLabel jLabel = null;

    private JButton jButtonLoad = null;

    private JButton jButtonSave = null;

    private JPanel jPanel4 = null;

    private JSplitPane jSplitPane = null;

    private JPanel jPanel1 = null;

    private JPanel jPanelCenter = null;

    private JScrollPane jScrollPane = null;

    private JTree jTreePlanner = null;

    private JButton jButton = null;

    private JButton jButton1 = null;

    private JLabel jLabel1 = null;

    private JLabel jLabel2 = null;

    private JButton jButtonReLoad = null;

    private JButton jButtonRecents = null;

    private JButton jButtonAddAll = null;

    /**
	 * This is the default constructor
	 */
    public PlannerEditor(IPlannerEditorController listener) {
        super();
        this.listener = listener;
        initialize();
    }

    /**
	 * This method initializes this
	 * 
	 * @return void
	 */
    private void initialize() {
        this.setSize(740, 360);
        this.setPreferredSize(new Dimension(740, 360));
        this.setLayout(new BorderLayout());
        this.add(getJPanel(), BorderLayout.NORTH);
        this.add(getJPanel3(), BorderLayout.SOUTH);
        this.add(getJSplitPane(), BorderLayout.CENTER);
    }

    /**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    private JPanel getJPanel() {
        if (jPanel == null) {
            jPanel = new JPanel();
            jPanel.setLayout(new BorderLayout());
            jPanel.add(getJPanel11(), BorderLayout.WEST);
            jPanel.add(getJPanel4(), BorderLayout.CENTER);
        }
        return jPanel;
    }

    /**
	 * This method initializes jPanel3	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    private JPanel getJPanel3() {
        if (jPanel3 == null) {
            jLabel1 = new JLabel();
            jLabel1.setText("  ");
            jPanel3 = new JPanel();
            jPanel3.setLayout(new GridBagLayout());
            jPanel3.add(getJButton(), new GridBagConstraints());
            jPanel3.add(jLabel1, new GridBagConstraints());
            jPanel3.add(getJButton1(), new GridBagConstraints());
        }
        return jPanel3;
    }

    /**
	 * This method initializes jPanel11	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    private JPanel getJPanel11() {
        if (jPanel11 == null) {
            jLabel2 = new JLabel();
            jLabel2.setText("   ");
            jLabel = new JLabel();
            jLabel.setText("   ");
            jPanel11 = new JPanel();
            jPanel11.setLayout(new FlowLayout());
            jPanel11.add(getJButtonAdd(), null);
            jPanel11.add(getJButtonAddAll(), null);
            jPanel11.add(getJButtonRemove(), null);
            jPanel11.add(getJButtonClear(), null);
            jPanel11.add(jLabel, null);
            jPanel11.add(getJButtonLoad(), null);
            jPanel11.add(getJButtonSave(), null);
            jPanel11.add(jLabel2, null);
            jPanel11.add(getJButtonReLoad(), null);
            jPanel11.add(getJButtonRecents(), null);
        }
        return jPanel11;
    }

    /**
	 * This method initializes jButtonAdd	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getJButtonAdd() {
        if (jButtonAdd == null) {
            jButtonAdd = new JButton();
            jButtonAdd.setText("Add");
            jButtonAdd.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    if (listener != null) {
                        listener.add();
                    }
                }
            });
        }
        return jButtonAdd;
    }

    /**
	 * This method initializes jButtonRemove	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getJButtonRemove() {
        if (jButtonRemove == null) {
            jButtonRemove = new JButton();
            jButtonRemove.setText("Remove");
            jButtonRemove.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    if (listener != null) {
                        listener.remove();
                    }
                }
            });
        }
        return jButtonRemove;
    }

    /**
	 * This method initializes jButtonClear	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getJButtonClear() {
        if (jButtonClear == null) {
            jButtonClear = new JButton();
            jButtonClear.setText("Clear");
            jButtonClear.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    if (listener != null) {
                        listener.clear();
                    }
                }
            });
        }
        return jButtonClear;
    }

    /**
	 * This method initializes jButtonLoad	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getJButtonLoad() {
        if (jButtonLoad == null) {
            jButtonLoad = new JButton();
            jButtonLoad.setText("Load");
            jButtonLoad.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    if (listener != null) {
                        listener.load();
                    }
                }
            });
        }
        return jButtonLoad;
    }

    /**
	 * This method initializes jButtonSave	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getJButtonSave() {
        if (jButtonSave == null) {
            jButtonSave = new JButton();
            jButtonSave.setText("Save");
            jButtonSave.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    if (listener != null) {
                        listener.save();
                    }
                }
            });
        }
        return jButtonSave;
    }

    /**
	 * This method initializes jPanel4	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    private JPanel getJPanel4() {
        if (jPanel4 == null) {
            jPanel4 = new JPanel();
            jPanel4.setLayout(new GridBagLayout());
        }
        return jPanel4;
    }

    /**
	 * This method initializes jSplitPane	
	 * 	
	 * @return javax.swing.JSplitPane	
	 */
    private JSplitPane getJSplitPane() {
        if (jSplitPane == null) {
            jSplitPane = new JSplitPane();
            jSplitPane.setDividerSize(4);
            jSplitPane.setLeftComponent(getJPanel1());
            jSplitPane.setRightComponent(getJPanelCenter());
            jSplitPane.setDividerLocation(220);
        }
        return jSplitPane;
    }

    /**
	 * This method initializes jPanel1	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    private JPanel getJPanel1() {
        if (jPanel1 == null) {
            jPanel1 = new JPanel();
            jPanel1.setLayout(new BorderLayout());
            jPanel1.add(getJScrollPane(), BorderLayout.CENTER);
        }
        return jPanel1;
    }

    /**
	 * This method initializes jPanelCenter	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    public JPanel getJPanelCenter() {
        if (jPanelCenter == null) {
            jPanelCenter = new JPanel();
            jPanelCenter.setLayout(new GridBagLayout());
        }
        return jPanelCenter;
    }

    /**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
    private JScrollPane getJScrollPane() {
        if (jScrollPane == null) {
            jScrollPane = new JScrollPane();
            jScrollPane.setViewportView(getJTreePlanner());
        }
        return jScrollPane;
    }

    /**
	 * This method initializes jTreePlanner	
	 * 	
	 * @return javax.swing.JTree	
	 */
    public JTree getJTreePlanner() {
        if (jTreePlanner == null) {
            jTreePlanner = new JTree();
            jTreePlanner.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {

                public void valueChanged(javax.swing.event.TreeSelectionEvent e) {
                    if (listener != null) {
                        listener.treeValueChanged(e);
                    }
                }
            });
        }
        return jTreePlanner;
    }

    /**
	 * This method initializes jButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getJButton() {
        if (jButton == null) {
            jButton = new JButton();
            jButton.setText("  APPLY  ");
            jButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    if (listener != null) {
                        listener.apply();
                    }
                }
            });
        }
        return jButton;
    }

    /**
	 * This method initializes jButton1	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getJButton1() {
        if (jButton1 == null) {
            jButton1 = new JButton();
            jButton1.setText(" REVERT ");
            jButton1.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    if (listener != null) {
                        listener.revert();
                    }
                }
            });
        }
        return jButton1;
    }

    public JButton getJButtonRecents() {
        if (jButtonRecents == null) {
            jButtonRecents = new JButton();
            jButtonRecents.setText("Recents");
            jButtonRecents.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    listener.recentsClicked(e);
                }
            });
        }
        return jButtonRecents;
    }

    /**
	 * This method initializes jButtonReLoad	
	 * 	
	 * @return javax.swing.JButton	
	 */
    public JButton getJButtonReLoad() {
        if (jButtonReLoad == null) {
            jButtonReLoad = new JButton();
            jButtonReLoad.setText("ReLoad");
            jButtonReLoad.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    listener.reload();
                }
            });
        }
        return jButtonReLoad;
    }

    /**
	 * This method initializes jButtonAddAll	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getJButtonAddAll() {
        if (jButtonAddAll == null) {
            jButtonAddAll = new JButton();
            jButtonAddAll.setText("Add All");
            jButtonAddAll.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    listener.addAll();
                }
            });
        }
        return jButtonAddAll;
    }
}
