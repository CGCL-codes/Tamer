package cn.tearcry.api.weather;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author rajab
 * 
 */
public class NowHanlder extends DefaultHandler {

    public static void main(String[] args) {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        SAXParser sp;
        try {
            sp = spf.newSAXParser();
            XMLReader xr = sp.getXMLReader();
            WeatherData data = new WeatherData();
            NowHanlder hanlder = new NowHanlder(data);
            xr.setContentHandler(hanlder);
            xr.parse(DataSourceManager.getInputSource(new File("D:/GraduationDesign/data/05_10_xian_now.xml")));
            HashMap<String, String> map = data.getNowData();
            HashMap<String, String> head = data.getHeadData();
            Iterator<String> ithead = head.keySet().iterator();
            while (ithead.hasNext()) {
                String k = ithead.next();
                System.out.println(k + ":" + head.get(k));
            }
            Iterator<String> iter = map.keySet().iterator();
            while (iter.hasNext()) {
                String key = iter.next();
                System.out.println(key + ":" + map.get(key));
            }
        } catch (ParserConfigurationException ex) {
            ex.printStackTrace();
        } catch (SAXException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private HashMap<String, String> nowData;

    private WeatherData wData;

    public NowHanlder(WeatherData wData) {
        nowData = wData.getNowData();
        this.wData = wData;
    }

    public void startElement(String uri, String localName, String qName, Attributes attr) {
        if (qName.equalsIgnoreCase("yweather:wind")) {
            nowData.put(WeatherKey.Weather.WIND_DIRECTION, UnitUtil.converWindDirection(attr.getValue(1)));
            nowData.put(WeatherKey.Weather.WIND_SPEED, attr.getValue(2));
        } else if (qName.equalsIgnoreCase("yweather:atmosphere")) {
            nowData.put(WeatherKey.Weather.HUMIDITY, attr.getValue(0) + "%");
            try {
                float vis = Math.round(Float.parseFloat(attr.getValue(1)) / 100);
                nowData.put(WeatherKey.Weather.VISIBILITY, vis + "");
            } catch (NumberFormatException ex) {
                nowData.put(WeatherKey.Weather.VISIBILITY, attr.getValue(1));
            }
            nowData.put(WeatherKey.Weather.PRESSURE, attr.getValue(2));
        } else if (qName.equalsIgnoreCase("yweather:condition")) {
            nowData.put(WeatherKey.Weather.DESCRIPTION, attr.getValue(0));
            nowData.put(WeatherKey.Weather.ICON, attr.getValue(1));
            nowData.put(WeatherKey.Weather.TEMPERATURE, attr.getValue(2));
            nowData.put(WeatherKey.Weather.LAST_UPDATE, UnitUtil.convertTime("EEE, dd MMM yyyy h:mm a", attr.getValue(3)));
        }
    }

    public void endElement(String uri, String localName, String qName) {
        if (qName.equals("yweather:condition")) wData.mNowParsed = true;
    }
}
