package org.magicbox.core.factory;

import java.util.Map;
import org.magicbox.core.model.CenterUser;
import org.magicbox.core.model.impl.CenterUserCommand;

/**
 * Users Abstract Factory
 * 
 * @author Massimiliano Dessi'(desmax74@yahoo.it)
 * @since jdk 1.6.0
 * @version 3.0.1
 */
public interface UserAbstractFactory {

    public CenterUserCommand createUserCommand();

    public CenterUserCommand createUserCommand(Map map);

    public CenterUser createUser(Map map);

    public CenterUser createUser(CenterUserCommand command);
}
