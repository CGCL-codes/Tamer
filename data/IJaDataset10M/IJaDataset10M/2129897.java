package artem.finance.gui;

import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.Dimension;
import javax.swing.JLabel;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFormattedTextField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.text.MaskFormatter;
import java.awt.Point;

/**
 * @author Burtsev Ivan
 *
 */
public class SpravochnikOrganizaziy extends JFrame {

    private static final long serialVersionUID = 1L;

    private JPanel jContentPane = null;

    private JLabel organizationLabel = null;

    private JLabel bankLabel = null;

    private JLabel gorodLabel = null;

    private JLabel mfoLabel = null;

    private JLabel schetLabel = null;

    private JLabel kodzkpoLabel = null;

    private JLabel shortnameLabel = null;

    private JLabel kategorijaLabel = null;

    private JScrollPane tablesScrollPane = null;

    private JTable organizationTable = null;

    protected JTextField organizationTextField = null;

    private JTextField kodzkpoTextField = null;

    private JTextField schetTextField = null;

    private JTextField bankTextField = null;

    private JTextField gorodTextField = null;

    private JFormattedTextField mfoFTextField = null;

    private JTextField shortnameTextField = null;

    private JFormattedTextField kategoriaFTextField = null;

    private JButton saveButton = null;

    private JButton editButton = null;

    private JLabel adressLabel = null;

    private JTextField adressTextField = null;

    private JButton deleteButton = null;

    private JLabel rawCountLabel = null;

    private JLabel countLabel = null;

    /**
	 * This is the default constructor
	 */
    public SpravochnikOrganizaziy() {
        super();
        initialize();
    }

    /**
	 * This method initializes this
	 * 
	 * @return void
	 */
    private void initialize() {
        this.setSize(1023, 646);
        this.setContentPane(getJContentPane());
        this.setTitle("���������� �����������");
    }

    /**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            countLabel = new JLabel();
            countLabel.setBounds(new Rectangle(137, 569, 111, 20));
            countLabel.setText("");
            rawCountLabel = new JLabel();
            rawCountLabel.setBounds(new Rectangle(12, 570, 120, 19));
            rawCountLabel.setText("����� �����������: ");
            adressLabel = new JLabel();
            adressLabel.setBounds(new Rectangle(846, 7, 89, 14));
            adressLabel.setText("�����:");
            kategorijaLabel = new JLabel();
            kategorijaLabel.setBounds(new Rectangle(938, 8, 76, 14));
            kategorijaLabel.setText("���������:");
            shortnameLabel = new JLabel();
            shortnameLabel.setBounds(new Rectangle(691, 7, 148, 14));
            shortnameLabel.setText("���������� ��������:");
            kodzkpoLabel = new JLabel();
            kodzkpoLabel.setBounds(new Rectangle(179, 10, 78, 14));
            kodzkpoLabel.setText("��� ����:");
            schetLabel = new JLabel();
            schetLabel.setBounds(new Rectangle(261, 9, 87, 14));
            schetLabel.setText("����:");
            mfoLabel = new JLabel();
            mfoLabel.setBounds(new Rectangle(609, 9, 74, 14));
            mfoLabel.setText("���:");
            gorodLabel = new JLabel();
            gorodLabel.setBounds(new Rectangle(515, 8, 81, 14));
            gorodLabel.setText("� ������:");
            bankLabel = new JLabel();
            bankLabel.setBounds(new Rectangle(360, 11, 92, 14));
            bankLabel.setText("����:");
            organizationLabel = new JLabel();
            organizationLabel.setBounds(new Rectangle(10, 10, 111, 14));
            organizationLabel.setName("");
            organizationLabel.setText("�����������:");
            jContentPane = new JPanel();
            jContentPane.setLayout(null);
            jContentPane.setSize(new Dimension(804, 585));
            jContentPane.add(organizationLabel, null);
            jContentPane.add(bankLabel, null);
            jContentPane.add(gorodLabel, null);
            jContentPane.add(mfoLabel, null);
            jContentPane.add(schetLabel, null);
            jContentPane.add(kodzkpoLabel, null);
            jContentPane.add(shortnameLabel, null);
            jContentPane.add(kategorijaLabel, null);
            jContentPane.add(getTablesScrollPane(), null);
            jContentPane.add(getOrganizationTextField(), null);
            jContentPane.add(getKodzkpoTextField(), null);
            jContentPane.add(getSchetTextField(), null);
            jContentPane.add(getBankTextField(), null);
            jContentPane.add(getGorodTextField(), null);
            jContentPane.add(getMfoFTextField(), null);
            jContentPane.add(getShortnameTextField(), null);
            jContentPane.add(getKategoriaFTextField(), null);
            jContentPane.add(getSaveButton(), null);
            jContentPane.add(getEditButton(), null);
            jContentPane.add(adressLabel, null);
            jContentPane.add(getAdressTextField(), null);
            jContentPane.add(getDeleteButton(), null);
            jContentPane.add(rawCountLabel, null);
            jContentPane.add(countLabel, null);
        }
        return jContentPane;
    }

    /**
	 * This method initializes tablesScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
    private JScrollPane getTablesScrollPane() {
        if (tablesScrollPane == null) {
            tablesScrollPane = new JScrollPane();
            tablesScrollPane.setBounds(new Rectangle(12, 74, 993, 488));
            tablesScrollPane.setViewportView(getOrganizationTable());
        }
        return tablesScrollPane;
    }

    /**
	 * This method initializes organizationTable	
	 * 	
	 * @return javax.swing.JTable	
	 */
    private JTable getOrganizationTable() {
        return organizationTable;
    }

