package net.sf.etl.parsers.grammar.lite;

/**
 * The class StringOp
 */
public class StringOp extends TokenRefOp {

    /** class identifier */
    public static final int CLASS_ID = 33;

    @Override
    public int eClassId() {
        return CLASS_ID;
    }

    /** start properties in this class */
    public static final int PROPERTIES_START = TokenRefOp.PROPERTIES_END;

    /** a constant for the feature quote */
    public static final int QUOTE_ID = PROPERTIES_START + 0;

    /** field for feature */
    private java.lang.String quote;

    /** @return value of quote*/
    public final java.lang.String getQuote() {
        return quote;
    }

    /** 
	 * Set value of quote
	 * @param _arg new value of quote
	 */
    public final void setQuote(java.lang.String _arg) {
        quote = _arg;
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
            case WRAPPER_ID:
                return Wrapper.class;
            case QUOTE_ID:
                return java.lang.String.class;
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
            case WRAPPER_ID:
                return getWrapper();
            case QUOTE_ID:
                return getQuote();
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
            case WRAPPER_ID:
                setWrapper((Wrapper) value);
                return;
            case QUOTE_ID:
                setQuote((java.lang.String) value);
                return;
            default:
                throw new IllegalArgumentException("Unknown feature id" + featureId);
        }
    }

    /** map from feature name to feature id */
    static final java.util.HashMap<String, Integer> FEATURE_NAME_TO_ID = createFeatureNameToIdMap();

    /** @return the created map from feature name to feature id for this class */
    static java.util.HashMap<String, Integer> createFeatureNameToIdMap() {
        java.util.HashMap<String, Integer> rc = TokenRefOp.createFeatureNameToIdMap();
        rc.put("quote", QUOTE_ID);
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
        return rc;
    }

    @Override
    public boolean eIsMany(int featureId) {
        return IS_MANY_ARRAY[featureId];
    }

    /** end value for properites, subclasses should start from it */
    public static final int PROPERTIES_END = PROPERTIES_START + 1;
}
