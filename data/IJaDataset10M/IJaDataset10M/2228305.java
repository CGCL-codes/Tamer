package dialogPackage;

import java.math.BigDecimal;
import javax.swing.JTextField;
import mainPackage.*;
import mainPackage.InputNumero;

/**
 * Inserire qui la descrizione del tipo.
 * Data di creazione: (15/10/2002 10.47.10)
 * @author: PeP1
 */
public class EditScadDer extends MiaJDialog {

    private javax.swing.JPanel ivjJDialogContentPane = null;

    private javax.swing.JRadioButton ivjAggiornamentoJRadioButton = null;

    private javax.swing.JButton ivjAnnullaJButton = null;

    private javax.swing.JPanel ivjButtonsJPanel = null;

    private javax.swing.JButton ivjCatDerJButton = null;

    private javax.swing.JTextField ivjCatDerJTextField = null;

    private javax.swing.JButton ivjCatOrigJButton = null;

    private javax.swing.JTextField ivjCatOrigJTextField = null;

    private javax.swing.JPanel ivjCenterJPanel = null;

    private com.ibm.easyswing.BoundButtonGroup ivjConsegueBBG = null;

    private javax.swing.JPanel ivjDistanzaJPanel = null;

    private InputNumero ivjDistanzaText = null;

    private javax.swing.JRadioButton ivjEvasioneJRadioButton = null;

    private javax.swing.JRadioButton ivjInserimentoJRadioButton = null;

    private javax.swing.JLabel ivjJLabel1 = null;

    private javax.swing.JButton ivjOkJButton = null;

    private Boolean ivjButtonFlag = null;

    IvjEventHandler ivjEventHandler = new IvjEventHandler();

    private ScegliCatImp ivjVediCatImpOrig = null;

    private ScegliCatImp ivjVediCatImpDer = null;

    private String ivjCodScadDer = null;

    private String ivjCodCatImpDer = null;

    private String ivjCodCatImpOrig = null;

    protected transient dialogPackage.EditScadDerListener fieldEditScadDerListenerEventMulticaster = null;

    private boolean corretto = false;

    class IvjEventHandler implements dialogPackage.ScegliCatImpListener, java.awt.event.ActionListener {

        public void actionPerformed(java.awt.event.ActionEvent e) {
            if (e.getSource() == EditScadDer.this.getCatOrigJButton()) connEtoC2(e);
            if (e.getSource() == EditScadDer.this.getCatDerJButton()) connEtoC3(e);
            if (e.getSource() == EditScadDer.this.getAnnullaJButton()) connEtoM1(e);
            if (e.getSource() == EditScadDer.this.getOkJButton()) connEtoC6(e);
            if (e.getSource() == EditScadDer.this.getOkJButton()) connEtoC7(e);
            if (e.getSource() == EditScadDer.this.getAnnullaJButton()) connEtoC8(e);
        }

        ;

        public void chiudiJButtonAction_actionPerformed(java.util.EventObject newEvent) {
        }

        ;

        public void sceltaJButtonAction_actionPerformed(java.util.EventObject newEvent) {
            if (newEvent.getSource() == EditScadDer.this.getVediCatImpOrig()) connEtoC4(newEvent);
            if (newEvent.getSource() == EditScadDer.this.getVediCatImpDer()) connEtoC5(newEvent);
        }

        ;
    }

    ;

    /**
 * Commento del constructor EditScadDer.
 */
    public EditScadDer() {
        super();
        initialize();
    }

    /**
 * Commento del constructor EditScadDer.
 * @param owner java.awt.Dialog
 */
    public EditScadDer(java.awt.Dialog owner) {
        super(owner);
        initialize();
    }

    /**
 * Commento del constructor EditScadDer.
 * @param owner java.awt.Dialog
 * @param title java.lang.String
 */
    public EditScadDer(java.awt.Dialog owner, String title) {
        super(owner, title);
        initialize();
    }

