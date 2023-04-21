package org.broadleafcommerce.openadmin.server.service.persistence.module;

import com.anasoft.os.daofusion.criteria.AssociationPath;
import com.anasoft.os.daofusion.criteria.AssociationPathElement;
import com.anasoft.os.daofusion.criteria.PersistentEntityCriteria;
import com.anasoft.os.daofusion.cto.client.CriteriaTransferObject;
import com.anasoft.os.daofusion.cto.server.CriteriaTransferObjectCountWrapper;
import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.broadleafcommerce.money.Money;
import org.broadleafcommerce.openadmin.client.dto.DynamicResultSet;
import org.broadleafcommerce.openadmin.client.dto.Entity;
import org.broadleafcommerce.openadmin.client.dto.FieldMetadata;
import org.broadleafcommerce.openadmin.client.dto.ForeignKey;
import org.broadleafcommerce.openadmin.client.dto.ForeignKeyRestrictionType;
import org.broadleafcommerce.openadmin.client.dto.MergedPropertyType;
import org.broadleafcommerce.openadmin.client.dto.OperationType;
import org.broadleafcommerce.openadmin.client.dto.PersistencePackage;
import org.broadleafcommerce.openadmin.client.dto.PersistencePerspective;
import org.broadleafcommerce.openadmin.client.dto.PersistencePerspectiveItemType;
import org.broadleafcommerce.openadmin.client.dto.Property;
import org.broadleafcommerce.openadmin.client.dto.VisibilityEnum;
import org.broadleafcommerce.openadmin.client.presentation.SupportedFieldType;
import org.broadleafcommerce.openadmin.client.service.ServiceException;
import org.broadleafcommerce.openadmin.server.cto.BaseCtoConverter;
import org.broadleafcommerce.openadmin.server.service.SandBoxContext;
import org.broadleafcommerce.openadmin.server.service.SandBoxMode;
import org.broadleafcommerce.openadmin.server.service.persistence.PersistenceManager;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.w3c.dom.DOMException;
import javax.persistence.Embedded;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * 
 * @author jfischer
 *
 */
public class BasicPersistenceModule implements PersistenceModule, RecordHelper, ApplicationContextAware {

    private static final Log LOG = LogFactory.getLog(BasicPersistenceModule.class);

    protected SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

    protected DecimalFormat decimalFormat = new DecimalFormat("0.########");

    protected ApplicationContext applicationContext;

    protected PersistenceManager persistenceManager;

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public boolean isCompatible(OperationType operationType) {
        return OperationType.ENTITY == operationType || OperationType.FOREIGNKEY == operationType;
    }

    public FieldManager getFieldManager() {
        return persistenceManager.getDynamicEntityDao().getFieldManager();
    }

