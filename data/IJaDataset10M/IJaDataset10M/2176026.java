package com.mentorgen.tools.util.profile.bundle.scheduler;

import java.util.List;
import java.util.Map;

public interface SchedulerIF {

    @SuppressWarnings("unchecked")
    public List schedule(Map containerMap);
}
