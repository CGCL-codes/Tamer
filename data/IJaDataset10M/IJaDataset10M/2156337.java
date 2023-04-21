package com.jvantage.ce.facilities.system;

import com.jvantage.sql.SQLConstants;
import com.jvantage.sql.SQLUtil;
import java.util.SortedSet;
import java.util.TreeSet;
import org.apache.commons.lang.*;

/**
 *
 * @author  Administrator
 */
public class LicenseInfo implements java.io.Serializable {

    public static final String LICENSOR = "Licensor";

    public static final String CUSTOMER_NAME = "CustName";

    public static final String CUSTOMER_NO = "CustNo";

    public static final String SERIAL_NO = "SerialNo";

    public static final String IP_ADDRESS = "IPAddress";

    public static final String FEATURE = "Feature";

    public static final String LICENSE_TYPE = "LicenseType";

    public static final String EXPIRE_DAYS = "Expires";

    public static final String PRODUCT = "Product";

    public static final String JPA_GENERATOR = "JpaGen";

    public static final String DATABASE = "Database";

    private String customerName = null;

    private String customerNo = null;

    private int expires = 0;

    private String feature = null;

    private String ipAddress = null;

    private boolean isValidDeterminationHasBeenMade = false;

    private boolean licenseIsValid = false;

    private LicenseStatusEnum licenseStatus = null;

    private String licenseType = null;

    private String licensor = null;

    private String product = null;

    private String serialNo = null;

    private boolean jpaGenerator = false;

    private String databases = null;

    /** Creates a new instance of LicenseInfo */
    public LicenseInfo() {
    }

    /**
     * Getter for property customerName.
     * @return Value of property customerName.
     */
    public java.lang.String getCustomerName() {
        return customerName;
    }

    /**
     * Getter for property customerNo.
     * @return Value of property customerNo.
     */
    public java.lang.String getCustomerNo() {
        return customerNo;
    }

    /**
     * Getter for property expires.
     * @return Value of property expires.
     */
    public int getExpires() {
        return expires;
    }

    /**
     * Getter for property feature.
     * @return Value of property feature.
     */
    public java.lang.String getFeature() {
        return feature;
    }

    /**
     * Getter for property ipAddress.
     * @return Value of property ipAddress.
     */
    public java.lang.String getIpAddress() {
        return ipAddress;
    }

    public boolean getLicenseIsValid() {
        if (isValidDeterminationHasBeenMade == false) {
            isValidDeterminationHasBeenMade = true;
            LicenseStatusEnum ls = getLicenseStatus();
            if (ls == null) {
                licenseIsValid = false;
            } else if (ls.getName().equalsIgnoreCase(LicenseStatusEnum.Invalid.getName())) {
                licenseIsValid = false;
            } else {
                licenseIsValid = true;
            }
        }
        return licenseIsValid;
    }

    /**
     * Getter for property licenseStatus.
     * @return Value of property licenseStatus.
     */
    public LicenseStatusEnum getLicenseStatus() {
        return licenseStatus;
    }

    /**
     * Getter for property licenseType.
     * @return Value of property licenseType.
     */
    public java.lang.String getLicenseType() {
        return licenseType;
    }

    /**
     * Getter for property licensor.
     * @return Value of property licensor.
     */
    public java.lang.String getLicensor() {
        return licensor;
    }

    /**
     * Getter for property product.
     * @return Value of property product.
     */
    public java.lang.String getProduct() {
        return product;
    }

    /**
     * Getter for property serialNo.
     * @return Value of property serialNo.
     */
    public java.lang.String getSerialNo() {
        return serialNo;
    }

    /**
     * Setter for property customerName.
     * @param customerName New value of property customerName.
     */
    public void setCustomerName(java.lang.String customerName) {
        this.customerName = customerName;
    }

    /**
     * Setter for property customerNo.
     * @param customerNo New value of property customerNo.
     */
    public void setCustomerNo(java.lang.String customerNo) {
        this.customerNo = customerNo;
    }

    /**
     * Setter for property expires.
     * @param expires New value of property expires.
     */
    public void setExpires(int expires) {
        this.expires = expires;
    }

    /**
     * Setter for property feature.
     * @param feature New value of property feature.
     */
    public void setFeature(java.lang.String feature) {
        this.feature = feature;
    }

    /**
     * Setter for property ipAddress.
     * @param ipAddress New value of property ipAddress.
     */
    public void setIpAddress(java.lang.String ipAddress) {
        this.ipAddress = ipAddress;
    }

    /**
     * Setter for property licenseStatus.
     * @param licenseStatus New value of property licenseStatus.
     */
    public void setLicenseStatus(LicenseStatusEnum licenseStatus) {
        this.licenseStatus = licenseStatus;
    }

    /**
     * Setter for property licenseType.
     * @param licenseType New value of property licenseType.
     */
    public void setLicenseType(java.lang.String licenseType) {
        this.licenseType = licenseType;
    }

    /**
     * Setter for property licensor.
     * @param licensor New value of property licensor.
     */
    public void setLicensor(java.lang.String licensor) {
        this.licensor = licensor;
    }

    /**
     * Setter for property product.
     * @param product New value of property product.
     */
    public void setProduct(java.lang.String product) {
        this.product = product;
    }

    public boolean isOracleEnabled() {
        return isDatabaseEnabled(SQLConstants.DATABASE_PRODUCT_NAME_ORACLE);
    }

    public boolean isDerbyEnabled() {
        return isDatabaseEnabled(SQLConstants.DATABASE_PRODUCT_NAME_DERBY);
    }

