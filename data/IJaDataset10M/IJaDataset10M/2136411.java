package dialogPackage;

import mainPackage.*;
import javax.swing.*;

/**
 * Inserire qui la descrizione del tipo.
 * Data di creazione: (24/09/2002 12.19.13)
 * @author: PeP1
 */
public class ScegliSpeAnt extends MiaJDialog {

    private JPanel ivjJDialogContentPane = null;

    private JButton ivjAggiungeJButton = null;

    private JButton ivjCercaJButton = null;

    private JButton ivjChiudiJButton = null;

    private JPanel ivjJPanel1 = null;

    private JButton ivjStampaJButton = null;

    private javax.swing.table.TableColumn ivjCausTC = null;

    private javax.swing.table.TableColumn ivjDataTC = null;

    private javax.swing.table.TableColumn ivjImportoTC = null;

    private JScrollPane ivjMostraSpesAntJScrollPane = null;

    private mainPackage.MiaJTable ivjMostraSpesAntScrollPaneTable = null;

    private javax.swing.table.TableColumn ivjPraticTC = null;

    private mainPackage.ModTabSVive tabSAnt;

    public final int SEL_SPVIVE_TUTTE = 0;

    public final int SEL_SPVIVE_UNA = 1;

    public final int SEL_ANT_TUTTE = 2;

    public final int SEL_ANT_UNA = 3;

    public int tipo;

    IvjEventHandler ivjEventHandler = new IvjEventHandler();

    private java.sql.Date ivjAData = null;

    private java.sql.Date ivjDaData = null;

    private String ivjCodPratica = null;

    private Integer ivjFlag = null;

    private EditEAnticipi ivjVediAnticipi = null;

    private EditSVive ivjVediSVive = null;

    private String ivjDescrPratica = null;

    private JButton ivjCancellaJButton = null;

    private JButton ivjModificaJButton = null;

    private Boolean ivjControlla = null;

    class IvjEventHandler implements dialogPackage.EditEAnticipiListener, dialogPackage.EditSViveListener, java.awt.event.ActionListener, java.awt.event.WindowListener {

        public void actionPerformed(java.awt.event.ActionEvent e) {
            if (e.getSource() == ScegliSpeAnt.this.getAggiungeJButton()) {
                connEtoC3(e);
            }
            if (e.getSource() == ScegliSpeAnt.this.getModificaJButton()) {
                connEtoC6(e);
            }
            if (e.getSource() == ScegliSpeAnt.this.getCancellaJButton()) {
                connEtoC7(e);
            }
            if (e.getSource() == ScegliSpeAnt.this.getChiudiJButton()) {
                connEtoM1(e);
            }
        }

        ;

        public void annullaJButtonAction_actionPerformed(java.util.EventObject newEvent) {
            if (newEvent.getSource() == ScegliSpeAnt.this.getVediSVive()) {
                connEtoC4(newEvent);
            }
            if (newEvent.getSource() == ScegliSpeAnt.this.getVediAnticipi()) {
                connEtoC9(newEvent);
            }
        }

        ;

        public void okJButtonAction_actionPerformed(java.util.EventObject newEvent) {
            if (newEvent.getSource() == ScegliSpeAnt.this.getVediSVive()) {
                connEtoC5(newEvent);
            }
            if (newEvent.getSource() == ScegliSpeAnt.this.getVediAnticipi()) {
                connEtoC10(newEvent);
            }
        }

        ;

        public void windowActivated(java.awt.event.WindowEvent e) {
            if (e.getSource() == ScegliSpeAnt.this) {
                connEtoC2(e);
            }
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
            if (e.getSource() == ScegliSpeAnt.this) {
                connEtoC1(e);
            }
        }

        ;
    }

    ;

    /**
     * Commento del constructor MostraSpeAnt.
     */
    public ScegliSpeAnt() {
        super();
        initialize();
    }

    /**
     * Commento del constructor MostraSpeAnt.
     * @param owner java.awt.Dialog
     */
    public ScegliSpeAnt(java.awt.Dialog owner) {
        super(owner);
        initialize();
    }

    /**
     * Commento del constructor MostraSpeAnt.
     * @param owner java.awt.Dialog
     * @param title java.lang.String
     */
    public ScegliSpeAnt(java.awt.Dialog owner, String title) {
        super(owner, title);
        initialize();
    }

