package net.sourceforge.fluxion.runcible.visitor;

import net.sourceforge.fluxion.runcible.Rule;

/**
 * Javadocs go here.
 *
 * @author Tony Burdett
 * @version 1.0
 * @date 19-Apr-2007
 */
public interface RuleVisitor {

    void visit(Rule rule);
}
