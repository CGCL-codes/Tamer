package org.openorb.orb.messaging;

import org.omg.Messaging.RELATIVE_REQ_TIMEOUT_POLICY_TYPE;
import org.omg.Messaging.RELATIVE_RT_TIMEOUT_POLICY_TYPE;
import org.omg.Messaging.REPLY_END_TIME_POLICY_TYPE;
import org.omg.Messaging.REPLY_START_TIME_POLICY_TYPE;
import org.omg.Messaging.REQUEST_END_TIME_POLICY_TYPE;
import org.omg.Messaging.REQUEST_START_TIME_POLICY_TYPE;
import org.omg.Messaging.RelativeRoundtripTimeoutPolicy;
import org.omg.Messaging.SYNC_SCOPE_POLICY_TYPE;
import org.omg.Messaging.SyncScopeHelper;
import org.omg.Messaging.SyncScopePolicy;
import org.omg.CORBA.TCKind;

/**
 * The PolicyFactory implementation for the messaging policies.
 *
 * @author Michael Rumpf
 * @version $Revision: 1.6 $ $Date: 2004/02/17 22:13:55 $
 */
final class PolicyFactoryImpl extends org.omg.CORBA.LocalObject implements org.omg.PortableInterceptor.PolicyFactory {

    /** Creates new PolicyFactory */
    private PolicyFactoryImpl() {
    }

    private static PolicyFactoryImpl s_instance = null;

    static PolicyFactoryImpl getInstance() {
        if (s_instance == null) {
            s_instance = new PolicyFactoryImpl();
        }
        return s_instance;
    }

    public synchronized org.omg.CORBA.Policy create_policy(int type, org.omg.CORBA.Any value) throws org.omg.CORBA.PolicyError {
        switch(type) {
            case SYNC_SCOPE_POLICY_TYPE.value:
                if (!value.type().equal(SyncScopeHelper.type()) && value.type().kind() != TCKind.tk_short && value.type().kind() != TCKind.tk_ushort) {
                    throw new org.omg.CORBA.PolicyError(org.omg.CORBA.BAD_POLICY_TYPE.value);
                }
                return createSyncScopePolicy(value.extract_short());
            case RELATIVE_REQ_TIMEOUT_POLICY_TYPE.value:
                if (!value.type().equal(org.omg.TimeBase.TimeTHelper.type())) {
                    throw new org.omg.CORBA.PolicyError(org.omg.CORBA.BAD_POLICY_TYPE.value);
                }
                throw new org.omg.CORBA.PolicyError(org.omg.CORBA.UNSUPPORTED_POLICY.value);
            case RELATIVE_RT_TIMEOUT_POLICY_TYPE.value:
                if (!value.type().equal(org.omg.TimeBase.TimeTHelper.type())) {
                    throw new org.omg.CORBA.PolicyError(org.omg.CORBA.BAD_POLICY_TYPE.value);
                }
                return new RelativeRoundtripTimeoutPolicyImpl(org.omg.TimeBase.TimeTHelper.extract(value));
            case REPLY_END_TIME_POLICY_TYPE.value:
                if (!value.type().equal(org.omg.TimeBase.UtcTHelper.type())) {
                    throw new org.omg.CORBA.PolicyError(org.omg.CORBA.BAD_POLICY_TYPE.value);
                }
                throw new org.omg.CORBA.PolicyError(org.omg.CORBA.UNSUPPORTED_POLICY.value);
            case REPLY_START_TIME_POLICY_TYPE.value:
                if (!value.type().equal(org.omg.TimeBase.UtcTHelper.type())) {
                    throw new org.omg.CORBA.PolicyError(org.omg.CORBA.BAD_POLICY_TYPE.value);
                }
                throw new org.omg.CORBA.PolicyError(org.omg.CORBA.UNSUPPORTED_POLICY.value);
            case REQUEST_END_TIME_POLICY_TYPE.value:
                if (!value.type().equal(org.omg.TimeBase.UtcTHelper.type())) {
                    throw new org.omg.CORBA.PolicyError(org.omg.CORBA.BAD_POLICY_TYPE.value);
                }
                throw new org.omg.CORBA.PolicyError(org.omg.CORBA.UNSUPPORTED_POLICY.value);
            case REQUEST_START_TIME_POLICY_TYPE.value:
                if (!value.type().equal(org.omg.TimeBase.UtcTHelper.type())) {
                    throw new org.omg.CORBA.PolicyError(org.omg.CORBA.BAD_POLICY_TYPE.value);
                }
                throw new org.omg.CORBA.PolicyError(org.omg.CORBA.UNSUPPORTED_POLICY.value);
            default:
                throw new org.omg.CORBA.PolicyError(org.omg.CORBA.BAD_POLICY.value);
        }
    }

    private SyncScopePolicy m_syncScope_none;

    private SyncScopePolicy m_syncScope_transport;

    private SyncScopePolicy m_syncScope_server;

    private SyncScopePolicy m_syncScope_target;

    private SyncScopePolicy createSyncScopePolicy(short polValue) throws org.omg.CORBA.PolicyError {
        switch(polValue) {
            case org.omg.Messaging.SYNC_NONE.value:
                if (m_syncScope_none == null) {
                    m_syncScope_none = new SyncScopePolicyImpl(polValue);
                }
                return m_syncScope_none;
            case org.omg.Messaging.SYNC_WITH_TRANSPORT.value:
                if (m_syncScope_transport == null) {
                    m_syncScope_transport = new SyncScopePolicyImpl(polValue);
                }
                return m_syncScope_transport;
            case org.omg.Messaging.SYNC_WITH_SERVER.value:
                if (m_syncScope_server == null) {
                    m_syncScope_server = new SyncScopePolicyImpl(polValue);
                }
                return m_syncScope_server;
            case org.omg.Messaging.SYNC_WITH_TARGET.value:
                if (m_syncScope_target == null) {
                    m_syncScope_target = new SyncScopePolicyImpl(polValue);
                }
                return m_syncScope_target;
            default:
                throw new org.omg.CORBA.PolicyError(org.omg.CORBA.BAD_POLICY_VALUE.value);
        }
    }

    private static class SyncScopePolicyImpl extends org.omg.CORBA.LocalObject implements SyncScopePolicy {

        SyncScopePolicyImpl(short val) {
            m_sync_scope = val;
        }

        private short m_sync_scope;

        public short synchronization() {
            return m_sync_scope;
        }

        public void destroy() {
        }

        public org.omg.CORBA.Policy copy() {
            return this;
        }

        public int policy_type() {
            return SYNC_SCOPE_POLICY_TYPE.value;
        }
    }

    private static class RelativeRoundtripTimeoutPolicyImpl extends org.omg.CORBA.LocalObject implements RelativeRoundtripTimeoutPolicy {

        RelativeRoundtripTimeoutPolicyImpl(long val) {
            m_relative_expiry = val;
        }

        private long m_relative_expiry;

        public long relative_expiry() {
            return m_relative_expiry;
        }

        public void destroy() {
        }

        public org.omg.CORBA.Policy copy() {
            return this;
        }

        public int policy_type() {
            return RELATIVE_RT_TIMEOUT_POLICY_TYPE.value;
        }
    }
}
