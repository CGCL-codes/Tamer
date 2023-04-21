package logica.gestoreProcessi;

/**
 * Rappresenta l'interfaccia comune alle parti Dati, Codice ed Area di Lavoro. 
 * Definisce le operazioni comuni effettuabili su di esse.
 *
 * @author Orlandin Marco
 * @version 1.0
 */
public interface InterfacciaDati {

    /**
	 * Indica se un parte puo' andare in swap o meno.
	 * @return true se la parte puo' andare in swap, false altrimenti.
	 */
    public boolean getSwappable();

    /**
	 * Ritorna la dimensione della parte.
	 * @return dimensione della parte.
	 */
    public long getDimensione();
}