    @SuppressWarnings("unchecked")
    public Serializable createPopulatedInstance(Serializable instance, Entity entity, Map<String, FieldMetadata> mergedProperties, Boolean setId) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, ParseException, NumberFormatException, InstantiationException, ClassNotFoundException {
        FieldManager fieldManager = getFieldManager();
        for (Property property : entity.getProperties()) {
            Field field = fieldManager.getField(instance.getClass(), property.getName());
            if (field == null) {
                LOG.debug("Unable to find a bean property for the reported property: " + property.getName() + ". Ignoring property.");
                continue;
            }
            Class<?> returnType = field.getType();
            String value = property.getValue();
            if (mergedProperties.get(property.getName()) != null) {
                Boolean mutable = mergedProperties.get(property.getName()).getMutable();
                Boolean readOnly = mergedProperties.get(property.getName()).getPresentationAttributes().getReadOnly();
                if ((mutable == null || mutable) && (readOnly == null || !readOnly)) {
                    if (value != null) {
                        switch(mergedProperties.get(property.getName()).getFieldType()) {
                            case BOOLEAN:
                                if (Character.class.isAssignableFrom(returnType)) {
                                    fieldManager.setFieldValue(instance, property.getName(), Boolean.valueOf(value) ? 'Y' : 'N');
                                } else {
                                    fieldManager.setFieldValue(instance, property.getName(), Boolean.valueOf(value));
                                }
                                break;
                            case DATE:
                                fieldManager.setFieldValue(instance, property.getName(), dateFormat.parse(value));
                                break;
                            case DECIMAL:
                                if (BigDecimal.class.isAssignableFrom(returnType)) {
                                    fieldManager.setFieldValue(instance, property.getName(), new BigDecimal(new Double(value)));
                                } else {
                                    fieldManager.setFieldValue(instance, property.getName(), new Double(value));
                                }
                                break;
                            case MONEY:
                                if (BigDecimal.class.isAssignableFrom(returnType)) {
                                    fieldManager.setFieldValue(instance, property.getName(), new BigDecimal(new Double(value)));
                                } else if (Double.class.isAssignableFrom(returnType)) {
                                    fieldManager.setFieldValue(instance, property.getName(), new Double(value));
                                } else {
                                    fieldManager.setFieldValue(instance, property.getName(), new Money(new Double(value)));
                                }
                                break;
                            case INTEGER:
                                if (int.class.isAssignableFrom(returnType) || Integer.class.isAssignableFrom(returnType)) {
                                    fieldManager.setFieldValue(instance, property.getName(), Integer.valueOf(value));
                                } else if (byte.class.isAssignableFrom(returnType) || Byte.class.isAssignableFrom(returnType)) {
                                    fieldManager.setFieldValue(instance, property.getName(), Byte.valueOf(value));
                                } else if (short.class.isAssignableFrom(returnType) || Short.class.isAssignableFrom(returnType)) {
                                    fieldManager.setFieldValue(instance, property.getName(), Short.valueOf(value));
                                } else if (long.class.isAssignableFrom(returnType) || Long.class.isAssignableFrom(returnType)) {
                                    fieldManager.setFieldValue(instance, property.getName(), Long.valueOf(value));
                                }
                                break;
                            default:
                                fieldManager.setFieldValue(instance, property.getName(), value);
                                break;
                            case EMAIL:
                                fieldManager.setFieldValue(instance, property.getName(), value);
                                break;
                            case FOREIGN_KEY:
                                {
                                    Serializable foreignInstance;
                                    if (StringUtils.isEmpty(value)) {
                                        foreignInstance = null;
                                    } else {
                                        if (SupportedFieldType.INTEGER.toString().equals(mergedProperties.get(property.getName()).getSecondaryType().toString())) {
                                            foreignInstance = persistenceManager.getDynamicEntityDao().retrieve(Class.forName(mergedProperties.get(property.getName()).getForeignKeyClass()), Long.valueOf(value));
                                        } else {
                                            foreignInstance = persistenceManager.getDynamicEntityDao().retrieve(Class.forName(mergedProperties.get(property.getName()).getForeignKeyClass()), value);
                                        }
                                    }
                                    if (Collection.class.isAssignableFrom(returnType)) {
                                        Collection collection = (Collection) fieldManager.getFieldValue(instance, property.getName());
                                        if (!collection.contains(foreignInstance)) {
                                            collection.add(foreignInstance);
                                        }
                                    } else if (Map.class.isAssignableFrom(returnType)) {
                                        throw new RuntimeException("Map structures are not supported for foreign key fields.");
                                    } else {
                                        fieldManager.setFieldValue(instance, property.getName(), foreignInstance);
                                    }
                                    break;
                                }
                            case ADDITIONAL_FOREIGN_KEY:
                                {
                                    Serializable foreignInstance;
                                    if (StringUtils.isEmpty(value)) {
                                        foreignInstance = null;
                                    } else {
                                        if (SupportedFieldType.INTEGER.toString().equals(mergedProperties.get(property.getName()).getSecondaryType().toString())) {
                                            foreignInstance = persistenceManager.getDynamicEntityDao().retrieve(Class.forName(mergedProperties.get(property.getName()).getForeignKeyClass()), Long.valueOf(value));
                                        } else {
                                            foreignInstance = persistenceManager.getDynamicEntityDao().retrieve(Class.forName(mergedProperties.get(property.getName()).getForeignKeyClass()), value);
                                        }
                                    }
                                    if (Collection.class.isAssignableFrom(returnType)) {
                                        Collection collection = (Collection) fieldManager.getFieldValue(instance, property.getName());
                                        if (!collection.contains(foreignInstance)) {
                                            collection.add(foreignInstance);
                                        }
                                    } else if (Map.class.isAssignableFrom(returnType)) {
                                        throw new RuntimeException("Map structures are not supported for foreign key fields.");
                                    } else {
                                        fieldManager.setFieldValue(instance, property.getName(), foreignInstance);
                                    }
                                    break;
                                }
                            case ID:
                                if (setId) {
                                    switch(mergedProperties.get(property.getName()).getSecondaryType()) {
                                        case INTEGER:
                                            fieldManager.setFieldValue(instance, property.getName(), Long.valueOf(value));
                                            break;
                                        case STRING:
                                            fieldManager.setFieldValue(instance, property.getName(), value);
                                            break;
                                    }
                                }
                                break;
                        }
                    } else {
                        if (fieldManager.getFieldValue(instance, property.getName()) != null && (mergedProperties.get(property.getName()).getFieldType() != SupportedFieldType.ID || setId)) {
                            fieldManager.setFieldValue(instance, property.getName(), null);
                        }
                    }
                }
            }
        }
        fieldManager.persistMiddleEntities();
        return instance;
    }

    public Entity getRecord(Map<String, FieldMetadata> primaryMergedProperties, Serializable record, Map<String, FieldMetadata> alternateMergedProperties, String pathToTargetObject) throws ParserConfigurationException, DOMException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, TransformerFactoryConfigurationError, IllegalArgumentException, TransformerException, SecurityException, ClassNotFoundException {
        List<Serializable> records = new ArrayList<Serializable>(1);
        records.add(record);
        Entity[] productEntities = getRecords(primaryMergedProperties, records, alternateMergedProperties, pathToTargetObject);
        return productEntities[0];
    }

