package net.virtualhockey.vhtags.leaguestandings;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspFragment;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import vh.data.contest.VHContestPhase;
import vh.data.contest.VHLeague;
import vh.data.season.VHSeason;
import vh.data.standings.VHStandings;
import vh.error.VHException;
import net.virtualhockey.infrastructure.HibernateUtil;
import net.virtualhockey.utils.TemplateManager;
import net.virtualhockey.vhtags.VHGenericSimpleTag;

/**
 * Handles the VH Tag 'leagueStandings' and uses the provided templates to construct a
 * table of the standings for the given league/subtype/matchday combination.
 * 
 * @version $Id: LeagueStandingsTagHandler.java 61 2006-12-10 18:39:34Z janjanke $
 * @author jankejan
 */
public class LeagueStandingsTagHandler extends VHGenericSimpleTag {

    private static Log s_log = LogFactory.getLog(LeagueStandingsTagHandler.class);

    private static final String TEMPLATE_ATTR_STANDINGS_ROWS = "standingsRows";

    private static final String TEMPLATE_ATTR_LEAGUE_NAME = "leagueName";

    private static final String TEMPLATE_ATTR_SEASON_NAME = "seasonName";

    private static final String TEMPLATE_ATTR_LEAGUE_SUBTYPE = "leagueSubType";

    private static final String TEMPLATE_TAG_RANK = "rank";

    private static final String TEMPLATE_TAG_CLUBNAME = "club";

    private static final String TEMPLATE_TAG_POINTS = "points";

    private static final String TEMPLATE_TAG_MATCHES = "numMatches";

    private static final String TEMPLATE_TAG_WINS = "wins";

    private static final String TEMPLATE_TAG_WINS_SHOOTOUT = "winsShootout";

    private static final String TEMPLATE_TAG_LOSSES_SHOOTOUT = "lossesShootout";

    private static final String TEMPLATE_TAG_LOSSES = "losses";

    private static final String TEMPLATE_TAG_GOALS_FOR = "goalsFor";

    private static final String TEMPLATE_TAG_GOALS_AGAINST = "goalsAgainst";

    private static final String TEMPLATE_TAG_GOALS_DIFF = "goalsDiff";

    private static final String TEMPLATE_TAG_REMAINING_GOALS = "remGoals";

    /** Fragment attribute 'mainPattern' */
    private JspFragment d_jspMainPattern;

    /** Pattern for play-off ranked standings rows. */
    private TemplateManager d_templateRowPattern1;

    /** Pattern for standings rows concerning neither play-off, nor play-out ranks. */
    private TemplateManager d_templateRowPattern2;

    /** Pattern for play-out ranked standings rows. */
    private TemplateManager d_templateRowPattern3;

    /** Tag attribute 'league' - code of the league whose reg. season matches are displayed */
    private String d_leagueCode;

    /** The number of the matchday that shall be shown. */
    private int d_numMatchday;

    /** The competition sub type for which the standings shall be shown. */
    private VHContestPhase d_compSubType;

    /** Indicates the criteria to be used to order the standings rows. */
    private VHStandings.SortCriteria d_orderBy;

    /**
   * Sets the tag attribut 'league'.
   */
    public void setLeague(String strLeagueCode) {
        d_leagueCode = strLeagueCode;
    }

    /**
   * Sets the attribute 'matchdayNum'.
   */
    public void setMatchdayNum(Integer num) {
        d_numMatchday = num.intValue();
    }

    /**
   * Sets the tag attribute 'subtype'.
   */
    public void setSubtype(String strSubtype) {
        d_compSubType = VHContestPhase.valueOf(strSubtype);
    }

    /**
   * Sets the tag attribute 'orderBy'.
   */
    public void setOrderBy(String strOrderBy) {
        d_orderBy = VHStandings.SortCriteria.valueOf(strOrderBy);
    }

    /**
   * Sets the tag attribute 'mainPattern'.
   */
    public void setMainPattern(JspFragment jspMainPattern) {
        d_jspMainPattern = jspMainPattern;
    }

    /**
   * Sets the tag attribute 'rowPattern1'.
   */
    public void setRowPattern1(String strRowPattern) {
        d_templateRowPattern1 = new TemplateManager(strRowPattern);
    }

    /**
   * Sets the tag attribute 'rowPattern2'.
   */
    public void setRowPattern2(String strRowPattern) {
        d_templateRowPattern2 = new TemplateManager(strRowPattern);
    }

    /**
   * Sets the tag attribute 'rowPattern3'.
   */
    public void setRowPattern3(String strRowPattern) {
        d_templateRowPattern3 = new TemplateManager(strRowPattern);
    }

    @Override
    public void doTag() throws JspException, IOException {
        VHLeague league = null;
        VHSeason season = null;
        try {
            league = (VHLeague) HibernateUtil.getSession().load(VHLeague.class, d_leagueCode);
        } catch (HibernateException ex) {
            s_log.error("A league with the code '" + d_leagueCode + "' does not exist in the database.", ex);
            doErrorOutput();
            return;
        }
        try {
            season = VHSeason.getActiveSeason();
        } catch (VHException ex) {
            s_log.error(ex.getMessage(), ex);
            doErrorOutput();
            return;
        }
        getPageContext().setAttribute(TEMPLATE_ATTR_LEAGUE_NAME, league.getName(getVHSession().getLocale()));
        getPageContext().setAttribute(TEMPLATE_ATTR_SEASON_NAME, season.getName());
        getPageContext().setAttribute(TEMPLATE_ATTR_LEAGUE_SUBTYPE, d_compSubType.getLocalizedName(getVHSession().getLocale()));
        List<VHStandings> standings = getStandings(league, season);
        if (standings == null) getPageContext().setAttribute(TEMPLATE_ATTR_STANDINGS_ROWS, ""); else getPageContext().setAttribute(TEMPLATE_ATTR_STANDINGS_ROWS, createRows(standings, league));
        d_jspMainPattern.invoke(getPageContext().getOut());
    }

