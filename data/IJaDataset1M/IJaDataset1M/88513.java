package org.lineman.separated;

import java.io.StringWriter;
import org.junit.Before;
import org.junit.Test;
import org.lineman.FieldSource;
import org.lineman.Identification;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TestFieldWriter {

    private static class TestSource implements FieldSource {

        CharSequence[][] values = new CharSequence[][] { { "HA", "HB" }, { "R1A", "R1B" }, { "R2A", "R2B" } };

        int position = 0;

        public boolean isHeaderRow() {
            return true;
        }

        public Identification getIdentification() {
            return Identification.UNIDENTIFIED;
        }

        public boolean hasNext() {
            return position < values.length;
        }

        public CharSequence[] next() {
            return values[position++];
        }

        public void remove() {
            fail("Inappropriate call to remove()");
        }
    }

    private FieldWriter tabbed;

    private StringWriter collector;

    @Before
    public void setUp() throws Exception {
        this.collector = new StringWriter();
        this.tabbed = new FieldWriter(collector);
    }

    private void check(final String expected) {
        assertEquals(expected + System.getProperty("line.separator"), collector.toString());
    }

    @Test
    public void separator() {
        assertEquals("\t", tabbed.getSeparator());
        assertEquals("~", new FieldWriter(collector, "~").getSeparator());
        assertEquals("FOO", new FieldWriter(collector, "FOO").getSeparator());
    }

    @Test
    public void printlnNull() {
        tabbed.println((CharSequence[]) null);
        check("");
    }

    @Test
    public void printlnEmpty() {
        tabbed.println(new CharSequence[0]);
        check("");
    }

    @Test
    public void printlnOneField() {
        tabbed.println(new CharSequence[] { "one" });
        check("one");
    }

    @Test
    public void printlnTwoFields() {
        tabbed.println(new CharSequence[] { "one", "two" });
        check("one\ttwo");
    }

    @Test
    public void printlnThreeFields() {
        tabbed.println(new CharSequence[] { "one", "two", "three" });
        check("one\ttwo\tthree");
    }

    @Test
    public void printlnAlternateDelimiter() {
        FieldWriter alternate = new FieldWriter(this.collector, "~");
        alternate.println(new CharSequence[] { "one", "two", "three" });
        check("one~two~three");
    }

    @Test
    public void printFieldSource() {
        tabbed.print(new TestSource());
        check("HA\tHB" + System.getProperty("line.separator") + "R1A\tR1B" + System.getProperty("line.separator") + "R2A\tR2B");
    }

    @Test
    public void printNullFieldSource() {
        tabbed.print(null, false);
        assertEquals("", collector.toString());
    }

    @Test
    public void suppressedHeaders() {
        tabbed.print(new TestSource(), true);
        check("R1A\tR1B" + System.getProperty("line.separator") + "R2A\tR2B");
    }

    @Test
    public void suppressedNoHeaders() {
        tabbed.print(new TestSource() {

            @Override
            public boolean isHeaderRow() {
                return false;
            }
        }, true);
        check("HA\tHB" + System.getProperty("line.separator") + "R1A\tR1B" + System.getProperty("line.separator") + "R2A\tR2B");
    }

    @Test
    public void emptyRead() {
        tabbed.print(FieldSource.EMPTY_SOURCE);
    }
}
