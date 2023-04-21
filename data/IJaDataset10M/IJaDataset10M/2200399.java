package org.fudaa.fudaa.tr.common;

import org.fudaa.dodico.telemac.io.ScopeTFileFormat;

public class TrWriterScopeT extends TrWriterScopeS {

    ScopeTFileFormat format_;

    String extension_ = ".scopT";

    public TrWriterScopeT() {
        format_ = ScopeTFileFormat.getInstance();
        filter_ = format_.createFileFilter();
    }
}
