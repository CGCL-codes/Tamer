package uk.ekiwi.messaging;

import java.util.ArrayList;

public interface QueueManagerInquirer {

    public ArrayList<String> getQList() throws MessagingException;

    public ArrayList<Queue> getQDetailsList() throws MessagingException;

    public ArrayList<Queue> getQDetailsList(ArrayList<String> selectedQueues) throws MessagingException;
}
