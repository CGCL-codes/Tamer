package joptsimple;

/**
 * <p>Visitor interface for option specifications.</p>
 *
 * @author <a href="mailto:pholser@alumni.rice.edu">Paul Holser</a>
 * @version $Id: OptionSpecVisitor.java,v 1.5 2009/03/06 20:35:08 pholser Exp $
 */
interface OptionSpecVisitor {

    void visit(NoArgumentOptionSpec spec);

    void visit(RequiredArgumentOptionSpec<?> spec);

    void visit(OptionalArgumentOptionSpec<?> spec);

    void visit(AlternativeLongOptionSpec spec);
}
