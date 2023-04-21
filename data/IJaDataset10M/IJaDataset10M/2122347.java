package gate.creole.ontology;

/**
 * An OntologyTripleStore object can be used to directly manipulate the
 * triples that represent an ontology, if the ontology implementation 
 * supports this. The OntologyTripleStore object can be accessed using
 * the method {@link Ontology#getOntologyTripleStore}.
 * 
 * @author Johann Petrak
 */
public interface OntologyTripleStore {

    /** 
   * Add a triple with a non-literal triple object to the triple store. 
   *
   * @param subject 
   * @param predicate
   * @param object 
   */
    public void addTriple(ONodeID subject, OURI predicate, ONodeID object);

    /** 
   * Add a triple with a literal triple object to the triple store. 
   *
   * @param subject
   * @param predicate
   * @param object 
   */
    public void addTriple(ONodeID subject, OURI predicate, Literal object);

    /** 
   * Remove a statement(triple) with a non-literal triple object 
   * from the ontology triple
   * store. The null value can be used for any of the subject, predicate,
   * or object parameters as a wildcard indicator: in that case, triples
   * with any value for that parameter will be removed.
   * 
   * NOTE: if a null value is passed for the object parameter, triples with
   * any value for the object, no matter if it is a literal or non-literal
   * (including blank nodes) will be removed!!
   * 
   * @param subject
   * @param predicate
   * @param object 
   */
    public void removeTriple(ONodeID subject, OURI predicate, ONodeID object);

    /** 
   * Remove a statement(triple) with a literal triple object from the ontology triple
   * store. The null value can be used for any of the subject, predicate,
   * or object parameters as a wildcard indicator: in that case, triples
   * with any value for that parameter will be removed.
   * 
   * NOTE: if a null value is passed for the object parameter, triples with
   * any value for the object, no matter if it is a literal or non-literal
   * (including blank nodes) will be removed!!
   * 
   * @param subject
   * @param predicate
   * @param object 
   */
    public void removeTriple(ONodeID subject, OURI predicate, Literal object);

    /** 
   * Add a listener for ontology triple store additions and removals. 
   * The listener will get the parameters of the original addition or 
   * deletion request, not any indication of which concrete triples were 
   * actually added or removed!
   * <p>
   * NOTE: the listener will not get all events for modifications made 
   * through methods other than the direct triple addition and removal
   * methods of the OntologyTripleStore object.
   * 
   * @param listener  OntologyTripleStoreListener object
   */
    public void addOntologyTripleStoreListener(OntologyTripleStoreListener listener);

    /** 
   * Remove an existing listener for ontology triple store additions and removals. 
   * 
   * @param listener  OntologyTripleStoreListener object
   */
    public void removeOntologyTripleStoreListener(OntologyTripleStoreListener listener);
}