    public Entity getRecord(Class<?> ceilingEntityClass, PersistencePerspective persistencePerspective, Serializable record) throws SecurityException, IllegalArgumentException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, DOMException, ParserConfigurationException, TransformerFactoryConfigurationError, TransformerException, NoSuchFieldException {
        Map<String, FieldMetadata> mergedProperties = getSimpleMergedProperties(ceilingEntityClass.getName(), persistencePerspective);
        return getRecord(mergedProperties, record, null, null);
    }

    public Entity[] getRecords(Class<?> ceilingEntityClass, PersistencePerspective persistencePerspective, List<Serializable> records) throws SecurityException, IllegalArgumentException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, DOMException, ParserConfigurationException, TransformerFactoryConfigurationError, TransformerException, NoSuchFieldException {
        Map<String, FieldMetadata> mergedProperties = getSimpleMergedProperties(ceilingEntityClass.getName(), persistencePerspective);
        return getRecords(mergedProperties, records, null, null);
    }

    public Map<String, FieldMetadata> getSimpleMergedProperties(String entityName, PersistencePerspective persistencePerspective) throws ClassNotFoundException, SecurityException, IllegalArgumentException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, NoSuchFieldException {
        return persistenceManager.getDynamicEntityDao().getSimpleMergedProperties(entityName, persistencePerspective);
    }

    public Entity[] getRecords(Map<String, FieldMetadata> primaryMergedProperties, List<Serializable> records) throws ParserConfigurationException, DOMException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, TransformerFactoryConfigurationError, IllegalArgumentException, TransformerException, SecurityException, ClassNotFoundException {
        return getRecords(primaryMergedProperties, records, null, null);
    }

    public Entity[] getRecords(Map<String, FieldMetadata> primaryMergedProperties, List<Serializable> records, Map<String, FieldMetadata> alternateMergedProperties, String pathToTargetObject) throws ParserConfigurationException, DOMException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, TransformerFactoryConfigurationError, IllegalArgumentException, TransformerException, SecurityException, ClassNotFoundException {
        Entity[] entities = new Entity[records.size()];
        int j = 0;
        for (Serializable recordEntity : records) {
            Serializable entity;
            if (pathToTargetObject != null) {
                entity = (Serializable) getFieldManager().getFieldValue(recordEntity, pathToTargetObject);
            } else {
                entity = recordEntity;
            }
            Entity entityItem = new Entity();
            entityItem.setType(new String[] { entity.getClass().getName() });
            entities[j] = entityItem;
            List<Property> props = new ArrayList<Property>(primaryMergedProperties.size());
            extractPropertiesFromPersistentEntity(primaryMergedProperties, entity, props);
            if (alternateMergedProperties != null) {
                extractPropertiesFromPersistentEntity(alternateMergedProperties, recordEntity, props);
            }
            Property[] properties = new Property[props.size()];
            properties = props.toArray(properties);
            entityItem.setProperties(properties);
            j++;
        }
        return entities;
    }

