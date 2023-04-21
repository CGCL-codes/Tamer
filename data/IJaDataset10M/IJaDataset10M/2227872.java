package com.patientis.model.common;

import com.patientis.framework.exceptions.InvalidValueDataTypeException;
import com.patientis.model.reference.ValueDataTypeReference;

/**
 * ParameterValueModel is a business object represents the parametervalue
 *
 * Design Patterns: <a href="/functionality/rm/4019548.html">BaseModel</a>
 */
public class ParameterValueModel extends ParameterValueDataModel implements IGetValue {

    private static final long serialVersionUID = 1;

    /**
	 * Default constructor
	 */
    public ParameterValueModel() {
    }

    /**
	 * Get the value - double, int, String or Date
	 * 
	 * @return
	 */
    public Object getValue() {
        switch(ValueDataTypeReference.get(super.getDataTypeRef().getId())) {
            case BOOLEAN:
                return super.getValueInt();
            case INTEGER:
                return super.getValueInt();
            case DATE:
                return super.getValueDate();
            case DOUBLE:
                return super.getValueDouble();
            case STRING:
                return super.getValueString();
            case REFERENCE:
                return super.getValueRef();
            case TERM:
                return super.getValueTermId();
            default:
                throw new InvalidValueDataTypeException();
        }
    }

    /**
	 * Get the value - double, int, String or Date
	 * 
	 * @return
	 */
    public void setValue(Object value) throws Exception {
        switch(ValueDataTypeReference.get(super.getDataTypeRef().getId())) {
            case BOOLEAN:
                setValueInt((long) Converter.convertBooleanInteger(value));
                break;
            case INTEGER:
                setValueInt((long) Converter.convertInteger(value));
                break;
            case DATE:
                setValueDate(Converter.convertDateTimeModel(value));
                break;
            case DOUBLE:
                setValueDouble(Converter.convertDouble(value));
                break;
            case STRING:
                setValueString(Converter.convertString(value));
                break;
            case REFERENCE:
                setValueRef(Converter.convertDisplayModel(value));
                break;
            case TERM:
                setValueTermId(Converter.convertTermId(value));
                break;
            default:
                throw new InvalidValueDataTypeException();
        }
    }

    @Override
    public Long getChildFormId() {
        return 0L;
    }
}
