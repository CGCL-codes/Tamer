package com.ail.core.persistence;

import com.ail.core.Type;
import com.ail.core.command.CommandArg;

/**
 * Arguments required by delete service
 * @version $Revision: 1.1 $
 * @state $State: Exp $
 * @date $Date: 2006/07/15 15:01:44 $
 * @source $Source: /home/bob/CVSRepository/projects/core/core.ear/core.jar/com/ail/core/persistence/DeleteArg.java,v $
 * @stereotype arg
 */
public interface DeleteArg extends CommandArg {

    /**
	 * Setter for the objectArg property. * @see #getObjectArg
	 * @param objectArg new value for property.
	 */
    void setObjectArg(Type objectArg);

    /**
     * Getter for the objectArg property. Object to persist
     * @return Value of objectArg, or null if it is unset
     */
    Type getObjectArg();
}
