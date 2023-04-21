package com.siemens.ct.exi;

import java.io.InputStream;
import java.io.OutputStream;

public abstract class AbstractTestEncoder extends AbstractTestCoder {

    public abstract void encodeTo(InputStream xmlInput, OutputStream exiOutput) throws Exception;
}
