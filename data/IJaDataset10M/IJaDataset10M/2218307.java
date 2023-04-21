package com.germinus.xpression.cms.directory;

import javax.jcr.Node;

public interface NodeByURLPathSearcher {

    Node searchByURLPath(String urlPath) throws DirectoryItemNotFoundException;
}
