package nz.ac.nassey.cs.barrio.interfaceFilter;

import edu.uci.ics.jung.graph.Vertex;
import nz.ac.massey.cs.barrio.filters.NodeFilter;

public class InterfaceFilter extends NodeFilter {

    public boolean acceptVertex(Vertex v) {
        String isInterface = v.getUserDatum("class.isInterface").toString();
        if (isInterface.equals("true")) return false;
        return true;
    }

    public String getName() {
        return "Interfaces";
    }
}
