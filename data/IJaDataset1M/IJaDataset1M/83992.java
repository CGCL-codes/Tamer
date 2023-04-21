package org.gaea.lib.selector;

import java.lang.reflect.Field;
import java.util.Collection;
import org.gaea.common.exception.GaeaException;
import org.gaea.common.exception.OQLTreatmentException;
import org.gaea.common.exception.GaeaException.Error;
import org.gaea.lib.struct.select.attribute.*;
import org.gaea.lib.struct.select.type.Type;
import org.gaea.lib.treatment.AggregationTreatment;

/**
 * 
 * @author jsgoupil
 * @author bbahi
 */
public class OQLData {

    /**
	 * NULL: Null Variable
	 * 
	 * FIELD: Field in a class
	 * 
	 * DATA: Could a text, integer, date, boolean, etc.
	 * 
	 * COLLECTION: List of data
	 * 
	 * CALCUL: calculation operation
	 * 
	 * @author jsgoupil
	 */
    public enum DataType {

        NULL, FIELD, DATA, COLLECTION, CALCUL, CAST
    }

    /**
	 * Data type
	 */
    private DataType _dataType;

    /**
	 * Data attribute
	 */
    private Attribute _attribute;

    /**
	 * Data result object
	 */
    private Object _obj;

    /**
	 * Data result name used of create structure class of projection
	 */
    private String _objName;

    /**
	 * cast class for an OQLData of type cast
	 */
    private Type _castType;

    /**
	 * @return the objName
	 */
    public String getObjName() {
        return _objName;
    }

    /**
	 * collection of group treatment used for handle partition
	 */
    private Collection _groupCollection;

    /**
	 * Constructor
	 * 
	 * @param attribute :
	 *            Data Attribute
	 * @throws GaeaException
	 */
    public OQLData(Attribute attribute) throws GaeaException {
        this(attribute, null);
    }

    /**
	 * Constructor
	 * 
	 * @param attribute :
	 *            Data attribute
	 * @param groupCollection :
	 *            collection of partition treatment
	 * @throws GaeaException
	 */
    public OQLData(Attribute attribute, Collection groupCollection) throws GaeaException {
        _groupCollection = groupCollection;
        _attribute = attribute;
        execute();
    }

