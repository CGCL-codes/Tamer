package artem.finance.gui;

import artem.finance.gui.db.BeansFactory;
import artem.finance.gui.utils.GuiUtils;
import artem.finance.gui.verifier.DateFormattedTextFieldVerifier;
import artem.finance.server.persist.Bank;
import artem.finance.server.persist.Contract;
import artem.finance.server.persist.Organization;
import artem.finance.server.persist.Prilozhenie;
import artem.finance.server.persist.Schet;
import artem.finance.server.persist.Vipiska;
import artem.finance.server.persist.Zatrata;
import artem.finance.server.persist.beans.BankBean;
import artem.finance.server.persist.beans.OrganizationBean;
import artem.finance.server.persist.beans.PrilozhenieBean;
import artem.finance.server.persist.beans.SchetBean;
import artem.finance.server.persist.beans.VipiskaBean;
import artem.finance.server.persist.beans.ZatrataBean;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import javax.swing.JTextField;
import javax.swing.JFormattedTextField;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JTextPane;
import java.lang.String;
import javax.swing.JCheckBox;
import javax.swing.text.DefaultFormatter;
import org.apache.commons.lang.StringUtils;

/**
 * @author Burtsev Ivan
 *
 */
public class VipiskaInput extends JInternalFrame {

    private static final long serialVersionUID = 1L;

    private JPanel jContentPane = null;

    private JLabel dateLabel = null;

    private JFormattedTextField dateFormattedTextField = null;

    private JLabel bankLabel = null;

    private JTextField valTextField = null;

    private JButton saveButton = null;

    private JButton clearButton = null;

    private JButton removeButton = null;

    private JComboBox bankComboBox = null;

    private JComboBox countComboBox = null;

    private JLabel countLabel = null;

    private JLabel valLabel = null;

    private JLabel orgLabel = null;

    private JComboBox orgComboBox = null;

    private JLabel groupLabel = null;

    private JComboBox groupComboBox = null;

    private JLabel purposeLabel = null;

    private JTextPane purposeTextPane = null;

    private JLabel valInputLabel = null;

    private JLabel grnInputLabel = null;

    private JFormattedTextField valInputFormattedTextField = null;

    private JFormattedTextField grnInputFormattedTextField = null;

    private JLabel valOutputLabel = null;

    private JFormattedTextField valOutputFormattedTextField = null;

    private JLabel grnOutputLabel = null;

    private JFormattedTextField grnOutputFormattedTextField = null;

    private JLabel dogovorLabel = null;

    private JComboBox dogovorComboBox = null;

    private JLabel summaLabel = null;

    private JFormattedTextField summaPoDogovoruFormattedTextField = null;

    private JLabel zatrataLabel = null;

    private JComboBox zatrataComboBox = null;

    private JLabel zatrataSummaLabel = null;

    private JFormattedTextField zatrataSummaFormattedTextField = null;

    private JFormattedTextField contractCurrencyTextField = null;

    private JTextField valZatrataTextField = null;

    private JLabel valDogovorLabel = null;

    private JLabel valZatrataLabel = null;

    private JLabel prilozhenieLabel = null;

    private JComboBox prilozhenieComboBox = null;

    private JCheckBox srokCheckBox = null;

    private JCheckBox priznakCheckBox = null;

    private BeansFactory factory;

    private Vipiska selectedVipiska;

    private Properties properties;

    private Properties getProperties() {
        return this.properties;
    }

