package de.gstpl.algo.genetic;

import de.gstpl.MyTestCase;
import de.gstpl.algo.AlgorithmFactory;
import de.gstpl.algo.TimetableObjectCollection;
import de.gstpl.algo.GeneralAlgoProperties;
import de.gstpl.data.ApplicationProperties;
import de.gstpl.data.ITimeInterval;
import java.util.ArrayList;
import java.util.List;

/**
 * Try to sort collections in GeneticAlgorithmA to avoid random query results
 * from cayenne (or db4o?).
 *
 * @author Peter Karich, peat_hal 'at' users 'dot' sourceforge 'dot' net
 */
public class GeneticAlgorithmATest extends MyTestCase {

    private GeneralAlgoProperties globalProp;

    private GeneticAlgorithmA gaInstance;

    private TimetableObjectCollection algorithmObjs;

    public GeneticAlgorithmATest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        gaInstance = AlgorithmFactory.create(GeneticAlgorithmA.class);
        algorithmObjs = gaInstance.getObjects();
        globalProp = algorithmObjs.getProperties();
        globalProp.setGuiAvailable(false);
        globalProp.setEnableSortingOnStart(true);
    }

    public void testMutation() throws Exception {
        System.out.println("mutation");
        GeneticAlgoProperties defaultProp = ((GeneticAlgoProperties) gaInstance.getProperties());
        long randomSeed = 123456;
        defaultProp.setRoomChangeRate(0.5f);
        defaultProp.setDayChangeRate(1f);
        defaultProp.setStartTimeChangeRate(10f);
        defaultProp.setMutationRate(0.8f);
        ApplicationProperties.get().setRandomSeed(randomSeed);
        gaInstance.doInitialization();
        algorithmObjs.prepareNextWeek();
        gaInstance.startOneWeek();
        Individuum origIndividuum = new Individuum();
        origIndividuum.addAllTimeIntervals(algorithmObjs.getTimeIntervals());
        int expEval = 829;
        List<Individuum> indis = new ArrayList<Individuum>(3);
        indis.add(new Individuum(origIndividuum));
        indis.add(new Individuum(origIndividuum));
        indis.add(new Individuum(origIndividuum));
        gaInstance.setIndividuums(indis);
        gaInstance.mutation();
        gaInstance.eval();
        gaInstance.select();
        assertEquals(expEval, indis.get(0).getEvaluation());
        Individuum cloned = (Individuum) indis.get(0).clone();
        ApplicationProperties.get().getRandom().setSeed(randomSeed);
        indis.set(0, new Individuum(origIndividuum));
        indis.set(1, new Individuum(origIndividuum));
        indis.set(2, new Individuum(origIndividuum));
        gaInstance.setIndividuums(indis);
        gaInstance.mutation();
        gaInstance.eval();
        gaInstance.select();
        assertEquals(expEval, indis.get(0).getEvaluation());
        assertEquals(cloned, indis.get(0));
    }

    public void testIndividuum() throws Exception {
        System.out.println("mutation");
        gaInstance.doInitialization();
        algorithmObjs.prepareNextWeek();
        List<? extends ITimeInterval> list = algorithmObjs.getTimeIntervals().subList(0, 10);
        Individuum indi1 = new Individuum();
        indi1.addAllTimeIntervals(list);
        Individuum indi2 = new Individuum();
        indi2.addAllTimeIntervals(list);
        Individuum indi3 = new Individuum(indi1, indi2, ApplicationProperties.get().getRandom().nextInt(indi1.size()));
        assertEquals(list.size(), indi1.size());
        assertEquals(list.size(), indi2.size());
        assertEquals(list.size(), indi3.size());
    }

    public void testEnsureSize() throws Exception {
        List<Individuum> indis = new ArrayList<Individuum>(3);
        gaInstance.doInitialization();
        algorithmObjs.prepareNextWeek();
        Individuum origIndividuum = new Individuum();
        origIndividuum.addAllTimeIntervals(algorithmObjs.getTimeIntervals());
        indis.add(new Individuum(origIndividuum));
        indis.add(new Individuum(origIndividuum));
        indis.add(new Individuum(origIndividuum));
        int expected = indis.size() + 5;
        gaInstance.setIndividuums(indis);
        indis = gaInstance.ensureSize(expected);
        assertEquals(expected, indis.size());
        expected = indis.size() - 2;
        gaInstance.setIndividuums(indis);
        indis = gaInstance.ensureSize(expected);
        assertEquals(expected, indis.size());
    }
}
