package org.apache.myfaces.trinidad.change;

import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.apache.myfaces.trinidad.logging.TrinidadLogger;

/**
 * Change specialization for adding a child component to a facet using document
 * mark up. While applying this Change, the child component is created and added to
 * the document.  If the facet doesn't exist, it will be created.  If the facet
 * does exist, all of its content will be removed and the new content added.
 */
public class SetFacetChildDocumentChange extends AddComponentDocumentChange {

    /**
   * Constructs an AddFacetDocumentChange with the specified child component mark up and
   *  the name of the facet.
   * @param facetName Name of facet to create the child component in
   * @param fragment DOM mark up for child component to be inserted.
   * @throws IllegalArgumentException if facetName or componentFragment is
   *         <code>null</code>
   */
    public SetFacetChildDocumentChange(String facetName, DocumentFragment fragment) {
        super(fragment);
        if ((facetName == null) || (facetName.length() == 0)) throw new IllegalArgumentException(_LOG.getMessage("FACET_NAME_MUST_SPECIFIED"));
        _facetName = facetName;
    }

    /**
   * Returns the identifier of the sibling before which this new child needs to
   *  be inserted.
   */
    public String getFacetName() {
        return _facetName;
    }

    /**
   * Given the DOM Node representing a Component, apply any necessary
   * DOM changes.
   * While applying this Change, the child component is created and added to
   * the document.  If the facet doesn't exist, it will be created.  If the facet
   * does exist, all of its content will be removed and the new content added.
   */
    public void changeDocument(Node componentNode) {
        if (componentNode == null) throw new IllegalArgumentException(_LOG.getMessage("NO_NODE_SPECIFIED"));
        DocumentFragment targetFragment = getImportedComponentFragment(componentNode);
        Element facetElement = ChangeUtils.__getFacetElement(componentNode, _facetName);
        if (facetElement != null) {
            ChangeUtils.__removeAllChildren(facetElement);
        } else {
            Document targetDocument = componentNode.getOwnerDocument();
            facetElement = targetDocument.createElementNS(_JSF_CORE_NAMESPACE, "f:facet");
            facetElement.setAttributeNS(_XMLNS_NAMESPACE, "xmlns:f", _JSF_CORE_NAMESPACE);
            facetElement.setAttribute(_FACET_ATTRIBUTE_NAME, _facetName);
            componentNode.appendChild(facetElement);
        }
        facetElement.appendChild(targetFragment);
    }

    /** 
   * Returns true if adding the DocumentChange should force the JSP Document
   * to reload
   */
    @Override
    public boolean getForcesDocumentReload() {
        return false;
    }

    private static final String _JSF_CORE_NAMESPACE = "http://java.sun.com/jsf/core";

    private static final String _XMLNS_NAMESPACE = "http://www.w3.org/2000/xmlns/";

    private static final String _FACET_ATTRIBUTE_NAME = "name";

    private final String _facetName;

    private static final TrinidadLogger _LOG = TrinidadLogger.createTrinidadLogger(SetFacetChildDocumentChange.class);
}
