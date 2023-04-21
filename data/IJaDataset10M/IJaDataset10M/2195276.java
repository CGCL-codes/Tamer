package com.predator.soldatweb.extras;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import com.predator.soldatweb.domain.PlayerStatistics;
import com.predator.soldatweb.visitors.KillsVisitor;
import com.soldat.parser.Player;
import com.soldat.parser.SoldatVisitor;

/**
 * Prepares the Mano a Mano results
 * 
 * @author Falc�, Rodrigo Ignacio (latest modification by $Author: roman_garcia $).
 * @version $Revision: 1.1 $ $Date: 2005/05/09 14:25:42 $
 */
public class ManoAManoExtra extends ExtraBase {

    /**
     * @param extraName
     */
    public ManoAManoExtra() {
        super("showManoAMano", "Mano a Mano Ranking");
    }

    public void processExtra(HttpServletRequest request, SoldatVisitor soldatVisitor) {
        List result = new ArrayList();
        KillsVisitor visitor = (KillsVisitor) soldatVisitor;
        for (Iterator it = visitor.getPlayers().iterator(); it.hasNext(); ) {
            Player p1 = (Player) it.next();
            List row = new ArrayList();
            row.add(p1.getName());
            for (Iterator it2 = visitor.getPlayers().iterator(); it2.hasNext(); ) {
                Player p2 = (Player) it2.next();
                if (!p2.getName().equals(p1.getName())) {
                    PlayerStatistics ps1 = visitor.getPlayerStatistic(p1);
                    PlayerStatistics ps2 = visitor.getPlayerStatistic(p2);
                    int p2KillP1 = ps1.getDeathsPerKiller(p2).size();
                    int p1KillP2 = ps2.getDeathsPerKiller(p1).size();
                    row.add(new Integer(p1KillP2 - p2KillP1));
                } else {
                    row.add("X");
                }
            }
            result.add(row);
        }
        request.setAttribute("manoAManoRanking", result);
    }
}
