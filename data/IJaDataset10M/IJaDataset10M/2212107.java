package net.sf.simplelogviewer.plugin;

import java.util.Iterator;

/**
 * Represents parsed log data.
 * 
 * @author Hvan Konstantin Vladimirovich (dotidot)
 */
public interface ILog {

    /**
	 * @return Log format description.
	 */
    ILogFormat getLogFormat();

    /**
	 * Return iterator over entries in this log.
	 * 
	 * @return Iterator over entries in this log.
	 */
    Iterator<ILogEntry> iterator();
}
