package net.sf.myra.cantminer;

import java.util.List;
import net.sf.myra.datamining.data.ContinuousAttribute;
import net.sf.myra.datamining.data.Instance;
import net.sf.myra.datamining.data.IntervalBuilder;
import net.sf.myra.datamining.data.Metadata;
import net.sf.myra.datamining.data.Term;
import net.sf.myra.datamining.data.IntervalBuilder.Interval;
import net.sf.myra.framework.Vertex;

/**
 * Utility class to calculate threshold values for continuous attributes.
 * 
 * @author Fernando Esteban Barril Otero
 * @version $Revision: 2340 $ $Date:: 2011-01-25 06:36:37#$
 */
public class ThresholdHelper {

    /**
	 * The interval builder helper instance.
	 */
    private IntervalBuilder builder;

    /**
	 * Default constructor.
	 * 
	 * @param metadata the current metadata instance.
	 */
    public ThresholdHelper(Metadata metadata) {
        builder = IntervalBuilder.getInstance(metadata);
    }

    /**
	 * Returns a copy of the specified vertex with a continuous attribute
	 * threshold information.
	 * 
	 * @param attribute the continuous attribute name instance.
	 * @param vertex the vertex representing the continuous attribute.
	 * @param instances the instances used in the threshold calculation.
	 * 
	 * @return a copy of the specified vertex with a continuous attribute
	 *         threshold information.
	 */
    public Vertex<Term> create(ContinuousAttribute attribute, Vertex<Term> vertex, List<Instance> instances) {
        return create(attribute, vertex, instances, null);
    }

    /**
	 * Returns a copy of the specified vertex with a continuous attribute
	 * threshold information.
	 * 
	 * @param attribute the continuous attribute name instance.
	 * @param vertex the vertex representing the continuous attribute.
	 * @param instances the instances used in the threshold calculation.
	 * @param value the target class value.
	 * 
	 * @return a copy of the specified vertex with a continuous attribute
	 *         threshold information.
	 */
    public Vertex<Term> create(ContinuousAttribute attribute, Vertex<Term> vertex, List<Instance> instances, String value) {
        Vertex<Term> clone = (Vertex<Term>) vertex.clone();
        Term info = clone.getInfo();
        Interval interval = null;
        if (value == null) {
            interval = builder.createSingle(instances, attribute);
        } else {
            interval = builder.createSingle(instances, attribute, value);
        }
        info.setValues(interval.getValues());
        info.setOperator(interval.getOperator());
        return clone;
    }

    /**
	 * Returns a copy of the specified vertex with a continuous attribute
	 * threshold information.
	 * 
	 * @param attribute the continuous attribute name instance.
	 * @param vertex the vertex representing the continuous attribute.
	 * @param instances the instances used in the threshold calculation.
	 * @param first indicates if the specified vertex is the first of the trail
	 * 
	 * @return a copy of the specified vertex with a continuous attribute
	 *         threshold information.
	 */
    public Vertex<Term> create(ContinuousAttribute attribute, Vertex<Term> vertex, List<Instance> instances, boolean first) {
        if (first && (attribute.getInitial() != null)) {
            Interval interval = attribute.getInitial();
            Vertex<Term> clone = (Vertex<Term>) vertex.clone();
            Term info = clone.getInfo();
            info.setValues(interval.getValues().clone());
            info.setOperator(interval.getOperator());
            return clone;
        } else {
            return create(attribute, vertex, instances, null);
        }
    }
}
