package com.sri.emo.wizard.completion.management;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import com.jcorporate.expresso.core.controller.Controller;
import com.jcorporate.expresso.core.controller.ControllerException;
import com.jcorporate.expresso.core.controller.ExpressoRequest;
import com.jcorporate.expresso.core.controller.ExpressoResponse;
import com.jcorporate.expresso.core.controller.NonHandleableException;
import com.jcorporate.expresso.core.db.DBException;
import com.sri.emo.dbobj.Part;
import com.sri.emo.dbobj.PartsFactory;
import com.sri.emo.wizard.completion.model.CompletionBean;
import com.sri.emo.wizard.completion.model.CompletionPartsBean;

/**
 * Test of PromptChooseParts state handler.
 */
public class TestPromptChooseParts extends ChooseCompletionTestBase {

    /**
     * Checks that all parts set up match that of PartsFactory.
     * @throws DBException
     * @throws ControllerException
     * @throws NonHandleableException
     */
    public void testListParts() throws DBException, ControllerException, NonHandleableException {
        Map parameters = new HashMap();
        ExpressoRequest request = testFixture.buildControllerRequest(parameters, PromptChooseParts.STATE_NAME);
        CompletionBean cb = new CompletionBean();
        cb.setTargetId(2);
        cb.initializeFromNodeId(null);
        request.getSession().setPersistentAttribute(CompletionBeanManager.BEAN_NAME, cb);
        Controller controller = testFixture.buildController(CompletionEditor.class);
        ExpressoResponse response = testFixture.invokeState(PromptChooseParts.STATE_NAME, controller, request);
        assertTrue(response != null);
        assertTrue(response.getErrors() == null || response.getErrors().size() == 0);
        cb = (CompletionBean) request.getSession().getPersistentAttribute(CompletionBeanManager.BEAN_NAME);
        assertTrue(cb != null);
        Set allParts = cb.getCompletionParts();
        assertTrue(allParts != null);
        Part[] moviedemoParts = PartsFactory.getParts("MOVIEDEMO");
        assertEquals(moviedemoParts.length, allParts.size());
        int index = 0;
        for (Iterator i = allParts.iterator(); i.hasNext(); ) {
            CompletionPartsBean partsBean = (CompletionPartsBean) i.next();
            assertEquals(moviedemoParts[index].getPartNumInt(), partsBean.getPart().getPartNumInt());
            index++;
        }
    }
}
