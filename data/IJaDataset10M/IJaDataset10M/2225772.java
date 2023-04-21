package dsrwebserver.pages.dsr;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.sql.ResultSet;
import java.sql.SQLException;
import ssmith.image.CustomJPEG;
import dsr.models.map.AbstractMapModel;
import dsrwebserver.missions.AbstractMission;
import dsrwebserver.tables.GamesTable;
import dsrwebserver.tables.MapDataTable;
import dsrwebserver.tables.UnitHistoryTable;

public class MapImageForFinishedGame extends MapImageAbstract {

    public MapImageForFinishedGame() {
        super();
    }

    public void process() throws Exception {
        this.content_type = "image/jpeg";
        int gameid = conn.headers.getGetValueAsInt("gid");
        int turn_no = conn.headers.getGetValueAsInt("turn_no");
        int turn_side = conn.headers.getGetValueAsInt("turn_side");
        GamesTable game = new GamesTable(dbs);
        game.selectRow(gameid);
        if (game.getGameStatus() == GamesTable.GS_FINISHED) {
            if (turn_no == 0) {
                turn_no = game.getTurnNo();
            }
            AbstractMission mission = AbstractMission.Factory(game.getMissionType());
            int our_side = -1;
            if (this.session.isLoggedIn()) {
                if (game.isPlayerInGame(current_login.getLoginID())) {
                    our_side = game.getSideFromPlayerID(current_login.getLoginID());
                }
            }
            mid = MapDataTable.GetMapIDFromGameID(dbs, game.getID());
            Dimension d = MapDataTable.GetMapSize(dbs, mid, game.getID());
            int smallest_side = Math.min(d.width, d.height);
            sq_size = SIZE / smallest_side;
            int img_width = sq_size * d.width;
            int img_height = sq_size * d.height;
            jpg = new CustomJPEG(img_width, img_height);
            Graphics g = jpg.getGraphics();
            g.setColor(Color.black);
            g.fillRect(0, 0, img_width - 1, img_height - 1);
            if (mission.wall_type == AbstractMapModel.BLOCK_WALLS) {
                drawWallEdges(g);
            }
            String sql = "SELECT * FROM MapDataSquares WHERE MapDataID = " + mid + " And SquareType = " + MapDataTable.MT_FLOOR;
            ResultSet rs = dbs.getResultSet(sql);
            while (rs.next()) {
                super.setFloorColour(g, rs.getInt("FloorTex"), mission);
                if (rs.getInt("EscapeHatch") > 0) {
                    g.setColor(ESCAPE_HATCH_COL);
                }
                g.fillRect(rs.getInt("MapX") * sq_size, rs.getInt("MapY") * sq_size, sq_size, sq_size);
                if (rs.getInt("DoorType") > 0) {
                    g.setColor(Color.green);
                    if (rs.getInt("DoorType") == MapDataTable.DOOR_EW) {
                        g.fillRect((rs.getInt("MapX") * sq_size), (rs.getInt("MapY") * sq_size) + (sq_size / 3), sq_size, sq_size / 3);
                    } else if (rs.getInt("DoorType") == MapDataTable.DOOR_NS) {
                        g.fillRect((rs.getInt("MapX") * sq_size) + (sq_size / 3), rs.getInt("MapY") * sq_size, sq_size / 3, sq_size);
                    } else {
                        throw new RuntimeException("Unknown door type: " + rs.getInt("DoorType"));
                    }
                }
            }
            if (mission.wall_type == AbstractMapModel.SLIM_WALLS) {
                drawWalls(g, gameid, turn_no, turn_side);
            }
            this.drawComputers(g, our_side);
            if (turn_no == game.getTurnNo()) {
                drawFlagOrEggs(g, game);
            }
            drawExplosions(g, game, turn_no);
            drawUnits(g, game, our_side, turn_no, turn_side);
            this.content_length = jpg.generateDataAsJPEG().length;
        }
    }

