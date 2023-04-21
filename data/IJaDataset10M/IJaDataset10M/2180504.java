package com.gever.goa.dailyoffice.tools.dao;

import java.util.List;
import com.gever.exception.DefaultException;
import com.gever.util.InitTestUtil;
import junit.framework.TestCase;

public class CardcaseDaoTest extends TestCase {

    private CardcaseDao cardDAO;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(CardcaseDaoTest.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
        InitTestUtil.init();
        cardDAO = ToolsFactory.getInstance().createCardcaseDao("unittest");
    }

    public void testQueryAll() throws DefaultException {
        List ls1 = cardDAO.queryByUser("1");
        assertEquals(ls1.size(), 0);
    }
}
