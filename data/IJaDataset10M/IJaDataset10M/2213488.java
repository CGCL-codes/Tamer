package quamj.qps.plugins.oci_qiop;

import com.ooc.CORBA.InputStream;
import com.ooc.CORBA.OutputStream;

/**
 *
 * @author  localadmin
 * @version
 */
public class Acceptor_impl extends org.omg.CORBA.LocalObject implements com.ooc.OCI.Acceptor {

    private org.apache.log4j.Category logCategory = null;

    String[] hosts_;

    private boolean multiProfile_;

    private int port_;

    java.net.ServerSocket socket_;

    private java.net.InetAddress localAddress_;

    private AcceptorInfo_impl info_;

    private org.omg.CORBA.ORB orb_ = null;

    /** Creates new Acceptor_impl */
    public Acceptor_impl(org.omg.CORBA.ORB orb, String[] hosts, int port, int backlog) {
        logCategory = org.apache.log4j.Category.getInstance(Acceptor_impl.class.getName());
        logCategory.debug("[" + logCategory.getName() + "] created OK!");
        orb_ = orb;
        port_ = port;
        hosts_ = hosts;
        logCategory.debug("Local Host Name = " + hosts_[0] + " Port = " + port_);
        info_ = new AcceptorInfo_impl(this);
        if (backlog == 0) {
            backlog = 50;
        }
        try {
            localAddress_ = java.net.InetAddress.getLocalHost();
        } catch (java.net.UnknownHostException ex) {
            throw new org.omg.CORBA.COMM_FAILURE(com.ooc.OB.MinorCodes.describeCommFailure(com.ooc.OB.MinorCodes.MinorGethostbyname) + ": " + ex.toString(), com.ooc.OB.MinorCodes.MinorGethostbyname, org.omg.CORBA.CompletionStatus.COMPLETED_NO);
        }
        try {
            socket_ = new java.net.ServerSocket(port, backlog);
            port_ = socket_.getLocalPort();
        } catch (java.net.BindException ex) {
            throw new org.omg.CORBA.COMM_FAILURE(com.ooc.OB.MinorCodes.describeCommFailure(com.ooc.OB.MinorCodes.MinorBind) + ": " + ex.toString(), com.ooc.OB.MinorCodes.MinorBind, org.omg.CORBA.CompletionStatus.COMPLETED_NO);
        } catch (java.io.IOException ex) {
            throw new org.omg.CORBA.COMM_FAILURE(com.ooc.OB.MinorCodes.describeCommFailure(com.ooc.OB.MinorCodes.MinorSocket) + ": " + ex.toString(), com.ooc.OB.MinorCodes.MinorSocket, org.omg.CORBA.CompletionStatus.COMPLETED_NO);
        }
    }

    public String id() {
        return PLUGIN_ID.value;
    }

    public int tag() {
        return TAG_QIOP.value;
    }

    public int handle() {
        throw new org.omg.CORBA.NO_IMPLEMENT();
    }

    public void listen() {
    }

    public com.ooc.OCI.ProfileInfo[] get_local_profiles(org.omg.IOP.IOR ior) {
        com.ooc.OCI.ProfileInfoSeqHolder profileInfoSeq = new com.ooc.OCI.ProfileInfoSeqHolder();
        profileInfoSeq.value = new com.ooc.OCI.ProfileInfo[0];
        for (int i = 0; i < hosts_.length; i++) {
            quamj.qps.plugins.oci_qiop.Util.extractAllProfileInfos(ior, profileInfoSeq, true, hosts_[i], port_, true);
        }
        return profileInfoSeq.value;
    }

    public void add_profiles(com.ooc.OCI.ProfileInfo profileInfo, org.omg.IOP.IORHolder ior) {
        if (port_ == 0) {
            throw new RuntimeException();
        }
        if ((profileInfo.major == 1) && (profileInfo.minor == 2)) {
            for (int i = 0; i < hosts_.length; i++) {
                quamj.qps.plugins.oci_qiop.ProfileBody_1_0 body = new quamj.qps.plugins.oci_qiop.ProfileBody_1_0();
                body.qiop_version = new quamj.qps.plugins.oci_qiop.Version(profileInfo.major, profileInfo.minor);
                body.host = hosts_[i];
                body.port = quamj.qps.plugins.oci_qiop.Util.convertPortToCorba(port_);
                body.object_key = profileInfo.key;
                int len = ior.value.profiles.length + 1;
                org.omg.IOP.TaggedProfile[] profiles = new org.omg.IOP.TaggedProfile[len];
                System.arraycopy(ior.value.profiles, 0, profiles, 0, ior.value.profiles.length);
                ior.value.profiles = profiles;
                ior.value.profiles[len - 1] = new org.omg.IOP.TaggedProfile();
                ior.value.profiles[len - 1].tag = this.tag();
                com.ooc.OCI.Buffer buf = new com.ooc.OCI.Buffer();
                OutputStream out = new OutputStream(buf);
                out._OB_writeEndian();
                quamj.qps.plugins.oci_qiop.ProfileBody_1_0Helper.write(out, body);
                ior.value.profiles[len - 1].profile_data = new byte[buf.length()];
                System.arraycopy(buf.data(), 0, ior.value.profiles[len - 1].profile_data, 0, buf.length());
            }
        }
    }

    public void shutdown() {
    }

