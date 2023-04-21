package com.acv.service.filter.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import com.acv.dao.catalog.categories.destinations.model.Destination;
import com.acv.dao.catalog.categories.facilities.model.Facility;
import com.acv.dao.catalog.categories.interests.model.Interest;
import com.acv.dao.catalog.categories.ratings.model.Rating;
import com.acv.dao.catalog.locations.ratings.model.RatingPOS;
import com.acv.dao.catalog.locations.resorts.model.Resort;
import com.acv.service.common.BaseManager;
import com.acv.service.filter.FilterManager;
import com.acv.service.filter.model.AccomodationFilterGroup;
import com.acv.service.filter.model.DestinationFilterGroup;
import com.acv.service.filter.model.FilterElement;
import com.acv.service.filter.model.FilteringRules;
import com.acv.service.filter.model.HotelFilterGroup;
import com.acv.service.filter.model.InterestFilterGroup;
import com.acv.service.filter.model.ResortFilterGroup;
import com.acv.service.i18n.I18nRetriever;

/**
 * The FilterManager interface implementation class.
 */
public class FilterManagerImpl extends BaseManager implements FilterManager {

    private I18nRetriever i18nRetriever;

    static List<String> languageCodesUpperCased = null;

    private List<String> getLanguageCodeListUpperCased() {
        if (languageCodesUpperCased == null) {
            List<String> languageCodes = getI18nRetriever().getLanguageCodeList();
            synchronized (FilterManagerImpl.class) {
                languageCodesUpperCased = new ArrayList<String>();
                for (String languageCode : languageCodes) {
                    languageCodesUpperCased.add(languageCode.toUpperCase());
                }
            }
        }
        return languageCodesUpperCased;
    }

    public FilteringRules generateFilteringRulesForInterest(Collection<Resort> resorts) {
        FilteringRules filteringRules = new FilteringRules();
        if (!resorts.isEmpty()) {
            filteringRules.setDestinationGroup(generateDestinationFilterGroup(resorts));
            for (FilterElement element : filteringRules.getDestinationGroup().getElements()) {
                if (filteringRules.getResortGroupMap().get(element.getId()) == null) {
                    filteringRules.getResortGroupMap().put(element.getId(), generateResortFeaturesFilterGroup(resorts, element.getId()));
                } else filteringRules.getResortGroupMap().put(element.getId(), updateResortFeaturesFilterGroup(resorts, filteringRules.getResortGroupMap().get(element.getId()), element.getId()));
            }
            for (FilterElement element : filteringRules.getDestinationGroup().getElements()) {
                if (filteringRules.getHotelGroupMap().get(element.getId()) == null) {
                    filteringRules.getHotelGroupMap().put(element.getId(), generateHotelFeaturesFilterGroup(resorts, element.getId()));
                } else filteringRules.getHotelGroupMap().put(element.getId(), updateHotelFeaturesFilterGroup(resorts, filteringRules.getHotelGroupMap().get(element.getId()), element.getId()));
            }
            filteringRules.setAccomodationGroup(generateAccomodationFilterGroup(resorts));
        }
        return filteringRules;
    }

    public FilteringRules generateFilteringRulesForDestination(Collection<Resort> resorts) {
        FilteringRules filteringRules = new FilteringRules();
        if (!resorts.isEmpty()) {
            String destId = resorts.iterator().next().getDestination().getId().toString();
            filteringRules.setInterestGroup(generateInterestFilterGroup(resorts));
            if (filteringRules.getResortGroupMap().get(destId) == null) {
                filteringRules.getResortGroupMap().put(destId, generateResortFeaturesFilterGroup(resorts, destId));
            } else filteringRules.getResortGroupMap().put(destId, updateResortFeaturesFilterGroup(resorts, filteringRules.getResortGroupMap().get(destId), destId));
            if (filteringRules.getHotelGroupMap().get(destId) == null) {
                filteringRules.getHotelGroupMap().put(destId, generateHotelFeaturesFilterGroup(resorts, destId));
            } else filteringRules.getHotelGroupMap().put(destId, updateHotelFeaturesFilterGroup(resorts, filteringRules.getHotelGroupMap().get(destId), destId));
            filteringRules.setAccomodationGroup(generateAccomodationFilterGroup(resorts));
        }
        return filteringRules;
    }

