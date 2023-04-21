package com.jeml.xhtml;

import com.jeml.*;

/**
 * Class to represent an Body tag
 *
 * @author BWillard
 */
public class Body extends Node {

    protected Body() {
        super("body");
    }

    /**
     * Generates a new Body node
     *
     * @return
     */
    public static Body gen() {
        return new Body();
    }
}
