package com.miden2ena.mogeci.action;

import junit.framework.*;
import com.miden2ena.mogeci.exceptions.DatabaseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;
import fr.improve.struts.taglib.layout.suggest.SuggestAction;
import com.miden2ena.mogeci.dao.*;
import com.miden2ena.mogeci.pojo.*;

/**
 *
 * @author Miden2Ena s.r.l.
 */
public class IstitutiSuggestActionTest extends TestCase {

    public IstitutiSuggestActionTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(IstitutiSuggestActionTest.class);
        return suite;
    }

    /**
     * Test of getSuggestionList method, of class com.miden2ena.mogeci.action.IstitutiSuggestAction.
     */
    public void testGetSuggestionList() {
        System.out.println("getSuggestionList");
    }
}