    public FilteringRules generateFilteringRulesForSearch(Collection<Resort> resorts) {
        FilteringRules filteringRules = new FilteringRules();
        if (!resorts.isEmpty()) {
            String destId = resorts.iterator().next().getDestination().getId().toString();
            filteringRules.setDestinationGroup(generateDestinationFilterGroup(resorts));
            filteringRules.setInterestGroup(generateInterestFilterGroup(resorts));
            if (filteringRules.getResortGroupMap().get(destId) == null) {
                filteringRules.getResortGroupMap().put(destId, generateResortFeaturesFilterGroup(resorts, destId));
            } else filteringRules.getResortGroupMap().put(destId, updateResortFeaturesFilterGroup(resorts, filteringRules.getResortGroupMap().get(destId), destId));
            if (filteringRules.getHotelGroupMap().get(destId) == null) {
                filteringRules.getHotelGroupMap().put(destId, generateHotelFeaturesFilterGroup(resorts, destId));
            } else filteringRules.getHotelGroupMap().put(destId, updateHotelFeaturesFilterGroup(resorts, filteringRules.getHotelGroupMap().get(destId), destId));
            filteringRules.setAccomodationGroup(generateAccomodationFilterGroup(resorts));
        }
        return filteringRules;
    }

    /**
	 * <p>
	 * This method generates a group of interest filters.
	 * </p>
	 * @param resorts
	 * @return <code>InterestFilterGroup</code>
	 */
    private InterestFilterGroup generateInterestFilterGroup(Collection<Resort> resorts) {
        InterestFilterGroup interestFilterGroup = new InterestFilterGroup();
        List<String> languageCodeListUpperCased = getLanguageCodeListUpperCased();
        HashMap<String, Interest> interests = new HashMap<String, Interest>();
        HashMap<String, Facility> facilities = new HashMap<String, Facility>();
        for (Resort resort : resorts) {
            for (Interest interest : resort.getInterests()) {
                interests.put(interest.getCode(), interest);
            }
            for (Facility facility : resort.getFacilities()) {
                facilities.put(facility.getCode(), facility);
            }
        }
        for (Interest interest : interests.values()) {
            FilterElement filterElement = new FilterElement();
            for (String languageCode : languageCodeListUpperCased) {
                filterElement.getDisplayName().put(languageCode, interest.getContent().get(languageCode).get("title"));
            }
            filterElement.setId(String.valueOf(interest.getId()));
            interestFilterGroup.getElements().add(filterElement);
        }
        for (Facility facility : facilities.values()) {
            if (interests.containsKey(facility.getCode())) continue;
            FilterElement filterElement = new FilterElement();
            for (String languageCode : languageCodeListUpperCased) {
                filterElement.getDisplayName().put(languageCode, facility.getContent().get(languageCode).get("title"));
            }
            filterElement.setId(String.valueOf(facility.getId()));
            interestFilterGroup.getElements().add(filterElement);
        }
        return interestFilterGroup;
    }

    /**
	 * <p>
	 * This method generates a group of destination filters.
	 * </p>
	 * @param resorts
	 * @return <code>DestinationFilterGroup</code>
	 */
    private DestinationFilterGroup generateDestinationFilterGroup(Collection<Resort> resorts) {
        DestinationFilterGroup destFilterGroup = new DestinationFilterGroup();
        List<String> languageCodeListUpperCased = getLanguageCodeListUpperCased();
        HashMap<String, Destination> destinations = new HashMap<String, Destination>();
        for (Resort resort : resorts) {
            Destination dest = resort.getDestination();
            if (dest != null) destinations.put(dest.getCode(), dest);
        }
        for (Destination destination : destinations.values()) {
            FilterElement filterElement = new FilterElement();
            for (String languageCode : languageCodeListUpperCased) {
                filterElement.getDisplayName().put(languageCode, destination.getContent().get(languageCode).get("title"));
            }
            filterElement.setResort(destination.isResort());
            filterElement.setId(String.valueOf(destination.getId()));
            destFilterGroup.getElements().add(filterElement);
        }
        return destFilterGroup;
    }

