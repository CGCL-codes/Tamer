package net.sourceforge.olduvai.lrac.logging;

import java.util.List;

/**
 * Provides a standard interface through which log data can be written
 * to a file or to a remote server. 
 * 
 * @author Peter McLachlan <spark343@cs.ubc.ca>
 *
 */
public interface LogWriter {

    /**
	 * Outputs the data through whatever mechanism the implmentation
	 * specifies. 
	 * 
	 * @param data
	 */
    public void writeData(List<LogEntry> data);
}
