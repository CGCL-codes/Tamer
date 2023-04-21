package net.sourceforge.olduvai.lrac.jdbcswiftdataservice;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.swing.JFrame;
import net.sourceforge.olduvai.lrac.TimeRangeSampleIntervalRelation;
import net.sourceforge.olduvai.lrac.drawer.queries.DetailQuery;
import net.sourceforge.olduvai.lrac.drawer.queries.SwatchQuery;
import net.sourceforge.olduvai.lrac.drawer.structure.strips.Strip;
import net.sourceforge.olduvai.lrac.drawer.structure.templates.Template;
import net.sourceforge.olduvai.lrac.genericdataservice.AbstractDataServiceDispatcher;
import net.sourceforge.olduvai.lrac.genericdataservice.DataResultInterface;
import net.sourceforge.olduvai.lrac.genericdataservice.StripDiskLoader;
import net.sourceforge.olduvai.lrac.genericdataservice.TemplateDiskLoader;
import net.sourceforge.olduvai.lrac.genericdataservice.TimeRangeIntervalRelationDiskLoader;
import net.sourceforge.olduvai.lrac.genericdataservice.cellviewer.AbstractCellViewer;
import net.sourceforge.olduvai.lrac.genericdataservice.cellviewer.GenericCellViewer;
import net.sourceforge.olduvai.lrac.genericdataservice.queries.ActiveSourceQueryInterface;
import net.sourceforge.olduvai.lrac.genericdataservice.queries.QueryInterface;
import net.sourceforge.olduvai.lrac.genericdataservice.queries.SwatchQueryInterface;
import net.sourceforge.olduvai.lrac.genericdataservice.records.SwatchRecordInterface;
import net.sourceforge.olduvai.lrac.genericdataservice.structure.DataCellInterface;
import net.sourceforge.olduvai.lrac.genericdataservice.structure.FocusGroupInterface;
import net.sourceforge.olduvai.lrac.genericdataservice.structure.InputChannelGroupInterface;
import net.sourceforge.olduvai.lrac.genericdataservice.structure.InputChannelInterface;
import net.sourceforge.olduvai.lrac.genericdataservice.structure.SourceGroupInterface;
import net.sourceforge.olduvai.lrac.genericdataservice.structure.SourceInterface;
import net.sourceforge.olduvai.lrac.jdbcswiftdataservice.structure.FocusGroup;
import net.sourceforge.olduvai.lrac.jdbcswiftdataservice.structure.SourceGroup;
import net.sourceforge.olduvai.lrac.util.Util;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * Implementation of the DataServiceDispatcher for LiveRAC.  
 * 
 * @author peter
 *
 */
public class DataServiceDispatcher extends AbstractDataServiceDispatcher {

    public static final String DEFAULTSOURCEGROUPFILE = "groups.xml";

    public static final String FOCUSGROUPFILE = "focusgroups.xml";

    public static final String DEFAULTTEMPLATEFILE = "templates.xml";

    public static final String DEFAULTTIMERANGEINTERVALRELATIONFILE = "intervals.xml";

    /**
	 * Default strip definition file distributed with LiveRAC
	 */
    public static final String DEFAULTSTRIPFILE = "strips.xml";

    public static final String[] PREDEFINEDPROFILEFOLDERS = { "bbb", "user" };

    public static final String[] PREDEFINEDPROFILENAMES = { "BBB", "User defined" };

    public static final String DEFAULTCONNECTIONURL = "jdbc:mysql://10.211.55.5/swift";

    public static final String DEFAULTPARTICIPANT = "blank";

    public static final String SYSTEMPREFSFOLDER = "net/sourceforge/olduvai/lrac/jdbcswiftdataservice/config/";

    public static final String USERPREFSFOLDER = System.getProperty("user.home") + "/LiveRAC/";

    static final String DEFAULTUSERCONNINFOFILE = System.getProperty("user.home") + "/LiveRAC/swiftconn.txt";

    /**
	 * Flagged to true to bypass connection dialog box and use pre-specified FIXEDCONNECT 
	 * URL, username/pass, and dates
	 */
    private static final boolean FIXEDCONNECT = false;

