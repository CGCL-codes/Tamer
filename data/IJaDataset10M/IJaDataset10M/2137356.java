package com.acv.webapp.action.forms.groupProfile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;
import com.acv.dao.catalog.locations.airports.model.Airport;
import com.acv.dao.catalog.locations.resorts.model.Resort;
import com.acv.dao.common.Constants;
import com.acv.dao.forms.model.GroupProfileForm;
import com.acv.dao.profiles.model.UserProfile;
import com.acv.dao.security.model.Audience;
import com.acv.dao.security.model.TravelAgency;
import com.acv.service.catalog.LocationManager;
import com.acv.service.i18n.model.I18nCacheableList;
import com.acv.service.profile.UserProfileManager;
import com.acv.webapp.action.bus.search.SearchRequestObject;
import com.acv.webapp.common.BaseAction;
import com.acv.webapp.util.Date;
import com.acv.webapp.util.PhoneNumberOneField;
import com.opensymphony.xwork2.ValidationAware;

public class GroupProfileFormAction extends BaseAction implements ValidationAware {

    private static final long serialVersionUID = 1L;

    private static final Logger log = Logger.getLogger(GroupProfileFormAction.class);

    private transient LocationManager locationManager;

    private transient UserProfileManager userProfileManager;

    protected GroupProfileForm groupProfileForm;

    protected PhoneNumberOneField groupProfileFormHomeTel;

    protected PhoneNumberOneField groupProfileFormFax;

    private List<String> hotelList;

    private SearchRequestObject seRequestObj;

    /**
	 * used to prepopulate the form with data from the SearchAction
	 */
    public void setSeRequestObj(SearchRequestObject seRequestObj) {
        this.seRequestObj = seRequestObj;
    }

    public SearchRequestObject getSeRequestObj() {
        return seRequestObj;
    }

    public String populate() {
        start();
        if (seRequestObj != null) {
            groupProfileForm.setDepartureTime(seRequestObj.getDepartureTime());
            groupProfileForm.setDepDate(seRequestObj.getDepartureDate());
            if (seRequestObj.getFlexibility() != null) {
                groupProfileForm.setFlexibleDates(getText("groupProfileForm.yesNoRadio" + (seRequestObj.getFlexibility().booleanValue() ? "1" : "2")));
            }
            groupProfileForm.setNumAdults(seRequestObj.getAdultsNum() + seRequestObj.getSeniorsNum());
            groupProfileForm.setNumChildren(seRequestObj.getInfantsNum() + seRequestObj.getChildrenNum());
            groupProfileForm.setRetDate(seRequestObj.getReturnDate());
            groupProfileForm.setReturningTime(seRequestObj.getReturnTime());
            Airport origin = getI18nRetriever().getAirportByCode(seRequestObj.getDepartureAirportCode());
            if (origin != null) {
                groupProfileForm.setOrigins(new ArrayList<String>());
                groupProfileForm.getOrigins().add(origin.getId().toString());
            }
            Airport destination = getI18nRetriever().getAirportByCode(seRequestObj.getDestinationAirportCode());
            if (destination != null) {
                groupProfileForm.setDestinations(new ArrayList<String>());
                groupProfileForm.getDestinations().add(destination.getId().toString());
            }
        } else {
            log.error("SearchRequestObject seRequestObj is null");
        }
        return SUCCESS;
    }

    @SuppressWarnings("static-access")
    public String start() {
        log.debug(">>>> GroupProfileFormAction - START");
        if (groupProfileForm == null) groupProfileForm = new GroupProfileForm();
        getPrePopulationManager().prePopulationDataMapping(groupProfileForm);
        groupProfileFormHomeTel = new PhoneNumberOneField(groupProfileForm.getHomeTel());
        groupProfileFormFax = new PhoneNumberOneField(groupProfileForm.getFax());
        setDefaultValue();
        try {
            UserProfile up = this.getUserProfileManager().getCurrentUser();
            if (up.isTravelAgency() || up.isTravelAgent()) {
                log.error(up + " audience:" + up.getAudience());
                Audience au = up.getAudience();
                if (au != null && au instanceof TravelAgency) {
                    TravelAgency ta = (TravelAgency) au;
                    groupProfileForm.setAgency(true);
                    groupProfileForm.setHomeTel(ta.getPhone());
                    groupProfileForm.setFax(ta.getFax());
                    groupProfileForm.setAgencyName(ta.getName());
                    if (ta.getBpno() != null) groupProfileForm.setBusinessPartnerNumber(ta.getBpno().toString());
                }
            }
        } catch (Exception e) {
            log.error(e);
        }
        return SUCCESS;
    }