    public void setSelectedVipiska(Vipiska vipiska) {
        try {
            selectedVipiska = (Vipiska) factory.getVipiskaServiceSLSB().findById(vipiska.getId());
            SimpleDateFormat sdformat = new SimpleDateFormat("dd.MM.yyyy");
            String newDate = sdformat.format(selectedVipiska.getDate());
            getDateFormattedTextField().setText(newDate);
            getDateFormattedTextField().setEditable(true);
            Bank bank = selectedVipiska.getBank();
            if (bank != null) {
                getBankComboBox().setSelectedItem(bank);
            }
            Schet schet = selectedVipiska.getSchet();
            if (schet != null && schet.getValuta() != null) {
                getValTextField().setText(schet.getValuta().getName());
                getCountComboBox().setSelectedItem(schet);
            }
            Organization organization = selectedVipiska.getOrganization();
            if (organization != null) {
                getOrgComboBox().setSelectedItem(organization);
            }
            if (selectedVipiska.getGroup() != null) {
                getGroupComboBox().setSelectedItem(selectedVipiska.getGroup());
            }
            getPurposeTextPane().setText(selectedVipiska.getNaznachenie());
            getValInputFormattedTextField().setText(GuiUtils.getDefaultFormatterForMoneyUnitsFields().valueToString(selectedVipiska.getValInput()));
            getGrnInputFormattedTextField().setText(GuiUtils.getDefaultFormatterForMoneyUnitsFields().valueToString(selectedVipiska.getGrnInput()));
            getGrnOutputFormattedTextField().setText(GuiUtils.getDefaultFormatterForMoneyUnitsFields().valueToString(selectedVipiska.getGrnOutput()));
            getValOutputFormattedTextField().setText(GuiUtils.getDefaultFormatterForMoneyUnitsFields().valueToString(selectedVipiska.getValOutput()));
            Contract leadContract = selectedVipiska.getContract();
            if (leadContract != null) {
                getDogovorComboBox().setSelectedItem(leadContract);
                if (selectedVipiska.getSumma() != null) {
                    getSummaPoDogovoruFormattedTextField().setText(GuiUtils.getDefaultFormatterForMoneyUnitsFields().valueToString(selectedVipiska.getSumma()));
                }
                if (leadContract.getValuta() != null) {
                    getContractCurrencyTextField().setText(leadContract.getValuta().getName());
                }
            }
            Zatrata zatrata = selectedVipiska.getZatrata();
            if (zatrata != null) {
                getZatrataComboBox().setSelectedItem(zatrata);
            }
            getZatrataSummaFormattedTextField().setText(GuiUtils.getDefaultFormatterForMoneyUnitsFields().valueToString(selectedVipiska.getZatrataSumma()));
            getValZatrataTextField().setText("USD");
            Prilozhenie prilozhenie = selectedVipiska.getPrilozhenie();
            if (prilozhenie != null) {
                getPrilozhenieComboBox().setSelectedItem(prilozhenie);
            }
            getSrokCheckBox().setSelected(selectedVipiska.getSrok());
            getPriznakCheckBox().setSelected(selectedVipiska.getPriznak());
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(getPurposeTextPane(), "Can not find appropriate payment, because of connection issue.");
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(getPurposeTextPane(), "Can not parse some of the money units.");
            e.printStackTrace();
        }
    }

    /**
	 * This method initializes dateFormattedTextField	
	 * 	
	 * @return javax.swing.JFormattedTextField	
	 */
    private JFormattedTextField getDateFormattedTextField() {
        if (dateFormattedTextField == null) {
            dateFormattedTextField = new JFormattedTextField(GuiUtils.getMformatter());
            dateFormattedTextField.setColumns(10);
            dateFormattedTextField.setInputVerifier(new DateFormattedTextFieldVerifier());
            dateFormattedTextField.setBounds(new Rectangle(100, 10, 70, 20));
            dateFormattedTextField.setValue(GuiUtils.productCurrentDate());
        }
        return dateFormattedTextField;
    }

