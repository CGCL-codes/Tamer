package net.sourceforge.deco.parsers;

import junit.framework.Assert;
import net.sourceforge.deco.parsers.utils.UnicityChecker;
import net.sourceforge.deco.testclass.EmptyClass;
import org.junit.Test;
import org.objectweb.asm.Type;

public class UnicityCheckerTest {

    @Test
    public void transferOnceAndOnlyOnce() {
        final Type t = Type.getType(EmptyClass.class);
        UnicityChecker checker = new UnicityChecker();
        Assert.assertFalse(checker.block(t));
        Assert.assertTrue(checker.block(t));
    }
}
