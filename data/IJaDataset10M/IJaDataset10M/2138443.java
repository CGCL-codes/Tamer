package com.kni.etl.ketl.writer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import org.w3c.dom.Node;
import com.kni.etl.dbutils.ResourcePool;
import com.kni.etl.ketl.ETLInPort;
import com.kni.etl.ketl.exceptions.KETLThreadException;
import com.kni.etl.ketl.exceptions.KETLWriteException;
import com.kni.etl.ketl.smp.DefaultWriterCore;
import com.kni.etl.ketl.smp.ETLThreadManager;
import com.kni.etl.util.XMLHelper;

/**
 * <p>
 * Title: ETLWriter
 * </p>
 * <p>
 * Description: Abstract base class for ETL destination loading.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: Kinetic Networks
 * </p>
 * 
 * @author Brian Sullivan
 * @version 0.1
 */
public class ExcelWriter extends ETLWriter implements DefaultWriterCore {

    @Override
    protected String getVersion() {
        return "$LastChangedRevision: 491 $";
    }

    /** The tab port index. */
    private int mTabPortIndex = -1;

    /** The xml out. */
    private BufferedWriter xmlOut;

    /** The out. */
    private PrintWriter out;

    @Override
    protected int initialize(Node xmlConfig) throws KETLThreadException {
        int res = super.initialize(xmlConfig);
        if (res != 0) return res;
        for (int i = 0; i < this.mInPorts.length; i++) {
            if (XMLHelper.getAttributeAsBoolean(this.mInPorts[i].getXMLConfig().getAttributes(), "TAB", false)) {
                this.mTabPortIndex = i;
            }
        }
        String filePath = this.getParameterValue(0, "FILEPATH");
        File fn;
        try {
            fn = new File(filePath);
            this.out = new PrintWriter(fn);
            this.xmlOut = new BufferedWriter(this.out);
            this.writeData("<?xml version=\"1.0\"?>\n" + "<?mso-application progid=\"Excel.Sheet\"?>" + "<ss:Workbook xmlns=\"urn:schemas-microsoft-com:office:spreadsheet\" " + "xmlns:o=\"urn:schemas-microsoft-com:office:office\" " + "xmlns:x=\"urn:schemas-microsoft-com:office:excel\"  " + "xmlns:ss=\"urn:schemas-microsoft-com:office:spreadsheet\"  " + "xmlns:html=\"http://www.w3.org/TR/REC-html40\">");
            this.writeData("<ss:Styles><Style ss:ID=\"Default\" ss:Name=\"Normal\"><Font ss:Size=\"8\"/>" + "</Style><ss:Style ss:ID=\"1\"><Borders><Border ss:Position=\"Bottom\"" + " ss:LineStyle=\"Continuous\" ss:Weight=\"1\"/></Borders></ss:Style>" + "<Style ss:ID=\"s22\"><NumberFormat" + " ss:Format=\"m/d/yy\\ h:mm;@\"/>" + "</Style>\n</ss:Styles>");
            if (this.mTabPortIndex == -1) this.createSheet("Results");
        } catch (IOException e) {
            throw new KETLThreadException(e, this);
        }
        return 0;
    }

    /**
	 * Instantiates a new excel writer.
	 * 
	 * @param pXMLConfig
	 *            the XML config
	 * @param pPartitionID
	 *            the partition ID
	 * @param pPartition
	 *            the partition
	 * @param pThreadManager
	 *            the thread manager
	 * 
	 * @throws KETLThreadException
	 *             the KETL thread exception
	 */
    public ExcelWriter(Node pXMLConfig, int pPartitionID, int pPartition, ETLThreadManager pThreadManager) throws KETLThreadException {
        super(pXMLConfig, pPartitionID, pPartition, pThreadManager);
        if (pPartition > 1) throw new KETLThreadException("Excel writer cannot be executed in parallel, add FLOWTYPE=\"FANIN\" to step definition", this);
    }

    /** The current tab. */
    private Object mCurrentTab = null;

