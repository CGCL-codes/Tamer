package jgenet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import jgenet.TS.Trans;

/**
 * <p>Class that represents the Transition Relation of the Transition System.<p>
 * 
 * @author Josep Carmona 
 * @author Jorge Munoz
 */
public class TRel {

    /** encoding of the Transition Relation*/
    private Encoding enc;

    /** Transition Relation*/
    private TS ts;

    /**
	 * Creates a Transition Relation class given a Transition System.
	 * @param enc Encoding of the Transition System.
	 * @param ts Transition System.
	 */
    public TRel(Encoding enc, TS ts) {
        this.enc = enc;
        this.ts = ts;
    }

    public void buildTR(Set<String> visibleEvents) {
        Map<String, List<String>> stateClasses = new HashMap<String, List<String>>();
        if (!visibleEvents.isEmpty()) {
            Iterator<Trans> itTrans = ts.getTransIterator();
            while (itTrans.hasNext()) {
                Trans trans = itTrans.next();
                String event = trans.getEvent();
                String source = trans.getSource();
                String target = trans.getTarget();
                int compare = source.compareTo(target);
                String minlex = (compare < 0 ? source : target);
                String maxlex = (compare > 0 ? source : target);
                if (!visibleEvents.contains(event)) {
                    if (stateClasses.get(minlex) == null) {
                        stateClasses.put(minlex, new ArrayList<String>());
                    }
                    int n = stateClasses.get(minlex).size();
                    if (n == 0) {
                        stateClasses.get(minlex).add(minlex);
                    } else if (n == 1) {
                        minlex = stateClasses.get(minlex).get(0);
                    }
                    if (stateClasses.get(maxlex) == null) {
                        stateClasses.put(maxlex, new ArrayList<String>());
                    }
                    int nn = stateClasses.get(maxlex).size();
                    if (nn == 0) {
                        stateClasses.get(minlex).add(maxlex);
                        stateClasses.get(maxlex).add(minlex);
                    } else if (nn >= 1) {
                        if (nn == 1) {
                            String cpmaxlex = maxlex;
                            String othermin = stateClasses.get(maxlex).get(0);
                            int compare2 = minlex.compareTo(othermin);
                            minlex = (compare2 < 0 ? minlex : othermin);
                            maxlex = (compare2 > 0 ? minlex : othermin);
                            stateClasses.get(cpmaxlex).clear();
                            stateClasses.get(cpmaxlex).add(minlex);
                        }
                        if (maxlex != minlex) {
                            for (String aaa : stateClasses.get(maxlex)) {
                                if (maxlex != minlex) stateClasses.get(minlex).add(aaa);
                                if (aaa != maxlex) {
                                    stateClasses.get(aaa).clear();
                                    stateClasses.get(aaa).add(minlex);
                                }
                            }
                            stateClasses.get(maxlex).clear();
                            stateClasses.get(maxlex).add(minlex);
                        }
                    }
                }
            }
        }
        Iterator<Trans> itTrans = ts.getTransIterator();
        while (itTrans.hasNext()) {
            Trans trans = itTrans.next();
        }
    }

    private void computeStateClasses(Map<String, List<String>> stateClasses) {
    }
}
