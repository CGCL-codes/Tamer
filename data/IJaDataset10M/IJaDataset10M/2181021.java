package org.mobicents.xdm.server.appusage.oma.groupusagelist;

import javax.xml.validation.Validator;
import org.mobicents.xdm.common.util.dom.DomUtils;
import org.mobicents.xdm.server.appusage.AppUsageDataSource;
import org.openxdm.xcap.common.error.ConstraintFailureConflictException;
import org.openxdm.xcap.common.error.InternalServerErrorException;
import org.openxdm.xcap.common.error.NotAuthorizedRequestException;
import org.openxdm.xcap.common.error.UniquenessFailureConflictException;
import org.openxdm.xcap.common.uri.DocumentSelector;
import org.openxdm.xcap.server.slee.appusage.resourcelists.ResourceListsAppUsage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * OMA XDM 1.1 Group Usage List XCAP App Usage.
 * @author martins
 *
 */
public class OMAGroupUsageListAppUsage extends ResourceListsAppUsage {

    public static final String ID = "org.openmobilealliance.group-usage-list";

    public static final String DEFAULT_DOC_NAMESPACE = "urn:ietf:params:xml:ns:resource-lists";

    public static final String MIMETYPE = "application/vnd.oma.group-usage-list+xml";

    private static final String LIST_ELEMENT_NAME = "list";

    private static final String EXTERNAL_ELEMENT_NAME = "external";

    private static final String ENTRY_REF_ELEMENT_NAME = "entry-ref";

    private static final String NOT_ALLOWED_ERROR_PHRASE = "Not allowed";

    /**
	 * 
	 * @param schemaValidator
	 */
    public OMAGroupUsageListAppUsage(Validator schemaValidator) {
        super(ID, DEFAULT_DOC_NAMESPACE, MIMETYPE, schemaValidator, "index", true);
    }

    @Override
    public void checkConstraintsOnPut(Document document, String xcapRoot, DocumentSelector documentSelector, AppUsageDataSource dataSource) throws UniquenessFailureConflictException, InternalServerErrorException, ConstraintFailureConflictException, NotAuthorizedRequestException {
        super.checkConstraintsOnPut(document, xcapRoot, documentSelector, dataSource);
        Element resourceLists = document.getDocumentElement();
        NodeList resourceListsChildNodeList = resourceLists.getChildNodes();
        for (int i = 0; i < resourceListsChildNodeList.getLength(); i++) {
            Node resourceListsChildNode = resourceListsChildNodeList.item(i);
            if (DomUtils.isElementNamed(resourceListsChildNode, LIST_ELEMENT_NAME)) {
                NodeList listChildNodeList = resourceListsChildNode.getChildNodes();
                for (int j = 0; j < listChildNodeList.getLength(); j++) {
                    Node listChildNode = listChildNodeList.item(j);
                    if (listChildNode.getNodeType() == Node.ELEMENT_NODE) {
                        if (DomUtils.getElementName(listChildNode).equals(ENTRY_REF_ELEMENT_NAME)) {
                            throw new ConstraintFailureConflictException(NOT_ALLOWED_ERROR_PHRASE);
                        } else if (DomUtils.getElementName(listChildNode).equals(EXTERNAL_ELEMENT_NAME)) {
                            throw new ConstraintFailureConflictException(NOT_ALLOWED_ERROR_PHRASE);
                        }
                    }
                }
            }
        }
    }
}
