package edu.arizona.cs.learn.timeseries.model.values;

import java.util.ArrayList;
import java.util.List;
import org.dom4j.Element;
import edu.arizona.cs.learn.util.DataMap;
import edu.arizona.cs.learn.util.XMLUtils;

public class Binary extends Value {

    public static final int TRUE = 1;

    public static final int FALSE = 0;

    public static final int UNKNOWN = -1;

    private Integer _uniqueValue;

    private List<Integer> _values;

    /**
	 * Construct a new Binary class where 1 = true, 0 = false
	 * and -1 = unknown
	 * @param value
	 */
    public Binary(Integer variableId, int value) {
        super(variableId);
        _uniqueValue = value;
        _values = new ArrayList<Integer>();
        _values.add(value);
    }

    /**
	 * Called by the copy method to construct an exact
	 * copy of this Value.
	 * @param uniqueValue
	 * @param values
	 */
    private Binary(Integer variableId, Integer uniqueValue, List<Integer> values) {
        super(variableId);
        _uniqueValue = uniqueValue;
        _values = new ArrayList<Integer>(values);
    }

    @Override
    public void merge(Value v) {
        if (!(v instanceof Binary)) throw new RuntimeException("Cannot merge different types: Binary - " + v.getClass().getSimpleName());
        Binary b = (Binary) v;
        _values.addAll(b._values);
        if (_uniqueValue != b._uniqueValue) {
            _uniqueValue = UNKNOWN;
        }
    }

    @Override
    public boolean considerDistance() {
        return !unknown();
    }

    @Override
    public double distance(Value v) {
        if (!(v instanceof Binary)) return Double.NaN;
        Binary b = (Binary) v;
        if (b._uniqueValue == _uniqueValue) return 0.0;
        return 1;
    }

    @Override
    public boolean unknown() {
        return _uniqueValue == UNKNOWN;
    }

    @Override
    public Value copy() {
        return new Binary(_variableId, _uniqueValue, _values);
    }

    @Override
    public String toString() {
        return "[" + _values.size() + "] -- " + _uniqueValue;
    }

    @Override
    public double value() {
        return _uniqueValue;
    }

    @Override
    public double multiply(Value v) {
        if (!(v instanceof Binary)) throw new RuntimeException("Cannot take dot of different types: Binary - " + v.getClass().getSimpleName());
        Binary b = (Binary) v;
        return b._uniqueValue * _uniqueValue;
    }

    @Override
    public void toXML(Element e) {
        e.addElement("value").addAttribute("class", Binary.class.getSimpleName()).addAttribute("variable", DataMap.getKey(_variableId)).addAttribute("value", _uniqueValue + "").addAttribute("values", XMLUtils.toString(_values));
    }

    public static Value fromXML(Element e) {
        String varName = e.attributeValue("variable");
        int value = Integer.parseInt(e.attributeValue("value"));
        String csList = e.attributeValue("values");
        String[] tokens = csList.split("[,]");
        List<Integer> values = new ArrayList<Integer>();
        for (String tok : tokens) values.add(Integer.parseInt(tok));
        return new Binary(DataMap.findOrAdd(varName), value, values);
    }
}