    private static final String FIXEDCONNECTIONURL = "jdbc:mysql://10.211.55.3/swift";

    private static final String FIXEDUSERNAME = "swiftdemo";

    private static final String FIXEDPASSWORD = "swiftdemo";

    private static final int FIXEDPROFILEINDEX = 0;

    private static final Date FIXEDBEGINDATE = new Date("08/23/07 12:0 AM, EDT");

    private static final Date FIXEDENDDATE = new Date("08/23/07 6:0 PM, EDT");

    static final double INTERVAL_5MINUTE = 12d;

    static final double INTERVAL_HOUR = 1d;

    static final double INTERVAL_DAY = 1 / 24;

    static final String INTERVAL_5MINUTE_DESC = "Every five minutes";

    static final String INTERVAL_HOUR_DESC = "Hourly";

    static final String INTERVAL_DAY_DESC = "Daily";

    static final String[] sampleIntervalDescriptions = { INTERVAL_5MINUTE_DESC, INTERVAL_HOUR_DESC, INTERVAL_DAY_DESC };

    private static final double[] supportedSampleIntervals = { INTERVAL_5MINUTE, INTERVAL_HOUR, INTERVAL_DAY };

    private static String selectedProfile;

    private static boolean loadProfileDefault;

    protected static String participantId = "swiftdemo";

    protected static String password = "swiftdemo";

    protected static String jdbcURL = DEFAULTCONNECTIONURL;

    /**
	 * The reader handles the actual nitty gritty interactions with the SWIFT jdbc source 
	 */
    SwiftJDBCReader reader = null;

    List<SourceGroupInterface> allSourceGroupList;

    List<InputChannelInterface> allChannelList;

    List<InputChannelGroupInterface> allChannelGroupList;

    /**
	 * Maps from an internal name to a channel group.  Uses: 
	 * 1) bind strips to the appropriate input channel group.
	 * 
	 */
    private Map<String, InputChannelGroupInterface> internalNameToInputChannelGroup;

    /**
	 * Maps from an internal name to an input channel.  This is used for two things: 
	 * 1) bind strips to the appropriate input channel.
	 * 2) incoming swatch / detail data only has the channel name, we need an efficient way to go from the name to the object  
	 */
    private Map<String, InputChannelInterface> internalNameToInputChannel;

    protected static String getParticipantId() {
        return participantId;
    }

    protected static String getPassword() {
        return password;
    }

    public static String getJDBCurl() {
        final String dbUrl = jdbcURL + "?user=" + getParticipantId() + "&password=" + getPassword();
        return dbUrl;
    }

    public static String getSelectedProfileName() {
        return selectedProfile;
    }

    public static String getSelectedProfileSystemPath() {
        return SYSTEMPREFSFOLDER + selectedProfile;
    }

    public static String getSelectedProfileUserPath() {
        return USERPREFSFOLDER + selectedProfile;
    }

    /**
	 * @return the loadProfileDefault
	 */
    public static boolean isLoadProfileDefault() {
        return loadProfileDefault;
    }

    private static void setCgiUrl(String connectUrl) {
        jdbcURL = connectUrl;
    }

    /**
	 * If the loadProfileDefault flag is set the user's preferences are discarded and the 
	 * profile 'defaults' are loaded.  
	 * 
	 * @param loadProfileDefault the loadProfileDefault to set
	 */
    public static void setLoadProfileDefault(boolean loadProfileDefault) {
        DataServiceDispatcher.loadProfileDefault = loadProfileDefault;
    }

    private static void setParticipantId(String newParticipantId) {
        participantId = newParticipantId;
    }

    static void setPassword(String newPassword) {
        password = newPassword;
    }

    public static void setSelectedProfile(int profileID) {
        selectedProfile = PREDEFINEDPROFILEFOLDERS[profileID] + "/";
    }

    /**
	 * Initialize the SWIFT reader in addition to linking the result interface.
	 * @param ri
	 */
    public DataServiceDispatcher(DataResultInterface ri) {
        super(ri);
        reader = new SwiftJDBCReader();
    }