    /**
	 * This method initializes valTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    private JTextField getValTextField() {
        if (valTextField == null) {
            valTextField = new JTextField();
            valTextField.setEditable(false);
            valTextField.setBounds(new Rectangle(588, 39, 50, 20));
            if (getCountComboBox().getSelectedIndex() > 0) {
                valTextField.setText(((Schet) getCountComboBox().getSelectedItem()).getValuta().getName());
            }
        }
        return valTextField;
    }

    /**
	 * This method initializes saveButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getSaveButton() {
        if (saveButton == null) {
            saveButton = new JButton();
            saveButton.setBounds(new Rectangle(265, 405, 120, 26));
            saveButton.setText(properties.getProperty("save"));
            saveButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    if (selectedVipiska == null) {
                        selectedVipiska = new Vipiska();
                    }
                    try {
                        if (GuiUtils.getDateFromFTF(getDateFormattedTextField()) == null) {
                            JOptionPane.showMessageDialog(null, "Date is empty. Please input correct date.");
                            return;
                        }
                        selectedVipiska.setDate(GuiUtils.getDateFromFTF(getDateFormattedTextField()));
                        selectedVipiska.setBank((Bank) getBankComboBox().getSelectedItem());
                        selectedVipiska.setSchet((Schet) getCountComboBox().getSelectedItem());
                        Object organization = getOrgComboBox().getSelectedItem();
                        if (organization instanceof Organization && !organization.equals(new Organization())) {
                            selectedVipiska.setOrganization((Organization) getOrgComboBox().getSelectedItem());
                        } else {
                            selectedVipiska.setOrganization(null);
                        }
                        selectedVipiska.setGroup((String) getGroupComboBox().getSelectedItem());
                        String purpose = getPurposeTextPane().getText();
                        if (purpose == null || "".equals(purpose)) {
                            selectedVipiska.setNaznachenie(StringUtils.EMPTY);
                        } else {
                            selectedVipiska.setNaznachenie(purpose);
                        }
                        Object contract = getDogovorComboBox().getSelectedItem();
                        if (contract instanceof Contract && !contract.equals(new Contract()) && selectedVipiska.getOrganization() != null) {
                            selectedVipiska.setContract((Contract) getDogovorComboBox().getSelectedItem());
                            selectedVipiska.setSumma(GuiUtils.getBigDecimalFromFormattedTextField(getSummaPoDogovoruFormattedTextField()));
                        } else {
                            selectedVipiska.setContract(null);
                        }
                        selectedVipiska.setValInput(GuiUtils.getBigDecimalFromFormattedTextField(getValInputFormattedTextField()));
                        selectedVipiska.setValOutput(GuiUtils.getBigDecimalFromFormattedTextField(getValOutputFormattedTextField()));
                        selectedVipiska.setGrnInput(GuiUtils.getBigDecimalFromFormattedTextField(getGrnInputFormattedTextField()));
                        selectedVipiska.setGrnOutput(GuiUtils.getBigDecimalFromFormattedTextField(getGrnOutputFormattedTextField()));
                        Object zatrata = getZatrataComboBox().getSelectedItem();
                        if (zatrata instanceof Zatrata && !zatrata.equals(new Zatrata())) {
                            selectedVipiska.setZatrata((Zatrata) zatrata);
                        } else {
                            selectedVipiska.setZatrata(null);
                        }
                        selectedVipiska.setZatrataSumma(GuiUtils.getBigDecimalFromFormattedTextField(getZatrataSummaFormattedTextField()));
                        Object prilozhenie = getPrilozhenieComboBox().getSelectedItem();
                        if (prilozhenie instanceof Prilozhenie && !prilozhenie.equals(new Prilozhenie())) {
                            selectedVipiska.setPrilozhenie((Prilozhenie) prilozhenie);
                        } else {
                            selectedVipiska.setPrilozhenie(null);
                        }
                        selectedVipiska.setPriznak(getPriznakCheckBox().isSelected());
                        selectedVipiska.setSrok(getSrokCheckBox().isSelected());
                        factory.getVipiskaServiceSLSB().saveOrUpdate(new VipiskaBean(selectedVipiska));
                        clear();
                        selectedVipiska = null;
                        JOptionPane.showMessageDialog(getPurposeTextPane(), properties.getProperty("vipiska") + " " + properties.getProperty("savedSuccessfully1"));
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(getPurposeTextPane(), properties.getProperty("vipiska") + " " + properties.getProperty("notSaved1"));
                        ex.printStackTrace();
                    }
                }
            });
        }
        return saveButton;
    }

    private void clear() {
        GuiUtils.clear(getContentPane());
        getPriznakCheckBox().setSelected(false);
        getSrokCheckBox().setSelected(false);
        getPurposeTextPane().setText("");
        getValInputFormattedTextField().setText("0.00");
        getValOutputFormattedTextField().setText("0.00");
        getGrnInputFormattedTextField().setText("0.00");
        getGrnOutputFormattedTextField().setText("0.00");
        getSummaPoDogovoruFormattedTextField().setText("0.00");
        getZatrataSummaFormattedTextField().setText("0.00");
        selectedVipiska = null;
    }

    /**
	 * This method initializes clearButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getClearButton() {
        if (clearButton == null) {
            clearButton = new JButton();
            clearButton.setBounds(new Rectangle(393, 405, 120, 26));
            clearButton.setText(properties.getProperty("clear"));
            clearButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    clear();
                }
            });
        }
        return clearButton;
    }

    /**
	 * This method initializes remove button
	 * 
	 * @return javax.swing.JButton
	 */
    private JButton getRemoveButton() {
        if (removeButton == null) {
            removeButton = new JButton();
            removeButton.setBounds(new Rectangle(516, 405, 120, 26));
            removeButton.setText("Remove");
            removeButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    try {
                        if (selectedVipiska == null) {
                            JOptionPane.showMessageDialog(getPurposeTextPane(), "Please select the payment to delete using payment search filter.");
                            return;
                        }
                        int choice = JOptionPane.showConfirmDialog(getPurposeTextPane(), "Do you really want to delete selected payment?", "Payment deletion", JOptionPane.OK_CANCEL_OPTION);
                        if (choice == 0) {
                            factory.getVipiskaServiceSLSB().delete(new VipiskaBean(selectedVipiska));
                            clear();
                        }
                    } catch (RemoteException re) {
                        JOptionPane.showMessageDialog(getPurposeTextPane(), "Can not delete the document, because of connection issue!");
                        re.printStackTrace();
                    }
                }
            });
        }
        return removeButton;
    }

    /**
	 * This method initializes bankComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
    private JComboBox getBankComboBox() {
        if (bankComboBox == null) {
            bankComboBox = GuiUtils.createBankComboBox(factory, bankComboBox, new Rectangle(100, 40, 192, 20));
            bankComboBox.addItemListener(new ItemListener() {

                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        if (!(e.getItem() instanceof Bank)) {
                            return;
                        }
                        Bank item = (Bank) e.getItem();
                        try {
                            if (item.getId() != null) {
                                List<Object> beans = factory.getSchetServiceSLSB().findAllCountsForBank(new BankBean(item));
                                beans.add(0, new Schet());
                                Object[] counts = beans.toArray();
                                DefaultComboBoxModel model = new DefaultComboBoxModel(counts);
                                getCountComboBox().setModel(model);
                            } else {
                                getCountComboBox().setModel(new DefaultComboBoxModel(new Object[0]));
                            }
                            getValTextField().setText("");
                            getContractCurrencyTextField().setText("");
                        } catch (RemoteException e1) {
                            JOptionPane.showMessageDialog(null, "Can not get counts for selected bank from the database.");
                            e1.printStackTrace();
                        }
                    }
                }
            });
        }
        return bankComboBox;
    }

    /**
	 * This method initializes countComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
    private JComboBox getCountComboBox() {
        if (countComboBox == null) {
            countComboBox = new JComboBox();
            countComboBox.setBounds(new Rectangle(342, 41, 171, 20));
            List<SchetBean> counts;
            List<Schet> unpacked = new ArrayList<Schet>();
            try {
                counts = factory.getSchetServiceSLSB().findAll();
                counts.add(0, new SchetBean(new Schet()));
                for (SchetBean bean : counts) {
                    unpacked.add(bean.getSchet());
                }
                Object[] countsWithEmptyValue = unpacked.toArray();
                DefaultComboBoxModel model = new DefaultComboBoxModel(countsWithEmptyValue);
                countComboBox.setModel(model);
                GuiUtils.makeComboBoxAutoSearchible(countComboBox, GuiUtils.getCountCellRenderer());
            } catch (RemoteException e1) {
                e1.printStackTrace();
            }
            countComboBox.addItemListener(new ItemListener() {

                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        if (!(e.getItem() instanceof Schet)) {
                            return;
                        }
                        Schet item = (Schet) e.getItem();
                        if (item.getValuta() != null) {
                            getValTextField().setText(item.getValuta().getName());
                        } else {
                            getValTextField().setText("");
                        }
                        if (item.getBank() != null) {
                            getBankComboBox().setSelectedItem(item.getBank());
                        } else {
                            getBankComboBox().setSelectedIndex(0);
                        }
                    }
                }
            });
        }
        return countComboBox;
    }

    /**
	 * This method initializes orgComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
    private JComboBox getOrgComboBox() {
        if (orgComboBox == null) {
            orgComboBox = GuiUtils.createOrganizationComboBox(factory, new Rectangle(100, 70, 543, 20));
            orgComboBox.addItemListener(new ItemListener() {

                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        if (!(e.getItem() instanceof Organization)) {
                            turnOnContractLineElements(false);
                            return;
                        }
                        Organization org = (Organization) e.getItem();
                        List<Object> contracts = new ArrayList<Object>();
                        try {
                            if (!new Organization().equals(org)) {
                                contracts = factory.getContractServiceSLSB().getContractsForOrganization(new OrganizationBean(org));
                            }
                        } catch (RemoteException re) {
                            JOptionPane.showMessageDialog(null, "Can not get contracts for this organization.");
                            re.printStackTrace();
                        }
                        Object[] contractsWithEmptyValue = contracts.toArray();
                        if (contracts.size() > 0) {
                            DefaultComboBoxModel model = new DefaultComboBoxModel(contractsWithEmptyValue);
                            model.insertElementAt(new Contract(), 0);
                            getDogovorComboBox().setModel(model);
                            turnOnContractLineElements(true);
                        } else {
                            getDogovorComboBox().setModel(new DefaultComboBoxModel(new Object[0]));
                            turnOnContractLineElements(false);
                        }
                    }
                }
            });
        }
        return orgComboBox;
    }

    /**
	 * This method is responsible for enabling and disabling elements of contract
	 * filling group.
	 *  
	 * @param on - true, makes components enabled, false - disables the components.
	 */
    private void turnOnContractLineElements(boolean on) {
        getDogovorComboBox().setEnabled(on);
        Contract item = (Contract) getDogovorComboBox().getSelectedItem();
        if (item != null && item.getValuta() != null) {
            getContractCurrencyTextField().setText(item.getValuta().getName());
        } else {
            getContractCurrencyTextField().setText("");
        }
        if (!on) {
            getSummaPoDogovoruFormattedTextField().setText("");
            getContractCurrencyTextField().setText("");
        }
        getContractCurrencyTextField().setEditable(on);
        getSummaPoDogovoruFormattedTextField().setEditable(on);
        getSummaPoDogovoruFormattedTextField().setEnabled(on);
        summaLabel.setEnabled(on);
        dogovorLabel.setEnabled(on);
        valDogovorLabel.setEnabled(on);
    }

    /**
	 * This method initializes groupComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
    private JComboBox getGroupComboBox() {
        if (groupComboBox == null) {
            groupComboBox = GuiUtils.createGroupComboBox(factory, groupComboBox, new Rectangle(100, 100, 544, 20));
        }
        return groupComboBox;
    }

    /**
	 * This method initializes purposeTextPane	
	 * 	
	 * @return javax.swing.JTextPane	
	 */
    private JTextPane getPurposeTextPane() {
        if (purposeTextPane == null) {
            purposeTextPane = new JTextPane();
            purposeTextPane.setBounds(new Rectangle(10, 150, 633, 70));
            purposeTextPane.setLayout(new BorderLayout());
            purposeTextPane.setBorder(BorderFactory.createLoweredBevelBorder());
        }
        return purposeTextPane;
    }

    /**
	 * This method initializes valInputFormattedTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    private JFormattedTextField getValInputFormattedTextField() {
        if (valInputFormattedTextField == null) {
            valInputFormattedTextField = GuiUtils.createMoneyUnitsFormattedTextField(valInputFormattedTextField, getProperties());
            valInputFormattedTextField.setBounds(new Rectangle(135, 230, 130, 20));
            valInputFormattedTextField.setText("0.00");
        }
        return valInputFormattedTextField;
    }

    /**
	 * This method initializes grnInputTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    private JFormattedTextField getGrnInputFormattedTextField() {
        if (grnInputFormattedTextField == null) {
            grnInputFormattedTextField = GuiUtils.createMoneyUnitsFormattedTextField(grnInputFormattedTextField, getProperties());
            grnInputFormattedTextField.setBounds(new Rectangle(135, 260, 130, 20));
            grnInputFormattedTextField.setText("0.00");
        }
        return grnInputFormattedTextField;
    }

    /**
	 * This method initializes valOutputTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    private JFormattedTextField getValOutputFormattedTextField() {
        if (valOutputFormattedTextField == null) {
            valOutputFormattedTextField = GuiUtils.createMoneyUnitsFormattedTextField(valOutputFormattedTextField, getProperties());
            valOutputFormattedTextField.setBounds(new Rectangle(435, 231, 130, 20));
            valOutputFormattedTextField.setText("0.00");
        }
        return valOutputFormattedTextField;
    }

    /**
	 * This method initializes grnOutputTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    private JFormattedTextField getGrnOutputFormattedTextField() {
        if (grnOutputFormattedTextField == null) {
            grnOutputFormattedTextField = GuiUtils.createMoneyUnitsFormattedTextField(grnOutputFormattedTextField, getProperties());
            grnOutputFormattedTextField.setBounds(new Rectangle(435, 259, 130, 20));
            grnOutputFormattedTextField.setText("0.00");
        }
        return grnOutputFormattedTextField;
    }

    /**
	 * This method initializes dogovorComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
    private JComboBox getDogovorComboBox() {
        if (dogovorComboBox == null) {
            dogovorComboBox = new JComboBox();
            dogovorComboBox.setBounds(new Rectangle(100, 290, 255, 20));
            dogovorComboBox.setEditable(false);
            dogovorComboBox.setEnabled(false);
            dogovorComboBox.addItemListener(new ItemListener() {

                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        Contract item = (Contract) e.getItem();
                        if (item.getValuta() != null) {
                            getContractCurrencyTextField().setText(item.getValuta().getName());
                        } else {
                            getContractCurrencyTextField().setText("");
                        }
                    }
                }
            });
        }
        return dogovorComboBox;
    }

    /**
	 * This method initializes summaTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    private JFormattedTextField getSummaPoDogovoruFormattedTextField() {
        if (summaPoDogovoruFormattedTextField == null) {
            summaPoDogovoruFormattedTextField = GuiUtils.createMoneyUnitsFormattedTextField(summaPoDogovoruFormattedTextField, getProperties());
            summaPoDogovoruFormattedTextField.setBounds(new Rectangle(435, 288, 100, 20));
            summaPoDogovoruFormattedTextField.setText("0.00");
            summaPoDogovoruFormattedTextField.setEditable(false);
            summaPoDogovoruFormattedTextField.setEnabled(false);
        }
        return summaPoDogovoruFormattedTextField;
    }

    /**
	 * This method initializes zatrataComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
    private JComboBox getZatrataComboBox() {
        if (zatrataComboBox == null) {
            zatrataComboBox = new JComboBox();
            zatrataComboBox.setBounds(new Rectangle(100, 320, 255, 20));
            List<ZatrataBean> zatrati;
            List<Zatrata> unpacked = new ArrayList<Zatrata>();
            try {
                zatrati = factory.getZatrataServiceSLSB().findAll();
                zatrati.add(0, new ZatrataBean(new Zatrata()));
                for (ZatrataBean bean : zatrati) {
                    unpacked.add(bean.getZatrata());
                }
                Object[] zatratiWithEmptyValue = unpacked.toArray();
                Arrays.sort(zatratiWithEmptyValue);
                DefaultComboBoxModel model = new DefaultComboBoxModel(zatratiWithEmptyValue);
                zatrataComboBox.setModel(model);
                GuiUtils.makeComboBoxAutoSearchible(zatrataComboBox, GuiUtils.getZatrataCellRenderer());
            } catch (RemoteException e) {
                JOptionPane.showMessageDialog(null, "Can not get all the values, because of remote problems." + e.getMessage());
                e.printStackTrace();
            }
        }
        return zatrataComboBox;
    }

    /**
	 * This method initializes zatrataSummaTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    private JFormattedTextField getZatrataSummaFormattedTextField() {
        if (zatrataSummaFormattedTextField == null) {
            zatrataSummaFormattedTextField = GuiUtils.createMoneyUnitsFormattedTextField(zatrataSummaFormattedTextField, getProperties());
            zatrataSummaFormattedTextField.setBounds(new Rectangle(435, 316, 100, 20));
            zatrataSummaFormattedTextField.setText("0.00");
        }
        return zatrataSummaFormattedTextField;
    }

    /**
	 * This method initializes valDogovorTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    private JTextField getContractCurrencyTextField() {
        if (contractCurrencyTextField == null) {
            contractCurrencyTextField = new JFormattedTextField();
            contractCurrencyTextField.setBounds(new Rectangle(600, 288, 41, 20));
            contractCurrencyTextField.setEditable(false);
            contractCurrencyTextField.setEnabled(false);
            if (getDogovorComboBox().getSelectedIndex() > 0) {
                contractCurrencyTextField.setText(((Contract) getDogovorComboBox().getSelectedItem()).getValuta().getName());
            }
        }
        return contractCurrencyTextField;
    }

    /**
	 * This method initializes valZatrataTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */
    private JTextField getValZatrataTextField() {
        if (valZatrataTextField == null) {
            valZatrataTextField = new JTextField();
            valZatrataTextField.setText("USD");
            valZatrataTextField.setBounds(new Rectangle(600, 315, 42, 20));
            valZatrataTextField.setEditable(false);
            valZatrataTextField.setEnabled(false);
        }
        return valZatrataTextField;
    }

    /**
	 * This method initializes prilozhenieComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
    private JComboBox getPrilozhenieComboBox() {
        if (prilozhenieComboBox == null) {
            prilozhenieComboBox = new JComboBox();
            prilozhenieComboBox.setBounds(new Rectangle(100, 350, 256, 20));
            List<PrilozhenieBean> prilozhenija;
            List<Prilozhenie> unpacked = new ArrayList<Prilozhenie>();
            try {
                prilozhenija = factory.getPrilozhenieServiceSLSB().findAll();
                prilozhenija.add(0, new PrilozhenieBean(new Prilozhenie()));
                for (PrilozhenieBean bean : prilozhenija) {
                    unpacked.add(bean.getPrilozhenie());
                }
                Object[] prilozhenijaWithEmptyValue = unpacked.toArray();
                DefaultComboBoxModel model = new DefaultComboBoxModel(prilozhenijaWithEmptyValue);
                prilozhenieComboBox.setModel(model);
                GuiUtils.makeComboBoxAutoSearchible(prilozhenieComboBox, GuiUtils.getPrilozhenieCellRenderer());
            } catch (RemoteException e) {
                JOptionPane.showMessageDialog(null, "Can not get all the values, because of remote problems." + e.getMessage());
                e.printStackTrace();
            }
        }
        return prilozhenieComboBox;
    }

    /**
	 * This method initializes srokCheckBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
    private JCheckBox getSrokCheckBox() {
        if (srokCheckBox == null) {
            srokCheckBox = new JCheckBox();
            srokCheckBox.setBounds(new Rectangle(8, 380, 151, 20));
            srokCheckBox.setText(properties.getProperty("rep90Days"));
        }
        return srokCheckBox;
    }

    /**
	 * This method initializes priznakCheckBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
    private JCheckBox getPriznakCheckBox() {
        if (priznakCheckBox == null) {
            priznakCheckBox = new JCheckBox();
            priznakCheckBox.setBounds(new Rectangle(162, 381, 205, 20));
            priznakCheckBox.setText(properties.getProperty("notRecordByDay"));
        }
        return priznakCheckBox;
    }

    /**
	 * This is the default constructor
	 */
    public VipiskaInput(Properties properties) {
        super("", false, true, false, true);
        this.properties = properties;
        factory = BeansFactory.getInstance();
        initialize();
    }

    /**
	 * This method initializes this
	 * 
	 * @return void
	 */
    private void initialize() {
        this.setSize(660, 470);
        this.setContentPane(getJContentPane());
        this.setTitle(properties.getProperty("vipiskaInput"));
    }

    /**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            prilozhenieLabel = new JLabel();
            prilozhenieLabel.setBounds(new Rectangle(10, 350, 80, 16));
            prilozhenieLabel.setText(properties.getProperty("attachment"));
            valZatrataLabel = new JLabel();
            valZatrataLabel.setBounds(new Rectangle(542, 317, 50, 16));
            valZatrataLabel.setText(properties.getProperty("currency"));
            valDogovorLabel = new JLabel();
            valDogovorLabel.setBounds(new Rectangle(544, 290, 50, 16));
            valDogovorLabel.setText(properties.getProperty("currency"));
            zatrataSummaLabel = new JLabel();
            zatrataSummaLabel.setBounds(new Rectangle(367, 315, 50, 16));
            zatrataSummaLabel.setText(properties.getProperty("sum"));
            zatrataLabel = new JLabel();
            zatrataLabel.setBounds(new Rectangle(10, 320, 70, 16));
            zatrataLabel.setText(properties.getProperty("expent"));
            summaLabel = new JLabel();
            summaLabel.setBounds(new Rectangle(366, 290, 50, 16));
            summaLabel.setText(properties.getProperty("sum"));
            dogovorLabel = new JLabel();
            dogovorLabel.setBounds(new Rectangle(10, 290, 70, 16));
            dogovorLabel.setText(properties.getProperty("dogovor"));
            grnOutputLabel = new JLabel();
            grnOutputLabel.setBounds(new Rectangle(306, 260, 110, 16));
            grnOutputLabel.setText(properties.getProperty("grnOut"));
            valOutputLabel = new JLabel();
            valOutputLabel.setBounds(new Rectangle(304, 230, 110, 16));
            valOutputLabel.setText(properties.getProperty("valOut"));
            grnInputLabel = new JLabel();
            grnInputLabel.setBounds(new Rectangle(10, 260, 110, 16));
            grnInputLabel.setText(properties.getProperty("grnIn"));
            valInputLabel = new JLabel();
            valInputLabel.setBounds(new Rectangle(10, 230, 110, 16));
            valInputLabel.setText(properties.getProperty("valIn"));
            purposeLabel = new JLabel();
            purposeLabel.setBounds(new Rectangle(10, 130, 80, 16));
            purposeLabel.setText(properties.getProperty("purpose"));
            groupLabel = new JLabel();
            groupLabel.setBounds(new Rectangle(10, 100, 50, 16));
            groupLabel.setText(properties.getProperty("group"));
            orgLabel = new JLabel();
            orgLabel.setBounds(new Rectangle(10, 70, 80, 16));
            orgLabel.setText(properties.getProperty("organization"));
            valLabel = new JLabel();
            valLabel.setBounds(new Rectangle(531, 42, 50, 16));
            valLabel.setText(properties.getProperty("currency"));
            countLabel = new JLabel();
            countLabel.setBounds(new Rectangle(299, 40, 40, 16));
            countLabel.setText(properties.getProperty("count"));
            bankLabel = new JLabel();
            bankLabel.setBounds(new Rectangle(10, 40, 35, 16));
            bankLabel.setText(properties.getProperty("bank"));
            dateLabel = new JLabel();
            dateLabel.setBounds(new Rectangle(10, 10, 35, 16));
            dateLabel.setText(properties.getProperty("date"));
            jContentPane = new JPanel();
            jContentPane.setLayout(null);
            jContentPane.add(dateLabel, null);
            jContentPane.add(getDateFormattedTextField(), null);
            jContentPane.add(bankLabel, null);
            jContentPane.add(getValTextField(), null);
            jContentPane.add(getSaveButton(), null);
            jContentPane.add(getClearButton(), null);
            jContentPane.add(getRemoveButton(), null);
            jContentPane.add(getBankComboBox(), null);
            jContentPane.add(getCountComboBox(), null);
            jContentPane.add(countLabel, null);
            jContentPane.add(valLabel, null);
            jContentPane.add(orgLabel, null);
            jContentPane.add(getOrgComboBox(), null);
            jContentPane.add(groupLabel, null);
            jContentPane.add(getGroupComboBox(), null);
            jContentPane.add(purposeLabel, null);
            jContentPane.add(getPurposeTextPane(), null);
            jContentPane.add(valInputLabel, null);
            jContentPane.add(grnInputLabel, null);
            jContentPane.add(getValInputFormattedTextField(), null);
            jContentPane.add(getGrnInputFormattedTextField(), null);
            jContentPane.add(valOutputLabel, null);
            jContentPane.add(getValOutputFormattedTextField(), null);
            jContentPane.add(grnOutputLabel, null);
            jContentPane.add(getGrnOutputFormattedTextField(), null);
            jContentPane.add(dogovorLabel, null);
            jContentPane.add(getDogovorComboBox(), null);
            jContentPane.add(summaLabel, null);
            jContentPane.add(getSummaPoDogovoruFormattedTextField(), null);
            jContentPane.add(zatrataLabel, null);
            jContentPane.add(getZatrataComboBox(), null);
            jContentPane.add(zatrataSummaLabel, null);
            jContentPane.add(getZatrataSummaFormattedTextField(), null);
            jContentPane.add(getContractCurrencyTextField(), null);
            jContentPane.add(getValZatrataTextField(), null);
            jContentPane.add(valDogovorLabel, null);
            jContentPane.add(valZatrataLabel, null);
            jContentPane.add(prilozhenieLabel, null);
            jContentPane.add(getPrilozhenieComboBox(), null);
            jContentPane.add(getSrokCheckBox(), null);
            jContentPane.add(getPriznakCheckBox(), null);
        }
        return jContentPane;
    }

    @Override
    public void doDefaultCloseAction() {
        super.doDefaultCloseAction();
        clear();
    }
}
