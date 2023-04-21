package org.gaea.lib.selector;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;
import org.gaea.common.exception.GaeaException;
import org.gaea.common.exception.OQLTreatmentException;
import org.gaea.common.exception.GaeaException.Error;
import org.gaea.lib.struct.select.attribute.Attribute;
import org.gaea.lib.struct.select.attribute.AttributeAs;
import org.gaea.lib.struct.select.attribute.AttributeField;
import org.gaea.lib.struct.select.attribute.Attribute.OQLPredicateType;
import org.gaea.lib.struct.select.constraint.ConstraintGroupBy;

/**
 * this class handles grouping collection
 * 
 * @author bbahi
 */
public class OQLGroupBy {

    /**
	 * Constants string
	 */
    public static final String PARTITION_FIELD = "partition";

    public static final String SET_METHOD_NAME = "set";

    public static final String GET_METHOD_NAME = "get";

    /**
	 * Group by attribute
	 */
    private ConstraintGroupBy _groupByAttribute;

    /**
	 * OQL tables
	 */
    private OQLTables _tables;

    /**
	 * projection attributes
	 */
    private OQLProjection _queryProjection;

    /**
	 * Group structure class
	 */
    private Class _groupStructClass;

    /**
	 * If group query has aggregation projection
	 */
    private boolean _hasAggregationProjection;

    /**
	 * tool for formatting identifier attribute for join table
	 */
    private OQLJointAttributeFormatting _jointFormatting;

    /**
	 * Constructor
	 * 
	 * @param groupByAttribute :
	 *            group by constraint attribute
	 * @param tables :
	 *            query table
	 * @param projection :
	 *            projection attribute
	 */
    public OQLGroupBy(ConstraintGroupBy groupByAttribute, OQLTables tables, OQLProjection projection) {
        setGroupByAttribute(groupByAttribute);
        setTables(tables);
        setQueryProjection(projection);
        setHasAggregationProjection();
        _jointFormatting = new OQLJointAttributeFormatting(tables);
    }

    /**
	 * Create and compile group structure
	 * 
	 * @param collection :
	 *            collection of grouping
	 * @throws GaeaException
	 */
    private void CreateGroupStructClass(Collection collection) throws GaeaException {
        Vector<Attribute> vec = getGroupByAttribute().getAttributeField();
        Vector<OQLFieldStruct> groupStructFields = new Vector<OQLFieldStruct>();
        for (Attribute attribute : vec) {
            Attribute att = ((AttributeField) attribute).getAttribute();
            Attribute.AttributeType type = att.getType();
            String attName = ((AttributeField) attribute).getIdentifier().toString();
            switch(type) {
                case RESTRICTION:
                case OR:
                case ORELSE:
                case AND:
                case ANDTHEN:
                    {
                        groupStructFields.add(new OQLFieldStruct(boolean.class, attName));
                        break;
                    }
                case IDENTIFIER:
                    {
                        OQLData data = new OQLData(attribute);
                        groupStructFields.add(new OQLFieldStruct(((OQLIdentifier) data.getObj()).getIdentifierClass(), attName));
                    }
                default:
                    break;
            }
        }
        if (isHasAggregationProjection()) {
            groupStructFields.addAll(getAggregationProjectionFields(collection));
        } else {
            groupStructFields.add(new OQLFieldStruct(Collection.class, PARTITION_FIELD));
        }
        OQLCreateStruct structCreator = new OQLCreateStruct(groupStructFields);
        if (getQueryProjection().getOrdering() != null && getQueryProjection().getOrdering().getType() != OQLPredicateType.ATOMIC) {
            String compareFunction = getQueryProjection().getCompareFunction(groupStructFields);
            structCreator.setCompareToFunctionCode(compareFunction);
        }
        setGroupStructClass(structCreator.createAndCompileStruct());
    }

    /**
	 * @return the groupByAttribute
	 */
    public ConstraintGroupBy getGroupByAttribute() {
        return _groupByAttribute;
    }

    /**
	 * @param groupByAttribute
	 *            the groupByAttribute to set
	 */
    public void setGroupByAttribute(ConstraintGroupBy groupByAttribute) {
        _groupByAttribute = groupByAttribute;
    }

