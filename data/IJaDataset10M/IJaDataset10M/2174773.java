package dialogPackage;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import mainPackage.*;

/**
 * Inserire qui la descrizione del tipo.
 * Data di creazione: (12/12/2002 12.43.40)
 * @author: PeP1
 */
public class EditDettaglioParc extends MiaJDialog {

    private javax.swing.JButton ivjAnnullaJButton = null;

    private javax.swing.JPanel ivjBottomJPanel = null;

    private provamask.INostroDate ivjdataText = null;

    private javax.swing.JLabel ivjDescrJLabel = null;

    private javax.swing.JTextField ivjDescrJTF = null;

    private mainPackage.InputNumero ivjimponibileText = null;

    private javax.swing.JCheckBox ivjIvaJCB = null;

    private javax.swing.JPanel ivjJDialogContentPane = null;

    private javax.swing.JLabel ivjJLabel = null;

    private javax.swing.JLabel ivjJLabel1 = null;

    private javax.swing.JLabel ivjJLabel22 = null;

    private javax.swing.JPanel ivjJPanel1 = null;

    private javax.swing.JPanel ivjJPanel2 = null;

    private mainPackage.InputNumero ivjmaxText = null;

    private mainPackage.InputNumero ivjminText = null;

    private javax.swing.JButton ivjOkJButton = null;

    private mainPackage.InputNumero ivjqtaText = null;

    private javax.swing.JButton ivjCalcJButton = null;

    protected transient dialogPackage.EditDettaglioParcListener fieldEditDettaglioParcListenerEventMulticaster = null;

    IvjEventHandler ivjEventHandler = new IvjEventHandler();

    private java.util.Date ivjData = null;

    private javax.swing.JLabel ivjMaxJLabel = null;

    private javax.swing.JLabel ivjMinJLabel = null;

    public int flag;

    public java.util.Date data;

    public java.lang.String descrizione;

    public java.math.BigDecimal imponibile;

    public java.math.BigDecimal min;

    public java.math.BigDecimal max;

    public java.math.BigDecimal qta;

    public boolean iva;

    public boolean corretto = false;

    public int indice = -1;

    class IvjEventHandler implements java.awt.event.ActionListener {

        public void actionPerformed(java.awt.event.ActionEvent e) {
            if (e.getSource() == EditDettaglioParc.this.getCalcJButton()) connEtoC2(e);
            if (e.getSource() == EditDettaglioParc.this.getOkJButton()) connEtoC4(e);
            if (e.getSource() == EditDettaglioParc.this.getOkJButton()) connEtoC3(e);
            if (e.getSource() == EditDettaglioParc.this.getAnnullaJButton()) connEtoC5(e);
        }

        ;
    }

    ;

    /**
 * Commento del constructor EditSpeseParc.
 */
    public EditDettaglioParc() {
        super();
        initialize();
    }

    /**
 * Commento del constructor EditSpeseParc.
 * @param owner java.awt.Dialog
 */
    public EditDettaglioParc(java.awt.Dialog owner) {
        super(owner);
        initialize();
    }

    /**
 * Commento del constructor EditSpeseParc.
 * @param owner java.awt.Dialog
 * @param title java.lang.String
 */
    public EditDettaglioParc(java.awt.Dialog owner, String title) {
        super(owner, title);
        initialize();
    }

    /**
 * Commento del constructor EditSpeseParc.
 * @param owner java.awt.Dialog
 * @param title java.lang.String
 * @param modal boolean
 */
    public EditDettaglioParc(java.awt.Dialog owner, String title, boolean modal) {
        super(owner, title, modal);
        initialize();
    }

    /**
 * Commento del constructor EditSpeseParc.
 * @param owner java.awt.Dialog
 * @param modal boolean
 */
    public EditDettaglioParc(java.awt.Dialog owner, boolean modal) {
        super(owner, modal);
        initialize();
    }

    /**
 * Commento del constructor EditSpeseParc.
 * @param owner java.awt.Frame
 */
    public EditDettaglioParc(java.awt.Frame owner) {
        super(owner);
        initialize();
    }

    /**
 * Commento del constructor EditSpeseParc.
 * @param owner java.awt.Frame
 * @param title java.lang.String
 */
    public EditDettaglioParc(java.awt.Frame owner, String title) {
        super(owner, title);
        initialize();
    }

