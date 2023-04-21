package org.mahjong.matoso.servlet.round;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.mahjong.matoso.bean.Round;
import org.mahjong.matoso.bean.Tournament;
import org.mahjong.matoso.constant.RequestCst;
import org.mahjong.matoso.constant.ServletCst;
import org.mahjong.matoso.service.RoundService;
import org.mahjong.matoso.servlet.MatosoServlet;
import org.mahjong.matoso.util.exception.FatalException;

public class FinalSessionView extends MatosoServlet {

    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger.getLogger(FinalSessionView.class);

    @Override
    public String serve(HttpServletRequest request, HttpServletResponse response) throws FatalException {
        if (LOGGER.isDebugEnabled()) LOGGER.debug("start serve");
        Tournament tournament = getTournament(request);
        if (LOGGER.isDebugEnabled()) LOGGER.debug("serve:tournament=" + tournament);
        if (tournament != null) {
            List<Round> rounds = RoundService.getRounds(tournament);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("serve:rounds=" + rounds);
                if (rounds != null) LOGGER.debug("serve:rounds.size=" + rounds.size());
            }
            if (rounds != null && !rounds.isEmpty()) {
                Round lastround = rounds.get(rounds.size() - 1);
                request.setAttribute(RequestCst.REQ_ATTR_FINAL_SESSION, lastround);
            }
        }
        if (LOGGER.isDebugEnabled()) LOGGER.debug("end serve");
        return ServletCst.REDIRECT_TO_TOURNAMENT_FINAL_SESSION_VIEW;
    }
}
