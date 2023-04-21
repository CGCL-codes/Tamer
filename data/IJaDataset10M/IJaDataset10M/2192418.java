package tag4m.feeds;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import tag4m.communication.Tag4M;
import tag4m.pachube.Data;
import tag4m.pachube.Feed;
import tag4m.pachube.Pachube;
import tag4m.pachube.PachubeException;
import tag4m.pachube.httpclient.HttpMethod;
import tag4m.pachube.httpclient.HttpRequest;
import tag4m.pachube.httpclient.HttpResponse;

public class PachubeConfiguration {

    public static enum TagDataType {

        UNKNOWN, AI0, AI1, AI2, DIO0, DIO1, DIO2, DIO3, TEMPERATURE, CURRENT, VOLTAGE, VBATT, RSSI, SLEEP
    }

    ;

    public static TagDataType getDataType(String name) {
        if (name.equalsIgnoreCase("ai0")) return TagDataType.AI0;
        if (name.equalsIgnoreCase("ai1")) return TagDataType.AI1;
        if (name.equalsIgnoreCase("ai2")) return TagDataType.AI2;
        if (name.equalsIgnoreCase("dio0")) return TagDataType.DIO0;
        if (name.equalsIgnoreCase("dio1")) return TagDataType.DIO1;
        if (name.equalsIgnoreCase("dio2")) return TagDataType.DIO2;
        if (name.equalsIgnoreCase("dio3")) return TagDataType.DIO3;
        if (name.equalsIgnoreCase("temperature")) return TagDataType.TEMPERATURE;
        if (name.equalsIgnoreCase("vbatt")) return TagDataType.VBATT;
        if (name.equalsIgnoreCase("voltage")) return TagDataType.VOLTAGE;
        if (name.equalsIgnoreCase("rssi")) return TagDataType.RSSI;
        if (name.equalsIgnoreCase("current")) return TagDataType.CURRENT;
        if (name.equalsIgnoreCase("sleep")) return TagDataType.SLEEP; else return TagDataType.UNKNOWN;
    }

    public static long FEED_MINIMUM_DELAY = 20000;

    private long lastFeed = 0;

    private Pachube p;

    private Feed f;

    private HashMap<TagDataType, Data> feeds = new HashMap<TagDataType, Data>();

    String KEY = null;

    boolean disabled = false;

    String disabledReason = "";

    public PachubeConfiguration(String KEY, int FEED_ID) throws PachubeException {
        super();
        p = new Pachube(KEY);
        f = p.getFeed(FEED_ID);
        this.KEY = KEY;
    }

    public static void LoadPachubeFeeds(PachubeDataFeed df, String file) throws IOException, NumberFormatException, PachubeException {
        FileInputStream fstream = new FileInputStream(file);
        DataInputStream in = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String strLine;
        String cfgLine = "";
        while ((strLine = br.readLine()) != null) {
            System.out.println("Process line:" + strLine);
            if (!strLine.equals("#")) {
                cfgLine += strLine + ":";
                continue;
            }
            strLine = cfgLine;
            String v[] = strLine.split(":");
            Long MAC = new Long(v[0]);
            String KEY = v[1];
            String FEED_ID = v[2];
            System.out.println(MAC + " " + KEY + " " + FEED_ID);
            PachubeConfiguration config = new PachubeConfiguration(KEY, Integer.parseInt(FEED_ID));
            for (int i = 3; i < v.length; i++) {
                String[] d = v[i].split(",");
                Data data = new Data();
                data.setTag(d[0]);
                data.setId(Integer.parseInt(d[1]));
                PachubeConfiguration.TagDataType type = PachubeConfiguration.getDataType(d[2]);
                data.setCalibration(d[3]);
                if (type != TagDataType.UNKNOWN) {
                    config.addFeedDatastream(type, data);
                    df.addTag4MPatchubeConfiguration(MAC, config);
                    System.out.println("Add data stream:" + d[0] + " " + d[1] + " " + d[2]);
                } else System.err.println("Ignore data type for MAC:" + MAC + ". Unknown data type.");
            }
            cfgLine = "";
        }
        in.close();
    }

    public void addFeedDatastream(TagDataType tagDataName, Data data) {
        feeds.put(tagDataName, data);
    }

    /**
	 * Check which tag data values are configured to be sent to patchube and feed them.
	 * @param tag
	 */
    public void updateFeed(Tag4M tag) {
        if (disabled) return;
        if (System.currentTimeMillis() - lastFeed <= FEED_MINIMUM_DELAY) {
            return;
        }
        String baseXML = "";
        lastFeed = System.currentTimeMillis();
        for (Iterator iterator = feeds.keySet().iterator(); iterator.hasNext(); ) {
            TagDataType t = (TagDataType) iterator.next();
            Data d = feeds.get(t);
            double value = 0.0;
            boolean validValue = true;
            switch(t) {
                case AI0:
                    {
                        value = tag.getAI0();
                        break;
                    }
                case AI1:
                    {
                        value = tag.getAI1();
                        break;
                    }
                case AI2:
                    {
                        value = tag.getAI2();
                        break;
                    }
                case DIO0:
                    {
                        value = tag.getDIO0();
                        break;
                    }
                case DIO1:
                    {
                        value = tag.getDIO1();
                        break;
                    }
                case DIO2:
                    {
                        value = tag.getDIO2();
                        break;
                    }
                case DIO3:
                    {
                        value = tag.getDIO3();
                        break;
                    }
                case TEMPERATURE:
                    {
                        value = tag.getTemp() / 1000;
                        break;
                    }
                case CURRENT:
                    {
                        value = tag.getCurrent();
                        break;
                    }
                case VOLTAGE:
                    {
                        value = tag.getVoltage();
                        break;
                    }
                case VBATT:
                    {
                        value = tag.getVBatt();
                        break;
                    }
                case RSSI:
                    {
                        value = tag.getRSSI();
                        break;
                    }
                case SLEEP:
                    {
                        value = tag.getSleepTime();
                        break;
                    }
                default:
                    validValue = false;
            }
            if (validValue) {
                for (Iterator i = feeds.values().iterator(); i.hasNext(); ) {
                    Data type = (Data) i.next();
                    if (type.getDependentID() == d.getId()) {
                        type.setDependentValue(value);
                    }
                }
                d.setValue(value);
                baseXML += d.toXML();
            }
        }
        try {
            p.updateFeed(f.getId(), toXMLWIthWrapper(baseXML));
        } catch (PachubeException e) {
            e.printStackTrace();
            disabledReason = e.getMessage();
        }
    }

    String toXMLWIthWrapper(String xml) {
        String ret = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<eeml xmlns=\"http://www.eeml.org/xsd/005\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" version=\"5\" xsi:schemaLocation=\"http://www.eeml.org/xsd/005 http://www.eeml.org/xsd/005/005.xsd\"><environment>";
        ret = ret + xml + "</environment></eeml>";
        return ret;
    }
}
