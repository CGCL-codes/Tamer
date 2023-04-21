package org.gbif.portal.web.controller.taxonomy;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.gbif.portal.dto.geospatial.CountryDTO;
import org.gbif.portal.dto.taxonomy.BriefTaxonConceptDTO;
import org.gbif.portal.dto.taxonomy.TaxonConceptDTO;
import org.gbif.portal.service.GeospatialManager;
import org.gbif.portal.service.TaxonomyManager;
import org.gbif.portal.web.controller.RestKeyValueController;
import org.gbif.portal.web.util.TaxonConceptUtils;
import org.springframework.context.MessageSource;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

/**
 * Country Taxonomy Browser.
 * 
 * TODO this class should be factored out and the TaxonomyBrowseController should handle both resource taxonomies and country taxonomies.
 * 
 * @author dmartin
 */
public class CountryTaxonomyBrowseController extends RestKeyValueController {

    /** Taxonomy Manager providing tree and taxonConcept lookup */
    protected TaxonomyManager taxonomyManager;

    /** Taxonomy Manager providing tree and taxonConcept lookup */
    protected TaxonConceptUtils taxonConceptUtils;

    /** Geospatial Manager providing country lookup */
    protected GeospatialManager geospatialManager;

    /** The request properties taxon concept key */
    protected String countryPropertyKey = "country";

    /** The request properties taxon concept key */
    protected String taxonConceptPropertyKey = "taxon";

    /** Threshold used to determining rendering */
    protected String taxonPriorityThresholdModelKey = "taxonPriorityThreshold";

    /** Threshold used to determining rendering */
    protected int taxonPriorityThreshold = 20;

    protected MessageSource messageSource;

    /**
	 * @see org.gbif.portal.web.controller.RestController#handleRequest(java.util.Map, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
    @Override
    public ModelAndView handleRequest(Map<String, String> propertiesMap, HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.debug("Viewing country taxonomy");
        String countryCode = propertiesMap.get(countryPropertyKey);
        String taxonConceptKey = propertiesMap.get(taxonConceptPropertyKey);
        ModelAndView mav = resolveAndCreateView(propertiesMap, request, false);
        if (countryCode == null) {
            return redirectToDefaultView();
        }
        Locale locale = RequestContextUtils.getLocale(request);
        CountryDTO countryDTO = geospatialManager.getCountryFor(countryCode, locale);
        logger.debug(countryDTO);
        if (countryDTO == null) redirectToDefaultView();
        mav.addObject("country", countryDTO);
        mav.addObject("messageSource", messageSource);
        if (taxonConceptKey != null && countryCode != null) {
            TaxonConceptDTO selectedConcept = taxonomyManager.getTaxonConceptFor(taxonConceptKey);
            mav.addObject("selectedConcept", selectedConcept);
            if (logger.isDebugEnabled()) {
                logger.debug(selectedConcept);
            }
            if (selectedConcept == null) redirectToDefaultView();
            List<BriefTaxonConceptDTO> concepts = taxonomyManager.getClassificationFor(taxonConceptKey, false, countryCode, true);
            List<BriefTaxonConceptDTO> childConcepts = taxonomyManager.getChildConceptsFor(taxonConceptKey, countryCode, true);
            taxonConceptUtils.organiseUnconfirmedNames(request, selectedConcept, concepts, childConcepts, taxonPriorityThreshold);
            mav.addObject("concepts", concepts);
        } else if (countryCode != null) {
            List<BriefTaxonConceptDTO> concepts = taxonomyManager.getRootTaxonConceptsForCountry(countryCode);
            mav.addObject("concepts", concepts);
        } else {
            return redirectToDefaultView();
        }
        return mav;
    }

    /**
	 * @param countryPropertyKey the countryPropertyKey to set
	 */
    public void setCountryPropertyKey(String countryPropertyKey) {
        this.countryPropertyKey = countryPropertyKey;
    }

    /**
	 * @param geospatialManager the geospatialManager to set
	 */
    public void setGeospatialManager(GeospatialManager geospatialManager) {
        this.geospatialManager = geospatialManager;
    }

    /**
	 * @param taxonConceptPropertyKey the taxonConceptPropertyKey to set
	 */
    public void setTaxonConceptPropertyKey(String taxonConceptPropertyKey) {
        this.taxonConceptPropertyKey = taxonConceptPropertyKey;
    }

    /**
	 * @param taxonomyManager the taxonomyManager to set
	 */
    public void setTaxonomyManager(TaxonomyManager taxonomyManager) {
        this.taxonomyManager = taxonomyManager;
    }

    /**
	 * @param taxonConceptUtils the taxonConceptUtils to set
	 */
    public void setTaxonConceptUtils(TaxonConceptUtils taxonConceptUtils) {
        this.taxonConceptUtils = taxonConceptUtils;
    }

    /**
	 * @param taxonPriorityThreshold the taxonPriorityThreshold to set
	 */
    public void setTaxonPriorityThreshold(int taxonPriorityThreshold) {
        this.taxonPriorityThreshold = taxonPriorityThreshold;
    }

    /**
	 * @param taxonPriorityThresholdModelKey the taxonPriorityThresholdModelKey to set
	 */
    public void setTaxonPriorityThresholdModelKey(String taxonPriorityThresholdModelKey) {
        this.taxonPriorityThresholdModelKey = taxonPriorityThresholdModelKey;
    }

    /**
	 * @param messageSource the messageSource to set
	 */
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
}
