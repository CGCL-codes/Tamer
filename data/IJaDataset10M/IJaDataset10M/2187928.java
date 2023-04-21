package de.frostcode.visualmon.web;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import javax.annotation.concurrent.Immutable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.commons.lang.StringUtils;
import org.jfree.util.Log;
import de.frostcode.visualmon.conf.StatusMonitorBuilder.Status;
import de.frostcode.visualmon.probe.ProbeData;

@XmlRootElement(name = "probes")
@XmlAccessorType(XmlAccessType.NONE)
@Immutable
final class ProbeDataList {

    private static final String VERSION, OS, VM;

    @XmlElement(name = "category")
    private final List<Category> data = new ArrayList<Category>();

    @XmlElement(name = "alert")
    private final List<Alert> alerts = new ArrayList<Alert>();

    @XmlElement(name = "log")
    private final List<LogEntry> logs;

    @XmlAttribute(name = "overall_status")
    private final String overallStatus;

    @XmlAttribute(name = "ips")
    private final String ips;

    @XmlAttribute(name = "empty_category_message")
    private final String emptyCategoryMessage;

    @XmlAttribute(name = "version")
    private final String version;

    @XmlAttribute(name = "os")
    private String os;

    @XmlAttribute(name = "vm")
    private String vm;

    @XmlAttribute(name = "title")
    private final String title;

    static {
        String resolvedVersion = ProbeDataList.class.getPackage().getImplementationVersion();
        if (StringUtils.isEmpty(resolvedVersion)) {
            JarFile jarfile;
            try {
                jarfile = new JarFile(ProbeDataList.class.getProtectionDomain().getCodeSource().getLocation().getFile());
                Manifest manifest = jarfile.getManifest();
                resolvedVersion = manifest.getMainAttributes().getValue("Implementation-Version");
            } catch (IOException e) {
                resolvedVersion = null;
            }
        }
        VERSION = resolvedVersion;
        OS = System.getProperty("os.name") + " (" + System.getProperty("os.arch") + ", " + System.getProperty("os.version") + ")";
        VM = System.getProperty("java.vm.name") + ", " + System.getProperty("java.vm.version") + " (" + System.getProperty("java.version") + ")";
    }

    protected ProbeDataList() {
        overallStatus = null;
        logs = null;
        ips = null;
        emptyCategoryMessage = null;
        version = null;
        title = null;
    }

    public ProbeDataList(final Map<String, Collection<ProbeData>> probeData, final EnumMap<Status, Collection<String>> alerts, final List<LogEntry> logs, final String emptyCategoryMessage, final String title) {
        if (null == probeData) throw new IllegalArgumentException("probeData null");
        this.emptyCategoryMessage = emptyCategoryMessage;
        ips = StringUtils.join(resolveIps(), ", ");
        version = VERSION;
        os = OS;
        vm = VM;
        this.title = title;
        for (Map.Entry<String, Collection<ProbeData>> entry : probeData.entrySet()) data.add(new Category(entry.getKey(), entry.getValue()));
        Status maxStatus = Status.OK;
        for (Entry<Status, Collection<String>> status : alerts.entrySet()) {
            for (String msg : status.getValue()) this.alerts.add(new Alert(status.getKey().name(), msg));
            if (maxStatus.ordinal() < status.getKey().ordinal()) maxStatus = status.getKey();
        }
        this.logs = logs;
        overallStatus = maxStatus.name();
    }

    public List<Category> getData() {
        return data;
    }

    public String getOverallStatus() {
        return overallStatus;
    }

    public List<LogEntry> getLogs() {
        return logs;
    }

    public String getIps() {
        return ips;
    }

    public String getEmptyCategoryMessage() {
        return emptyCategoryMessage;
    }

    public String getVersion() {
        return version;
    }

    public String getOs() {
        return os;
    }

    public String getVm() {
        return vm;
    }

    public String getTitle() {
        return title;
    }

    private List<String> resolveIps() {
        List<String> resultIps = new ArrayList<String>();
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            if (localHost.isLoopbackAddress()) {
                Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces();
                while (ifaces.hasMoreElements()) {
                    NetworkInterface iface = ifaces.nextElement();
                    if (!iface.isLoopback()) {
                        for (Enumeration<InetAddress> adrs = iface.getInetAddresses(); adrs.hasMoreElements(); ) {
                            InetAddress addr = adrs.nextElement();
                            if (!addr.isLoopbackAddress() && !addr.isLinkLocalAddress() && !addr.isMulticastAddress() && !addr.isAnyLocalAddress()) resultIps.add(addr.getHostAddress());
                        }
                    }
                }
            } else resultIps.add(localHost.getHostAddress());
        } catch (UnknownHostException e) {
            Log.error("Cannot get local IP", e);
            return resultIps;
        } catch (SocketException e) {
            Log.error("Cannot get local IP", e);
            return resultIps;
        }
        return resultIps;
    }

    @XmlRootElement(name = "category")
    @XmlAccessorType(XmlAccessType.NONE)
    private static final class Category {

        @XmlAttribute
        private final String title;

        @XmlElement
        private final Collection<ProbeData> data;

        protected Category() {
            title = null;
            data = null;
        }

        public Category(final String title, final Collection<ProbeData> data) {
            this.title = title;
            this.data = data;
        }

        public String getTitle() {
            return title;
        }

        public Collection<ProbeData> getData() {
            return data;
        }
    }

    @XmlRootElement(name = "alert")
    @XmlAccessorType(XmlAccessType.NONE)
    private static final class Alert {

        @XmlAttribute
        private final String status;

        @XmlAttribute
        private final String message;

        protected Alert() {
            status = null;
            message = null;
        }

        public Alert(final String status, final String message) {
            this.status = status;
            this.message = message;
        }

        public String getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }
    }
}
