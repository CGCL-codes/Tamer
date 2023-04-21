package xxl.core.indexStructures.separators;

import xxl.core.functions.AbstractFunction;
import xxl.core.functions.Function;
import xxl.core.indexStructures.Separator;

/**
 * 
 * An example for {@link xxl.core.indexStructures.Separator} with String values.
 *
 */
public class StringSeparator extends Separator {

    public static final Function<String, Separator> FACTORY_FUNCTION = new AbstractFunction<String, Separator>() {

        public Separator invoke(String key) {
            return new StringSeparator((key == null) ? null : key);
        }
    };

    public StringSeparator(String sepValue) {
        super(sepValue);
    }

    @Override
    public Object clone() {
        return new StringSeparator((isDefinite()) ? (String) this.sepValue : null);
    }

    /**
	 * case insensitive 
	 * @param separator
	 * @return
	 */
    public int compareTo(Object sep) {
        StringSeparator stSep = (StringSeparator) sep;
        if (!stSep.isDefinite() && !this.isDefinite()) return 0;
        if (!stSep.isDefinite()) return -1;
        if (!this.isDefinite()) return 1;
        return ((String) this.sepValue()).compareToIgnoreCase(((String) stSep.sepValue()));
    }
}
