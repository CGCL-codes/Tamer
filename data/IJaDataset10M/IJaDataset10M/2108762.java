package SnapExtractor;

import org.omg.CORBA.SystemException;
import org.omg.CORBA.UserException;
import org.tango.utils.DevFailedUtils;
import fr.esrf.Tango.AttrDataFormat;
import fr.esrf.Tango.AttrWriteType;
import fr.esrf.Tango.DevFailed;
import fr.esrf.Tango.DevState;
import fr.esrf.Tango.DevVarLongStringArray;
import fr.esrf.TangoApi.DbDatum;
import fr.esrf.TangoDs.Attr;
import fr.esrf.TangoDs.Attribute;
import fr.esrf.TangoDs.DeviceClass;
import fr.esrf.TangoDs.DeviceImpl;
import fr.esrf.TangoDs.Except;
import fr.esrf.TangoDs.ImageAttr;
import fr.esrf.TangoDs.SpectrumAttr;
import fr.esrf.TangoDs.TangoConst;
import fr.esrf.TangoDs.TimedAttrData;
import fr.esrf.TangoDs.Util;
import fr.soleil.commonarchivingapi.ArchivingTools.Tools.DbData;
import fr.soleil.snapArchivingApi.SnapExtractorApi.convert.ConverterFactory;
import fr.soleil.snapArchivingApi.SnapExtractorApi.convert.IConverter;
import fr.soleil.snapArchivingApi.SnapExtractorApi.datasources.db.ISnapReader;
import fr.soleil.snapArchivingApi.SnapExtractorApi.datasources.db.SnapReaderFactory;
import fr.soleil.snapArchivingApi.SnapExtractorApi.devicelink.Warnable;
import fr.soleil.snapArchivingApi.SnapExtractorApi.naming.DynamicAttributeNamerFactory;
import fr.soleil.snapArchivingApi.SnapExtractorApi.naming.IDynamicAttributeNamer;
import fr.soleil.snapArchivingApi.SnapExtractorApi.tools.Tools;
import fr.soleil.snapArchivingApi.SnapManagerApi.SnapManagerApi;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.SnapAttributeExtract;
import fr.soleil.snapArchivingApi.SnapshotingTools.Tools.SnapshotingException;

public class SnapExtractor extends DeviceImpl implements TangoConst {

    protected int state;

    private final String m_version;

    private int id = 0;

    /**
     * User identifier (name) used to connect the database SNAP. <br>
     * <b>Default value : </b> snap
     */
    String snapUser;

    /**
     * Password used to connect the database SNAP. <br>
     * <b>Default value : </b> snap
     */
    String snapPassword;

    protected int formerState = DevState._UNKNOWN;

    private String dbHost;

    private String dbName;

    private String dbSchema;

    private String isRac;

    private ISnapReader snapReader;

    SnapExtractor(DeviceClass cl, String s, String version) throws DevFailed {
        super(cl, s);
        m_version = version;
        init_device();
    }

    SnapExtractor(DeviceClass cl, String s, String d, String version) throws DevFailed {
        super(cl, s, d);
        m_version = version;
        init_device();
    }

    @Override
    public void init_device() throws DevFailed {
        System.out.println("SnapExtractor() create " + device_name);
        get_device_property();
        try {
            SnapManagerApi.initSnapConnection(dbHost, dbName, dbSchema, snapUser, snapPassword, isRac);
            snapReader = SnapReaderFactory.getImpl(SnapReaderFactory.REAL);
            snapReader.openConnection();
        } catch (SnapshotingException e) {
            get_logger().warn(e.toString(), e);
            throw e.toTangoException();
        }
        set_state(DevState.ON);
    }

    public void remove_dyn_attr(String argin) throws DevFailed {
        get_logger().info("Entering remove_dynamic_attribute()");
        String[] obj_to_del = new String[3];
        obj_to_del[0] = device_name;
        obj_to_del[1] = "attribute";
        obj_to_del[2] = argin;
        Util tg = Util.instance();
        tg.get_dserver_device().rem_obj_polling(obj_to_del, false);
        remove_attribute(argin);
        get_logger().info("Exiting remove_dynamic_attribute()");
    }

