package fr.ign.cogit.geoxygene.filter;

import org.apache.log4j.Logger;

/**
 * @author Julien Perret
 */
public class PropertyIsLessThanOrEqualTo extends BinaryComparisonOpsType {

    static Logger logger = Logger.getLogger(PropertyIsLessThanOrEqualTo.class.getName());

    @Override
    public boolean evaluate(Object object) {
        Object property = this.getPropertyName().evaluate(object);
        if (property == null) {
            return false;
        }
        if (property instanceof String) {
            if (!this.isMatchCase()) {
                return (String.CASE_INSENSITIVE_ORDER.compare(((String) property), this.getLiteral().getValue()) <= 0);
            }
        }
        if (property instanceof Number) {
            return (((Number) property).doubleValue() <= Double.parseDouble(this.getLiteral().getValue()));
        }
        return property.equals(this.getLiteral());
    }

    @Override
    public String toString() {
        return this.getPropertyName() + " <= " + this.getLiteral().toString();
    }
}
