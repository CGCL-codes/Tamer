package org.japano.metadata;

import java.util.List;
import org.japano.action.Parameter;

/**
 Common interface to metadata tags for xjavadoc and annotation metadata.
 
 @author Sven Helmberger ( sven dot helmberger at gmx dot de )
 @version $Id: MetadataTag.java,v 1.8 2005/09/27 21:30:51 fforw Exp $
 #SFLOGO# 
 */
public interface MetadataTag {

    /**
   Returns the tag attribute value with the given name or <code>null</code> if no such
   attribute is set.
   @param name attribute name
   @return value or <code>null</code>
   */
    public String getAttribute(String name);

    /**
   Returns the tag attribute value with the given name or <code>null</code> if no such
   attribute is set.
   @param name attribute name
   @return value or <code>null</code>
   */
    public String[] getAttributes(String name);

    /**
   Returns a list with siblings of this tag of the given type.
   @param type tag type
   @return list
   */
    public List getSiblings(TagType type);

    /**
   Returns the first sibling of the given type.
   @param type tag type
   @return list
   */
    public MetadataTag getSibling(TagType type);

    /**
   Generates an error message with this tag's start as error position.
   */
    public void error(String message);

    /**
   Returns the japano tag type for this metadata tag.
   @return tag type      
   @see TagType
   */
    TagType getType();
}
