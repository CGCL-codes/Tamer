package ru.wdcb.esse.ogsadai.client.toolkit.activity.esse;

import java.text.*;
import java.util.*;
import org.apache.log4j.*;
import uk.org.ogsadai.client.toolkit.*;
import uk.org.ogsadai.client.toolkit.activity.*;
import uk.org.ogsadai.client.toolkit.wsi.*;
import java.io.*;
import uk.org.ogsadai.common.*;
import ru.wdcb.esapi.datamodel.*;
import java.util.zip.*;

/**
 * This activity delivers to a file on the container's filesystem from another activity.
 * <P>
 * It has one input and no output.
 *
 * @author The OGSA-DAI Project Team
 */
public class DataToXml extends Activity {

    private static Logger mLog = Logger.getLogger(DataToXml.class.getName());

    private StringBuffer params = new StringBuffer();

    /**
     * Constructs a request to deliver data to a file on the container's filesystem
     */
    public DataToXml() {
        addOutput("getXmlDataOutput");
    }

    public void setValue(String name, String value) {
        params.append("<value name=\"" + name + "\" value=\"" + value + "\"/>\n");
    }

    public void setRange(String name, String min, String max, String step) {
        params.append("<range name=\"" + name + "\" min=\"" + min + "\" max=\"" + max + "\" step=\"" + step + "\"/>\n");
    }

    public ActivityOutput getOutput() {
        return getOutputs()[0];
    }

    /**
     * @see uk.org.ogsadai.client.toolkit.activity.Activity#generateXML()
     */
    public String generateXML() {
        StringBuffer sb = new StringBuffer();
        sb.append("<getXmlData name=\"");
        sb.append(getName());
        sb.append("\">\n");
        sb.append(params);
        sb.append("<output name=\"");
        sb.append(getOutputs()[0].getName());
        sb.append("\"/>\n");
        sb.append("</getXmlData>");
        return sb.toString();
    }