    public com.ooc.OCI.Transport accept(boolean block) {
        java.net.Socket socket;
        try {
            if (!block) {
                socket_.setSoTimeout(1);
            } else {
                socket_.setSoTimeout(0);
            }
            socket = socket_.accept();
        } catch (java.io.InterruptedIOException ex) {
            logCategory.error("java.io.InterruptedIOException");
            if (!block) {
                return null;
            } else {
                throw new org.omg.CORBA.COMM_FAILURE(com.ooc.OB.MinorCodes.describeCommFailure(com.ooc.OB.MinorCodes.MinorAccept) + ": " + ex.toString(), com.ooc.OB.MinorCodes.MinorAccept, org.omg.CORBA.CompletionStatus.COMPLETED_NO);
            }
        } catch (java.io.IOException ex) {
            logCategory.error("java.io.IOException");
            throw new org.omg.CORBA.COMM_FAILURE(com.ooc.OB.MinorCodes.describeCommFailure(com.ooc.OB.MinorCodes.MinorAccept) + ": " + ex.toString(), com.ooc.OB.MinorCodes.MinorAccept, org.omg.CORBA.CompletionStatus.COMPLETED_NO);
        }
        try {
            socket.setTcpNoDelay(true);
        } catch (java.net.SocketException ex) {
            logCategory.error("java.net.SocketException");
            throw new org.omg.CORBA.COMM_FAILURE(com.ooc.OB.MinorCodes.describeCommFailure(com.ooc.OB.MinorCodes.MinorSetsockopt) + ": " + ex.toString(), com.ooc.OB.MinorCodes.MinorSetsockopt, org.omg.CORBA.CompletionStatus.COMPLETED_NO);
        }
        Transport_impl tr = null;
        try {
            tr = new Transport_impl(this, socket, orb_);
            socket = null;
        } catch (org.omg.CORBA.SystemException ex) {
            logCategory.error("org.omg.CORBA.SystemException");
            try {
                socket.close();
            } catch (java.io.IOException e) {
            }
            throw ex;
        }
        quamj.qps.plugins.oci_qiop.Binding binding = quamj.qps.plugins.oci_qiop.Binding.create_for_server(orb_, tr);
        com.ooc.OCI.TransportInfo trInfo = tr.get_info();
        try {
            info_._OB_callAcceptCB(trInfo);
        } catch (org.omg.CORBA.SystemException ex) {
            tr.close();
            throw ex;
        }
        return tr;
    }

    public com.ooc.OCI.Transport connect_self() {
        java.net.Socket socket = null;
        try {
            socket = new java.net.Socket(localAddress_, port_);
        } catch (java.net.ConnectException ex) {
            throw new org.omg.CORBA.TRANSIENT(com.ooc.OB.MinorCodes.describeTransient(com.ooc.OB.MinorCodes.MinorConnectFailed) + ": " + ex.toString(), com.ooc.OB.MinorCodes.MinorConnectFailed, org.omg.CORBA.CompletionStatus.COMPLETED_NO);
        } catch (java.io.IOException ex) {
            throw new org.omg.CORBA.COMM_FAILURE(com.ooc.OB.MinorCodes.describeCommFailure(com.ooc.OB.MinorCodes.MinorSocket) + ": " + ex.toString(), com.ooc.OB.MinorCodes.MinorSocket, org.omg.CORBA.CompletionStatus.COMPLETED_NO);
        }
        try {
            socket.setTcpNoDelay(true);
        } catch (java.net.SocketException ex) {
            try {
                socket.close();
            } catch (java.io.IOException e) {
            }
            throw new org.omg.CORBA.COMM_FAILURE(com.ooc.OB.MinorCodes.describeCommFailure(com.ooc.OB.MinorCodes.MinorSetsockopt) + ": " + ex.toString(), com.ooc.OB.MinorCodes.MinorSetsockopt, org.omg.CORBA.CompletionStatus.COMPLETED_NO);
        }
        com.ooc.OCI.Transport tr = null;
        try {
            tr = new Transport_impl(this, socket, orb_);
        } catch (org.omg.CORBA.SystemException ex) {
            try {
                socket.close();
            } catch (java.io.IOException e) {
            }
            throw ex;
        }
        return tr;
    }

    public com.ooc.OCI.AcceptorInfo get_info() {
        return info_;
    }

    public void close() {
        info_._OB_destroy();
        try {
            socket_.close();
            socket_ = null;
        } catch (java.io.IOException ex) {
        }
    }

    public void finalize() throws Throwable {
        if (socket_ != null) {
            close();
        }
        super.finalize();
    }

    private String printProfile(com.ooc.OCI.ProfileInfo profileInfo) {
        StringBuffer strBuffer = new StringBuffer();
        strBuffer.append(" version: [").append(profileInfo.major).append(".");
        strBuffer.append(profileInfo.minor).append("] ");
        strBuffer.append(" tag: [").append(profileInfo.id).append("]");
        return strBuffer.toString();
    }

    private void printSocket(java.net.Socket socket) {
        String remote_addr = (socket.getInetAddress()).getHostAddress();
        int remote_port = socket.getPort();
        String local_addr = (socket.getLocalAddress()).getHostAddress();
        int local_port = (socket.getLocalPort());
    }

    private void printServerSocket(java.net.ServerSocket socket) {
        String local_addr = (socket.getInetAddress()).getHostAddress();
        int local_port = (socket.getLocalPort());
    }
}
