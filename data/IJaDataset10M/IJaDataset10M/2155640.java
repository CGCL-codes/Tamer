package de.fzi.herakles.strategy.component.impl;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.semanticweb.owl.apibinding.OWLManager;
import org.semanticweb.owl.model.OWLOntologyManager;
import de.fzi.herakles.DummyReasonerAdapter;
import de.fzi.herakles.ReasonerAdapter;
import de.fzi.herakles.strategy.component.Selector;

/**
 * @author bock
 *
 */
public class PropertyConstraintSelectorTest {

    private static OWLOntologyManager manager;

    private ReasonerAdapter[] reasoners;

    private Set<ReasonerAdapter> reasonersSet;

    /**
	 * @throws java.lang.Exception
	 */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        manager = OWLManager.createOWLOntologyManager();
    }

    /**
	 * @throws java.lang.Exception
	 */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        manager = null;
    }

    /**
	 * @throws java.lang.Exception
	 */
    @Before
    public void setUp() throws Exception {
        this.reasoners = new ReasonerAdapter[10];
        this.reasonersSet = new HashSet<ReasonerAdapter>();
        for (int i = 0; i < 10; i++) {
            ReasonerAdapter reasoner = new DummyReasonerAdapter(manager, "DummyReasoner" + i);
            reasoner.getReasonerProperties().setProperty("testproperty", String.valueOf(i % 3));
            this.reasoners[i] = reasoner;
            this.reasonersSet.add(reasoner);
        }
    }

    /**
	 * @throws java.lang.Exception
	 */
    @After
    public void tearDown() throws Exception {
    }

    /**
	 * Test method for {@link de.fzi.herakles.strategy.component.impl.PropertyConstraintSelector#select()}.
	 */
    @Test
    public final void testSelect() {
        PropertyConstraintSelector selector = new PropertyConstraintSelector();
        selector.setReasonerPool(reasonersSet);
        selector.addConstraint("testproperty", "0");
        assertTrue("Constraint not met", selector.select().contains(reasoners[0]));
        assertTrue("Constraint not met", selector.select().contains(reasoners[3]));
        assertTrue("Constraint not met", selector.select().contains(reasoners[6]));
        assertTrue("Constraint not met", selector.select().contains(reasoners[9]));
        selector.clearConstraints();
        selector.addConstraint("testproperty", "1");
        assertTrue("Constraint not met", selector.select().contains(reasoners[1]));
        assertTrue("Constraint not met", selector.select().contains(reasoners[4]));
        assertTrue("Constraint not met", selector.select().contains(reasoners[7]));
        selector.clearConstraints();
        selector.addConstraint("testproperty", "2");
        assertTrue("Constraint not met", selector.select().contains(reasoners[2]));
        assertTrue("Constraint not met", selector.select().contains(reasoners[5]));
        assertTrue("Constraint not met", selector.select().contains(reasoners[8]));
    }
}
