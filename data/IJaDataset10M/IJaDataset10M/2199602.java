package org.jrecruiter.web.actions;

import java.util.HashMap;
import junit.framework.Assert;
import junit.framework.TestCase;
import org.apache.struts2.ServletActionContext;
import org.jrecruiter.service.JobService;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;
import com.opensymphony.xwork2.ActionContext;

/**
 * Test the Struts 2 Logout Action
 *
 * @author Gunnar Hillert
 * @version $Id: BaseActionTest.java 563 2010-06-05 01:24:30Z ghillert $
 */
public class BaseActionTest extends TestCase {

    @Override
    protected void setUp() throws Exception {
        ActionContext ac = new ActionContext(new HashMap<String, Object>());
        ActionContext.setContext(ac);
        ActionContext.getContext().setSession(new HashMap<String, Object>());
        ServletActionContext.setRequest(new MockHttpServletRequest());
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        ActionContext.getContext().setSession(null);
        super.tearDown();
    }

    public void testExecute() throws Exception {
        LogoutAction logoutAction = new LogoutAction();
        JobService jobService = Mockito.mock(JobService.class);
        String ret = logoutAction.execute();
        Assert.assertEquals("success", ret);
    }
}
