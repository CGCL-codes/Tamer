package de.mse.mogwai.formmaker.view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.jgoodies.forms.factories.DefaultComponentFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Visual class FormMakerMainDialogView.
 *
 * Created with Mogwai FormMaker 0.6.
 */
public class FormMakerMainDialogView extends JPanel {

    private javax.swing.JLabel m_component_2;

    private javax.swing.JComboBox m_container;

    private javax.swing.JLabel m_component_5;

    private javax.swing.JLabel m_component_6;

    private javax.swing.JTextField m_component_name;

    private javax.swing.JTextField m_type;

    private javax.swing.JLabel m_component_9;

    private javax.swing.JSpinner m_nocolspanned;

    private javax.swing.JLabel m_component_12;

    private javax.swing.JSpinner m_norowsspanned;

    private JPanel m_component_14;

    private javax.swing.JButton m_addcolumnbutton;

    private javax.swing.JButton m_addrowbutton;

    private javax.swing.JButton m_removecolumnbutton;

    private javax.swing.JButton m_removerowbutton;

    private javax.swing.JTable m_layouttable;

    /**
	 * Constructor.
	 */
    public FormMakerMainDialogView() {
        this.initialize();
    }

    /**
	 * Initialize method.
	 */
    private void initialize() {
        String rowDef = "2dlu,p,8dlu,p,8dlu,p,8dlu,p,2dlu,p,8dlu,p,8dlu,p,2dlu,p,200dlu,p,2dlu";
        String colDef = "2dlu,right:70dlu,2dlu,140dlu:grow,2dlu,right:60dlu,2dlu,20dlu,2dlu,right:60dlu,2dlu,20dlu,2dlu";
        FormLayout layout = new FormLayout(colDef, rowDef);
        this.setLayout(layout);
        CellConstraints cons = new CellConstraints();
        this.add(DefaultComponentFactory.getInstance().createSeparator("Current layout container"), cons.xywh(2, 2, 11, 1));
        this.add(this.getComponent_2(), cons.xywh(2, 4, 1, 1));
        this.add(this.getContainer(), cons.xywh(4, 4, 1, 1));
        this.add(DefaultComponentFactory.getInstance().createSeparator("Main component properties"), cons.xywh(2, 6, 11, 1));
        this.add(this.getComponent_5(), cons.xywh(2, 8, 1, 1));
        this.add(this.getComponent_6(), cons.xywh(2, 10, 1, 1));
        this.add(this.getComponent_name(), cons.xywh(4, 8, 1, 1));
        this.add(this.getType(), cons.xywh(4, 10, 1, 1));
        this.add(this.getComponent_9(), cons.xywh(6, 8, 1, 1));
        this.add(this.getNoColSpanned(), cons.xywh(8, 8, 1, 1));
        this.add(this.getComponent_12(), cons.xywh(10, 8, 1, 1));
        this.add(this.getNoRowsSpanned(), cons.xywh(12, 8, 1, 1));
        this.add(DefaultComponentFactory.getInstance().createSeparator("Dialog layout table"), cons.xywh(2, 12, 11, 1));
        this.add(this.getComponent_14(), cons.xywh(2, 14, 11, 1));
        this.add(new JScrollPane(this.getLayoutTable()), cons.xywh(2, 16, 11, 3));
        this.buildGroups();
    }

    /**
	 * Getter method for component Component_2.
	 *
	 * @return the initialized component
	 */
    public javax.swing.JLabel getComponent_2() {
        if (this.m_component_2 == null) {
            this.m_component_2 = new javax.swing.JLabel();
            this.m_component_2.setName("Component_2");
            this.m_component_2.setText("Container :");
        }
        return this.m_component_2;
    }

