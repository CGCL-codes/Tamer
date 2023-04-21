package dialogPackage;

import java.util.*;
import mainPackage.*;

/**
 * Inserire qui la descrizione del tipo.
 * Data di creazione: (02/10/2002 10.53.44)
 * @author: PeP1
 */
public class EditParamTasso extends MiaJDialog {

    private javax.swing.JButton ivjAvantiJButton = null;

    private javax.swing.JPanel ivjBottomJPanel = null;

    private javax.swing.JButton ivjIndietroJButton = null;

    private javax.swing.JPanel ivjJDialogContentPane = null;

    private javax.swing.JPanel ivjJPanel1 = null;

    private javax.swing.JTextArea ivjJTextArea1 = null;

    private javax.swing.JPanel ivjJPanel2 = null;

    private com.ibm.uicontrolsswing.ImageCanvas ivjimgValue = null;

    IvjEventHandler ivjEventHandler = new IvjEventHandler();

    private javax.swing.JPanel ivjParamJPanel = null;

    private javax.swing.JPanel ivjCenterJPanel = null;

    private javax.swing.JPanel ivjJPanel3 = null;

    private javax.swing.JPanel ivjJPanel4 = null;

    private javax.swing.JPanel ivjJPanel5 = null;

    private javax.swing.JPanel ivjJPanel6 = null;

    private javax.swing.JCheckBox ivjLegaleJCB = null;

    private mainPackage.InputNumero ivjLegaleText = null;

    private Integer ivjFlag = null;

    protected transient dialogPackage.EditParamTassoListener fieldEditParamTassoListenerEventMulticaster = null;

    private javax.swing.JButton ivjAnnullaJButton = null;

    class IvjEventHandler implements java.awt.event.ActionListener, java.awt.event.ItemListener, java.awt.event.WindowListener {

        public void actionPerformed(java.awt.event.ActionEvent e) {
            if (e.getSource() == EditParamTasso.this.getAnnullaJButton()) connEtoM1(e);
            if (e.getSource() == EditParamTasso.this.getIndietroJButton()) connEtoC5(e);
            if (e.getSource() == EditParamTasso.this.getAvantiJButton()) connEtoC6(e);
        }

        ;

        public void itemStateChanged(java.awt.event.ItemEvent e) {
            if (e.getSource() == EditParamTasso.this.getLegaleJCB()) connEtoC2(e);
        }

        ;

        public void windowActivated(java.awt.event.WindowEvent e) {
            if (e.getSource() == EditParamTasso.this) connEtoC3(e);
        }

        ;

        public void windowClosed(java.awt.event.WindowEvent e) {
        }

        ;

        public void windowClosing(java.awt.event.WindowEvent e) {
        }

        ;

        public void windowDeactivated(java.awt.event.WindowEvent e) {
        }

        ;

        public void windowDeiconified(java.awt.event.WindowEvent e) {
        }

        ;

        public void windowIconified(java.awt.event.WindowEvent e) {
        }

        ;

        public void windowOpened(java.awt.event.WindowEvent e) {
            if (e.getSource() == EditParamTasso.this) connEtoC4(e);
        }

        ;
    }

    ;

    /**
 * Commento del constructor EditParamTCredito.
 */
    public EditParamTasso() {
        super();
        initialize();
    }

    /**
 * Commento del constructor EditParamTCredito.
 * @param owner java.awt.Dialog
 */
    public EditParamTasso(java.awt.Dialog owner) {
        super(owner);
        initialize();
    }

    /**
 * Commento del constructor EditParamTCredito.
 * @param owner java.awt.Dialog
 * @param title java.lang.String
 */
    public EditParamTasso(java.awt.Dialog owner, String title) {
        super(owner, title);
        initialize();
    }

    /**
 * Commento del constructor EditParamTCredito.
 * @param owner java.awt.Dialog
 * @param title java.lang.String
 * @param modal boolean
 */
    public EditParamTasso(java.awt.Dialog owner, String title, boolean modal) {
        super(owner, title, modal);
        initialize();
    }

    /**
 * Commento del constructor EditParamTCredito.
 * @param owner java.awt.Dialog
 * @param modal boolean
 */
    public EditParamTasso(java.awt.Dialog owner, boolean modal) {
        super(owner, modal);
        initialize();
    }

    /**
 * Commento del constructor EditParamTCredito.
 * @param owner java.awt.Frame
 */
    public EditParamTasso(java.awt.Frame owner) {
        super(owner);
        initialize();
    }

