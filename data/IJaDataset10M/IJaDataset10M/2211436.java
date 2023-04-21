package fr.soleil.snapArchivingApi.SnapManagerApi;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;
import fr.esrf.Tango.AttrDataFormat;
import fr.esrf.Tango.AttrWriteType;
import fr.esrf.Tango.DevFailed;
import fr.esrf.Tango.DevState;
import fr.esrf.Tango.ErrSeverity;
import fr.esrf.TangoApi.ApiUtil;
import fr.esrf.TangoApi.AttributeInfo;
import fr.esrf.TangoApi.AttributeProxy;
import fr.esrf.TangoApi.CommandInfo;
import fr.esrf.TangoApi.Database;
import fr.esrf.TangoApi.DeviceAttribute;
import fr.esrf.TangoApi.DeviceData;
import fr.esrf.TangoApi.DeviceProxy;
import fr.esrf.TangoDs.TangoConst;
import fr.soleil.snapArchivingApi.SnapshotingApi.ConfigConst;
import fr.soleil.snapArchivingApi.SnapshotingApi.DataBaseAPI;
import fr.soleil.snapArchivingApi.SnapshotingApi.GetConf;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.Criterions;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.GlobalConst;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.SnapAttributeExtract;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.SnapAttributeHeavy;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.SnapAttributeLight;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.SnapContext;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.SnapShot;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.SnapShotLight;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.SnapshotingException;

public class SnapManagerApi {

    private static final String DATA_BASE_API_NOT_INIT = "DataBaseAPI not initialized";

    private static final String m_LAUNCHSNAP = "LaunchSnapShot";

    private static final String m_CREATECONTEXT = "CreateNewContext";

    private static final String m_TRIGGERLAUNCHSNAP = "TriggerLaunchSnapShot";

    private static DataBaseAPI snapDataBase = null;

    private static final String m_snapArchiverClassDevice = "SnapArchiver";

    private static String[] m_snapArchiverList;

    private static final String m_snapManagerClassDevice = "SnapManager";

    private static String[] m_snapManagerList;

    private static final String m_snapBrowserClassDevice = "SnapExtractor";

    private static String[] m_snapBrowserList;

    /**
     * @return the Host of the snap database
     */
    public static String getSnapHost() {
        if (snapDataBase != null) {
            return snapDataBase.getHost();
        } else {
            return "";
        }
    }

    /**
     * @return the name of the snap database
     */
    public static String getSnapDatabaseName() {
        if (snapDataBase != null) {
            return snapDataBase.getDbName();
        } else {
            return "";
        }
    }

    /**
     * @return the user of the snap database
     */
    public static String getSnapUser() {
        if (snapDataBase != null) {
            return snapDataBase.getUser();
        } else {
            return "";
        }
    }

    /**
     * @return the password of the snap database
     */
    public static String getSnapPassword() {
        if (snapDataBase != null) {
            return snapDataBase.getPassword();
        } else {
            return "";
        }
    }

    /**
     * @return the snap database type (MySQL, ORACLE)...
     */
    public static int getSnapDbType() {
        if (snapDataBase != null) {
            return snapDataBase.getDb_type();
        }
        return -1;
    }

    /**
     * @return the facility property for that type of archiving.
     */
    private static boolean getFacility() {
        boolean facility = false;
        try {
            facility = GetConf.getFacility(m_snapArchiverClassDevice);
        } catch (SnapshotingException e) {
            System.err.println(e.toString());
        }
        return facility;
    }

    /**
     * This method returns the name of one of the running devices, according to
     * the given class
     * 
     * @param deviceClass
     *            The device's class
     * @return The device's name
     * @throws DevFailed
     */
    private static String chooseDevice(String deviceClass) throws DevFailed {
        String device_name = "";
        String[] devicesList = null;
        if (deviceClass == m_snapArchiverClassDevice) {
            initDeviceList(m_snapArchiverClassDevice);
            devicesList = m_snapArchiverList;
        } else if (deviceClass == m_snapManagerClassDevice) {
            initDeviceList(m_snapManagerClassDevice);
            devicesList = m_snapManagerList;
        } else if (deviceClass == m_snapBrowserClassDevice) {
            initDeviceList(m_snapBrowserClassDevice);
            devicesList = m_snapBrowserList;
        }
        Random hasard = new Random(System.currentTimeMillis());
        if (devicesList.length > 0) {
            int choosed_index = hasard.nextInt(devicesList.length);
            device_name = devicesList[choosed_index];
        }
        return device_name;
    }

