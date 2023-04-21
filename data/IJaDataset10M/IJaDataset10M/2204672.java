package wilos.test.model.misc.concreteiteration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import wilos.model.misc.concreteiteration.ConcreteIteration;
import wilos.model.spem2.iteration.Iteration;
import wilos.utils.Constantes.State;

public class ConcreteIterationTest {

    private ConcreteIteration concreteIteration;

    public static final String CONCRETENAME = "concreteName";

    public static final String PREFIX = "prefix";

    public static final Boolean IS_OPTIONAL = true;

    @Before
    public void setUp() {
        this.concreteIteration = new ConcreteIteration();
        this.concreteIteration.setConcreteName(CONCRETENAME);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testHashCode() {
        ConcreteIteration tmp = new ConcreteIteration();
        tmp.setConcreteName(CONCRETENAME);
        Iteration iteration = new Iteration();
        iteration.setPrefix(PREFIX);
        iteration.setIsOptional(IS_OPTIONAL);
        tmp.setIteration(iteration);
        this.concreteIteration.setIteration(iteration);
        assertNotNull(this.concreteIteration.hashCode());
        assertNotNull(tmp.hashCode());
        assertEquals(this.concreteIteration.hashCode(), tmp.hashCode());
    }

    @Test
    public void testEqualsObject() {
        ConcreteIteration tmp = new ConcreteIteration();
        tmp.setConcreteName(CONCRETENAME);
        Iteration iteration = new Iteration();
        iteration.setPrefix(PREFIX);
        iteration.setIsOptional(IS_OPTIONAL);
        tmp.setIteration(iteration);
        this.concreteIteration.setIteration(iteration);
        assertNotNull(this.concreteIteration);
        assertNotNull(tmp);
        assertTrue(this.concreteIteration.equals(tmp));
    }

    @Test
    public void testClone() {
        ConcreteIteration tmp = null;
        Iteration iteration = new Iteration();
        iteration.setPrefix(PREFIX);
        iteration.setIsOptional(IS_OPTIONAL);
        this.concreteIteration.setIteration(iteration);
        try {
            tmp = this.concreteIteration.clone();
        } catch (CloneNotSupportedException e) {
            fail("Error CloneNotSupportedException in the testClone method");
        }
        assertNotNull(tmp);
        assertEquals(tmp, this.concreteIteration);
    }

    @Test
    public void testAddIteration() {
        Iteration iteration = new Iteration();
        iteration.setPrefix(PREFIX);
        iteration.setIsOptional(IS_OPTIONAL);
        this.concreteIteration.addIteration(iteration);
        assertEquals(this.concreteIteration.getIteration(), iteration);
        assertTrue(iteration.getConcreteIterations().contains(this.concreteIteration));
        assertEquals(this.concreteIteration.getState(), State.CREATED);
    }

    @Test
    public void testRemoveIteration() {
        Iteration iteration = new Iteration();
        iteration.setPrefix(PREFIX);
        iteration.setIsOptional(IS_OPTIONAL);
        this.concreteIteration.addIteration(iteration);
        this.concreteIteration.removeIteration(iteration);
        assertNull(this.concreteIteration.getIteration());
        assertFalse(iteration.getConcreteIterations().contains(this.concreteIteration));
    }
}
