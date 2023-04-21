package org.geogurus.cartoweb;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.logging.Logger;
import org.geogurus.tools.string.ConversionUtilities;

/**
 * Represents a images.ini cartoweb3 configuration file (client-side)
 * See cartoweb.org for documentation about this file.
 * @author nicolas Ribot
 */
public class ImagesConf extends CartowebConf {

    /** collection of mapSizes objects, indexed by MapSize.id attribute */
    private Hashtable<String, CartowebMapSize> mapSizes = null;

    /** the array of CartowebScales as dislay String, (hyphen-separated list of attributes)
     * to work with view components like
     * web pages
     */
    private String[] mapSizesAsString;

    private Boolean mapSizesActive;

    private Integer mapWidth;

    private Integer mapHeight;

    private Integer mapSizesDefault;

    private Boolean collapsibleKeymap;

    private Boolean noDrawKeymap;

    private Boolean noDrawScalebar;

    public ImagesConf() {
        logger = Logger.getLogger(this.getClass().getName());
        mapSizesActive = Boolean.FALSE;
        collapsibleKeymap = Boolean.FALSE;
        noDrawKeymap = Boolean.TRUE;
        noDrawScalebar = Boolean.FALSE;
    }

    /**
     * load this object from the given cartoweb3 .ini configuration file
     * @param iniFile
     * @return true if the object is correctly loaded, false otherwise. 
     *         In this case, see the generated log (warning level)
     */
    @Override
    public boolean loadFromFile(InputStream iniStream) {
        if (!super.loadFromFile(iniStream)) {
            return false;
        }
        setMapSizes(new Hashtable<String, CartowebMapSize>());
        Enumeration e = iniProps.propertyNames();
        while (e.hasMoreElements()) {
            String prop = (String) e.nextElement();
            if (prop.length() >= 1 && prop.trim().indexOf(";") == 0) {
                continue;
            }
            String[] keys = ConversionUtilities.explodeKey(prop);
            if (keys.length > 2) {
                if ("mapSizes".equalsIgnoreCase(keys[0])) {
                    CartowebMapSize ms = getMapSizes().containsKey(keys[1]) ? getMapSizes().get(keys[1]) : new CartowebMapSize(keys[1]);
                    if ("label".equalsIgnoreCase(keys[2])) {
                        ms.setLabel(iniProps.getProperty(prop));
                    } else if ("width".equalsIgnoreCase(keys[2])) {
                        ms.setWidth(new Integer(iniProps.getProperty(prop)));
                    } else if ("height".equalsIgnoreCase(keys[2])) {
                        ms.setHeight(new Integer(iniProps.getProperty(prop)));
                    }
                    getMapSizes().put(ms.getId(), ms);
                }
            } else if ("mapSizesActive".equalsIgnoreCase(prop)) {
                setMapSizesActive(new Boolean(iniProps.getProperty(prop)));
            } else if ("mapWidth".equalsIgnoreCase(prop)) {
                setMapWidth(new Integer(iniProps.getProperty(prop)));
            } else if ("mapHeight".equalsIgnoreCase(prop)) {
                setMapHeight(new Integer(iniProps.getProperty(prop)));
            } else if ("mapSizesDefault".equalsIgnoreCase(prop)) {
                setMapSizesDefault(new Integer(iniProps.getProperty(prop)));
            } else if ("collapsibleKeymap".equalsIgnoreCase(prop)) {
                setCollapsibleKeymap(new Boolean(iniProps.getProperty(prop)));
            } else if ("noDrawKeymap".equalsIgnoreCase(prop)) {
                setNoDrawKeymap(new Boolean(iniProps.getProperty(prop)));
            } else if ("noDrawScalebar".equalsIgnoreCase(prop)) {
                setNoDrawScalebar(new Boolean(iniProps.getProperty(prop)));
            } else {
                logger.warning("unknown images.ini property: " + prop);
            }
        }
        return true;
    }

    /**
     * load this object from the given cartoweb3 .ini configuration file path
     * @param iniFile
     * @return true if the object is correctly loaded, false otherwise. 
     *         In this case, see the generated log (warning level)
     */
    @Override
    public boolean loadFromFile(String iniFilePath) {
        if (!super.loadFromFile(iniFilePath)) {
            return false;
        }
        return this.loadFromFile(iniFile);
    }

