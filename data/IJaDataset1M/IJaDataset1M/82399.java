package com.plus.fcentre.jobfilter.web.modelandview;

import org.springframework.web.servlet.ModelAndView;
import com.plus.fcentre.jobfilter.domain.Agency;

/**
 * Interface for building ModelAndView objects associated with agencies.
 * <p>
 * A singleton object implementing this interface is typically constructed by
 * the Spring container and referenced from individual Web MVC controllers.
 * </p>
 * 
 * @author Steve Jefferson
 */
public interface AgencyModelAndViewBuilder {

    /**
	 * Retrieve agency details view name.
	 * 
	 * @return agency details view name.
	 */
    public String getAgencyDetailsView();

    /**
	 * Update agency details view name.
	 * 
	 * @param agencyDetailsView agency details view name.
	 */
    public void setAgencyDetailsView(String agencyDetailsView);

    /**
	 * Retrieve agency list view name.
	 * 
	 * @return agency list view name.
	 */
    public String getAgencyListView();

    /**
	 * Update agency list view name.
	 * 
	 * @param agencyListView agency list view name.
	 */
    public void setAgencyListView(String agencyListView);

    /**
	 * Construct ModelAndView for agency details page.
	 * 
	 * @param agency agency whose details are to be displayed
	 * @return agency details ModelAndView.
	 */
    public ModelAndView createAgencyDetailsModelAndView(Agency agency);

    /**
	 * Construct ModelAndView for agency list page.
	 * 
	 * @return agency list ModelAndView.
	 */
    public ModelAndView createAgencyListModelAndView();
}
