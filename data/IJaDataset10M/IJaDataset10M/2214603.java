package DL.Ontology;

import DL.DataStore.*;

/**
 * Converter data store interface definition
 * 
 */
public interface IOntologyStore extends IDataStore {

    /** Open an ontology for modifications */
    void OpenOntology(String ontology) throws Exception;

    /** Close an opened ontology (and commit changes) */
    void CloseOntology() throws Exception;

    /** Get symbol table interface */
    ISymbolTable getSymbolTable() throws Exception;
}
