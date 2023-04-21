package ru.newton.pokertrainer.businesslogic.statistics.selectorhands;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.newton.pokertrainer.businesslogic.databaseservices.statistics.SelectorHH;
import ru.newton.pokertrainer.businesslogic.statistics.constant.SelectorType;
import ru.newton.pokertrainer.entities.dictionary.LimitType;
import ru.newton.pokertrainer.entities.dictionary.PokerType;
import ru.newton.pokertrainer.entities.statistics.SelectedHH;
import ru.newton.pokertrainer.entities.users.Site;
import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author newton
 *         Date: Mar 30, 2011
 */
public class SelectorHandsTest {

    private static final long TEST_USER_ID = 1;

    private ApplicationContext context;

    private SelectorHands selectorHands;

    @Before
    public void setup() {
        context = new ClassPathXmlApplicationContext("spring.xml");
        selectorHands = context.getBean(SelectorHands.class);
    }

    @Test
    public void saveSelectorTest() {
        SelectedHH selectedHH = new SelectedHH();
        selectedHH.setUserId(TEST_USER_ID);
        selectedHH.setSelectorType(SelectorType.ALL_HANDS);
        selectorHands.deleteSelector(TEST_USER_ID);
        selectorHands.saveSelector(TEST_USER_ID, selectedHH);
        SelectedHH testSelectedHH = selectorHands.loadSelector(TEST_USER_ID);
        Assert.assertTrue(selectedHH.equals(testSelectedHH));
        selectedHH.setSelectorType(SelectorType.COUNT_LAST_DAYS);
        testSelectedHH.setSelectorType(SelectorType.COUNT_LAST_DAYS);
        selectorHands.saveSelector(TEST_USER_ID, selectedHH);
        testSelectedHH = selectorHands.loadSelector(TEST_USER_ID);
        Assert.assertTrue(selectedHH.equals(testSelectedHH));
    }

    @Test
    public void loadSelectorTest() {
        SelectedHH selectedHH = new SelectedHH();
        selectedHH.setUserId(TEST_USER_ID);
        selectedHH.setSelectorType(SelectorType.ALL_HANDS);
        selectorHands.deleteSelector(TEST_USER_ID);
        selectorHands.saveSelector(TEST_USER_ID, selectedHH);
        SelectedHH testSelectedHH = selectorHands.loadSelector(TEST_USER_ID);
        Assert.assertTrue(selectedHH.equals(testSelectedHH));
        selectorHands.deleteSelector(TEST_USER_ID);
        testSelectedHH = selectorHands.loadSelector(TEST_USER_ID);
        Assert.assertEquals(null, testSelectedHH);
    }
}
