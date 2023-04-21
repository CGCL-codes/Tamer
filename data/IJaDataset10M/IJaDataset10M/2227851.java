package de.mse.mogwai.utils.erdesigner.dialogs;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import com.jgoodies.forms.factories.DefaultComponentFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Visual class DatabaseGeneratorView.
 * 
 * Created with Mogwai FormMaker 0.6.
 */
public class DatabaseGeneratorView extends JPanel {

    private javax.swing.JRadioButton m_savetofile;

    private javax.swing.JRadioButton m_savetodatabase;

    private javax.swing.JButton m_retrievestatementsbutton;

    private javax.swing.JLabel m_component_9;

    private javax.swing.JTextField m_destination_file;

    private javax.swing.JButton m_searchfile;

    private JPanel m_component_12;

    private javax.swing.JButton m_okbutton;

    private javax.swing.JButton m_cancelbutton;

    private javax.swing.JTree m_statementlist;

    /**
	 * Constructor.
	 */
    public DatabaseGeneratorView() {
        this.initialize();
    }

    /**
	 * Initialize method.
	 */
    private void initialize() {
        String rowDef = "2dlu,p,8dlu,p,2dlu,p,8dlu,p,8dlu,p,8dlu,p,200dlu,p,8dlu,p,8dlu,p,8dlu,p,2dlu";
        String colDef = "2dlu,60dlu,2dlu,2dlu,200dlu:grow,2dlu,right:40dlu,2dlu,20dlu,2dlu";
        FormLayout layout = new FormLayout(colDef, rowDef);
        this.setLayout(layout);
        CellConstraints cons = new CellConstraints();
        this.add(DefaultComponentFactory.getInstance().createSeparator("Generator options"), cons.xywh(2, 2, 8, 1));
        this.add(this.getSaveToFile(), cons.xywh(2, 6, 4, 1));
        this.add(this.getSaveToDatabase(), cons.xywh(2, 4, 4, 1));
        this.add(this.getRetrieveStatementsButton(), cons.xywh(2, 8, 8, 1));
        this.add(DefaultComponentFactory.getInstance().createSeparator("Statements overview"), cons.xywh(2, 10, 8, 1));
        this.add(DefaultComponentFactory.getInstance().createSeparator("Functions"), cons.xywh(2, 16, 8, 1));
        this.add(this.getComponent_9(), cons.xywh(2, 18, 1, 1));
        this.add(this.getDestination_file(), cons.xywh(4, 18, 4, 1));
        this.add(this.getSearchFile(), cons.xywh(9, 18, 1, 1));
        this.add(this.getComponent_12(), cons.xywh(2, 20, 8, 1));
        this.add(new JScrollPane(this.getStatementList()), cons.xywh(2, 12, 8, 3));
        this.buildGroups();
    }

