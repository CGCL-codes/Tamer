package org.magicbox.core.factory;

import java.util.Map;
import org.magicbox.core.model.Center;
import org.magicbox.core.model.impl.CenterCommand;

/**
 * Center Abstract Factory
 * 
 * @author Massimiliano Dessi'(desmax74@yahoo.it)
 * @since jdk 1.6.0
 * @version 3.0.1
 */
public interface CenterAbstractFactory {

    public CenterCommand createCenterCommand();

    public Center createCenter(Map map);

    public Center createCenter(CenterCommand command);
}
