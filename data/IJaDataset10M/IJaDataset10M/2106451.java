package org.sodeja.silan.compiler.src;

import java.util.List;
import org.sodeja.collections.ListUtils;

public class BinaryHeader implements MethodHeader {

    public final String selector;

    public final String argument;

    public BinaryHeader(String selector, String argument) {
        this.selector = selector;
        this.argument = argument;
    }

    @Override
    public List<String> getArguments() {
        return ListUtils.asList(argument);
    }

    @Override
    public String getSelector() {
        return selector;
    }
}
