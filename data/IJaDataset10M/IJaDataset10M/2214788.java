package com.siemens.ct.exi;

import java.io.InputStream;
import java.io.OutputStream;

public abstract class AbstractTestDecoder extends AbstractTestCoder {

    public abstract void decodeTo(InputStream exiDocument, OutputStream xmlOutput) throws Exception;
}