    /**
   * Retrieves the right LeagueRankFollowUp objects, orders them according to the order by
   * criteria and returns them. If a matchday is passed that has not yet been played, then
   * the last available standings are returned.
   * 
   * @param league the league for which the standings are retrieved
   * @param season the season for which the standings are retrieved
   * @return an ordered list of standings or <code>null</code> if no LeagueRankFollowUp
   *         objects exist for the given VHLeague/Subtype/VHSeason combination
   */
    @SuppressWarnings("unchecked")
    private List<VHStandings> getStandings(VHLeague league, VHSeason season) {
        List results = null;
        Query query = HibernateUtil.getSession().createQuery("SELECT MAX( l.d_round ) FROM net.virtualhockey.data.LeagueRankFollowUp AS l WHERE l.d_competition = :leagueCode " + "AND l.d_compSubType = :compSubType AND l.d_season.d_id = :seasonId");
        query.setString("leagueCode", league.getId());
        query.setString("compSubType", d_compSubType.name());
        query.setInteger("seasonId", season.getId());
        Integer maxRound = (Integer) query.uniqueResult();
        if (maxRound == null) {
            s_log.error("For the league " + league.getId() + " / Subtype " + d_compSubType.name() + " / VHSeason " + season.getName() + " no LeagueRankFollowUp objects have been initialized. No standings are created.");
            return null;
        }
        if (d_numMatchday > maxRound.intValue()) d_numMatchday = maxRound.intValue();
        Criteria criteria = HibernateUtil.getSession().createCriteria(VHStandings.class);
        criteria.add(Expression.eq("d_round", new Integer(d_numMatchday)));
        criteria.add(Expression.eq("d_competition", league));
        criteria.add(Expression.eq("d_season", season));
        criteria.add(Expression.eq("d_compSubType", d_compSubType));
        criteria.addOrder(Order.asc("d_rank"));
        try {
            results = criteria.list();
        } catch (HibernateException ex) {
            s_log.error("Impossible to retrieve LeagueRankFollowUp objects for the constructed standings.", ex);
            return null;
        }
        VHStandings.setSortCriteria(d_orderBy);
        Collections.sort((List<VHStandings>) results);
        VHStandings.setSortCriteria(null);
        return results;
    }

    /**
   * Creates the standings rows.
   * 
   * @param standings an ordered list of standings to be displayed
   * @param the league for which the standings are created
   * @return the string containing the HTML code for all the standings rows
   */
    private String createRows(List<VHStandings> standings, VHLeague league) {
        StringBuilder sb = new StringBuilder("");
        TemplateManager pattern;
        int middle = 0, bottom = 0;
        if (d_compSubType == VHContestPhase.REGULAR_SEASON) {
            middle = league.getNumFinalRound() > 0 ? league.getNumFinalRound() : league.getNumPromoted();
            bottom = league.getNumTeams() - (league.getNumRelegationRound() > 0 ? league.getNumRelegationRound() : league.getNumRelegated());
            if (middle >= bottom) middle = -1;
        } else if (d_compSubType == VHContestPhase.FINAL_ROUND) {
            middle = league.getNumPromoted();
            bottom = -1;
        } else if (d_compSubType == VHContestPhase.RELEGATION_ROUND) {
            middle = -1;
            bottom = league.getNumRelegationRound() - league.getNumRelegated();
        }
        for (int i = 0; i < standings.size(); i++) {
            VHStandings standing = standings.get(i);
            if (bottom != -1 && i >= bottom) pattern = d_templateRowPattern3; else if (middle != -1 && i >= middle) pattern = d_templateRowPattern2; else pattern = d_templateRowPattern1;
            pattern.replace(TEMPLATE_TAG_RANK, String.valueOf(i + 1));
            pattern.replace(TEMPLATE_TAG_CLUBNAME, standing.getClub().getName());
            pattern.replace(TEMPLATE_TAG_POINTS, String.valueOf(standing.getPoints()));
            pattern.replace(TEMPLATE_TAG_MATCHES, String.valueOf(standing.getNumMatches()));
            pattern.replace(TEMPLATE_TAG_WINS, String.valueOf(standing.getWins()));
            pattern.replace(TEMPLATE_TAG_WINS_SHOOTOUT, String.valueOf(standing.getWinsShootout()));
            pattern.replace(TEMPLATE_TAG_LOSSES, String.valueOf(standing.getLosses()));
            pattern.replace(TEMPLATE_TAG_LOSSES_SHOOTOUT, String.valueOf(standing.getLossesShootout()));
            pattern.replace(TEMPLATE_TAG_GOALS_FOR, String.valueOf(standing.getGoalsScored()));
            pattern.replace(TEMPLATE_TAG_GOALS_AGAINST, String.valueOf(standing.getGoalsAgainst()));
            pattern.replace(TEMPLATE_TAG_GOALS_DIFF, String.valueOf(standing.getGoalsDiff()));
            pattern.replace(TEMPLATE_TAG_REMAINING_GOALS, standing.getRemainingGoals().toString());
            sb.append(pattern.toString());
            pattern.reset();
        }
        return sb.toString();
    }
}
