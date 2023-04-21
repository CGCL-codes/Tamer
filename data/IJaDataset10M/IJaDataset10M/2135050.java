package dialogPackage;

import javax.swing.event.*;
import mainPackage.*;
import mainPackage.ModelloTabella;
import dbConnPackage.DBDavide;
import javax.swing.*;

/**
 * Inserire qui la descrizione del tipo.
 * Data di creazione: (18/07/2002 11.36.49)
 * @author: PeP1
 */
public class ScegliControParti extends MiaJDialog {

    private JPanel ivjJDialogContentPane = null;

    private JButton ivjAggiungeJButton = null;

    private JPanel ivjButtonsJPanel = null;

    private JButton ivjCancellaJButton = null;

    private JButton ivjCercaJButton = null;

    private JButton ivjChiudiJButton = null;

    private JButton ivjModificaJButton = null;

    private JButton ivjSceltaJButton = null;

    private JButton ivjStampaJButton = null;

    private JScrollPane ivjControPartiJScrollPane = null;

    private mainPackage.MiaJTable ivjScrollPaneTable = null;

    private javax.swing.table.TableColumn ivjTCCognome = null;

    private javax.swing.table.TableColumn ivjTCNato = null;

    private javax.swing.table.TableColumn ivjTCNome = null;

    private String ivjCodiceAnagrafe = null;

    private String ivjCognome = null;

    private String ivjDataNascita = null;

    private String ivjNome = null;

    private JOptionPane ivjOp = null;

    private EditAnagr ivjAggiungiAnagrafe = null;

    private mainPackage.ModelloTabella tabControParti;

    IvjEventHandler ivjEventHandler = new IvjEventHandler();

    private int ris;

    private Cerca ivjRicerca = null;

    protected transient dialogPackage.ScegliControPartiListener fieldScegliControPartiListenerEventMulticaster = null;

    private boolean corretto = false;

    class IvjEventHandler implements dialogPackage.CercaListener, dialogPackage.EditAnagrListener, java.awt.event.ActionListener, java.awt.event.WindowListener {

        public void actionPerformed(java.awt.event.ActionEvent e) {
            if (e.getSource() == ScegliControParti.this.getAggiungeJButton()) connEtoC3(e);
            if (e.getSource() == ScegliControParti.this.getModificaJButton()) connEtoC4(e);
            if (e.getSource() == ScegliControParti.this.getCancellaJButton()) connEtoC5(e);
            if (e.getSource() == ScegliControParti.this.getCercaJButton()) connEtoC10(e);
            if (e.getSource() == ScegliControParti.this.getSceltaJButton()) connEtoC11(e);
            if (e.getSource() == ScegliControParti.this.getChiudiJButton()) connEtoM1(e);
            if (e.getSource() == ScegliControParti.this.getSceltaJButton()) connEtoC12(e);
            if (e.getSource() == ScegliControParti.this.getChiudiJButton()) connEtoC13(e);
        }

        ;

        public void annullaJButtonAction_actionPerformed(java.util.EventObject newEvent) {
            if (newEvent.getSource() == ScegliControParti.this.getAggiungiAnagrafe()) connEtoC6(newEvent);
            if (newEvent.getSource() == ScegliControParti.this.getRicerca()) connEtoC9(newEvent);
        }

        ;

        public void okJButtonAction_actionPerformed(java.util.EventObject newEvent) {
            if (newEvent.getSource() == ScegliControParti.this.getAggiungiAnagrafe()) connEtoC7(newEvent);
            if (newEvent.getSource() == ScegliControParti.this.getRicerca()) connEtoC8(newEvent);
        }

        ;

        public void windowActivated(java.awt.event.WindowEvent e) {
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
            if (e.getSource() == ScegliControParti.this) connEtoC2(e);
        }

        ;
    }

    ;

    /**
	 * Commento del constructor ScegliControParti.
	 */
    public ScegliControParti() {
        super();
        initialize();
    }

    /**
	 * Commento del constructor ScegliControParti.
	 * @param owner java.awt.Dialog
	 */
    public ScegliControParti(java.awt.Dialog owner) {
        super(owner);
        initialize();
    }

