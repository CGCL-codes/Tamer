package com.volantis.mcs.dissection.dom.impl;

import com.volantis.mcs.dissection.DissectionException;
import com.volantis.mcs.dissection.dom.impl.TString;
import com.volantis.mcs.dissection.dom.impl.TStringContext;
import java.io.Writer;

public interface TStringWriter {

    /**
     * The copyright statement.
     */
    static String mark = "(c) Volantis Systems Ltd 2003.";

    public void write(TStringContext context, TString string, Writer writer) throws DissectionException;
}
