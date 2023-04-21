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
public class ParameterPFRTest {

    private EntityManager em;

    private HHPool hhPool = null;

    private List<Integer> selectedHandsList = null;

    private ParameterPFR parameterPFR = null;

    public ParameterPFRTest(EntityManager em) {
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
        parameterPFR = new ParameterPFR(hhPool, selectedHandsList);
        double testPFR = parameterPFR.getPFR();
        double standardPFR = 13.8D;
        double delta = 9.0E-02D;
        Assert.assertEquals(testPFR, standardPFR, delta);
    }
}
