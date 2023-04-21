package net.sourceforge.ondex.webservice2;

import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import net.sourceforge.ondex.core.AttributeName;
import net.sourceforge.ondex.core.CV;
import net.sourceforge.ondex.core.ConceptAccession;
import net.sourceforge.ondex.core.ConceptClass;
import net.sourceforge.ondex.core.ConceptName;
import net.sourceforge.ondex.core.EvidenceType;
import net.sourceforge.ondex.core.GDS;
import net.sourceforge.ondex.core.ONDEXConcept;
import net.sourceforge.ondex.core.ONDEXIterator;
import net.sourceforge.ondex.core.ONDEXView;
import net.sourceforge.ondex.exception.type.AccessDeniedException;
import net.sourceforge.ondex.exception.type.NullValueException;
import net.sourceforge.ondex.webservice2.Exceptions.AttributeNameNotFoundException;
import net.sourceforge.ondex.webservice2.Exceptions.CVNotFoundException;
import net.sourceforge.ondex.webservice2.Exceptions.CaughtException;
import net.sourceforge.ondex.webservice2.Exceptions.ConceptAccessionNotFoundException;
import net.sourceforge.ondex.webservice2.Exceptions.ConceptClassNotFoundException;
import net.sourceforge.ondex.webservice2.Exceptions.ConceptNameNotFoundException;
import net.sourceforge.ondex.webservice2.Exceptions.ConceptNotFoundException;
import net.sourceforge.ondex.webservice2.Exceptions.DeleteException;
import net.sourceforge.ondex.webservice2.Exceptions.EvidenceTypeNotFoundException;
import net.sourceforge.ondex.webservice2.Exceptions.GraphNotFoundException;
import net.sourceforge.ondex.webservice2.Exceptions.MetaDataNotFoundException;
import net.sourceforge.ondex.webservice2.Exceptions.NullViewException;
import net.sourceforge.ondex.webservice2.Exceptions.ReadOnlyException;
import net.sourceforge.ondex.webservice2.Exceptions.StartUpException;
import net.sourceforge.ondex.webservice2.api.ONDEXConceptWSApi;
import net.sourceforge.ondex.webservice2.result.WSCV;
import net.sourceforge.ondex.webservice2.result.WSConcept;
import net.sourceforge.ondex.webservice2.result.WSConceptAccession;
import net.sourceforge.ondex.webservice2.result.WSConceptClass;
import net.sourceforge.ondex.webservice2.result.WSConceptGDS;
import net.sourceforge.ondex.webservice2.result.WSConceptName;
import net.sourceforge.ondex.webservice2.result.WSEvidenceType;
import net.sourceforge.ondex.webservice2.result.WSAttributeName;

/**
 * Used to make sure all API methods that should be implemented in webservice are.
 *
 * @author Christian Brenninkmeijer
 * @deprecated Only used to make sure all API methods that should be implemented in webserviceare.
 */
public class ONDEXConceptWS implements ONDEXConcept, ONDEXConceptWSApi {

    WebServiceEngine engine;

    public ONDEXConceptWS() {
        engine = WebServiceEngine.getWebServiceEngine();
    }

    /**
     * Unimplemented Stub included only for automatic API syncronization checking
     */
    @WebMethod(exclude = true)
    @Override
    public ConceptAccession createConceptAccession(String accession, CV elementOf, boolean ambiguous) {
        throw new UnsupportedOperationException("Use webservice method.");
    }

