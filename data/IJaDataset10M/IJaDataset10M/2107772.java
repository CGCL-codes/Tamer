package grouprecommendations.experiment;

import java.net.HttpURLConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import grouprecommendations.model.FeedSource;
import grouprecommendations.model.Link;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import constants.IDatabaseConstants;
import database.DBAccessPool;
import grouprecommendations.util.IConstants;
import grouprecommendations.util.StringUtils;
import grouprecommendations.Database.DBFunctions;

/**
 *
 * @author freddurao
 */
public class ImportMecoDataMain {

    /**
     * @param args the command line arguments
     * @throws SchedulerException 
     * @throws JobExecutionException 
     *  delete FROM `groupmender`.`termFrequency`;
		delete FROM `groupmender`.`users_link_rate`;
		delete  FROM `groupmender`.`link`;
		delete FROM `groupmender`.`webpage`;
		delete FROM `groupmender`.`source`;
     * @throws ParseException 
     * 
     */
    static Timestamp lastImport = null;

    static int daysIntervalForImportData = 1;

    public static void main(String[] args) throws JobExecutionException, SchedulerException, ParseException {
        int[] codes = new int[2];
        codes[0] = HttpURLConnection.HTTP_NOT_FOUND;
        codes[1] = HttpURLConnection.HTTP_FORBIDDEN;
        for (int i = 0; i < 10; i++) {
            List<Link> links = new ArrayList<Link>();
            ImportMecoDataMain.getDocumentsFromMecoDB(links, i);
            insertMecoDataIntoGroupMenderDB(codes, links);
        }
    }

    /**
	 * @param codes
	 * @param links
	 */
    private static void insertMecoDataIntoGroupMenderDB(int[] codes, List<Link> links) {
        DBFunctions dbFunctions = new DBFunctions();
        List<FeedSource> feedList = new ArrayList<FeedSource>();
        for (Link link : links) {
            if (!StringUtils.isPageOfCode(link.getUrlString(), codes)) {
                dbFunctions.insertIntoMecoDocsTable(link);
            }
        }
        dbFunctions.updateLastImportMecoDocsTable(daysIntervalForImportData);
        for (Link link : dbFunctions.getMecoDocs()) {
            FeedSource feed = new FeedSource(link.getUrlString());
            feed.updateLinkList(true);
            feedList.add(feed);
        }
    }

    /**
     * The new input from MecoDB will start from the date corresponding to the most recent document imported from meco db.
     * @return
     */
    public static Timestamp getDateOfMostRecentDocumentImportedFromMecoDB() {
        DBFunctions dbFunctions = new DBFunctions();
        Timestamp timestamp = dbFunctions.getDateOfMostRecentDocumentImportedFromMecoDB();
        return timestamp;
    }

    public static Timestamp getLastImportFromMecoDB() {
        DBFunctions dbFunctions = new DBFunctions();
        Timestamp timestamp = dbFunctions.getLastImportFromMecoDB();
        return timestamp;
    }

    /**
     *  X documents will be retrieved everyday from Meco DB and the top Y will be recommended.
     *  In the second unit of time (2X-Y) will be analyzed and  (2X-Y) - Y documents will be recommended. An so fourth...
     *  The unit of time is customized.  
     * @param links
     * @param mostRecentDocumentDate
     * @throws ParseException
     */
    public static void getDocumentsFromMecoDB(List<Link> links, int addedDaysToLastRetrieval) throws ParseException {
        Timestamp mostRecentDocumentDate = getLastImportFromMecoDB();
        System.out.println("Retrieving data from " + mostRecentDocumentDate.toString());
        String queryDate = (mostRecentDocumentDate == null) ? "" : "and i.CreatedAt = '" + mostRecentDocumentDate + "'";
        System.out.println("retrieving documents of date " + queryDate);
        int amountDocumentsToBeRetrieved = 5;
        String queryAmountDocumentToRetrieve = (mostRecentDocumentDate == null) ? "top " + amountDocumentsToBeRetrieved + "" : "";
        String GuidUser = "D23F654E-6AC2-4A1B-A864-E7EA6257AE8C";
        int locationId = 10101205;
        String medicalConditionId = "(1001,168)";
        try {
            DBAccessPool mecoDBAccess = new DBAccessPool(IDatabaseConstants.DB_IP_ADDRESS, IDatabaseConstants.DB_SCHEMA, IDatabaseConstants.DB_INSTANCE, IDatabaseConstants.DB_USERNAME, IDatabaseConstants.DB_PASSWORD, IDatabaseConstants.DB_DRIVER, IDatabaseConstants.DB_SERVER);
            String sql = "select distinct " + queryAmountDocumentToRetrieve + " s.IdSignal, d.IdDocument, d.Title, d.Timestamp, d.Link, mc.name as medicalConditionName, l.name as locationName" + " from dbo.IndicatorDocument id ," + "     Data.Document d, " + "     Data.signal s, " + "	  dbo.MedicalCondition mc , " + "	  Meta.Location l , " + "     Data.Indicator i, " + "     Data.SignalDefinition sd, " + "     dbo.IndicatorMedicalCondition imc, " + "     dbo.IndicatorLocation il " + " where id.IdDocument = d.IdDocument  and id.IdIndicator = i.IdIndicator" + "  and s.IdIndicator = i.IdIndicator   and s.IdSignalDefinition = sd.IdSignalDefinition" + "  and i.IdIndicator = imc.IdIndicator and i.IdIndicator = il.IdIndicator" + "  and imc.IdMedicalCondition = mc.IdMedical and l.IdLocation = il.IdLocation" + "  and sd.GuidUser   =  '" + GuidUser + "' " + queryDate + "  and il.IdLocation = " + locationId + "" + "  and imc.IdMedicalCondition in " + medicalConditionId + "" + "  and d.link  is not null and d.link  <> ''" + "  and d.Title is not null and d.Title <> '' order by d.Timestamp";
            System.out.println(sql);
            PreparedStatement ps = mecoDBAccess.prepareStatement(sql);
            ResultSet rs = mecoDBAccess.ExecQuery(ps);
            while (rs != null && rs.next()) {
                Link link = new Link(new Integer(rs.getInt("IdDocument")).longValue(), null, null, rs.getString("Title"), StringUtils.convertClobToString(rs.getClob("Link")), rs.getTimestamp("Timestamp"), 0F, 0L);
                link.setSignalMessage(createSinalMessage(rs.getString("medicalConditionName"), rs.getString("LocationName"), rs.getInt("IdSignal")));
                links.add(link);
            }
            mecoDBAccess.closeConnection(ps);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * @param medicalCondition
     * @param location
     * @return
     * @throws ParseException
     */
    public static String createSinalMessage(String medicalCondition, String location, int signalId) throws ParseException {
        String signalMessage = "Attention: there is an increasing activity on " + medicalCondition + " in " + location + " area. Check it out at " + signalId;
        System.out.println(signalMessage);
        return signalMessage;
    }
}
