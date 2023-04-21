package dialogPackage;

import javax.swing.JViewport;
import mainPackage.*;

/**
 * Inserire qui la descrizione del tipo.
 * Data di creazione: (04/11/2002 15.32.26)
 * @author: PeP1
 */
public class ScegliFormulario extends MiaJDialog {

    private javax.swing.JPanel ivjJDialogContentPane = null;

    private javax.swing.JButton ivjAggiungeJButton = null;

    private javax.swing.JPanel ivjButtonsJPanel = null;

    private javax.swing.JButton ivjCancellaJButton = null;

    private javax.swing.JButton ivjCercaJButton = null;

    private javax.swing.JButton ivjChiudiJButton = null;

    private javax.swing.table.TableColumn ivjDescrizioneTC = null;

    private javax.swing.JScrollPane ivjJScrollPane1 = null;

    private javax.swing.JButton ivjModificaJButton = null;

    private javax.swing.JButton ivjSceltaJButton = null;

    private mainPackage.MiaJTable ivjScrollPaneTable = null;

    private javax.swing.JButton ivjStampaJButton = null;

    private EditFormulario ivjAggiungiFormulario = null;

    IvjEventHandler ivjEventHandler = new IvjEventHandler();

    private mainPackage.ModelloTabella tabFormulari;

    class IvjEventHandler implements dialogPackage.EditFormularioListener, java.awt.event.ActionListener, java.awt.event.WindowListener {

        public void actionPerformed(java.awt.event.ActionEvent e) {
            if (e.getSource() == ScegliFormulario.this.getAggiungeJButton()) connEtoC2(e);
            if (e.getSource() == ScegliFormulario.this.getModificaJButton()) connEtoC3(e);
            if (e.getSource() == ScegliFormulario.this.getChiudiJButton()) connEtoM1(e);
            if (e.getSource() == ScegliFormulario.this.getSceltaJButton()) connEtoC6(e);
            if (e.getSource() == ScegliFormulario.this.getCancellaJButton()) connEtoC7(e);
        }

        ;

        public void annullaJButtonAction_actionPerformed(java.util.EventObject newEvent) {
            if (newEvent.getSource() == ScegliFormulario.this.getAggiungiFormulario()) connEtoC8(newEvent);
        }

        ;

        public void okJButtonAction_actionPerformed(java.util.EventObject newEvent) {
            if (newEvent.getSource() == ScegliFormulario.this.getAggiungiFormulario()) connEtoC9(newEvent);
        }

        ;

        public void windowActivated(java.awt.event.WindowEvent e) {
            if (e.getSource() == ScegliFormulario.this) connEtoC4(e);
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
            if (e.getSource() == ScegliFormulario.this) connEtoC5(e);
        }

        ;
    }

    ;

    /**
 * Commento del constructor ScegliFormulario.
 */
    public ScegliFormulario() {
        super();
        initialize();
    }

    /**
 * Commento del constructor ScegliFormulario.
 * @param owner java.awt.Dialog
 */
    public ScegliFormulario(java.awt.Dialog owner) {
        super(owner);
        initialize();
    }

    /**
 * Commento del constructor ScegliFormulario.
 * @param owner java.awt.Dialog
 * @param title java.lang.String
 */
    public ScegliFormulario(java.awt.Dialog owner, String title) {
        super(owner, title);
        initialize();
    }

    /**
 * Commento del constructor ScegliFormulario.
 * @param owner java.awt.Dialog
 * @param title java.lang.String
 * @param modal boolean
 */
    public ScegliFormulario(java.awt.Dialog owner, String title, boolean modal) {
        super(owner, title, modal);
        initialize();
    }

    /**
 * Commento del constructor ScegliFormulario.
 * @param owner java.awt.Dialog
 * @param modal boolean
 */
    public ScegliFormulario(java.awt.Dialog owner, boolean modal) {
        super(owner, modal);
        initialize();
    }

    /**
 * Commento del constructor ScegliFormulario.
 * @param owner java.awt.Frame
 */
    public ScegliFormulario(java.awt.Frame owner) {
        super(owner);
        initialize();
    }

    /**
 * Commento del constructor ScegliFormulario.
 * @param owner java.awt.Frame
 * @param title java.lang.String
 */
    public ScegliFormulario(java.awt.Frame owner, String title) {
        super(owner, title);
        initialize();
    }

