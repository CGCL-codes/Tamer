package fr.ign.cogit.geoxygene.filter;

import org.apache.log4j.Logger;

/**
 * @author GTouya
 */
public class PropertyIsNull extends BinaryComparisonOpsType {

    static Logger logger = Logger.getLogger(PropertyIsNull.class.getName());

    @Override
    public boolean evaluate(Object object) {
        Object property = this.getPropertyName().evaluate(object);
        if (property == null) return true;
        return false;
    }
}
