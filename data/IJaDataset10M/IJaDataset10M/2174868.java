package org.icefaces.application.showcase.view.bean.examples.component.selectInputText;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.icefaces.application.showcase.util.MessageBundleLoader;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import java.beans.XMLDecoder;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * <p>Application-scope bean used to store static lookup information for
 * AutoComplete (selectInputText) example. Statically referenced by
 * SelectInputTextBean as the cityDictionary is rather large.</p>
 * <p>This class loads the city data from an xml data set.  Once an instance
 * is created a call to #generateCityMatches will generate a list of potential
 * matches. </p>
 *
 * @since 1.7
 */
public class CityDictionary implements Serializable {

    private static Log log = LogFactory.getLog(CityDictionary.class);

    private static boolean initialized;

    private static ArrayList cityDictionary;

    private static final String DATA_RESOURCE_PATH = "/WEB-INF/classes/org/icefaces/application/showcase/view/resources/city.xml.zip";

    /**
     * Creates a new instnace of CityDictionary.  The city dictionary is unpacked
     * and initialized during construction.  This will result in a short delay
     * of 2-3 seconds on the server as a result of processing the large file.
     */
    public CityDictionary() {
        try {
            log.info(MessageBundleLoader.getMessage("bean.selectInputText.info.initializingDictionary"));
            synchronized (this) {
                init();
            }
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error(MessageBundleLoader.getMessage("bean.selectInputText.error.initializingDictionary"));
            }
        }
    }

    /**
     * Comparator utility for sorting city names.
     */
    private static final Comparator LABEL_COMPARATOR = new Comparator() {

        public int compare(Object o1, Object o2) {
            SelectItem selectItem1 = (SelectItem) o1;
            SelectItem selectItem2 = (SelectItem) o2;
            return selectItem1.getLabel().compareToIgnoreCase(selectItem2.getLabel());
        }
    };

    /**
     * Gets the cityDictionary of cities.
     *
     * @return cityDictionary list in sorted by city name, ascending.
     */
    public List getDictionary() {
        return cityDictionary;
    }

    /**
     * Generates a short list of cities that match the given searchWord.  The
     * length of the list is specified by the maxMatches attribute.
     *
     * @param searchWord city name to search for
     * @param maxMatches max number of possibilities to return
     * @return list of SelectItem objects which contain potential city names.
     */
    public ArrayList generateCityMatches(String searchWord, int maxMatches) {
        ArrayList matchList = new ArrayList(maxMatches);
        if ((searchWord == null) || (searchWord.trim().length() == 0)) {
            return matchList;
        }
        try {
            int insert = Collections.binarySearch(cityDictionary, new SelectItem("", searchWord), LABEL_COMPARATOR);
            if (insert < 0) {
                insert = Math.abs(insert) - 1;
            }
            for (int i = 0; i < maxMatches; i++) {
                if ((insert + i) >= cityDictionary.size() || i >= maxMatches) {
                    break;
                }
                matchList.add(cityDictionary.get(insert + i));
            }
        } catch (Throwable e) {
            log.error(MessageBundleLoader.getMessage("bean.selectInputText.error.findingMatches"), e);
        }
        return matchList;
    }

    /**
     * Reads the zipped xml city cityDictionary and loads it into memory.
     */
    private static void init() throws IOException {
        if (!initialized) {
            initialized = true;
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();
            InputStream is = ec.getResourceAsStream(DATA_RESOURCE_PATH);
            ZipInputStream zipStream = new ZipInputStream(is);
            ZipEntry firstEntry = zipStream.getNextEntry();
            XMLDecoder xDecoder = new XMLDecoder(zipStream);
            List cityList = (List) xDecoder.readObject();
            xDecoder.close();
            zipStream.close();
            if (cityList == null) {
                throw new IOException();
            }
            cityDictionary = new ArrayList(cityList.size());
            City tmpCity;
            for (int i = 0, max = cityList.size(); i < max; i++) {
                tmpCity = (City) cityList.get(i);
                if (tmpCity != null && tmpCity.getCity() != null) {
                    cityDictionary.add(new SelectItem(tmpCity, tmpCity.getCity()));
                }
            }
            cityList.clear();
            Collections.sort(cityDictionary, LABEL_COMPARATOR);
        }
    }
}