    /**
 * Commento del constructor EditScadDer.
 * @param owner java.awt.Dialog
 * @param title java.lang.String
 * @param modal boolean
 */
    public EditScadDer(java.awt.Dialog owner, String title, boolean modal) {
        super(owner, title, modal);
        initialize();
    }

    /**
 * Commento del constructor EditScadDer.
 * @param owner java.awt.Dialog
 * @param modal boolean
 */
    public EditScadDer(java.awt.Dialog owner, boolean modal) {
        super(owner, modal);
        initialize();
    }

    /**
 * Commento del constructor EditScadDer.
 * @param owner java.awt.Frame
 */
    public EditScadDer(java.awt.Frame owner) {
        super(owner);
        initialize();
    }

    /**
 * Commento del constructor EditScadDer.
 * @param owner java.awt.Frame
 * @param title java.lang.String
 */
    public EditScadDer(java.awt.Frame owner, String title) {
        super(owner, title);
        initialize();
    }

    /**
 * Commento del constructor EditScadDer.
 * @param owner java.awt.Frame
 * @param title java.lang.String
 * @param modal boolean
 */
    public EditScadDer(java.awt.Frame owner, String title, boolean modal) {
        super(owner, title, modal);
        initialize();
    }

    /**
 * Commento del constructor EditScadDer.
 * @param owner java.awt.Frame
 * @param modal boolean
 */
    public EditScadDer(java.awt.Frame owner, boolean modal) {
        super(owner, modal);
        initialize();
    }

    /**
 *
 * @param newListener dialogPackage.EditScadDerListener
 */
    public void addEditScadDerListener(dialogPackage.EditScadDerListener newListener) {
        fieldEditScadDerListenerEventMulticaster = dialogPackage.EditScadDerListenerEventMulticaster.add(fieldEditScadDerListenerEventMulticaster, newListener);
        return;
    }

    /**
 * Inserire qui la descrizione del metodo.
 * Data di creazione: (16/10/2002 10.40.46)
 * @param codScadDer java.lang.String
 */
    public void caricaScadDer(String codScadDer) {
        try {
            pulisciTutto();
            setCodScadDer(codScadDer);
            java.sql.ResultSet rs = dbConnPackage.DBDavide.selScadDerivata(codScadDer);
            while (rs.next()) {
                setCodCatImpOrig(rs.getString(1));
                getCatOrigJTextField().setText(rs.getString(2));
                setCodCatImpDer(rs.getString(3));
                getCatDerJTextField().setText(rs.getString(4));
                getConsegueBBG().setSelectedButtonIndex(rs.getInt(5));
                getDistanzaText().setBigDecimal(new BigDecimal(rs.getInt(6)));
            }
            rs.close();
        } catch (Exception e) {
            mainPackage.CostantiDavide.msgEccezione("Eccezione in caricaScadDer(codScadDer) di EditScadDer");
            e.printStackTrace();
        }
    }