    public void remove_dyn_attrs(String[] argin) throws DevFailed {
        get_logger().info("Entering remove_dyn_attrs()");
        if (argin == null) {
            return;
        }
        for (int i = 0; i < argin.length; i++) {
            try {
                String[] obj_to_del = new String[3];
                obj_to_del[0] = device_name;
                obj_to_del[1] = "attribute";
                obj_to_del[2] = argin[i];
                Util tg = Util.instance();
                tg.get_dserver_device().rem_obj_polling(obj_to_del, false);
                this.remove_attribute(argin[i]);
            } catch (Throwable t) {
                Tools.printIfDevFailed(t);
                continue;
            }
        }
        get_logger().info("Exiting remove_dyn_attrs()");
    }

    public void remove_all_dyn_attr() throws DevFailed {
        get_logger().info("Entering remove_dynamic_attribute()");
        int numberOfAttributes = this.dev_attr == null ? 0 : this.dev_attr.get_attr_nb();
        String[] attributesToRemove = new String[numberOfAttributes];
        for (int i = 0; i < numberOfAttributes; i++) {
            Attribute nextAttribute = this.dev_attr.get_attr_by_ind(i);
            String attributeName = nextAttribute.get_name();
            attributesToRemove[i] = attributeName;
        }
        this.remove_dyn_attrs(attributesToRemove);
    }

    public void get_device_property() throws DevFailed {
        snapUser = "";
        snapPassword = "";
        dbHost = "";
        dbName = "";
        dbSchema = "";
        isRac = "";
        if (Util._UseDb == false) {
            return;
        }
        String[] propnames = { "DbUser", "DbPassword", "DbHost", "DbName", "DbSchema", "IsRac" };
        DbDatum[] dev_prop = get_db_device().get_property(propnames);
        SnapExtractorClass ds_class = (SnapExtractorClass) get_device_class();
        int i = -1;
        if (dev_prop[++i].is_empty() == false) {
            snapUser = dev_prop[i].extractString();
        } else {
            DbDatum cl_prop = ds_class.get_class_property(dev_prop[i].name);
            if (cl_prop.is_empty() == false) {
                snapUser = cl_prop.extractString();
            }
        }
        if (dev_prop[++i].is_empty() == false) {
            snapPassword = dev_prop[i].extractString();
        } else {
            DbDatum cl_prop = ds_class.get_class_property(dev_prop[i].name);
            if (cl_prop.is_empty() == false) {
                snapPassword = cl_prop.extractString();
            }
        }
        if (dev_prop[++i].is_empty() == false) {
            dbHost = dev_prop[i].extractString();
        } else {
            DbDatum cl_prop = ds_class.get_class_property(dev_prop[i].name);
            if (cl_prop.is_empty() == false) {
                dbHost = cl_prop.extractString();
            }
        }
        if (dev_prop[++i].is_empty() == false) {
            dbName = dev_prop[i].extractString();
        } else {
            DbDatum cl_prop = ds_class.get_class_property(dev_prop[i].name);
            if (cl_prop.is_empty() == false) {
                dbName = cl_prop.extractString();
            }
        }
        if (dev_prop[++i].is_empty() == false) {
            dbSchema = dev_prop[i].extractString();
        } else {
            DbDatum cl_prop = ds_class.get_class_property(dev_prop[i].name);
            if (cl_prop.is_empty() == false) {
                dbSchema = cl_prop.extractString();
            }
        }
        if (dev_prop[++i].is_empty() == false) {
            isRac = dev_prop[i].extractString();
        } else {
            DbDatum cl_prop = ds_class.get_class_property(dev_prop[i].name);
            if (cl_prop.is_empty() == false) {
                isRac = cl_prop.extractString();
            }
        }
    }

    @Override
    public void always_executed_hook() {
    }