    /**
	 * Getter method for component SaveToFile.
	 * 
	 * @return the initialized component
	 */
    public javax.swing.JRadioButton getSaveToFile() {
        if (this.m_savetofile == null) {
            this.m_savetofile = new javax.swing.JRadioButton();
            this.m_savetofile.setActionCommand("Save SQL to file");
            this.m_savetofile.setName("SaveToFile#Group1!File");
            this.m_savetofile.setText("Save SQL to file");
            this.m_savetofile.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    handleSaveToFileActionPerformed(e.getActionCommand());
                }
            });
        }
        return this.m_savetofile;
    }

    /**
	 * Getter method for component SaveToDatabase.
	 * 
	 * @return the initialized component
	 */
    public javax.swing.JRadioButton getSaveToDatabase() {
        if (this.m_savetodatabase == null) {
            this.m_savetodatabase = new javax.swing.JRadioButton();
            this.m_savetodatabase.setActionCommand("Send SQL to database");
            this.m_savetodatabase.setName("SaveToDatabase#Group1!DB");
            this.m_savetodatabase.setSelected(true);
            this.m_savetodatabase.setText("Send SQL to database");
            this.m_savetodatabase.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    handleSaveToDatabaseActionPerformed(e.getActionCommand());
                }
            });
        }
        return this.m_savetodatabase;
    }

    /**
	 * Getter method for component RetrieveStatementsButton.
	 * 
	 * @return the initialized component
	 */
    public javax.swing.JButton getRetrieveStatementsButton() {
        if (this.m_retrievestatementsbutton == null) {
            this.m_retrievestatementsbutton = new javax.swing.JButton();
            this.m_retrievestatementsbutton.setActionCommand("Retrieve Statements");
            this.m_retrievestatementsbutton.setIcon(new ImageIcon(ClassLoader.getSystemResource("toolbarButtonGraphics/general/Refresh16.gif")));
            this.m_retrievestatementsbutton.setName("RetrieveStatementsButton");
            this.m_retrievestatementsbutton.setText("Retrieve Statements");
            this.m_retrievestatementsbutton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    handleRetrieveStatementsButtonActionPerformed(e.getActionCommand());
                }
            });
        }
        return this.m_retrievestatementsbutton;
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
            this.m_component_9.setText("Destination file:");
        }
        return this.m_component_9;
    }

    /**
	 * Getter method for component Destination_file.
	 * 
	 * @return the initialized component
	 */
    public javax.swing.JTextField getDestination_file() {
        if (this.m_destination_file == null) {
            this.m_destination_file = new javax.swing.JTextField();
            this.m_destination_file.setName("Destination_file");
            this.m_destination_file.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    handleDestination_fileActionPerformed(e.getActionCommand());
                }
            });
        }
        return this.m_destination_file;
    }

    /**
	 * Getter method for component SearchFile.
	 * 
	 * @return the initialized component
	 */
    public javax.swing.JButton getSearchFile() {
        if (this.m_searchfile == null) {
            this.m_searchfile = new javax.swing.JButton();
            this.m_searchfile.setIcon(new ImageIcon(ClassLoader.getSystemResource("toolbarButtonGraphics/general/Find16.gif")));
            this.m_searchfile.setName("SearchFile");
            this.m_searchfile.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    handleSearchFileActionPerformed(e.getActionCommand());
                }
            });
        }
        return this.m_searchfile;
    }

    /**
	 * Getter method for component Component_12.
	 * 
	 * @return the initialized component
	 */
    public JPanel getComponent_12() {
        if (this.m_component_12 == null) {
            this.m_component_12 = new JPanel();
            String rowDef = "p";
            String colDef = "60dlu,2dlu:grow,60dlu";
            FormLayout layout = new FormLayout(colDef, rowDef);
            this.m_component_12.setLayout(layout);
            CellConstraints cons = new CellConstraints();
            this.m_component_12.add(this.getOKButton(), cons.xywh(1, 1, 1, 1));
            this.m_component_12.add(this.getCancelButton(), cons.xywh(3, 1, 1, 1));
            this.m_component_12.setName("Component_12");
        }
        return this.m_component_12;
    }

    /**
	 * Getter method for component OKButton.
	 * 
	 * @return the initialized component
	 */
    public javax.swing.JButton getOKButton() {
        if (this.m_okbutton == null) {
            this.m_okbutton = new javax.swing.JButton();
            this.m_okbutton.setActionCommand("Generate !");
            this.m_okbutton.setIcon(new ImageIcon(ClassLoader.getSystemResource("toolbarButtonGraphics/general/Export16.gif")));
            this.m_okbutton.setName("OKButton");
            this.m_okbutton.setText("Generate !");
            this.m_okbutton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    handleOKButtonActionPerformed(e.getActionCommand());
                }
            });
        }
        return this.m_okbutton;
    }

    /**
	 * Getter method for component CancelButton.
	 * 
	 * @return the initialized component
	 */
    public javax.swing.JButton getCancelButton() {
        if (this.m_cancelbutton == null) {
            this.m_cancelbutton = new javax.swing.JButton();
            this.m_cancelbutton.setActionCommand("Cancel");
            this.m_cancelbutton.setIcon(new ImageIcon(ClassLoader.getSystemResource("toolbarButtonGraphics/general/Stop16.gif")));
            this.m_cancelbutton.setName("CancelButton");
            this.m_cancelbutton.setText("Cancel");
            this.m_cancelbutton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    handleCancelButtonActionPerformed(e.getActionCommand());
                }
            });
        }
        return this.m_cancelbutton;
    }

    /**
	 * Getter method for component StatementList.
	 * 
	 * @return the initialized component
	 */
    public javax.swing.JTree getStatementList() {
        if (this.m_statementlist == null) {
            this.m_statementlist = new javax.swing.JTree();
            this.m_statementlist.setForeground(new Color(51, 51, 51));
            this.m_statementlist.setName("StatementList");
        }
        return this.m_statementlist;
    }

    /**
	 * Initialize method.
	 */
    private void buildGroups() {
        ButtonGroup Group1 = new ButtonGroup();
        Group1.add(this.getSaveToFile());
        Group1.add(this.getSaveToDatabase());
    }

    /**
	 * Getter for the group value for group Group1.
	 * 
	 * @return the value for the current selected item in the group or null if
	 *         nothing was selected
	 */
    public String getGroup1Value() {
        if (this.getSaveToFile().isSelected()) return "File";
        if (this.getSaveToDatabase().isSelected()) return "DB";
        return null;
    }

    /**
	 * Setter for the group value for group Group1.
	 * 
	 * @param the
	 *            value for the current selected item in the group or null if
	 *            nothing is selected
	 */
    public void setGroup1Value(String value) {
        this.getSaveToFile().setSelected("File".equals(value));
        this.getSaveToDatabase().setSelected("DB".equals(value));
    }

    /**
	 * Action listener implementation for SaveToFile.
	 * 
	 * @param actionCommand
	 *            the spanned action command
	 */
    public void handleSaveToFileActionPerformed(String actionCommand) {
    }

    /**
	 * Action listener implementation for SaveToDatabase.
	 * 
	 * @param actionCommand
	 *            the spanned action command
	 */
    public void handleSaveToDatabaseActionPerformed(String actionCommand) {
    }

    /**
	 * Action listener implementation for RetrieveStatementsButton.
	 * 
	 * @param actionCommand
	 *            the spanned action command
	 */
    public void handleRetrieveStatementsButtonActionPerformed(String actionCommand) {
    }

    /**
	 * Action listener implementation for Destination_file.
	 * 
	 * @param actionCommand
	 *            the spanned action command
	 */
    public void handleDestination_fileActionPerformed(String actionCommand) {
    }

    /**
	 * Action listener implementation for SearchFile.
	 * 
	 * @param actionCommand
	 *            the spanned action command
	 */
    public void handleSearchFileActionPerformed(String actionCommand) {
    }

    /**
	 * Action listener implementation for OKButton.
	 * 
	 * @param actionCommand
	 *            the spanned action command
	 */
    public void handleOKButtonActionPerformed(String actionCommand) {
    }

    /**
	 * Action listener implementation for CancelButton.
	 * 
	 * @param actionCommand
	 *            the spanned action command
	 */
    public void handleCancelButtonActionPerformed(String actionCommand) {
    }

    /**
	 * Test main method.
	 */
    public static void main(String args[]) {
        JFrame test = new JFrame("Test for DatabaseGeneratorView");
        test.setContentPane(new DatabaseGeneratorView());
        test.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        test.pack();
        test.show();
    }
}
