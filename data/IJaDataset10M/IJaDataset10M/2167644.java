package org.gfix.binding;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.gfix.annotation.FixBinding;
import org.gfix.annotation.FixBound;
import org.gfix.annotation.FixFormatter;
import org.gfix.annotation.FixGroup;
import org.gfix.converter.Converter;
import org.gfix.converter.EnumConverter;
import org.gfix.converter.NoConverter;
import org.gfix.converter.StandardConverters;

/**
 * This holds the overall metadata for a given binding for a simple POJOs
 * @author alan
 *
 */
class FixBindingMetaData {

    private Map<Integer, FieldMetadata> tagToField = new HashMap<Integer, FieldMetadata>();

    private Set<FieldMetadata> fieldToTag = new TreeSet<FieldMetadata>(new FieldMetadataComparator());

    FixBindingMetaData() {
    }

    @SuppressWarnings("unchecked")
    void addBinding(FixBinding bind, Field field, BeanPropertyHierarchy prefix) throws FixBindingException {
        if (tagToField.containsKey(bind.fixTag())) {
            throw new FixBindingException("Tag :" + bind.fixTag() + " is already bound to field :" + tagToField.get(bind.fixTag()).getFieldName());
        }
        FixFormatter format = bind.formatter();
        Converter<?> formatter = null;
        if (!NoConverter.class.isAssignableFrom(format.formatter())) {
            formatter = createClass(format.formatter());
        } else if (field.getType().isEnum()) {
            Class<Enum<?>> clazz = (Class<Enum<?>>) field.getType();
            formatter = EnumConverter.getEnumConverter(clazz);
        } else if (BigDecimal.class.isAssignableFrom(field.getType())) {
            formatter = StandardConverters.getBigDecimalConverter();
        }
        LeafFieldMetadata fmd = new LeafFieldMetadata();
        fmd.converter = formatter;
        fmd.fieldName = field.getName();
        fmd.tagOrder = bind.order();
        fmd.tag = bind.fixTag();
        fmd.hierarchy = prefix;
        fieldToTag.add(fmd);
        tagToField.put(bind.fixTag(), fmd);
    }

    void addGroupBinding(FixGroup groupBinding, Field field, BeanPropertyHierarchy prefix) throws FixBindingException {
        FixBound fb = field.getAnnotation(FixBound.class);
        Class<?> collectionType = null;
        if (fb == null) {
            collectionType = deriveCollectionType(field);
        } else {
            collectionType = fb.javaType();
        }
        GroupFieldMetadata gmd = new GroupFieldMetadata();
        gmd.fieldName = field.getName();
        gmd.tagOrder = groupBinding.order();
        gmd.tag = groupBinding.fixGroupTag();
        gmd.hierarchy = prefix;
        gmd.parentData = this;
        ClassFixBindingDescription tempDes = new ClassFixBindingDescription(collectionType);
        gmd.elementData = tempDes.getMetadata(groupBinding.fixVersion());
        fieldToTag.add(gmd);
        tagToField.put(groupBinding.fixGroupTag(), gmd);
    }

