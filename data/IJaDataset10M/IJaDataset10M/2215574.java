package ch.oois.infocore.ejb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ch.oois.infocore.exception.IDNotFoundException;
import ch.oois.infocore.exception.InvalidRaceStateException;
import ch.oois.infocore.to.CompetitionRaceMetaTO;
import ch.oois.infocore.to.CompetitionTO;
import ch.oois.infocore.to.EventTO;
import ch.oois.infocore.to.RaceMetaTO;
import ch.oois.infocore.to.RaceTO;

/**
 * This test suite tests the creation, update and delete of races.
 *
 * @author Mario Daepp
 */
public class RaceManagementTest {

    private static final String[] RACE_NAMES = { "TestRace001-ABCD", "TestRace002-ABCD" };

    private static final int[] RACE_TYPES = { RaceTO.MIDDLE, RaceTO.MIDDLE };

    private static final int[] RACE_STATES = { RaceTO.READY, RaceTO.FINISHED };

    private InfoManagerRemote m_infoManager;

    private EventTO m_event;

    private CompetitionTO m_competition;

    private List<Integer> m_raceIds = new ArrayList<Integer>(RACE_NAMES.length);

    @BeforeClass
    public void beforeClass() throws Exception {
        m_infoManager = TestHelper.getRemoteEJB(InfoManagerRemote.class);
        m_event = m_infoManager.createEvent(new EventTO("TestEvent001-ABCD"));
        m_competition = m_infoManager.createCompetition(new CompetitionTO("TestComp001-ABCD"), m_event.getId());
    }

    @Test(groups = { "createRaces" })
    public void testCreateRaces() throws IDNotFoundException {
        for (int i = 0; i < RACE_NAMES.length; i++) {
            RaceTO race = new RaceTO(RACE_NAMES[i], RACE_TYPES[i], RACE_STATES[i]);
            race = m_infoManager.createRace(race, m_competition.getId());
            Assert.assertNotNull(race.getId());
            m_raceIds.add(race.getId());
        }
    }

    /**
   * - get all events as transfer objects
   * @throws IDNotFoundException
   */
    @Test(groups = { "testRaces" }, dependsOnGroups = { "createRaces" })
    public void testGetRaces() throws IDNotFoundException {
        Map<Integer, Integer> raceMap = new HashMap<Integer, Integer>();
        for (Integer id : m_raceIds) {
            raceMap.put(id, new Integer(1));
        }
        List<RaceMetaTO> races = m_infoManager.getRaces(m_competition.getId());
        Assert.assertNotNull(races);
        for (RaceMetaTO race : races) {
            raceMap.put(race.getId(), new Integer(0));
        }
        int sum = 0;
        for (Integer value : raceMap.values()) {
            sum += value.intValue();
        }
        Assert.assertEquals(0, sum);
    }

    @Test(groups = { "testRaces" }, dependsOnGroups = { "createRaces" })
    public void testUpdateRace() throws IDNotFoundException, InvalidRaceStateException {
        RaceTO race = new RaceTO(m_raceIds.get(0), "New name", RACE_TYPES[0], RACE_STATES[0]);
        race = m_infoManager.updateRace(race);
        Assert.assertEquals(race.getName(), "New name");
    }

    @Test(groups = { "testRaces" }, dependsOnGroups = { "createRaces" })
    public void testRaceTypesAndStates() throws IDNotFoundException, InvalidRaceStateException {
        List<CompetitionRaceMetaTO> competitions = m_infoManager.getCompetitions(m_event.getId());
        CompetitionRaceMetaTO competition = competitions.get(0);
        Map<String, Integer> raceTypes = competition.getRaceTypes();
        Assert.assertEquals(1, raceTypes.size());
        Integer type = raceTypes.get(RaceTO.TYPE_AS_TEXT[RaceTO.MIDDLE]);
        Assert.assertNotNull(type);
        Assert.assertEquals(new Integer(RaceTO.MIDDLE), type);
        Map<String, Integer> raceStates = competition.getRaceStates();
        Assert.assertEquals(3, raceStates.size());
        Integer state = raceStates.get(RaceTO.STATE_AS_TEXT[RaceTO.FINISHED]);
        Assert.assertNotNull(state);
        Assert.assertEquals(new Integer(RaceTO.FINISHED), state);
        state = raceStates.get(RaceTO.STATE_AS_TEXT[RaceTO.READY]);
        Assert.assertNotNull(state);
        Assert.assertEquals(new Integer(RaceTO.READY), state);
        state = raceStates.get(RaceTO.STATE_AS_TEXT[RaceTO.ALL]);
        Assert.assertNotNull(state);
        Assert.assertEquals(new Integer(RaceTO.ALL), state);
    }

    @Test(expectedExceptions = { InvalidRaceStateException.class }, groups = { "deleteRaces" }, dependsOnGroups = { "testRaces" })
    public void testDeleteRaceInInvalidState() throws IDNotFoundException, InvalidRaceStateException {
        m_infoManager.deleteRace(m_raceIds.get(0));
    }

    @Test(groups = { "deleteRaces" }, dependsOnGroups = { "testRaces" }, dependsOnMethods = { "testDeleteRaceInInvalidState" })
    public void testDeleteRaces() throws IDNotFoundException, InvalidRaceStateException {
        m_infoManager.closeRace(m_raceIds.get(0));
        for (Integer raceId : m_raceIds) {
            m_infoManager.deleteRace(raceId);
        }
        List<CompetitionRaceMetaTO> competitions = m_infoManager.getCompetitions(m_event.getId());
        Assert.assertEquals(CollectionUtils.size(competitions), 1);
        Assert.assertTrue(CollectionUtils.isEmpty(competitions.get(0).getRaces()));
    }

    @AfterClass
    public void afterClass() throws Exception {
        m_infoManager.deleteEvent(m_event.getId());
    }
}
