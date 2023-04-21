package com.xensource.xenapi;

import java.util.Map;
import java.util.Set;
import java.util.Date;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.apache.xmlrpc.XmlRpcException;

/**
 * The metrics associated with a virtual network device
 *
 * @author Tom Wilkie <tom.wilkie@gmail.com>
 */
public class VIFMetrics {

    /**
     * The XenAPI reference to this object.
     */
    protected final String ref;

    private VIFMetrics(String ref) {
        this.ref = ref;
    }

    /**
     * This code helps ensure there is only one
     * VIFMetrics instance per XenAPI reference.
     */
    private static final Map<String, SoftReference<VIFMetrics>> cache = new HashMap<String, SoftReference<VIFMetrics>>();

    protected static synchronized VIFMetrics getInstFromRef(String ref) {
        if (VIFMetrics.cache.containsKey(ref)) {
            VIFMetrics instance = VIFMetrics.cache.get(ref).get();
            if (instance != null) {
                return instance;
            }
        }
        VIFMetrics instance = new VIFMetrics(ref);
        VIFMetrics.cache.put(ref, new SoftReference<VIFMetrics>(instance));
        return instance;
    }

    /**
     * Represents all the fields in a VIFMetrics
     */
    public static class Record implements Types.Record {

        public String toString() {
            StringWriter writer = new StringWriter();
            PrintWriter print = new PrintWriter(writer);
            print.printf("%1$20s: %2$s\n", "uuid", this.uuid);
            print.printf("%1$20s: %2$s\n", "ioReadKbs", this.ioReadKbs);
            print.printf("%1$20s: %2$s\n", "ioWriteKbs", this.ioWriteKbs);
            print.printf("%1$20s: %2$s\n", "lastUpdated", this.lastUpdated);
            return writer.toString();
        }

        /**
         * Convert a VIF_metrics.Record to a Map
         */
        public Map<String, Object> toMap() {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("uuid", this.uuid);
            map.put("io_read_kbs", this.ioReadKbs);
            map.put("io_write_kbs", this.ioWriteKbs);
            map.put("last_updated", this.lastUpdated);
            return map;
        }

        /**
         * unique identifier/object reference
         */
        public String uuid;

        /**
         * Read bandwidth (KiB/s)
         */
        public Double ioReadKbs;

        /**
         * Write bandwidth (KiB/s)
         */
        public Double ioWriteKbs;

        /**
         * Time at which this information was last updated
         */
        public Date lastUpdated;
    }

