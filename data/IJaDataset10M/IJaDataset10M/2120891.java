package org.helianto.web;

import java.util.ArrayList;
import java.util.List;
import org.easymock.classextension.EasyMock;
import org.helianto.document.PrivateDocument;
import org.helianto.document.filter.PrivateDocumentFilterAdapter;
import org.helianto.web.action.SimpleModel;

/**
 * 
 * @author mauriciofernandesdecastro
 */
public class PrivateDocumentSelectionFlowTests extends AbstractPrivateDocumentFlowTest {

    /**
	 * Same base path as the superclass.
	 */
    protected String getBasePath() {
        return "privateDocument/";
    }

    public void testStart() {
        PrivateDocumentFilterAdapter filter = new PrivateDocumentFilterAdapter(entity, "");
        List<PrivateDocument> selectionList = new ArrayList<PrivateDocument>();
        EasyMock.expect(documentMgr.findPrivateDocuments(EasyMock.eq(filter))).andReturn(selectionList);
        EasyMock.replay(documentMgr);
        doSelectionStartTest();
        EasyMock.verify(documentMgr);
        @SuppressWarnings("unchecked") SimpleModel<PrivateDocument> model = (SimpleModel<PrivateDocument>) getFlowAttribute("privateDocumentModel");
        assertSame(selectionList, model.getList());
    }
}
