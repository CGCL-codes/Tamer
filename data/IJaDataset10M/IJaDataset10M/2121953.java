package org.netxilia.api.value;

import java.io.Serializable;
import org.joda.time.ReadablePartial;

public class ErrorValue implements IGenericValue, Serializable {

    private static final long serialVersionUID = -813056889077405223L;

    private final ErrorValueType errorType;

    public ErrorValue(ErrorValueType errorType) {
        this.errorType = errorType;
    }

    public ErrorValue(String rawString) {
        String errorType = rawString.substring(1);
        this.errorType = ErrorValueType.valueOf(errorType);
    }

    public ErrorValueType getErrorType() {
        return errorType;
    }

    public Boolean getBooleanValue() {
        return null;
    }

    public Double getNumberValue() {
        return null;
    }

    public String getStringValue() {
        return "#" + errorType.name();
    }

    @Override
    public ReadablePartial getDateValue() {
        return null;
    }

    public GenericValueType getValueType() {
        return GenericValueType.ERROR;
    }

    @Override
    public String toString() {
        return "Formula Error: " + getErrorType();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((errorType == null) ? 0 : errorType.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ErrorValue other = (ErrorValue) obj;
        if (errorType == null) {
            if (other.errorType != null) {
                return false;
            }
        } else if (!errorType.equals(other.errorType)) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(IGenericValue value) {
        if (value == null) {
            return 1;
        }
        if (!(value instanceof ErrorValue)) {
            return -1;
        }
        ErrorValueType other = ((ErrorValue) value).errorType;
        if (other == null) {
            if (this.errorType == null) {
                return 0;
            }
            return 1;
        }
        return this.errorType.compareTo(other);
    }
}
