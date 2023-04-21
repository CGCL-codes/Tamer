package de.cue4net.eventservice.web.controller;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import de.cue4net.eventservice.core.SecurityUtils;
import de.cue4net.eventservice.dao.UserAccountManager;
import de.cue4net.eventservice.dao.event.EventDAO;
import de.cue4net.eventservice.dao.event.EventPriceDiscountDAO;
import de.cue4net.eventservice.dao.event.EventTypeDAO;
import de.cue4net.eventservice.dao.location.LocationDAO;
import de.cue4net.eventservice.dao.locationservice.LocationServiceDAO;
import de.cue4net.eventservice.dao.locationservice.LocationServicePriceDiscountDAO;
import de.cue4net.eventservice.dao.locationservice.LocationServiceTypeDAO;
import de.cue4net.eventservice.dao.registration.DiscountCategoryDAO;
import de.cue4net.eventservice.model.event.Event;
import de.cue4net.eventservice.model.event.EventPriceDiscount;
import de.cue4net.eventservice.model.event.EventType;
import de.cue4net.eventservice.model.location.Location;
import de.cue4net.eventservice.model.location.locationservice.LocationService;
import de.cue4net.eventservice.model.location.locationservice.LocationServicePriceDiscount;
import de.cue4net.eventservice.model.registration.DiscountCategory;
import de.cue4net.eventservice.model.shared.Address;
import de.cue4net.eventservice.model.shared.Email;
import de.cue4net.eventservice.model.shared.MonetaryAmount;
import de.cue4net.eventservice.model.shared.Phone;
import de.cue4net.eventservice.model.user.User;

/**
 * @author Keino Uelze - cue4net
 * @version $Id: AdminHelperFunctionController.java,v 1.17 2008-06-05 11:00:08 keino Exp $
 */
public class AdminHelperFunctionController extends SimpleFormController implements InitializingBean {

    /** Logger for this class and subclasses */
    protected final Log logger = LogFactory.getLog(getClass());

    private LocationDAO locationDAO;

    private EventDAO eventDAO;

    private UserAccountManager userAccountManager;

    private EventTypeDAO eventTypeDAO;

    private DiscountCategoryDAO discountCategoryDAO;

    private EventPriceDiscountDAO eventPriceDiscountDAO;

    private LocationServiceDAO locationServiceDAO;

    private LocationServicePriceDiscountDAO locationServicePriceDiscountDAO;

    private LocationServiceTypeDAO locationServiceTypeDAO;

    private Location location;

    private User user;

    private Date now;

