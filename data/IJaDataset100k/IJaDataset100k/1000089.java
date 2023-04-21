package self.micromagic.eterna.sql.converter;

import self.micromagic.eterna.digester.ConfigurationException;
import self.micromagic.eterna.share.TypeManager;
import self.micromagic.util.ObjectRef;
import self.micromagic.util.StringRef;

public class FloatConverter extends ObjectConverter {

    private Float DEFAULT_VALUE = new Float(0.0F);

    public int getConvertType(StringRef typeName) {
        if (typeName != null) {
            typeName.setString("float");
        }
        return TypeManager.TYPE_FLOAT;
    }

    public float getResult(Object result) throws ConfigurationException {
        try {
            return this.convertToFloat(result);
        } catch (Exception ex) {
            throw getErrorTypeException(result, "float");
        }
    }

    public float convertToFloat(Object value) {
        if (value == null) {
            return 0;
        }
        if (value instanceof Number) {
            return ((Number) value).floatValue();
        }
        if (value instanceof String) {
            try {
                return Float.parseFloat((String) value);
            } catch (NumberFormatException ex) {
            }
        }
        if (value instanceof ObjectRef) {
            ObjectRef ref = (ObjectRef) value;
            if (ref.isNumber()) {
                return (float) ref.doubleValue();
            } else if (ref.isString()) {
                try {
                    return Float.parseFloat(ref.toString());
                } catch (NumberFormatException ex) {
                }
            }
        }
        throw new ClassCastException(getCastErrorMessage(value, "float"));
    }

    public float convertToFloat(String value) {
        if (value == null) {
            return 0;
        }
        try {
            return Float.parseFloat(value);
        } catch (NumberFormatException ex) {
        }
        throw new ClassCastException(getCastErrorMessage(value, "float"));
    }

    public Object convert(Object value) {
        if (value instanceof Float) {
            return (Float) value;
        }
        try {
            return new Float(this.convertToFloat(value));
        } catch (Exception ex) {
            if (this.needThrow) {
                if (ex instanceof RuntimeException) {
                    throw (RuntimeException) ex;
                }
                throw new ClassCastException(getCastErrorMessage(value, "float"));
            } else {
                return DEFAULT_VALUE;
            }
        }
    }

    public Object convert(String value) {
        try {
            return new Float(this.convertToFloat(value));
        } catch (Exception ex) {
            if (this.needThrow) {
                if (ex instanceof RuntimeException) {
                    throw (RuntimeException) ex;
                }
                throw new ClassCastException(getCastErrorMessage(value, "float"));
            } else {
                return DEFAULT_VALUE;
            }
        }
    }
}
