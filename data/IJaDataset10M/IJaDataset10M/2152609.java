package org.ln.millesimus.trace;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;

public class XMLManager {

    private static List<Township> townshipList = null;

    private static HashMap<String, String> districtMap = null;

    private static HashMap<String, String> regionMap = null;

    private static String townshipPath = "township.xml";

    protected static List<Township> getTownshipList() {
        if (townshipList == null) {
            parse();
        }
        return townshipList;
    }

    protected static HashMap<String, String> getDistrictMap() {
        if (districtMap == null) {
            parse();
        }
        return districtMap;
    }

    protected static HashMap<String, String> getRegionMap() {
        if (regionMap == null) {
            parse();
        }
        return regionMap;
    }

    /**
	 * 
	 * 
	 * 
	 * @param abbr
	 * @return
	 */
    protected static String getDistrictNameByAbbr(String abbr) {
        if (getDistrictMap().containsKey(abbr)) return getDistrictMap().get(abbr);
        return "UNKNOW";
    }

    /**
	 * @return
	 */
    protected static Collection<String> getDistrictList() {
        return getDistrictMap().values();
    }

    /**
	 * @param region
	 * @return
	 */
    protected static String getRegionNameByAbbr(String abbr) {
        if (getRegionMap().containsKey(abbr)) return getRegionMap().get(abbr);
        return "UNKNOW";
    }

    /**
	 * @return
	 */
    protected static Collection<String> getRegionList() {
        return getRegionMap().values();
    }

    /**
	 * 
	 * @param district
	 * @return
	 */
    protected static List<Township> getTownshipListByDistricy(String district) {
        List<Township> districtList = new ArrayList<Township>();
        for (Township obj : getTownshipList()) {
            if (obj.getDistrict().equalsIgnoreCase(district)) districtList.add(obj);
        }
        return districtList;
    }

    /**
	 * Restituisce 
	 * 
	 * 
	 * @param district
	 * @return
	 */
    protected static List<Township> getTownshipListByInitial(String prefix) {
        List<Township> result = new ArrayList<Township>();
        for (Township obj : getTownshipList()) {
            if (obj.getName().startsWith(prefix.toUpperCase())) result.add(obj);
        }
        return result;
    }

    /**
	 * Restituisce una lista di comuni appartenenti ad una region
	 * 
	 * 
	 * 
	 * @param region
	 * @return
	 */
    protected static List<Township> getTownshipListByRegion(String region) {
        List<Township> result = new ArrayList<Township>();
        for (Township obj : getTownshipList()) {
            if (obj.getDistrict().equalsIgnoreCase(region)) result.add(obj);
        }
        return result;
    }

    /**
	 * Restituisce il codice corrispondente ad un comune, se esiste una stringa
	 * di ? altrimenti
	 * 
	 * @param township
	 * @return
	 */
    protected static String getCodeByTownship(String township) {
        for (Township obj : getTownshipList()) {
            if (obj.getName().equalsIgnoreCase(township)) return obj.getCode();
        }
        return "????";
    }

    /**
	 * 
	 */
    private static void parse() {
        SAXParserFactory parserFact = SAXParserFactory.newInstance();
        TraceHandler handler = new TraceHandler();
        InputStream stream = new XMLManager().getClass().getResourceAsStream(townshipPath);
        try {
            SAXParser saxparser = parserFact.newSAXParser();
            saxparser.parse(stream, handler);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        townshipList = handler.getList();
        districtMap = handler.getDistrictMap();
        regionMap = handler.getRegionMap();
    }
}
