package org.jgroups.protocols;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;
import org.jgroups.Address;
import org.jgroups.Event;
import org.jgroups.Global;
import org.jgroups.Header;
import org.jgroups.Message;
import org.jgroups.stack.IpAddress;
import org.jgroups.stack.Protocol;
import org.jgroups.structures.IP;
import org.jgroups.util.Streamable;

/**
 * DRM : Delete Replicated Messages
 * This protocol removes the replicated messages,
 * besides it can also filters the received packets that do not have the DRM header
 * 
 * @author micky
 */
public class DRM extends Protocol implements Runnable {

    Thread drm_thread_sleep = null;

    int max_size_vec = 50;

    long time_clear = 10000;

    Vector drm_vec = new Vector();

    private long idDRM_actual = 0;

    Address local_addr = null;

    /**
     * Creates a new instance of DRM
     */
    public DRM() {
        drm_thread_sleep = new Thread(this, "DRM Thread sleep");
        drm_thread_sleep.start();
    }

    public String getName() {
        return "DRM";
    }

    public void up(Event evt) {
        if (evt.getType() == Event.SET_LOCAL_ADDRESS) {
            local_addr = (Address) evt.getArg();
            passUp(evt);
        } else if (evt.getType() == Event.MSG) {
            Message msg = (Message) evt.getArg();
            DRMHeader drmh = (DRMHeader) msg.getHeader("DRM");
            if (drmh != null) {
                String aux = msg.getSrc().toString() + drmh.idDRM;
                msg.removeHeader("DRM");
                if (msg.getSrc().compareTo(local_addr) != 0) {
                    IP local = new IP(((IpAddress) local_addr).getIpAddress());
                    IP source = new IP(((IpAddress) msg.getSrc()).getIpAddress());
                    if (local.compareTo(source) != 0) {
                        synchronized (this) {
                            if (!drm_vec.contains(aux)) {
                                drm_vec.addElement(aux);
                                passUp(new Event(Event.MSG, msg));
                            }
                        }
                    }
                } else if ((msg.getSrc().compareTo(local_addr) == 0) && (msg.getDest().compareTo(local_addr) == 0)) {
                    passUp(new Event(Event.MSG, msg));
                }
            } else passUp(evt);
        } else passUp(evt);
    }

    public void down(Event evt) {
        if (evt.getType() == Event.MSG) {
            Message msg = (Message) evt.getArg();
            long id = newId();
            msg.putHeader(getName(), new DRMHeader(id));
            passDown(evt);
        } else passDown(evt);
    }

    public void run() {
        while (true) {
            try {
                drm_thread_sleep.sleep(time_clear);
                synchronized (this) {
                    removeElements(drm_vec);
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    private long newId() {
        if (idDRM_actual == Long.MAX_VALUE - 1) {
            idDRM_actual = 0;
        } else idDRM_actual++;
        return idDRM_actual;
    }

    private boolean removeElements(Vector v) {
        boolean pasa = false;
        if (v.size() > max_size_vec) {
            pasa = true;
            int num_elim = (v.size() - max_size_vec);
            for (int i = 0; i < num_elim; i++) v.remove(0);
        }
        return pasa;
    }

    public boolean setProperties(Properties props) {
        String str;
        str = props.getProperty("max_size");
        if (str != null) {
            max_size_vec = Integer.parseInt(str);
            props.remove("max_size");
        } else if (str != null) {
            time_clear = Integer.parseInt(str);
            props.remove("time_clear");
        }
        if (props.size() > 0) {
            return false;
        }
        return true;
    }

    public String toString() {
        return getName();
    }

    public static class DRMHeader extends Header implements Streamable {

        public long idDRM = 0;

        public DRMHeader() {
        }

        public DRMHeader(long id) {
            this.idDRM = id;
        }

        public String toString() {
            return "[idDRM=" + idDRM + "]";
        }

        public long size() {
            return Global.LONG_SIZE;
        }

        public void writeExternal(ObjectOutput out) throws IOException {
            out.writeLong(idDRM);
        }

        public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
            idDRM = in.readLong();
        }

        public void writeTo(DataOutputStream out) throws IOException {
            out.writeLong(idDRM);
        }

        public void readFrom(DataInputStream in) throws IOException, IllegalAccessException, InstantiationException {
            idDRM = in.readLong();
        }
    }
}
