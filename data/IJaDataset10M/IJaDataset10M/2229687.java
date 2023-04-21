package org.makumba.forms.tags;

import javax.servlet.http.HttpServletRequest;
import org.makumba.DataDefinition;
import org.makumba.LogicException;
import org.makumba.analyser.PageCache;
import org.makumba.commons.MultipleKey;
import org.makumba.commons.attributes.RequestAttributes;
import org.makumba.controller.Logic;
import org.makumba.forms.responder.Responder;
import org.makumba.forms.responder.ResponderOperation;

/**
 * mak:new tag
 * @author Cristian Bogdan
 * @version $Id: NewTag.java 2298 2008-05-12 00:32:00Z manuelbernhardt $
 */
public class NewTag extends FormTagBase {

    private static final long serialVersionUID = 1L;

    DataDefinition type = null;

    String multipleSubmitErrorMsg = null;

    public void setType(String s) {
        type = ddp.getDataDefinition(s);
    }

    public void setMultipleSubmitErrorMsg(String s) {
        checkNoParent("multipleSubmitErrorMsg");
        multipleSubmitErrorMsg = s;
    }

    /**
     * {@inheritDoc}
     */
    public void setTagKey(PageCache pageCache) {
        Object keyComponents[] = { type.getName(), handler, afterHandler, fdp.getParentListKey(this), formName, getClass() };
        tagKey = new MultipleKey(keyComponents);
    }

    /**
     * {@inheritDoc}
     */
    public void initialiseState() {
        super.initialiseState();
        if (type != null) responder.setNewType(type);
        if (multipleSubmitErrorMsg != null) responder.setMultipleSubmitErrorMsg(multipleSubmitErrorMsg);
    }

    public DataDefinition getDataTypeAtAnalysis(PageCache pageCache) {
        return type;
    }

    @Override
    public ResponderOperation getResponderOperation(String operation) {
        if (operation.equals("new")) {
            return newOp;
        }
        return null;
    }

    private static final ResponderOperation newOp = new ResponderOperation() {

        private static final long serialVersionUID = 1L;

        public Object respondTo(HttpServletRequest req, Responder resp, String suffix, String parentSuffix) throws LogicException {
            String handlerName;
            if (resp.getHandler() != null) {
                handlerName = resp.getHandler();
            } else {
                handlerName = "on_new" + Logic.upperCase(resp.getNewType());
            }
            String afterHandlerName;
            if (resp.getAfterHandler() != null) {
                afterHandlerName = resp.getAfterHandler();
            } else {
                afterHandlerName = "after_new" + Logic.upperCase(resp.getNewType());
            }
            return Logic.doNew(resp.getController(), handlerName, afterHandlerName, resp.getNewType(), resp.getHttpData(req, suffix), new RequestAttributes(resp.getController(), req, resp.getDatabase()), resp.getDatabase(), getConnectionProvider(req, resp.getController()));
        }

        public String verify(Responder resp) {
            return null;
        }
    };

    @Override
    protected void doAnalyzedCleanup() {
        super.doAnalyzedCleanup();
        type = null;
        multipleSubmitErrorMsg = null;
    }
}
