package org.jmlspecs.eclipse.jmldom;

/**
 * This interface identifies AST nodes that are specific to JML
 */
public interface JmlNode {

    /** Returns the scanner token used to create this node
   * (this is not the same as the AST Node type, since multiple
   * scanner tokens might use the same node type)
   * @return the scanner token used to create this node
   */
    public int getToken();

    public String getTokenString();

    public static final int JML_RESULT = 84;

    public static final int JML_EXPRESSION_STATEMENT = 85;

    public static final int JML_REQUIRES_CLAUSE = 86;

    public static final int JML_ENSURES_CLAUSE = 87;

    public static final int JML_INVARIANT_CLAUSE = 88;

    public static final int JML_MODEL_IMPORT = 89;

    public static final int JML_ASSUME_STATEMENT = 90;

    public static final int JML_LOCAL_GHOST_STATEMENT = 91;

    public static final int JML_SET_STATEMENT = 92;

    public static final int JML_CLASS_PREDICATE_CLAUSE = 93;

    public static final int JML_FIELD_DECLARATION = 94;

    public static final int JML_SIMPLE_EXPRESSION = 95;

    public static final int JML_OLD_EXPRESSION = 96;

    public static final int JML_QUANTIFIED_EXPRESSION = 97;

    public static final int JML_METHOD_INVOCATION = 98;

    public static final int JML_LBL_EXPRESSION = 99;

    public static final int JML_NOT_SPECIFIED = 100;

    public static final int JML_NOT_ASSIGNED_EXPRESSION = 101;

    public static final int JML_IS_INITIALIZED_EXPRESSION = 102;

    public static final int JML_TYPE_LITERAL = 103;

    public static final int JML_FIELD_SPECIFICATION = 104;

    public static final int JML_REFINE_SPECIFICATION = 105;

    public static final int JML_ERROR_EXPRESSION = 106;

    public static final int JML_BODY_DECLARATION = 107;

    public static final int JML_MODIFIERS_DECLARATION = 108;

    public static final int JML_INFORMAL_EXPRESSION = 109;

    public static final int JML_FIELD_WILD_ACCESS = 110;

    public static final int JML_ARRAY_RANGE_ACCESS = 111;

    public static final int JML_SIGNALS_CLAUSE = 112;

    public static final int JML_SIGNALS_ONLY_CLAUSE = 113;

    public static final int JML_ASSIGNABLE_CLAUSE = 114;

    public static final int JML_METHOD_SPECIFICATION_CASE = 115;

    public static final int JML_DECLARATION_METHOD_SPECIFICATION = 116;

    public static final int JML_SIMPLE_METHOD_SPECIFICATION = 117;

    public static final int JML_EXPRESSION_METHOD_SPECIFICATION = 118;

    public static final int JML_CLASS_CONSTRAINT_CLAUSE = 119;

    public static final int JML_REPRESENTS_CLAUSE = 120;
}
