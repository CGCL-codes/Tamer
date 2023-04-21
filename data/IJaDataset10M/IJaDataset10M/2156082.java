package org.one.stone.soup.wiki.trigger;

import org.one.stone.soup.wiki.controller.WikiControllerInterface;

public class RebuildTrigger extends Trigger {

    public RebuildTrigger(WikiControllerInterface controller) {
        super(controller);
    }

    public String getPageName() {
        return "/OpenForum/Triggers/RebuildTrigger";
    }
}
