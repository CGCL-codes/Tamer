package bexee.model.activity;

import org.jmock.*;
import bexee.core.*;
import bexee.model.activity.impl.InvokeImpl;

/**
 * @version $Revision: 1.1 $, $Date: 2004/12/15 14:18:16 $
 * @author Pawel Kowalski
 */
public class InvokeImplTest extends MockObjectTestCase {

    private InvokeImpl invoke = null;

    protected void setUp() throws Exception {
        super.setUp();
        invoke = new InvokeImpl();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        invoke = null;
    }

    public final void testAccept() {
        Mock mockProcessController = new Mock(ProcessController.class);
        ProcessInstance processInstance = new ProcessInstance(null, null);
        mockProcessController.expects(once()).method("process").with(same(invoke), same(processInstance));
        try {
            invoke.accept((ProcessController) mockProcessController.proxy(), processInstance);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mockProcessController.verify();
    }

    public final void testDefaultValues() {
        assertEquals(false, invoke.isSuppressJoinFailure());
    }
}
