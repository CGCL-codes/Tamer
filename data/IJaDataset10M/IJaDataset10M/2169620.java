package org.matsim.plans;

import org.matsim.basic.v01.IdImpl;
import org.matsim.testcases.MatsimTestCase;

public class PersonTest extends MatsimTestCase {

    /**
	 * Tests {@link org.matsim.plans.Person#removeWorstPlans(int)} when all
	 * plans have the type <code>null</code>.
	 *
	 * @author mrieser
	 */
    public void testRemoveWorstPlans_nullType() {
        Person person = new Person(new IdImpl("1"));
        Plan plan1 = new Plan("15.0", person);
        Plan plan2 = new Plan("22.0", person);
        Plan plan3 = new Plan(null, person);
        Plan plan4 = new Plan("1.0", person);
        Plan plan5 = new Plan("18.0", person);
        person.addPlan(plan1);
        person.addPlan(plan2);
        person.addPlan(plan3);
        person.addPlan(plan4);
        person.addPlan(plan5);
        assertEquals("test we have all plans we want", 5, person.getPlans().size());
        person.removeWorstPlans(6);
        assertEquals("test that no plans are removed if maxSize > plans.size()", 5, person.getPlans().size());
        person.removeWorstPlans(5);
        assertEquals("test that no plans are removed if maxSize == plans.size()", 5, person.getPlans().size());
        person.removeWorstPlans(4);
        assertEquals("test that a plan was removed", 4, person.getPlans().size());
        assertFalse("test that plan with undefined score was removed.", person.getPlans().contains(plan3));
        person.removeWorstPlans(3);
        assertEquals("test that a plan was removed", 3, person.getPlans().size());
        assertFalse("test that the plan with minimal score was removed", person.getPlans().contains(plan4));
        person.removeWorstPlans(1);
        assertEquals("test that two plans were removed", 1, person.getPlans().size());
        assertTrue("test that the plan left has highest score", person.getPlans().contains(plan2));
    }

    /**
	 * Tests {@link org.matsim.plans.Person#removeWorstPlans(int)} when the
	 * plans have different types set.
	 *
	 * @author mrieser
	 */
    public void testRemoveWorstPlans_withTypes() {
        Person person = new Person(new IdImpl("1"));
        Plan plan1 = new Plan("15.0", person);
        plan1.setType(Plan.Type.CAR);
        Plan plan2 = new Plan("22.0", person);
        plan2.setType(Plan.Type.PT);
        Plan plan3 = new Plan(null, person);
        plan3.setType(Plan.Type.CAR);
        Plan plan4 = new Plan("1.0", person);
        plan4.setType(Plan.Type.PT);
        Plan plan5 = new Plan("18.0", person);
        plan5.setType(Plan.Type.CAR);
        Plan plan6 = new Plan("21.0", person);
        plan6.setType(Plan.Type.PT);
        person.addPlan(plan1);
        person.addPlan(plan2);
        person.addPlan(plan3);
        person.addPlan(plan4);
        person.addPlan(plan5);
        person.addPlan(plan6);
        assertEquals("test we have all plans we want", 6, person.getPlans().size());
        person.removeWorstPlans(4);
        assertEquals("test that two plans were removed", 4, person.getPlans().size());
        assertFalse("test that plan with undefined score was removed.", person.getPlans().contains(plan3));
        assertFalse("test that plan with worst score was removed.", person.getPlans().contains(plan4));
        person.removeWorstPlans(2);
        assertEquals("test that two plans were removed", 2, person.getPlans().size());
        assertFalse("test that the plan with worst score was removed", person.getPlans().contains(plan1));
        assertTrue("test that the now only plan of type a was not removed", person.getPlans().contains(plan5));
        assertFalse("test that the plan with the 2nd-worst score was removed", person.getPlans().contains(plan6));
        person.removeWorstPlans(1);
        assertEquals("test that no plans were removed", 2, person.getPlans().size());
        assertTrue("test that the plan with highest score of type a was not removed", person.getPlans().contains(plan5));
        assertTrue("test that the plan with highest score of type b was not removed", person.getPlans().contains(plan2));
    }

    /**
	 * Tests that after a call to {@link org.matsim.plans.Person#removeWorstPlans(int)}
	 * the person still has a selected plan, even when the previously selected plan was
	 * the one with the worst score.
	 *
	 * @author mrieser
	 */
    public void testRemoveWorstPlans_selectedPlan() {
        Person person = new Person(new IdImpl("1"));
        Plan plan1 = new Plan("15.0", person);
        Plan plan2 = new Plan("22.0", person);
        Plan plan3 = new Plan(null, person);
        Plan plan4 = new Plan("1.0", person);
        Plan plan5 = new Plan("18.0", person);
        Plan plan6 = new Plan("21.0", person);
        person.addPlan(plan1);
        person.addPlan(plan2);
        person.addPlan(plan3);
        person.addPlan(plan4);
        person.addPlan(plan5);
        person.addPlan(plan6);
        person.setSelectedPlan(plan1);
        assertEquals(plan1, person.getSelectedPlan());
        person.removeWorstPlans(5);
        assertEquals(plan1, person.getSelectedPlan());
        person.removeWorstPlans(3);
        assertFalse("plan should no longer be selected!", plan1.isSelected());
        assertNotSame("plan1 should no longer be the selected plan!", plan1, person.getSelectedPlan());
        assertNotNull("person has no selected plan!", person.getSelectedPlan());
        assertTrue((person.getSelectedPlan() == plan2) || (person.getSelectedPlan() == plan5) || (person.getSelectedPlan() == plan6));
        person.removeWorstPlans(1);
        assertEquals(plan2, person.getSelectedPlan());
    }

    /**
	 * Test {@link org.matsim.plans.Person#getRandomUnscoredPlan()} when the
	 * @author mrieser
	 */
    public void testGetRandomUnscoredPlan() {
        Plans population = new Plans(Plans.NO_STREAMING);
        Person person = null;
        Plan[] plans = new Plan[10];
        try {
            person = new Person(new IdImpl(1));
            plans[0] = person.createPlan(null, "no");
            plans[1] = person.createPlan("0.0", "no");
            plans[2] = person.createPlan(null, "no");
            plans[3] = person.createPlan("-50.0", "no");
            plans[4] = person.createPlan("+50.0", "no");
            plans[5] = person.createPlan("+50.0", "no");
            plans[6] = person.createPlan("+60.0", "no");
            plans[7] = person.createPlan(null, "no");
            plans[8] = person.createPlan("-10.0", "no");
            plans[9] = person.createPlan(null, "no");
            population.addPerson(person);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Plan plan = person.getRandomUnscoredPlan();
        assertTrue(plan.hasUndefinedScore());
        plan.setScore(1.0);
        plan = person.getRandomUnscoredPlan();
        assertTrue(plan.hasUndefinedScore());
        plan.setScore(2.0);
        plan = person.getRandomUnscoredPlan();
        assertTrue(plan.hasUndefinedScore());
        plan.setScore(3.0);
        plan = person.getRandomUnscoredPlan();
        assertTrue(plan.hasUndefinedScore());
        plan.setScore(4.0);
        plan = person.getRandomUnscoredPlan();
        assertNull(plan);
        for (int i = 0; i < plans.length; i++) {
            assertFalse(plans[i].hasUndefinedScore());
        }
    }
}
