package net.sourceforge.geeboss.model.editors.converters;

import net.sourceforge.geeboss.model.editors.Parameter;
import net.sourceforge.geeboss.model.editors.Value;

/**
 * Base class for Converters
 * @author <a href="mailto:fborry@free.fr">Frederic BORRY</a>
 */
public abstract class AbstractConverter implements Converter {

    /** The parameter associated to this converter */
    protected Parameter mParameter;

    /** The value associated to this converter */
    protected Value mValue;

    /**
     * Create a new Converter instance given a Parameter and a Value
     * @param parameter the parameter
     * @param value the value
     */
    public AbstractConverter(Parameter parameter, Value value) {
        mParameter = parameter;
        mValue = value;
    }
}
