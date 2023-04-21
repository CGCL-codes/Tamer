package logahawk.formatters;

import java.util.*;
import org.testng.*;
import org.testng.annotations.*;

public class FormatterSigFormatterTest {

    public FormatterSigFormatterTest() {
        this.helpers.add(new Helper(null, false, null));
        this.helpers.add(new Helper(Arrays.<String>asList(), false, null));
        this.helpers.add(new Helper(null, false, Arrays.<String>asList()));
        this.helpers.add(new Helper(Arrays.<String>asList(), false, Arrays.<String>asList()));
        this.helpers.add(new Helper(Arrays.asList(""), false, null));
        this.helpers.add(new Helper(Arrays.asList("H"), false, null));
        this.helpers.add(new Helper(Arrays.asList("H", "W"), false, null));
        this.helpers.add(new Helper(Arrays.asList("%"), false, null));
        this.helpers.add(new Helper(Arrays.asList("%s", "H"), true, Arrays.asList("H")));
        this.helpers.add(new Helper(Arrays.asList("%%"), true, Arrays.asList("%")));
        this.helpers.add(new Helper(Arrays.asList("%1$s", "H"), true, Arrays.asList("H")));
        this.helpers.add(new Helper(Arrays.asList("%", "H"), false, null));
        this.helpers.add(new Helper(Arrays.asList("H", "%"), false, null));
        this.helpers.add(new Helper(Arrays.asList("H", "%", "W"), false, Arrays.asList("H", "W")));
        this.helpers.add(new Helper(Arrays.asList("%s", "H"), true, Arrays.asList("H")));
        this.helpers.add(new Helper(Arrays.asList("H", "%s", "W"), true, Arrays.asList("H", "W")));
        this.helpers.add(new Helper(Arrays.asList("%s H W", "1"), true, Arrays.asList("1 H W")));
        this.helpers.add(new Helper(Arrays.asList("H %s W", "1"), true, Arrays.asList("H 1 W")));
        this.helpers.add(new Helper(Arrays.asList("H W %s", "1"), true, Arrays.asList("H W 1")));
        this.helpers.add(new Helper(Arrays.asList("B %s N %s W", "1", "2"), true, Arrays.asList("B 1 N 2 W")));
        this.helpers.add(new Helper(Arrays.asList("B %1$s N %1$s W", "1"), true, Arrays.asList("B 1 N 1 W")));
        this.helpers.add(new Helper(Arrays.asList("B %1$s N %2$s W", "1", "2"), true, Arrays.asList("B 1 N 2 W")));
        this.helpers.add(new Helper(Arrays.asList("B %2$s N %1$s W", "1", "2"), true, Arrays.asList("B 2 N 1 W")));
        this.helpers.add(new Helper(Arrays.asList("B %s N %<s W", "1"), true, Arrays.asList("B 1 N 1 W")));
        this.helpers.add(new Helper(Arrays.asList("B %s N %<s W", "1", "2"), true, Arrays.asList("B 1 N 1 W", "2")));
        this.helpers.add(new Helper(Arrays.asList("H %s", "1", "W"), true, Arrays.asList("H 1", "W")));
        this.helpers.add(new Helper(Arrays.asList("B", "N %s", "1", "W"), true, Arrays.asList("B", "N 1", "W")));
        this.helpers.add(new Helper(Arrays.asList("H %s", "1", "W %s", "2"), true, Arrays.asList("H 1", "W 2")));
        this.helpers.add(new Helper(Arrays.asList("B %s", "1", "N %s", "2", "W %s", "3"), true, Arrays.asList("B 1", "N 2", "W 3")));
        this.helpers.add(new Helper(Arrays.asList("B %s", "1", "@", "N %s", "2", "@", "W %s", "3", "@"), true, Arrays.asList("B 1", "@", "N 2", "@", "W 3", "@")));
    }

    /** List of {@link Helper}s to be used in the unit tests here. */
    private final List<Helper> helpers = new ArrayList<Helper>();

    @Test
    public void test() {
        FormatterSigFormatter f = new FormatterSigFormatter();
        for (Helper h : this.helpers) {
            boolean canFormat = f.canFormat(h.arguments);
            Assert.assertEquals(canFormat, h.canFormat, h.toString());
            if (canFormat) {
                f.format(h.arguments);
                String sizeMessage = h.arguments.size() + " != " + h.result.size() + " from [" + h.toString() + "]";
                Assert.assertEquals(h.arguments.size(), h.result.size(), sizeMessage);
                for (int i = 0; i < h.arguments.size(); ++i) {
                    String argMessage = "\"" + h.arguments.get(i) + "\" from [" + h.toString() + "]";
                    Assert.assertEquals(h.arguments.get(i), h.result.get(i), argMessage);
                }
            }
        }
    }

    /**
	 * A helper class to simplify testing. This contains lists of arguments that are ready to be formatted, and ancillary
	 * data that we use for testing.
	 */
    private static class Helper {

        public final List<Object> arguments;

        public final List<Object> result;

        public Helper(Collection<String> arguments, boolean canFormat, Collection<String> result) {
            this.arguments = arguments == null ? new ArrayList<Object>() : new ArrayList<Object>(arguments);
            this.canFormat = canFormat;
            this.result = result == null ? new ArrayList<Object>() : new ArrayList<Object>(result);
        }

        public final boolean canFormat;

        @Override
        public String toString() {
            StringBuffer sb = new StringBuffer();
            for (Object o : this.arguments) {
                sb.append("\"");
                sb.append(o);
                sb.append("\",");
            }
            return sb.toString();
        }
    }
}
