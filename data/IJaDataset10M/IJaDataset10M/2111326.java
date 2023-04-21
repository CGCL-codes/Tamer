package javasrc.symtab;

import java.util.*;

/*******************************************************************************
 * This interface is used as a handle to all classes that can be tagged
 ******************************************************************************/
interface Taggable {

    /** Create tag information about an object */
    void generateTags(HTMLTagContainer tagList);
}