    /**
     * Commento del constructor MostraSpeAnt.
     * @param owner java.awt.Dialog
     * @param title java.lang.String
     * @param modal boolean
     */
    public ScegliSpeAnt(java.awt.Dialog owner, String title, boolean modal) {
        super(owner, title, modal);
        initialize();
    }

    /**
     * Commento del constructor MostraSpeAnt.
     * @param owner java.awt.Dialog
     * @param modal boolean
     */
    public ScegliSpeAnt(java.awt.Dialog owner, boolean modal) {
        super(owner, modal);
        initialize();
    }

    /**
     * Commento del constructor MostraSpeAnt.
     * @param owner java.awt.Frame
     */
    public ScegliSpeAnt(java.awt.Frame owner) {
        super(owner);
        initialize();
    }

    /**
     * Commento del constructor MostraSpeAnt.
     * @param owner java.awt.Frame
     * @param title java.lang.String
     */
    public ScegliSpeAnt(java.awt.Frame owner, String title) {
        super(owner, title);
        initialize();
    }

    /**
     * Commento del constructor MostraSpeAnt.
     * @param owner java.awt.Frame
     * @param title java.lang.String
     * @param modal boolean
     */
    public ScegliSpeAnt(java.awt.Frame owner, String title, boolean modal) {
        super(owner, title, modal);
        initialize();
    }

    /**
     * Commento del constructor MostraSpeAnt.
     * @param owner java.awt.Frame
     * @param modal boolean
     */
    public ScegliSpeAnt(java.awt.Frame owner, boolean modal) {
        super(owner, modal);
        initialize();
    }

    /**
     * Inserire qui la descrizione del metodo.
     * Data di creazione: (24/09/2002 14.48.01)
     */
    public void aggiornaVisual() {
        mainPackage.CostantiDavide.msgInfo("Sto aggiornando la visualizzazione di ScegliSpeAnt");
        if (tabSAnt == null) {
            switch(tipo) {
                case SEL_SPVIVE_TUTTE:
                    {
                        tabSAnt = new mainPackage.ModTabSVive(dbConnPackage.DBDavide.selESVive(getDaData(), getAData()));
                        break;
                    }
                case SEL_SPVIVE_UNA:
                    {
                        tabSAnt = new mainPackage.ModTabSVive(dbConnPackage.DBDavide.selESVive(getCodPratica(), getDaData(), getAData()));
                        break;
                    }
                case SEL_ANT_TUTTE:
                    {
                        tabSAnt = new mainPackage.ModTabSVive(dbConnPackage.DBDavide.selEAnt(getDaData(), getAData()));
                        break;
                    }
                case SEL_ANT_UNA:
                    {
                        tabSAnt = new mainPackage.ModTabSVive(dbConnPackage.DBDavide.selEAnt(getCodPratica(), getDaData(), getAData()));
                        break;
                    }
            }
        } else {
            switch(tipo) {
                case SEL_SPVIVE_TUTTE:
                    {
                        tabSAnt.setModel(dbConnPackage.DBDavide.selESVive(getDaData(), getAData()));
                        break;
                    }
                case SEL_SPVIVE_UNA:
                    {
                        tabSAnt.setModel(dbConnPackage.DBDavide.selESVive(getCodPratica(), getDaData(), getAData()));
                        break;
                    }
                case SEL_ANT_TUTTE:
                    {
                        tabSAnt.setModel(dbConnPackage.DBDavide.selEAnt(getDaData(), getAData()));
                        break;
                    }
                case SEL_ANT_UNA:
                    {
                        tabSAnt.setModel(dbConnPackage.DBDavide.selEAnt(getCodPratica(), getDaData(), getAData()));
                        break;
                    }
            }
        }
        getMostraSpesAntScrollPaneTable().setModel(tabSAnt);
    }

