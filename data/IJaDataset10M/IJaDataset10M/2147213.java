package royere.cwi.structure;

import royere.cwi.framework.PropertyInfo;
import royere.cwi.framework.Expression;
import royere.cwi.framework.Keys;
import java.util.Iterator;
import java.util.Comparator;
import java.util.TreeSet;
import java.util.Set;

/** 
* StrahlerMetric originally applies to trees and measures
* the flow of information going through a node and going
* towards the root. Its definition makes it possible to compute
* a value for nodes in a dag based on the same formula. The version
* we implement here is from Fedou and Delest defined on trees (number of registers ...).
* We assume that the graph we deal with is a dag (directed + no cycles).
* @author: Scott, March 1999.
* @author Guy Melan�on, July 1999 (revision).
*/
public class StrahlerMetric extends Metric {

    public static final String Name = "Strahler Metric";

    public StrahlerMetric() {
        super();
    }

    public StrahlerMetric(Element element, Graph graph) {
        super(element, graph);
    }

    /** setRequirements is called for us by super() */
    public Expression setRequirements() {
        Expression requirements = Expression.addRequirement(null, Keys.ISACYCLIC, Boolean.TRUE);
        requirements = Expression.addRequirement(requirements, Keys.ISDIRECTED, Boolean.TRUE);
        return requirements;
    }

    /** Implements getName() */
    public String getName() {
        return Name;
    }

    public MetricValue calculateValue() {
        if (element instanceof Node) {
            Iterator successors = ((Node) element).getSuccessors(this.graph);
            if (!successors.hasNext()) {
                metricValue = new MetricDouble();
                metricValue.setValue(new MetricDouble(1.0));
            } else {
                MetricComparator comparator = new MetricComparator(StrahlerMetric.Name, this.graph);
                TreeSet succList = new TreeSet(comparator);
                while (successors.hasNext()) {
                    Node node = (Node) successors.next();
                    succList.add(node);
                }
                successors = succList.iterator();
                Node aSuccessor = (Node) successors.next();
                Metric aSuccStrahlerMetric = aSuccessor.getOrMakeMetric(StrahlerMetric.Name, this.graph);
                MetricValue baseStrahler = aSuccStrahlerMetric.getOrCalculateValue();
                MetricDouble available = new MetricDouble();
                available.setValue(baseStrahler);
                MetricDouble one = new MetricDouble(1.0);
                available.subtract(one);
                MetricDouble additional = new MetricDouble(0.0);
                while (successors.hasNext()) {
                    aSuccessor = (Node) successors.next();
                    aSuccStrahlerMetric = aSuccessor.getOrMakeMetric(StrahlerMetric.Name, this.graph);
                    MetricValue succStrahlerValue = aSuccStrahlerMetric.getOrCalculateValue();
                    if (succStrahlerValue.greaterThan(available)) {
                        additional.add(succStrahlerValue);
                        additional.subtract(available);
                        available.setValue(succStrahlerValue);
                        available.subtract(one);
                    } else {
                        available.subtract(one);
                    }
                }
                metricValue = new MetricDouble();
                metricValue.setValue(baseStrahler);
                metricValue.add(additional);
            }
            return metricValue;
        } else {
            return (MetricValue) null;
        }
    }

    /**
   * Implements calculateGlobally. Uses a layer by layer traversal of nodes,
   * starting with the nodes at the top of the graph. Edges are <i>not</i> assigned a value.
   */
    public void calculateGlobally() {
        OrderedSet currentLayer = this.graph.getEntryNodesSet();
        OrderedSet nextLayer = new OrderedSet();
        while (!currentLayer.isEmpty()) {
            nextLayer.clear();
            Iterator currentLayerIter = currentLayer.iterator();
            while (currentLayerIter.hasNext()) {
                Node node = (Node) currentLayerIter.next();
                Metric nodeMetric = node.getOrMakeMetric(this.getName(), this.graph);
                MetricValue nodeMetricValue = nodeMetric.getOrCalculateValue();
                int outDegree = node.getOutgoingDegree(this.graph);
                double outEdgesValue = ((Double) nodeMetricValue.getValue()).doubleValue() / outDegree;
                Iterator outEdges = node.getOutgoingEdges(this.graph);
            }
            currentLayer.clear();
            currentLayer.addAll(nextLayer);
        }
    }
}
