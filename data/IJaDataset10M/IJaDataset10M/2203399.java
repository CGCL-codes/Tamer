package hu.sztaki.lpds.statistics.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import hu.sztaki.lpds.statistics.jobState.StateType;

/**
 * This class populates the data fields in the Metric Information.
 *
 * Example:
 * <code>
 * Map<String, List<MetricInformation>> ms = null;
 *       MetricInformationFactory mif = new MetricInformationFactory(new DBBase());
 *       try {
 *           ms = mif.getMetric("user"); //Gets all the diffrent types of statistics that the user cares about
 * }...
 *  StatistiticsInformationHarvester sf = new StatistiticsInformationHarvester(new DBBase());
 *       try {
 *           sf.getUser(ms, userID); //Populates the statistics with the particular user's data
 *       }
 * </code>
 *
 * This class supports retrieving metrics from three types of tables.
 * stat_user, stat_portal, stat_resource, stat_ConcreteWorkflow, stat_WorkflowInstance, stat_AbstractJob :
 *    Currently only workflow instance has any actual data, the rest are just a method of mapping to the stat_statistics
 *    row. But, if an addional statistics column were added it to these tables it could be easily accessed by
 *    adding an appropiate row to stat_metric_description
 * stat_statistics : Holds statistics about the overall jobs
 * stat_JobStateTypeStatistics : Holds statistics for the states the jobs enter, grouped by StateType @see jobState.StateType
 *
 * @author smoniz
 */
public class StatistiticsInformationHarvester extends InformationHarvester {

    /**
     * Give it an object that creates connections
     * @param connectionSource
     */
    public StatistiticsInformationHarvester(DBBase connectionSource) {
        this.connectionSource = connectionSource;
    }

