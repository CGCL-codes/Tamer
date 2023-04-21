package org.openscience.cdk.iupac.generator;

import org.openscience.cdk.interfaces.AtomContainer;

/**
 *  This interface defines the API for the implementation of
 *  IUPAC rules in the org.openscience.cdk.iupac.generator
 *  package.
 *
 * @cdk.module experimental
 *
 * @author Egon Willighagen
 */
public interface Rule {

    /** Returns the name of this rule.
     */
    public String getName();

    /** Applies this rule to this molecule.
     * 
     * @return null if this rule was not applicable
     */
    public IUPACNamePart apply(AtomContainer ac);

    public String localize(String s);

    /**
     * Flag that must be set by a Rule's apply() method.
     *
     * @see #apply(AtomContainer)
     */
    public static final String NONE_APPLICABLE = "org.openscience.cdk.iupac.generator.NONE_APPLICABLE";

    public static final String COMPLETED_FLAG = "org.openscience.cdk.iupac.generator.COMPLETED";

    public static final String ATOM_NAMED_FLAG = "org.openscience.cdk.iupac.generator.ATOM_NAMED";

    public static final String ATOM_NUMBERED_FLAG = "org.openscience.cdk.iupac.generator.ATOM_NUMBERED";

    public static final String ATOM_MUST_BE_NUMBERED_FLAG = "org.openscience.cdk.iupac.generator.ATOM_MUST_BE_NUMBERED";

    public static final String ATOM_HAS_VALENCY = "org.openscience.cdk.iupac.generator.ATOM_HAS_VALENCY";

    public static final String IUPAC_NAME = "org.openscience.cdk.iupac.generator.IUPAC_NAME";

    public static final String ELEMENT_COUNT = "org.openscience.cdk.iupac.generator.ELEMENT_COUNT";

    public static final String CARBON_COUNT = "org.openscience.cdk.iupac.generator.CARBON_COUNT";

    public static final String HYDROGEN_COUNT = "org.openscience.cdk.iupac.generator.HYDROGEN_COUNT";

    public static final String BROMO_COUNT = "org.openscience.cdk.iupac.generator.BROMO_COUNT";

    public static final String CHLORO_COUNT = "org.openscience.cdk.iupac.generator.CHLORO_COUNT";

    public static final String FLUORO_COUNT = "org.openscience.cdk.iupac.generator.FLUORO_COUNT";
}
