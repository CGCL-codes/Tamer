package ow.ipmulticast.igmpd;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Inet4Address;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import ow.ipmulticast.FilterMode;
import ow.ipmulticast.Group;
import ow.ipmulticast.GroupSet;
import ow.ipmulticast.Host;
import ow.ipmulticast.IGMP;
import ow.ipmulticast.IGMPHandler;
import ow.ipmulticast.QuerierSet;
import ow.ipmulticast.Utilities;
import ow.ipmulticast.VirtualInterface;
import ow.util.Timer;

public final class IGMPDaemon {

    private static final Logger logger = Logger.getLogger("ipmulticast");

    public static final int TIMER_SCALE = 10;

    public static final int MAX_HOST_REPORT_DELAY = 100;

    private int robustnessVariable;

    private int queryInterval;

    private IGMPDaemonConfiguration config;

    private IGMP igmp;

    private Set<VirtualInterface> vifSet;

    private Thread updatingDaemonThread = null;

    private long lastQueryTime = 0L;

    private GroupChangeCallback callback;

    private boolean suspended = false;

    public IGMPDaemon(IGMPDaemonConfiguration config) throws IOException {
        this.config = config;
        this.robustnessVariable = config.getInitialRobustnessVariable();
        this.queryInterval = config.getInitialQueryInterval();
        this.igmp = IGMP.getInstance();
        this.vifSet = VirtualInterface.getVirtualInterfaces();
    }

    public void start(GroupChangeCallback callback) {
        Runnable r;
        Thread th;
        this.callback = callback;
        Set<Group> s = new HashSet<Group>();
        for (VirtualInterface vif : this.vifSet) {
            s.clear();
            GroupSet groupSet = vif.getGroupSet();
            for (Group group : groupSet.getGroups()) {
                s.add(group);
            }
            callback.included(s, vif);
        }
        s = null;
        this.igmp.start(new Handler());
        r = new Runnable() {

            public void run() {
                for (int i = 0; i < robustnessVariable; i++) {
                    queryGroups();
                    try {
                        Thread.sleep(queryInterval * 1000 / 4);
                    } catch (InterruptedException e) {
                        logger.log(Level.WARNING, "A thread sending startup queries has been interrupted.", e);
                        break;
                    }
                }
            }
        };
        th = new Thread(r);
        th.setName("Startup Query Issuer");
        th.setDaemon(true);
        th.start();
        r = new UpdatingDaemon();
        th = new Thread(r);
        th.setName("Updating Daemon");
        th.setDaemon(true);
        th.start();
        this.updatingDaemonThread = th;
    }

    public synchronized void stop() {
        this.igmp.stop();
        if (this.updatingDaemonThread != null) {
            this.updatingDaemonThread.interrupt();
        }
    }

    public synchronized void suspend() {
        this.igmp.suspend();
        this.suspended = true;
    }

    public synchronized void resume() {
        this.igmp.resume();
        this.suspended = false;
        this.notifyAll();
    }

    /**
	 * Returns the current Group Membership Interval.
	 */
    public long getGroupMembershipInterval() {
        return robustnessVariable * (queryInterval * 1000) + (config.getQueryResponseInterval() * 100);
    }

    private class Handler implements IGMPHandler {

