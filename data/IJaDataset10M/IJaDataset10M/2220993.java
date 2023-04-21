package fr.itris.glips.svgeditor.visualresources;

import java.awt.geom.*;
import org.apache.batik.dom.svg.*;
import org.w3c.dom.*;
import java.util.*;
import fr.itris.glips.svgeditor.display.handle.*;
import fr.itris.glips.svgeditor.resources.*;

/**
 * the class providing methods to handle the resources
 * @author ITRIS, Jordi SUC
 */
public class SVGVisualResourceToolkit {

    /**
     * the visual resources module
     */
    private SVGVisualResources visualResources = null;

    /**
     * the constructor of the class
     * @param visualResources the visual resource module
     */
    public SVGVisualResourceToolkit(SVGVisualResources visualResources) {
        this.visualResources = visualResources;
    }

    /**
     * @return Returns the visualResources.
     */
    protected SVGVisualResources getVisualResources() {
        return visualResources;
    }

    /**
     * creates a resource node given its name
     * @param handle the current handle
     * @param parentNode the parentNode
     * @param nodeName the name of the node of the accurate resource
     * @param idShapeToBeAppended the id of the shape to be appended as a child of the resource
     * @return the element corresponding to the given name of the resource
     */
    protected Element createVisualResource(SVGHandle handle, Element parentNode, String nodeName, String idShapeToBeAppended) {
        Element resource = null;
        ResourceBundle bundle = ResourcesManager.bundle;
        if (handle != null && parentNode != null && nodeName != null && !nodeName.equals("")) {
            Element childElement = null;
            if (idShapeToBeAppended != null && !idShapeToBeAppended.equals("")) {
                Element elt = null;
                Document doc = handle.getScrollPane().getSVGCanvas().getDocument();
                if (doc != null) {
                    elt = doc.getElementById(idShapeToBeAppended);
                    if (elt != null) {
                        childElement = (Element) doc.importNode(elt, true);
                        childElement.setAttribute("id", "");
                        AffineTransform af = handle.getSvgElementsManager().getTransform(childElement);
                        if (af != null) {
                            double e = 0, f = 0;
                            Rectangle2D bounds = handle.getSvgElementsManager().getNodeBounds(elt);
                            if (bounds != null) {
                                e = -bounds.getX();
                                f = -bounds.getY();
                            }
                            af.preConcatenate(AffineTransform.getTranslateInstance(e, f));
                            handle.getSvgElementsManager().setTransform(childElement, af);
                        }
                    }
                }
            }
            String svgNS = parentNode.getOwnerDocument().getDocumentElement().getNamespaceURI();
            resource = parentNode.getOwnerDocument().createElementNS(svgNS, nodeName);
            if (nodeName.equals("linearGradient")) {
                resource.setAttributeNS(null, "x1", "0%");
                resource.setAttributeNS(null, "y1", "0%");
                resource.setAttributeNS(null, "x2", "100%");
                resource.setAttributeNS(null, "y2", "0%");
                resource.setAttributeNS(null, "spreadMethod", "pad");
                resource.setAttributeNS(null, "gradientTransform", "");
                resource.setAttributeNS(null, "gradientUnits", "objectBoundingBox");
                Element child = parentNode.getOwnerDocument().createElementNS(svgNS, "stop");
                child.setAttributeNS(null, "offset", "0%");
                child.setAttributeNS(null, "style", "stop-color:#000000;stop-opacity:1.0;");
                resource.appendChild(child);
            } else if (nodeName.equals("radialGradient")) {
                resource.setAttributeNS(null, "cx", "50%");
                resource.setAttributeNS(null, "cy", "50%");
                resource.setAttributeNS(null, "r", "50%");
                resource.setAttributeNS(null, "fx", "50%");
                resource.setAttributeNS(null, "fy", "50%");
                resource.setAttributeNS(null, "spreadMethod", "pad");
                resource.setAttributeNS(null, "gradientTransform", "");
                resource.setAttributeNS(null, "gradientUnits", "objectBoundingBox");
                Element child = parentNode.getOwnerDocument().createElementNS(svgNS, "stop");
                child.setAttributeNS(null, "offset", "0%");
                child.setAttributeNS(null, "style", "stop-color:#000000;stop-opacity:1.0;");
                resource.appendChild(child);
            } else if (nodeName.equals("pattern")) {
                resource.setAttributeNS(null, "x", "0%");
                resource.setAttributeNS(null, "y", "0%");
                resource.setAttributeNS(null, "width", "100%");
                resource.setAttributeNS(null, "height", "100%");
                resource.setAttributeNS(null, "patternUnits", "objectBoundingBox");
                resource.setAttributeNS(null, "patternContentUnits", "userSpaceOnUse");
            } else if (nodeName.equals("marker")) {
                resource.setAttributeNS(null, "refX", "0");
                resource.setAttributeNS(null, "refY", "0");
                resource.setAttributeNS(null, "markerWidth", "0");
                resource.setAttributeNS(null, "markerHeight", "0");
                resource.setAttributeNS(null, "markerUnits", "strokeWidth");
                resource.setAttributeNS(null, "orient", "auto");
            }
            String baseId = resource.getNodeName();
            if (bundle != null) {
                try {
                    baseId = "vresourcename_".concat(baseId);
                    baseId = bundle.getString(baseId);
                    baseId = baseId.replaceAll("\\s+", "");
                } catch (Exception ex) {
                    baseId = resource.getNodeName();
                }
            }
            String id = handle.getSvgElementsManager().getId(baseId, null);
            resource.setAttributeNS(null, "id", id);
            if (childElement != null) {
                resource.appendChild(childElement);
            }
        }
        return resource;
    }

