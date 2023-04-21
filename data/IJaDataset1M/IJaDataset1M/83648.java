package pl.wcislo.sbql4j.source.tree;

import java.util.List;

/**
 * A tree node to stand in for a malformed expression.
 *
 * @author Peter von der Ah&eacute;
 * @author Jonathan Gibbons
 * @since 1.6
 */
public interface ErroneousTree extends ExpressionTree {

    List<? extends Tree> getErrorTrees();
}
