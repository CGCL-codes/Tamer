package com.once.log;

import java.util.HashMap;

class StartLogEntry extends LogEntry implements ILogEntry {

    StartLogEntry(String user, String location, String checkPoint, String debugData) {
        super(user, location, checkPoint, ILogEntry.TYPE_START, debugData, null);
    }

    StartLogEntry(String user, String location, String debugData) {
        super(user, location, null, ILogEntry.TYPE_START, debugData, null);
    }

    StartLogEntry(String user, String location, String debugData, HashMap<String, String> headers) {
        super(user, location, null, ILogEntry.TYPE_START, debugData, headers);
    }
}
