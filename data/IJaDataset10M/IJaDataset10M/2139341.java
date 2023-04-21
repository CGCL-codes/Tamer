package listaspesa.shared.services;

import listaspesa.shared.dto.NegozioDTO;
import com.google.gwt.user.client.rpc.RemoteService;

/**
 * Questa interfaccia contiene i servizi relativi ai negozi.
 * 
 * @author dario
 * 
 */
public interface INegozioService extends RemoteService {

    /**
	 * Crea un nuovo negozio a partire dai dati contenuti nel DTO passato come
	 * parametro.
	 * 
	 * @param negozioDTO
	 *            il DTO contenente i dati del nuovo negozio.
	 * @return il DTO con i dati del negozio appena inserito. Il campo id sarà
	 *         impostato al valore generato automaticamente all'inserimento nel
	 *         database.
	 * @throws RPCException
	 *             Se si verifica un errore durante la chiamata al server viene
	 *             sollevata questa eccezione contenente il messaggio di errore.
	 */
    NegozioDTO creaNegozio(NegozioDTO negozioDTO) throws RPCException;

    /**
	 * Aggiunge un nuovo prodotto all'elenco dei prodotti venduti dal negozio.
	 * 
	 * @param negozioId
	 *            l'identificativo del negozio.
	 * @param prodottoId
	 *            l'identificativo del prodotto.
	 * @param prezzo
	 *            il prezzo del prodotto.
	 * @throws RPCException
	 *             Se si verifica un errore durante la chiamata al server viene
	 *             sollevata questa eccezione contenente il messaggio di errore.
	 */
    void aggiungiProdotto(String negozioId, String prodottoId, Double prezzo) throws RPCException;

    /**
	 * Rimuove un prodotto dall'elenco dei prodotti venduti dal negozio.
	 * 
	 * @param negozioId
	 *            l'identificativo del negozio.
	 * @param prodottoId
	 *            l'identificativo del prodotto.
	 * @throws RPCException
	 *             Se si verifica un errore durante la chiamata al server viene
	 *             sollevata questa eccezione contenente il messaggio di errore.
	 */
    void rimuoviProdotto(String negozioId, String prodottoId) throws RPCException;

    /**
	 * Elimina un negozio dal database.
	 * 
	 * @param negozioId
	 *            l'identificativo del negozio.
	 * @throws RPCException
	 *             Se si verifica un errore durante la chiamata al server viene
	 *             sollevata questa eccezionse contenente il messaggio di errore.
	 */
    void eliminaNegozio(String negozioId) throws RPCException;
}
