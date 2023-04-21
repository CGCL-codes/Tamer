package playground.kai.bvwp;

import java.util.Map;
import java.util.TreeMap;
import org.matsim.api.core.v01.Id;

class ScenarioForEval {

    Map<Id, Values> values = new TreeMap<Id, Values>();

    ScenarioForEval() {
    }

    ScenarioForEval createDeepCopy() {
        ScenarioForEval nnn = new ScenarioForEval();
        for (Id id : values.keySet()) {
            Values oldValues = this.getByODRelation(id);
            Values newValues = oldValues.createDeepCopy();
            nnn.values.put(id, newValues);
        }
        return nnn;
    }

    Values getByODRelation(Id id) {
        return values.get(id);
    }

    void setValuesForODRelation(Id id, Values tmp) {
        values.put(id, tmp);
    }
}
