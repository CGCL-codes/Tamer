package org.nakedobjects.metamodel.value;

import java.text.DecimalFormat;
import org.nakedobjects.applib.adapters.EncoderDecoder;
import org.nakedobjects.applib.adapters.Parser;
import org.nakedobjects.applib.value.Color;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.TextEntryParseException;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.value.ColorValueFacet;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;
import org.nakedobjects.metamodel.specloader.SpecificationLoader;

public class ColorValueSemanticsProvider extends ValueSemanticsProviderAbstract implements ColorValueFacet {

    public static Class<? extends Facet> type() {
        return ColorValueFacet.class;
    }

    private static final int TYPICAL_LENGTH = 4;

    private static final boolean IMMUTABLE = true;

    private static final boolean EQUAL_BY_CONTENT = false;

    private static final Object DEFAULT_VALUE = Color.BLACK;

    /**
     * Required because implementation of {@link Parser} and {@link EncoderDecoder}.
     */
    public ColorValueSemanticsProvider() {
        this(null, null, null, null);
    }

    public ColorValueSemanticsProvider(final FacetHolder holder, final NakedObjectConfiguration configuration, final SpecificationLoader specificationLoader, final RuntimeContext runtimeContext) {
        super(type(), holder, Color.class, TYPICAL_LENGTH, IMMUTABLE, EQUAL_BY_CONTENT, DEFAULT_VALUE, configuration, specificationLoader, runtimeContext);
    }

    @Override
    protected Object doParse(final Object original, final String text) {
        try {
            if (text.startsWith("0x")) {
                return new Color(Integer.parseInt(text.substring(2), 16));
            } else if (text.startsWith("#")) {
                return new Color(Integer.parseInt(text.substring(1), 16));
            } else {
                return new Color(Integer.parseInt(text));
            }
        } catch (final NumberFormatException e) {
            throw new TextEntryParseException("Not a number " + text, e);
        }
    }

    @Override
    public String titleString(final Object object) {
        final Color color = (Color) object;
        return color.title();
    }

    public String titleStringWithMask(final Object object, final String usingMask) {
        final Color color = (Color) object;
        return titleString(new DecimalFormat(usingMask), color.intValue());
    }

    @Override
    protected String doEncode(final Object object) {
        final Color color = (Color) object;
        return Integer.toHexString(color.intValue());
    }

    @Override
    protected Object doRestore(final String data) {
        return new Color(Integer.parseInt(data, 16));
    }

    public int colorValue(final NakedObject object) {
        if (object == null) {
            return 0;
        }
        final Color color = (Color) object.getObject();
        return color.intValue();
    }

    public NakedObject createValue(final NakedObject object, final int colorAsInt) {
        final Color color = new Color(colorAsInt);
        return getRuntimeContext().adapterFor(color);
    }

    @Override
    public String toString() {
        return "ColorValueSemanticsProvider";
    }
}