        public void process(Inet4Address src, Inet4Address dest, int type, int code, Inet4Address groupAddress, byte[] data, VirtualInterface vif) {
            QuerierSet querierSet;
            GroupSet groupSet;
            Group group;
            Host host;
            int igmpVersion;
            boolean added = false;
            if (vif == null) {
                return;
            }
            switch(type) {
                case IGMP.IGMP_MEMBERSHIP_QUERY:
                    if (vif == null) break;
                    if (data.length >= 12) {
                        igmpVersion = 3;
                    } else {
                        if (code != 0) igmpVersion = 2; else igmpVersion = 1;
                    }
                    boolean suppressRouterProcessing = false;
                    if (igmpVersion >= 3) {
                        suppressRouterProcessing = (((int) data[8] & 0x08) != 0);
                    }
                    logger.log(Level.INFO, "A membership query received: IGMPv" + igmpVersion + " (data.length:" + data.length + ") from " + src + " to " + groupAddress + ".");
                    if (!suppressRouterProcessing) {
                        IGMPDaemon.this.lastQueryTime = Timer.currentTimeMillis();
                        querierSet = vif.getQuerierSet();
                        querierSet.registerQuerier(src, igmpVersion);
                        if (!config.getKeepBeingQuerier()) {
                            long localAddressLong = ((long) vif.getLocalAddressAsInt()) & 0xffffffff;
                            long srcAddressLong = Utilities.Inet4AddressToLong(src);
                            if (srcAddressLong < localAddressLong) {
                                vif.setQuerier(false);
                                logger.log(Level.INFO, "Current querier: " + src);
                            }
                        }
                        if (igmpVersion >= 3) {
                            int qrv = data[8] & 0x7;
                            int oldRobustnessVariable = robustnessVariable;
                            if (qrv != 0) {
                                robustnessVariable = qrv;
                            } else {
                                robustnessVariable = config.getInitialRobustnessVariable();
                            }
                            if (robustnessVariable != oldRobustnessVariable) {
                                logger.log(Level.INFO, "Robustness variable: " + robustnessVariable);
                            }
                            int qqic = data[9] & 0xff;
                            int qqi;
                            int oldQueryInterval = queryInterval;
                            if (qqic != 0) {
                                qqi = decodeMaxRespAndQQIC((byte) qqic);
                            } else {
                                qqi = config.getInitialQueryInterval();
                            }
                            queryInterval = qqi;
                            if (queryInterval != oldQueryInterval) {
                                logger.log(Level.INFO, "Querier's Query Interval: " + queryInterval + " (code: 0x" + Integer.toHexString(qqic) + ").");
                            }
                        }
                    }
                    break;
                case IGMP.IGMP_V1_MEMBERSHIP_REPORT:
                    if (vif == null) break;
                    igmpVersion = 1;
                    groupSet = vif.getGroupSet();
                    if (groupSet.getGroup(groupAddress) == null) added = true;
                    group = groupSet.registerGroup(groupAddress);
                    group.registerHost(src, igmpVersion);
                    if (added) {
                        Set<Group> s = new HashSet<Group>();
                        s.add(group);
                        callback.included(s, vif);
                    }
                    logger.log(Level.INFO, "A v1 membership report received from " + src + " to " + groupAddress + ".");
                    break;
                case IGMP.IGMP_V2_MEMBERSHIP_REPORT:
                    if (vif == null) break;
                    igmpVersion = 2;
                    groupSet = vif.getGroupSet();
                    if (groupSet.getGroup(groupAddress) == null) added = true;
                    group = groupSet.registerGroup(groupAddress);
                    group.registerHost(src, igmpVersion);
                    if (added) {
                        Set<Group> s = new HashSet<Group>();
                        s.add(group);
                        callback.included(s, vif);
                    }
                    logger.log(Level.INFO, "A v2 membership report received from " + src + " to " + groupAddress + ".");
                    break;
                case IGMP.IGMP_V3_MEMBERSHIP_REPORT:
                    if (vif == null) break;
                    igmpVersion = 3;
                    groupSet = vif.getGroupSet();
                    if (data.length < 8) {
                        logger.log(Level.WARNING, "Size of an IGMPv3 membership report message is less than 8: " + data.length);
                        break;
                    }
                    {
                        int numOfGroupRecords = Utilities.byteArrayToInt(data, 6, 2);
                        Set<Inet4Address> sourceSet = new HashSet<Inet4Address>();
                        Set<Group> updatedGroupSet = new HashSet<Group>();
                        Set<Group> addedGroupSet = new HashSet<Group>();
                        logger.log(Level.INFO, "A v3 membership report received (# of group records: " + numOfGroupRecords + ") from " + src + " to " + groupAddress + ".");
                        int index = 8;
                        for (int i = 0; i < numOfGroupRecords; i++) {
                            sourceSet.clear();
                            int recordType = Utilities.byteArrayToInt(data, index, 1);
                            int auxDataLen = Utilities.byteArrayToInt(data, index + 1, 1);
                            int numOfSources = Utilities.byteArrayToInt(data, index + 2, 2);
                            int mcastAddressInt = Utilities.byteArrayToInt(data, index + 4, 4);
                            Inet4Address mcastAddress = Utilities.intToInet4Address(mcastAddressInt);
                            logger.log(Level.INFO, "A group record in a v3 membership report: record type: " + recordType + ", # of sources: " + numOfSources + ", mcast address: " + mcastAddress);
                            if (groupSet.getGroup(mcastAddress) == null) {
                                added = true;
                            }
                            group = groupSet.registerGroup(mcastAddress);
                            host = group.registerHost(src, 3);
                            updatedGroupSet.add(group);
                            if (added) {
                                addedGroupSet.add(group);
                            }
                            for (int j = 0; j < numOfSources; j++) {
                                int srcAddressInt = Utilities.byteArrayToInt(data, index + 8 + (8 * j), 4);
                                Inet4Address srcAddress = Utilities.intToInet4Address(srcAddressInt);
                                sourceSet.add(srcAddress);
                                logger.log(Level.INFO, "In a group record, src address: " + srcAddress);
                            }
                            switch(recordType) {
                                case 1:
                                case 3:
                                    host.setFilterMode(FilterMode.INCLUDE);
                                    host.setSourceSet(sourceSet);
                                    break;
                                case 2:
                                case 4:
                                    host.setFilterMode(FilterMode.EXCLUDE);
                                    host.setSourceSet(sourceSet);
                                    break;
                                case 5:
                                    if (host.getFilterMode() == FilterMode.EXCLUDE) {
                                        host.removeSourceSet(sourceSet);
                                    } else {
                                        host.addSourceSet(sourceSet);
                                    }
                                    break;
                                case 6:
                                    if (host.getFilterMode() == FilterMode.EXCLUDE) {
                                        host.addSourceSet(sourceSet);
                                    } else {
                                        host.removeSourceSet(sourceSet);
                                    }
                                    break;
                                default:
                                    logger.log(Level.INFO, "Invalid record type: " + recordType);
                            }
                            index += ((numOfSources + auxDataLen) * 4);
                        }
                        for (Group g : updatedGroupSet) {
                            g.updateFilter();
                        }
                        if (addedGroupSet.size() > 0) {
                            callback.included(addedGroupSet, vif);
                        }
                    }
                    break;
                case IGMP.IGMP_V2_LEAVE_GROUP:
                    if (vif == null) break;
                    groupSet = vif.getGroupSet();
                    Set<Group> removedGroupSet = groupSet.unregisterHost(groupAddress, src);
                    if (removedGroupSet.size() > 0) {
                        callback.excluded(removedGroupSet, vif);
                    }
                    logger.log(Level.INFO, "A v2 leave group message received.");
                    break;
                default:
                    logger.log(Level.INFO, "Invalid IGMP message type: " + type);
            }
        }
    }

