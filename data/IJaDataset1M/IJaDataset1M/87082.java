package dialogPackage;

import javax.swing.JViewport;
import mainPackage.*;

/**
 * Inserire qui la descrizione del tipo.
 * Data di creazione: (31/07/2002 14.05.14)
 * @author: PeP1
 */
public class ScegliCompetenze extends MiaJDialog {

    private javax.swing.JPanel ivjButtonsJPanel = null;

    private javax.swing.JButton ivjChiudiJButton = null;

    private javax.swing.JPanel ivjJDialogContentPane = null;

    private javax.swing.JButton ivjSceltaJButton = null;

    private javax.swing.JScrollPane ivjJScrollPane = null;

    private mainPackage.MiaJTable ivjScrollPaneTable = null;

    private javax.swing.table.TableColumn ivjTCDescrizione = null;

    private javax.swing.table.TableColumn ivjTCRif = null;

    public java.util.Vector descrCompetenze;

    public java.util.Vector codiciCompetenze;

    public mainPackage.ModelloTabella tabCompetenze;

    IvjEventHandler ivjEventHandler = new IvjEventHandler();

    protected transient dialogPackage.ScegliCompetenzeListener fieldScegliCompetenzeListenerEventMulticaster = null;

    class IvjEventHandler implements java.awt.event.ActionListener, java.awt.event.WindowListener {

        public void actionPerformed(java.awt.event.ActionEvent e) {
            if (e.getSource() == ScegliCompetenze.this.getSceltaJButton()) connEtoC3(e);
            if (e.getSource() == ScegliCompetenze.this.getChiudiJButton()) connEtoM1(e);
            if (e.getSource() == ScegliCompetenze.this.getSceltaJButton()) connEtoC4(e);
            if (e.getSource() == ScegliCompetenze.this.getChiudiJButton()) connEtoC5(e);
        }

        ;

        public void windowActivated(java.awt.event.WindowEvent e) {
            if (e.getSource() == ScegliCompetenze.this) connEtoC2(e);
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
        }

        ;
    }

    ;

    /**
 * Commento del constructor ScegliCompetenze.
 */
    public ScegliCompetenze() {
        super();
        initialize();
    }

    /**
 * Commento del constructor ScegliCompetenze.
 * @param owner java.awt.Dialog
 */
    public ScegliCompetenze(java.awt.Dialog owner) {
        super(owner);
        initialize();
    }

    /**
 * Commento del constructor ScegliCompetenze.
 * @param owner java.awt.Dialog
 * @param title java.lang.String
 */
    public ScegliCompetenze(java.awt.Dialog owner, String title) {
        super(owner, title);
        initialize();
    }

    /**
 * Commento del constructor ScegliCompetenze.
 * @param owner java.awt.Dialog
 * @param title java.lang.String
 * @param modal boolean
 */
    public ScegliCompetenze(java.awt.Dialog owner, String title, boolean modal) {
        super(owner, title, modal);
        initialize();
    }

    /**
 * Commento del constructor ScegliCompetenze.
 * @param owner java.awt.Dialog
 * @param modal boolean
 */
    public ScegliCompetenze(java.awt.Dialog owner, boolean modal) {
        super(owner, modal);
        initialize();
    }

    /**
 * Commento del constructor ScegliCompetenze.
 * @param owner java.awt.Frame
 */
    public ScegliCompetenze(java.awt.Frame owner) {
        super(owner);
        initialize();
    }

    /**
 * Commento del constructor ScegliCompetenze.
 * @param owner java.awt.Frame
 * @param title java.lang.String
 */
    public ScegliCompetenze(java.awt.Frame owner, String title) {
        super(owner, title);
        initialize();
    }

    /**
 * Commento del constructor ScegliCompetenze.
 * @param owner java.awt.Frame
 * @param title java.lang.String
 * @param modal boolean
 */
    public ScegliCompetenze(java.awt.Frame owner, String title, boolean modal) {
        super(owner, title, modal);
        initialize();
    }

    /**
 * Commento del constructor ScegliCompetenze.
 * @param owner java.awt.Frame
 * @param modal boolean
 */
    public ScegliCompetenze(java.awt.Frame owner, boolean modal) {
        super(owner, modal);
        initialize();
    }

    /**
 *
 * @param newListener dialogPackage.ScegliCompetenzeListener
 */
    public void addScegliCompetenzeListener(dialogPackage.ScegliCompetenzeListener newListener) {
        fieldScegliCompetenzeListenerEventMulticaster = dialogPackage.ScegliCompetenzeListenerEventMulticaster.add(fieldScegliCompetenzeListenerEventMulticaster, newListener);
        return;
    }