    protected void buildRunQueue(List<QueryInterface> requestQueue, List<QueryInterface> runQueue) {
        boolean seenSwatchQuery = false;
        Date startDate = null;
        Date endDate = null;
        synchronized (requestQueue) {
            QueryInterface query;
            for (int i = requestQueue.size() - 1; i >= 0; i--) {
                query = requestQueue.get(i);
                if (query instanceof SwatchQuery) {
                    if (seenSwatchQuery == false) {
                        runQueue.add(query);
                        seenSwatchQuery = true;
                    }
                } else if (query instanceof DetailQuery) {
                    DetailQuery detailQuery = (DetailQuery) query;
                    if (startDate == null && endDate == null) {
                        startDate = detailQuery.getBeginDate();
                        endDate = detailQuery.getEndDate();
                    }
                    if (startDate == detailQuery.getBeginDate() && endDate == detailQuery.getEndDate()) {
                        runQueue.add(detailQuery);
                    }
                }
                requestQueue.remove(i);
            }
        }
    }

    /**
	 * TODO: not yet implemented
	 */
    public boolean cancelQuery(int queryNumber) {
        System.err.println("Query cancelling not yet implemented for SWIFT");
        return false;
    }

    public Date[] connect(JFrame mainFrame) throws IOException {
        Date[] dates = null;
        if (FIXEDCONNECT) {
            setCgiUrl(FIXEDCONNECTIONURL);
            setParticipantId(FIXEDUSERNAME);
            setPassword(FIXEDPASSWORD);
            setSelectedProfile(FIXEDPROFILEINDEX);
            dates = new Date[] { FIXEDBEGINDATE, FIXEDENDDATE };
        } else {
            ConnectionDialog connectDialog = new ConnectionDialog(mainFrame);
            connectDialog.setVisible(true);
            setCgiUrl(connectDialog.getConnectUrl());
            setParticipantId(connectDialog.getParticipantId());
            setPassword(connectDialog.getPassword());
            Date beginDate = connectDialog.getBeginDate();
            Date endDate = connectDialog.getEndDate();
            setSelectedProfile(connectDialog.getSelectedProfileIndex());
            if (beginDate != null && endDate != null) {
                dates = new Date[] { beginDate, endDate };
            }
        }
        if (dates != null) {
            reader.init();
            copySelectedProfile();
            getInputChannels();
            getInputChannelGroups();
            getAllSourceGroups();
        }
        return dates;
    }

    /**
	 * Copies the profile out of the java package into the user's folder structure
	 * @param currProfile
	 */
    private void copySelectedProfile() {
        final String currProfile = getSelectedProfileName();
        if (Util.folderExists(DataServiceDispatcher.USERPREFSFOLDER + currProfile + "/")) {
            return;
        }
        Util.createFolder(DataServiceDispatcher.USERPREFSFOLDER + currProfile + "/");
        ClassLoader cl = this.getClass().getClassLoader();
        String stripLocation = DataServiceDispatcher.getSelectedProfileSystemPath() + DataServiceDispatcher.DEFAULTSTRIPFILE;
        String stripString = Util.getInputStreamAsString(cl.getResourceAsStream(stripLocation));
        Util.saveStringToFile(DataServiceDispatcher.getSelectedProfileUserPath() + DataServiceDispatcher.DEFAULTSTRIPFILE, stripString);
        String templateString = Util.getInputStreamAsString(cl.getResourceAsStream(DataServiceDispatcher.getSelectedProfileSystemPath() + DataServiceDispatcher.DEFAULTTEMPLATEFILE));
        Util.saveStringToFile(DataServiceDispatcher.getSelectedProfileUserPath() + DataServiceDispatcher.DEFAULTTEMPLATEFILE, templateString);
        String focusString = Util.getInputStreamAsString(cl.getResourceAsStream(DataServiceDispatcher.getSelectedProfileSystemPath() + DataServiceDispatcher.FOCUSGROUPFILE));
        Util.saveStringToFile(DataServiceDispatcher.getSelectedProfileUserPath() + DataServiceDispatcher.FOCUSGROUPFILE, focusString);
        String intervalString = Util.getInputStreamAsString(cl.getResourceAsStream(DataServiceDispatcher.getSelectedProfileSystemPath() + DataServiceDispatcher.DEFAULTTIMERANGEINTERVALRELATIONFILE));
        Util.saveStringToFile(DataServiceDispatcher.getSelectedProfileUserPath() + DataServiceDispatcher.DEFAULTTIMERANGEINTERVALRELATIONFILE, intervalString);
        String groupPath = DataServiceDispatcher.getSelectedProfileSystemPath() + DataServiceDispatcher.DEFAULTSOURCEGROUPFILE;
        String groupString = Util.getInputStreamAsString(cl.getResourceAsStream(groupPath));
        Util.saveStringToFile(DataServiceDispatcher.getSelectedProfileUserPath() + DataServiceDispatcher.DEFAULTSOURCEGROUPFILE, groupString);
    }