    /**
     * returns the duplicate resource of the given resource
     * @param handle a svg handle
     * @param resourceNode a resource node
     * @return the duplicated node
     */
    protected Element duplicateVisualResource(SVGHandle handle, Element resourceNode) {
        Element resource = null;
        ResourceBundle bundle = getVisualResources().getBundle();
        if (resourceNode != null) {
            Element parentNode = (Element) resourceNode.getParentNode();
            if (parentNode != null) {
                resource = (Element) resourceNode.cloneNode(true);
                String suffix = "";
                if (bundle != null) {
                    try {
                        suffix = bundle.getString("vresource_duplicatednodesuffix");
                    } catch (Exception ex) {
                    }
                }
                String id = resource.getAttribute("id");
                id = handle.getSvgElementsManager().getId(id + suffix, null);
                resource.setAttribute("id", id);
            }
        }
        return resource;
    }

    /**
     * removes the given resource node
     * @param handle the current svg handle
     * @param resourceNode the resource node
     */
    protected void removeVisualResource(SVGHandle handle, Element resourceNode) {
        if (handle != null && resourceNode != null) {
            Element parentNode = (Element) resourceNode.getParentNode();
            if (parentNode != null) {
                parentNode.removeChild(resourceNode);
            }
        }
    }

    /**
     * appends the givn resource node to the given parent node
     * @param handle a svg handle 
     * @param parentNode a parent node
     * @param resourceNode a resource node
     */
    protected void appendVisualResource(SVGHandle handle, Element parentNode, Element resourceNode) {
        if (handle != null && parentNode != null && resourceNode != null) {
            String id = resourceNode.getAttribute("id");
            if (!handle.getSvgElementsManager().checkId(id)) {
                id = handle.getSvgElementsManager().getId(id, null);
            }
            parentNode.appendChild(resourceNode);
        }
    }

    /**
     * whether the given node can be removed or not
     * @param handle the current handle
     * @param resourceNode the resource node
     * @return whether the given node can be removed or not
     */
    protected boolean canRemoveVisualResource(SVGHandle handle, Element resourceNode) {
        if (handle != null && resourceNode != null) {
            String id = resourceNode.getAttribute("id");
            if (!handle.getSvgResourcesManager().isResourceUsed(id)) {
                return true;
            }
        }
        return false;
    }

