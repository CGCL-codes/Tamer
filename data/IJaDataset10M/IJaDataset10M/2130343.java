package artem.finance.gui;

import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Rectangle;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFormattedTextField;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 * 
 */
public class DataSearch extends JPanel {

    private static final long serialVersionUID = 1L;

    private JLabel jInfoLabel = null;

    private JLabel jDataSLabel = null;

    private JLabel jDataPoLabel = null;

    private JLabel jSumLabel = null;

    private JLabel jNumSchetaLabel = null;

    private JLabel jOplataLabel = null;

    private JFormattedTextField jDataSFormattedTextField = null;

    private JFormattedTextField jDataPoFormattedTextField = null;

    private JFormattedTextField jSumFormattedTextField = null;

    private JTextField jNumSchetaTextField = null;

    private JComboBox jOplataComboBox = null;

    private JLabel jBankLabel = null;

    private JLabel jPoctavschikLabel = null;

    private JLabel jPodrazdelenieLabel = null;

    private JLabel jDogovorLabel = null;

    private JLabel jZatrataLabel = null;

    private JComboBox jBankComboBox = null;

    private JComboBox jPostavschikComboBox = null;

    private JComboBox jPodrazdelenieComboBox = null;

    private JComboBox jDogovorComboBox = null;

    private JComboBox jZatrataComboBox = null;

    private JButton jDannieButton = null;

    private JButton jOchistitButton = null;

    private JScrollPane jScrollPane = null;

    private JTable jTable = null;

    private JLabel jVsegoOplachenoLabel = null;

    private JTextField jVsegoPolachenoTextField = null;

    /**
	 * This method initializes 
	 * 
	 */
    public DataSearch() {
        super();
        initialize();
    }

    /**
	 * This method initializes this
	 * 
	 */
    private void initialize() {
        jVsegoOplachenoLabel = new JLabel();
        jVsegoOplachenoLabel.setBounds(new Rectangle(50, 390, 100, 20));
        jVsegoOplachenoLabel.setText("����� ��������");
        jZatrataLabel = new JLabel();
        jZatrataLabel.setBounds(new Rectangle(250, 165, 100, 20));
        jZatrataLabel.setText("�������");
        jDogovorLabel = new JLabel();
        jDogovorLabel.setBounds(new Rectangle(250, 135, 100, 20));
        jDogovorLabel.setText("�������");
        jPodrazdelenieLabel = new JLabel();
        jPodrazdelenieLabel.setBounds(new Rectangle(250, 105, 100, 20));
        jPodrazdelenieLabel.setText("�������������");
        jPoctavschikLabel = new JLabel();
        jPoctavschikLabel.setBounds(new Rectangle(250, 75, 100, 20));
        jPoctavschikLabel.setText("���������");
        jBankLabel = new JLabel();
        jBankLabel.setBounds(new Rectangle(250, 45, 100, 20));
        jBankLabel.setText("����");
        jOplataLabel = new JLabel();
        jOplataLabel.setBounds(new Rectangle(15, 165, 110, 20));
        jOplataLabel.setText("������� �� ������");
        jNumSchetaLabel = new JLabel();
        jNumSchetaLabel.setBounds(new Rectangle(15, 135, 110, 20));
        jNumSchetaLabel.setText("� �����");
        jSumLabel = new JLabel();
        jSumLabel.setBounds(new Rectangle(15, 105, 110, 20));
        jSumLabel.setText("�����");
        jDataPoLabel = new JLabel();
        jDataPoLabel.setBounds(new Rectangle(15, 75, 110, 20));
        jDataPoLabel.setText("���� �������� ��");
        jDataSLabel = new JLabel();
        jDataSLabel.setBounds(new Rectangle(15, 45, 110, 20));
        jDataSLabel.setText("���� �������� �");
        jInfoLabel = new JLabel();
        jInfoLabel.setBounds(new Rectangle(15, 15, 300, 20));
        jInfoLabel.setText("������� ���� ��� ����� �������� ��� ������:");
        this.setLayout(null);
        this.setSize(new Dimension(665, 416));
        this.add(jInfoLabel, null);
        this.add(jDataSLabel, null);
        this.add(jDataPoLabel, null);
        this.add(jSumLabel, null);
        this.add(jNumSchetaLabel, null);
        this.add(jOplataLabel, null);
        this.add(getJDataSFormattedTextField(), null);
        this.add(getJDataPoFormattedTextField(), null);
        this.add(getJSumFormattedTextField(), null);
        this.add(getJNumSchetaTextField(), null);
        this.add(getJOplataComboBox(), null);
        this.add(jBankLabel, null);
        this.add(jPoctavschikLabel, null);
        this.add(jPodrazdelenieLabel, null);
        this.add(jDogovorLabel, null);
        this.add(jZatrataLabel, null);
        this.add(getJBankComboBox(), null);
        this.add(getJPostavschikComboBox(), null);
        this.add(getJPodrazdelenieComboBox(), null);
        this.add(getJDogovorComboBox(), null);
        this.add(getJZatrataComboBox(), null);
        this.add(getJDannieButton(), null);
        this.add(getJOchistitButton(), null);
        this.add(getJScrollPane(), null);
        this.add(jVsegoOplachenoLabel, null);
        this.add(getJVsegoPolachenoTextField(), null);
    }

