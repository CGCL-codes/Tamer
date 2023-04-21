package tcg.scada.sim.iecsim.datastore;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;
import org.apache.log4j.Logger;
import org.apache.log4j.NDC;
import tcg.scada.sim.iecsim.datastore.DataPoint.EDataPointType;
import tcg.scada.sim.iecsim.main.Manager;

public class DataStoreTable {

    private ArrayList<DataPoint> datapoints_ = new ArrayList<DataPoint>();

    private HashMap<String, DataPoint> dpNameLookup_ = new HashMap<String, DataPoint>();

    private HashMap<Integer, DataPoint> dpAddressLookup_ = new HashMap<Integer, DataPoint>();

    private ArrayList<DataPoint> diLookup_ = new ArrayList<DataPoint>();

    private ArrayList<DataPoint> ddiLookup_ = new ArrayList<DataPoint>();

    private ArrayList<DataPoint> tdiLookup_ = new ArrayList<DataPoint>();

    private ArrayList<DataPoint> aiLookup_ = new ArrayList<DataPoint>();

    private ArrayList<DataPoint> utcLookup_ = new ArrayList<DataPoint>();

    private ArrayList<DataPoint> miLookup_ = new ArrayList<DataPoint>();

    private ArrayList<DataPoint> cxLookup_ = new ArrayList<DataPoint>();

    private DataStore parent_ = null;

    private String id_ = "";

    private int addressOffset_ = 0;

    private Logger logger_ = null;

    public DataStoreTable(DataStore location, String id, int offset) {
        parent_ = location;
        id_ = id;
        addressOffset_ = offset;
        logger_ = parent_.getLogger();
        initData();
    }

    /** Get all relevant data points from database **/
    public synchronized void initData() {
        NDC.push(id_);
        Connection conn = Manager.getDBConnection();
        if (conn == null) {
            NDC.pop();
            return;
        }
        try {
            String datatable = Manager.getProperty("ste.scada.sim.iecsim.dptable", Manager.DEF_DPTABLE);
            String valuetable = Manager.getProperty("ste.scada.sim.iecsim.valuetable", Manager.DEF_VALUETABLE);
            Statement smnt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String query = "SELECT DP.LINE, DP.STATION, DP.SUBSYSTEM, DP.EQUIPMENT, DP.POINT, " + "DP.EQPT_TY, DP.ADDRESS, VAL.INIT_VALUE " + "FROM " + datatable + " DP LEFT OUTER JOIN " + valuetable + " VAL ON " + "DP.POINT=VAL.POINT " + "WHERE OCCID='" + id_ + "' " + "ORDER BY ADDRESS, BIT";
            logger_.debug("Query: " + query);
            ResultSet rs = smnt.executeQuery(query);
            if (rs == null) {
                logger_.warn("Can not get list of datapoints. Empty result set.");
            }
            String initValue = "", dpname = "";
            int address = 0;
            EDataPointType type = null;
            while (rs.next()) {
                dpname = rs.getString("LINE") + "/" + rs.getString("STATION") + "/" + rs.getString("SUBSYSTEM") + "/" + rs.getString("EQUIPMENT") + "/" + rs.getString("POINT");
                address = rs.getInt("ADDRESS") - addressOffset_;
                if (rs.getString("EQPT_TY").equals("DI")) {
                    type = EDataPointType.TYPE_DI;
                } else if (rs.getString("EQPT_TY").equals("DDI")) {
                    type = EDataPointType.TYPE_DDI;
                } else if (rs.getString("EQPT_TY").equals("TDI")) {
                    type = EDataPointType.TYPE_TDI;
                } else if (rs.getString("EQPT_TY").equals("AI")) {
                    type = EDataPointType.TYPE_AI;
                } else if (rs.getString("EQPT_TY").equals("UTC")) {
                    type = EDataPointType.TYPE_UTC;
                } else if (rs.getString("EQPT_TY").equals("MI")) {
                    type = EDataPointType.TYPE_MI;
                } else if (rs.getString("EQPT_TY").startsWith("C")) {
                    type = EDataPointType.TYPE_CX;
                } else {
                    logger_.warn("Unknown datapoint type. Default to AI (2 bytes integer).");
                    type = EDataPointType.TYPE_AI;
                }
                initValue = rs.getString("INIT_VALUE");
                if (initValue == null) {
                    if (type == EDataPointType.TYPE_DI) {
                        initValue = DataPoint.STR_BOOLEAN_TRUE;
                    } else if (type == EDataPointType.TYPE_CX) {
                        initValue = "";
                    } else {
                        initValue = "0";
                    }
                }
                if (type == EDataPointType.TYPE_DI) {
                    if (initValue.equalsIgnoreCase("On")) {
                        initValue = DataPoint.STR_BOOLEAN_TRUE;
                    } else if (initValue.equalsIgnoreCase("Off")) {
                        initValue = DataPoint.STR_BOOLEAN_FALSE;
                    }
                }
                DataPoint dp = new DataPoint(this, dpname, address, type);
                try {
                    dp.setValue(initValue);
                } catch (Exception ex) {
                    logger_.error("Can not set the initial value for " + dpname + ". Value: " + initValue);
                    logger_.error("Exception: " + ex.getMessage());
                }
                datapoints_.add(dp);
                dpNameLookup_.put(dpname, dp);
                dpAddressLookup_.put(new Integer(address), dp);
                if (type == EDataPointType.TYPE_DI) {
                    diLookup_.add(dp);
                } else if (type == EDataPointType.TYPE_DDI) {
                    ddiLookup_.add(dp);
                } else if (type == EDataPointType.TYPE_TDI) {
                    tdiLookup_.add(dp);
                } else if (type == EDataPointType.TYPE_AI) {
                    aiLookup_.add(dp);
                } else if (type == EDataPointType.TYPE_UTC) {
                    utcLookup_.add(dp);
                } else if (type == EDataPointType.TYPE_MI) {
                    miLookup_.add(dp);
                } else if (type == EDataPointType.TYPE_CX) {
                    cxLookup_.add(dp);
                } else {
                }
            }
            rs.close();
            smnt.close();
            logger_.info("Actual data size: " + datapoints_.size());
        } catch (SQLException e) {
            logger_.error("Can not get the list of datapoints. SQLException: " + e.getMessage());
        } catch (Exception e) {
            logger_.error("Can not get the list of datapoints. Exception: " + e.getMessage());
        }
        Manager.closeDBConnection(conn);
        NDC.pop();
    }

