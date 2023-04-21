package com.neidetcher.tuml.model;

import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;

public class MessageTest extends TestCase {

    public void testConstructor() {
        String rawString = "manager.findCustomer()->dao.findCustomer(String customerName):Customer";
        Message message = new Message(rawString);
        System.out.println(message);
    }

    public void testAssignLifespan() {
        List<Message> messages = new ArrayList<Message>();
        messages.add(new Message("->client.showCustomerReport():JSP"));
        messages.add(new Message("client.showCustomerReport()->action.showCustomerReport():JSP"));
        messages.add(new Message("action.showCustomerReport()->db.helloWorld():void"));
        messages.add(new Message("action.showCustomerReport()->mgr.findCustomer(String customerName):Customer"));
        messages.add(new Message("mgr.findCustomer()->dao.findCustomer(String customerName):Customer"));
        messages.add(new Message("dao.findCustomer()->db.select * from customer(customer name):Customer"));
        Message.assignLifespan(messages);
        assertEquals(1, messages.get(5).getLifeSpan());
    }
}
