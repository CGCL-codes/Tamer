package hudson.zipscript.parser.template.element.directive.setdir;

import hudson.zipscript.parser.template.element.PatternMatcher;
import hudson.zipscript.parser.template.element.component.Component;

public class GlobalComponent implements Component {

    public PatternMatcher[] getPatternMatchers() {
        return new PatternMatcher[] { new GlobalPatternMatcher() };
    }
}
