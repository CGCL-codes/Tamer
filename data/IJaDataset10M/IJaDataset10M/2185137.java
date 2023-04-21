package org.hl7.types.impl;

import org.hl7.types.ANY;
import org.hl7.types.BL;
import org.hl7.types.CS;
import org.hl7.types.ST;

/**
 * Adapter for java.net.URL to the org.hl7.types.URL interface.
 * 
 * FIXME: There are a couple of quirks:
 *  - the j.n.URL has no way of getting at an opaque part (i.e. everything after the scheme, and I'm not sure we can
 * reassemble that from the URL accessors.
 *  - given that it's so hard to get at the opaque part, the equals implementation may not be ideal.
 */
public class URLforTELimpl extends ANYimpl implements org.hl7.types.URL {

    String _data;

    protected URLforTELimpl(String data) {
        super(null);
        if (data == null) throw new IllegalArgumentException(); else this._data = data;
    }

    public static org.hl7.types.URL valueOf(String data) {
        if (data == null) return URLnull.NI; else return new URLforTELimpl(data);
    }

    public final BL equal(ANY that) {
        if (that instanceof org.hl7.types.URL) {
            if (that instanceof URLforTELimpl) {
                return BLimpl.valueOf(this._data.equals(((URLforTELimpl) that)._data));
            }
        }
        return BLimpl.FALSE;
    }

    public CS scheme() {
        return CSnull.NA;
    }

    public ST address() {
        if (this.isNullJ() && _data == null) return STnull.NA; else {
            return STjlStringAdapter.valueOf(_data);
        }
    }

    public String toString() {
        return _data;
    }
}