    /**
	 * Commento del constructor ScegliControParti.
	 * @param owner java.awt.Dialog
	 * @param title java.lang.String
	 */
    public ScegliControParti(java.awt.Dialog owner, String title) {
        super(owner, title);
        initialize();
    }

    /**
	 * Commento del constructor ScegliControParti.
	 * @param owner java.awt.Dialog
	 * @param title java.lang.String
	 * @param modal boolean
	 */
    public ScegliControParti(java.awt.Dialog owner, String title, boolean modal) {
        super(owner, title, modal);
        initialize();
    }

    /**
	 * Commento del constructor ScegliControParti.
	 * @param owner java.awt.Dialog
	 * @param modal boolean
	 */
    public ScegliControParti(java.awt.Dialog owner, boolean modal) {
        super(owner, modal);
        initialize();
    }

    /**
	 * Commento del constructor ScegliControParti.
	 * @param owner java.awt.Frame
	 */
    public ScegliControParti(java.awt.Frame owner) {
        super(owner);
        initialize();
    }

    /**
	 * Commento del constructor ScegliControParti.
	 * @param owner java.awt.Frame
	 * @param title java.lang.String
	 */
    public ScegliControParti(java.awt.Frame owner, String title) {
        super(owner, title);
        initialize();
    }

    /**
	 * Commento del constructor ScegliControParti.
	 * @param owner java.awt.Frame
	 * @param title java.lang.String
	 * @param modal boolean
	 */
    public ScegliControParti(java.awt.Frame owner, String title, boolean modal) {
        super(owner, title, modal);
        initialize();
    }

    /**
	 * Commento del constructor ScegliControParti.
	 * @param owner java.awt.Frame
	 * @param modal boolean
	 */
    public ScegliControParti(java.awt.Frame owner, boolean modal) {
        super(owner, modal);
        initialize();
    }

    /**
	 *
	 * @param newListener dialogPackage.ScegliControPartiListener
	 */
    public void addScegliControPartiListener(dialogPackage.ScegliControPartiListener newListener) {
        fieldScegliControPartiListenerEventMulticaster = dialogPackage.ScegliControPartiListenerEventMulticaster.add(fieldScegliControPartiListenerEventMulticaster, newListener);
        return;
    }

    /**
	 * Inserire qui la descrizione del metodo.
	 * Data di creazione: (12/07/2002 11.54.31)
	 */
    public void aggiornaVisual() {
        if (tabControParti == null) {
            tabControParti = new ModelloTabella(DBDavide.selAvversari());
        } else {
            tabControParti.setModel(DBDavide.selAvversari());
        }
        getScrollPaneTable().setModel(tabControParti);
        return;
    }

