package dialogPackage;

import java.sql.*;
import javax.swing.JTextField;
import mainPackage.*;
import dbConnPackage.*;

/**
 * Inserire qui la descrizione del tipo.
 * Data di creazione: (31/01/2002 16.25.04)
 * @author: PeP1
 */
public class EditAutorita extends javax.swing.JDialog {

    private javax.swing.JButton ivjAnnullaJButton = null;

    private javax.swing.JPanel ivjButtonsJPanel = null;

    private javax.swing.JPanel ivjCenterJPanel = null;

    private javax.swing.JPanel ivjJDialogContentPane = null;

    private javax.swing.JLabel ivjJLabel1 = null;

    private javax.swing.JPanel ivjLeftJPanel = null;

    private javax.swing.JButton ivjOkJButton = null;

    private javax.swing.JPanel ivjRightJPanel = null;

    private javax.swing.JPanel ivjTopJPanel = null;

    private Boolean ivjButtonFlag = null;

    private String ivjCodice = null;

    IvjEventHandler ivjEventHandler = new IvjEventHandler();

    protected transient dialogPackage.EditAutoritaListener fieldEditAutoritaListenerEventMulticaster = null;

    private javax.swing.JButton ivjTariffeJButton = null;

    private JTextField ivjTariffeBJTextField = null;

    private mainPackage.JTextFieldLimitato ivjDescrizioneBJTextField = null;

    private boolean corretto = true;

    private ScegliAutoritaPred ivjAutoritaPred = null;

    class IvjEventHandler implements dialogPackage.ScegliAutoritaPredListener, java.awt.event.ActionListener, java.beans.PropertyChangeListener {

        public void actionPerformed(java.awt.event.ActionEvent e) {
            if (e.getSource() == EditAutorita.this.getAnnullaJButton()) connEtoM1(e);
            if (e.getSource() == EditAutorita.this.getOkJButton()) connEtoC1(e);
            if (e.getSource() == EditAutorita.this.getOkJButton()) connEtoC2(e);
            if (e.getSource() == EditAutorita.this.getAnnullaJButton()) connEtoC4(e);
            if (e.getSource() == EditAutorita.this.getTariffeJButton()) connEtoM2(e);
        }

        ;

        public void propertyChange(java.beans.PropertyChangeEvent evt) {
            if (evt.getSource() == EditAutorita.this && (evt.getPropertyName().equals("background"))) connEtoC5(evt);
        }

        ;

        public void sceltaJButtonAction_actionPerformed(java.util.EventObject newEvent) {
            if (newEvent.getSource() == EditAutorita.this.getAutoritaPred()) connEtoM3(newEvent);
        }

        ;
    }

    ;

    /**
 * Commento del constructor EditAutorita.
 */
    public EditAutorita() {
        super();
        initialize();
    }

    /**
 * Commento del constructor EditAutorita.
 * @param owner java.awt.Dialog
 */
    public EditAutorita(java.awt.Dialog owner) {
        super(owner);
        initialize();
    }

    /**
 * Commento del constructor EditAutorita.
 * @param owner java.awt.Dialog
 * @param title java.lang.String
 */
    public EditAutorita(java.awt.Dialog owner, String title) {
        super(owner, title);
        initialize();
    }

    /**
 * Commento del constructor EditAutorita.
 * @param owner java.awt.Dialog
 * @param title java.lang.String
 * @param modal boolean
 */
    public EditAutorita(java.awt.Dialog owner, String title, boolean modal) {
        super(owner, title, modal);
        initialize();
    }

    /**
 * Commento del constructor EditAutorita.
 * @param owner java.awt.Dialog
 * @param modal boolean
 */
    public EditAutorita(java.awt.Dialog owner, boolean modal) {
        super(owner, modal);
        initialize();
    }

    /**
 * Commento del constructor EditAutorita.
 * @param owner java.awt.Frame
 */
    public EditAutorita(java.awt.Frame owner) {
        super(owner);
        initialize();
    }

    /**
 * Commento del constructor EditAutorita.
 * @param owner java.awt.Frame
 * @param title java.lang.String
 */
    public EditAutorita(java.awt.Frame owner, String title) {
        super(owner, title);
        initialize();
    }

