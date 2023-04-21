package org.datascooter.bundle;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Id;
import javax.persistence.MapKey;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import org.datascooter.annotations.AContainerAttribute;
import org.datascooter.bundle.attribute.ContainerAttribute;
import org.datascooter.bundle.attribute.DBIndex;
import org.datascooter.bundle.attribute.FieldContainerAttribute;
import org.datascooter.bundle.attribute.PersistAttribute;
import org.datascooter.bundle.attribute.PersistFieldAttribute;
import org.datascooter.bundle.attribute.PersistMethodAttribute;
import org.datascooter.exception.EntityNotMappedException;
import org.datascooter.generator.IntIncrementIdGenerator;
import org.datascooter.generator.LongIncrementIdGenerator;
import org.datascooter.generator.UUIDGenerator;
import org.datascooter.impl.Condition;
import org.datascooter.inface.IBundleProvider;
import org.datascooter.utils.DBType;
import org.datascooter.utils.LangUtils;
import org.datascooter.utils.SnipUtils;
import org.datascooter.utils.TypeUtils;

public class FieldMappingProvider implements IBundleProvider {

    private String parentBundle;

    @Override
    public void explore() throws Exception {
    }

    public void exploreFields(List<Field> fieldList, Class<?> clazz) {
        fieldList.addAll(Arrays.asList(clazz.getDeclaredFields()));
        Iterator<Field> iterator = fieldList.iterator();
        while (iterator.hasNext()) {
            Field next = iterator.next();
            if (Modifier.isTransient(next.getModifiers()) || next.getAnnotation(Transient.class) != null) {
                iterator.remove();
            }
        }
        if (clazz.getSuperclass() != null) {
            exploreFields(fieldList, clazz.getSuperclass());
        }
    }

    @Override
    public EntityBundle getItem(String dname) {
        return null;
    }

