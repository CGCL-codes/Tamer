package dsrwebserver.tables;

import java.sql.SQLException;
import ssmith.dbs.MySQLConnection;
import dsrwebserver.DSRWebServer;

public class VisibleEnemiesTable {

    public static final int VT_SEEN = 0;

    public static final int VT_HEARD_MOVING = 1;

    public static final int VT_HEARD_SHOOTING = 2;

    private VisibleEnemiesTable() {
    }

    public static void CreateTable(MySQLConnection dbs) throws SQLException {
        if (dbs.doesTableExist("VisibleEnemies") == false) {
            dbs.runSQL("CREATE TABLE VisibleEnemies (VisibleEnemyID INTEGER AUTO_INCREMENT KEY, GameID INTEGER, TurnNo INTEGER, PhaseNo INTEGER, UnitID INTEGER, Side TINYINT, MapX SMALLINT, MapY SMALLINT, SeenBySide TINYINT, Angle SMALLINT, DateCreated TIMESTAMP DEFAULT CURRENT_TIMESTAMP )");
        }
        VisibleEnemiesTable.DeleteOldRecs(dbs);
    }

    public static void DeleteOldRecs(MySQLConnection dbs) throws SQLException {
        dbs.runSQLDelete("DELETE FROM VisibleEnemies WHERE GameID IN (SELECT GameID FROM Games WHERE GameStatus = " + GamesTable.GS_FINISHED + ")");
    }

    public static void AddRec(MySQLConnection dbs, GamesTable game, UnitsTable unit_seen, int loginid_log, int seen_by_side, int vis_type, long event_time) throws SQLException {
        try {
            if (dbs.getScalarAsInt("SELECT Count(*) FROM VisibleEnemies WHERE GameID = " + game.getID() + " AND TurnNo = " + game.getTurnNo() + " AND PhaseNo = " + game.getPhaseNo() + " AND UnitID = " + unit_seen.getID() + " AND MapX = " + unit_seen.getMapX() + " AND MapY = " + unit_seen.getMapY() + " AND Angle = " + unit_seen.getAngle() + " AND SeenBySide = " + seen_by_side + " AND VisibleType = " + vis_type) == 0) {
                if (vis_type == VT_HEARD_SHOOTING) {
                    GameLogTable.AddRec(dbs, game, loginid_log, -1, "An enemy unit has been heard shooting!", false, false, event_time);
                } else {
                    if (dbs.getScalarAsInt("SELECT Count(*) FROM VisibleEnemies WHERE GameID = " + game.getID() + " AND TurnNo = " + game.getTurnNo() + " AND PhaseNo = " + game.getPhaseNo() + " AND UnitID = " + unit_seen.getID() + "  AND SeenBySide = " + seen_by_side) == 0) {
                        if (vis_type == VT_SEEN) {
                        } else if (vis_type == VT_HEARD_MOVING) {
                        }
                    }
                }
                dbs.RunIdentityInsert("INSERT INTO VisibleEnemies (GameID, TurnNo, PhaseNo, UnitID, Side, MapX, MapY, Angle, SeenBySide, VisibleType) VALUES (" + game.getID() + ", " + game.getTurnNo() + ", " + game.getPhaseNo() + ", " + unit_seen.getID() + ", " + unit_seen.getSide() + ", " + unit_seen.getMapX() + ", " + unit_seen.getMapY() + ", " + unit_seen.getAngle() + ", " + seen_by_side + ", " + vis_type + ")");
            }
        } catch (Exception ex) {
            DSRWebServer.HandleError(ex, true);
        }
    }
}