    /**
 * Comment
 */
    public void catDerJButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
        getVediCatImpDer().show();
        return;
    }

    /**
 * Comment
 */
    public void catOrigJButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
        getVediCatImpOrig().show();
        return;
    }

    /**
 * connEtoC1:  (EditScadDer.initialize() --> EditScadDer.editScadDer_Initialize()V)
 */
    private void connEtoC1() {
        try {
            this.editScadDer_Initialize();
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    /**
 * connEtoC2:  (CatOrigJButton.action.actionPerformed(java.awt.event.ActionEvent) --> EditScadDer.catOrigJButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
    private void connEtoC2(java.awt.event.ActionEvent arg1) {
        try {
            this.catOrigJButton_ActionPerformed(arg1);
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    /**
 * connEtoC3:  (CatDerJButton.action.actionPerformed(java.awt.event.ActionEvent) --> EditScadDer.catDerJButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
    private void connEtoC3(java.awt.event.ActionEvent arg1) {
        try {
            this.catDerJButton_ActionPerformed(arg1);
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    /**
 * connEtoC4:  (VediCatImpOrig.scegliCatImp.sceltaJButtonAction_actionPerformed(java.util.EventObject) --> EditScadDer.vediCatImpOrig_SceltaJButtonAction(Ljava.util.EventObject;)V)
 * @param arg1 java.util.EventObject
 */
    private void connEtoC4(java.util.EventObject arg1) {
        try {
            this.vediCatImpOrig_SceltaJButtonAction(arg1);
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    /**
 * connEtoC5:  (VediCatImpDer.scegliCatImp.sceltaJButtonAction_actionPerformed(java.util.EventObject) --> EditScadDer.vediCatImpDer_SceltaJButtonAction(Ljava.util.EventObject;)V)
 * @param arg1 java.util.EventObject
 */
    private void connEtoC5(java.util.EventObject arg1) {
        try {
            this.vediCatImpDer_SceltaJButtonAction(arg1);
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    /**
 * connEtoC6:  (OkJButton.action.actionPerformed(java.awt.event.ActionEvent) --> EditScadDer.okJButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
    private void connEtoC6(java.awt.event.ActionEvent arg1) {
        try {
            this.okJButton_ActionPerformed(arg1);
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    /**
 * connEtoC7:  (OkJButton.action.actionPerformed(java.awt.event.ActionEvent) --> EditScadDer.fireOkJButtonAction_actionPerformed(Ljava.util.EventObject;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
    private void connEtoC7(java.awt.event.ActionEvent arg1) {
        try {
            this.fireOkJButtonAction_actionPerformed(new java.util.EventObject(this));
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    /**
 * connEtoC8:  (AnnullaJButton.action.actionPerformed(java.awt.event.ActionEvent) --> EditScadDer.fireAnnullaJButtonAction_actionPerformed(Ljava.util.EventObject;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
    private void connEtoC8(java.awt.event.ActionEvent arg1) {
        try {
            this.fireAnnullaJButtonAction_actionPerformed(new java.util.EventObject(this));
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    /**
 * connEtoM1:  (AnnullaJButton.action.actionPerformed(java.awt.event.ActionEvent) --> EditScadDer.dispose()V)
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
 * Comment
 */
    public void editScadDer_Initialize() {
        javax.swing.JButton[] b_azione = { getOkJButton(), getAnnullaJButton() };
        mainPackage.CostantiDavide.inizBordiPulsanti(b_azione, mainPackage.CostantiDavide.TIPO_AZIONE);
        javax.swing.JButton[] b_selezione = { getCatOrigJButton(), getCatDerJButton() };
        mainPackage.CostantiDavide.inizBordiPulsanti(b_selezione, mainPackage.CostantiDavide.TIPO_SELEZIONE);
        getConsegueBBG().setBorder(new javax.swing.border.TitledBorder(Messages.getString("EditScadDer.Consegue_2")));
        return;
    }

    /**
 * Metodo per il supporto di eventi del listener.
 * @param newEvent java.util.EventObject
 */
    protected void fireAnnullaJButtonAction_actionPerformed(java.util.EventObject newEvent) {
        if (fieldEditScadDerListenerEventMulticaster == null) {
            return;
        }
        ;
        fieldEditScadDerListenerEventMulticaster.annullaJButtonAction_actionPerformed(newEvent);
    }

    /**
 * Metodo per il supporto di eventi del listener.
 * @param newEvent java.util.EventObject
 */
    protected void fireOkJButtonAction_actionPerformed(java.util.EventObject newEvent) {
        if (!corretto) {
            return;
        }
        if (fieldEditScadDerListenerEventMulticaster == null) {
            return;
        }
        ;
        fieldEditScadDerListenerEventMulticaster.okJButtonAction_actionPerformed(newEvent);
    }

    /**
 * Restituisce il valore della proprieta AggiornamentoJRadioButton.
 * @return javax.swing.JRadioButton
 */
    private javax.swing.JRadioButton getAggiornamentoJRadioButton() {
        if (ivjAggiornamentoJRadioButton == null) {
            try {
                ivjAggiornamentoJRadioButton = new javax.swing.JRadioButton();
                ivjAggiornamentoJRadioButton.setName("AggiornamentoJRadioButton");
                ivjAggiornamentoJRadioButton.setText(Messages.getString("EditScadDer.All__aggiornamento_dell__impegno_origine_4"));
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjAggiornamentoJRadioButton;
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
                getButtonsJPanel().add(getAnnullaJButton(), getAnnullaJButton().getName());
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjButtonsJPanel;
    }

    /**
 * Restituisce il valore della proprieta CatDerJButton.
 * @return javax.swing.JButton
 */
    private javax.swing.JButton getCatDerJButton() {
        if (ivjCatDerJButton == null) {
            try {
                ivjCatDerJButton = new javax.swing.JButton();
                ivjCatDerJButton.setName("CatDerJButton");
                ivjCatDerJButton.setText(Messages.getString("EditScadDer.Cat._Derivata_9"));
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjCatDerJButton;
    }

    /**
 * Restituisce il valore della proprieta CatDerJTextField.
 * @return javax.swing.JTextField
 */
    private javax.swing.JTextField getCatDerJTextField() {
        if (ivjCatDerJTextField == null) {
            try {
                ivjCatDerJTextField = new JTextField();
                ivjCatDerJTextField.setName("CatDerJTextField");
                ivjCatDerJTextField.setEditable(false);
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjCatDerJTextField;
    }

    /**
 * Restituisce il valore della proprieta CatOrigJButton.
 * @return javax.swing.JButton
 */
    private javax.swing.JButton getCatOrigJButton() {
        if (ivjCatOrigJButton == null) {
            try {
                ivjCatOrigJButton = new javax.swing.JButton();
                ivjCatOrigJButton.setName("CatOrigJButton");
                ivjCatOrigJButton.setText(Messages.getString("EditScadDer.Cat._Origine_12"));
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjCatOrigJButton;
    }

    /**
 * Restituisce il valore della proprieta CatOrigJTextField.
 * @return javax.swing.JTextField
 */
    private javax.swing.JTextField getCatOrigJTextField() {
        if (ivjCatOrigJTextField == null) {
            try {
                ivjCatOrigJTextField = new JTextField();
                ivjCatOrigJTextField.setName("CatOrigJTextField");
                ivjCatOrigJTextField.setEditable(false);
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjCatOrigJTextField;
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
                java.awt.GridBagConstraints constraintsCatOrigJButton = new java.awt.GridBagConstraints();
                constraintsCatOrigJButton.gridx = 0;
                constraintsCatOrigJButton.gridy = 0;
                constraintsCatOrigJButton.fill = java.awt.GridBagConstraints.HORIZONTAL;
                constraintsCatOrigJButton.insets = new java.awt.Insets(4, 4, 4, 4);
                getCenterJPanel().add(getCatOrigJButton(), constraintsCatOrigJButton);
                java.awt.GridBagConstraints constraintsCatDerJButton = new java.awt.GridBagConstraints();
                constraintsCatDerJButton.gridx = 0;
                constraintsCatDerJButton.gridy = 1;
                constraintsCatDerJButton.fill = java.awt.GridBagConstraints.HORIZONTAL;
                constraintsCatDerJButton.insets = new java.awt.Insets(4, 4, 4, 4);
                getCenterJPanel().add(getCatDerJButton(), constraintsCatDerJButton);
                java.awt.GridBagConstraints constraintsCatOrigJTextField = new java.awt.GridBagConstraints();
                constraintsCatOrigJTextField.gridx = 1;
                constraintsCatOrigJTextField.gridy = 0;
                constraintsCatOrigJTextField.gridwidth = 2;
                constraintsCatOrigJTextField.fill = java.awt.GridBagConstraints.HORIZONTAL;
                constraintsCatOrigJTextField.weightx = 1.0;
                constraintsCatOrigJTextField.insets = new java.awt.Insets(4, 4, 4, 4);
                getCenterJPanel().add(getCatOrigJTextField(), constraintsCatOrigJTextField);
                java.awt.GridBagConstraints constraintsCatDerJTextField = new java.awt.GridBagConstraints();
                constraintsCatDerJTextField.gridx = 1;
                constraintsCatDerJTextField.gridy = 1;
                constraintsCatDerJTextField.gridwidth = 2;
                constraintsCatDerJTextField.fill = java.awt.GridBagConstraints.HORIZONTAL;
                constraintsCatDerJTextField.weightx = 1.0;
                constraintsCatDerJTextField.insets = new java.awt.Insets(4, 4, 4, 4);
                getCenterJPanel().add(getCatDerJTextField(), constraintsCatDerJTextField);
                java.awt.GridBagConstraints constraintsConsegueBBG = new java.awt.GridBagConstraints();
                constraintsConsegueBBG.gridx = 0;
                constraintsConsegueBBG.gridy = 2;
                constraintsConsegueBBG.gridwidth = 2;
                constraintsConsegueBBG.fill = java.awt.GridBagConstraints.BOTH;
                constraintsConsegueBBG.weightx = 1.0;
                constraintsConsegueBBG.weighty = 1.0;
                constraintsConsegueBBG.insets = new java.awt.Insets(4, 4, 4, 4);
                getCenterJPanel().add(getConsegueBBG(), constraintsConsegueBBG);
                java.awt.GridBagConstraints constraintsDistanzaJPanel = new java.awt.GridBagConstraints();
                constraintsDistanzaJPanel.gridx = 2;
                constraintsDistanzaJPanel.gridy = 2;
                constraintsDistanzaJPanel.fill = java.awt.GridBagConstraints.BOTH;
                constraintsDistanzaJPanel.weightx = 1.0;
                constraintsDistanzaJPanel.weighty = 1.0;
                constraintsDistanzaJPanel.insets = new java.awt.Insets(4, 4, 4, 4);
                getCenterJPanel().add(getDistanzaJPanel(), constraintsDistanzaJPanel);
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjCenterJPanel;
    }

    /**
 * Restituisce il valore della proprieta CodCatImpDer.
 * @return java.lang.String
 */
    private java.lang.String getCodCatImpDer() {
        return ivjCodCatImpDer;
    }

    /**
 * Restituisce il valore della proprieta CodCatImpOrig.
 * @return java.lang.String
 */
    private java.lang.String getCodCatImpOrig() {
        return ivjCodCatImpOrig;
    }

    /**
 * Restituisce il valore della proprieta CodScadDer.
 * @return java.lang.String
 */
    private java.lang.String getCodScadDer() {
        return ivjCodScadDer;
    }

    /**
 * Metodo creato per supportare la promozione dell'attributo di codScadDerThis.
 * @return java.lang.String
 */
    public java.lang.String getCodScadDerThis() {
        return getCodScadDer();
    }

    /**
 * Restituisce il valore della proprieta ConsegueBBG.
 * @return com.ibm.easyswing.BoundButtonGroup
 */
    private com.ibm.easyswing.BoundButtonGroup getConsegueBBG() {
        if (ivjConsegueBBG == null) {
            try {
                ivjConsegueBBG = new com.ibm.easyswing.BoundButtonGroup();
                ivjConsegueBBG.setName("ConsegueBBG");
                ivjConsegueBBG.setLayout(new java.awt.GridBagLayout());
                java.awt.GridBagConstraints constraintsInserimentoJRadioButton = new java.awt.GridBagConstraints();
                constraintsInserimentoJRadioButton.gridx = 0;
                constraintsInserimentoJRadioButton.gridy = 0;
                constraintsInserimentoJRadioButton.fill = java.awt.GridBagConstraints.HORIZONTAL;
                constraintsInserimentoJRadioButton.insets = new java.awt.Insets(4, 4, 4, 4);
                getConsegueBBG().add(getInserimentoJRadioButton(), constraintsInserimentoJRadioButton);
                java.awt.GridBagConstraints constraintsEvasioneJRadioButton = new java.awt.GridBagConstraints();
                constraintsEvasioneJRadioButton.gridx = 0;
                constraintsEvasioneJRadioButton.gridy = 1;
                constraintsEvasioneJRadioButton.fill = java.awt.GridBagConstraints.HORIZONTAL;
                constraintsEvasioneJRadioButton.insets = new java.awt.Insets(4, 4, 4, 4);
                getConsegueBBG().add(getEvasioneJRadioButton(), constraintsEvasioneJRadioButton);
                java.awt.GridBagConstraints constraintsAggiornamentoJRadioButton = new java.awt.GridBagConstraints();
                constraintsAggiornamentoJRadioButton.gridx = 0;
                constraintsAggiornamentoJRadioButton.gridy = 2;
                constraintsAggiornamentoJRadioButton.fill = java.awt.GridBagConstraints.HORIZONTAL;
                constraintsAggiornamentoJRadioButton.insets = new java.awt.Insets(4, 4, 4, 4);
                getConsegueBBG().add(getAggiornamentoJRadioButton(), constraintsAggiornamentoJRadioButton);
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjConsegueBBG;
    }

    /**
 * Restituisce il valore della proprieta DistanzaJPanel.
 * @return javax.swing.JPanel
 */
    private javax.swing.JPanel getDistanzaJPanel() {
        if (ivjDistanzaJPanel == null) {
            try {
                ivjDistanzaJPanel = new javax.swing.JPanel();
                ivjDistanzaJPanel.setName("DistanzaJPanel");
                ivjDistanzaJPanel.setLayout(new java.awt.GridBagLayout());
                java.awt.GridBagConstraints constraintsJLabel1 = new java.awt.GridBagConstraints();
                constraintsJLabel1.gridx = 0;
                constraintsJLabel1.gridy = 0;
                constraintsJLabel1.insets = new java.awt.Insets(4, 4, 4, 4);
                getDistanzaJPanel().add(getJLabel1(), constraintsJLabel1);
                java.awt.GridBagConstraints constraintsDistanzaText = new java.awt.GridBagConstraints();
                constraintsDistanzaText.gridx = 0;
                constraintsDistanzaText.gridy = 1;
                constraintsDistanzaText.fill = java.awt.GridBagConstraints.HORIZONTAL;
                constraintsDistanzaText.weightx = 1.0;
                constraintsDistanzaText.insets = new java.awt.Insets(4, 4, 4, 4);
                getDistanzaJPanel().add(getDistanzaText(), constraintsDistanzaText);
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjDistanzaJPanel;
    }

    /**
 * Restituisce il valore della proprieta DistanzaText.
 * @return mainPackage.InputNumero
 */
    private InputNumero getDistanzaText() {
        if (ivjDistanzaText == null) {
            try {
                ivjDistanzaText = new InputNumero("###0");
                ivjDistanzaText.setName("DistanzaText");
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjDistanzaText;
    }

    /**
 * Restituisce il valore della proprieta EvasioneJRadioButton.
 * @return javax.swing.JRadioButton
 */
    private javax.swing.JRadioButton getEvasioneJRadioButton() {
        if (ivjEvasioneJRadioButton == null) {
            try {
                ivjEvasioneJRadioButton = new javax.swing.JRadioButton();
                ivjEvasioneJRadioButton.setName("EvasioneJRadioButton");
                ivjEvasioneJRadioButton.setText(Messages.getString("EditScadDer.All__evasione_dell__impegno_origine_20"));
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjEvasioneJRadioButton;
    }

    /**
 * Restituisce il valore della proprieta InserimentoJRadioButton.
 * @return javax.swing.JRadioButton
 */
    private javax.swing.JRadioButton getInserimentoJRadioButton() {
        if (ivjInserimentoJRadioButton == null) {
            try {
                ivjInserimentoJRadioButton = new javax.swing.JRadioButton();
                ivjInserimentoJRadioButton.setName("InserimentoJRadioButton");
                ivjInserimentoJRadioButton.setSelected(true);
                ivjInserimentoJRadioButton.setText(Messages.getString("EditScadDer.All__inserimento_dell__impegno_origine_22"));
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjInserimentoJRadioButton;
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
                ivjJLabel1.setText(Messages.getString("EditScadDer.Distanza_(gg.)_27"));
                ivjJLabel1.setForeground(java.awt.Color.black);
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjJLabel1;
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
                ivjOkJButton.setText("Ok");
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjOkJButton;
    }

    /**
 * Restituisce il valore della proprieta VediCatImpDer.
 * @return dialogPackage.ScegliCatImp
 */
    private ScegliCatImp getVediCatImpDer() {
        if (ivjVediCatImpDer == null) {
            try {
                ivjVediCatImpDer = new dialogPackage.ScegliCatImp(this);
                ivjVediCatImpDer.setName("VediCatImpDer");
                ivjVediCatImpDer.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjVediCatImpDer;
    }

    /**
 * Restituisce il valore della proprieta VediCatImp.
 * @return dialogPackage.ScegliCatImp
 */
    private ScegliCatImp getVediCatImpOrig() {
        if (ivjVediCatImpOrig == null) {
            try {
                ivjVediCatImpOrig = new dialogPackage.ScegliCatImp(this);
                ivjVediCatImpOrig.setName("VediCatImpOrig");
                ivjVediCatImpOrig.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjVediCatImpOrig;
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
        getCatOrigJButton().addActionListener(ivjEventHandler);
        getCatDerJButton().addActionListener(ivjEventHandler);
        getVediCatImpOrig().addScegliCatImpListener(ivjEventHandler);
        getVediCatImpDer().addScegliCatImpListener(ivjEventHandler);
        getAnnullaJButton().addActionListener(ivjEventHandler);
        getOkJButton().addActionListener(ivjEventHandler);
    }

    /**
 * Inizializzare la classe.
 */
    private void initialize() {
        try {
            setName("EditScadDer");
            setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
            setSize(396, 244);
            setModal(true);
            setTitle(Messages.getString("EditScadDer.Scadenza_Derivata_33"));
            setContentPane(getJDialogContentPane());
            initConnections();
            connEtoC1();
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
            EditScadDer aEditScadDer;
            aEditScadDer = new EditScadDer();
            aEditScadDer.setModal(true);
            aEditScadDer.addWindowListener(new java.awt.event.WindowAdapter() {

                public void windowClosing(java.awt.event.WindowEvent e) {
                    System.exit(0);
                }

                ;
            });
            aEditScadDer.show();
            java.awt.Insets insets = aEditScadDer.getInsets();
            aEditScadDer.setSize(aEditScadDer.getWidth() + insets.left + insets.right, aEditScadDer.getHeight() + insets.top + insets.bottom);
            aEditScadDer.setVisible(true);
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
            if (getCatOrigJTextField().getText().equals("")) {
                javax.swing.JOptionPane.showMessageDialog(this, Messages.getString("EditScadDer.Selezionare_la_Categoria_di_Origine_36"), Messages.getString("EditScadDer.Errore_!_37"), javax.swing.JOptionPane.OK_OPTION);
                corretto = false;
                return;
            }
            if (getCatDerJTextField().getText().equals("")) {
                javax.swing.JOptionPane.showMessageDialog(this, Messages.getString("EditScadDer.Selezionare_la_Categoria_Derivata_39"), Messages.getString("EditScadDer.Errore_!_40"), javax.swing.JOptionPane.OK_OPTION);
                corretto = false;
                return;
            }
            if (getButtonFlag().booleanValue()) {
                String codCatOrig = getCodCatImpOrig();
                String codCatDer = getCodCatImpDer();
                int distanza = getDistanzaText().getBigDecimal().intValue();
                int quando = getConsegueBBG().getSelectedButtonIndex();
                dbConnPackage.DBDavide.insScadDer(codCatOrig, codCatDer, distanza, quando);
            } else {
                String codScadDer = getCodScadDer();
                String codCatOrig = getCodCatImpOrig();
                String codCatDer = getCodCatImpDer();
                int distanza = getDistanzaText().getBigDecimal().intValue();
                int quando = getConsegueBBG().getSelectedButtonIndex();
                dbConnPackage.DBDavide.updScadDer(codScadDer, codCatOrig, codCatDer, distanza, quando);
            }
            corretto = true;
            dispose();
        } catch (Exception e) {
            mainPackage.CostantiDavide.msgEccezione("Eccezione in okJButtonAction di EditScadDer");
            e.printStackTrace();
            corretto = false;
        }
        return;
    }

    /**
 * Inserire qui la descrizione del metodo.
 * Data di creazione: (16/10/2002 10.36.14)
 */
    public void pulisciTutto() {
        try {
            String vuoto = "";
            setCodScadDer(vuoto);
            setCodCatImpOrig(vuoto);
            setCodCatImpDer(vuoto);
            getCatOrigJTextField().setText(vuoto);
            getCatDerJTextField().setText(vuoto);
            getConsegueBBG().setSelectedButtonIndex(0);
            getDistanzaText().setValue(new BigDecimal(0));
        } catch (Exception e) {
            mainPackage.CostantiDavide.msgEccezione("Eccezione in pulisciTutto() di EditScadDer");
            e.printStackTrace();
        }
    }

    /**
 *
 * @param newListener dialogPackage.EditScadDerListener
 */
    public void removeEditScadDerListener(dialogPackage.EditScadDerListener newListener) {
        fieldEditScadDerListenerEventMulticaster = dialogPackage.EditScadDerListenerEventMulticaster.remove(fieldEditScadDerListenerEventMulticaster, newListener);
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
 * Impostare CodCatImpDer su un nuovo valore.
 * @param newValue java.lang.String
 */
    private void setCodCatImpDer(java.lang.String newValue) {
        if (ivjCodCatImpDer != newValue) {
            try {
                String oldValue = getCodCatImpDer();
                ivjCodCatImpDer = newValue;
                firePropertyChange("codScadDerThis", oldValue, newValue);
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        ;
    }

    /**
 * Impostare CodCatImpOrig su un nuovo valore.
 * @param newValue java.lang.String
 */
    private void setCodCatImpOrig(java.lang.String newValue) {
        if (ivjCodCatImpOrig != newValue) {
            try {
                String oldValue = getCodCatImpOrig();
                ivjCodCatImpOrig = newValue;
                firePropertyChange("codScadDerThis", oldValue, newValue);
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        ;
    }

    /**
 * Impostare CodScadDer su un nuovo valore.
 * @param newValue java.lang.String
 */
    private void setCodScadDer(java.lang.String newValue) {
        if (ivjCodScadDer != newValue) {
            try {
                String oldValue = getCodScadDer();
                ivjCodScadDer = newValue;
                firePropertyChange("codScadDerThis", oldValue, newValue);
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        ;
    }

    /**
 * Metodo creato per supportare la promozione dell'attributo di codScadDerThis.
 * @param arg1 java.lang.String
 */
    public void setCodScadDerThis(java.lang.String arg1) {
        setCodScadDer(arg1);
    }

    /**
 * Comment
 */
    public void vediCatImpDer_SceltaJButtonAction(java.util.EventObject newEvent) {
        setCodCatImpDer(getVediCatImpDer().getCodiceCatImpThis());
        getCatDerJTextField().setText(getVediCatImpDer().getCategoriaImpThis());
        return;
    }

    /**
 * Comment
 */
    public void vediCatImpOrig_SceltaJButtonAction(java.util.EventObject newEvent) {
        setCodCatImpOrig(getVediCatImpOrig().getCodiceCatImpThis());
        getCatOrigJTextField().setText(getVediCatImpOrig().getCategoriaImpThis());
        return;
    }
}
