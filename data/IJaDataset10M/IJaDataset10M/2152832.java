package uk.ac.rothamsted.ovtk.Layouter;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import uk.ac.rothamsted.ovtk.Console.Console;
import uk.ac.rothamsted.ovtk.Graph.Concept;
import uk.ac.rothamsted.ovtk.Graph.Concept_Class;
import uk.ac.rothamsted.ovtk.Graph.ONDEXGraph;

public class FastCircleLayouter extends GeneralLayouter {

    private int radius = 50;

    private int bigradius = 200;

    private final int distance = 30;

    private final double PI = Math.PI;

    public FastCircleLayouter() {
        super.name = "FastCircleLayouter";
        super.valueDescriptions.add(0, new String("radius"));
        super.valueDescriptions.add(1, new String("big radius"));
        super.standardValues.add(0, "" + radius);
        super.standardValues.add(1, "" + bigradius);
    }

    public void setValues(Vector values) {
        this.radius = Integer.parseInt((String) values.get(0));
        this.bigradius = Integer.parseInt((String) values.get(1));
    }

    public void applyLayout(ONDEXGraph graph) {
        double start = System.currentTimeMillis();
        Console.println(2, "Applying FastCircleLayout to Graph.");
        Console.startProgress("Applying FastCircleLayout to Graph.");
        Hashtable conceptClasses = new Hashtable();
        Iterator e = graph.getGraphData().getConcept_classes().values().iterator();
        while (e.hasNext()) {
            Concept_Class cc = (Concept_Class) e.next();
            Vector visibleConcepts = new Vector();
            conceptClasses.put(cc.getId(), visibleConcepts);
        }
        Object[] concepts = graph.getConceptIDs().values().toArray();
        for (int l = 0; l < concepts.length; l++) {
            Concept c = (Concept) concepts[l];
            if (c.isVisible()) {
                Vector cc = (Vector) conceptClasses.get(c.getOf_type_FK().getId());
                cc.add(c);
            }
        }
        e = conceptClasses.values().iterator();
        int count_circles = 0;
        while (e.hasNext()) {
            Vector cc = (Vector) e.next();
            if (cc.size() > 0) {
                count_circles++;
            }
        }
        Console.println(2, "number of circles=" + count_circles);
        e = conceptClasses.values().iterator();
        int r = this.radius;
        int bigr = this.bigradius;
        double bigangle = (2 * PI) / count_circles;
        int actual_circle = -1;
        while (e.hasNext()) {
            Vector cc = (Vector) e.next();
            if (cc.size() > 0) {
                actual_circle++;
                double actualx = distance + bigr + bigr * Math.cos(actual_circle * bigangle);
                double actualy = distance + bigr + bigr * Math.sin(actual_circle * bigangle);
                int n = cc.size();
                double mx = actualx + r;
                double my = actualy + r;
                double angle = (2 * PI) / n;
                for (int i = 0; i < n; i++) {
                    Concept c = (Concept) cc.get(i);
                    c.setX(mx + r * Math.cos(i * angle));
                    c.setY(my + r * Math.sin(i * angle));
                }
            }
        }
        Console.println(2, "Applying FastCircleLayout finished. - " + (System.currentTimeMillis() - start) / 1000 + " s");
        Console.stopProgress();
    }
}
