package org.one.stone.soup.wiki.builder;

import org.one.stone.soup.wiki.controller.WikiControllerInterface;

public interface WikiBuilderListener {

    public void buildStarted(WikiControllerInterface builder);

    public void buildComplete(WikiControllerInterface builder);
}