    /**
	 * This method initializes jDataSFormattedTextField	
	 * 	
	 * @return javax.swing.JFormattedTextField	
	 */
    private JFormattedTextField getJDataSFormattedTextField() {
        if (jDataSFormattedTextField == null) {
            try {
                MaskFormatter mformatter = new MaskFormatter();
                mformatter.setMask("##.##.####");
                mformatter.setValidCharacters("0123456789");
                mformatter.setPlaceholderCharacter('_');
                mformatter.setValueClass(java.sql.Date.class);
                DefaultFormatterFactory dff = new DefaultFormatterFactory(mformatter);
                jDataSFormattedTextField = new JFormattedTextField();
                jDataSFormattedTextField.setColumns(10);
                jDataSFormattedTextField.setFormatterFactory(dff);
                jDataSFormattedTextField.setBounds(new Rectangle(130, 45, 70, 20));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return jDataSFormattedTextField;
    }

    /**
	 * This method initializes jDataPoFormattedTextField	
	 * 	
	 * @return javax.swing.JFormattedTextField	
	 */
    private JFormattedTextField getJDataPoFormattedTextField() {
        if (jDataPoFormattedTextField == null) {
            try {
                MaskFormatter mformatter = new MaskFormatter();
                mformatter.setMask("##.##.####");
                mformatter.setValidCharacters("0123456789");
                mformatter.setPlaceholderCharacter('_');
                mformatter.setValueClass(java.sql.Date.class);
                DefaultFormatterFactory dff = new DefaultFormatterFactory(mformatter);
                jDataPoFormattedTextField = new JFormattedTextField();
                jDataPoFormattedTextField.setColumns(10);
                jDataPoFormattedTextField.setFormatterFactory(dff);
                jDataPoFormattedTextField.setBounds(new Rectangle(130, 75, 70, 20));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return jDataPoFormattedTextField;
    }

    /**
	 * This method initializes jSumFormattedTextField	
	 * 	
	 * @return javax.swing.JFormattedTextField	
	 */
    private JFormattedTextField getJSumFormattedTextField() {
        if (jSumFormattedTextField == null) {
            try {
                MaskFormatter mformatter = new MaskFormatter();
                mformatter.setMask("###############");
                mformatter.setValidCharacters("0123456789");
                DefaultFormatterFactory dff = new DefaultFormatterFactory(mformatter);
                jSumFormattedTextField = new JFormattedTextField();
                jSumFormattedTextField.setColumns(10);
                jSumFormattedTextField.setFormatterFactory(dff);
                jSumFormattedTextField.setBounds(new Rectangle(130, 105, 100, 20));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return jSumFormattedTextField;
    }

    /**
	 * This method initializes jNumSchetaTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    private JTextField getJNumSchetaTextField() {
        if (jNumSchetaTextField == null) {
            jNumSchetaTextField = new JTextField();
            jNumSchetaTextField.setBounds(new Rectangle(130, 135, 100, 20));
        }
        return jNumSchetaTextField;
    }

    /**
	 * This method initializes jOplataComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
    private JComboBox getJOplataComboBox() {
        if (jOplataComboBox == null) {
            jOplataComboBox = new JComboBox();
            jOplataComboBox.setBounds(new Rectangle(130, 165, 100, 20));
        }
        return jOplataComboBox;
    }

    /**
	 * This method initializes jBankComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
    private JComboBox getJBankComboBox() {
        if (jBankComboBox == null) {
            jBankComboBox = new JComboBox();
            jBankComboBox.setBounds(new Rectangle(360, 45, 120, 20));
        }
        return jBankComboBox;
    }

    /**
	 * This method initializes jPostavschikComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
    private JComboBox getJPostavschikComboBox() {
        if (jPostavschikComboBox == null) {
            jPostavschikComboBox = new JComboBox();
            jPostavschikComboBox.setBounds(new Rectangle(360, 75, 120, 20));
        }
        return jPostavschikComboBox;
    }

    /**
	 * This method initializes jPodrazdelenieComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
    private JComboBox getJPodrazdelenieComboBox() {
        if (jPodrazdelenieComboBox == null) {
            jPodrazdelenieComboBox = new JComboBox();
            jPodrazdelenieComboBox.setBounds(new Rectangle(360, 105, 120, 20));
        }
        return jPodrazdelenieComboBox;
    }

    /**
	 * This method initializes jDogovorComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
    private JComboBox getJDogovorComboBox() {
        if (jDogovorComboBox == null) {
            jDogovorComboBox = new JComboBox();
            jDogovorComboBox.setBounds(new Rectangle(360, 135, 120, 20));
        }
        return jDogovorComboBox;
    }

    /**
	 * This method initializes jZatrataComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
    private JComboBox getJZatrataComboBox() {
        if (jZatrataComboBox == null) {
            jZatrataComboBox = new JComboBox();
            jZatrataComboBox.setBounds(new Rectangle(360, 165, 120, 20));
        }
        return jZatrataComboBox;
    }

    /**
	 * This method initializes jDannieButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getJDannieButton() {
        if (jDannieButton == null) {
            jDannieButton = new JButton();
            jDannieButton.setBounds(new Rectangle(500, 45, 120, 26));
            jDannieButton.setText("������");
        }
        return jDannieButton;
    }

    /**
	 * This method initializes jOchistitButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getJOchistitButton() {
        if (jOchistitButton == null) {
            jOchistitButton = new JButton();
            jOchistitButton.setBounds(new Rectangle(500, 85, 120, 26));
            jOchistitButton.setText("��������");
        }
        return jOchistitButton;
    }

    /**
	 * This method initializes jScrollPane	
	 * 	
	 * @return javax.swing.JScrollPane	
	 */
    private JScrollPane getJScrollPane() {
        if (jScrollPane == null) {
            jScrollPane = new JScrollPane();
            jScrollPane.setBounds(new Rectangle(15, 200, 620, 190));
            jScrollPane.setViewportView(getJTable());
        }
        return jScrollPane;
    }

    /**
	 * This method initializes jTable	
	 * 	
	 * @return javax.swing.JTable	
	 */
    private JTable getJTable() {
        if (jTable == null) {
            DatabaseTableModel tableModel = new DatabaseTableModel(true);
            final List<String> names = new ArrayList<String>();
            names.add("����");
            names.add("�������������");
            names.add("���������");
            names.add("�������");
            names.add("�����");
            tableModel.setColumnNames(names);
            final List<Class> types = new ArrayList<Class>(0);
            types.add(String.class);
            types.add(String.class);
            types.add(String.class);
            types.add(String.class);
            types.add(Double.class);
            tableModel.setColumnTypes(types);
            jTable = new JTable(tableModel);
            DefaultTableColumnModel columns = (DefaultTableColumnModel) jTable.getColumnModel();
            TableColumn tc = columns.getColumn(0);
            tc.setPreferredWidth(200);
        }
        return jTable;
    }

    /**
	 * This method initializes jVsegoPolachenoTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    private JTextField getJVsegoPolachenoTextField() {
        if (jVsegoPolachenoTextField == null) {
            jVsegoPolachenoTextField = new JTextField();
            jVsegoPolachenoTextField.setBounds(new Rectangle(150, 390, 100, 20));
        }
        return jVsegoPolachenoTextField;
    }
}
