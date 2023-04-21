package logahawk.formatters;

import java.util.*;
import org.testng.*;
import org.testng.annotations.*;

/**
 *
 */
public class DefaultSignatureFormatterContainerTest {

    /**
   * This test confirms that our parts are working correctly, so we can correctly attribute errors to the parts or the
   * DefaultSignatureFormatterContainer.
   */
    @Test
    public void testAddingSigFormatter() {
        SignatureFormatter f = new AddingSigFormatter();
        List<Object> args = new ArrayList<Object>();
        args.clear();
        Assert.assertFalse(f.canFormat(args));
        args.clear();
        args.add(5);
        Assert.assertFalse(f.canFormat(args));
        args.clear();
        args.add(5);
        args.add(7);
        Assert.assertTrue(f.canFormat(args));
        f.format(args);
        Assert.assertEquals(args.size(), 1);
        Assert.assertEquals(args.get(0), 5 + 7);
        args.clear();
        args.add(11);
        args.add(13);
        args.add(17);
        Assert.assertTrue(f.canFormat(args));
        f.format(args);
        Assert.assertEquals(args.size(), 2);
        Assert.assertEquals(args.get(0), 11 + 13);
        Assert.assertEquals(args.get(1), 17);
    }

    /** Test that the order of which SignatureFormatters are added changes the overall behavior. */
    @Test
    public void testOrder1() {
        SignatureFormatter f1 = new AddingSigFormatter();
        SignatureFormatter f2 = new RemoveFirstSigFormatter();
        SignatureFormatterContainer c = new DefaultSignatureFormatterContainer();
        c.add(f1, f2);
        List<Object> args = new ArrayList<Object>();
        args.add(5);
        args.add(7);
        c.format(args);
        Assert.assertEquals(args.size(), 0);
    }

    /** Test that the order of which SignatureFormatters are added changes the overall behavior. */
    @Test
    public void testOrder2() {
        SignatureFormatter f1 = new RemoveFirstSigFormatter();
        SignatureFormatter f2 = new AddingSigFormatter();
        SignatureFormatterContainer c = new DefaultSignatureFormatterContainer();
        c.add(f1, f2);
        List<Object> args = new ArrayList<Object>();
        args.add(5);
        args.add(7);
        c.format(args);
        Assert.assertEquals(args.size(), 1);
        Assert.assertEquals(args.get(0), 7);
    }

    /**
   * This test confirms that our parts are working correctly, so we can correctly attribute errors to the parts or the
   * DefaultSignatureFormatterContainer.
   */
    @Test
    public void testRemoveFirstSigFormatter() {
        SignatureFormatter f = new RemoveFirstSigFormatter();
        List<Object> args = new ArrayList<Object>();
        args.clear();
        Assert.assertFalse(f.canFormat(args));
        args.clear();
        args.add(5);
        Assert.assertTrue(f.canFormat(args));
        f.format(args);
        Assert.assertEquals(args.size(), 0);
        args.clear();
        args.add(5);
        args.add(7);
        Assert.assertTrue(f.canFormat(args));
        f.format(args);
        Assert.assertEquals(args.size(), 1);
    }

    /** Adds to together the first two arguments if they are integers. */
    private static class AddingSigFormatter implements SignatureFormatter {

        public boolean canFormat(List<Object> arguments) {
            return arguments.size() >= 2 && arguments.get(0) instanceof Integer && arguments.get(1) instanceof Integer;
        }

        public void format(List<Object> arguments) {
            int x = (Integer) arguments.get(0);
            int y = (Integer) arguments.get(1);
            arguments.remove(0);
            arguments.remove(0);
            arguments.add(0, x + y);
        }
    }

    /** Removes the first argument if there are 1 or more arguments. */
    private static class RemoveFirstSigFormatter implements SignatureFormatter {

        public boolean canFormat(final List<Object> arguments) {
            return arguments.size() >= 1;
        }

        public void format(final List<Object> arguments) {
            arguments.remove(0);
        }
    }
}
