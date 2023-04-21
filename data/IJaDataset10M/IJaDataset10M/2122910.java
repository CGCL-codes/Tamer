package org.mandarax.zkb;

import org.jdom.Document;
import org.mandarax.kernel.KnowledgeBase;
import org.mandarax.util.logging.LogCategories;

/**
 * Interface for zkb driver that can import / export knowledge bases from/to
 * xml data sources. XML data sources are represented by JDOM objects.
 * Similar to XKB drivers, but does not store objects in the XML but manages them
 * in a map that is stored separatly in a resource file. This is done by a delagate called Object Persistency Service. 
 * The main xml file and the resource file plus additional meta information are then wrapped together in a zip archive.
 * @author <A href="http://www-ist.massey.ac.nz/JBDietrich" target="_top">Jens Dietrich</A>
 * @version 3.4 <7 March 05>
 * @since 2.2
 * @see ObjectPersistencyService
 */
public interface ZKBDriver extends LogCategories {

    public static final int NO_DTD_REF = 0;

    public static final int INTERNAL_DTD_REF = 1;

    public static final int EXTERNAL_DTD_REF = 2;

    public static final int JUST_INCLUDE_DTD = 3;

    /**
     * Export a knowledge base, i.e., convert it into an xml document.
     * All encountered object references should be registered with the ops.
     * @return an xml document
     * @param kb a knowledge base
     * @param ops an object persistency service
     * @throws an ZKBException is thrown if export fails
     */
    Document exportKnowledgeBase(KnowledgeBase kb, ObjectPersistencyService ops) throws ZKBException;

    /**
     * Get a short text describing the driver.
     * @return a text
     */
    String getDescription();

    /**
	 * Get the location (URL) of the associated DTD.
	 * @return an url
	 */
    String getURL4DTD();

    /**
     * Get the name of the driver.
     * @return a text
     */
    String getName();

    /**
     * Import a knowledge base, i.e., build it from an xml document.
     * @return a knowledge base
     * @param doc an xml document
     * @param ops the object persistency service
     * @throws an XKBException is thrown if import fails
     */
    KnowledgeBase importKnowledgeBase(Document doc, ObjectPersistencyService ops) throws ZKBException;

    /**
     * Indicates whether the driver (and the underlying xml format (=dtd))
     * supports auto facts.
     * @return a boolean
     */
    boolean supportsAutoFacts();

    /**
     * Indicates whether the driver (and the underlying xml format (=dtd))
     * supports clause sets.
     * @return a boolean
     */
    boolean supportsClauseSets();

    /**
     * Indicates whether the driver (and the underlying xml format (=dtd))
     * supports facts. (some formats might see facts as rules without body)
     * @return a boolean
     */
    boolean supportsFacts();

    /**
     * Indicates whether the driver (and the underlying xml format (=dtd))
     * supports functions.
     * @return a boolean
     */
    boolean supportsFunctions();

    /**
     * Indicates whether the driver (and the underlying xml format (=dtd))
     * supports the java semantics (e.g. JFunctions, JPredicate).
     * @return a boolean
     */
    boolean supportsJavaSemantics();

    /**
     * Indicates whether the driver (and the underlying xml format (=dtd))
     * supports multiple premises.
     * @return a boolean
     */
    boolean supportsMultiplePremises();

    /**
     * Indicates whether the driver (and the underlying xml format (=dtd))
     * supports multiple premises connected by OR.
     * @return a boolean
     */
    boolean supportsOrPremises();

    /**
     * Indicates whether the driver (and the underlying xml format (=dtd))
     * supports types.
     * @return a boolean
     */
    boolean supportsTypes();

    /**
     * Indicates whether the driver (and the underlying xml format (=dtd))
     * supports queries.
     * @return a boolean
     */
    boolean supportsQueries();

    /**
	 * Get the dtd reference policy.
	 * @return int
	 */
    public int getDtdRefPolicy();

    /**
	 * Sets the dtd reference policy.
	 * @param dtdRefPolicy The dtd reference policy to set
	 */
    public void setDtdRefPolicy(int dtdRefPolicy);
}