    /**
     * This method gets all the running SnapArchivers and stores the name in the
     * m_snapArchiverList
     * 
     * @throws DevFailed
     */
    private static void initDeviceList(String deviceClass) throws DevFailed {
        Database dbase = ApiUtil.get_db_obj();
        String[] runningDeviceList = dbase.get_device_exported_for_class("*" + deviceClass + "*");
        String[] myRunningDeviceList = new String[runningDeviceList.length];
        int j = 0;
        for (int i = 0; i < runningDeviceList.length; i++) {
            if (deviceLivingTest(runningDeviceList[i], deviceClass)) {
                myRunningDeviceList[j] = runningDeviceList[i];
                j++;
            }
        }
        if (deviceClass == m_snapArchiverClassDevice) {
            m_snapArchiverList = new String[j];
            for (int i = 0; i < j; i++) {
                m_snapArchiverList[i] = myRunningDeviceList[i];
            }
        } else if (deviceClass == m_snapManagerClassDevice) {
            m_snapManagerList = new String[j];
            for (int i = 0; i < j; i++) {
                m_snapManagerList[i] = myRunningDeviceList[i];
            }
        } else if (deviceClass == m_snapBrowserClassDevice) {
            m_snapBrowserList = new String[j];
            for (int i = 0; i < j; i++) {
                m_snapBrowserList[i] = myRunningDeviceList[i];
            }
        }
    }

    /**
     * Tests if the given device is alive
     * 
     * @param deviceName
     * @return true if the device is running
     */
    private static boolean deviceLivingTest(String deviceName, String deviceClass) {
        try {
            DeviceProxy deviceProxy = new DeviceProxy(deviceName);
            deviceProxy.ping();
            CommandInfo[] commandList = deviceProxy.command_list_query();
            if (deviceClass == m_snapArchiverClassDevice) {
                for (int i = 0; i < commandList.length; i++) {
                    if (m_TRIGGERLAUNCHSNAP.equals(commandList[i].cmd_name)) {
                        return true;
                    }
                }
            } else if (deviceClass == m_snapManagerClassDevice) {
                for (int i = 0; i < commandList.length; i++) {
                    if (m_CREATECONTEXT.equals(commandList[i].cmd_name)) {
                        return true;
                    }
                    if (m_LAUNCHSNAP.equals(commandList[i].cmd_name)) {
                        return true;
                    }
                }
            }
            return false;
        } catch (DevFailed devFailed) {
            System.err.println("ERROR !! " + "\r\n" + "\t Origin : \t " + "SnapManagerApi.deviceLivingTest" + "\r\n" + "\t Reason : \t " + "SNAP_FAILURE" + "\r\n" + "\t Description : \t " + devFailed.getMessage() + "\r\n" + "\t Additional information : \t " + "The device " + deviceName + " does not answer..." + "\r\n");
            return false;
        }
    }

    private static String[] split_att_name_3_fields(String att_name) {
        String host = "";
        String domain = "";
        String family = "";
        String member = "";
        String attribut = "";
        String[] argout = new String[5];
        String[] decoupe;
        if (att_name.startsWith("//")) {
            att_name = att_name.substring(2, att_name.length());
        } else {
            att_name = "HOST:port/" + att_name;
        }
        decoupe = att_name.split("/");
        host = decoupe[0];
        domain = decoupe[1];
        family = decoupe[2];
        member = decoupe[3];
        attribut = decoupe[4];
        argout[0] = host;
        argout[1] = domain;
        argout[2] = family;
        argout[3] = member;
        argout[4] = attribut;
        return argout;
    }

    private static String getDefaultHost(final String host) {
        String hostResult = host;
        if (hostResult.isEmpty()) {
            try {
                hostResult = GetConf.getHost(m_snapManagerClassDevice);
                if ("".equals(hostResult)) {
                    hostResult = ConfigConst.default_shost;
                }
            } catch (SnapshotingException e) {
                System.err.println(e.toString());
                hostResult = ConfigConst.default_shost;
            }
        }
        return hostResult;
    }

    public static String getDefaultDbName(final String name) {
        String result = name;
        if (result.isEmpty()) {
            try {
                result = GetConf.getName(m_snapManagerClassDevice);
                if ("".equals(result)) {
                    result = ConfigConst.default_sbd;
                }
            } catch (SnapshotingException e) {
                System.err.println(e.toString());
                result = ConfigConst.default_sbd;
            }
        }
        return result;
    }

    public static String getDefaultSchema(final String name) {
        String result = name;
        if (result.isEmpty()) {
            try {
                result = GetConf.getSchema(m_snapManagerClassDevice);
                if ("".equals(result)) {
                    result = ConfigConst.default_sschema;
                }
            } catch (SnapshotingException e) {
                System.err.println(e.toString());
                result = ConfigConst.default_sschema;
            }
        }
        return result;
    }

    public static String getDefaultUser(final String name) {
        String result = name;
        if (result.isEmpty()) {
            try {
                result = GetConf.getUser(m_snapManagerClassDevice);
                if ("".equals(result)) {
                    result = ConfigConst.default_smuser;
                }
            } catch (SnapshotingException e) {
                System.err.println(e.toString());
                result = ConfigConst.default_smuser;
            }
        }
        return result;
    }