    /**
     * Populate metrics map with the data for the specific workflow instance
     * @param metrics Metrics Map<String, List<MetricInformation>> from MetricInformationFactory
     * @param wrtid Workflow Instance ID, commonly retrieved through MenuPopulator
     */
    public void getWorkflowInstance(Map<String, List<MetricInformation>> metrics, String wrtid) {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connectionSource.getConnection();
            ps = con.prepareStatement("SELECT *  " + " FROM `stat_statistics` JOIN `stat_WorkflowInstance` ON " + "`stat_statistics`.`ID` = `stat_WorkflowInstance`.`statistics_ID`" + "WHERE wrtid = ?");
            ps.setString(1, wrtid);
            populateMetricData(ps, metrics, con);
        } catch (IOException e0) {
            e0.printStackTrace();
        } catch (SQLException e0) {
            e0.printStackTrace();
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Populate the metrics map with data for specific Concrete Workflow
     * @param metrics   Metrics Map<String, List<MetricInformation>> from MetricInformationFactory
     * @param wfid Concrete Workflow ID
     */
    public void getConcreteWorkflow(Map<String, List<MetricInformation>> metrics, String wfid) {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connectionSource.getConnection();
            ps = con.prepareStatement("SELECT * " + " FROM `stat_statistics` JOIN `stat_ConcreteWorkflow` ON " + "`stat_statistics`.`ID` = `stat_ConcreteWorkflow`.`statistics_ID`" + "WHERE wfid = ?");
            ps.setString(1, wfid);
            populateMetricData(ps, metrics, con);
        } catch (IOException e0) {
            e0.printStackTrace();
        } catch (SQLException e0) {
            e0.printStackTrace();
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Get statistics for the specific abstract job
     * @param   metrics Metrics Map<String, List<MetricInformation>> from MetricInformationFactory
     * @param jobName Name of the Job
     * @param wfId Concrete Workflow the job is from
     */
    public void getAbstractJob(Map<String, List<MetricInformation>> metrics, String jobName, String wfId) {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connectionSource.getConnection();
            ps = con.prepareStatement("SELECT *  " + " FROM `stat_statistics` JOIN `stat_AbstractJob` ON " + "`stat_statistics`.`ID` = `stat_AbstractJob`.`statistics_ID`" + "WHERE jobName = ? AND wfid = ?");
            ps.setString(1, jobName);
            ps.setString(2, wfId);
            populateMetricData(ps, metrics, con);
        } catch (IOException e0) {
            e0.printStackTrace();
        } catch (SQLException e0) {
            e0.printStackTrace();
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Populate metrics with data for the user's statistics
     * @param metrics Metrics Map<String, List<MetricInformation>> from MetricInformationFactory
     * @param userID
     */
    public void getUser(Map<String, List<MetricInformation>> metrics, String userID) {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connectionSource.getConnection();
            ps = con.prepareStatement("SELECT *  " + " FROM `stat_statistics` JOIN `stat_user` ON " + "`stat_statistics`.`ID` = `stat_user`.`statistics_ID`" + "WHERE userID = ?");
            ps.setString(1, userID);
            populateMetricData(ps, metrics, con);
        } catch (IOException e0) {
            e0.printStackTrace();
        } catch (SQLException e0) {
            e0.printStackTrace();
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Populate metrics with data for the portal
     * We do require the portal URL as it is possible for this system to manage multiple portals
     * @param metrics  Metrics Map<String, List<MetricInformation>> from MetricInformationFactory
     * @param URL portal's URL as entered in stat_portal
     */
    public void getPortal(Map<String, List<MetricInformation>> metrics, String URL) {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connectionSource.getConnection();
            ps = con.prepareStatement("SELECT * " + " FROM `stat_statistics` JOIN `stat_portal` ON " + "`stat_statistics`.`ID` = `stat_portal`.`statistics_ID`" + "WHERE URL = ?");
            ps.setString(1, URL);
            populateMetricData(ps, metrics, con);
        } catch (SQLException e0) {
            e0.printStackTrace();
        } catch (IOException e0) {
            e0.printStackTrace();
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * populate metrics with statistics about the given resource
     * @param metrics Metrics Map<String, List<MetricInformation>> from MetricInformationFactory
     * @param URL Resource URL, stat_resource
     */
    public void getResource(Map<String, List<MetricInformation>> metrics, String URL) {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connectionSource.getConnection();
            ps = con.prepareStatement("SELECT *  " + " FROM `stat_statistics` JOIN `stat_resource` ON " + "`stat_statistics`.`ID` = `stat_resource`.`statistics_ID`" + "WHERE URL = ?");
            ps.setString(1, URL);
            populateMetricData(ps, metrics, con);
        } catch (SQLException e0) {
            e0.printStackTrace();
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e0) {
            e0.printStackTrace();
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * populate metrics with stats about the named DCI
     * @param metrics
     * @param name
     */
    public void getDCI(Map<String, List<MetricInformation>> metrics, String name) {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = connectionSource.getConnection();
            ps = con.prepareStatement("SELECT *  " + " FROM `stat_statistics` JOIN `stat_DCI` ON " + "`stat_statistics`.`ID` = `stat_DCI`.`statistics_ID`" + "WHERE name = ?");
            ps.setString(1, name);
            populateMetricData(ps, metrics, con);
        } catch (SQLException e0) {
            e0.printStackTrace();
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e0) {
            e0.printStackTrace();
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Helper that takes the PreparedStatement, executes, parses and stores in the correct MetricInformation object
     * @param ps
     * @param metrics
     * @param con
     */
    private void populateMetricData(PreparedStatement ps, Map<String, List<MetricInformation>> metrics, Connection con) {
        ResultSet rst = null;
        try {
            rst = ps.executeQuery();
            Map<StateType, List<MetricInformation>> statusMetrics = new HashMap<StateType, List<MetricInformation>>();
            if (rst.next()) {
                int id = rst.getInt("ID");
                for (List<MetricInformation> c : metrics.values()) {
                    for (MetricInformation m : c) {
                        if (m.getSource().equalsIgnoreCase("stat_JobStateTypeStatistics")) {
                            List<MetricInformation> l = statusMetrics.get(m.getType());
                            if (l == null) {
                                l = new ArrayList<MetricInformation>();
                            }
                            l.add(m);
                            statusMetrics.put(m.getType(), l);
                        } else {
                            m.setData(rst.getFloat(m.getColumn_Name()));
                        }
                    }
                }
                rst.close();
                ps.close();
                ps = con.prepareStatement("SELECT * FROM `stat_JobStateTypeStatistics` WHERE statistics_ID = ?");
                ps.setInt(1, id);
                rst = ps.executeQuery();
                while (rst.next()) {
                    StateType stateType = StateType.valueOf(rst.getString("StateType").toUpperCase());
                    if (stateType != null) {
                        List<MetricInformation> l = statusMetrics.get(stateType);
                        if (l != null) {
                            for (MetricInformation m : l) {
                                m.setData(rst.getFloat(m.getColumn_Name()));
                            }
                        }
                    }
                }
                con.close();
            }
        } catch (SQLException e0) {
            e0.printStackTrace();
        } finally {
            if (rst != null) {
                try {
                    rst.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
