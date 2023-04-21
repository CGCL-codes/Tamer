package quamj.qps.plugins.oci_qiop.control;

import org.omg.IOP.*;
import org.omg.PortableInterceptor.ClientRequestInterceptor;
import org.omg.PortableInterceptor.ClientRequestInfo;
import org.omg.PortableInterceptor.Current;
import org.omg.PortableInterceptor.CurrentHelper;
import quamj.qps.plugins.oci_qiop.*;
import quamj.qps.plugins.oci_qiop.configuration.*;
import quamj.qps.*;
import quamj.qps.util.*;
import quamj.qps.control.*;
import quamj.qps.configuration.*;

public class PerformanceClientRequestInteceptor extends org.omg.CORBA.LocalObject implements ClientRequestInterceptor {

    private org.apache.log4j.Category log = null;

    private int slot_id = -1;

    private Codec codec_ = null;

    private PhysicalTime physicalTime_ = null;

    private org.omg.CORBA.ORB orb_ = null;

    private QoSControl qosControl_ = null;

    private String[] objectOperations = { "_is_a", "_is_equivalent", "_non_existent", "_hash", "_duplicate", "_release", "_get_interface", "_get_interface_def", "_request", "_create_request", "_get_policy", "_set_policy_override", "_get_domain_managers", "negotiate_QoS" };

    /** Creates a new instance of PerformanceClientRequestInteceptor */
    public PerformanceClientRequestInteceptor(int sl_id, Codec codec) {
        log = org.apache.log4j.Category.getInstance(this.getClass().getName());
        log.debug("[" + log.getName() + "] created OK!");
        slot_id = sl_id;
        codec_ = codec;
        physicalTime_ = new PhysicalTime("timehost.cs.utwente.nl", 3600000);
    }

    public void destroy() {
    }

    public String name() {
        return "PerformanceClientRequestInterceptor";
    }

    public void send_request(ClientRequestInfo clientRequestInfo) throws org.omg.PortableInterceptor.ForwardRequest {
        if (isQPSAware(clientRequestInfo) == false) {
            return;
        }
        try {
            if (orb_ == null) {
                log.error("send_request is called prior the orb has been initialized");
                return;
            }
            org.omg.CORBA.Any timeAny = orb_.create_any();
            long[] timeData = new long[4];
            timeData[0] = physicalTime_.getTime();
            SensorDataSeqHelper.insert(timeAny, timeData);
            ServiceContext ctx = new ServiceContext(SENSOR_DATA_REQUEST_SERVICE_ID.value, codec_.encode(timeAny));
            clientRequestInfo.add_request_service_context(ctx, false);
        } catch (org.omg.IOP.CodecPackage.InvalidTypeForEncoding invTypeEncd) {
            log.error("Ivalid type encoding", invTypeEncd);
        }
    }

    public void send_poll(ClientRequestInfo clientRequestInfo) {
    }

    public void receive_exception(ClientRequestInfo clientRequestInfo) throws org.omg.PortableInterceptor.ForwardRequest {
        if (orb_ == null) {
            log.error("send_request is called prior the orb has been initialized");
            return;
        }
        try {
            ServiceContext ctx = clientRequestInfo.get_reply_service_context(SENSOR_DATA_REPLY_SERVICE_ID.value);
            org.omg.CORBA.Any timeAny = codec_.decode(ctx.context_data);
            if (timeAny.type().kind().value() == org.omg.CORBA.TCKind._tk_null) {
                log.error("Service Context is null");
                return;
            }
            long t = physicalTime_.getTime();
            long[] timeData = SensorDataSeqHelper.extract(timeAny);
            timeData[3] = t;
            N_S_ID_TYPE n_s_id = this.getN_S_ID(clientRequestInfo);
            if (n_s_id == null) {
                return;
            }
            if (qosControl_ == null) {
                log.error("QoSContorl is NULL");
                return;
            }
            try {
                SensorCollector_impl collector = (SensorCollector_impl) qosControl_.getCollector(n_s_id);
                if (collector == null) {
                    System.out.println("no collector has been assosiated to the N_S_ID : " + n_s_id.client_id + ":" + n_s_id.server_id);
                } else {
                    collector.addArrayData(timeData);
                }
            } catch (quamj.qps.control.InvalidSessionIDException isid) {
                log.error("Invalid SessionID", isid);
            }
        } catch (org.omg.IOP.CodecPackage.FormatMismatch fm) {
            log.error("format mismatched", fm);
            return;
        } catch (org.omg.CORBA.BAD_PARAM e) {
        }
    }

