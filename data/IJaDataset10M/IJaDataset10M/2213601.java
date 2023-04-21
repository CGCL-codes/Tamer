package org.ladybug.gui.toolbox.tableindexers;

import org.ladybug.core.users.Team;

/**
 * @author Aurelian Pop
 */
public interface TeamTableIndexer extends TableIndexer {

    /**
     * Provides the team that is currently selected.
     * 
     * @return The active team.
     */
    Team getActiveTeam();

    /**
     * Specifies the index of the team that is the active (user selected) one.
     * 
     * @param index
     *            the index in the table
     */
    void setActiveTeamIndex(int index);
}
