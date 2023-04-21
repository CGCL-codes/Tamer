package netfone;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Call {

    private String dialedNumber;

    private Double callCost;

    private String destination;

    private String localAreaCode;

    private Date date;

    static SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public String getDateString() {
        if (date == null) {
            return "";
        } else {
            return formatter.format(date);
        }
    }

    public String getNumber() {
        return "(" + getLocalAreaCode() + ")" + dialedNumber.substring(App.LOCAL_AREA_CODE_LENGTH);
    }

    public String getLocalAreaCode() {
        return dialedNumber.substring(0, App.LOCAL_AREA_CODE_LENGTH);
    }

    public Double getCallCost() {
        return callCost;
    }

    public void setCallCost(Double callCost) {
        this.callCost = callCost;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDialedNumber() {
        return dialedNumber;
    }

    public void setDialedNumber(String dialedNumber) {
        this.dialedNumber = dialedNumber;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
