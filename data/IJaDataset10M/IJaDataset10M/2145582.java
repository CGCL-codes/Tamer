package logica.gestorePolitiche;

import java.util.*;
import org.apache.log4j.Logger;
import logica.gestoreMemoria.*;

/**
 * Classe rappresentante la politica di rimpiazzo Aging (Not Frequently Used) 
 * @author Secchiero Marco
 * @version 1.0
 */
public class Aging extends Rimpiazzo {

    private static final Logger logger = Logger.getLogger(Aging.class);

    /**
	 * struttura interna della politica, si cerca all'interno di questa lista
	 * la Struttura che � stata utilizzata di meno
	 */
    private final ArrayList<Struttura> listaStruttura = new ArrayList<Struttura>();

    /**
	 * Ricerca una Struttura all'interno della lista per un eventuale 
	 * aggiornamento del suo ultimoUtilizzo
	 * @param Struttura struct - Struttura di confronto
	 * @return la posizione da aggiornare
	 */
    private int trovaStruttura(final Struttura struct) {
        int i = 0;
        boolean trovato = false;
        logger.info("cerco la Struttura parametro all'interno della lista");
        while ((i < listaStruttura.size()) && !trovato) {
            Struttura temp = (Struttura) listaStruttura.get(i);
            trovato = temp.equals(struct);
            i++;
        }
        return (trovato) ? i-- : -1;
    }

    /**
	 * Realizza la politica Aging (LRU via software) cercando
	 * nella lista la Struttura con ultimoUtilizzo pi� basso
	 * @return la Struttura da rimpiazzare
	 */
    private Struttura lru() {
        long minimo = listaStruttura.get(0).getUltimoUtilizzo();
        int i = 1;
        Struttura minStruct = (Struttura) listaStruttura.get(0);
        logger.info("cerco Struttura con minore ultimoUtilizzo");
        while (i < listaStruttura.size()) {
            Struttura temp = (Struttura) listaStruttura.get(i);
            if (temp.getUltimoUtilizzo() < minimo) {
                minStruct = temp;
            }
            i++;
        }
        return minStruct;
    }

    /**
  	 * Costruttore
  	 * @param int nuovoTipo - tipologia di Struttura
	 * @param boolean globale - tipologia di politica
  	 */
    public Aging(int nuovoTipo, boolean globale) {
        logger.info("Costruttore");
        setTipo(nuovoTipo);
        setGlobale(globale);
    }

    /**
  	 * Delega la realizzazione della politica al metodo lru
  	 * @param int idProcesso - idProcesso, utile nel caso di politica locale
  	 * @param Memoria refMemoria - riferimento alla memoria principale
  	 * @return id della Struttura da rimpiazzare
  	 */
    public long scegli(int idProcesso, Memoria refMemoria) {
        logger.info("chiamo il metodo lru per cercare la Struttura �" + "da rimpiazzare");
        return lru().getId();
    }

    /**
  	 * Aggiorna la lista inserendo la nuova Struttura in coda se non � 
  	 * ancora presente rimpiazzando quello vecchio se presente
  	 * @param Struttura struct - Struttura da inserire nella lista
  	 */
    public void aggiorna(Struttura struct) {
        int pos = trovaStruttura(struct);
        logger.info("inserico,o modifico se presente,la Struttura parametro");
        if (pos >= 0) {
            listaStruttura.set(pos, struct);
        } else {
            listaStruttura.add(struct);
        }
    }
}