    /**
	 * Adds the headers.
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
    private void addHeaders() throws IOException {
        this.writeData("<ss:Row ss:StyleID=\"1\">");
        for (ETLInPort element : this.mInPorts) {
            this.printCell(element.mstrName, String.class);
        }
        this.writeData("</ss:Row>");
    }

    public int putNextRecord(Object[] o, Class[] pExpectedDataTypes, int pRecordWidth) throws KETLWriteException {
        try {
            if (this.mTabPortIndex != -1) {
                Object data = this.mInPorts[this.mTabPortIndex].isConstant() ? this.mInPorts[this.mTabPortIndex].getConstantValue() : o[this.mInPorts[this.mTabPortIndex].getSourcePortIndex()];
                if (this.mCurrentTab == null || this.mCurrentTab.equals(data) == false) {
                    if (this.mCurrentTab != null) {
                        this.closeSheet();
                    }
                    this.mCurrentTab = data;
                    this.createSheet(data.toString());
                }
            }
            this.writeData("<ss:Row>");
            for (ETLInPort element : this.mInPorts) {
                Object data = element.isConstant() ? element.getConstantValue() : o[element.getSourcePortIndex()];
                this.printCell(data, element.getPortClass());
            }
            this.writeData("</ss:Row>");
        } catch (IOException e) {
            throw new KETLWriteException(e.getMessage());
        }
        return 1;
    }

    @Override
    protected void close(boolean success, boolean jobFailed) {
        try {
            if (this.xmlOut != null) {
                this.xmlOut.close();
                this.xmlOut = null;
            }
            if (this.out != null) {
                this.out = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
	 * Write data.
	 * 
	 * @param pData
	 *            the data
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
    void writeData(String pData) throws IOException {
        this.xmlOut.write(pData);
    }

    /**
	 * Creates the sheet.
	 * 
	 * @param pName
	 *            the name
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
    void createSheet(String pName) throws IOException {
        this.writeData("<ss:Worksheet ss:Name=\"" + this.checkAndEscapeXMLData(pName) + "\"><ss:Table>");
        this.addHeaders();
    }

    /** The custom date format. */
    SimpleDateFormat customDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

    /**
	 * Prints the cell.
	 * 
	 * @param pValue
	 *            the value
	 * @param pClass
	 *            the class
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
    void printCell(Object pValue, Class pClass) throws IOException {
        if (pValue == null) this.writeData("<ss:Cell><ss:Data ss:Type=\"String\">"); else if (pClass == String.class) {
            this.writeData("<ss:Cell><ss:Data ss:Type=\"String\">");
            this.writeData(this.checkAndEscapeXMLData((String) pValue));
        } else if (pClass == BigDecimal.class) {
            this.writeData("<ss:Cell><ss:Data ss:Type=\"Number\">");
            this.writeData(this.checkAndEscapeXMLData(Double.toString(((BigDecimal) pValue).doubleValue())));
        } else if (pClass == Timestamp.class) {
            this.writeData("<ss:Cell ss:StyleID=\"s22\"><ss:Data ss:Type=\"DateTime\">");
            this.writeData(this.checkAndEscapeXMLData(this.customDateFormat.format((Timestamp) pValue)));
        } else {
            this.writeData("<ss:Cell><ss:Data ss:Type=\"String\">");
            this.writeData(this.checkAndEscapeXMLData(pValue.toString()));
        }
        this.writeData("</ss:Data></ss:Cell>");
    }

    /**
	 * Pad null.
	 * 
	 * @param arg0
	 *            the arg0
	 * 
	 * @return the string
	 */
    public static String padNull(String arg0) {
        if (arg0 == null || arg0.length() == 1) return "&nbsp;";
        return arg0;
    }

    /**
	 * Close sheet.
	 * 
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
    void closeSheet() throws IOException {
        this.writeData("</ss:Table></ss:Worksheet>");
    }

    /** The data length warning. */
    private boolean mDataLengthWarning = true;

    /**
	 * Check and escape XML data.
	 * 
	 * @param pXML
	 *            the XML
	 * 
	 * @return the string
	 */
    private String checkAndEscapeXMLData(String pXML) {
        if (pXML.length() > 255) {
            pXML = pXML.substring(0, 254);
            if (this.mDataLengthWarning) {
                ResourcePool.LogMessage(this, ResourcePool.ERROR_MESSAGE, "Data length over 255, actual length " + pXML.length());
                this.mDataLengthWarning = false;
            }
        }
        String str = pXML.replaceAll("&", "&amp;");
        str = pXML.replaceAll("\"", "&quot;");
        str = str.replaceAll("<", "&lt;");
        str = str.replaceAll(">", "&gt;");
        str = str.replaceAll("�", "&apos;");
        return str;
    }

    @Override
    public int complete() throws KETLThreadException {
        int res = super.complete();
        if (res != 0) return res;
        try {
            this.closeSheet();
            this.writeData("</ss:Workbook>");
            if (this.xmlOut != null) {
                this.xmlOut.close();
                this.xmlOut = null;
            }
            if (this.out != null) {
                this.out.flush();
                this.out = null;
            }
        } catch (IOException e) {
            throw new KETLThreadException(e, e.getMessage());
        }
        return 0;
    }
}
