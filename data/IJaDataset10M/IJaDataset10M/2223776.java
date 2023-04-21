package de.ecovations.opencms.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import org.opencms.file.CmsDataAccessException;
import org.opencms.file.CmsFile;
import org.opencms.file.CmsObject;
import org.opencms.file.CmsResource;
import org.opencms.file.collectors.A_CmsResourceCollector;
import org.opencms.file.collectors.CmsCollectorData;
import org.opencms.file.collectors.I_CmsResourceCollector;
import org.opencms.main.CmsException;
import org.opencms.main.CmsIllegalArgumentException;
import org.opencms.main.OpenCms;
import org.opencms.xml.content.CmsXmlContent;
import org.opencms.xml.content.CmsXmlContentFactory;
import org.opencms.xml.types.I_CmsXmlContentValue;
import org.opencms.file.collectors.Messages;

/**
 * Reads xml page defintions
 * 
 * @author konrad.wulf
 * @deprecated can only collect resources of the same type therefore adding needed to be implemented using {@link PageDefinitionJspBean}.
 *
 */
public class ResourceCollector extends A_CmsResourceCollector {

    /** Static array of the collectors implemented by this class. */
    private static final String[] COLLECTORS = { "columnOfPageDef" };

    /** Array list for fast collector name lookup. */
    private static final List COLLECTORS_LIST = Collections.unmodifiableList(Arrays.asList(COLLECTORS));

    /**
     * @see org.opencms.file.collectors.I_CmsResourceCollector#getCollectorNames()
     */
    public List getCollectorNames() {
        return COLLECTORS_LIST;
    }

    /**
     * @see org.opencms.file.collectors.I_CmsResourceCollector#getCreateLink(org.opencms.file.CmsObject, java.lang.String, java.lang.String)
     */
    public String getCreateLink(CmsObject cms, String collectorName, String param) throws CmsDataAccessException, CmsException {
        if (collectorName == null) {
            collectorName = COLLECTORS[0];
        }
        switch(COLLECTORS_LIST.indexOf(collectorName)) {
            case 0:
                return "http://ecovations.de";
            default:
                throw new CmsDataAccessException(Messages.get().container(Messages.ERR_COLLECTOR_NAME_INVALID_1, collectorName));
        }
    }

    /**
     * @see org.opencms.file.collectors.I_CmsResourceCollector#getCreateParam(org.opencms.file.CmsObject, java.lang.String, java.lang.String)
     */
    public String getCreateParam(CmsObject cms, String collectorName, String param) throws CmsDataAccessException {
        if (collectorName == null) {
            collectorName = COLLECTORS[0];
        }
        switch(COLLECTORS_LIST.indexOf(collectorName)) {
            case 0:
                return param;
            default:
                throw new CmsDataAccessException(Messages.get().container(Messages.ERR_COLLECTOR_NAME_INVALID_1, collectorName));
        }
    }

    /**
     * @see org.opencms.file.collectors.I_CmsResourceCollector#getResults(org.opencms.file.CmsObject, java.lang.String, java.lang.String)
     */
    public List<CmsResource> getResults(CmsObject cms, String collectorName, String param) throws CmsDataAccessException, CmsException {
        if (collectorName == null) {
            collectorName = COLLECTORS[0];
        }
        switch(COLLECTORS_LIST.indexOf(collectorName)) {
            case 0:
                return null;
            default:
                throw new CmsDataAccessException(Messages.get().container(Messages.ERR_COLLECTOR_NAME_INVALID_1, collectorName));
        }
    }

    /**
     * Returns all resources in the column of a xml page defintion pointed to by the parameter.<p>
     * 
     * @param cms the current OpenCms user context
     * @param param the page definition file to use + column index, separated by a "|", e.g. "/home/index.html|1"
     * 
     * @return all resources in the requested column of a given xml page defintion
     * 
     * @throws CmsException if something goes wrong
     * @throws CmsIllegalArgumentException if the given param argument is not a link to a single file
     * 
     */
    protected List getAllInXmlColumn(CmsObject cms, String param) throws CmsException, CmsIllegalArgumentException {
        CmsCollectorData data = new CmsCollectorData(param);
        int columnIndex = data.getCount();
        String element = "Column[" + columnIndex + "]/Content";
        List<CmsResource> result = new ArrayList<CmsResource>();
        Locale locale = cms.getRequestContext().getLocale();
        String collectorName = "singleFile";
        I_CmsResourceCollector collector = OpenCms.getResourceManager().getContentCollector(collectorName);
        if (collector == null) {
            throw new CmsException(org.opencms.jsp.Messages.get().container(org.opencms.jsp.Messages.ERR_COLLECTOR_NOT_FOUND_1, collectorName));
        }
        List<CmsResource> collectorResult = new ArrayList<CmsResource>();
        Iterator<CmsResource> it = collectorResult.iterator();
        CmsResource resource = (CmsResource) it.next();
        CmsFile file = cms.readFile(resource);
        CmsXmlContent content = CmsXmlContentFactory.unmarshal(cms, file);
        if (!content.hasLocale(locale)) {
            Iterator it2 = OpenCms.getLocaleManager().getDefaultLocales().iterator();
            while (it2.hasNext()) {
                Locale _locale = (Locale) it2.next();
                if (content.hasLocale(_locale)) {
                    locale = _locale;
                    break;
                }
            }
        }
        int pp = 0;
        while (content.hasValue(element, locale, pp)) {
            I_CmsXmlContentValue value = content.getValue(element, locale, pp);
            String columnEntry = value.getStringValue(cms);
            ++pp;
            result.add(cms.readResource(columnEntry));
        }
        return result;
    }
}
