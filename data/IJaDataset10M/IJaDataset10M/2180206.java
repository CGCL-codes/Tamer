package org.mahjong.matoso.servlet.ranking;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.mahjong.matoso.bean.Player;
import org.mahjong.matoso.bean.Tournament;
import org.mahjong.matoso.constant.ApplicationCst;
import org.mahjong.matoso.constant.RequestCst;
import org.mahjong.matoso.constant.ServletCst;
import org.mahjong.matoso.constant.SessionCst;
import org.mahjong.matoso.service.RankingService;
import org.mahjong.matoso.service.RoundService;
import org.mahjong.matoso.servlet.MatosoServlet;
import org.mahjong.matoso.util.NumberUtils;
import org.mahjong.matoso.util.exception.FatalException;

/**
 * Display an dynamic view of the ranking by shifting the page every N seconds.
 * 
 * @author ctrung
 * @date 7 mars 2010
 */
public class DynamicViewRanking extends MatosoServlet {

    @Override
    public String serve(HttpServletRequest request, HttpServletResponse response) throws FatalException {
        HttpSession session = request.getSession();
        Tournament tournament = super.getTournament(request);
        session.setAttribute(SessionCst.ATTR_TOURNAMENT, tournament);
        List<Player> orderedPlayers = (List<Player>) session.getAttribute(SessionCst.ATTR_RANKING);
        if (orderedPlayers == null) {
            orderedPlayers = RankingService.getByTournament(tournament);
            session.setAttribute(SessionCst.ATTR_RANKING, orderedPlayers);
        }
        session.setAttribute("rankingTeam", RankingService.getTeamsForTournament(tournament));
        session.setAttribute(SessionCst.ATTR_LAST_PLAYED_SESSION, RoundService.getLastPlayedSession(tournament));
        Integer newValue = NumberUtils.getInteger(request.getParameter(RequestCst.PARAM_NB_ELEMENTS_PER_PAGE));
        Integer inSessionValue = (Integer) session.getAttribute(SessionCst.SES_ATTR_NB_PLAYERS_BY_PAGE);
        if (newValue != null) session.setAttribute(SessionCst.SES_ATTR_NB_PLAYERS_BY_PAGE, newValue); else if (inSessionValue == null) session.setAttribute(SessionCst.SES_ATTR_NB_PLAYERS_BY_PAGE, ApplicationCst.NB_ELEMENTS_BY_PAGE_DEFAULT);
        return ServletCst.REDIRECT_TO_DYNAMIC_VIEW_RANKING;
    }
}
