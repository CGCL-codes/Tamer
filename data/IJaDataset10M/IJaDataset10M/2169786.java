package org.pigeons.tivo;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FontTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testIncreaseSize() {
        Font font = new Font(22);
        assertTrue(font.getString().equals("default-22.font"));
        font.increaseSize();
        assertTrue(font.getString().equals("default-23.font"));
    }

    @Test
    public void testMaxSize() {
        Font font = new Font(50);
        font.increaseSize();
        assertTrue(font.getString().equals("default-50.font"));
    }

    @Test
    public void testMinSize() {
        Font font = new Font(8);
        font.decreaseSize();
        assertTrue(font.getString().equals("default-8.font"));
    }

    @Test
    public void testDecreaseSize() {
        Font font = new Font(22);
        assertTrue(font.getString().equals("default-22.font"));
        font.decreaseSize();
        assertTrue(font.getString().equals("default-21.font"));
    }

    @Test
    public void testGetStringInt() {
        Font font = new Font(22);
        assertTrue(font.getString(2).equals("default-24.font"));
    }
}
