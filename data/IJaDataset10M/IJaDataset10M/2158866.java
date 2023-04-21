package com.hp.hpl.guess.piccolo.util;

import edu.umd.cs.piccolo.nodes.PText;

public class SerText extends PText implements SerInterface {

    private static final long serialVersionUID = 3490133894612141702L;

    public SerText() {
    }

    public SerText(String text) {
        super(text);
    }
}
