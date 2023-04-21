package org.iwtemplatingj.output;

import org.iwtemplatingj.DataCaptureNode;

public interface DataCaptureNodeStringifier<T extends DataCaptureNode> {

    public String convert(T node);

    public String getFullDocumentAsString(T node);
}
