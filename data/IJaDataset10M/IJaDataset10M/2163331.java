package net.sourceforge.processdash.tool.export.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import net.sourceforge.processdash.data.DataContext;
import net.sourceforge.processdash.log.time.TimeLog;

public interface TimeLogExporter {

    public void dumpTimeLogEntries(TimeLog timeLog, DataContext data, Collection filter, OutputStream out) throws IOException;
}
