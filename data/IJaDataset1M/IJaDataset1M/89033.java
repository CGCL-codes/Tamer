package ru.ksu.niimm.cll.mocassin.crawl.parser.gate;

import gate.Document;
import gate.util.GateException;
import java.io.File;
import java.util.List;
import ru.ksu.niimm.cll.mocassin.util.GateDocumentMetadata;

public interface GateDocumentDAO {

    /**
	 * add a given file as GATE with a given id
	 * 
	 * @param documentId
	 * @param file
	 * @throws PersistenceException
	 */
    void save(String documentId, File file, String encoding) throws AccessGateStorageException, PersistenceException;

    /**
	 * delete a file with a given id if it exists; otherwise, nothing will be
	 * done
	 * 
	 * @param documentId
	 * @throws AccessGateDocumentException
	 * @throws PersistenceException
	 */
    void delete(String documentId) throws AccessGateDocumentException, PersistenceException;

    /**
	 * returns identifiers of all the documents from given corpus (see
	 * parameters in a configuration file)
	 * 
	 * @return
	 * @throws AccessGateStorageException
	 * @throws GateException
	 */
    List<String> getDocumentIds() throws AccessGateDocumentException, AccessGateStorageException;

    /**
	 * returns document with given id (e.g. <i>math/0410002</i>); <br>
	 * IMPORTANT: call of 'release' method ({@link #release(Document)}) is
	 * required after the work with opened document has finished
	 * 
	 * @param documentId
	 * @return
	 * @throws AccessGateStorageException
	 * @throws GateException
	 */
    Document load(String documentId) throws AccessGateDocumentException, AccessGateStorageException;

    /**
	 * releases resources connected with given document. <br/>
	 * If the document is null, the method does nothing
	 * 
	 * @param document
	 */
    void release(Document document);

    /**
	 * loads the metadata of a document with a given id
	 * 
	 * @param documentId
	 * @return
	 * @throws AccessGateDocumentException
	 * @throws AccessGateStorageException
	 */
    GateDocumentMetadata loadMetadata(String documentId) throws AccessGateDocumentException, AccessGateStorageException;
}