    public static String getDefaultPwd(final String name) {
        String result = name;
        if (result.isEmpty()) {
            try {
                result = GetConf.getPwd(m_snapManagerClassDevice);
                if ("".equals(result)) {
                    result = ConfigConst.default_smpasswd;
                }
            } catch (SnapshotingException e) {
                System.err.println(e.toString());
                result = ConfigConst.default_smpasswd;
            }
        }
        return result;
    }

    public static boolean getisRac(final String isRAC) {
        boolean result = Boolean.parseBoolean(isRAC);
        if (isRAC.isEmpty()) {
            try {
                String s = GetConf.isRAC(m_snapManagerClassDevice);
                if ("".equals(s)) {
                    result = false;
                } else {
                    result = Boolean.parseBoolean(s);
                }
            } catch (SnapshotingException e) {
                System.err.println(e.toString());
                result = false;
            }
        }
        return result;
    }

    public static synchronized void initSnapConnection(String host, String name, String schema, String user, String pass, String isRac) throws SnapshotingException {
        System.out.println("host: " + host);
        System.out.println("name: " + name);
        System.out.println("schema: " + schema);
        System.out.println("user: " + user);
        System.out.println("pass: " + pass);
        snapDataBase = new DataBaseAPI(getDefaultHost(host), getDefaultDbName(name), getDefaultSchema(schema), getDefaultUser(user), getDefaultPwd(pass), getisRac(isRac));
        snapDataBase.connect_auto();
    }

    /**
     * This method gets informations on a given attribute and registers the
     * attribute into the database "Snap"
     * 
     * @param att_complete_name
     *            the given attribute
     * @throws SnapshotingException
     *             exception throwned in case of communications problems with
     *             the device or database
     */
    private static void register(String att_complete_name) throws SnapshotingException {
        if (snapDataBase == null) {
            throw new SnapshotingException(DATA_BASE_API_NOT_INIT);
        }
        try {
            int index = att_complete_name.lastIndexOf("/");
            String device_name = att_complete_name.substring(0, index);
            DeviceProxy deviceProxy = new DeviceProxy(device_name);
            Database dbase = ApiUtil.get_db_obj();
            String[] devslist = dbase.get_device_class_list(deviceProxy.info().server_id);
            for (int i = 0; i < devslist.length; i++) {
                if (i > 1 && 0 == i % 2 && devslist[i].equalsIgnoreCase(device_name)) {
                    device_name = devslist[i];
                }
            }
            String attribute_name = att_complete_name.substring(index + 1);
            AttributeInfo att_info = deviceProxy.get_attribute_info(attribute_name);
            attribute_name = att_info.name;
            att_complete_name = device_name + "/" + attribute_name;
            System.out.println("SnapManagerAPI.register: " + att_complete_name);
            String[] att_splitted_name = split_att_name_3_fields(att_complete_name);
            Timestamp time = new Timestamp(new java.util.Date().getTime());
            SnapAttributeHeavy snapAttribute = new SnapAttributeHeavy(att_complete_name);
            snapAttribute.setRegistration_time(time);
            snapAttribute.setAttribute_complete_name(att_complete_name);
            snapAttribute.setAttribute_device_name(device_name);
            snapAttribute.setDomain(att_splitted_name[1]);
            snapAttribute.setFamily(att_splitted_name[2]);
            snapAttribute.setMember(att_splitted_name[3]);
            snapAttribute.setAttribute_name(att_splitted_name[4]);
            snapAttribute.setData_type(att_info.data_type);
            snapAttribute.setData_format(att_info.data_format.value());
            snapAttribute.setWritable(att_info.writable.value());
            snapAttribute.setMax_dim_x(att_info.max_dim_x);
            snapAttribute.setMax_dim_y(att_info.max_dim_y);
            snapAttribute.setLevel(att_info.level.value());
            snapAttribute.setCtrl_sys(att_splitted_name[0]);
            snapAttribute.setArchivable(0);
            snapAttribute.setSubstitute(0);
            snapDataBase.registerAttribute(snapAttribute);
        } catch (DevFailed devFailed) {
            String message = GlobalConst.SNAPSHOTING_ERROR_PREFIX + " : " + GlobalConst.ATT_UNREACH_EXCEPTION;
            String reason = "Failed while executing SnapManagerApi.register() method...";
            String desc = "";
            throw new SnapshotingException(message, reason, ErrSeverity.PANIC, desc, "", devFailed);
        } catch (SnapshotingException e) {
            throw e;
        }
    }

    /**
     * This method insure that a given attribute was registered into Snap DB
     * 
     * @param attributeName
     *            the attribute name.
     */
    public static void insureRegitration(String attributeName) throws SnapshotingException {
        if (snapDataBase == null) {
            throw new SnapshotingException(DATA_BASE_API_NOT_INIT);
        }
        if (!snapDataBase.isRegistered(attributeName)) {
            register(attributeName);
        }
    }

