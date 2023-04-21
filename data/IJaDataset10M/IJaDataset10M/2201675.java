package blms.tests.facade;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import junit.framework.TestCase;
import blms.exceptions.DBException;
import blms.exceptions.InvalidParameterException;
import blms.facade.Blms;
import blms.tests.TestConstants;
import blms.util.Configuration;
import blms.util.LeagueUtil;
import blms.util.UserLeagueUtil;
import blms.util.Util;

/**
 * This class has tests for the facade used in our system to communicate with
 * the web pages
 *
 */
public class BlmsTest extends TestCase {

    private Blms blms;

    private SimpleDateFormat format;

    public void setUp() {
        try {
            blms = Blms.getInstance();
            blms.removeAllLeagues();
            blms.removeAllMatches();
            blms.removeAllUsers();
            format = new SimpleDateFormat("dd/MM/yyyy");
            blms.dateFormat(LeagueUtil.DATE_FORMAT);
            blms.saveConfiguration();
        } catch (DBException e) {
            fail();
        } catch (InvalidParameterException e) {
            fail();
        }
    }

    public void tearDown() {
        try {
            blms = Blms.getInstance();
            blms.removeAllLeagues();
            blms.removeAllMatches();
            blms.removeAllUsers();
        } catch (DBException e) {
            fail();
        }
    }

