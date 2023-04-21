package org.genos.gmf.templates;

import java.sql.Connection;
import org.genos.gmf.form.Form;
import org.genos.gmf.resources.Resource;

public class ResourceTemplate extends Resource {

    /**
     * Default constructor
     */
    public ResourceTemplate() throws Exception {
        super();
        resForm = new Form();
    }

    /**
     * Gets the caption of the resource
     * @return  Resource's caption.
     */
    public String getResourceDescription() {
        return "Resource Template description";
    }

    /**
	 * Execute the resource and get an abstract of it for building the path to the resource.
	 * @param uid			User unique id.
	 * @return				HTML code from the execution (abstract version for path).
     * @throws Exception
	 */
    public String executeAbstractPath(int uid) throws Exception {
        return "TODO";
    }

    /**
     * Get an abstract
     * @param   conn    Database connection.
     * @return  HTML code.
     */
    public String getHTMLAbstract(Connection conn) {
        return "TODO";
    }

    /**
     * Builds HTML output for the resource
     * @param   conn        Database connection.
     * @param   uid         User id.
     * @param   contents    HTML with the abstracts of contained resources, if any.
     * @return              HTML to show this resource.
     * @throws Exception
     */
    public String getHTML(Connection conn, int uid, String contents) throws Exception {
        return "TODO";
    }
}
