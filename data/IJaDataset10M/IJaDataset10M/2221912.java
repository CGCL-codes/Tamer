package seismosurfer.update;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.StringTokenizer;
import seismosurfer.data.MagData;
import seismosurfer.data.QuakeData;
import seismosurfer.database.DB;
import seismosurfer.database.QuakeDAO;
import com.bbn.openmap.util.Debug;

/**
 * An extension to Updater for the greek (GI-NOA)
 * catalogs.
 *
 */
public class GINOAUpdater extends Updater {

    public GINOAUpdater(int catalogID) {
        super(catalogID);
    }

    protected void insert(QuakeData qd) {
        try {
            DB.begin();
            QuakeDAO dao = new QuakeDAO();
            dao.insertQuake(qd);
            if (!qd.getCatalogName().equalsIgnoreCase(LOCAL_BEFORE_1964_CATALOG_NAME)) {
                MagData mag = new MagData();
                mag.setMagnitude(qd.getMagnitude());
                mag.setMagnitudeType(ML_MAGNITUDE);
                mag.setCalculated(false);
                dao.insertMagnitude(mag);
                mag = new MagData();
                mag.setMagnitude(qd.getMagnitude() + 0.5);
                mag.setMagnitudeType(MS_MAGNITUDE);
                mag.setCalculated(true);
                dao.insertMagnitude(mag);
            } else if (qd.getCatalogName().equalsIgnoreCase(LOCAL_BEFORE_1964_CATALOG_NAME)) {
                MagData mag = new MagData();
                mag.setMagnitude(qd.getMagnitude());
                mag.setMagnitudeType(MS_MAGNITUDE);
                mag.setCalculated(false);
                dao.insertMagnitude(mag);
                mag = new MagData();
                mag.setMagnitude(qd.getMagnitude() - 0.5);
                mag.setMagnitudeType(ML_MAGNITUDE);
                mag.setCalculated(true);
                dao.insertMagnitude(mag);
            }
            DB.commit();
        } catch (SQLException e) {
            DB.handleTransactionError(e);
        }
    }

    protected void parse() {
        String line;
        StringTokenizer st;
        int count = 0;
        String[] dateTime = new String[6];
        Iterator it = data.iterator();
        while (it.hasNext()) {
            line = (String) it.next();
            count++;
            if (Debug.debugging("update")) {
                Debug.output("Line " + count + ": " + line);
            }
            if (empty(line)) {
                continue;
            }
            st = new StringTokenizer(line);
            if (st.countTokens() != 10) {
                if (Debug.debugging("update")) {
                    Debug.output("Skipping line " + count + " ...");
                }
                continue;
            }
            QuakeData qd = new QuakeData();
            qd.setSource(GI_NOA_SOURCE_NAME);
            qd.setCatalogID(this.catalog.getCatalogID());
            qd.setCatalogName(this.catalog.getCatalogName());
            for (int j = 0; j < 6; j++) {
                dateTime[j] = st.nextToken();
            }
            long time = getDateTime(dateTime[0], dateTime[1], dateTime[2], dateTime[3], dateTime[4], dateTime[5]);
            if (time == -1) {
                if (Debug.debugging("update")) {
                    Debug.output("Skipping line " + count + " ...");
                }
                continue;
            }
            qd.setDateTime(time);
            try {
                qd.setLatitude(Double.parseDouble(st.nextToken()));
                qd.setLongitude(Double.parseDouble(st.nextToken()));
                qd.setDepth(Double.parseDouble(st.nextToken()));
                qd.setMagnitude(Double.parseDouble(st.nextToken()));
            } catch (NumberFormatException e) {
                Debug.error("One of the values is not in the expected format." + e.getMessage());
                continue;
            }
            filtered.add(qd);
        }
        data.clear();
    }

    protected static long getDateTime(String year, String month, String day, String hour, String minute, String second) {
        int mn = 0;
        int i;
        int months = 12;
        String[] monthNames = { "JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC" };
        boolean correct = false;
        for (i = 0; i < months; i++) {
            if (month.equals(monthNames[i])) {
                mn = i;
                correct = true;
                break;
            }
        }
        if (!correct) {
            Debug.error("Value for month is not in the expected format.");
            return -1;
        }
        Calendar tempCal = Calendar.getInstance();
        tempCal.setTimeInMillis(0);
        try {
            tempCal.set(Integer.parseInt(year), mn, Integer.parseInt(day), Integer.parseInt(hour), Integer.parseInt(minute), Math.round(Float.parseFloat(second)));
        } catch (NumberFormatException e) {
            Debug.error(e.getMessage());
            return -1;
        }
        java.util.Date myDate = tempCal.getTime();
        return myDate.getTime();
    }
}