    /**
	 * Comment
	 */
    public void aggiungeJButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
        getAggiungiAnagrafe().setButtonFlagThis(new Boolean(true));
        getAggiungiAnagrafe().pulisciTutto();
        getAggiungiAnagrafe().creaAnagrafe();
        getAggiungiAnagrafe().impostaSelezioni(1);
        getAggiungiAnagrafe().show();
        if (getAggiungiAnagrafe().cognomeInserito != null) {
            getScrollPaneTable().ricercaValoreConChiave(getAggiungiAnagrafe().cognomeInserito, 0, getAggiungiAnagrafe().codInserito, 0, this);
            getAggiungiAnagrafe().cognomeInserito = null;
        }
        return;
    }

    /**
	 * Comment
	 */
    public void aggiungiAnagrafe_AnnullaJButtonAction(java.util.EventObject newEvent) {
        aggiornaVisual();
        return;
    }

    /**
	 * Comment
	 */
    public void aggiungiAnagrafe_OkJButtonAction(java.util.EventObject newEvent) {
        aggiornaVisual();
        return;
    }

    /**
	 * Comment
	 */
    public void cancellaJButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
        try {
            if (getScrollPaneTable().getRowCount() > 0 && getScrollPaneTable().getSelectedRow() != -1) {
                ris = JOptionPane.showConfirmDialog(this, getOp().getMessage(), Messages.getString("ScegliControParti.Scegliere_1"), JOptionPane.YES_NO_OPTION);
                if (ris == JOptionPane.YES_OPTION) {
                    String codice = tabControParti.getCodiceSel(getScrollPaneTable().getSelectedRow());
                    DBDavide.cancellaRecord("Anag", codice);
                }
                aggiornaVisual();
                return;
            }
        } catch (Exception e) {
            mainPackage.CostantiDavide.msgInfo("Eccez. in Cancella di SgliControparti");
            e.printStackTrace();
        }
    }

    /**
	 * Comment
	 */
    public void cercaJButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
        getRicerca().getDescrizioneJTextField().setText("");
        getRicerca().show();
        return;
    }

    /**
	 * connEtoC1:  (ScegliControParti.initialize() --> ScegliControParti.scegliControParti_Initialize()V)
	 */
    private void connEtoC1() {
        try {
            this.scegliControParti_Initialize();
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    /**
	 * connEtoC10:  (CercaJButton.action.actionPerformed(java.awt.event.ActionEvent) --> ScegliControParti.cercaJButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
	 * @param arg1 java.awt.event.ActionEvent
	 */
    private void connEtoC10(java.awt.event.ActionEvent arg1) {
        try {
            this.cercaJButton_ActionPerformed(arg1);
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    /**
	 * connEtoC11:  (SceltaJButton.action.actionPerformed(java.awt.event.ActionEvent) --> ScegliControParti.sceltaJButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
	 * @param arg1 java.awt.event.ActionEvent
	 */
    private void connEtoC11(java.awt.event.ActionEvent arg1) {
        try {
            this.sceltaJButton_ActionPerformed(arg1);
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    /**
	 * connEtoC12:  (SceltaJButton.action.actionPerformed(java.awt.event.ActionEvent) --> ScegliControParti.fireSceltaJButtonAction_actionPerformed(Ljava.util.EventObject;)V)
	 * @param arg1 java.awt.event.ActionEvent
	 */
    private void connEtoC12(java.awt.event.ActionEvent arg1) {
        try {
            this.fireSceltaJButtonAction_actionPerformed(new java.util.EventObject(this));
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    /**
	 * connEtoC13:  (ChiudiJButton.action.actionPerformed(java.awt.event.ActionEvent) --> ScegliControParti.fireChiudiJButtonAction_actionPerformed(Ljava.util.EventObject;)V)
	 * @param arg1 java.awt.event.ActionEvent
	 */
    private void connEtoC13(java.awt.event.ActionEvent arg1) {
        try {
            this.fireChiudiJButtonAction_actionPerformed(new java.util.EventObject(this));
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    /**
	 * connEtoC2:  (ScegliControParti.window.windowOpened(java.awt.event.WindowEvent) --> ScegliControParti.scegliControParti_WindowOpened(Ljava.awt.event.WindowEvent;)V)
	 * @param arg1 java.awt.event.WindowEvent
	 */
    private void connEtoC2(java.awt.event.WindowEvent arg1) {
        try {
            this.scegliControParti_WindowOpened(arg1);
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    /**
	 * connEtoC3:  (AggiungeJButton.action.actionPerformed(java.awt.event.ActionEvent) --> ScegliControParti.aggiungeJButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
	 * @param arg1 java.awt.event.ActionEvent
	 */
    private void connEtoC3(java.awt.event.ActionEvent arg1) {
        try {
            this.aggiungeJButton_ActionPerformed(arg1);
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    /**
	 * connEtoC4:  (ModificaJButton.action.actionPerformed(java.awt.event.ActionEvent) --> ScegliControParti.modificaJButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
	 * @param arg1 java.awt.event.ActionEvent
	 */
    private void connEtoC4(java.awt.event.ActionEvent arg1) {
        try {
            this.modificaJButton_ActionPerformed(arg1);
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    /**
	 * connEtoC5:  (CancellaJButton.action.actionPerformed(java.awt.event.ActionEvent) --> ScegliControParti.cancellaJButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
	 * @param arg1 java.awt.event.ActionEvent
	 */
    private void connEtoC5(java.awt.event.ActionEvent arg1) {
        try {
            this.cancellaJButton_ActionPerformed(arg1);
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    /**
	 * connEtoC6:  (AggiungiAnagrafe.edit_Anagr.annullaJButtonAction_actionPerformed(java.util.EventObject) --> ScegliControParti.aggiungiAnagrafe_AnnullaJButtonAction(Ljava.util.EventObject;)V)
	 * @param arg1 java.util.EventObject
	 */
    private void connEtoC6(java.util.EventObject arg1) {
        try {
            this.aggiungiAnagrafe_AnnullaJButtonAction(arg1);
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    /**
	 * connEtoC7:  (AggiungiAnagrafe.edit_Anagr.okJButtonAction_actionPerformed(java.util.EventObject) --> ScegliControParti.aggiungiAnagrafe_OkJButtonAction(Ljava.util.EventObject;)V)
	 * @param arg1 java.util.EventObject
	 */
    private void connEtoC7(java.util.EventObject arg1) {
        try {
            this.aggiungiAnagrafe_OkJButtonAction(arg1);
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    /**
	 * connEtoC8:  (Ricerca.cerca.okJButtonAction_actionPerformed(java.util.EventObject) --> ScegliControParti.ricerca_OkJButtonAction(Ljava.util.EventObject;)V)
	 * @param arg1 java.util.EventObject
	 */
    private void connEtoC8(java.util.EventObject arg1) {
        try {
            this.ricerca_OkJButtonAction(arg1);
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    /**
	 * connEtoC9:  (Ricerca.cerca.annullaJButtonAction_actionPerformed(java.util.EventObject) --> ScegliControParti.ricerca_AnnullaJButtonAction(Ljava.util.EventObject;)V)
	 * @param arg1 java.util.EventObject
	 */
    private void connEtoC9(java.util.EventObject arg1) {
        try {
            this.ricerca_AnnullaJButtonAction(arg1);
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    /**
	 * connEtoM1:  (ChiudiJButton.action.actionPerformed(java.awt.event.ActionEvent) --> ScegliControParti.dispose()V)
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
	 * Metodo per il supporto di eventi del listener.
	 * @param newEvent java.util.EventObject
	 */
    protected void fireChiudiJButtonAction_actionPerformed(java.util.EventObject newEvent) {
        if (fieldScegliControPartiListenerEventMulticaster == null) {
            return;
        }
        ;
        fieldScegliControPartiListenerEventMulticaster.chiudiJButtonAction_actionPerformed(newEvent);
    }

    /**
	 * Metodo per il supporto di eventi del listener.
	 * @param newEvent java.util.EventObject
	 */
    protected void fireSceltaJButtonAction_actionPerformed(java.util.EventObject newEvent) {
        if (!corretto) {
            return;
        }
        if (fieldScegliControPartiListenerEventMulticaster == null) {
            return;
        }
        ;
        fieldScegliControPartiListenerEventMulticaster.sceltaJButtonAction_actionPerformed(newEvent);
        corretto = false;
    }

    /**
	 * Restituisce il valore della proprieta AggiungeJButton.
	 * @return javax.swing.JButton
	 */
    private javax.swing.JButton getAggiungeJButton() {
        if (ivjAggiungeJButton == null) {
            try {
                ivjAggiungeJButton = new javax.swing.JButton();
                ivjAggiungeJButton.setName("AggiungeJButton");
                ivjAggiungeJButton.setText(Messages.getString("ScegliControParti.Aggiunge_6"));
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjAggiungeJButton;
    }

    /**
	 * Restituisce il valore della proprieta AggiungiAnagrafe.
	 * @return dialogPackage.Edit_Anagr
	 */
    private EditAnagr getAggiungiAnagrafe() {
        if (ivjAggiungiAnagrafe == null) {
            try {
                ivjAggiungiAnagrafe = new dialogPackage.EditAnagr(this);
                ivjAggiungiAnagrafe.setName("AggiungiAnagrafe");
                ivjAggiungiAnagrafe.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjAggiungiAnagrafe;
    }

    /**
	 *
	 */
    private static void getBuilderData() {
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
                getButtonsJPanel().add(getAggiungeJButton(), getAggiungeJButton().getName());
                getButtonsJPanel().add(getModificaJButton(), getModificaJButton().getName());
                ivjButtonsJPanel.add(getCancellaJButton());
                ivjButtonsJPanel.add(getCercaJButton());
                ivjButtonsJPanel.add(getStampaJButton());
                ivjButtonsJPanel.add(getSceltaJButton());
                ivjButtonsJPanel.add(getChiudiJButton());
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjButtonsJPanel;
    }

    /**
	 * Restituisce il valore della proprieta CancellaJButton.
	 * @return javax.swing.JButton
	 */
    private javax.swing.JButton getCancellaJButton() {
        if (ivjCancellaJButton == null) {
            try {
                ivjCancellaJButton = new javax.swing.JButton();
                ivjCancellaJButton.setName("CancellaJButton");
                ivjCancellaJButton.setText(Messages.getString("ScegliControParti.Cancella_10"));
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjCancellaJButton;
    }

    /**
	 * Restituisce il valore della proprieta CercaJButton.
	 * @return javax.swing.JButton
	 */
    private javax.swing.JButton getCercaJButton() {
        if (ivjCercaJButton == null) {
            try {
                ivjCercaJButton = new javax.swing.JButton();
                ivjCercaJButton.setName("CercaJButton");
                ivjCercaJButton.setText(Messages.getString("ScegliControParti.Cerca_12"));
                ivjCercaJButton.setEnabled(true);
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjCercaJButton;
    }

    /**
	 * Restituisce il valore della proprieta ChiudiJButton.
	 * @return javax.swing.JButton
	 */
    private javax.swing.JButton getChiudiJButton() {
        if (ivjChiudiJButton == null) {
            try {
                ivjChiudiJButton = new javax.swing.JButton();
                ivjChiudiJButton.setName("ChiudiJButton");
                ivjChiudiJButton.setText(Messages.getString("ScegliControParti.Chiudi_14"));
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjChiudiJButton;
    }

    /**
	 * Restituisce il valore della proprieta CodiceAnagrafe.
	 * @return java.lang.String
	 */
    private java.lang.String getCodiceAnagrafe() {
        return ivjCodiceAnagrafe;
    }

    /**
	 * Metodo creato per supportare la promozione dell'attributo di codiceAnagrafeThis.
	 * @return java.lang.String
	 */
    public java.lang.String getCodiceAnagrafeThis() {
        return getCodiceAnagrafe();
    }

    /**
	 * Restituisce il valore della proprieta Cognome.
	 * @return java.lang.String
	 */
    private java.lang.String getCognome() {
        return ivjCognome;
    }

    /**
	 * Metodo creato per supportare la promozione dell'attributo di cognomeThis.
	 * @return java.lang.String
	 */
    public java.lang.String getCognomeThis() {
        return getCognome();
    }

    /**
	 * Restituisce il valore della proprieta ControPartiJScrollPane.
	 * @return javax.swing.JScrollPane
	 */
    private javax.swing.JScrollPane getControPartiJScrollPane() {
        if (ivjControPartiJScrollPane == null) {
            try {
                ivjControPartiJScrollPane = new javax.swing.JScrollPane();
                ivjControPartiJScrollPane.setName("ControPartiJScrollPane");
                ivjControPartiJScrollPane.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                ivjControPartiJScrollPane.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
                getControPartiJScrollPane().setViewportView(getScrollPaneTable());
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjControPartiJScrollPane;
    }

    /**
	 * Restituisce il valore della proprieta DataNascita.
	 * @return java.lang.String
	 */
    private java.lang.String getDataNascita() {
        return ivjDataNascita;
    }

    /**
	 * Metodo creato per supportare la promozione dell'attributo di dataNascitaThis.
	 * @return java.lang.String
	 */
    public java.lang.String getDataNascitaThis() {
        return getDataNascita();
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
                getJDialogContentPane().add(getControPartiJScrollPane(), "Center");
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjJDialogContentPane;
    }

    /**
	 * Restituisce il valore della proprieta ModificaJButton.
	 * @return javax.swing.JButton
	 */
    private javax.swing.JButton getModificaJButton() {
        if (ivjModificaJButton == null) {
            try {
                ivjModificaJButton = new javax.swing.JButton();
                ivjModificaJButton.setName("ModificaJButton");
                ivjModificaJButton.setText(Messages.getString("ScegliControParti.Modifica_20"));
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjModificaJButton;
    }

    /**
	 * Restituisce il valore della proprieta Nome.
	 * @return java.lang.String
	 */
    private java.lang.String getNome() {
        return ivjNome;
    }

    /**
	 * Metodo creato per supportare la promozione dell'attributo di nomeThis.
	 * @return java.lang.String
	 */
    public java.lang.String getNomeThis() {
        return getNome();
    }

    /**
	 * Restituisce il valore della proprieta Op.
	 * @return javax.swing.JOptionPane
	 */
    private javax.swing.JOptionPane getOp() {
        if (ivjOp == null) {
            try {
                ivjOp = new javax.swing.JOptionPane();
                ivjOp.setName("Op");
                ivjOp.setMessage(Messages.getString("ScegliControParti.Vuoi_veramente_cancellare_questa_registrazione__22"));
                ivjOp.setMessageType(javax.swing.JOptionPane.QUESTION_MESSAGE);
                ivjOp.setBounds(647, 593, 369, 90);
                ivjOp.setOptionType(javax.swing.JOptionPane.YES_NO_OPTION);
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjOp;
    }

    /**
	 * Restituisce il valore della proprieta Ricerca.
	 * @return dialogPackage.Cerca
	 */
    private Cerca getRicerca() {
        if (ivjRicerca == null) {
            try {
                ivjRicerca = new dialogPackage.Cerca(this);
                ivjRicerca.setName(Messages.getString("ScegliControParti.Ricerca_23"));
                ivjRicerca.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjRicerca;
    }

    /**
	 * Restituisce il valore della proprieta SceltaJButton.
	 * @return javax.swing.JButton
	 */
    private javax.swing.JButton getSceltaJButton() {
        if (ivjSceltaJButton == null) {
            try {
                ivjSceltaJButton = new javax.swing.JButton();
                ivjSceltaJButton.setName("SceltaJButton");
                ivjSceltaJButton.setText(Messages.getString("ScegliControParti.Scelta_25"));
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjSceltaJButton;
    }

    /**
	 * Restituisce il valore della proprieta ScrollPaneTable.
	 * @return mainPackage.MiaJTable
	 */
    private mainPackage.MiaJTable getScrollPaneTable() {
        if (ivjScrollPaneTable == null) {
            try {
                ivjScrollPaneTable = new mainPackage.MiaJTable();
                ivjScrollPaneTable.setName("ScrollPaneTable");
                getControPartiJScrollPane().setColumnHeaderView(ivjScrollPaneTable.getTableHeader());
                getControPartiJScrollPane().getViewport().setScrollMode(JViewport.BACKINGSTORE_SCROLL_MODE);
                ivjScrollPaneTable.setAutoResizeMode(mainPackage.MiaJTable.AUTO_RESIZE_ALL_COLUMNS);
                ivjScrollPaneTable.setBounds(0, 0, 808, 120);
                ivjScrollPaneTable.setAutoCreateColumnsFromModel(false);
                ivjScrollPaneTable.addColumn(getTCCognome());
                ivjScrollPaneTable.addColumn(getTCNome());
                ivjScrollPaneTable.addColumn(getTCNato());
                ListSelectionModel rowSM = ivjScrollPaneTable.getSelectionModel();
                rowSM.addListSelectionListener(new ListSelectionListener() {

                    public void valueChanged(ListSelectionEvent e) {
                        if (e.getValueIsAdjusting()) return;
                        ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                        if (lsm.isSelectionEmpty()) {
                        } else {
                            int selectedRow = lsm.getMinSelectionIndex();
                            try {
                                getAggiungiAnagrafe().setCodiceThis(tabControParti.getCodiceSel(getScrollPaneTable().getSelectedRow()));
                                setCodiceAnagrafe(tabControParti.getCodiceSel(getScrollPaneTable().getSelectedRow()));
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                });
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjScrollPaneTable;
    }

    /**
	 * Restituisce il valore della proprieta StampaJButton.
	 * @return javax.swing.JButton
	 */
    private javax.swing.JButton getStampaJButton() {
        if (ivjStampaJButton == null) {
            try {
                ivjStampaJButton = new javax.swing.JButton();
                ivjStampaJButton.setName("StampaJButton");
                ivjStampaJButton.setText(Messages.getString("ScegliControParti.Stampa_28"));
                ivjStampaJButton.setEnabled(false);
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjStampaJButton;
    }

    /**
	 * Restituisce il valore della proprieta TCCognome.
	 * @return javax.swing.table.TableColumn
	 */
    private javax.swing.table.TableColumn getTCCognome() {
        if (ivjTCCognome == null) {
            try {
                ivjTCCognome = new javax.swing.table.TableColumn();
                ivjTCCognome.setModelIndex(1);
                ivjTCCognome.setHeaderValue(Messages.getString("ScegliControParti.Cognome_29"));
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjTCCognome;
    }

    /**
	 * Restituisce il valore della proprieta TCNato.
	 * @return javax.swing.table.TableColumn
	 */
    private javax.swing.table.TableColumn getTCNato() {
        if (ivjTCNato == null) {
            try {
                ivjTCNato = new javax.swing.table.TableColumn();
                ivjTCNato.setModelIndex(3);
                ivjTCNato.setHeaderValue(Messages.getString("ScegliControParti.Nato_30"));
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjTCNato;
    }

    /**
	 * Restituisce il valore della proprieta TCNome.
	 * @return javax.swing.table.TableColumn
	 */
    private javax.swing.table.TableColumn getTCNome() {
        if (ivjTCNome == null) {
            try {
                ivjTCNome = new javax.swing.table.TableColumn();
                ivjTCNome.setModelIndex(2);
                ivjTCNome.setHeaderValue(Messages.getString("ScegliControParti.Nome_31"));
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjTCNome;
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
        this.addWindowListener(ivjEventHandler);
        getAggiungeJButton().addActionListener(ivjEventHandler);
        getModificaJButton().addActionListener(ivjEventHandler);
        getCancellaJButton().addActionListener(ivjEventHandler);
        getAggiungiAnagrafe().addEdit_AnagrListener(ivjEventHandler);
        getRicerca().addCercaListener(ivjEventHandler);
        getCercaJButton().addActionListener(ivjEventHandler);
        getSceltaJButton().addActionListener(ivjEventHandler);
        getChiudiJButton().addActionListener(ivjEventHandler);
    }

    /**
	 * Inizializzare la classe.
	 */
    private void initialize() {
        try {
            setName("ScegliControParti");
            setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
            setSize(800, 305);
            setModal(true);
            setTitle("Anagrafe Controparti");
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
            ScegliControParti aScegliControParti;
            aScegliControParti = new ScegliControParti();
            aScegliControParti.setModal(true);
            aScegliControParti.addWindowListener(new java.awt.event.WindowAdapter() {

                public void windowClosing(java.awt.event.WindowEvent e) {
                    System.exit(0);
                }

                ;
            });
            aScegliControParti.show();
            java.awt.Insets insets = aScegliControParti.getInsets();
            aScegliControParti.setSize(aScegliControParti.getWidth() + insets.left + insets.right, aScegliControParti.getHeight() + insets.top + insets.bottom);
            aScegliControParti.setVisible(true);
        } catch (Throwable exception) {
            System.err.println("Eccezione verificatasi in main() di javax.swing.JDialog");
            exception.printStackTrace(System.out);
        }
    }

    /**
	 * Comment
	 */
    public void modificaJButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
        try {
            if (getScrollPaneTable().getRowCount() > 0 && getScrollPaneTable().getSelectedRow() != -1) {
                getAggiungiAnagrafe().setButtonFlagThis(new Boolean(false));
                getAggiungiAnagrafe().pulisciTutto();
                getAggiungiAnagrafe().caricaAnagrafe(tabControParti.getCodiceSel(getScrollPaneTable().getSelectedRow()));
                getAggiungiAnagrafe().show();
            }
        } catch (Exception e) {
            mainPackage.CostantiDavide.msgEccezione("Eccezione in modificaJButton di ScegliControParti");
            e.printStackTrace();
        }
        return;
    }

    /**
	 *
	 * @param newListener dialogPackage.ScegliControPartiListener
	 */
    public void removeScegliControPartiListener(dialogPackage.ScegliControPartiListener newListener) {
        fieldScegliControPartiListenerEventMulticaster = dialogPackage.ScegliControPartiListenerEventMulticaster.remove(fieldScegliControPartiListenerEventMulticaster, newListener);
        return;
    }

    /**
	 * Comment
	 */
    public void ricerca_AnnullaJButtonAction(java.util.EventObject newEvent) {
        aggiornaVisual();
        return;
    }

    /**
	 * Comment
	 */
    public void ricerca_OkJButtonAction(java.util.EventObject newEvent) {
        try {
            int i, risultato = 0;
            boolean trovato = false;
            String pattern = getRicerca().getDescrizioneJTextFieldText();
            if (pattern.equals("")) {
                aggiornaVisual();
                return;
            }
            getScrollPaneTable().ricercaValore(pattern, 0, this);
        } catch (Exception e) {
            mainPackage.CostantiDavide.msgEccezione("Eccezione in cerca controparti");
            e.printStackTrace();
        }
        return;
    }

    /**
	 * Comment
	 */
    public void scegliControParti_Initialize() {
        getScrollPaneTable().setSelectionMode(0);
        javax.swing.JButton[] bottoni = { getAggiungeJButton(), getModificaJButton(), getCancellaJButton(), getCercaJButton(), getStampaJButton(), getSceltaJButton(), getChiudiJButton() };
        mainPackage.CostantiDavide.inizBordiPulsanti(bottoni, mainPackage.CostantiDavide.TIPO_AZIONE);
        return;
    }

    /**
	 * Comment
	 */
    public void scegliControParti_WindowOpened(java.awt.event.WindowEvent windowEvent) {
        aggiornaVisual();
        return;
    }

    /**
	 * Comment
	 */
    public void sceltaJButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
        int count = getScrollPaneTable().getRowCount();
        int rigaSel = getScrollPaneTable().getSelectedRow();
        if (count > 0 && rigaSel != -1) {
            setCognome(getScrollPaneTable().getValueAt(getScrollPaneTable().getSelectedRow(), 0).toString());
            setNome(getScrollPaneTable().getValueAt(getScrollPaneTable().getSelectedRow(), 1).toString());
            setDataNascita(getScrollPaneTable().getValueAt(getScrollPaneTable().getSelectedRow(), 2).toString());
            dispose();
            corretto = true;
        }
        return;
    }

    /**
	 * Impostare CodiceAnagrafe su un nuovo valore.
	 * @param newValue java.lang.String
	 */
    private void setCodiceAnagrafe(java.lang.String newValue) {
        if (ivjCodiceAnagrafe != newValue) {
            try {
                String oldValue = getCodiceAnagrafe();
                ivjCodiceAnagrafe = newValue;
                firePropertyChange("nomeThis", oldValue, newValue);
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        ;
    }

    /**
	 * Metodo creato per supportare la promozione dell'attributo di codiceAnagrafeThis.
	 * @param arg1 java.lang.String
	 */
    public void setCodiceAnagrafeThis(java.lang.String arg1) {
        setCodiceAnagrafe(arg1);
    }

    /**
	 * Impostare Cognome su un nuovo valore.
	 * @param newValue java.lang.String
	 */
    private void setCognome(java.lang.String newValue) {
        if (ivjCognome != newValue) {
            try {
                String oldValue = getCognome();
                ivjCognome = newValue;
                firePropertyChange("nomeThis", oldValue, newValue);
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        ;
    }

    /**
	 * Metodo creato per supportare la promozione dell'attributo di cognomeThis.
	 * @param arg1 java.lang.String
	 */
    public void setCognomeThis(java.lang.String arg1) {
        setCognome(arg1);
    }

    /**
	 * Impostare DataNascita su un nuovo valore.
	 * @param newValue java.lang.String
	 */
    private void setDataNascita(java.lang.String newValue) {
        if (ivjDataNascita != newValue) {
            try {
                String oldValue = getDataNascita();
                ivjDataNascita = newValue;
                firePropertyChange("nomeThis", oldValue, newValue);
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        ;
    }

    /**
	 * Metodo creato per supportare la promozione dell'attributo di dataNascitaThis.
	 * @param arg1 java.lang.String
	 */
    public void setDataNascitaThis(java.lang.String arg1) {
        setDataNascita(arg1);
    }

    /**
	 * Impostare Nome su un nuovo valore.
	 * @param newValue java.lang.String
	 */
    private void setNome(java.lang.String newValue) {
        if (ivjNome != newValue) {
            try {
                String oldValue = getNome();
                ivjNome = newValue;
                firePropertyChange("nomeThis", oldValue, newValue);
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        ;
    }

    /**
	 * Metodo creato per supportare la promozione dell'attributo di nomeThis.
	 * @param arg1 java.lang.String
	 */
    public void setNomeThis(java.lang.String arg1) {
        setNome(arg1);
    }
}
