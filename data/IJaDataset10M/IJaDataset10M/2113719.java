package net.ukrpost.storage.maildir;

import org.apache.log4j.Logger;
import javax.mail.Flags;
import java.io.File;
import java.net.InetAddress;

public class MaildirFilename implements Comparable {

    private static Logger log = Logger.getLogger(MaildirFilename.class);

    /**
     * deliveryCounter should be incremented each time a new delivery comes
     * from the same thread.
     */
    protected int deliveryCounter = 0;

    private String originalfilename = null;

    private int timestamp = 0;

    private String deliveryid = null;

    private boolean modifiedDeliveryId = false;

    private String hostname = null;

    protected int deliveryProcessId = (Thread.currentThread().hashCode() % 65534 + 1);

    /**
     * <tt>uniq</tt> and <tt>info</tt> are defined in Maildir specification
     * as two parts of any maildir messages filename.
     * Here they are used to speedup toString() and similar operations.
     */
    private String uniq = null;

    private String info = null;

    private long size = -1;

    protected Flags flags = new Flags();

    private boolean modified = true;

    private static char colon = ':';

    static {
        if (System.getProperty("os.name").toLowerCase().indexOf("windows") != -1) {
            colon = ';';
        }
    }

    public MaildirFilename() {
        try {
            hostname = InetAddress.getLocalHost().getHostName();
        } catch (Exception ex) {
            hostname = "localhost";
        }
        timestamp = (int) (System.currentTimeMillis() / 1000);
    }

    private File theFile = null;

    File getFile() {
        return theFile;
    }

    void setFile(File file) {
        theFile = file;
    }

    public MaildirFilename(File f) {
        this(f.getName());
        theFile = f;
        if (!f.getName().startsWith(getUniq())) {
            setTimestamp((int) (f.lastModified() / 1000));
        }
        log.debug("constructed from file: " + f.getName());
        log.debug("file last modified: " + f.lastModified());
        log.debug("uniq: " + getUniq());
        log.debug("host: " + getHostname());
        log.debug("tstamp: " + getTimestamp());
    }

    public MaildirFilename(String str) {
        this();
        if (str != null) originalfilename = str; else return;
        log.debug("MaildirFilename(" + str + ")");
        int dot_one = str.indexOf(".");
        if (dot_one == -1) return;
        int dot_two = str.indexOf(".", dot_one + 1);
        if (dot_two == -1) return;
        int dot_three = str.indexOf(colon, dot_two + 1);
        String stamp = str.substring(0, dot_one);
        try {
            timestamp = Integer.parseInt(stamp);
        } catch (NumberFormatException nfex) {
            return;
        }
        setDeliveryId(str.substring(dot_one + 1, dot_two));
        modifiedDeliveryId = false;
        String host = str.substring(dot_two + 1, (dot_three == -1 ? str.length() : dot_three));
        info = (dot_three == -1 ? null : str.substring(dot_three + 1));
        int sizeidx = host.indexOf(",S=");
        if (sizeidx != -1) {
            String sizestr = host.substring(sizeidx + 3);
            hostname = host.substring(0, sizeidx);
            try {
                size = Long.parseLong(sizestr);
            } catch (NumberFormatException nfex) {
            }
        } else {
            hostname = host;
        }
        setHostname(hostname);
        if (info != null) {
            int flagsidx = info.indexOf("2,");
            if (flagsidx != -1) for (int i = 2; i < info.length(); i++) {
                char flag = info.charAt(i);
                log.debug("flag: " + flag);
                switch(flag) {
                    case 'S':
                        flags.add(Flags.Flag.SEEN);
                        break;
                    case 'R':
                        flags.add(Flags.Flag.ANSWERED);
                        break;
                    case 'T':
                        flags.add(Flags.Flag.DELETED);
                        break;
                    case 'D':
                        flags.add(Flags.Flag.DRAFT);
                        break;
                    case 'F':
                        flags.add(Flags.Flag.FLAGGED);
                        break;
                }
            }
        }
        uniq = getUniq();
        modified = false;
    }

    public boolean getFlag(Flags.Flag f) {
        return flags.contains(f);
    }

    public Flags getFlags() {
        return new Flags(flags);
    }

