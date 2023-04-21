package com.cosmocoder.monopoly.test;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import javax.sql.DataSource;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.orm.hibernate3.LocalSessionFactoryBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import com.cosmocoder.monopoly.dao.UserDao;
import com.cosmocoder.monopoly.domain.Player;
import com.cosmocoder.monopoly.domain.Room;
import com.cosmocoder.monopoly.domain.Theme;
import com.cosmocoder.monopoly.domain.User;
import com.cosmocoder.persistence.Dao;
import com.cosmocoder.persistence.DaoFactory;

@TransactionConfiguration
@Transactional
@ContextConfiguration(locations = { "/com/cosmocoder/monopoly/config/MonopolyContext.xml" })
public class DaoTest extends AbstractTransactionalJUnit4SpringContextTests {

    @Test
    public void testDaoNow() {
        ApplicationContext appContext = new ClassPathXmlApplicationContext("com/cosmocoder/monopoly/config/MonopolyContext.xml");
        LocalSessionFactoryBean sessionFactory = (LocalSessionFactoryBean) appContext.getBean("&sessionFactory");
        Configuration cfg = sessionFactory.getConfiguration();
        SchemaExport schemaExport = new SchemaExport(cfg);
        try {
            Field declaredField = LocalSessionFactoryBean.class.getDeclaredField("configTimeDataSourceHolder");
            declaredField.setAccessible(true);
            @SuppressWarnings("unchecked") ThreadLocal<DataSource> tl = (ThreadLocal<DataSource>) (declaredField.get(null));
            tl.set((DataSource) appContext.getBean("dataSource"));
        } catch (Throwable e) {
            e.printStackTrace();
        }
        schemaExport.create(false, true);
        UserDao userDao3 = (UserDao) appContext.getBean("userDao");
        DaoFactory daoFactory = (DaoFactory) appContext.getBean("daoFactory");
        daoFactory.setDaoForEntity(User.class, userDao3);
        Dao<User, String> userDao = daoFactory.getDao(User.class);
        Dao<Player, Long> playerDao = daoFactory.getDao(Player.class);
        Dao<Room, Long> roomDao = daoFactory.getDao(Room.class);
        Dao<Theme, Long> themeDao = daoFactory.getDao(Theme.class);
        Theme theme = new Theme();
        theme.setName("jingle bells");
        themeDao.makePersistent(theme);
        Room room = new Room();
        room.setName("room dos jingles");
        room.setTheme(theme);
        room = roomDao.makePersistent(room);
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setLogin("jingle" + i);
            user = userDao.makePersistent(user);
            Player player = new Player();
            player.setRoom(room);
            player.setUser(user);
            room.getPlayers().add(player);
            user.getPlayers().add(player);
            playerDao.makePersistent(player);
        }
        User user2 = userDao.get("jingle1");
        System.out.println(user2.getLogin());
        Collection<Room> gotRooms = roomDao.getByProperties(new HashMap<String, Object>() {

            {
                put("name", "room dos jingles");
            }
        });
        Room firstRoom = gotRooms.iterator().next();
        Collection<Player> players = firstRoom.getPlayers();
        for (Player roomPlayer : players) {
            System.out.println(roomPlayer.getUser().getLogin());
        }
    }
}