    /**
 * connEtoC1:  (ScegliCompetenze.initialize() --> ScegliCompetenze.scegliCompetenze_Initialize()V)
 */
    private void connEtoC1() {
        try {
            this.scegliCompetenze_Initialize();
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    /**
 * connEtoC2:  (ScegliCompetenze.window.windowActivated(java.awt.event.WindowEvent) --> ScegliCompetenze.scegliCompetenze_WindowActivated(Ljava.awt.event.WindowEvent;)V)
 * @param arg1 java.awt.event.WindowEvent
 */
    private void connEtoC2(java.awt.event.WindowEvent arg1) {
        try {
            this.scegliCompetenze_WindowActivated(arg1);
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    /**
 * connEtoC3:  (SceltaJButton.action.actionPerformed(java.awt.event.ActionEvent) --> ScegliCompetenze.sceltaJButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
    private void connEtoC3(java.awt.event.ActionEvent arg1) {
        try {
            this.sceltaJButton_ActionPerformed(arg1);
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    /**
 * connEtoC4:  (SceltaJButton.action.actionPerformed(java.awt.event.ActionEvent) --> ScegliCompetenze.fireSceltaJButtonAction_actionPerformed(Ljava.util.EventObject;)V)
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
 * connEtoC5:  (ChiudiJButton.action.actionPerformed(java.awt.event.ActionEvent) --> ScegliCompetenze.fireChiudiJButtonAction_actionPerformed(Ljava.util.EventObject;)V)
 * @param arg1 java.awt.event.ActionEvent
 */
    private void connEtoC5(java.awt.event.ActionEvent arg1) {
        try {
            this.fireChiudiJButtonAction_actionPerformed(new java.util.EventObject(this));
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    /**
 * connEtoM1:  (ChiudiJButton.action.actionPerformed(java.awt.event.ActionEvent) --> ScegliCompetenze.dispose()V)
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
        if (fieldScegliCompetenzeListenerEventMulticaster == null) {
            return;
        }
        ;
        fieldScegliCompetenzeListenerEventMulticaster.chiudiJButtonAction_actionPerformed(newEvent);
    }

    /**
 * Metodo per il supporto di eventi del listener.
 * @param newEvent java.util.EventObject
 */
    protected void fireSceltaJButtonAction_actionPerformed(java.util.EventObject newEvent) {
        if (fieldScegliCompetenzeListenerEventMulticaster == null) {
            return;
        }
        ;
        fieldScegliCompetenzeListenerEventMulticaster.sceltaJButtonAction_actionPerformed(newEvent);
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
                getButtonsJPanel().add(getSceltaJButton(), getSceltaJButton().getName());
                ivjButtonsJPanel.add(getChiudiJButton());
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjButtonsJPanel;
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
                ivjChiudiJButton.setText(Messages.getString("ScegliCompetenze.Chiudi_3"));
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjChiudiJButton;
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
                getJDialogContentPane().add(getJScrollPane(), "Center");
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjJDialogContentPane;
    }

    /**
 * Restituisce il valore della proprieta JScrollPane.
 * @return javax.swing.JScrollPane
 */
    private javax.swing.JScrollPane getJScrollPane() {
        if (ivjJScrollPane == null) {
            try {
                ivjJScrollPane = new javax.swing.JScrollPane();
                ivjJScrollPane.setName("JScrollPane");
                ivjJScrollPane.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                ivjJScrollPane.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
                getJScrollPane().setViewportView(getScrollPaneTable());
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjJScrollPane;
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
                ivjSceltaJButton.setText(Messages.getString("ScegliCompetenze.Scelta_9"));
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
                getJScrollPane().setColumnHeaderView(ivjScrollPaneTable.getTableHeader());
                getJScrollPane().getViewport().setScrollMode(JViewport.BACKINGSTORE_SCROLL_MODE);
                ivjScrollPaneTable.setAutoResizeMode(mainPackage.MiaJTable.AUTO_RESIZE_LAST_COLUMN);
                ivjScrollPaneTable.setBounds(0, 0, 200, 200);
                ivjScrollPaneTable.setAutoCreateColumnsFromModel(false);
                ivjScrollPaneTable.addColumn(getTCRif());
                ivjScrollPaneTable.addColumn(getTCDescrizione());
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjScrollPaneTable;
    }

    /**
 * Restituisce il valore della proprieta TCDescrizione.
 * @return javax.swing.table.TableColumn
 */
    private javax.swing.table.TableColumn getTCDescrizione() {
        if (ivjTCDescrizione == null) {
            try {
                ivjTCDescrizione = new javax.swing.table.TableColumn();
                ivjTCDescrizione.setModelIndex(2);
                ivjTCDescrizione.setHeaderValue(Messages.getString("ScegliCompetenze.Descrizione_11"));
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjTCDescrizione;
    }

    /**
 * Restituisce il valore della proprieta TCRif.
 * @return javax.swing.table.TableColumn
 */
    private javax.swing.table.TableColumn getTCRif() {
        if (ivjTCRif == null) {
            try {
                ivjTCRif = new javax.swing.table.TableColumn();
                ivjTCRif.setModelIndex(1);
                ivjTCRif.setWidth(100);
                ivjTCRif.setHeaderValue(Messages.getString("ScegliCompetenze.Rif._12"));
                ivjTCRif.setMaxWidth(100);
                ivjTCRif.setMinWidth(100);
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjTCRif;
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
        getSceltaJButton().addActionListener(ivjEventHandler);
        getChiudiJButton().addActionListener(ivjEventHandler);
    }

    /**
 * Inizializzare la classe.
 */
    private void initialize() {
        try {
            setName("ScegliCompetenze");
            setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
            setSize(564, 349);
            setModal(true);
            setTitle(Messages.getString("ScegliCompetenze.Competenze_14"));
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
            ScegliCompetenze aScegliCompetenze;
            aScegliCompetenze = new ScegliCompetenze();
            aScegliCompetenze.setModal(true);
            aScegliCompetenze.addWindowListener(new java.awt.event.WindowAdapter() {

                public void windowClosing(java.awt.event.WindowEvent e) {
                    System.exit(0);
                }

                ;
            });
            aScegliCompetenze.show();
            java.awt.Insets insets = aScegliCompetenze.getInsets();
            aScegliCompetenze.setSize(aScegliCompetenze.getWidth() + insets.left + insets.right, aScegliCompetenze.getHeight() + insets.top + insets.bottom);
            aScegliCompetenze.setVisible(true);
        } catch (Throwable exception) {
            System.err.println("Eccezione verificatasi in main() di javax.swing.JDialog");
            exception.printStackTrace(System.out);
        }
    }

    /**
 *
 * @param newListener dialogPackage.ScegliCompetenzeListener
 */
    public void removeScegliCompetenzeListener(dialogPackage.ScegliCompetenzeListener newListener) {
        fieldScegliCompetenzeListenerEventMulticaster = dialogPackage.ScegliCompetenzeListenerEventMulticaster.remove(fieldScegliCompetenzeListenerEventMulticaster, newListener);
        return;
    }

    /**
 * Comment
 */
    public void scegliCompetenze_Initialize() {
        if (descrCompetenze == null) {
            descrCompetenze = new java.util.Vector(50, 10);
        } else {
            descrCompetenze.clear();
        }
        if (codiciCompetenze == null) {
            codiciCompetenze = new java.util.Vector(50, 10);
        } else {
            codiciCompetenze.clear();
        }
        javax.swing.JButton[] bottoni = { getSceltaJButton(), getChiudiJButton() };
        mainPackage.CostantiDavide.inizBordiPulsanti(bottoni, mainPackage.CostantiDavide.TIPO_AZIONE);
        return;
    }

    /**
 * Comment
 */
    public void scegliCompetenze_WindowActivated(java.awt.event.WindowEvent windowEvent) {
        mainPackage.CostantiDavide.msgInfo("ScegliCompetenze -> windowActivated()");
        try {
            if (tabCompetenze == null) {
                tabCompetenze = new mainPackage.ModelloTabella(dbConnPackage.DBDavide.selCompetenze());
            } else {
                tabCompetenze.setModel(dbConnPackage.DBDavide.selCompetenze());
            }
            getScrollPaneTable().setModel(tabCompetenze);
            mainPackage.CostantiDavide.msgInfo("size: " + descrCompetenze.size());
            if (descrCompetenze.size() > 0) {
                getScrollPaneTable().clearSelection();
                int i = 0;
                while (i < descrCompetenze.size()) {
                    for (int j = 0; j < getScrollPaneTable().getRowCount(); j++) {
                        if (descrCompetenze.get(i).toString().equalsIgnoreCase(getScrollPaneTable().getValueAt(j, 1).toString())) {
                            mainPackage.CostantiDavide.msgInfo("Riga: " + getScrollPaneTable().getValueAt(j, 1));
                            getScrollPaneTable().addRowSelectionInterval(j, j);
                        }
                    }
                    i++;
                }
            } else {
                getScrollPaneTable().clearSelection();
            }
        } catch (Exception e) {
            mainPackage.CostantiDavide.msgEccezione("Eccezione in windowActivated di ScegliCompetenze");
            e.printStackTrace();
        }
        return;
    }

    /**
 * Comment
 */
    public void sceltaJButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
        try {
            descrCompetenze.clear();
            codiciCompetenze.clear();
            for (int i = 0; i < getScrollPaneTable().getRowCount(); i++) {
                if (getScrollPaneTable().isRowSelected(i)) {
                    descrCompetenze.add(getScrollPaneTable().getValueAt(i, 1));
                    codiciCompetenze.add(tabCompetenze.getCodiceSel(i));
                }
            }
            dispose();
        } catch (Exception e) {
            mainPackage.CostantiDavide.msgEccezione("Eccezione in scelta di ScegliCompetenze");
            e.printStackTrace();
        }
        return;
    }
}
