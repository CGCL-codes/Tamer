package org.xmi.repository;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import org.xmi.filter.InfosetFilter;
import org.xmi.model.Element;
import org.xmi.model.Model;
import org.xmi.model.ModelStatistic;

/**
 * Connection for the Model in the Repository <p>
 * When a version can not be found the latest available will be taken.<br/>
 * The Objects returned will not be the same instances in subsequent calls.
 */
public interface RepositoryConnection extends InfosetFilter {

    /**
     * the head version number
     */
    public static final int HEAD = -1;

    /**
	 * Close the Connection
	 * @throws IOException
	 * @throws RepositoryException
	 */
    public void close() throws IOException, RepositoryException;

    /**
	 * Commit the Connection
	 * @throws IOException
	 * @throws RepositoryException
	 */
    public void commit() throws IOException, RepositoryException;

    /**
	 * Rollback the Connection
	 * @throws IOException
	 * @throws RepositoryException
	 */
    public void rollback() throws IOException, RepositoryException;

    /**
	 * Create a new Element Instance
	 * @param parent
	 * @param id
	 * @param name
	 * @param type
	 * @param namespace
	 * @param values
	 * @return
	 * @throws IOException
	 * @throws RepositoryException
	 */
    public Element createElement(Element parent, String id, String name, String type, String namespace, Map<String, Collection<String>> values) throws IOException, RepositoryException;

    /**
     * Create a new Model instance
     * @param id
     * @param name
     * @param targetNamespace
     * @param namespaces
     * @param comment
     * @return
     * @throws IOException
     * @throws RepositoryException
     */
    public Model createModel(String id, String name, String targetNamespace, Map<String, String> namespaces, String comment) throws IOException, RepositoryException;

    /**
	 * The Model for this Connection
	 * @return the model
	 * @throws IOException
	 * @throws RepositoryException
	 */
    public Model getModel() throws IOException, RepositoryException;

    /**
	 * Element by id
	 * @param id the element id
	 * @return the element
	 * @throws IOException
	 * @throws RepositoryException
	 */
    public Element getElement(String id) throws IOException, RepositoryException;

    /**
	 * Save the Model
	 * @param document
	 * @throws IOException
	 * @throws RepositoryException
	 */
    public void saveModel(Model model) throws IOException, RepositoryException;

    /**
	 * Save the Element
	 * @param element
	 * @throws IOException
	 * @throws RepositoryException
	 */
    public void saveElement(Element element) throws IOException, RepositoryException;

    /**
	 * Evaluate the ModelPath
	 * @param the path
	 * @return the element list
	 * @throws IOException
	 * @throws RepositoryException
	 */
    public Collection<Element> eval(String path) throws IOException, RepositoryException;

    /**
	 * Document Statistic
	 * @param documentId the model id
	 * @return
	 * @throws IOException
	 * @throws ModelNotFoundException
	 */
    public ModelStatistic getStatistic() throws IOException, ModelNotFoundException, RepositoryException;

    /**
	 * The class name of the Driver
	 * @return
	 */
    @SuppressWarnings("unchecked")
    public Class getImplementationClass();
}