    /**
 * Commento del constructor EditAutorita.
 * @param owner java.awt.Frame
 * @param title java.lang.String
 * @param modal boolean
 */
    public EditAutorita(java.awt.Frame owner, String title, boolean modal) {
        super(owner, title, modal);
        initialize();
    }

    /**
 * Commento del constructor EditAutorita.
 * @param owner java.awt.Frame
 * @param modal boolean
 */
    public EditAutorita(java.awt.Frame owner, boolean modal) {
        super(owner, modal);
        initialize();
    }

    /**
 * Inserire qui la descrizione del metodo.
 * Data di creazione: (26/04/2002 15.15.23)
 * @param x boolean
 */
    public void abilitaTariffe(boolean x) {
        getTariffeJButton().setEnabled(x);
    }

    /**
 *
 * @param newListener dialogPackage.EditAutoritaListener
 */
    public void addEditAutoritaListener(dialogPackage.EditAutoritaListener newListener) {
        fieldEditAutoritaListenerEventMulticaster = dialogPackage.EditAutoritaListenerEventMulticaster.add(fieldEditAutoritaListenerEventMulticaster, newListener);
        return;
    }

    /**
 * Inserire qui la descrizione del metodo.
 * Data di creazione: (16/01/2003 12.41.19)
 * @param codiceAutorita java.lang.String
 */
    public void caricaAutorita(String codiceAutorita, String descrAutorita) {
        try {
            getDescrizioneBJTextField().setText(descrAutorita);
            String tariffa = "";
            ResultSet rs = DBDavide.selAutoritaPred(codiceAutorita);
            if (rs.next()) {
                ResultSet rs1 = DBDavide.selAutorita(rs.getString(1));
                if (rs1.next()) {
                    tariffa = rs1.getString(1);
                }
                rs1.close();
            } else {
                tariffa = descrAutorita;
            }
            rs.close();
            getTariffeBJTextField().setText(tariffa);
        } catch (Exception e) {
            mainPackage.CostantiDavide.msgEccezione("Eccezione in caricaAutorita(String codiceAutorita, String descrAutorita) di EditAutorita");
            e.printStackTrace();
        }
    }

