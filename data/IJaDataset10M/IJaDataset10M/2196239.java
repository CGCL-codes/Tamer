package at.ac.tuwien.infosys.www.pixy.analysis.inter;

import java.util.*;
import at.ac.tuwien.infosys.www.pixy.analysis.inter.callstring.CSContext;

public class ConnectorFunction {

    private Map<CSContext, CSContext> pos2pos;

    private Map<CSContext, Set<CSContext>> reverse;

    public ConnectorFunction() {
        this.pos2pos = new HashMap<CSContext, CSContext>();
        this.reverse = new HashMap<CSContext, Set<CSContext>>();
    }

    public void add(int from, int to) {
        CSContext fromInt = new CSContext(from);
        CSContext toInt = new CSContext(to);
        this.pos2pos.put(fromInt, toInt);
        Set<CSContext> reverseSet = this.reverse.get(toInt);
        if (reverseSet == null) {
            reverseSet = new HashSet<CSContext>();
            reverseSet.add(fromInt);
            this.reverse.put(toInt, reverseSet);
        } else {
            reverseSet.add(fromInt);
        }
    }

    public CSContext apply(int input) {
        CSContext output = (CSContext) this.pos2pos.get(new CSContext(input));
        return output;
    }

    public Set<CSContext> reverseApply(int output) {
        return this.reverse.get(new CSContext(output));
    }

    public String toString() {
        if (this.pos2pos.isEmpty()) {
            return "<empty>";
        }
        StringBuilder myString = new StringBuilder();
        for (Iterator iter = this.pos2pos.entrySet().iterator(); iter.hasNext(); ) {
            Map.Entry entry = (Map.Entry) iter.next();
            CSContext from = (CSContext) entry.getKey();
            CSContext to = (CSContext) entry.getValue();
            myString.append(from);
            myString.append(" -> ");
            myString.append(to);
            myString.append(System.getProperty("line.separator"));
        }
        return myString.substring(0, myString.length() - 1);
    }
}
