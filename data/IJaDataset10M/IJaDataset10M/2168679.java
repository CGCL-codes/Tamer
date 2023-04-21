package mocaptracking;

import mocapcommon.trackdata.TrackedBody;
import mocapcommon.trackdata.DataFrame;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.LinkedList;
import javax.swing.JFrame;

/**
 *
 * @author m.weber
 */
public class WriteBodyNameDBThread implements Runnable {

    /** frame reference */
    JFrame refFrame;

    /** prepared statement for writing to db */
    private PreparedStatement updateNameStat;

    /** mysql connection */
    private Connection mysqlCon;

    /** queue of tracked data to write */
    private LinkedList<WriteDBData> dataList;

    /** flag if thread should finish */
    private boolean doFinish;

    /** data to be written to database */
    public class WriteDBData {

        public WriteDBData(DataFrame d, int id) {
            data = d;
            dataID = id;
        }

        /** the data */
        public DataFrame data;

        /** data artid */
        public int dataID;
    }

    ;

    /** constructor */
    public WriteBodyNameDBThread(Connection con, JFrame fr) {
        mysqlCon = con;
        refFrame = fr;
        dataList = new LinkedList<WriteDBData>();
        doFinish = false;
        try {
            updateNameStat = mysqlCon.prepareStatement("update bodies set name=? where " + "id=?");
        } catch (SQLException e) {
            javax.swing.JOptionPane.showMessageDialog(refFrame, "ARTWriteBodyNameDB: Couldn't update table: " + e.getMessage(), "SQL error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    /** write all data to database in thread */
    @Override
    public final void run() {
        while (!doFinish) {
            try {
                while (!dataList.isEmpty()) {
                    WriteDBData data = null;
                    synchronized (dataList) {
                        data = dataList.getFirst();
                    }
                    if (data != null) {
                        for (Iterator<TrackedBody> bodyIt = data.data.bodies.iterator(); bodyIt.hasNext(); ) {
                            TrackedBody body = bodyIt.next();
                            if (data.dataID != -1) {
                                if (body.name != null && body.name.isEmpty()) updateNameStat.setString(1, null); else updateNameStat.setString(1, body.name);
                                updateNameStat.setInt(2, body.dbid);
                                updateNameStat.executeUpdate();
                            }
                        }
                        synchronized (dataList) {
                            dataList.remove(data);
                        }
                    }
                    Thread.yield();
                }
            } catch (SQLException ex) {
                javax.swing.JOptionPane.showMessageDialog(refFrame, "animateButtonActionPerformed: Couldn't update table: " + ex.getMessage(), "SQL error", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
    }

    /** add data to queue */
    public final void addToQueue(DataFrame data, int dataID) {
        synchronized (dataList) {
            dataList.add(new WriteDBData(data.clone(), dataID));
        }
    }

    /** finish thread */
    public final void finish() {
        doFinish = true;
    }
}
