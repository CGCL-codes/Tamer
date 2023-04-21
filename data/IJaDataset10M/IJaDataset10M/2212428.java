package erepublik.dto;

import static org.junit.Assert.*;
import org.junit.*;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import org.xml.sax.InputSource;
import erepublik.impl.ERepublicServiceImpl;

public class BattleTest {

    SimpleDateFormat dateFormat;

    ERepublicServiceImpl service;

    @Before
    public void beforeClass() throws Exception {
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        this.service = (ERepublicServiceImpl) ERepublicServiceImpl.getInstance();
    }

    @Test
    public void testXMLFile() throws Exception {
        InputStream in = new FileInputStream("test/resources/Battle.xml");
        Battle battle = this.service.parse(Battle.class, new InputSource(in));
        checkBattle(battle);
    }

    @Test
    @Ignore("Test on live data")
    public void testServiceCall() throws Exception {
        Battle battle = this.service.getBattle(1);
        checkBattle(battle);
    }

    public void checkBattle(Battle battle) throws Exception {
        assertEquals(new Integer(2236), battle.getCombatantCount());
        assertEquals("2010-07-20T08:06:08", dateFormat.format(battle.getFinishedAt().getTime()));
        assertEquals("HAS_WINNER", battle.getFinishedReason());
        assertEquals("HU", battle.getAttacker().getCode());
        assertEquals(new Integer(1901), battle.getAttacker().getUnitsKilled());
        assertEquals(new Float(127590.08), battle.getAttacker().getDamageDealt());
        assertEquals("Hungary", battle.getAttacker().getName());
        assertEquals(new Integer(13), battle.getAttacker().getId());
        assertEquals("ES", battle.getDefender().getCode());
        assertEquals(new Integer(923), battle.getDefender().getUnitsKilled());
        assertEquals(new Float(69495.15), battle.getDefender().getDamageDealt());
        assertEquals("Spain", battle.getDefender().getName());
        assertEquals(new Integer(15), battle.getDefender().getId());
        assertEquals(new Integer(2543), battle.getWar().getId());
        assertEquals(new Integer(1), battle.getId());
        assertEquals("Piedmont", battle.getRegion().getName());
        assertEquals(new Integer(272), battle.getRegion().getId());
        assertEquals(false, battle.getResistance());
        assertEquals(new Integer(13), battle.getWinnerCountryId());
    }
}
