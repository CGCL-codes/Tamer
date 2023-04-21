package progranet.model.service;

import java.util.List;
import java.util.Map;
import progranet.ganesa.metamodel.Account;
import progranet.ganesa.metamodel.Facet;
import progranet.omg.core.types.NamedElement;
import progranet.omg.core.types.Property;
import progranet.omg.core.types.Type;

/**
 * Definition of data import and export operations available for remote clients.
 * 
 * @author jmech
 */
public interface XMLRemoteService {

    /**
	 * Gets schema of the model for the supplied facet.
	 * 
	 * @param facet	The schema facet.
	 * @return	XML representing schema and converted to set of bytes.
	 */
    byte[] getModelSchema(Facet facet);

    /**
     * Checks if indicated user exists within the supplied facet.
     * 
     * @param aUser	User name.
     * @param aPassword	User password.
     * @param facet	Facet to check in.
     * @return	User's account identifier or empty string if user doesn't exist.
     */
    String checkUser(String aUser, String aPassword, Facet facet);

    /**
     * TODO: document method
     * 
     * @param aQuery
     * @param facet
     * @return
     */
    byte[] exportXmlFromQuery(String aQuery, Facet facet);

    /**
     * TODO: document method
     * 
     * @param aClassName
     * @param facet
     * @return
     */
    byte[] exportXmlForClass(String aClassName, Facet facet);

    /**
     * TODO: document method
     * 
     * @param aClassName
     * @param propertyName
     * @param simpleFilter
     * @param references
     * @param depht
     * @param facet
     * @return
     */
    byte[] exportXmlForClass(String aClassName, String propertyName, String simpleFilter, String references, int depht, Facet facet);

    /**
     * TODO: document method
     * 
     * @param clazz
     * @param simple
     * @param filterReferences
     * @param facet
     * @return
     */
    byte[] exportXmlForCriteria(Type clazz, String simple, Map<Property, List<Object>> filterReferences, Facet facet);

    /**
     * TODO: document method
     * 
     * @param clazz
     * @param root
     * @param simple
     * @param filterReferences
     * @param depth
     * @param facet
     * @return
     */
    byte[] exportXmlForCriteria(Type clazz, NamedElement root, String simple, Map<Property, List<Object>> filterReferences, int depth, Facet facet);

    /**
     * TODO: document method
     * 
     * @param account
     * @param list
     * @param root
     * @param depth
     * @param facet
     * @return
     */
    byte[] exportXml(Account account, List<? extends progranet.omg.cmof.Element> list, NamedElement root, int depth, Facet facet);

    /**
     * TODO: document method
     * 
     * @param aXml
     * @param facetId
     * @return
     */
    byte[] importXml(byte[] aXml, String facetId);
}