    protected void extractPropertiesFromPersistentEntity(Map<String, FieldMetadata> mergedProperties, Serializable entity, List<Property> props) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, SecurityException, IllegalArgumentException, ClassNotFoundException {
        FieldManager fieldManager = getFieldManager();
        for (Map.Entry<String, FieldMetadata> entry : mergedProperties.entrySet()) {
            String property = entry.getKey();
            FieldMetadata metadata = mergedProperties.get(property);
            if (Class.forName(metadata.getInheritedFromType()).isAssignableFrom(entity.getClass())) {
                boolean proceed = true;
                if (property.contains(".")) {
                    StringTokenizer tokens = new StringTokenizer(property, ".");
                    Object testObject = entity;
                    while (tokens.hasMoreTokens()) {
                        String token = tokens.nextToken();
                        if (tokens.hasMoreTokens()) {
                            testObject = fieldManager.getFieldValue(testObject, token);
                            if (testObject == null) {
                                Property propertyItem = new Property();
                                propertyItem.setName(property);
                                if (props.contains(propertyItem)) {
                                    proceed = false;
                                    break;
                                }
                                propertyItem.setValue(null);
                                props.add(propertyItem);
                                proceed = false;
                                break;
                            }
                        }
                    }
                }
                if (!proceed) {
                    continue;
                }
                boolean isFieldAccessible = true;
                Object value = null;
                try {
                    value = fieldManager.getFieldValue(entity, property);
                } catch (Exception e1) {
                    isFieldAccessible = false;
                }
                String strVal;
                checkField: {
                    if (isFieldAccessible) {
                        Property propertyItem = new Property();
                        propertyItem.setName(property);
                        if (props.contains(propertyItem)) {
                            continue;
                        }
                        props.add(propertyItem);
                        String displayVal = null;
                        if (value != null) {
                            if (metadata.getCollection()) {
                                propertyItem.getMetadata().setFieldType(metadata.getFieldType());
                                strVal = null;
                            } else if (Date.class.isAssignableFrom(value.getClass())) {
                                strVal = dateFormat.format((Date) value);
                            } else if (Timestamp.class.isAssignableFrom(value.getClass())) {
                                strVal = dateFormat.format(new Date(((Timestamp) value).getTime()));
                            } else if (Calendar.class.isAssignableFrom(value.getClass())) {
                                strVal = dateFormat.format(((Calendar) value).getTime());
                            } else if (Double.class.isAssignableFrom(value.getClass())) {
                                strVal = decimalFormat.format(value);
                            } else if (BigDecimal.class.isAssignableFrom(value.getClass())) {
                                strVal = decimalFormat.format(((BigDecimal) value).doubleValue());
                            } else if (metadata.getForeignKeyClass() != null) {
                                strVal = fieldManager.getFieldValue(value, metadata.getForeignKeyProperty()).toString();
                                Object temp = fieldManager.getFieldValue(value, metadata.getForeignKeyDisplayValueProperty());
                                if (temp != null) {
                                    displayVal = temp.toString();
                                }
                            } else {
                                strVal = value.toString();
                            }
                            propertyItem.setValue(strVal);
                            propertyItem.setDisplayValue(displayVal);
                            break checkField;
                        }
                    }
                    try {
                        Method method;
                        try {
                            String temp = "get" + property.substring(0, 1).toUpperCase() + property.substring(1, property.length());
                            method = entity.getClass().getMethod(temp, new Class[] {});
                        } catch (NoSuchMethodException e) {
                            method = entity.getClass().getMethod(property, new Class[] {});
                        }
                        value = method.invoke(entity, new String[] {});
                        Property propertyItem = new Property();
                        propertyItem.setName(property);
                        if (props.contains(propertyItem)) {
                            continue;
                        }
                        props.add(propertyItem);
                        if (value == null) {
                            strVal = null;
                        } else {
                            if (Date.class.isAssignableFrom(value.getClass())) {
                                strVal = dateFormat.format((Date) value);
                            } else if (Timestamp.class.isAssignableFrom(value.getClass())) {
                                strVal = dateFormat.format(new Date(((Timestamp) value).getTime()));
                            } else if (Calendar.class.isAssignableFrom(value.getClass())) {
                                strVal = dateFormat.format(((Calendar) value).getTime());
                            } else if (Double.class.isAssignableFrom(value.getClass())) {
                                strVal = decimalFormat.format(value);
                            } else if (BigDecimal.class.isAssignableFrom(value.getClass())) {
                                strVal = decimalFormat.format(((BigDecimal) value).doubleValue());
                            } else {
                                strVal = value.toString();
                            }
                        }
                        propertyItem.setValue(strVal);
                    } catch (NoSuchMethodException e) {
                        LOG.debug("Unable to find a specified property in the entity: " + property);
                    }
                }
            }
        }
    }

    protected Entity update(PersistencePackage persistencePackage, Object primaryKey) throws ServiceException {
        try {
            Entity entity = persistencePackage.getEntity();
            PersistencePerspective persistencePerspective = persistencePackage.getPersistencePerspective();
            Class<?>[] entities = persistenceManager.getPolymorphicEntities(persistencePackage.getCeilingEntityFullyQualifiedClassname());
            Map<String, FieldMetadata> mergedProperties = persistenceManager.getDynamicEntityDao().getMergedProperties(persistencePackage.getCeilingEntityFullyQualifiedClassname(), entities, (ForeignKey) persistencePerspective.getPersistencePerspectiveItems().get(PersistencePerspectiveItemType.FOREIGNKEY), persistencePerspective.getAdditionalNonPersistentProperties(), persistencePerspective.getAdditionalForeignKeys(), MergedPropertyType.PRIMARY, persistencePerspective.getPopulateToOneFields(), persistencePerspective.getIncludeFields(), persistencePerspective.getExcludeFields(), persistencePerspective.getConfigurationKey(), "");
            if (primaryKey == null) {
                primaryKey = getPrimaryKey(entity, mergedProperties);
            }
            Serializable instance = persistenceManager.getDynamicEntityDao().retrieve(Class.forName(entity.getType()[0]), primaryKey);
            SandBoxContext context = SandBoxContext.getSandBoxContext();
            if (context != null && context.getSandBoxMode() != SandBoxMode.IMMEDIATE_COMMIT) {
                instance = (Serializable) SerializationUtils.clone(instance);
            }
            instance = createPopulatedInstance(instance, entity, mergedProperties, false);
            instance = persistenceManager.getDynamicEntityDao().merge(instance);
            List<Serializable> entityList = new ArrayList<Serializable>(1);
            entityList.add(instance);
            return getRecords(mergedProperties, entityList, null, null)[0];
        } catch (Exception e) {
            LOG.error("Problem editing entity", e);
            throw new ServiceException("Problem updating entity : " + e.getMessage(), e);
        }
    }

