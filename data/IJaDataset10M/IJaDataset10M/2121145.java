package org.infoset.xml.memory;

import org.infoset.xml.*;

/**
 *
 * @author R. Alexander Milowski
 */
public class MemoryTypeDefinition implements TypeDefinition {

    Name name;

    /** Creates a new instance of MemoryTypeDefinition */
    public MemoryTypeDefinition(Name name) {
        this.name = name;
    }

    public Name getName() {
        return name;
    }
}
