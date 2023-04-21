package org.genos.gmf.resources.formatters;

import org.genos.gmf.resources.Resource;
import org.w3c.dom.Document;

/**
 * Interface for all classes exporting XML (used by formatters)
 */
public interface IXMLSource {

    /**
	 * Set resource.
	 * @param _res	Resource.
	 */
    public void setResource(Resource _res);

    /**
	 * Set user id.
	 * @param _uid	User id.
	 */
    public void setUid(int _uid);

    /**
	 * XML tree
	 * @return		XML Document
	 */
    public Document buildXml() throws Exception;
}
