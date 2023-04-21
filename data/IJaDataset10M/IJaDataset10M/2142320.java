package com.atosorigin.nl.agreement.utility;

import java.util.Date;
import java.util.List;
import com.atosorigin.nl.agreement.dispatcher.Customer;
import com.atosorigin.nl.agreement.events.Event;
import com.atosorigin.nl.agreement.runtime.SimulatorCallback;

/**
 * Implementation of <tt>SimulatorCallBack</tt> which is verbose
 * on the standard console.
 * 
 * @author Jeroen Benckhuijsen
 */
public class ConsoleCallback implements SimulatorCallback {

    @Override
    public void showCurrentDate(Date date) {
        System.out.println("The current date is " + date.toString());
    }

    @Override
    public void showEventsToProcess(List<Event> events) {
        System.out.println("Handling events for today:");
        for (Event event : events) {
            System.out.println("--" + event.toString());
        }
    }

    @Override
    public void showUpdatedCustomer(Customer customer) {
        System.out.println("Updated customer after events:\n" + customer.toString());
    }

    @Override
    public void waitForNextStep() throws InterruptedException {
        Thread.sleep(1);
    }
}