    /**
	 * Creates a new {@link WSConceptAccession} and adds it to the
	 * {@link WSConcept}.
	 *
	 * @param graphId
	 *            the ID of the Graph
	 * @param conceptId
	 *            the ID of the {@link WSConcept} to add the ConceptAccession to
	 * @param accession
	 *            the accession of the {@link WSConceptAccession}
	 * @param elementOfCVId
	 *            the ID of the {@link WSCV} for the elementOf property
	 * @param isAmbiguous
	 *            whether the {@link WSConceptAccession} is ambiguous. The
	 *            default is true. (Optional)
	 * @return the accession of the {@link WSConceptAccession}
     * @throws GraphNotFoundException No Graph found with the given ID.
     * @throws MetaDataNotFoundException
     * @throws CVNotFoundException
     * @throws ReadOnlyException
     * @throws ConceptNotFoundException
     * @throws CaughtException
	 */
    @WebResult(name = "accession")
    @WebMethod(exclude = false)
    @Override
    public String createConceptAccession(@WebParam(name = "graphId") Long graphId, @WebParam(name = "conceptId") Integer conceptId, @WebParam(name = "accession") String accession, @WebParam(name = "elementOfCVId") String elementOfCVId, @WebParam(name = "isAmbiguous") Boolean isAmbiguous) throws GraphNotFoundException, MetaDataNotFoundException, CVNotFoundException, ReadOnlyException, ConceptNotFoundException, CaughtException {
        return engine.createConceptAccession(graphId, conceptId, accession, elementOfCVId, isAmbiguous);
    }

    /**
     * Unimplemented Stub included only for automatic API syncronization checking
     */
    @WebMethod(exclude = true)
    @Override
    public GDS<ONDEXConcept> createConceptGDS(AttributeName attrname, Object value, boolean doIndex) throws AccessDeniedException, NullValueException {
        throw new UnsupportedOperationException("Use webservice method.");
    }

    /**
	 * Creates a new {@link WSConceptGDS} and adds it to the {@link WSConcept}.
	 *
	 * @param graphId
	 *            the ID of the Graph
	 * @param conceptId
	 *            the ID of the , CaughtException{@link WSConcept} to add the GDS to
	 * @param attributeNameId
	 *            the {@link WSAttributeName} of the GDS
	 * @param value
	 *            the value of the GDS
	 * @return the ID of the {@link WSAttributeName}
     * @throws GraphNotFoundException No Graph found with the given ID.
     * @throws AttributeNameNotFoundException
     * @throws MetaDataNotFoundException
     * @throws ReadOnlyException
     * @throws ConceptNotFoundException
     * @throws CaughtException
	 */
    @WebResult(name = "attributeNameId")
    @WebMethod(exclude = false)
    @Override
    public String createConceptGDS(@WebParam(name = "graphId") Long graphId, @WebParam(name = "conceptId") Integer conceptId, @WebParam(name = "attributeNameId") String attributeNameId, @WebParam(name = "value") String value) throws GraphNotFoundException, AttributeNameNotFoundException, MetaDataNotFoundException, ReadOnlyException, ConceptNotFoundException, CaughtException {
        return engine.createConceptGDS(graphId, conceptId, attributeNameId, value);
    }

    /**
     * Unimplemented Stub included only for automatic API syncronization checking
     */
    @Override
    @WebMethod(exclude = true)
    public ConceptName createConceptName(String name, boolean isPreferred) {
        throw new UnsupportedOperationException("Use webservice method.");
    }

    /**
	 * Creates a new {@link WSConceptName} and adds it to the {@link WSConcept}.
	 *
	 * @param graphId
	 *            the ID of the Graph
	 * @param conceptId
	 *            the ID of the {@link WSConcept} to add the ConceptName to
	 * @param name
	 *            the name of the {@link WSConceptName}
	 * @param isPreferred
	 *            whether the {@link WSConceptName} is preferred. The default is
	 *            false. (Optional)
	 * @return the name of the {@link WSConceptName}
     * @throws GraphNotFoundException No Graph found with the given ID.
     * @throws ReadOnlyException
     * @throws ConceptNotFoundException
     * @throws CaughtException
	 */
    @WebResult(name = "name")
    @WebMethod(exclude = false)
    @Override
    public String createConceptName(@WebParam(name = "graphId") Long graphId, @WebParam(name = "conceptId") Integer conceptId, @WebParam(name = "name") String name, @WebParam(name = "isPreferred") Boolean isPreferred) throws GraphNotFoundException, ReadOnlyException, ConceptNotFoundException, CaughtException {
        return engine.createConceptName(graphId, conceptId, name, isPreferred);
    }

    /**
     * Unimplemented Stub included only for automatic API syncronization checking
     */
    @WebMethod(exclude = true)
    @Override
    public ConceptAccession deleteConceptAccession(String accession, CV elementOf) {
        throw new UnsupportedOperationException("Use webservice method.");
    }

