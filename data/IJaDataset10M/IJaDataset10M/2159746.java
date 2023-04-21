package net.sf.brico.cmd.base;

import net.sf.brico.cmd.Context;
import net.sf.brico.cmd.ContextFactory;

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
public class DefaultContextFactory implements ContextFactory {

    public DefaultContextFactory() {
        super();
    }

    public Context createContext() {
        return new DefaultContext();
    }
}
