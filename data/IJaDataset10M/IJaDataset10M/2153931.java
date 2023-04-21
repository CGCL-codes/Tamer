package au.edu.educationau.opensource.dsm.adapters;

import java.util.HashMap;
import java.util.Map;
import au.edu.educationau.opensource.dsm.obj.SearchCriteria;
import au.edu.educationau.opensource.dsm.util.EducationAuUtils;

/**
 * A facade to the EDNASearchAdapterImpl that derives the query param value from
 * multiqueryText custom params if they are present. Otherwise performs
 * inherited behaviour.
 * 
 * The derived value is Verity syntax, and the word param gets set accordingly.
 */
public class MQParamsToEDNASearchAdapterImpl extends EDNASearchAdapterImpl {

    /**
	 * Relational fields. Eg. a search in dc:date is done using Verity
	 * relational syntax, as opposed to a text search.
	 */
    private static String[] relationalFields = new String[] { "dc:date", "dc:date_created", "dc:date_valid", "dc:date_available", "dc:date_issued", "dc:date_modified", "dc:date_accepted", "dc:date_copyrighted", "dc:date_submitted", "ev:date", "ev:date_start", "ev:date_end", "ev:date_duration", "ev:date_earlyregistration", "ev:date_regisration", "ev:date_abstractsubmission", "ev:date_papersubmission" };

    /**
	 * Extended field scopes. Eg. a search in dc:date is actually executed as a
	 * search in dc:date or dc:date_created or dc:date_valid etc
	 */
    private static Map searchFieldScopes = new HashMap();

    static {
        searchFieldScopes.put("dc:date", new String[] { "dc:date", "dc:date_created", "dc:date_valid", "dc:date_available", "dc:date_issued", "dc:date_modified", "dc:date_accepted", "dc:date_copyrighted", "dc:date_submitted", "ev:date_start", "ev:date_end", "ev:date_duration", "ev:date_earlyregistration", "ev:date_regisration", "ev:date_abstractsubmission", "ev:date_papersubmission" });
        searchFieldScopes.put("ev:date", new String[] { "ev:date_start", "ev:date_end" });
        searchFieldScopes.put("ev:location", new String[] { "ev:location", "ev:location_address", "ev:location_city", "ev:location_country", "ev:location_region", "ev:location_postcode", "ev:location_town" });
        searchFieldScopes.put("dc:coverage", new String[] { "dc:coverage", "ev:location", "ev:location_address", "ev:location_city", "ev:location_country", "ev:location_region", "ev:location_postcode", "ev:location_town" });
        searchFieldScopes.put("dc:title", new String[] { "dc:title", "dc:title_alternative" });
        searchFieldScopes.put("edna:audience", new String[] { "edna:audience", "agls:audience" });
    }

    public void doPrime() throws Throwable {
        String query = EducationAuUtils.convertMQParamsToVeritySyntax(getSearchCriteria(), getProperties().getAdapterCode(), relationalFields, searchFieldScopes);
        if (query != null && query.length() > 0) {
            try {
                setSearchCriteria((SearchCriteria) getSearchCriteria().clone());
            } catch (CloneNotSupportedException e) {
            }
            if (getSearchCriteria().isCaseSensitive()) {
                getSearchCriteria().setQuery(query);
            } else {
                getSearchCriteria().setQuery(query.toLowerCase());
            }
            getSearchCriteria().setKeywordConstraint("verity");
        }
        super.doPrime();
    }

    public String toString() {
        return "o.m.d.a.MQParamsToEDNASearchAdapterImpl";
    }
}
