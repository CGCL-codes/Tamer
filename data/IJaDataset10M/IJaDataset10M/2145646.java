package org.buglet.bugsystem;

import org.buglet.env.BugEnv;
import java.util.*;

public class BugConfig {

    public static String organisation = "58k.com";

    public static String defaultStatus = "Incoming";

    public static BugDataValues priority = null;

    public static BugDataValues status = null;

    public static BugDataValues project = null;

    static BugConfig bcInstance = null;

    public static BugConfig getInstance() {
        if (bcInstance == null) bcInstance = new BugConfig();
        return bcInstance;
    }

    private BugConfig() {
        initAll();
    }

    public static void initPrio() {
        if (priority == null) priority = new BugDataValues("priority");
    }

    public static void initStat() {
        if (status == null) status = new BugDataValues("status");
    }

    public static void initProj() {
        if (project == null) project = new BugDataValues("project");
    }

    public static void initAll() {
        initPrio();
        initStat();
        initProj();
    }

    public String getSelect(String name) {
        StringBuffer returnVal = new StringBuffer("<Select name=>\n");
        if (name.equals("status")) returnVal.append(status.toOptions());
        if (name.equals("priority")) returnVal.append(priority.toOptions());
        if (name.equals("project")) returnVal.append(project.toOptions());
        returnVal.append("</Select>\n");
        return returnVal.toString();
    }

    public static String toXMLData() {
        initAll();
        StringBuffer returnVal = new StringBuffer("<BugConfig>\n").append(priority.toXMLData()).append(status.toXMLData()).append(project.toXMLData()).append("</BugConfig>\n");
        return returnVal.toString();
    }

    public static void main(String[] args) {
        System.out.println(BugConfig.toXMLData());
    }
}
