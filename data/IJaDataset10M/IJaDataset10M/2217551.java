package mnemosyne.core;

import java.lang.reflect.Field;

/**
 * @version $Id: AopSystem.java,v 1.1 2004/09/01 14:58:05 charlesblaxland Exp $
 */
public interface AopSystem {

    public boolean isAopIntroducedField(Field field);
}
