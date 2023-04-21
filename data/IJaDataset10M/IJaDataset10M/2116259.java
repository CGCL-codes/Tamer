package net.sf.cathcart.plugin;

import static org.junit.Assert.*;
import org.junit.Test;

public class MojoBaseTest {

    MojoBase base = new Mojo();

    StringBuffer buffer = new StringBuffer(100);

    @Test
    public void shouldNotHaveTab() {
        assertEquals("", base.tab(0));
    }

    @Test
    public void shouldTabFourSpaces() {
        assertEquals("    ", base.tab(1));
    }

    @Test
    public void shouldAppendToEmptyBuffer() {
        base.append(buffer, "x", 1);
        assertEquals("\n    x", buffer.toString());
    }

    @Test
    public void shouldAppendToBuffer() {
        buffer.append("y");
        base.append(buffer, "x", 1);
        assertEquals("y\n    x", buffer.toString());
    }
}
