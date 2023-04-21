package de.uka.aifb.owl.transaction;

import org.eclipse.emf.transaction.ResourceSetChangeEvent;
import org.eclipse.emf.transaction.ResourceSetListenerImpl;

public class Mylistener extends ResourceSetListenerImpl {

    public void resourceSetChanged(ResourceSetChangeEvent event) {
        System.out.println("A change has been made with " + event.getNotifications().size() + " notifications produced.");
    }
}