    @SuppressWarnings("unchecked")
    private Class<?> deriveCollectionType(Field f) throws FixBindingException {
        Class<?> dType = f.getType();
        if (dType.isArray()) {
            return dType.getComponentType();
        } else {
            if (f.getGenericType() instanceof ParameterizedType) {
                Type[] genericArges = ((ParameterizedType) f.getGenericType()).getActualTypeArguments();
                if (genericArges != null && genericArges.length == 1 && genericArges[0] instanceof Class) {
                    return (Class) genericArges[0];
                }
            }
            throw new FixBindingException(f.getName() + " is not typed need a FixBound annotation");
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T createClass(Class<?> clazz) throws FixBindingException {
        try {
            return (T) clazz.newInstance();
        } catch (Exception e) {
            throw new FixBindingException("", e);
        }
    }

    public int getMappedFields() {
        return tagToField.size();
    }

    /**
	 * Does this Metadata map the given tag.
	 * @param tag
	 * @return
	 */
    public boolean binds(int tag) {
        return tagToField.containsKey(tag);
    }

    boolean setValue(int tag, String contents, FixBindingContext onBean) throws FixBindingException {
        boolean groupHandled = false;
        GroupState gs = (GroupState) onBean.getGroupState(this);
        if (gs != null) {
            if (!gs.setValue(tag, contents, onBean)) {
                onBean.removeGroupState(this);
            } else {
                return true;
            }
        }
        if (!groupHandled) {
            FieldMetadata fmd = tagToField.get(tag);
            if (fmd != null) {
                fmd.setValueOn(contents, onBean);
                return true;
            }
            return false;
        }
        return false;
    }

    void serialise(Object source, MessageSink sink) throws FixBindingException {
        for (FieldMetadata field : fieldToTag) {
            field.writeTagToSink(sink, source);
        }
    }

    private static final class GroupState {

        GroupFieldMetadata group;

        int expectedLength;

        FixBindingContext currentContext = new FixBindingContext();

        private Set<Integer> tagsAlreadySeen = new HashSet<Integer>();

        private Collection<Object> objects;

        public GroupState(int length, GroupFieldMetadata grp) {
            this.expectedLength = length;
            this.group = grp;
            this.objects = new ArrayList<Object>(this.expectedLength);
        }

        boolean setValue(int tag, String contents, FixBindingContext onBean) throws FixBindingException {
            boolean nested = false;
            if (currentContext.getGroupState(group.elementData) == null) {
                if (tagsAlreadySeen.contains(tag)) {
                    objects.add(currentContext.getBoundObject());
                    currentContext.setBoundObject(null);
                    tagsAlreadySeen.clear();
                }
            } else {
                nested = true;
            }
            if (group.elementData.setValue(tag, contents, currentContext)) {
                if (!nested) {
                    tagsAlreadySeen.add(tag);
                }
                return true;
            } else if (this.group.parentData.binds(tag)) {
                objects.add(currentContext.getBoundObject());
                return false;
            } else {
                return true;
            }
        }
    }

    /**
	 * This holds a representation of the FixBinding.
	 * @author alan
	 *
	 */
    private abstract static class FieldMetadata {

        protected int tagOrder;

        protected String fieldName;

        protected int tag;

        protected BeanPropertyHierarchy hierarchy;

        public BeanPropertyHierarchy getHierarchy() {
            return hierarchy;
        }

        public int getTagOrder() {
            return tagOrder;
        }

        public String getFieldName() {
            return fieldName;
        }

        public int getTag() {
            return tag;
        }

        public abstract void setValueOn(String contents, FixBindingContext bean) throws FixBindingException;

        public abstract void writeTagToSink(MessageSink sink, Object source) throws FixBindingException;
    }

    private static class GroupFieldMetadata extends FieldMetadata {

        FixBindingMetaData parentData;

        FixBindingMetaData elementData;

        public void setValueOn(String contents, FixBindingContext bean) throws FixBindingException {
            GroupState state = new GroupState(Integer.parseInt(contents), this);
            bean.addGroupState(parentData, state);
            try {
                Object theBean = getHierarchy().getBean(bean);
                BeanUtils.setProperty(theBean, getFieldName(), state.objects);
            } catch (IllegalAccessException e) {
                throw new FixBindingException("", e);
            } catch (InvocationTargetException e) {
                throw new FixBindingException("", e);
            } catch (NoSuchMethodException e) {
                throw new FixBindingException("", e);
            } catch (InstantiationException e) {
                throw new FixBindingException("", e);
            }
        }

        public void writeTagToSink(MessageSink sink, Object source) throws FixBindingException {
        }
    }

    private static class LeafFieldMetadata<T> extends FieldMetadata {

        private Converter<T> converter;

        @SuppressWarnings("unchecked")
        public void writeTagToSink(MessageSink sink, Object source) throws FixBindingException {
            try {
                Object from = getHierarchy().getBeanNoCreate(source);
                T value = (T) PropertyUtils.getProperty(from, getFieldName());
                if (value != null) {
                    String s = convertToFix(value);
                    if (s != null && s.length() != 0) {
                        sink.writeTag(getTag(), s);
                    }
                }
            } catch (IllegalAccessException e) {
                throw new FixBindingException("", e);
            } catch (InvocationTargetException e) {
                throw new FixBindingException("", e);
            } catch (NoSuchMethodException e) {
                throw new FixBindingException("", e);
            }
        }

        public void setValueOn(String contents, FixBindingContext source) throws FixBindingException {
            try {
                Object theBean = getHierarchy().getBean(source);
                Object value = convertFromFix(contents);
                BeanUtils.setProperty(theBean, getFieldName(), value);
            } catch (IllegalAccessException e) {
                throw new FixBindingException("", e);
            } catch (InvocationTargetException e) {
                throw new FixBindingException("", e);
            } catch (NoSuchMethodException e) {
                throw new FixBindingException("", e);
            } catch (InstantiationException e) {
                throw new FixBindingException("", e);
            }
        }

        private Object convertFromFix(String contents) {
            if (converter != null) {
                return converter.convertFromFix(contents);
            } else {
                return contents;
            }
        }

        private String convertToFix(T value) {
            if (converter != null) {
                return converter.convertToFix(value);
            } else {
                return String.valueOf(value);
            }
        }
    }

    private static class FieldMetadataComparator implements Comparator<FieldMetadata> {

        @Override
        public int compare(FieldMetadata o1, FieldMetadata o2) {
            if (o1.getTagOrder() < o2.getTagOrder()) {
                return -1;
            } else if (o1.getTagOrder() > o2.getTagOrder()) {
                return 1;
            } else {
                int hierarchyCompare = o1.getHierarchy().compareTo(o2.getHierarchy());
                if (hierarchyCompare == 0) {
                    return o1.getFieldName().compareTo(o2.getFieldName());
                } else {
                    return hierarchyCompare;
                }
            }
        }
    }
}
