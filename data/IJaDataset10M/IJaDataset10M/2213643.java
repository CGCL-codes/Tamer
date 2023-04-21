package com.akjava.wiki.client.modules;

import com.akjava.wiki.client.core.NodeImp;

/**
 * 
 *
 */
public class Text extends NodeImp {

    public Text() {
        setName("text");
    }

    public Text(String line) {
        this();
        addAttribute(line);
    }

    /**
 * @return
 */
    public String getText() {
        if (getAttributes().length > 0) {
            return getAttributes()[0];
        }
        return "";
    }
}
