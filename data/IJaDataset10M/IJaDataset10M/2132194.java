package fulmine.model.container;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import java.util.Random;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import fulmine.Domain;
import fulmine.Type;
import fulmine.context.FulmineContext;
import fulmine.context.IFrameworkContext;
import fulmine.event.EventFrameExecution;
import fulmine.event.listener.EventListenerUtils;
import fulmine.event.listener.JUnitEventListener;
import fulmine.model.field.IField;
import fulmine.model.field.IntegerField;
import fulmine.model.field.LongField;
import fulmine.model.field.containerdefinition.IContainerDefinitionField;

/**
 * Test cases for the {@link AbstractDynamicContainer}
 * 
 * @author Ramon Servadei
 */
@SuppressWarnings("all")
public class DynamicContainerJUnitTest {

    JUnitDynamicContainer candidate;

    Mockery mocks = new JUnit4Mockery();

    IFrameworkContext context = new FulmineContext(getClass().getSimpleName());

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        context.start();
        candidate = new JUnitDynamicContainer();
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
        context.destroy();
    }

    /**
     * Test method for
     * {@link fulmine.model.container.AbstractDynamicContainer#add(IField)}.
     */
    @Test
    public void testAdd() {
        final String id = "longfield";
        LongField field = new LongField(id);
        candidate.add(field);
        assertEquals("did not add component", field, candidate.get((id)));
        assertSame("did not add component", field, candidate.get((id)));
        assertEquals("Did not add component to definition", 2, candidate.getDefinition().getWireCodeForIdentity((id)));
    }

    /**
     * Test method for
     * {@link fulmine.model.container.AbstractDynamicContainer#add(IField)} and
     * {@link fulmine.model.container.AbstractDynamicContainer#remove(fulmine.model.component.IComponent)}
     * .
     * 
     * @throws CloneNotSupportedException
     */
    @Test
    public void testAddRemove() throws CloneNotSupportedException {
        final String id = "longfield";
        final LongField field = new LongField(id);
        final IContainerDefinitionField definition = mocks.mock(IContainerDefinitionField.class);
        candidate.getFields().put(candidate.getDefinition().getIdentity(), definition);
        mocks.checking(new Expectations() {

            {
                one(definition).add(field);
                one(definition).remove(field);
                one(definition).destroy();
                one(definition).clone();
                will(returnValue(definition));
                one(definition).getAddress();
                one(definition).resetChanges();
            }
        });
        candidate.add(field);
        candidate.remove(field);
        assertNull("did not remove component", candidate.get((id)));
    }

    @Test
    public void testReadWriteSimple() throws InterruptedException {
        candidate.add(new IntegerField("sdf3"));
        candidate.beginFrame(new EventFrameExecution());
        ((IntegerField) candidate.get(("sdf3"))).set(23);
        ContainerJUnitTest.doReadWrite(context, candidate);
    }

    @Test
    public void testReadWriteAndRemove() throws InterruptedException {
        final IContainer remoteContainer = context.getRemoteContainer(ContainerJUnitTest.REMOTE_CONTEXT_IDENTITY, candidate.getIdentity(), candidate.getType(), candidate.getDomain());
        final JUnitEventListener dummyListener = new JUnitEventListener("DynamicContainerJUnitTest.testReadWriteAndRemove", EventListenerUtils.createFilter(IContainer.class));
        dummyListener.setMinimumExpectedUpdateCount(1);
        remoteContainer.addListener(dummyListener);
        candidate.addListener(dummyListener);
        candidate.add(new IntegerField("sdf3"));
        final LongField longField = new LongField(ContainerJUnitTest.IWF_FIELD1);
        candidate.add(longField);
        final LongField longField2 = new LongField(ContainerJUnitTest.IWF_FIELD2);
        candidate.add(longField2);
        candidate.add(new LongField("sdf4"));
        Random rnd = new Random();
        candidate.beginFrame(new EventFrameExecution());
        ((IntegerField) candidate.get(("sdf3"))).set(23);
        ((LongField) candidate.get((ContainerJUnitTest.IWF_FIELD1))).set(rnd.nextLong());
        ((LongField) candidate.get((ContainerJUnitTest.IWF_FIELD2))).set(rnd.nextLong());
        ContainerJUnitTest.doReadWrite(context, candidate, false);
        candidate.beginFrame(new EventFrameExecution());
        candidate.remove(longField2);
        ContainerJUnitTest.doReadWrite(context, candidate, false);
        candidate.beginFrame(new EventFrameExecution());
        ((IntegerField) candidate.get(("sdf3"))).set(24);
        candidate.remove(longField);
        ContainerJUnitTest.doReadWrite(context, candidate, false);
    }

    private class JUnitDynamicContainer extends AbstractDynamicContainer {

        public JUnitDynamicContainer() {
            super(context.getIdentity(), "JUnitDynamicContainer-" + System.nanoTime(), Type.get(101), Domain.get(2), context, true);
        }
    }
}
