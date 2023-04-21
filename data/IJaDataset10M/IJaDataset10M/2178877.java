package ru.satseqsys.gate.scenario;

import java.util.List;
import org.apache.commons.collections.MapUtils;

public class ScenarioTemplate {

    private List<ScenarioStage> stages;

    public List<ScenarioStage> getStages() {
        return stages;
    }

    public void setStages(List<ScenarioStage> stages) {
        this.stages = stages;
    }

    @SuppressWarnings("unchecked")
    public boolean checkAcceptance(Object o) {
        if (stages == null || stages.isEmpty()) {
            return false;
        }
        return stages.get(0).checkAcceptance(o, MapUtils.EMPTY_MAP);
    }
}
