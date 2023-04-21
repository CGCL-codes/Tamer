package dialogPackage;

import java.sql.*;
import mainPackage.*;
import mainPackage.ModelloTabella;
import javax.swing.event.*;
import javax.swing.*;
import dbConnPackage.DBDavide;

/**
 * Inserire qui la descrizione del tipo.
 * Data di creazione: (09/07/2002 12.16.12)
 * @author: PeP1
 */
public class ScegliStato extends MiaJDialog {

    private JButton ivjAggiungeJButton = null;

    private JPanel ivjButtonsJPanel = null;

    private JButton ivjCancellaJButton = null;

    private JButton ivjCercaJButton = null;

    private JButton ivjChiudiJButton = null;

    private JPanel ivjJDialogContentPane = null;

    private JButton ivjModificaJButton = null;

    private JScrollPane ivjOggettoJScrollPane = null;

    private JButton ivjSceltaJButton = null;

    private mainPackage.MiaJTable ivjScrollPaneTable = null;

    private JButton ivjStampaJButton = null;

    private javax.swing.table.TableColumn ivjTCDescrizione = null;

    private EditStato ivjAggiungiStato = null;

    private String ivjDescrizione = null;

    IvjEventHandler ivjEventHandler = new IvjEventHandler();

    private mainPackage.ModelloTabella tabStati;

    protected transient dialogPackage.ScegliStatoListener fieldScegliStatoListenerEventMulticaster = null;

    private JOptionPane ivjOp = null;

    private int ris;

    private String ivjCodiceStato = null;

    private Cerca ivjRicerca = null;

    class IvjEventHandler implements dialogPackage.CercaListener, dialogPackage.EditStatoListener, java.awt.event.ActionListener, java.awt.event.WindowListener {

        public void actionPerformed(java.awt.event.ActionEvent e) {
            if (e.getSource() == ScegliStato.this.getChiudiJButton()) connEtoM8(e);
            if (e.getSource() == ScegliStato.this.getSceltaJButton()) connEtoC9(e);
            if (e.getSource() == ScegliStato.this.getSceltaJButton()) connEtoC4(e);
            if (e.getSource() == ScegliStato.this.getCancellaJButton()) connEtoC5(e);
            if (e.getSource() == ScegliStato.this.getCercaJButton()) connEtoC6(e);
            if (e.getSource() == ScegliStato.this.getAggiungeJButton()) connEtoC11(e);
            if (e.getSource() == ScegliStato.this.getModificaJButton()) connEtoC12(e);
        }

        ;

        public void annullaJButtonAction_actionPerformed(java.util.EventObject newEvent) {
            if (newEvent.getSource() == ScegliStato.this.getAggiungiStato()) connEtoC2(newEvent);
            if (newEvent.getSource() == ScegliStato.this.getRicerca()) connEtoC8(newEvent);
        }

        ;

        public void okJButtonAction_actionPerformed(java.util.EventObject newEvent) {
            if (newEvent.getSource() == ScegliStato.this.getAggiungiStato()) connEtoC3(newEvent);
            if (newEvent.getSource() == ScegliStato.this.getRicerca()) connEtoC7(newEvent);
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
            if (e.getSource() == ScegliStato.this) connEtoC1(e);
        }

        ;
    }

    ;

    /**
 * Commento del constructor ScegliStato.
 */
    public ScegliStato() {
        super();
        initialize();
    }

    /**
 * Commento del constructor ScegliStato.
 * @param owner java.awt.Dialog
 */
    public ScegliStato(java.awt.Dialog owner) {
        super(owner);
        initialize();
    }

    /**
 * Commento del constructor ScegliStato.
 * @param owner java.awt.Dialog
 * @param title java.lang.String
 */
    public ScegliStato(java.awt.Dialog owner, String title) {
        super(owner, title);
        initialize();
    }

    /**
 * Commento del constructor ScegliStato.
 * @param owner java.awt.Dialog
 * @param title java.lang.String
 * @param modal boolean
 */
    public ScegliStato(java.awt.Dialog owner, String title, boolean modal) {
        super(owner, title, modal);
        initialize();
    }

    /**
 * Commento del constructor ScegliStato.
 * @param owner java.awt.Dialog
 * @param modal boolean
 */
    public ScegliStato(java.awt.Dialog owner, boolean modal) {
        super(owner, modal);
        initialize();
    }

    /**
 * Commento del constructor ScegliStato.
 * @param owner java.awt.Frame
 */
    public ScegliStato(java.awt.Frame owner) {
        super(owner);
        initialize();
    }

