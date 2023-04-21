package ru.newton.pokertrainer.businesslogic.statistics.parameters;

import org.junit.Assert;
import ru.newton.pokertrainer.businesslogic.databaseservices.handhistory.pool.HHPool;
import ru.newton.pokertrainer.businesslogic.importmodule.pokerstars.ImporterPokerStars;
import ru.newton.pokertrainer.businesslogic.statistics.SelectorHands;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

/**
 * @author newton
 *         Date: Feb 28, 2011
 */
public class ParameterHandsCountTest {

    private EntityManager em;

    private HHPool hhPool = null;

    private List<Integer> selectedHandsList = null;

    private ParameterHandsCount parameterHandsCount = null;

    public ParameterHandsCountTest(EntityManager em) {
        this.em = em;
        this.hhPool = new HHPool(em);
        this.selectedHandsList = new ArrayList();
    }

    public void fullTest() {
        getParameter_SmallTest();
        getParameter_BigTest();
    }

    private void getParameter_SmallTest() {
        String filePath = "pokertrainerweb/testfiles/";
        String fileName = "statistic_analyzer_small.txt";
        String fileFullName = filePath + fileName;
        ImporterPokerStars importerPokerStars = new ImporterPokerStars(em);
        hhPool = importerPokerStars.importPokerStars(fileFullName);
        SelectorHands selectorHands = new SelectorHands(hhPool);
        selectedHandsList = selectorHands.selectAll();
        parameterHandsCount = new ParameterHandsCount(hhPool, selectedHandsList);
        Assert.assertTrue(parameterHandsCount.getHandsCount() == 6);
    }

    private void getParameter_BigTest() {
        String filePath = "pokertrainerweb/testfiles/";
        String fileName = "Poker_Stars_100.txt";
        String fileFullName = filePath + fileName;
        ImporterPokerStars importTestUtilities = new ImporterPokerStars(null);
        hhPool = importTestUtilities.importPokerStars(fileFullName);
        SelectorHands selectorHands = new SelectorHands(hhPool);
        selectedHandsList = selectorHands.selectAll();
        parameterHandsCount = new ParameterHandsCount(hhPool, selectedHandsList);
        Assert.assertTrue(parameterHandsCount.getHandsCount() == 100);
    }
}