    /**
	 * <p>
	 * This method generates a group of resort features filters.
	 * </p>
	 * @param resorts
	 * @param destId
	 * @return <code>ResortFilterGroup</code>
	 */
    private ResortFilterGroup generateResortFeaturesFilterGroup(Collection<Resort> resorts, String destId) {
        ResortFilterGroup resortFeaturesfilterGroup = new ResortFilterGroup();
        List<String> languageCodeListUpperCased = getLanguageCodeListUpperCased();
        HashMap<String, Rating> resortRatings = new HashMap<String, Rating>();
        for (Resort resort : resorts) {
            Destination dest = resort.getDestination();
            if (dest != null) {
                if (!dest.getId().toString().equalsIgnoreCase(destId)) continue;
                boolean isResort = dest.isResort();
                for (RatingPOS ratingPOS : resort.getRatingPOS()) {
                    if (isResort) {
                        resortRatings.put(ratingPOS.getRating().getCode(), ratingPOS.getRating());
                    }
                }
            }
        }
        for (Rating rating : resortRatings.values()) {
            FilterElement filterElement = new FilterElement();
            for (String languageCode : languageCodeListUpperCased) {
                filterElement.getDisplayName().put(languageCode, rating.getContent().get(languageCode).get("title"));
            }
            filterElement.setId(String.valueOf(rating.getId()));
            resortFeaturesfilterGroup.getElements().add(filterElement);
        }
        return resortFeaturesfilterGroup;
    }

    /**
	 * <p>
	 * This method updates the resort features filter group.
	 * </p>
	 * @param resorts
	 * @param resortFilterGroup
	 * @param destId
	 * @return <code>ResortFilterGroup</code>
	 */
    private ResortFilterGroup updateResortFeaturesFilterGroup(Collection<Resort> resorts, ResortFilterGroup resortFilterGroup, String destId) {
        ResortFilterGroup resortFeaturesfilterGroup = resortFilterGroup;
        List<String> languageCodeListUpperCased = getLanguageCodeListUpperCased();
        HashMap<String, Rating> resortRatings = new HashMap<String, Rating>();
        for (Resort resort : resorts) {
            Destination dest = resort.getDestination();
            if (dest != null) {
                if (!dest.getId().toString().equalsIgnoreCase(destId)) continue;
                boolean isResort = dest.isResort();
                for (RatingPOS ratingPOS : resort.getRatingPOS()) {
                    if (isResort) {
                        for (FilterElement elem : resortFeaturesfilterGroup.getElements()) {
                            if (elem.getId().equalsIgnoreCase(ratingPOS.getRating().getId().toString())) continue;
                        }
                        resortRatings.put(ratingPOS.getRating().getCode(), ratingPOS.getRating());
                    }
                }
            }
        }
        for (Rating rating : resortRatings.values()) {
            FilterElement filterElement = new FilterElement();
            for (String languageCode : languageCodeListUpperCased) {
                filterElement.getDisplayName().put(languageCode, rating.getContent().get(languageCode).get("title"));
            }
            filterElement.setId(String.valueOf(rating.getId()));
            resortFeaturesfilterGroup.getElements().add(filterElement);
        }
        return resortFeaturesfilterGroup;
    }

    /**
	 * <p>
	 * This method generates a group of hotel features filters.
	 * </p>
	 * @param resorts
	 * @param destId
	 * @return <code>HotelFilterGroup</code>
	 */
    private HotelFilterGroup generateHotelFeaturesFilterGroup(Collection<Resort> resorts, String destId) {
        HotelFilterGroup hotelFeaturesfilterGroup = new HotelFilterGroup();
        List<String> languageCodeListUpperCased = getLanguageCodeListUpperCased();
        HashMap<String, Rating> hotelRatings = new HashMap<String, Rating>();
        for (Resort resort : resorts) {
            Destination dest = resort.getDestination();
            if (dest != null) {
                if (!dest.getId().toString().equalsIgnoreCase(destId)) continue;
                boolean isResort = dest.isResort();
                for (RatingPOS ratingPOS : resort.getRatingPOS()) {
                    if (!isResort) {
                        for (FilterElement elem : hotelFeaturesfilterGroup.getElements()) {
                            if (elem.getId().equalsIgnoreCase(ratingPOS.getRating().getId().toString())) continue;
                        }
                        hotelRatings.put(ratingPOS.getRating().getCode(), ratingPOS.getRating());
                    }
                }
            }
        }
        for (Rating rating : hotelRatings.values()) {
            FilterElement filterElement = new FilterElement();
            for (String languageCode : languageCodeListUpperCased) {
                filterElement.getDisplayName().put(languageCode, rating.getContent().get(languageCode).get("title"));
            }
            filterElement.setId(String.valueOf(rating.getId()));
            hotelFeaturesfilterGroup.getElements().add(filterElement);
        }
        return hotelFeaturesfilterGroup;
    }

