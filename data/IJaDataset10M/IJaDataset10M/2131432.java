package com.google.gwt.dev.util.arg;

import com.google.gwt.util.tools.ArgHandlerInt;

/**
 * An ArgHandler to provide the -XfragmentMerge flag.
 */
public class ArgHandlerFragmentMerge extends ArgHandlerInt {

    private final OptionFragmentsMerge option;

    public ArgHandlerFragmentMerge(OptionFragmentsMerge option) {
        this.option = option;
    }

    @Override
    public String getPurpose() {
        return "EXPERIMENTAL: Enables Fragment merging code splitter.";
    }

    @Override
    public String getTag() {
        return "-XfragmentMerge";
    }

    @Override
    public String[] getTagArgs() {
        return new String[] { "numFragments" };
    }

    @Override
    public void setInt(int value) {
        option.setFragmentsMerge(value);
    }
}
