package com.wgo.surveyModel.domain.common.impl;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import java.util.Date;
import java.util.Set;
import com.wgo.surveyModel.domain.common.*;

public class SurveyDefImpl implements SurveyDef {

    private Set<Vessel> vessels;

    private Long dbId = null;

    public Long getDbId() {
        return dbId;
    }

    public void setDbId(Long dbId) {
        this.dbId = dbId;
    }

    public SurveyDefImpl() {
        vessels = new HashSet<Vessel>();
    }

    public Set<Vessel> getVessels() {
        return vessels;
    }

    public void setVessels(Set<Vessel> vessels) {
        this.vessels = vessels;
    }

    public boolean addVessel(Vessel vessel) {
        if (!vessels.contains(vessel)) {
            this.vessels.add(vessel);
            if (this != vessel.getSurveydef()) {
                vessel.setSurveydef(this);
            }
        }
        return true;
    }

    public boolean removeVessel(Vessel vessel) {
        this.vessels.remove(vessel);
        ((VesselImpl) vessel).setSurveydef(null);
        return true;
    }
}