    @Override
    public EntityBundle getBundle(Object obj) throws EntityNotMappedException, ClassNotFoundException, SecurityException, NoSuchMethodException {
        EntityBundle entityBundle = null;
        Class<? extends Object> clazz = LangUtils.getClazz(obj);
        if (TypeUtils.isPrimitive(clazz)) {
            throw new EntityNotMappedException("***Error - native class mapping:  " + clazz.getName());
        }
        entityBundle = preprocessBundle(clazz);
        List<Field> fieldList = new ArrayList<Field>();
        exploreFields(fieldList, clazz);
        for (Field field : fieldList) {
            Transient pass = field.getAnnotation(Transient.class);
            if (pass != null || Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            field.setAccessible(true);
            Class<?> type = field.getType();
            if (TypeUtils.isPrimitive(type)) {
                field.setAccessible(true);
                entityBundle.addPersistAttribute(new PersistFieldAttribute(field.getName(), LangUtils.underlineName(field.getName()), TypeUtils.getTypeByValue(type), TypeUtils.getDefaultScale(type), TypeUtils.getDefaultPrecision(type), true, true, true));
            } else {
                type = LangUtils.resolveClass(field, field.getType());
                if (Collection.class.isAssignableFrom(type)) {
                    continue;
                } else if (Map.class.isAssignableFrom(type)) {
                    continue;
                } else if (type.isArray()) {
                    continue;
                }
                Embeddable embeddable = type.getAnnotation(Embeddable.class);
                entityBundle.addContainerAttribute(new FieldContainerAttribute(field.getName(), type.getName(), obtainMapKey(field), null, embeddable != null, false));
            }
        }
        customizeBundle(entityBundle, fieldList, clazz);
        if (parentBundle == null) {
            produceId(entityBundle, fieldList, clazz);
        }
        return entityBundle;
    }

    public EntityBundle getBundle(Object obj, String parentBundle) throws EntityNotMappedException, SecurityException, ClassNotFoundException, NoSuchMethodException {
        this.parentBundle = parentBundle;
        return getBundle(obj);
    }

    private String obtainMapKey(AccessibleObject object) {
        AContainerAttribute annotation = object.getAnnotation(AContainerAttribute.class);
        if (annotation != null) {
            return annotation.mapKeyAttribute();
        }
        MapKey mapKeyAnn = object.getAnnotation(MapKey.class);
        if (mapKeyAnn != null) {
            return mapKeyAnn.name();
        }
        return "id";
    }

    private void customizeBundle(EntityBundle entityBundle, List<Field> fieldList, Class<? extends Object> clazz) throws EntityNotMappedException, SecurityException, NoSuchMethodException, ClassNotFoundException {
        customizeFields(entityBundle, fieldList);
        customizeMethods(entityBundle, clazz);
        Table table = clazz.getAnnotation(Table.class);
        if (table != null) {
            entityBundle.table = table.name();
            if (table != null && table.uniqueConstraints() != null && table.uniqueConstraints().length > 0) {
                int count = 1;
                for (UniqueConstraint constraint : table.uniqueConstraints()) {
                    count++;
                    DBIndex dbIndex = new DBIndex(count + "_" + LangUtils.getRandomIndexString(4), SnipUtils.UNIQUE);
                    entityBundle.addIndex(dbIndex);
                    for (String column : constraint.columnNames()) {
                        PersistAttribute attribute = (PersistAttribute) entityBundle.getAttributeByColumn(column);
                        if (attribute != null) {
                            dbIndex.addAttribute(attribute);
                        }
                    }
                }
            }
        }
        MappedSuperclass mappedSuperclass = clazz.getAnnotation(MappedSuperclass.class);
        if (mappedSuperclass != null) {
            entityBundle.isUncheckable = true;
        }
        if (parentBundle == null) {
            postProcessBundle(clazz, entityBundle);
        }
    }

    private void customizeMethods(EntityBundle entityBundle, Class<? extends Object> clazz) throws EntityNotMappedException, NoSuchMethodException, SecurityException, ClassNotFoundException {
        for (Method method : clazz.getMethods()) {
            Column column = method.getAnnotation(Column.class);
            if (column != null) {
                PersistAttribute attribute = entityBundle.getAttribute(LangUtils.prepareName(method.getName()));
                if (attribute != null) {
                    entityBundle.customizeAttribute(column, attribute);
                }
            }
            if (Map.class.isAssignableFrom(method.getReturnType())) {
                ContainerAttribute attribute = entityBundle.getContainerAttribute(LangUtils.prepareName(method.getName()));
                if (attribute != null) {
                    attribute.setMapKeyAttribute(obtainMapKey(method));
                }
            }
            Embedded embedded = method.getAnnotation(Embedded.class);
            if (embedded != null) {
                Class<?> trueClass = LangUtils.resolveClass(method, method.getReturnType());
                Embeddable embeddable = trueClass.getAnnotation(Embeddable.class);
                if (embeddable != null) {
                    EntityBundle bundle = new FieldMappingProvider().getBundle(trueClass, entityBundle.entity);
                    entityBundle.addEmbeded(bundle);
                    DSMapper.addBundleInt(bundle);
                }
            }
        }
    }

    private void customizeFields(EntityBundle entityBundle, List<Field> fieldList) {
        for (Field field : fieldList) {
            Column column = field.getAnnotation(Column.class);
            if (column != null) {
                PersistAttribute attribute = entityBundle.getAttribute(field.getName());
                if (attribute != null) {
                    entityBundle.customizeAttribute(column, attribute);
                }
            }
            if (Map.class.isAssignableFrom(field.getType())) {
                ContainerAttribute attribute = entityBundle.getContainerAttribute(field.getName());
                if (attribute != null) {
                    attribute.setMapKeyAttribute(obtainMapKey(field));
                }
            }
        }
    }

    private void postProcessBundle(Class<? extends Object> clazz, EntityBundle entityBundle) {
        DiscriminatorValue discriminatorValue = clazz.getAnnotation(DiscriminatorValue.class);
        DiscriminatorColumn discriminatorColumn = clazz.getAnnotation(DiscriminatorColumn.class);
        if (discriminatorValue != null && discriminatorColumn != null) {
            entityBundle.setDiscriminator(new Condition(discriminatorColumn.name(), discriminatorValue.value()));
        }
    }

    private void provideGenerator(EntityBundle entityBundle, PersistAttribute attribute) {
        if (entityBundle.idGenerator == null) {
            switch(attribute.getType()) {
                case LONG:
                    entityBundle.setIdGenerator(new LongIncrementIdGenerator());
                    break;
                case INT:
                    entityBundle.setIdGenerator(new IntIncrementIdGenerator());
                    break;
                case CHAR:
                    if (attribute.getScale() >= 36) {
                        entityBundle.setIdGenerator(new UUIDGenerator());
                    }
                    break;
                case STRING:
                    if (attribute.getScale() >= 36) {
                        entityBundle.setIdGenerator(new UUIDGenerator());
                    }
                    break;
            }
        }
    }

    private void produceId(EntityBundle entityBundle, List<Field> fieldList, Class<? extends Object> clazz2) throws SecurityException, NoSuchMethodException {
        for (Field field : fieldList) {
            Class<?> clazz = LangUtils.resolveClass(field, field.getType());
            String name = field.getName();
            Id id = field.getAnnotation(Id.class);
            if (id != null) {
                DBType typeByValue = TypeUtils.getTypeByValue(clazz);
                int scale = 0;
                if (typeByValue.equals(DBType.STRING)) {
                    scale = 36;
                }
                entityBundle.setId(new PersistFieldAttribute(name, LangUtils.underlineName(name), typeByValue, scale, 0, true, true, false));
            }
        }
        if (entityBundle.id == null) {
            for (Method method : clazz2.getMethods()) {
                Id id = method.getAnnotation(Id.class);
                if (id != null) {
                    Class<?> clazz3 = LangUtils.resolveClass(method, method.getReturnType());
                    String attrName = LangUtils.prepareName(method.getName());
                    DBType typeByValue = TypeUtils.getTypeByValue(clazz3);
                    int scale = 0;
                    if (typeByValue.equals(DBType.STRING)) {
                        scale = 36;
                    }
                    entityBundle.setId(new PersistMethodAttribute(attrName, LangUtils.underlineName(attrName), typeByValue, scale, 0, true, true, false));
                }
            }
        }
        if (entityBundle.id == null) {
            PersistAttribute attribute = entityBundle.getAttribute("id");
            if (attribute != null) {
                entityBundle.setId(attribute);
                attribute.setNullable(false);
            }
        }
        if (entityBundle.id != null) {
            provideGenerator(entityBundle, entityBundle.id);
            if (entityBundle.id.getScale() > 255) {
                entityBundle.id.setScale(36);
            }
        }
    }

    private EntityBundle preprocessBundle(Class<? extends Object> clazz) {
        String entityName = clazz.getName();
        String schema = null;
        String tableName = LangUtils.underlineName(clazz.getSimpleName()).toUpperCase();
        EntityBundle entityBundle = new EntityBundle(entityName, schema, tableName, clazz.getName(), false, true);
        return entityBundle;
    }

    @Override
    public List<EntityBundle> provideNonVolatile() {
        return null;
    }

    @Override
    public List<EntityBundle> getSchemaList() {
        return null;
    }

    @Override
    public List<CrossTable> getCrossList() {
        return null;
    }
}
