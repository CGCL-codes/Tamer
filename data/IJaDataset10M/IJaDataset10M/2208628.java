package org.openexi.fujitsu.schema;

/**
 * This class represents a typed data value that results from simple-type
 * schema validation. A typed data value is either an atomic or a list value.
 */
public abstract class TypedValue {

    private final EXISchema m_corpus;

    protected int m_type;

    TypedValue(EXISchema corpus) {
        m_corpus = corpus;
        m_type = EXISchema.NIL_NODE;
    }

    /**
   * Returns true if it is a list value, otherwise (i&#46;e&#46; atomic value)
   * returns false.
   * @return true if it is a list value
   */
    public abstract boolean isList();

    /**
   * Return the schema corpus that contains the type.
   * @return schema corpus
   */
    public final EXISchema getEXISchema() {
        return m_corpus;
    }

    /**
   * Set type value.
   * @param type either an atomic type or a list type
   */
    void setType(int type) {
        m_type = type;
    }

    /**
   * Returns an atomic type or a list type based on whether it is
   * AtomicTypedValue or ListTypedValue.
   * @return either an AtomicTypedValue or a ListTypedValue
   */
    public int getType() {
        return m_type;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof TypedValue) {
            TypedValue that = (TypedValue) obj;
            return this.m_corpus == that.m_corpus && this.m_type == that.m_type;
        }
        return false;
    }
}
