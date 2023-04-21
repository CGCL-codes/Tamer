package gov.sns.xal.tools.simulationmanager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class TwissFastParser {

    static List<TwissFastData> parse(String xmlFile) throws Exception {
        List<TwissFastData> list = new ArrayList<TwissFastData>();
        DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = dbfactory.newDocumentBuilder();
        Document doc = builder.parse(new File(xmlFile));
        Element root = doc.getDocumentElement();
        NodeList childList = root.getElementsByTagName("table");
        Element table = (Element) childList.item(0);
        String tableName = table.getAttribute("name");
        if (!tableName.equals("twiss")) {
            throw new RuntimeException("Failed in getting <table> in file = " + xmlFile);
        }
        NodeList recordList = table.getElementsByTagName("record");
        for (int irec = 0; irec < recordList.getLength(); irec++) {
            Element record = (Element) recordList.item(irec);
            String id = record.getAttribute("id");
            double x = Double.parseDouble(record.getAttribute("x"));
            double y = Double.parseDouble(record.getAttribute("y"));
            double z = Double.parseDouble(record.getAttribute("z"));
            double xp = Double.parseDouble(record.getAttribute("xp"));
            double yp = Double.parseDouble(record.getAttribute("yp"));
            double zp = Double.parseDouble(record.getAttribute("zp"));
            double ax = Double.parseDouble(record.getAttribute("ax"));
            double ay = Double.parseDouble(record.getAttribute("ay"));
            double az = Double.parseDouble(record.getAttribute("az"));
            double bx = Double.parseDouble(record.getAttribute("bx"));
            double by = Double.parseDouble(record.getAttribute("by"));
            double bz = Double.parseDouble(record.getAttribute("bz"));
            double ex = Double.parseDouble(record.getAttribute("ex"));
            double ey = Double.parseDouble(record.getAttribute("ey"));
            double ez = Double.parseDouble(record.getAttribute("ez"));
            double mux = Double.parseDouble(record.getAttribute("mux"));
            double muy = Double.parseDouble(record.getAttribute("muy"));
            double muz = Double.parseDouble(record.getAttribute("muz"));
            double sx = Double.parseDouble(record.getAttribute("sx"));
            double sy = Double.parseDouble(record.getAttribute("sy"));
            double sz = Double.parseDouble(record.getAttribute("sz"));
            double etx = Double.parseDouble(record.getAttribute("etx"));
            double ety = Double.parseDouble(record.getAttribute("ety"));
            double etpx = Double.parseDouble(record.getAttribute("etpx"));
            double etpy = Double.parseDouble(record.getAttribute("etpy"));
            double w = 0;
            String sw = record.getAttribute("w");
            if ((sw != null) && (!sw.equals(""))) {
                w = Double.parseDouble(sw);
            }
            double s = Double.parseDouble(record.getAttribute("z"));
            String shape = record.getAttribute("shape");
            if (shape == null) {
                shape = "";
            }
            double apx = 0;
            String sapx = record.getAttribute("apx");
            if ((sapx != null) && (!sapx.equals(""))) {
                apx = Double.parseDouble(sapx);
            }
            double apy = 0;
            String sapy = record.getAttribute("apy");
            if ((sapy != null) && (!sapy.equals(""))) {
                apy = Double.parseDouble(sapx);
            }
            list.add(new TwissFastData(id, x, y, z, xp, yp, zp, ax, ay, az, bx, by, bz, ex, ey, ez, mux, muy, muz, sx, sy, sz, etx, ety, etpx, etpy, w, s, shape, apx, apy));
        }
        return list;
    }

    static TwissFastLists parseLists(String xmlFile) throws Exception {
        TwissFastLists lists = new TwissFastLists();
        DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = dbfactory.newDocumentBuilder();
        Document doc = builder.parse(new File(xmlFile));
        Element root = doc.getDocumentElement();
        NodeList childList = root.getElementsByTagName("table");
        Element table = (Element) childList.item(0);
        String tableName = table.getAttribute("name");
        if (!tableName.equals("twiss")) {
            throw new RuntimeException("Failed in getting <table> in file, tableName = " + xmlFile + " '" + tableName + "'");
        }
        NodeList recordList = table.getElementsByTagName("record");
        for (int irec = 0; irec < recordList.getLength(); irec++) {
            Element record = (Element) recordList.item(irec);
            String id = record.getAttribute("id");
            double x = Double.parseDouble(record.getAttribute("x"));
            double y = Double.parseDouble(record.getAttribute("y"));
            double z = Double.parseDouble(record.getAttribute("z"));
            double xp = Double.parseDouble(record.getAttribute("xp"));
            double yp = Double.parseDouble(record.getAttribute("yp"));
            double zp = Double.parseDouble(record.getAttribute("zp"));
            double ax = Double.parseDouble(record.getAttribute("ax"));
            double ay = Double.parseDouble(record.getAttribute("ay"));
            double az = Double.parseDouble(record.getAttribute("az"));
            double bx = Double.parseDouble(record.getAttribute("bx"));
            double by = Double.parseDouble(record.getAttribute("by"));
            double bz = Double.parseDouble(record.getAttribute("bz"));
            double ex = Double.parseDouble(record.getAttribute("ex"));
            double ey = Double.parseDouble(record.getAttribute("ey"));
            double ez = Double.parseDouble(record.getAttribute("ez"));
            double mux = Double.parseDouble(record.getAttribute("mux"));
            double muy = Double.parseDouble(record.getAttribute("muy"));
            double muz = Double.parseDouble(record.getAttribute("muz"));
            double sx = Double.parseDouble(record.getAttribute("sx"));
            double sy = Double.parseDouble(record.getAttribute("sy"));
            double sz = Double.parseDouble(record.getAttribute("sz"));
            double etx = Double.parseDouble(record.getAttribute("etx"));
            double ety = Double.parseDouble(record.getAttribute("ety"));
            double etpx = Double.parseDouble(record.getAttribute("etpx"));
            double etpy = Double.parseDouble(record.getAttribute("etpy"));
            double w = 0;
            String sw = record.getAttribute("w");
            if ((sw != null) && (!sw.equals(""))) {
                w = Double.parseDouble(sw);
            }
            double s = Double.parseDouble(record.getAttribute("z"));
            String shape = record.getAttribute("shape");
            if (shape == null) {
                shape = "";
            }
            double apx = 0;
            String sapx = record.getAttribute("apx");
            if ((sapx != null) && (!sapx.equals(""))) {
                apx = Double.parseDouble(sapx);
            }
            double apy = 0;
            String sapy = record.getAttribute("apy");
            if ((sapy != null) && (!sapy.equals(""))) {
                apy = Double.parseDouble(sapx);
            }
            lists.add(id, x, y, z, xp, yp, zp, ax, ay, az, bx, by, bz, ex, ey, ez, mux, muy, muz, sx, sy, sz, etx, ety, etpx, etpy, w, s, shape, apx, apy);
        }
        return lists;
    }

    public static void main(String argv[]) {
        try {
            TwissFastParser.parse("./t3d/twiss.xml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
