package org.warko.app;

import org.warko.app.listener.StreamApplicationListener;

public interface ApplicationStream extends ApplicationObject {

    public byte[] getData();

    public int available();

    public int getDataLength();

    public void addListener(StreamApplicationListener l);

    public void removeListener(StreamApplicationListener l);
}
