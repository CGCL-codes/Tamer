package org.slasoi.businessManager.postSale.reporting.impl.mapping;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.slasoi.businessManager.postSale.reporting.impl.types.ReportType;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * @author Beatriz Fuentes (TID) This class encapsulates the format of the report: title, groups of data, params, etc
 */
public class Report {

    private static Logger logger = Logger.getLogger(Report.class.getName());

    String title = null;

    String author = null;

    ReportType type = null;

    List<Group> groups = new ArrayList<Group>();

    public Report() {
    }

    public Report(ReportType type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public static Report fromXML(String reportXml) {
        logger.info("Report::fromXML");
        logger.info(reportXml);
        XStream xstream = new XStream(new DomDriver());
        xstream.setClassLoader(Report.class.getClassLoader());
        xstream.alias(org.slasoi.businessManager.postSale.reporting.impl.mapping.Report.class.getSimpleName(), org.slasoi.businessManager.postSale.reporting.impl.mapping.Report.class);
        xstream.alias(org.slasoi.businessManager.postSale.reporting.impl.mapping.Group.class.getSimpleName(), org.slasoi.businessManager.postSale.reporting.impl.mapping.Group.class);
        Report report = new Report();
        try {
            report = (Report) xstream.fromXML(reportXml);
            logger.info(report.toString());
        } catch (Exception e) {
            logger.info("Unable to deserialised Report. \n");
            logger.info("Reason: " + e.getMessage());
            e.printStackTrace();
        }
        return report;
    }

    public static Report fromXMLFile(String filename) {
        return (fromXML(readFile(filename)));
    }

    public String toXML() {
        logger.info("Report to XML");
        logger.info(this);
        XStream xstream = new XStream();
        xstream.alias(org.slasoi.businessManager.postSale.reporting.impl.mapping.Report.class.getSimpleName(), org.slasoi.businessManager.postSale.reporting.impl.mapping.Report.class);
        xstream.alias(org.slasoi.businessManager.postSale.reporting.impl.mapping.Group.class.getSimpleName(), org.slasoi.businessManager.postSale.reporting.impl.mapping.Group.class);
        String reportStr = xstream.toXML(this);
        logger.info(reportStr);
        return (reportStr);
    }

    public static String readFile(String fileName) {
        String content = null;
        try {
            FileInputStream file = new FileInputStream(fileName);
            byte[] b = new byte[file.available()];
            file.read(b);
            file.close();
            content = new String(b);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }

    public String toString() {
        String aux = "Report " + title + "\n";
        aux = aux + "List of groups:\n";
        for (Group gr : groups) aux = aux + gr.toString() + "\n";
        return aux;
    }

    public ReportType getType() {
        return type;
    }

    public void setType(ReportType type) {
        this.type = type;
    }
}