    /**
	 * Used by the file loaders to retrieve either the user specified version of a file 
	 * for a given profile, or the system default version of the file.  
	 * @param fileName
	 * 
	 * @return
	 */
    final InputStream getFileStream(String fileName) {
        final String defaultSystemFile = DataServiceDispatcher.getSelectedProfileSystemPath() + fileName;
        final String defaultUserFile = DataServiceDispatcher.getSelectedProfileUserPath() + fileName;
        return Util.chooseStream(this.getClass().getClassLoader(), defaultSystemFile, defaultUserFile, isLoadProfileDefault());
    }

    public List<FocusGroupInterface> loadSavedFocusGroups() throws IOException {
        return FocusGroup.loadFocusGroups(getFileStream(FOCUSGROUPFILE));
    }

    public List<InputChannelGroupInterface> getInputChannelGroups() {
        if (allChannelGroupList == null) {
            try {
                allChannelGroupList = reader.getInputChannelGroups(internalNameToInputChannel);
            } catch (IOException e) {
                allChannelGroupList = new ArrayList<InputChannelGroupInterface>();
            }
            Map<String, InputChannelGroupInterface> newGroupMap = new HashMap<String, InputChannelGroupInterface>(allChannelGroupList.size());
            for (Iterator<InputChannelGroupInterface> it = allChannelGroupList.iterator(); it.hasNext(); ) {
                final InputChannelGroupInterface channelGroup = it.next();
                newGroupMap.put(channelGroup.getInternalName(), channelGroup);
            }
            internalNameToInputChannelGroup = newGroupMap;
        }
        return allChannelGroupList;
    }

    public List<InputChannelInterface> getInputChannels() {
        if (allChannelList == null) {
            try {
                allChannelList = reader.getInputChannels();
            } catch (IOException e) {
                System.err.println("DataServiceDispatcher: Error loading input channels");
                allChannelList = new ArrayList<InputChannelInterface>();
            }
            Map<String, InputChannelInterface> newChannelMap = new HashMap<String, InputChannelInterface>(allChannelList.size());
            for (Iterator<InputChannelInterface> it = allChannelList.iterator(); it.hasNext(); ) {
                final InputChannelInterface channel = it.next();
                newChannelMap.put(channel.getInternalName(), channel);
            }
            internalNameToInputChannel = newChannelMap;
        }
        return allChannelList;
    }

    public Set<SourceInterface> loadActiveSources(ActiveSourceQueryInterface query) throws IOException {
        return reader.getSourceSet(query);
    }

    public List<SourceGroupInterface> getAllSourceGroups() {
        if (allSourceGroupList == null) {
            try {
                allSourceGroupList = reader.getSourceGroups();
            } catch (IOException e) {
                System.err.println("DataServiceDispatcher: error loading all source groups");
                System.out.println(e.getMessage());
                allSourceGroupList = new ArrayList<SourceGroupInterface>();
            }
        }
        return allSourceGroupList;
    }

    /**
	 * TODO: implement for SWIFT? 
	 */
    public List<SourceInterface> getAllSources() {
        System.err.println("Can't view list of all sources for SWIFT at this time.  Only source group list is available.");
        return new ArrayList<SourceInterface>(0);
    }

    public List<Strip> loadSavedStrips() throws IOException {
        return StripDiskLoader.loadStrips(getFileStream(DEFAULTSTRIPFILE));
    }

    public List<Template> loadSavedTemplates() throws IOException {
        return TemplateDiskLoader.loadTemplates(getFileStream(DEFAULTTEMPLATEFILE));
    }

