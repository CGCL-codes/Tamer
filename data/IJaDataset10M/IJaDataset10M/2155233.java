package playground.kai.bvwp;

import org.matsim.core.basic.v01.IdImpl;
import playground.kai.bvwp.Values.Entry;
import playground.kai.bvwp.Values.Mode;
import playground.kai.bvwp.Values.Type;

/**
 * @author Ihab
 *
 */
class IllustrationAP200PVScenarioInduced {

    static ScenarioForEval createNullfall1() {
        ScenarioForEval nullfall = new ScenarioForEval();
        Values nullfallForOD = new Values();
        nullfall.setValuesForODRelation(new IdImpl("BC"), nullfallForOD);
        {
            ValuesForAMode roadValues = nullfallForOD.getByMode(Mode.road);
            {
                ValuesForAUserType pvValuesRoad = roadValues.getByType(Type.PV_NON_COMMERCIAL);
                pvValuesRoad.setByEntry(Entry.XX, 1000.);
                pvValuesRoad.setByEntry(Entry.km, 100.);
                pvValuesRoad.setByEntry(Entry.hrs, 1.);
            }
            ValuesForAMode railValues = nullfallForOD.getByMode(Mode.rail);
            {
                ValuesForAUserType pvValuesRail = railValues.getByType(Type.PV_NON_COMMERCIAL);
                pvValuesRail.setByEntry(Entry.XX, 10.);
                pvValuesRail.setByEntry(Entry.km, 100.);
                pvValuesRail.setByEntry(Entry.hrs, 6.);
            }
        }
        return nullfall;
    }

    static ScenarioForEval createPlanfall1(ScenarioForEval nullfall) {
        ScenarioForEval planfall = nullfall.createDeepCopy();
        Values planfallForOD = planfall.getByODRelation(new IdImpl("BC"));
        {
            ValuesForAMode railValues = planfallForOD.getByMode(Mode.rail);
            railValues.getByType(Type.PV_NON_COMMERCIAL).incByEntry(Entry.hrs, -4.);
            double delta = 90.;
            railValues.getByType(Type.PV_NON_COMMERCIAL).incByEntry(Entry.XX, delta);
            planfall.getByODRelation(new IdImpl("BC")).getByMode(Mode.road).getByType(Type.PV_NON_COMMERCIAL).incByEntry(Entry.XX, -0.);
        }
        return planfall;
    }
}