    /**
 * Commento del constructor ScegliStato.
 * @param owner java.awt.Frame
 * @param title java.lang.String
 */
    public ScegliStato(java.awt.Frame owner, String title) {
        super(owner, title);
        initialize();
    }

    /**
 * Commento del constructor ScegliStato.
 * @param owner java.awt.Frame
 * @param title java.lang.String
 * @param modal boolean
 */
    public ScegliStato(java.awt.Frame owner, String title, boolean modal) {
        super(owner, title, modal);
        initialize();
    }

    /**
 * Commento del constructor ScegliStato.
 * @param owner java.awt.Frame
 * @param modal boolean
 */
    public ScegliStato(java.awt.Frame owner, boolean modal) {
        super(owner, modal);
        initialize();
    }

    /**
 *
 * @param newListener dialogPackage.ScegliStatoListener
 */
    public void addScegliStatoListener(dialogPackage.ScegliStatoListener newListener) {
        fieldScegliStatoListenerEventMulticaster = dialogPackage.ScegliStatoListenerEventMulticaster.add(fieldScegliStatoListenerEventMulticaster, newListener);
        return;
    }

    /**
 * Inserire qui la descrizione del metodo.
 * Data di creazione: (10/04/2002 11.12.37)
 */
    private void aggiornaVisual() {
        mainPackage.CostantiDavide.msgInfo("Aggiorna la visualizzazione di tabStati");
        tabStati.setModel(DBDavide.selStati());
        getScrollPaneTable().setModel(tabStati);
        return;
    }