    @Override
    public void afterPropertiesSet() throws Exception {
        user = SecurityUtils.getSystemUser(userAccountManager);
        now = new Date();
    }

    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors) throws Exception {
        return new ModelAndView();
    }

    protected Map<String, Boolean> referenceData(HttpServletRequest request) {
        boolean feedback = true;
        String parameter = request.getParameter("do");
        Map<String, Boolean> map = new Hashtable<String, Boolean>();
        if ("KG".equals(parameter)) {
            map.put("locationCreation", createKGDiscountsLocationservicesPricesAndEvents());
        } else {
            feedback = false;
        }
        map.put("feedback", feedback);
        return map;
    }

    private boolean createKGLocation() {
        boolean returnValue = true;
        location = new Location();
        location.setCode("KGN");
        location.setName("Karma Guen");
        location.setDescription("Karma Kagyü retreat center in south spain.");
        Address address = new Address();
        address.setStreet("Aldea Alta, Apt. Correos 179");
        address.setCity("Vélez-Málaga");
        address.setZipCode("29700");
        address.setCountry("Spain");
        location.setAddress(address);
        Phone phone = new Phone();
        phone.setPhone("+34.952.115197");
        phone.setMobile("");
        phone.setFax("+34.952.115197");
        location.setPhone(phone);
        Email email = new Email();
        email.setEmailAddress("karmaguen@diamondway-center.org");
        location.setEmail(email);
        location.setCreatedByUser(user);
        location.setModifiedByUser(user);
        Date now = new Date();
        location.setCreateDate(now);
        location.setModifiedDate(now);
        returnValue &= locationDAO.createOrUpdateLocation(location);
        return returnValue;
    }

    private boolean createKGDiscountsLocationservicesPricesAndEvents() {
        boolean returnValue = true;
        location = locationDAO.getLocationByID(14793L);
        DiscountCategory discountCategoryEarlyPayment = new DiscountCategory();
        discountCategoryEarlyPayment.setLocation(location);
        discountCategoryEarlyPayment.setPercentageDiscount(25.00D);
        discountCategoryEarlyPayment.setCode("earlyPayment");
        discountCategoryEarlyPayment.setName("early Payment");
        discountCategoryEarlyPayment.setDescription("Discount for early payment.");
        discountCategoryEarlyPayment.setCreateDate(now);
        discountCategoryEarlyPayment.setModifiedDate(now);
        discountCategoryEarlyPayment.setCreatedByUser(user);
        discountCategoryEarlyPayment.setModifiedByUser(user);
        returnValue &= discountCategoryDAO.createOrUpdateDiscountCategory(discountCategoryEarlyPayment);
        DiscountCategory discountCategorySupporters = new DiscountCategory();
        discountCategorySupporters.setLocation(location);
        discountCategorySupporters.setPercentageDiscount(25.00D);
        discountCategorySupporters.setCode("supporters");
        discountCategorySupporters.setName("Supporters");
        discountCategorySupporters.setDescription("Discount for supporters.");
        discountCategorySupporters.setCreateDate(now);
        discountCategorySupporters.setModifiedDate(now);
        discountCategorySupporters.setCreatedByUser(user);
        discountCategorySupporters.setModifiedByUser(user);
        returnValue &= discountCategoryDAO.createOrUpdateDiscountCategory(discountCategorySupporters);
        DiscountCategory discountCategoryLaeer = new DiscountCategory();
        discountCategoryLaeer.setLocation(location);
        discountCategoryLaeer.setPercentageDiscount(25.00D);
        discountCategoryLaeer.setCode("laeer");
        discountCategoryLaeer.setName("Lat. America, East Europe, Russia");
        discountCategoryLaeer.setDescription("Discount for Latin America, East Europe and Russia.");
        discountCategoryLaeer.setCreateDate(now);
        discountCategoryLaeer.setModifiedDate(now);
        discountCategoryLaeer.setCreatedByUser(user);
        discountCategoryLaeer.setModifiedByUser(user);
        returnValue &= discountCategoryDAO.createOrUpdateDiscountCategory(discountCategoryLaeer);
        DiscountCategory discountCategoryChildren = new DiscountCategory();
        discountCategoryChildren.setLocation(location);
        discountCategoryChildren.setPercentageDiscount(50.00D);
        discountCategoryChildren.setCode("kd4-14");
        discountCategoryChildren.setName("Children 4-14 years");
        discountCategoryChildren.setDescription("Discount for children between the age of 4 and 14.");
        discountCategoryChildren.setCreateDate(now);
        discountCategoryChildren.setModifiedDate(now);
        discountCategoryChildren.setCreatedByUser(user);
        discountCategoryChildren.setModifiedByUser(user);
        returnValue &= discountCategoryDAO.createOrUpdateDiscountCategory(discountCategoryChildren);
        DiscountCategory discountCategory2ndParent = new DiscountCategory();
        discountCategory2ndParent.setLocation(location);
        discountCategory2ndParent.setPercentageDiscount(0.00D);
        discountCategory2ndParent.setCode("2ndParent");
        discountCategory2ndParent.setName("Second Parent");
        discountCategory2ndParent.setDescription("Discount for the 2nd parent.");
        discountCategory2ndParent.setCreateDate(now);
        discountCategory2ndParent.setModifiedDate(now);
        discountCategory2ndParent.setCreatedByUser(user);
        discountCategory2ndParent.setModifiedByUser(user);
        returnValue &= discountCategoryDAO.createOrUpdateDiscountCategory(discountCategory2ndParent);
        LocationService locationServiceFaA = new LocationService();
        locationServiceFaA.setCode("FaA_KG");
        locationServiceFaA.setName("Food and Accommodation");
        locationServiceFaA.setDescription("Food and Accommodation");
        locationServiceFaA.setLocation(location);
        locationServiceFaA.setCountableUnit(LocationService.DAY_BASED_UNIT);
        locationServiceFaA.setAmountUnitsPerPackage(1.0D);
        locationServiceFaA.setImpliciteService(true);
        MonetaryAmount servicePrice = new MonetaryAmount();
        servicePrice.setCurrency(Currency.getInstance("EUR"));
        servicePrice.setValue(new BigDecimal("18.0"));
        locationServiceFaA.setServicePrice(servicePrice);
        locationServiceFaA.setCreateDate(now);
        locationServiceFaA.setModifiedDate(now);
        locationServiceFaA.setCreatedByUser(user);
        locationServiceFaA.setModifiedByUser(user);
        locationServiceFaA.setLocationServiceType(locationServiceTypeDAO.getLocationServiceTypeByName("Full Board Residential"));
        locationServiceDAO.createOrUpdateLocationService(locationServiceFaA);
        LocationService locationServiceAirpPickUp = new LocationService();
        locationServiceAirpPickUp.setCode("Airp_Pickup_KG");
        locationServiceAirpPickUp.setName("Airport Pickup");
        locationServiceAirpPickUp.setDescription("Airport Pickup");
        locationServiceAirpPickUp.setLocation(location);
        locationServiceAirpPickUp.setCountableUnit(LocationService.PIECE_BASED_UNIT);
        locationServiceAirpPickUp.setAmountUnitsPerPackage(1.0D);
        locationServiceAirpPickUp.setImpliciteService(false);
        servicePrice.setCurrency(Currency.getInstance("EUR"));
        servicePrice.setValue(new BigDecimal("20.0"));
        locationServiceAirpPickUp.setServicePrice(servicePrice);
        locationServiceAirpPickUp.setCreateDate(now);
        locationServiceAirpPickUp.setModifiedDate(now);
        locationServiceAirpPickUp.setCreatedByUser(user);
        locationServiceAirpPickUp.setModifiedByUser(user);
        locationServiceAirpPickUp.setLocationServiceType(locationServiceTypeDAO.getLocationServiceTypeByID(0L));
        locationServiceDAO.createOrUpdateLocationService(locationServiceAirpPickUp);
        LocationService locationServiceAirpBringBack = new LocationService();
        locationServiceAirpBringBack.setCode("Airp_Bringback_KG");
        locationServiceAirpBringBack.setName("Airport Bringback");
        locationServiceAirpBringBack.setDescription("Airport Bringback");
        locationServiceAirpBringBack.setLocation(location);
        locationServiceAirpBringBack.setCountableUnit(LocationService.PIECE_BASED_UNIT);
        locationServiceAirpBringBack.setAmountUnitsPerPackage(1.0D);
        locationServiceAirpBringBack.setImpliciteService(false);
        servicePrice.setCurrency(Currency.getInstance("EUR"));
        servicePrice.setValue(new BigDecimal("20.0"));
        locationServiceAirpBringBack.setServicePrice(servicePrice);
        locationServiceAirpBringBack.setCreateDate(now);
        locationServiceAirpBringBack.setModifiedDate(now);
        locationServiceAirpBringBack.setCreatedByUser(user);
        locationServiceAirpBringBack.setModifiedByUser(user);
        locationServiceAirpBringBack.setLocationServiceType(locationServiceTypeDAO.getLocationServiceTypeByID(0L));
        locationServiceDAO.createOrUpdateLocationService(locationServiceAirpBringBack);
        LocationServicePriceDiscount locationServicePriceDiscountEarlyPayment = new LocationServicePriceDiscount();
        locationServicePriceDiscountEarlyPayment.setDiscountCategory(discountCategoryEarlyPayment);
        locationServicePriceDiscountEarlyPayment.setLocationService(locationServiceFaA);
        servicePrice.setCurrency(Currency.getInstance("EUR"));
        servicePrice.setValue(new BigDecimal("18.0"));
        locationServicePriceDiscountEarlyPayment.setServicePrice(servicePrice);
        locationServicePriceDiscountDAO.createOrUpdateLocationServicePriceDiscount(locationServicePriceDiscountEarlyPayment);
        LocationServicePriceDiscount locationServicePriceDiscountSupporters = new LocationServicePriceDiscount();
        locationServicePriceDiscountSupporters.setDiscountCategory(discountCategorySupporters);
        locationServicePriceDiscountSupporters.setLocationService(locationServiceFaA);
        servicePrice.setCurrency(Currency.getInstance("EUR"));
        servicePrice.setValue(new BigDecimal("15.0"));
        locationServicePriceDiscountSupporters.setServicePrice(servicePrice);
        locationServicePriceDiscountDAO.createOrUpdateLocationServicePriceDiscount(locationServicePriceDiscountSupporters);
        LocationServicePriceDiscount locationServicePriceDiscountLaeer = new LocationServicePriceDiscount();
        locationServicePriceDiscountLaeer.setDiscountCategory(discountCategoryLaeer);
        locationServicePriceDiscountLaeer.setLocationService(locationServiceFaA);
        servicePrice.setCurrency(Currency.getInstance("EUR"));
        servicePrice.setValue(new BigDecimal("15.0"));
        locationServicePriceDiscountLaeer.setServicePrice(servicePrice);
        locationServicePriceDiscountDAO.createOrUpdateLocationServicePriceDiscount(locationServicePriceDiscountLaeer);
        LocationServicePriceDiscount locationServicePriceDiscountChildren = new LocationServicePriceDiscount();
        locationServicePriceDiscountChildren.setDiscountCategory(discountCategoryChildren);
        locationServicePriceDiscountChildren.setLocationService(locationServiceFaA);
        servicePrice.setCurrency(Currency.getInstance("EUR"));
        servicePrice.setValue(new BigDecimal("9.0"));
        locationServicePriceDiscountChildren.setServicePrice(servicePrice);
        locationServicePriceDiscountDAO.createOrUpdateLocationServicePriceDiscount(locationServicePriceDiscountChildren);
        LocationServicePriceDiscount locationServicePriceDiscount2ndParent = new LocationServicePriceDiscount();
        locationServicePriceDiscount2ndParent.setDiscountCategory(discountCategory2ndParent);
        locationServicePriceDiscount2ndParent.setLocationService(locationServiceFaA);
        servicePrice.setCurrency(Currency.getInstance("EUR"));
        servicePrice.setValue(new BigDecimal("18.0"));
        locationServicePriceDiscount2ndParent.setServicePrice(servicePrice);
        locationServicePriceDiscountDAO.createOrUpdateLocationServicePriceDiscount(locationServicePriceDiscount2ndParent);
        locationServicePriceDiscountEarlyPayment = new LocationServicePriceDiscount();
        locationServicePriceDiscountEarlyPayment.setDiscountCategory(discountCategoryEarlyPayment);
        locationServicePriceDiscountEarlyPayment.setLocationService(locationServiceAirpPickUp);
        servicePrice.setCurrency(Currency.getInstance("EUR"));
        servicePrice.setValue(new BigDecimal("20.0"));
        locationServicePriceDiscountEarlyPayment.setServicePrice(servicePrice);
        locationServicePriceDiscountDAO.createOrUpdateLocationServicePriceDiscount(locationServicePriceDiscountEarlyPayment);
        locationServicePriceDiscountSupporters = new LocationServicePriceDiscount();
        locationServicePriceDiscountSupporters.setDiscountCategory(discountCategorySupporters);
        locationServicePriceDiscountSupporters.setLocationService(locationServiceAirpPickUp);
        servicePrice.setCurrency(Currency.getInstance("EUR"));
        servicePrice.setValue(new BigDecimal("20.0"));
        locationServicePriceDiscountSupporters.setServicePrice(servicePrice);
        locationServicePriceDiscountDAO.createOrUpdateLocationServicePriceDiscount(locationServicePriceDiscountSupporters);
        locationServicePriceDiscountLaeer = new LocationServicePriceDiscount();
        locationServicePriceDiscountLaeer.setDiscountCategory(discountCategoryLaeer);
        locationServicePriceDiscountLaeer.setLocationService(locationServiceAirpPickUp);
        servicePrice.setCurrency(Currency.getInstance("EUR"));
        servicePrice.setValue(new BigDecimal("20.0"));
        locationServicePriceDiscountLaeer.setServicePrice(servicePrice);
        locationServicePriceDiscountDAO.createOrUpdateLocationServicePriceDiscount(locationServicePriceDiscountLaeer);
        locationServicePriceDiscountChildren = new LocationServicePriceDiscount();
        locationServicePriceDiscountChildren.setDiscountCategory(discountCategoryChildren);
        locationServicePriceDiscountChildren.setLocationService(locationServiceAirpPickUp);
        servicePrice.setCurrency(Currency.getInstance("EUR"));
        servicePrice.setValue(new BigDecimal("20.0"));
        locationServicePriceDiscountChildren.setServicePrice(servicePrice);
        locationServicePriceDiscountDAO.createOrUpdateLocationServicePriceDiscount(locationServicePriceDiscountChildren);
        locationServicePriceDiscount2ndParent = new LocationServicePriceDiscount();
        locationServicePriceDiscount2ndParent.setDiscountCategory(discountCategory2ndParent);
        locationServicePriceDiscount2ndParent.setLocationService(locationServiceAirpPickUp);
        servicePrice.setCurrency(Currency.getInstance("EUR"));
        servicePrice.setValue(new BigDecimal("20.0"));
        locationServicePriceDiscount2ndParent.setServicePrice(servicePrice);
        locationServicePriceDiscountDAO.createOrUpdateLocationServicePriceDiscount(locationServicePriceDiscount2ndParent);
        locationServicePriceDiscountEarlyPayment = new LocationServicePriceDiscount();
        locationServicePriceDiscountEarlyPayment.setDiscountCategory(discountCategoryEarlyPayment);
        locationServicePriceDiscountEarlyPayment.setLocationService(locationServiceAirpBringBack);
        servicePrice.setCurrency(Currency.getInstance("EUR"));
        servicePrice.setValue(new BigDecimal("20.0"));
        locationServicePriceDiscountEarlyPayment.setServicePrice(servicePrice);
        locationServicePriceDiscountDAO.createOrUpdateLocationServicePriceDiscount(locationServicePriceDiscountEarlyPayment);
        locationServicePriceDiscountSupporters = new LocationServicePriceDiscount();
        locationServicePriceDiscountSupporters.setDiscountCategory(discountCategorySupporters);
        locationServicePriceDiscountSupporters.setLocationService(locationServiceAirpBringBack);
        servicePrice.setCurrency(Currency.getInstance("EUR"));
        servicePrice.setValue(new BigDecimal("20.0"));
        locationServicePriceDiscountSupporters.setServicePrice(servicePrice);
        locationServicePriceDiscountDAO.createOrUpdateLocationServicePriceDiscount(locationServicePriceDiscountSupporters);
        locationServicePriceDiscountLaeer = new LocationServicePriceDiscount();
        locationServicePriceDiscountLaeer.setDiscountCategory(discountCategoryLaeer);
        locationServicePriceDiscountLaeer.setLocationService(locationServiceAirpBringBack);
        servicePrice.setCurrency(Currency.getInstance("EUR"));
        servicePrice.setValue(new BigDecimal("20.0"));
        locationServicePriceDiscountLaeer.setServicePrice(servicePrice);
        locationServicePriceDiscountDAO.createOrUpdateLocationServicePriceDiscount(locationServicePriceDiscountLaeer);
        locationServicePriceDiscountChildren = new LocationServicePriceDiscount();
        locationServicePriceDiscountChildren.setDiscountCategory(discountCategoryChildren);
        locationServicePriceDiscountChildren.setLocationService(locationServiceAirpBringBack);
        servicePrice.setCurrency(Currency.getInstance("EUR"));
        servicePrice.setValue(new BigDecimal("20.0"));
        locationServicePriceDiscountChildren.setServicePrice(servicePrice);
        locationServicePriceDiscountDAO.createOrUpdateLocationServicePriceDiscount(locationServicePriceDiscountChildren);
        locationServicePriceDiscount2ndParent = new LocationServicePriceDiscount();
        locationServicePriceDiscount2ndParent.setDiscountCategory(discountCategory2ndParent);
        locationServicePriceDiscount2ndParent.setLocationService(locationServiceAirpBringBack);
        servicePrice.setCurrency(Currency.getInstance("EUR"));
        servicePrice.setValue(new BigDecimal("20.0"));
        locationServicePriceDiscount2ndParent.setServicePrice(servicePrice);
        locationServicePriceDiscountDAO.createOrUpdateLocationServicePriceDiscount(locationServicePriceDiscount2ndParent);
        EventType eventTypeAbstract = eventTypeDAO.getEventTypeByID(1L);
        Event eventAbstractMeta = new Event();
        eventAbstractMeta.setCreateDate(now);
        eventAbstractMeta.setModifiedDate(now);
        eventAbstractMeta.setCreatedByUser(user);
        eventAbstractMeta.setModifiedByUser(user);
        eventAbstractMeta.setLocation(location);
        eventAbstractMeta.setEventType(eventTypeAbstract);
        eventAbstractMeta.setDescription("");
        MonetaryAmount eventPrice = new MonetaryAmount();
        eventPrice.setCurrency(Currency.getInstance("EUR"));
        eventPrice.setValue(new BigDecimal(0.0));
        eventAbstractMeta.setEventPrice(eventPrice);
        eventAbstractMeta.setCode("KGN2007");
        eventAbstractMeta.setName("Meta Event KGN2007");
        Calendar eventStart = Calendar.getInstance();
        Calendar eventEnd = Calendar.getInstance();
        eventStart.set(2007, 4, 24, 16, 0, 0);
        eventEnd.set(2007, 5, 6, 23, 59, 0);
        eventAbstractMeta.setEventStart(eventStart.getTime());
        eventAbstractMeta.setEventEnd(eventEnd.getTime());
        eventAbstractMeta.setParentEvent(eventDAO.getEventByID(0L));
        returnValue &= eventDAO.createOrUpdateEvent(eventAbstractMeta);
        EventType eventType = eventTypeDAO.getEventTypeByID(14449L);
        eventStart.set(2007, 4, 24, 16, 0, 0);
        eventEnd.set(2007, 4, 24, 20, 0, 0);
        eventCreationHelperKGNClassA(eventType, eventStart.getTime(), eventEnd.getTime(), "KGN07-MK", "Mahakala Empowerment", 20.0D, eventAbstractMeta);
        eventStart.set(2007, 4, 25, 9, 0, 0);
        eventEnd.set(2007, 4, 25, 23, 0, 0);
        eventCreationHelperKGNClassA(eventType, eventStart.getTime(), eventEnd.getTime(), "KGN07-PH1/5", "Phowa day 1", 20.0D, eventAbstractMeta);
        eventStart.set(2007, 4, 26, 9, 0, 0);
        eventEnd.set(2007, 4, 26, 23, 0, 0);
        eventCreationHelperKGNClassA(eventType, eventStart.getTime(), eventEnd.getTime(), "KGN07-PH2/5", "Phowa day 2", 20.0D, eventAbstractMeta);
        eventStart.set(2007, 4, 27, 9, 0, 0);
        eventEnd.set(2007, 4, 27, 23, 0, 0);
        eventCreationHelperKGNClassA(eventType, eventStart.getTime(), eventEnd.getTime(), "KGN07-PH3/5", "Phowa day 3", 20.0D, eventAbstractMeta);
        eventStart.set(2007, 4, 28, 9, 0, 0);
        eventEnd.set(2007, 4, 28, 23, 0, 0);
        eventCreationHelperKGNClassA(eventType, eventStart.getTime(), eventEnd.getTime(), "KGN07-PH4/5", "Phowa day 4", 20.0D, eventAbstractMeta);
        eventStart.set(2007, 4, 29, 9, 0, 0);
        eventEnd.set(2007, 4, 29, 23, 0, 0);
        eventCreationHelperKGNClassA(eventType, eventStart.getTime(), eventEnd.getTime(), "KGN07-PH5/5", "Phowa day 5", 20.0D, eventAbstractMeta);
        eventStart.set(2007, 4, 30, 10, 0, 0);
        eventEnd.set(2007, 4, 30, 16, 0, 0);
        eventCreationHelperKGNClassA(eventType, eventStart.getTime(), eventEnd.getTime(), "KGN07-TA1/2", "Tara Empowerment day1", 20.0D, eventAbstractMeta);
        eventStart.set(2007, 4, 31, 10, 0, 0);
        eventEnd.set(2007, 4, 31, 16, 0, 0);
        eventCreationHelperKGNClassA(eventType, eventStart.getTime(), eventEnd.getTime(), "KGN07-TA2/2", "Tara Empowerment day2", 20.0D, eventAbstractMeta);
        eventStart.set(2007, 5, 1, 10, 0, 0);
        eventEnd.set(2007, 5, 1, 16, 0, 0);
        eventCreationHelperKGNClassA(eventType, eventStart.getTime(), eventEnd.getTime(), "KGN07-K2", "Second Karmapa Empowerment", 20.0D, eventAbstractMeta);
        eventStart.set(2007, 5, 2, 10, 0, 0);
        eventEnd.set(2007, 5, 2, 16, 0, 0);
        eventCreationHelperKGNClassA(eventType, eventStart.getTime(), eventEnd.getTime(), "KGN07-DK", "Duka Empowerment", 20.0D, eventAbstractMeta);
        eventStart.set(2007, 5, 4, 10, 0, 0);
        eventEnd.set(2007, 5, 4, 16, 0, 0);
        eventCreationHelperKGNClassA(eventType, eventStart.getTime(), eventEnd.getTime(), "KGN07-TK", "Techings from Karmapa", 20.0D, eventAbstractMeta);
        eventStart.set(2007, 5, 3, 10, 0, 0);
        eventEnd.set(2007, 5, 3, 15, 0, 0);
        eventCreationHelperKGNBenalmadena(eventType, eventStart.getTime(), eventEnd.getTime(), "KGN07-BNOLLE", "Benalmadena Ole Lecture", 10.0D, eventAbstractMeta);
        eventStart.set(2007, 5, 3, 16, 0, 0);
        eventEnd.set(2007, 5, 3, 18, 0, 0);
        eventCreationHelperKGNBenalmadena(eventType, eventStart.getTime(), eventEnd.getTime(), "KGN07-BNDMEM", "Benalmadena Diamond Mind Empowerment", 10.0D, eventAbstractMeta);
        eventStart.set(2007, 5, 5, 10, 0, 0);
        eventEnd.set(2007, 5, 5, 16, 0, 0);
        eventCreationHelperKGNClassB(eventType, eventStart.getTime(), eventEnd.getTime(), "KGN07-IT1/2", "ITAS day1", 7.0D, eventAbstractMeta);
        eventStart.set(2007, 5, 6, 10, 0, 0);
        eventEnd.set(2007, 5, 6, 16, 0, 0);
        eventCreationHelperKGNClassB(eventType, eventStart.getTime(), eventEnd.getTime(), "KGN07-IT2/2", "ITAS day2", 7.0D, eventAbstractMeta);
        return returnValue;
    }

    private boolean eventCreationHelperKGNClassA(EventType eventType, Date eventStart, Date eventEnd, String code, String name, Double price, Event parentEvent) {
        boolean returnValue = true;
        DiscountCategory discountCategory = null;
        Event event = new Event();
        event.setCreateDate(now);
        event.setModifiedDate(now);
        event.setCreatedByUser(user);
        event.setModifiedByUser(user);
        event.setLocation(location);
        event.setEventType(eventType);
        event.setDescription("");
        MonetaryAmount eventPrice = new MonetaryAmount();
        eventPrice.setCurrency(Currency.getInstance("EUR"));
        eventPrice.setValue(new BigDecimal(price));
        event.setEventPrice(eventPrice);
        event.setCode(code);
        event.setName(name);
        event.setEventStart(eventStart);
        event.setEventEnd(eventEnd);
        event.setParentEvent(parentEvent);
        returnValue &= eventDAO.createOrUpdateEvent(event);
        EventPriceDiscount eventPriceDiscount = new EventPriceDiscount();
        eventPriceDiscount.setEvent(event);
        discountCategory = discountCategoryDAO.getDiscountCategoryByCodeForLocation("earlyPayment", location);
        eventPriceDiscount.setDiscountCategory(discountCategory);
        eventPrice.setCurrency(Currency.getInstance("EUR"));
        eventPrice.setValue(new BigDecimal(14.0D));
        eventPriceDiscount.setEventPrice(eventPrice);
        returnValue &= eventPriceDiscountDAO.createOrUpdateEventPriceDiscount(eventPriceDiscount);
        eventPriceDiscount.setEvent(event);
        discountCategory = discountCategoryDAO.getDiscountCategoryByCodeForLocation("supporters", location);
        eventPriceDiscount.setDiscountCategory(discountCategory);
        eventPrice.setCurrency(Currency.getInstance("EUR"));
        eventPrice.setValue(new BigDecimal(10.0D));
        eventPriceDiscount.setEventPrice(eventPrice);
        returnValue &= eventPriceDiscountDAO.createOrUpdateEventPriceDiscount(eventPriceDiscount);
        eventPriceDiscount.setEvent(event);
        discountCategory = discountCategoryDAO.getDiscountCategoryByCodeForLocation("laeer", location);
        eventPriceDiscount.setDiscountCategory(discountCategory);
        eventPrice.setCurrency(Currency.getInstance("EUR"));
        eventPrice.setValue(new BigDecimal(10.0D));
        eventPriceDiscount.setEventPrice(eventPrice);
        returnValue &= eventPriceDiscountDAO.createOrUpdateEventPriceDiscount(eventPriceDiscount);
        eventPriceDiscount.setEvent(event);
        discountCategory = discountCategoryDAO.getDiscountCategoryByCodeForLocation("kd4-14", location);
        eventPriceDiscount.setDiscountCategory(discountCategory);
        eventPrice.setCurrency(Currency.getInstance("EUR"));
        eventPrice.setValue(new BigDecimal(9.0D));
        eventPriceDiscount.setEventPrice(eventPrice);
        returnValue &= eventPriceDiscountDAO.createOrUpdateEventPriceDiscount(eventPriceDiscount);
        eventPriceDiscount.setEvent(event);
        discountCategory = discountCategoryDAO.getDiscountCategoryByCodeForLocation("2ndParent", location);
        eventPriceDiscount.setDiscountCategory(discountCategory);
        eventPrice.setCurrency(Currency.getInstance("EUR"));
        eventPrice.setValue(new BigDecimal(0.0D));
        eventPriceDiscount.setEventPrice(eventPrice);
        returnValue &= eventPriceDiscountDAO.createOrUpdateEventPriceDiscount(eventPriceDiscount);
        return returnValue;
    }

    private boolean eventCreationHelperKGNClassB(EventType eventType, Date eventStart, Date eventEnd, String code, String name, Double price, Event parentEvent) {
        boolean returnValue = true;
        DiscountCategory discountCategory = null;
        Event event = new Event();
        event.setCreateDate(now);
        event.setModifiedDate(now);
        event.setCreatedByUser(user);
        event.setModifiedByUser(user);
        event.setLocation(location);
        event.setEventType(eventType);
        event.setDescription("");
        MonetaryAmount eventPrice = new MonetaryAmount();
        eventPrice.setCurrency(Currency.getInstance("EUR"));
        eventPrice.setValue(new BigDecimal(price));
        event.setEventPrice(eventPrice);
        event.setCode(code);
        event.setName(name);
        event.setEventStart(eventStart);
        event.setEventEnd(eventEnd);
        event.setParentEvent(parentEvent);
        returnValue &= eventDAO.createOrUpdateEvent(event);
        EventPriceDiscount eventPriceDiscount = new EventPriceDiscount();
        eventPriceDiscount.setEvent(event);
        discountCategory = discountCategoryDAO.getDiscountCategoryByCodeForLocation("earlyPayment", location);
        eventPriceDiscount.setDiscountCategory(discountCategory);
        eventPrice.setCurrency(Currency.getInstance("EUR"));
        eventPrice.setValue(new BigDecimal(7.0D));
        eventPriceDiscount.setEventPrice(eventPrice);
        returnValue &= eventPriceDiscountDAO.createOrUpdateEventPriceDiscount(eventPriceDiscount);
        eventPriceDiscount.setEvent(event);
        discountCategory = discountCategoryDAO.getDiscountCategoryByCodeForLocation("supporters", location);
        eventPriceDiscount.setDiscountCategory(discountCategory);
        eventPrice.setCurrency(Currency.getInstance("EUR"));
        eventPrice.setValue(new BigDecimal(10.0D));
        eventPriceDiscount.setEventPrice(eventPrice);
        returnValue &= eventPriceDiscountDAO.createOrUpdateEventPriceDiscount(eventPriceDiscount);
        eventPriceDiscount.setEvent(event);
        discountCategory = discountCategoryDAO.getDiscountCategoryByCodeForLocation("laeer", location);
        eventPriceDiscount.setDiscountCategory(discountCategory);
        eventPrice.setCurrency(Currency.getInstance("EUR"));
        eventPrice.setValue(new BigDecimal(10.0D));
        eventPriceDiscount.setEventPrice(eventPrice);
        returnValue &= eventPriceDiscountDAO.createOrUpdateEventPriceDiscount(eventPriceDiscount);
        eventPriceDiscount.setEvent(event);
        discountCategory = discountCategoryDAO.getDiscountCategoryByCodeForLocation("kd4-14", location);
        eventPriceDiscount.setDiscountCategory(discountCategory);
        eventPrice.setCurrency(Currency.getInstance("EUR"));
        eventPrice.setValue(new BigDecimal(3.0D));
        eventPriceDiscount.setEventPrice(eventPrice);
        returnValue &= eventPriceDiscountDAO.createOrUpdateEventPriceDiscount(eventPriceDiscount);
        eventPriceDiscount.setEvent(event);
        discountCategory = discountCategoryDAO.getDiscountCategoryByCodeForLocation("2ndParent", location);
        eventPriceDiscount.setDiscountCategory(discountCategory);
        eventPrice.setCurrency(Currency.getInstance("EUR"));
        eventPrice.setValue(new BigDecimal(0.0D));
        eventPriceDiscount.setEventPrice(eventPrice);
        returnValue &= eventPriceDiscountDAO.createOrUpdateEventPriceDiscount(eventPriceDiscount);
        return returnValue;
    }

    private boolean eventCreationHelperKGNBenalmadena(EventType eventType, Date eventStart, Date eventEnd, String code, String name, Double price, Event parentEvent) {
        boolean returnValue = true;
        DiscountCategory discountCategory = null;
        Event event = new Event();
        event.setCreateDate(now);
        event.setModifiedDate(now);
        event.setCreatedByUser(user);
        event.setModifiedByUser(user);
        event.setLocation(location);
        event.setEventType(eventType);
        event.setDescription("");
        MonetaryAmount eventPrice = new MonetaryAmount();
        eventPrice.setCurrency(Currency.getInstance("EUR"));
        eventPrice.setValue(new BigDecimal(price));
        event.setEventPrice(eventPrice);
        event.setCode(code);
        event.setName(name);
        event.setEventStart(eventStart);
        event.setEventEnd(eventEnd);
        event.setParentEvent(parentEvent);
        returnValue &= eventDAO.createOrUpdateEvent(event);
        EventPriceDiscount eventPriceDiscount = new EventPriceDiscount();
        eventPriceDiscount.setEvent(event);
        discountCategory = discountCategoryDAO.getDiscountCategoryByCodeForLocation("earlyPayment", location);
        eventPriceDiscount.setDiscountCategory(discountCategory);
        eventPrice.setCurrency(Currency.getInstance("EUR"));
        eventPrice.setValue(new BigDecimal(10.0D));
        eventPriceDiscount.setEventPrice(eventPrice);
        returnValue &= eventPriceDiscountDAO.createOrUpdateEventPriceDiscount(eventPriceDiscount);
        eventPriceDiscount.setEvent(event);
        discountCategory = discountCategoryDAO.getDiscountCategoryByCodeForLocation("supporters", location);
        eventPriceDiscount.setDiscountCategory(discountCategory);
        eventPrice.setCurrency(Currency.getInstance("EUR"));
        eventPrice.setValue(new BigDecimal(10.0D));
        eventPriceDiscount.setEventPrice(eventPrice);
        returnValue &= eventPriceDiscountDAO.createOrUpdateEventPriceDiscount(eventPriceDiscount);
        eventPriceDiscount.setEvent(event);
        discountCategory = discountCategoryDAO.getDiscountCategoryByCodeForLocation("laeer", location);
        eventPriceDiscount.setDiscountCategory(discountCategory);
        eventPrice.setCurrency(Currency.getInstance("EUR"));
        eventPrice.setValue(new BigDecimal(10.0D));
        eventPriceDiscount.setEventPrice(eventPrice);
        returnValue &= eventPriceDiscountDAO.createOrUpdateEventPriceDiscount(eventPriceDiscount);
        eventPriceDiscount.setEvent(event);
        discountCategory = discountCategoryDAO.getDiscountCategoryByCodeForLocation("kd4-14", location);
        eventPriceDiscount.setDiscountCategory(discountCategory);
        eventPrice.setCurrency(Currency.getInstance("EUR"));
        eventPrice.setValue(new BigDecimal(10.0D));
        eventPriceDiscount.setEventPrice(eventPrice);
        returnValue &= eventPriceDiscountDAO.createOrUpdateEventPriceDiscount(eventPriceDiscount);
        eventPriceDiscount.setEvent(event);
        discountCategory = discountCategoryDAO.getDiscountCategoryByCodeForLocation("2ndParent", location);
        eventPriceDiscount.setDiscountCategory(discountCategory);
        eventPrice.setCurrency(Currency.getInstance("EUR"));
        eventPrice.setValue(new BigDecimal(10.0D));
        eventPriceDiscount.setEventPrice(eventPrice);
        returnValue &= eventPriceDiscountDAO.createOrUpdateEventPriceDiscount(eventPriceDiscount);
        return returnValue;
    }

    public LocationDAO getLocationDAO() {
        return locationDAO;
    }

    public void setLocationDAO(LocationDAO locationDAO) {
        this.locationDAO = locationDAO;
    }

    public EventDAO getEventDAO() {
        return eventDAO;
    }

    public void setEventDAO(EventDAO eventDAO) {
        this.eventDAO = eventDAO;
    }

    public UserAccountManager getUserAccountManager() {
        return userAccountManager;
    }

    public void setUserAccountManager(UserAccountManager userAccountManager) {
        this.userAccountManager = userAccountManager;
    }

    public EventTypeDAO getEventTypeDAO() {
        return eventTypeDAO;
    }

    public void setEventTypeDAO(EventTypeDAO eventTypeDAO) {
        this.eventTypeDAO = eventTypeDAO;
    }

    public DiscountCategoryDAO getDiscountCategoryDAO() {
        return discountCategoryDAO;
    }

    public void setDiscountCategoryDAO(DiscountCategoryDAO discountCategoryDAO) {
        this.discountCategoryDAO = discountCategoryDAO;
    }

    public EventPriceDiscountDAO getEventPriceDiscountDAO() {
        return eventPriceDiscountDAO;
    }

    public void setEventPriceDiscountDAO(EventPriceDiscountDAO eventPriceDiscountDAO) {
        this.eventPriceDiscountDAO = eventPriceDiscountDAO;
    }

    public LocationServiceDAO getLocationServiceDAO() {
        return locationServiceDAO;
    }

    public void setLocationServiceDAO(LocationServiceDAO locationServiceDAO) {
        this.locationServiceDAO = locationServiceDAO;
    }

    public LocationServiceTypeDAO getLocationServiceTypeDAO() {
        return locationServiceTypeDAO;
    }

    public void setLocationServiceTypeDAO(LocationServiceTypeDAO locationServiceTypeDAO) {
        this.locationServiceTypeDAO = locationServiceTypeDAO;
    }

    public LocationServicePriceDiscountDAO getLocationServicePriceDiscountDAO() {
        return locationServicePriceDiscountDAO;
    }

    public void setLocationServicePriceDiscountDAO(LocationServicePriceDiscountDAO locationServicePriceDiscountDAO) {
        this.locationServicePriceDiscountDAO = locationServicePriceDiscountDAO;
    }
}
