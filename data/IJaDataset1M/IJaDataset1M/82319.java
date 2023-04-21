package main.registry;

/**
 *
 * @author Alessandro Beggiato
 * @version 1.0
 * Le classi che implementano questa interfaccia dichiarano di essere in grado
 * di navigare gli istanti gia' calcolati della simulazione
 */
public interface Navigable {

    /**
	 * Porta lo stato a una situazione precedente
	 * @param change un oggetto che descrive i cambiamenti da apportare allo stato
	 * @throws ClassCastException se il tipo di change non e' quello corretto per la classe che implementa Navigable
	 */
    public void rollBack(Object change);

    /**
	 * Porta lo stato a una situazione successivo
	 * @param change un oggetto che descrive i cambiamenti da apportare allo stato
	 * @throws ClassCastException se il tipo di change non e' quello corretto per la classe che implementa Navigable
	 */
    public void stepForward(Object change);
}
