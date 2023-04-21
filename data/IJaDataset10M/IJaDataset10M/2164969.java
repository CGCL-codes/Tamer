package org.dcm4chee.xero.search;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.context.FacesContext;
import org.dcm4chee.xero.cycle.CyclePageAction;
import org.dcm4chee.xero.search.study.StudySearchConditionParser;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;

/** This class handles the search page results */
@Name("SearchPageAction")
public class SearchPageAction extends CyclePageAction {

    @In
    FacesContext facesContext;

    @In(required = false, value = "StudySearchCriteria")
    @Out(value = "StudySearchCriteria", scope = ScopeType.CONVERSATION)
    SearchCriteria criteria;

    @In(required = false)
    @Out(required = false, scope = ScopeType.CONVERSATION)
    List<String> studySelection;

    static StudySearchConditionParser parser = new StudySearchConditionParser();

    /** This method sets up the various search conditions.  
	 * @return The submit type string.
	 */
    @SuppressWarnings("unchecked")
    @Begin(join = true)
    public String search() {
        Map<String, String[]> map = facesContext.getExternalContext().getRequestParameterValuesMap();
        String[] submitValues = map.get("submit");
        String submit = (submitValues == null ? null : submitValues[0]);
        log.info("SearchPageAction called with submit=" + submit);
        parseSearchCriteria(submit, map);
        parseStudySelection(submit, map);
        if ("Add".equals(submit) || "View".equals(submit)) {
            addCycle(studySelection);
        }
        if (submit != null) return submit;
        return "success";
    }

    /** Generate the study selection from the map */
    void parseStudySelection(String submit, Map<String, String[]> map) {
        if (submit == null || submit.length() == 0) return;
        String[] uids = map.get("study");
        if (uids == null) {
            studySelection = Collections.emptyList();
        } else {
            studySelection = Arrays.asList(uids);
        }
    }

    /** Parses the search criteria from the map */
    void parseSearchCriteria(String submit, Map<String, ?> map) {
        if (submit != null) {
            log.debug("Parsing criteria from provided values.");
            criteria = parser.parseFromMap(map);
        } else if (criteria == null) {
            log.debug("Resetting criteria, as it was null.");
            Map<String, String> defaultMap = new HashMap<String, String>();
            defaultMap.put("ModalitiesInStudy", "");
            defaultMap.put("PatientID", "");
            defaultMap.put("AccessionNumber", "");
            defaultMap.put("StudyDateTime", "");
            criteria = parser.parseFromMap(defaultMap);
        }
        log.debug("Found study search criteria #0", criteria.getXml());
        log.debug("Query parameters: #0", criteria.getURLParameters());
    }
}
