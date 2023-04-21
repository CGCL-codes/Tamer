package eip.chapter10.processmanager.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;
import org.apache.axiom.om.OMElement;
import org.apache.log4j.Logger;
import eip.chapter10.processmanager.DivingAgencyService;
import eip.chapter10.processmanager.DivingBooking;
import eip.chapter10.processmanager.DivingRequest;
import eip.util.JibxUtil;
import eip.util.StaxUtil;

public class DivingAgencyServiceImpl implements DivingAgencyService {

    private static final Logger logger = Logger.getLogger(DivingAgencyServiceImpl.class);

    private String divingAgencyName;

    private String endTime;

    private String instructorName;

    private double pricePerPerson;

    private String startingTime;

    private String address;

    public OMElement requestBooking(OMElement requestElem) throws Exception {
        DivingBooking booking = processBooking((DivingRequest) JibxUtil.unmarshall(requestElem.toString(), DivingRequest.class));
        return StaxUtil.createOMElement(JibxUtil.marshall(booking));
    }

    public DivingBooking processBooking(DivingRequest request) {
        if (logger.isInfoEnabled()) {
            logger.info(divingAgencyName + " received a request");
        }
        DivingBooking booking = new DivingBooking();
        booking.setDivingBookID(new Random().nextLong());
        booking.setDivingStartingTime(fillTime(request.getRequestDate(), startingTime));
        booking.setDivingEndTime(fillTime(request.getRequestDate(), endTime));
        booking.setInstructorName(instructorName);
        booking.setPrice(request.getNumberOfDivers() * pricePerPerson);
        booking.setAddress(address);
        return booking;
    }

    private Date fillTime(Date date, String time) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(time.substring(0, time.indexOf(":"))));
        calendar.set(Calendar.MINUTE, Integer.valueOf(time.substring(time.indexOf(":") + 1)));
        return calendar.getTime();
    }

    public String getDivingAgencyName() {
        return divingAgencyName;
    }

    public void setDivingAgencyName(String divingAgencyName) {
        this.divingAgencyName = divingAgencyName;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public double getPricePerPerson() {
        return pricePerPerson;
    }

    public void setPricePerPerson(double pricePerPerson) {
        this.pricePerPerson = pricePerPerson;
    }

    public String getStartingTime() {
        return startingTime;
    }

    public void setStartingTime(String startingTime) {
        this.startingTime = startingTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
