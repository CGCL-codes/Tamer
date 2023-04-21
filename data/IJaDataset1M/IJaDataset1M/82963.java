package com.yawnefpark.scorekeeper.state;

import static org.junit.Assert.assertEquals;
import org.junit.After;
import org.junit.Before;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.yawnefpark.scorekeeper.domain.AtBatResultType;
import com.yawnefpark.scorekeeper.factory.RulesetFactoryEnum;

/**
 * @author sperreault
 */
public class StatusBaseTest {

    private final String[] contextFiles = { "configs/applicationContext.xml", "configs/dao-context.xml" };

    public static final boolean HOME_TEAM_BATTING = false;

    public static final boolean VISITING_TEAM_BATTING = true;

    protected CurrentSituationContext getCS() {
        return CurrentSituationContext.getInstance();
    }

    /**
	 * @param name
	 */
    public StatusBaseTest() {
        ApplicationContext context = new ClassPathXmlApplicationContext(contextFiles);
        context.getBean("currentSituationContext");
    }

    /**
	 * @see junit.framework.TestCase#setUp()
	 */
    @Before
    public void setUp() throws Exception {
        System.out.println("Initializing application");
        getCS().resetState();
    }

    /**
	 * @see junit.framework.TestCase#tearDown()
	 */
    @After
    public void tearDown() throws Exception {
    }

    protected void assertAndDisplayCurrentStatus(int outs, boolean onFirst, boolean onSecond, boolean onThird, boolean visitingTeamAtBat, int visitingTeamScore, int homeTeamScore) {
        System.out.println(getCS().getCurrentSituationString());
        assertCurrentStatus(outs, onFirst, onSecond, onThird, visitingTeamAtBat, visitingTeamScore, homeTeamScore);
    }

    protected void assertCurrentStatus(int outs, boolean onFirst, boolean onSecond, boolean onThird, boolean visitingTeamAtBat, int visitingTeamScore, int homeTeamScore) {
        assertEquals("Expected " + outs + " outs but was " + getCS().getOuts() + " outs.", outs, getCS().getOuts());
        assertEquals("Expected " + onFirst + " onFirst", onFirst, getCS().isOnFirst());
        assertEquals("Expected " + onSecond + " onSecond", onSecond, getCS().isOnSecond());
        assertEquals("Expected " + onThird + " onThird", onThird, getCS().isOnThird());
        assertEquals("Expected " + (visitingTeamAtBat ? " visitingTeam at bat" : " homeTeam at bat"), visitingTeamAtBat, !getCS().isHomeTeamAtBat());
        assertEquals("Expected " + visitingTeamScore + " visitingTeamScore", visitingTeamScore, getCS().getVisitingTeamRuns());
        assertEquals("Expected " + homeTeamScore + " homeTeamScore", homeTeamScore, getCS().getHomeTeamRuns());
    }

    /**
	 * 
	 * @param atBatResultTypeEnum
	 */
    protected void processAtBat(AtBatResultType atBatResultTypeEnum) {
        getCS().executeAtBatResult(atBatResultTypeEnum);
    }

    /**
	 * 
	 * @param stateEnum
	 * @return
	 */
    protected State createStateFromDefaultStateFactory(StateEnum stateEnum) {
        return StateFactory.createStateFactoryState(RulesetFactoryEnum.DEFAULT_RULESET, stateEnum);
    }
}
