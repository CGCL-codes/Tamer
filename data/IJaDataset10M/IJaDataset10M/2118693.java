package com.teaminabox.eclipse.wiki.editors;

import java.util.ArrayList;
import java.util.Arrays;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.WhitespaceRule;
import com.teaminabox.eclipse.wiki.text.TextRegionMatcher;

public class WikiScanner extends RuleBasedScanner {

    public WikiScanner(WikiEditor wikiEditor) {
        ArrayList<IRule> rules = new ArrayList<IRule>();
        rules.add(new WhitespaceRule(new WikiWhitespaceDetector()));
        TextRegionMatcher[] matchers = wikiEditor.getContext().getContentRenderer().getScannerMatchers();
        for (TextRegionMatcher element : matchers) {
            element.setEditor(wikiEditor);
        }
        rules.addAll(Arrays.asList(matchers));
        setRules(rules.toArray(new IRule[rules.size()]));
    }
}
