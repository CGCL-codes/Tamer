package fulmine.model.container;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import fulmine.Domain;
import fulmine.IDomain;
import fulmine.IType;
import fulmine.Type;
import fulmine.context.FulmineContext;
import fulmine.context.IFrameworkContext;
import fulmine.model.container.impl.Record;
import fulmine.model.field.containerdefinition.ContainerDefinitionField;
import fulmine.model.field.containerdefinition.IContainerDefinitionField;

/**
 * Test cases for the {@link ContainerFactory}
 * 
 * @author Ramon Servadei
 * 
 */
public class ContainerFactoryJUnitTest {

    private static final IDomain DOMAIN = Domain.get(45);

    private static final String RECORD_NAME = "lasers";

    private static final IType CODE = Type.get(Type.BASE_USER_START + 1, "code");

    private static final IType UNREGISTERED = Type.get(CODE.value() + 1);

    private ContainerDefinitionField containerDefinition;

    IFrameworkContext context = new FulmineContext(getClass().getSimpleName());

    private Record record;

    JUnitContainerBuilder builder;

    ContainerFactory candidate;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        context.start();
        record = new Record(context.getIdentity(), RECORD_NAME, CODE, DOMAIN, context, true);
        builder = new JUnitContainerBuilder();
        candidate = new ContainerFactory();
        containerDefinition = new ContainerDefinitionField("junit");
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
     * {@link fulmine.model.container.ContainerFactory#registerBuilder(IType, fulmine.model.container.ContainerFactory.IContainerBuilder)}
     * .
     */
    @Test
    public void testRegisterBuilder() {
        candidate.registerBuilder(CODE, builder);
        assertTrue("registerBuilder", candidate.containsType(CODE));
    }

    /**
     * Test method for
     * {@link fulmine.model.container.ContainerFactory#registerBuilder(IType, fulmine.model.container.ContainerFactory.IContainerBuilder)}
     * .
     */
    @Test
    public void testRegisterInvalidTypeBuilder() {
        try {
            candidate.registerBuilder(Type.get(Type.BASE_USER_START - 2), builder);
            fail("Should throw exception");
        } catch (IllegalArgumentException e) {
        }
    }

    /**
     * Test method for
     * {@link fulmine.model.container.ContainerFactory#containsType(IType)}.
     */
    @Test
    public void testContainsType() {
        assertFalse("registerBuilder", candidate.containsType(UNREGISTERED));
    }

    /**
     * Test method for
     * {@link fulmine.model.container.ContainerFactory#getDefinition(IType)}.
     */
    @Test
    public void testGetDefinition() {
        try {
            candidate.getDefinition(UNREGISTERED);
            fail("Should throw exception for unknown code");
        } catch (IllegalArgumentException e) {
        }
        candidate.registerBuilder(CODE, builder);
        assertEquals("definition", containerDefinition, candidate.getDefinition(CODE));
    }

    /**
     * Test method for
     * {@link fulmine.model.container.ContainerFactory#createContainer(java.lang.String, int)}
     * .
     */
    @Test
    public void testCreateContainer() {
        assertEquals("container", new Record(context.getIdentity(), "lasers", UNREGISTERED, DOMAIN, context, true), candidate.createContainer(context.getIdentity(), "lasers", UNREGISTERED, DOMAIN, context, true));
        candidate.registerBuilder(CODE, builder);
        assertEquals("container", record, candidate.createContainer(context.getIdentity(), RECORD_NAME, CODE, DOMAIN, context, true));
    }

    private class JUnitContainerBuilder implements IContainerFactory.IContainerBuilder {

        public IContainer createContainer(String nativeContextIdentity, String identity, IType type, IDomain domain, IFrameworkContext hostContext, boolean local) {
            return record;
        }

        public IContainerDefinitionField createContainerDefinition() {
            return containerDefinition;
        }
    }
}
