package com.ail.core.persistence;

import com.ail.annotation.ServiceArgument;
import com.ail.annotation.ServiceCommand;
import com.ail.annotation.ServiceInterface;
import com.ail.core.Type;
import com.ail.core.command.Argument;
import com.ail.core.command.Command;
import com.ail.core.persistence.hibernate.HibernateUpdateService;

/**
 * Arguments required by create service
 */
@ServiceInterface
public interface UpdateService {

    /**
     * Arguments required by create service
     */
    @ServiceArgument
    public interface UpdateArgument extends Argument {

        /**
         * Setter for the objectArg property. * @see #getObjectArg
         * @param objectArg new value for property.
         */
        void setObjectArg(Type objectArg);

        /**
         * Getter for the objectArg property. Object to persist
         * @return Value of objectArg
         */
        Type getObjectArg();
    }

    @ServiceCommand(defaultServiceClass = HibernateUpdateService.class)
    public interface UpdateCommand extends Command, UpdateArgument {
    }
}
