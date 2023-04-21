package ch.epfl.lbd.etl.extractors;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import ch.epfl.lbd.database.connection.DatabaseConnection;
import ch.epfl.lbd.database.connection.exception.DatabaseConnectionException;
import ch.epfl.lbd.database.objects.RelationalTable;
import ch.epfl.lbd.database.providers.oracle.connection.OracleConnection;
import ch.epfl.lbd.database.providers.postgresql.connection.PostgreSqlConnection;
import ch.epfl.lbd.database.spatial.GPSPoint;
import ch.epfl.lbd.etl.ETLProcedure;
import ch.epfl.lbd.trajectories.Episode;
import ch.epfl.lbd.trajectories.Move;
import ch.epfl.lbd.trajectories.MovingObject;
import ch.epfl.lbd.trajectories.Stop;
import ch.epfl.lbd.trajectories.Trajectory;
import org.junit.Test;

public class TrajectoryExtractor extends ETLProcedure {

    protected ArrayList<Trajectory> trajectories;

    protected ArrayList<Episode> episodes;

    private DatabaseConnection sourceConnection;

    private DatabaseConnection destinationConnection;

    @Override
    public void run() {
        try {
            sourceConnection = new OracleConnection("connections.properties", "connection1");
            sourceConnection.openConnection();
            destinationConnection = new PostgreSqlConnection("connections.properties", "connection4");
            destinationConnection.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DatabaseConnectionException e) {
            e.printStackTrace();
        }
        String[] trj_columns = { "ID", "START", "END", "MOVING_ENTITY", "TYPE", "AVG_SPEED", "AVG_TRAVEL_TIME", "GROUP_ID", "geom" };
        Integer[] trj_types = { RelationalTable.INTEGER, RelationalTable.TIMESTAMP, RelationalTable.TIMESTAMP, RelationalTable.INTEGER, RelationalTable.VARCHAR, RelationalTable.DOUBLE, RelationalTable.DOUBLE, RelationalTable.INTEGER, RelationalTable.LINESTRING };
        String trj_tableName = "MILAN_TRAJECTORIES";
        RelationalTable trj_table = new RelationalTable(trj_tableName, trj_columns);
        trj_table.setTypes(trj_types);
        trj_table.setForced(true);
        trj_table.loadObject(destinationConnection);
        String[] epi_columns = { "ID", "START", "END", "MOVING_ENTITY", "TYPE", "AVG_SPEED", "VAR_SPEED", "TRJ_ID", "geom" };
        Integer[] epi_types = { RelationalTable.INTEGER, RelationalTable.TIMESTAMP, RelationalTable.TIMESTAMP, RelationalTable.INTEGER, RelationalTable.VARCHAR, RelationalTable.DOUBLE, RelationalTable.DOUBLE, RelationalTable.INTEGER, RelationalTable.LINESTRING };
        String epi_tableName = "MILAN_EPISODES";
        RelationalTable epi_table = new RelationalTable(epi_tableName, epi_columns);
        epi_table.setTypes(epi_types);
        epi_table.setForced(true);
        epi_table.loadObject(destinationConnection);
        try {
            String query = "SELECT * " + "	FROM milan_gmd3" + "	order by userid, datetime";
            ResultSet results = sourceConnection.getSQLQueryResults(query);
            int i = 0;
            int trj_id = 1;
            int eps = 1;
            GPSPoint lastPt = null;
            GPSPoint pt = null;
            Trajectory trj = new Trajectory();
            while (results.next()) {
                Episode newEpisode;
                String tag = results.getString("tag");
                double lat = results.getDouble("longitude");
                double lng = results.getDouble("latitude");
                double northing = results.getDouble("northing");
                double easting = results.getDouble("easting");
                int userid = results.getInt("userid");
                Timestamp datetime = results.getTimestamp("datetime");
                String name = "";
                String desc = "";
                if (tag.equalsIgnoreCase("B")) {
                    if (trj.getLastEpisode() != null) {
                        Object[] values = null;
                        values = new Object[9];
                        values[0] = (Integer) trj_id;
                        values[1] = trj.getLifeSpan().getStart();
                        values[2] = trj.getLifeSpan().getEnd();
                        values[3] = trj.getMovingObject().getId();
                        values[4] = "";
                        values[5] = trj.getAvgSpeed();
                        values[6] = trj.getAvgTravelTime();
                        values[7] = 0;
                        values[8] = trj.getGeometry();
                        if (values[8] != null) {
                            trj_table.insertRow(values);
                            ArrayList<Episode> episodes = trj.getEpisodes();
                            for (int j = 0; j < episodes.size(); j++) {
                                Episode episode = episodes.get(j);
                                Object[] vals = new Object[9];
                                String type = "";
                                if (episode instanceof Move) type = "MOVE"; else type = "STOP";
                                vals[0] = (Integer) eps;
                                vals[1] = episode.getLifeSpan().getStart();
                                vals[2] = episode.getLifeSpan().getEnd();
                                vals[3] = trj.getMovingObject().getId();
                                vals[4] = type;
                                vals[5] = episode.getAvgSpeed();
                                vals[6] = episode.getVarSpeed();
                                vals[7] = (Integer) trj_id;
                                vals[8] = episode.getGeometry();
                                epi_table.insertRow(vals);
                                eps++;
                            }
                            logger.info(trj_id + " trajectories inserted");
                            trj_id++;
                        }
                    }
                    trj = new Trajectory();
                    trj.setMovingObject(new MovingObject(userid));
                }
                if (pt != null) lastPt = pt;
                pt = new GPSPoint(lat, lng, northing, easting, userid, datetime, name, desc);
                pt.comparePrePoint(lastPt);
                Episode lastEpisode = trj.getLastEpisode();
                if (tag.equalsIgnoreCase("B") || tag.equalsIgnoreCase("S")) {
                    if (lastEpisode != null && lastEpisode instanceof Move) {
                        lastEpisode.merge(new Move(pt));
                    }
                    newEpisode = new Stop(pt);
                } else {
                    if (lastEpisode != null && lastEpisode instanceof Stop) {
                        newEpisode = new Move(lastPt);
                        newEpisode.merge(new Move(pt));
                    } else newEpisode = new Move(pt);
                }
                trj.addEpisode(newEpisode);
                i++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        trj_table.releaseObject();
        epi_table.releaseObject();
        try {
            sourceConnection.closeConnection();
            destinationConnection.closeConnection();
        } catch (DatabaseConnectionException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testOneTrajectory() throws Exception {
        PostgreSqlConnection conn = new PostgreSqlConnection("connections.properties", "connection4");
        conn.openConnection();
        String query = "SELECT * " + "	FROM milan_gmd3" + "	WHERE userid = 288437 " + "		AND date_trunc('day', datetime) = date('2007-04-05') " + "		AND id < 1100000" + "	ORDER BY datetime  " + "	LIMIT 200";
        ResultSet results = conn.getSQLQueryResults(query);
        Trajectory trj = new Trajectory();
        GPSPoint lastPt = null;
        GPSPoint pt = null;
        while (results.next()) {
            Episode newEpisode;
            String tag = results.getString("tag");
            double lat = results.getDouble("latitude");
            double lng = results.getDouble("longitude");
            double northing = results.getDouble("northing");
            double easting = results.getDouble("easting");
            int userid = results.getInt("userid");
            Timestamp datetime = results.getTimestamp("datetime");
            String name = "";
            String desc = "";
            if (pt != null) lastPt = pt;
            pt = new GPSPoint(lat, lng, northing, easting, userid, datetime, name, desc);
            pt.comparePrePoint(lastPt);
            Episode lastEpisode = trj.getLastEpisode();
            if (tag.equalsIgnoreCase("B") || tag.equalsIgnoreCase("S")) {
                if (lastEpisode != null && lastEpisode instanceof Move) {
                    lastEpisode.merge(new Move(pt));
                }
                newEpisode = new Stop(pt);
            } else {
                if (lastEpisode != null && lastEpisode instanceof Stop) {
                    newEpisode = new Move(lastPt);
                    newEpisode.merge(new Move(pt));
                } else newEpisode = new Move(pt);
            }
            trj.addEpisode(newEpisode);
        }
        logger.info("MOVES");
        ArrayList<Move> moves = trj.getMoves();
        for (Move move : moves) logger.info(move.getAvgSpeed());
        logger.info("STOPS");
        ArrayList<Stop> stops = trj.getStops();
        for (Stop stop : stops) logger.info(stop.getAvgSpeed());
        conn.closeConnection();
    }
}
