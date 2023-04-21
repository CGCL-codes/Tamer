package de.tuc.in.sse.weit.export.steuerung.configuration;

import de.tuc.in.sse.weit.export.steuerung.configuration.Format;
import de.tuc.in.sse.weit.export.steuerung.configuration.Group;

/**
 * A Format describes an output format, e.g. PDF or HTML. *
 * 
 * @author sihling
 */
public interface Format {

    /**
	 * Returns the id of this format.
	 * 
	 * @return the id of this format
	 */
    public String getName();

    /**
	 * Gets the specified file extension. A specific file extension is
	 * "directory" bearing the meaning that the export is to be stored to a
	 * given directory.
	 * 
	 * @return the specified extension
	 */
    public String getExtension();

    /**
	 * Returns the parent group or null if this is a root element.
	 * 
	 * @return the parent group or null if this is a root element
	 */
    public Group<Format> getParentGroup();

    /**
	 * Returns the additional information associated with this output format.
	 * 
	 * @return the info message
	 */
    public String getInfo();
}