    @Override
    public String deleteConceptAccession(@WebParam(name = "graphId") Long graphId, @WebParam(name = "conceptId") Integer conceptId, @WebParam(name = "accession") String accession, @WebParam(name = "elementOfCVId") String elementOfCVId) throws GraphNotFoundException, MetaDataNotFoundException, CVNotFoundException, ReadOnlyException, ConceptNotFoundException, ConceptAccessionNotFoundException, CaughtException {
        engine.deleteConceptAccession(graphId, conceptId, accession, elementOfCVId);
        return "done";
    }

    /**
     * Unimplemented Stub included only for automatic API syncronization checking
     */
    @WebMethod(exclude = true)
    @WebResult(name = "success")
    @Override
    public GDS<ONDEXConcept> deleteConceptGDS(AttributeName attrname) {
        throw new UnsupportedOperationException("Use webservice method.");
    }

    @Override
    public String deleteConceptGDS(@WebParam(name = "graphId") Long graphId, @WebParam(name = "conceptId") Integer conceptId, @WebParam(name = "attributeNameId") String attributeNameId) throws GraphNotFoundException, MetaDataNotFoundException, CVNotFoundException, ReadOnlyException, ConceptNotFoundException, ConceptAccessionNotFoundException, CaughtException {
        engine.deleteConceptAccession(graphId, conceptId, attributeNameId, attributeNameId);
        return "done";
    }

    /**
     * Unimplemented Stub included only for automatic API syncronization checking
     */
    @WebMethod(exclude = true)
    @WebResult(name = "success")
    @Override
    public ConceptName deleteConceptName(String name) {
        throw new UnsupportedOperationException("Use webservice method.");
    }

    @Override
    public String deleteConceptName(@WebParam(name = "graphId") Long graphId, @WebParam(name = "conceptId") Integer conceptId, @WebParam(name = "name") String name) throws GraphNotFoundException, ReadOnlyException, ConceptNotFoundException, ConceptNameNotFoundException, CaughtException {
        engine.deleteConceptName(graphId, conceptId, name);
        return "done";
    }

    /**
     * Unimplemented Stub included only for automatic API syncronization checking
     */
    @WebMethod(exclude = true)
    @Override
    public String getAnnotation() {
        throw new UnsupportedOperationException("Use webservice method.");
    }

    /**
 	 * Returns the annotation of this instance of {@link WSConcept}.
     *
	 * @param graphId
	 *            the ID of the Graph
	 * @param conceptId
	 *            the ID of the {@link WSConcept}
	 * @return annotation String
     * @throws GraphNotFoundException
     * @throws ConceptNotFoundException
     * @throws CaughtException
     */
    @WebMethod(exclude = false)
    @Override
    @WebResult(name = "annotation")
    public String getAnnotation(@WebParam(name = "graphId") Long graphId, @WebParam(name = "conceptId") Integer conceptId) throws GraphNotFoundException, ConceptNotFoundException, CaughtException {
        return engine.getAnnotation(graphId, conceptId);
    }

    /**
     * Unimplemented Stub included only for automatic API syncronization checking
     */
    @WebMethod(exclude = true)
    @Override
    public ConceptAccession getConceptAccession(String accession, CV elementOf) {
        throw new UnsupportedOperationException("Use webservice method.");
    }

    @Override
    public WSConceptAccession getConceptAccession(@WebParam(name = "graphId") Long graphId, @WebParam(name = "conceptId") Integer conceptId, @WebParam(name = "accession") String accession, @WebParam(name = "elementOfCVId") String elementOfCVId) throws GraphNotFoundException, MetaDataNotFoundException, CVNotFoundException, ConceptNotFoundException, CaughtException {
        engine.getConceptAccession(graphId, conceptId, accession, elementOfCVId);
        return null;
    }

    /**
     * Unimplemented Stub included only for automatic API syncronization checking
     */
    @WebMethod(exclude = true)
    @Override
    public ONDEXIterator<ConceptAccession> getConceptAccessions() {
        throw new UnsupportedOperationException("Use webservice method.");
    }

