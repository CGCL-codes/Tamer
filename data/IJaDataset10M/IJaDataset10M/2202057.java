package org.nomicron.suber.model.test;

import org.nomicron.suber.constants.PreferenceValues;
import org.nomicron.suber.enums.PlayerRights;
import org.nomicron.suber.enums.PlayerStatus;
import org.nomicron.suber.model.bean.PreferenceType;
import org.nomicron.suber.model.factory.MetaFactory;
import org.nomicron.suber.model.factory.PlayerFactory;
import org.nomicron.suber.model.object.Element;
import org.nomicron.suber.model.object.Player;
import com.dreamlizard.miles.text.StringCipher;
import com.dreamlizard.miles.time.Moment;

/**
 * Tests for the Player.
 */
public class PlayerTest extends SuberTest {

    /**
     * Contstruct the test.
     *
     * @param name test name
     */
    public PlayerTest(String name) {
        super(name);
    }

    public void testObject() throws Exception {
        PlayerFactory playerFactory = MetaFactory.getInstance().getPlayerFactory();
        PreferenceValues preferenceValues = (PreferenceValues) getApplicationContext().getBean("preferenceValues");
        String alias = "alias test";
        String email = "test@nomicron.org";
        String firstName = "Jeff";
        String lastName = "Melby";
        String ipAddress = "192.168.0.20";
        Moment lastAccessDate = new Moment();
        String username = "geek";
        String password = "nomic";
        PlayerStatus playerStatus = PlayerStatus.INACTIVE;
        StringCipher stringCypher = (StringCipher) getApplicationContext().getBean("stringCipher");
        String encryptedPassword = stringCypher.encryptToByteArrayString(password);
        PreferenceType preferenceType = MetaFactory.getInstance().getPreferenceTypeFactory().findByBeanName("voteReminderEmailPreferenceType");
        Element element = new Element();
        element.save();
        Player player = new Player();
        player.setAlias(alias);
        player.setEmail(email);
        player.setFirstName(firstName);
        player.setLastName(lastName);
        player.setIpAddress(ipAddress);
        player.setLastAccessDate(lastAccessDate);
        player.setUsername(username);
        player.setPassword(password);
        player.setPlayerStatus(playerStatus);
        player.addElement(element);
        player.addPlayerRights(PlayerRights.VOTER);
        player.setPreferenceValue(preferenceType, preferenceValues.getYesPreferenceValue());
        player.save();
        resetSession();
        assertEquals(alias, player.getAlias());
        assertEquals(email, player.getEmail());
        assertEquals(firstName, player.getFirstName());
        assertEquals(lastName, player.getLastName());
        assertEquals(ipAddress, player.getIpAddress());
        assertEquals(lastAccessDate, player.getLastAccessDate());
        assertEquals(username, player.getUsername());
        assertEquals(encryptedPassword, player.getEncryptedPassword());
        assertEquals(playerStatus, player.getPlayerStatus());
        assertTrue(player.getElementList().contains(element));
        assertTrue(player.hasPlayerRights(PlayerRights.VOTER));
        assertFalse(player.hasPlayerRights(PlayerRights.ADMINISTRATOR));
        assertEquals(preferenceValues.getYesPreferenceValue(), player.getPreferenceValue(preferenceType));
        Player aliasPlayer = playerFactory.getPlayerByAlias(alias);
        assertEquals(player, aliasPlayer);
        assertNull(playerFactory.getPlayerByAlias("nope"));
        Player usernamePlayer = playerFactory.getPlayerByUsername(username);
        assertEquals(player, usernamePlayer);
        assertNull(playerFactory.getPlayerByUsername("nah"));
        Player usernamePlayer2 = playerFactory.getPlayerByUsername("Geek");
        assertEquals(player, usernamePlayer2);
    }

    public void testMatchPlayer() throws Exception {
        Player player1 = new Player();
        player1.setEmail("jmelby@geemail.om");
        player1.save();
        Player player2 = new Player();
        player2.setEmail("day@texisbone.om");
        player2.save();
        Player player3 = new Player();
        player3.setEmail("wurm.ouroboros@meep.om");
        player3.save();
        Player player4 = new Player();
        player4.setEmail(null);
        player4.save();
        resetSession();
        PlayerFactory playerFactory = MetaFactory.getInstance().getPlayerFactory();
        assertEquals(player1, playerFactory.matchPlayer("Jef jme...@geemail.om"));
        assertEquals(player1, playerFactory.matchPlayer("Jeff Melby jme...@geemail.om"));
        assertEquals(player2, playerFactory.matchPlayer("d...@texisbone.om"));
        assertEquals(player2, playerFactory.matchPlayer("Ben Hamill d...@texisbone.om"));
        assertEquals(player3, playerFactory.matchPlayer("wurm.ourobo...@meep.om"));
        assertEquals(player3, playerFactory.matchPlayer("The Wurm Ouroboros wurm.ourobo...@meep.om"));
        assertNull(playerFactory.matchPlayer("a...@nomicron.org"));
    }
}