    public boolean isMySQLEnabled() {
        return isDatabaseEnabled(SQLConstants.DATABASE_PRODUCT_NAME_MYSQL);
    }

    public boolean isDB2Enabled() {
        return isDatabaseEnabled(SQLConstants.DATABASE_PRODUCT_NAME_DB2);
    }

    public boolean isPostgreSQLEnabled() {
        return isDatabaseEnabled(SQLConstants.DATABASE_PRODUCT_NAME_POSTGRESQL);
    }

    public boolean isDatabaseEnabled(String databaseName) {
        if (StringUtils.isBlank(databaseName)) {
            return false;
        }
        if (SQLUtil.isSupportedDatabase(databaseName) == false) {
            return false;
        }
        if (databaseFeatureIsDefined() == false) {
            return false;
        }
        String databaseFeature = getDatabases().toLowerCase();
        return databaseFeature.contains(databaseName.toLowerCase());
    }

    public SortedSet<String> getEnabledDatabases() {
        TreeSet<String> databaseSet = new TreeSet<String>();
        if (isOracleEnabled()) {
            databaseSet.add(SQLConstants.DATABASE_PRODUCT_NAME_ORACLE);
        }
        if (isDB2Enabled()) {
            databaseSet.add(SQLConstants.DATABASE_PRODUCT_NAME_DB2);
        }
        if (isDerbyEnabled()) {
            databaseSet.add(SQLConstants.DATABASE_PRODUCT_NAME_DERBY);
        }
        if (isMySQLEnabled()) {
            databaseSet.add(SQLConstants.DATABASE_PRODUCT_NAME_MYSQL);
        }
        if (isPostgreSQLEnabled()) {
            databaseSet.add(SQLConstants.DATABASE_PRODUCT_NAME_POSTGRESQL);
        }
        return databaseSet;
    }

    /**
     * @return boolean - true is this license contains database features.
     */
    public boolean databaseFeatureIsDefined() {
        return StringUtils.isNotBlank(getDatabases());
    }

    /**
     * Setter for property serialNo.
     * @param serialNo New value of property serialNo.
     */
    public void setSerialNo(java.lang.String serialNo) {
        this.serialNo = serialNo;
    }

    public String toString() {
        final int leftPadLabel = 14;
        StringBuffer msg = new StringBuffer();
        msg.append("License Information").append(SystemUtils.LINE_SEPARATOR);
        if (StringUtils.isNotBlank(getLicensor())) {
            msg.append(StringUtils.leftPad(LICENSOR, leftPadLabel) + " [" + getLicensor() + "]").append(SystemUtils.LINE_SEPARATOR);
        }
        if (StringUtils.isNotBlank(getLicenseType())) {
            msg.append(StringUtils.leftPad(LICENSE_TYPE, leftPadLabel) + " [" + getLicenseType() + "]").append(SystemUtils.LINE_SEPARATOR);
        }
        if (StringUtils.isNotBlank(getProduct())) {
            msg.append(StringUtils.leftPad(PRODUCT, leftPadLabel) + " [" + getProduct() + "]").append(SystemUtils.LINE_SEPARATOR);
        }
        if (StringUtils.isNotBlank(getCustomerName())) {
            msg.append(StringUtils.leftPad(CUSTOMER_NAME, leftPadLabel) + " [" + getCustomerName() + "]").append(SystemUtils.LINE_SEPARATOR);
        }
        if (StringUtils.isNotBlank(getCustomerNo())) {
            msg.append(StringUtils.leftPad(CUSTOMER_NO, leftPadLabel) + " [" + getCustomerNo() + "]").append(SystemUtils.LINE_SEPARATOR);
        }
        if (StringUtils.isNotBlank(getSerialNo())) {
            msg.append(StringUtils.leftPad(SERIAL_NO, leftPadLabel) + " [" + getSerialNo() + "]").append(SystemUtils.LINE_SEPARATOR);
        }
        if (StringUtils.isNotBlank(getIpAddress())) {
            msg.append(StringUtils.leftPad(IP_ADDRESS, leftPadLabel) + " [" + getIpAddress() + "]").append(SystemUtils.LINE_SEPARATOR);
        }
        if (StringUtils.isNotBlank(getFeature())) {
            msg.append(StringUtils.leftPad(FEATURE, leftPadLabel) + " [" + getFeature() + "]").append(SystemUtils.LINE_SEPARATOR);
        }
        if (StringUtils.isNotBlank(getDatabases())) {
            msg.append(StringUtils.leftPad(DATABASE, leftPadLabel) + " [" + getDatabases() + "]").append(SystemUtils.LINE_SEPARATOR);
        }
        msg.append(StringUtils.leftPad(JPA_GENERATOR, leftPadLabel) + " [" + isJpaGenerator() + "]").append(SystemUtils.LINE_SEPARATOR);
        if (getExpires() > 0) {
            msg.append(StringUtils.leftPad(EXPIRE_DAYS, leftPadLabel) + " [" + getExpires() + "]").append(SystemUtils.LINE_SEPARATOR);
        }
        if (getLicenseStatus() == null) {
            msg.append(StringUtils.leftPad("LicenseStatus", leftPadLabel) + " [Unknown]");
        } else {
            msg.append(StringUtils.leftPad("LicenseStatus", leftPadLabel) + " [" + getLicenseStatus().getName() + "]");
        }
        return msg.toString();
    }

    public boolean isJpaGenerator() {
        return jpaGenerator;
    }

    public void setJpaGenerator(boolean jpaGenerator) {
        this.jpaGenerator = jpaGenerator;
    }

    public String getDatabases() {
        return databases;
    }

    public void setDatabases(String databases) {
        this.databases = databases;
    }
}
