package org.jazzteam.edu.patterns.memento;

import junit.framework.Assert;
import org.junit.Test;

public class TestMemento {

    @Test
    public void testMemento() {
        Originator o = new Originator();
        o.changeState("state1");
        o.changeState("state2");
        o.changeState("state3");
        o.loadState();
        Assert.assertEquals(o.getCurrentState().get(0), "state2");
    }
}
