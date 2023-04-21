package edu.harvard.iq.safe.saasystem.auditschema;

import java.io.*;

/**
 *
 * @author Akio Sone
 */
public class Network implements Serializable {

    /**
     *
     */
    public Network() {
    }

    /**
     *
     * @param groupName
     * @param netAdminEmail
     */
    public Network(String groupName, String netAdminEmail) {
        this.groupName = groupName;
        this.netAdminEmail = netAdminEmail;
    }

    public Network(String groupName, String netAdminEmail, String geographicCoding) {
        this.groupName = groupName;
        this.netAdminEmail = netAdminEmail;
        this.geographicCoding = geographicCoding;
    }

    /**
     *
     */
    protected String groupName;

    /**
     *
     * @return
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     *
     * @param groupName
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    /**
     *
     */
    protected String netAdminEmail;

    /**
     *
     * @return
     */
    public String getNetAdminEmail() {
        return netAdminEmail;
    }

    /**
     *
     * @param netAdminEmail
     */
    public void setNetAdminEmail(String netAdminEmail) {
        this.netAdminEmail = netAdminEmail;
    }

    protected String geographicCoding;

    /**
     * Get the value of geographicCoding
     *
     * @return the value of geographicCoding
     */
    public String getGeographicCoding() {
        return geographicCoding;
    }

    /**
     * Set the value of geographicCoding
     *
     * @param geographicCoding new value of geographicCoding
     */
    public void setGeographicCoding(String geographicCoding) {
        this.geographicCoding = geographicCoding;
    }
}
