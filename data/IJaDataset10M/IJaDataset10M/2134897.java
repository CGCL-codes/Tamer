package org.nakedobjects.metamodel.value;

import java.text.DecimalFormat;
import org.nakedobjects.metamodel.adapter.InvalidEntryException;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.value.CharValueFacet;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;
import org.nakedobjects.metamodel.specloader.SpecificationLoader;

public abstract class CharValueSemanticsProviderAbstract extends ValueSemanticsProviderAbstract implements CharValueFacet {

    private static Class<? extends Facet> type() {
        return CharValueFacet.class;
    }

    private static final boolean IMMUTABLE = true;

    private static final boolean EQUAL_BY_CONTENT = true;

    private static final int TYPICAL_LENGTH = 1;

    public CharValueSemanticsProviderAbstract(final FacetHolder holder, final Class<?> adaptedClass, final Object defaultValue, final NakedObjectConfiguration configuration, final SpecificationLoader specificationLoader, final RuntimeContext runtimeContext) {
        super(type(), holder, adaptedClass, TYPICAL_LENGTH, IMMUTABLE, EQUAL_BY_CONTENT, defaultValue, configuration, specificationLoader, runtimeContext);
    }

    @Override
    public Object doParse(final Object original, final String entry) {
        if (entry.length() > 1) {
            throw new InvalidEntryException("Only a single character is required");
        } else {
            return Character.valueOf(entry.charAt(0));
        }
    }

    @Override
    public String titleString(final Object value) {
        return value == null ? "" : value.toString();
    }

    @Override
    public String titleStringWithMask(final Object value, final String usingMask) {
        return titleString(new DecimalFormat(usingMask), value);
    }

    @Override
    protected String doEncode(final Object object) {
        return object.toString();
    }

    @Override
    protected Object doRestore(final String data) {
        return Character.valueOf(data.charAt(0));
    }

    public Character charValue(final NakedObject object) {
        return object == null ? null : (Character) object.getObject();
    }

    public NakedObject createValue(final Character value) {
        return getRuntimeContext().adapterFor(value);
    }

    @Override
    public String toString() {
        return "CharacterValueSemanticsProvider";
    }
}
