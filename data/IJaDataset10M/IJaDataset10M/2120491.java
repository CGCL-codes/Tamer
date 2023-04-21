package org.saintandreas.serket.test;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.TreeSet;
import org.junit.Test;
import org.saintandreas.serket.service.ServiceType;

public class Misc {

    @Test
    public void testServiceTypeEnums() throws UnknownHostException {
        TreeSet<String> set = new TreeSet<String>();
        for (Object o : System.getProperties().keySet()) {
            set.add(o.toString());
        }
        for (String s : set) {
            System.out.println(s);
        }
        for (ServiceType type : ServiceType.values()) {
            System.out.println(type.name());
            System.out.println(type.scpdResourcePath());
        }
        System.out.println(InetAddress.getLocalHost().getHostName());
    }
}
