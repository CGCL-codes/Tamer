package uvw.event.syscalls;

import uvw.event.*;

public class EventOpen extends UberloggerEvent {

    String filename;

    long fd;

    public EventOpen(byte[] data) {
        super(data);
        int i = 0;
        while (data[i + 52] != '\0') {
            i++;
        }
        filename = new String(data, 52, i);
        fd = _result;
    }

    public long getFd() {
        return fd;
    }

    public String getFilename() {
        return filename;
    }
}
