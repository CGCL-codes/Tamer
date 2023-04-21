package net.sf.etl.parsers.grammar.lite;

/**
 * The class Let
 */
public class Let extends SyntaxStatement {

    /** class identifier */
    public static final int CLASS_ID = 8;

    @Override
    public int eClassId() {
        return CLASS_ID;
    }

    /** start properties in this class */
    public static final int PROPERTIES_START = SyntaxStatement.PROPERTIES_END;

    /** a constant for the feature name */
    public static final int NAME_ID = PROPERTIES_START + 0;

    /** field for feature */
    private java.lang.String name;

    /** @return value of name*/
    public final java.lang.String getName() {
        return name;
    }

    /** 
	 * Set value of name
	 * @param _arg new value of name
	 */
    public final void setName(java.lang.String _arg) {
        name = _arg;
    }

    /** a constant for the feature operator */
    public static final int OPERATOR_ID = PROPERTIES_START + 1;

    /** field for feature */
    private java.lang.String operator;

    /** @return value of operator*/
    public final java.lang.String getOperator() {
        return operator;
    }

    /** 
	 * Set value of operator
	 * @param _arg new value of operator
	 */
    public final void setOperator(java.lang.String _arg) {
        operator = _arg;
    }

    /** a constant for the feature expression */
    public static final int EXPRESSION_ID = PROPERTIES_START + 2;

    /** field for feature */
    private Syntax expression;

    /** @return value of expression*/
    public final Syntax getExpression() {
        return expression;
    }

    /** 
	 * Set value of expression
	 * @param _arg new value of expression
	 */
    public final void setExpression(Syntax _arg) {
        if (_arg != null) {
            if (_arg.eOwner() != null) {
                throw new IllegalArgumentException("Element owner must be null");
            }
            _arg.owner = null;
            _arg.ownerFeature = EXPRESSION_ID;
        }
        Syntax _old = expression;
        if (_old != null) {
            _old.owner = null;
            _old.ownerFeature = -1;
        }
        expression = _arg;
    }

    @Override
    public Class<?> eFeatureType(int featureId) {
        switch(featureId) {
            case START_LINE_ID:
                return java.lang.Integer.class;
            case START_COLUMN_ID:
                return java.lang.Integer.class;
            case START_OFFSET_ID:
                return java.lang.Long.class;
            case END_LINE_ID:
                return java.lang.Integer.class;
            case END_COLUMN_ID:
                return java.lang.Integer.class;
            case END_OFFSET_ID:
                return java.lang.Long.class;
            case DOCUMENTATION_ID:
                return DocumentationLine.class;
            case NAME_ID:
                return java.lang.String.class;
            case OPERATOR_ID:
                return java.lang.String.class;
            case EXPRESSION_ID:
                return Syntax.class;
            default:
                throw new IllegalArgumentException("Unknown feature id" + featureId);
        }
    }

    @Override
    public Object eGet(int featureId) {
        switch(featureId) {
            case START_LINE_ID:
                return getStartLine();
            case START_COLUMN_ID:
                return getStartColumn();
            case START_OFFSET_ID:
                return getStartOffset();
            case END_LINE_ID:
                return getEndLine();
            case END_COLUMN_ID:
                return getEndColumn();
            case END_OFFSET_ID:
                return getEndOffset();
            case DOCUMENTATION_ID:
                return getDocumentation();
            case NAME_ID:
                return getName();
            case OPERATOR_ID:
                return getOperator();
            case EXPRESSION_ID:
                return getExpression();
            default:
                throw new IllegalArgumentException("Unknown feature id" + featureId);
        }
    }

    @Override
    public void eSet(int featureId, Object value) {
        switch(featureId) {
            case START_LINE_ID:
                setStartLine((java.lang.Integer) value);
                return;
            case START_COLUMN_ID:
                setStartColumn((java.lang.Integer) value);
                return;
            case START_OFFSET_ID:
                setStartOffset((java.lang.Long) value);
                return;
            case END_LINE_ID:
                setEndLine((java.lang.Integer) value);
                return;
            case END_COLUMN_ID:
                setEndColumn((java.lang.Integer) value);
                return;
            case END_OFFSET_ID:
                setEndOffset((java.lang.Long) value);
                return;
            case DOCUMENTATION_ID:
                throw new IllegalArgumentException("The feature documentation does not support set operation");
            case NAME_ID:
                setName((java.lang.String) value);
                return;
            case OPERATOR_ID:
                setOperator((java.lang.String) value);
                return;
            case EXPRESSION_ID:
                setExpression((Syntax) value);
                return;
            default:
                throw new IllegalArgumentException("Unknown feature id" + featureId);
        }
    }

    /** map from feature name to feature id */
    static final java.util.HashMap<String, Integer> FEATURE_NAME_TO_ID = createFeatureNameToIdMap();

    /** @return the created map from feature name to feature id for this class */
    static java.util.HashMap<String, Integer> createFeatureNameToIdMap() {
        java.util.HashMap<String, Integer> rc = SyntaxStatement.createFeatureNameToIdMap();
        rc.put("name", NAME_ID);
        rc.put("operator", OPERATOR_ID);
        rc.put("expression", EXPRESSION_ID);
        return rc;
    }

    /** @return the cached map from feature name to feature id for this class */
    @Override
    protected java.util.HashMap<String, Integer> eNameToIdMap() {
        return FEATURE_NAME_TO_ID;
    }

    /** array that represent values for eIsMany() */
    static final boolean IS_MANY_ARRAY[] = createIsManyArray();

    /** @return array that represent values for eIsMany() */
    static boolean[] createIsManyArray() {
        boolean rc[] = new boolean[PROPERTIES_END];
        rc[DOCUMENTATION_ID] = true;
        return rc;
    }

    @Override
    public boolean eIsMany(int featureId) {
        return IS_MANY_ARRAY[featureId];
    }

    /** end value for properites, subclasses should start from it */
    public static final int PROPERTIES_END = PROPERTIES_START + 3;
}
