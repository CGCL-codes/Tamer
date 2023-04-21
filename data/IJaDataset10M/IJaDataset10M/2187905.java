package org.slosc.rest.core;

import javax.ws.rs.core.Variant;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Locale;

/**
 * @author : Lilantha Darshana (lilantha_os@yahoo.com)
 *         Date    : Dec 13, 2008
 * @version: 1.0
 */
public class VariantListBuilderImpl extends Variant.VariantListBuilder {

    public List<Variant> build() {
        throw new UnsupportedOperationException("build()");
    }

    public Variant.VariantListBuilder add() {
        throw new UnsupportedOperationException("add()");
    }

    public Variant.VariantListBuilder languages(Locale... languages) {
        throw new UnsupportedOperationException("languages()");
    }

    public Variant.VariantListBuilder encodings(String... encodings) {
        throw new UnsupportedOperationException("encodings()");
    }

    public Variant.VariantListBuilder mediaTypes(MediaType... mediaTypes) {
        throw new UnsupportedOperationException("mediaTypes()");
    }
}