    /**
 * Commento del constructor ScegliFormulario.
 * @param owner java.awt.Frame
 * @param title java.lang.String
 * @param modal boolean
 */
    public ScegliFormulario(java.awt.Frame owner, String title, boolean modal) {
        super(owner, title, modal);
        initialize();
    }

    /**
 * Commento del constructor ScegliFormulario.
 * @param owner java.awt.Frame
 * @param modal boolean
 */
    public ScegliFormulario(java.awt.Frame owner, boolean modal) {
        super(owner, modal);
        initialize();
    }

    /**
 * Inserire qui la descrizione del metodo.
 * Data di creazione: (09/12/2002 12.54.48)
 */
    private void aggiornaVisual() {
        try {
            if (tabFormulari == null) {
                tabFormulari = new mainPackage.ModelloTabella(dbConnPackage.DBDavide.selFormulari());
            } else {
                tabFormulari.setModel(dbConnPackage.DBDavide.selFormulari());
            }
            getScrollPaneTable().setModel(tabFormulari);
        } catch (Exception e) {
            mainPackage.CostantiDavide.msgEccezione("Eccezione in aggiornaVisual() di ScegliFormulario");
            e.printStackTrace();
        }
    }

    /**
 * Comment
 */
    public void aggiungeJButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
        getAggiungiFormulario().setButtonFlagThis(new Boolean(true));
        getAggiungiFormulario().pulisciTutto();
        getAggiungiFormulario().show();
        return;
    }

    /**
 * Comment
 */
    public void aggiungiFormulario_AnnullaJButtonAction(java.util.EventObject newEvent) {
        aggiornaVisual();
        return;
    }

    /**
 * Comment
 */
    public void aggiungiFormulario_OkJButtonAction(java.util.EventObject newEvent) {
        aggiornaVisual();
        return;
    }

    /**
 * Comment
 */
    public void cancellaJButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
        try {
            int rigaSel = getScrollPaneTable().getSelectedRow();
            int count = getScrollPaneTable().getRowCount();
            if (count > 0 && rigaSel != -1) {
                int ris = javax.swing.JOptionPane.showConfirmDialog(this, Messages.getString("ScegliFormulario.Sei_sicuro_di_voler_cancellare_questa_registrazione__2"), Messages.getString("ScegliFormulario.Scegliere_3"), javax.swing.JOptionPane.YES_NO_OPTION);
                if (ris == javax.swing.JOptionPane.YES_OPTION) {
                    String codice = tabFormulari.getCodiceSel(rigaSel);
                    dbConnPackage.DBDavide.cancellaRecord("Formul", codice);
                }
                aggiornaVisual();
                return;
            }
        } catch (Exception e) {
            mainPackage.CostantiDavide.msgInfo("Eccez. in cancella di ScegliFormulario().....");
            e.printStackTrace();
        }
        return;
    }

    /**
 * connEtoC1:  (ScegliFormulario.initialize() --> ScegliFormulario.scegliFormulario_Initialize()V)
 */
    private void connEtoC1() {
        try {
            this.scegliFormulario_Initialize();
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    /**
 * connEtoC2:  (AggiungeJButton.action.actionPerformed(java.awt.event.ActionEvent) --> ScegliFormulario.aggiungeJButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
    private void connEtoC2(java.awt.event.ActionEvent arg1) {
        try {
            this.aggiungeJButton_ActionPerformed(arg1);
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    /**
 * connEtoC3:  (ModificaJButton.action.actionPerformed(java.awt.event.ActionEvent) --> ScegliFormulario.modificaJButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
    private void connEtoC3(java.awt.event.ActionEvent arg1) {
        try {
            this.modificaJButton_ActionPerformed(arg1);
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    /**
 * connEtoC4:  (ScegliFormulario.window.windowActivated(java.awt.event.WindowEvent) --> ScegliFormulario.scegliFormulario_WindowActivated(Ljava.awt.event.WindowEvent;)V)
 * @param arg1 java.awt.event.WindowEvent
 */
    private void connEtoC4(java.awt.event.WindowEvent arg1) {
        try {
            this.scegliFormulario_WindowActivated(arg1);
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    /**
 * connEtoC5:  (ScegliFormulario.window.windowOpened(java.awt.event.WindowEvent) --> ScegliFormulario.scegliFormulario_WindowOpened(Ljava.awt.event.WindowEvent;)V)
 * @param arg1 java.awt.event.WindowEvent
 */
    private void connEtoC5(java.awt.event.WindowEvent arg1) {
        try {
            this.scegliFormulario_WindowOpened(arg1);
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    /**
 * connEtoC6:  (SceltaJButton.action.actionPerformed(java.awt.event.ActionEvent) --> ScegliFormulario.sceltaJButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
    private void connEtoC6(java.awt.event.ActionEvent arg1) {
        try {
            this.sceltaJButton_ActionPerformed(arg1);
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    /**
 * connEtoC7:  (CancellaJButton.action.actionPerformed(java.awt.event.ActionEvent) --> ScegliFormulario.cancellaJButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
    private void connEtoC7(java.awt.event.ActionEvent arg1) {
        try {
            this.cancellaJButton_ActionPerformed(arg1);
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    /**
 * connEtoC8:  (AggiungiFormulario.editFormulario.annullaJButtonAction_actionPerformed(java.util.EventObject) --> ScegliFormulario.aggiungiFormulario_AnnullaJButtonAction(Ljava.util.EventObject;)V)
 * @param arg1 java.util.EventObject
 */
    private void connEtoC8(java.util.EventObject arg1) {
        try {
            this.aggiungiFormulario_AnnullaJButtonAction(arg1);
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    /**
 * connEtoC9:  (AggiungiFormulario.editFormulario.okJButtonAction_actionPerformed(java.util.EventObject) --> ScegliFormulario.aggiungiFormulario_OkJButtonAction(Ljava.util.EventObject;)V)
 * @param arg1 java.util.EventObject
 */
    private void connEtoC9(java.util.EventObject arg1) {
        try {
            this.aggiungiFormulario_OkJButtonAction(arg1);
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    /**
 * connEtoM1:  (ChiudiJButton.action.actionPerformed(java.awt.event.ActionEvent) --> ScegliFormulario.dispose()V)
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
 * Restituisce il valore della proprieta AggiungeJButton.
 * @return javax.swing.JButton
 */
    private javax.swing.JButton getAggiungeJButton() {
        if (ivjAggiungeJButton == null) {
            try {
                ivjAggiungeJButton = new javax.swing.JButton();
                ivjAggiungeJButton.setName("AggiungeJButton");
                ivjAggiungeJButton.setText(Messages.getString("ScegliFormulario.Aggiunge_7"));
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjAggiungeJButton;
    }

    /**
 * Restituisce il valore della proprieta AggiungiFormulario.
 * @return dialogPackage.EditFormulario
 */
    private EditFormulario getAggiungiFormulario() {
        if (ivjAggiungiFormulario == null) {
            try {
                ivjAggiungiFormulario = new dialogPackage.EditFormulario(this);
                ivjAggiungiFormulario.setName("AggiungiFormulario");
                ivjAggiungiFormulario.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjAggiungiFormulario;
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
                ivjCancellaJButton.setText(Messages.getString("ScegliFormulario.Cancella_11"));
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
                ivjCercaJButton.setText(Messages.getString("ScegliFormulario.Cerca_13"));
                ivjCercaJButton.setEnabled(false);
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
                ivjChiudiJButton.setText(Messages.getString("ScegliFormulario.Chiudi_15"));
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjChiudiJButton;
    }

    /**
 * Restituisce il valore della proprieta DescrizioneTC.
 * @return javax.swing.table.TableColumn
 */
    private javax.swing.table.TableColumn getDescrizioneTC() {
        if (ivjDescrizioneTC == null) {
            try {
                ivjDescrizioneTC = new javax.swing.table.TableColumn();
                ivjDescrizioneTC.setModelIndex(1);
                ivjDescrizioneTC.setHeaderValue(Messages.getString("ScegliFormulario.Descrizione_16"));
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjDescrizioneTC;
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
                getJDialogContentPane().add(getJScrollPane1(), "Center");
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjJDialogContentPane;
    }

    /**
 * Restituisce il valore della proprieta JScrollPane1.
 * @return javax.swing.JScrollPane
 */
    private javax.swing.JScrollPane getJScrollPane1() {
        if (ivjJScrollPane1 == null) {
            try {
                ivjJScrollPane1 = new javax.swing.JScrollPane();
                ivjJScrollPane1.setName("JScrollPane1");
                ivjJScrollPane1.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                ivjJScrollPane1.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
                getJScrollPane1().setViewportView(getScrollPaneTable());
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjJScrollPane1;
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
                ivjModificaJButton.setText(Messages.getString("ScegliFormulario.Modifica_22"));
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjModificaJButton;
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
                ivjSceltaJButton.setText(Messages.getString("ScegliFormulario.Scelta_24"));
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
                getJScrollPane1().setColumnHeaderView(ivjScrollPaneTable.getTableHeader());
                getJScrollPane1().getViewport().setScrollMode(JViewport.BACKINGSTORE_SCROLL_MODE);
                ivjScrollPaneTable.setBounds(0, 0, 200, 200);
                ivjScrollPaneTable.setAutoCreateColumnsFromModel(false);
                ivjScrollPaneTable.addColumn(getDescrizioneTC());
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
                ivjStampaJButton.setText(Messages.getString("ScegliFormulario.Stampa_27"));
                ivjStampaJButton.setEnabled(false);
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjStampaJButton;
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
        getAggiungeJButton().addActionListener(ivjEventHandler);
        getModificaJButton().addActionListener(ivjEventHandler);
        this.addWindowListener(ivjEventHandler);
        getChiudiJButton().addActionListener(ivjEventHandler);
        getSceltaJButton().addActionListener(ivjEventHandler);
        getCancellaJButton().addActionListener(ivjEventHandler);
        getAggiungiFormulario().addEditFormularioListener(ivjEventHandler);
    }

    /**
 * Inizializzare la classe.
 */
    private void initialize() {
        try {
            setName(Messages.getString("ScegliFormulario.ScegliFormulario_28"));
            setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
            setSize(800, 246);
            setModal(true);
            setTitle(Messages.getString("ScegliFormulario.Formulari_29"));
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
            ScegliFormulario aScegliFormulario;
            aScegliFormulario = new ScegliFormulario();
            aScegliFormulario.setModal(true);
            aScegliFormulario.addWindowListener(new java.awt.event.WindowAdapter() {

                public void windowClosing(java.awt.event.WindowEvent e) {
                    System.exit(0);
                }

                ;
            });
            aScegliFormulario.show();
            java.awt.Insets insets = aScegliFormulario.getInsets();
            aScegliFormulario.setSize(aScegliFormulario.getWidth() + insets.left + insets.right, aScegliFormulario.getHeight() + insets.top + insets.bottom);
            aScegliFormulario.setVisible(true);
        } catch (Throwable exception) {
            System.err.println("Eccezione verificatasi in main() di javax.swing.JDialog");
            exception.printStackTrace(System.out);
        }
    }

    /**
 * Comment
 */
    public void modificaJButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
        int rigaSel = getScrollPaneTable().getSelectedRow();
        if (rigaSel != -1) {
            getAggiungiFormulario().setButtonFlagThis(new Boolean(false));
            getAggiungiFormulario().caricaFormulario(tabFormulari.getCodiceSel(rigaSel));
            getAggiungiFormulario().show();
        }
        return;
    }

    /**
 * Comment
 */
    public void scegliFormulario_Initialize() {
        getScrollPaneTable().setSelectionMode(0);
        javax.swing.JButton[] b_azione = { getAggiungeJButton(), getModificaJButton(), getCancellaJButton(), getCercaJButton(), getStampaJButton(), getSceltaJButton(), getChiudiJButton() };
        mainPackage.CostantiDavide.inizBordiPulsanti(b_azione, mainPackage.CostantiDavide.TIPO_AZIONE);
        return;
    }

    /**
 * Comment
 */
    public void scegliFormulario_WindowActivated(java.awt.event.WindowEvent windowEvent) {
        aggiornaVisual();
        return;
    }

    /**
 * Comment
 */
    public void scegliFormulario_WindowOpened(java.awt.event.WindowEvent windowEvent) {
        aggiornaVisual();
        return;
    }

    /**
 * Comment
 */
    public void sceltaJButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
        try {
            dispose();
        } catch (Exception e) {
            mainPackage.CostantiDavide.msgEccezione("Eccezione in scelta... di ScegliFormulario");
            e.printStackTrace();
        }
        return;
    }
}
