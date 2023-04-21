package net.sf.dozer.util.mapping.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import net.sf.dozer.util.mapping.classmap.ClassMap;
import net.sf.dozer.util.mapping.classmap.Mappings;
import net.sf.dozer.util.mapping.fieldmap.ExcludeFieldMap;
import net.sf.dozer.util.mapping.fieldmap.FieldMap;
import net.sf.dozer.util.mapping.fieldmap.GenericFieldMap;
import net.sf.dozer.util.mapping.fieldmap.MapFieldMap;
import org.apache.commons.lang.StringUtils;

/**
 * Internal class that decorates raw ClassMap objects and performs various validations on the explicit field mappings.
 * It applies global configuration and class level attributes to raw class mappings. It also creates the ClassMap
 * "prime" instance for bi-directional mappings. The ClassMap prime is created by copying the original ClassMap and
 * reversing the attributes. Only intended for internal use.
 * 
 * @author garsombke.franz
 */
public class MappingsParser {

    public Map processMappings(Mappings mappings) {
        Map result = new HashMap();
        FieldMap fieldMapPrime = null;
        if (mappings.getMapping() == null || mappings.getMapping().size() == 0) {
            return result;
        }
        Iterator iter = mappings.getMapping().iterator();
        ClassMap classMap = null;
        ClassMap classMapPrime = null;
        Set mapIds = new HashSet();
        while (iter.hasNext()) {
            classMap = (ClassMap) iter.next();
            ReflectionUtils.findPropertyDescriptor(classMap.getSrcClassToMap(), "", null);
            ReflectionUtils.findPropertyDescriptor(classMap.getDestClassToMap(), "", null);
            String theClassMapKey = ClassMapKeyFactory.createKey(classMap.getSrcClassToMap(), classMap.getDestClassToMap(), classMap.getMapId());
            if (result.containsKey(theClassMapKey)) {
                throw new IllegalArgumentException("Duplicate Class Mapping Found. Source: " + classMap.getSrcClassToMap().getName() + " Destination: " + classMap.getDestClassToMap().getName());
            }
            if (!MappingUtils.isBlankOrNull(classMap.getMapId())) {
                if (mapIds.contains(classMap.getMapId())) {
                    throw new IllegalArgumentException("Duplicate Map Id's Found. Map Id: " + classMap.getMapId());
                }
                mapIds.add(classMap.getMapId());
            }
            result.put(theClassMapKey, classMap);
            classMapPrime = new ClassMap(mappings.getConfiguration());
            MappingUtils.reverseFields(classMap, classMapPrime);
            if (classMap.getFieldMaps() != null) {
                Object[] fms = classMap.getFieldMaps().toArray();
                if (!StringUtils.equals(classMap.getType(), MapperConstants.ONE_WAY)) {
                    for (int i = 0; i < fms.length; i++) {
                        FieldMap fieldMap = (FieldMap) fms[i];
                        fieldMap.validate();
                        if (!(fieldMap instanceof ExcludeFieldMap)) {
                            if (MappingUtils.isSupportedMap(classMap.getDestClassToMap()) || MappingUtils.isSupportedMap(classMap.getSrcClassToMap()) || MappingUtils.isSupportedMap(fieldMap.getDestFieldType(classMap.getDestClassToMap())) || MappingUtils.isSupportedMap(fieldMap.getSrcFieldType(classMap.getSrcClassToMap()))) {
                                FieldMap fm = new MapFieldMap(fieldMap);
                                classMap.removeFieldMapping(fieldMap);
                                classMap.addFieldMapping(fm);
                                fieldMap = fm;
                            }
                        }
                        if (!(StringUtils.equals(fieldMap.getType(), MapperConstants.ONE_WAY) && !(fieldMap instanceof ExcludeFieldMap))) {
                            fieldMapPrime = (FieldMap) fieldMap.clone();
                            fieldMapPrime.setClassMap(classMapPrime);
                            if (fieldMapPrime instanceof ExcludeFieldMap && StringUtils.equals(fieldMap.getType(), MapperConstants.ONE_WAY)) {
                                fieldMapPrime = new GenericFieldMap(classMapPrime);
                            }
                            MappingUtils.reverseFields(fieldMap, fieldMapPrime);
                            if (!(fieldMap instanceof ExcludeFieldMap)) {
                                MappingUtils.applyGlobalCopyByReference(mappings.getConfiguration(), fieldMap, classMap);
                            }
                            if (!(fieldMapPrime instanceof ExcludeFieldMap)) {
                                MappingUtils.applyGlobalCopyByReference(mappings.getConfiguration(), fieldMapPrime, classMapPrime);
                            }
                        } else {
                            fieldMapPrime = new ExcludeFieldMap(classMapPrime);
                            MappingUtils.reverseFields(fieldMap, fieldMapPrime);
                        }
                        classMapPrime.addFieldMapping(fieldMapPrime);
                    }
                } else {
                    for (int i = 0; i < fms.length; i++) {
                        FieldMap oneWayFieldMap = (FieldMap) fms[i];
                        oneWayFieldMap.validate();
                        MappingUtils.applyGlobalCopyByReference(mappings.getConfiguration(), oneWayFieldMap, classMap);
                        if ((StringUtils.equals(oneWayFieldMap.getType(), MapperConstants.ONE_WAY))) {
                            fieldMapPrime = new ExcludeFieldMap(classMapPrime);
                            MappingUtils.reverseFields(oneWayFieldMap, fieldMapPrime);
                            classMapPrime.addFieldMapping(fieldMapPrime);
                        }
                    }
                }
            }
            if (!StringUtils.equals(classMap.getType(), MapperConstants.ONE_WAY)) {
                result.put(ClassMapKeyFactory.createKey(classMap.getDestClassToMap(), classMap.getSrcClassToMap(), classMap.getMapId()), classMapPrime);
            }
        }
        return result;
    }
}
