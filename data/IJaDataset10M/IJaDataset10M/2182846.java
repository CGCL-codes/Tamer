package net.sf.rptserver.report;

import java.io.*;
import java.util.*;
import org.quartz.Trigger;
import org.quartz.JobDetail;
import net.sf.rptserver.lang.*;
import net.sf.rptserver.destination.*;
import net.sf.rptserver.dependency.*;
import net.sf.rptserver.datasource.*;
import net.sf.rptserver.warehouse.*;

public class Report extends ReportServerObject {

    private static Random random = new Random();

    private String name = null;

    private String dir = null;

    private String file = null;

    private String workdir = null;

    private ArrayList triggers = new ArrayList(2);

    private JobDetail jobDetail = null;

    private Datasource ds = null;

    private ArrayList destinations = new ArrayList(5);

    private Dependency dep = null;

    private Warehouse pre = null;

    private Warehouse post = null;

    public Report() {
    }

    public void setName(String n) {
        this.name = n;
    }

    public String getName() {
        return (this.name);
    }

    public void setDir(String d) {
        this.dir = d;
    }

    public String getDir() {
        return (this.dir);
    }

    public void setFile(String f) {
        this.file = f;
    }

    public String getFile() {
        return (this.file);
    }

    public void setDatasource(Datasource d) {
        this.ds = d;
    }

    public Datasource getDatasource() {
        return (this.ds);
    }

    public void setWorkdir(String w) {
        workdir = w;
    }

    public String getWorkdir() {
        return (this.workdir);
    }

    public void addDestination(Destination d) {
        this.destinations.add(d);
    }

    public ArrayList getDestinations() {
        return (this.destinations);
    }

    public void addTrigger(Trigger t) {
        t.setJobName("Report - " + getName());
        t.setJobGroup("Default Group");
        this.triggers.add(t);
    }

    public ArrayList getTriggers() {
        return (this.triggers);
    }

    public void setDependency(Dependency d) {
        this.dep = d;
    }

    public Dependency getDependency() {
        return (this.dep);
    }

    public void setPreWarehouse(Warehouse w) {
        this.pre = w;
    }

    public Warehouse getPreWarehouse() {
        return (this.pre);
    }

    public void setPostWarehouse(Warehouse w) {
        this.post = w;
    }

    public Warehouse getPostWarehouse() {
        return (this.post);
    }

    public JobDetail getJobDetail() {
        if (jobDetail == null) {
            jobDetail = new JobDetail("Report - " + getName(), "Default Group", ReportJob.class);
        }
        return (jobDetail);
    }

    public String toString() {
        StringBuffer rc = new StringBuffer();
        rc.append("Report: {");
        rc.append("name=");
        rc.append(getName());
        rc.append(",dir=");
        rc.append(getDir());
        rc.append(",file=");
        rc.append(getFile());
        rc.append(",datasource=");
        rc.append(getDatasource());
        rc.append(",dependency=");
        rc.append(getDependency());
        rc.append(",pre=");
        rc.append(getPreWarehouse());
        rc.append(",post=");
        rc.append(getPostWarehouse());
        rc.append(", destinations=");
        for (int x = 0; x < getDestinations().size(); x++) {
            Destination d = (Destination) getDestinations().get(x);
            rc.append(d);
        }
        rc.append(", triggers=");
        for (int x = 0; x < getTriggers().size(); x++) {
            rc.append("trigger=");
            rc.append(getTriggers().get(x));
            rc.append(",");
        }
        rc.append("} ");
        return (rc.toString());
    }
}
