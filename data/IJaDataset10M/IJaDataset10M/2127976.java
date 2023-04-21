package org.grailrtls.server;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.concurrent.*;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;

/**
 * The DatabaseManager is in charge of all interactions between the server
 * and the database.  It performs common inserts, such as samples, packets, and locations,
 * in batches after loading them into an internal queue.  Less frequent queries,
 * such as regions, hubs, and antennas, are executed immediately to reduce server
 * delays.
 * 
 * @author Robert S. Moore II
 * 
 */
public final class DatabaseManager extends Thread {

    /**
	 * The number of milliseconds to wait while inserting a query string into
	 * the queue.
	 */
    public static final int QUEUE_INSERT_TIMEOUT = 200;

    /**
	 * The maximum number of milliseconds that this {@code DatabaseManager} will
	 * wait before checking the queue for data.
	 */
    protected static final int QUEUE_POLL_INTERVAL = 1000;

    /**
	 * The value of a point in the "location_types" table of the database.
	 */
    private static final int LOCATION_TYPE_POINT = 0;

    /**
	 * The value of a rectangle in the "location_types" table of the database.
	 */
    private static final int LOCATION_TYPE_RECTANGLE = 1;

    /**
	 * The value of an ellipse in the "location_types" table of the database.
	 */
    private static final int LOCATION_TYPE_ELLIPSE = 2;

    /**
	 * The value of the M1 localization algorithm in the "algorithms" table of the
	 * database.
	 */
    private static final int ALGORITHM_M1 = 0;

    /**
	 * The value of the M2 localization algorithm in the "algorithms" table of the
	 * database.
	 */
    private static final int ALGORITHM_M2 = 1;

    /**
	 * The value of the M3 localization algorithm in the "algorithms" table of the
	 * database.
	 */
    private static final int ALGORITHM_M3 = 2;

    /**
	 * The value of the k-Nearest-Neighbors localization algorithm in the "algorithms" table of the
	 * database.
	 */
    private static final int ALGORITHM_KNN = 3;

    /**
	 * The value of the Simple Point Matching localization algorithm in the "algorithms" table of the
	 * database.
	 */
    private static final int ALGORITHM_SPM = 4;

    /**
	 * The value of the Area-Based Probability localization algorithm in the "algorithms" table of the
	 * database.
	 */
    private static final int ALGORITHM_ABP = 5;

    /**
	 * The value of the "LLS" localization algorithm in the "algorithms" table of the
	 * database.
	 */
    private static final int ALGORITHM_LLS = 6;

    /**
	 * The value of the "NLS" localization algorithm in the "algorithms" table of the
	 * database.
	 */
    private static final int ALGORITHM_NLS = 7;

    /**
	 * The value of the "SL" localization algorithm in the "algorithms" table of the
	 * database.
	 */
    private static final int ALGORITHM_SL = 8;

    /**
	 * The value of the "KNL" localization algorithm in the "algorithms" table of the
	 * database.
	 */
    private static final int ALGORITHM_KNL = 9;

    /**
	 * The value of the mean-based fingerprinting function in the "fingerprint_functions" table
	 * of the database.
	 */
    private static final int FF_ALG_MEAN = 0;

    /**
	 * The value of the median-based fingerprinting function in the "fingerprint_functions" table
	 * of the database.
	 */
    private static final int FF_ALG_MEDIAN = 1;

    /**
	 * The value of the variance-based fingerprinting function in the "fingerprint_functions" table
	 * of the database.
	 */
    private static final int FF_ALG_STDDEV = 2;

    /**
	 * The value of the time- and mean-based fingerprinting function in the "fingerprint_functions" table
	 * of the database.
	 */
    private static final int FF_ALG_TIME_MEAN = 3;

    /**
	 * The value of the time- and median-based fingerprinting function in the "fingerprint_functions" table
	 * of the database.
	 */
    private static final int FF_ALG_TIME_MEDIAN = 4;

    /**
	 * The value of the time- and variance-based fingerprinting function in the "fingerprint_functions" table
	 * of the database.
	 */
    private static final int FF_ALG_TIME_STDDEV = 5;