    /**
     * Get a record containing the current state of the given VIF_metrics.
     *
     * @return all fields from the object
     */
    public VIFMetrics.Record getRecord() throws Types.BadServerResponse, ConnectionHelper.NoConnectionOnThisThreadException, XmlRpcException {
        String method_call = "VIF_metrics.get_record";
        String session = ConnectionHelper.instance().getSession();
        Object[] method_params = { Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref) };
        Map response = ConnectionHelper.instance().dispatch(method_call, method_params);
        if (response.get("Status").equals("Success")) {
            Object result = response.get("Value");
            return Types.toVIFMetricsRecord(result);
        } else if (response.get("Status").equals("Failure")) {
            Object[] error = (Object[]) response.get("ErrorDescription");
        }
        throw new Types.BadServerResponse(response);
    }

    /**
     * Get a reference to the VIF_metrics instance with the specified UUID.
     *
     * @param uuid UUID of object to return
     * @return reference to the object
     */
    public static VIFMetrics getByUuid(String uuid) throws Types.BadServerResponse, ConnectionHelper.NoConnectionOnThisThreadException, XmlRpcException {
        String method_call = "VIF_metrics.get_by_uuid";
        String session = ConnectionHelper.instance().getSession();
        Object[] method_params = { Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(uuid) };
        Map response = ConnectionHelper.instance().dispatch(method_call, method_params);
        if (response.get("Status").equals("Success")) {
            Object result = response.get("Value");
            return Types.toVIFMetrics(result);
        } else if (response.get("Status").equals("Failure")) {
            Object[] error = (Object[]) response.get("ErrorDescription");
        }
        throw new Types.BadServerResponse(response);
    }

    /**
     * Get the uuid field of the given VIF_metrics.
     *
     * @return value of the field
     */
    public String getUuid() throws Types.BadServerResponse, ConnectionHelper.NoConnectionOnThisThreadException, XmlRpcException {
        String method_call = "VIF_metrics.get_uuid";
        String session = ConnectionHelper.instance().getSession();
        Object[] method_params = { Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref) };
        Map response = ConnectionHelper.instance().dispatch(method_call, method_params);
        if (response.get("Status").equals("Success")) {
            Object result = response.get("Value");
            return Types.toString(result);
        } else if (response.get("Status").equals("Failure")) {
            Object[] error = (Object[]) response.get("ErrorDescription");
        }
        throw new Types.BadServerResponse(response);
    }

    /**
     * Get the io/read_kbs field of the given VIF_metrics.
     *
     * @return value of the field
     */
    public Double getIoReadKbs() throws Types.BadServerResponse, ConnectionHelper.NoConnectionOnThisThreadException, XmlRpcException {
        String method_call = "VIF_metrics.get_io_read_kbs";
        String session = ConnectionHelper.instance().getSession();
        Object[] method_params = { Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref) };
        Map response = ConnectionHelper.instance().dispatch(method_call, method_params);
        if (response.get("Status").equals("Success")) {
            Object result = response.get("Value");
            return Types.toDouble(result);
        } else if (response.get("Status").equals("Failure")) {
            Object[] error = (Object[]) response.get("ErrorDescription");
        }
        throw new Types.BadServerResponse(response);
    }

    /**
     * Get the io/write_kbs field of the given VIF_metrics.
     *
     * @return value of the field
     */
    public Double getIoWriteKbs() throws Types.BadServerResponse, ConnectionHelper.NoConnectionOnThisThreadException, XmlRpcException {
        String method_call = "VIF_metrics.get_io_write_kbs";
        String session = ConnectionHelper.instance().getSession();
        Object[] method_params = { Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref) };
        Map response = ConnectionHelper.instance().dispatch(method_call, method_params);
        if (response.get("Status").equals("Success")) {
            Object result = response.get("Value");
            return Types.toDouble(result);
        } else if (response.get("Status").equals("Failure")) {
            Object[] error = (Object[]) response.get("ErrorDescription");
        }
        throw new Types.BadServerResponse(response);
    }

    /**
     * Get the last_updated field of the given VIF_metrics.
     *
     * @return value of the field
     */
    public Date getLastUpdated() throws Types.BadServerResponse, ConnectionHelper.NoConnectionOnThisThreadException, XmlRpcException {
        String method_call = "VIF_metrics.get_last_updated";
        String session = ConnectionHelper.instance().getSession();
        Object[] method_params = { Marshalling.toXMLRPC(session), Marshalling.toXMLRPC(this.ref) };
        Map response = ConnectionHelper.instance().dispatch(method_call, method_params);
        if (response.get("Status").equals("Success")) {
            Object result = response.get("Value");
            return Types.toDate(result);
        } else if (response.get("Status").equals("Failure")) {
            Object[] error = (Object[]) response.get("ErrorDescription");
        }
        throw new Types.BadServerResponse(response);
    }

    /**
     * Return a list of all the VIF_metrics instances known to the system.
     *
     * @return references to all objects
     */
    public static Set<VIFMetrics> getAll() throws Types.BadServerResponse, ConnectionHelper.NoConnectionOnThisThreadException, XmlRpcException {
        String method_call = "VIF_metrics.get_all";
        String session = ConnectionHelper.instance().getSession();
        Object[] method_params = { Marshalling.toXMLRPC(session) };
        Map response = ConnectionHelper.instance().dispatch(method_call, method_params);
        if (response.get("Status").equals("Success")) {
            Object result = response.get("Value");
            return Types.toSetOfVIFMetrics(result);
        } else if (response.get("Status").equals("Failure")) {
            Object[] error = (Object[]) response.get("ErrorDescription");
        }
        throw new Types.BadServerResponse(response);
    }

    /**
     * Get all the VIFMetrics Records at once, in a single XML RPC call
     *
     * @return A map from VIFMetrics to VIFMetrics.Record
     */
    public static Map<VIFMetrics, VIFMetrics.Record> getAllRecords() throws Types.BadServerResponse, ConnectionHelper.NoConnectionOnThisThreadException, XmlRpcException {
        String method_call = "VIF_metrics.get_all_records";
        String session = ConnectionHelper.instance().getSession();
        Object[] method_params = { Marshalling.toXMLRPC(session) };
        Map response = ConnectionHelper.instance().dispatch(method_call, method_params);
        if (response.get("Status").equals("Success")) {
            Object result = response.get("Value");
            return Types.toMapOfVIFMetricsVIFMetricsRecord(result);
        } else if (response.get("Status").equals("Failure")) {
            Object[] error = (Object[]) response.get("ErrorDescription");
        }
        throw new Types.BadServerResponse(response);
    }
}