    /**
     * TODO LG
     * 
     * @param att_name
     * @return
     * @throws SnapshotingException
     */
    public static int getAttId(String att_name) throws SnapshotingException {
        if (snapDataBase == null) {
            throw new SnapshotingException(DATA_BASE_API_NOT_INIT);
        }
        return snapDataBase.getAttID(att_name);
    }

    public static int createContext(SnapContext snapContext) throws SnapshotingException {
        if (snapDataBase == null) {
            throw new SnapshotingException(DATA_BASE_API_NOT_INIT);
        }
        List<SnapAttributeLight> theoricAttList = snapContext.getAttributeList();
        for (int i = 0; i < theoricAttList.size(); i++) {
            SnapAttributeLight snapAtt = theoricAttList.get(i);
            String attributeName = snapAtt.getAttribute_complete_name();
            SnapManagerApi.insureRegitration(attributeName);
            int att_id = SnapManagerApi.getAttId(attributeName.trim());
            snapAtt.setAttribute_id(att_id);
        }
        return snapDataBase.create_context(snapContext);
    }

    /**
     * @param snapContext
     */
    public static int createContext2Manager(SnapContext snapContext) throws SnapshotingException {
        if (snapDataBase == null) {
            throw new SnapshotingException(DATA_BASE_API_NOT_INIT);
        }
        int timeout = 3000;
        int newContextID = -1;
        try {
            String device = chooseDevice(m_snapManagerClassDevice);
            if (!device.equals("")) {
                DeviceProxy deviceProxy = new DeviceProxy(device);
                deviceProxy.set_timeout_millis(snapContext.getAttributeList().size() * timeout);
                deviceProxy.ping();
                DeviceData device_data_in = null;
                device_data_in = new DeviceData();
                device_data_in.insert(snapContext.toArray());
                DeviceData device_data_out = deviceProxy.command_inout("CreateNewContext", device_data_in);
                newContextID = device_data_out.extractLong();
            } else {
                String message = GlobalConst.SNAPSHOTING_ERROR_PREFIX + " : " + GlobalConst.ERROR_SNAPPATTERN_CREATION;
                String reason = "Failed while executing SnapManagerApi.createContext2Manager() method...";
                String desc = "";
                throw new SnapshotingException(message, reason, ErrSeverity.PANIC, desc, "");
            }
        } catch (DevFailed devFailed) {
            String message = GlobalConst.SNAPSHOTING_ERROR_PREFIX + " : " + GlobalConst.DEV_UNREACH_EXCEPTION;
            String reason = "Failed while executing SnapManagerApi.createContext2Manager() method...";
            String desc = "";
            throw new SnapshotingException(message, reason, ErrSeverity.PANIC, desc, "", devFailed);
        }
        return newContextID;
    }

    public static ArrayList getAllContext() throws SnapshotingException {
        if (snapDataBase == null) {
            throw new SnapshotingException(DATA_BASE_API_NOT_INIT);
        }
        return snapDataBase.getAllContext();
    }

    /**
     * TODO LG Description : Extracts the clause SQL from the given criterions
     * and gets the contexts which subscribe to thoses conditions
     * 
     * @param criterions
     *            Conditions related to the fields of the context table
     * @return a list of contexts which subscribe to the given conditions
     *         (Criterions)
     * @throws SnapshotingException
     */
    public static ArrayList getContext(Criterions criterions) throws SnapshotingException {
        if (snapDataBase == null) {
            throw new SnapshotingException(DATA_BASE_API_NOT_INIT);
        }
        String clause = criterions.getContextClause();
        int context_id = criterions.getIdContextContextTable();
        return snapDataBase.getContext(clause, context_id);
    }

    /**
     * This method - registers the new Sanpshot in the database SnapDB, - gets
     * the ID of the snapshot being built and - return a 'SnapShot' object with
     * the ID field filled.
     * 
     * @return a 'SnapShot' object with the snapID field filled.
     * @throws SnapshotingException
     */
    public static SnapShot registerSnapShot(int contextID) throws SnapshotingException {
        if (snapDataBase == null) {
            throw new SnapshotingException(DATA_BASE_API_NOT_INIT);
        }
        Timestamp time = new Timestamp(System.currentTimeMillis());
        SnapShot snapShot = snapDataBase.createNewSnap(contextID, time);
        return snapShot;
    }

