package org.cybergarage.upnp;

import java.util.*;

public class ServiceList extends Vector {

    public static final String ELEM_NAME = "serviceList";

    public ServiceList() {
    }

    public Service getService(int n) {
        Object obj = null;
        try {
            obj = get(n);
        } catch (Exception e) {
        }
        ;
        return (Service) obj;
    }
}
