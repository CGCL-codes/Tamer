package net.sf.gham.core.entity.match;

import net.sf.gham.Gham;
import net.sf.gham.core.control.ObjectShower;
import net.sf.gham.core.entity.team.TeamInterface;
import net.sf.gham.swing.util.GlassPaneSwingWorker;

/**
 * @author fabio
 *
 */
public class TeamInMatchResult implements TeamInterface {

    private String name;

    private int id;

    private boolean currentTeam = false;

    public TeamInMatchResult(String name, int id, boolean currentTeam) {
        this.name = name;
        this.id = id;
        this.currentTeam = currentTeam;
    }

    /**
     * @return Returns the id.
     */
    public int getId() {
        return id;
    }

    /**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    public String toString() {
        return name;
    }

    public int getTeamId() {
        return id;
    }

    public String getTeamName() {
        return name;
    }

    public void mouseClicked() {
        new GlassPaneSwingWorker<Integer, Integer>() {

            @Override
            protected Integer executeInBackground() throws Exception {
                ObjectShower.singleton().showTeam(id);
                return id;
            }
        }.execute();
    }

    public boolean isClickable() {
        return id != Gham.main.getTeamId();
    }

    public boolean isCurrentTeam() {
        return currentTeam;
    }
}
