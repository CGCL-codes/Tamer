package org.authorsite.bib.dto;

import java.io.*;

/**
 * <code>AbstractDTO</code> is the base class for all the core DTO types. The use
 * of an abstract super class was motivated by the possibility of using the template
 * method pattern (GOF) and the possible need to treat different DTOs as being of
 * essentially the same type.
 *
 * @author  jejking
 * @version $Revision: 1.3 $
 */
public abstract class AbstractDTO implements Serializable {

    /** Creates a new instance of AbstractDTO */
    public AbstractDTO() {
    }

    /** Accessor method which returns the ID of the DTO.
     * @return An Integer object representing the unique of the object represented by the DTO.
     */
    public abstract Integer getID();
}