    public DataPoint getDataPoint(String name) {
        return dpNameLookup_.get(name);
    }

    public DataPoint getDataPoint(int address) {
        return dpAddressLookup_.get(new Integer(address));
    }

    public Enumeration<String> getDatapointList() {
        Set<String> keys = dpNameLookup_.keySet();
        Vector<String> vectorKeys = new Vector<String>();
        vectorKeys.addAll(keys);
        return vectorKeys.elements();
    }

    public Collection<DataPoint> getAllDataPoints() {
        return dpNameLookup_.values();
    }

    public Collection<DataPoint> getAllDIPoints() {
        return diLookup_;
    }

    public Collection<DataPoint> getAllDDIPoints() {
        return ddiLookup_;
    }

    public Collection<DataPoint> getAllTDIPoints() {
        return tdiLookup_;
    }

    public Collection<DataPoint> getAllAIPoints() {
        return aiLookup_;
    }

    public Collection<DataPoint> getAllUTCPoints() {
        return utcLookup_;
    }

    public Collection<DataPoint> getAllMIPoints() {
        return miLookup_;
    }

    public Collection<DataPoint> getAllCXPoints() {
        return cxLookup_;
    }

    public void createSnapshot(Properties prop) {
        String key = "", value = "";
        DataPoint dp = null;
        for (int i = 0; i < datapoints_.size(); i++) {
            dp = datapoints_.get(i);
            if (dp != null) {
                key = dp.getName();
                try {
                    value = dp.getValue();
                    prop.setProperty(key, value);
                } catch (Exception ex) {
                }
            }
        }
    }

    public void loadSnapshot(Properties prop) {
        String key = "", value = "";
        DataPoint dp = null;
        Enumeration<Object> keys = prop.keys();
        while (keys.hasMoreElements()) {
            key = (String) keys.nextElement();
            value = prop.getProperty(key);
            dp = dpNameLookup_.get(key);
            if (dp != null) {
                try {
                    dp.setValue(value);
                } catch (Exception ex) {
                }
            }
        }
    }

    protected void broadcastUpdate(DataPoint dp) {
        parent_.getPrimaryHost().onDataPointChange(dp);
        parent_.getStandbyHost().onDataPointChange(dp);
    }
}
