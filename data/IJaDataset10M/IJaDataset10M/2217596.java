package com.ivis.xprocess.properties.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.ivis.xprocess.framework.XchangeElement;
import com.ivis.xprocess.framework.Xrecord;
import com.ivis.xprocess.framework.impl.XelementImpl;
import com.ivis.xprocess.framework.impl.XrecordImpl;
import com.ivis.xprocess.framework.properties.PropertyType;
import com.ivis.xprocess.framework.properties.Xproperty;
import com.ivis.xprocess.framework.schema.SchemaRepository;
import com.ivis.xprocess.framework.xml.DeXMLifier;
import com.ivis.xprocess.framework.xml.IPersistenceHelper;
import com.ivis.xprocess.util.Merge2;

public class RecordProperty<T extends Xrecord> extends PropertyImpl {

    private static final Logger logger = Logger.getLogger(RecordProperty.class.getName());

    private T value;

    private IPersistenceHelper persistenceHelper;

    public RecordProperty(String name, T value) {
        super(name);
        this.value = value;
    }

    public RecordProperty(IPersistenceHelper persistenceHelper, org.jdom.Element element, XchangeElement xchangeElement) {
        this.persistenceHelper = persistenceHelper;
        initializeFromJDOMElement(element, xchangeElement);
    }

    public Xproperty clone() {
        return clone(null, null);
    }

    @SuppressWarnings("unchecked")
    public Xproperty clone(Map<XelementImpl, XelementImpl> prototypesToClones, Map<String, String> substitutionStringMap) {
        T clonedRecord = (T) ((XrecordImpl) value).clone(prototypesToClones, null);
        RecordProperty<T> clone = new RecordProperty<T>(this.getName(), clonedRecord);
        return clone;
    }

    public T getRecordValue() {
        return value;
    }

    @SuppressWarnings("unchecked")
    public void initializeFromJDOMElement(org.jdom.Element element, XchangeElement xchangeElement) {
        super.initializeFromJDOMElement(element);
        value = (T) DeXMLifier.unpersistRecord(persistenceHelper, element, xchangeElement);
    }

    @Override
    public org.jdom.Element getJDOMElement() {
        org.jdom.Element jdomElement = super.getJDOMElement();
        String classDes = SchemaRepository.recordDictionary.getDesignator(value.getClass());
        jdomElement.setAttribute("class", classDes);
        Iterator<Xproperty> propertyIter = value.getProperties();
        while (propertyIter.hasNext()) {
            Xproperty xproperty = propertyIter.next();
            if (xproperty instanceof UUIDProperty) {
                continue;
            }
            org.jdom.Element propertyAsJdom = ((PropertyImpl) xproperty).getJDOMElement();
            jdomElement.addContent(propertyAsJdom);
        }
        return jdomElement;
    }

    public Class<?> getImplementationClass() {
        return null;
    }

    @SuppressWarnings("unchecked")
    protected boolean equals(Xproperty xproperty) {
        if ((xproperty == null) || !(xproperty instanceof RecordProperty) || !(this.getName().equals(xproperty.getName()))) {
            return false;
        }
        RecordProperty that = (RecordProperty) xproperty;
        if (value == null) {
            return (that.value == null);
        }
        return value.equals(that.getRecordValue());
    }

    @SuppressWarnings("unchecked")
    public static MergedProperty merge(Xproperty base, Xproperty local, Xproperty latest) {
        Xrecord baseRecord = (base == null) ? null : ((RecordProperty) base).getRecordValue();
        Xrecord localRecord = (local == null) ? null : ((RecordProperty) local).getRecordValue();
        Xrecord latestRecord = (latest == null) ? null : ((RecordProperty) latest).getRecordValue();
        String name = null;
        if (base != null) {
            name = base.getName();
        } else if (local != null) {
            name = local.getName();
        } else if (latest != null) {
            name = latest.getName();
        }
        MergedRecord merged = internalMerge(baseRecord, localRecord, latestRecord);
        RecordProperty mergedProperty = new RecordProperty(name, merged.getRecord());
        return new MergedProperty(mergedProperty, merged.getStatus());
    }

    @SuppressWarnings({ "unchecked" })
    public static MergedRecord internalMerge(Xrecord base, Xrecord local, Xrecord latest) {
        if ((base == null) && (local == null) && (latest == null)) {
            logger.log(Level.WARNING, "Merge called on Xrecord with all null record args");
            return null;
        }
        if (local == null) {
            if (base == null) {
                return new MergedRecord(latest, Merge2.MERGE_MERGED);
            } else {
                if (base.equals(latest)) {
                    return new MergedRecord(null, Merge2.MERGE_MERGED);
                } else {
                    return new MergedRecord(null, Merge2.MERGE_CONFLICTED);
                }
            }
        }
        Constructor constructor = null;
        Xrecord result = null;
        try {
            constructor = local.getClass().getConstructor(IPersistenceHelper.class);
            result = (Xrecord) constructor.newInstance(local.getPersistenceHelper());
        } catch (Exception e) {
            logger.log(Level.FINE, "Problem instantiating record class");
        }
        Map<String, Xproperty> baseMap = (base == null) ? new HashMap<String, Xproperty>() : ((XrecordImpl) base).getPropertyMap();
        Map<String, Xproperty> localMap = (local == null) ? new HashMap<String, Xproperty>() : ((XrecordImpl) local).getPropertyMap();
        Map<String, Xproperty> latestMap = (latest == null) ? new HashMap<String, Xproperty>() : ((XrecordImpl) latest).getPropertyMap();
        Set<String> allProps = new HashSet<String>();
        allProps.addAll(baseMap.keySet());
        allProps.addAll(localMap.keySet());
        allProps.addAll(latestMap.keySet());
        Xproperty localProp = null;
        Xproperty baseProp = null;
        Xproperty latestProp = null;
        MergedProperty mergedProp = null;
        int status = Merge2.MERGE_MERGED;
        Class propClass = null;
        for (String prop : allProps) {
            localProp = localMap.get(prop);
            baseProp = baseMap.get(prop);
            latestProp = latestMap.get(prop);
            if (localProp != null) {
                propClass = localProp.getClass();
            } else if (latestProp != null) {
                propClass = latestProp.getClass();
            } else if (baseProp != null) {
                propClass = baseProp.getClass();
            }
            if (propClass != null) {
                try {
                    Method mergeMethod = propClass.getMethod("merge", Xproperty.class, Xproperty.class, Xproperty.class);
                    mergedProp = (MergedProperty) mergeMethod.invoke(null, baseProp, localProp, latestProp);
                } catch (Exception e) {
                    logger.log(Level.FINE, "Can execute merge method", e);
                }
                if (mergedProp != null) {
                    if (mergedProp.getStatus() == Merge2.MERGE_CONFLICTED) {
                        status = Merge2.MERGE_CONFLICTED;
                    }
                    ((XrecordImpl) result).assignProperty(mergedProp.getProp());
                }
            } else {
                logger.log(Level.FINE, "Can not obtain property class");
            }
        }
        return new MergedRecord(result, status);
    }

    public String retrieveValue() {
        return value.toString();
    }

    public Object retrieveObjectValue() {
        return value;
    }

    public PropertyType getPropertyType() {
        return PropertyType.RECORD;
    }
}
