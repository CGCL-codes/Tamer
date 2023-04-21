package org.openejb.ri.server;

import java.net.InetAddress;

/**
* The unique identity of the client application that invoked the method.
* Uniquely identifies the process (Java Virtual Machine) and computer (URL) that invoked the method.
*/
public class ClientIdentity implements java.io.Serializable {

    static final String JVM_ID = Runtime.getRuntime().hashCode() + ":" + System.currentTimeMillis();

    protected InetAddress inet;

    protected String jvm;

    protected String proxy;

    public ClientIdentity() {
        jvm = JVM_ID;
        try {
            inet = InetAddress.getLocalHost();
        } catch (Exception e) {
            inet = null;
        }
    }

    public String getJvmID() {
        return jvm;
    }

    public InetAddress getInetAddress() {
        return inet;
    }

    public boolean equals(Object other) {
        if (other instanceof ClientIdentity) {
            ClientIdentity otherIdentity = (ClientIdentity) other;
            if (this.jvm.equals(otherIdentity.jvm)) {
                if ((otherIdentity.inet == null && this.inet == null) || this.inet.equals(otherIdentity.inet)) return true;
            }
        }
        return false;
    }

    public String toString() {
        return inet.toString() + ":" + jvm;
    }

    public int hashCode() {
        return this.toString().hashCode();
    }
}
