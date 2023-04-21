package org.nakedobjects.nof.reflect.java.value;

import org.nakedobjects.applib.capabilities.EncoderDecoder;
import org.nakedobjects.applib.capabilities.Parser;
import org.nakedobjects.noa.adapter.TextEntryParseException;
import org.nakedobjects.noa.adapter.value.BigIntegerValueFacet;
import org.nakedobjects.noa.facets.FacetHolder;
import org.nakedobjects.nof.core.conf.Configuration;
import org.nakedobjects.nof.core.context.NakedObjectsContext;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class BigIntegerValueSemanticsProvider extends ValueSemanticsProviderAbstract implements BigIntegerValueFacet {

    private static final int TYPICAL_LENGTH = 19;

    private static Class type() {
        return BigIntegerValueFacet.class;
    }

    private static final NumberFormat DEFAULT_FORMAT = NumberFormat.getNumberInstance();

    private static final boolean IMMUTABLE = true;

    private static final boolean EQUAL_BY_CONTENT = true;

    private static final Object DEFAULT_VALUE = BigInteger.valueOf(0);

    private NumberFormat format;

    /**
     * Required because implementation of {@link Parser} and {@link EncoderDecoder}.
     */
    public BigIntegerValueSemanticsProvider() {
        this(null);
    }

    public BigIntegerValueSemanticsProvider(final FacetHolder holder) {
        super(type(), holder, BigInteger.class, TYPICAL_LENGTH, IMMUTABLE, EQUAL_BY_CONTENT, DEFAULT_VALUE);
        final String formatRequired = NakedObjectsContext.getConfiguration().getString(Configuration.ROOT + "value.format.int");
        if (formatRequired == null) {
            format = DEFAULT_FORMAT;
        } else {
            format = new DecimalFormat(formatRequired);
        }
    }

    @Override
    protected Object doParse(final Object original, final String entry) {
        try {
            return new BigInteger(entry);
        } catch (final NumberFormatException e) {
            throw new TextEntryParseException("Not an integer " + entry, e);
        }
    }

    @Override
    public String titleString(final Object object) {
        return titleString(format, object);
    }

    public String titleWithMask(final String mask, final Object value) {
        return titleString(new DecimalFormat(mask), value);
    }

    @Override
    protected String doEncode(final Object object) {
        return object.toString();
    }

    @Override
    protected Object doRestore(final String data) {
        return new BigInteger(data);
    }

    @Override
    public String toString() {
        return "BigIntegerValueSemanticsProvider: " + format;
    }
}
