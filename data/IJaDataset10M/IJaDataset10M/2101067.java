package dsrwebserver.pages.dsr;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import ssmith.dbs.SQLFuncs;
import ssmith.html.HTMLFunctions;
import ssmith.util.Interval;
import dsrwebserver.DSRWebServer;
import dsrwebserver.components.MainLayout;
import dsrwebserver.missions.AbstractMission;
import dsrwebserver.pages.AbstractHTMLPage;
import dsrwebserver.tables.EquipmentTypesTable;
import dsrwebserver.tables.GamesTable;

public class miscstatspage extends AbstractHTMLPage {

    private static final int DAYS = 180;

    private static StringBuffer str_league, str_weps;

    private static Interval update_forum_interval = new Interval(1000 * 60 * 10, true);

    private String cutoff;

    public miscstatspage() {
        super();
    }

    @Override
    public void process() throws Exception {
        StringBuffer str = new StringBuffer();
        if (update_forum_interval.hitInterval() || str_league == null) {
            str_league = new StringBuffer();
            str_weps = new StringBuffer();
            HTMLFunctions.Heading(str_league, 5, "Only games from the last " + DAYS + " days are included.");
            Calendar c = Calendar.getInstance();
            c.add(Calendar.DAY_OF_MONTH, -DAYS);
            cutoff = SQLFuncs.d2sql(c, false);
            try {
                ResultSet rs = dbs.getResultSet("SELECT Mission, Count(*) as X FROM Games WHERE DateTurnStarted >= " + cutoff + " GROUP BY Mission ORDER BY X DESC LIMIT 10");
                HTMLFunctions.Heading(str_league, 3, "Top 10 Most Popular Missions (including current games)");
                HTMLFunctions.StartOrderedList(str_league);
                while (rs.next()) {
                    if (rs.getInt("X") > 0) {
                        HTMLFunctions.AddListEntry(str_league, AbstractMission.GetMissionNameFromType(rs.getInt("Mission"), true, false) + " (" + rs.getInt("X") + ")");
                    }
                }
                rs.close();
                HTMLFunctions.EndOrderedList(str_league);
            } catch (Exception ex) {
                DSRWebServer.HandleError(ex, true);
            }
            try {
                String sql = "SELECT Equipment.EquipmentTypeID, Count(*) AS X from Equipment ";
                sql = sql + " INNER JOIN EquipmentTypes ON EquipmentTypes.EquipmentTypeID = Equipment.EquipmentTypeID ";
                sql = sql + " WHERE Equipment.DateCreated >= " + cutoff;
                sql = sql + " AND EquipmentTypes.MajorTypeID IN (1, 2, 3, 4, 5, 9, 10, 21, 22)";
                sql = sql + " GROUP BY Equipment.EquipmentTypeID ORDER BY X DESC";
                ResultSet rs = dbs.getResultSet(sql);
                HTMLFunctions.Heading(str_weps, 3, "Most Popular Weapons");
                HTMLFunctions.StartOrderedList(str_weps);
                while (rs.next()) {
                    HTMLFunctions.AddListEntry(str_weps, EquipmentTypesTable.GetNameFromEquipmentType(dbs, rs.getInt("EquipmentTypeID")) + " (" + rs.getInt("X") + ")");
                }
                rs.close();
                HTMLFunctions.EndOrderedList(str_weps);
                miscstatspage.str_league.append(miscstatspage.str_weps);
            } catch (Exception ex) {
                DSRWebServer.HandleError(ex, true);
            }
            HTMLFunctions.Heading(str_league, 3, "Missions Stats");
            HTMLFunctions.StartTable(str_league, "stats", "", 1, "", 5);
            HTMLFunctions.StartRow(str_league);
            HTMLFunctions.AddCellHeading(str_league, "Name");
            HTMLFunctions.AddCellHeading(str_league, "Total Games");
            HTMLFunctions.AddCellHeading(str_league, "Side 1 wins");
            HTMLFunctions.AddCellHeading(str_league, "Side 2 wins");
            HTMLFunctions.AddCellHeading(str_league, "Side 3 wins");
            HTMLFunctions.AddCellHeading(str_league, "Side 4 wins");
            HTMLFunctions.AddCellHeading(str_league, "Draws");
            HTMLFunctions.AddCellHeading(str_league, "Avg Turns");
            HTMLFunctions.AddCellHeading(str_league, "Notes");
            HTMLFunctions.EndRow(str_league);
            StringBuffer str_unplayed_missions = new StringBuffer();
            for (int j = 0; j < AbstractMission.order.length; j++) {
                int i = AbstractMission.order[j];
                if (AbstractMission.IsValidMission(i)) {
                    showStatsForMission(str_league, i, str_unplayed_missions);
                }
            }
            HTMLFunctions.EndTable(str_league);
            if (this.session.isLoggedIn()) {
                if (this.current_login.isAdmin()) {
                    HTMLFunctions.Heading(str_league, 3, "Missions Not Played (in last " + DAYS + " days)");
                    HTMLFunctions.StartOrderedList(str_league);
                    str_league.append(str_unplayed_missions);
                    HTMLFunctions.EndOrderedList(str_league);
                }
            }
        }
        str.append(str_league);
        this.body_html.append(MainLayout.GetHTML(this, "Miscellaneous Statistics", str));
    }