    /**
	 * Verify if group by constraint attributes are found in projection
	 * attributes when group use an aggregation
	 * 
	 * @return true if constraint attributes are valid
	 */
    public boolean isValidConstraint() {
        boolean isValid = true;
        Vector<Attribute> vecAttribute = getQueryProjection().getAttributes().getAttributes();
        if (vecAttribute.size() == 1 && vecAttribute.get(0).getType() == Attribute.AttributeType.ALL) {
            return true;
        }
        for (Attribute attribute : getGroupByAttribute().getAttributeField()) {
            Attribute att = ((AttributeField) attribute).getAttribute();
            Attribute.AttributeType type = att.getType();
            switch(type) {
                case IDENTIFIER:
                    {
                        isValid = false;
                        for (Attribute projectionAttribute : vecAttribute) {
                            if (projectionAttribute.equals(attribute) || projectionAttribute.equals(att)) {
                                isValid = true;
                                break;
                            }
                        }
                    }
                default:
                    break;
            }
        }
        return isValid;
    }

    /**
	 * @return the tables
	 */
    public OQLTables getTables() {
        return _tables;
    }

    /**
	 * @param tables
	 *            the tables to set
	 */
    public void setTables(OQLTables tables) {
        _tables = tables;
    }

    /**
	 * Group collection by constraint attributes
	 * 
	 * @param collection :
	 *            collection of grouping
	 * @return collection grouped
	 * @throws GaeaException
	 * @throws SecurityException
	 */
    public Collection groupCollection(Collection collection) throws GaeaException {
        Vector<Attribute> vec = getGroupByAttribute().getAttributeField();
        CreateGroupStructClass(collection);
        Collection resultCollection = new Vector<Object>();
        Object groupObject = null;
        int size = vec.size();
        for (int i = 0; i < size; i++) {
            Attribute attribute = vec.get(i);
            Attribute att = ((AttributeField) attribute).getAttribute();
            String attName = ((AttributeField) attribute).getIdentifier().toString();
            Attribute.AttributeType type = att.getType();
            switch(type) {
                case RESTRICTION:
                case OR:
                case ORELSE:
                case AND:
                case ANDTHEN:
                    {
                        Filter filter = Filter.createFilter(att, getTables());
                        groupObject = CreateGroupObject();
                        Collection partition = filter.applyFilter(collection);
                        if (isHasAggregationProjection()) {
                            setProjectionFields(partition, groupObject);
                        } else {
                            SetFieldGroupObject(groupObject, partition, PARTITION_FIELD);
                        }
                        SetFieldGroupObject(groupObject, true, attName);
                        resultCollection.add(groupObject);
                        break;
                    }
                case IDENTIFIER:
                    {
                        resultCollection = getIdentifierGroups(collection, i);
                        i = size;
                        break;
                    }
                default:
                    break;
            }
        }
        resultCollection = applyHavingClause(resultCollection);
        if (getQueryProjection().getOrdering() != null && getQueryProjection().getOrdering().getType() != OQLPredicateType.ATOMIC) {
            return getQueryProjection().getOrdering().orderingCollection(resultCollection);
        }
        return resultCollection;
    }

    /**
	 * Grouping collection by identifier attribute
	 * 
	 * @param collection :
	 *            collection of grouping
	 * @param fieldAttributeIndex :
	 *            index of identifier of create group
	 * @return collection grouped by identifier attribute
	 * @throws GaeaException
	 * @throws GaeaException
	 */
    public Collection getIdentifierGroups(Collection collection, int fieldAttributeIndex) throws GaeaException {
        int groupConstraintFiledCount = getGroupByAttribute().getAttributeField().size();
        if (fieldAttributeIndex >= groupConstraintFiledCount) {
            return collection;
        }
        Attribute filedAttribute = ((AttributeField) getGroupByAttribute().getAttributeField().get(fieldAttributeIndex)).getAttribute();
        String attName = ((AttributeField) getGroupByAttribute().getAttributeField().get(fieldAttributeIndex)).getIdentifier().toString();
        OQLData attributeData = new OQLData(filedAttribute);
        if (attributeData.getDataType() == OQLData.DataType.FIELD) {
            Vector<Object> vec = new Vector<Object>(collection);
            Collection resultCollection = new Vector<Object>();
            Attribute identifier = _jointFormatting.formattingIdentifierAttribute((OQLIdentifier) attributeData.getObj());
            while (!vec.isEmpty()) {
                int index = fieldAttributeIndex;
                Object object = vec.get(0);
                Collection<Object> groupCollection = new Vector<Object>();
                Object attributeObject = OQLPolymorphism.getChildObject(object, identifier);
                groupCollection.add(object);
                vec.remove(object);
                int i = 0;
                for (i = 0; i < vec.size(); i++) {
                    Object nextObject = vec.get(i);
                    if (attributeObject.equals(OQLPolymorphism.getChildObject(nextObject, identifier))) {
                        groupCollection.add(nextObject);
                        vec.remove(nextObject);
                        i--;
                    }
                }
                if (++index < groupConstraintFiledCount) {
                    Collection partition = getIdentifierGroups(groupCollection, index);
                    Iterator it = partition.iterator();
                    while (it.hasNext()) {
                        SetFieldGroupObject(it.next(), attributeObject, attName);
                    }
                    resultCollection.addAll(partition);
                } else {
                    Object groupObject = CreateGroupObject();
                    SetFieldGroupObject(groupObject, attributeObject, attName);
                    if (isHasAggregationProjection()) {
                        setProjectionFields(groupCollection, groupObject);
                    } else {
                        SetFieldGroupObject(groupObject, groupCollection, PARTITION_FIELD);
                    }
                    resultCollection.add(groupObject);
                }
            }
            return resultCollection;
        }
        return getIdentifierGroups(collection, fieldAttributeIndex++);
    }

