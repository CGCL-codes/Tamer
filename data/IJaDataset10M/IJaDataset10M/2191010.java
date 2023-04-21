package com.desktopdeveloper.pendulum.components.list;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: kent_p
 * Date: 22-Oct-2004
 * Time: 12:19:29
 * To change this template use File | Settings | File Templates.
 */
public class PListModelTest extends MockObjectTestCase {

    private Mock mockItem;

    protected void setUp() throws Exception {
        super.setUp();
        mockItem = mock(PListItem.class, "Test List Item");
    }

    public void testImplementsListModelInterface() {
        PListModel model = new PListModel(new ArrayList());
        assertTrue(model instanceof ListModel);
    }

    public void testItemsAvailableOnListAfterConstruction() {
        List listItems = new ArrayList();
        listItems.add(mockItem.proxy());
        PListModel model = new PListModel(listItems);
        assertEquals(mockItem.proxy(), model.getElementAt(0));
        assertEquals(1, model.getSize());
        assertEquals(listItems, model.getListItems());
    }
}
