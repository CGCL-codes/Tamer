package org.icehockeymanager.ihm.clients.devgui.ihm.league.std;

import org.icehockeymanager.ihm.clients.devgui.controller.*;
import org.icehockeymanager.ihm.game.league.helper.*;
import org.icehockeymanager.ihm.game.league.std.*;

public class StdLeagueTools {

    /**
   * Returns the title of this Playoffs
   * 
   * @param level
   *          The level of a global playoffs (0 = final, 1 = semiFinal etc.)
   * @return The title value
   */
    private static String getPlayoffLevelTitleKey(int level) {
        if (level == 0) {
            return "stdleague.playOffFinal";
        }
        if (level == 1) {
            return "stdleague.playOffSemiFinal";
        }
        if (level == 2) {
            return "stdleague.playOffQuarterFinal";
        }
        return "stdleague." + String.valueOf(level);
    }

    /**
   * Returns complete description of this element (owner, league, name)
   * @param leagueElement 
   * 
   * @return The description value
   */
    public static String getLeagueElementDescription(LeagueElement leagueElement) {
        if (leagueElement instanceof StdRegularSeason) {
            String owner = leagueElement.getLeague().getLeagueOwner().getName();
            String league = leagueElement.getLeague().getName();
            String name = ClientController.getInstance().getTranslation(leagueElement.getNameKey());
            if (leagueElement.getLeagueElementGroup().getLeagueElementCount() > 1) {
                String leagueElementGroup = ClientController.getInstance().getTranslation(leagueElement.getLeagueElementGroup().getNameKey());
                return owner + " : " + league + " : " + leagueElementGroup + " : " + name;
            } else {
                return owner + " : " + league + " : " + name;
            }
        }
        if (leagueElement instanceof StdPlayoffs) {
            String owner = leagueElement.getLeague().getLeagueOwner().getName();
            String league = leagueElement.getLeague().getName();
            if (leagueElement.getLeagueElementGroup().getLeagueElementCount() > 1) {
                String name = ClientController.getInstance().getTranslation(getPlayoffLevelTitleKey(leagueElement.getLevel()));
                String leagueElementGroup = ClientController.getInstance().getTranslation(leagueElement.getNameKey());
                return owner + " : " + league + " : " + leagueElementGroup + " : " + name;
            } else {
                String name = ClientController.getInstance().getTranslation(leagueElement.getNameKey());
                name += ClientController.getInstance().getTranslation(getPlayoffLevelTitleKey(leagueElement.getLevel()));
                return owner + " : " + league + " : " + name;
            }
        }
        return "unknown std leagueelement";
    }

    /**
   * Returns complete description of this element (owner, league, name)
   * @param leagueElement 
   * 
   * @return The description value
   */
    public static String getLeagueElementShortDescription(LeagueElement leagueElement) {
        if (leagueElement instanceof StdRegularSeason) {
            String name = ClientController.getInstance().getTranslation(leagueElement.getNameKey());
            return name;
        }
        if (leagueElement instanceof StdPlayoffs) {
            String name = ClientController.getInstance().getTranslation(leagueElement.getNameKey());
            name += ClientController.getInstance().getTranslation(getPlayoffLevelTitleKey(leagueElement.getLevel()));
            return name;
        }
        return "unknown std leagueelement";
    }
}
