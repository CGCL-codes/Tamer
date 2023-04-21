package com.volantis.mcs.build.themes.definitions.types;

/**
 * Visitor interface for navigating Type hierarchies.
 */
public interface TypeVisitor {

    /**
     * Visit a Keywords type.
     * @param visitee The Keywords being visited
     * @param obj A general-purpose argument
     */
    public void visitKeywords(Keywords visitee, Object obj);

    /**
     * Visit a PairType type.
     * @param visitee The PairType being visited
     * @param obj A general-purpose argument
     */
    public void visitPairType(PairType visitee, Object obj);

    /**
     * Visit a ChoiceType type.
     * @param visitee The ChoiceType being visited
     * @param obj A general-purpose argument
     */
    public void visitChoiceType(ChoiceType visitee, Object obj);

    /**
     * Visit a TypeRef type.
     * @param visitee The TypeRef being visited
     * @param obj A general-purpose argument
     */
    public void visitTypeRef(TypeRef visitee, Object obj);

    /**
     * Visit a FractionType type.
     * @param visitee The FractionType being visited
     * @param obj A general-purpose argument
     */
    public void visitFractionType(FractionType visitee, Object obj);
}