    /**
	 * Decodes a value of the Querier's Query Interval Code (QQIC) field in IGMPv3 membership query message
	 * and returns Querier's Query Interval (QQI).
	 */
    private static int decodeMaxRespAndQQIC(byte qqic) {
        int qqi;
        if (qqic < 128) {
            qqi = qqic;
        } else {
            int mant = (qqic & 0xf) | 0x10;
            int exp = (qqic >>> 4) & 0x03;
            qqi = mant << (exp + 3);
        }
        return qqi;
    }

    /**
	 * Encodes Querier's Query Interval (QQI) to a value in Querier's Query Interval Code (QQIC).
	 */
    private static byte encodeMaxRespAndQQIC(int qqi) {
        byte qqic;
        if (qqi < 128) {
            qqic = (byte) qqi;
        } else {
            int exp = 3;
            int mant = qqi >>> 3;
            while (true) {
                if ((mant & 0xffffffe0) == 0) {
                    break;
                }
                mant >>>= 1;
                exp++;
            }
            exp = exp - 3;
            if (exp > 7) {
                exp = 7;
                mant = 0x0f;
            }
            qqic = (byte) (0x80 | (exp << 4) | (mant & 0x0f));
        }
        return qqic;
    }

    public void queryGroups() {
        int code;
        byte[] data = new byte[4];
        code = encodeMaxRespAndQQIC(config.getQueryResponseInterval());
        int qrv = this.robustnessVariable;
        if (qrv > 7) qrv = 7;
        data[0] |= qrv;
        data[1] = encodeMaxRespAndQQIC(this.queryInterval);
        for (VirtualInterface vif : this.vifSet) {
            if (vif.isRegisterVIF()) continue;
            int igmpVersion = vif.getGroupSet().getLowestIGMPVersion();
            boolean sent = false;
            switch(igmpVersion) {
                case 1:
                    this.igmp.send(vif.getLocalAddress(), IGMP.ALL_HOSTS_GROUP, IGMP.IGMP_MEMBERSHIP_QUERY, 0, null, null);
                    sent = true;
                    break;
                case 2:
                    this.igmp.send(vif.getLocalAddress(), IGMP.ALL_HOSTS_GROUP, IGMP.IGMP_MEMBERSHIP_QUERY, code, null, null);
                    sent = true;
                    break;
                case 3:
                    this.igmp.send(vif.getLocalAddress(), IGMP.ALL_HOSTS_GROUP, IGMP.IGMP_MEMBERSHIP_QUERY, code, null, data);
                    sent = true;
                    break;
                default:
                    logger.log(Level.WARNING, "Invalid IGMP version number in queryGroups():" + igmpVersion);
            }
            if (sent) {
                logger.log(Level.INFO, "A v" + igmpVersion + " membership query sent. ifname: " + vif.getName() + ", src addr: " + vif.getLocalAddress() + ".");
            }
        }
        this.lastQueryTime = Timer.currentTimeMillis();
    }

    public Set<VirtualInterface> getVirtualInterfaceSet() {
        return this.vifSet;
    }

    public void printStatus(PrintStream out) {
        for (VirtualInterface vif : this.vifSet) {
            out.println(vif);
        }
    }

    private class UpdatingDaemon implements Runnable {

        public void run() {
            try {
                while (true) {
                    Thread.sleep(config.getUpdateInterval());
                    synchronized (IGMPDaemon.this) {
                        while (suspended) {
                            IGMPDaemon.this.wait();
                        }
                    }
                    long now = Timer.currentTimeMillis();
                    for (VirtualInterface vif : vifSet) {
                        if (vif.isRegisterVIF()) continue;
                        long groupMambershipInterval = getGroupMembershipInterval();
                        GroupSet groupSet = vif.getGroupSet();
                        Set<Group> removedGroupSet = groupSet.expire(groupMambershipInterval);
                        if (removedGroupSet.size() > 0) {
                            callback.excluded(removedGroupSet, vif);
                        }
                        if (now > lastQueryTime + (queryInterval * 1000)) {
                            queryGroups();
                        }
                    }
                }
            } catch (InterruptedException e) {
                logger.log(Level.WARNING, "UpdatingDaemon interrupted and die.", e);
            }
        }
    }
}