    /**
	 * Returns all the {@link WSConceptAccession} in the specified
	 * {@link WSConcept}.
	 *
	 * @param graphId
	 *            the ID of the Graph
	 * @param conceptId
	 *            the ID of the {@link WSConcept}
	 * @return all the {@link WSConceptAccession} in the specified
	 *         {@link WSConcept}
     * @throws GraphNotFoundException No Graph found with the given ID.
     * @throws ConceptNotFoundException
     * @throws CaughtException
	 */
    @WebResult(name = "conceptAccessions")
    @WebMethod(exclude = false)
    @Override
    public List<WSConceptAccession> getConceptAccessions(@WebParam(name = "graphId") Long graphId, @WebParam(name = "conceptId") Integer conceptId) throws GraphNotFoundException, ConceptNotFoundException, NullViewException, CaughtException {
        return engine.getConceptAccessions(graphId, conceptId);
    }

    /**
     * Unimplemented Stub included only for automatic API syncronization checking
     */
    @WebMethod(exclude = true)
    @Override
    public GDS<ONDEXConcept> getConceptGDS(AttributeName attrname) {
        throw new UnsupportedOperationException("Use webservice method.");
    }

    /**
	 * Returns all the {@link WSConceptGDS} in the specified {@link WSConcept}.
	 *
	 * @param graphId
	 *            the ID of the Graph
	 * @param conceptId
	 *            the ID of the {@link WSConcept}
	 * @return all the {@link WSConceptGDS} in the specified {@link WSConcept}
     * @throws GraphNotFoundException No Graph found with the given ID.
     * @throws ConceptNotFoundException
     * @throws CaughtException
	 */
    @WebResult(name = "conceptGDSs")
    @WebMethod(exclude = false)
    @Override
    public List<WSConceptGDS> getConceptGDSs(@WebParam(name = "graphId") Long graphId, @WebParam(name = "conceptId") Integer conceptId) throws GraphNotFoundException, ConceptNotFoundException, NullViewException, CaughtException {
        return engine.getConceptGDSs(graphId, conceptId);
    }

    /**
     * Unimplemented Stub included only for automatic API syncronization checking
     */
    @WebMethod(exclude = true)
    @Override
    public ONDEXIterator<GDS<ONDEXConcept>> getConceptGDSs() {
        throw new UnsupportedOperationException("Use webservice method.");
    }

    /**
	 * Returns the {@link WSConceptGDS} with the specified
	 * {@link WSAttributeName} from the {@link WSConcept}.
	 *
	 * @param graphId
	 *            the ID of the Graph
	 * @param conceptId
	 *            the ID of the {@link WSConcept}
	 * @param attributeNameId
	 *            the ID of the {@link WSAttributeName}
	 * @return the {@link WSConceptGDS} with the specified
	 *         {@link WSAttributeName}
     * @throws GraphNotFoundException No Graph found with the given ID.
     * @throws AttributeNameNotFoundException
     * @throws MetaDataNotFoundException
     * @throws ConceptNotFoundException
     * @throws CaughtException
	 */
    @WebResult(name = "conceptGDS")
    @WebMethod(exclude = false)
    @Override
    public WSConceptGDS getConceptGDS(@WebParam(name = "graphId") Long graphId, @WebParam(name = "conceptId") Integer conceptId, @WebParam(name = "attributeNameId") String attributeNameId) throws GraphNotFoundException, AttributeNameNotFoundException, MetaDataNotFoundException, ConceptNotFoundException, CaughtException {
        return engine.getConceptGDS(graphId, conceptId, attributeNameId);
    }

    /**
     * Unimplemented Stub included only for automatic API syncronization checking
     */
    @WebMethod(exclude = true)
    @Override
    public ConceptName getConceptName() {
        throw new UnsupportedOperationException("Use webservice method.");
    }

    @Override
    public WSConceptName getConceptName(@WebParam(name = "graphId") Long graphId, @WebParam(name = "conceptId") Integer conceptId) throws GraphNotFoundException, ConceptNotFoundException, CaughtException {
        engine.getConceptName(graphId, conceptId);
        return null;
    }

    /**
     * Unimplemented Stub included only for automatic API syncronization checking
     */
    @WebMethod(exclude = true)
    @Override
    public ConceptName getConceptName(String name) {
        throw new UnsupportedOperationException("Use webservice method getConceptNameWithName.");
    }

