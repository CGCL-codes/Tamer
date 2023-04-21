package com.pentagaia.eclipse.sgs.prjconf;

import java.util.Map;

/**
 * A configured maven repository
 * 
 * @author mepeisen
 */
public interface IMavenRepository {

    /**
     * Returns the id of this repository
     * 
     * @return id of this repository
     */
    String getId();

    /**
     * Returns a displayable name of this repository
     * 
     * @return displayable name
     */
    String getName();

    /**
     * Returns the url where to find the repository
     * 
     * @return url where to find the repository
     */
    String getUrl();

    /**
     * returns the layout of this repository
     * 
     * @return repository layout
     */
    String getLayout();

    /**
     * A map containing additional properties
     * 
     * @return additional properties
     */
    Map<String, String> getAdditionalProperties();
}
