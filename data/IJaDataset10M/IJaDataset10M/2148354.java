package gov.noaa.eds.xapi.generic.modules;

import org.apache.log4j.Logger;
import org.apache.xpath.XPathAPI;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;
import org.xmldb.api.modules.XPathQueryService;
import gov.noaa.eds.xapi.generic.*;
import javax.xml.transform.TransformerException;

/** A slow query service that examines each resource one by one.  It uses
 * Xalan Xpath implementation {@link http://xml.apache.org/xalan-j}.
 *
 * @version $Id: GenericXPathQueryService.java,v 1.3 2004/12/23 23:39:15 mrxtravis Exp $
 * @author tns
 */
public class GenericXPathQueryService extends GenericConfigurable implements XPathQueryService {

    private static Logger log = Logger.getLogger(GenericXPathQueryService.class);

    private static String version = "1.0";

    private Collection collection = null;

    /** Creates a new instance of GenericXPathQueryService */
    public GenericXPathQueryService() {
    }

    /** Unsupported
     *@todo Figure out how namespaces fit in and implement this method
     */
    public void removeNamespace(String str) {
        throw new UnsupportedOperationException();
    }

    /** Grabs each Resource from a collection and runs an XPath query on
     *the DOM.  If one or more nodes match the query, then a resource is added
     *the the ResourceSet.
     *@param query The string query to perform on the collection
     *@return A set of resources which match the query
     */
    public ResourceSet query(String query) throws XMLDBException {
        if (this.collection == null) {
            throw new IllegalStateException("Collection property has not been set.");
        }
        if (query == null) {
            throw new NullPointerException("Parameter query can not be null");
        }
        String[] resourceIds = this.collection.listResources();
        ResourceSet resources = new GenericResourceSet();
        for (int i = 0; i < resourceIds.length; i++) {
            try {
                Resource resource = (Resource) this.collection.getResource(resourceIds[i]);
                if ("XMLResource".equals(resource.getResourceType())) {
                    XMLResource xResource = (XMLResource) resource;
                    Node node = xResource.getContentAsDOM();
                    NodeList nodeList = XPathAPI.selectNodeList(node, query);
                    for (int j = 0, sz = nodeList.getLength(); j < sz; j++) {
                        Node resultNode = nodeList.item(j);
                        if (resultNode.equals(node)) {
                            resources.addResource(xResource);
                        } else {
                            GenericXmlNodeResource nodeResource = new GenericXmlNodeResource(resultNode);
                            resources.addResource(nodeResource);
                        }
                    }
                }
            } catch (XMLDBException e) {
                log.warn("Error:", e);
            } catch (TransformerException e) {
                log.warn("Error while doing xpath query", e);
            }
        }
        return resources;
    }

    /**Unsupported
     *@todo Figure out how namespaces fit in and implement this method
     */
    public String getNamespace(String str) {
        throw new UnsupportedOperationException();
    }

    /**Sets the collection in which to do queries against
     *@param collection The collection to do a query against
     */
    public void setCollection(Collection collection) {
        if (this.collection != null) {
            throw new IllegalStateException("Collection property has " + "already been set and can not be changed.");
        }
        this.collection = collection;
    }

    /**Unsupported
     *@todo Figure out how namespaces fit in and implement this method
     */
    public void setNamespace(String str, String str1) {
        throw new UnsupportedOperationException();
    }

    /** Unsupported
     *@todo Implement this method
     */
    public ResourceSet queryResource(String str, String str1) {
        throw new UnsupportedOperationException();
    }

    /**Returns the version, currently 1.0
     *@return "1.0"
     */
    public String getVersion() {
        return this.version;
    }

    /** Returns XPathQueryService, which is the name of this service
     *@return 'XPathQueryService'
     */
    public String getName() {
        return "XPathQueryService";
    }

    /**Unsupported
     *@todo Figure out how namespaces fit in and implement this method
     */
    public void clearNamespaces() {
        throw new UnsupportedOperationException();
    }
}