    /**
 * Commento del constructor EditSpeseParc.
 * @param owner java.awt.Frame
 * @param title java.lang.String
 * @param modal boolean
 */
    public EditDettaglioParc(java.awt.Frame owner, String title, boolean modal) {
        super(owner, title, modal);
        initialize();
    }

    /**
 * Commento del constructor EditSpeseParc.
 * @param owner java.awt.Frame
 * @param modal boolean
 */
    public EditDettaglioParc(java.awt.Frame owner, boolean modal) {
        super(owner, modal);
        initialize();
    }

    /**
 *
 * @param newListener dialogPackage.EditDettaglioParcListener
 */
    public void addEditDettaglioParcListener(dialogPackage.EditDettaglioParcListener newListener) {
        fieldEditDettaglioParcListenerEventMulticaster = dialogPackage.EditDettaglioParcListenerEventMulticaster.add(fieldEditDettaglioParcListenerEventMulticaster, newListener);
        return;
    }

    /**
 * Comment
 */
    public void annullaJButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
        try {
            dispose();
        } catch (Exception e) {
            mainPackage.CostantiDavide.msgInfo("Eccez. in annulla actionPerformed di EditDettalioParc");
            e.printStackTrace();
        }
    }

    /**
 * Comment
 */
    public void calcJButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
        try {
            boolean val = mainPackage.CostantiDavide.eseguiCalcolatrice();
            if (val == false) {
                javax.swing.JOptionPane.showMessageDialog(this, Messages.getString("EditDettaglioParc.Spiacente.Il_programma_non_pu_u00F2_essere_avviato_!_2"), Messages.getString("EditDettaglioParc.Information_3"), javax.swing.JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            mainPackage.CostantiDavide.msgInfo("Eccez. in calcJButton di EditDettaglioParc");
            e.printStackTrace();
        }
    }

    /**
 * Inserire qui la descrizione del metodo.
 * Data di creazione: (12/12/2002 15.49.04)
 * @param data java.util.Date
 * @param descrizione java.lang.String
 * @param imponibile java.math.BigDecimal
 * @param min java.math.BigDecimal
 * @param max java.math.BigDecimal
 * @param qta java.math.BigDecimal
 * @param iva boolean
 */
    public void caricaValori(java.util.Date data, String descrizione, java.math.BigDecimal imponibile, java.math.BigDecimal min, java.math.BigDecimal max, java.math.BigDecimal qta, boolean iva) {
        try {
            getdataText().setData(data);
            getDescrJTF().setText(descrizione);
            getimponibileText().setBigDecimal(imponibile);
            getminText().setBigDecimal(min);
            getmaxText().setBigDecimal(max);
            getqtaText().setBigDecimal(qta);
            getIvaJCB().setSelected(iva);
        } catch (Exception e) {
            mainPackage.CostantiDavide.msgInfo("Eccex. in caricaValori(...) di EditDettParc");
            e.printStackTrace();
        }
    }

    /**
 * connEtoC1:  (EditSpeseParc.initialize() --> EditDettaglioParc.editSpeseParc_Initialize()V)
 */
    private void connEtoC1() {
        try {
            this.editSpeseParc_Initialize();
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    /**
 * connEtoC2:  (CalcJButton.action.actionPerformed(java.awt.event.ActionEvent) --> EditDettaglioParc.calcJButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
    private void connEtoC2(java.awt.event.ActionEvent arg1) {
        try {
            this.calcJButton_ActionPerformed(arg1);
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    /**
 * connEtoC3:  (OkJButton.action.actionPerformed(java.awt.event.ActionEvent) --> EditDettaglioParc.fireOkJButtonAction_actionPerformed(Ljava.util.EventObject;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
    private void connEtoC3(java.awt.event.ActionEvent arg1) {
        try {
            this.fireOkJButtonAction_actionPerformed(new java.util.EventObject(this));
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    /**
 * connEtoC4:  (OkJButton.action.actionPerformed(java.awt.event.ActionEvent) --> EditDettaglioParc.okJButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
    private void connEtoC4(java.awt.event.ActionEvent arg1) {
        try {
            this.okJButton_ActionPerformed(arg1);
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    /**
 * connEtoC5:  (AnnullaJButton.action.actionPerformed(java.awt.event.ActionEvent) --> EditDettaglioParc.annullaJButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
    private void connEtoC5(java.awt.event.ActionEvent arg1) {
        try {
            this.annullaJButton_ActionPerformed(arg1);
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    /**
 * Comment
 */
    public void editSpeseParc_Initialize() {
        try {
            javax.swing.JButton[] b_azione = { getOkJButton(), getAnnullaJButton(), getCalcJButton() };
            mainPackage.CostantiDavide.inizBordiPulsanti(b_azione, mainPackage.CostantiDavide.TIPO_AZIONE);
        } catch (Exception e) {
            mainPackage.CostantiDavide.msgInfo("Eccez. in inizialize di EditDettaglioParc");
            e.printStackTrace();
        }
    }

    /**
 * Metodo per il supporto di eventi del listener.
 * @param newEvent java.util.EventObject
 */
    protected void fireOkJButtonAction_actionPerformed(java.util.EventObject newEvent) {
        if (corretto == false) {
            return;
        }
        if (fieldEditDettaglioParcListenerEventMulticaster == null) {
            return;
        }
        ;
        fieldEditDettaglioParcListenerEventMulticaster.okJButtonAction_actionPerformed(newEvent);
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
 * Restituisce il valore della proprieta BottomJPanel.
 * @return javax.swing.JPanel
 */
    private javax.swing.JPanel getBottomJPanel() {
        if (ivjBottomJPanel == null) {
            try {
                ivjBottomJPanel = new javax.swing.JPanel();
                ivjBottomJPanel.setName("BottomJPanel");
                ivjBottomJPanel.setLayout(new java.awt.FlowLayout());
                getBottomJPanel().add(getOkJButton(), getOkJButton().getName());
                this.getRootPane().setDefaultButton(getOkJButton());
                ivjBottomJPanel.add(getAnnullaJButton());
                ivjBottomJPanel.add(getCalcJButton());
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjBottomJPanel;
    }

    /**
 *
 */
    private static void getBuilderData() {
    }

    /**
 * Restituisce il valore della proprieta CalcJButton.
 * @return javax.swing.JButton
 */
    private javax.swing.JButton getCalcJButton() {
        if (ivjCalcJButton == null) {
            try {
                ivjCalcJButton = new javax.swing.JButton();
                ivjCalcJButton.setName("CalcJButton");
                ivjCalcJButton.setText(Messages.getString("EditDettaglioParc.Calcolatrice_11"));
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjCalcJButton;
    }

    /**
 * Restituisce il valore della proprieta Data.
 * @return java.util.Date
 */
    private java.util.Date getData() {
        return ivjData;
    }

    /**
 * Restituisce il valore della proprieta dataText.
 * @return provamask.INostroDate
 */
    private provamask.INostroDate getdataText() {
        if (ivjdataText == null) {
            try {
                ivjdataText = new provamask.INostroDate();
                ivjdataText.setName("dataText");
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjdataText;
    }

    /**
 * Metodo creato per supportare la promozione dell'attributo di dataThis.
 * @return java.util.Date
 */
    public java.util.Date getDataThis() {
        return getData();
    }

    /**
 * Restituisce il valore della proprieta DescrJLabel.
 * @return javax.swing.JLabel
 */
    private javax.swing.JLabel getDescrJLabel() {
        if (ivjDescrJLabel == null) {
            try {
                ivjDescrJLabel = new javax.swing.JLabel();
                ivjDescrJLabel.setName("DescrJLabel");
                ivjDescrJLabel.setText(Messages.getString("EditDettaglioParc.Descrizione_14"));
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjDescrJLabel;
    }

    /**
 * Restituisce il valore della proprieta DescrJTF.
 * @return javax.swing.JTextField
 */
    private javax.swing.JTextField getDescrJTF() {
        if (ivjDescrJTF == null) {
            try {
                ivjDescrJTF = new mainPackage.JTextFieldLimitato(80);
                ivjDescrJTF.setName("DescrJTF");
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjDescrJTF;
    }

    /**
 * Restituisce il valore della proprieta imponibileText.
 * @return provamask.InputNumero
 */
    private mainPackage.InputNumero getimponibileText() {
        if (ivjimponibileText == null) {
            try {
                ivjimponibileText = new mainPackage.InputNumero();
                ivjimponibileText.setName("imponibileText");
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjimponibileText;
    }

    /**
 * Restituisce il valore della proprieta IvaJCB.
 * @return javax.swing.JCheckBox
 */
    private javax.swing.JCheckBox getIvaJCB() {
        if (ivjIvaJCB == null) {
            try {
                ivjIvaJCB = new javax.swing.JCheckBox();
                ivjIvaJCB.setName("IvaJCB");
                ivjIvaJCB.setText(Messages.getString("EditDettaglioParc.Spesa_Imponibile_Iva_18"));
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjIvaJCB;
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
                getJDialogContentPane().add(getBottomJPanel(), "South");
                getJDialogContentPane().add(getJPanel1(), "Center");
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjJDialogContentPane;
    }

    /**
 * Restituisce il valore della proprieta JLabel.
 * @return javax.swing.JLabel
 */
    private javax.swing.JLabel getJLabel() {
        if (ivjJLabel == null) {
            try {
                ivjJLabel = new javax.swing.JLabel();
                ivjJLabel.setName("JLabel");
                ivjJLabel.setText(Messages.getString("EditDettaglioParc.Imponibile_23"));
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjJLabel;
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
                ivjJLabel1.setText(Messages.getString("EditDettaglioParc.Data_25"));
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjJLabel1;
    }

    /**
 * Restituisce il valore della proprieta JLabel22.
 * @return javax.swing.JLabel
 */
    private javax.swing.JLabel getJLabel22() {
        if (ivjJLabel22 == null) {
            try {
                ivjJLabel22 = new javax.swing.JLabel();
                ivjJLabel22.setName("JLabel22");
                ivjJLabel22.setText(Messages.getString("EditDettaglioParc.Quantit_u00E0_27"));
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjJLabel22;
    }

    /**
 * Restituisce il valore della proprieta JPanel1.
 * @return javax.swing.JPanel
 */
    private javax.swing.JPanel getJPanel1() {
        if (ivjJPanel1 == null) {
            try {
                ivjJPanel1 = new javax.swing.JPanel();
                ivjJPanel1.setName("JPanel1");
                ivjJPanel1.setLayout(new java.awt.GridBagLayout());
                java.awt.GridBagConstraints constraintsJLabel1 = new java.awt.GridBagConstraints();
                constraintsJLabel1.gridx = 0;
                constraintsJLabel1.gridy = 0;
                constraintsJLabel1.fill = java.awt.GridBagConstraints.HORIZONTAL;
                constraintsJLabel1.insets = new java.awt.Insets(4, 4, 4, 4);
                getJPanel1().add(getJLabel1(), constraintsJLabel1);
                java.awt.GridBagConstraints constraintsdataText = new java.awt.GridBagConstraints();
                constraintsdataText.gridx = 1;
                constraintsdataText.gridy = 0;
                constraintsdataText.fill = java.awt.GridBagConstraints.HORIZONTAL;
                constraintsdataText.weightx = 1.0;
                constraintsdataText.insets = new java.awt.Insets(4, 4, 4, 4);
                getJPanel1().add(getdataText(), constraintsdataText);
                java.awt.GridBagConstraints constraintsJPanel2 = new java.awt.GridBagConstraints();
                constraintsJPanel2.gridx = 2;
                constraintsJPanel2.gridy = 0;
                constraintsJPanel2.fill = java.awt.GridBagConstraints.BOTH;
                constraintsJPanel2.weightx = 8.0;
                constraintsJPanel2.insets = new java.awt.Insets(4, 4, 4, 4);
                getJPanel1().add(getJPanel2(), constraintsJPanel2);
                java.awt.GridBagConstraints constraintsDescrJLabel = new java.awt.GridBagConstraints();
                constraintsDescrJLabel.gridx = 0;
                constraintsDescrJLabel.gridy = 1;
                constraintsDescrJLabel.fill = java.awt.GridBagConstraints.HORIZONTAL;
                constraintsDescrJLabel.insets = new java.awt.Insets(4, 4, 4, 4);
                getJPanel1().add(getDescrJLabel(), constraintsDescrJLabel);
                java.awt.GridBagConstraints constraintsDescrJTF = new java.awt.GridBagConstraints();
                constraintsDescrJTF.gridx = 1;
                constraintsDescrJTF.gridy = 1;
                constraintsDescrJTF.gridwidth = 2;
                constraintsDescrJTF.fill = java.awt.GridBagConstraints.HORIZONTAL;
                constraintsDescrJTF.weightx = 1.0;
                constraintsDescrJTF.insets = new java.awt.Insets(4, 4, 4, 4);
                getJPanel1().add(getDescrJTF(), constraintsDescrJTF);
                java.awt.GridBagConstraints constraintsJLabel = new java.awt.GridBagConstraints();
                constraintsJLabel.gridx = 0;
                constraintsJLabel.gridy = 2;
                constraintsJLabel.fill = java.awt.GridBagConstraints.HORIZONTAL;
                constraintsJLabel.insets = new java.awt.Insets(4, 4, 4, 4);
                getJPanel1().add(getJLabel(), constraintsJLabel);
                java.awt.GridBagConstraints constraintsimponibileText = new java.awt.GridBagConstraints();
                constraintsimponibileText.gridx = 1;
                constraintsimponibileText.gridy = 2;
                constraintsimponibileText.fill = java.awt.GridBagConstraints.HORIZONTAL;
                constraintsimponibileText.weightx = 1.0;
                constraintsimponibileText.insets = new java.awt.Insets(4, 4, 4, 4);
                getJPanel1().add(getimponibileText(), constraintsimponibileText);
                java.awt.GridBagConstraints constraintsMinJLabel = new java.awt.GridBagConstraints();
                constraintsMinJLabel.gridx = 0;
                constraintsMinJLabel.gridy = 3;
                constraintsMinJLabel.fill = java.awt.GridBagConstraints.HORIZONTAL;
                constraintsMinJLabel.insets = new java.awt.Insets(4, 4, 4, 4);
                getJPanel1().add(getMinJLabel(), constraintsMinJLabel);
                java.awt.GridBagConstraints constraintsminText = new java.awt.GridBagConstraints();
                constraintsminText.gridx = 1;
                constraintsminText.gridy = 3;
                constraintsminText.fill = java.awt.GridBagConstraints.HORIZONTAL;
                constraintsminText.weightx = 1.0;
                constraintsminText.insets = new java.awt.Insets(4, 4, 4, 4);
                getJPanel1().add(getminText(), constraintsminText);
                java.awt.GridBagConstraints constraintsMaxJLabel = new java.awt.GridBagConstraints();
                constraintsMaxJLabel.gridx = 0;
                constraintsMaxJLabel.gridy = 4;
                constraintsMaxJLabel.fill = java.awt.GridBagConstraints.HORIZONTAL;
                constraintsMaxJLabel.insets = new java.awt.Insets(4, 4, 4, 4);
                getJPanel1().add(getMaxJLabel(), constraintsMaxJLabel);
                java.awt.GridBagConstraints constraintsmaxText = new java.awt.GridBagConstraints();
                constraintsmaxText.gridx = 1;
                constraintsmaxText.gridy = 4;
                constraintsmaxText.fill = java.awt.GridBagConstraints.HORIZONTAL;
                constraintsmaxText.weightx = 1.0;
                constraintsmaxText.insets = new java.awt.Insets(4, 4, 4, 4);
                getJPanel1().add(getmaxText(), constraintsmaxText);
                java.awt.GridBagConstraints constraintsJLabel22 = new java.awt.GridBagConstraints();
                constraintsJLabel22.gridx = 0;
                constraintsJLabel22.gridy = 5;
                constraintsJLabel22.fill = java.awt.GridBagConstraints.HORIZONTAL;
                constraintsJLabel22.insets = new java.awt.Insets(4, 4, 4, 4);
                getJPanel1().add(getJLabel22(), constraintsJLabel22);
                java.awt.GridBagConstraints constraintsqtaText = new java.awt.GridBagConstraints();
                constraintsqtaText.gridx = 1;
                constraintsqtaText.gridy = 5;
                constraintsqtaText.fill = java.awt.GridBagConstraints.HORIZONTAL;
                constraintsqtaText.weightx = 1.0;
                constraintsqtaText.insets = new java.awt.Insets(4, 4, 4, 4);
                getJPanel1().add(getqtaText(), constraintsqtaText);
                java.awt.GridBagConstraints constraintsIvaJCB = new java.awt.GridBagConstraints();
                constraintsIvaJCB.gridx = 2;
                constraintsIvaJCB.gridy = 5;
                constraintsIvaJCB.insets = new java.awt.Insets(4, 4, 4, 4);
                getJPanel1().add(getIvaJCB(), constraintsIvaJCB);
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjJPanel1;
    }

    /**
 * Restituisce il valore della proprieta JPanel2.
 * @return javax.swing.JPanel
 */
    private javax.swing.JPanel getJPanel2() {
        if (ivjJPanel2 == null) {
            try {
                ivjJPanel2 = new javax.swing.JPanel();
                ivjJPanel2.setName("JPanel2");
                ivjJPanel2.setLayout(null);
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjJPanel2;
    }

    /**
 * Restituisce il valore della proprieta JLabel21.
 * @return javax.swing.JLabel
 */
    private javax.swing.JLabel getMaxJLabel() {
        if (ivjMaxJLabel == null) {
            try {
                ivjMaxJLabel = new javax.swing.JLabel();
                ivjMaxJLabel.setName("MaxJLabel");
                ivjMaxJLabel.setText(Messages.getString("EditDettaglioParc.Massimo_31"));
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjMaxJLabel;
    }

    /**
 * Restituisce il valore della proprieta maxText.
 * @return provamask.InputNumero
 */
    private mainPackage.InputNumero getmaxText() {
        if (ivjmaxText == null) {
            try {
                ivjmaxText = new mainPackage.InputNumero();
                ivjmaxText.setName("maxText");
                ivjmaxText.setEditable(true);
                ivjmaxText.setEnabled(false);
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjmaxText;
    }

    /**
 * Restituisce il valore della proprieta JLabel2.
 * @return javax.swing.JLabel
 */
    private javax.swing.JLabel getMinJLabel() {
        if (ivjMinJLabel == null) {
            try {
                ivjMinJLabel = new javax.swing.JLabel();
                ivjMinJLabel.setName("MinJLabel");
                ivjMinJLabel.setText(Messages.getString("EditDettaglioParc.Minimo_34"));
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjMinJLabel;
    }

    /**
 * Restituisce il valore della proprieta minText.
 * @return provamask.InputNumero
 */
    private mainPackage.InputNumero getminText() {
        if (ivjminText == null) {
            try {
                ivjminText = new mainPackage.InputNumero();
                ivjminText.setName("minText");
                ivjminText.setEnabled(false);
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjminText;
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
 * Restituisce il valore della proprieta qtaText.
 * @return provamask.InputNumero
 */
    private mainPackage.InputNumero getqtaText() {
        if (ivjqtaText == null) {
            try {
                ivjqtaText = new mainPackage.InputNumero(new DecimalFormat("###.##"));
                ivjqtaText.setName("qtaText");
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjqtaText;
    }

    /**
 * Richiamato ogni volta che la parte lancia un'eccezione.
 * @param exception java.lang.Throwable
 */
    private void handleException(java.lang.Throwable exception) {
    }

    /**
 * Inserire qui la descrizione del metodo.
 * Data di creazione: (12/12/2002 13.21.56)
 * @param val int
 */
    public void impostaFunzionalita(int val) {
        try {
            switch(val) {
                case 0:
                    {
                        getMinJLabel().setEnabled(false);
                        getMaxJLabel().setEnabled(false);
                        getIvaJCB().setEnabled(true);
                        break;
                    }
                case 1:
                    {
                        getMinJLabel().setEnabled(true);
                        getMaxJLabel().setEnabled(true);
                        getIvaJCB().setEnabled(false);
                        break;
                    }
            }
        } catch (Exception e) {
            mainPackage.CostantiDavide.msgInfo("Eccez. in impostaFunzionalita(int) di EditDettaglioParc...");
            e.printStackTrace();
        }
    }

    /**
 * Inizializza collegamenti
 * @exception java.lang.Exception La descrizione dell'eccezione.
 */
    private void initConnections() throws java.lang.Exception {
        getCalcJButton().addActionListener(ivjEventHandler);
        getOkJButton().addActionListener(ivjEventHandler);
        getAnnullaJButton().addActionListener(ivjEventHandler);
    }

    /**
 * Inizializzare la classe.
 */
    private void initialize() {
        try {
            setName("EditSpeseParc");
            setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
            setSize(366, 252);
            setModal(true);
            setTitle("");
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
            EditDettaglioParc aEditDettaglioParc;
            aEditDettaglioParc = new EditDettaglioParc();
            aEditDettaglioParc.setModal(true);
            aEditDettaglioParc.addWindowListener(new java.awt.event.WindowAdapter() {

                public void windowClosing(java.awt.event.WindowEvent e) {
                    System.exit(0);
                }

                ;
            });
            aEditDettaglioParc.show();
            java.awt.Insets insets = aEditDettaglioParc.getInsets();
            aEditDettaglioParc.setSize(aEditDettaglioParc.getWidth() + insets.left + insets.right, aEditDettaglioParc.getHeight() + insets.top + insets.bottom);
            aEditDettaglioParc.setVisible(true);
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
            if (getDescrJTF().getText().equals("")) {
                javax.swing.JOptionPane.showMessageDialog(this, Messages.getString("EditDettaglioParc.Descrizione_mancante.Inserirla_!_45"), Messages.getString("EditDettaglioParc.Information_46"), javax.swing.JOptionPane.ERROR_MESSAGE);
                corretto = false;
                return;
            }
            if (getimponibileText().getText().equals("") || (getimponibileText().getText().equals("0,00"))) {
                javax.swing.JOptionPane.showMessageDialog(this, Messages.getString("EditDettaglioParc.Importo_nullo_o_mancante.Inserirlo_!_49"), Messages.getString("EditDettaglioParc.Information_50"), javax.swing.JOptionPane.ERROR_MESSAGE);
                corretto = false;
                return;
            }
            corretto = true;
            data = getdataText().getData();
            descrizione = getDescrJTF().getText();
            imponibile = getimponibileText().getBigDecimal();
            min = getminText().getBigDecimal();
            max = getmaxText().getBigDecimal();
            qta = getqtaText().getBigDecimal();
            iva = getIvaJCB().isSelected();
            dispose();
        } catch (Exception e) {
            mainPackage.CostantiDavide.msgInfo("Eccez. in OkActionPermorfed di EditDettaglioParc");
            e.printStackTrace();
        }
    }

    /**
 * Inserire qui la descrizione del metodo.
 * Data di creazione: (12/12/2002 13.17.39)
 */
    public void pulisciTutto() {
        try {
            getdataText().setData(getData());
            getDescrJTF().setText("");
            getimponibileText().setValue(new BigDecimal(0.0));
            getminText().setValue(new BigDecimal(0.0));
            getmaxText().setValue(new BigDecimal(0.0));
            getqtaText().setText("1,00");
            getIvaJCB().setSelected(false);
        } catch (Exception e) {
            mainPackage.CostantiDavide.msgInfo("Eccez. in pulisciTutto() di EditDettaglioParc");
            e.printStackTrace();
        }
    }

    /**
 *
 * @param newListener dialogPackage.EditDettaglioParcListener
 */
    public void removeEditDettaglioParcListener(dialogPackage.EditDettaglioParcListener newListener) {
        fieldEditDettaglioParcListenerEventMulticaster = dialogPackage.EditDettaglioParcListenerEventMulticaster.remove(fieldEditDettaglioParcListenerEventMulticaster, newListener);
        return;
    }

    /**
 * Impostare Data su un nuovo valore.
 * @param newValue java.util.Date
 */
    private void setData(java.util.Date newValue) {
        if (ivjData != newValue) {
            try {
                java.util.Date oldValue = getData();
                ivjData = newValue;
                firePropertyChange("dataThis", oldValue, newValue);
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        ;
    }

    /**
 * Metodo creato per supportare la promozione dell'attributo di dataThis.
 * @param arg1 java.util.Date
 */
    public void setDataThis(java.util.Date arg1) {
        setData(arg1);
    }
}
