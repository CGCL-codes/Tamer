package net.sourceforge.huntforgold.logic;

import net.sourceforge.huntforgold.gui.PersonalInfoGUI;
import net.sourceforge.huntforgold.model.Nationality;
import net.sourceforge.huntforgold.model.player.Titles;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * The personal information state
 */
public class PersonalInfoState extends GameState {

    /** The logger */
    private static Logger log = Logger.getLogger(PersonalInfoState.class);

    /**
   * Creates a new PersonalInfoState
   */
    public PersonalInfoState() {
        super(PERSONAL_INFO);
        setRenderer(new PersonalInfoGUI(this));
    }

    /**
   * Called when this state is entered.
   */
    public void enter() {
        keyboard.setStickyMode(true);
        List data = new ArrayList();
        List age = new ArrayList();
        age.add("Age");
        age.add("" + player.getAge());
        data.add(age);
        List pg = new ArrayList();
        pg.add("Gold");
        pg.add("" + player.getPersonalGold());
        data.add(pg);
        List pa = new ArrayList();
        pa.add("Acres");
        pa.add("" + player.getPersonalAcres());
        data.add(pa);
        List married = new ArrayList();
        married.add("Married");
        married.add(player.getMissions().isMarriage() ? "Yes" : "No");
        data.add(married);
        Nationality nat = Nationality.getNationality();
        for (int i = 0; i < nat.getNumberOfNationalities(); i++) {
            List title = new ArrayList();
            Titles t = player.getTitle(i);
            title.add(nat.getName(i) + " title");
            title.add(t.getName());
            data.add(title);
        }
        List missions = new ArrayList();
        missions.add("Missions");
        missions.add("" + player.getMissions().getNumberCompleted());
        data.add(missions);
        List ships = new ArrayList();
        ships.add("Ships");
        ships.add("" + player.getFleet().getNumberOfShips());
        data.add(ships);
        List crew = new ArrayList();
        crew.add("Crew");
        crew.add("" + player.getCrewNumber());
        data.add(crew);
        List morale = new ArrayList();
        morale.add("Morale");
        morale.add("" + player.getCrewMorale().getName());
        data.add(morale);
        List cannons = new ArrayList();
        cannons.add("Cannons");
        cannons.add("" + player.getCannons());
        data.add(cannons);
        int[] columns = new int[] { 50, 50 };
        ((PersonalInfoGUI) getRenderer()).setData(data, columns);
    }

    /**
   * Goes to the next state
   * @param state The next state
   */
    public void nextState(int state) {
        if (state == 0) {
            game.setCurrentState(CAPTAINS_CABIN);
        } else {
            log.fatal("Unknown option: " + state);
            System.exit(1);
        }
    }

    /**
   * Called whenever the game leaves this state.
   */
    public void leave() {
        keyboard.setStickyMode(false);
    }
}
