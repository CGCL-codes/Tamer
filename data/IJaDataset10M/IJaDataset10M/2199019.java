package d2.plans;

import gatech.mmpm.sensor.Sensor;
import gatech.mmpm.util.XMLWriter;
import java.util.HashMap;
import gatech.mmpm.util.Pair;

public class FailureTransition extends Transition {

    /**
	 * 
	 */
    private static final long serialVersionUID = 2988867343208897394L;

    public FailureTransition() {
        super();
        setElementID("FAILURE" + getElementID());
    }

    public FailureTransition(Plan p) {
        super();
        setElementID("FAILURE" + getElementID());
        this.setPlan(p);
    }

    public Sensor getCondition() {
        return getPlan().getFailureCondition();
    }

    public void writeToXML(String planID, XMLWriter w) {
        w.rawXML("<transition id='" + this.getElementID() + "' type=\"FailureTransition\" nextPlan='" + planID + "'>");
        w.tag("conditions");
        getCondition().writeToXML(w);
        w.tag("/conditions");
        w.tag("nextStates");
        for (Pair<Integer, State> t : getNextStates()) {
            w.rawXML("<nextState id='" + t._b.getElementID() + "' tokens='" + t._a + "'/>");
        }
        w.tag("/nextStates");
        w.rawXML("</transition>");
        w.flush();
    }

    public Object clone(HashMap<Object, Object> alreadyCloned) {
        if (alreadyCloned.get(this) != null) return alreadyCloned.get(this);
        FailureTransition cloneT = new FailureTransition();
        alreadyCloned.put(this, cloneT);
        cloneT.elementID = elementID;
        cloneT.plan = (plan == null ? null : (Plan) plan.clone(alreadyCloned));
        cloneT.c = (Sensor) c.clone();
        for (Pair<Integer, State> p : nextTokenStates) {
            cloneT.nextTokenStates.add(new Pair<Integer, State>(new Integer((int) p._a), (State) p._b.clone(alreadyCloned)));
        }
        return cloneT;
    }
}