    /**
     * This method verifies if a certain match is being added correctly
     * considering only the match basic information
     */
    public void testAddMatch() {
        try {
            blms.useDatabase(TestConstants.data1Path);
            blms.removeAllUsers();
            blms.removeAllLeagues();
            blms.removeAllMatches();
            Long idWinner = blms.createUser("test1", "test1", "", "555", "", "test1@g", "");
            Long idLoser = blms.createUser("test2", "test2", "", "888", "", "test2@g", "");
            Long leagueId = blms.createLeague("LEAGUE1", "1");
            blms.joinLeague(idLoser + "", leagueId + "", "200");
            GregorianCalendar c = new GregorianCalendar();
            Date date = c.getTime();
            assertEquals(blms.getNumberOfMatches(leagueId + ""), new Long(0));
            assertEquals(blms.getNumberOfMatches(idWinner + "", leagueId + ""), new Long(0));
            assertEquals(blms.getNumberOfMatches(idLoser + "", leagueId + ""), new Long(0));
            Long matchId = blms.addMatchResult(leagueId + "", format.format(date), idWinner + "", idLoser + "");
            Long matchIdReturned = blms.getMatch(leagueId + "", "1");
            assertEquals(matchId, matchIdReturned);
            assertEquals(blms.getNumberOfMatches(leagueId + ""), new Long(1));
            assertEquals(blms.getNumberOfMatches(idWinner + "", leagueId + ""), new Long(1));
            assertEquals(blms.getNumberOfMatches(idLoser + "", leagueId + ""), new Long(1));
            assertEquals(format.format(date), blms.getMatchDate(matchIdReturned + ""));
            assertEquals(idWinner, blms.getMatchWinner(matchIdReturned + ""));
            assertEquals(idLoser, blms.getMatchLoser(matchIdReturned + ""));
            try {
                blms.addMatchResult(10 + "", format.format(date), idWinner + "", idLoser + "");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.addMatchResult("", format.format(date), idWinner + "", idLoser + "");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.addMatchResult(null, format.format(date), idWinner + "", idLoser + "");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.addMatchResult("20", format.format(date), idWinner + "", idLoser + "");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.addMatchResult(leagueId + "", format.format(date), "", idLoser + "");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.addMatchResult(leagueId + "", format.format(date), null, idLoser + "");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.addMatchResult(leagueId + "", format.format(date), "aaa", idLoser + "");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.addMatchResult(leagueId + "", format.format(date), "20", idLoser + "");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.addMatchResult(leagueId + "", format.format(date), idWinner + "", "");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.addMatchResult(leagueId + "", format.format(date), "", "aaaa");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.addMatchResult(leagueId + "", format.format(date), "", null);
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.addMatchResult(leagueId + "", format.format(date), "", "30");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.addMatchResult(leagueId + "", format.format(date), idLoser + "", idLoser + "");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.addMatchResult(leagueId + "", "19-20-2007", "", idLoser + "");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.addMatchResult(leagueId + "", "11/29/2007", "", idLoser + "");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.addMatchResult(leagueId + "", "", "", idLoser + "");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.addMatchResult(leagueId + "", null, "", idLoser + "");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
        } catch (DBException e) {
            fail();
        } catch (InvalidParameterException e) {
            fail();
        }
    }

    /**
     * This method verifies if a certain match is being added correctly
     * considering the whole match information
     */
    public void testAddMatch2() {
        try {
            blms.useDatabase(TestConstants.data1Path);
            blms.removeAllUsers();
            blms.removeAllLeagues();
            blms.removeAllMatches();
            Long idWinner = blms.createUser("test1", "test1", "", "555", "", "test1@g", "");
            Long idLoser = blms.createUser("test2", "test2", "", "888", "", "test2@g", "");
            Long leagueId = blms.createLeague("LEAGUE1", "1");
            blms.joinLeague(idLoser + "", leagueId + "", "200");
            GregorianCalendar c = new GregorianCalendar();
            Date date = c.getTime();
            assertEquals(blms.getNumberOfMatches(leagueId + ""), new Long(0));
            assertEquals(blms.getNumberOfMatches(idWinner + "", leagueId + ""), new Long(0));
            assertEquals(blms.getNumberOfMatches(idLoser + "", leagueId + ""), new Long(0));
            Long matchId = blms.addMatchResult(leagueId + "", format.format(date), idWinner + "", idLoser + "", "5", "2", "3", "1");
            Long matchIdReturned = blms.getMatch(leagueId + "", "1");
            assertEquals(matchId, matchIdReturned);
            assertEquals(blms.getNumberOfMatches(leagueId + ""), new Long(1));
            assertEquals(blms.getNumberOfMatches(idWinner + "", leagueId + ""), new Long(1));
            assertEquals(blms.getNumberOfMatches(idLoser + "", leagueId + ""), new Long(1));
            assertEquals(format.format(date), blms.getMatchDate(matchIdReturned + ""));
            assertEquals(idWinner, blms.getMatchWinner(matchIdReturned + ""));
            assertEquals(idLoser, blms.getMatchLoser(matchIdReturned + ""));
            assertEquals("5", blms.getMatchLength(matchId + ""));
            assertEquals("2", blms.getMatchScore(matchId + ""));
            assertEquals("3", blms.getMatchLongestRunForWinner(matchId + ""));
            assertEquals("1", blms.getMatchLongestRunForLoser(matchId + ""));
            try {
                blms.addMatchResult("", format.format(date), idWinner + "", idLoser + "", "5", "2", "3", "1");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.addMatchResult("20", format.format(date), idWinner + "", idLoser + "", "5", "2", "3", "1");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.addMatchResult(null, format.format(date), idWinner + "", idLoser + "", "5", "2", "3", "1");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.addMatchResult(leagueId + "", format.format(date), "", idLoser + "", "5", "2", "3", "1");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.addMatchResult(leagueId + "", format.format(date), null, idLoser + "", "5", "2", "3", "1");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.addMatchResult(leagueId + "", format.format(date), "aaa", idLoser + "", "5", "2", "3", "1");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.addMatchResult(leagueId + "", format.format(date), "20", idLoser + "", "5", "2", "3", "1");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.addMatchResult(leagueId + "", format.format(date), idWinner + "", "", "5", "2", "3", "1");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.addMatchResult(leagueId + "", format.format(date), "", "aaaa", "5", "2", "3", "1");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.addMatchResult(leagueId + "", format.format(date), "", null, "5", "2", "3", "1");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.addMatchResult(leagueId + "", format.format(date), "", "30", "5", "2", "3", "1");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.addMatchResult(leagueId + "", format.format(date), idLoser + "", idLoser + "", "5", "2", "3", "1");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.addMatchResult(leagueId + "", "19-20-2007", idWinner + "", idLoser + "", "5", "2", "3", "1");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.addMatchResult(leagueId + "", "11/29/2007", idWinner + "", idLoser + "", "5", "2", "3", "1");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.addMatchResult(leagueId + "", "", idWinner + "", idLoser + "", "5", "2", "3", "1");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.addMatchResult(leagueId + "", null, idWinner + "", idLoser + "", "5", "2", "3", "1");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.addMatchResult(leagueId + "", "19/12/2008", idWinner + "", idLoser + "", "0", "2", "3", "1");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.addMatchResult(leagueId + "", "19/12/2008", idWinner + "", idLoser + "", "", "2", "3", "1");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.addMatchResult(leagueId + "", "19/12/2008", idWinner + "", idLoser + "", null, "2", "3", "1");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.addMatchResult(leagueId + "", "19/12/2008", idWinner + "", idLoser + "", "aaaa", "2", "3", "1");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.addMatchResult(leagueId + "", "19/12/2008", idWinner + "", idLoser + "", "5", "10", "3", "1");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.addMatchResult(leagueId + "", "19/12/2008", idWinner + "", idLoser + "", "5", "-2", "3", "1");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.addMatchResult(leagueId + "", "19/12/2008", idWinner + "", idLoser + "", "5", "", "3", "1");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.addMatchResult(leagueId + "", "19/12/2008", idWinner + "", idLoser + "", "5", null, "3", "1");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.addMatchResult(leagueId + "", "19/12/2008", idWinner + "", idLoser + "", "5", "aa", "3", "1");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.addMatchResult(leagueId + "", "19/12/2008", idWinner + "", idLoser + "", "5", "2", "0", "1");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.addMatchResult(leagueId + "", "19/12/2008", idWinner + "", idLoser + "", "5", "2", "-3", "1");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.addMatchResult(leagueId + "", "19/12/2008", idWinner + "", idLoser + "", "5", "2", "20", "1");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.addMatchResult(leagueId + "", "19/12/2008", idWinner + "", idLoser + "", "5", "2", "", "1");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.addMatchResult(leagueId + "", "19/12/2008", idWinner + "", idLoser + "", "5", "2", null, "1");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.addMatchResult(leagueId + "", "19/12/2008", idWinner + "", idLoser + "", "5", "2", "aa", "1");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.addMatchResult(leagueId + "", "19/12/2008", idWinner + "", idLoser + "", "5", "2", "3", "3");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.addMatchResult(leagueId + "", "19/12/2008", idWinner + "", idLoser + "", "5", "2", "3", "0");
            } catch (InvalidParameterException e) {
                fail(e.getMessage());
            } catch (DBException e) {
                fail();
            }
            try {
                blms.addMatchResult(leagueId + "", "19/12/2008", idWinner + "", idLoser + "", "5", "2", "3", "2");
            } catch (InvalidParameterException e) {
                fail();
            } catch (DBException e) {
                fail();
            }
            try {
                blms.addMatchResult(leagueId + "", "19/12/2008", idWinner + "", idLoser + "", "5", "2", "2", "-1");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.addMatchResult(leagueId + "", "19/12/2008", idWinner + "", idLoser + "", "5", "2", "2", "");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.addMatchResult(leagueId + "", "19/12/2008", idWinner + "", idLoser + "", "5", "2", "2", null);
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.addMatchResult(leagueId + "", "19/12/2008", idWinner + "", idLoser + "", "5", "2", "2", "aaa");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.addMatchResult(leagueId + "", "19/12/2008", idWinner + "", idLoser + "", "5", "2", "2", "-1");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.addMatchResult(leagueId + "", "19/12/2008", idWinner + "", idLoser + "", "5", "2", "0", "1");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.addMatchResult(leagueId + "", "19/12/2008", idWinner + "", idLoser + "", "5", "2", "10", "1");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.addMatchResult(leagueId + "", "19/12/2008", idWinner + "", idLoser + "", "5", "2", "5", "1");
            } catch (InvalidParameterException e) {
                fail();
            } catch (DBException e) {
                fail();
            }
            try {
                blms.addMatchResult(leagueId + "", "19/12/2008", idWinner + "", idLoser + "", "5", "2", "1", "1");
            } catch (InvalidParameterException e) {
                fail();
            } catch (DBException e) {
                fail();
            }
            try {
                blms.addMatchResult(leagueId + "", "19/12/2008", idWinner + "", idLoser + "", "5", "2", "", "1");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.addMatchResult(leagueId + "", "19/12/2008", idWinner + "", idLoser + "", "5", "2", null, "1");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.addMatchResult(leagueId + "", "19/12/2008", idWinner + "", idLoser + "", "5", "2", "a", "1");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
        } catch (DBException e) {
            fail();
        } catch (InvalidParameterException e) {
            fail();
        }
    }

    /**
     * This method verifies if the returning of some informations, like number of wins
     * , number of losses and number of matches is being done correctly
     */
    public void testGets() {
        try {
            blms.useDatabase(TestConstants.data1Path);
            blms.removeAllUsers();
            blms.removeAllLeagues();
            blms.removeAllMatches();
            Long idWinner = blms.createUser("test1", "test1", "", "555", "", "test1@g", "");
            Long idLoser = blms.createUser("test2", "test2", "", "888", "", "test2@g", "");
            Long leagueId = blms.createLeague("LEAGUE1", "1");
            blms.joinLeague(idLoser + "", leagueId + "", "200");
            GregorianCalendar c = new GregorianCalendar();
            Date date = c.getTime();
            assertEquals(blms.getNumberOfMatches(leagueId + ""), new Long(0));
            assertEquals(blms.getNumberOfMatches(idWinner + "", leagueId + ""), new Long(0));
            assertEquals(blms.getNumberOfMatches(idLoser + "", leagueId + ""), new Long(0));
            Long matchId = blms.addMatchResult(leagueId + "", format.format(date), idWinner + "", idLoser + "");
            Long matchIdReturned = blms.getMatch(leagueId + "", "1");
            assertEquals(matchId, matchIdReturned);
            assertEquals(blms.getNumberOfMatches(leagueId + ""), new Long(1));
            assertEquals(blms.getNumberOfMatches(idWinner + "", leagueId + ""), new Long(1));
            assertEquals(blms.getNumberOfMatches(idLoser + "", leagueId + ""), new Long(1));
            assertEquals(blms.getNumberOfWins(idWinner + "", leagueId + ""), new Long(1));
            assertEquals(blms.getNumberOfLosses(idLoser + "", leagueId + ""), new Long(1));
            assertEquals(format.format(date), blms.getMatchDate(matchIdReturned + ""));
            assertEquals(idWinner, blms.getMatchWinner(matchIdReturned + ""));
            assertEquals(idLoser, blms.getMatchLoser(matchIdReturned + ""));
            try {
                blms.getNumberOfMatches("", leagueId + "");
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getNumberOfMatches(null, leagueId + "");
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getNumberOfMatches("aaa", leagueId + "");
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getNumberOfMatches("10", leagueId + "");
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getNumberOfMatches(idLoser + "", "");
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getNumberOfMatches(idLoser + "", null);
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getNumberOfMatches(idLoser + "", "aaa");
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getNumberOfMatches(idLoser + "", "10");
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getNumberOfWins("", leagueId + "");
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getNumberOfWins(null, leagueId + "");
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getNumberOfWins("10", leagueId + "");
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getNumberOfWins("aaa", leagueId + "");
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getNumberOfWins(idLoser + "", "");
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getNumberOfWins(idLoser + "", null);
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getNumberOfWins(idLoser + "", "13");
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getNumberOfWins(idLoser + "", "aaaa");
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getNumberOfLosses("", leagueId + "");
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getNumberOfLosses(null, leagueId + "");
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getNumberOfLosses("a", leagueId + "");
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getNumberOfLosses("23", leagueId + "");
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getNumberOfLosses(idLoser + "", "");
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getNumberOfLosses(idLoser + "", null);
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getNumberOfLosses(idLoser + "", "12");
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getNumberOfLosses(idLoser + "", "aaa");
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
        } catch (DBException e) {
            fail();
        } catch (InvalidParameterException e) {
            fail();
        }
    }

    /**
     * This method verifies if the returning of a match id is being done correctly
     */
    public void testGetMatch() {
        try {
            blms.useDatabase(TestConstants.data1Path);
            blms.removeAllUsers();
            blms.removeAllLeagues();
            blms.removeAllMatches();
            Long idWinner = blms.createUser("test1", "test1", "", "555", "", "test1@g", "");
            Long idLoser = blms.createUser("test2", "test2", "", "888", "", "test2@g", "");
            Long idLoser2 = blms.createUser("test3", "test3", "", "888", "", "test3@g", "");
            Long leagueId = blms.createLeague("LEAGUE1", "1");
            blms.joinLeague(idLoser + "", leagueId + "", "200");
            blms.joinLeague(idLoser2 + "", leagueId + "", "200");
            GregorianCalendar c = new GregorianCalendar(2007, 5, 20);
            Date date = c.getTime();
            assertEquals(blms.getNumberOfMatches(leagueId + ""), new Long(0));
            assertEquals(blms.getNumberOfMatches(idWinner + "", leagueId + ""), new Long(0));
            assertEquals(blms.getNumberOfMatches(idLoser + "", leagueId + ""), new Long(0));
            Long matchId = blms.addMatchResult(leagueId + "", format.format(date), idWinner + "", idLoser + "");
            c = new GregorianCalendar(2007, 5, 25);
            date = c.getTime();
            Long matchId2 = blms.addMatchResult(leagueId + "", format.format(date), idWinner + "", idLoser2 + "");
            Long match = blms.getMatch(leagueId + "", "1");
            assertEquals(match, matchId);
            Long match2 = blms.getMatch(leagueId + "", "2");
            assertEquals(match2, matchId2);
            match = blms.getMatch(idWinner + "", leagueId + "", "1");
            assertEquals(match, matchId);
            match2 = blms.getMatch(idWinner + "", leagueId + "", "2");
            assertEquals(match2, matchId2);
            match = blms.getMatch(idLoser + "", leagueId + "", "1");
            assertEquals(match, matchId);
            match2 = blms.getMatch(idLoser2 + "", leagueId + "", "1");
            assertEquals(match2, matchId2);
            try {
                blms.getMatch(idLoser2 + "", "", "1");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getMatch(idLoser2 + "", null, "1");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getMatch(idLoser2 + "", "aaa", "1");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getMatch(idLoser2 + "", "12", "1");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getMatch("", "1");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getMatch(null, "1");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getMatch("12", "1");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getMatch("aaaa", "1");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getMatch("", leagueId + "", "1");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getMatch(null, leagueId + "", "1");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getMatch("aaaa", leagueId + "", "1");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getMatch("12", leagueId + "", "1");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getMatch(idWinner + "", leagueId + "", "");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getMatch(idWinner + "", leagueId + "", "12");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getMatch(idWinner + "", leagueId + "", null);
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getMatch(idWinner + "", leagueId + "", "aaaa");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getMatch(leagueId + "", "");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getMatch(leagueId + "", "aaa");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getMatch(leagueId + "", null);
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getMatch(leagueId + "", "12");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
        } catch (DBException e) {
            fail();
        } catch (InvalidParameterException e) {
            fail();
        }
    }

    /**
     * This method verifies if the returning of matches attributes is being done correctly
     */
    public void testGetMatchAttributes() {
        try {
            blms.useDatabase(TestConstants.data1Path);
            blms.removeAllUsers();
            blms.removeAllLeagues();
            blms.removeAllMatches();
            Long idWinner = blms.createUser("test1", "test1", "", "555", "", "test1@g", "");
            Long idLoser = blms.createUser("test2", "test2", "", "888", "", "test2@g", "");
            Long leagueId = blms.createLeague("LEAGUE1", "1");
            blms.joinLeague(idLoser + "", leagueId + "", "200");
            GregorianCalendar c = new GregorianCalendar();
            Date date = c.getTime();
            assertEquals(blms.getNumberOfMatches(leagueId + ""), new Long(0));
            assertEquals(blms.getNumberOfMatches(idWinner + "", leagueId + ""), new Long(0));
            assertEquals(blms.getNumberOfMatches(idLoser + "", leagueId + ""), new Long(0));
            Long matchId = blms.addMatchResult(leagueId + "", format.format(date), idWinner + "", idLoser + "");
            assertEquals(format.format(date), blms.getMatchDate(matchId + ""));
            assertEquals(idWinner, blms.getMatchWinner(matchId + ""));
            assertEquals(idLoser, blms.getMatchLoser(matchId + ""));
            assertEquals("", blms.getMatchLongestRunForLoser(matchId + ""));
            assertEquals("", blms.getMatchLongestRunForWinner(matchId + ""));
            assertEquals("", blms.getMatchLength(matchId + ""));
            assertEquals("", blms.getMatchScore(matchId + ""));
            try {
                blms.getMatchScore("");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getMatchScore(null);
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getMatchScore("aaaaa");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getMatchScore("50");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getMatchLength("");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getMatchLength(null);
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getMatchLength("aaaaa");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getMatchLength("50");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getMatchDate("");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getMatchDate(null);
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getMatchDate("aaaaa");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getMatchDate("50");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getMatchLoser("");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getMatchLoser(null);
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getMatchLoser("aaaaa");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getMatchLoser("50");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getMatchWinner("");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getMatchWinner(null);
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getMatchWinner("aaaaa");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getMatchWinner("50");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getMatchLongestRunForWinner("");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getMatchLongestRunForWinner(null);
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getMatchLongestRunForWinner("aaaaa");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getMatchLongestRunForWinner("50");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getMatchLongestRunForLoser("");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getMatchLongestRunForLoser(null);
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getMatchLongestRunForLoser("aaaaa");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getMatchLongestRunForLoser("50");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
        } catch (DBException e) {
            fail();
        } catch (InvalidParameterException e) {
            fail();
        }
    }

    /**
     * This method verifies if the update of match results is being done correctly
     */
    public void testUpdateMatchResults() {
        try {
            blms.useDatabase(TestConstants.data1Path);
            blms.removeAllUsers();
            blms.removeAllLeagues();
            blms.removeAllMatches();
            Long idWinner = blms.createUser("test1", "test1", "", "555", "", "test1@g", "");
            Long idLoser = blms.createUser("test2", "test2", "", "888", "", "test2@g", "");
            Long leagueId = blms.createLeague("LEAGUE1", "1");
            blms.joinLeague(idLoser + "", leagueId + "", "200");
            GregorianCalendar c = new GregorianCalendar();
            Date date = c.getTime();
            assertEquals(blms.getNumberOfMatches(leagueId + ""), new Long(0));
            assertEquals(blms.getNumberOfMatches(idWinner + "", leagueId + ""), new Long(0));
            assertEquals(blms.getNumberOfMatches(idLoser + "", leagueId + ""), new Long(0));
            Long matchId = blms.addMatchResult(leagueId + "", format.format(date), idWinner + "", idLoser + "", "5", "2", "3", "1");
            Long matchIdReturned = blms.getMatch(leagueId + "", "1");
            assertEquals(matchId, matchIdReturned);
            assertEquals(blms.getNumberOfMatches(leagueId + ""), new Long(1));
            assertEquals(blms.getNumberOfMatches(idWinner + "", leagueId + ""), new Long(1));
            assertEquals(blms.getNumberOfMatches(idLoser + "", leagueId + ""), new Long(1));
            assertEquals(format.format(date), blms.getMatchDate(matchIdReturned + ""));
            assertEquals(idWinner, blms.getMatchWinner(matchIdReturned + ""));
            assertEquals(idLoser, blms.getMatchLoser(matchIdReturned + ""));
            assertEquals("5", blms.getMatchLength(matchId + ""));
            assertEquals("2", blms.getMatchScore(matchId + ""));
            assertEquals("3", blms.getMatchLongestRunForWinner(matchId + ""));
            assertEquals("1", blms.getMatchLongestRunForLoser(matchId + ""));
            blms.updateMatchResult(matchId + "", format.format(date), idLoser + "", idWinner + "", "10", "5", "4", "2");
            assertEquals(format.format(date), blms.getMatchDate(matchIdReturned + ""));
            assertEquals(idLoser, blms.getMatchWinner(matchIdReturned + ""));
            assertEquals(idWinner, blms.getMatchLoser(matchIdReturned + ""));
            assertEquals("10", blms.getMatchLength(matchId + ""));
            assertEquals("5", blms.getMatchScore(matchId + ""));
            assertEquals("4", blms.getMatchLongestRunForWinner(matchId + ""));
            assertEquals("2", blms.getMatchLongestRunForLoser(matchId + ""));
            try {
                blms.updateMatchResult("", format.format(date), idLoser + "", idWinner + "", "10", "5", "4", "2");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.updateMatchResult(null, format.format(date), idLoser + "", idWinner + "", "10", "5", "4", "2");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.updateMatchResult("aaaa", format.format(date), idLoser + "", idWinner + "", "10", "5", "4", "2");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.updateMatchResult("30", format.format(date), idLoser + "", idWinner + "", "10", "5", "4", "2");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.updateMatchResult(matchId + "", format.format(date), idLoser + "", idLoser + "", "10", "5", "4", "2");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.updateMatchResult(matchId + "", "19-20-2007", idWinner + "", idLoser + "", "10", "5", "4", "2");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.updateMatchResult(matchId + "", "11/29/2007", idWinner + "", idLoser + "", "10", "5", "4", "2");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.updateMatchResult(matchId + "", "", idWinner + "", idLoser + "", "10", "5", "4", "2");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.updateMatchResult(matchId + "", null, idWinner + "", idLoser + "", "10", "5", "4", "2");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.updateMatchResult(matchId + "", format.format(date), idWinner + "", idLoser + "", "0", "5", "4", "2");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.updateMatchResult(matchId + "", format.format(date), idWinner + "", idLoser + "", "", "5", "4", "2");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.updateMatchResult(matchId + "", format.format(date), idWinner + "", idLoser + "", null, "5", "4", "2");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.updateMatchResult(matchId + "", format.format(date), idWinner + "", idLoser + "", "aaa", "5", "4", "2");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.updateMatchResult(matchId + "", format.format(date), idWinner + "", idLoser + "", "10", "15", "4", "2");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.updateMatchResult(matchId + "", format.format(date), idWinner + "", idLoser + "", "10", "-2", "4", "2");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.updateMatchResult(matchId + "", format.format(date), idWinner + "", idLoser + "", "10", "", "4", "2");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.updateMatchResult(matchId + "", format.format(date), idWinner + "", idLoser + "", "10", null, "4", "2");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.updateMatchResult(matchId + "", format.format(date), idWinner + "", idLoser + "", "10", "a", "4", "2");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.updateMatchResult(matchId + "", format.format(date), idWinner + "", idLoser + "", "10", "5", "0", "2");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.updateMatchResult(matchId + "", format.format(date), idWinner + "", idLoser + "", "10", "5", "-4", "2");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.updateMatchResult(matchId + "", format.format(date), idWinner + "", idLoser + "", "10", "5", "15", "2");
                blms.addMatchResult(leagueId + "", "19/12/2008", idWinner + "", idLoser + "", "5", "2", "20", "1");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.updateMatchResult(matchId + "", format.format(date), idWinner + "", idLoser + "", "10", "5", "", "2");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.updateMatchResult(matchId + "", format.format(date), idWinner + "", idLoser + "", "10", "5", null, "2");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.updateMatchResult(matchId + "", format.format(date), idWinner + "", idLoser + "", "10", "5", "a", "2");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.updateMatchResult(matchId + "", format.format(date), idWinner + "", idLoser + "", "10", "5", "7", "8");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.updateMatchResult(matchId + "", format.format(date), idWinner + "", idLoser + "", "10", "5", "7", "0");
            } catch (InvalidParameterException e) {
                fail(e.getMessage());
            } catch (DBException e) {
                fail();
            }
            try {
                blms.updateMatchResult(matchId + "", format.format(date), idWinner + "", idLoser + "", "10", "5", "7", "5");
            } catch (InvalidParameterException e) {
                fail();
            } catch (DBException e) {
                fail();
            }
            try {
                blms.updateMatchResult(matchId + "", format.format(date), idWinner + "", idLoser + "", "10", "5", "7", "-8");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.updateMatchResult(matchId + "", format.format(date), idWinner + "", idLoser + "", "10", "5", "7", "");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.updateMatchResult(matchId + "", format.format(date), idWinner + "", idLoser + "", "10", "5", "7", null);
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.updateMatchResult(matchId + "", format.format(date), idWinner + "", idLoser + "", "10", "5", "7", "a");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.updateMatchResult(matchId + "", format.format(date), idWinner + "", idLoser + "", "10", "5", "7", "-10");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
        } catch (DBException e) {
            fail();
        } catch (InvalidParameterException e) {
            fail();
        } catch (ParseException e) {
            fail();
        }
    }

    /**
     * This method verifies if the dates are being returned correctly according
     * to the dates
     */
    public void testGetMatchByDate() {
        try {
            blms.useDatabase(TestConstants.data1Path);
            blms.removeAllUsers();
            blms.removeAllLeagues();
            blms.removeAllMatches();
            Long idWinner = blms.createUser("test1", "test1", "", "555", "", "test1@g", "");
            Long idLoser = blms.createUser("test2", "test2", "", "888", "", "test2@g", "");
            Long leagueId = blms.createLeague("LEAGUE1", "1");
            blms.joinLeague(idLoser + "", leagueId + "", "200");
            GregorianCalendar c = new GregorianCalendar(2008, 4, 22);
            Date date = c.getTime();
            assertEquals(blms.getNumberOfMatches(leagueId + ""), new Long(0));
            assertEquals(blms.getNumberOfMatches(idWinner + "", leagueId + ""), new Long(0));
            assertEquals(blms.getNumberOfMatches(idLoser + "", leagueId + ""), new Long(0));
            Long matchId = blms.addMatchResult(leagueId + "", format.format(date), idWinner + "", idLoser + "");
            c = new GregorianCalendar(2008, 4, 30);
            Date date2 = c.getTime();
            Long matchId2 = blms.addMatchResult(leagueId + "", format.format(date2), idWinner + "", idLoser + "");
            Long matchIdReturned = blms.getMatch(leagueId + "", "1");
            assertEquals(matchId, matchIdReturned);
            assertEquals(blms.getNumberOfMatches(leagueId + ""), new Long(2));
            assertEquals(blms.getNumberOfMatches(idWinner + "", leagueId + ""), new Long(2));
            assertEquals(blms.getNumberOfMatches(idLoser + "", leagueId + ""), new Long(2));
            assertEquals(format.format(date), blms.getMatchDate(matchIdReturned + ""));
            assertEquals(idWinner, blms.getMatchWinner(matchIdReturned + ""));
            assertEquals(idLoser, blms.getMatchLoser(matchIdReturned + ""));
            matchIdReturned = blms.getMatch(leagueId + "", "2");
            assertEquals(matchId2, matchIdReturned);
            assertEquals(blms.getNumberOfMatches(leagueId + ""), new Long(2));
            assertEquals(blms.getNumberOfMatches(idWinner + "", leagueId + ""), new Long(2));
            assertEquals(blms.getNumberOfMatches(idLoser + "", leagueId + ""), new Long(2));
            assertEquals(format.format(date2), blms.getMatchDate(matchIdReturned + ""));
            assertEquals(idWinner, blms.getMatchWinner(matchIdReturned + ""));
            assertEquals(idLoser, blms.getMatchLoser(matchIdReturned + ""));
            c = new GregorianCalendar(2007, 3, 22);
            Date dateInit = c.getTime();
            c = new GregorianCalendar(2008, 5, 1);
            Date dateEnd = c.getTime();
            Long matchByDate = blms.getMatchByDate(leagueId + "", format.format(dateInit), format.format(dateEnd), "1");
            assertEquals(matchByDate, matchId);
            matchByDate = blms.getMatchByDate(leagueId + "", format.format(dateInit), format.format(dateEnd), "2");
            assertEquals(matchByDate, matchId2);
            matchByDate = blms.getMatchByDate(idWinner + "", leagueId + "", format.format(dateInit), format.format(dateEnd), "1");
            assertEquals(matchByDate, matchId);
            matchByDate = blms.getMatchByDate(idWinner + "", leagueId + "", format.format(dateInit), format.format(dateEnd), "2");
            assertEquals(matchByDate, matchId2);
            try {
                blms.getMatchByDate(idWinner + "", leagueId + "", format.format(dateInit), format.format(dateInit), "2");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getMatchByDate(leagueId + "", format.format(dateInit), format.format(dateInit), "2");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getMatchByDate(idWinner + "", leagueId + "", "11/23/2008", format.format(dateEnd), "2");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getMatchByDate(idWinner + "", leagueId + "", "", format.format(dateEnd), "2");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getMatchByDate(idWinner + "", leagueId + "", "aaaa", format.format(dateEnd), "2");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getMatchByDate(idWinner + "", leagueId + "", null, format.format(dateEnd), "2");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getMatchByDate(idWinner + "", leagueId + "", "22-09-1999", format.format(dateEnd), "2");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getMatchByDate(leagueId + "", format.format(dateInit), "11/23/2008", "2");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getMatchByDate(leagueId + "", format.format(dateInit), "", "2");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getMatchByDate(leagueId + "", format.format(dateInit), "aaaa", "2");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getMatchByDate(leagueId + "", format.format(dateInit), null, "2");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getMatchByDate(leagueId + "", format.format(dateInit), "22-09-1999", "2");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getMatchByDate("", leagueId + "", format.format(dateInit), "22-09-1999", "2");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getMatchByDate("aaa", leagueId + "", format.format(dateInit), "22-09-1999", "2");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getMatchByDate(null, leagueId + "", format.format(dateInit), "22-09-1999", "2");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getMatchByDate("30", leagueId + "", format.format(dateInit), "22-09-1999", "2");
                fail();
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
        } catch (DBException e) {
            fail();
        } catch (InvalidParameterException e) {
            fail();
        } catch (ParseException e) {
            fail();
        }
    }

    public void testGetMatchAsObject() {
        try {
            blms.useDatabase(TestConstants.data1Path);
            blms.removeAllUsers();
            blms.removeAllLeagues();
            blms.removeAllMatches();
            Long us1 = blms.createUser("test1", "test1", "", "555", "", "test1@g", "");
            Long us2 = blms.createUser("test2", "test2", "", "888", "", "test2@g", "");
            Long leagueId = blms.createLeague("LEAGUE1", String.valueOf(us1));
            blms.joinLeague(String.valueOf(us2), String.valueOf(leagueId), "100");
            blms.addMatchResult(String.valueOf(leagueId), "11/02/1987", String.valueOf(us1), String.valueOf(us2));
            assertEquals(blms.getMatchAsObject(String.valueOf(us1), String.valueOf(leagueId), "1"), "date=11/02/1987,opponent=test2 test2,myScore=,opponentScore=,myRun=,opponentRun=,myHandicap=0,winStatus=true");
            assertEquals(blms.getMatchAsObject(String.valueOf(us2), String.valueOf(leagueId), "1"), "date=11/02/1987,opponent=test1 test1,myScore=,opponentScore=,myRun=,opponentRun=,myHandicap=100,winStatus=false");
            blms.addMatchResult(String.valueOf(leagueId), "11/02/1987", String.valueOf(us1), String.valueOf(us2), "150", "87", "23", "30");
            assertEquals(blms.getMatchAsObject(String.valueOf(us1), String.valueOf(leagueId), "2"), "date=11/02/1987,opponent=test2 test2,myScore=150,opponentScore=87,myRun=23,opponentRun=30,myHandicap=0,winStatus=true");
            assertEquals(blms.getMatchAsObject(String.valueOf(us2), String.valueOf(leagueId), "2"), "date=11/02/1987,opponent=test1 test1,myScore=87,opponentScore=150,myRun=30,opponentRun=23,myHandicap=100,winStatus=false");
            blms.addMatchResult(String.valueOf(leagueId), "09/02/1987", String.valueOf(us2), String.valueOf(us1), "150", "87", "23", "30");
            assertEquals(blms.getMatchAsObject(String.valueOf(us2), String.valueOf(leagueId), "1"), "date=09/02/1987,opponent=test1 test1,myScore=150,opponentScore=87,myRun=23,opponentRun=30,myHandicap=100,winStatus=true");
            assertEquals(blms.getMatchAsObject(String.valueOf(us1), String.valueOf(leagueId), "1"), "date=09/02/1987,opponent=test2 test2,myScore=87,opponentScore=150,myRun=30,opponentRun=23,myHandicap=0,winStatus=false");
            try {
                blms.getMatchAsObject("user", String.valueOf(leagueId), "1");
                fail();
            } catch (InvalidParameterException e) {
            }
            try {
                blms.getMatchAsObject("", String.valueOf(leagueId), "1");
                fail();
            } catch (InvalidParameterException e) {
            }
            try {
                blms.getMatchAsObject(String.valueOf(us1), "league", "1");
                fail();
            } catch (InvalidParameterException e) {
            }
            try {
                blms.getMatchAsObject(String.valueOf(us1), "", "1");
                fail();
            } catch (InvalidParameterException e) {
            }
            try {
                blms.getMatchAsObject(String.valueOf(us1), String.valueOf(leagueId), "5");
                fail();
            } catch (InvalidParameterException e) {
            }
            try {
                blms.getMatchAsObject(String.valueOf(us1), String.valueOf(leagueId), "0");
                fail();
            } catch (InvalidParameterException e) {
            }
            try {
                blms.getMatchAsObject(String.valueOf(us1), String.valueOf(leagueId), "");
                fail();
            } catch (InvalidParameterException e) {
            }
        } catch (DBException e) {
        } catch (InvalidParameterException e) {
        }
    }

    /**
     * This method verifies if the returning of a userLeague attribute is verifying the 
     * error conditions correctly
     */
    public void testGetUserLeagueAttribute() {
        try {
            blms.useDatabase(TestConstants.data1Path);
            blms.removeAllUsers();
            blms.removeAllLeagues();
            blms.removeAllMatches();
            Long idWinner = blms.createUser("test1", "test1", "", "555", "", "test1@g", "");
            Long idLoser = blms.createUser("test2", "test2", "", "888", "", "test2@g", "");
            Long leagueId = blms.createLeague("LEAGUE1", "1");
            blms.joinLeague(idLoser + "", leagueId + "", "200");
        } catch (InvalidParameterException e1) {
            fail();
        } catch (DBException e1) {
            fail();
        }
        try {
            blms.getUserLeagueAttribute("", 1 + "", UserLeagueUtil.CURRENT_HANDICAP);
            fail();
        } catch (InvalidParameterException e) {
        } catch (DBException e) {
        }
        try {
            blms.getUserLeagueAttribute("sdsd", 1 + "", UserLeagueUtil.CURRENT_HANDICAP);
            fail();
        } catch (InvalidParameterException e) {
        } catch (DBException e) {
        }
        try {
            blms.getUserLeagueAttribute(null, 1 + "", UserLeagueUtil.CURRENT_HANDICAP);
            fail();
        } catch (InvalidParameterException e) {
        } catch (DBException e) {
        }
        try {
            blms.getUserLeagueAttribute("200", 1 + "", UserLeagueUtil.CURRENT_HANDICAP);
            fail();
        } catch (InvalidParameterException e) {
        } catch (DBException e) {
        }
        try {
            blms.getUserLeagueAttribute(1 + "", "", UserLeagueUtil.CURRENT_HANDICAP);
            fail();
        } catch (InvalidParameterException e) {
        } catch (DBException e) {
        }
        try {
            blms.getUserLeagueAttribute(1 + "", null, UserLeagueUtil.CURRENT_HANDICAP);
            fail();
        } catch (InvalidParameterException e) {
        } catch (DBException e) {
        }
        try {
            blms.getUserLeagueAttribute(1 + "", "100", UserLeagueUtil.CURRENT_HANDICAP);
            fail();
        } catch (InvalidParameterException e) {
        } catch (DBException e) {
        }
        try {
            blms.getUserLeagueAttribute(1 + "", "ads", UserLeagueUtil.CURRENT_HANDICAP);
            fail();
        } catch (InvalidParameterException e) {
        } catch (DBException e) {
        }
        try {
            blms.getUserLeagueAttribute(1 + "", 1 + "", "");
            fail();
        } catch (InvalidParameterException e) {
        } catch (DBException e) {
        }
        try {
            blms.getUserLeagueAttribute(1 + "", 1 + "", null);
            fail();
        } catch (InvalidParameterException e) {
        } catch (DBException e) {
        }
        try {
            blms.getUserLeagueAttribute(1 + "", 1 + "", "date");
            fail();
        } catch (InvalidParameterException e) {
        } catch (DBException e) {
        }
        try {
            blms.getUserLeagueAttribute(1 + "", 1 + "", "handicap");
            fail();
        } catch (InvalidParameterException e) {
        } catch (DBException e) {
        }
    }

    /**
     * This method verifies if the returning of the number of matches between players
     * is being done correctly
     */
    public void testGetNumberOfMatchesBetween() {
        try {
            blms.useDatabase(TestConstants.data1Path);
            blms.removeAllUsers();
            blms.removeAllLeagues();
            blms.removeAllMatches();
            Long idWinner = blms.createUser("test1", "test1", "", "555", "", "test1@g", "");
            Long idLoser = blms.createUser("test2", "test2", "", "888", "", "test2@g", "");
            Long leagueId = blms.createLeague("LEAGUE1", "1");
            blms.joinLeague(idLoser + "", leagueId + "", "200");
            GregorianCalendar calendar = new GregorianCalendar();
            Date date = calendar.getTime();
            assertEquals(blms.getNumberOfMatches(leagueId + ""), new Long(0));
            assertEquals(blms.getNumberOfMatches(idWinner + "", leagueId + ""), new Long(0));
            assertEquals(blms.getNumberOfMatches(idLoser + "", leagueId + ""), new Long(0));
            calendar = new GregorianCalendar(2007, 07, 13);
            Date date2 = calendar.getTime();
            calendar = new GregorianCalendar(2007, 10, 20);
            Date date3 = calendar.getTime();
            Long matchId = blms.addMatchResult(leagueId + "", format.format(date), idWinner + "", idLoser + "");
            Long matchId2 = blms.addMatchResult(leagueId + "", format.format(date2), idLoser + "", idWinner + "");
            Long matchId3 = blms.addMatchResult(leagueId + "", format.format(date3), idWinner + "", idLoser + "");
            Long numberOfMacthesBetween = blms.getNumberOfMacthesBetween(idWinner + "", idLoser + "", leagueId + "");
            assertEquals(numberOfMacthesBetween, new Long(3));
            try {
                blms.getNumberOfMacthesBetween("", idLoser + "", leagueId + "");
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getNumberOfMacthesBetween(null, idLoser + "", leagueId + "");
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getNumberOfMacthesBetween("20", idLoser + "", leagueId + "");
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getNumberOfMacthesBetween(idWinner + "", "", leagueId + "");
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getNumberOfMacthesBetween(idWinner + "", null, leagueId + "");
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getNumberOfMacthesBetween(idWinner + "", "20", leagueId + "");
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getNumberOfMacthesBetween(idWinner + "", idLoser + "", "");
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getNumberOfMacthesBetween(idWinner + "", idLoser + "", null);
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
            try {
                blms.getNumberOfMacthesBetween(idWinner + "", idLoser + "", "50");
            } catch (InvalidParameterException e) {
            } catch (DBException e) {
            }
        } catch (DBException e) {
            fail();
        } catch (InvalidParameterException e) {
            fail();
        }
    }

    /**
     * This method verifies if the returning of matches between a given date period
     * is being done correctly for a certain player in a certain league
     */
    public void testGetMatchesAsString1() {
        try {
            blms.useDatabase(TestConstants.data1Path);
            blms.removeAllUsers();
            blms.removeAllLeagues();
            blms.removeAllMatches();
            Long idWinner = blms.createUser("test1", "test1", "", "555", "", "test1@g", "");
            Long idLoser = blms.createUser("test2", "test2", "", "888", "", "test2@g", "");
            Long leagueId = blms.createLeague("LEAGUE1", "1");
            GregorianCalendar calendar = new GregorianCalendar();
            Date date = calendar.getTime();
            assertEquals(blms.getNumberOfMatches(leagueId + ""), new Long(0));
            assertEquals(blms.getNumberOfMatches(idWinner + "", leagueId + ""), new Long(0));
            assertEquals(blms.getNumberOfMatches(idLoser + "", leagueId + ""), new Long(0));
            blms.joinLeague(idLoser + "", leagueId + "", "200");
            calendar = new GregorianCalendar(2007, 07, 13);
            Date date2 = calendar.getTime();
            calendar = new GregorianCalendar(2007, 10, 20);
            Date date3 = calendar.getTime();
            Long matchId = blms.addMatchResult(leagueId + "", format.format(date), idWinner + "", idLoser + "");
            Long matchId2 = blms.addMatchResult(leagueId + "", format.format(date2), idLoser + "", idWinner + "");
            Long matchId3 = blms.addMatchResult(leagueId + "", format.format(date3), idWinner + "", idLoser + "");
            Configuration configuration = blms.getConfiguration(Util.FORMAT_CONFIG);
            SimpleDateFormat format = new SimpleDateFormat(configuration.getConfigValue());
            List<List<String>> matchsAsString = blms.getMatchsAsString(leagueId + "", idWinner + "", format.format(date2), format.format(date3));
            assertNotNull(matchsAsString);
            assertEquals(matchsAsString.size(), 2);
            List<String> list1 = matchsAsString.get(0);
            List<String> list2 = matchsAsString.get(1);
            assertNotNull(list1);
            assertNotNull(list2);
            assertEquals(list1.size(), 11);
            assertEquals(list2.size(), 11);
            try {
                blms.getMatchsAsString("", idWinner + "", format.format(date2), format.format(date3));
            } catch (DBException e) {
            } catch (InvalidParameterException e) {
            }
            try {
                blms.getMatchsAsString(null, idWinner + "", format.format(date2), format.format(date3));
            } catch (DBException e) {
            } catch (InvalidParameterException e) {
            }
            try {
                blms.getMatchsAsString("50", idWinner + "", format.format(date2), format.format(date3));
            } catch (DBException e) {
            } catch (InvalidParameterException e) {
            }
            try {
                blms.getMatchsAsString(leagueId + "", "", format.format(date2), format.format(date3));
            } catch (DBException e) {
            } catch (InvalidParameterException e) {
            }
            try {
                blms.getMatchsAsString(leagueId + "", null, format.format(date2), format.format(date3));
            } catch (DBException e) {
            } catch (InvalidParameterException e) {
            }
            try {
                blms.getMatchsAsString(leagueId + "", "70", format.format(date2), format.format(date3));
            } catch (DBException e) {
            } catch (InvalidParameterException e) {
            }
            try {
                blms.getMatchsAsString(leagueId + "", idWinner + "", "", format.format(date3));
            } catch (DBException e) {
            } catch (InvalidParameterException e) {
            }
            try {
                blms.getMatchsAsString(leagueId + "", idWinner + "", null, format.format(date3));
            } catch (DBException e) {
            } catch (InvalidParameterException e) {
            }
            try {
                blms.getMatchsAsString(leagueId + "", idWinner + "", "xxxx", format.format(date3));
            } catch (DBException e) {
            } catch (InvalidParameterException e) {
            }
            try {
                blms.getMatchsAsString(leagueId + "", idWinner + "", "19-20-2005", format.format(date3));
            } catch (DBException e) {
            } catch (InvalidParameterException e) {
            }
            try {
                blms.getMatchsAsString(leagueId + "", idWinner + "", format.format(date2), "");
            } catch (DBException e) {
            } catch (InvalidParameterException e) {
            }
            try {
                blms.getMatchsAsString(leagueId + "", idWinner + "", format.format(date2), null);
            } catch (DBException e) {
            } catch (InvalidParameterException e) {
            }
            try {
                blms.getMatchsAsString(leagueId + "", idWinner + "", format.format(date2), "xxxx");
            } catch (DBException e) {
            } catch (InvalidParameterException e) {
            }
            try {
                blms.getMatchsAsString(leagueId + "", idWinner + "", format.format(date2), "11-02-2002");
            } catch (DBException e) {
            } catch (InvalidParameterException e) {
            }
        } catch (DBException e) {
            fail(e.getMessage());
        } catch (InvalidParameterException e) {
            fail(e.getMessage());
        }
    }

    /**
     * This method verifies if the returning of matches between a given date period
     * is being done correctly for a certain league
     */
    public void testGetMatchesAsString2() {
        try {
            blms.useDatabase(TestConstants.data1Path);
            blms.removeAllUsers();
            blms.removeAllLeagues();
            blms.removeAllMatches();
            Long idWinner = blms.createUser("test1", "test1", "", "555", "", "test1@g", "");
            Long idLoser = blms.createUser("test2", "test2", "", "888", "", "test2@g", "");
            Long leagueId = blms.createLeague("LEAGUE1", "1");
            GregorianCalendar calendar = new GregorianCalendar();
            Date date = calendar.getTime();
            assertEquals(blms.getNumberOfMatches(leagueId + ""), new Long(0));
            assertEquals(blms.getNumberOfMatches(idWinner + "", leagueId + ""), new Long(0));
            assertEquals(blms.getNumberOfMatches(idLoser + "", leagueId + ""), new Long(0));
            blms.joinLeague(idLoser + "", leagueId + "", "200");
            calendar = new GregorianCalendar(2007, 07, 13);
            Date date2 = calendar.getTime();
            calendar = new GregorianCalendar(2007, 10, 20);
            Date date3 = calendar.getTime();
            Long matchId = blms.addMatchResult(leagueId + "", format.format(date), idWinner + "", idLoser + "");
            Long matchId2 = blms.addMatchResult(leagueId + "", format.format(date2), idLoser + "", idWinner + "");
            Long matchId3 = blms.addMatchResult(leagueId + "", format.format(date3), idWinner + "", idLoser + "");
            Configuration configuration = blms.getConfiguration(Util.FORMAT_CONFIG);
            SimpleDateFormat format = new SimpleDateFormat(configuration.getConfigValue());
            List<List<String>> matchsAsString = blms.getMatchsAsString(leagueId + "", format.format(date2), format.format(date3));
            assertNotNull(matchsAsString);
            assertEquals(matchsAsString.size(), 2);
            List<String> list1 = matchsAsString.get(0);
            List<String> list2 = matchsAsString.get(1);
            assertNotNull(list1);
            assertNotNull(list2);
            assertEquals(list1.size(), 11);
            assertEquals(list2.size(), 11);
            try {
                blms.getMatchsAsString("", format.format(date2), format.format(date3));
            } catch (DBException e) {
            } catch (InvalidParameterException e) {
            }
            try {
                blms.getMatchsAsString(null, format.format(date2), format.format(date3));
            } catch (DBException e) {
            } catch (InvalidParameterException e) {
            }
            try {
                blms.getMatchsAsString("50", format.format(date2), format.format(date3));
            } catch (DBException e) {
            } catch (InvalidParameterException e) {
            }
            try {
                blms.getMatchsAsString(leagueId + "", "", format.format(date3));
            } catch (DBException e) {
            } catch (InvalidParameterException e) {
            }
            try {
                blms.getMatchsAsString(leagueId + "", null, format.format(date3));
            } catch (DBException e) {
            } catch (InvalidParameterException e) {
            }
            try {
                blms.getMatchsAsString(leagueId + "", "xxxx", format.format(date3));
            } catch (DBException e) {
            } catch (InvalidParameterException e) {
            }
            try {
                blms.getMatchsAsString(leagueId + "", "19-20-2005", format.format(date3));
            } catch (DBException e) {
            } catch (InvalidParameterException e) {
            }
            try {
                blms.getMatchsAsString(leagueId + "", format.format(date2), "");
            } catch (DBException e) {
            } catch (InvalidParameterException e) {
            }
            try {
                blms.getMatchsAsString(leagueId + "", format.format(date2), null);
            } catch (DBException e) {
            } catch (InvalidParameterException e) {
            }
            try {
                blms.getMatchsAsString(leagueId + "", format.format(date2), "xxxx");
            } catch (DBException e) {
            } catch (InvalidParameterException e) {
            }
            try {
                blms.getMatchsAsString(leagueId + "", format.format(date2), "11-02-2002");
            } catch (DBException e) {
            } catch (InvalidParameterException e) {
            }
        } catch (DBException e) {
            fail(e.getMessage());
        } catch (InvalidParameterException e) {
            fail(e.getMessage());
        }
    }
}
