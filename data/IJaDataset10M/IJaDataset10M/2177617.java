package com.custardsource.cache.simulator.fqn;

import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexLogFileFqnSource extends LogFileFqnSource {

    private final Pattern pattern;

    private final Integer group;

    public RegexLogFileFqnSource(InputStream input, Pattern pattern, Integer group, Integer maxPopulation) {
        super(input, maxPopulation);
        this.pattern = pattern;
        this.group = group;
    }

    @Override
    protected String getFqn(String line) {
        Matcher m = pattern.matcher(line);
        if (m.matches()) {
            return m.group(group);
        }
        return null;
    }
}
