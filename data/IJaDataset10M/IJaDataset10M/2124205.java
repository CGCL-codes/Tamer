package org.nakedobjects.nof.reflect.java.value;

import org.nakedobjects.applib.capabilities.EncoderDecoder;
import org.nakedobjects.applib.capabilities.Parser;
import org.nakedobjects.applib.value.MultilineString;
import org.nakedobjects.noa.adapter.NakedObject;
import org.nakedobjects.noa.adapter.value.MultilineStringValueFacet;
import org.nakedobjects.noa.facets.FacetHolder;
import org.nakedobjects.nof.core.persist.PersistorUtil;

public class MultilineStringValueSemanticsProvider extends ValueSemanticsProviderAbstract implements MultilineStringValueFacet {

    public static Class type() {
        return MultilineStringValueFacet.class;
    }

    private static final int TYPICAL_LENGTH = 200;

    private static final boolean IMMUTABLE = true;

    private static final boolean EQUAL_BY_CONTENT = true;

    private static final Object DEFAULT_VALUE = null;

    /**
     * Required because implementation of {@link Parser} and {@link EncoderDecoder}.
     */
    public MultilineStringValueSemanticsProvider() {
        this(null);
    }

    public MultilineStringValueSemanticsProvider(final FacetHolder holder) {
        super(type(), holder, MultilineString.class, TYPICAL_LENGTH, IMMUTABLE, EQUAL_BY_CONTENT, DEFAULT_VALUE);
    }

    @Override
    protected Object doParse(final Object original, final String text) {
        return new MultilineString(text);
    }

    @Override
    public String titleString(final Object value) {
        return value == null ? "" : stringValue(value);
    }

    @Override
    protected String doEncode(final Object object) {
        final String text = stringValue(object);
        if (text.equals("NULL") || isEscaped(text)) {
            return escapeText(text);
        } else {
            return text;
        }
    }

    @Override
    protected Object doRestore(final String data) {
        if (isEscaped(data)) {
            return new MultilineString(data.substring(1));
        } else {
            return new MultilineString(data);
        }
    }

    private boolean isEscaped(final String text) {
        return text.startsWith("/");
    }

    private String escapeText(final String text) {
        return "/" + text;
    }

    public NakedObject createValue(final String value) {
        return PersistorUtil.createAdapter(new MultilineString(value));
    }

    public String stringValue(final NakedObject object) {
        return object == null ? null : stringValue(object.getObject());
    }

    private String stringValue(final Object object) {
        return ((MultilineString) object).getString();
    }
}
