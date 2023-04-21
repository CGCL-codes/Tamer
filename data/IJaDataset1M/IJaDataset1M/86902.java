package it.polimi.miaria.epo.utils;

import java.io.File;
import org.apache.commons.digester.*;

public class CapRuleSet {

    private Cap cap;

    public CapRuleSet() {
        cap = new Cap();
    }

    public Cap initialize(String xmlCapFile) {
        try {
            Digester digester = new Digester();
            digester.setValidating(false);
            digester.addObjectCreate("alert", Cap.class);
            digester.addCallMethod("alert/identifier", "setIdentifier", 0);
            digester.addCallMethod("alert/sender", "setSender", 0);
            digester.addCallMethod("alert/sent", "setSent", 0);
            digester.addCallMethod("alert/scope", "setScope", 0);
            digester.addCallMethod("alert/info/area/areaDesc", "setAreaDesc", 0);
            digester.addCallMethod("alert/info/area/circle", "setCircle", 0);
            digester.addCallMethod("alert/info/event", "setEvent", 0);
            digester.addCallMethod("alert/info/headline", "setHeadline", 0);
            digester.addCallMethod("alert/info/description", "setDescription", 0);
            digester.addCallMethod("alert/info/category", "setCategory", 0);
            digester.addCallMethod("alert/info/urgency", "setUrgency", 0);
            digester.addCallMethod("alert/info/severity", "setSeverity", 0);
            digester.addCallMethod("alert/info/certainty", "setCertainty", 0);
            digester.addCallMethod("alert/info/senderName", "setSenderName", 0);
            digester.addCallMethod("alert/info/web", "setWeb", 0);
            File fileObj = new File(xmlCapFile);
            cap = (Cap) digester.parse(fileObj);
        } catch (Exception e) {
            System.out.println("Ops, c'� un problema con il CAP: " + e);
            e.printStackTrace();
        }
        cap.addPath(xmlCapFile);
        return cap;
    }
}