    /**
	 * @return the groupStructClass
	 */
    public Class getGroupStructClass() {
        return _groupStructClass;
    }

    /**
	 * @param groupStructClass
	 *            the groupStructClass to set
	 */
    public void setGroupStructClass(Class groupStructClass) {
        _groupStructClass = groupStructClass;
    }

    /**
	 * Create group object by group structure class
	 * 
	 * @return group object
	 * @throws OQLTreatmentException
	 */
    private Object CreateGroupObject() throws OQLTreatmentException {
        try {
            Method[] methods = getGroupStructClass().getDeclaredMethods();
            Object groupObjet = getGroupStructClass().newInstance();
            for (Method method : methods) {
                Class[] parmetreTypes = method.getParameterTypes();
                if (parmetreTypes.length > 0 && parmetreTypes[0].equals(boolean.class)) {
                    method.invoke(groupObjet, false);
                }
            }
            return groupObjet;
        } catch (IllegalArgumentException e) {
            throw new OQLTreatmentException(Error.TREATMENT_RestrictionOnTheFly, e);
        } catch (IllegalAccessException e) {
            throw new OQLTreatmentException(Error.REFLECTION_AccessProblem, e);
        } catch (InvocationTargetException e) {
            throw new OQLTreatmentException(Error.TREATMENT_RestrictionOnTheFly, e);
        } catch (InstantiationException e) {
            throw new OQLTreatmentException(Error.TREATMENT_RestrictionOnTheFly, e);
        }
    }

    /**
	 * set a field in group object
	 * 
	 * @param groupObject :
	 *            group object where set field
	 * @param attributeObject :
	 *            object to set in field
	 * @param AttributeName :
	 *            field name
	 * @throws OQLTreatmentException
	 * @throws OQLTreatmentException
	 */
    private void SetFieldGroupObject(Object groupObject, Object attributeObject, String AttributeName) throws OQLTreatmentException {
        try {
            Method[] methods = getGroupStructClass().getDeclaredMethods();
            for (Method method : methods) {
                if (method.getName().equals(SET_METHOD_NAME + AttributeName)) {
                    method.invoke(groupObject, attributeObject);
                }
            }
        } catch (IllegalArgumentException e) {
            throw new OQLTreatmentException(Error.TREATMENT_RestrictionOnTheFly, e);
        } catch (IllegalAccessException e) {
            throw new OQLTreatmentException(Error.REFLECTION_AccessProblem, e);
        } catch (InvocationTargetException e) {
            throw new OQLTreatmentException(Error.TREATMENT_RestrictionOnTheFly, e);
        }
    }

    /**
	 * @return the queryProjection
	 */
    public OQLProjection getQueryProjection() {
        return _queryProjection;
    }

    /**
	 * @param queryProjection
	 *            the queryProjection to set
	 */
    public void setQueryProjection(OQLProjection queryProjection) {
        _queryProjection = queryProjection;
    }