    /**
     * Comment
     */
    public void aggiungeJButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
        String codice = null, descrizione = null;
        if (getFlag().equals(new Integer(0))) {
            getVediSVive().setButtonFlagThis(new Boolean(true));
            getVediSVive().pulisciTutto();
            int riga = getMostraSpesAntScrollPaneTable().getSelectedRow();
            codice = getCodPratica();
            descrizione = getDescrPratica();
            if (getControlla().equals(new Boolean(true))) {
                getVediSVive().impostaFunzionalita(1, codice, descrizione);
            } else {
                getVediSVive().impostaFunzionalita(0, codice, descrizione);
            }
            getVediSVive().setCodPraticaThis(getCodPraticaThis());
            getVediSVive().show();
        } else {
            if (getFlag().equals(new Integer(1))) {
                getVediAnticipi().setButtonFlagThis(new Boolean(true));
                getVediAnticipi().pulisciTutto();
                int riga = getMostraSpesAntScrollPaneTable().getSelectedRow();
                codice = getCodPratica();
                descrizione = getDescrPratica();
                getVediAnticipi().impostaFunzionalita(1, codice, descrizione);
                getVediAnticipi().show();
            }
        }
        return;
    }

    /**
     * Comment
     */
    public void cancJButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
        try {
            if (getFlag().equals(new Integer(0))) {
                if (getMostraSpesAntScrollPaneTable().getRowCount() > 0 && getMostraSpesAntScrollPaneTable().getSelectedRow() != -1) {
                    int ris = JOptionPane.showConfirmDialog(this, Messages.getString("ScegliSpeAnt.Vuoi_veramente_cancellare_questa_registrazione__2"), Messages.getString("ScegliSpeAnt.Scegliere_3"), JOptionPane.YES_NO_OPTION);
                    if (ris == JOptionPane.YES_OPTION) {
                        String codice = tabSAnt.getCodiceSel(getMostraSpesAntScrollPaneTable().getSelectedRow());
                        dbConnPackage.DBDavide.cancellaRecord("Nota_Spe", codice);
                    }
                    aggiornaVisual();
                }
            } else {
                if (getFlag().equals(new Integer(1))) {
                    if (getMostraSpesAntScrollPaneTable().getSelectedRow() != -1) {
                        int ris = JOptionPane.showConfirmDialog(this, Messages.getString("ScegliSpeAnt.Vuoi_veramente_cancellare_questa_registrazione__5"), Messages.getString("ScegliSpeAnt.Scegliere_6"), JOptionPane.YES_NO_OPTION);
                        if (ris == JOptionPane.YES_OPTION) {
                            String codice = tabSAnt.getCodiceSel(getMostraSpesAntScrollPaneTable().getSelectedRow());
                            dbConnPackage.DBDavide.cancellaRecord(Messages.getString("ScegliSpeAnt.Anticipi_7"), codice);
                        }
                        aggiornaVisual();
                    }
                }
            }
            return;
        } catch (Exception e) {
            mainPackage.CostantiDavide.msgInfo("Eccez. in cancella di ScegliSpeAnt ");
            e.printStackTrace();
        }
    }

    /**
     * connEtoC1:  (MostraSpeAnt.window.windowOpened(java.awt.event.WindowEvent) --> ScegliSpeAnt.mostraSpeAnt_WindowOpened(Ljava.awt.event.WindowEvent;)V)
     * @param arg1 java.awt.event.WindowEvent
     */
    private void connEtoC1(java.awt.event.WindowEvent arg1) {
        try {
            this.mostraSpeAnt_WindowOpened(arg1);
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC10:  (VediAnticipi.editEAnticipi.okJButtonAction_actionPerformed(java.util.EventObject) --> ScegliSpeAnt.vediAnticipi_OkJButtonAction(Ljava.util.EventObject;)V)
     * @param arg1 java.util.EventObject
     */
    private void connEtoC10(java.util.EventObject arg1) {
        try {
            this.vediAnticipi_OkJButtonAction(arg1);
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC2:  (MostraSpeAnt.window.windowActivated(java.awt.event.WindowEvent) --> ScegliSpeAnt.mostraSpeAnt_WindowActivated(Ljava.awt.event.WindowEvent;)V)
     * @param arg1 java.awt.event.WindowEvent
     */
    private void connEtoC2(java.awt.event.WindowEvent arg1) {
        try {
            this.mostraSpeAnt_WindowActivated(arg1);
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC3:  (AggiungeJButton.action.actionPerformed(java.awt.event.ActionEvent) --> ScegliSpeAnt.aggiungeJButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
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
     * connEtoC4:  (VediSVive.editSVive.okJButtonAction_actionPerformed(java.util.EventObject) --> ScegliSpeAnt.vediSVive_OkJButtonAction_actionPerformed1(Ljava.util.EventObject;)V)
     * @param arg1 java.util.EventObject
     */
    private void connEtoC4(java.util.EventObject arg1) {
        try {
            this.vediSVive_AnnullaJButtonAction(arg1);
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC5:  (VediAnticipi.editEAnticipi.okJButtonAction_actionPerformed(java.util.EventObject) --> ScegliSpeAnt.vediAnticipi_OkJButtonAction_actionPerformed1(Ljava.util.EventObject;)V)
     * @param arg1 java.util.EventObject
     */
    private void connEtoC5(java.util.EventObject arg1) {
        try {
            this.vediSVive_OkJButtonAction(arg1);
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC6:  (ModJButton.action.actionPerformed(java.awt.event.ActionEvent) --> ScegliSpeAnt.modJButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
     * @param arg1 java.awt.event.ActionEvent
     */
    private void connEtoC6(java.awt.event.ActionEvent arg1) {
        try {
            this.modJButton_ActionPerformed(arg1);
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC7:  (CancJButton.action.actionPerformed(java.awt.event.ActionEvent) --> ScegliSpeAnt.cancJButton_ActionPerformed(Ljava.awt.event.ActionEvent;)V)
     * @param arg1 java.awt.event.ActionEvent
     */
    private void connEtoC7(java.awt.event.ActionEvent arg1) {
        try {
            this.cancJButton_ActionPerformed(arg1);
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC8:  (MostraSpeAnt.initialize() --> ScegliSpeAnt.mostraSpeAnt_Initialize()V)
     */
    private void connEtoC8() {
        try {
            this.mostraSpeAnt_Initialize();
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    /**
     * connEtoC9:  (VediAnticipi.editEAnticipi.annullaJButtonAction_actionPerformed(java.util.EventObject) --> ScegliSpeAnt.vediAnticipi_AnnullaJButtonAction(Ljava.util.EventObject;)V)
     * @param arg1 java.util.EventObject
     */
    private void connEtoC9(java.util.EventObject arg1) {
        try {
            this.vediAnticipi_AnnullaJButtonAction(arg1);
        } catch (java.lang.Throwable ivjExc) {
            handleException(ivjExc);
        }
    }

    /**
     * connEtoM1:  (ChiudiJButton.action.actionPerformed(java.awt.event.ActionEvent) --> MostraSpeAnt.dispose()V)
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
     * Restituisce il valore della proprieta AData.
     * @return java.sql.Date
     */
    private java.sql.Date getAData() {
        return ivjAData;
    }

    /**
     * Metodo creato per supportare la promozione dell'attributo di ADataThis.
     * @return java.sql.Date
     */
    public java.sql.Date getADataThis() {
        return getAData();
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
                ivjAggiungeJButton.setText(Messages.getString("ScegliSpeAnt.Aggiunge_10"));
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjAggiungeJButton;
    }

    /**
     *
     */
    private static void getBuilderData() {
    }

    /**
     * Restituisce il valore della proprieta CancJButton.
     * @return javax.swing.JButton
     */
    private javax.swing.JButton getCancellaJButton() {
        if (ivjCancellaJButton == null) {
            try {
                ivjCancellaJButton = new javax.swing.JButton();
                ivjCancellaJButton.setName("CancellaJButton");
                ivjCancellaJButton.setText(Messages.getString("ScegliSpeAnt.Cancella_12"));
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjCancellaJButton;
    }

    /**
     * Restituisce il valore della proprieta CausTC.
     * @return javax.swing.table.TableColumn
     */
    private javax.swing.table.TableColumn getCausTC() {
        int col = 3;
        if (ivjCausTC == null) {
            try {
                ivjCausTC = new javax.swing.table.TableColumn();
                String titolo = getTitle();
                ivjCausTC.setModelIndex(col);
                ivjCausTC.setHeaderValue(Messages.getString("ScegliSpeAnt.Causale_13"));
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjCausTC;
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
                ivjCercaJButton.setText(Messages.getString("ScegliSpeAnt.Cerca_15"));
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
                ivjChiudiJButton.setText(Messages.getString("ScegliSpeAnt.Chiudi_17"));
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjChiudiJButton;
    }

    /**
     * Restituisce il valore della proprieta CodPratica.
     * @return java.lang.String
     */
    private java.lang.String getCodPratica() {
        return ivjCodPratica;
    }

    /**
     * Metodo creato per supportare la promozione dell'attributo di codPraticaThis.
     * @return java.lang.String
     */
    public java.lang.String getCodPraticaThis() {
        return getCodPratica();
    }

    /**
     * Restituisce il valore della proprieta Controlla.
     * @return java.lang.Boolean
     */
    private java.lang.Boolean getControlla() {
        return ivjControlla;
    }

    /**
     * Metodo creato per supportare la promozione dell'attributo di controllaThis.
     * @return java.lang.Boolean
     */
    public java.lang.Boolean getControllaThis() {
        return getControlla();
    }

    /**
     * Restituisce il valore della proprieta DaData.
     * @return java.sql.Date
     */
    private java.sql.Date getDaData() {
        return ivjDaData;
    }

    /**
     * Metodo creato per supportare la promozione dell'attributo di daDataThis.
     * @return java.sql.Date
     */
    public java.sql.Date getDaDataThis() {
        return getDaData();
    }

    /**
     * Restituisce il valore della proprieta DataTC.
     * @return javax.swing.table.TableColumn
     */
    private javax.swing.table.TableColumn getDataTC() {
        if (ivjDataTC == null) {
            try {
                ivjDataTC = new javax.swing.table.TableColumn();
                ivjDataTC.setModelIndex(1);
                ivjDataTC.setHeaderValue(Messages.getString("ScegliSpeAnt.Data_18"));
                ivjDataTC.setPreferredWidth(5);
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjDataTC;
    }

    /**
     * Restituisce il valore della proprieta DescrPratica.
     * @return java.lang.String
     */
    private java.lang.String getDescrPratica() {
        return ivjDescrPratica;
    }

    /**
     * Metodo creato per supportare la promozione dell'attributo di descrPraticaThis.
     * @return java.lang.String
     */
    public java.lang.String getDescrPraticaThis() {
        return getDescrPratica();
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
     * Restituisce il valore della proprieta ImportoTC.
     * @return javax.swing.table.TableColumn
     */
    private javax.swing.table.TableColumn getImportoTC() {
        int col = 2;
        if (ivjImportoTC == null) {
            try {
                ivjImportoTC = new javax.swing.table.TableColumn();
                String titolo = getTitle();
                ivjImportoTC.setModelIndex(col);
                ivjImportoTC.setHeaderValue(Messages.getString("ScegliSpeAnt.Importo_19"));
                ivjImportoTC.setPreferredWidth(5);
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjImportoTC;
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
                getJDialogContentPane().add(getJPanel1(), "South");
                getJDialogContentPane().add(getMostraSpesAntJScrollPane(), "Center");
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
                ivjJPanel1.setLayout(new java.awt.FlowLayout());
                getJPanel1().add(getAggiungeJButton(), getAggiungeJButton().getName());
                getJPanel1().add(getModificaJButton(), getModificaJButton().getName());
                getJPanel1().add(getCancellaJButton(), getCancellaJButton().getName());
                getJPanel1().add(getCercaJButton(), getCercaJButton().getName());
                getJPanel1().add(getStampaJButton(), getStampaJButton().getName());
                getJPanel1().add(getChiudiJButton(), getChiudiJButton().getName());
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjJPanel1;
    }

    /**
     * Restituisce il valore della proprieta ModJButton.
     * @return javax.swing.JButton
     */
    private javax.swing.JButton getModificaJButton() {
        if (ivjModificaJButton == null) {
            try {
                ivjModificaJButton = new javax.swing.JButton();
                ivjModificaJButton.setName("ModificaJButton");
                ivjModificaJButton.setText(Messages.getString("ScegliSpeAnt.Modifica_25"));
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjModificaJButton;
    }

    /**
     * Restituisce il valore della proprieta MostraSpesAntJScrollPane.
     * @return javax.swing.JScrollPane
     */
    private javax.swing.JScrollPane getMostraSpesAntJScrollPane() {
        if (ivjMostraSpesAntJScrollPane == null) {
            try {
                ivjMostraSpesAntJScrollPane = new javax.swing.JScrollPane();
                ivjMostraSpesAntJScrollPane.setName("MostraSpesAntJScrollPane");
                ivjMostraSpesAntJScrollPane.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                ivjMostraSpesAntJScrollPane.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
                getMostraSpesAntJScrollPane().setViewportView(getMostraSpesAntScrollPaneTable());
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjMostraSpesAntJScrollPane;
    }

    /**
     * Restituisce il valore della proprieta MostraSpesAntScrollPaneTable.
     * @return mainPackage.MiaJTable
     */
    private mainPackage.MiaJTable getMostraSpesAntScrollPaneTable() {
        if (ivjMostraSpesAntScrollPaneTable == null) {
            try {
                ivjMostraSpesAntScrollPaneTable = new mainPackage.MiaJTable();
                ivjMostraSpesAntScrollPaneTable.setName("MostraSpesAntScrollPaneTable");
                getMostraSpesAntJScrollPane().setColumnHeaderView(ivjMostraSpesAntScrollPaneTable.getTableHeader());
                getMostraSpesAntJScrollPane().getViewport().setScrollMode(JViewport.BACKINGSTORE_SCROLL_MODE);
                ivjMostraSpesAntScrollPaneTable.setBounds(0, 0, 200, 200);
                ivjMostraSpesAntScrollPaneTable.setAutoCreateColumnsFromModel(false);
                ivjMostraSpesAntScrollPaneTable.addColumn(getDataTC());
                ivjMostraSpesAntScrollPaneTable.addColumn(getCausTC());
                ivjMostraSpesAntScrollPaneTable.addColumn(getImportoTC());
                ivjMostraSpesAntScrollPaneTable.addColumn(getPraticTC());
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjMostraSpesAntScrollPaneTable;
    }

    /**
     * Restituisce il valore della proprieta PraticTC.
     * @return javax.swing.table.TableColumn
     */
    private javax.swing.table.TableColumn getPraticTC() {
        if (ivjPraticTC == null) {
            try {
                ivjPraticTC = new javax.swing.table.TableColumn();
                ivjPraticTC.setModelIndex(4);
                ivjPraticTC.setHeaderValue(Messages.getString("ScegliSpeAnt.Pratica_28"));
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjPraticTC;
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
                ivjStampaJButton.setText(Messages.getString("ScegliSpeAnt.Stampa_30"));
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
     * Restituisce il valore della proprieta VediAnticipi.
     * @return dialogPackage.EditEAnticipi
     */
    private EditEAnticipi getVediAnticipi() {
        if (ivjVediAnticipi == null) {
            try {
                ivjVediAnticipi = new dialogPackage.EditEAnticipi(this);
                ivjVediAnticipi.setName("VediAnticipi");
                ivjVediAnticipi.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjVediAnticipi;
    }

    /**
     * Restituisce il valore della proprieta VediSVive.
     * @return dialogPackage.EditSVive
     */
    private EditSVive getVediSVive() {
        if (ivjVediSVive == null) {
            try {
                ivjVediSVive = new dialogPackage.EditSVive(this);
                ivjVediSVive.setName("VediSVive");
                ivjVediSVive.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        return ivjVediSVive;
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
        getVediSVive().addEditSViveListener(ivjEventHandler);
        getVediAnticipi().addEditEAnticipiListener(ivjEventHandler);
        getChiudiJButton().addActionListener(ivjEventHandler);
    }

    /**
     * Inizializzare la classe.
     */
    private void initialize() {
        try {
            setName("MostraSpeAnt");
            setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
            setSize(728, 295);
            setModal(true);
            setContentPane(getJDialogContentPane());
            initConnections();
            connEtoC8();
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
            ScegliSpeAnt aScegliSpeAnt;
            aScegliSpeAnt = new ScegliSpeAnt();
            aScegliSpeAnt.setModal(true);
            aScegliSpeAnt.addWindowListener(new java.awt.event.WindowAdapter() {

                public void windowClosing(java.awt.event.WindowEvent e) {
                    System.exit(0);
                }

                ;
            });
            aScegliSpeAnt.show();
            java.awt.Insets insets = aScegliSpeAnt.getInsets();
            aScegliSpeAnt.setSize(aScegliSpeAnt.getWidth() + insets.left + insets.right, aScegliSpeAnt.getHeight() + insets.top + insets.bottom);
            aScegliSpeAnt.setVisible(true);
        } catch (Throwable exception) {
            System.err.println("Eccezione verificatasi in main() di javax.swing.JDialog");
            exception.printStackTrace(System.out);
        }
    }

    /**
     * Comment
     */
    public void modJButton_ActionPerformed(java.awt.event.ActionEvent actionEvent) {
        try {
            if (getMostraSpesAntScrollPaneTable().getRowCount() > 0 && getMostraSpesAntScrollPaneTable().getSelectedRow() != -1) {
                getVediSVive().flag = 0;
                getVediAnticipi().flag = 0;
                String codice = null;
                if (getFlag().equals(new Integer(0))) {
                    if (getMostraSpesAntScrollPaneTable().getRowCount() > 0 && getMostraSpesAntScrollPaneTable().getSelectedRow() != -1) {
                        getVediSVive().setButtonFlagThis(new Boolean(false));
                        if (tipo == 1) {
                            codice = getCodPraticaThis();
                        } else {
                            if (tipo == 0) {
                                codice = tabSAnt.getValueAt(getMostraSpesAntScrollPaneTable().getSelectedRow(), 6).toString();
                            }
                        }
                        String descrizione = tabSAnt.getValueAt(getMostraSpesAntScrollPaneTable().getSelectedRow(), 4).toString();
                        getVediSVive().caricaSVive(tabSAnt.getCodiceSel(getMostraSpesAntScrollPaneTable().getSelectedRow()), descrizione);
                        getVediSVive().impostaFunzionalita(1, codice, descrizione);
                        getVediSVive().show();
                    }
                } else {
                    if (getFlag().equals(new Integer(1))) {
                        if (getMostraSpesAntScrollPaneTable().getSelectedRow() != -1) {
                            getVediAnticipi().setButtonFlagThis(new Boolean(false));
                            if (tipo == 2) {
                                codice = tabSAnt.getValueAt(getMostraSpesAntScrollPaneTable().getSelectedRow(), 6).toString();
                            } else {
                                if (tipo == 3) {
                                    codice = getCodPratica();
                                }
                            }
                            String descrizione = tabSAnt.getValueAt(getMostraSpesAntScrollPaneTable().getSelectedRow(), 4).toString();
                            getVediAnticipi().caricaAnticipi(tabSAnt.getCodiceSel(getMostraSpesAntScrollPaneTable().getSelectedRow()), descrizione);
                            getVediAnticipi().impostaFunzionalita(1, codice, descrizione);
                            getVediAnticipi().setCodPraticaThis(getCodPratica());
                            getVediAnticipi().show();
                        }
                    }
                }
            }
        } catch (Exception e) {
            mainPackage.CostantiDavide.msgInfo("Eccez. in modifica di ScegliSpeAnt");
            e.printStackTrace();
        }
        return;
    }

    /**
     * Comment
     */
    public void mostraSpeAnt_Initialize() {
        javax.swing.JButton[] bottoni = { getAggiungeJButton(), getModificaJButton(), getCancellaJButton(), getCercaJButton(), getStampaJButton(), getChiudiJButton() };
        mainPackage.CostantiDavide.inizBordiPulsanti(bottoni, mainPackage.CostantiDavide.TIPO_AZIONE);
        return;
    }

    /**
     * Comment
     */
    public void mostraSpeAnt_WindowActivated(java.awt.event.WindowEvent windowEvent) {
        aggiornaVisual();
        if (getMostraSpesAntScrollPaneTable().getRowCount() > 0) {
            getMostraSpesAntScrollPaneTable().selezionaRiga(0, 0);
        }
        return;
    }

    /**
     * Comment
     */
    public void mostraSpeAnt_WindowOpened(java.awt.event.WindowEvent windowEvent) {
        aggiornaVisual();
        if (getMostraSpesAntScrollPaneTable().getRowCount() > 0) {
            getMostraSpesAntScrollPaneTable().selezionaRiga(0, 0);
            return;
        }
        return;
    }

    /**
     * Impostare AData su un nuovo valore.
     * @param newValue java.sql.Date
     */
    private void setAData(java.sql.Date newValue) {
        if (ivjAData != newValue) {
            try {
                java.sql.Date oldValue = getAData();
                ivjAData = newValue;
                firePropertyChange("ADataThis", oldValue, newValue);
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        ;
    }

    /**
     * Metodo creato per supportare la promozione dell'attributo di ADataThis.
     * @param arg1 java.sql.Date
     */
    public void setADataThis(java.sql.Date arg1) {
        setAData(arg1);
    }

    /**
     * Impostare CodPratica su un nuovo valore.
     * @param newValue java.lang.String
     */
    private void setCodPratica(java.lang.String newValue) {
        if (ivjCodPratica != newValue) {
            try {
                String oldValue = getCodPratica();
                ivjCodPratica = newValue;
                firePropertyChange("codPraticaThis", oldValue, newValue);
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        ;
    }

    /**
     * Metodo creato per supportare la promozione dell'attributo di codPraticaThis.
     * @param arg1 java.lang.String
     */
    public void setCodPraticaThis(java.lang.String arg1) {
        setCodPratica(arg1);
    }

    /**
     * Impostare Controlla su un nuovo valore.
     * @param newValue java.lang.Boolean
     */
    private void setControlla(java.lang.Boolean newValue) {
        if (ivjControlla != newValue) {
            try {
                Boolean oldValue = getControlla();
                ivjControlla = newValue;
                firePropertyChange("controllaThis", oldValue, newValue);
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        ;
    }

    /**
     * Metodo creato per supportare la promozione dell'attributo di controllaThis.
     * @param arg1 java.lang.Boolean
     */
    public void setControllaThis(java.lang.Boolean arg1) {
        setControlla(arg1);
    }

    /**
     * Impostare DaData su un nuovo valore.
     * @param newValue java.sql.Date
     */
    private void setDaData(java.sql.Date newValue) {
        if (ivjDaData != newValue) {
            try {
                java.sql.Date oldValue = getDaData();
                ivjDaData = newValue;
                firePropertyChange("ADataThis", oldValue, newValue);
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        ;
    }

    /**
     * Metodo creato per supportare la promozione dell'attributo di daDataThis.
     * @param arg1 java.sql.Date
     */
    public void setDaDataThis(java.sql.Date arg1) {
        setDaData(arg1);
    }

    /**
     * Impostare DescrPratica su un nuovo valore.
     * @param newValue java.lang.String
     */
    private void setDescrPratica(java.lang.String newValue) {
        if (ivjDescrPratica != newValue) {
            try {
                String oldValue = getDescrPratica();
                ivjDescrPratica = newValue;
                firePropertyChange("codPraticaThis", oldValue, newValue);
            } catch (java.lang.Throwable ivjExc) {
                handleException(ivjExc);
            }
        }
        ;
    }

    /**
     * Metodo creato per supportare la promozione dell'attributo di descrPraticaThis.
     * @param arg1 java.lang.String
     */
    public void setDescrPraticaThis(java.lang.String arg1) {
        setDescrPratica(arg1);
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

    /**
     * Comment
     */
    public void vediAnticipi_AnnullaJButtonAction(java.util.EventObject newEvent) {
        aggiornaVisual();
        return;
    }

    /**
     * Comment
     */
    public void vediAnticipi_OkJButtonAction(java.util.EventObject newEvent) {
        aggiornaVisual();
        return;
    }

    /**
     * Comment
     */
    public void vediSVive_AnnullaJButtonAction(java.util.EventObject newEvent) {
        aggiornaVisual();
        return;
    }

    /**
     * Comment
     */
    public void vediSVive_OkJButtonAction(java.util.EventObject newEvent) {
        aggiornaVisual();
        return;
    }

    public void stampa() {
        if (tabSAnt != null) {
            reportPackage.FreeRep fr = new reportPackage.FreeRep(this, tabSAnt, "Spese");
        }
    }
}