    /**
 * connEtoC1:  (OkJButton.action.actionPerformed(java.awt.event.ActionEvent) --> EditAutorita.okJButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
    private void connEtoC1(java.awt.event.ActionEvent arg1) {
        try {
            this.okJButton_ActionPerformed(arg1);
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    /**
 * connEtoC2:  (OkJButton.action.actionPerformed(java.awt.event.ActionEvent) --> EditAutorita.fireOkJButtonAction_actionPerformed(Ljava.util.EventObject;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
    private void connEtoC2(java.awt.event.ActionEvent arg1) {
        try {
            this.fireOkJButtonAction_actionPerformed(new java.util.EventObject(this));
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    /**
 * connEtoC4:  (AnnullaJButton.action.actionPerformed(java.awt.event.ActionEvent) --> EditAutorita.fireAnnullaJButtonAction_actionPerformed(Ljava.util.EventObject;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
    private void connEtoC4(java.awt.event.ActionEvent arg1) {
        try {
            this.fireAnnullaJButtonAction_actionPerformed(new java.util.EventObject(this));
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    /**
 * connEtoC5:  (EditAutorita.initialize() --> EditAutorita.editAutorita_Initialize()V)
 */
    private void connEtoC5() {
        try {
            this.editAutorita_Initialize();
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    /**
 * connEtoC5:  (EditAutorita.background --> EditAutorita.editAutorita_Initialize()V)
 * @param arg1 java.beans.PropertyChangeEvent
 */
    private void connEtoC5(java.beans.PropertyChangeEvent arg1) {
        try {
            this.editAutorita_Initialize();
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    /**
 * connEtoM1:  (AnnullaJButton.action.actionPerformed(java.awt.event.ActionEvent) --> EditAutorita.dispose()V)
 * @param arg1 java.awt.event.ActionEvent
 */
    private void connEtoM1(java.awt.event.ActionEvent arg1) {
        try {
            this.dispose();
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    /**
 * connEtoM2:  (TariffeJButton.action.actionPerformed(java.awt.event.ActionEvent) --> AutoritaPred.show()V)
 * @param arg1 java.awt.event.ActionEvent
 */
    private void connEtoM2(java.awt.event.ActionEvent arg1) {
        try {
            getAutoritaPred().show();
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    /**
 * connEtoM3:  (AutoritaPred.scegliAutoritaPred.sceltaJButtonAction_actionPerformed(java.util.EventObject) --> TariffeBJTextField.text)
 * @param arg1 java.util.EventObject
 */
    private void connEtoM3(java.util.EventObject arg1) {
        try {
            getTariffeBJTextField().setText(getAutoritaPred().getDescrizioneThis());
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    /**
 * Comment
 */
    public void editAutorita_Initialize() {
        javax.swing.JButton[] b_azione = { getOkJButton(), getAnnullaJButton() };
        mainPackage.CostantiDavide.inizBordiPulsanti(b_azione, mainPackage.CostantiDavide.TIPO_AZIONE);
        javax.swing.JButton[] b_selezione = { getTariffeJButton() };
        mainPackage.CostantiDavide.inizBordiPulsanti(b_selezione, mainPackage.CostantiDavide.TIPO_SELEZIONE);
        return;
    }

    /**
 * Comment
 */
    public void editAutorita_WindowOpened(java.awt.event.WindowEvent windowEvent) {
        return;
    }

    /**
 * Metodo per il supporto di eventi del listener.
 * @param newEvent java.util.EventObject
 */
    protected void fireAnnullaJButtonAction_actionPerformed(java.util.EventObject newEvent) {
        if (fieldEditAutoritaListenerEventMulticaster == null) {
            return;
        }
        ;
        fieldEditAutoritaListenerEventMulticaster.annullaJButtonAction_actionPerformed(newEvent);
    }

    /**
 * Metodo per il supporto di eventi del listener.
 * @param newEvent java.util.EventObject
 */
    protected void fireOkJButtonAction_actionPerformed(java.util.EventObject newEvent) {
        if (!corretto) {
            return;
        }
        if (fieldEditAutoritaListenerEventMulticaster == null) {
            return;
        }
        ;
        fieldEditAutoritaListenerEventMulticaster.okJButtonAction_actionPerformed(newEvent);
    }

    /**
 * Restituisce il valore della proprieta AnnullaJButton.
 * @return javax.swing.JButton
 */
    private javax.swing.JButton getAnnullaJButton() {
        if (ivjAnnullaJButton == null) {
            try {
                ivjAnnullaJButton = new javax.swing.JButton();
                ivjAnnullaJButton.setName("AnnullaJButton");
                ivjAnnullaJButton.setText(Messages.getString("Generale.Annulla"));
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjAnnullaJButton;
    }

    /**
 * Restituisce il valore della proprieta AutoritaPred.
 * @return dialogPackage.ScegliAutoritaPred
 */
    private ScegliAutoritaPred getAutoritaPred() {
        if (ivjAutoritaPred == null) {
            try {
                ivjAutoritaPred = new dialogPackage.ScegliAutoritaPred(this);
                ivjAutoritaPred.setName("AutoritaPred");
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjAutoritaPred;
    }

    /**
 * Restituisce il valore della proprieta ButtonFlag.
 * @return java.lang.Boolean
 */
    private java.lang.Boolean getButtonFlag() {
        return ivjButtonFlag;
    }

    /**
 * Metodo creato per supportare la promozione dell'attributo di buttonFlagThis.
 * @return java.lang.Boolean
 */
    public java.lang.Boolean getButtonFlagThis() {
        return getButtonFlag();
    }

    /**
 * Restituisce il valore della proprieta ButtonsJPanel.
 * @return javax.swing.JPanel
 */
    private javax.swing.JPanel getButtonsJPanel() {
        if (ivjButtonsJPanel == null) {
            try {
                ivjButtonsJPanel = new javax.swing.JPanel();
                ivjButtonsJPanel.setName("ButtonsJPanel");
                ivjButtonsJPanel.setLayout(new java.awt.FlowLayout());
                getButtonsJPanel().add(getOkJButton(), getOkJButton().getName());
                this.getRootPane().setDefaultButton(getOkJButton());
                ivjButtonsJPanel.add(getAnnullaJButton());
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjButtonsJPanel;
    }

    /**
 * Restituisce il valore della proprieta CenterJPanel.
 * @return javax.swing.JPanel
 */
    private javax.swing.JPanel getCenterJPanel() {
        if (ivjCenterJPanel == null) {
            try {
                ivjCenterJPanel = new javax.swing.JPanel();
                ivjCenterJPanel.setName("CenterJPanel");
                ivjCenterJPanel.setLayout(new java.awt.GridBagLayout());
                java.awt.GridBagConstraints constraintsJLabel1 = new java.awt.GridBagConstraints();
                constraintsJLabel1.gridx = 0;
                constraintsJLabel1.gridy = 0;
                constraintsJLabel1.insets = new java.awt.Insets(4, 4, 4, 4);
                getCenterJPanel().add(getJLabel1(), constraintsJLabel1);
                java.awt.GridBagConstraints constraintsDescrizioneBJTextField = new java.awt.GridBagConstraints();
                constraintsDescrizioneBJTextField.gridx = 1;
                constraintsDescrizioneBJTextField.gridy = 0;
                constraintsDescrizioneBJTextField.fill = java.awt.GridBagConstraints.HORIZONTAL;
                constraintsDescrizioneBJTextField.weightx = 1.0;
                constraintsDescrizioneBJTextField.insets = new java.awt.Insets(4, 4, 4, 4);
                getCenterJPanel().add(getDescrizioneBJTextField(), constraintsDescrizioneBJTextField);
                java.awt.GridBagConstraints constraintsTariffeJButton = new java.awt.GridBagConstraints();
                constraintsTariffeJButton.gridx = 0;
                constraintsTariffeJButton.gridy = 1;
                constraintsTariffeJButton.insets = new java.awt.Insets(4, 4, 4, 4);
                getCenterJPanel().add(getTariffeJButton(), constraintsTariffeJButton);
                java.awt.GridBagConstraints constraintsTariffeBJTextField = new java.awt.GridBagConstraints();
                constraintsTariffeBJTextField.gridx = 1;
                constraintsTariffeBJTextField.gridy = 1;
                constraintsTariffeBJTextField.fill = java.awt.GridBagConstraints.HORIZONTAL;
                constraintsTariffeBJTextField.weightx = 1.0;
                constraintsTariffeBJTextField.insets = new java.awt.Insets(4, 4, 4, 4);
                getCenterJPanel().add(getTariffeBJTextField(), constraintsTariffeBJTextField);
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjCenterJPanel;
    }

    /**
 * Restituisce il valore della proprieta Codice.
 * @return java.lang.String
 */
    private java.lang.String getCodice() {
        return ivjCodice;
    }

    /**
 * Metodo creato per supportare la promozione dell'attributo di codiceThis.
 * @return java.lang.String
 */
    public java.lang.String getCodiceThis() {
        return getCodice();
    }

    /**
 * Restituisce il valore della proprieta DescrizioneBJTextField.
 * @return mainPackage.JTextFieldLimitato
 */
    private mainPackage.JTextFieldLimitato getDescrizioneBJTextField() {
        if (ivjDescrizioneBJTextField == null) {
            try {
                ivjDescrizioneBJTextField = new mainPackage.JTextFieldLimitato(50);
                ivjDescrizioneBJTextField.setName("DescrizioneBJTextField");
                ivjDescrizioneBJTextField.setText("");
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjDescrizioneBJTextField;
    }

    /**
 * Restituisce il valore della proprieta JDialogContentPane.
 * @return javax.swing.JPanel
 */
    private javax.swing.JPanel getJDialogContentPane() {
        if (ivjJDialogContentPane == null) {
            try {
                ivjJDialogContentPane = new javax.swing.JPanel();
                ivjJDialogContentPane.setName("JDialogContentPane");
                ivjJDialogContentPane.setLayout(new java.awt.BorderLayout());
                getJDialogContentPane().add(getButtonsJPanel(), "South");
                getJDialogContentPane().add(getLeftJPanel(), "West");
                getJDialogContentPane().add(getRightJPanel(), "East");
                getJDialogContentPane().add(getTopJPanel(), "North");
                getJDialogContentPane().add(getCenterJPanel(), "Center");
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjJDialogContentPane;
    }

    /**
 * Restituisce il valore della proprieta JLabel1.
 * @return javax.swing.JLabel
 */
    private javax.swing.JLabel getJLabel1() {
        if (ivjJLabel1 == null) {
            try {
                ivjJLabel1 = new javax.swing.JLabel();
                ivjJLabel1.setName("JLabel1");
                ivjJLabel1.setText(Messages.getString("EditAutorita.Descrizione_18"));
                ivjJLabel1.setForeground(java.awt.Color.black);
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjJLabel1;
    }

    /**
 * Restituisce il valore della proprieta LeftJPanel.
 * @return javax.swing.JPanel
 */
    private javax.swing.JPanel getLeftJPanel() {
        if (ivjLeftJPanel == null) {
            try {
                ivjLeftJPanel = new javax.swing.JPanel();
                ivjLeftJPanel.setName("LeftJPanel");
                ivjLeftJPanel.setLayout(new java.awt.FlowLayout());
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjLeftJPanel;
    }

    /**
 * Restituisce il valore della proprieta OkJButton.
 * @return javax.swing.JButton
 */
    private javax.swing.JButton getOkJButton() {
        if (ivjOkJButton == null) {
            try {
                ivjOkJButton = new javax.swing.JButton();
                ivjOkJButton.setName("OkJButton");
                ivjOkJButton.setText(Messages.getString("EditAutorita.Ok_21"));
                ivjOkJButton.setActionCommand("OkJButton");
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjOkJButton;
    }

    /**
 * Restituisce il valore della proprieta RightJPanel.
 * @return javax.swing.JPanel
 */
    private javax.swing.JPanel getRightJPanel() {
        if (ivjRightJPanel == null) {
            try {
                ivjRightJPanel = new javax.swing.JPanel();
                ivjRightJPanel.setName("RightJPanel");
                ivjRightJPanel.setLayout(new java.awt.FlowLayout());
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjRightJPanel;
    }

    /**
 * Restituisce il valore della proprieta BoundJTextField1.
 * @return mainPackage.JTextFieldLimitato
 */
    private JTextField getTariffeBJTextField() {
        if (ivjTariffeBJTextField == null) {
            try {
                ivjTariffeBJTextField = new JTextField();
                ivjTariffeBJTextField.setName("TariffeBJTextField");
                ivjTariffeBJTextField.setEditable(false);
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjTariffeBJTextField;
    }

    /**
 * Restituisce il valore della proprieta TariffeJButton.
 * @return javax.swing.JButton
 */
    private javax.swing.JButton getTariffeJButton() {
        if (ivjTariffeJButton == null) {
            try {
                ivjTariffeJButton = new javax.swing.JButton();
                ivjTariffeJButton.setName("TariffeJButton");
                ivjTariffeJButton.setText(Messages.getString("EditAutorita.Tariffe_26"));
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjTariffeJButton;
    }

    /**
 * Restituisce il valore della proprieta TopJPanel.
 * @return javax.swing.JPanel
 */
    private javax.swing.JPanel getTopJPanel() {
        if (ivjTopJPanel == null) {
            try {
                ivjTopJPanel = new javax.swing.JPanel();
                ivjTopJPanel.setName("TopJPanel");
                ivjTopJPanel.setLayout(new java.awt.FlowLayout());
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjTopJPanel;
    }

    /**
 * Richiamato ogni volta che la parte lancia un'eccezione.
 * @param exception java.lang.Throwable
 */
    private void handleException(java.lang.Throwable exception) {
    }

    /**
 * Inizializza collegamenti
 * @exception java.lang.Exception La descrizione dell'eccezione.
 */
    private void initConnections() throws java.lang.Exception {
        getAnnullaJButton().addActionListener(ivjEventHandler);
        getOkJButton().addActionListener(ivjEventHandler);
        getTariffeJButton().addActionListener(ivjEventHandler);
        getAutoritaPred().addScegliAutoritaPredListener(ivjEventHandler);
        this.addPropertyChangeListener(ivjEventHandler);
    }

    /**
 * Inizializzare la classe.
 */
    private void initialize() {
        try {
            setName("EditAutorita");
            setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
            setSize(536, 162);
            setModal(true);
            setTitle(Messages.getString("EditAutorita.Autorit_u00E0_giudicanti_29"));
            setContentPane(getJDialogContentPane());
            initConnections();
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    /**
 * Punto di immissione main - avvia la parte quando questa viene eseguita come applicazione
 * @param args java.lang.String[]
 */
    public static void main(java.lang.String[] args) {
        try {
            EditAutorita aEditAutorita;
            aEditAutorita = new EditAutorita();
            aEditAutorita.setModal(true);
            aEditAutorita.addWindowListener(new java.awt.event.WindowAdapter() {

                public void windowClosing(java.awt.event.WindowEvent e) {
                    System.exit(0);
                }

                ;
            });
            aEditAutorita.show();
            java.awt.Insets insets = aEditAutorita.getInsets();
            aEditAutorita.setSize(aEditAutorita.getWidth() + insets.left + insets.right, aEditAutorita.getHeight() + insets.top + insets.bottom);
            aEditAutorita.setVisible(true);
        } catch (Throwable exception) {
            System.err.println("Eccezione verificatasi in main() di javax.swing.JDialog");
            exception.printStackTrace(System.out);
        }
    }

    /**
 * Comment
 */
    public void okJButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
        try {
            if (getDescrizioneBJTextField().getText().equals("")) {
                javax.swing.JOptionPane.showMessageDialog(this, Messages.getString("EditAutorita.Descrizione_mancante_!_32"), Messages.getString("EditAutorita.Errore_33"), javax.swing.JOptionPane.ERROR_MESSAGE);
                corretto = false;
                return;
            }
            if (getTariffeBJTextField().getText().equals("")) {
                javax.swing.JOptionPane.showMessageDialog(this, Messages.getString("EditAutorita.Selezionare_la_tariffa_associata_!_35"), Messages.getString("EditAutorita.Errore_36"), javax.swing.JOptionPane.ERROR_MESSAGE);
                corretto = false;
                return;
            }
            int ris;
            if (getButtonFlag().booleanValue()) {
                String descrizione = getDescrizioneBJTextField().getText();
                String tariffa = getTariffeBJTextField().getText();
                ris = DBDavide.insAutorita(descrizione, tariffa);
            } else {
                String descrizione = getDescrizioneBJTextField().getText();
                String codice = getCodice();
                ris = DBDavide.updAutorita(codice, descrizione);
            }
            dispose();
            corretto = true;
        } catch (Exception e) {
            mainPackage.CostantiDavide.msgEccezione("Eccezione in okJButton di EditAutorita");
            e.printStackTrace();
        }
        return;
    }

    /**
 * Inserire qui la descrizione del metodo.
 * Data di creazione: (16/01/2003 12.37.04)
 */
    public void pulisciTutto() {
        try {
            String v = "";
            setCodice(v);
            getDescrizioneBJTextField().setText(v);
            getTariffeBJTextField().setText(v);
        } catch (Exception e) {
            mainPackage.CostantiDavide.msgEccezione("Eccezione in pulisciTutto() di EditAutorita");
            e.printStackTrace();
        }
    }

    /**
 *
 * @param newListener dialogPackage.EditAutoritaListener
 */
    public void removeEditAutoritaListener(dialogPackage.EditAutoritaListener newListener) {
        fieldEditAutoritaListenerEventMulticaster = dialogPackage.EditAutoritaListenerEventMulticaster.remove(fieldEditAutoritaListenerEventMulticaster, newListener);
        return;
    }

    /**
 * Impostare ButtonFlag su un nuovo valore.
 * @param newValue java.lang.Boolean
 */
    private void setButtonFlag(java.lang.Boolean newValue) {
        if (ivjButtonFlag != newValue) {
            try {
                Boolean oldValue = getButtonFlag();
                ivjButtonFlag = newValue;
                firePropertyChange("buttonFlagThis", oldValue, newValue);
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        ;
    }

    /**
 * Metodo creato per supportare la promozione dell'attributo di buttonFlagThis.
 * @param arg1 java.lang.Boolean
 */
    public void setButtonFlagThis(java.lang.Boolean arg1) {
        setButtonFlag(arg1);
    }

    /**
 * Impostare Codice su un nuovo valore.
 * @param newValue java.lang.String
 */
    private void setCodice(java.lang.String newValue) {
        if (ivjCodice != newValue) {
            try {
                String oldValue = getCodice();
                ivjCodice = newValue;
                firePropertyChange("codiceThis", oldValue, newValue);
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        ;
    }

    /**
 * Metodo creato per supportare la promozione dell'attributo di codiceThis.
 * @param arg1 java.lang.String
 */
    public void setCodiceThis(java.lang.String arg1) {
        setCodice(arg1);
    }
}
