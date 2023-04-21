package de.creatronix.artist3k.controller.form;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import de.creatronix.artist3k.model.Application;

public class ApplicationAddForm extends ActionForm {

    /**
	 * 
	 */
    private static final long serialVersionUID = -1250599385923494530L;

    static SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");

    Application application = new Application();

    String bookerName;

    String stageActName;

    String eventName;

    String locationName;

    Collection allUser;

    Collection allStageActs;

    private Collection allEvents;

    private Collection allLocations;

    public Collection getAllStageActs() {
        return allStageActs;
    }

    public Collection getAllUser() {
        return allUser;
    }

    public void setAllUser(Collection allUser) {
        this.allUser = allUser;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public String getBookerName() {
        return bookerName;
    }

    public void setBookerName(String bookerName) {
        this.bookerName = bookerName;
    }

    public void setAllStageActs(Collection allStageActs) {
        this.allStageActs = allStageActs;
    }

    public void setAllEvents(Collection allEvents) {
        this.allEvents = allEvents;
    }

    public Collection getAllEvents() {
        return allEvents;
    }

    public String getStageActName() {
        return stageActName;
    }

    public void setStageActName(String stageActName) {
        this.stageActName = stageActName;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
    }

    public Collection getAllLocations() {
        return allLocations;
    }

    public void setAllLocations(Collection allLocations) {
        this.allLocations = allLocations;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getSendDate() {
        return calendar2DateString(application.getSendDate());
    }

    public void setSendDate(String sendDate) {
        try {
            this.application.setSendDate(dateString2Calendar(sendDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public Calendar dateString2Calendar(String s) throws ParseException {
        Calendar cal = Calendar.getInstance();
        Date d1 = df.parse(s);
        cal.setTime(d1);
        return cal;
    }

    public String calendar2DateString(Calendar cal) {
        return df.format(cal.getTime());
    }

    public void setDesiredDate(String desiredDate) {
        try {
            application.setDesiredDate(dateString2Calendar(desiredDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void setAskedForPosterAndFlyerDate(String askedForPosterAndFlyerDate) {
        try {
            application.setAskedForPosterAndFlyerDate(dateString2Calendar(askedForPosterAndFlyerDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void setSendPosterAndFlyerDate(String sendPosterAndFlyerDate) {
        try {
            application.setSendPosterAndFlyerDate(dateString2Calendar(sendPosterAndFlyerDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void setDemandedFee(String demandedFee) {
        if (demandedFee.matches("[0-9]+")) {
            application.setDemandedFee(new BigDecimal(demandedFee));
        } else application.setDemandedFee(new BigDecimal(0));
    }

    public void setLodgingCosts(String lodgingCosts) {
        if (lodgingCosts.matches("[0-9]+")) application.setLodgingCosts(new BigDecimal(lodgingCosts)); else application.setLodgingCosts(new BigDecimal(0));
    }

    public void setReceivedExpenses(String receivedExpenses) {
        if (receivedExpenses.matches("[0-9]+")) application.setReceivedExpenses(new BigDecimal(receivedExpenses)); else application.setReceivedExpenses(new BigDecimal(0));
    }

    public void setReceivedFee(String receivedFee) {
        if (receivedFee.matches("[0-9]+")) application.setReceivedFee(new BigDecimal(receivedFee)); else application.setReceivedFee(new BigDecimal(0));
    }

    public void setTransportationCosts(String transportationCosts) {
        if (transportationCosts.matches("[0-9]+")) application.setTransportationCosts(new BigDecimal(transportationCosts)); else application.setTransportationCosts(new BigDecimal(0));
    }

    public void setNumberOfSentFlyers(String numberOfSentFlyers) {
        application.setNumberOfSentFlyers(numberOfSentFlyers);
    }

    public void setNumberOfSentPosters(String numberOfSentPosters) {
        application.setNumberOfSentPosters(numberOfSentPosters);
    }

    public String getComment() {
        return application.getComment();
    }

    public void setComment(String comment) {
        application.setComment(comment);
    }
}
