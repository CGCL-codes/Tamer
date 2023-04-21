package openschool.domain.dao;

import java.util.List;
import openschool.domain.model.Cours;
import openschool.domain.model.Document;
import openschool.domain.model.Formation;

/**
 * DAO for the documents table
 * 
 * @author Maha
 *
 */
public interface DocumentDAO {

    /**
	 * Update a document
	 * @param doc
	 */
    void updateDocument(Document doc);

    /**
	 * Save a document
	 * @param doc
	 */
    void saveDocument(Document doc);

    Document getDocument(Long idDocument);

    List<Document> findDocumentByCours(Long idCours);
}
