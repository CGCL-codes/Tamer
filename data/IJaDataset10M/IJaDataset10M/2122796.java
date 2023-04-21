package org.jheuristics.ga.operators.selectors;

import java.util.Arrays;
import org.easymock.MockControl;
import org.jheuristics.Individual;
import org.jheuristics.ga.Population;
import junit.framework.TestCase;

public class WorseIndividualsSelectorTest extends TestCase {

    private MockControl controlPopulation;

    private MockControl controlIndividual[] = new MockControl[10];

    private Individual individuals[] = new Individual[controlIndividual.length];

    private Population pop;

    private WorseIndividualsSelector selector;

    protected void setUp() throws Exception {
        super.setUp();
        controlPopulation = MockControl.createStrictControl(Population.class);
        for (int i = 0; i < controlIndividual.length; i++) {
            controlIndividual[i] = MockControl.createNiceControl(Individual.class);
            individuals[i] = (Individual) controlIndividual[i].getMock();
        }
        pop = (Population) controlPopulation.getMock();
        selector = new WorseIndividualsSelector();
    }

    public final void testSelectExceptions() {
        try {
            selector.select(null, 0, null, null);
            fail();
        } catch (NullPointerException e) {
        }
        try {
            selector.select(pop, -1, null, null);
            fail();
        } catch (IllegalArgumentException e) {
        }
        pop.size();
        controlPopulation.setReturnValue(9);
        controlPopulation.replay();
        try {
            selector.select(pop, 10, null, null);
            fail();
        } catch (IllegalArgumentException e) {
        }
        controlPopulation.verify();
    }

    public final void testSelect0() {
        pop.size();
        controlPopulation.setReturnValue(10);
        controlPopulation.replay();
        assertEquals(0, selector.select(pop, 0, null, null).length);
        controlPopulation.verify();
    }

    public final void testSelect1() {
        pop.size();
        controlPopulation.setReturnValue(individuals.length);
        pop.iterator();
        controlPopulation.setReturnValue(Arrays.asList(individuals).iterator());
        controlPopulation.setReturnValue(Arrays.asList(individuals).iterator());
        controlPopulation.replay();
        for (int i = 0; i < controlIndividual.length; i++) {
            controlIndividual[i].replay();
        }
        Individual[] selected = selector.select(pop, 1, null, null);
        assertEquals(individuals[0], selected[0]);
        controlPopulation.verify();
    }

    public final void testSelect5() {
        pop.size();
        controlPopulation.setReturnValue(individuals.length);
        pop.iterator();
        controlPopulation.setReturnValue(Arrays.asList(individuals).iterator());
        controlPopulation.setReturnValue(Arrays.asList(individuals).iterator());
        controlPopulation.replay();
        for (int i = 0; i < controlIndividual.length; i++) {
            controlIndividual[i].replay();
        }
        Individual[] selected = selector.select(pop, 5, null, null);
        for (int i = 0; i < 5; i++) {
            assertEquals(individuals[i], selected[i]);
        }
        controlPopulation.verify();
    }

    public final void testSelectSorting() {
        pop.size();
        controlPopulation.setReturnValue(3);
        pop.iterator();
        controlPopulation.setReturnValue(Arrays.asList(new Individual[] { individuals[1], individuals[2], individuals[0] }).iterator());
        controlPopulation.setReturnValue(Arrays.asList(new Individual[] { individuals[1], individuals[2], individuals[0] }).iterator());
        controlPopulation.replay();
        individuals[0].compareTo(individuals[1]);
        controlIndividual[0].setReturnValue(1);
        individuals[0].compareTo(individuals[2]);
        controlIndividual[0].setReturnValue(1);
        individuals[1].compareTo(individuals[0]);
        controlIndividual[1].setReturnValue(-1);
        individuals[1].compareTo(individuals[2]);
        controlIndividual[1].setReturnValue(1);
        individuals[2].compareTo(individuals[0]);
        controlIndividual[2].setReturnValue(-1);
        individuals[2].compareTo(individuals[1]);
        controlIndividual[2].setReturnValue(-1);
        for (int i = 0; i < 3; i++) {
            controlIndividual[i].replay();
        }
        Individual[] selected = selector.select(pop, 2, null, null);
        for (int i = 0; i < 2; i++) {
            assertEquals(new Integer(i).toString(), individuals[i + 1], selected[i]);
        }
        controlPopulation.verify();
    }
}