    private void showStatsForMission(StringBuffer str, int missionid, StringBuffer str_unplayed_missions) throws SQLException {
        AbstractMission mission = AbstractMission.Factory(missionid);
        int s1 = dbs.getScalarAsInt("SELECT Count(*) FROM Games WHERE DateTurnStarted >= " + cutoff + " AND Mission = " + missionid + " AND WinningSide = 1");
        int s2 = dbs.getScalarAsInt("SELECT Count(*) FROM Games WHERE DateTurnStarted >= " + cutoff + " AND Mission = " + missionid + " AND WinningSide = 2");
        int s3 = dbs.getScalarAsInt("SELECT Count(*) FROM Games WHERE DateTurnStarted >= " + cutoff + " AND Mission = " + missionid + " AND WinningSide = 3");
        int s4 = dbs.getScalarAsInt("SELECT Count(*) FROM Games WHERE DateTurnStarted >= " + cutoff + " AND Mission = " + missionid + " AND WinningSide = 4");
        int draws = dbs.getScalarAsInt("SELECT Count(*) FROM Games WHERE DateTurnStarted >= " + cutoff + " AND Mission = " + missionid + " AND WinType IN (" + GamesTable.WIN_DRAW + ", " + GamesTable.WIN_DRAW_MUTUAL_CONCEDE + ")");
        int tot = s1 + s2 + s3 + s4 + draws;
        int at = dbs.getScalarAsInt("SELECT AVG(TurnNo) FROM Games WHERE DateTurnStarted >= " + cutoff + " AND Mission = " + missionid + " AND COALESCE(WinningSide, 0) <> 0");
        if (tot > 0) {
            HTMLFunctions.StartRow(str);
            HTMLFunctions.AddCell(str, "<nobr>" + AbstractMission.GetMissionNameFromType(missionid, true, false) + "</nobr>");
            HTMLFunctions.AddCell(str, tot + "");
            if (tot == 0) {
                tot = 1;
            }
            int tot_s1 = ((s1 * 100) / tot);
            int tot_s2 = ((s2 * 100) / tot);
            HTMLFunctions.AddCell(str, "<nobr>" + s1 + " (" + tot_s1 + "%)</nobr>");
            HTMLFunctions.AddCell(str, "<nobr>" + s2 + " (" + tot_s2 + "%)</nobr>");
            if (mission.getNumOfSides() >= 3) {
                HTMLFunctions.AddCell(str, "<nobr>" + s3 + " (" + ((s3 * 100) / tot) + "%)</nobr>");
            } else {
                HTMLFunctions.AddCell(str, "");
            }
            if (mission.getNumOfSides() >= 4) {
                HTMLFunctions.AddCell(str, "<nobr>" + s4 + " (" + ((s4 * 100) / tot) + "%)</nobr>");
            } else {
                HTMLFunctions.AddCell(str, "");
            }
            HTMLFunctions.AddCell(str, "<nobr>" + draws + " (" + ((draws * 100) / tot) + "%)</nobr>");
            HTMLFunctions.AddCell(str, "" + at);
            String notes = "";
            if (tot >= 3 && (tot_s1 < 20 || tot_s2 < 20)) {
                notes = "Unbalanced?";
            }
            HTMLFunctions.AddCell(str, notes);
            HTMLFunctions.EndRow(str);
        } else {
            HTMLFunctions.AddListEntry(str_unplayed_missions, mission.mission_name);
        }
    }
}
