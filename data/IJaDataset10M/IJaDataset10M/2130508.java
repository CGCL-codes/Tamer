package eu.mpower.framework.fsa.j2me.XML_11073;

import eu.mpower.framework.fsa.j2me.core.FrameImp;
import eu.mpower.framework.fsa.j2me.core.Sensor;
import eu.mpower.framework.fsa.j2me.types.Value;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import java.util.Hashtable;
import java.util.Vector;

/**
 * This class is a parser to get a message in IEEE11073 format
 *
 * @author SFM
 * @version 0.1
 */
public class IEEE11073PulseOximeterHistorySAX extends DefaultHandler {

    private boolean rest = false;

    private boolean inTime = false;

    private boolean SPO2 = true;

    private boolean error = false;

    private StringBuffer file = new StringBuffer("");

    private String aux = "";

    private int n = 0;

    private int nvalues = 0;

    private Sensor sensor;

    private Vector query;

    private Value v;

    private Hashtable types = new Hashtable();

    /**
     * The constructor for class IEEE11073PulseOximeterHistorySAX
     * @param sensor Sensor to whom want to create IEEE11073 message
     * @param num_values Number of values that wan to be showed
     *
     */
    public IEEE11073PulseOximeterHistorySAX(Sensor sensor, int num_values) {
        types.put("heartRate", "heartRate");
        types.put("spo2", "spo2");
        this.sensor = sensor;
        nvalues = num_values;
        query = new Vector();
    }

    /**
     * This method ...
     *
     * @param namespaceURI
     * @param localName
     * @param qualifiedName
     * @param atts
     * @throws SAXException
     */
    public void startElement(String namespaceURI, String localName, String qualifiedName, Attributes atts) throws SAXException {
        if (qualifiedName.equals("absoluteTime")) {
            inTime = true;
            rest = true;
            n++;
        }
        if (qualifiedName.equals("metricPartition")) {
            file.append("<" + qualifiedName + ">");
            return;
        }
        if (!inTime && !rest) {
            if (atts.getLength() > 0) {
                file.append("<" + qualifiedName + " ");
                for (int i = 0; i < atts.getLength(); i++) {
                    file.append(atts.getQName(i) + "=\"" + atts.getValue(i) + "\">");
                }
            } else {
                file.append("<" + qualifiedName + ">");
            }
        }
    }

    /**
     * This method ...
     *
     * @param namespaceURI
     * @param localName
     * @param qualifiedName
     * @throws SAXException
     */
    public void endElement(String namespaceURI, String localName, String qualifiedName) throws SAXException {
        if (qualifiedName.equals("absoluteTime")) {
            inTime = false;
            rest = true;
            return;
        }
        if (qualifiedName.equals("observationValue")) {
            rest = false;
            return;
        }
        if (!inTime && !rest) {
            if (qualifiedName.equals("metricPartition")) {
                file.append("</" + qualifiedName + ">");
            } else {
                file.append("</" + qualifiedName + ">");
            }
        }
    }

    /**
     * This method ...
     *
     * @param ch
     * @param start
     * @param length
     *
     * @throws SAXException
     */
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (!rest) {
            for (int i = start; i < start + length; i++) {
                if (ch[i] != '\n') {
                    aux += ch[i];
                }
            }
            file.append(aux);
            aux = "";
        } else {
            if (inTime) {
                if (SPO2) {
                    query = FrameImp.queryDataDB(sensor.getIDSensor(), "spo2", nvalues);
                    SPO2 = false;
                } else {
                    query = FrameImp.queryDataDB(sensor.getIDSensor(), "heartRate", nvalues);
                }
                if (query.size() == 0) {
                    error = true;
                    v = new Value();
                    v.setTime("no_Time no_Time no_Time no_Time:no_Time:no_Time no_Time no_Time");
                    v.setVal("no_Value");
                    query.addElement((Value) v);
                }
                for (int i = 0; i < query.size(); i++) {
                    v = (Value) query.elementAt(i);
                    String time = v.getTime();
                    file.append("<absoluteTime type=\"ieee.dim.types.absoluteTime\">");
                    time = time.substring(time.indexOf(" ") + 1);
                    file.append("<month>" + time.substring(0, time.indexOf(" ")) + "</month>");
                    time = time.substring(time.indexOf(" ") + 1);
                    file.append("<day>" + time.substring(0, time.indexOf(" ")) + "</day>");
                    time = time.substring(time.indexOf(" "));
                    String hour = time.substring(time.indexOf(" ") + 1);
                    file.append("<hour>" + hour.substring(0, hour.indexOf(":")) + "</hour>");
                    hour = hour.substring(hour.indexOf(":") + 1);
                    file.append("<minute>" + hour.substring(0, hour.indexOf(":")) + "</minute>");
                    hour = hour.substring(hour.indexOf(":") + 1);
                    file.append("<second>" + hour.substring(0, hour.indexOf(" ")) + "</second>");
                    time = time.substring(time.indexOf(" ") + 1);
                    time = time.substring(time.indexOf(" ") + 1);
                    time = time.substring(time.indexOf(" ") + 1);
                    file.append("<year>" + time + "</year>");
                    file.append("</absoluteTime>");
                    file.append("<observationValue type=\"ieee.dim.types.NuObsValue\">" + "<uniCode>544</uniCode>" + "<metricID>19384</metricID>" + "<Value>");
                    file.append("" + v.getVal());
                    file.append("</Value>" + "<state type=\"ieee.dim.types.MeasurementStatus\">" + "<status>" + "OK" + "</status>" + "</state>" + "</observationValue>");
                }
                inTime = false;
            }
        }
    }

    /**
     * This method return the string where IEEE11073 message is stored
     *
     * @return IEEE11073 message
     */
    public String getFile() {
        if (error) {
            error = false;
            return "No data into database";
        }
        return file.toString();
    }
}
