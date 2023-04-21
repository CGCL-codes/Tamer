package abc.aspectj.ast;

/** ClassnamePatternExpr that is just a name pattern.
 * 
 *  @author Oege de Moor
 *  @author Aske Simon Christensen
 */
public interface CPEName extends ClassnamePatternExpr {

    public NamePattern getNamePattern();
}
