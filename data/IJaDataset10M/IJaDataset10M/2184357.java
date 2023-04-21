package com.entelience.probe.compliance;

import java.util.Date;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XNIException;
import com.entelience.probe.ProbeXmlParser;
import com.entelience.sql.DbHelper;
import com.entelience.probe.compliance.objects.PromisecSpectatorLogEntry;

public class PromisecSpectatorXmlParser extends ProbeXmlParser {

    private Date date;

    private PromisecSpectatorXmlImport imp;

    public static PromisecSpectatorXmlParser newParser() {
        return new PromisecSpectatorXmlParser();
    }

    public String getMainXmlTag() {
        return "Host";
    }

    protected String[] getIgnoredTags() {
        return null;
    }

    public String[] getKeyTags() {
        return null;
    }

    String hostName;

    String ipAddress;

    String os;

    String mac;

    String user;

    Integer assetId = null;

    private void getHostInfos(XMLAttributes attrs) {
        hostName = DbHelper.nullify(attrs.getValue("Name"));
        ipAddress = DbHelper.nullify(attrs.getValue("IpAddress"));
        os = DbHelper.nullify(attrs.getValue("OsVersion"));
        mac = DbHelper.nullify(attrs.getValue("MacAddress"));
        user = DbHelper.nullify(attrs.getValue("LastLoggedOnUser"));
        assetId = null;
    }

    private void fillIndication(XMLAttributes attrs) throws Exception {
        PromisecSpectatorLogEntry entry = new PromisecSpectatorLogEntry();
        entry.setDate(date);
        entry.setHostName(hostName);
        entry.setIp(ipAddress);
        entry.setDomainUser(user);
        entry.setOS(os);
        entry.setMacAddr(mac);
        entry.setEventName(DbHelper.nullify(attrs.getValue("ObjectName")));
        entry.setEventResult(DbHelper.nullify(attrs.getValue("Status")));
        entry.setEventDetail(DbHelper.nullify(attrs.getValue("Details")));
        if (assetId != null) entry.setAssetId(assetId);
        assetId = imp.importEntry(entry);
    }

    protected void startElt(QName element, XMLAttributes attrs, Augmentations augs) throws XNIException {
        try {
            if ("Host".equals(element.localpart)) {
                getHostInfos(attrs);
            }
            if ("Indication".equals(element.localpart)) {
                fillIndication(attrs);
            }
        } catch (Exception e) {
            XNIException ex = new XNIException(e.getMessage());
            ex.initCause(e);
            throw ex;
        }
    }

    public void endElement(QName element, Augmentations augs) throws XNIException {
    }

    protected void configure(PromisecSpectatorXmlImport promisecSpectatorXmlImport, Date d) {
        this.date = d;
        this.imp = promisecSpectatorXmlImport;
    }
}