    /**
 * Alla pressione del tasto aggiungi si apre la finestra EditStao
 * e il campo text associato deve essere vuoto
 */
    public void aggiungeJButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
        getAggiungiStato().getDescrizioneBJTextField().setText("");
        getAggiungiStato().setButtonFlagThis(new Boolean(true));
        getAggiungiStato().show();
        return;
    }

    /**
 * Comment
 */
    public void aggiungiStato_AnnullaJButtonAction(java.util.EventObject newEvent) {
        aggiornaVisual();
        return;
    }

    /**
 * Comment
 */
    public void aggiungiStato_OkJButtonAction(java.util.EventObject newEvent) {
        aggiornaVisual();
        return;
    }

    /**
 * Comment
 */
    public void cancellaJButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
        try {
            if (getScrollPaneTable().getRowCount() > 0 && getScrollPaneTable().getSelectedRow() != -1) {
                ris = JOptionPane.showConfirmDialog(this, getOp().getMessage(), Messages.getString("ScegliStato.Scegliere_3"), JOptionPane.YES_NO_OPTION);
                if (ris == JOptionPane.YES_OPTION) {
                    String codice = tabStati.getCodiceSel(getScrollPaneTable().getSelectedRow());
                    DBDavide.cancellaRecord("Stato", codice);
                }
                aggiornaVisual();
                return;
            }
        } catch (Exception e) {
            mainPackage.CostantiDavide.msgInfo("Eccez.in cancella di ScegliStato()...");
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
 * connEtoC1:  (ScegliStato.window.windowOpened(java.awt.event.WindowEvent) --> ScegliStato.scegliStato_WindowOpened(Ljava.awt.event.WindowEvent;)V)
 * @param arg1 java.awt.event.WindowEvent
 */
    private void connEtoC1(java.awt.event.WindowEvent arg1) {
        try {
            this.scegliStato_WindowOpened(arg1);
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    /**
 * connEtoC10:  (ScegliStato.initialize() --> ScegliStato.scegliStato_Initialize()V)
 */
    private void connEtoC10() {
        try {
            this.scegliStato_Initialize();
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    private void connEtoC11(java.awt.event.ActionEvent arg1) {
        try {
            this.aggiungeJButton_ActionPerformed(arg1);
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    /**
 * connEtoC12:  (ModificaJButton.action.actionPerformed(java.awt.event.ActionEvent) --> ScegliStato.modificaJButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
    private void connEtoC12(java.awt.event.ActionEvent arg1) {
        try {
            this.modificaJButton_ActionPerformed(arg1);
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    /**
 * connEtoC2:  (AggiungiStato.editStato.annullaJButtonAction_actionPerformed(java.util.EventObject) --> ScegliStato.aggiungiStato_AnnullaJButtonAction(Ljava.util.EventObject;)V)
 * @param arg1 java.util.EventObject
 */
    private void connEtoC2(java.util.EventObject arg1) {
        try {
            this.aggiungiStato_AnnullaJButtonAction(arg1);
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    /**
 * connEtoC3:  (AggiungiStato.editStato.okJButtonAction_actionPerformed(java.util.EventObject) --> ScegliStato.aggiungiStato_OkJButtonAction(Ljava.util.EventObject;)V)
 * @param arg1 java.util.EventObject
 */
    private void connEtoC3(java.util.EventObject arg1) {
        try {
            this.aggiungiStato_OkJButtonAction(arg1);
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    /**
 * connEtoC4:  (SceltaJButton.action.actionPerformed(java.awt.event.ActionEvent) --> ScegliStato.fireSceltaJButtonAction_actionPerformed(Ljava.util.EventObject;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
    private void connEtoC4(java.awt.event.ActionEvent arg1) {
        try {
            this.fireSceltaJButtonAction_actionPerformed(new java.util.EventObject(this));
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    /**
 * connEtoC5:  (CancellaJButton.action.actionPerformed(java.awt.event.ActionEvent) --> ScegliStato.cancellaJButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
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
 * connEtoC6:  (CercaJButton.action.actionPerformed(java.awt.event.ActionEvent) --> ScegliStato.cercaJButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
    private void connEtoC6(java.awt.event.ActionEvent arg1) {
        try {
            this.cercaJButton_ActionPerformed(arg1);
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    /**
 * connEtoC7:  (Ricerca.cerca.okJButtonAction_actionPerformed(java.util.EventObject) --> ScegliStato.ricerca_OkJButtonAction(Ljava.util.EventObject;)V)
 * @param arg1 java.util.EventObject
 */
    private void connEtoC7(java.util.EventObject arg1) {
        try {
            this.ricerca_OkJButtonAction(arg1);
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    /**
 * connEtoC8:  (Ricerca.cerca.annullaJButtonAction_actionPerformed(java.util.EventObject) --> ScegliStato.ricerca_AnnullaJButtonAction(Ljava.util.EventObject;)V)
 * @param arg1 java.util.EventObject
 */
    private void connEtoC8(java.util.EventObject arg1) {
        try {
            this.ricerca_AnnullaJButtonAction(arg1);
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    /**
 * connEtoC9:  (SceltaJButton.action.actionPerformed(java.awt.event.ActionEvent) --> ScegliStato.sceltaJButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
    private void connEtoC9(java.awt.event.ActionEvent arg1) {
        try {
            this.sceltaJButton_ActionPerformed(arg1);
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    /**
 * connEtoM8:  (ChiudiJButton.action.actionPerformed(java.awt.event.ActionEvent) --> ScegliStato.dispose()V)
 * @param arg1 java.awt.event.ActionEvent
 */
    private void connEtoM8(java.awt.event.ActionEvent arg1) {
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
    protected void fireSceltaJButtonAction_actionPerformed(java.util.EventObject newEvent) {
        if (fieldScegliStatoListenerEventMulticaster == null) {
            return;
        }
        ;
        fieldScegliStatoListenerEventMulticaster.sceltaJButtonAction_actionPerformed(newEvent);
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
                ivjAggiungeJButton.setText(Messages.getString("ScegliStato.Aggiunge_8"));
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjAggiungeJButton;
    }

    /**
 * Restituisce il valore della proprieta AggiungiStato.
 * @return dialogPackage.EditStato
 */
    private EditStato getAggiungiStato() {
        if (ivjAggiungiStato == null) {
            try {
                ivjAggiungiStato = new dialogPackage.EditStato(this);
                ivjAggiungiStato.setName("AggiungiStato");
                ivjAggiungiStato.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjAggiungiStato;
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
                ivjCancellaJButton.setText(Messages.getString("ScegliStato.Cancella_12"));
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
                ivjCercaJButton.setText(Messages.getString("ScegliStato.Cerca_14"));
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
                ivjChiudiJButton.setText(Messages.getString("ScegliStato.Chiudi_16"));
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjChiudiJButton;
    }

    /**
 * Restituisce il valore della proprieta CodiceStato.
 * @return java.lang.String
 */
    private java.lang.String getCodiceStato() {
        return ivjCodiceStato;
    }

    /**
 * Metodo creato per supportare la promozione dell'attributo di codiceStatoThis.
 * @return java.lang.String
 */
    public java.lang.String getCodiceStatoThis() {
        return getCodiceStato();
    }

    /**
 * Restituisce il valore della proprieta Descrizione.
 * @return java.lang.String
 */
    private java.lang.String getDescrizione() {
        return ivjDescrizione;
    }

    /**
 * Metodo creato per supportare la promozione dell'attributo di descrizioneThis.
 * @return java.lang.String
 */
    public java.lang.String getDescrizioneThis() {
        return getDescrizione();
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
                getJDialogContentPane().add(getOggettoJScrollPane(), "Center");
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
                ivjModificaJButton.setText(Messages.getString("ScegliStato.Modifica_21"));
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjModificaJButton;
    }

    /**
 * Restituisce il valore della proprieta OggettoJScrollPane.
 * @return javax.swing.JScrollPane
 */
    private javax.swing.JScrollPane getOggettoJScrollPane() {
        if (ivjOggettoJScrollPane == null) {
            try {
                ivjOggettoJScrollPane = new javax.swing.JScrollPane();
                ivjOggettoJScrollPane.setName("OggettoJScrollPane");
                getOggettoJScrollPane().setViewportView(getScrollPaneTable());
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjOggettoJScrollPane;
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
                ivjOp.setMessage(Messages.getString("ScegliStato.Vuoi_veramente_cancellare_questa_registrazione__24"));
                ivjOp.setMessageType(javax.swing.JOptionPane.QUESTION_MESSAGE);
                ivjOp.setBounds(579, 551, 369, 90);
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
                ivjRicerca.setName(Messages.getString("ScegliStato.Ricerca_25"));
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
                ivjSceltaJButton.setText(Messages.getString("ScegliStato.Scelta_27"));
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
                getOggettoJScrollPane().setColumnHeaderView(ivjScrollPaneTable.getTableHeader());
                getOggettoJScrollPane().getViewport().setScrollMode(JViewport.BACKINGSTORE_SCROLL_MODE);
                ivjScrollPaneTable.setAutoResizeMode(mainPackage.MiaJTable.AUTO_RESIZE_ALL_COLUMNS);
                ivjScrollPaneTable.setBounds(0, 0, 293, 99);
                ivjScrollPaneTable.setAutoCreateColumnsFromModel(false);
                ivjScrollPaneTable.addColumn(getTCDescrizione());
                ListSelectionModel rowSM = ivjScrollPaneTable.getSelectionModel();
                rowSM.addListSelectionListener(new ListSelectionListener() {

                    public void valueChanged(ListSelectionEvent e) {
                        if (e.getValueIsAdjusting()) return;
                        ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                        if (lsm.isSelectionEmpty()) {
                            mainPackage.CostantiDavide.msgInfo("No rows are selected.");
                        } else {
                            int selectedRow = lsm.getMinSelectionIndex();
                            mainPackage.CostantiDavide.msgInfo("Row " + selectedRow + " is selected.");
                            try {
                                getAggiungiStato().setCodiceThis(tabStati.getCodiceSel(getScrollPaneTable().getSelectedRow()));
                                mainPackage.CostantiDavide.msgInfo(Messages.getString("ScegliStato.Codice_selezionato___32") + getAggiungiStato().getCodiceThis());
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
                ivjStampaJButton.setText(Messages.getString("ScegliStato.Stampa_34"));
                ivjStampaJButton.addActionListener(new java.awt.event.ActionListener() {

                    public void actionPerformed(java.awt.event.ActionEvent e) {
                        stampa();
                    }
                });
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjStampaJButton;
    }

    /**
 * Restituisce il valore della proprieta TCDescrizione.
 * @return javax.swing.table.TableColumn
 */
    private javax.swing.table.TableColumn getTCDescrizione() {
        if (ivjTCDescrizione == null) {
            try {
                ivjTCDescrizione = new javax.swing.table.TableColumn();
                ivjTCDescrizione.setModelIndex(1);
                ivjTCDescrizione.setHeaderValue(Messages.getString("ScegliStato.Descrizione_35"));
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjTCDescrizione;
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
        getChiudiJButton().addActionListener(ivjEventHandler);
        this.addWindowListener(ivjEventHandler);
        getAggiungiStato().addEditStatoListener(ivjEventHandler);
        getSceltaJButton().addActionListener(ivjEventHandler);
        getCancellaJButton().addActionListener(ivjEventHandler);
        getCercaJButton().addActionListener(ivjEventHandler);
        getRicerca().addCercaListener(ivjEventHandler);
        getAggiungeJButton().addActionListener(ivjEventHandler);
        getModificaJButton().addActionListener(ivjEventHandler);
    }

    /**
 * Inizializzare la classe.
 */
    private void initialize() {
        try {
            setName("ScegliStato");
            setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
            setSize(800, 238);
            setModal(true);
            setTitle(Messages.getString("ScegliStato.Stato_Pratiche_37"));
            setContentPane(getJDialogContentPane());
            initConnections();
            connEtoC10();
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
            ScegliStato aScegliStato;
            aScegliStato = new ScegliStato();
            aScegliStato.setModal(true);
            aScegliStato.addWindowListener(new java.awt.event.WindowAdapter() {

                public void windowClosing(java.awt.event.WindowEvent e) {
                    System.exit(0);
                }

                ;
            });
            aScegliStato.show();
            java.awt.Insets insets = aScegliStato.getInsets();
            aScegliStato.setSize(aScegliStato.getWidth() + insets.left + insets.right, aScegliStato.getHeight() + insets.top + insets.bottom);
            aScegliStato.setVisible(true);
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
            int count = getScrollPaneTable().getRowCount();
            int rigaSel = getScrollPaneTable().getSelectedRow();
            if (count > 0 && rigaSel != -1) {
                String descrizione = getScrollPaneTable().getValueAt(rigaSel, 0).toString();
                getAggiungiStato().setDescrizioneBJTextFieldText(descrizione);
                getAggiungiStato().setButtonFlagThis(new Boolean(false));
                getAggiungiStato().show();
            }
        } catch (Exception e) {
            mainPackage.CostantiDavide.msgInfo("Eccez. in modifica di ScegliStato");
            e.printStackTrace();
        }
        return;
    }

    /**
 *
 * @param newListener dialogPackage.ScegliStatoListener
 */
    public void removeScegliStatoListener(dialogPackage.ScegliStatoListener newListener) {
        fieldScegliStatoListenerEventMulticaster = dialogPackage.ScegliStatoListenerEventMulticaster.remove(fieldScegliStatoListenerEventMulticaster, newListener);
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
            mainPackage.CostantiDavide.msgEccezione("Eccezione in cerca stato");
            e.printStackTrace();
        }
        return;
    }

    /**
 * Comment
 */
    public void scegliStato_Initialize() {
        getScrollPaneTable().setSelectionMode(0);
        javax.swing.JButton[] bottoni = { getAggiungeJButton(), getModificaJButton(), getCancellaJButton(), getCercaJButton(), getStampaJButton(), getSceltaJButton(), getChiudiJButton() };
        mainPackage.CostantiDavide.inizBordiPulsanti(bottoni, mainPackage.CostantiDavide.TIPO_AZIONE);
        return;
    }

    /**
 * Comment
 */
    public void scegliStato_WindowOpened(java.awt.event.WindowEvent windowEvent) {
        tabStati = new ModelloTabella(DBDavide.selStati());
        getScrollPaneTable().setModel(tabStati);
        this.repaint();
        return;
    }

    /**
 * Comment
 */
    public void sceltaJButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
        if (getScrollPaneTable().getRowCount() > 0 && getScrollPaneTable().getSelectedRow() != -1) {
            setDescrizione(getScrollPaneTable().getValueAt(getScrollPaneTable().getSelectedRow(), 0).toString());
            setCodiceStato(tabStati.getCodiceSel(getScrollPaneTable().getSelectedRow()));
            dispose();
        }
        return;
    }

    /**
 * Impostare CodiceStato su un nuovo valore.
 * @param newValue java.lang.String
 */
    private void setCodiceStato(java.lang.String newValue) {
        if (ivjCodiceStato != newValue) {
            try {
                String oldValue = getCodiceStato();
                ivjCodiceStato = newValue;
                firePropertyChange("codiceStatoThis", oldValue, newValue);
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        ;
    }

    /**
 * Metodo creato per supportare la promozione dell'attributo di codiceStatoThis.
 * @param arg1 java.lang.String
 */
    public void setCodiceStatoThis(java.lang.String arg1) {
        setCodiceStato(arg1);
    }

    /**
 * Impostare Descrizione su un nuovo valore.
 * @param newValue java.lang.String
 */
    private void setDescrizione(java.lang.String newValue) {
        if (ivjDescrizione != newValue) {
            try {
                String oldValue = getDescrizione();
                ivjDescrizione = newValue;
                firePropertyChange("codiceStatoThis", oldValue, newValue);
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        ;
    }

    /**
 * Metodo creato per supportare la promozione dell'attributo di descrizioneThis.
 * @param arg1 java.lang.String
 */
    public void setDescrizioneThis(java.lang.String arg1) {
        setDescrizione(arg1);
    }

    public void stampa() {
        if (tabStati != null) {
            reportPackage.FreeRep fr = new reportPackage.FreeRep(this, tabStati, "PraticaStato");
        }
    }
}
