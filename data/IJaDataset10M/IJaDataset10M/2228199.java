package org.viewaframework.util;

import java.util.Arrays;
import junit.framework.TestCase;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.viewaframework.common.MyTrayView;
import org.viewaframework.view.ViewActionDescriptor;
import org.viewaframework.view.ViewContainer;
import org.w3c.dom.Document;

public class ViewActionDescriptorFileHandlerTest extends TestCase {

    private static final String TRAY_RIGHT_MENU_NODE_NAME = "rightMenu";

    private static final String TRAY_LEFT_MENU_NODE_NAME = "leftMenu";

    private ViewContainer view;

    private Mockery mockery;

    public void setUp() throws Exception {
        mockery = new Mockery();
        view = mockery.mock(ViewContainer.class);
        mockery.checking(new Expectations() {

            {
                atLeast(1).of(view).getActionDescriptors();
                will(returnValue(Arrays.asList(new ViewActionDescriptor("/rightMenu[@text='rightMenu']"), new ViewActionDescriptor("/rightMenu/hide[@text='hide']"), new ViewActionDescriptor("/leftMenu[@text='leftMenu']"), new ViewActionDescriptor("/leftMenu/other[@text='other']"))));
            }
        });
    }

    public void testGetDocument() throws Exception {
        MyTrayView view = new MyTrayView();
        ViewActionDescriptorFileHandler handler = new ViewActionDescriptorFileHandler();
        Document document = handler.getDocument(view);
        assertNotNull(document);
        assertEquals(document.getElementsByTagName(TRAY_LEFT_MENU_NODE_NAME).getLength(), 1);
        assertEquals(document.getElementsByTagName(TRAY_RIGHT_MENU_NODE_NAME).getLength(), 1);
    }

    public void testGetDocumentFromActionDescriptor() throws Exception {
        ViewActionDescriptorFileHandler handler = new ViewActionDescriptorFileHandler();
        Document document = handler.getDocumentFromActionDescriptors(view);
        assertNotNull(document);
        assertEquals(document.getElementsByTagName(TRAY_LEFT_MENU_NODE_NAME).getLength(), 1);
        assertEquals(document.getElementsByTagName(TRAY_RIGHT_MENU_NODE_NAME).getLength(), 1);
    }
}
