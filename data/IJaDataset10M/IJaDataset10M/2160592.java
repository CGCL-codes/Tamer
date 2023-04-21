package ho.module.teamAnalyzer.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO Missing Class Documentation
 *
 * @author TODO Author Name
 */
public class RosterPlayerData {

    private String name;

    private RosterRoleData[] app = new RosterRoleData[25];

    private int id;

    /**
     * Creates a new PlayerData object.
     */
    public RosterPlayerData() {
        for (int i = 0; i < 25; i++) {
            app[i] = new RosterRoleData(i);
        }
    }

    /**
     * TODO Missing Method Documentation
     *
     * @param i TODO Missing Method Parameter Documentation
     */
    public void setId(int i) {
        id = i;
    }

    /**
     * TODO Missing Method Documentation
     *
     * @return TODO Missing Return Method Documentation
     */
    public int getId() {
        return id;
    }

    /**
     * TODO Missing Method Documentation
     *
     * @return TODO Missing Return Method Documentation
     */
    public int getMainPosition() {
        int pos = -1;
        int val = -1;
        for (int i = 0; i < 25; i++) {
            if (app[i].getApp() > val) {
                pos = i;
                val = app[i].getApp();
            }
        }
        return pos;
    }

    /**
     * TODO Missing Method Documentation
     *
     * @return TODO Missing Return Method Documentation
     */
    public RosterRoleData getMainRole() {
        return app[getMainPosition()];
    }

    /**
     * TODO Missing Method Documentation
     *
     * @param i TODO Missing Method Parameter Documentation
     */
    public void setName(String i) {
        name = i;
    }

    /**
     * TODO Missing Method Documentation
     *
     * @return TODO Missing Return Method Documentation
     */
    public String getName() {
        return name;
    }

    /**
     * TODO Missing Method Documentation
     *
     * @return TODO Missing Return Method Documentation
     */
    public List<RosterRoleData> getSecondaryRoles() {
        int main = getMainPosition();
        List<RosterRoleData> l = new ArrayList<RosterRoleData>();
        for (int i = 0; i < app.length; i++) {
            RosterRoleData array_element = app[i];
            if ((array_element.getApp() > 0) && (array_element.getPos() != main)) {
                l.add(array_element);
            }
        }
        return l;
    }

    /**
     * TODO Missing Method Documentation
     *
     * @param spot TODO Missing Method Parameter Documentation
     */
    public void addMatch(SpotLineup spot) {
        if (spot.getPosition() > -1) {
            app[spot.getPosition()].addMatch(spot.getRating());
        }
    }
}