    public void saveFocusGroups(List<FocusGroupInterface> focusGroups) throws IOException {
        FocusGroup.saveGroups(Util.getCreateFile(getSelectedProfileUserPath() + FOCUSGROUPFILE), focusGroups);
    }

    public void saveStrips(List<Strip> strips) throws IOException {
        StripDiskLoader.saveStrips(Util.getCreateFile(getSelectedProfileUserPath() + DEFAULTSTRIPFILE), strips);
    }

    public void saveTemplates(List<Template> templates) throws IOException {
        TemplateDiskLoader.saveTemplates(Util.getCreateFile(getSelectedProfileUserPath() + DEFAULTTEMPLATEFILE), templates);
    }

    @Override
    protected void serverRequest(QueryInterface req) throws IOException {
        if (req instanceof SwatchQueryInterface) {
            List<SwatchRecordInterface> result = reader.getCellSwatchValues((SwatchQueryInterface) req, this);
            if (result != null) resultInterface.swatchQueryResult(result);
        } else if (req instanceof DetailQuery) {
            resultInterface.detailQueryResult(reader.getChartData((DetailQuery) req));
        }
    }

    public DataResultInterface getResultInterface() {
        return resultInterface;
    }

    public List<SourceGroupInterface> loadSavedSourceGroups() {
        try {
            return SourceGroup.loadSelectedSourceGroups(allSourceGroupList, getFileStream(DEFAULTSOURCEGROUPFILE));
        } catch (IOException e) {
            System.err.println("DataServiceDispatcher: error loading saved source groups");
            return new ArrayList<SourceGroupInterface>();
        }
    }

    public String getConnectionName() {
        return jdbcURL;
    }

    public AbstractCellViewer getCellViewer(DataCellInterface cell, JFrame owner) {
        return GenericCellViewer.makeGenericCellViewer(cell, owner);
    }

    public double[] getSupportedSampleIntervals() {
        return supportedSampleIntervals;
    }

    public String getUserId() {
        return participantId;
    }

    public HSSFWorkbook makeExcelExport(List<DataCellInterface> cells) {
        System.err.println("TODO: Excel export not yet implemented");
        return null;
    }

    public SortedSet<TimeRangeSampleIntervalRelation> getTimeRangeSampleIntervalRelations() {
        try {
            return TimeRangeIntervalRelationDiskLoader.loadRelations(getFileStream(DEFAULTTIMERANGEINTERVALRELATIONFILE));
        } catch (IOException e) {
            System.err.println("DataServiceDispatcher: Error loading TimeRangeSampleIntervalRelations");
            return new TreeSet<TimeRangeSampleIntervalRelation>();
        }
    }

    public FocusGroupInterface newFocusGroup(String focusGroupName, int type) {
        return new FocusGroup(focusGroupName, type);
    }

    public void saveTimeRangeSampleIntervalRelations(List<TimeRangeSampleIntervalRelation> relations) throws IOException {
        TimeRangeIntervalRelationDiskLoader.saveRelations(getSelectedProfileUserPath() + "/" + DEFAULTTIMERANGEINTERVALRELATIONFILE, relations);
    }

    /**
	 * Right now we are not permitting individually selectable sources, so no user selections 
	 * are being saved either.  
	 *  
	 */
    public List<SourceInterface> loadSavedSources() throws IOException {
        return new ArrayList<SourceInterface>();
    }

    public void saveSelectedSourcesAndGroups(List<SourceGroupInterface> groups, List<SourceInterface> sources, List<SourceInterface> excludedSources, String sortKey) throws IOException {
        SourceGroup.saveSelectedSourceGroups(Util.getCreateFile(getSelectedProfileUserPath() + DEFAULTSOURCEGROUPFILE), groups, sortKey);
    }

    public Map<String, InputChannelGroupInterface> getInternalNameToInputChannelGroup() {
        return internalNameToInputChannelGroup;
    }

    public Map<String, InputChannelInterface> getInternalNameToInputChannel() {
        return internalNameToInputChannel;
    }

    public String getLocalDataPath() {
        return USERPREFSFOLDER;
    }
}
