package net.thartm.treeutil.bulk.job;

import org.alfresco.service.cmr.repository.NodeRef;
import org.springframework.extensions.webscripts.WebScriptException;

public class AbstractNodeCreationJob {

    private NodeRef parentRef;

    private NodeRef templateRef;

    public AbstractNodeCreationJob() {
        super();
    }

    protected NodeRef getNodeRef(String reference) {
        if (reference != null && reference.length() > 0) {
            if (NodeRef.isNodeRef(reference)) {
                return new NodeRef(reference);
            } else {
                String[] tokens = reference.split("\\/");
                if (tokens.length > 0) {
                    String ref = tokens[0] + "://" + tokens[1] + "/" + tokens[2];
                    return new NodeRef(ref);
                }
            }
        }
        throw new WebScriptException(500, "Reference Argument: " + reference + " is not a valid String");
    }

    /**
	 * @return the parentRef
	 */
    public NodeRef getParentRef() {
        return parentRef;
    }

    /**
	 * @return the templateRef
	 */
    public NodeRef getTemplateRef() {
        return templateRef;
    }

    /**
	 * @param parentRef the parentRef to set
	 */
    public void setParentRef(NodeRef parentRef) {
        this.parentRef = parentRef;
    }

    public void setParentRef(String parentRef) {
        this.parentRef = getNodeRef(parentRef);
    }

    /**
	 * @param templateRef the templateRef to set
	 */
    public void setTemplateRef(NodeRef templateRef) {
        this.templateRef = templateRef;
    }

    public void setTemplateRef(String templateRef) {
        this.templateRef = getNodeRef(templateRef);
    }
}
