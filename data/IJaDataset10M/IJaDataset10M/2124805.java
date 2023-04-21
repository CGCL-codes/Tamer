package com.wgo.surveyModel.domain.common;

import java.util.Set;
import com.wgo.surveyModel.domain.common.helpers.Persistent;

public interface Vessel extends Equipment, Persistent {

    public String getName();

    public void setName(String name);

    public Float getWeight();

    public void setWeight(Float weight);

    public Set<Streamer> getStreamers();

    public boolean addStreamer(Streamer streamer);

    public boolean removeStreamer(Streamer streamer);

    public SurveyDef getSurveydef();

    public void setSurveydef(SurveyDef surveydef);
}
