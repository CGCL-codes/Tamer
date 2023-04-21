package net.sourceforge.ejb3checker.test.lib.def;

import net.sourceforge.ejb3checker.lib.dt.InvalidAnnotationCount;
import net.sourceforge.ejb3checker.lib.dt.InvalidMethodParameterCount;
import net.sourceforge.ejb3checker.lib.dt.InvalidReturnType;
import net.sourceforge.ejb3checker.lib.dt.MissingSuperInterface;
import net.sourceforge.ejb3checker.lib.dt.MissingTwinMethod;
import net.sourceforge.ejb3checker.lib.dt.NoDefaultConstructor;
import net.sourceforge.ejb3checker.test.lib.test_classes.GetterSetterTestClass;
import net.sourceforge.ejb3checker.test.lib.test_classes.NoDefaultConstructorTestClass;

/**
 * TODO docme
 *
 * @author foobaamarook
 */
public final class EntityBeanTestCase extends AbstractDefaultTestCase {

    public void testGetterSetter() {
        styleChecker.checkClass(GetterSetterTestClass.class, styleListener);
        assertEquals(1, counterListener.getStyleProblemCount(InvalidReturnType.class));
        assertEquals(4, counterListener.getStyleProblemCount(MissingTwinMethod.class));
        assertEquals(1, counterListener.getStyleProblemCount(InvalidMethodParameterCount.class));
        assertEquals(1, counterListener.getStyleProblemCount(MissingSuperInterface.class));
        assertEquals(7, counterListener.getTotalProblemsCount());
    }

    public void testNoDefaultConstructor() {
        styleChecker.checkClass(NoDefaultConstructorTestClass.class, styleListener);
        assertEquals(1, counterListener.getStyleProblemCount(NoDefaultConstructor.class));
        assertEquals(1, counterListener.getStyleProblemCount(InvalidAnnotationCount.class));
        assertEquals(2, counterListener.getTotalProblemsCount());
    }
}
