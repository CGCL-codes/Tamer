package org.processmining.analysis.heuristics;

import javax.swing.*;
import org.processmining.analysis.AnalysisInputItem;
import org.processmining.analysis.AnalysisPlugin;
import org.processmining.framework.models.heuristics.*;
import org.processmining.framework.plugin.ProvidedObject;
import org.processmining.mining.geneticmining.util.MethodsOverIndividuals;

public class HNStructuralPropertySummary implements AnalysisPlugin {

    public HNStructuralPropertySummary() {
    }

    public String getName() {
        return "HN Property Summary";
    }

    public String getHtmlDescription() {
        return "This plug-ins summarizes how many activities, labels and arcs a Heuristic Net contains.";
    }

    public AnalysisInputItem[] getInputItems() {
        AnalysisInputItem aanalysisinputitem[] = { new AnalysisInputItem("Individual") {

            public boolean accepts(ProvidedObject providedobject) {
                Object aobj[] = providedobject.getObjects();
                boolean flag = false;
                for (int i = 0; i < aobj.length; i++) if (aobj[i] instanceof HeuristicsNet) flag = true;
                return flag;
            }

            final HNStructuralPropertySummary this$0;

            {
                this$0 = HNStructuralPropertySummary.this;
            }
        } };
        return aanalysisinputitem;
    }

    public JComponent analyse(AnalysisInputItem aanalysisinputitem[]) {
        JTextArea jtextarea = null;
        Object aobj[] = aanalysisinputitem[0].getProvidedObjects()[0].getObjects();
        HeuristicsNet heuristicsnet = null;
        for (int i = 0; i < aobj.length; i++) if (aobj[i] instanceof HeuristicsNet) heuristicsnet = MethodsOverIndividuals.removeDanglingElementReferences((HeuristicsNet) aobj[i]);
        String s = calculateStructuralPropertySummary(heuristicsnet);
        jtextarea = new JTextArea(s);
        jtextarea.setEditable(false);
        return new JScrollPane(jtextarea);
    }

    private String calculateStructuralPropertySummary(HeuristicsNet heuristicsnet) {
        StringBuffer stringbuffer = new StringBuffer();
        int i = 0;
        int j = 0;
        for (int k = 0; k < heuristicsnet.getOutputSets().length; k++) {
            if (heuristicsnet.getInputSet(k).size() > 0 || heuristicsnet.getOutputSet(k).size() > 0) i++;
            for (int l = 0; l < heuristicsnet.getOutputSet(k).size(); l++) {
                for (int i1 = 0; i1 < heuristicsnet.getOutputSet(k).get(l).size(); i1++) {
                    int j1 = heuristicsnet.getOutputSet(k).get(l).get(i1);
                    j += heuristicsnet.getInputSetsWithElement(j1, k).size();
                }
            }
        }
        stringbuffer.append("The selected Heuristic net has the following properties: \n\n");
        stringbuffer.append((new StringBuilder()).append("Amount of labels: ").append(heuristicsnet.getReverseDuplicatesMapping().length).append("\n\n").toString());
        stringbuffer.append((new StringBuilder()).append("Amount of activities: ").append(i).append("\n\n").toString());
        stringbuffer.append((new StringBuilder()).append("Amount of arc: ").append(j).append("\n\n").toString());
        return stringbuffer.toString();
    }
}