    public String[] get_snap(int argin) throws DevFailed {
        String[] argout = new String[5];
        get_logger().info("Entering get_snap()");
        try {
            SnapAttributeExtract[] sae = snapReader.getSnap(argin);
            if (sae == null || sae.length == 0) {
                Except.throw_exception("INPUT_ERROR", "Snapshot ID does not exist", "SnapExtractor.GetSnap");
            }
            argout = this.add_attributes(sae);
        } catch (Throwable t) {
            Tools.printIfDevFailed(t);
            if (t instanceof DevFailed) {
                throw (DevFailed) t;
            }
        }
        get_logger().info("Exiting get_snap()");
        return argout;
    }

    public String[] get_snap_value(int snapID, String attr_name) throws DevFailed {
        get_logger().info("Entering get_snap_value()");
        SnapAttributeExtract[] extraction = snapReader.getSnapValues(snapID, attr_name);
        String[] argout = get_attribute_value(extraction[0]);
        get_logger().info("Exiting get_snap_value()");
        return argout;
    }

    public String[] getSnapValues(int snapID, boolean readValues, String... attributeNames) throws DevFailed {
        String[] argout = new String[attributeNames.length];
        SnapAttributeExtract[] extraction = snapReader.getSnapValues(snapID, attributeNames);
        int i = 0;
        for (SnapAttributeExtract snapAttributeExtract : extraction) {
            if (readValues) {
                argout[i++] = snapAttributeExtract.valueToString(0);
            } else {
                argout[i++] = snapAttributeExtract.valueToString(1);
            }
        }
        return argout;
    }

    public int[] get_snap_id(int ctxID, String[] criterions) throws DevFailed {
        get_logger().info("Entering get_snap_id()");
        int[] snap_ids = snapReader.getSnapshotsID(ctxID, criterions);
        if (snap_ids == null) {
            Except.throw_exception("INPUT_ERROR", "Invalid Criterions", "SnapExtractor.GetSnapID");
        }
        get_logger().info("Exiting get_snaps_id()");
        return snap_ids;
    }

    /**
     * 
     * @param sae
     *            : list of SnapAttributeExtract
     * @param attr_name
     *            : the given attribute name
     * @return the index of the attribute in the list of SnapAttributeExtract.
     */
    private int getIndexOfAttribute(SnapAttributeExtract[] sae, String attr_name) {
        int index = -1;
        for (int i = 0; i < sae.length; i++) {
            if (sae[i].getAttribute_complete_name().equalsIgnoreCase(attr_name)) {
                index = i;
                break;
            }
        }
        return index;
    }

    /**
     * 
     * @param snapAttributeExtract
     * @return read_value and write_value
     */
    private String[] get_attribute_value(SnapAttributeExtract snapAttributeExtract) {
        String[] argout = { "NaN", "NaN" };
        try {
            SnapAttributeExtract currentExtract = snapAttributeExtract;
            argout[0] = currentExtract.valueToString(0);
            argout[1] = currentExtract.valueToString(1);
        } catch (Exception e) {
        }
        return argout;
    }

    /**
     * Loads the list of snapshots attached to a given context, specified by its
     * ID. Returns a DevVarLongStringArray object, where the array is as long as
     * there are many snapshots for the context. The Long part is the snapshot's
     * ID, the String part is the concatenation of its Date and Comment fields
     * values.
     * 
     * @param argin
     *            The context ID
     * @return The snapshots: [ID, Date + Comment]*n
     * @throws DevFailed
     */
    public DevVarLongStringArray get_snaps_for_context(int argin) throws DevFailed {
        get_logger().info("Entering get_snaps_for_context()");
        DevVarLongStringArray snapshots = snapReader.getSnapshotsForContext(argin);
        if (snapshots == null) {
            return emptyDevVarLongStringArrayResult();
        }
        get_logger().info("Exiting get_snaps_for_context()");
        return snapshots;
    }

