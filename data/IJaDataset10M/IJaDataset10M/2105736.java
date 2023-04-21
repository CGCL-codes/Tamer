package org.jtools.tmplc.parser;

import java.util.List;

/**
 * Definition of the Template Language.
 * @author <a href="mailto:rainer.noack@jtools.org">rainer noack </a>
 */
public interface ParserNode {

    List<ParserNode> getChildNodes();

    List<Tag> getEndTags();

    List<Tag> getEscapeTags();

    NodeHandler.Factory getHandlerFactory();

    Tag getStartTag();

    boolean isInheritEscapes();

    boolean isLineDelimited();

    void setChildNodes(List<ParserNode> nodes);

    void addChildNode(ParserNode... nodes);

    <R, D> R accept(NodeVisitor<R, D> visitor, D... data);
}