    /**
 * Commento del constructor EditParamTCredito.
 * @param owner java.awt.Frame
 * @param title java.lang.String
 */
    public EditParamTasso(java.awt.Frame owner, String title) {
        super(owner, title);
        initialize();
    }

    /**
 * Commento del constructor EditParamTCredito.
 * @param owner java.awt.Frame
 * @param title java.lang.String
 * @param modal boolean
 */
    public EditParamTasso(java.awt.Frame owner, String title, boolean modal) {
        super(owner, title, modal);
        initialize();
    }

    /**
 * Commento del constructor EditParamTCredito.
 * @param owner java.awt.Frame
 * @param modal boolean
 */
    public EditParamTasso(java.awt.Frame owner, boolean modal) {
        super(owner, modal);
        initialize();
    }

    /**
 *
 * @param newListener dialogPackage.EditParamTassoListener
 */
    public void addEditParamTassoListener(dialogPackage.EditParamTassoListener newListener) {
        fieldEditParamTassoListenerEventMulticaster = dialogPackage.EditParamTassoListenerEventMulticaster.add(fieldEditParamTassoListenerEventMulticaster, newListener);
        return;
    }

    /**
 * Questo metodo si occupa di caricare il tasso legale relativo alla data odierna
 * Data di creazione: (03/10/2002 16.59.30)
 * @param dd java.sql.Date
 */
    public void caricaTasso(java.sql.Date dd) {
        try {
            java.sql.ResultSet rs = dbConnPackage.DBDavide.selTasso(dd);
            if (rs.next()) {
                getLegaleText().setBigDecimal(rs.getBigDecimal(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
 * connEtoC1:  (EditParamModoInt.initialize() --> EditParamTasso.editParamModoInt_Initialize()V)
 */
    private void connEtoC1() {
        try {
            this.editParamModoInt_Initialize();
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    /**
 * connEtoC2:  (LegaleJCB.item.itemStateChanged(java.awt.event.ItemEvent) --> EditParamTasso.legaleJCB_ItemStateChanged(Ljava.awt.event.ItemEvent;)V)
 * @param arg1 java.awt.event.ItemEvent
 */
    private void connEtoC2(java.awt.event.ItemEvent arg1) {
        try {
            this.legaleJCB_ItemStateChanged(arg1);
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    /**
 * connEtoC3:  (EditParamModoInt.window.windowActivated(java.awt.event.WindowEvent) --> EditParamTasso.editParamModoInt_WindowActivated(Ljava.awt.event.WindowEvent;)V)
 * @param arg1 java.awt.event.WindowEvent
 */
    private void connEtoC3(java.awt.event.WindowEvent arg1) {
        try {
            this.editParamModoInt_WindowActivated(arg1);
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    /**
 * connEtoC4:  (EditParamModoInt.window.windowOpened(java.awt.event.WindowEvent) --> EditParamTasso.editParamModoInt_WindowOpened(Ljava.awt.event.WindowEvent;)V)
 * @param arg1 java.awt.event.WindowEvent
 */
    private void connEtoC4(java.awt.event.WindowEvent arg1) {
        try {
            this.editParamModoInt_WindowOpened(arg1);
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    /**
 * connEtoC5:  (IndietroJButton.action.actionPerformed(java.awt.event.ActionEvent) --> EditParamTasso.fireIndietroJButtonAction_actionPerformed(Ljava.util.EventObject;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
    private void connEtoC5(java.awt.event.ActionEvent arg1) {
        try {
            this.fireIndietroJButtonAction_actionPerformed(new java.util.EventObject(this));
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    /**
 * connEtoC6:  (AvantiJButton.action.actionPerformed(java.awt.event.ActionEvent) --> EditParamTasso.fireAvantiJButtonAction_actionPerformed(Ljava.util.EventObject;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
    private void connEtoC6(java.awt.event.ActionEvent arg1) {
        try {
            this.fireAvantiJButtonAction_actionPerformed(new java.util.EventObject(this));
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    /**
 * connEtoM1:  (AnnJButton.action.actionPerformed(java.awt.event.ActionEvent) --> EditParamTCredito.dispose()V)
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
    public void editParamModoInt_Initialize() {
        javax.swing.JButton[] bottoni = { getIndietroJButton(), getAvantiJButton(), getAnnullaJButton() };
        mainPackage.CostantiDavide.inizBordiPulsanti(bottoni, mainPackage.CostantiDavide.TIPO_AZIONE);
        getParamJPanel().setBorder(new javax.swing.border.TitledBorder(Messages.getString("EditParamTasso.Tasso_1")));
        getimgValue().setImage(new com.ibm.uicontrolsswing.ImageGlyph(getClass().getResource("/RIVALUTA.gif")));
        return;
    }

    /**
 * Comment
 */
    public void editParamModoInt_WindowActivated(java.awt.event.WindowEvent windowEvent) {
        Calendar c = java.util.Calendar.getInstance();
        c.setTime(c.getTime());
        java.sql.Date dd = new java.sql.Date(c.getTime().getTime());
        caricaTasso(dd);
        return;
    }

    /**
 * Comment
 */
    public void editParamModoInt_WindowOpened(java.awt.event.WindowEvent windowEvent) {
        Calendar c = java.util.Calendar.getInstance();
        c.setTime(c.getTime());
        java.sql.Date dd = new java.sql.Date(c.getTime().getTime());
        caricaTasso(dd);
        return;
    }

    /**
 * Metodo per il supporto di eventi del listener.
 * @param newEvent java.util.EventObject
 */
    protected void fireAvantiJButtonAction_actionPerformed(java.util.EventObject newEvent) {
        if (fieldEditParamTassoListenerEventMulticaster == null) {
            return;
        }
        ;
        fieldEditParamTassoListenerEventMulticaster.avantiJButtonAction_actionPerformed(newEvent);
    }

    /**
 * Metodo per il supporto di eventi del listener.
 * @param newEvent java.util.EventObject
 */
    protected void fireIndietroJButtonAction_actionPerformed(java.util.EventObject newEvent) {
        if (fieldEditParamTassoListenerEventMulticaster == null) {
            return;
        }
        ;
        fieldEditParamTassoListenerEventMulticaster.indietroJButtonAction_actionPerformed(newEvent);
    }

    /**
 * Restituisce il valore della proprieta AnnJButton.
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
 * Restituisce il valore della proprieta AvantiJButton.
 * @return javax.swing.JButton
 */
    private javax.swing.JButton getAvantiJButton() {
        if (ivjAvantiJButton == null) {
            try {
                ivjAvantiJButton = new javax.swing.JButton();
                ivjAvantiJButton.setName("AvantiJButton");
                ivjAvantiJButton.setText(Messages.getString("EditParamTasso.Avanti->_6"));
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjAvantiJButton;
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
                ivjBottomJPanel.setBackground(new java.awt.Color(204, 204, 204));
                getBottomJPanel().add(getIndietroJButton(), getIndietroJButton().getName());
                ivjBottomJPanel.add(getAvantiJButton());
                ivjBottomJPanel.add(getAnnullaJButton());
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjBottomJPanel;
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
                ivjCenterJPanel.setLayout(new java.awt.BorderLayout());
                ivjCenterJPanel.setBackground(java.awt.SystemColor.activeCaption);
                getCenterJPanel().add(getJPanel3(), "West");
                getCenterJPanel().add(getJPanel4(), "East");
                getCenterJPanel().add(getJPanel5(), "North");
                getCenterJPanel().add(getJPanel6(), "South");
                getCenterJPanel().add(getParamJPanel(), "Center");
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjCenterJPanel;
    }

    /**
 * Restituisce il valore della proprieta Flag.
 * @return java.lang.Integer
 */
    private java.lang.Integer getFlag() {
        return ivjFlag;
    }

    /**
 * Metodo creato per supportare la promozione dell'attributo di flagThis.
 * @return java.lang.Integer
 */
    public java.lang.Integer getFlagThis() {
        return getFlag();
    }

    /**
 * Restituisce il valore della proprieta imgValue.
 * @return com.ibm.uicontrolsswing.ImageCanvas
 */
    private com.ibm.uicontrolsswing.ImageCanvas getimgValue() {
        if (ivjimgValue == null) {
            try {
                ivjimgValue = new com.ibm.uicontrolsswing.ImageCanvas();
                ivjimgValue.setName("imgValue");
                ivjimgValue.setPreferredSize(new java.awt.Dimension(100, 100));
                ivjimgValue.setScalable(true);
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjimgValue;
    }

    /**
 * Restituisce il valore della proprieta IndietroJButton.
 * @return javax.swing.JButton
 */
    private javax.swing.JButton getIndietroJButton() {
        if (ivjIndietroJButton == null) {
            try {
                ivjIndietroJButton = new javax.swing.JButton();
                ivjIndietroJButton.setName("IndietroJButton");
                ivjIndietroJButton.setText(Messages.getString("EditParamTasso.<-Indietro_16"));
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjIndietroJButton;
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
                ivjJDialogContentPane.setBackground(java.awt.Color.blue);
                getJDialogContentPane().add(getJPanel2(), "West");
                getJDialogContentPane().add(getJPanel1(), "Center");
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjJDialogContentPane;
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
                ivjJPanel1.setLayout(new java.awt.BorderLayout());
                ivjJPanel1.setBackground(java.awt.Color.blue);
                getJPanel1().add(getJTextArea1(), "North");
                getJPanel1().add(getBottomJPanel(), "South");
                getJPanel1().add(getCenterJPanel(), "Center");
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
                ivjJPanel2.setLayout(new java.awt.BorderLayout());
                ivjJPanel2.setBackground(new java.awt.Color(204, 204, 204));
                getJPanel2().add(getimgValue(), "North");
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjJPanel2;
    }

    /**
 * Restituisce il valore della proprieta JPanel3.
 * @return javax.swing.JPanel
 */
    private javax.swing.JPanel getJPanel3() {
        if (ivjJPanel3 == null) {
            try {
                ivjJPanel3 = new javax.swing.JPanel();
                ivjJPanel3.setName("JPanel3");
                ivjJPanel3.setLayout(new java.awt.FlowLayout());
                ivjJPanel3.setBackground(new java.awt.Color(204, 204, 204));
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjJPanel3;
    }

    /**
 * Restituisce il valore della proprieta JPanel4.
 * @return javax.swing.JPanel
 */
    private javax.swing.JPanel getJPanel4() {
        if (ivjJPanel4 == null) {
            try {
                ivjJPanel4 = new javax.swing.JPanel();
                ivjJPanel4.setName("JPanel4");
                ivjJPanel4.setLayout(new java.awt.FlowLayout());
                ivjJPanel4.setBackground(new java.awt.Color(204, 204, 204));
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjJPanel4;
    }

    /**
 * Restituisce il valore della proprieta JPanel5.
 * @return javax.swing.JPanel
 */
    private javax.swing.JPanel getJPanel5() {
        if (ivjJPanel5 == null) {
            try {
                ivjJPanel5 = new javax.swing.JPanel();
                ivjJPanel5.setName("JPanel5");
                ivjJPanel5.setLayout(new java.awt.FlowLayout());
                ivjJPanel5.setBackground(new java.awt.Color(204, 204, 204));
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjJPanel5;
    }

    /**
 * Restituisce il valore della proprieta JPanel6.
 * @return javax.swing.JPanel
 */
    private javax.swing.JPanel getJPanel6() {
        if (ivjJPanel6 == null) {
            try {
                ivjJPanel6 = new javax.swing.JPanel();
                ivjJPanel6.setName("JPanel6");
                ivjJPanel6.setLayout(new java.awt.FlowLayout());
                ivjJPanel6.setBackground(new java.awt.Color(204, 204, 204));
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjJPanel6;
    }

    /**
 * Restituisce il valore della proprieta JTextArea1.
 * @return javax.swing.JTextArea
 */
    private javax.swing.JTextArea getJTextArea1() {
        if (ivjJTextArea1 == null) {
            try {
                ivjJTextArea1 = new javax.swing.JTextArea();
                ivjJTextArea1.setName("JTextArea1");
                ivjJTextArea1.setLineWrap(true);
                ivjJTextArea1.setWrapStyleWord(true);
                ivjJTextArea1.setText(Messages.getString("EditParamTasso.Selezionare_LEGALE_per_applicare_il_tasso_legale_(il_programma_si_adegua_automaticamente_alle_variazioni_del_tasso)_o_deselezionare_e_specificare_il_tasso_convenzionale_(p.es._per_indicare_il_2,5%_inserire_il_valore_0.025)_31"));
                ivjJTextArea1.setBackground(java.awt.SystemColor.inactiveCaptionText);
                ivjJTextArea1.setEditable(false);
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjJTextArea1;
    }

    /**
 * Restituisce il valore della proprieta LegaleJCB.
 * @return javax.swing.JCheckBox
 */
    private javax.swing.JCheckBox getLegaleJCB() {
        if (ivjLegaleJCB == null) {
            try {
                ivjLegaleJCB = new javax.swing.JCheckBox();
                ivjLegaleJCB.setName("LegaleJCB");
                ivjLegaleJCB.setSelected(true);
                ivjLegaleJCB.setText(Messages.getString("EditParamTasso.Legale_33"));
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjLegaleJCB;
    }

    /**
 * Restituisce il valore della proprieta LegaleText.
 * @return provamask.InputNumero
 */
    private mainPackage.InputNumero getLegaleText() {
        if (ivjLegaleText == null) {
            try {
                ivjLegaleText = new mainPackage.InputNumero();
                ivjLegaleText.setName("LegaleText");
                ivjLegaleText.setPreferredSize(new java.awt.Dimension(80, 20));
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjLegaleText;
    }

    /**
 * Restituisce il valore della proprieta ParamJPanel.
 * @return javax.swing.JPanel
 */
    private javax.swing.JPanel getParamJPanel() {
        if (ivjParamJPanel == null) {
            try {
                ivjParamJPanel = new javax.swing.JPanel();
                ivjParamJPanel.setName("ParamJPanel");
                ivjParamJPanel.setLayout(new java.awt.FlowLayout());
                ivjParamJPanel.setBackground(new java.awt.Color(204, 204, 204));
                getParamJPanel().add(getLegaleJCB(), getLegaleJCB().getName());
                ivjParamJPanel.add(getLegaleText());
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjParamJPanel;
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
        getLegaleJCB().addItemListener(ivjEventHandler);
        getIndietroJButton().addActionListener(ivjEventHandler);
        getAvantiJButton().addActionListener(ivjEventHandler);
        this.addWindowListener(ivjEventHandler);
    }

    /**
 * Inizializzare la classe.
 */
    private void initialize() {
        try {
            setName("EditParamModoInt");
            setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
            setBackground(java.awt.SystemColor.activeCaptionBorder);
            setModal(true);
            setFont(new java.awt.Font("dialog", 0, 12));
            setSize(500, 250);
            setTitle(Messages.getString("EditParamTasso.Inserimento_Guidato_Parametri_di_Rivalutazione_38"));
            setContentPane(getJDialogContentPane());
            initConnections();
            connEtoC1();
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    /**
 * Comment
 */
    public void legaleJCB_ItemStateChanged(java.awt.event.ItemEvent itemEvent) {
        if (getLegaleJCB().isSelected()) {
            getLegaleText().setEnabled(false);
        } else {
            getLegaleText().setEnabled(true);
        }
        return;
    }

    /**
 *
 * @return boolean
 */
    public boolean legaleJCBIsSelected() {
        return getLegaleJCB().isSelected();
    }

    /**
 *
 * @param b boolean
 */
    public void legaleJCBSetSelected(boolean b) {
        getLegaleJCB().setSelected(b);
    }

    /**
 *
 * @return java.lang.String
 */
    public java.lang.String legaleTextGetText() {
        return getLegaleText().getText();
    }

    /**
 *
 * @param t java.lang.String
 */
    public void legaleTextSetText(java.lang.String t) {
        getLegaleText().setText(t);
    }

    /**
 * Punto di immissione main - avvia la parte quando questa viene eseguita come applicazione
 * @param args java.lang.String[]
 */
    public static void main(java.lang.String[] args) {
        try {
            EditParamTasso aEditParamTasso;
            aEditParamTasso = new EditParamTasso();
            aEditParamTasso.setModal(true);
            aEditParamTasso.addWindowListener(new java.awt.event.WindowAdapter() {

                public void windowClosing(java.awt.event.WindowEvent e) {
                    System.exit(0);
                }

                ;
            });
            aEditParamTasso.show();
            java.awt.Insets insets = aEditParamTasso.getInsets();
            aEditParamTasso.setSize(aEditParamTasso.getWidth() + insets.left + insets.right, aEditParamTasso.getHeight() + insets.top + insets.bottom);
            aEditParamTasso.setVisible(true);
        } catch (Throwable exception) {
            System.err.println("Eccezione verificatasi in main() di javax.swing.JDialog");
            exception.printStackTrace(System.out);
        }
    }

    /**
 *
 * @param newListener dialogPackage.EditParamTassoListener
 */
    public void removeEditParamTassoListener(dialogPackage.EditParamTassoListener newListener) {
        fieldEditParamTassoListenerEventMulticaster = dialogPackage.EditParamTassoListenerEventMulticaster.remove(fieldEditParamTassoListenerEventMulticaster, newListener);
        return;
    }

    /**
 * Impostare Flag su un nuovo valore.
 * @param newValue java.lang.Integer
 */
    private void setFlag(java.lang.Integer newValue) {
        if (ivjFlag != newValue) {
            try {
                Integer oldValue = getFlag();
                ivjFlag = newValue;
                firePropertyChange("flagThis", oldValue, newValue);
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        ;
    }

    /**
 * Metodo creato per supportare la promozione dell'attributo di flagThis.
 * @param arg1 java.lang.Integer
 */
    public void setFlagThis(java.lang.Integer arg1) {
        setFlag(arg1);
    }
}