    public static final void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: GetXML service_url");
            return;
        }
        SimpleDateFormat isoDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat isoDateFormat2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssz");
        isoDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        isoDateFormat2.setTimeZone(TimeZone.getTimeZone("GMT"));
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.set(1957, 00, 01, 0, 0, 0);
        String startDate = isoDateFormat.format(cal.getTime());
        cal.add(Calendar.MONTH, 1);
        String endDate = isoDateFormat.format(cal.getTime());
        ;
        ActivityRequest req = new ActivityRequest();
        DataToXml dataReq = new DataToXml();
        try {
        } catch (Exception ex) {
            System.out.println("\n\nError!" + dataReq.getOutput().toString());
            ex.printStackTrace();
        }
        req.clear();
        try {
            long time = System.currentTimeMillis();
            WSIDataService service = (WSIDataService) GenericServiceFetcher.getInstance().getDataService(args[0], "EsseResource");
            dataReq = new DataToXml();
            dataReq.setValue("source", "ncep25full");
            dataReq.setValue("name", "SkinTemperature");
            dataReq.setValue("verticalCoord", "Surface");
            dataReq.setRange("time", "2001-01-01T00:00:00UTC", "2001-01-02T23:59:59UTC", "");
            dataReq.setRange("lat", "0", "20", "1");
            dataReq.setRange("lon", "-180", "-150", "1");
            req.add(dataReq);
            Response resp = service.perform(req);
            File outFile = new File("d:/ncep_try.xml");
            FileOutputStream out = new FileOutputStream(outFile);
            out.write(dataReq.getOutput().getData().getBytes());
            out.close();
            long time2 = System.currentTimeMillis();
            System.out.println("Time: " + ((time2 - time) / 1000d));
            return;
        } catch (Exception ex) {
            System.out.println("\n\nError!" + dataReq.getOutput().toString());
            ex.printStackTrace();
        }
        req.clear();
        try {
            WSIDataService service = (WSIDataService) GenericServiceFetcher.getInstance().getDataService(args[0], "EsseResource");
            dataReq = new DataToXml();
            dataReq.setValue("source", "ncep25");
            dataReq.setValue("parameter", "air0.surface");
            dataReq.setRange("time", "2000-01-01", "2000-02-01", "21600");
            dataReq.setValue("point", "55 37.5");
            dataReq.setValue("level", "0");
            dataReq.setValue("seasonal", "");
            req.add(dataReq);
        } catch (Exception ex) {
            System.out.println("\n\nError!" + dataReq.getOutput().toString());
            ex.printStackTrace();
        }
        req.clear();
        try {
            WSIDataService service = (WSIDataService) GenericServiceFetcher.getInstance().getDataService(args[0], "EsseResource");
            dataReq = new DataToXml();
            dataReq.setValue("parameter", "bz.IMFMin");
            cal.set(1996, 05, 01, 0, 0, 0);
            startDate = isoDateFormat2.format(cal.getTime());
            cal.add(Calendar.DAY_OF_MONTH, 1);
            endDate = isoDateFormat2.format(cal.getTime());
            ;
            dataReq.setRange("time", startDate, endDate, "360");
            dataReq.setValue("station", "ALL");
            dataReq.setValue("source", "spidr_indices");
            req.add(dataReq);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        req.clear();
        try {
            WSIDataService service = (WSIDataService) GenericServiceFetcher.getInstance().getDataService(args[0], "EsseResource");
            dataReq = new DataToXml();
            dataReq.setValue("parameter", "hgt.pressure_level");
            dataReq.setValue("level", "10");
            dataReq.setRange("time", "1980-01-01T00:00:00UTC", "1980-01-01T23:59:59UTC", "");
            dataReq.setValue("polygon", "20 -20 20 20 -20 20 -20 -20");
            dataReq.setValue("source", "ncep25y");
            req.add(dataReq);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        req.clear();
        try {
            WSIDataService service = (WSIDataService) GenericServiceFetcher.getInstance().getDataService(args[0], "EsseResource");
            dataReq = new DataToXml();
            dataReq.setValue("name", "PrecipitationRate");
            dataReq.setValue("verticalCoord", "Surface");
            dataReq.setValue("source", "ncep25");
            cal.set(2005, 6, 1, 0, 0, 0);
            startDate = isoDateFormat2.format(cal.getTime());
            cal.add(Calendar.DAY_OF_MONTH, 12);
            endDate = isoDateFormat2.format(cal.getTime());
            ;
            dataReq.setRange("time", startDate, endDate, "360");
            dataReq.setValue("point", "55 38.5");
            req.add(dataReq);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        req.clear();
        try {
            WSIDataService service = (WSIDataService) GenericServiceFetcher.getInstance().getDataService(args[0], "NarrResource");
            dataReq = new DataToXml();
            dataReq.setRange("time", "1990-01-01T00:00:00UTC", "1990-03-25T23:59:59UTC", "");
            dataReq.setValue("parameter", "tmpsfc.surface");
            dataReq.setValue("source", "narr");
            dataReq.setRange("lat", "43", "44", "");
            dataReq.setRange("lon", "-130", "-128", "");
            req.add(dataReq);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        req.clear();
        try {
            WSIDataService service = (WSIDataService) GenericServiceFetcher.getInstance().getDataService(args[0], "EsseNetcdfResource");
            dataReq = new DataToXml();
            cal.set(2007, 0, 1, 0, 0, 0);
            startDate = isoDateFormat2.format(cal.getTime());
            cal.add(Calendar.HOUR, 21);
            endDate = isoDateFormat2.format(cal.getTime());
            ;
            dataReq.setRange("time", startDate, endDate, "10800");
            dataReq.setRange("lon", "-220", "-100", "");
            dataReq.setRange("lat", "0", "15.0", "");
            dataReq.setValue("parameter", "temp.surface");
            dataReq.setValue("source", "ncep25_dods@http://clust1.wdcb.ru/weather/Data/CRUTEM3v.nc");
            req.add(dataReq);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        req.clear();
        try {
            WSIDataService service = (WSIDataService) GenericServiceFetcher.getInstance().getDataService(args[0], "EsseClassResource");
            dataReq = new DataToXml();
            cal.set(2004, 02, 15, 0, 0, 0);
            startDate = isoDateFormat.format(cal.getTime());
            cal.add(Calendar.MONTH, 1);
            endDate = isoDateFormat.format(cal.getTime());
            ;
            dataReq.setRange("time", startDate, endDate, "0");
            dataReq.setRange("lat", "18.0", "18.0", "0");
            dataReq.setRange("lon", "308.0", "308.0", "0");
            dataReq.setValue("parameter", "ANAL_TEMP.surface");
            dataReq.setValue("source", "SST100");
            req.add(dataReq);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        req.clear();
        try {
            WSIDataService service = (WSIDataService) GenericServiceFetcher.getInstance().getDataService(args[0], "EsseResource");
            dataReq = new DataToXml();
            cal.set(2000, 02, 15, 0, 0, 0);
            startDate = isoDateFormat.format(cal.getTime());
            cal.add(Calendar.MONTH, 1);
            endDate = isoDateFormat.format(cal.getTime());
            ;
            dataReq.setRange("time", startDate, endDate, "0");
            dataReq.setValue("parameter", "iono_E.Iono");
            cal.set(1997, 05, 01, 0, 0, 0);
            startDate = isoDateFormat.format(cal.getTime());
            cal.add(Calendar.DAY_OF_MONTH, 3);
            endDate = isoDateFormat.format(cal.getTime());
            ;
            dataReq.setRange("time", startDate, endDate, "360");
            dataReq.setValue("station", "ALL");
            dataReq.setValue("source", "spidriono");
            req.add(dataReq);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
