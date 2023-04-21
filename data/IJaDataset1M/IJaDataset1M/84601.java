package org.mahjong.matoso.servlet.tournament;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.mahjong.matoso.bean.Player;
import org.mahjong.matoso.bean.Tournament;
import org.mahjong.matoso.constant.ServletCst;
import org.mahjong.matoso.service.DrawService;
import org.mahjong.matoso.service.PlayerService;
import org.mahjong.matoso.servlet.MatosoServlet;
import org.mahjong.matoso.util.exception.FatalException;

/**
 * View all the players draw.
 * 
 * @author ctrung
 * @date 03 Dec. 2009
 */
public class ViewTournamentDraw extends MatosoServlet {

    private static final long serialVersionUID = 1L;

    public String serve(HttpServletRequest request, HttpServletResponse response) throws FatalException {
        Tournament tournament = super.getTournament(request);
        List<Player> ps = PlayerService.getListFromTournament(tournament);
        request.setAttribute("tournament", tournament);
        request.setAttribute("players", ps);
        DrawService.initDraw(ps);
        return ServletCst.REDIRECT_TO_TOURNAMENT_DRAW;
    }
}