    /**
	 * Getting the aggregation projection attributes fields
	 * 
	 * @param collection :
	 *            collection of grouping
	 * @return aggregation projection attributes fields
	 * @throws GaeaException
	 */
    private Vector<OQLFieldStruct> getAggregationProjectionFields(Collection collection) throws GaeaException {
        Vector<OQLFieldStruct> fields = new Vector<OQLFieldStruct>();
        for (Attribute attribute : getQueryProjection().getAttributes().getAttributes()) {
            boolean aggregation = false;
            if (attribute.getType() == Attribute.AttributeType.AGGREGATION) {
                aggregation = true;
            } else if (attribute.getType() == Attribute.AttributeType.AS) {
                AttributeAs as = (AttributeAs) attribute;
                if (as.getAttribute1().getType() == Attribute.AttributeType.AGGREGATION) {
                    aggregation = true;
                }
            } else if (attribute.getType() == Attribute.AttributeType.FIELD) {
                AttributeField field = (AttributeField) attribute;
                if (field.getAttribute().getType() == Attribute.AttributeType.AGGREGATION) {
                    aggregation = true;
                }
            }
            if (aggregation) {
                _jointFormatting.formattingAttribute(attribute);
                OQLData data = new OQLData(attribute, collection);
                fields.add(new OQLFieldStruct(data.getObj().getClass(), data.getObjName()));
            }
        }
        return fields;
    }

    /**
	 * Setting the aggregation fields in group object
	 * 
	 * @param collection :
	 *            partition collection of aggregation treatment
	 * @param groupObject :
	 *            group object to set aggregation fields
	 * @throws GaeaException
	 */
    private void setProjectionFields(Collection collection, Object groupObject) throws GaeaException {
        for (Attribute attribute : getQueryProjection().getAttributes().getAttributes()) {
            String methodName = "";
            boolean aggregation = false;
            try {
                if (attribute.getType() == Attribute.AttributeType.AGGREGATION) {
                    aggregation = true;
                } else if (attribute.getType() == Attribute.AttributeType.AS) {
                    AttributeAs as = (AttributeAs) attribute;
                    if (as.getAttribute1().getType() == Attribute.AttributeType.AGGREGATION) {
                        aggregation = true;
                    }
                } else if (attribute.getType() == Attribute.AttributeType.FIELD) {
                    AttributeField field = (AttributeField) attribute;
                    if (field.getAttribute().getType() == Attribute.AttributeType.AGGREGATION) {
                        aggregation = true;
                    }
                }
                if (aggregation) {
                    OQLData data = new OQLData(attribute, collection);
                    methodName = SET_METHOD_NAME + data.getObjName();
                    Method method = getGroupStructClass().getDeclaredMethod(methodName, data.getObj().getClass());
                    method.invoke(groupObject, data.getObj());
                }
            } catch (NoSuchMethodException e) {
                throw new OQLTreatmentException(Error.REFLECTION_MethodNotExisting, e, methodName, getGroupStructClass().getName());
            } catch (IllegalArgumentException e) {
                throw new OQLTreatmentException(Error.TREATMENT_RestrictionOnTheFly, e);
            } catch (IllegalAccessException e) {
                throw new OQLTreatmentException(Error.REFLECTION_AccessProblem, e);
            } catch (InvocationTargetException e) {
                throw new OQLTreatmentException(Error.TREATMENT_RestrictionOnTheFly, e);
            }
        }
    }

    /**
	 * filter group collection by having clause
	 * 
	 * @param collection :
	 *            group collection
	 * @return group collection filtered
	 * @throws GaeaException
	 */
    private Collection applyHavingClause(Collection collection) throws GaeaException {
        if (getGroupByAttribute().hasHavingClause()) {
            Filter filter = Filter.createFilter(getGroupByAttribute().getAttributeHaving(), getTables());
            filter.setHavingFilter(true);
            return filter.applyFilter(collection);
        }
        return collection;
    }

    /**
	 * @return the hasAggregationProjection
	 */
    public boolean isHasAggregationProjection() {
        return _hasAggregationProjection;
    }

    /**
	 * Verify if projection attribute contains an aggregation attribute and set
	 * hasAggregationProjection
	 */
    private void setHasAggregationProjection() {
        _hasAggregationProjection = false;
        for (Attribute attribute : getQueryProjection().getAttributes().getAttributes()) {
            if (attribute.getType() == Attribute.AttributeType.AGGREGATION) {
                _hasAggregationProjection = true;
            } else if (attribute.getType() == Attribute.AttributeType.AS) {
                AttributeAs as = (AttributeAs) attribute;
                if (as.getAttribute1().getType() == Attribute.AttributeType.AGGREGATION) {
                    _hasAggregationProjection = true;
                }
            } else if (attribute.getType() == Attribute.AttributeType.FIELD) {
                AttributeField field = (AttributeField) attribute;
                if (field.getAttribute().getType() == Attribute.AttributeType.AGGREGATION) {
                    _hasAggregationProjection = true;
                }
            }
        }
    }
}