    /**
	 * Returns the {@link WSConceptName} with the specified name from the
	 * {@link WSConcept}.
	 *
	 * @param graphId
	 *            the ID of the Graph
	 * @param conceptId
	 *            the ID of the {@link WSConcept}
	 * @param name
	 *            the name of the {@link WSConceptName}
	 * @return the {@link WSConceptName} with the specified name
     * @throws GraphNotFoundException No Graph found with the given ID.
     * @throws ConceptNotFoundException
     * @throws CaughtException
	 */
    @WebResult(name = "conceptName")
    @WebMethod(exclude = false)
    @Override
    public WSConceptName getConceptNameWithName(@WebParam(name = "graphId") Long graphId, @WebParam(name = "conceptId") Integer conceptId, @WebParam(name = "name") String name) throws GraphNotFoundException, ConceptNotFoundException, CaughtException {
        return engine.getConceptNameWithName(graphId, conceptId, name);
    }

    /**
     * Unimplemented Stub included only for automatic API syncronization checking
     */
    @WebMethod(exclude = true)
    @Override
    public ONDEXIterator<ConceptName> getConceptNames() {
        throw new UnsupportedOperationException("Use webservice method.");
    }

    /**
	 * Returns all the {@link WSConceptName} in the specified {@link WSConcept}.
	 *
	 * @param graphId
	 *            the ID of the Graph
	 * @param conceptId
	 *            the ID of the {@link WSConcept}
	 * @return all the {@link WSConceptName} in the specified {@link WSConcept}
     * @throws GraphNotFoundException No Graph found with the given ID.
     * @throws ConceptNotFoundException 
     * @throws CaughtException
	 */
    @WebResult(name = "conceptNames")
    @WebMethod(exclude = false)
    @Override
    public List<WSConceptName> getConceptNames(@WebParam(name = "graphId") Long graphId, @WebParam(name = "conceptId") Integer conceptId) throws GraphNotFoundException, ConceptNotFoundException, NullViewException, CaughtException {
        return engine.getConceptNames(graphId, conceptId);
    }

    /**
     * Unimplemented Stub included only for automatic API syncronization checking
     */
    @WebMethod(exclude = true)
    @Override
    public String getDescription() {
        throw new UnsupportedOperationException("Use webservice method.");
    }

    /**
	 * Returns the description of this instance of {@link WSConcept}.
	 *
	 * @param graphId
	 *            the ID of the Graph
	 * @param conceptId
	 *            the ID of the {@link WSConcept}
	 * @return the {@link WSConceptName} with the specified name
     * @throws GraphNotFoundException No Graph found with the given ID.
     * @throws ConceptNotFoundException
     * @throws CaughtException
	 */
    @WebResult(name = "descriptiom")
    @WebMethod(exclude = false)
    @Override
    public String getDescription(@WebParam(name = "graphId") Long graphId, @WebParam(name = "conceptId") Integer conceptId) throws GraphNotFoundException, ConceptNotFoundException, CaughtException {
        return engine.getDescription(graphId, conceptId);
    }

    /**
     * Unimplemented Stub included only for automatic API syncronization checking
     */
    @WebMethod(exclude = true)
    @Override
    public CV getElementOf() {
        throw new UnsupportedOperationException("Use webservice method.");
    }

    /**
	 * Returns the {@link WSCV}, which this {@link WSConcept} belongs to.
	 *
	 * @param graphId
	 *            the ID of the Graph
	 * @param conceptId
	 *            the ID of the {@link WSConcept}
	 * @return the {@link WSConceptName} with the specified name
     * @throws GraphNotFoundException No Graph found with the given ID.
     * @throws ConceptNotFoundException
     * @throws CaughtException
	 */
    @WebResult(name = "elementOf")
    @WebMethod(exclude = false)
    @Override
    public WSCV getElementOf(@WebParam(name = "graphId") Long graphId, @WebParam(name = "conceptId") Integer conceptId) throws GraphNotFoundException, ConceptNotFoundException, CVNotFoundException, CaughtException {
        return engine.getElementOf(graphId, conceptId);
    }

