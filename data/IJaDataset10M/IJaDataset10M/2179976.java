package org.mobicents.ssf.flow.engine.builder.template;

import java.util.ArrayList;
import java.util.List;

public class PatternListTemplate extends AbstractAnnotatedTemplate {

    private List<PatternTemplate> patterns;

    public List<PatternTemplate> getPatterns() {
        return patterns;
    }

    public void setPatterns(List<PatternTemplate> patterns) {
        this.patterns = patterns;
    }

    public void addPattern(PatternTemplate pattern) {
        if (this.patterns == null) {
            this.patterns = new ArrayList<PatternTemplate>();
        }
        this.patterns.add(pattern);
    }
}
