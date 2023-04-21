package logica.gestoreProcessi;

import logica.gestoreMemoria.InterfacciaMemoriaVirtuale;

/**
 * Rappresenta l'interfaccia di un processo, utlizzata dagli altri package
 * per accedere ai metodi del processo.
 *
 * @author Orlandin Marco
 * @version 1.1
 */
public interface InterfacciaProcesso {

    /**
	 * Alloca in memoria virtuale tutte le parti (dati, codice, area di lavoro) 
	 * definite per il processo.
	 */
    public void allocaTutto();

    /**
	 * Aggiunge un parte dati al processo.
	 * @param long dim dimensione della nuova parte.
	 * @param boolean swappable indica se la nuova parte può andare in swap.
	 */
    public void pushDati(long dim, boolean swappable);

    /**
	 * Aggiunge un parte codice al processo (queste parti non possono andare 
	 * in swap).
	 * @param long dim dimensione della nuova parte.
	 */
    public void pushCodice(long dim);

    /**
	 * Aggiunge un parte Area di Lavoro al processo.
	 * @param long dim dimensione della nuova parte.
	 * @param boolean swappable indica se la nuova parte può andare in swap.
	 */
    public void pushAreaLavoro(long dim, boolean swappable);

    /**
	 * Ritorna un interfaccia alla memoria virtuale del processo.
	 * @return memoria virtuale del processo.
	 */
    public InterfacciaMemoriaVirtuale getMemoriaVirtuale();

    /**
	 * Aggiunge una nuova attivita al processo.
	 * @param istante Indica l'istante di tempo in cui dovrò venire eseguita 
	 * l'azione.
	 * @param typeAction indica il tipo di azione da eseguire (riferisci, 
	 * modifica, condividi).
	 * @param target indica l'id della pagina\segmento su cui eseguire l'azione.
	 * @throws IstanteErrato. 
	 */
    public void pushAttivita(long target, int typeAction, int istante) throws IstanteErrato;

    /**
	 * Ritorna la somma delle dimensioni di tutte le parti dati definite.
	 * @return somma di tutte le parti dati.
	 */
    public long getDimDati();

    /**
	 * Ritorna la somma delle dimensioni di tutte le parti codice definite per 
	 * il processo, in caso non ve ne siano viene sollevata un eccezione. 
	 * Questo perché un processo deve avere sempre una parte codice.
	 * @return somma di tutte le parti codice.
	 * @throws ParteCodiceMancante .
	 */
    public long getDimCodice() throws ParteCodiceMancante;

    /**
	 * Ritorna la somma delle dimensioni di tutte le parti Area di Lavoro 
	 * definite.
	 * @return somma di tutte le parti area di lavoro.
	 */
    public long getDimAreaLavoro();

    /**
	/**
	 * Ritorna la dimensione del processo data dalla somma delle dimensione
	 * della parte codice, dati ed area di lavoro.
	 * @return Difmensione del processo.
	 */
    public long getDimProcesso();

    /**
	 * Ritorna il tempo di arrivo del processo.
	 * @return tempo di arrivo del processo.
	 */
    public int getTempoArrivo();

    /**
	 * Ritorna il tempo richiesto dal processo per terminare.
	 * @return tempo richiesto dal processo per terminare.
	 */
    public int getTempoRichiesto();

    /**
	 * Ritorna per quanto tempo ha eseguito il processo.
	 * @return Tempo eseguito dal processo.
	 */
    public int getTempoEseguito();

    /**
	 * Ritorna il nome del processo.
	 * @return nome del processo.
	 */
    public String getNome();

    /**
	 * Ritorna l'identificativo del processo.
	 * @return id del processo.
	 */
    public int getId();
}
