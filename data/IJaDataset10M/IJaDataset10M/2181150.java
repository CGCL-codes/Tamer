package quamj.qps.configuration;

import quamj.qps.*;
import java.util.*;

/**
 * The QPS policy holds information about the status of a QoS agreement for an Object
 * reference.  The policy is custom created through the <b> QPSPolicyFactory </b>.
 *
 * <p>
 * Applications should not directly create and/or modify this policy,
 * instead an exposed by the <b> QoSRepository </b> interface should to use
 * used.
 *
 * @author   Christian Tzolov &lt;tzolov@cs.utwente.nl&gt;
 * @version  $Id: QoSPolicy_impl.java,v 1.9 2002/05/21 14:21:54 kris_tz Exp $
 */
public class QoSPolicy_impl extends org.omg.CORBA.LocalObject implements quamj.qps.Policy {

    private org.apache.log4j.Category log = null;

    private boolean isAgreed = false;

    N_S_ID_TYPE n_s_id = null;

    private QoSAccessStrategy qosAccessStrategy = null;

    public QoSPolicy_impl(String reqQoS) {
        log = org.apache.log4j.Category.getInstance(QoSPolicy_impl.class.getName());
        log.debug("[" + log.getName() + "]");
        qosAccessStrategy = QoSAccessManager.getInstance().strategy();
        isAgreed = false;
        this.req_qos(reqQoS);
    }

    public int policy_type() {
        return QPS_POLICY_ID.value;
    }

    public org.omg.CORBA.Policy copy() {
        return this;
    }

    public void destroy() {
    }

    public N_S_ID_TYPE neg_session_id() {
        if (n_s_id == null) {
            return null;
        }
        return new N_S_ID_TYPE(n_s_id.client_id, n_s_id.server_id);
    }

    public void neg_session_id(N_S_ID_TYPE val) {
        n_s_id = val;
    }

    public boolean has_agreed_qos() {
        return isAgreed;
    }

    public String req_qos() {
        Object reqQoSObj = qosAccessStrategy.get_required_qos(this);
        if (reqQoSObj == null) {
            return null;
        }
        return (String) reqQoSObj;
    }

    public void req_qos(String val) {
        isAgreed = false;
        log.debug("-> UNBOUND Binding state :");
        qosAccessStrategy.set_required_qos(this, val);
    }

    public String agreed_qos() {
        String agreedQoS = qosAccessStrategy.get_required_qos(this);
        return agreedQoS;
    }

    public void agreed_qos(String _val) {
        log.debug("agreedQoS is :" + _val);
        qosAccessStrategy.set_agreed_qos(this, _val);
        isAgreed = (_val != null);
    }

    public boolean has_NO_QOS() {
        String requiredQoS = this.req_qos();
        if ((requiredQoS != null) && (requiredQoS.equals(quamj.qps.util.QML.NO_QOS))) {
            return true;
        }
        return false;
    }
}
