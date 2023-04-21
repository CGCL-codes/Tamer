package dsr.playback;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import com.mysql.jdbc.ResultSet;
import ssmith.dbs.MySQLConnection;
import dsrwebserver.pages.appletcomm.PlaybackComms;
import dsrwebserver.tables.GamesTable;

public class ClientTurnDataCommFunctions {

    public static String GetTurnDataRequest(int game_id, int login_id, int turn) {
        return "cmd=" + PlaybackComms.GET_TURN_DATA + "&gid=" + game_id + "&lid=" + login_id + "&turn=" + turn;
    }

    public static String GetTurnDataResponse(MySQLConnection dbs, HashMap<String, String> hashmap) throws SQLException {
        StringBuffer str = new StringBuffer();
        int gameid = Integer.parseInt(hashmap.get("gid"));
        int loginid = Integer.parseInt(hashmap.get("lid"));
        int turn_no = Integer.parseInt(hashmap.get("turn"));
        str.append(gameid + "|" + turn_no + "|");
        GamesTable game = new GamesTable(dbs);
        game.selectRow(gameid);
        int side = 0;
        if (game.getGameStatus() == GamesTable.GS_STARTED) {
            if (game.isPlayerInGame(loginid)) {
                side = game.getSideFromPlayerID(loginid);
            } else {
                return null;
            }
        }
        String sql = "SELECT * FROM UnitHistory ";
        sql = sql + " WHERE GameID = " + gameid + " AND TurnNo = " + turn_no;
        if (side > 0) {
            sql = sql + " AND SeenBySide" + side + " = 1";
        }
        sql = sql + " ORDER BY TurnNo, EventTime, DateCreated";
        ResultSet rs = dbs.getResultSet(sql);
        while (rs.next()) {
            str.append(rs.getInt("UnitID") + "|" + rs.getInt("Status") + "|");
            str.append(rs.getInt("MapX") + "|" + rs.getInt("MapY") + "|");
            str.append(rs.getInt("Angle") + "|" + rs.getInt("EventType") + "|");
            str.append(rs.getInt("Radius") + "|" + rs.getInt("OriginalSquareType") + "|");
            str.append(rs.getInt("TurnNo") + "|");
        }
        return str.toString();
    }

    public static ArrayList<ClientPlaybackTurnData> DecodeResponse(String response) {
        String data[] = response.split("\\|");
        ArrayList<ClientPlaybackTurnData> al_data = new ArrayList<ClientPlaybackTurnData>();
        if (data.length > 2) {
            int cell = 2;
            while (true) {
                ClientPlaybackTurnData turn_data = new ClientPlaybackTurnData();
                turn_data.unit_id = Integer.parseInt(data[cell]);
                turn_data.status = Byte.parseByte(data[cell + 1]);
                turn_data.mapx = Byte.parseByte(data[cell + 2]);
                turn_data.mapy = Byte.parseByte(data[cell + 3]);
                turn_data.angle = Integer.parseInt(data[cell + 4]);
                turn_data.event_type = Byte.parseByte(data[cell + 5]);
                turn_data.rad = Byte.parseByte(data[cell + 6]);
                turn_data.orig_sq_type = Byte.parseByte(data[cell + 7]);
                turn_data.turn_no = Byte.parseByte(data[cell + 8]);
                cell += 9;
                al_data.add(turn_data);
                if (cell >= data.length - 6) {
                    break;
                }
            }
        }
        return al_data;
    }
}
