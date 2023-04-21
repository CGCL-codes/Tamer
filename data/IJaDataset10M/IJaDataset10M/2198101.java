package gov.sns.tools.pvlogger;

import gov.sns.tools.database.ConnectionDictionary;
import gov.sns.tools.database.DatabaseAdaptor;
import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class ArraySqlStateStore extends SqlStateStore {

    /** Description of the Field */
    protected static final String PV_SNAPSHOT_ARRAY_TABLE = "epics.mach_snapshot_sgnl_array";

    public ArraySqlStateStore(DatabaseAdaptor adaptor, Connection connection) {
        super(adaptor, connection);
    }

    public ArraySqlStateStore(Connection connection) {
        super(connection);
    }

    public ArraySqlStateStore(DatabaseAdaptor adaptor, String urlSpec, String user, String password) throws StateStoreException {
        super(adaptor, urlSpec, user, password);
    }

    public ArraySqlStateStore(String urlSpec, String user, String password) throws StateStoreException {
        super(urlSpec, user, password);
    }

    public ArraySqlStateStore(DatabaseAdaptor adaptor, ConnectionDictionary dictionary) throws StateStoreException {
        super(adaptor, dictionary);
    }

    public ArraySqlStateStore(ConnectionDictionary dictionary) throws StateStoreException {
        super(dictionary);
    }

    /**
	 * Create the prepared statement if it does not already exist.
	 *
	 * @return                        the prepared statement for inserting a new channel snapshot
	 * @exception SQLException        Description of the Exception
	 * @throws java.sql.SQLException  if an exception occurs during a SQL evaluation
	 */
    @Override
    protected PreparedStatement getChannelSnapshotInsertStatement() throws SQLException {
        if (CHANNEL_SNAPSHOT_INSERT == null) {
            CHANNEL_SNAPSHOT_INSERT = _connection.prepareStatement("INSERT INTO " + PV_SNAPSHOT_ARRAY_TABLE + "(" + PV_SNAPSHOT_MACHINE_SNAPSHOT_COL + ", " + PV_SNAPSHOT_PV_COL + ", " + PV_SNAPSHOT_TIMESTAMP_COL + ", " + PV_SNAPSHOT_VALUE_COL + ", " + PV_SNAPSHOT_STATUS_COL + ", " + PV_SNAPSHOT_SEVERITY_COL + ") VALUES (?, ?, ?, ?, ?, ?)");
        }
        return CHANNEL_SNAPSHOT_INSERT;
    }

    /**
	 * Create the prepared statement if it does not already exist.
	 *
	 * @return                        the prepared statement to query for channel snapshots by
	 *      machine snapshot
	 * @exception SQLException        Description of the Exception
	 * @throws java.sql.SQLException  if an exception occurs during a SQL evaluation
	 */
    @Override
    protected PreparedStatement getChannelSnapshotQueryByMachineSnapshotStatement() throws SQLException {
        if (CHANNEL_SNAPSHOT_QUERY_BY_MACHINE_SNAPSHOT == null) {
            CHANNEL_SNAPSHOT_QUERY_BY_MACHINE_SNAPSHOT = _connection.prepareStatement("SELECT * FROM " + PV_SNAPSHOT_ARRAY_TABLE + " WHERE " + PV_SNAPSHOT_MACHINE_SNAPSHOT_COL + " = ?");
        }
        return CHANNEL_SNAPSHOT_QUERY_BY_MACHINE_SNAPSHOT;
    }

    /**
	 * Publish the channel snapshot and associate it with the machine snapshot given by the
	 * machine snapshop id.
	 *
	 * @param snapshot                                     The channel snapshot to publish
	 * @param machineId                                    The unique id of the associated machine
	 *      snapshot
	 * @exception StateStoreException                      Description of the Exception
	 * @throws gov.sns.tools.pvlogger.StateStoreException  if a SQL exception is thrown
	 */
    public void publish(final ChannelSnapshot[] snapshots, final long machineId) throws StateStoreException {
        String pvs[] = new String[snapshots.length];
        Timestamp times[] = new Timestamp[snapshots.length];
        double values[][] = new double[snapshots.length][];
        int stats[] = new int[snapshots.length];
        int severs[] = new int[snapshots.length];
        int count = 0;
        for (int index = 0; index < snapshots.length; index++) {
            ChannelSnapshot snapshot = snapshots[index];
            if (snapshot != null) {
                pvs[count] = snapshot.getPV();
                times[count] = snapshot.getTimestamp().getSQLTimestamp();
                values[count] = snapshot.getValue();
                stats[count] = snapshot.getStatus();
                severs[count] = snapshot.getSeverity();
                count++;
            }
        }
        if ((count > 0) && (count < snapshots.length)) {
            String pvsnew[] = new String[count];
            System.arraycopy(pvs, 0, pvsnew, 0, count);
            pvs = pvsnew;
            Timestamp timesnew[] = new Timestamp[count];
            System.arraycopy(times, 0, timesnew, 0, count);
            times = timesnew;
            double valuesnew[][] = new double[count][];
            System.arraycopy(values, 0, valuesnew, 0, count);
            values = valuesnew;
            int statsnew[] = new int[count];
            System.arraycopy(stats, 0, statsnew, 0, count);
            stats = statsnew;
            int seversnew[] = new int[count];
            System.arraycopy(severs, 0, seversnew, 0, count);
            severs = seversnew;
        }
        if (count > 0) {
            try {
                getChannelSnapshotInsertStatement();
                CHANNEL_SNAPSHOT_INSERT.setLong(1, machineId);
                Array pvArray = _databaseAdaptor.getArray("float4", _connection, pvs);
                CHANNEL_SNAPSHOT_INSERT.setArray(2, pvArray);
                Array timestampArray = _databaseAdaptor.getArray("timestamp", _connection, times);
                CHANNEL_SNAPSHOT_INSERT.setArray(3, timestampArray);
                Array valueArray = _databaseAdaptor.getArray("float4", _connection, values);
                CHANNEL_SNAPSHOT_INSERT.setArray(4, valueArray);
                Array statArray = _databaseAdaptor.getArray("int4", _connection, stats);
                CHANNEL_SNAPSHOT_INSERT.setArray(5, statArray);
                Array severArray = _databaseAdaptor.getArray("int4", _connection, severs);
                CHANNEL_SNAPSHOT_INSERT.setArray(6, severArray);
                CHANNEL_SNAPSHOT_INSERT.addBatch();
            } catch (SQLException exception) {
                throw new StateStoreException("Error publishing a channel snapshot.", exception);
            }
        } else {
            System.out.println("count = " + count);
        }
    }

    /**
	 * Publish the machine snapshot.
	 *
	 * @param machineSnapshot                              The machine snapshot to publish.
	 * @exception StateStoreException                      Description of the Exception
	 * @throws gov.sns.tools.pvlogger.StateStoreException  if a SQL exception is thrown
	 */
    @Override
    public void publish(final MachineSnapshot machineSnapshot) throws StateStoreException {
        java.sql.Timestamp time = new java.sql.Timestamp(machineSnapshot.getTimestamp().getTime());
        try {
            getMachineSnapshotNextPrimaryKeyStatement();
            getMachineSnapshotInsertStatement();
            getChannelSnapshotInsertStatement();
            ResultSet idResult = MACHINE_SNAPSHOT_NEXT_PKEY.executeQuery();
            commit();
            idResult.next();
            long id = idResult.getLong(1);
            MACHINE_SNAPSHOT_INSERT.setLong(1, id);
            MACHINE_SNAPSHOT_INSERT.setTimestamp(2, time);
            MACHINE_SNAPSHOT_INSERT.setString(3, machineSnapshot.getType());
            MACHINE_SNAPSHOT_INSERT.setString(4, machineSnapshot.getComment());
            MACHINE_SNAPSHOT_INSERT.executeUpdate();
            commit();
            final ChannelSnapshot[] channelSnapshots = machineSnapshot.getChannelSnapshots();
            publish(channelSnapshots, id);
            CHANNEL_SNAPSHOT_INSERT.executeBatch();
            commit();
            CHANNEL_SNAPSHOT_INSERT.clearBatch();
            machineSnapshot.setId(id);
        } catch (SQLException exception) {
            exception.getNextException().printStackTrace();
            throw new StateStoreException("Error publishing a machine snapshot.", exception);
        }
    }
}
