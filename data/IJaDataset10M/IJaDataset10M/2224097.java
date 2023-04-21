package net.sourceforge.myreserve.web;

import javax.faces.event.ActionEvent;

public class FacesUtil {

    public static String getActionAttribute(ActionEvent event, String name) {
        return (String) event.getComponent().getAttributes().get(name);
    }
}
