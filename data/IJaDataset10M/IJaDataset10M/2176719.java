package cz.cuni.mff.ksi.jinfer.crudemdl.cleaning.emptychildren;

import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpInterval;
import cz.cuni.mff.ksi.jinfer.base.regexp.RegexpType;
import cz.cuni.mff.ksi.jinfer.base.regexp.Regexp;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for CleanerEmptyChildren.
 *
 * @author anti
 */
public class CleanerEmptyChildrenTest {

    public CleanerEmptyChildrenTest() {
    }

    /**
   * Test of cleanRegularExpression method, of class CleanerEmptyChildren.
   */
    @Test
    public void testCleanRegularExpression() {
        System.out.println("cleanRegularExpression");
        Regexp<String> r3 = Regexp.<String>getMutable();
        r3.setInterval(RegexpInterval.getOnce());
        r3.setType(RegexpType.CONCATENATION);
        r3.setImmutable();
        Regexp<String> r2 = Regexp.<String>getMutable();
        r2.setInterval(RegexpInterval.getOnce());
        r2.setType(RegexpType.TOKEN);
        r2.setContent("a");
        r2.setImmutable();
        Regexp<String> r1 = Regexp.<String>getMutable();
        r1.setInterval(RegexpInterval.getOnce());
        r1.setType(RegexpType.CONCATENATION);
        r1.addChild(r3);
        r1.addChild(r3);
        r1.addChild(r2);
        r1.addChild(r3);
        r1.addChild(r3);
        r1.setImmutable();
        Regexp<String> regexp = r1;
        CleanerEmptyChildren<String> instance = new CleanerEmptyChildren<String>();
        Regexp<String> result = instance.cleanRegularExpression(regexp);
        assertEquals("a", result.toString());
    }

    /**
   * Test of cleanRegularExpression method, of class CleanerEmptyChildren.
   */
    @Test
    public void testCleanRegularExpression1() {
        System.out.println("cleanRegularExpression1");
        Regexp<String> r3 = Regexp.<String>getMutable();
        r3.setInterval(RegexpInterval.getOnce());
        r3.setType(RegexpType.TOKEN);
        r3.setContent("a");
        r3.setImmutable();
        Regexp<String> r2 = Regexp.<String>getMutable();
        r2.setInterval(RegexpInterval.getOnce());
        r2.setType(RegexpType.CONCATENATION);
        r2.addChild(r3);
        r2.addChild(r3);
        r2.addChild(r3);
        r2.addChild(r3);
        r2.setImmutable();
        Regexp<String> r1 = Regexp.<String>getMutable();
        r1.setInterval(RegexpInterval.getOnce());
        r1.setType(RegexpType.CONCATENATION);
        r1.addChild(r2);
        r1.setImmutable();
        Regexp<String> regexp = r1;
        CleanerEmptyChildren<String> instance = new CleanerEmptyChildren<String>();
        Regexp<String> result = instance.cleanRegularExpression(regexp);
        assertEquals("(a\n,a\n,a\n,a)", result.toString());
    }

    /**
   * Test of cleanRegularExpression method, of class CleanerEmptyChildren.
   */
    @Test
    public void testCleanRegularExpression2() {
        System.out.println("cleanRegularExpression2");
        Regexp<String> r3 = Regexp.<String>getMutable();
        r3.setInterval(RegexpInterval.getOnce());
        r3.setType(RegexpType.TOKEN);
        r3.setContent("a");
        r3.setImmutable();
        Regexp<String> r2 = Regexp.<String>getMutable();
        r2.setInterval(RegexpInterval.getOnce());
        r2.setType(RegexpType.CONCATENATION);
        r2.addChild(r3);
        r2.setImmutable();
        Regexp<String> r1 = Regexp.<String>getMutable();
        r1.setInterval(RegexpInterval.getOnce());
        r1.setType(RegexpType.CONCATENATION);
        r1.addChild(r2);
        r1.setImmutable();
        Regexp<String> regexp = r1;
        CleanerEmptyChildren<String> instance = new CleanerEmptyChildren<String>();
        Regexp<String> result = instance.cleanRegularExpression(regexp);
        assertEquals("a", result.toString());
    }

    /**
   * Test of cleanRegularExpression method, of class CleanerEmptyChildren.
   */
    @Test
    public void testCleanRegularExpression3() {
        System.out.println("cleanRegularExpression3");
        Regexp<String> r5 = Regexp.<String>getMutable();
        r5.setInterval(RegexpInterval.getOnce());
        r5.setType(RegexpType.TOKEN);
        r5.setContent("b");
        r5.setImmutable();
        Regexp<String> r4 = Regexp.<String>getMutable();
        r4.setInterval(RegexpInterval.getOnce());
        r4.setType(RegexpType.CONCATENATION);
        r4.addChild(r5);
        r4.setImmutable();
        Regexp<String> r3 = Regexp.<String>getMutable();
        r3.setInterval(RegexpInterval.getOnce());
        r3.setType(RegexpType.TOKEN);
        r3.setContent("a");
        r3.setImmutable();
        Regexp<String> r2 = Regexp.<String>getMutable();
        r2.setInterval(RegexpInterval.getOnce());
        r2.setType(RegexpType.CONCATENATION);
        r2.setImmutable();
        Regexp<String> r1 = Regexp.<String>getMutable();
        r1.setInterval(RegexpInterval.getOnce());
        r1.setType(RegexpType.CONCATENATION);
        r1.addChild(r2);
        r1.addChild(r3);
        r1.addChild(r4);
        r1.setImmutable();
        Regexp<String> regexp = r1;
        CleanerEmptyChildren<String> instance = new CleanerEmptyChildren<String>();
        Regexp<String> result = instance.cleanRegularExpression(regexp);
        assertEquals("(a\n,b)", result.toString());
    }
}
