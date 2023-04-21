package edu.pitt.dbmi.marx.mars.query;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class EMRDateRangeQuery extends EMRQuery {

    Date startDate;

    Date endDate;

    private static final String queryTemplate = "/templates/emrDateRangeQueryTemplate.xml";

    public static final SimpleDateFormat FORMAT_YYYYMMDD = new SimpleDateFormat("yyyyMMdd");

    public EMRDateRangeQuery(String reportType, Date startDate, Date endDate) {
        super(reportType);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public Document getQueryDocument() throws ParserConfigurationException, SAXException, IOException {
        Document document = super.getQueryDocument();
        Node requestNode = document.getFirstChild();
        Node dateLimitNode = null;
        NodeList paramNodes = requestNode.getChildNodes();
        for (int i = 0; i < paramNodes.getLength(); i++) {
            Node paramNode = paramNodes.item(i);
            if (paramNode.getNodeName().equalsIgnoreCase("DateLimit")) {
                dateLimitNode = paramNode;
                break;
            }
        }
        paramNodes = dateLimitNode.getChildNodes();
        Node startDateNode = null;
        Node endDateNode = null;
        for (int i = 0; i < paramNodes.getLength(); i++) {
            Node paramNode = paramNodes.item(i);
            if (paramNode.getNodeName().equalsIgnoreCase("StartDate")) {
                startDateNode = paramNode;
            } else if (paramNode.getNodeName().equalsIgnoreCase("EndDate")) {
                endDateNode = paramNode;
            }
        }
        startDateNode.setTextContent(FORMAT_YYYYMMDD.format(getStartDate()));
        endDateNode.setTextContent(FORMAT_YYYYMMDD.format(getEndDate()));
        return document;
    }

    protected String getQuery() {
        return "typ:" + getReportType();
    }

    @Override
    protected String getQueryTemplateFilename() {
        return queryTemplate;
    }
}
