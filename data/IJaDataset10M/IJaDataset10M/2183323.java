package model.entities.gamemode;

import static org.junit.Assert.assertTrue;
import model.entities.gamemode.RuleModel;
import model.entities.gamemode.TeamModel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utilities.Constants;
import controller.MainControllerFactory;
import controller.entities.gamemode.RuleFactory;
import controller.entities.gamemode.TeamFactory;

public final class RuleModelTest {

    private RuleModel model;

    @Before
    public void setUp() throws Exception {
        model = MainControllerFactory.get().instantiate(new RuleFactory()).getModel();
    }

    @After
    public void tearDown() throws Exception {
        MainControllerFactory.get().removeAll();
    }

    @Test
    public void testCheckVictory() {
        TeamModel teamModel = MainControllerFactory.get().instantiate(new TeamFactory()).getModel();
        assertTrue(!model.checkVictory(teamModel.getScore(), teamModel.getTeamName()));
        teamModel.addPoints(model.getScoreLimit() + 1);
        assertTrue(model.checkVictory(teamModel.getScore(), teamModel.getTeamName()));
    }

    @Test
    public void testGetGeometry() {
        assertTrue(model.getGeometry() != null);
    }

    @Test
    public void testGetScoreLimit() {
        assertTrue(model.getScoreLimit() == Constants.SCORE_LIMIT);
    }

    @Test
    public void testGetBody() {
        assertTrue(model.getBody() != null);
    }
}