    public Object getPrimaryKey(Entity entity, Map<String, FieldMetadata> mergedProperties) throws RuntimeException {
        Object primaryKey = null;
        String idProperty = null;
        for (String property : mergedProperties.keySet()) {
            if (mergedProperties.get(property).getFieldType() == SupportedFieldType.ID && !property.contains(".")) {
                idProperty = property;
                break;
            }
        }
        if (idProperty == null) {
            throw new RuntimeException("Could not find a primary key property in the passed entity with type: " + entity.getType()[0]);
        }
        for (Property property : entity.getProperties()) {
            if (property.getName().equals(idProperty)) {
                switch(property.getMetadata().getSecondaryType()) {
                    case INTEGER:
                        primaryKey = Long.valueOf(property.getValue());
                        break;
                    case STRING:
                        primaryKey = property.getValue();
                        break;
                }
                break;
            }
        }
        if (primaryKey == null) {
            throw new RuntimeException("Could not find the primary key property (" + idProperty + ") in the passed entity with type: " + entity.getType()[0]);
        }
        return primaryKey;
    }

    public BaseCtoConverter getCtoConverter(PersistencePerspective persistencePerspective, CriteriaTransferObject cto, String ceilingEntityFullyQualifiedClassname, Map<String, FieldMetadata> mergedProperties) throws ClassNotFoundException {
        BaseCtoConverter ctoConverter = new BaseCtoConverter();
        for (Map.Entry<String, FieldMetadata> entry : mergedProperties.entrySet()) {
            String propertyName = entry.getKey();
            AssociationPath associationPath;
            int dotIndex = propertyName.lastIndexOf('.');
            StringBuilder property;
            Class clazz = Class.forName(mergedProperties.get(propertyName).getInheritedFromType());
            Field field = getFieldManager().getField(clazz, propertyName);
            if (dotIndex >= 0) {
                property = new StringBuilder(propertyName.substring(dotIndex + 1, propertyName.length()));
                String prefix = propertyName.substring(0, dotIndex);
                StringTokenizer tokens = new StringTokenizer(prefix, ".");
                List<AssociationPathElement> elementList = new ArrayList<AssociationPathElement>(20);
                StringBuilder sb = new StringBuilder(150);
                StringBuilder pathBuilder = new StringBuilder(150);
                while (tokens.hasMoreElements()) {
                    String token = tokens.nextToken();
                    sb.append(token);
                    pathBuilder.append(token);
                    field = getFieldManager().getField(clazz, pathBuilder.toString());
                    Embedded embedded = field.getAnnotation(Embedded.class);
                    if (embedded != null) {
                        sb.append('.');
                    } else {
                        elementList.add(new AssociationPathElement(sb.toString()));
                        sb = new StringBuilder(150);
                    }
                    pathBuilder.append('.');
                }
                if (!elementList.isEmpty()) {
                    AssociationPathElement[] elements = elementList.toArray(new AssociationPathElement[elementList.size()]);
                    associationPath = new AssociationPath(elements);
                } else {
                    property = property.insert(0, sb.toString());
                    associationPath = AssociationPath.ROOT;
                }
            } else {
                property = new StringBuilder(propertyName);
                associationPath = AssociationPath.ROOT;
            }
            String convertedProperty = property.toString();
            switch(mergedProperties.get(propertyName).getFieldType()) {
                case BOOLEAN:
                    Class<?> targetType = null;
                    if (field != null) {
                        targetType = field.getType();
                    }
                    if (targetType == null || targetType.equals(Boolean.class)) {
                        ctoConverter.addBooleanMapping(ceilingEntityFullyQualifiedClassname, propertyName, associationPath, convertedProperty);
                    } else {
                        ctoConverter.addCharacterMapping(ceilingEntityFullyQualifiedClassname, propertyName, associationPath, convertedProperty);
                    }
                    break;
                case DATE:
                    ctoConverter.addDateMapping(ceilingEntityFullyQualifiedClassname, propertyName, associationPath, convertedProperty);
                    break;
                case DECIMAL:
                    ctoConverter.addDecimalMapping(ceilingEntityFullyQualifiedClassname, propertyName, associationPath, convertedProperty);
                    break;
                case MONEY:
                    ctoConverter.addDecimalMapping(ceilingEntityFullyQualifiedClassname, propertyName, associationPath, convertedProperty);
                    break;
                case INTEGER:
                    ctoConverter.addLongMapping(ceilingEntityFullyQualifiedClassname, propertyName, associationPath, convertedProperty);
                    break;
                default:
                    ctoConverter.addStringLikeMapping(ceilingEntityFullyQualifiedClassname, propertyName, associationPath, convertedProperty);
                    break;
                case EMAIL:
                    ctoConverter.addStringLikeMapping(ceilingEntityFullyQualifiedClassname, propertyName, associationPath, convertedProperty);
                    break;
                case FOREIGN_KEY:
                    if (cto.get(propertyName).getFilterValues().length > 0) {
                        ForeignKey foreignKey = (ForeignKey) persistencePerspective.getPersistencePerspectiveItems().get(PersistencePerspectiveItemType.FOREIGNKEY);
                        if (mergedProperties.get(propertyName).getCollection()) {
                            if (ForeignKeyRestrictionType.COLLECTION_SIZE_EQ.toString().equals(foreignKey.getRestrictionType().toString())) {
                                ctoConverter.addCollectionSizeEqMapping(ceilingEntityFullyQualifiedClassname, propertyName, AssociationPath.ROOT, propertyName);
                            } else {
                                AssociationPath foreignCategory = new AssociationPath(new AssociationPathElement(propertyName));
                                ctoConverter.addLongMapping(ceilingEntityFullyQualifiedClassname, propertyName, foreignCategory, mergedProperties.get(propertyName).getForeignKeyProperty());
                            }
                        } else if (cto.get(propertyName).getFilterValues()[0] == null || "null".equals(cto.get(propertyName).getFilterValues()[0])) {
                            ctoConverter.addNullMapping(ceilingEntityFullyQualifiedClassname, propertyName, associationPath, propertyName);
                        } else {
                            AssociationPath foreignCategory = new AssociationPath(new AssociationPathElement(propertyName));
                            ctoConverter.addLongEQMapping(ceilingEntityFullyQualifiedClassname, propertyName, foreignCategory, mergedProperties.get(propertyName).getForeignKeyProperty());
                        }
                    }
                    break;
                case ADDITIONAL_FOREIGN_KEY:
                    if (cto.get(propertyName).getFilterValues().length > 0) {
                        int additionalForeignKeyIndexPosition = Arrays.binarySearch(persistencePerspective.getAdditionalForeignKeys(), new ForeignKey(propertyName, null, null), new Comparator<ForeignKey>() {

                            public int compare(ForeignKey o1, ForeignKey o2) {
                                return o1.getManyToField().compareTo(o2.getManyToField());
                            }
                        });
                        ForeignKey foreignKey = persistencePerspective.getAdditionalForeignKeys()[additionalForeignKeyIndexPosition];
                        if (mergedProperties.get(propertyName).getCollection()) {
                            if (ForeignKeyRestrictionType.COLLECTION_SIZE_EQ.toString().equals(foreignKey.getRestrictionType().toString())) {
                                ctoConverter.addCollectionSizeEqMapping(ceilingEntityFullyQualifiedClassname, propertyName, AssociationPath.ROOT, propertyName);
                            } else {
                                AssociationPath foreignCategory = new AssociationPath(new AssociationPathElement(propertyName));
                                ctoConverter.addLongMapping(ceilingEntityFullyQualifiedClassname, propertyName, foreignCategory, mergedProperties.get(propertyName).getForeignKeyProperty());
                            }
                        } else if (cto.get(propertyName).getFilterValues()[0] == null || "null".equals(cto.get(propertyName).getFilterValues()[0])) {
                            ctoConverter.addNullMapping(ceilingEntityFullyQualifiedClassname, propertyName, associationPath, propertyName);
                        } else {
                            AssociationPath foreignCategory = new AssociationPath(new AssociationPathElement(propertyName));
                            ctoConverter.addLongEQMapping(ceilingEntityFullyQualifiedClassname, propertyName, foreignCategory, mergedProperties.get(propertyName).getForeignKeyProperty());
                        }
                    }
                    break;
                case ID:
                    switch(mergedProperties.get(propertyName).getSecondaryType()) {
                        case INTEGER:
                            ctoConverter.addLongEQMapping(ceilingEntityFullyQualifiedClassname, propertyName, associationPath, convertedProperty);
                            break;
                        case STRING:
                            ctoConverter.addStringLikeMapping(ceilingEntityFullyQualifiedClassname, propertyName, associationPath, convertedProperty);
                            break;
                    }
                    break;
            }
        }
        return ctoConverter;
    }

