package org.norecess.nolatte.ast;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class IdentifierTest {

    @Test
    public void shouldBeEqualsOnlyOnIdentifierText() {
        assertTrue(new Identifier(123, "\\z").equals(new Identifier(616, "\\z")));
        assertTrue(new Identifier(123, "\\foobar").equals(new Identifier(616, "\\foobar")));
        assertTrue(new Identifier(123, "\\z").equals(new Identifier(8675309, "\\z")));
    }

    @Test
    public void shouldNotCleanNormalIdentifiers() {
        assertEquals("should not be cleaned", new Identifier(123, "\\z"), new Identifier(123, "\\z"));
        assertEquals("should not be cleaned", new Identifier(123, "\\foo-bar"), new Identifier(123, "\\foo-bar"));
    }

    @Test
    public void shouldCleanBindingVariables() {
        assertEquals("should clean binding identifier", new Identifier(123, "\\z"), new Identifier(123, "\\z="));
        assertEquals("should clean binding identifier", new Identifier(123, "\\foo-bar"), new Identifier(123, "\\foo-bar="));
    }

    @Test
    public void shoudlCleanNamedParameters() {
        assertEquals("should clean named parameter", new Identifier(123, "\\z"), new Identifier(123, "\\=z"));
        assertEquals("should clean named parameter", new Identifier(123, "\\foo-bar"), new Identifier(123, "\\=foo-bar"));
    }

    @Test
    public void shouldCleanRestParameter() {
        assertEquals("should clean rest parameter", new Identifier(123, "\\z"), new Identifier(123, "\\&z"));
        assertEquals("should clean rest parameter", new Identifier(123, "\\foo-bar"), new Identifier(123, "\\&foo-bar"));
        assertEquals("should clean rest parameter", new Identifier(123, "\\-"), new Identifier(123, "\\&-"));
    }
}