    /**
     * Unimplemented Stub included only for automatic API syncronization checking
     */
    @WebMethod(exclude = true)
    @Override
    public ConceptClass getOfType() {
        throw new UnsupportedOperationException("Use webservice method getOfTypeConcept.");
    }

    /**
	 * Returns the {@link WSConceptClass} this {@link WSConcept} belongs to.
	 *
	 * @param graphId
	 *            the ID of the Graph
	 * @param conceptId
	 *            the ID of the {@link WSConcept}
	 * @return the {@link WSConceptName} with the specified name
     * @throws GraphNotFoundException No Graph found with the given ID.
     * @throws ConceptNotFoundException
     * @throws CaughtException
	 */
    @WebResult(name = "typeOf")
    @WebMethod(exclude = false)
    @Override
    public WSConceptClass getOfTypeConcept(@WebParam(name = "graphId") Long graphId, @WebParam(name = "conceptId") Integer conceptId) throws GraphNotFoundException, ConceptNotFoundException, CaughtException {
        return engine.getOfTypeConcept(graphId, conceptId);
    }

    /**
     * Unimplemented Stub included only for automatic API syncronization checking
     */
    @WebMethod(exclude = true)
    @Override
    public String getPID() {
        throw new UnsupportedOperationException("Use webservice method.");
    }

    /**
	 * Returns the parser id of this instance of {@link WSConcept}.
	 *
	 * @param graphId
	 *            the ID of the Graph
	 * @param conceptId
	 *            the ID of the {@link WSConcept}
	 * @return the {@link WSConceptName} with the specified name
     * @throws GraphNotFoundException No Graph found with the given ID.
     * @throws ConceptNotFoundException
     * @throws CaughtException
	 */
    @WebResult(name = "pid")
    @WebMethod(exclude = false)
    @Override
    public String getPID(@WebParam(name = "graphId") Long graphId, @WebParam(name = "conceptId") Integer conceptId) throws GraphNotFoundException, ConceptNotFoundException, CaughtException {
        return engine.getPID(graphId, conceptId);
    }

    /**
     * Unimplemented Stub included only for automatic API syncronization checking
     */
    @WebMethod(exclude = true)
    @Override
    public boolean inheritedFrom(ConceptClass cc) {
        throw new UnsupportedOperationException("Use webservice method inheritedFromConcept.");
    }

    /**
	 * Returns whether this {@link WSConcept} is inherited from the given {@link WSConceptClass}.
     *
	 * This is the case when its ofType {@link WSConceptClass}'
     * either equals the given {@link WSConceptClass} or is a transitive
	 * specialisation of the given {@link WSConceptClass}.
	 *
	 * @param graphId
	 *            the ID of the Graph
	 * @param conceptId
	 *            the ID of the {@link WSConcept}
	 * @param conceptClassId
	 *            the ID of the {@link WSConceptClass} for the ofType property
     *
     * @return whether the above holds.
	 *
	 * @throws GraphNotFoundException No Graph found with the given ID.
     * @throws ConceptNotFoundException
     * @throws MetaDataNotFoundException
     * @throws ConceptClassNotFoundException
     * @throws CaughtException
	 */
    @WebResult(name = "inheritedFrom")
    @WebMethod(exclude = false)
    @Override
    public boolean inheritedFromConcept(@WebParam(name = "graphId") Long graphId, @WebParam(name = "conceptId") Integer conceptId, @WebParam(name = "conceptClassId") String conceptClassId) throws GraphNotFoundException, ConceptNotFoundException, MetaDataNotFoundException, ConceptClassNotFoundException, CaughtException {
        return engine.inheritedFromConcept(graphId, conceptId, conceptClassId);
    }

    /**
     * Unimplemented Stub included only for automatic API syncronization checking
     */
    @WebMethod(exclude = true)
    @WebResult(name = "success")
    @Override
    public void setAnnotation(String annotation) {
        throw new UnsupportedOperationException("Use webservice method.");
    }

