package org.isi.monet.applications.frontserver.control;

import org.isi.monet.applications.frontserver.core.model.ServerConfiguration;
import org.isi.monet.applications.frontserver.core.model.Session;

public class Converter {

    public static void convert(Session oSource, org.isi.monet.core.model.Session oTarget) {
        oTarget.setId(oSource.getId());
    }

    public static void convert(org.isi.monet.core.model.Session oSource, Session oTarget) {
        oTarget.setId(oSource.getId());
    }

    public static void convert(ServerConfiguration oSource, org.isi.monet.core.model.ServerConfiguration oTarget) {
        oTarget.setHost(oSource.getIp());
        oTarget.setName(oSource.getName());
        oTarget.setPort(String.valueOf(oSource.getPort()));
    }
}
