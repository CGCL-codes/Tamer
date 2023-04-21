package org.jdmp.core.variable;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import org.jdmp.core.util.ObservableMap;
import org.ujmp.core.Matrix;

public interface Variables extends ObservableMap<Variable> {

    public String getAllAsString(String variableKey);

    public boolean getAsBoolean(String variableKey);

    public byte getAsByte(String variableKey);

    public char getAsChar(String variableKey);

    public double getAsDouble(String variableKey);

    public float getAsFloat(String variableKey);

    public int getAsInt(String variableKey);

    public long getAsLong(String variableKey);

    public short getAsShort(String variableKey);

    public Date getAsDate(String variableKey);

    public BigDecimal getAsBigDecimal(String variableKey);

    public BigInteger getAsBigInteger(String variableKey);

    public Matrix getMatrix(String variableKey);

    public String getAsString(String variableKey);

    public Object getAsObject(String variableKey);

    public Object getObject(String variableKey);

    public void setMatrix(String variableKey, Matrix matrix);

    public void setObject(String variableKey, Object value);
}
