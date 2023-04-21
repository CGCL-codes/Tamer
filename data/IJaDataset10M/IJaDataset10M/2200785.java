package com.vayoodoot.message;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Sachin Shetty
 * Date: May 18, 2007
 * Time: 8:10:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class BuddyEvent extends Message {

    protected static final String messageName = BuddyEvent.class.getName();

    protected static String messageString = getMessageString(messageName);

    protected String buddyList;

    protected int accountType;

    protected int event;

    protected String eventDescription;

    public BuddyEvent() {
        super(messageName);
    }

    public void recievedElement(String elementName, String elementValue) {
        super.recievedElement(elementName, elementValue);
        if (elementName.equalsIgnoreCase("buddy_list")) {
            buddyList = elementValue;
        }
        if (elementName.equalsIgnoreCase("account_type")) {
            accountType = Integer.parseInt(elementValue);
        }
        if (elementName.equalsIgnoreCase("event")) {
            event = Integer.parseInt(elementValue);
        }
        if (elementName.equalsIgnoreCase("event_description")) {
            eventDescription = elementValue;
        }
    }

    public String getXMLString() throws MessageException {
        if (messageString == null) throw new MessageException("message string is null, check the log file for message loading errors");
        HashMap hm = getValuesMap();
        hm.put("BUDDY_LIST", buddyList);
        hm.put("ACCOUNT_TYPE", accountType);
        hm.put("BUDDY_EVENT", event);
        hm.put("EVENT_DESCRIPTION", eventDescription);
        String message = MessageFormatter.getInstantiatedString(messageString, hm);
        return message;
    }

    public String getBuddyList() {
        return buddyList;
    }

    public void setBuddyList(String buddyList) {
        this.buddyList = buddyList;
    }

    public int getAccountType() {
        return accountType;
    }

    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }

    public int getEvent() {
        return event;
    }

    public void setEvent(int event) {
        this.event = event;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }
}