    public int getTotalRecords(String ceilingEntityFullyQualifiedClassname, CriteriaTransferObject cto, BaseCtoConverter ctoConverter) throws ClassNotFoundException {
        PersistentEntityCriteria countCriteria = ctoConverter.convert(new CriteriaTransferObjectCountWrapper(cto).wrap(), ceilingEntityFullyQualifiedClassname);
        return persistenceManager.getDynamicEntityDao().count(countCriteria, Class.forName(ceilingEntityFullyQualifiedClassname));
    }

    public void extractProperties(Map<MergedPropertyType, Map<String, FieldMetadata>> mergedProperties, List<Property> properties) throws NumberFormatException {
        extractPropertiesFromMetadata(mergedProperties.get(MergedPropertyType.PRIMARY), properties, false);
    }

    protected void extractPropertiesFromMetadata(Map<String, FieldMetadata> mergedProperties, List<Property> properties, Boolean isHiddenOverride) throws NumberFormatException {
        for (Map.Entry<String, FieldMetadata> entry : mergedProperties.entrySet()) {
            String property = entry.getKey();
            Property prop = new Property();
            FieldMetadata metadata = mergedProperties.get(property);
            prop.setName(property);
            if (properties.contains(prop)) {
                continue;
            }
            properties.add(prop);
            prop.setMetadata(metadata);
            if (isHiddenOverride) {
                prop.getMetadata().getPresentationAttributes().setVisibility(VisibilityEnum.HIDDEN_ALL);
            }
        }
    }

