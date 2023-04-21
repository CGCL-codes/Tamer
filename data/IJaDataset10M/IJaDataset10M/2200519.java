package com.google.gwt.resources.css.ast;

import java.util.List;

/**
 * Indicates that the node contains other nodes.
 */
public interface HasNodes {

    List<CssNode> getNodes();
}
