package edu.upmc.opi.caBIG.caTIES.gate.persist;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.globus.gsi.GlobusCredential;
import uk.org.ogsadai.client.toolkit.activity.ActivityRequest;
import uk.org.ogsadai.client.toolkit.activity.sql.SQLQuery;
import uk.org.ogsadai.client.toolkit.activity.sql.WebRowSet;
import uk.org.ogsadai.client.toolkit.service.DataService;
import edu.upmc.opi.caBIG.caTIES.common.CaTIES_FormatUtils;
import edu.upmc.opi.caBIG.caTIES.common.CaTIES_Utils;

/**
 * Provides a rough taxonomy of documents acquired across time. The technique is
 * to determine the time span of all documents and create year/month/week bins
 * over that duration. When a particular bin is chosen the database is hit to
 * pull actual documents in that window.
 */
public class CaTIES_HistogramTreeModel extends DefaultTreeModel {

    private static final long serialVersionUID = -1367268679382435409L;

    /**
	 * Field logger.
	 */
    private static final Logger logger = Logger.getLogger(CaTIES_HistogramTreeModel.class);

    private static JFrame frame = null;

    private static CaTIES_HistogramTreeModel cachedInstance = null;

    protected static String[] monthNames = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };

    private GlobusCredential globusCredential = null;

    private DefaultMutableTreeNode selectedNode = null;

    private MutableTreeNode projectNode = null;

    private DataService gridDataService = null;

    public CaTIES_HistogramTreeModel() {
        super(new DefaultMutableTreeNode("CaTIES"), true);
    }

    public static CaTIES_HistogramTreeModel getInstance() {
        if (cachedInstance == null) {
            cachedInstance = new CaTIES_HistogramTreeModel();
        }
        return cachedInstance;
    }

    public boolean login(String user, String password) {
        boolean result = false;
        try {
            String distinguishedName = this.globusCredential.getIdentity();
            logger.debug(distinguishedName);
            initialize();
            result = true;
        } catch (Exception e) {
            result = false;
            e.printStackTrace();
        }
        return result;
    }

    public DataService getGridDataService() {
        return this.gridDataService;
    }

    public void setGridDataService(DataService gridDataService) {
        this.gridDataService = gridDataService;
    }

    public void initialize() {
        try {
            this.projectNode = (DefaultMutableTreeNode) this.getRoot();
            DefaultMutableTreeNode organizationNode = new DefaultMutableTreeNode("Local Organization");
            CaTIES_Organization organization = new CaTIES_Organization();
            organization.setName("Local Organization");
            organizationNode.setUserObject(organization);
            insertNodeInto(organizationNode, this.projectNode, this.projectNode.getChildCount());
            buildHistogram(organizationNode);
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    public void searchDocuments(String documentID) {
        try {
            String sqlQueryString = "select UUID, COLLECTION_DATE_TIME  from " + "pathology_report ";
            sqlQueryString += "where ";
            sqlQueryString += "UUID = '";
            sqlQueryString += documentID;
            sqlQueryString += "'";
            logger.debug("\nPerforming SQL query: " + sqlQueryString);
            SQLQuery query = new SQLQuery(sqlQueryString);
            WebRowSet rowset = new WebRowSet(query.getOutput());
            ActivityRequest request = new ActivityRequest();
            request.add(query);
            request.add(rowset);
            this.gridDataService.perform(request);
            ResultSet result = rowset.getResultSet();
            while (result.next()) {
                documentID = result.getString(1);
                java.sql.Date aquisitionDate = result.getDate(2);
                String formattedDate = CaTIES_FormatUtils.formatTimestampForMySQL(new Timestamp(aquisitionDate.getTime()));
                DefaultMutableTreeNode documentNode = new DefaultMutableTreeNode(documentID + " " + formattedDate);
                CaTIES_Document caTiesDocument = new CaTIES_Document();
                caTiesDocument.setUuid(documentID);
                documentNode.setUserObject(caTiesDocument);
                insertNodeInto(documentNode, this.projectNode, 0);
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    protected void buildHistogram(DefaultMutableTreeNode parent) {
        try {
            Timestamp startDate = getStartDate();
            Timestamp endDate = getEndDate();
            Calendar startDateCalendar = new GregorianCalendar();
            startDateCalendar.setTime(new java.util.Date(startDate.getTime()));
            startDateCalendar.set(Calendar.DAY_OF_MONTH, 1);
            startDateCalendar.set(Calendar.HOUR_OF_DAY, 0);
            startDateCalendar.set(Calendar.MINUTE, 0);
            startDateCalendar.set(Calendar.SECOND, 0);
            startDateCalendar.set(Calendar.MILLISECOND, 0);
            Calendar nextDateCalendar = new GregorianCalendar();
            nextDateCalendar.setTime(new Date(startDateCalendar.getTime().getTime()));
            nextDateCalendar.add(Calendar.MONTH, 1);
            nextDateCalendar.add(Calendar.MILLISECOND, -1);
            Calendar endDateCalendar = new GregorianCalendar();
            endDateCalendar.setTime(new Date(endDate.getTime()));
            endDateCalendar.set(Calendar.DAY_OF_MONTH, 1);
            endDateCalendar.set(Calendar.HOUR_OF_DAY, 0);
            endDateCalendar.set(Calendar.MINUTE, 0);
            endDateCalendar.set(Calendar.SECOND, 0);
            endDateCalendar.set(Calendar.MILLISECOND, 0);
            endDateCalendar.add(Calendar.MONTH, 1);
            endDateCalendar.add(Calendar.MILLISECOND, -1);
            DefaultMutableTreeNode yearNode = null;
            int lastPrincipalYear = -1;
            while (startDateCalendar.getTime().getTime() < endDateCalendar.getTime().getTime()) {
                logger.debug("Month bin start = " + startDateCalendar.getTime() + " end = " + nextDateCalendar.getTime());
                int principalYear = startDateCalendar.get(Calendar.YEAR);
                if (principalYear != lastPrincipalYear) {
                    yearNode = new DefaultMutableTreeNode(principalYear + "");
                    insertNodeInto(yearNode, parent, 0);
                    lastPrincipalYear = principalYear;
                }
                int principalMonth = startDateCalendar.get(Calendar.MONTH);
                DefaultMutableTreeNode monthNode = new DefaultMutableTreeNode(monthNames[principalMonth]);
                insertNodeInto(monthNode, yearNode, 0);
                Vector byWeekHistogram = histogramByWeek(startDateCalendar, nextDateCalendar);
                for (int idx = 0; idx < byWeekHistogram.size() - 1; idx++) {
                    int principalWeek = idx;
                    java.util.Date binStartDate = (java.util.Date) byWeekHistogram.elementAt(idx);
                    java.util.Date binEndDate = (java.util.Date) byWeekHistogram.elementAt(idx + 1);
                    logger.debug("Week bin start = " + binStartDate + " end = " + binEndDate);
                    String corpusName = principalYear + " " + monthNames[principalMonth] + " Week #" + principalWeek;
                    logger.debug("[SpinReportLoader] Checking Corpus ==> " + corpusName);
                    DefaultMutableTreeNode weekNode = new DefaultMutableTreeNode("week " + principalWeek);
                    CaTIES_CaseInterval caseInterval = new CaTIES_CaseInterval();
                    caseInterval.setName("Week #" + principalWeek);
                    if (binStartDate.getTime() == binEndDate.getTime()) {
                        caseInterval.setMin(new Timestamp(binStartDate.getTime()));
                        caseInterval.setMax(new Timestamp(binEndDate.getTime() + (24L * 60L * 60L * 1000L) - 1L));
                    } else {
                        caseInterval.setMin(new Timestamp(binStartDate.getTime()));
                        caseInterval.setMax(new Timestamp(binEndDate.getTime() - 1L));
                    }
                    caseInterval.setCaseCount(-1);
                    weekNode.setUserObject(caseInterval);
                    insertNodeInto(weekNode, monthNode, 0);
                }
                startDateCalendar.add(Calendar.MONTH, 1);
                nextDateCalendar.setTime(new Date(startDateCalendar.getTime().getTime()));
                nextDateCalendar.add(Calendar.MONTH, 1);
                nextDateCalendar.add(Calendar.MILLISECOND, -1);
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    /**
	 * @param startDateCalendar
	 * @param nextDateCalendar
	 * @return
	 */
    protected Vector histogramByWeek(Calendar startDateCalendar, Calendar endDateCalendar) {
        logger.debug("Start = " + startDateCalendar.getTime() + " End = " + endDateCalendar.getTime());
        Hashtable weekStartTimes = new Hashtable();
        Calendar nextDateCalendar = new GregorianCalendar();
        nextDateCalendar.setTime(new Date(startDateCalendar.getTime().getTime()));
        while (!nextDateCalendar.getTime().after(endDateCalendar.getTime())) {
            int weekOfMonth = nextDateCalendar.get(Calendar.WEEK_OF_MONTH);
            logger.debug("weekOfMonth = " + weekOfMonth + " nextDateCalendar = " + nextDateCalendar.getTime());
            if (weekStartTimes.get(new Integer(weekOfMonth)) == null) {
                logger.debug("Adding week start weekOfMonth = " + weekOfMonth + " nextDateCalendar = " + nextDateCalendar.getTime());
                weekStartTimes.put(new Integer(weekOfMonth), nextDateCalendar.getTime());
            }
            nextDateCalendar.add(Calendar.HOUR, 24);
        }
        Vector byWeekHistogram = new Vector(weekStartTimes.size() + 1);
        int idx = 0;
        for (; idx < weekStartTimes.size(); idx++) {
            byWeekHistogram.add(weekStartTimes.get(new Integer(idx + 1)));
        }
        byWeekHistogram.add(endDateCalendar.getTime());
        for (int jdx = 0; jdx < byWeekHistogram.size(); jdx++) {
            logger.debug("byWeekHistogram[" + jdx + "] = " + (java.util.Date) byWeekHistogram.elementAt(jdx));
        }
        return byWeekHistogram;
    }

    protected Timestamp getStartDate() {
        Timestamp startDate = null;
        try {
            String sqlQueryString = "select min(COLLECTION_DATE_TIME) START_DATE ";
            sqlQueryString += "from ";
            sqlQueryString += "pathology_report ";
            sqlQueryString += "where COLLECTION_DATE_TIME is not null";
            logger.debug("\nPerforming SQL query: " + sqlQueryString);
            SQLQuery query = new SQLQuery(sqlQueryString);
            WebRowSet rowset = new WebRowSet(query.getOutput());
            ActivityRequest request = new ActivityRequest();
            request.add(query);
            request.add(rowset);
            this.gridDataService.perform(request);
            ResultSet result = rowset.getResultSet();
            while (result.next()) {
                startDate = result.getTimestamp(1);
                break;
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
        return startDate;
    }

    protected Timestamp getEndDate() {
        Timestamp endDate = null;
        try {
            String sqlQueryString = "select max(COLLECTION_DATE_TIME) START_DATE ";
            sqlQueryString += "from ";
            sqlQueryString += "pathology_report ";
            sqlQueryString += "where COLLECTION_DATE_TIME is not null";
            logger.debug("\nPerforming SQL query: " + sqlQueryString);
            SQLQuery query = new SQLQuery(sqlQueryString);
            WebRowSet rowset = new WebRowSet(query.getOutput());
            ActivityRequest request = new ActivityRequest();
            request.add(query);
            request.add(rowset);
            this.gridDataService.perform(request);
            ResultSet result = rowset.getResultSet();
            while (result.next()) {
                endDate = result.getTimestamp(1);
                break;
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
        return endDate;
    }

    public boolean isWeekNode(DefaultMutableTreeNode node) {
        return node.getUserObject() != null && node.getUserObject() instanceof CaTIES_CaseInterval;
    }

    public boolean isCaseNode(DefaultMutableTreeNode node) {
        return node.getUserObject() != null && node.getUserObject() instanceof CaTIES_Document;
    }

    public void populateWeekNode(DefaultMutableTreeNode parent) {
        try {
            if (parent == null || parent.getChildCount() > 0) {
                ;
            } else {
                CaTIES_CaseInterval caseInterval = (CaTIES_CaseInterval) parent.getUserObject();
                Timestamp binStartDate = caseInterval.getMin();
                Timestamp binEndDate = caseInterval.getMax();
                String sqlQueryString = "select UUID, COLLECTION_DATE_TIME  from " + ".pathology_report ";
                sqlQueryString += "where ";
                sqlQueryString += "COLLECTION_DATE_TIME >= '";
                sqlQueryString += CaTIES_FormatUtils.formatTimestampForMySQL(binStartDate);
                sqlQueryString += "'";
                sqlQueryString += " and ";
                sqlQueryString += "COLLECTION_DATE_TIME < '";
                sqlQueryString += CaTIES_FormatUtils.formatTimestampForMySQL(binEndDate);
                sqlQueryString += "'";
                sqlQueryString += " order by COLLECTION_DATE_TIME";
                logger.debug("\nPerforming SQL query: " + sqlQueryString);
                SQLQuery query = new SQLQuery(sqlQueryString);
                WebRowSet rowset = new WebRowSet(query.getOutput());
                ActivityRequest request = new ActivityRequest();
                request.add(query);
                request.add(rowset);
                this.gridDataService.perform(request);
                ResultSet result = rowset.getResultSet();
                caseInterval.setCaseCount(0);
                while (result.next()) {
                    String documentID = result.getString(1);
                    java.sql.Date aquisitionDate = result.getDate(2);
                    String formattedDate = CaTIES_FormatUtils.formatTimestampForMySQL(new Timestamp(aquisitionDate.getTime()));
                    DefaultMutableTreeNode documentNode = new DefaultMutableTreeNode(documentID + " " + formattedDate);
                    CaTIES_Document caTiesDocument = new CaTIES_Document();
                    caTiesDocument.setUuid(documentID);
                    documentNode.setUserObject(caTiesDocument);
                    insertNodeInto(documentNode, parent, parent.getChildCount());
                    caseInterval.setCaseCount(caseInterval.getCaseCount() + 1);
                }
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    public DefaultMutableTreeNode getSelectedNode() {
        return this.selectedNode;
    }

    public void setSelectedNode(DefaultMutableTreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }

    public GlobusCredential getGlobusCredential() {
        return globusCredential;
    }

    public void setGlobusCredential(GlobusCredential globusCredential) {
        this.globusCredential = globusCredential;
    }

    public static void main(String[] args) {
        try {
            BasicConfigurator.configure();
            logger.setLevel(Level.DEBUG);
            CaTIES_HistogramTreeModel treeModel = CaTIES_HistogramTreeModel.getInstance();
            JTree histogramTree = new JTree(treeModel);
            JScrollPane treeScrollPane = new JScrollPane(histogramTree);
            JFrame frame = new JFrame("CaTIES Histogram Tree Model");
            frame.getContentPane().add(treeScrollPane, BorderLayout.CENTER);
            frame.addWindowListener(new WindowAdapter() {

                public void windowClosing(WindowEvent e) {
                    int n = JOptionPane.showConfirmDialog(CaTIES_HistogramTreeModel.frame, "Are you sure you want to exit\n" + "the CaTIES_DataStoreViewer?", "Really Quit?", JOptionPane.YES_NO_OPTION);
                    if (n == JOptionPane.YES_OPTION) {
                        System.exit(0);
                    }
                }
            });
            CaTIES_Utils.centerComponent(frame);
            frame.pack();
            frame.setVisible(true);
        } catch (Exception x) {
            x.printStackTrace();
        }
    }
}
