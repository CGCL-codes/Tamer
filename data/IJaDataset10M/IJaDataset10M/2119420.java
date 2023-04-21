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
public class ParameterWSSDTest {

    private EntityManager em;

    private HHPool hhPool = null;

    private List<Integer> selectedHandsList = null;

    private ParameterWSSD parameterWSSD = null;

    public ParameterWSSDTest(EntityManager em) {
        this.em = em;
        this.hhPool = new HHPool(em);
        this.selectedHandsList = new ArrayList();
    }

    public void fullTest() {
        getParameter_BigTest();
    }

    private void getParameter_BigTest() {
        String filePath = "pokertrainerweb/testfiles/";
        String fileName = "Poker_Stars_100.txt";
        String fileFullName = filePath + fileName;
        ImporterPokerStars importerPokerStars = new ImporterPokerStars(em);
        hhPool = importerPokerStars.importPokerStars(fileFullName);
        SelectorHands selectorHands = new SelectorHands(hhPool);
        selectedHandsList = selectorHands.selectAllActiveHands();
        parameterWSSD = new ParameterWSSD(hhPool, selectedHandsList);
        double testWSSD = parameterWSSD.getWSSD();
        double standardWSSD = 4.7D;
        double delta = 9.0E-02D;
        Assert.assertEquals(testWSSD, standardWSSD, delta);
    }
}