    public void setFlags(Flags f) {
        modified = true;
        flags.add(f);
    }

    public void setFlag(Flags.Flag f) {
        modified = true;
        flags.add(f);
    }

    public void removeFlag(Flags.Flag f) {
        modified = true;
        flags.remove(f);
    }

    public void removeFlags(Flags f) {
        modified = true;
        flags.remove(f);
    }

    public long getSize() {
        return size;
    }

    public void setSize(long sz) {
        if (sz == size) return;
        modified = true;
        size = sz;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String host) {
        modified = true;
        hostname = host;
    }

    public String getDeliveryId() {
        if (deliveryid == null || modifiedDeliveryId) deliveryid = Integer.toString(deliveryProcessId) + (deliveryCounter == 0 ? "" : "_" + Integer.toString(deliveryCounter));
        return deliveryid;
    }

    public void setDeliveryId(String id) {
        if (id.equals(deliveryid)) return;
        modified = true;
        modifiedDeliveryId = true;
        try {
            deliveryProcessId = Integer.parseInt(id);
            return;
        } catch (NumberFormatException nfex) {
        }
        int underscore = id.lastIndexOf("_");
        if (underscore != -1) {
            String procId = id.substring(0, underscore);
            String delivCnt = id.substring(underscore + 1);
            try {
                deliveryProcessId = Integer.parseInt(procId);
                deliveryCounter = Integer.parseInt(delivCnt);
            } catch (NumberFormatException nfex) {
            }
        }
        deliveryid = id;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int ts) {
        if (ts == timestamp) return;
        modified = true;
        timestamp = ts;
    }

    public int getDeliveryCounter() {
        return deliveryCounter;
    }

    public void setDeliveryCounter(int dc) {
        modified = true;
        modifiedDeliveryId = true;
        deliveryCounter = dc;
    }

    public String getUniq() {
        if (modified || modifiedDeliveryId) {
            final StringBuffer sb = new StringBuffer();
            sb.append(getTimestamp()).append(".");
            sb.append(getDeliveryId()).append(".");
            sb.append(getHostname());
            uniq = sb.toString();
        }
        return uniq;
    }

    public String getInfo() {
        if (modified) {
            final StringBuffer sb = new StringBuffer();
            Flags.Flag[] flgs = flags.getSystemFlags();
            if (flgs.length > 0) sb.append(colon).append("2,");
            for (int i = flgs.length - 1; i >= 0; i--) {
                if (flgs[i] == Flags.Flag.SEEN) sb.append('S'); else if (flgs[i] == Flags.Flag.ANSWERED) sb.append('R'); else if (flgs[i] == Flags.Flag.DELETED) sb.append('T'); else if (flgs[i] == Flags.Flag.DRAFT) sb.append('D'); else if (flgs[i] == Flags.Flag.FLAGGED) sb.append('F');
            }
            info = sb.toString();
        }
        return info;
    }

    /**
     * Compares two MaildirFilenames taking deliveryid and deliverycounter to account.
     */
    public int compareTo(Object o) {
        if (!(o instanceof MaildirFilename) || o == null || o == this) return 0;
        MaildirFilename m = (MaildirFilename) o;
        int timestampRes = getTimestamp() - m.getTimestamp();
        if (timestampRes != 0) return timestampRes;
        int procRes = deliveryProcessId - m.deliveryProcessId;
        if (procRes != 0) return procRes;
        int counterRes = deliveryCounter - m.deliveryCounter;
        if (counterRes != 0) return counterRes;
        int delivRes = getDeliveryId().compareTo(m.getDeliveryId());
        if (delivRes != 0) return delivRes;
        int hostnameRes = getHostname().compareTo(m.getHostname());
        if (hostnameRes != 0) return hostnameRes;
        return 0;
    }

    public boolean equals(Object o) {
        if (!(o instanceof MaildirFilename) || o == null) return false;
        if (o == this) return true;
        final MaildirFilename omfn = (MaildirFilename) o;
        return toString().equals(omfn.toString());
    }

    public String toString() {
        if (!modified) return originalfilename;
        final StringBuffer sb = new StringBuffer();
        sb.append(getUniq());
        if (size > 0) sb.append(",S=").append(size);
        sb.append(getInfo());
        return sb.toString();
    }
}
