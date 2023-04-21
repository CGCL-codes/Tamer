package view.configurazione;

import org.apache.log4j.Logger;
import com.nexes.wizard.WizardPanelDescriptor;

/**
 * Descrittore di utilit� per la classe ConfPanelParametriMacchina
 * @author Silvia Ballardin
 * @version 1.0
 */
public class ConfPanelParametriMacchinaDescriptor extends WizardPanelDescriptor {

    /**
     * Logger
     */
    private static final Logger logger = Logger.getLogger(ConfPanelParametriMacchinaDescriptor.class);

    /**
     * Reference al pannello dei parametri macchina
     */
    private ConfPanelParametriMacchina pnlMacchina;

    /**
     * Reference alla classe genitrice
     */
    @SuppressWarnings("unused")
    private ConfWizard owner;

    /**
     * Costante che identifica il pannello corrente
     */
    public static final String IDENTIFIER = "PARMAC_PANEL";

    /**
     * Costruttore della classe ConfPanelParametriMacchina
     * @param Confwizard owner - la classe genitrice
     */
    public ConfPanelParametriMacchinaDescriptor(ConfWizard owner) {
        this.owner = owner;
        pnlMacchina = new ConfPanelParametriMacchina(owner);
        setPanelDescriptorIdentifier(IDENTIFIER);
        setPanelComponent(pnlMacchina);
        logger.info("Creato descrittore di utilit� del" + " pannello relativo ai parametri macchina");
    }

    /**
     * Metodo da ridefinire per impostare a che pannello spostarsi dopo la
     * pressione del tasto next
     * @return Object - identificatore del pannello in cui mi sposto
     */
    public Object getNextPanelDescriptor() {
        return ConfPanelMemoriaDescriptor.IDENTIFIER;
    }

    /**
     * Metodo da ridefinire per impostare a che pannello spostarsi dopo la
     * pressione del tasto back
     * @return Object - identificatore del pannello in cui mi sposto
     */
    public Object getBackPanelDescriptor() {
        return ConfPanelModalitaDescriptor.IDENTIFIER;
    }

    /**
     * Metodo da ridefinire per impostare le operazioni da fare quando il pannello
     * diventa invisibile, prima che appaia il nuovo pannello; ritornando -1
     * posso impedire la visualizzazione del nuovo pannello
     * @return int - 0 o -1, indica se posso passare al prossimo pannello o meno
     */
    public int aboutToHidePanel() {
        logger.info("Eseguo le operazioni da fare prima di passare dal" + " pannello dei parametri macchina al pannello di scelta memoria");
        return 0;
    }

    /**
     * Metodo da ridefinire per impostare le operazioni da fare prima della
     * visualizzazione del pannello corrente, ma dopo la scomparsa del pannello
     * precedente
     */
    public void displayingPanel() {
        logger.info("Eseguo le operazioni da fare prima di visualizzare" + " il pannello dei parametri macchina");
    }

    /**
     * Metodo che preimposta i dati di default
     */
    public void setDatiDefault() {
        logger.info("Invoco pnlMacchina.setDatiDefault() per preimpostare" + " i dati di default nel pannello relativo ai parametri macchina");
        pnlMacchina.setDatiDefault();
    }

    /**
     * Metodo che definisce se nel pannello corrente abilitare o meno l'help
     * @return boolean - true o false, indica se l'help � abilitato o meno
     */
    public boolean isHelp() {
        return true;
    }
}
