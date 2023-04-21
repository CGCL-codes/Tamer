package org.kumenya.psi.tree;

import org.kumenya.lang.LighterLazyParseableNode;

/**
 * Provides more flexible parsing capabilities for ILazyParseableElementType.
 * todo: merge with ILazyParseableElementType
 */
public interface ILightLazyParseableElementType {

    /**
   * Parses the contents of the specified chameleon node and returns the lightweight tree
   * representing parsed contents.
   *
   * @param chameleon the node to parse.
   * @return the parsed contents of the node.
   */
    FlyweightCapableTreeStructure<LighterASTNode> parseContents(final LighterLazyParseableNode chameleon);
}
