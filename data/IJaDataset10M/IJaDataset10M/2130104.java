package org.dcm4chee.xero.search.filter;

import static org.dcm4chee.xero.metadata.servlet.MetaDataServlet.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import org.dcm4chee.xero.location.InstanceFileLocatorFactory;
import org.dcm4chee.xero.location.MBeanInstanceFileLocator;
import org.dcm4chee.xero.metadata.filter.Filter;
import org.dcm4chee.xero.metadata.filter.FilterItem;
import org.dcm4chee.xero.metadata.filter.FilterUtil;
import org.dcm4chee.xero.metadata.filter.MemoryCacheFilter;
import org.dcm4chee.xero.wado.WadoParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This filter knows how to get the raw file location URL, from the file system
 * management name instance.
 * 
 * @author bwallace
 * 
 */
public class FileLocationMgtFilter implements Filter<URL> {

    private static final Logger log = LoggerFactory.getLogger(FileLocationMgtFilter.class);

    private static FileLocationParameterChecker checker = new FileLocationParameterChecker(null, "dcm4chee", "idc2");

    private static InstanceFileLocatorFactory locatorFactory = new InstanceFileLocatorFactory();

    /** Create a try next URL */
    private static URL createTryNext() {
        try {
            return new URL("http://trynext");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private static URL TRY_NEXT_URL = createTryNext();

    /**
    * Returns the DICOM file <tt>URL</tt> for the given arguments.
    * <p>
    * The underlying MBean 
    * 
    * @param instanceUID
    *           Unique identifier of the instance.
    * 
    * @return The file URL or null if not found.
    * 
    * @throws IOException
    */
    protected URL getDICOMFileURL(String aeTitle, String objectUID) throws IOException {
        try {
            MBeanInstanceFileLocator locator = locatorFactory.getInstanceFileLocator(aeTitle);
            Object location = locator.locateInstance(objectUID);
            if (location == null) return TRY_NEXT_URL; else if (location instanceof String) return getDICOMFileURL((String) location, objectUID); else return new URL("file:///" + location);
        } catch (Exception e) {
            throw new RuntimeException("Unable to determine location of instance file", e);
        }
    }

    /** Get the URL of the local file - may not be updated for DB changes etc */
    public URL filter(FilterItem<URL> filterItem, Map<String, Object> params) {
        if (!checker.isLocationTypeInParameters(params)) {
            URL ret = filterItem.callNextFilter(params);
            log.debug("Calling next filter, returned {}", ret);
            return ret;
        }
        long start = System.nanoTime();
        String objectUID = (String) params.get("objectUID");
        try {
            String aeTitle = FilterUtil.getString(params, WadoParams.AE, "local");
            URL url = getDICOMFileURL(aeTitle, objectUID);
            if (url == null) return null;
            if (url == TRY_NEXT_URL) {
                log.info("Calling next file locations.");
                return filterItem.callNextFilter(params);
            }
            int size = url.toString().length() * 2 + 64;
            params.put(MemoryCacheFilter.CACHE_SIZE, size);
            log.info("Time to read " + objectUID + " file location=" + nanoTimeToString(System.nanoTime() - start) + " size of URL=" + size);
            return url;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            log.warn("Caught exception getting dicom file location:" + e, e);
            return filterItem.callNextFilter(params);
        }
    }
}
