package net.sourceforge.basher.utils;

import net.sourceforge.basher.Phase;
import org.ops4j.gaderian.*;
import org.ops4j.gaderian.internal.Module;
import org.ops4j.gaderian.schema.Translator;

/**
 * @author Johan Lindquist
 */
public class PhaseEnumTranslator implements Translator {

    public Object translate(final Module contributingModule, final Class propertyType, final String inputValue, final Location location) {
        try {
            return Phase.valueOf(inputValue);
        } catch (Exception e) {
            throw new ApplicationRuntimeException("Could not translate input value '" + inputValue + " to Phase enumeration", location, e);
        }
    }
}
