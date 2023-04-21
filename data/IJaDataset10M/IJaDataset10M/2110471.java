package net.sf.beatrix.module.analyzer.signature;

import net.sf.beatrix.lang.Integers;
import net.sf.beatrix.lang.NullArgumentException;

public class Signature {

    private String name;

    private short[] pattern;

    private String patternString;

    public Signature(String name, short[] pattern) {
        if (name == null) {
            throw new NullArgumentException("name");
        }
        if (name.isEmpty()) {
            throw new IllegalArgumentException("You have to provide a non-empty signature name.");
        }
        if (pattern == null) {
            throw new NullArgumentException("pattern");
        }
        for (short s : pattern) {
            if (s < 0 || s > 255) {
                throw new IllegalArgumentException("The pattern has to contain values in [0, 255] only.");
            }
        }
        this.name = name;
        this.pattern = pattern;
    }

    public String getName() {
        return name;
    }

    public short[] getPattern() {
        return pattern;
    }

    public String getPatternString() {
        if (patternString == null) {
            StringBuilder sb = new StringBuilder();
            for (short s : getPattern()) {
                sb.append(Integers.toHexString(s, true, false));
                sb.append(" ");
            }
            patternString = sb.toString();
        }
        return patternString;
    }

    @Override
    public String toString() {
        return getName() + ": " + getPatternString();
    }
}
