package org.fao.geonet.services.crs;

import jeeves.interfaces.Service;
import jeeves.server.ServiceConfig;
import jeeves.server.context.ServiceContext;
import jeeves.utils.Util;
import org.fao.geonet.constants.Params;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.jdom.Element;
import org.opengis.metadata.Identifier;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.IdentifiedObject;
import org.opengis.referencing.crs.CRSAuthorityFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import java.util.Collection;
import java.util.Set;

/**
 * Get all Coordinate Reference System defined in GeoTools Referencing database
 * and return them as a Jeeves XML response element.
 *
 * @author francois
 */
public class Search implements Service {

    public void init(String appPath, ServiceConfig params) throws Exception {
    }

    /**
	 * Search for CRS
	 * 
	 * @param params
	 *            Parameter "name" is a list of word separated by spaces.
	 *            Parameter "maxResults" is the max number of CRS returned.
	 *            Parameter "type" is the type of CRS. Default value is
	 *            CoordinateReferenceSystem.
	 */
    public Element exec(Element params, ServiceContext context) throws Exception {
        int maxResults = Integer.valueOf(Util.getParam(params, "maxResults", "50"));
        String searchText = Util.getParam(params, Params.NAME, "");
        String filter[] = searchText.toUpperCase().split(" ");
        String crsType = Util.getParam(params, Params.TYPE, "");
        Class<? extends IdentifiedObject> crsTypeClass = CoordinateReferenceSystem.class;
        if (Constant.CRSType.containsKey(crsType)) crsTypeClass = Constant.CRSType.get(crsType);
        Element crs = filterCRS(filter, crsTypeClass, maxResults);
        return crs;
    }

    /**
	 * filters all CRS Names from all available CRS authorities
	 * 
	 * @param filter
	 *            array of keywords
	 * @param crsTypeClass
	 *            type of CRS to search for
	 * @param maxResults
	 *            maximum number of results
	 * @return XML with all CRS Names which contain all the filter keywords
	 */
    private Element filterCRS(String[] filter, Class<? extends IdentifiedObject> crsTypeClass, int maxResults) {
        Element crsList = new Element("crsList");
        int i = 0;
        for (Object object : ReferencingFactoryFinder.getCRSAuthorityFactories(null)) {
            CRSAuthorityFactory factory = (CRSAuthorityFactory) object;
            String authorityTitle = (factory.getAuthority().getTitle() == null ? "" : factory.getAuthority().getTitle().toString());
            String authorityEdition = (factory.getAuthority().getEdition() == null ? "" : factory.getAuthority().getEdition().toString());
            String authorityCodeSpace = "";
            Collection<? extends Identifier> ids = factory.getAuthority().getIdentifiers();
            for (Identifier id : ids) {
                authorityCodeSpace = id.getCode();
            }
            try {
                Set<String> codes = factory.getAuthorityCodes(crsTypeClass);
                for (Object codeObj : codes) {
                    String code = (String) codeObj;
                    String description;
                    try {
                        description = factory.getDescriptionText(code).toString();
                    } catch (Exception e1) {
                        description = "-";
                    }
                    description += " (" + authorityCodeSpace + ":" + code + ")";
                    if (matchesFilter(description.toUpperCase(), filter)) {
                        Element crs = GetCRS.formatCRS(authorityTitle, authorityEdition, authorityCodeSpace, code, description);
                        crsList.addContent(crs);
                        if (++i >= maxResults) return crsList;
                    }
                }
            } catch (FactoryException e) {
                System.out.println("CRS Authority:" + e.getMessage());
            }
        }
        return crsList;
    }

    /**
	 * checks if all keywords in filter array are in input
	 * 
	 * @param input
	 *            test string
	 * @param filter
	 *            array of keywords
	 * @return true, if all keywords in filter are in the input, false otherwise
	 */
    protected boolean matchesFilter(String input, String[] filter) {
        for (String match : filter) {
            if (!input.contains(match)) return false;
        }
        return true;
    }
}
