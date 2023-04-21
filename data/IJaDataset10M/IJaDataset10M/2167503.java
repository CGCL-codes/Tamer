package com.gestioni.adoc.aps.system.services.repository;

import javax.jcr.Node;
import javax.jcr.Session;

/**
 * Interfaccia per la definizione dei metodi di uso generale del repository
 * @author dani
 *
 */
public interface IJcrRepositoryDAO {

    public static final String TYPE_NODE_FASCICOLO = "adoc:fascicolo";

    public static final String TYPE_NODE_DOCUMENTO = "adoc:documento";

    public static final String TYPE_NODE_DOCUMENTO_PERSONALE = "adoc:documentoPersonale";

    public static final String TYPE_NODE_NODO_TITOLARIO = "adoc:nodoTitolario";

    public static final String TYPE_NODE_PROTOCOLLO = "adoc:protocollo";

    public static final String TYPE_NODE_PECMAIL = "adoc:pecmail";

    /**
	 * Metodo per l'inizializzazione del repository. Attraverso questo metodo,
	 * il sistema controlla la congruenza della struttura base del repository
	 * con le esigenze richieste dal sistema. Verifica quindi la presenta dei nodi
	 * base a cui il sistema si appoggia per l'inserimento dei dati
	 * provenienti dal portale
	 *
	 * FIXME Tutti i metodi al momento utilizzano delle finte credenziali
	 * @throws Throwable
	 */
    public void init() throws Throwable;

    /**
	 * Motodo per l'estrazione di un nodo(jcr) generico dato l' identificativo
	 * univoco uuid
	 * @param session La sessione di collegamento con il jcr-repository
	 * @param uuid Identificativo univoco del nodo JCR-Repository
	 * @return Il nodo ricercato.
	 */
    public Node getNodeByUUID(Session session, String uuid);

    /**
	 * Metodo che restituisce un nodo del repository dato il path del nodo richiesto
	 * @param session La sessione di collegamento con il jcr-repository
	 * @param pathNode Il path del nodo ricercato (Es: /TitRoot/1/1.01)
	 * @return Il nodo ricercato.
	 */
    public Node getNodeByPath(Session session, String pathNode);

    /**
	 * Rimuove un nodo dato il sui identificativo univoco generato da jackrabbit
	 * @param credentials Le credenziali d'accesso al JCR-Repository
	 * @param uuid Identificativo univoco del nodo JCR-Repository
	 */
    public void removeByUUID(String userIdentifier, String uuid);

    /**
	 * Rimuove un nodo dato il sui identificativo univoco generato da jackrabbit
	 * ATTENZIONE: la cancellazione non è effettiva. Occorre salvare la sessione per renderla effettiva
	 * @param credentials Le credenziali d'accesso al JCR-Repository
	 * @param uuid Identificativo univoco del nodo JCR-Repository
	 */
    public void removeByUUIDinTransaction(Session session, String uuid);

    /**
	 * Modoto per la visualizzazione per la fase di SVILUPPO di tutto l'albero del repository
	 * @param userIdentifier
	 */
    public void viewTree(String userIdentifier);
}
