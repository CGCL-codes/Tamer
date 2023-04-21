package net.sf.ussrp.farm;

import net.sf.jasperreports.engine.JasperPrint;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Iterator;
import java.util.HashMap;
import java.net.URL;
import net.sf.ussrp.farm.JasperFarm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Thomas Ludwig Kast (tomkast@users.sourceforge.net)
 * @version $Id: UssrpJob.java,v 1.13 2006/01/22 06:29:49 tomkast Exp $
 */
public class UssrpJob {

    private static final int SLEEP = 500;

    public static final int BYTES = 1;

    public static final int JASPER_PRINT = 2;

    private int reportId;

    private String params;

    private byte[] bytes;

    private JasperPrint jasperPrint;

    private EngineUrl engineUrl;

    private int dataType;

    protected final Log logger = LogFactory.getLog(getClass());

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public JasperPrint getJasperPrint() {
        return jasperPrint;
    }

    public void setJasperPrint(JasperPrint jasperPrint) {
        this.jasperPrint = jasperPrint;
    }

    public EngineUrl getEngineUrl() {
        return engineUrl;
    }

    public void setEngineUrl(EngineUrl engineUrl) {
        this.engineUrl = engineUrl;
    }

    public void start() {
        URL javaUrl = null;
        ByteArrayOutputStream out = null;
        ObjectInputStream ins = null;
        InputStream in = null;
        try {
            boolean allNotAvailable = true;
            while (allNotAvailable) {
                synchronized (JasperFarm.engineUrls) {
                    for (Iterator i = JasperFarm.engineUrls.iterator(); i.hasNext(); ) {
                        setEngineUrl((EngineUrl) i.next());
                        if (getEngineUrl().isAvailable()) {
                            getEngineUrl().setAvailable(false);
                            allNotAvailable = false;
                            break;
                        }
                    }
                }
                if (allNotAvailable) Thread.sleep(SLEEP);
            }
            if (getDataType() == UssrpJob.JASPER_PRINT) {
                String stringUrl = getEngineUrl().getUrl() + "/engine.htm?dataType=" + UssrpJob.JASPER_PRINT + "&id=" + getReportId() + "&" + getParams();
                logger.info("start() about to get JASPER_PRINT from url " + stringUrl);
                javaUrl = new URL(stringUrl);
                ins = new ObjectInputStream(javaUrl.openStream());
                setJasperPrint((JasperPrint) ins.readObject());
                ins.close();
                getEngineUrl().setAvailable(true);
                logger.info("start() retrieved JASPER_PRINT, number of pages " + getJasperPrint().getPages().size());
            } else if (getDataType() == UssrpJob.BYTES) {
                String stringUrl = getEngineUrl().getUrl() + "/engine.htm?dataType=" + UssrpJob.BYTES + "&id=" + getReportId() + "&" + getParams();
                logger.info("start() about to get BYTES from url " + stringUrl);
                javaUrl = new URL(stringUrl);
                in = javaUrl.openStream();
                int i = 0;
                byte[] byteBuffer = new byte[1];
                out = new ByteArrayOutputStream();
                while ((i = in.read(byteBuffer)) > -1) {
                    out.write(byteBuffer, 0, byteBuffer.length);
                    byteBuffer = new byte[in.available()];
                }
                byte[] bytes = out.toByteArray();
                out.close();
                in.close();
                setBytes(bytes);
                getEngineUrl().setAvailable(true);
                logger.info("start() retrieved BYTES, number bytes " + bytes.length);
            } else {
                logger.error("start() no data type set");
                if (getEngineUrl() != null) getEngineUrl().setAvailable(true);
            }
        } catch (Exception e) {
            try {
                logger.error("start(), " + e.getMessage());
                e.printStackTrace();
                if (getEngineUrl() != null) getEngineUrl().setAvailable(true);
                if (ins != null) ins.close();
                if (in != null) in.close();
                if (out != null) out.close();
            } catch (Exception e2) {
                logger.error("start(), " + e2.getMessage());
                e.printStackTrace();
            }
        }
    }
}
