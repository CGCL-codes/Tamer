package de.mpiwg.vspace.profiles.ui.styles;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.Color;

public abstract class TemplateStyler {

    public abstract void doSyntaxHighlighting();

    protected List<StyleRange> processPattern(String patternStr, String text, Color color, int group) {
        Pattern pattern = Pattern.compile(patternStr, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(text);
        List<StyleRange> styles = new ArrayList<StyleRange>();
        List<Region> regions = new ArrayList<Region>();
        while (matcher.find()) {
            int start = matcher.start(group);
            int end = matcher.end(group);
            regions.add(new Region(start, end));
        }
        for (Region r : regions) {
            StyleRange tagRange = new StyleRange();
            tagRange.start = r.start;
            tagRange.length = r.end - r.start;
            tagRange.foreground = color;
            styles.add(tagRange);
        }
        return styles;
    }

    protected class Region {

        public int start;

        public int end;

        public Region(int start, int end) {
            this.start = start;
            this.end = end;
        }
    }
}
