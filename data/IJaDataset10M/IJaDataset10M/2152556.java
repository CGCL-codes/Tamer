package glaceo.web.controllers;

import glaceo.conf.GConstants;
import glaceo.data.GClub;
import glaceo.data.helper.GMatchType;
import glaceo.error.GDataValidationException;
import glaceo.error.GException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller for club related pages.
 *
 * @version $Id$
 * @author jjanke
 */
@Controller
public class GClubController extends GController {

    @RequestMapping(value = GConstants.URL_CLUB_OVERVIEW, method = RequestMethod.GET)
    public ModelAndView handleClubOverview(HttpServletRequest request, HttpServletResponse response, @RequestParam(GConstants.REQUEST_CLUB) String strClubId) throws GException {
        addGeneralClubInfoToRequest(request, strClubId);
        selectActiveContextMenuItem(GConstants.URL_CLUB_OVERVIEW);
        return new ModelAndView("clubHome.page");
    }

    @RequestMapping(value = GConstants.URL_CLUB_MATCHES, method = RequestMethod.GET)
    public ModelAndView handleClubLeagueMatches(HttpServletRequest request, HttpServletResponse response, @RequestParam(GConstants.REQUEST_CLUB) String strClubId) throws GException {
        GMatchType matchType = GMatchType.ALL;
        GClub club = addGeneralClubInfoToRequest(request, strClubId);
        selectActiveContextMenuItem(GConstants.URL_CLUB_MATCHES);
        request.setAttribute("URL_SELF", GConstants.URL_CLUB_MATCHES);
        if (request.getParameter(GConstants.REQUEST_MATCH_TYPE) != null) matchType = getDataValidator().validateMatchType(GConstants.REQUEST_MATCH_TYPE, request.getParameter(GConstants.REQUEST_MATCH_TYPE));
        request.setAttribute("v_matchType", matchType);
        request.setAttribute("v_leagueMatches", getDao().findMatches(club, matchType, club.getLeague()));
        return new ModelAndView("clubMatches.page");
    }

    /**
   * Adds general arguments to the request (like club object and main menu).
   *
   * @param request the request to which the arguments shall be added
   * @param strClubId the ID of the requested club
   * @return the club object matching the given id
   * @throws GDataValidationException if the club could not be validated successfully
   */
    private GClub addGeneralClubInfoToRequest(HttpServletRequest request, String strClubId) throws GDataValidationException {
        GClub club = getDao().findEntity(GClub.class, strClubId);
        if (club == null) throw new GDataValidationException(GConstants.REQUEST_CLUB, strClubId, "The provided club ID is invalid.");
        setCommonRequestAttributes(request);
        request.setAttribute("v_club", club);
        return club;
    }
}