    /**
     * @return
     */
    private DevVarLongStringArray emptyDevVarLongStringArrayResult() {
        DevVarLongStringArray ret = new DevVarLongStringArray();
        int[] lvalue = new int[1];
        java.lang.String[] svalue = new java.lang.String[1];
        lvalue[0] = -1;
        svalue[0] = "NO SNAPSHOT FOR THIS CONTEXT ID";
        ret.lvalue = lvalue;
        ret.svalue = svalue;
        return ret;
    }

    /**
     * @return
     */
    private String[] emptyResult() {
        String[] argout = new String[3];
        argout[0] = "NO SUCH SNAPSHOT";
        argout[1] = "";
        argout[2] = "";
        return argout;
    }

    public static void main(String[] argv) {
        try {
            Util tg = Util.init(argv, "SnapExtractor");
            tg.server_init();
            System.out.println("Ready to accept request");
            tg.server_run();
        } catch (OutOfMemoryError ex) {
            System.err.println("Can't allocate memory !!!!");
            System.err.println("Exiting");
        } catch (UserException ex) {
            Except.print_exception(ex);
            System.err.println("Received a CORBA user exception");
            System.err.println("Exiting");
        } catch (SystemException ex) {
            Except.print_exception(ex);
            System.err.println("Received a CORBA system exception");
            System.err.println("Exiting");
        }
        System.exit(-1);
    }

    private String[] add_attributes(SnapAttributeExtract[] sae) throws DevFailed {
        int numberOfAttributes = sae.length;
        String[] argout = new String[3 * numberOfAttributes];
        int j = 0;
        for (int i = 0; i < numberOfAttributes; i++) {
            SnapAttributeExtract currentExtract = sae[i];
            String realName = currentExtract.getAttribute_complete_name();
            try {
                String[] names = this.addAttribute(currentExtract);
                String read_name = names[0];
                String write_name = names[1];
                argout[j] = realName;
                argout[j + 1] = read_name;
                argout[j + 2] = write_name;
            } catch (DevFailed e) {
                DevFailedUtils.printDevFailed(e);
                System.err.println("cannot add attr  " + realName);
            }
            j += 3;
        }
        return argout;
    }

    /**
     * @param currentExtract
     * @return
     * @throws DevFailed
     */
    private String[] addAttribute(SnapAttributeExtract currentExtract) throws DevFailed {
        if (currentExtract.getValue() == null) {
            String message = "Null value for attribute |" + currentExtract.getAttribute_complete_name() + "|";
            this.trace(message, Warnable.LOG_LEVEL_WARN);
            return getErrorNames(currentExtract);
        }
        IConverter converter = ConverterFactory.getImpl(ConverterFactory.DEFAULT);
        DbData dbData = converter.convert(currentExtract);
        if (dbData == null) {
            String message = "Null DbData for attribute |" + currentExtract.getAttribute_complete_name() + "| (if the attribute is R/W or is a spectrum, at least one of its value's component is null)";
            this.trace(message, Warnable.LOG_LEVEL_WARN);
            return getErrorNames(currentExtract);
        }
        String[] names = this.buildDynamicAttributesNames(currentExtract);
        this.createDynamicAttribute(dbData, names);
        return names;
    }

    private String[] getErrorNames(SnapAttributeExtract currentExtract) {
        String[] errorNames = new String[2];
        String name = currentExtract.getAttribute_complete_name();
        errorNames[0] = name + "_ERROR";
        errorNames[1] = name + "_ERROR";
        return errorNames;
    }

    /**
     * @param currentExtract
     * @return
     */
    private String[] buildDynamicAttributesNames(SnapAttributeExtract currentExtract) {
        String random_name_1 = "", random_name_2 = "";
        String[] names;
        boolean hasBothReadAndWriteValues = currentExtract.getWritable() == AttrWriteType._READ_WITH_WRITE || currentExtract.getWritable() == AttrWriteType._READ_WRITE;
        IDynamicAttributeNamer dynamicAttributeNamer = DynamicAttributeNamerFactory.getImpl(DynamicAttributeNamerFactory.DEFAULT);
        boolean firstIsRead = currentExtract.getWritable() != AttrWriteType._WRITE;
        random_name_1 = dynamicAttributeNamer.getName(currentExtract, id, firstIsRead);
        if (hasBothReadAndWriteValues) {
            random_name_2 = dynamicAttributeNamer.getName(currentExtract, id, !firstIsRead);
        }
        id++;
        names = new String[2];
        names[0] = random_name_1;
        names[1] = random_name_2;
        return names;
    }

