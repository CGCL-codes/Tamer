package net.sourceforge.ejb3checker.lib.impl.def;

import java.lang.reflect.Constructor;
import net.sourceforge.ejb3checker.lib.IStyleListener;
import net.sourceforge.ejb3checker.lib.dt.NoDefaultConstructor;
import net.sourceforge.ejb3checker.lib.impl.AbstractStyleChecker;

/**
 * This <code>IStyleChecker</code> checks wether the specified classes got a
 * default constructor.
 *
 * @author foobaamarook
 */
final class DefaultConstructorTester extends AbstractStyleChecker {

    /**
         * @see net.sourceforge.ejb3checker.lib.IStyleChecker#checkClass(java.lang.Class, net.sourceforge.ejb3checker.lib.IStyleListener)
         */
    public void checkClass(final Class<?> clazz, final IStyleListener styleListener) {
        for (final Constructor constructor : clazz.getConstructors()) {
            if (constructor.getParameterTypes().length == 0) {
                return;
            }
        }
        styleListener.onStyleProblem(new NoDefaultConstructor(clazz));
    }
}