    /**
	 * Sets the annotation of this instance of {@link WSConcept}.
	 *
	 *
	 * @param graphId
	 *            the ID of the Graph
	 * @param conceptId
	 *            the ID of the {@link WSConcept}
	 * @param annotation
	 *            the annotation to set.
     * @return String text confirming success or throws an exception.
     * @throws GraphNotFoundException No Graph found with the given ID.
     * @throws ConceptNotFoundException
     * @throws CaughtException
	 */
    @WebMethod(exclude = false)
    @WebResult(name = "success")
    @Override
    public String setAnnotation(@WebParam(name = "graphId") Long graphId, @WebParam(name = "conceptId") Integer conceptId, @WebParam(name = "annotation") String annotation) throws GraphNotFoundException, ConceptNotFoundException, CaughtException {
        return engine.setAnnotation(graphId, conceptId, annotation);
    }

    /**
     * Unimplemented Stub included only for automatic API syncronization checking
     */
    @WebMethod(exclude = true)
    @Override
    public void setDescription(String description) {
        throw new UnsupportedOperationException("Use webservice method.");
    }

    /**
	 * Sets the description of this instance of {@link WSConcept}.
	 *
	 * @param graphId
	 *            the ID of the Graph
	 * @param conceptId
	 *            the ID of the {@link WSConcept}
	 * @param description
	 *            the description to set
     * @return String text confirming success or throws an exception.
     * @throws GraphNotFoundException No Graph found with the given ID.
     * @throws ConceptNotFoundException
     * @throws CaughtException
	 */
    @WebMethod(exclude = false)
    @WebResult(name = "success")
    @Override
    public String setDescription(@WebParam(name = "graphId") Long graphId, @WebParam(name = "conceptId") Integer conceptId, @WebParam(name = "description") String description) throws GraphNotFoundException, ConceptNotFoundException, CaughtException {
        return engine.setDescription(graphId, conceptId, description);
    }

    /**
     * Unimplemented Stub included only for automatic API syncronization checking
     * @throws AccessDeniedException
     * @throws NullValueException 
     */
    @WebMethod(exclude = true)
    @WebResult(name = "success")
    @Override
    public void addContext(ONDEXConcept ac) throws AccessDeniedException, NullValueException {
        throw new UnsupportedOperationException("Use webservice method addTagConcept.");
    }

    /**
	 * Adds an {@link WSConcept} to the tag of this {@link WSConcept}.
	 *
	 * @param graphId
	 *            the ID of the Graph
	 * @param conceptId
	 *            the ID of the {@link WSConcept} to add to {@link WSConcept}
	 * @param tagId
	 *             the ID of the tag being added {@link WSConcept}
     * @return String text confirming success or throws an exception.
     * @throws GraphNotFoundException No Graph found with the given ID.
     * @throws ConceptNotFoundException
     * @throws ReadOnlyException
     * @throws CaughtException
	 */
    @WebMethod(exclude = false)
    @WebResult(name = "success")
    @Override
    public String addTagConcept(@WebParam(name = "graphId") Long graphId, @WebParam(name = "conceptId") Integer conceptId, @WebParam(name = "tagId") Integer tagId) throws GraphNotFoundException, ConceptNotFoundException, ReadOnlyException, CaughtException {
        return engine.addTagConcept(graphId, conceptId, tagId);
    }

    /**
     * Unimplemented Stub included only for automatic API syncronization checking
     */
    @WebMethod(exclude = true)
    @Override
    public void addEvidenceType(EvidenceType evidencetype) {
        throw new UnsupportedOperationException("Use webservice method addEvidenceTypeConcept.");
    }

    /**
	 * Adds a given {@link WSEvidenceType} to the {@link WSConcept}.
	 *
	 * @param graphId
	 *            the ID of the Graph
	 * @param conceptId
	 *            the ID of the {@link WSConcept} to add to {@link WSConcept}
	 * @param evidenceTypeId
	 *             the ID of the {@link WSEvidenceType} being added.
     * @return String text confirming success or throws an exception.
     * @throws GraphNotFoundException No Graph found with the given ID.
     * @throws ConceptNotFoundException
     * @throws ReadOnlyException
     * @throws CaughtException
	 */
    @WebMethod(exclude = false)
    @WebResult(name = "success")
    @Override
    public String addEvidenceTypeConcept(@WebParam(name = "graphId") Long graphId, @WebParam(name = "conceptId") Integer conceptId, @WebParam(name = "evidenceTypeId") String evidenceTypeId) throws GraphNotFoundException, ConceptNotFoundException, ReadOnlyException, MetaDataNotFoundException, EvidenceTypeNotFoundException, CaughtException {
        return engine.addEvidenceTypeConcept(graphId, conceptId, evidenceTypeId);
    }