    public String process() {
        if (groupProfileFormHomeTel != null && !"".equals(groupProfileFormHomeTel.getPhoneNumber())) groupProfileForm.setHomeTel(groupProfileFormHomeTel.getPhoneNumber().toString());
        if (groupProfileFormFax != null && !"".equals(groupProfileFormFax.getPhoneNumber())) groupProfileForm.setFax(groupProfileFormFax.getPhoneNumber().toString());
        sendEmail();
        saveMessage(getText("groupProfile.added"));
        return SUCCESS;
    }

    public String end() {
        log.debug(">>>> GroupProfileFormAction - END");
        return SUCCESS;
    }

    public void setDefaultValue() {
        if (groupProfileForm.getFirstName() != null) {
            groupProfileForm.setName(groupProfileForm.getFirstName() + " " + groupProfileForm.getLastName());
        }
        if (groupProfileForm.getPreferedLanguage() == null) {
            groupProfileForm.setPreferedLanguage(getLanguageList().get(getLocale().getLanguage()));
        }
        groupProfileForm.setFlexibleDates(getText("groupProfileForm.yesNoRadio1"));
        groupProfileForm.setBookedBefore(getText("groupProfileForm.yesNoRadio2"));
        groupProfileForm.setMeetingRequire(getText("groupProfileForm.yesNoRadio2"));
        groupProfileForm.setInsuranceRates(getText("groupProfileForm.insuranceRates3"));
    }

    private void sendEmail() {
        String to = groupProfileForm.getEmail();
        String subject = getText("groupProfile.subject");
        Map<String, String> model = new HashMap<String, String>();
        model.put("name", groupProfileForm.getName());
        model.put("email", groupProfileForm.getEmail());
        model.put("homeTel", groupProfileForm.getHomeTel());
        model.put("fax", groupProfileForm.getFax() != null ? groupProfileForm.getFax() : "");
        model.put("preferredLanguage", groupProfileForm.getPreferedLanguage());
        model.put("bookedBefore", groupProfileForm.getBookedBefore());
        model.put("tripType", groupProfileForm.getTripType());
        model.put("groupName", groupProfileForm.getGroupName());
        model.put("groupType", groupProfileForm.getGroupType());
        model.put("otherGroupType", groupProfileForm.getOtherGroupType() != null ? groupProfileForm.getOtherGroupType() : "");
        model.put("numAdults", Integer.toString(groupProfileForm.getNumAdults()));
        model.put("numChildren", Integer.toString(groupProfileForm.getNumChildren()));
        model.put("budget", groupProfileForm.getBudget());
        model.put("depDate", groupProfileForm.getDepDate().toString());
        if (getTimeList().containsKey(groupProfileForm.getDepartureTime())) {
            model.put("departureTime", getTimeList().get(groupProfileForm.getDepartureTime()));
        } else {
            model.put("departureTime", groupProfileForm.getDepartureTime());
        }
        model.put("flexibleDates", groupProfileForm.getFlexibleDates());
        model.put("retDate", groupProfileForm.getRetDate().toString());
        if (getTimeList().containsKey(groupProfileForm.getDepartureTime())) {
            model.put("returningDate", getTimeList().get(groupProfileForm.getReturningTime()));
        } else {
            model.put("returningDate", groupProfileForm.getReturningTime());
        }
        model.put("origin", getOriginCities());
        model.put("destination", getDestinationCities());
        model.put("hotelOne", groupProfileForm.getHotelOne());
        model.put("hotelTwo", groupProfileForm.getHotelTwo());
        model.put("hotelThree", groupProfileForm.getHotelThree());
        model.put("occupancyType", groupProfileForm.getOccupancyType() != null ? groupProfileForm.getOccupancyType() : "");
        model.put("roomCat", groupProfileForm.getRoomCat() != null ? groupProfileForm.getRoomCat() : "");
        model.put("otherRoomCat", groupProfileForm.getOtherRoomCat() != null ? groupProfileForm.getOtherRoomCat() : "");
        model.put("insuranceRates", groupProfileForm.getInsuranceRates() != null ? groupProfileForm.getInsuranceRates() : "");
        model.put("meetingRequire", groupProfileForm.getMeetingRequire());
        model.put("additionalComments", groupProfileForm.getAdditionalComments());
        model.put("FORM", "");
        String template = Constants.EMAIL_GROUP_FORM_TEMPLATE;
        if (groupProfileForm.isAgency()) {
            model.put("agencyName", groupProfileForm.getAgencyName());
            model.put("businessPartnerNumber", groupProfileForm.getBusinessPartnerNumber());
            template = Constants.EMAIL_AGENCY_GROUP_FORM_TEMPLATE;
        }
        sendEmail(to, template, model, null);
    }