    /**
     * This method triggers the snapshoting oh attributes that belong to the
     * context identified by the the given contextID
     * 
     * @param contextID
     *            The context identifier
     * @return The 'SnapManagerResult.OK_SNAPLAUNCH' if success,
     *         'SnapManagerResult.ERROR_SNAPLAUNCH' otherwise
     */
    public static int launchSnap(int contextID) throws DevFailed {
        String snapArchiverName = chooseDevice(m_snapArchiverClassDevice);
        if (snapArchiverName.equals("")) {
            throw new DevFailed();
        }
        DeviceProxy snapArchiverProxy = new DeviceProxy(snapArchiverName);
        DeviceData device_data = null;
        device_data = new DeviceData();
        device_data.insert((short) contextID);
        snapArchiverProxy.command_inout("TriggerLaunchSnapShot", device_data);
        while (snapArchiverProxy.state().equals(DevState.RUNNING)) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
        }
        DeviceData device_data_out = snapArchiverProxy.command_inout("GetSnapShotResult", device_data);
        int snapID = device_data_out.extractShort();
        return snapID;
    }

    public static List<SnapAttributeExtract> getContextAssociatedAttributes(int id_context) throws SnapshotingException {
        if (snapDataBase == null) {
            throw new SnapshotingException(DATA_BASE_API_NOT_INIT);
        }
        return snapDataBase.getContextAssociatedAttributes(id_context);
    }

    /**
     * TODO LG This method returns a list of attributes that belong to the
     * context identified by the given id_context and subscribe to the given
     * conditions (criterions)
     * 
     * @param id_context
     *            The context identifier
     * @param criterions
     *            Conditions related to fields of the context table
     * @return a list of attributes that belong to the context identified by the
     *         given id_context and subscribe to the given conditions
     *         (criterions)
     * @throws SnapshotingException
     */
    public static List<SnapAttributeHeavy> getContextAssociatedAttributes(int id_context, Criterions criterions) throws SnapshotingException {
        if (snapDataBase == null) {
            throw new SnapshotingException(DATA_BASE_API_NOT_INIT);
        }
        List<SnapAttributeHeavy> res = snapDataBase.getContextAttributes(id_context, criterions.getAttributeClause());
        return res;
    }

    /**
     * TODO LG
     * 
     * @param snapshot
     * @return
     * @throws SnapshotingException
     */
    public static List<SnapAttributeExtract> getSnapshotAssociatedAttributes(SnapShotLight snapshot) throws SnapshotingException {
        return getSnapshotAssociatedAttributes(snapshot, -1);
    }

    /**
     * 
     * @param snapshot
     * @param contextID
     * @return
     * @throws SnapshotingException
     */
    public static List<SnapAttributeExtract> getSnapshotAssociatedAttributes(SnapShotLight snapshot, int contextID) throws SnapshotingException {
        if (snapDataBase == null) {
            throw new SnapshotingException(DATA_BASE_API_NOT_INIT);
        }
        List<SnapAttributeExtract> arrayList = new ArrayList<SnapAttributeExtract>();
        if (contextID < 0) {
            contextID = getContextID(snapshot.getId_snap());
        }
        List<SnapAttributeExtract> theoricList = getContextAssociatedAttributes(contextID);
        for (int i = 0; i < theoricList.size(); i++) {
            SnapAttributeExtract snapAttributeLight = theoricList.get(i);
            SnapAttributeExtract snapAttributeExtract = snapDataBase.getSnapResult(snapAttributeLight, snapshot.getId_snap());
            arrayList.add(snapAttributeExtract);
        }
        return arrayList;
    }

    public static Vector getAttDefinitionData(String attributeName) throws SnapshotingException {
        if (snapDataBase == null) {
            throw new SnapshotingException(DATA_BASE_API_NOT_INIT);
        }
        Vector vector = snapDataBase.getAttDefinitionData(attributeName);
        return vector;
    }

    public static int getMaxID() throws SnapshotingException {
        if (snapDataBase == null) {
            throw new SnapshotingException(DATA_BASE_API_NOT_INIT);
        }
        return snapDataBase.getMaxContextID();
    }

    public static ArrayList getContextAssociatedSnapshots(int id_context) throws SnapshotingException {
        if (snapDataBase == null) {
            throw new SnapshotingException(DATA_BASE_API_NOT_INIT);
        }
        return snapDataBase.getContextAssociatedSnapshots(id_context);
    }

    /**
     * TODO LG Description : Extracts the clause SQL from the given criterions
     * and gets the snapshots which subscribe to thoses conditions
     * 
     * @param criterions
     *            Conditions related to the fields of the snapshot table
     * @return a list of snapshots which subscribe to the given conditions
     *         (Criterions)
     * @throws SnapshotingException
     */
    public static ArrayList getContextAssociatedSnapshots(Criterions criterions) throws SnapshotingException {
        if (snapDataBase == null) {
            throw new SnapshotingException(DATA_BASE_API_NOT_INIT);
        }
        String clause = criterions.getSnapshotClause();
        int id_context = criterions.getIdContextSnapTable();
        int id_snap = criterions.getIdSnap();
        return snapDataBase.getContextAssociatedSnapshots(clause, id_context, id_snap);
    }

    public static int getContextID(int idSnapshot) throws SnapshotingException {
        if (snapDataBase == null) {
            throw new SnapshotingException(DATA_BASE_API_NOT_INIT);
        }
        return snapDataBase.getContextID(idSnapshot);
    }

    public static List<SnapAttributeExtract> getSnapResult(int id_snap) throws SnapshotingException {
        if (snapDataBase == null) {
            throw new SnapshotingException(DATA_BASE_API_NOT_INIT);
        }
        List<SnapAttributeExtract> arrayList = new ArrayList<SnapAttributeExtract>();
        int contextID = snapDataBase.getContextID(id_snap);
        List<SnapAttributeExtract> theoricList = getContextAssociatedAttributes(contextID);
        for (int i = 0; i < theoricList.size(); i++) {
            SnapAttributeExtract snapAttributeLight = theoricList.get(i);
            SnapAttributeExtract snapAttributeExtract = snapDataBase.getSnapResult(snapAttributeLight, id_snap);
            arrayList.add(snapAttributeExtract);
        }
        return arrayList;
    }

    public static SnapAttributeExtract[] getSnapValues(int idSnap, String... attributeNames) throws SnapshotingException {
        SnapAttributeExtract[] conf = snapDataBase.getAttributeConfig(attributeNames);
        for (SnapAttributeExtract snapAttributeExtract : conf) {
            snapAttributeExtract = snapDataBase.getSnapResult(snapAttributeExtract, idSnap);
        }
        return conf;
    }

    /**
     * TODO LG
     * 
     * @param id_snap
     * @param new_comment
     * @throws SnapshotingException
     */
    public static void updateSnapComment(int id_snap, String new_comment) throws SnapshotingException {
        if (snapDataBase == null) {
            throw new SnapshotingException(DATA_BASE_API_NOT_INIT);
        }
        snapDataBase.updateSnapComment(id_snap, new_comment);
    }

    /**
     * Get the comment of a snapshot
     * 
     * @param snapID
     *            the snapshot ID
     * @return the comment
     * @throws SnapshotingException
     */
    public static String getSnapComment(int snapID) throws SnapshotingException {
        if (snapDataBase == null) {
            throw new SnapshotingException(DATA_BASE_API_NOT_INIT);
        }
        return snapDataBase.getSnapComment(snapID);
    }

    /**
     * This method is used by a client (GUI) to trigger an equipment setting.
     * 
     * @param snapShot
     * @throws SnapshotingException
     */
    public static void setEquipmentsWithSnapshot(SnapShot snapShot) throws SnapshotingException {
        try {
            int timeout = 3000;
            String device = chooseDevice(m_snapManagerClassDevice);
            if (!device.equals("")) {
                DeviceProxy deviceProxy = new DeviceProxy(device);
                deviceProxy.set_timeout_millis(snapShot.getAttribute_List().size() * timeout);
                deviceProxy.ping();
                DeviceData device_data = null;
                device_data = new DeviceData();
                device_data.insert(snapShot.toArray());
                deviceProxy.command_inout("SetEquipmentsWithSnapshot", device_data);
            } else {
                String message = GlobalConst.SNAPSHOTING_ERROR_PREFIX + " : " + GlobalConst.ERROR_SNAP_SET_EQUIPMENT;
                String reason = "Failed while executing SnapManagerApi.setEquipmentsWithSnapshot() method...";
                String desc = "";
                throw new SnapshotingException(message, reason, ErrSeverity.PANIC, desc, "");
            }
        } catch (DevFailed devFailed) {
            String message = GlobalConst.SNAPSHOTING_ERROR_PREFIX + " : " + GlobalConst.DEV_UNREACH_EXCEPTION;
            String reason = "Failed while executing SnapManagerApi.setEquipmentsWithSnapshot() method...";
            String desc = "";
            throw new SnapshotingException(message, reason, ErrSeverity.PANIC, desc, "", devFailed);
        }
    }

    /**
     * This method is used by a snapshoting device to actually set equipements
     * to the their values on the given snapshot.
     * 
     * @param snapShot
     * @throws SnapshotingException
     */
    public static void TriggerSetEquipments(SnapShot snapShot) throws SnapshotingException {
        List<SnapAttributeExtract> attribute_List = snapShot.getAttribute_List();
        SnapshotingException snapFinalEx = null;
        for (int i = 0; i < attribute_List.size(); i++) {
            SnapAttributeExtract snapAttributeExtract = attribute_List.get(i);
            if (snapAttributeExtract.getWritable() != AttrWriteType._READ && snapAttributeExtract.getWritable() != AttrWriteType._READ_WITH_WRITE) {
                try {
                    AttributeProxy attributeProxy = new AttributeProxy(snapAttributeExtract.getAttribute_complete_name());
                    DeviceAttribute deviceAttribute = new DeviceAttribute(attributeProxy.name());
                    Object value = snapAttributeExtract.getWriteValue();
                    switch(snapAttributeExtract.getData_format()) {
                        case AttrDataFormat._SCALAR:
                            if (value == null || "NaN".equals(value)) {
                                break;
                            }
                            switch(snapAttributeExtract.getData_type()) {
                                case TangoConst.Tango_DEV_STRING:
                                    deviceAttribute.insert(((String) value).toString());
                                    break;
                                case TangoConst.Tango_DEV_STATE:
                                    deviceAttribute.insert(((Integer) value).intValue());
                                    break;
                                case TangoConst.Tango_DEV_UCHAR:
                                    deviceAttribute.insert_uc(((Byte) value).byteValue());
                                    break;
                                case TangoConst.Tango_DEV_LONG:
                                    deviceAttribute.insert(((Integer) value).intValue());
                                    break;
                                case TangoConst.Tango_DEV_ULONG:
                                    deviceAttribute.insert_us(((Integer) value).intValue());
                                    break;
                                case TangoConst.Tango_DEV_BOOLEAN:
                                    deviceAttribute.insert(((Boolean) value).booleanValue());
                                    break;
                                case TangoConst.Tango_DEV_USHORT:
                                    deviceAttribute.insert_us(((Short) value).shortValue());
                                    break;
                                case TangoConst.Tango_DEV_SHORT:
                                    deviceAttribute.insert(((Short) value).shortValue());
                                    break;
                                case TangoConst.Tango_DEV_FLOAT:
                                    deviceAttribute.insert(((Float) value).floatValue());
                                    break;
                                case TangoConst.Tango_DEV_DOUBLE:
                                    deviceAttribute.insert(((Double) value).doubleValue());
                                    break;
                                default:
                                    deviceAttribute.insert(((Double) value).doubleValue());
                                    break;
                            }
                            break;
                        case AttrDataFormat._SPECTRUM:
                            if (value == null || "[NaN]".equals(value) || "NaN".equals(value)) {
                                break;
                            }
                            switch(snapAttributeExtract.getData_type()) {
                                case TangoConst.Tango_DEV_UCHAR:
                                    Byte[] byteVal = (Byte[]) value;
                                    byte[] byteVal2 = null;
                                    if (byteVal != null) {
                                        byteVal2 = new byte[byteVal.length];
                                        for (int j = 0; j < byteVal.length; j++) {
                                            byte val = 0;
                                            if (byteVal[j] != null) {
                                                val = byteVal[j].byteValue();
                                            }
                                            byteVal2[j] = val;
                                        }
                                    }
                                    deviceAttribute.insert_uc(byteVal2, byteVal.length, 0);
                                    break;
                                case TangoConst.Tango_DEV_LONG:
                                    Integer[] longVal = (Integer[]) value;
                                    int[] longVal2 = null;
                                    if (longVal != null) {
                                        longVal2 = new int[longVal.length];
                                        for (int j = 0; j < longVal.length; j++) {
                                            int val = 0;
                                            if (longVal[j] != null) {
                                                val = longVal[j].intValue();
                                            }
                                            longVal2[j] = val;
                                        }
                                    }
                                    deviceAttribute.insert(longVal2);
                                    break;
                                case TangoConst.Tango_DEV_ULONG:
                                    Integer[] longValu = (Integer[]) value;
                                    int[] longValu2 = null;
                                    if (longValu != null) {
                                        longValu2 = new int[longValu.length];
                                        for (int j = 0; j < longValu.length; j++) {
                                            int val = 0;
                                            if (longValu[j] != null) {
                                                val = longValu[j].intValue();
                                            }
                                            longValu2[j] = val;
                                        }
                                    }
                                    deviceAttribute.insert_us(longValu2);
                                    break;
                                case TangoConst.Tango_DEV_SHORT:
                                    Short[] shortVal = (Short[]) value;
                                    short[] shortVal2 = null;
                                    if (shortVal != null) {
                                        shortVal2 = new short[shortVal.length];
                                        for (int j = 0; j < shortVal.length; j++) {
                                            short val = 0;
                                            if (shortVal[j] != null) {
                                                val = shortVal[j].shortValue();
                                            }
                                            shortVal2[j] = val;
                                        }
                                    }
                                    deviceAttribute.insert(shortVal2);
                                    break;
                                case TangoConst.Tango_DEV_USHORT:
                                    Short[] shortValu = (Short[]) value;
                                    short[] shortValu2 = null;
                                    if (shortValu != null) {
                                        shortValu2 = new short[shortValu.length];
                                        for (int j = 0; j < shortValu.length; j++) {
                                            short val = 0;
                                            if (shortValu[j] != null) {
                                                val = shortValu[j].shortValue();
                                            }
                                            shortValu2[j] = val;
                                        }
                                    }
                                    deviceAttribute.insert_us(shortValu2);
                                    break;
                                case TangoConst.Tango_DEV_FLOAT:
                                    Float[] floatVal = (Float[]) value;
                                    float[] floatVal2 = null;
                                    if (floatVal != null) {
                                        floatVal2 = new float[floatVal.length];
                                        for (int j = 0; j < floatVal.length; j++) {
                                            float val = 0;
                                            if (floatVal[j] != null) {
                                                val = floatVal[j].floatValue();
                                            }
                                            floatVal2[j] = val;
                                        }
                                    }
                                    deviceAttribute.insert(floatVal2);
                                    break;
                                case TangoConst.Tango_DEV_DOUBLE:
                                    Double[] doubleVal = (Double[]) value;
                                    double[] doubleVal2 = null;
                                    if (doubleVal != null) {
                                        doubleVal2 = new double[doubleVal.length];
                                        for (int j = 0; j < doubleVal.length; j++) {
                                            double val = Double.NaN;
                                            if (doubleVal[j] != null) {
                                                val = doubleVal[j].doubleValue();
                                            }
                                            doubleVal2[j] = val;
                                        }
                                    }
                                    deviceAttribute.insert(doubleVal2);
                                    break;
                                case TangoConst.Tango_DEV_STRING:
                                    String[] stringVal = (String[]) value;
                                    deviceAttribute.insert(stringVal);
                                    break;
                                case TangoConst.Tango_DEV_BOOLEAN:
                                    Boolean[] boolVal = (Boolean[]) value;
                                    boolean[] boolVal2 = null;
                                    if (boolVal != null) {
                                        boolVal2 = new boolean[boolVal.length];
                                        for (int j = 0; j < boolVal.length; j++) {
                                            boolean val = false;
                                            if (boolVal[j] != null) {
                                                val = boolVal[j].booleanValue();
                                            }
                                            boolVal2[j] = val;
                                        }
                                    }
                                    deviceAttribute.insert(boolVal2);
                                    break;
                                default:
                                    break;
                            }
                            break;
                        default:
                    }
                    attributeProxy.write(deviceAttribute);
                } catch (DevFailed devFailed) {
                    String nameOfFailure = snapAttributeExtract.getAttribute_complete_name();
                    String message = GlobalConst.SNAPSHOTING_ERROR_PREFIX + " : " + GlobalConst.ERROR_SNAP_SET_EQUIPMENT + " on attribute: " + nameOfFailure;
                    String reason = "Failed while executing SnapManagerApi.TriggerSetEquipments() method on attribute:" + nameOfFailure;
                    String desc = reason;
                    if (snapFinalEx == null) {
                        snapFinalEx = new SnapshotingException(message, reason, ErrSeverity.PANIC, desc, "", devFailed);
                    } else {
                        SnapshotingException snapEx = new SnapshotingException(devFailed);
                        snapFinalEx.addStack(message, reason, ErrSeverity.PANIC, desc, "", snapEx);
                    }
                }
            }
        }
        if (snapFinalEx != null) {
            throw snapFinalEx;
        }
    }

    /**
     * 
     * @param cmd_name
     * @param option
     * @param id_snap
     * @throws SnapshotingException
     */
    public static String setEquipmentWithCommand(String cmd_name, String option, SnapShot snapShot) throws SnapshotingException {
        try {
            int timeout = 3000;
            String device = chooseDevice(m_snapManagerClassDevice);
            if (!device.equals("")) {
                DeviceProxy deviceProxy = new DeviceProxy(device);
                deviceProxy.set_timeout_millis(snapShot.getAttribute_List().size() * timeout);
                deviceProxy.ping();
                DeviceData device_data = null;
                device_data = new DeviceData();
                device_data.insert(new String[] { cmd_name, option, String.valueOf(snapShot.getId_snap()) });
                device_data = deviceProxy.command_inout("SetEquipmentsWithCommand", device_data);
                return device_data.extractString();
            } else {
                String message = GlobalConst.SNAPSHOTING_ERROR_PREFIX + " : " + GlobalConst.ERROR_SNAP_SET_EQUIPMENT;
                String reason = "Failed while executing SnapManagerApi.setEquipmentWithCommand() method...";
                String desc = "";
                throw new SnapshotingException(message, reason, ErrSeverity.PANIC, desc, "");
            }
        } catch (DevFailed devFailed) {
            String message = GlobalConst.SNAPSHOTING_ERROR_PREFIX + " : " + GlobalConst.DEV_UNREACH_EXCEPTION;
            String reason = "Failed while executing SnapManagerApi.setEquipmentWithCommand() method...";
            String desc = "";
            throw new SnapshotingException(message, reason, ErrSeverity.PANIC, desc, "", devFailed);
        }
    }
}