    /**
     * Unimplemented Stub included only for automatic API syncronization checking
     */
    @WebMethod(exclude = true)
    @Override
    public ONDEXView<ONDEXConcept> getContext() {
        throw new UnsupportedOperationException("Use webservice method getTagConcept.");
    }

    @Override
    public List<WSConcept> getTagConcept(@WebParam(name = "graphId") Long graphId, @WebParam(name = "conceptId") Integer conceptId) throws GraphNotFoundException, ConceptNotFoundException, NullViewException, CaughtException {
        engine.getTagConcept(graphId, conceptId);
        return null;
    }

    /**
     * Unimplemented Stub included only for automatic API syncronization checking
     */
    @WebMethod(exclude = true)
    @Override
    public ONDEXIterator<EvidenceType> getEvidence() {
        throw new UnsupportedOperationException("Use webservice method getEvidenceConcept.");
    }

    /**
	 * Returns a list of all evidences for this {@link WSConcept}.
	 *
	 * @param graphId
	 *            the ID of the Graph
	 * @param conceptId
	 *            the ID of the {@link WSConcept}
	 * @return A list of the EvidenceTypes {@link WSEvidenceType}
     * @throws GraphNotFoundException No Graph found with the given ID.
     * @throws ConceptNotFoundException
     * @throws CaughtException
	 */
    @WebResult(name = "evidence")
    @WebMethod(exclude = false)
    @Override
    public List<WSEvidenceType> getEvidenceConcept(@WebParam(name = "graphId") Long graphId, @WebParam(name = "conceptId") Integer conceptId) throws GraphNotFoundException, ConceptNotFoundException, NullViewException, MetaDataNotFoundException, CaughtException {
        return engine.getEvidenceConcept(graphId, conceptId);
    }

    /**
     * Unimplemented Stub included only for automatic API syncronization checking
     */
    @WebMethod(exclude = true)
    @Override
    public int getId() {
        throw new UnsupportedOperationException("Not supported by webservice.");
    }

    /**
     * Unimplemented Stub included only for automatic API syncronization checking
     */
    @WebMethod(exclude = true)
    @Override
    public boolean removeContext(ONDEXConcept ac) {
        throw new UnsupportedOperationException("Use webservice method removeTagConcept.");
    }

    @Override
    public String removeTagConcept(@WebParam(name = "graphId") Long graphId, @WebParam(name = "conceptId") Integer conceptId, @WebParam(name = "tagId") Integer tagId) throws GraphNotFoundException, ConceptNotFoundException, NullViewException, ReadOnlyException, DeleteException, CaughtException {
        engine.removeTagConcept(graphId, conceptId, tagId);
        return "done";
    }

    /**
     * Unimplemented Stub included only for automatic API syncronization checking
     */
    @WebMethod(exclude = true)
    @Override
    public boolean removeEvidenceType(EvidenceType evidencetype) {
        throw new UnsupportedOperationException("Use webservice method removeEvidenceTypeConcept.");
    }

    @Override
    public String removeEvidenceTypeConcept(@WebParam(name = "graphId") Long graphId, @WebParam(name = "conceptId") Integer conceptId, @WebParam(name = "evidenceTypeId") String evidenceTypeId) throws GraphNotFoundException, ConceptNotFoundException, NullViewException, ReadOnlyException, MetaDataNotFoundException, EvidenceTypeNotFoundException, CaughtException {
        engine.removeEvidenceTypeConcept(graphId, conceptId, evidenceTypeId);
        return "done";
    }

    /**
     * Unimplemented Stub included only for automatic API syncronization checking
     */
    @WebMethod(exclude = true)
    @Override
    public long getSID() {
        throw new UnsupportedOperationException("Not supported in webservice.");
    }
}
