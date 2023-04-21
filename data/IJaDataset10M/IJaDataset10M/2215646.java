package com.edbaskerville.gridsweeper.parameters;

import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class ListSweepTest {

    private ListSweep sweep;

    @Before
    public void setUp() {
        sweep = new ListSweep("param");
    }

    @Test
    public void emptySweep() {
        List<ParameterMap> maps = sweep.generateMaps(null);
        assertEquals(maps.size(), 0);
    }

    @Test
    public void simpleSweep() {
        sweep.add("5");
        sweep.add("6");
        sweep.add("14");
        sweep.add("-15");
        List<ParameterMap> maps = sweep.generateMaps(null);
        assertEquals("5", maps.get(0).get("param"));
        assertEquals("6", maps.get(1).get("param"));
        assertEquals("14", maps.get(2).get("param"));
        assertEquals("-15", maps.get(3).get("param"));
    }
}