    private void drawWalls(Graphics g, int gameid, int turn_no, int turn_side) throws SQLException {
        String sql = "SELECT * FROM MapDataSquares WHERE MapDataID = " + mid + " And SquareType = " + MapDataTable.MT_WALL;
        ResultSet rs = dbs.getResultSet(sql);
        g.setColor(Color.green.darker());
        while (rs.next()) {
            g.fillRect((rs.getInt("MapX") * sq_size), (rs.getInt("MapY") * sq_size), sq_size, sq_size);
        }
        rs.close();
        if (turn_no > 0 && turn_side > 0) {
            sql = "SELECT * FROM UnitHistory WHERE EventType = " + UnitHistoryTable.UH_WALL_DESTROYED + " AND GameID = " + gameid + " AND (TurnNo > " + turn_no + " OR (TurnNo = " + turn_no + " AND TurnSide >= " + turn_side + "))";
            rs = dbs.getResultSet(sql);
            g.setColor(Color.green.darker().darker());
            while (rs.next()) {
                if (rs.getInt("OriginalSquareType") == MapDataTable.MT_WALL) {
                    g.fillRect((rs.getInt("MapX") * sq_size), (rs.getInt("MapY") * sq_size), sq_size, sq_size);
                } else if (rs.getInt("OriginalSquareType") == MapDataTable.MT_FLOOR && rs.getInt("OriginalDoorType") > 0) {
                    if (rs.getInt("OriginalDoorType") == MapDataTable.DOOR_EW) {
                        g.fillRect((rs.getInt("MapX") * sq_size), (rs.getInt("MapY") * sq_size) + (sq_size / 3), sq_size, sq_size / 3);
                    } else if (rs.getInt("OriginalDoorType") == MapDataTable.DOOR_NS) {
                        g.fillRect((rs.getInt("MapX") * sq_size) + (sq_size / 3), rs.getInt("MapY") * sq_size, sq_size / 3, sq_size);
                    }
                }
            }
            rs.close();
        }
    }

    private void drawUnits(Graphics g, GamesTable game, int our_side, int turn_no, int turn_side) throws SQLException {
        ResultSet rs_units = dbs.getResultSet("SELECT * FROM Units WHERE GameID = " + game.getID() + " ORDER BY Side, OrderBy");
        while (rs_units.next()) {
            String sql = "SELECT * FROM UnitHistory";
            sql = sql + " WHERE GameID = " + game.getID();
            sql = sql + " AND UnitID = " + rs_units.getInt("UnitID");
            if (turn_no > 0 && turn_no < game.getTurnNo()) {
                sql = sql + " AND TurnNo <= " + turn_no;
            }
            sql = sql + " AND EventType IN (" + UnitHistoryTable.UH_UNIT_MOVEMENT + ", " + UnitHistoryTable.UH_UNIT_KILLED + ", " + UnitHistoryTable.UH_UNIT_DEPLOYED + ")";
            sql = sql + " ORDER BY TurnNo DESC, TurnSide DESC, EventTime DESC, DateCreated DESC";
            sql = sql + " LIMIT 1";
            ResultSet rs_hist = dbs.getResultSet(sql);
            if (rs_hist.next()) {
                g.setColor(GetColourForSide(rs_units.getInt("Side"), our_side, game.getNumOfSides()));
                if (rs_hist.getInt("EventType") == UnitHistoryTable.UH_UNIT_KILLED) {
                    g.drawOval(rs_hist.getInt("MapX") * sq_size, rs_hist.getInt("MapY") * sq_size, sq_size, sq_size);
                } else {
                    g.fillOval(rs_hist.getInt("MapX") * sq_size, rs_hist.getInt("MapY") * sq_size, sq_size, sq_size);
                    g.setColor(Color.yellow);
                    int sx = rs_hist.getInt("MapX") * sq_size + (sq_size / 2);
                    int sy = rs_hist.getInt("MapY") * sq_size + (sq_size / 2);
                    g.drawLine(sx, sy, sx + GetXOffSetFromAngle(rs_hist.getInt("Angle"), sq_size), sy + GetYOffSetFromAngle(rs_hist.getInt("Angle"), sq_size));
                }
                g.setColor(Color.black);
                g.drawString("" + rs_units.getInt("OrderBy"), (rs_hist.getInt("MapX") * sq_size) + 2, (rs_hist.getInt("MapY") * sq_size) + 12);
                g.drawString("" + rs_units.getInt("OrderBy"), (rs_hist.getInt("MapX") * sq_size) + 4, (rs_hist.getInt("MapY") * sq_size) + 12);
                g.drawString("" + rs_units.getInt("OrderBy"), (rs_hist.getInt("MapX") * sq_size) + 3, (rs_hist.getInt("MapY") * sq_size) + 11);
                g.drawString("" + rs_units.getInt("OrderBy"), (rs_hist.getInt("MapX") * sq_size) + 3, (rs_hist.getInt("MapY") * sq_size) + 13);
                g.setColor(Color.white);
                int x = (rs_hist.getInt("MapX") * sq_size) + 3;
                int y = (rs_hist.getInt("MapY") * sq_size) + 12;
                g.drawString("" + rs_units.getInt("OrderBy"), x, y);
            }
        }
    }
}