    /**
	 * Getter method for component Container.
	 *
	 * @return the initialized component
	 */
    public javax.swing.JComboBox getContainer() {
        if (this.m_container == null) {
            this.m_container = new javax.swing.JComboBox();
            this.m_container.setName("Container");
            this.m_container.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    handleContainerActionPerformed(e.getActionCommand());
                }
            });
        }
        return this.m_container;
    }

    /**
	 * Getter method for component Component_5.
	 *
	 * @return the initialized component
	 */
    public javax.swing.JLabel getComponent_5() {
        if (this.m_component_5 == null) {
            this.m_component_5 = new javax.swing.JLabel();
            this.m_component_5.setName("Component_5");
            this.m_component_5.setText("Component name :");
        }
        return this.m_component_5;
    }

    /**
	 * Getter method for component Component_6.
	 *
	 * @return the initialized component
	 */
    public javax.swing.JLabel getComponent_6() {
        if (this.m_component_6 == null) {
            this.m_component_6 = new javax.swing.JLabel();
            this.m_component_6.setName("Component_6");
            this.m_component_6.setText("Type :");
        }
        return this.m_component_6;
    }

    /**
	 * Getter method for component Component_name.
	 *
	 * @return the initialized component
	 */
    public javax.swing.JTextField getComponent_name() {
        if (this.m_component_name == null) {
            this.m_component_name = new javax.swing.JTextField();
            this.m_component_name.setName("Component_name");
            this.m_component_name.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    handleComponent_nameActionPerformed(e.getActionCommand());
                }
            });
        }
        return this.m_component_name;
    }

    /**
	 * Getter method for component Type.
	 *
	 * @return the initialized component
	 */
    public javax.swing.JTextField getType() {
        if (this.m_type == null) {
            this.m_type = new javax.swing.JTextField();
            this.m_type.setBackground(new Color(238, 238, 238));
            this.m_type.setEditable(false);
            this.m_type.setEnabled(false);
            this.m_type.setName("Type");
            this.m_type.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    handleTypeActionPerformed(e.getActionCommand());
                }
            });
        }
        return this.m_type;
    }

    /**
	 * Getter method for component Component_9.
	 *
	 * @return the initialized component
	 */
    public javax.swing.JLabel getComponent_9() {
        if (this.m_component_9 == null) {
            this.m_component_9 = new javax.swing.JLabel();
            this.m_component_9.setName("Component_9");
            this.m_component_9.setText("# Col. spanned :");
        }
        return this.m_component_9;
    }

    /**
	 * Getter method for component NoColSpanned.
	 *
	 * @return the initialized component
	 */
    public javax.swing.JSpinner getNoColSpanned() {
        if (this.m_nocolspanned == null) {
            this.m_nocolspanned = new javax.swing.JSpinner();
            this.m_nocolspanned.setName("NoColSpanned");
            this.m_nocolspanned.addChangeListener(new javax.swing.event.ChangeListener() {

                public void stateChanged(javax.swing.event.ChangeEvent e) {
                    handleNoColSpannedStateChanged();
                }
            });
        }
        return this.m_nocolspanned;
    }

    /**
	 * Getter method for component Component_12.
	 *
	 * @return the initialized component
	 */
    public javax.swing.JLabel getComponent_12() {
        if (this.m_component_12 == null) {
            this.m_component_12 = new javax.swing.JLabel();
            this.m_component_12.setName("Component_12");
            this.m_component_12.setText("# Rows spanned :");
        }
        return this.m_component_12;
    }

    /**
	 * Getter method for component NoRowsSpanned.
	 *
	 * @return the initialized component
	 */
    public javax.swing.JSpinner getNoRowsSpanned() {
        if (this.m_norowsspanned == null) {
            this.m_norowsspanned = new javax.swing.JSpinner();
            this.m_norowsspanned.setName("NoRowsSpanned");
            this.m_norowsspanned.addChangeListener(new javax.swing.event.ChangeListener() {

                public void stateChanged(javax.swing.event.ChangeEvent e) {
                    handleNoRowsSpannedStateChanged();
                }
            });
        }
        return this.m_norowsspanned;
    }

    /**
	 * Getter method for component Component_14.
	 *
	 * @return the initialized component
	 */
    public JPanel getComponent_14() {
        if (this.m_component_14 == null) {
            this.m_component_14 = new JPanel();
            String rowDef = "p";
            String colDef = "80dlu,2dlu,80dlu,2dlu,80dlu,2dlu,80dlu";
            FormLayout layout = new FormLayout(colDef, rowDef);
            this.m_component_14.setLayout(layout);
            CellConstraints cons = new CellConstraints();
            this.m_component_14.add(this.getAddColumnButton(), cons.xywh(1, 1, 1, 1));
            this.m_component_14.add(this.getAddRowButton(), cons.xywh(3, 1, 1, 1));
            this.m_component_14.add(this.getRemoveColumnButton(), cons.xywh(5, 1, 1, 1));
            this.m_component_14.add(this.getRemoveRowButton(), cons.xywh(7, 1, 1, 1));
            this.m_component_14.setFocusable(false);
            this.m_component_14.setName("Component_14");
        }
        return this.m_component_14;
    }

    /**
	 * Getter method for component AddColumnButton.
	 *
	 * @return the initialized component
	 */
    public javax.swing.JButton getAddColumnButton() {
        if (this.m_addcolumnbutton == null) {
            this.m_addcolumnbutton = new javax.swing.JButton();
            this.m_addcolumnbutton.setActionCommand("Add column");
            this.m_addcolumnbutton.setFocusable(false);
            this.m_addcolumnbutton.setIcon(new ImageIcon(ClassLoader.getSystemResource("toolbarButtonGraphics/table/ColumnInsertAfter16.gif")));
            this.m_addcolumnbutton.setName("AddColumnButton");
            this.m_addcolumnbutton.setText("Add column");
            this.m_addcolumnbutton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    handleAddColumnButtonActionPerformed(e.getActionCommand());
                }
            });
            this.m_addcolumnbutton.addChangeListener(new javax.swing.event.ChangeListener() {

                public void stateChanged(javax.swing.event.ChangeEvent e) {
                    handleAddColumnButtonStateChanged();
                }
            });
        }
        return this.m_addcolumnbutton;
    }

    /**
	 * Getter method for component AddRowButton.
	 *
	 * @return the initialized component
	 */
    public javax.swing.JButton getAddRowButton() {
        if (this.m_addrowbutton == null) {
            this.m_addrowbutton = new javax.swing.JButton();
            this.m_addrowbutton.setActionCommand("Add row");
            this.m_addrowbutton.setFocusable(false);
            this.m_addrowbutton.setIcon(new ImageIcon(ClassLoader.getSystemResource("toolbarButtonGraphics/table/RowInsertBefore16.gif")));
            this.m_addrowbutton.setName("AddRowButton");
            this.m_addrowbutton.setText("Add row");
            this.m_addrowbutton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    handleAddRowButtonActionPerformed(e.getActionCommand());
                }
            });
            this.m_addrowbutton.addChangeListener(new javax.swing.event.ChangeListener() {

                public void stateChanged(javax.swing.event.ChangeEvent e) {
                    handleAddRowButtonStateChanged();
                }
            });
        }
        return this.m_addrowbutton;
    }

    /**
	 * Getter method for component RemoveColumnButton.
	 *
	 * @return the initialized component
	 */
    public javax.swing.JButton getRemoveColumnButton() {
        if (this.m_removecolumnbutton == null) {
            this.m_removecolumnbutton = new javax.swing.JButton();
            this.m_removecolumnbutton.setActionCommand("Remove column");
            this.m_removecolumnbutton.setFocusable(false);
            this.m_removecolumnbutton.setIcon(new ImageIcon(ClassLoader.getSystemResource("toolbarButtonGraphics/table/ColumnDelete16.gif")));
            this.m_removecolumnbutton.setName("RemoveColumnButton");
            this.m_removecolumnbutton.setText("Remove column");
            this.m_removecolumnbutton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    handleRemoveColumnButtonActionPerformed(e.getActionCommand());
                }
            });
            this.m_removecolumnbutton.addChangeListener(new javax.swing.event.ChangeListener() {

                public void stateChanged(javax.swing.event.ChangeEvent e) {
                    handleRemoveColumnButtonStateChanged();
                }
            });
        }
        return this.m_removecolumnbutton;
    }

    /**
	 * Getter method for component RemoveRowButton.
	 *
	 * @return the initialized component
	 */
    public javax.swing.JButton getRemoveRowButton() {
        if (this.m_removerowbutton == null) {
            this.m_removerowbutton = new javax.swing.JButton();
            this.m_removerowbutton.setActionCommand("Remove row");
            this.m_removerowbutton.setFocusable(false);
            this.m_removerowbutton.setIcon(new ImageIcon(ClassLoader.getSystemResource("toolbarButtonGraphics/table/RowDelete16.gif")));
            this.m_removerowbutton.setName("RemoveRowButton");
            this.m_removerowbutton.setText("Remove row");
            this.m_removerowbutton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    handleRemoveRowButtonActionPerformed(e.getActionCommand());
                }
            });
            this.m_removerowbutton.addChangeListener(new javax.swing.event.ChangeListener() {

                public void stateChanged(javax.swing.event.ChangeEvent e) {
                    handleRemoveRowButtonStateChanged();
                }
            });
        }
        return this.m_removerowbutton;
    }

    /**
	 * Getter method for component LayoutTable.
	 *
	 * @return the initialized component
	 */
    public javax.swing.JTable getLayoutTable() {
        if (this.m_layouttable == null) {
            this.m_layouttable = new javax.swing.JTable();
            this.m_layouttable.setName("LayoutTable");
            this.m_layouttable.setRowSelectionAllowed(false);
        }
        return this.m_layouttable;
    }

    /**
	 * Initialize method.
	 */
    private void buildGroups() {
    }

    /**
     * Action listener implementation for Container.
	 *
	 * @param actionCommand the spanned action command
	 */
    public void handleContainerActionPerformed(String actionCommand) {
    }

    /**
     * Action listener implementation for Container.
	 *
	 * @param selectedItem the current selected item
	 */
    public void handleContainerActionPerformed(Object selectedItem) {
    }

    /**
     * Action listener implementation for Component_name.
	 *
	 * @param actionCommand the spanned action command
	 */
    public void handleComponent_nameActionPerformed(String actionCommand) {
    }

    /**
     * Action listener implementation for Type.
	 *
	 * @param actionCommand the spanned action command
	 */
    public void handleTypeActionPerformed(String actionCommand) {
    }

    /**
     * Change listener implementation for NoColSpanned.
	 *
	 * @param item the selected item
	 */
    public void handleNoColSpannedStateChanged() {
    }

    /**
     * Change listener implementation for NoRowsSpanned.
	 *
	 * @param item the selected item
	 */
    public void handleNoRowsSpannedStateChanged() {
    }

    /**
     * Action listener implementation for AddColumnButton.
	 *
	 * @param actionCommand the spanned action command
	 */
    public void handleAddColumnButtonActionPerformed(String actionCommand) {
    }

    /**
     * Change listener implementation for AddColumnButton.
	 *
	 * @param item the selected item
	 */
    public void handleAddColumnButtonStateChanged() {
    }

    /**
     * Action listener implementation for AddRowButton.
	 *
	 * @param actionCommand the spanned action command
	 */
    public void handleAddRowButtonActionPerformed(String actionCommand) {
    }

    /**
     * Change listener implementation for AddRowButton.
	 *
	 * @param item the selected item
	 */
    public void handleAddRowButtonStateChanged() {
    }

    /**
     * Action listener implementation for RemoveColumnButton.
	 *
	 * @param actionCommand the spanned action command
	 */
    public void handleRemoveColumnButtonActionPerformed(String actionCommand) {
    }

    /**
     * Change listener implementation for RemoveColumnButton.
	 *
	 * @param item the selected item
	 */
    public void handleRemoveColumnButtonStateChanged() {
    }

    /**
     * Action listener implementation for RemoveRowButton.
	 *
	 * @param actionCommand the spanned action command
	 */
    public void handleRemoveRowButtonActionPerformed(String actionCommand) {
    }

    /**
     * Change listener implementation for RemoveRowButton.
	 *
	 * @param item the selected item
	 */
    public void handleRemoveRowButtonStateChanged() {
    }

    /**
	 * Test main method.
	 */
    public static void main(String args[]) {
        JFrame test = new JFrame("Test for FormMakerMainDialogView");
        test.setContentPane(new FormMakerMainDialogView());
        test.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        test.pack();
        test.show();
    }
}
