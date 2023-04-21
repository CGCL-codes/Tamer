package simple.xml.util;

import simple.xml.Attribute;
import simple.xml.Root;

/**
 * The <code>Entry</code> object represents entries to the dictionary
 * object. Every entry must have a name attribute, which is used to
 * establish mappings within the <code>Dictionary</code> object. Each
 * entry entered into the dictionary can be retrieved using its name. 
 * <p>
 * The entry can be serialzed with the dictionary to an XML document.
 * Items stored within the dictionary need to extend this entry
 * object to ensure that they can be mapped and serialized with the
 * dictionary. Implementations should override the root annotation.
 *
 * @author Niall Gallagher
 */
@Root(name = "entry")
public abstract class Entry {

    /**
    * Represents the name of the entry instance used for mappings.
    */
    @Attribute(name = "name")
    protected String name;
}
