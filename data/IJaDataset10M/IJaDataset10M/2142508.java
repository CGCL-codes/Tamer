package org.xhtmlrenderer.css.sheet;

import java.util.ArrayList;
import java.util.List;

public class MediaRule implements RulesetContainer {

    private List _mediaTypes = new ArrayList();

    private List _contents = new ArrayList();

    private int _origin;

    public MediaRule(int origin) {
        _origin = origin;
    }

    public void addMedium(String medium) {
        _mediaTypes.add(medium);
    }

    public boolean matches(String medium) {
        if (medium.equalsIgnoreCase("all") || _mediaTypes.contains("all")) {
            return true;
        } else {
            return _mediaTypes.contains(medium.toLowerCase());
        }
    }

    public void addContent(Ruleset ruleset) {
        _contents.add(ruleset);
    }

    public List getContents() {
        return _contents;
    }

    public int getOrigin() {
        return _origin;
    }
}