    public void updateMergedProperties(PersistencePackage persistencePackage, Map<MergedPropertyType, Map<String, FieldMetadata>> allMergedProperties) throws ServiceException {
        String ceilingEntityFullyQualifiedClassname = persistencePackage.getCeilingEntityFullyQualifiedClassname();
        try {
            PersistencePerspective persistencePerspective = persistencePackage.getPersistencePerspective();
            Class<?>[] entities = persistenceManager.getPolymorphicEntities(ceilingEntityFullyQualifiedClassname);
            Map<String, FieldMetadata> mergedProperties = persistenceManager.getDynamicEntityDao().getMergedProperties(ceilingEntityFullyQualifiedClassname, entities, (ForeignKey) persistencePerspective.getPersistencePerspectiveItems().get(PersistencePerspectiveItemType.FOREIGNKEY), persistencePerspective.getAdditionalNonPersistentProperties(), persistencePerspective.getAdditionalForeignKeys(), MergedPropertyType.PRIMARY, persistencePerspective.getPopulateToOneFields(), persistencePerspective.getIncludeFields(), persistencePerspective.getExcludeFields(), persistencePerspective.getConfigurationKey(), "");
            allMergedProperties.put(MergedPropertyType.PRIMARY, mergedProperties);
        } catch (Exception e) {
            LOG.error("Problem fetching results for " + ceilingEntityFullyQualifiedClassname, e);
            throw new ServiceException("Unable to fetch results for " + ceilingEntityFullyQualifiedClassname, e);
        }
    }

    public Entity update(PersistencePackage persistencePackage) throws ServiceException {
        return update(persistencePackage, null);
    }

    public Entity add(PersistencePackage persistencePackage) throws ServiceException {
        try {
            Entity entity = persistencePackage.getEntity();
            PersistencePerspective persistencePerspective = persistencePackage.getPersistencePerspective();
            Class<?>[] entities = persistenceManager.getPolymorphicEntities(persistencePackage.getCeilingEntityFullyQualifiedClassname());
            Map<String, FieldMetadata> mergedProperties = persistenceManager.getDynamicEntityDao().getMergedProperties(persistencePackage.getCeilingEntityFullyQualifiedClassname(), entities, (ForeignKey) persistencePerspective.getPersistencePerspectiveItems().get(PersistencePerspectiveItemType.FOREIGNKEY), persistencePerspective.getAdditionalNonPersistentProperties(), persistencePerspective.getAdditionalForeignKeys(), MergedPropertyType.PRIMARY, persistencePerspective.getPopulateToOneFields(), persistencePerspective.getIncludeFields(), persistencePerspective.getExcludeFields(), persistencePerspective.getConfigurationKey(), "");
            String idProperty = null;
            for (String property : mergedProperties.keySet()) {
                if (mergedProperties.get(property).getFieldType() == SupportedFieldType.ID) {
                    idProperty = property;
                    break;
                }
            }
            if (idProperty == null) {
                throw new RuntimeException("Could not find a primary key property in the passed entity with type: " + entity.getType()[0]);
            }
            Object primaryKey = null;
            try {
                primaryKey = getPrimaryKey(entity, mergedProperties);
            } catch (Exception e) {
            }
            if (primaryKey == null) {
                Serializable instance = (Serializable) Class.forName(entity.getType()[0]).newInstance();
                instance = createPopulatedInstance(instance, entity, mergedProperties, false);
                instance = persistenceManager.getDynamicEntityDao().persist(instance);
                List<Serializable> entityList = new ArrayList<Serializable>(1);
                entityList.add(instance);
                return getRecords(mergedProperties, entityList, null, null)[0];
            } else {
                return update(persistencePackage, primaryKey);
            }
        } catch (ServiceException e) {
            LOG.error("Problem adding new entity", e);
            throw e;
        } catch (Exception e) {
            LOG.error("Problem adding new entity", e);
            throw new ServiceException("Problem adding new entity : " + e.getMessage(), e);
        }
    }

