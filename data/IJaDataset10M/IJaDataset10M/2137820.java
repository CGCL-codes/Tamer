package felper.service;

import felper.message.Message;

/**
 * @version 1.0
 * @created 13-May-2007 12:14:38
 */
public class ServiceIdentifier {

    private Integer serviceID;

    private String serviceDescription;

    private Message messageExample;

    public ServiceIdentifier() {
    }

    public void finalize() throws Throwable {
    }

    public Integer getServiceID() {
        return serviceID;
    }

    /**
     * @param newVal
     */
    public void setServiceID(Integer newVal) {
        serviceID = newVal;
    }

    public String getServiceDescription() {
        return serviceDescription;
    }

    /**
     * @param newVal
     */
    public void setServiceDescription(String newVal) {
        serviceDescription = newVal;
    }

    public Message getMessageExample() {
        return messageExample;
    }

    /**
     * @param newVal
     */
    public void setMessageExample(Message newVal) {
        messageExample = newVal;
    }
}
