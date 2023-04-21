package org.akrogen.tkui.usecases.backingbean.wsdl;

import org.akrogen.tkui.dom.xul.dom.simples.Button;
import org.w3c.dom.events.Event;

public class WSDLBackingBean {

    public void send(Event e) {
        Button button = (Button) e.getTarget();
        System.out.println("Call service");
    }
}
