package be.smals.bowlinggame.scorebord.dao;

import javax.annotation.Resource;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.dialect.HSQLDialect;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.unitils.UnitilsTestExecutionListener;
import org.unitils.dataset.annotation.RefreshDataSet;
import org.unitils.hibernate.util.HibernateAssert;
import org.unitils.inject.annotation.InjectIntoByType;
import org.unitils.inject.annotation.TestedObject;
import be.smals.bowlinggame.scorebord.domain.Player;

@RefreshDataSet
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "Hibernate-test-context.xml" })
@TestExecutionListeners(listeners = { DependencyInjectionTestExecutionListener.class, UnitilsTestExecutionListener.class })
public class BasicTestSetup {

    @TestedObject
    private PlayerDao playerDao;

    @Resource
    @InjectIntoByType
    private SessionFactory sessionFactory;

    @Test
    public void checkingTheDatabase() throws Exception {
        Assert.assertNotNull(sessionFactory);
        Assert.assertNotNull(playerDao);
        playerDao.afterPropertiesSet();
    }

    @Test
    public void testFindById() {
        Player findPlayer = playerDao.findPlayer(1L);
        Assert.assertNotNull(findPlayer);
        Assert.assertEquals("Jeroen", findPlayer.getName());
        Assert.assertNotNull(findPlayer.getGameResults());
        Assert.assertEquals(1, findPlayer.getGameResults().size());
        Assert.assertNotNull(findPlayer.getTrophies());
        Assert.assertEquals(1, findPlayer.getTrophies().size());
    }

    @Test
    public void testTheMappings() {
        sessionFactory.getCurrentSession().beginTransaction();
        HibernateAssert.assertMappingWithDatabaseConsistent(new Configuration().configure(), sessionFactory.getCurrentSession(), new HSQLDialect());
        sessionFactory.getCurrentSession().getTransaction().commit();
    }

    @Test
    public void testCreatePlayer() {
        Player player = new Player("Hans-Willem Dezutter");
        playerDao.saveNewPlayer(player);
    }
}