    private DevVarLongStringArray createDynamicAttribute(DbData dbData, String[] names) throws DevFailed {
        try {
            DevVarLongStringArray argout = new DevVarLongStringArray();
            boolean _2value = dbData.getWritable() == AttrWriteType._READ_WITH_WRITE || dbData.getWritable() == AttrWriteType._READ_WRITE ? true : false;
            String random_name_1 = names[0];
            String random_name_2 = names[1];
            if (dbData.getData_type() == Tango_DEV_STATE) {
                dbData.setData_type(Tango_DEV_LONG);
            }
            switch(dbData.getData_format()) {
                case AttrDataFormat._SCALAR:
                    add_attribute(new Attr(random_name_1, dbData.getData_type(), AttrWriteType.READ));
                    if (_2value) {
                        add_attribute(new Attr(random_name_2, dbData.getData_type(), AttrWriteType.READ));
                    }
                    break;
                case AttrDataFormat._SPECTRUM:
                    add_attribute(new SpectrumAttr(random_name_1, dbData.getData_type(), dbData.getMax_x()));
                    if (_2value) {
                        add_attribute(new SpectrumAttr(random_name_2, dbData.getData_type(), dbData.getMax_x()));
                    }
                    break;
                case AttrDataFormat._IMAGE:
                    add_attribute(new ImageAttr(random_name_1, dbData.getData_type(), dbData.getMax_x(), dbData.getMax_y()));
                    break;
            }
            set_polled_attr(names);
            set_poll_ring_depth(dbData.size());
            DevVarLongStringArray poll_1, poll_2;
            poll_1 = new DevVarLongStringArray();
            poll_2 = new DevVarLongStringArray();
            poll_1.lvalue = new int[1];
            poll_1.lvalue[0] = 0;
            poll_1.svalue = new String[3];
            poll_1.svalue[0] = device_name;
            poll_1.svalue[1] = "attribute";
            poll_1.svalue[2] = random_name_1;
            if (_2value) {
                poll_2.lvalue = new int[1];
                poll_2.lvalue[0] = 0;
                poll_2.svalue = new String[3];
                poll_2.svalue[0] = device_name;
                poll_2.svalue[1] = "attribute";
                poll_2.svalue[2] = random_name_2;
            }
            Util tg_1 = Util.instance();
            tg_1.get_dserver_device().add_obj_polling(poll_1, false);
            Util tg_2 = Util.instance();
            if (_2value) {
                tg_2.get_dserver_device().add_obj_polling(poll_2, false);
            }
            if (_2value) {
                DbData[] dbDatas = dbData.splitDbData();
                DbData readDbData = dbDatas[0];
                DbData writeDbData = dbDatas[1];
                TimedAttrData[] readTimedAttrDatas = readDbData.getDataAsTimedAttrData();
                TimedAttrData[] writeTimedAttrDatas = writeDbData.getDataAsTimedAttrData();
                updateWriteData(writeTimedAttrDatas);
                tg_1.fill_attr_polling_buffer(this, random_name_1, readTimedAttrDatas);
                tg_2.fill_attr_polling_buffer(this, random_name_2, writeTimedAttrDatas);
            } else {
                tg_1.fill_attr_polling_buffer(this, random_name_1, dbData.getDataAsTimedAttrData());
            }
            argout = new DevVarLongStringArray();
            argout.lvalue = new int[1];
            if (!_2value) {
                argout.svalue = new String[1];
            } else {
                argout.svalue = new String[2];
            }
            argout.lvalue[0] = dbData.getData_timed().length;
            argout.svalue[0] = random_name_1;
            if (_2value) {
                argout.svalue[1] = random_name_2;
            }
            return argout;
        } catch (Throwable t) {
            Tools.throwDevFailed(t);
        }
        return null;
    }

