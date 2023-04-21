package org.dbwiki.data.query.visual;

import java.util.ArrayList;

public class ArgumentsNode extends Node {

    private ArrayList<String> arguments = new ArrayList<String>();

    boolean add(ArgumentNode arg) {
        arguments.add(arg.get());
        return true;
    }

    ArrayList<String> get() {
        return arguments;
    }
}
