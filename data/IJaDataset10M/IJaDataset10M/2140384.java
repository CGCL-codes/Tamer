package matcher;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import util.DBConstants;
import util.DbUtil;
import util.IPrepareStatementBack;
import util.ISQLCallback;
import util.MatchUtil;
import util.Sport;

/**
 * http://spreadsheets.google.com/ccc?key=0
 * Aoh5EKZDaM1GdGtZMVd5VDZSVngxSlNTNEV3UkExUVE&hl=zh_CN
 * 
 * 
 */
@SuppressWarnings("unchecked")
public class MatchDao {

    public static void main(String[] args) {
        matchAllSports();
        matchHorseRace();
    }

    private static void testMatchSport() {
    }

    public static void matchAllSports() {
        for (Sport s : Sport.values()) matchSport(s);
    }

    private static void matchSport(Sport sport) {
        MatchDao dao = new MatchDao();
        System.out.println("Tab ..............");
        List<TabSport> tabModels = dao.seleceTabSportRecords(sport.getTabName());
        System.out.println("Betfair ..............");
        List<BetfairSport> betfairModels = dao.findBetfairSports(sport.getBetfairName());
        List<MatchResult> matched = new ArrayList<MatchResult>();
        for (TabSport tabModel : tabModels) {
            for (BetfairSport betfairModel : betfairModels) {
                MatchResult matchResult = MatchHelper.matchSport(tabModel, betfairModel, sport);
                if (matchResult != null && matchResult.isMatch()) {
                    matched.add(matchResult);
                    int matchId;
                    try {
                        matchId = insertMatchSport(tabModel, betfairModel);
                        matchResult.setMatchId(matchId);
                        matchResult.setBetfairMenuPath(betfairModel.getSport_menu());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
        matchSportPlayer(dao, sport, matched);
    }

    private static void matchSportPlayer(MatchDao dao, Sport sport, List<MatchResult> matched) {
        System.out.println(matched.size());
        for (MatchResult result : matched) {
            printMatchSport(result.getTabRecord(), result.getBetfairRecord());
            TabSport tabSportModel = (TabSport) result.getTabRecord();
            BetfairSport betfairModel = (BetfairSport) result.getBetfairRecord();
            List<BetfairPlayer> betPlayers = dao.findBetfairPlayer(betfairModel.getSport_id());
            List<TabOdds> tabOdds = dao.findTabOdds("select * from oddstable where id in(" + tabSportModel.getPlayerIds() + ")");
            for (BetfairPlayer player : betPlayers) {
                for (TabOdds tabPlayer : tabOdds) {
                    if (MatchHelper.matchPlayers(tabPlayer, player, sport).isMatch()) {
                        printMatchOdds(player, tabPlayer);
                        try {
                            insertMatchOdds(result.getMatchId(), tabPlayer, player, tabSportModel, betfairModel);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                }
            }
        }
    }

    private static void matchHorseRace() {
        MatchDao dao = new MatchDao();
        List<TabRace> list = dao.selectTabRaceRecords();
        List<BetfairRace> braces = dao.selectBetfairRaceRecords();
        for (TabRace tabRace : list) {
            for (BetfairRace betRace : braces) {
                MatchResult mr = MatchHelper.matchHorseRace(tabRace, betRace);
                if (mr != null && mr.isMatch()) {
                    printMatchedTabBetRace(tabRace, betRace);
                    try {
                        final int matchId = insertMatchRace(tabRace, betRace);
                        System.out.println("Horse Information for Race " + betRace.getRace_name());
                        matchHorseRunner(dao, tabRace, betRace, matchId);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
    }

    private static void matchHorseRunner(MatchDao dao, TabRace tabRace, BetfairRace betRace, final int matchId) throws SQLException {
        List<BetfairRunner> betRunners = dao.findBetfairRunners(betRace.getRace_id());
        List<TabRunner> tabRunners = dao.findTabRunners(tabRace.getRace_id());
        if (betRunners != null && !betRunners.isEmpty() && tabRunners != null && !tabRunners.isEmpty()) {
            for (TabRunner tabRunner : tabRunners) {
                for (BetfairRunner betRunner : betRunners) {
                    MatchResult runnerMr = MatchUtil.matchRunner(tabRunner, betRunner);
                    if (runnerMr != null && runnerMr.isMatch()) {
                        insertMatchRacePlayer(matchId, tabRunner, betRunner, betRace.getRace_menu());
                        printRunnerInfo(tabRunner, betRunner);
                        break;
                    }
                }
            }
        }
    }

    private static void printRunnerInfo(TabRunner tabRunner, BetfairRunner betRunner) {
        System.out.println("Tab: Runner->" + tabRunner.getRunner_name() + " win :" + tabRunner.getRunner_win() + " place:" + tabRunner.getRunner_place());
        System.out.println("Bet: Runner->" + betRunner.getRunner_name() + " back:" + betRunner.getRunner_back() + " lay:" + betRunner.getRunner_lay());
    }

    private List<BetfairRunner> findBetfairRunners(final int raceId) {
        String sql = "select * from " + DBConstants.TABLE_BETFAILRUNNER + " where race_id=" + raceId;
        return (List<BetfairRunner>) DbUtil.getInstance().query(sql, new ISQLCallback() {

            public Object parseResultSet(ResultSet rs) throws Exception {
                List<BetfairRunner> trs = new ArrayList<BetfairRunner>();
                while (rs.next()) {
                    BetfairRunner tr = new BetfairRunner();
                    DbUtil.setFields(tr, rs);
                    trs.add(tr);
                }
                return trs;
            }
        });
    }

    private TabRunner findTabRunner(final String raceId, String runnerName) {
        String sql = "select * from " + DBConstants.TABLE_TABRUNNER + " where race_id=" + addSingleQuote(raceId) + " and runner_name = " + addSingleQuote(runnerName);
        return (TabRunner) DbUtil.getInstance().query(sql, new ISQLCallback() {

            public Object parseResultSet(ResultSet rs) throws Exception {
                if (rs.next()) {
                    TabRunner tr = new TabRunner();
                    DbUtil.setFields(tr, rs);
                    return tr;
                }
                return null;
            }
        });
    }

    private List<TabRunner> findTabRunners(final String raceId) {
        String sql = "select * from " + DBConstants.TABLE_TABRUNNER + " where race_id=" + addSingleQuote(raceId);
        return (List<TabRunner>) DbUtil.getInstance().query(sql, new ISQLCallback() {

            public Object parseResultSet(ResultSet rs) throws Exception {
                List<TabRunner> trs = new ArrayList<TabRunner>();
                while (rs.next()) {
                    TabRunner tr = new TabRunner();
                    DbUtil.setFields(tr, rs);
                    trs.add(tr);
                }
                return trs;
            }
        });
    }

    private static void printMatchedTabBetRace(TabRace tabRace, BetfairRace betRace) {
        System.out.println("----------------------Match----------------------");
        System.out.println("Tab race");
        System.out.println(tabRace);
        System.out.println("Bet race");
        System.out.println(betRace);
    }

    private static void insertMatchRacePlayer(final int matchId, final TabRunner tabRunner, final BetfairRunner betRunner, final String betfairMenu) throws SQLException {
        String sql = "insert into matchrunner (match_id,tab_race_id,tab_runner_name,tab_runner_id,tab_runner_win,tab_runner_place,bet_race_id,bet_runner_name,bet_runner_id,bet_runner_lay,bet_runner_back,bet_race_menu) values (?,?,?,?,?,?,?,?,?,?,?,?)";
        DbUtil.getInstance().executeInsert(sql, new IPrepareStatementBack() {

            public void setParameters(PreparedStatement ps) throws SQLException {
                int i = 0;
                ps.setInt(++i, matchId);
                ps.setString(++i, tabRunner.getRace_id());
                ps.setString(++i, tabRunner.getRunner_name());
                ps.setInt(++i, tabRunner.getRunner_id());
                ps.setDouble(++i, tabRunner.getRunner_win());
                ps.setDouble(++i, tabRunner.getRunner_place());
                ps.setInt(++i, betRunner.getRace_id());
                ps.setString(++i, betRunner.getRunner_name());
                ps.setInt(++i, betRunner.getRunner_id());
                ps.setDouble(++i, betRunner.getRunner_lay());
                ps.setDouble(++i, betRunner.getRunner_back());
                ps.setString(++i, betfairMenu);
            }
        });
    }

    private static int insertMatchRace(final TabRace tabRace, final BetfairRace betRace) throws SQLException {
        String sql = "insert into matchrace (tab_record_id,tab_race_id,tab_venue_name,tab_race_status,runner_number,tab_runners,bet_record_id,bet_runners,bet_race_id,bet_race_name,bet_race_menu,bet_race_status) values (?,?,?,?,?,?,?,?,?,?,?,?)";
        DbUtil.getInstance().executeInsert(sql, new IPrepareStatementBack() {

            public void setParameters(PreparedStatement ps) throws SQLException {
                int i = 0;
                ps.setInt(++i, tabRace.getId());
                ps.setString(++i, tabRace.getRace_id());
                ps.setString(++i, tabRace.getVenue_name());
                ps.setString(++i, tabRace.getStatus());
                ps.setInt(++i, tabRace.getRunners_num());
                ps.setString(++i, tabRace.getRunners());
                ps.setInt(++i, betRace.getId());
                ps.setString(++i, betRace.getRunners());
                ps.setInt(++i, betRace.getRace_id());
                ps.setString(++i, betRace.getRace_name());
                ps.setString(++i, betRace.getRace_menu());
                ps.setString(++i, betRace.getStatus());
            }
        });
        return (Integer) DbUtil.getInstance().query("SELECT  LAST_INSERT_ID()", new ISQLCallback() {

            public Object parseResultSet(ResultSet rs) throws Exception {
                if (rs.next()) {
                    return rs.getInt(1);
                }
                return -1;
            }
        });
    }

    private static int insertMatchSport(final TabSport tabModel, final BetfairSport betfairModel) throws SQLException {
        String sql = "insert into matchsport (sportname,tab_id,tab_league,tab_eventname,tab_subeventname,tab_starttime,tab_players,betfair_id,batfair_name,betfair_menu,betfair_starttime,betfair_players) values (?,?,?,?,?,?,?,?,?,?,?,?)";
        DbUtil.getInstance().executeInsert(sql, new IPrepareStatementBack() {

            public void setParameters(PreparedStatement ps) throws SQLException {
                int i = 0;
                ps.setString(++i, tabModel.getName());
                ps.setInt(++i, tabModel.getId());
                ps.setString(++i, tabModel.getLeaguename());
                ps.setString(++i, tabModel.getEventname());
                ps.setString(++i, tabModel.getSubeventname());
                ps.setTimestamp(++i, tabModel.getStarttime());
                ps.setString(++i, tabModel.getPlayers());
                ps.setInt(++i, betfairModel.getId());
                ps.setString(++i, betfairModel.getSport_name());
                ps.setString(++i, betfairModel.getSport_menu());
                ps.setTimestamp(++i, betfairModel.getStart_time());
                ps.setString(++i, betfairModel.getPlayers());
            }
        });
        return (Integer) DbUtil.getInstance().query("SELECT  LAST_INSERT_ID()", new ISQLCallback() {

            public Object parseResultSet(ResultSet rs) throws Exception {
                if (rs.next()) {
                    return rs.getInt(1);
                }
                return -1;
            }
        });
    }

    private static void insertMatchOdds(final int matchId, final TabOdds tabModel, final BetfairPlayer player, final TabSport tabSportModel, final BetfairSport betfairModel) throws SQLException {
        String sql = "insert into matchodds (match_id,tab_id,tab_player,tab_odds,tab_oddsurl,betfair_id,betfair_menu,betfair_player,betfair_back,betfair_lay,sportname, tab_league, tab_eventname, tab_subeventname, batfair_name) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        DbUtil.getInstance().executeInsert(sql, new IPrepareStatementBack() {

            public void setParameters(PreparedStatement ps) throws SQLException {
                int i = 0;
                ps.setInt(++i, matchId);
                ps.setInt(++i, tabModel.getId());
                ps.setString(++i, tabModel.getSide());
                ps.setDouble(++i, tabModel.getOdds());
                ps.setString(++i, tabModel.getOddsurl());
                ps.setInt(++i, player.getId());
                ps.setString(++i, betfairModel.getSport_menu());
                ps.setString(++i, player.getPlayer_name());
                ps.setDouble(++i, player.getPlayer_back());
                ps.setDouble(++i, player.getPlayer_lay());
                ps.setString(++i, tabModel.getName());
                ps.setString(++i, tabModel.getLeaguename());
                ps.setString(++i, tabModel.getEventname());
                ps.setString(++i, tabModel.getSubeventname());
                ps.setString(++i, betfairModel.getSport_name());
            }
        });
    }

    private static void printMatchOdds(BetfairPlayer player, TabOdds tabModel) {
        System.out.println(MessageFormat.format("Odds from betfair:{0} Back: {1}, Lay {2}", player.getPlayer_name(), player.getPlayer_back(), player.getPlayer_lay()));
        System.out.println(MessageFormat.format("Odds from Tab:    {0} Odds: {1}", tabModel.getSide().trim(), tabModel.getOdds()));
    }

    private static void printMatchSport(Object tabRecord, Object betfairRecord) {
        System.out.println(MessageFormat.format("{0}\n{1}", tabRecord, betfairRecord));
    }

    public List<TabOdds> findTabOdds(String sql) {
        return (List<TabOdds>) DbUtil.getInstance().query(sql, new ISQLCallback() {

            public Object parseResultSet(ResultSet rs) throws Exception {
                List<TabOdds> models = new ArrayList<TabOdds>();
                while (rs.next()) {
                    TabOdds model = new TabOdds();
                    DbUtil.setFields(model, rs);
                    models.add(model);
                }
                return models;
            }
        });
    }

    public List<BetfairPlayer> findBetfairPlayer(int sport_id) {
        return (List<BetfairPlayer>) DbUtil.getInstance().query("select * from " + DBConstants.TABLE_BETFAIRPLAYER + " where sport_id=" + sport_id, new ISQLCallback() {

            public Object parseResultSet(ResultSet rs) throws Exception {
                List<BetfairPlayer> playerModels = new ArrayList<BetfairPlayer>();
                while (rs.next()) {
                    BetfairPlayer player = new BetfairPlayer();
                    DbUtil.setFields(player, rs);
                    playerModels.add(player);
                }
                return playerModels;
            }
        });
    }

    /**
	 * Fetch data from: tabsport
	 * 
	 * @param sportName
	 * @return
	 */
    public List<TabSport> seleceTabSportRecords(String sportName) {
        String tabRecordsSql = MessageFormat.format("select * from {0} where name={1}", DBConstants.TABLE_TABSPORT, addSingleQuote(sportName));
        System.out.println(tabRecordsSql);
        return (List<TabSport>) DbUtil.getInstance().query(tabRecordsSql, new ISQLCallback() {

            public Object parseResultSet(ResultSet rs) throws Exception {
                List<TabSport> tabmodels = new ArrayList<TabSport>();
                while (rs.next()) {
                    TabSport model = new TabSport();
                    DbUtil.setFields(model, rs);
                    tabmodels.add(model);
                }
                return tabmodels;
            }
        });
    }

    /**
	 * Fetch all race from tab tables
	 */
    public List<TabRace> selectTabRaceRecords() {
        String tabHorseRace = MessageFormat.format("select * from {0}", DBConstants.TABLE_TABRACE);
        return (List<TabRace>) DbUtil.getInstance().query(tabHorseRace, new ISQLCallback() {

            public Object parseResultSet(ResultSet rs) throws Exception {
                List<TabRace> races = new ArrayList<TabRace>();
                while (rs.next()) {
                    TabRace race = new TabRace();
                    DbUtil.setFields(race, rs);
                    races.add(race);
                }
                return races;
            }
        });
    }

    public List<BetfairRace> selectBetfairRaceRecords() {
        String betfairRaceSQL = MessageFormat.format("select * from {0}", DBConstants.TABLE_BETFAIRRACE);
        return (List<BetfairRace>) DbUtil.getInstance().query(betfairRaceSQL, new ISQLCallback() {

            public Object parseResultSet(ResultSet rs) throws Exception {
                List<BetfairRace> list = new ArrayList<BetfairRace>();
                while (rs.next()) {
                    BetfairRace race = new BetfairRace();
                    DbUtil.setFields(race, rs);
                    list.add(race);
                }
                return list;
            }
        });
    }

    /**
	 * Fetch data from: betfairsupport
	 * 
	 * @param sportName
	 * @return
	 */
    public List<BetfairSport> findBetfairSports(String sportName) {
        String tabRecordsSql = MessageFormat.format("select * from {0} where sport_type={1}", DBConstants.TABLE_BETFAIRSPORT, addSingleQuote(sportName));
        System.out.println(tabRecordsSql);
        return (List<BetfairSport>) DbUtil.getInstance().query(tabRecordsSql, new ISQLCallback() {

            public Object parseResultSet(ResultSet rs) throws Exception {
                List<BetfairSport> models = new ArrayList<BetfairSport>();
                while (rs.next()) {
                    BetfairSport model = new BetfairSport();
                    DbUtil.setFields(model, rs);
                    models.add(model);
                }
                return models;
            }
        });
    }

    private String addSingleQuote(String sportName) {
        return "'" + sportName + "'";
    }
}
