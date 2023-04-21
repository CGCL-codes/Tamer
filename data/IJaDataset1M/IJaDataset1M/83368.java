package au.edu.diasb.annotation.dannotate;

import java.util.Properties;
import javax.servlet.http.HttpServletRequest;
import org.json.JSONObject;
import org.openrdf.model.Resource;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import au.edu.diasb.chico.config.PropertyTree;
import au.edu.diasb.chico.mvc.RequestFailureException;
import au.edu.diasb.chico.mvc.RequestParameterException;

/**
 * First cut at Dannotate annotation schema support ... work in progress
 * 
 * @author scrawley
 */
public interface AnnotationSchema {

    /**
	 * Populate the properties object for an annotation creation form
	 * from the parameters in the request.
	 * 
	 * @param request The annotation create request.
	 * @param props The JSP properties object to be populated.
	 * @param tree The annotation tree to be populated.
	 * @throws RequestFailureException
	 */
    void addCreateProperties(HttpServletRequest request, Properties props, PropertyTree tree) throws RequestFailureException;

    /**
	 * Populate the properties object for an annotation edit form from
	 * the request and the JSON representation of the annotation.
	 * 
	 * @param request The annotation edit request.
	 * @param obj The JSON version of the annotation
	 * @param props The properties object to be populated.
	 * @param tree The properties tree version of the annotation.
	 * @throws RequestFailureException
	 */
    void addEditProperties(HttpServletRequest request, JSONObject obj, Properties props, PropertyTree tree) throws RequestFailureException;

    /**
	 * The annotation display form is rendered from a PropertTree 
	 * object that represents the annotation.  This method adds
	 * additional information or synthetic / derived properties
	 * to simplify the display JSPs.
	 * 
	 * @param propsTree
	 */
    void addDisplayProperties(PropertyTree propsTree);

    /**
	 * Get the view name for the creation form
	 * 
	 * @return the view name.
	 */
    String getCreateViewName();

    /**
	 * Get the view name for the edit form
	 * 
	 * @return the view name.
	 */
    String getEditViewName();

    /**
	 * Get the view name for the annotation display formlet
	 * 
	 * @return the view name.
	 */
    String getDisplayViewName();

    /**
	 * Get the schema name or URI
	 * 
	 * @return the schema name or URI
	 */
    String getName();

    /**
	 * The human readable name for the schema
	 * 
	 * @return the human readable name
	 */
    String getHumanReadableName();

    /**
     * The schema URI.
     * 
     * @return the schema URI
     */
    String getUri();

    /**
	 * Build the RDF for an annotation from the contents of a create or update
	 * form, and other details.
	 * 
	 * @param conn the RepositoryConnection for the annotation RDF
	 * @param url the url for the resource being annotated
	 * @param request the create / edit submit request
	 * @throws RequestParameterException
	 * @throws RepositoryException
	 */
    void addRDFStatements(RepositoryConnection conn, Resource subj, HttpServletRequest request) throws RequestParameterException, RepositoryException;
}