    private String getOriginCities() {
        List<String> cities = new ArrayList<String>(groupProfileForm.getOrigins().size());
        for (String airportId : groupProfileForm.getOrigins()) {
            cities.add(getOriginList().get(new Long(airportId)));
        }
        return StringUtils.collectionToDelimitedString(cities, ", ");
    }

    private String getDestinationCities() {
        List<String> cities = new ArrayList<String>(groupProfileForm.getDestinations().size());
        for (String airportId : groupProfileForm.getDestinations()) {
            cities.add(getDestinationList().get(new Long(airportId)));
        }
        return StringUtils.collectionToDelimitedString(cities, ", ");
    }

    /**
	 * to provide the datasourse for time list in search value defined in
	 * ApplicationResources.properties
	 *
	 * @return Map
	 */
    public Map<String, String> getTimeList() {
        Map<String, String> timeList = new TreeMap<String, String>();
        timeList.put(Date.ANY.toString(), getText("search.anyOfTime"));
        timeList.put(Date.MORNING.toString(), getText("search.morningOfTime"));
        timeList.put(Date.AFTERNOON.toString(), getText("search.afternoonOfTime"));
        timeList.put(Date.EVENING.toString(), getText("search.eveningOfTime"));
        return timeList;
    }

    public I18nCacheableList getDestinationList() {
        return getI18nRetriever().getDestinationAirportList(getLangToUperCase());
    }

    public I18nCacheableList getOriginList() {
        return getI18nRetriever().getOriginAirportList(getLangToUperCase());
    }

    public List<String> getYesNoRadio() {
        return buildList("groupProfileForm", "yesNoRadio");
    }

    public List<String> getTripType() {
        return buildList("groupProfileForm", "tripTypes");
    }

    public List<String> getGroupType() {
        return buildList("groupProfileForm", "groupTypes");
    }

    public List<String> getBudget() {
        return buildList("groupProfileForm", "budgetRange");
    }

    public List<String> getOccupancyType() {
        return buildList("groupProfileForm", "occupancyTypes");
    }

    public List<String> getRoomCat() {
        return buildList("groupProfileForm", "roomCategories");
    }

    public List<String> getAroomCat() {
        return buildList("groupProfileForm", "aroomCategories");
    }

    public List<String> getInsuranceRates() {
        return buildList("groupProfileForm", "insuranceRates");
    }

    private List<String> getHotelsByAirport(String airportId) {
        List<Resort> resorts = locationManager.getResortsByAirport(airportId);
        List<String> result = new ArrayList<String>(resorts.size());
        for (Resort resort : resorts) {
            result.add(resort.getContent().get(getLocale().getLanguage().toUpperCase()).getTitle());
        }
        if (result.isEmpty()) log.warn("Hotel list is empty for destination with airportId: " + airportId);
        return result;
    }

    public List<String> getHotelList() {
        if (hotelList == null || hotelList.isEmpty()) {
            Collection<String> dests = groupProfileForm.getDestinations();
            if (dests != null && !dests.isEmpty() && !dests.iterator().next().equals("")) {
                hotelList = new ArrayList<String>();
                hotelList.add("");
                for (String dest : dests) {
                    hotelList.addAll(getHotelsByAirport(dest));
                }
            } else {
                hotelList = new ArrayList<String>(1);
                hotelList.add("");
            }
        }
        return hotelList;
    }

    public GroupProfileForm getGroupProfileForm() {
        return groupProfileForm;
    }

    public void setGroupProfileForm(GroupProfileForm groupProfileForm) {
        this.groupProfileForm = groupProfileForm;
    }

    public PhoneNumberOneField getGroupProfileFormHomeTel() {
        return groupProfileFormHomeTel;
    }

    public void setGroupProfileFormHomeTel(PhoneNumberOneField groupProfileFormHomeTel) {
        this.groupProfileFormHomeTel = groupProfileFormHomeTel;
    }

    public PhoneNumberOneField getGroupProfileFormFax() {
        return groupProfileFormFax;
    }

    public void setGroupProfileFormFax(PhoneNumberOneField groupProfileFormFax) {
        this.groupProfileFormFax = groupProfileFormFax;
    }

    public I18nCacheableList getLanguageList() {
        return getI18nRetriever().getLanguageList(getLocale().getLanguage());
    }

    public void setLocationManager(LocationManager locationManager) {
        this.locationManager = locationManager;
    }

    public UserProfileManager getUserProfileManager() {
        return userProfileManager;
    }

    public void setUserProfileManager(UserProfileManager userProfileManager) {
        this.userProfileManager = userProfileManager;
    }
}
