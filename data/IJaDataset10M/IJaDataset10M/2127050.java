package org.jz.xs.entity.valueholders;

import java.math.RoundingMode;
import org.jz.xs.entity.EntityField;
import org.jz.xs.tools.TypeConverter;

public class StringValueHolder extends AbstractValueHolder {

    public StringValueHolder(EntityField _Requisite) {
        super(_Requisite);
    }

    @Override
    public String getValueAsString() throws Exception {
        if (fValue == null) {
            return "";
        } else {
            return (String) fValue;
        }
    }

    @Override
    public int getValueAsInteger() throws Exception {
        throw new Exception("Cannot extract Integer value from String datatype");
    }

    @Override
    public double getValueAsFloat() throws Exception {
        throw new Exception("Cannot extract Float value from String datatype");
    }

    @Override
    public java.math.BigDecimal getValueAsCurrency() throws Exception {
        throw new Exception("Cannot extract Currency value from String datatype");
    }

    @Override
    public java.util.Date getValueAsDate() throws Exception {
        throw new Exception("Cannot extract Date value from String datatype");
    }

    @Override
    public java.math.BigDecimal getValueAsDecimal() throws Exception {
        throw new Exception("Cannot extract Decimal value from String datatype");
    }

    @Override
    public String getOrgValueAsString() throws Exception {
        if (fOrgValue == null) {
            return "";
        } else {
            return (String) fOrgValue;
        }
    }

    @Override
    public int getOrgValueAsInteger() throws Exception {
        throw new Exception("Cannot extract Integer value from String datatype");
    }

    @Override
    public double getOrgValueAsFloat() throws Exception {
        throw new Exception("Cannot extract Float value from String datatype");
    }

    @Override
    public java.math.BigDecimal getOrgValueAsCurrency() throws Exception {
        throw new Exception("Cannot extract Currency value from String datatype");
    }

    @Override
    public java.util.Date getOrgValueAsDate() throws Exception {
        throw new Exception("Cannot extract Date value from String datatype");
    }

    @Override
    public java.math.BigDecimal getOrgValueAsDecimal() throws Exception {
        throw new Exception("Cannot extract Decimal value from String datatype");
    }

    @Override
    public void setValueAsString(String _Value) throws Exception {
        fValue = _Value;
        notifyListeners();
    }

    @Override
    public void setValueAsInteger(int _Value) throws Exception {
        fValue = TypeConverter.convertIntToStr(_Value);
        notifyListeners();
    }

    @Override
    public void setValueAsFloat(double _Value) throws Exception {
        fValue = TypeConverter.convertFloatToStr(_Value);
        notifyListeners();
    }

    @Override
    public void setValueAsCurrency(java.math.BigDecimal _Value) throws Exception {
        fValue = _Value.setScale(2, RoundingMode.HALF_UP).toString();
        notifyListeners();
    }

    @Override
    public void setValueAsDate(java.util.Date _Value) throws Exception {
        if (_Value == null) {
            fValue = null;
        } else {
            fValue = TypeConverter.convertDateToStr(_Value);
        }
        notifyListeners();
    }

    @Override
    public void setValueAsDecimal(java.math.BigDecimal _Value) throws Exception {
        fValue = _Value.toString();
        notifyListeners();
    }

    public void setValue(Object _Value) throws Exception {
        if ((_Value == null) || (_Value instanceof String)) {
            fValue = _Value;
        } else {
            throw new Exception("Invalid datatype for String value holder");
        }
    }

    /**
    * ��������� ���������� �������� ��������� 
    * @param _Value ������������ ��������� ��������
    * @throws Exception
    */
    public void setOrgValue(Object _Value) throws Exception {
        if ((_Value == null) || (_Value instanceof String)) {
            fValue = _Value;
            fOrgValue = _Value;
        } else {
            throw new Exception("Invalid datatype for String value holder");
        }
    }
}