    public void receive_other(ClientRequestInfo clientRequestInfo) throws org.omg.PortableInterceptor.ForwardRequest {
        System.out.println("receive_other");
    }

    public void receive_reply(ClientRequestInfo clientRequestInfo) {
        if (orb_ == null) {
            log.error("send_request is called prior the orb has been initialized");
            return;
        }
        try {
            ServiceContext ctx = clientRequestInfo.get_reply_service_context(SENSOR_DATA_REPLY_SERVICE_ID.value);
            org.omg.CORBA.Any timeAny = codec_.decode(ctx.context_data);
            if (timeAny.type().kind().value() == org.omg.CORBA.TCKind._tk_null) {
                log.error("Service Context is null");
                return;
            }
            long t = physicalTime_.getTime();
            long[] timeData = SensorDataSeqHelper.extract(timeAny);
            timeData[3] = t;
            N_S_ID_TYPE n_s_id = this.getN_S_ID(clientRequestInfo);
            if (n_s_id == null) {
                return;
            }
            if (qosControl_ == null) {
                log.error("QoSContorl is NULL");
                return;
            }
            try {
                SensorCollector_impl collector = (SensorCollector_impl) qosControl_.getCollector(n_s_id);
                if (collector == null) {
                    System.out.println("no collector has been assosiated to the N_S_ID : " + n_s_id.client_id + ":" + n_s_id.server_id);
                } else {
                    collector.addArrayData(timeData);
                }
            } catch (quamj.qps.control.InvalidSessionIDException isid) {
                log.error("Invalid SessionID", isid);
            }
        } catch (org.omg.IOP.CodecPackage.FormatMismatch fm) {
            log.error("format mismatched", fm);
            return;
        } catch (org.omg.CORBA.BAD_PARAM e) {
        }
    }

    public void _initialize(org.omg.CORBA.ORB orb) throws quamj.qps.InitializationFailedException {
        orb_ = orb;
        try {
            org.omg.CORBA.Object obj = orb.resolve_initial_references("QualityProvisioningService");
            QualityProvisioningService qps = QualityProvisioningServiceHelper.narrow(obj);
            qosControl_ = qps.getQoSControl();
        } catch (org.omg.CORBA.ORBPackage.InvalidName ex) {
            log.error("Can't resolve ", ex);
        }
    }

    private boolean isACorbaObjectOperation(String operation_name) {
        if (operation_name == null) return false;
        for (int i = 0; i < objectOperations.length; i++) {
            if (objectOperations[i].equals(operation_name)) {
                return true;
            }
        }
        return false;
    }

    private boolean isQPSAware(ClientRequestInfo ri) {
        if (this.isACorbaObjectOperation(ri.operation())) {
            return false;
        }
        try {
            org.omg.CORBA.Policy corbaPolicy = ri.get_request_policy(QIOP_POLICY_ID.value);
            QIOPPolicy qiopPolicy = QIOPPolicyHelper.narrow(corbaPolicy);
            return true;
        } catch (org.omg.CORBA.INV_POLICY invPolicy) {
        }
        return false;
    }

    private N_S_ID_TYPE getN_S_ID(ClientRequestInfo ri) {
        if (this.isACorbaObjectOperation(ri.operation())) {
            return null;
        }
        try {
            org.omg.CORBA.Policy corbaPolicy = ri.get_request_policy(QPS_POLICY_ID.value);
            quamj.qps.Policy qpsPolicy = quamj.qps.PolicyHelper.narrow(corbaPolicy);
            return qpsPolicy.neg_session_id();
        } catch (org.omg.CORBA.INV_POLICY invPolicy) {
        }
        return null;
    }
}
