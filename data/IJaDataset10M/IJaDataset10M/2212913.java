package main.replacementpolicy;

import java.util.Vector;
import main.ReplacementPolicy;
import main.process.LogicReference;

/**
 * Classe che implementa l'algoritmo di rimpiazzo Not Frequently Used.
 * In breve, ad ogni round aggiorna un contatore associato ad ogni area di
 * memoria in memoria principale. Questo contatore aumenta piu' velocemente per le aree
 * piu' spesso riferite. Cosi', dato che le candidate per la rimozione in caso di swap sono 
 * le aree col contatore associato piu' basso, favorisce la permanenza in memoria di aree
 * spesso riferite di recente.
 * @author Amir Gharaba
 *
 */
public class Nfu extends ReplacementPolicy {

    /**
	 * Rappresentazione interna della RAM tramite Vector<MemoryAreaInformation>
	 */
    private Vector<MemoryAreaInformation> ram_;

    /** 
	 * Tempo di refresh, ossia ogni quanti cicli di clock il contatore delle
	 * aree viene aggiornato
	 */
    private int refreshTime_;

    /**
	 * Variabile d'appoggio che conta quanti cicli di refresh sono passati
	 */
    private int round_;

    public Nfu() {
        super();
        ram_ = new Vector<MemoryAreaInformation>();
        refreshTime_ = 0;
        round_ = 0;
    }

    /**
	 * Metodo che restituisce il tempo di refresh scelto per l'algoritmo NFU.
	 * @return intero che indica ogni quanti cicli di clock viene posto a false
	 * il bit di riferimento ed aggiornato il contatore.
	 */
    public int getRefreshTime() {
        return refreshTime_;
    }

    /**
	 * Metodo che consente di settare ogni quanti cicli di clock si desidera
	 * effettuare il refresh del contatore delle aree di memoria e del loro bit R.
	 * @param refreshTime: tempo di refresh, ossia ogni quanti cicli di clock
	 * va posto a false il bit R ed aggiornato il contatore.
	 */
    public void setRefreshTime(int refreshTime) throws IllegalArgumentException {
        if (refreshTime < 1) {
            throw new IllegalArgumentException("Impossibile settare un numero di cicli di clock per round inferiore ad 1.");
        }
        this.refreshTime_ = refreshTime;
    }

    /**
	 * Esegue il refresh:
	 * -per ogni pagina, incrementa il contatore di 1 se il rispettivo bit R e' a true
	 * -pone a false il bit R di ogni pagina.
	 */
    private void refresh() {
        for (int i = 0; i < ram_.size(); i++) {
            if (ram_.elementAt(i).getBitR()) {
                ram_.elementAt(i).setCounter(ram_.elementAt(i).getCounter() + 1);
            }
            ram_.elementAt(i).setBitR(false);
        }
    }

    @Override
    public void delete(int deletedArea) throws IllegalArgumentException {
        if (deletedArea < 0) {
            throw new IllegalArgumentException("Identificativo dell'area da rimuovere errato.");
        }
        for (int i = 0; i < ram_.size(); i++) {
            if (ram_.elementAt(i).getArea() == deletedArea) {
                ram_.removeElementAt(i);
            }
        }
    }

    @Override
    public int getAreaToClean() {
        MemoryAreaInformation lowestCounterArea = ram_.elementAt(0);
        for (int i = 1; i < ram_.size(); i++) {
            if (ram_.elementAt(i).getCounter() < lowestCounterArea.getCounter()) {
                lowestCounterArea = ram_.elementAt(i);
            }
        }
        return lowestCounterArea.getArea();
    }

    @Override
    public void update(int updatedArea, LogicReference referenceId) throws IllegalArgumentException {
        if (updatedArea < 0) {
            throw new IllegalArgumentException("Identificativo dell'area da inserire errato.");
        }
        while (referenceId.getResolutionTime() > refreshTime_ * round_) {
            refresh();
            round_++;
        }
        boolean areaUpdated = false;
        for (int i = 0; i < ram_.size() && !areaUpdated; i++) {
            if (ram_.elementAt(i).getArea() == updatedArea) {
                ram_.elementAt(i).setBitR(true);
                areaUpdated = true;
            }
        }
        if (!areaUpdated) {
            ram_.insertElementAt(new MemoryAreaInformation(updatedArea), ram_.size());
        }
    }
}