    /**
	 * This method initializes organizationTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    private JTextField getOrganizationTextField() {
        if (organizationTextField == null) {
            organizationTextField = new JTextField();
            organizationTextField.setBounds(new Rectangle(11, 31, 161, 20));
        }
        return organizationTextField;
    }

    /**
	 * This method initializes kodzkpoTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    private JTextField getKodzkpoTextField() {
        if (kodzkpoTextField == null) {
            kodzkpoTextField = new JTextField();
            kodzkpoTextField.setBounds(new Rectangle(178, 31, 73, 20));
        }
        return kodzkpoTextField;
    }

    /**
	 * This method initializes schetTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    private JTextField getSchetTextField() {
        if (schetTextField == null) {
            schetTextField = new JTextField();
            schetTextField.setBounds(new Rectangle(258, 31, 97, 20));
        }
        return schetTextField;
    }

    /**
	 * This method initializes bankTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    private JTextField getBankTextField() {
        if (bankTextField == null) {
            bankTextField = new JTextField();
            bankTextField.setBounds(new Rectangle(359, 31, 152, 20));
        }
        return bankTextField;
    }

    /**
	 * This method initializes gorodTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    private JTextField getGorodTextField() {
        if (gorodTextField == null) {
            gorodTextField = new JTextField();
            gorodTextField.setBounds(new Rectangle(514, 31, 93, 20));
        }
        return gorodTextField;
    }

    /**
	 * This method initializes mfoTextField	
	 * 	
	 * @return javax.swing.JFormattedTextField	
	 */
    private JFormattedTextField getMfoFTextField() {
        if (mfoFTextField == null) {
            MaskFormatter maskFormatter = new MaskFormatter();
            mfoFTextField = new JFormattedTextField();
            mfoFTextField.setBounds(new Rectangle(610, 31, 77, 20));
            mfoFTextField.setColumns(8);
            mfoFTextField.setFocusLostBehavior(0);
        }
        return mfoFTextField;
    }

    /**
	 * This method initializes shortnameTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    private JTextField getShortnameTextField() {
        if (shortnameTextField == null) {
            shortnameTextField = new JTextField();
            shortnameTextField.setBounds(new Rectangle(689, 31, 132, 20));
        }
        return shortnameTextField;
    }

    /**
	 * This method initializes kategoriaTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    private JFormattedTextField getKategoriaFTextField() {
        if (kategoriaFTextField == null) {
            kategoriaFTextField = new JFormattedTextField();
            kategoriaFTextField.setLocation(new Point(938, 31));
            kategoriaFTextField.setSize(new Dimension(65, 20));
            kategoriaFTextField.setColumns(6);
            kategoriaFTextField.setFocusLostBehavior(0);
        }
        return kategoriaFTextField;
    }

    /**
	 * This method initializes saveButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getSaveButton() {
        if (saveButton == null) {
            saveButton = new JButton();
            saveButton.setPreferredSize(new Dimension(90, 23));
            saveButton.setLocation(new Point(621, 586));
            saveButton.setSize(new Dimension(120, 23));
            saveButton.setText("���������");
            saveButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent ae) {
                    actionPerformedButtons(ae);
                }
            });
        }
        return saveButton;
    }

    /**
	 * This method initializes editButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getEditButton() {
        if (editButton == null) {
            editButton = new JButton();
            editButton.setPreferredSize(new Dimension(90, 23));
            editButton.setLocation(new Point(745, 586));
            editButton.setSize(new Dimension(135, 23));
            editButton.setText("�������������");
            editButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent ae) {
                    actionPerformedButtons(ae);
                }
            });
        }
        return editButton;
    }

    /**
	 * This method initializes adressTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    private JTextField getAdressTextField() {
        if (adressTextField == null) {
            adressTextField = new JTextField();
            adressTextField.setBounds(new Rectangle(824, 31, 108, 20));
        }
        return adressTextField;
    }

    /**
	 * This method initializes deleteButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getDeleteButton() {
        if (deleteButton == null) {
            deleteButton = new JButton();
            deleteButton.setPreferredSize(new Dimension(90, 23));
            deleteButton.setLocation(new Point(883, 585));
            deleteButton.setSize(new Dimension(120, 23));
            deleteButton.setText("�������");
            deleteButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent ae) {
                    actionPerformedButtons(ae);
                }
            });
        }
        return deleteButton;
    }

    private void actionPerformedButtons(ActionEvent ae) {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                SpravochnikOrganizaziy thisClass = new SpravochnikOrganizaziy();
                thisClass.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
                thisClass.setVisible(true);
            }
        });
    }
}