    /**
	 * Human-readable representations of the various fingerprint functions in the database.
	 */
    private static final String[] FF_ALG_STRINGS = { "Mean", "Median", "Variance", "Time-Mean", "Time-Median", "Time-Variance" };

    /**
	 * The connection to the database.
	 */
    private Connection connection;

    /**
	 * Executes statements on the database.
	 */
    private Statement statement;

    /**
	 * The hostname of the server running the database.
	 */
    final String hostname;

    /**
	 * The name of the database used for storing GRAIL data.
	 */
    final String database_name;

    /**
	 * The username in the database.
	 */
    final String username;

    /**
	 * The password in the database.
	 */
    final String password;

    /**
	 * The reference to the server.&nbsp; Used for logging.
	 */
    private final LocalizationServer server;

    /**
	 * Contains the strings used for making SQL statements.
	 */
    private final BlockingQueue<String> queue = new LinkedBlockingQueue<String>();

    /**
	 * A queue of {@code Region}s that need to be updated/inserted into the
	 * database.
	 */
    private final ConcurrentLinkedQueue<Region> region_queue = new ConcurrentLinkedQueue<Region>();

    /**
	 * Flag to indicate if the DatabaseManager should exit gracefully.
	 */
    private boolean keep_running = true;

    /**
	 * Formatter for inserting timestamps into the database.
	 */
    public final SimpleDateFormat date_formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
	 * Creates a new <code>DatabaseManager</code> with the specified
	 * arguments.&nbsp; If any exceptions are generated, they will be thrown to
	 * the invoking method and the object will not be instantiated.&nbsp; Any
	 * arguments passed as <code>null</code> are likely to generate
	 * exceptions.
	 * 
	 * @param hostname
	 *            the hostname of the server running the database
	 * @param database_name
	 *            the name of the database in which GRAIL data is stored
	 * @param username
	 *            the username to access the database
	 * @param password
	 *            the password to access the database
	 * @param server
	 *            connection to the server for logging
	 * @throws ClassNotFoundException
	 *             if the correct JDBC driver cannot be loaded
	 * @throws SQLException
	 */
    public DatabaseManager(final String hostname, final String database_name, final String username, final String password, final LocalizationServer server) throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        this.connection = DriverManager.getConnection("jdbc:postgresql://" + hostname + "/" + database_name, username, password);
        this.connection.setAutoCommit(false);
        this.statement = this.connection.createStatement();
        this.hostname = hostname;
        this.database_name = database_name;
        this.username = username;
        this.password = password;
        this.server = server;
    }

    @Override
    public void run() {
        while (this.keep_running) {
            synchronized (this.queue) {
                try {
                    this.queue.wait(DatabaseManager.QUEUE_POLL_INTERVAL);
                } catch (InterruptedException _) {
                }
            }
            while (!this.region_queue.isEmpty()) {
                Region region = this.region_queue.poll();
                if (!this.insert(region)) break;
            }
            if (!this.queue.isEmpty()) {
                try {
                    if (checkConnection()) {
                        this.server.io.logInfo("Processing " + this.queue.size() + " batch inserts.");
                        synchronized (this.queue) {
                            for (final Iterator<String> query = this.queue.iterator(); query.hasNext(); ) {
                                this.statement.addBatch(query.next());
                                query.remove();
                            }
                        }
                        if (!executeStatements()) this.server.io.logError("Could not execute SQL statements.  Check connection?");
                    } else {
                        this.server.io.logWarning("Could not connect to the database.");
                    }
                } catch (SQLException sqle) {
                    this.server.io.logError(sqle.toString());
                    this.server.io.logError(sqle.getNextException().toString());
                }
            }
        }
    }

    public boolean insert(final Sample sample) {
        return true;
    }

    /**
	 * Inserts a {@link Fingerprint} into the database for a given
	 * {@link Region}.
	 * 
	 * @param fingerprint
	 *            the <code>Fingerprint</code> to insert
	 * @param region
	 *            the <code>Region</code> for which to insert the
	 *            <code>Fingerprint</code>
	 * @return <code>true</code> if the insert succeeds for all landmarks,
	 *         else <code>false</code>
	 */
    public synchronized boolean insert(final Fingerprint fingerprint, final Region region) {
        if (region.database_id == -1) {
            if (!insert(region)) {
                server.io.logWarning("Could not insert fingerprint " + fingerprint.toString() + " because region " + region.name + " has no database ID.");
                return false;
            }
        }
        final long now = System.currentTimeMillis();
        String formatted_timestamp = date_formatter.format(now);
        int algorithm_id = FingerprintFunction.getType(fingerprint.fingerprint_function);
        if (algorithm_id == -1) {
            this.server.io.logWarning("Unknown fingerprint function type.  Cannot insert fingerprint into the DB.");
            return false;
        }
        StringBuffer query = new StringBuffer();
        query.append("INSERT INTO fingerprints (mac_id,phy,region_id,timestamp,algorithm,hashcode) VALUES (");
        query.append(fingerprint.getTransmitter().mac_address.toLong()).append(',');
        query.append(fingerprint.getTransmitter().physical_layer_type).append(',');
        query.append(region.database_id).append(",'");
        query.append(formatted_timestamp).append("',");
        query.append(FingerprintFunction.getType(fingerprint.fingerprint_function));
        query.append(",").append(fingerprint.hashcode).append(")");
        boolean success = true;
        try {
            this.statement.executeUpdate(query.toString());
            this.connection.commit();
        } catch (SQLException sqle) {
            success = false;
            this.server.io.logWarning("Failed to insert fingerprint into the DB.");
            this.server.io.logWarning(sqle.toString());
        }
        if (success) {
            Map<Landmark, List<Sample>> landmarks_to_samples = fingerprint.getOriginalSamples();
            for (Landmark landmark : landmarks_to_samples.keySet()) {
                for (Sample sample : landmarks_to_samples.get(landmark)) {
                    StringBuffer rssi_query = new StringBuffer();
                    rssi_query.append("INSERT INTO fingerprint_values (fingerprint_hash,hub_id,antenna_id,rssi,sample_hash,timestamp) VALUES (");
                    rssi_query.append(fingerprint.hashcode).append(",");
                    rssi_query.append(landmark.hub.getID().toLong()).append(",");
                    rssi_query.append(landmark.antenna).append(",");
                    double rssi = fingerprint.getRSSI(landmark);
                    if (rssi == Double.NaN) rssi = NetworkTransmitter.RSSI_PAD_VALUE;
                    rssi_query.append(rssi).append(",");
                    rssi_query.append(sample.hash_code);
                    rssi_query.append(",'").append(formatted_timestamp).append("')");
                    try {
                        this.queue.offer(rssi_query.toString(), QUEUE_INSERT_TIMEOUT, TimeUnit.MILLISECONDS);
                    } catch (InterruptedException ie) {
                        this.server.io.logWarning("Unable to add fingerprint to query: " + rssi_query.toString());
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public synchronized boolean insert(final Region region) {
        if (!this.checkConnection()) {
            if (!this.region_queue.contains(region)) this.region_queue.offer(region);
            return false;
        }
        long start_stamp = System.currentTimeMillis();
        server.io.logDebug("Region " + region.name + " has id " + region.database_id);
        ResultSet dimensions;
        try {
            dimensions = this.statement.executeQuery("SELECT minx, maxx, miny, maxy, minz, maxz, region_id, image_url FROM regions WHERE name='" + region.name + "' AND end_stamp IS NULL");
            this.connection.commit();
        } catch (SQLException sqle) {
            server.io.logWarning("Could not retrieve region \"" + region.name + "\" from database.");
            server.io.logWarning(sqle.toString());
            return false;
        }
        try {
            if (dimensions.next()) {
                String db_image_url = dimensions.getString("image_url");
                if ((region.dimensions_min[0] == dimensions.getDouble("minx")) && (region.dimensions_max[0] == dimensions.getDouble("maxx")) && (region.dimensions_min[1] == dimensions.getDouble("miny")) && (region.dimensions_max[1] == dimensions.getDouble("maxy")) && (region.dimensions_min[2] == dimensions.getDouble("minz")) && (region.dimensions_max[2] == dimensions.getDouble("maxz")) && ((region.image_url == null && db_image_url == null) || (region.image_url != null && region.image_url.equals(db_image_url)))) {
                    region.database_id = dimensions.getInt("region_id");
                    server.io.logInfo("Found region " + region.name + " in the database (" + region.database_id + ").");
                    server.io.logDebug("X: " + region.dimensions_min[0] + "|" + dimensions.getDouble("minx") + " -> " + region.dimensions_max[0] + "|" + dimensions.getDouble("maxx"));
                    server.io.logDebug("Y: " + region.dimensions_min[1] + "|" + dimensions.getDouble("miny") + " -> " + region.dimensions_max[1] + "|" + dimensions.getDouble("maxy"));
                    server.io.logDebug("Z: " + region.dimensions_min[2] + "|" + dimensions.getDouble("minz") + " -> " + region.dimensions_max[2] + "|" + dimensions.getDouble("maxz"));
                    server.io.logDebug("Img: " + region.image_url);
                    return true;
                } else {
                    server.io.logInfo("Need to update region definition for " + region.name + ".");
                    server.io.logDebug("X: " + region.dimensions_min[0] + "|" + dimensions.getDouble("minx") + " -> " + region.dimensions_max[0] + "|" + dimensions.getDouble("maxx"));
                    server.io.logDebug("Y: " + region.dimensions_min[1] + "|" + dimensions.getDouble("miny") + " -> " + region.dimensions_max[1] + "|" + dimensions.getDouble("maxy"));
                    server.io.logDebug("Z: " + region.dimensions_min[2] + "|" + dimensions.getDouble("minz") + " -> " + region.dimensions_max[2] + "|" + dimensions.getDouble("maxz"));
                    server.io.logDebug("Img: " + region.image_url + " -> " + dimensions.getString("image_url"));
                }
                try {
                    this.statement.executeUpdate("UPDATE regions SET end_stamp='" + date_formatter.format(new Date(start_stamp)) + "' WHERE name='" + region.name + "' AND end_stamp IS NULL");
                    this.connection.commit();
                } catch (SQLException sqle) {
                    server.io.logWarning("Could not close region with id " + region.database_id);
                    server.io.logWarning(sqle.toString());
                    return false;
                }
            }
        } catch (SQLException sqle) {
            server.io.logWarning("Could not find region with id " + region.database_id + " in the database.");
            server.io.logWarning(sqle.toString());
        }
        try {
            this.statement.execute("INSERT INTO regions (name, units, region_type, minx, maxx, miny, maxy, minz, maxz, start_stamp" + (region.image_url == null ? ")" : ", image_url)") + " VALUES ('" + region.name + "'," + region.dimensions_units + "," + region.type + "," + region.dimensions_min[0] + "," + region.dimensions_max[0] + "," + region.dimensions_min[1] + "," + region.dimensions_max[1] + "," + region.dimensions_min[2] + "," + region.dimensions_max[2] + ",'" + date_formatter.format(new Date(start_stamp)) + (region.image_url == null ? "');" : "', '" + region.image_url + "');"));
            this.connection.commit();
        } catch (SQLException sqle) {
            server.io.logWarning("Could not insert new region definition for \"" + region.name + "\".");
            server.io.logWarning(sqle.toString());
            return false;
        }
        try {
            ResultSet region_id_set = this.statement.executeQuery("SELECT region_id FROM regions WHERE name='" + region.name + "' AND start_stamp='" + date_formatter.format(new Date(start_stamp)) + "'");
            this.connection.commit();
            if (region_id_set.next()) {
                region.database_id = region_id_set.getInt("region_id");
                return true;
            } else {
                server.io.logWarning("Could not find updated region \"" + region.name + "\" in database.");
                return false;
            }
        } catch (SQLException sqle) {
            server.io.logWarning("Could not retrieve created region from database.");
            server.io.logWarning(sqle.toString());
            return false;
        }
    }

    public synchronized boolean insert(final Landmark landmark) {
        if (!this.checkConnection()) return false;
        synchronized (landmark.regions) {
            for (Region region : landmark.regions) {
                if (region.database_id == -1) if ((region.database_id = this.getRegionIDByName(region.name)) == -1) continue;
                ResultSet landmark_coords;
                try {
                    landmark_coords = this.statement.executeQuery("SELECT x, y, z FROM antennas WHERE region_id=" + region.database_id + " AND hub_id=" + landmark.hub.getID().toLong() + " AND antenna_id=" + landmark.antenna + " AND phy=" + landmark.phy + " AND end_stamp IS NULL");
                    this.connection.commit();
                } catch (SQLException sqle) {
                    server.io.logWarning("Could not query database for landmark " + landmark + '.');
                    server.io.logWarning(sqle.toString());
                    continue;
                }
                try {
                    double[] coords = region.landmark_positions.get(landmark);
                    long now = System.currentTimeMillis();
                    if (landmark_coords.next()) {
                        double x = landmark_coords.getFloat("x");
                        double y = landmark_coords.getFloat("y");
                        double z = landmark_coords.getFloat("z");
                        if (coords[0] == x && coords[1] == y && coords[2] == z) continue; else {
                            try {
                                this.statement.executeUpdate("UPDATE antennas SET end_stamp='" + date_formatter.format(new Date(now)) + "' WHERE hub_id=" + landmark.hub.getID().toLong() + " AND antenna_id=" + landmark.antenna + " AND phy=" + landmark.phy + " AND end_stamp IS NULL");
                                this.connection.commit();
                            } catch (SQLException sqle) {
                                server.io.logWarning("Could not update old landmark definition for landmark " + landmark);
                                server.io.logWarning(sqle.toString());
                            }
                        }
                    }
                    try {
                        this.statement.execute("INSERT INTO antennas (name, hub_id, phy, antenna_id, region_id, x, y, z, start_stamp) VALUES ('" + landmark.name + "', " + landmark.hub.getID().toLong() + ", " + landmark.phy + ", " + landmark.antenna + ", " + region.database_id + ", " + coords[0] + ", " + coords[1] + ", " + coords[2] + ", '" + date_formatter.format(new Date(now)) + "')");
                        this.connection.commit();
                    } catch (SQLException sqle) {
                        server.io.logWarning("Could not insert new landmark definition for landmark " + landmark);
                        server.io.logWarning(sqle.toString());
                        continue;
                    }
                } catch (SQLException sqle) {
                    server.io.logWarning("Could not find landmark " + landmark + " in database.");
                    server.io.logWarning(sqle.toString());
                }
            }
        }
        return true;
    }

    public synchronized boolean insert(final Position position) {
        int location_type = this.getPositionID(position);
        if (location_type == -1) {
            server.io.logWarning("Could not determine location type for position " + position);
            return false;
        }
        int algorithm_type = this.getAlgorithmID(position);
        if (algorithm_type == -1) {
            server.io.logWarning("Could not determine algorithm for position " + position);
            return false;
        }
        if (position.region.database_id == -1 && !this.insert(position.region)) {
            server.io.logWarning("Could not retrieve region id for region " + position.region.name + " to insert position " + position);
            return false;
        }
        StringBuffer query = new StringBuffer();
        query.append("INSERT INTO locations (phy,mac_id,region_id,loc_type,algorithm,start_msec,end_msec,start_stamp,end_stamp,x,y,z,xprime,yprime,zprime,hashcode) VALUES (");
        query.append(position.phy).append(',').append(position.mac_address.toLong()).append(',');
        query.append(position.region.database_id).append(',').append(location_type);
        query.append(',').append(algorithm_type).append(',');
        query.append(position.time_start).append(',').append(position.time_end).append(",'");
        query.append(date_formatter.format(new Date(position.time_start))).append("','").append(date_formatter.format(new Date(position.time_end))).append("',");
        query.append(position.coordinates[0]).append(',');
        query.append(position.coordinates[1]).append(',');
        query.append(position.coordinates[2]).append(',');
        query.append(position.coordinate_deltas[0]).append(',');
        query.append(position.coordinate_deltas[1]).append(',');
        query.append(position.coordinate_deltas[2]).append(',');
        if (position.fingerprint == null) query.append("0)"); else query.append(position.fingerprint.hashcode).append(')');
        if (this.queue.remainingCapacity() != 0) this.queue.offer(query.toString()); else {
            server.io.logWarning("Could not insert position " + position + " because the queue was full.");
            return false;
        }
        return true;
    }

    /**
	 * Inserts the specified hub into the database and updates the Hub object if necessary.
	 * @param hub the hub to be inserted into the database.
	 * @return {@code true} if the hub is successfully inserted/updated, else {@code false}.
	 */
    public synchronized boolean insert(final Hub hub) {
        if (!this.checkConnection()) return false;
        try {
            List<Region> regions = hub.getRegions();
            server.io.logDebug("Hub " + hub.getID() + " is defined in " + regions.size() + " regions.");
            long now = System.currentTimeMillis();
            synchronized (regions) {
                for (Region region : regions) {
                    if (region.database_id == -1 && !this.insert(region)) {
                        server.io.logWarning("Could not retrieve database id for region " + region.name + ".");
                        continue;
                    }
                    ResultSet hub_pos;
                    try {
                        hub_pos = this.statement.executeQuery("SELECT x, y, z FROM hubs WHERE hub_id=" + hub.getID().toLong() + " AND region_id=" + region.database_id + " AND end_stamp IS NULL");
                        this.connection.commit();
                    } catch (SQLException sqle) {
                        server.io.logWarning("Could not retrieve hub information from the database for " + hub.getID().toString());
                        continue;
                    }
                    double[] hub_position = region.hub_positions.get(hub);
                    if (hub_pos.next()) {
                        double x = hub_pos.getDouble("x");
                        double y = hub_pos.getDouble("y");
                        double z = hub_pos.getDouble("z");
                        if (hub_position == null) {
                            server.io.logError("Could not find position in region " + region.name + " for hub " + hub.getID().toString() + ".");
                            continue;
                        }
                        if (hub_position[0] == x && hub_position[1] == y && hub_position[2] == z) continue; else {
                            this.statement.executeUpdate("UPDATE hubs SET end_stamp='" + date_formatter.format(new Date(now)) + "' WHERE hub_id=" + hub.getID().toLong() + " AND region_id=" + region.database_id + " AND end_stamp IS NULL");
                            this.connection.commit();
                        }
                    } else {
                        server.io.logInfo("Could not find hub definition in database.  Adding new hub " + hub);
                    }
                    try {
                        this.statement.executeUpdate("INSERT INTO hubs (hub_id,name,region_id,x,y,z,start_stamp) VALUES (" + hub.getID().toLong() + ",'" + hub.getName() + "'," + region.database_id + "," + hub_position[0] + "," + hub_position[1] + "," + hub_position[2] + ",'" + date_formatter.format(new Date(now)) + "')");
                        this.connection.commit();
                    } catch (SQLException sqle) {
                        server.io.logWarning("Failed to insert hub " + hub.getID() + " into database.");
                        server.io.logWarning(sqle.toString());
                    }
                }
            }
        } catch (SQLException sqle) {
            server.io.logWarning("Could not retrieve hub definition from database.");
        }
        return true;
    }

    /**
	 * Inserts the specified network transmitter into the database.  This method is likely to be called
	 * very frequently during the initial execution of the server, as many previously-unknown network transmitters
	 * will be detected.
	 * @param transmitter the network transmitter to insert into the database.
	 * @return {@code true} if the network transmitter was successfully inserted, else {@code false}.
	 */
    public synchronized boolean insert(NetworkTransmitter transmitter) {
        if (transmitter.in_database) return true;
        if (!checkConnection()) {
            this.server.io.logWarning("Could not connect to DB. Not inserting " + transmitter);
            return false;
        }
        ResultSet db_entry;
        try {
            db_entry = this.statement.executeQuery("SELECT type_id FROM transmitters WHERE mac_id=" + transmitter.mac_address.toLong() + " AND phy=" + transmitter.physical_layer_type);
            this.connection.commit();
            if (db_entry.next()) {
                transmitter.in_database = true;
                return true;
            }
        } catch (SQLException sqle) {
            this.server.io.logWarning("Could not retrieve transmitter from DB.");
            this.server.io.logWarning(sqle.toString());
            return false;
        }
        try {
            this.server.io.logDebug("INSERT INTO transmitters (mac_id, phy, type_id) VALUES " + "(" + transmitter.mac_address.toLong() + "," + transmitter.physical_layer_type + "," + (transmitter.is_mobile ? 0 : 1) + ")");
            this.statement.executeUpdate("INSERT INTO transmitters (mac_id, phy, type_id) VALUES " + "(" + transmitter.mac_address.toLong() + "," + transmitter.physical_layer_type + "," + (transmitter.is_mobile ? 0 : 1) + ")");
            this.connection.commit();
            transmitter.in_database = true;
            this.server.io.logInfo("Inserted " + transmitter + " into the DB.");
        } catch (SQLException sqle) {
            this.server.io.logWarning("Could not insert " + transmitter + " into DB.");
            this.server.io.logWarning(sqle.toString());
        }
        return true;
    }

    /**
	 * Verifies the connection to the database and reconnects if necessary.
	 * 
	 * @return <code>false</code> if the connection to the database is closed
	 */
    private synchronized boolean checkConnection() {
        try {
            if (this.connection == null || this.connection.isClosed()) {
                this.connection = DriverManager.getConnection("jdbc:postgresql://" + this.hostname + '/' + this.database_name, this.username, this.password);
                this.connection.setAutoCommit(false);
                this.statement = this.connection.createStatement();
            }
        } catch (SQLException sqle) {
            this.server.io.logWarning("Could not reconnect to the database.");
            return false;
        }
        return true;
    }

    /**
	 * Executes the batch of statements contained in {@link #statement} and
	 * commits them to the database.&nbsp; If any exceptions are thrown while
	 * processing the statements, then all statements are rolled-back and
	 * <code>false</code> is returned.&nbsp; After successful execution of the
	 * batched statements, <code>statement</code> is cleared.
	 * 
	 * @return <code>false</code> if the statements were not committed to the
	 *         database
	 * @throws SQLException
	 *             if an SQLException occurs while attempting to roll-back
	 *             statements after a failure.
	 */
    private synchronized boolean executeStatements() throws SQLException {
        if (!checkConnection()) return false;
        try {
            this.statement.executeBatch();
            this.connection.commit();
            this.statement.clearBatch();
            return true;
        } catch (BatchUpdateException bue) {
            this.server.io.logWarning("Exception inside executeStatements " + bue);
            this.server.io.logWarning("Exception = " + bue.getNextException());
            int[] counts = bue.getUpdateCounts();
            for (int i = 0; i < counts.length; i++) {
                this.server.io.logWarning("Statement[" + i + "] :" + counts[i]);
            }
            this.connection.rollback();
            this.server.io.logWarning("Clearing batch statement.");
            this.server.io.logDebug("(" + this.statement.toString() + ")");
            this.statement.clearBatch();
        } catch (SQLException sqle) {
            this.server.io.logWarning("Exception inside executeStatements" + sqle);
            this.server.io.logWarning("Exception = " + sqle.getNextException());
            this.connection.rollback();
        }
        return false;
    }

    /**
	 * Retrieves a region's database ID from the database using only the name of the region.
	 * @param region_name the name of the region whose ID will be retrieved.
	 * @return the database ID of the specified region, or -1 if the region could not be found in the database.
	 */
    synchronized int getRegionIDByName(String region_name) {
        try {
            ResultSet region_id_set = this.statement.executeQuery("SELECT region_id FROM regions WHERE name='" + region_name + "' AND start_stamp IS NULL");
            this.connection.commit();
            if (region_id_set.next()) {
                return region_id_set.getInt("region_id");
            } else {
                server.io.logWarning("Could not find region \"" + region_name + "\" in database.");
                return -1;
            }
        } catch (SQLException sqle) {
            server.io.logWarning("Could not retrieve created region from database.");
            server.io.logWarning(sqle.toString());
            return -1;
        }
    }

    /**
	 * Causes this database manager to stop running.  There may be a small delay if the database
	 * manager is currently waiting on the queue.
	 *
	 */
    public synchronized void stopRunning() {
        this.keep_running = false;
        this.queue.notifyAll();
    }

    /**
	 * A utility method that converts the internal representation of localization 
	 * result types to the database representation.
	 * @param position the Position whose type needs to be translated.
	 * @return the database value of the localization result's type.
	 * @see Position#ELLIPSE
	 * @see Position#POINT
	 * @see Position#TILES
	 */
    private int getPositionID(Position position) {
        switch(position.type) {
            case Position.ELLIPSE:
                return DatabaseManager.LOCATION_TYPE_ELLIPSE;
            case Position.POINT:
                return DatabaseManager.LOCATION_TYPE_POINT;
            case Position.TILES:
                return DatabaseManager.LOCATION_TYPE_RECTANGLE;
            default:
                return -1;
        }
    }

    /**
	 * A utility method that converts the internal representation of a localization
	 * algorithm into the database representation.
	 * @param position the localization result containing the localization algorithm used.
	 * @return the database value of the localization algorithm, or -1 if the algorithm
	 * is not in the database.
	 */
    private int getAlgorithmID(Position position) {
        if (position.algorithm instanceof LocalizationAlgorithmM1) return DatabaseManager.ALGORITHM_M1;
        if (position.algorithm instanceof LocalizationAlgorithmM2) return DatabaseManager.ALGORITHM_M2;
        if (position.algorithm instanceof LocalizationAlgorithmM3) return DatabaseManager.ALGORITHM_M3;
        if (position.algorithm instanceof LocalizationAlgorithmSPM) return DatabaseManager.ALGORITHM_SPM;
        if (position.algorithm instanceof LocalizationAlgorithmKNN) return DatabaseManager.ALGORITHM_KNN;
        if (position.algorithm instanceof LocalizationAlgorithmKNL) return DatabaseManager.ALGORITHM_KNL; else return -1;
    }

    /**
	 * A utility method that converts the internal representation of a fingerprint
	 * algorithm to the database representation.
	 * @param function the fingerprint algorithm whose database ID is desired.
	 * @return the database value of the fingerprint algorithm, or -1 if the algorithm
	 * is not in the database.
	 */
    private int getFingerprintAlgorithm(FingerprintFunction function) {
        if (function instanceof MeanFingerprintFunction) return FF_ALG_MEAN;
        if (function instanceof MedianFingerprintFunction) return FF_ALG_MEDIAN;
        if (function instanceof StdDevFingerprintFunction) return FF_ALG_STDDEV;
        if (function instanceof TimeMeanFingerprintFunction) return FF_ALG_TIME_MEAN;
        if (function instanceof TimeMedianFingerprintFunction) return FF_ALG_TIME_MEDIAN;
        if (function instanceof TimeStdDevFingerprintFunction) return FF_ALG_TIME_STDDEV; else return -1;
    }

    /**
	 * Takes a binary byte array as input and returns in escaped octal
	 * appropriate for the postgres bytea data type.
	 * 
	 * @return <code>string</code> of the returned binary data.
	 */
    private String binaryToEscapedOctal(byte bin[]) {
        if (bin == null) {
            return "NULL";
        }
        StringBuffer sb;
        int i, val;
        sb = new StringBuffer(5 * bin.length + 16);
        sb.append("'");
        for (i = 0; i < bin.length; i++) {
            val = (bin[i] & 0xFF);
            sb.append("\\\\");
            if (val < 8) {
                sb.append("00");
            } else if (val < 64) {
                sb.append("0");
            }
            sb.append(Integer.toOctalString(val));
        }
        sb.append("'::bytea");
        return sb.toString();
    }

    public String show() {
        return "";
    }
}
