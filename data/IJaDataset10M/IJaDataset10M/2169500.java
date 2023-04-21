package de.iritgo.aktera.license;

import de.iritgo.simplelife.math.NumberTools;
import org.apache.commons.logging.Log;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;

/**
 * @version $Id: LicenseInfo.java,v 1.22 2006/10/12 11:50:38 haardt Exp $
 */
public class LicenseInfo {

    public static final String MIN_VERSION = "1.5";

    public static final String UNLIMITED = "-1";

    public static final String TYPE_STANDARD = "standard";

    private Properties props;

    private String id;

    private String vendor;

    private String product;

    private String version;

    private String name;

    private String company;

    private String type;

    private String serial;

    private Date validUntil;

    private Integer users;

    private boolean valid;

    private String machineId;

    private String[] blackList = new String[] { "5c2fe8cd-1044791f229-7fff" };

    private Log log;

    public LicenseInfo(Log log) {
        this.log = log;
    }

    public LicenseInfo(Log log, Properties props) {
        this.log = log;
        this.props = props;
        id = props.getProperty("id");
        id = id != null ? id.trim() : "";
        vendor = props.getProperty("vendor");
        vendor = vendor != null ? vendor.trim() : "";
        product = props.getProperty("product");
        product = product != null ? product.trim() : "";
        version = props.getProperty("version");
        version = version != null ? version.trim() : "";
        type = props.getProperty("type");
        type = type != null ? type.trim() : "";
        serial = props.getProperty("serial");
        serial = serial != null ? serial.trim() : "";
        name = props.getProperty("name");
        name = name != null ? name.trim() : "";
        company = props.getProperty("company");
        company = company != null ? company.trim() : "";
        machineId = props.getProperty("machine");
        machineId = machineId != null ? machineId.trim() : "";
        if (UNLIMITED.equals(props.getProperty("validity"))) {
            validUntil = null;
        } else {
            try {
                validUntil = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, Locale.US).parse(props.getProperty("validity"));
            } catch (ParseException x) {
                validUntil = new Date(System.currentTimeMillis() + 5 * 60 * 1000);
            }
        }
        if (UNLIMITED.equals(props.getProperty("feature.user.count"))) {
            users = null;
        } else {
            try {
                users = new Integer(Integer.parseInt(props.getProperty("feature.user.count")));
            } catch (NumberFormatException x) {
                users = new Integer(5);
            }
        }
        checkLicense();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Date getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(Date validUntil) {
        this.validUntil = validUntil;
    }

    public Integer getUsers() {
        return users;
    }

    public int getUserCount() {
        return users.intValue();
    }

    public boolean hasUserLimit() {
        return users != null;
    }

    public void setUsers(Integer users) {
        this.users = users;
    }

    public boolean isValid() {
        return valid && (validUntil == null || System.currentTimeMillis() <= validUntil.getTime());
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    /**
	 * Check the license for validity.
	 */
    protected void checkLicense() {
        valid = false;
        if (!id.endsWith("License V2.0")) {
            log.error("Wrong license file version");
            return;
        }
        if (!TYPE_STANDARD.equals(type)) {
            log.error("Wrong license type");
            return;
        }
        boolean inBlackList = false;
        for (int i = 0; i < blackList.length; ++i) {
            if (blackList[i].equals(serial)) {
                inBlackList = true;
            }
        }
        if (inBlackList) {
            log.error("Serial number is blacklisted");
            return;
        }
        if (!verifyMachineId()) {
            log.error("Machine id doesn't match this machine");
            return;
        }
        String[] thisVersion = version.split("\\.");
        String[] minVersion = MIN_VERSION.split("\\.");
        if (thisVersion != null && thisVersion.length >= 2 && minVersion != null && minVersion.length >= 2) {
            int thisMajorVersion = NumberTools.toInt(thisVersion[0], 0);
            int thisMinorVersion = NumberTools.toInt(thisVersion[1], 0);
            int minMajorVersion = NumberTools.toInt(minVersion[0], Integer.MAX_VALUE);
            int minMinorVersion = NumberTools.toInt(minVersion[1], Integer.MAX_VALUE);
            if (thisMajorVersion < minMajorVersion || (thisMajorVersion == minMajorVersion && thisMinorVersion < minMinorVersion)) {
                log.error("Invalid application version (Required version: " + MIN_VERSION + " Current version: " + version + ")");
                return;
            }
        } else {
            log.error("Invalid application version specification (Required version: " + MIN_VERSION + " Current version: " + version + ")");
            return;
        }
        valid = true;
    }

    public boolean moduleAllowed(@SuppressWarnings("unused") String moduleName) {
        return true;
    }

    public boolean appAllowed(@SuppressWarnings("unused") String appName) {
        return true;
    }

    private boolean verifyMachineId() {
        String myMachineId = LicenseTools.machineInfo();
        if (myMachineId == null) {
            return false;
        }
        return myMachineId.equals(machineId);
    }

    public boolean hasFeature(String feature) {
        boolean has = false;
        try {
            has = new Boolean(Boolean.parseBoolean(props.getProperty("feature." + feature)));
        } catch (NumberFormatException x) {
        }
        return has;
    }

    public boolean hasFeatureLimit(String feature) {
        String sCount = props.getProperty("feature." + feature + ".count");
        return sCount != null && !"-1".equals(sCount);
    }

    public int featureCount(String feature) {
        String sCount = props.getProperty("feature." + feature + ".count");
        int count = 0;
        try {
            count = new Integer(Integer.parseInt(props.getProperty("feature." + feature + ".count")));
        } catch (NumberFormatException x) {
        }
        return count;
    }

    public Properties getLicenseInfoAsProperties() {
        return props;
    }
}
