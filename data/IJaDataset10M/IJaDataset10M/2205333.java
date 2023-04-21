package org.qedeq.kernel.base.module;

/**
 * Definition of operator. This is a predicate constant. For example the
 * predicate "x is a set" could be defined in MK with the formula "\exists y: x \in y".
 * <p>
 * There must also be the possibility to define basic predicate constants like
 * "x is element of y".
 *
 * @version $Revision: 1.3 $
 * @author  Michael Meyling
 */
public interface PredicateDefinition extends NodeType {

    /**
     * Get number of arguments for the defined object. Carries information about the argument
     * number the defined object needs.
     *
     * @return  Argument number.
     */
    public String getArgumentNumber();

    /**
     * This name together with {@link #getArgumentNumber()} identifies a predicate.
     *
     * @return  Name of defined predicate.
     */
    public String getName();

    /**
     * Get LaTeX output for definition. The replaceable arguments must are marked as "#1",
     * "#2" and so on. For example "\mathfrak{M}(#1)"
     *
     * @return  LaTeX pattern for definition type setting.
     */
    public String getLatexPattern();

    /**
     * Get variable list of definition arguments.
     *
     * @return  List of formulas or subject variables to be replaced in the LaTeX pattern.
     *          Could be <code>null</code>.
     */
    public VariableList getVariableList();

    /**
     * Get term that defines the object. Could be <code>null</code>.
     *
     * @return  Defining formula.
     */
    public Formula getFormula();

    /**
     * Get description. Only necessary if formula is not self-explanatory.
     *
     * @return  Description.
     */
    public LatexList getDescription();
}
