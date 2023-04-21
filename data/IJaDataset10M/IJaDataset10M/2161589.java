package org.gbif.portal.web.content.geospatial;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.WordUtils;
import org.gbif.portal.dto.PropertyStoreTripletDTO;
import org.gbif.portal.dto.geospatial.GeoRegionDTO;
import org.gbif.portal.service.GeospatialManager;
import org.gbif.portal.web.content.filter.FilterHelper;
import org.gbif.portal.web.content.filter.PicklistHelper;
import org.gbif.portal.web.filter.CriterionDTO;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author davejmartin
 *
 */
public class GeoRegionFilterHelper implements FilterHelper, PicklistHelper {

    protected GeospatialManager geospatialManager;

    /**
	 * @see org.gbif.portal.web.content.filter.FilterHelper#addCriterion2Request(org.gbif.portal.web.filter.CriterionDTO, org.springframework.web.servlet.ModelAndView, javax.servlet.http.HttpServletRequest)
	 */
    public void addCriterion2Request(CriterionDTO criterionDTO, ModelAndView mav, HttpServletRequest request) {
    }

    public String getDefaultDisplayValue(HttpServletRequest request) {
        return null;
    }

    public String getDefaultValue(HttpServletRequest request) {
        return null;
    }

    public String getDisplayValue(String value, Locale locale) {
        GeoRegionDTO geoRegion = geospatialManager.getGeoRegionFor(value);
        if (geoRegion != null && geoRegion.getName() != null) {
            return geoRegion.getName();
        }
        return value;
    }

    public void preProcess(List<PropertyStoreTripletDTO> triplets, HttpServletRequest request, HttpServletResponse response) {
    }

    /**
	 * @param geospatialManager the geospatialManager to set
	 */
    public void setGeospatialManager(GeospatialManager geospatialManager) {
        this.geospatialManager = geospatialManager;
    }

    /**
	 * @see org.gbif.portal.web.content.filter.PicklistHelper#getPicklist(javax.servlet.http.HttpServletRequest, java.util.Locale)
	 */
    public Map<String, String> getPicklist(HttpServletRequest request, Locale locale) {
        List<GeoRegionDTO> geoRegions = geospatialManager.getAllGeoRegions();
        Map<String, String> picklist = new LinkedHashMap<String, String>();
        for (GeoRegionDTO gr : geoRegions) picklist.put(gr.getKey(), WordUtils.capitalizeFully(gr.getName(), new char[] { ' ', '.', '(', '[', '{' }));
        return picklist;
    }
}
