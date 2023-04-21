package org.inigma.utopia.calculator;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import junit.framework.TestCase;
import org.inigma.utopia.Army;
import org.inigma.utopia.Military;
import org.inigma.utopia.Province;
import org.inigma.utopia.Race;
import org.inigma.utopia.utils.CalendarUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.runner.RunWith;

@RunWith(JUnit4ClassRunner.class)
public class MilitaryCalculatorTest extends TestCase {

    private MilitaryCalculator calculator;

    @Before
    public void setup() {
        calculator = new MilitaryCalculator();
    }

    @Test
    public void getMatches() {
        ArrayList<Integer> list = new ArrayList<Integer>();
        list.add(4500);
        list.add(5000);
        Collection<Integer> matches = calculator.getMatches(list);
        assertNotNull(matches);
        assertEquals(7, matches.size());
        list.add(5500);
        matches = calculator.getMatches(list);
        assertNotNull(matches);
        assertEquals(6, matches.size());
    }

    @Test
    public void determineDefense() {
        ArrayList<Integer> list = new ArrayList<Integer>();
        list.add(58583);
        list.add(78784);
        Collection<Integer> matches = calculator.getMatches(list);
        assertNotNull(matches);
        assertEquals(1, matches.size());
    }

    @Test
    @Ignore
    public void adjustMilitaryWithCB() {
        Province province = new Province();
        province.setRace(Race.Dwarf);
        province.setSoldiers(1753);
        province.setOffspecs(0);
        province.setDefspecs(3149);
        province.setElites(7687);
        province.setHorses(5254);
        province.setOffense(69045);
        province.setDefense(85110);
        Calendar returnTime = CalendarUtils.getCalendar();
        returnTime.add(Calendar.HOUR_OF_DAY, 3);
        Military military = province.getMilitary();
        military.setOffense(49130);
        military.setDefense(76634);
        Collection<Army> armies = new LinkedList<Army>();
        military.setArmies(armies);
        Army home = new Army();
        home.setSoldiers(844);
        home.setOffspecs(0);
        home.setDefspecs(3023);
        home.setElites(4985);
        home.setHorses(1871);
        armies.add(home);
        Army attack = new Army();
        attack.setSoldiers(1027);
        attack.setOffspecs(0);
        attack.setDefspecs(0);
        attack.setElites(2779);
        attack.setHorses(3338);
        attack.setReturnTime(returnTime);
        armies.add(attack);
        calculator.adjustMilitaryInformation(province);
        assertEquals(41636, military.getOffense());
        assertEquals(61802, military.getDefense());
        Iterator<Army> iterator = military.getArmies().iterator();
        Army army = iterator.next();
        assertEquals(836, army.getSoldiers());
        assertEquals(0, army.getOffspecs());
        assertEquals(3149, army.getDefspecs());
        assertEquals(4936, army.getElites());
        assertEquals(1586, army.getHorses());
        army = iterator.next();
        assertEquals(917, army.getSoldiers());
        assertEquals(0, army.getOffspecs());
        assertEquals(0, army.getDefspecs());
        assertEquals(2751, army.getElites());
        assertEquals(3668, army.getHorses());
    }
}