    /**
	 * handles the attribute data
	 * 
	 * @author jsgoupil, bbahi
	 * @throws GaeaException
	 */
    public void execute() throws GaeaException {
        Attribute.AttributeType type = _attribute.getType();
        switch(type) {
            case IDENTIFIER:
                {
                    AttributeIdentifier attributeTyped = (AttributeIdentifier) ((AttributeIdentifier) _attribute).clone();
                    _dataType = DataType.FIELD;
                    if (attributeTyped.hasChild()) {
                        Attribute identifier = attributeTyped.getChild().clone();
                        _obj = new OQLIdentifier(identifier, attributeTyped.getIdentifier());
                        _objName = ((OQLIdentifier) _obj).getIdentifierName();
                    } else {
                        OQLData data = new OQLData(attributeTyped.getIdentifier(), getGroupCollection());
                        _obj = data.getObj();
                        _objName = data.getObjName();
                    }
                    break;
                }
            case IDENTIFIERNAME:
                {
                    _obj = _attribute;
                    _objName = ((AttributeIdentifierName) _attribute).getAttribute();
                    break;
                }
            case POSTFIX:
                {
                    AttributePostFix attributeTyped = (AttributePostFix) _attribute;
                    Attribute identifier = attributeTyped.getAttribute2();
                    if (identifier.getType() == Attribute.AttributeType.IDENTIFIER || identifier.getType() == Attribute.AttributeType.IDENTIFIERNAME) {
                        _obj = new OQLIdentifier(identifier, attributeTyped.getAttribute1());
                        _objName = ((OQLIdentifier) _obj).getIdentifierName();
                        _dataType = DataType.FIELD;
                    } else {
                        OQLData data = new OQLData(identifier, getGroupCollection());
                        _obj = data.getObj();
                        _objName = data._objName;
                        _dataType = data.getDataType();
                    }
                    break;
                }
            case ABS:
                {
                    AttributeAbs attributeTyped = (AttributeAbs) _attribute;
                    OQLData data = new OQLData(attributeTyped.getAttribute(), getGroupCollection());
                    _dataType = data.getDataType();
                    if (data.getDataType() == DataType.DATA) {
                        if (data.getObj().getClass().getGenericSuperclass() == Number.class) {
                            _obj = Math.abs(((Number) data.getObj()).doubleValue());
                        } else {
                            throw new OQLTreatmentException(Error.TREATMENT_DataTypeNoteANumber, "abs");
                        }
                    } else if (data.getDataType() == DataType.FIELD) {
                        _obj = data.getObj();
                        _objName = "Abs_" + data._objName;
                    } else {
                        throw new OQLTreatmentException(Error.TREATMENT_DataTypeNoteANumber, "abs");
                    }
                    break;
                }
            case PLUS:
                {
                    AttributePlus attributeTyped = (AttributePlus) _attribute;
                    OQLData data = new OQLData(attributeTyped.getAttribute(), getGroupCollection());
                    _dataType = data.getDataType();
                    if (data.getDataType() == DataType.DATA) {
                        if (data.getObj().getClass().getGenericSuperclass() == Number.class) {
                            _obj = +((Number) data.getObj()).doubleValue();
                        } else {
                            throw new OQLTreatmentException(Error.TREATMENT_DataTypeNoteANumber, "+");
                        }
                    } else if (data.getDataType() == DataType.FIELD) {
                        _obj = data.getObj();
                        _objName = "Plus_" + data._objName;
                    } else {
                        throw new OQLTreatmentException(Error.TREATMENT_DataTypeNoteANumber, "-");
                    }
                    break;
                }
            case MINUS:
                {
                    AttributeMinus attributeTyped = (AttributeMinus) _attribute;
                    OQLData data = new OQLData(attributeTyped.getAttribute(), getGroupCollection());
                    _dataType = data.getDataType();
                    if (data.getDataType() == DataType.DATA) {
                        _dataType = DataType.DATA;
                        if (data.getObj().getClass().getGenericSuperclass() == Number.class) {
                            _obj = UtilityTreatment.getAbsNumber((Number) data.getObj());
                        } else {
                            throw new OQLTreatmentException(Error.TREATMENT_DataTypeNoteANumber, "-");
                        }
                    } else if (data.getDataType() == DataType.FIELD) {
                        _obj = data.getObj();
                        _objName = "Minus_" + data._objName;
                    } else {
                        throw new OQLTreatmentException(Error.TREATMENT_DataTypeNoteANumber, "-");
                    }
                    break;
                }
            case AGGREGATION:
                {
                    AttributeAggregation attributeTyped = (AttributeAggregation) _attribute;
                    Collection collection = UtilityTreatment.getInstance().getCollection(attributeTyped.getAttribute(), true, getGroupCollection());
                    AggregationTreatment aggt = new AggregationTreatment();
                    _obj = aggt.getAggregationResult(attributeTyped.getAggregationType(), collection);
                    String attributeCollectionName = "";
                    if (getGroupCollection() == null && collection != null && !collection.isEmpty()) {
                        Field fields[] = collection.iterator().next().getClass().getFields();
                        attributeCollectionName = "_" + fields[0].getName();
                    }
                    _objName = attributeTyped.getAggregationType().name() + attributeCollectionName;
                    _dataType = DataType.DATA;
                    break;
                }
            case SELECT:
                {
                    _obj = UtilityTreatment.getInstance().getCollection(_attribute, true, getGroupCollection());
                    _objName = "collection";
                    _dataType = DataType.COLLECTION;
                    break;
                }
            case AS:
                {
                    AttributeAs attributeTyped = (AttributeAs) _attribute;
                    OQLData data2 = new OQLData(attributeTyped.getAttribute2(), getGroupCollection());
                    _objName = data2._objName;
                    OQLData data = new OQLData(attributeTyped.getAttribute1(), getGroupCollection());
                    _obj = data.getObj();
                    _dataType = data.getDataType();
                    break;
                }
            case FIELD:
                {
                    AttributeField attributeTyped = (AttributeField) _attribute;
                    OQLData data2 = new OQLData(attributeTyped.getIdentifier());
                    _objName = data2._objName;
                    OQLData data = new OQLData(attributeTyped.getAttribute(), getGroupCollection());
                    _obj = data.getObj();
                    _dataType = data.getDataType();
                    break;
                }
            case IN:
                {
                    AttributeIn attributeTyped = (AttributeIn) _attribute;
                    OQLData data2 = new OQLData(attributeTyped.getAttribute1(), getGroupCollection());
                    _objName = data2._objName;
                    OQLData data = new OQLData(attributeTyped.getAttribute2(), getGroupCollection());
                    _obj = data.getObj();
                    _dataType = data.getDataType();
                    break;
                }
            case CAST:
                OQLData data = new OQLData(((AttributeCast) _attribute).getAttribute(), getGroupCollection());
                _dataType = data.getDataType();
                _objName = data._objName;
                if (data.getDataType() == DataType.DATA || data._dataType == DataType.COLLECTION) {
                    Type castType = ((AttributeCast) _attribute).getCastType();
                    _obj = OQLCasting.cast(data.getObj(), castType);
                } else {
                    _obj = data.getObj();
                }
                break;
            case STRING:
                _dataType = DataType.DATA;
                _obj = ((AttributeString) _attribute).getAttribute();
                break;
            case INTEGER:
                _dataType = DataType.DATA;
                _obj = Integer.valueOf(((AttributeInt) _attribute).getAttribute());
                break;
            case DATE:
                _dataType = DataType.DATA;
                _obj = ((AttributeDate) _attribute).getDate();
                break;
            case FLOAT:
                _dataType = DataType.DATA;
                _obj = Float.valueOf(((AttributeFloat) _attribute).getAttribute());
                break;
            case BOOLEAN:
                _dataType = DataType.DATA;
                _obj = Boolean.valueOf(((AttributeBoolean) _attribute).getAttribute());
                break;
            case CALCUL:
                _obj = _attribute;
                _dataType = DataType.CALCUL;
                break;
            case CHAR:
                _dataType = DataType.DATA;
                _obj = Character.valueOf(((AttributeChar) _attribute).getAttribute());
                break;
            case TIME:
                _dataType = DataType.DATA;
                _obj = ((AttributeTime) _attribute).getTime();
                break;
            case TIMESTAMP:
                _dataType = DataType.DATA;
                _obj = ((AttributeTimeStamp) _attribute).getTimeStamp();
                break;
            default:
                break;
        }
    }

    /**
	 * 
	 * @return object data
	 */
    public Object getObj() {
        return _obj;
    }

    /**
	 * @return the type
	 */
    public DataType getDataType() {
        return _dataType;
    }

    /**
	 * @param dataType
	 *            the type to set
	 */
    public void setDataType(DataType dataType) {
        _dataType = dataType;
    }

    /**
	 * @return the attribute
	 */
    public Attribute getAttribute() {
        return _attribute;
    }

    /**
	 * @param attribute
	 *            the attribute to set
	 */
    public void setAttribute(Attribute attribute) {
        _attribute = attribute;
    }

    /**
	 * @return the groupCollection
	 */
    public Collection getGroupCollection() {
        return _groupCollection;
    }

    /**
	 * @param groupCollection
	 *            the groupCollection to set
	 */
    public void setGroupCollection(Collection groupCollection) {
        _groupCollection = groupCollection;
    }

    public Type getCastType() {
        return _castType;
    }
}
