package com.x5.template;

import java.util.Formatter;
import org.junit.Test;
import static org.junit.Assert.*;

public class LocaleTagTest {

    @Test
    public void testRemoveClutter() {
        Chunk c = new Chunk();
        c.append("Bla bla bla _[A new car!] bla bla");
        assertEquals("Bla bla bla A new car! bla bla", c.toString());
    }

    @Test
    public void testFmtStringStatic() {
        Chunk c = new Chunk();
        c.append("Bla bla bla {_[A new %s car!],blue} bla bla");
        assertEquals("Bla bla bla A new blue car! bla bla", c.toString());
    }

    @Test
    public void testArg() {
        Chunk c = new Chunk();
        c.set("color", "blue");
        c.append("Bla bla bla {_[A new %s car!],~color} bla bla");
        assertEquals("Bla bla bla A new blue car! bla bla", c.toString());
    }

    @Test
    public void testManyArgs() {
        Chunk c = new Chunk();
        c.set("color", "blue");
        c.set("make", "lexus");
        c.append("Bla bla bla {_[A new %s %s!],~color,~make} bla bla");
        assertEquals("Bla bla bla A new blue lexus! bla bla", c.toString());
    }

    @Test
    public void testRemoveClutterBraced() {
        Chunk c = new Chunk();
        c.append("Bla bla bla {_[A new car!]} bla bla");
        assertEquals("Bla bla bla A new car! bla bla", c.toString());
    }

    @Test
    public void testMoreThanOne() {
        Chunk c = new Chunk();
        c.append("Bla bla bla _[A new car!] bla _[A new house!] bla");
        assertEquals("Bla bla bla A new car! bla A new house! bla", c.toString());
    }

    @Test
    public void testMoreThanOneBraced() {
        Chunk c = new Chunk();
        c.append("Bla bla bla {_[A new car!]} bla {_[A new house!]} bla");
        assertEquals("Bla bla bla A new car! bla A new house! bla", c.toString());
    }

    @Test
    public void testMoreThanOneMixed() {
        Chunk c = new Chunk();
        c.append("Bla bla bla {_[A new car!]} bla _[A new house!] bla. ");
        c.append("Bla bla bla {_[A new pet!]} bla _[A new life!] bla");
        assertEquals("Bla bla bla A new car! bla A new house! bla. Bla bla bla A new pet! bla A new life! bla", c.toString());
    }

    @Test
    public void testMismatchedArgs() {
        Chunk c = new Chunk();
        c.set("color", "blue");
        c.set("make", "lexus");
        c.append("Bla bla bla {_[A new %s %s!],~color} bla bla");
        assertEquals("Bla bla bla A new %s %s! bla bla", c.toString());
    }

    @Test
    public void testDeepTranslationAndFormat() {
        Chunk c = new Chunk();
        c.set("color", "_[blue]");
        c.set("make", "lexus");
        ChunkLocale.registerLocale("de_DE", new String[] { "A new %s %s!", "Eine neue %s %s!", "blue", "blau" });
        c.setLocale("de_DE");
        c.append("Bla bla bla {_[A new %s %s!],~color,~make} bla bla");
        assertEquals("Bla bla bla Eine neue blau lexus! bla bla", c.toString());
    }

    @Test
    public void testTranslationNoFile() {
        Chunk c = new Chunk();
        ChunkLocale.registerLocale("de_DE", new String[] { "A new car!", "Eine neue Auto!" });
        c.setLocale("de_DE");
        c.append("Bla bla bla _[A new car!] bla bla");
        assertEquals("Bla bla bla Eine neue Auto! bla bla", c.toString());
    }

    @Test
    public void testTranslation() {
        Chunk c = new Chunk();
        c.setLocale("fr_FR");
        c.append("Bla bla bla _[A new car!] bla bla");
        assertEquals("Bla bla bla Un nouveau auto! bla bla", c.toString());
    }
}
