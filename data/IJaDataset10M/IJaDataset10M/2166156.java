package artem.finance.gui;

import artem.finance.gui.DBComboBoxModel;
import artem.finance.gui.DBConnection;
import artem.finance.gui.verifier.DateFormattedTextFieldVerifier;
import artem.finance.gui.verifier.PrecentVerifier;
import artem.finance.server.dao.util.DaoFactory;
import artem.finance.server.persist.Contract;
import artem.finance.server.persist.Service;
import artem.finance.server.persist.Subservice;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Rectangle;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import javax.swing.JTextField;
import javax.swing.JFormattedTextField;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.text.DefaultFormatter;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import javax.swing.text.NumberFormatter;
import javax.swing.JRadioButton;

/**
 * @author Burtsev Ivan
 *
 */
public class DogovorInput2 extends JPanel {

    private static final long serialVersionUID = 1L;

    private JLabel regNumLabel = null;

    private JTextField regNumTextField = null;

    private JLabel regDateLabel = null;

    private JFormattedTextField regDateFormattedTextField = null;

    private JLabel numDogLabel = null;

    private JTextField numDogTextField = null;

    private JLabel dateZaklLabel = null;

    private JFormattedTextField dateZaklFormattedTextField = null;

    private JFormattedTextField dateSFormattedTextField = null;

    private JFormattedTextField datePOFormattedTextField = null;

    private JLabel sLabel = null;

    private JLabel poLabel = null;

    private JLabel srokDogLabel = null;

    private JLabel zakazLabel = null;

    private JComboBox zakazComboBox = null;

    private JLabel sumDogLabel = null;

    private JFormattedTextField sumDogFormattedTextField = null;

    private JLabel periodLabel = null;

    private JComboBox periodComboBox = null;

    private JLabel valLabel = null;

    private JComboBox valComboBox = null;

    private JLabel postavshikLabel = null;

    private JComboBox postavshikComboBox = null;

    private JLabel naznPlatLabel = null;

    private JComboBox naznPlatComboBox = null;

    private JLabel podsluzhbaLabel = null;

    private JLabel sluzhbaLabel = null;

    private JComboBox podsluzhbaComboBox = null;

    private JComboBox sluzhbaComboBox = null;

    private JLabel otvetstvennijLabel = null;

    private JTextField otvetstvennijTextField = null;

    private JLabel telLabel = null;

    private JFormattedTextField telFormattedTextField = null;

    private JLabel avansLabel = null;

    private JFormattedTextField avansFormattedTextField = null;

    private JLabel sumAvansaLabel = null;

    private JFormattedTextField sumAvansaFormattedTextField = null;

    private JLabel penjaLabel = null;

    private JFormattedTextField penjaFormattedTextField = null;

    private JLabel periodPeniLabel = null;

    private JComboBox periodPeniComboBox = null;

    private JButton saveButton = null;

    private JButton clearButton = null;

    private JRadioButton inoutRadioButton = null;

    private JLabel inLabel = null;

    private Contract contract = new Contract();

    private Properties properties;