    /**
	 * <p>
	 * This method updates the hotel features filter group.
	 * </p>
	 * @param resorts
	 * @param hotelFilterGroup
	 * @param destId
	 * @return <code>HotelFilterGroup</code>
	 */
    private HotelFilterGroup updateHotelFeaturesFilterGroup(Collection<Resort> resorts, HotelFilterGroup hotelFilterGroup, String destId) {
        HotelFilterGroup hotelFeaturesfilterGroup = hotelFilterGroup;
        List<String> languageCodeListUpperCased = getLanguageCodeListUpperCased();
        HashMap<String, Rating> hotelRatings = new HashMap<String, Rating>();
        for (Resort resort : resorts) {
            Destination dest = resort.getDestination();
            if (dest != null) {
                if (!dest.getId().toString().equalsIgnoreCase(destId)) continue;
                boolean isResort = dest.isResort();
                for (RatingPOS ratingPOS : resort.getRatingPOS()) {
                    if (!isResort) {
                        for (FilterElement elem : hotelFeaturesfilterGroup.getElements()) {
                            if (elem.getId().equalsIgnoreCase(ratingPOS.getRating().getId().toString())) continue;
                        }
                        hotelRatings.put(ratingPOS.getRating().getCode(), ratingPOS.getRating());
                    }
                }
            }
        }
        for (Rating rating : hotelRatings.values()) {
            FilterElement filterElement = new FilterElement();
            for (String languageCode : languageCodeListUpperCased) {
                filterElement.getDisplayName().put(languageCode, rating.getContent().get(languageCode).get("title"));
            }
            filterElement.setId(String.valueOf(rating.getId()));
            hotelFeaturesfilterGroup.getElements().add(filterElement);
        }
        return hotelFeaturesfilterGroup;
    }

    /**
	 * <p>
	 * This method generates a group of accomodation filters.
	 * </p>
	 * @param resorts
	 * @return <code>AccomodationFilterGroup</code>
	 */
    private AccomodationFilterGroup generateAccomodationFilterGroup(Collection<Resort> resorts) {
        AccomodationFilterGroup accomodationFilterGroup = new AccomodationFilterGroup();
        boolean hasAtLeastOneAllInclusiveResort = false;
        boolean hasAtLeastOneNonAllInclusiveResort = false;
        boolean showAllInclusive = false;
        for (Resort resort : resorts) {
            if (hasAtLeastOneAllInclusiveResort && hasAtLeastOneNonAllInclusiveResort) {
                showAllInclusive = true;
                break;
            }
            if (resort.getAllInclusive()) hasAtLeastOneAllInclusiveResort = true; else hasAtLeastOneNonAllInclusiveResort = true;
        }
        List<FilterElement> elements = new ArrayList<FilterElement>();
        FilterElement overallRating = new FilterElement();
        overallRating.setId(FilterElement.ACCOMODATION_OVERALL_ID);
        FilterElement priceRange = new FilterElement();
        priceRange.setId(FilterElement.ACCOMODATION_PRICE_RANGE_ID);
        FilterElement allInclusive = new FilterElement();
        allInclusive.setId(FilterElement.ACCOMODATION_ALL_INCLUSIVE_ID);
        FilterElement nbOfRooms = new FilterElement();
        nbOfRooms.setId(FilterElement.ACCOMODATION_NB_OF_ROOMS_ID);
        FilterElement proximityToAirport = new FilterElement();
        proximityToAirport.setId(FilterElement.ACCOMODATION_PROXIMITY_ID);
        elements.add(overallRating);
        elements.add(priceRange);
        elements.add(nbOfRooms);
        elements.add(proximityToAirport);
        if (showAllInclusive) elements.add(allInclusive);
        accomodationFilterGroup.setElements(elements);
        return accomodationFilterGroup;
    }

    public I18nRetriever getI18nRetriever() {
        return i18nRetriever;
    }

    public void setI18nRetriever(I18nRetriever retriever) {
        i18nRetriever = retriever;
    }
}
