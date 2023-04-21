package org.colombbus.tangara.ide.controller.action;

import static org.junit.Assert.assertEquals;
import javax.swing.Action;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * {@link CloseTangaraAction} test case
 * 
 * @version $Id: CloseTangaraActionTest.java 196 2009-09-01 14:26:25Z swip $
 * @author Aurelien Bourdon <aurelien.bourdon@gmail.com>
 */
public class CloseTangaraActionTest extends TangaraActionTest {

    private CloseTangaraAction closeTangaraAction;

    /**
	 * @throws java.lang.Exception
	 */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    /**
	 * @throws java.lang.Exception
	 */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    /**
	 * @throws java.lang.Exception
	 */
    @Override
    @Before
    public void setUp() throws Exception {
        closeTangaraAction = new CloseTangaraAction();
    }

    /**
	 * @throws java.lang.Exception
	 */
    @Override
    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testConfigure() {
        String actionName = (String) closeTangaraAction.getValue(Action.NAME);
        String actionNameBundle = RESOURCE_BUNDLE.getString("CloseTangaraAction.name");
        assertEquals(actionNameBundle, actionName);
    }
}