    /**
	 * This method initializes regNumTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    private JTextField getRegNumTextField() {
        if (regNumTextField == null) {
            regNumTextField = new JTextField();
            regNumTextField.setBounds(new Rectangle(160, 11, 116, 20));
            regNumTextField.putClientProperty("JComponent.sizeVariant", "large");
            SwingUtilities.updateComponentTreeUI(regNumTextField);
        }
        return regNumTextField;
    }

    /**
	 * This method initializes regDateFormattedTextField	
	 * 	
	 * @return javax.swing.JFormattedTextField	
	 */
    private JFormattedTextField getRegDateFormattedTextField() {
        if (regDateFormattedTextField == null) {
            try {
                MaskFormatter mformatter = new MaskFormatter();
                mformatter.setMask("##.##.####");
                mformatter.setValidCharacters("0123456789");
                mformatter.setPlaceholderCharacter('_');
                mformatter.setValueClass(java.sql.Date.class);
                DefaultFormatterFactory dff = new DefaultFormatterFactory(mformatter);
                regDateFormattedTextField = new JFormattedTextField();
                regDateFormattedTextField.setColumns(10);
                regDateFormattedTextField.setFormatterFactory(dff);
                regDateFormattedTextField.setInputVerifier(new DateFormattedTextFieldVerifier());
                regDateFormattedTextField.setBounds(new Rectangle(390, 12, 72, 20));
                SimpleDateFormat sdformat = new SimpleDateFormat("dd.MM.yyyy");
                String currentDate = sdformat.format(new Date());
                regDateFormattedTextField.setText(currentDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return regDateFormattedTextField;
    }

    /**
	 * This method initializes numDogTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    private JTextField getNumDogTextField() {
        if (numDogTextField == null) {
            numDogTextField = new JTextField();
            numDogTextField.setBounds(new Rectangle(159, 40, 117, 20));
        }
        return numDogTextField;
    }

    /**
	 * This method initializes dateZaklFormattedTextField	
	 * 	
	 * @return javax.swing.JFormattedTextField	
	 * @throws ParseException 
	 */
    private JFormattedTextField getDateZaklFormattedTextField() {
        if (dateZaklFormattedTextField == null) {
            try {
                MaskFormatter mformatter = new MaskFormatter();
                mformatter.setMask("##.##.####");
                mformatter.setValidCharacters("0123456789");
                mformatter.setPlaceholderCharacter('_');
                mformatter.setValueClass(java.sql.Date.class);
                DefaultFormatterFactory dff = new DefaultFormatterFactory(mformatter);
                dateZaklFormattedTextField = new JFormattedTextField();
                dateZaklFormattedTextField.setColumns(10);
                dateZaklFormattedTextField.setFormatterFactory(dff);
                dateZaklFormattedTextField.setInputVerifier(new DateFormattedTextFieldVerifier());
                dateZaklFormattedTextField.setBounds(new Rectangle(31, 109, 66, 20));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return dateZaklFormattedTextField;
    }

    /**
	 * This method initializes dateSFormattedTextField	
	 * 	
	 * @return javax.swing.JFormattedTextField	
	 */
    private JFormattedTextField getDateSFormattedTextField() {
        if (dateSFormattedTextField == null) {
            try {
                MaskFormatter mformatter = new MaskFormatter();
                mformatter.setMask("##.##.####");
                mformatter.setValidCharacters("0123456789");
                mformatter.setPlaceholderCharacter('_');
                mformatter.setValueClass(java.sql.Date.class);
                DefaultFormatterFactory dff = new DefaultFormatterFactory(mformatter);
                dateSFormattedTextField = new JFormattedTextField();
                dateSFormattedTextField.setColumns(10);
                dateSFormattedTextField.setFormatterFactory(dff);
                dateSFormattedTextField.setInputVerifier(new DateFormattedTextFieldVerifier());
                dateSFormattedTextField.setBounds(new Rectangle(230, 110, 68, 20));
            } catch (ParseException pe) {
                pe.printStackTrace();
            }
        }
        return dateSFormattedTextField;
    }

    /**
	 * This method initializes datePOFormattedTextField	
	 * 	
	 * @return javax.swing.JFormattedTextField	
	 */
    private JFormattedTextField getDatePOFormattedTextField() {
        if (datePOFormattedTextField == null) {
            try {
                MaskFormatter mformatter = new MaskFormatter();
                mformatter.setMask("##.##.####");
                mformatter.setValidCharacters("0123456789");
                mformatter.setPlaceholderCharacter('_');
                mformatter.setValueClass(java.sql.Date.class);
                DefaultFormatterFactory dff = new DefaultFormatterFactory(mformatter);
                datePOFormattedTextField = new JFormattedTextField();
                datePOFormattedTextField.setColumns(10);
                datePOFormattedTextField.setFormatterFactory(dff);
                datePOFormattedTextField.setInputVerifier(new DateFormattedTextFieldVerifier());
                datePOFormattedTextField.setBounds(new Rectangle(391, 110, 69, 20));
            } catch (ParseException pe) {
                pe.printStackTrace();
            }
        }
        return datePOFormattedTextField;
    }

    /**
	 * This method initializes zakazComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
    private JComboBox getZakazComboBox() {
        if (zakazComboBox == null) {
            DBConnection dbconn = new DBConnection();
            DBComboBoxModel dbcbm = new DBComboBoxModel();
            zakazComboBox = new JComboBox(dbcbm);
            zakazComboBox.setBounds(new Rectangle(480, 108, 139, 22));
            zakazComboBox.setEditable(true);
            JTextField editorComponent = (JTextField) zakazComboBox.getEditor().getEditorComponent();
            editorComponent.getDocument().addDocumentListener(new SearchDocumentListener(zakazComboBox));
        }
        return zakazComboBox;
    }

    /**
	 * This method initializes sumDogTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    private JTextField getSumDogFormattedTextField() {
        if (sumDogFormattedTextField == null) {
            try {
                DecimalFormatSymbols dfs = new DecimalFormatSymbols();
                dfs.setGroupingSeparator(' ');
                dfs.setDecimalSeparator('.');
                BigDecimal bdecimal = new BigDecimal("0.00");
                bdecimal.setScale(2);
                sumDogFormattedTextField = new JFormattedTextField(bdecimal);
                DecimalFormat format = new DecimalFormat("#,###.##", dfs);
                DefaultFormatter df = new NumberFormatter(format);
                df.setValueClass(sumDogFormattedTextField.getValue().getClass());
                DefaultFormatterFactory dff = new DefaultFormatterFactory(df, df, df);
                sumDogFormattedTextField.setFormatterFactory(dff);
                sumDogFormattedTextField.setInputVerifier(new SumFormattedTextFieldVerifier());
                sumDogFormattedTextField.setColumns(15);
                sumDogFormattedTextField.setBounds(new Rectangle(159, 141, 115, 20));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sumDogFormattedTextField;
    }

    /**
	 * This method initializes periodComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
    private JComboBox getPeriodComboBox() {
        if (periodComboBox == null) {
            DefaultComboBoxModel cbmodel = new DefaultComboBoxModel();
            cbmodel.addElement("�����������");
            cbmodel.addElement("���");
            cbmodel.addElement("�����");
            cbmodel.addElement("�������");
            cbmodel.addElement("���������");
            cbmodel.addElement("�������");
            periodComboBox = new JComboBox(cbmodel);
            periodComboBox.setBounds(new Rectangle(159, 170, 115, 22));
        }
        return periodComboBox;
    }

    /**
	 * This method initializes valComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
    private JComboBox getValComboBox() {
        if (valComboBox == null) {
            DefaultComboBoxModel cbmodel = new DefaultComboBoxModel();
            cbmodel.addElement("UAH");
            valComboBox = new JComboBox(cbmodel);
            valComboBox.setBounds(new Rectangle(160, 199, 114, 22));
        }
        return valComboBox;
    }

    /**
	 * This method initializes postavshikComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
    private JComboBox getPostavshikComboBox() {
        if (postavshikComboBox == null) {
            DBConnection dbconn = new DBConnection();
            DBComboBoxModel dbcbm = new DBComboBoxModel();
            postavshikComboBox = new JComboBox(dbcbm);
            postavshikComboBox.setBounds(new Rectangle(160, 230, 317, 22));
            postavshikComboBox.setEditable(true);
            JTextField editorComponent = (JTextField) postavshikComboBox.getEditor().getEditorComponent();
            editorComponent.addFocusListener(new java.awt.event.FocusAdapter() {

                String currenttext = null;

                DefaultComboBoxModel dbcbm = (DefaultComboBoxModel) postavshikComboBox.getModel();

                DBConnection dbconn = new DBConnection();

                @Override
                public void focusLost(java.awt.event.FocusEvent e) {
                    System.out.println(dbcbm.getSize());
                    currenttext = postavshikComboBox.getSelectedItem().toString();
                    System.out.println(currenttext);
                    String query = "INSERT INTO ��������������������� (���������������, ����������������������, �������������, �������������������) " + "VALUES (0, '" + currenttext + "', 0, 0);";
                    int j = 0;
                    for (int i = 1; i < dbcbm.getSize(); i++) {
                        if (dbcbm.getElementAt(i).toString().equals(currenttext)) {
                            j++;
                        }
                    }
                    if (j == 0) {
                        dbconn.insert("jdbc:odbc:fin", "", "", query);
                    }
                    System.out.println("focusLost()");
                }
            });
            editorComponent.getDocument().addDocumentListener(new SearchDocumentListener(postavshikComboBox));
        }
        return postavshikComboBox;
    }

    /**
	 * This method initializes naznPlatComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
    private JComboBox getNaznPlatComboBox() {
        if (naznPlatComboBox == null) {
            DBConnection dbconn = new DBConnection();
            DBComboBoxModel dbcbm = new DBComboBoxModel();
            naznPlatComboBox = new JComboBox(dbcbm);
            naznPlatComboBox.setBounds(new Rectangle(160, 258, 621, 22));
            naznPlatComboBox.setEditable(true);
            JTextField editorComponent = (JTextField) naznPlatComboBox.getEditor().getEditorComponent();
            editorComponent.getDocument().addDocumentListener(new SearchDocumentListener(naznPlatComboBox));
        }
        return naznPlatComboBox;
    }

    /**
	 * This method initializes podsluzhbaComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
    private JComboBox getPodsluzhbaComboBox() {
        if (podsluzhbaComboBox == null) {
            DBConnection dbconn = new DBConnection();
            DBComboBoxModel dbcbm = new DBComboBoxModel();
            podsluzhbaComboBox = new JComboBox(dbcbm);
            podsluzhbaComboBox.setBounds(new Rectangle(160, 289, 176, 22));
            podsluzhbaComboBox.setEditable(true);
            JTextField editorComponent = (JTextField) podsluzhbaComboBox.getEditor().getEditorComponent();
            editorComponent.getDocument().addDocumentListener(new SearchDocumentListener(podsluzhbaComboBox));
        }
        return podsluzhbaComboBox;
    }

    /**
	 * This method initializes sluzhbaComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
    private JComboBox getSluzhbaComboBox() {
        if (sluzhbaComboBox == null) {
            DBConnection dbconn = new DBConnection();
            DBComboBoxModel dbcbm = new DBComboBoxModel();
            sluzhbaComboBox = new JComboBox(dbcbm);
            sluzhbaComboBox.setBounds(new Rectangle(160, 319, 177, 22));
            sluzhbaComboBox.setEditable(true);
            JTextField editorComponent = (JTextField) sluzhbaComboBox.getEditor().getEditorComponent();
            editorComponent.getDocument().addDocumentListener(new SearchDocumentListener(sluzhbaComboBox));
        }
        return sluzhbaComboBox;
    }

    /**
	 * This method initializes otvetstvennijTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    private JTextField getOtvetstvennijTextField() {
        if (otvetstvennijTextField == null) {
            otvetstvennijTextField = new JTextField();
            otvetstvennijTextField.setBounds(new Rectangle(160, 351, 176, 20));
        }
        return otvetstvennijTextField;
    }

    /**
	 * This method initializes telTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    private JFormattedTextField getTelFormattedTextField() {
        if (telFormattedTextField == null) {
            try {
                MaskFormatter mformatter = new MaskFormatter();
                mformatter.setMask("##-##");
                mformatter.setValidCharacters("0987654321");
                mformatter.setPlaceholderCharacter('_');
                DefaultFormatterFactory dff = new DefaultFormatterFactory(mformatter);
                telFormattedTextField = new JFormattedTextField();
                telFormattedTextField.setColumns(5);
                telFormattedTextField.setFormatterFactory(dff);
                telFormattedTextField.setBounds(new Rectangle(160, 382, 55, 20));
            } catch (ParseException pe) {
                pe.printStackTrace();
            }
        }
        return telFormattedTextField;
    }

    /**
	 * This method initializes avansFormattedTextField	
	 * 	
	 * @return javax.swing.JFormattedTextField	
	 */
    private JFormattedTextField getAvansFormattedTextField() {
        if (avansFormattedTextField == null) {
            DecimalFormatSymbols dfs = new DecimalFormatSymbols();
            dfs.setDecimalSeparator('.');
            BigDecimal bdecimal = new BigDecimal("0.00");
            bdecimal.setScale(2);
            avansFormattedTextField = new JFormattedTextField(bdecimal);
            avansFormattedTextField.setVisible(false);
            DecimalFormat format = new DecimalFormat("#,###.##", dfs);
            DefaultFormatter df = new NumberFormatter(format);
            df.setValueClass(avansFormattedTextField.getValue().getClass());
            DefaultFormatterFactory dff = new DefaultFormatterFactory(df, df, df);
            avansFormattedTextField.setFormatterFactory(dff);
            avansFormattedTextField.setInputVerifier(new PrecentVerifier());
            avansFormattedTextField.setBounds(new Rectangle(470, 290, 130, 20));
        }
        return avansFormattedTextField;
    }

    /**
	 * This method initializes sumAvansaFormattedTextField	
	 * 	
	 * @return javax.swing.JFormattedTextField	
	 */
    private JFormattedTextField getSumAvansaFormattedTextField() {
        if (sumAvansaFormattedTextField == null) {
            try {
                DecimalFormatSymbols dfs = new DecimalFormatSymbols();
                dfs.setGroupingSeparator(' ');
                dfs.setDecimalSeparator('.');
                BigDecimal bdecimal = new BigDecimal("0.00");
                bdecimal.setScale(2);
                sumAvansaFormattedTextField = new JFormattedTextField(bdecimal);
                sumAvansaFormattedTextField.setVisible(false);
                DecimalFormat format = new DecimalFormat("#,###.##", dfs);
                DefaultFormatter df = new NumberFormatter(format);
                df.setValueClass(sumDogFormattedTextField.getValue().getClass());
                DefaultFormatterFactory dff = new DefaultFormatterFactory(df, df, df);
                sumAvansaFormattedTextField.setFormatterFactory(dff);
                sumAvansaFormattedTextField.setInputVerifier(new SumFormattedTextFieldVerifier());
                sumAvansaFormattedTextField.setBounds(new Rectangle(470, 321, 131, 20));
            } catch (Exception e) {
                e.printStackTrace();
            }
            sumAvansaFormattedTextField.addFocusListener(new java.awt.event.FocusAdapter() {

                @Override
                public void focusGained(java.awt.event.FocusEvent e) {
                    BigDecimal sumDog;
                    try {
                        sumDog = (BigDecimal) sumDogFormattedTextField.getFormatter().stringToValue(sumDogFormattedTextField.getText());
                        System.out.println(sumDog);
                        BigDecimal avans = (BigDecimal) avansFormattedTextField.getFormatter().stringToValue(avansFormattedTextField.getText());
                        System.out.println(avans);
                        BigDecimal res = (sumDog.multiply(avans)).divide(new BigDecimal(100));
                        System.out.println(res);
                        System.out.println(res);
                        sumAvansaFormattedTextField.setValue(res);
                    } catch (ParseException e1) {
                        JOptionPane.showMessageDialog(null, properties.getProperty("manualSumInput"));
                        e1.printStackTrace();
                    }
                }
            });
        }
        return sumAvansaFormattedTextField;
    }

    /**
	 * This method initializes penjaFormattedTextField	
	 * 	
	 * @return javax.swing.JFormattedTextField	
	 */
    private JFormattedTextField getPenjaFormattedTextField() {
        if (penjaFormattedTextField == null) {
            DecimalFormatSymbols dfs = new DecimalFormatSymbols();
            dfs.setDecimalSeparator('.');
            BigDecimal bdecimal = new BigDecimal("0.00");
            bdecimal.setScale(2);
            penjaFormattedTextField = new JFormattedTextField(bdecimal);
            DecimalFormat format = new DecimalFormat("#,###.##", dfs);
            DefaultFormatter df = new NumberFormatter(format);
            df.setValueClass(avansFormattedTextField.getValue().getClass());
            DefaultFormatterFactory dff = new DefaultFormatterFactory(df, df, df);
            penjaFormattedTextField.setFormatterFactory(dff);
            penjaFormattedTextField.setInputVerifier(new PrecentVerifier());
            penjaFormattedTextField.setBounds(new Rectangle(470, 351, 131, 20));
        }
        return penjaFormattedTextField;
    }

    /**
	 * This method initializes periodPeniComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
    private JComboBox getPeriodPeniComboBox() {
        if (periodPeniComboBox == null) {
            DefaultComboBoxModel cbmodel = new DefaultComboBoxModel();
            cbmodel.addElement(properties.getProperty("once"));
            cbmodel.addElement(properties.getProperty("day"));
            cbmodel.addElement(properties.getProperty("month"));
            cbmodel.addElement(properties.getProperty("year"));
            periodPeniComboBox = new JComboBox(cbmodel);
            periodPeniComboBox.setBounds(new Rectangle(470, 379, 131, 22));
        }
        return periodPeniComboBox;
    }

    /**
	 * This method initializes saveButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getSaveButton() {
        if (saveButton == null) {
            saveButton = new JButton();
            saveButton.setBounds(new Rectangle(548, 435, 108, 24));
            saveButton.setText(properties.getProperty("save"));
            saveButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    Contract contract = getContract();
                    try {
                        DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
                        java.sql.Date regdate = new java.sql.Date((df.parse(regDateFormattedTextField.getText())).getTime());
                        System.out.println(regdate + " " + df.parse(regDateFormattedTextField.getText()));
                        java.sql.Date datezakl = new java.sql.Date((df.parse(dateZaklFormattedTextField.getText())).getTime());
                        System.out.println(datezakl + " " + df.parse(dateZaklFormattedTextField.getText()));
                        java.sql.Date sdate = new java.sql.Date((df.parse(dateSFormattedTextField.getText())).getTime());
                        System.out.println(sdate + " " + df.parse(dateSFormattedTextField.getText()));
                        java.sql.Date podate = new java.sql.Date((df.parse(datePOFormattedTextField.getText())).getTime());
                        System.out.println(podate + " " + df.parse(datePOFormattedTextField.getText()));
                        contract.setId(2L);
                        contract.setRegDate(regdate);
                        Service service = new Service();
                        service.setId(2L);
                        service.setName("testservice");
                        Subservice subservice = new Subservice();
                        subservice.setId(1L);
                        subservice.setService(service);
                        subservice.setName("testsubservice");
                        Set<Subservice> subservices = new HashSet<Subservice>();
                        DaoFactory daoFactory = DaoFactory.getInstance();
                        daoFactory.getServiceDAO().saveOrUpdate(service);
                        daoFactory.getSubServiceDAO().saveOrUpdate(subservice);
                        daoFactory.getContractDAO().save(contract);
                        JOptionPane.showMessageDialog(null, "Contract is successfuly saved. ");
                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(null, properties.getProperty("dogNotSaved"));
                        e1.printStackTrace();
                    }
                }
            });
        }
        return saveButton;
    }

    /**
	 * This method initializes clearButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getClearButton() {
        if (clearButton == null) {
            clearButton = new JButton();
            clearButton.setBounds(new Rectangle(669, 434, 109, 26));
            clearButton.setText(properties.getProperty("clear"));
            clearButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                }
            });
        }
        return clearButton;
    }

    /**
	 * This method initializes inoutRadioButton	
	 * 	
	 * @return javax.swing.JRadioButton	
	 */
    private JRadioButton getInoutRadioButton() {
        if (inoutRadioButton == null) {
            inoutRadioButton = new JRadioButton();
            inoutRadioButton.setSelected(true);
            inoutRadioButton.setBounds(new Rectangle(590, 15, 21, 21));
            if (getInOutState() == true) {
                avansLabel.setVisible(false);
                avansFormattedTextField.setVisible(false);
                sumAvansaLabel.setVisible(false);
                sumAvansaFormattedTextField.setVisible(false);
                penjaLabel.setVisible(true);
                penjaFormattedTextField.setVisible(true);
                periodPeniLabel.setVisible(true);
                periodPeniComboBox.setVisible(true);
            } else if (getInOutState() == false) {
                avansLabel.setVisible(true);
                avansFormattedTextField.setVisible(true);
                sumAvansaLabel.setVisible(true);
                sumAvansaFormattedTextField.setVisible(true);
                penjaLabel.setVisible(false);
                penjaFormattedTextField.setVisible(false);
                periodPeniLabel.setVisible(false);
                periodPeniComboBox.setVisible(false);
            }
            System.out.println("itemStateChanged()");
        }
        return inoutRadioButton;
    }

    private boolean getInOutState() {
        if (inoutRadioButton.isSelected()) {
            return true;
        }
        return false;
    }

    /**
	 * 
	 */
    public DogovorInput2(Properties properties) {
        this.properties = properties;
        inLabel = new JLabel();
        inLabel.setBounds(new Rectangle(480, 17, 103, 14));
        inLabel.setText(properties.getProperty("in") + "\\" + properties.getProperty("out"));
        periodPeniLabel = new JLabel();
        periodPeniLabel.setBounds(new Rectangle(370, 387, 82, 14));
        periodPeniLabel.setText(properties.getProperty("periodPeni"));
        penjaLabel = new JLabel();
        penjaLabel.setBounds(new Rectangle(369, 357, 67, 14));
        penjaLabel.setText(properties.getProperty("penja"));
        sumAvansaLabel = new JLabel();
        sumAvansaLabel.setBounds(new Rectangle(369, 328, 99, 14));
        sumAvansaLabel.setText(properties.getProperty("avansSum"));
        avansLabel = new JLabel();
        avansLabel.setBounds(new Rectangle(370, 296, 82, 14));
        avansLabel.setText(properties.getProperty("avans"));
        telLabel = new JLabel();
        telLabel.setBounds(new Rectangle(30, 388, 75, 14));
        telLabel.setText(properties.getProperty("tel"));
        otvetstvennijLabel = new JLabel();
        otvetstvennijLabel.setBounds(new Rectangle(29, 358, 129, 14));
        otvetstvennijLabel.setText(properties.getProperty("resp"));
        sluzhbaLabel = new JLabel();
        sluzhbaLabel.setBounds(new Rectangle(30, 327, 93, 14));
        sluzhbaLabel.setText(properties.getProperty("service"));
        podsluzhbaLabel = new JLabel();
        podsluzhbaLabel.setBounds(new Rectangle(30, 298, 110, 14));
        podsluzhbaLabel.setText(properties.getProperty("subservice"));
        naznPlatLabel = new JLabel();
        naznPlatLabel.setBounds(new Rectangle(30, 267, 130, 14));
        naznPlatLabel.setText(properties.getProperty("payPurpose"));
        postavshikLabel = new JLabel();
        postavshikLabel.setBounds(new Rectangle(29, 238, 118, 14));
        postavshikLabel.setText(properties.getProperty("supplier"));
        valLabel = new JLabel();
        valLabel.setBounds(new Rectangle(30, 206, 71, 14));
        valLabel.setText(properties.getProperty("currency"));
        periodLabel = new JLabel();
        periodLabel.setBounds(new Rectangle(30, 177, 79, 14));
        periodLabel.setText(properties.getProperty("period"));
        sumDogLabel = new JLabel();
        sumDogLabel.setBounds(new Rectangle(31, 146, 113, 14));
        sumDogLabel.setText(properties.getProperty("dogSum"));
        zakazLabel = new JLabel();
        zakazLabel.setBounds(new Rectangle(480, 86, 60, 14));
        zakazLabel.setText(properties.getProperty("order"));
        srokDogLabel = new JLabel();
        srokDogLabel.setBounds(new Rectangle(229, 85, 157, 14));
        srokDogLabel.setText(properties.getProperty("dogValidity"));
        poLabel = new JLabel();
        poLabel.setBounds(new Rectangle(359, 117, 30, 14));
        poLabel.setText(properties.getProperty("to"));
        sLabel = new JLabel();
        sLabel.setBounds(new Rectangle(192, 117, 30, 14));
        sLabel.setText(properties.getProperty("from"));
        dateZaklLabel = new JLabel();
        dateZaklLabel.setBounds(new Rectangle(30, 85, 184, 14));
        dateZaklLabel.setText(properties.getProperty("dogDate"));
        numDogLabel = new JLabel();
        numDogLabel.setBounds(new Rectangle(30, 46, 117, 14));
        numDogLabel.setText(properties.getProperty("dogNum"));
        regDateLabel = new JLabel();
        regDateLabel.setBounds(new Rectangle(281, 17, 107, 14));
        regDateLabel.setText(properties.getProperty("regDate"));
        regNumLabel = new JLabel();
        regNumLabel.setBounds(new Rectangle(30, 18, 125, 14));
        regNumLabel.setText(properties.getProperty("regNum"));
        this.setLayout(null);
        this.add(regNumLabel, null);
        this.add(getRegNumTextField(), null);
        this.add(regDateLabel, null);
        this.add(getRegDateFormattedTextField(), null);
        this.add(numDogLabel, null);
        this.add(getNumDogTextField(), null);
        this.add(dateZaklLabel, null);
        this.add(getDateZaklFormattedTextField(), null);
        this.add(getDateSFormattedTextField(), null);
        this.add(getDatePOFormattedTextField(), null);
        this.add(sLabel, null);
        this.add(poLabel, null);
        this.add(srokDogLabel, null);
        this.add(zakazLabel, null);
        this.add(getZakazComboBox(), null);
        this.add(sumDogLabel, null);
        this.add(getSumDogFormattedTextField(), null);
        this.add(periodLabel, null);
        this.add(getPeriodComboBox(), null);
        this.add(valLabel, null);
        this.add(getValComboBox(), null);
        this.add(postavshikLabel, null);
        this.add(getPostavshikComboBox(), null);
        this.add(naznPlatLabel, null);
        this.add(getNaznPlatComboBox(), null);
        this.add(podsluzhbaLabel, null);
        this.add(sluzhbaLabel, null);
        this.add(getPodsluzhbaComboBox(), null);
        this.add(getSluzhbaComboBox(), null);
        this.add(otvetstvennijLabel, null);
        this.add(getOtvetstvennijTextField(), null);
        this.add(telLabel, null);
        this.add(getTelFormattedTextField(), null);
        this.add(avansLabel, null);
        avansLabel.setVisible(false);
        this.add(getAvansFormattedTextField(), null);
        this.add(sumAvansaLabel, null);
        sumAvansaLabel.setVisible(false);
        this.add(getSumAvansaFormattedTextField(), null);
        this.add(penjaLabel, null);
        this.add(getPenjaFormattedTextField(), null);
        this.add(periodPeniLabel, null);
        this.add(getPeriodPeniComboBox(), null);
        this.add(getSaveButton(), null);
        this.add(getClearButton(), null);
        this.add(getInoutRadioButton(), null);
        this.add(inLabel, null);
    }

    /**
	 * @param contract the contract to set
	 */
    public void setContract(Contract contract) {
        this.contract = contract;
    }

    /**
	 * @return the contract
	 */
    public Contract getContract() {
        return contract;
    }
}