    /**
     * imports a resource node from the resource store
     * @param handle the current handle
     * @param parentNode the parentNode
     * @param nodeToBeImported the node that should be imported
     * @return the imported node
     */
    protected Element importVisualResource(SVGHandle handle, Element parentNode, Element nodeToBeImported) {
        Element resourceNode = null;
        Document visualResourceStore = getVisualResources().getVisualResourceStore();
        ResourceBundle bundle = getVisualResources().getBundle();
        if (handle != null && parentNode != null && visualResourceStore != null && nodeToBeImported != null) {
            final Element defs = getVisualResources().getDefs(handle);
            if (defs != null) {
                Document doc = defs.getOwnerDocument();
                resourceNode = (Element) doc.importNode(nodeToBeImported, true);
                String baseId = resourceNode.getNodeName(), importedLabel = "";
                if (bundle != null) {
                    try {
                        importedLabel = bundle.getString("labelimported");
                        baseId = "vresourcename_".concat(baseId);
                        baseId = bundle.getString(baseId);
                        baseId = importedLabel.concat(baseId);
                        baseId = baseId.replaceAll("\\s+", "");
                    } catch (Exception ex) {
                        baseId = resourceNode.getNodeName();
                    }
                }
                String id = handle.getSvgElementsManager().getId(baseId, null);
                resourceNode.setAttribute("id", id);
            }
        }
        return resourceNode;
    }

    /**
     * gets the attribute node of the parent node given its name or creates it if it does not exist
     * @param handle the current handle
     * @param parentNode the parent node
     * @param name the name of the attribute
     * @param value the value of the attribute if it is created
     * @return the attribute node
     */
    public Node getVisualResourceAttributeNode(SVGHandle handle, Element parentNode, String name, String value) {
        Node attNode = null;
        if (handle != null && parentNode != null && name != null && !name.equals("")) {
            attNode = parentNode.getAttributeNode(name);
            if (attNode == null) {
                if (value == null) {
                    value = "";
                }
                attNode = parentNode.getOwnerDocument().createAttribute(name);
                attNode.setNodeValue(value);
                parentNode.setAttributeNode((Attr) attNode);
            }
        }
        return attNode;
    }

    /**
     * creates a child of a resource node given its parent
     * @param handle the current svg handle
     * @param parentNode the parentNode to which the child will be appended
     * @return the element corresponding to a child of a resource whose name is nodeName
     */
    protected Element createVisualResourceChildStructure(SVGHandle handle, Node parentNode) {
        Element resourceChild = null;
        if (handle != null && parentNode != null) {
            if (parentNode.getNodeName().equals("linearGradient") || parentNode.getNodeName().equals("radialGradient")) {
                String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;
                resourceChild = parentNode.getOwnerDocument().createElementNS(svgNS, "stop");
                resourceChild.setAttributeNS(null, "offset", "0%");
                resourceChild.setAttributeNS(null, "style", "stop-color:#000000;stop-opacity:1.0;");
            }
        }
        return resourceChild;
    }

    /**
     * appends a child of a resource node to its parent
     * @param handle the current svg handle
     * @param parentNode the parentNode to which the child will be appended
     * @param childNode the child of a resource node
     */
    protected void appendVisualResourceChild(SVGHandle handle, Node parentNode, Node childNode) {
        if (handle != null && parentNode != null && childNode != null) {
            if (parentNode.getNodeName().equals("linearGradient") || parentNode.getNodeName().equals("radialGradient")) {
                parentNode.appendChild(childNode);
            }
        }
    }

    /**
     * removes the child node from the dom
     * @param handle a svg handle
     * @param childNode the node to be removed
     */
    protected void removeVisualResourceChild(SVGHandle handle, Node childNode) {
        if (handle != null && childNode != null) {
            final Node parentNode = childNode.getParentNode();
            final Node fchildNode = childNode;
            if (parentNode != null) {
                parentNode.removeChild(fchildNode);
            }
        }
    }
}
