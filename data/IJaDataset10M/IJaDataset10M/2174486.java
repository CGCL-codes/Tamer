package org.jkva.makebuilder.core;

import javax.lang.model.element.TypeElement;
import java.util.List;
import java.util.Set;

/**
 * This class is responsible for reading all necessary information from a given Java class.
 *
 * $Author$
 * $Revision$
 */
public interface ClassParser {

    /**
     * Read the necessary meta data from the given type.
     *
     * @param typeElement The type to inspect.
     * @return The meta data for this type.
     */
    ClassMetaData readMetaData(TypeElement typeElement);
}
