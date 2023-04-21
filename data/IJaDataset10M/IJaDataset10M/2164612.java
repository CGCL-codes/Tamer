package org.panopticode.java.ant;

import org.panopticode.rdf.PanopticodeRDFStore;
import java.util.LinkedList;
import java.util.List;

public class BuildBreakers {

    private List<SPARQLBuildBreakerDefinition> buildBreakers = new LinkedList<SPARQLBuildBreakerDefinition>();

    public void addSPARQLBuildBreaker(SPARQLBuildBreakerDefinition buildBreakerDef) {
        buildBreakers.add(buildBreakerDef);
    }

    public void execute(PanopticodeRDFStore store) {
        for (SPARQLBuildBreakerDefinition buildBreaker : buildBreakers) {
            long start = System.currentTimeMillis();
            buildBreaker.execute(store);
            System.out.println("SPARQLBuildBreaker executed in " + (System.currentTimeMillis() - start) + "ms");
        }
    }
}
