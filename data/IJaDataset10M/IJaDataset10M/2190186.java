package com.googlecode.mjorm.query;

import com.mongodb.BasicDBObject;

public class TypeCriterion extends AbstractCriterion {

    public enum Type {

        DOUBLE(1), STRING(2), OBJECT(3), ARRAY(4), BINARY(5), OBJECT_ID(7), BOOLEAN(8), DATE(9), NULL(10), REGEX(11), JAVASCRIPT(13), SYMBOL(14), JAVASCRIPT_WITH_SCOPE(15), INT32(16), TIMESTAMP(17), INT64(18), MIN_KEY(255), MAX_KEY(127);

        private Number code;

        Type(Number code) {
            this.code = code;
        }

        public Number getCode() {
            return code;
        }
    }

    private Number typeCode;

    public TypeCriterion(Number typeCode) {
        this.typeCode = typeCode;
    }

    public TypeCriterion(Type type) {
        this(type.getCode());
    }

    /**
	 * @return the typeCode
	 */
    public Number getTypeCode() {
        return typeCode;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public Object toQueryObject() {
        return new BasicDBObject("$type", typeCode);
    }
}
