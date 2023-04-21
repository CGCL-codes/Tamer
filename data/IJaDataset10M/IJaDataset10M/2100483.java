package test.model.basic;

import junit.framework.TestCase;
import org.scopemvc.core.*;
import org.scopemvc.model.basic.*;

/**
 * <P>
 *
 * </P>
 *
 * @author <A HREF="mailto:smeyfroi@users.sourceforge.net">Steve Meyfroidt</A>
 * @created 05 September 2002
 * @version $Revision: 1.5 $ $Date: 2002/11/20 00:19:58 $
 */
public final class TestModelChangeEvent extends TestCase {

    /**
     * Constructor for the TestModelChangeEvent object
     *
     * @param inName Name of the test
     */
    public TestModelChangeEvent(String inName) {
        super(inName);
    }

    /**
     * A unit test for JUnit
     */
    public void testModelChangeEvent() {
        ModelChangeEventSource model = new BasicTestModel("model1");
        Selector selector = Selector.fromInt(0);
        ModelChangeEvent event = new BasicModelChangeEvent();
        event.setType(ModelChangeEvent.ACCESS_CHANGED);
        event.setModel(model);
        event.setSelector(selector);
        assertTrue(ModelChangeEvent.ACCESS_CHANGED == event.getType());
        assertSame(model, event.getModel());
        assertSame(selector, event.getSelector());
        model = new BasicTestModel("model2");
        selector = Selector.fromInt(1);
        event.setType(ModelChangeEvent.VALUE_ADDED);
        event.setModel(model);
        event.setSelector(selector);
        assertTrue(ModelChangeEvent.VALUE_ADDED == event.getType());
        assertSame(model, event.getModel());
        assertSame(selector, event.getSelector());
    }

    /**
     * The JUnit setup method
     */
    protected void setUp() {
    }
}
