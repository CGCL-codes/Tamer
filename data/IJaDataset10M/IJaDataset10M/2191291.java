package edu.arizona.cs.learn.timeseries.model.values;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math.stat.descriptive.SummaryStatistics;
import org.dom4j.Element;
import edu.arizona.cs.learn.util.DataMap;
import edu.arizona.cs.learn.util.XMLUtils;

public class Real extends Value {

    private boolean _unknown = false;

    private List<Double> _values;

    private SummaryStatistics _stats;

    /**
	 * Construct a new Real class where Double.NaN = unknown
	 * and otherwise the value is stored as is.
	 * @param value
	 */
    public Real(Integer variableId, double value) {
        super(variableId);
        _values = new ArrayList<Double>();
        _values.add(value);
        _stats = new SummaryStatistics();
        _stats.addValue(value);
        if (Double.compare(Double.NaN, value) == 0) _unknown = true;
    }

    /**
	 * Called by copy to create an exact copy of this
	 * value.
	 * @param uknown
	 * @param values
	 */
    private Real(Integer variableId, boolean uknown, List<Double> values) {
        super(variableId);
        _unknown = false;
        _values = new ArrayList<Double>(values);
        _stats = new SummaryStatistics();
        for (Double d : values) _stats.addValue(d);
    }

    @Override
    public void merge(Value v) {
        if (!(v instanceof Real)) throw new RuntimeException("Cannot merge different types: Real - " + v.getClass().getSimpleName());
        Real r = (Real) v;
        for (Double d : r._values) {
            _values.add(d);
            _stats.addValue(d);
            if (Double.compare(Double.NaN, d) == 0) _unknown = true;
        }
    }

    @Override
    public boolean considerDistance() {
        return true;
    }

    @Override
    public double distance(Value v) {
        if (!(v instanceof Real)) throw new RuntimeException("Distance undefined between: Real - " + v.getClass().getSimpleName());
        Real r = (Real) v;
        return _stats.getMean() - r._stats.getMean();
    }

    @Override
    public boolean unknown() {
        return _unknown;
    }

    @Override
    public Value copy() {
        return new Real(_variableId, _unknown, _values);
    }

    @Override
    public String toString() {
        return "[" + _values.size() + "] - " + _stats.getMean();
    }

    @Override
    public double value() {
        return _stats.getMean();
    }

    @Override
    public double multiply(Value v) {
        if (!(v instanceof Real)) throw new RuntimeException("dot undefined between: Real - " + v.getClass().getSimpleName());
        Real r = (Real) v;
        return _stats.getMean() * r._stats.getMean();
    }

    @Override
    public void toXML(Element e) {
        e.addElement("value").addAttribute("class", Real.class.getSimpleName()).addAttribute("variable", DataMap.getKey(_variableId)).addAttribute("unknown", _unknown + "").addAttribute("values", XMLUtils.toString(_values));
    }

    public static Value fromXML(Element e) {
        String varName = e.attributeValue("variable");
        boolean unknown = Boolean.parseBoolean(e.attributeValue("unknown"));
        String csList = e.attributeValue("values");
        String[] tokens = csList.split("[,]");
        List<Double> values = new ArrayList<Double>();
        for (String tok : tokens) values.add(Double.parseDouble(tok));
        return new Real(DataMap.findOrAdd(varName), unknown, values);
    }
}