    /**
     * Returns the string representation of this object as key=value pairs, where
     * key is the cartoweb3 property name for this configuration object.
     * @return
     */
    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        if (getMapSizesActive() != null) {
            b.append("mapSizesActive = ").append(mapSizesActive.toString()).append(System.getProperty("line.separator")).append(System.getProperty("line.separator"));
        }
        if (getMapWidth() != null) {
            b.append("mapWidth = ").append(mapWidth.toString()).append(System.getProperty("line.separator")).append(System.getProperty("line.separator"));
        }
        if (getMapHeight() != null) {
            b.append("mapHeight = ").append(mapHeight.toString()).append(System.getProperty("line.separator")).append(System.getProperty("line.separator"));
        }
        if (getMapSizesDefault() != null) {
            b.append("mapSizesDefault = ").append(mapSizesDefault.toString()).append(System.getProperty("line.separator")).append(System.getProperty("line.separator"));
        }
        if (getCollapsibleKeymap() != null) {
            b.append("collapsibleKeymap = ").append(collapsibleKeymap.toString()).append(System.getProperty("line.separator")).append(System.getProperty("line.separator"));
        }
        if (getNoDrawKeymap() != null) {
            b.append("noDrawKeymap = ").append(noDrawKeymap.toString()).append(System.getProperty("line.separator")).append(System.getProperty("line.separator"));
        }
        if (getNoDrawScalebar() != null) {
            b.append("noDrawScalebar = ").append(noDrawScalebar.toString()).append(System.getProperty("line.separator")).append(System.getProperty("line.separator"));
        }
        if (getMapSizes() != null) {
            String[] sortedKeys = (String[]) mapSizes.keySet().toArray(new String[mapSizes.size()]);
            Arrays.sort(sortedKeys);
            for (int i = 0; i < sortedKeys.length; i++) {
                b.append(mapSizes.get(sortedKeys[i]).toString()).append(System.getProperty("line.separator"));
            }
        }
        return b.toString();
    }

    public Hashtable<String, CartowebMapSize> getMapSizes() {
        return mapSizes;
    }

    public void setMapSizes(Hashtable<String, CartowebMapSize> mapSizes) {
        this.mapSizes = mapSizes;
    }

    public Boolean getMapSizesActive() {
        return mapSizesActive;
    }

    public void setMapSizesActive(Boolean mapSizesActive) {
        this.mapSizesActive = mapSizesActive;
    }

    public Integer getMapWidth() {
        return mapWidth;
    }

    public void setMapWidth(Integer mapWidth) {
        this.mapWidth = mapWidth;
    }

    public Integer getMapHeight() {
        return mapHeight;
    }

    public void setMapHeight(Integer mapHeight) {
        this.mapHeight = mapHeight;
    }

    public Integer getMapSizesDefault() {
        return mapSizesDefault;
    }

    public void setMapSizesDefault(Integer mapSizesDefault) {
        this.mapSizesDefault = mapSizesDefault;
    }

    public Boolean getCollapsibleKeymap() {
        return collapsibleKeymap;
    }

    public void setCollapsibleKeymap(Boolean collapsibleKeymap) {
        this.collapsibleKeymap = collapsibleKeymap;
    }

    public Boolean getNoDrawKeymap() {
        return noDrawKeymap;
    }

    public void setNoDrawKeymap(Boolean noDrawKeymap) {
        this.noDrawKeymap = noDrawKeymap;
    }

    public Boolean getNoDrawScalebar() {
        return noDrawScalebar;
    }

    public void setNoDrawScalebar(Boolean noDrawScalebar) {
        this.noDrawScalebar = noDrawScalebar;
    }

    /**
     * Calling this method will generate the mapSizesAsString array from the 
     * mapSizes hash, each time a call is made (to assure both collections are synchronized
     * @return the array of CartowebMapSize.display string
     */
    public String[] getMapSizesAsString() {
        int s = mapSizes == null ? 0 : mapSizes.size();
        mapSizesAsString = new String[s];
        int i = 0;
        for (CartowebMapSize mapSize : mapSizes.values()) {
            mapSizesAsString[i++] = mapSize.getDisplayString();
        }
        return mapSizesAsString;
    }

    /** Calling this method will reinitialize the list of mapSizes
     * with the given objects. if parameters is not null.
     * INvalid CartowebScale string representation will be discarded<br/>
     * Thus, passing a vector with invalid string will result to a empty list of CartowebScale
     * A valid string representation of a scale object is:<br/>
     * id - label - value - visible <br/>
     * characters separing attributes are then: ' - '
     * @param scalesAsString
     */
    public void setMapSizesAsString(String[] mapSizesAsString) {
        if (mapSizesAsString != null) {
            StringTokenizer tok = null;
            mapSizes = new Hashtable<String, CartowebMapSize>(mapSizesAsString.length);
            String t = null;
            for (String s : mapSizesAsString) {
                tok = new StringTokenizer(s, " - ");
                if (tok.countTokens() != 4) {
                    logger.warning("invalid string representation for MapSize object: " + s);
                } else {
                    CartowebMapSize mapSize = new CartowebMapSize(tok.nextToken());
                    t = tok.nextToken();
                    mapSize.setLabel("null".equalsIgnoreCase(t) || t.length() == 0 ? null : t);
                    t = tok.nextToken();
                    mapSize.setWidth("null".equalsIgnoreCase(t) || t.length() == 0 ? null : Integer.parseInt(t.trim()));
                    t = tok.nextToken();
                    mapSize.setHeight("null".equalsIgnoreCase(t) || t.length() == 0 ? null : Integer.parseInt(t.trim()));
                    mapSizes.put(mapSize.getId(), mapSize);
                }
            }
        }
        this.mapSizesAsString = mapSizesAsString;
    }
}
