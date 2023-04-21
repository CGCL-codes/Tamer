package com.indigen.victor.actions;

import java.util.Hashtable;
import java.util.Map;
import org.w3c.dom.Node;
import com.indigen.victor.core.Progress;
import com.indigen.victor.core.SitemapBuilder;
import com.indigen.victor.core.SitemapZoneBuilder;

public class GuessSitemapZoneAction extends VictorAction {

    public Map process() throws Exception {
        Node body = getContentAsDom();
        String nodeid = getStringFromXPath(body, "node-id");
        String lang = getStringFromXPath(body, "lang");
        String progressId = getStringFromXPath(body, "progress-id");
        Node ruleNode = getNodeFromXPath(body, "rule");
        Progress progress = Progress.getProgress(this, progressId);
        setProgress(progress);
        SitemapBuilder sb = new SitemapZoneBuilder(this, lang, ruleNode);
        if (sb.guessFromNode(VICTOR_NS + "node-" + nodeid) == false) return null;
        Map map = new Hashtable();
        return map;
    }
}
