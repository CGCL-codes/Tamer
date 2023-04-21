package org.docflower.model.actionhandlers;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import javax.xml.namespace.NamespaceContext;
import org.docflower.consts.DocFlowerConsts;
import org.docflower.model.Action;
import org.docflower.model.ActionEngine;
import org.docflower.ui.UpdateInfo;
import org.docflower.util.ActionHandlerException;
import org.docflower.util.LowLevelException;
import org.docflower.util.Messages;
import org.docflower.xml.DOMUtils;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class CallActionHandler extends SingleNodeActionHandler {

    private static final String PARAM = "Param";

    private static final String PARAMS = "#Params";

    private String xpath;

    private String actionToCallId;

    @SuppressWarnings("unchecked")
    @Override
    public void register(Action action, UpdateInfo updateInfo) {
        Node actionDesc = ActionEngine.getSingleton().getAction(getActionToCallId(), action.getContext());
        Map<String, Object> params = null;
        if (updateInfo.getData().containsKey(PARAMS)) {
            params = (Map<String, Object>) updateInfo.getData().get(PARAMS);
        }
        if (actionDesc != null) {
            ActionEngine.getSingleton().prepareActionHandlerList(actionDesc, action, (Node) updateInfo.getData().get(BASE_NODE_INSTANCE), params, updateInfo);
        } else {
            throw new LowLevelException(MessageFormat.format(Messages.CallActionHandler_UnableFindCalledAction, getActionToCallId()));
        }
    }

    @Override
    public void init(Action parent, Element actionItemElement, Map<String, Object> params, Node baseNode, UpdateInfo updateInfo) {
        if (DocFlowerConsts.KEY_LOCAL_ACTION_NS.equals(actionItemElement.getAttribute(DocFlowerConsts.ACTION_TYPE_LOCAL))) {
            throw new LowLevelException(Messages.CallActionHandler_IsNotSupportedWithinLocalAction);
        }
        super.init(parent, actionItemElement, params, baseNode, updateInfo);
        if (params != null) {
            updateInfo.getData().put(PARAMS, params);
        }
        xpath = DOMUtils.getNullableAttrValue(actionItemElement, DocFlowerConsts.ATTR_XPATH);
        setActionToCallId(DOMUtils.getNullableAttrValue(actionItemElement, DocFlowerConsts.ATTR_ID));
        handleNestedParams(parent, actionItemElement, parent.getContext().getDataModel().getDescriptor().getActionNamespaceContext(), updateInfo);
    }

    /**
	 * Unused method. CallActionHandler is just like container for other actions
	 * and isn't registered in the list of action handlers for execution so
	 * there is nothing to handle for it.
	 */
    @Override
    public boolean handle(Action parent, UpdateInfo updateInfo) throws ActionHandlerException {
        return false;
    }

    /**
	 * Unused method. CallActionHandler is just like container for other actions
	 * and isn't registered in the list of action handlers for execution so
	 * there is nothing to rollback for it.
	 */
    @Override
    public boolean rollback(Action parent, UpdateInfo updateInfo) throws ActionHandlerException {
        return false;
    }

    private void handleNestedParams(Action parent, Element actionItemElement, NamespaceContext namespaceContext, UpdateInfo updateInfo) {
        if (actionItemElement.getChildNodes().getLength() > 0) {
            for (int i = 0; i < actionItemElement.getChildNodes().getLength(); i++) {
                Node node = actionItemElement.getChildNodes().item(i);
                if (node instanceof Element) {
                    if (PARAM.equals(node.getLocalName())) {
                        addParam(parent, (Element) node, updateInfo);
                    }
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void addParam(Action parent, Element paramElement, UpdateInfo updateInfo) {
        Map<String, Object> params;
        if (updateInfo.getData().containsKey(PARAMS)) {
            params = (Map<String, Object>) updateInfo.getData().get(PARAMS);
        } else {
            params = new HashMap<String, Object>();
            updateInfo.getData().put(PARAMS, params);
        }
        String key = DOMUtils.getNullableAttrValue(paramElement, DocFlowerConsts.ATTR_NAME);
        if (!params.containsKey(key)) {
            params.put(key, DOMUtils.getNullableAttrValue(paramElement, DocFlowerConsts.ATTR_VALUE));
        } else {
            throw new LowLevelException(MessageFormat.format(Messages.CallActionHandler_AlreadyExists, key));
        }
    }

    public void setXpath(String xpath) {
        this.xpath = xpath;
    }

    public String getXpath() {
        return xpath;
    }

    public void setActionToCallId(String actionToCallId) {
        this.actionToCallId = actionToCallId;
    }

    public String getActionToCallId() {
        return actionToCallId;
    }
}
