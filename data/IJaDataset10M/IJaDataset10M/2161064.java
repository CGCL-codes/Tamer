package org.bibnet.heclinet.helper;

public class PY {

    public String correct(final String input) {
        final String output = input.replaceAll("ca\\.;", "ca. ");
        return output;
    }
}
