package com.db4o.internal.encoding;

import com.db4o.config.encoding.*;
import com.db4o.foundation.*;

/**
 * @exclude
 */
public class LatinStringEncoding extends BuiltInStringEncoding {

    public String decode(byte[] bytes, int start, int length) {
        throw new NotImplementedException();
    }

    public byte[] encode(String str) {
        throw new NotImplementedException();
    }

    protected LatinStringIO createStringIo(StringEncoding encoding) {
        return new LatinStringIO();
    }
}
