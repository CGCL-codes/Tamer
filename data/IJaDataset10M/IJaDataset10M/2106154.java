package org.xbeans.communication;

import org.xbeans.DOMListener;

public interface XbeanSender extends DOMListener {

    void setId(String id);

    String getId();

    void setCompression(boolean compressionOn);

    boolean getCompression();
}