    /**
     * update x dimension to the data array length
     * 
     * @param data
     */
    private void updateWriteData(TimedAttrData[] data) {
        for (int i = 0; i < data.length; i++) {
            int size = 0;
            switch(data[i].data_type) {
                case Tango_DEV_BOOLEAN:
                    size = data[i].bool_ptr.length;
                    break;
                case Tango_DEV_SHORT:
                    size = data[i].sh_ptr.length;
                    break;
                case Tango_DEV_LONG:
                    size = data[i].lg_ptr.length;
                    break;
                case Tango_DEV_LONG64:
                    size = data[i].lg64_ptr.length;
                    break;
                case Tango_DEV_FLOAT:
                    size = data[i].fl_ptr.length;
                    break;
                case Tango_DEV_DOUBLE:
                    size = data[i].db_ptr.length;
                    break;
                case Tango_DEV_STRING:
                    size = data[i].str_ptr.length;
                    break;
                default:
                    ;
            }
            data[i].x = size;
        }
    }

    /**
     * @param i
     */
    private String formatStatus(int _state) {
        switch(_state) {
            case DevState._ALARM:
                return "Archiving problems have been detected.";
            case DevState._FAULT:
                return "This device isn't working properly.";
            case DevState._INIT:
                return "No control step has been completed yet. Please wait.";
            case DevState._OFF:
                return "This device is waiting.";
            case DevState._ON:
                return "This device is running normally.";
            default:
                return "Unknown";
        }
    }

    public void trace(String msg, int level) throws DevFailed {
        switch(level) {
            case Warnable.LOG_LEVEL_DEBUG:
                get_logger().debug(msg);
                break;
            case Warnable.LOG_LEVEL_INFO:
                get_logger().info(msg);
                break;
            case Warnable.LOG_LEVEL_WARN:
                get_logger().warn(msg);
                break;
            case Warnable.LOG_LEVEL_ERROR:
                get_logger().error(msg);
                break;
            case Warnable.LOG_LEVEL_FATAL:
                get_logger().fatal(msg);
                break;
            default:
                Tools.throwDevFailed(new IllegalArgumentException("Expected LOG_LEVEL_DEBUG(9), LOG_LEVEL_INFO(7), LOG_LEVEL_WARN(5), LOG_LEVEL_ERROR(3), or LOG_LEVEL_FATAL(1), got " + level + "instead."));
        }
    }

    public void trace(String msg, Throwable t, int level) throws DevFailed {
        switch(level) {
            case Warnable.LOG_LEVEL_DEBUG:
                get_logger().debug(msg, t);
                break;
            case Warnable.LOG_LEVEL_INFO:
                get_logger().info(msg, t);
                break;
            case Warnable.LOG_LEVEL_WARN:
                get_logger().warn(msg, t);
                break;
            case Warnable.LOG_LEVEL_ERROR:
                get_logger().error(msg, t);
                break;
            case Warnable.LOG_LEVEL_FATAL:
                get_logger().fatal(msg, t);
                break;
            default:
                Tools.throwDevFailed(new IllegalArgumentException("Expected LOG_LEVEL_DEBUG(9), LOG_LEVEL_INFO(7), LOG_LEVEL_WARN(5), LOG_LEVEL_ERROR(3), or LOG_LEVEL_FATAL(1), got " + level + "instead."));
        }
    }

    @Override
    public void read_attr(Attribute attr) throws DevFailed {
        String attr_name = attr.get_name();
        get_logger().info("In read_attr for attribute " + attr_name);
        if (attr_name == "version") {
            attr.set_value(m_version);
        }
    }

    @Override
    public void delete_device() throws DevFailed {
    }
}
