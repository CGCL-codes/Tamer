package com.wgo.surveyModel.domain.common.impl;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import java.util.Date;
import java.util.Set;
import com.wgo.surveyModel.domain.common.*;

public class StreamerImpl extends EquipmentImpl implements Streamer {

    private Integer lenght;

    private String type;

    private Vessel vessel;

    private Set<StreamerSection> sections;

    public StreamerImpl() {
        super();
        sections = new HashSet<StreamerSection>();
    }

    public Vessel getVessel() {
        return vessel;
    }

    public void setVessel(Vessel vessel) {
        if (null != vessel) {
            this.vessel = vessel;
        } else if (null != this.vessel) {
            this.vessel.removeStreamer(this);
            this.vessel = null;
        }
    }

    public Set<StreamerSection> getSections() {
        return sections;
    }

    public void setSections(Set<StreamerSection> sections) {
        this.sections = sections;
    }

    public boolean addSection(StreamerSection section) {
        if (!sections.contains(section)) {
            this.sections.add(section);
            if (this != section.getStreamer()) {
                section.setStreamer(this);
            }
        }
        return true;
    }

    public boolean removeSection(StreamerSection section) {
        this.sections.remove(section);
        ((StreamerSectionImpl) section).setStreamer(null);
        return true;
    }

    public Integer getLenght() {
        return lenght;
    }

    public void setLenght(Integer lenght) {
        this.lenght = lenght;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getStatus(Date date) {
        Class clazz;
        try {
            String name = getClass().getSimpleName();
            String packageName = this.getClass().getPackage().getName();
            if (name.endsWith("Ejb")) {
                name = name.substring(0, name.length() - 3) + "ImplUser";
                packageName = this.getClass().getSuperclass().getPackage().getName();
            } else name += "User";
            String className = packageName + "." + name;
            clazz = Class.forName(className);
            Method method = clazz.getMethod("getStatus", Date.class);
            Object[] args = new Object[1];
            args[0] = date;
            return (Integer) method.invoke(clazz.newInstance(), args);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Double doStuff(Integer p2, Boolean p3) {
        Class clazz;
        try {
            String name = getClass().getSimpleName();
            String packageName = this.getClass().getPackage().getName();
            if (name.endsWith("Ejb")) {
                name = name.substring(0, name.length() - 3) + "ImplUser";
                packageName = this.getClass().getSuperclass().getPackage().getName();
            } else name += "User";
            String className = packageName + "." + name;
            clazz = Class.forName(className);
            Method method = clazz.getMethod("doStuff", Integer.class, Boolean.class);
            Object[] args = new Object[2];
            args[0] = p2;
            args[0] = p3;
            return (Double) method.invoke(clazz.newInstance(), args);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
