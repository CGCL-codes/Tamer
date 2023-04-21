package com.foursoft.fourever.export.configuration;

import java.util.Set;
import com.foursoft.component.MessageId;

/**
 * A view represents the first step of the export transformation. The available
 * information is reduced to a subset of interest.
 * 
 * @author $author$
 * @version $Revision: 1.12 $
 */
public interface View {

    /**
	 * @return the id of this view
	 */
    public MessageId getName();

    /**
	 * Returns a set of {@link Format} which are associated to this view.
	 * 
	 * @return the set of allowed formats for this view. May return an empty Set
	 *         but not null.
	 */
    public Set<Format> getAllowedFormats();

    /**
	 * Return the default format for this view.
	 * 
	 * @return The default format or null.
	 */
    public Format getDefaultFormat();

    /**
	 * Check whether the given format is one of the possible export formats.
	 * 
	 * @param formatToCheck
	 *            The format to check.
	 * @return True if format is possible, otherwise false.
	 */
    public boolean isFormatAllowed(Format formatToCheck);

    /**
	 * @return the parent group or null if this is a root element
	 */
    public ViewGroup getParentGroup();
}
