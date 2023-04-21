package it.unibo.deis.interaction.p2p.tucson;

import java.util.Hashtable;

/**
 * Factory per la creazione di istanze di SignalObserver
 * Ogni SignalObserver dedicato alla ricezione di una particolare tipologia 
 * di segnali � un singleton: SignalObserverFactory realizza questa caratteristica.
 * 
 * @author Gianfy
 */
public class SignalObserverFactory {

    protected static Hashtable<String, SignalObserver> signalObservers = null;

    /**
	 * Ritorna il singleton deputato alla ricezione dei segnali di tipo signalName
	 * 
	 * @param signalName Il nome identificativo dei segnali che dovr� ricevere 
	 * @return Il riferimento alla istanza di SignalObserver presente in memoria
	 */
    public static SignalObserver getObserver(String signalName) {
        initHashtable();
        SignalObserver signalObserver = signalObservers.get(signalName);
        if (signalObserver == null || !signalObserver.isAlive()) {
            signalObserver = new SignalObserver(signalName);
            signalObservers.put(signalName, signalObserver);
            signalObserver.start();
            Thread.yield();
        }
        return signalObserver;
    }

    /**
	 * Verifica se esiste attualmente un SignalObserver locale in ascolto per un particolare tipo di segnale
	 * 
	 * @param signalName Il nome del segnale
	 * @return Se esiste un SignalObserver attivo per quel segnale
	 */
    public static boolean existsObserver(String signalName) {
        SignalObserver signalObserver = signalObservers.get(signalName);
        if (signalObserver != null) if (!signalObserver.isStopping()) return true; else {
            signalObserver.stop();
            signalObservers.remove(signalName);
        }
        return false;
    }

    protected static void initHashtable() {
        if (signalObservers == null) signalObservers = new Hashtable<String, SignalObserver>();
    }
}