    public void remove(PersistencePackage persistencePackage) throws ServiceException {
        try {
            Entity entity = persistencePackage.getEntity();
            PersistencePerspective persistencePerspective = persistencePackage.getPersistencePerspective();
            Class<?>[] entities = persistenceManager.getPolymorphicEntities(persistencePackage.getCeilingEntityFullyQualifiedClassname());
            Map<String, FieldMetadata> mergedProperties = persistenceManager.getDynamicEntityDao().getMergedProperties(persistencePackage.getCeilingEntityFullyQualifiedClassname(), entities, (ForeignKey) persistencePerspective.getPersistencePerspectiveItems().get(PersistencePerspectiveItemType.FOREIGNKEY), persistencePerspective.getAdditionalNonPersistentProperties(), persistencePerspective.getAdditionalForeignKeys(), MergedPropertyType.PRIMARY, persistencePerspective.getPopulateToOneFields(), persistencePerspective.getIncludeFields(), persistencePerspective.getExcludeFields(), persistencePerspective.getConfigurationKey(), "");
            Object primaryKey = getPrimaryKey(entity, mergedProperties);
            Serializable instance = persistenceManager.getDynamicEntityDao().retrieve(Class.forName(entity.getType()[0]), primaryKey);
            switch(persistencePerspective.getOperationTypes().getRemoveType()) {
                case FOREIGNKEY:
                    for (Property property : entity.getProperties()) {
                        String originalPropertyName = property.getName();
                        FieldManager fieldManager = getFieldManager();
                        if (fieldManager.getField(instance.getClass(), property.getName()) == null) {
                            LOG.debug("Unable to find a bean property for the reported property: " + originalPropertyName + ". Ignoring property.");
                            continue;
                        }
                        if (SupportedFieldType.FOREIGN_KEY == mergedProperties.get(originalPropertyName).getFieldType()) {
                            String value = property.getValue();
                            ForeignKey foreignKey = (ForeignKey) persistencePerspective.getPersistencePerspectiveItems().get(PersistencePerspectiveItemType.FOREIGNKEY);
                            Serializable foreignInstance = persistenceManager.getDynamicEntityDao().retrieve(Class.forName(foreignKey.getForeignKeyClass()), Long.valueOf(value));
                            Collection collection = (Collection) fieldManager.getFieldValue(instance, property.getName());
                            collection.remove(foreignInstance);
                            break;
                        }
                    }
                    break;
                case ENTITY:
                    persistenceManager.getDynamicEntityDao().remove(instance);
                    break;
            }
        } catch (Exception e) {
            LOG.error("Problem removing entity", e);
            throw new ServiceException("Problem removing entity : " + e.getMessage(), e);
        }
    }

    public DynamicResultSet fetch(PersistencePackage persistencePackage, CriteriaTransferObject cto) throws ServiceException {
        Entity[] payload;
        int totalRecords;
        String ceilingEntityFullyQualifiedClassname = persistencePackage.getCeilingEntityFullyQualifiedClassname();
        PersistencePerspective persistencePerspective = persistencePackage.getPersistencePerspective();
        try {
            Class<?>[] entities = persistenceManager.getDynamicEntityDao().getAllPolymorphicEntitiesFromCeiling(Class.forName(ceilingEntityFullyQualifiedClassname));
            Map<String, FieldMetadata> mergedProperties = persistenceManager.getDynamicEntityDao().getMergedProperties(ceilingEntityFullyQualifiedClassname, entities, (ForeignKey) persistencePerspective.getPersistencePerspectiveItems().get(PersistencePerspectiveItemType.FOREIGNKEY), persistencePerspective.getAdditionalNonPersistentProperties(), persistencePerspective.getAdditionalForeignKeys(), MergedPropertyType.PRIMARY, persistencePerspective.getPopulateToOneFields(), persistencePerspective.getIncludeFields(), persistencePerspective.getExcludeFields(), persistencePerspective.getConfigurationKey(), "");
            BaseCtoConverter ctoConverter = getCtoConverter(persistencePerspective, cto, ceilingEntityFullyQualifiedClassname, mergedProperties);
            PersistentEntityCriteria queryCriteria = ctoConverter.convert(cto, ceilingEntityFullyQualifiedClassname);
            List<Serializable> records = persistenceManager.getDynamicEntityDao().query(queryCriteria, Class.forName(ceilingEntityFullyQualifiedClassname));
            payload = getRecords(mergedProperties, records, null, null);
            totalRecords = getTotalRecords(ceilingEntityFullyQualifiedClassname, cto, ctoConverter);
        } catch (Exception e) {
            LOG.error("Problem fetching results for " + ceilingEntityFullyQualifiedClassname, e);
            throw new ServiceException("Unable to fetch results for " + ceilingEntityFullyQualifiedClassname, e);
        }
        return new DynamicResultSet(null, payload, totalRecords);
    }

    public void setPersistenceManager(PersistenceManager persistenceManager) {
        this.persistenceManager = persistenceManager;
    }
}
