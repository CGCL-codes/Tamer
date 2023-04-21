package com.bluebrim.page.shared;

import java.util.Map;
import com.bluebrim.layout.shared.CoLayoutParameters;

/**
 * A <code>CoPageLocation</code> represents an association between a page 
 * and a context that the page belongs to.<br>
 * A page is often part of a context and the page often has a certain place in that
 * context. This fact makes it possible for the context to apply properties for
 * the page e. i. pagenumber, section, department, text and layout parameters etc. <br>
 * 
 * @author G�ran St�ck 2002-09-17
 */
public interface CoPageLocation {

    /**
	 * Put values for the text variables in the specified Map.
	 * For example if the map contains a key "pagenumber" and the context
	 * has a value for that key the value is associated with that key. 
	 */
    public void bindTextVariableValues(Map values);

    /**
	 * Answer true if specified location express the same position as
	 * this location. For example if the supplied location express that
	 * the page is physical page 3 in a context then the method
	 * answer true if our location also express physical page 3 in the same context.
	 */
    public boolean satisfyPagePlaceRequest(CoPageLocation pageLocation);

    /**
	 * Return the name of the page location in the context i. e.
	 * the page number. Can be used when displaying page elements 
	 * in GUI's that handle the context that the page is part of.
	 */
    public String getLocationName();

    /** 
	 * Returns the name of the context for example the name of issue
	 */
    public String getContextName();

    /**
	 * Layout parameters has an override mechanism that makes it possible
	 * to express parameters that is local for certain page locations.
	 */
    public CoLayoutParameters getLayoutParameters();
}
