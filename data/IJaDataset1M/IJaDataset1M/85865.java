package org.judo.propertyobject;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.jdom.Element;
import org.judo.database.annotations.Auto;
import org.judo.database.annotations.Mapto;
import org.judo.database.annotations.Nullable;
import org.judo.database.annotations.PK;
import org.judo.database.annotations.TableName;
import org.judo.dataproperty.DataProperty;
import org.judo.frontcontroller.ValidationFailed;
import org.judo.properties.Props;
import org.judo.service.JudoService;
import org.judo.util.ConvenienceBase;
import org.judo.util.DateUtil;
import org.judo.util.Properties;
import org.judo.util.Util;
import org.judo.validation.ValidationMessage;

public class PropertyObject extends ConvenienceBase {

    private HashMap objProps = new HashMap();

    private HashMap<String, HashMap> originalValues = new HashMap<String, HashMap>();

    private HashMap<String, String> mappedColumns = new HashMap<String, String>();

    List<ValidationMessage> messages = new ArrayList<ValidationMessage>();

    private boolean declaredOnly;

    private boolean declaredChecked;

    private boolean hasCheckedMap = false;

    private boolean isAutoIncrement;

    private boolean managed;

    private String definedPersistedName;

    public List<ValidationMessage> messages() {
        return messages;
    }

    public static void main(String s[]) {
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean managed() {
        return managed;
    }

    public void managed(boolean state) {
        managed = state;
    }

    protected void getMapValues() {
        List<String> names = propNames();
        for (String colName : names) {
            try {
                if (hasAnnotation(colName, Mapto.class)) {
                    String mapedValue = annotationMap(colName);
                    mappedColumns.put(colName, mapedValue);
                }
            } catch (PropertyNotFoundException e) {
            }
            String mapedValue = Props.get(this.getClass().getSimpleName() + "." + colName);
            if (mapedValue != null) mappedColumns.put(colName, mapedValue);
        }
        hasCheckedMap = true;
    }

    /**
	 * This method returns true if any properties are mapped to
	 * the value specified by the 'name' parameter.
	 * 
	 *  In otherwords if the there was a field:
	 *  
	 *  TODO: Check map file
	 *  
	 *  @Mapto("aaa")
	 *  private int bbb;
	 * 
	 * Then isMappedColumn("aaa") would return true and
	 * isMappedColumn("bbb") would return false
	 * 
	 * @param name
	 * @return
	 */
    public boolean isMappedColumn(String name) {
        if (!hasCheckedMap) getMapValues();
        return mappedColumns.containsKey(name);
    }

    public String mappedColumn(String name) {
        if (!hasCheckedMap) getMapValues();
        return mappedColumns.get(name);
    }

    public void moveFromMapToCol() {
        if (!hasCheckedMap) getMapValues();
        List<String> names = propNames();
        for (String curProp : names) {
            if (mappedColumns.containsKey(curProp)) {
                String mapColumn = mappedColumns.get(curProp);
                try {
                    set(curProp, get(mapColumn));
                } catch (PropertyNotFoundException e) {
                }
            }
        }
    }

    public void moveFromColToMap() {
        if (!hasCheckedMap) getMapValues();
        List<String> names = propNames();
        for (String curProp : names) {
            if (mappedColumns.containsKey(curProp)) {
                String mapColumn = mappedColumns.get(curProp);
                try {
                    set(mapColumn, get(curProp));
                } catch (PropertyNotFoundException e) {
                }
            }
        }
    }

    public boolean hasChangedSinceQuery(String prop) throws PropertyNotFoundException {
        Object value = findPropValue(prop);
        Object originalValue = originalValues.get(prop);
        if (value == null && originalValue == null) return false;
        if (value == null && originalValue != null) return true;
        if (value != null && originalValue == null) return true;
        if (value.equals(originalValue)) return false; else return true;
    }

    public HashMap allOriginalValues() {
        return originalValues;
    }

    public void allOriginalValues(HashMap values) {
        originalValues.putAll(values);
    }

    public void copyProps(Map map) throws CopyException {
        try {
            if (map instanceof PropertyObject) {
                copyProps((PropertyObject) map);
                return;
            }
            Iterator keys = map.keySet().iterator();
            while (keys.hasNext()) {
                Object key = keys.next();
                Object mapValue = map.get(key);
                if (hasOnlyDeclaredProperties()) writeToDeclaredProp(key.toString(), mapValue); else {
                    if (!writeToDeclaredProp(key.toString(), mapValue)) putInDynamicProp(key.toString(), mapValue);
                }
            }
        } catch (CopyException e) {
            throw e;
        } catch (Exception e) {
            throw new CopyException("Could not bind properties: " + e);
        }
    }

    private void putInDynamicProp(String name, Object mapValue) {
        objProps.put(name, mapValue);
    }

    public void copyProps(Object obj) throws CopyException {
        copyObjProps(obj);
    }

    public void copyProps(PropertyObject obj) throws CopyException {
        try {
            copyProps(obj.dynamicProperties());
            copyObjProps(obj);
            copyPropFields(obj);
        } catch (CopyException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new CopyException("Could not bind properties: " + e);
        }
    }

    private void copyPropFields(PropertyObject obj) throws CopyException {
        try {
            Field fields[] = obj.getClass().getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                if (!hasField(fields[i].getName())) continue;
                if (obj.hasAnnotation(fields[i].getName(), Prop.class)) {
                    String name = fields[i].getName();
                    fields[i].setAccessible(true);
                    Object value = fields[i].get(obj);
                    if (hasOnlyDeclaredProperties()) writeToDeclaredProp(name.toString(), value); else {
                        if (!writeToDeclaredProp(name.toString(), value)) objProps.put(name.toString(), value);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new CopyException("Could not bind field properties: " + e);
        }
    }

    public boolean hasField(String name) {
        try {
            Field field = this.getClass().getDeclaredField(name);
            if (field == null) return false;
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public void copyProps(ResultSet obj) {
        System.out.println("Binding as a ResultSet");
    }

    public void copyProps(HttpServletRequest obj) throws CopyException {
        Enumeration names = obj.getParameterNames();
        messages.clear();
        if (obj.getAttribute("ignore_valdiation") != null && obj.getAttribute("ignore_valdiation").equals("true")) {
            return;
        }
        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            String values[] = obj.getParameterValues(name);
            for (int i = 0; i < values.length; i++) {
                String value = values[i];
                try {
                    if (hasOnlyDeclaredProperties()) writeToDeclaredProp(name.toString(), value); else {
                        if (!writeToDeclaredProp(name.toString(), value)) objProps.put(name.toString(), value);
                    }
                } catch (ValidationError e) {
                    if (value != null && !value.equals("")) messages.add(new ValidationMessage(name, e.message));
                }
            }
        }
    }

    public void copyProps(String xml) {
        System.out.println("Binding as an XML String");
    }

    public void copyProps(PropertyResourceBundle properties) {
        System.out.println("Binding as a Properties File");
    }

    public void copyProps(PropertyResourceBundle properties, String startPath) {
        System.out.println("Binding as a Properties File");
    }

    public void copyProps(InetAddress address) {
    }

    public boolean isDataProperty(String propName) {
        return false;
    }

    public boolean propertyIsService(String propName) {
        try {
            if (Properties.propertyExists(this, propName)) {
                PropertyDescriptor prop = Properties.getProperty(this, propName);
                if (JudoService.class.isAssignableFrom(prop.getPropertyType())) return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Class propertyType(String propName) throws PropertyNotFoundException {
        Class type = null;
        try {
            if (Properties.propertyExists(this, propName)) {
                PropertyDescriptor prop = Properties.getProperty(this, propName);
                type = prop.getPropertyType();
            } else {
                Object obj = get(propName);
                if (obj == null) throw new PropertyNotFoundException("Property '" + propName + "' not found in " + this.getClass().getCanonicalName());
                type = obj.getClass();
            }
        } catch (PropertyNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new PropertyNotFoundException("Property '" + propName + "' not found in " + this.getClass().getCanonicalName() + " because: " + e);
        }
        return type;
    }

    protected void copyObjProps(Object obj) throws CopyException {
        try {
            List props = Properties.getFullProperties(obj);
            for (int i = 0; i < props.size(); i++) {
                PropertyDescriptor prop = (PropertyDescriptor) props.get(i);
                String name = prop.getName();
                Object value = Properties.getPropertyValue(name, obj);
                if (value instanceof DataProperty) {
                    DataProperty dataProp = (DataProperty) value;
                    if (hasOnlyDeclaredProperties()) writeToDeclaredProp(name.toString(), dataProp.value()); else {
                        if (!writeToDeclaredProp(name.toString(), dataProp.value())) objProps.put(name.toString(), dataProp.value());
                    }
                } else {
                    if (hasOnlyDeclaredProperties()) writeToDeclaredProp(name.toString(), value); else {
                        if (!writeToDeclaredProp(name.toString(), value)) objProps.put(name.toString(), value);
                    }
                }
            }
        } catch (CopyException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new CopyException("Could not bind properties: " + e);
        }
    }

    public Object getRawPropertyValue(String name) throws PropertyNotFoundException {
        Object prop = null;
        try {
            PropertyDescriptor desc = null;
            if (objProps.containsKey(name)) {
                prop = objProps.get(name);
            } else {
                if (hasAnnotation(name, Prop.class)) {
                    Field field = this.getClass().getDeclaredField(name);
                    field.setAccessible(true);
                    prop = field.get(this);
                } else {
                    try {
                        desc = Properties.getProperty(this, name);
                    } catch (Exception e) {
                    }
                    if (desc != null) prop = Properties.getPropertyValue(name, this); else throw new PropertyNotFoundException("No declared or dynamic properties for: " + name);
                }
            }
        } catch (PropertyNotFoundException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new PropertyNotFoundException("The was a problem getting declared or dynamic properties for: " + name + ". Error: " + e);
        }
        return prop;
    }

    public void persistedName(String name) {
        definedPersistedName = name;
    }

    public String persistedName() {
        String name = null;
        TableName persistedName = (TableName) this.getClass().getAnnotation(TableName.class);
        if (persistedName != null) name = persistedName.value(); else name = this.getClass().getSimpleName();
        if (Props.get(this.getClass().getSimpleName()) != null) name = Props.get(this.getClass().getSimpleName());
        if (definedPersistedName != null) name = definedPersistedName;
        return name;
    }

    public Map dynamicProperties() {
        return objProps;
    }

    public void writePrimativePropsToMap(Map map) throws Exception {
        List props = primativePropNames();
        for (int i = 0; i < props.size(); i++) {
            String name = props.get(i).toString();
            Object data = findPropValue(name);
            map.put(name, data);
        }
    }

    public void writePropsToMap(Map map) throws Exception {
        List props = propNames();
        for (int i = 0; i < props.size(); i++) {
            String name = props.get(i).toString();
            Object data = findPropValue(name);
            map.put(name, data);
        }
    }

    public void writePrimativePropsToObj(Object obj) throws Exception {
        List props = primativePropNames();
        for (int i = 0; i < props.size(); i++) {
            String name = props.get(i).toString();
            Object data = findPropValue(name);
            try {
                Properties.setObjValue(obj, name, data);
            } catch (Exception e) {
            }
        }
    }

    public void writePropsToObj(Object obj) throws Exception {
        List props = propNames();
        for (int i = 0; i < props.size(); i++) {
            String name = props.get(i).toString();
            Object data = findPropValue(name);
            try {
                Properties.setObjValue(obj, name, data);
            } catch (Exception e) {
            }
        }
    }

    public void writePrimativePropsToElement(Element node) throws Exception {
        List props = primativePropNames();
        for (int i = 0; i < props.size(); i++) {
            String name = props.get(i).toString();
            Object data = findPropValue(name);
            Element propElement = new Element(name);
            if (data == null) {
                propElement.setText("null");
            } else {
                propElement.setText(data.toString());
            }
            node.addContent(propElement);
        }
    }

    public boolean propIsPrimitive(String name) throws PropertyNotFoundException {
        String type = null;
        PropertyDescriptor desc = null;
        try {
            desc = Properties.getProperty(this, name);
        } catch (Exception e) {
        }
        if (desc != null) {
            type = desc.getPropertyType().getCanonicalName();
        } else if (objProps.containsKey(name)) {
            Object value = objProps.get(name);
            if (value == null) return true;
            type = value.getClass().getCanonicalName();
        } else {
            try {
                Field field = this.getClass().getDeclaredField(name);
                type = field.getType().getCanonicalName();
            } catch (Exception e) {
                throw new PropertyNotFoundException("Property not found: " + name);
            }
        }
        if (type.equals("java.lang.Integer")) return true; else if (type.equals("java.lang.Short")) return true; else if (type.equals("java.lang.Long")) return true; else if (type.equals("java.lang.Float")) return true; else if (type.equals("java.lang.Double")) return true; else if (type.equals("java.lang.Boolean")) return true; else if (type.equals("java.lang.String")) return true; else if (type.equals("java.util.Date")) return true; else if (type.equals("java.sql.Date")) return true; else if (type.equals("java.lang.Character")) return true; else if (type.equals("int")) return true; else if (type.equals("short")) return true; else if (type.equals("long")) return true; else if (type.equals("double")) return true; else if (type.equals("float")) return true; else if (type.equals("boolean")) return true; else if (type.equals("char")) return true;
        return false;
    }

    public List primativePropNames() {
        ArrayList propList = new ArrayList();
        Iterator keys = objProps.keySet().iterator();
        while (keys.hasNext()) {
            String key = keys.next().toString();
            try {
                if (propIsPrimitive(key)) propList.add(key);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            List props = Properties.getFullProperties(this);
            for (int i = 0; i < props.size(); i++) {
                PropertyDescriptor prop = (PropertyDescriptor) props.get(i);
                String name = prop.getName();
                if (propIsPrimitive(name)) propList.add(name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return propList;
    }

    public List<String> propNames() {
        ArrayList propList = new ArrayList();
        Iterator keys = objProps.keySet().iterator();
        while (keys.hasNext()) {
            propList.add("" + keys.next());
        }
        try {
            Field fields[] = this.getClass().getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                if (hasAnnotation(fields[i].getName(), Prop.class)) propList.add(fields[i].getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            List props = Properties.getFullProperties(this);
            for (int i = 0; i < props.size(); i++) {
                PropertyDescriptor prop = (PropertyDescriptor) props.get(i);
                String name = prop.getName();
                propList.add(name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return propList;
    }

    public void set(String propName, Object obj) {
        if (!writeToDeclaredProperty(propName, obj)) objProps.put(propName, obj);
        check();
    }

    public void set(String propName, int value) {
        Object obj = new Integer(value);
        if (!writeToDeclaredProperty(propName, obj)) {
            objProps.put(propName, obj);
        }
        check();
    }

    public void set(String propName, String value) {
        if (!writeToDeclaredProperty(propName, value)) {
            objProps.put(propName, value);
        }
        check();
    }

    public void set(String propName, java.util.Date value) {
        check();
    }

    public void set(String propName, float value) {
        Object obj = new Float(value);
        if (!writeToDeclaredProperty(propName, obj)) objProps.put(propName, obj);
        check();
    }

    public void set(String propName, double value) {
        Object obj = new Double(value);
        if (!writeToDeclaredProperty(propName, obj)) objProps.put(propName, obj);
        check();
    }

    public void set(String propName, boolean value) {
        Object obj = new Boolean(value);
        if (!writeToDeclaredProperty(propName, obj)) objProps.put(propName, obj);
        check();
    }

    public int intProp(String name) throws PropertyNotFoundException {
        int retValue = 0;
        try {
            Object obj = findPropValue(name);
            retValue = Integer.parseInt(obj.toString());
        } catch (PropertyNotFoundException e) {
            throw e;
        } catch (NumberFormatException e) {
            throw new PropertyNotFoundException("The was a problem getting declared or dynamic properties for: " + name + ". The value could not be converted to requested type.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new PropertyNotFoundException("The was a problem getting declared or dynamic properties for: " + name + ". Error: " + e);
        }
        return retValue;
    }

    public double doubleProp(String name) throws PropertyNotFoundException {
        double retValue = 0;
        try {
            Object obj = findPropValue(name);
            retValue = Double.parseDouble(obj.toString());
        } catch (PropertyNotFoundException e) {
            throw e;
        } catch (NumberFormatException e) {
            throw new PropertyNotFoundException("The was a problem getting declared or dynamic properties for: " + name + ". The value could not be converted to requested type.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new PropertyNotFoundException("The was a problem getting declared or dynamic properties for: " + name + ". Error: " + e);
        }
        return retValue;
    }

    public java.util.Date dateProp(String name) throws PropertyNotFoundException {
        Date date = null;
        try {
            Object obj = findPropValue(name);
            if (obj instanceof Date) date = (Date) obj; else date = DateUtil.getDateFromStr(obj.toString());
        } catch (PropertyNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new PropertyNotFoundException("The was a problem getting declared or dynamic properties for: " + name + ". Error: " + e);
        }
        return date;
    }

    public float floatProp(String name) throws PropertyNotFoundException {
        float retValue = 0;
        try {
            Object obj = findPropValue(name);
            retValue = Float.parseFloat(obj.toString());
        } catch (PropertyNotFoundException e) {
            throw e;
        } catch (NumberFormatException e) {
            throw new PropertyNotFoundException("The was a problem getting declared or dynamic properties for: " + name + ". The value could not be converted to requested type (Float).");
        } catch (Exception e) {
            e.printStackTrace();
            throw new PropertyNotFoundException("The was a problem getting declared or dynamic (Float) properties for: " + name + ". Error: " + e);
        }
        return retValue;
    }

    public boolean booleanProp(String name) throws PropertyNotFoundException {
        boolean retValue = false;
        try {
            Object obj = findPropValue(name);
            retValue = Boolean.parseBoolean(obj.toString());
        } catch (PropertyNotFoundException e) {
            throw e;
        } catch (NumberFormatException e) {
            throw new PropertyNotFoundException("The was a problem getting declared or dynamic properties for: " + name + ". The value could not be converted to requested type.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new PropertyNotFoundException("The was a problem getting declared or dynamic properties for: " + name + ". Error: " + e);
        }
        return retValue;
    }

    public Object get(String name) throws PropertyNotFoundException {
        Object retValue = null;
        try {
            retValue = findPropValue(name);
        } catch (PropertyNotFoundException e) {
            throw e;
        } catch (NumberFormatException e) {
            throw new PropertyNotFoundException("The was a problem getting declared or dynamic properties for: " + name + ". The value could not be converted to requested type.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new PropertyNotFoundException("The was a problem getting declared or dynamic properties for: " + name + ". Error: " + e);
        }
        return retValue;
    }

    protected void check() {
        checkConstraints();
        checkTriggers();
    }

    protected void checkConstraints() {
    }

    protected void checkTriggers() {
    }

    public Object findPropValue(String name) throws PropertyNotFoundException {
        Object prop = null;
        try {
            PropertyDescriptor desc = null;
            if (objProps.containsKey(name)) {
                prop = objProps.get(name);
            } else {
                if (hasAnnotation(name, Prop.class)) {
                    Field field = this.getClass().getDeclaredField(name);
                    field.setAccessible(true);
                    prop = field.get(this);
                    if (prop != null) {
                        if (prop instanceof DataProperty) prop = ((DataProperty) prop).value();
                    }
                } else {
                    try {
                        desc = Properties.getProperty(this, name);
                    } catch (Exception e) {
                    }
                    if (desc != null) {
                        prop = Properties.getPropertyValue(name, this);
                        if (prop instanceof DataProperty) prop = ((DataProperty) prop).value();
                    } else {
                        throw new PropertyNotFoundException("No declared or dynamic properties for: " + name);
                    }
                }
            }
        } catch (PropertyNotFoundException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new PropertyNotFoundException("The was a problem getting declared or dynamic properties for: " + name + ". Error: " + e);
        }
        return prop;
    }

    protected boolean writeToDeclaredProperty(String name, Object value) {
        try {
            return writeToDeclaredProp(name, value);
        } catch (Exception e) {
            return false;
        }
    }

    protected boolean writeToDeclaredProp(String name, Object value) throws CopyException, ValidationError {
        String type = null;
        try {
            if (hasAnnotation(name, Prop.class)) {
                try {
                    setFieldValue(name, value);
                    return true;
                } catch (CopyException e) {
                    throw e;
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new CopyException("The value '" + value + "' (Data type: '" + value.getClass().getCanonicalName() + "') could not be put in declared field @Prop property '" + name + "' of '" + this.getClass().getCanonicalName() + "' because: " + e);
                }
            }
        } catch (PropertyNotFoundException e1) {
        }
        try {
            Properties.getProperty(this, name);
        } catch (Exception e) {
            return false;
        }
        if (value == null) return true;
        try {
            PropertyDescriptor prop = Properties.getProperty(this, name);
            Class propertyType = prop.getPropertyType();
            if (DataProperty.class.isAssignableFrom(propertyType)) {
                DataProperty dataProperty = findDataProperty(name);
                Object convertedValue = convertToCorrectType(name, dataProperty.getInternalType(), value);
                dataProperty.populate(convertedValue);
            } else if (value instanceof List) {
                System.out.println("TODO: Writing a List property in PropertyObject");
            } else {
                Object convertedValue = convertToCorrectType(name, propertyType, value);
                Properties.setObjValue(this, name, convertedValue);
            }
        } catch (CopyException e) {
            throw e;
        } catch (ValidationError e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new CopyException("The value '" + value + "' (Data type: '" + value.getClass().getCanonicalName() + "') could not be put in declared property '" + name + "' (Data type: '" + type + "') of '" + this.getClass().getCanonicalName() + "' because: " + e);
        }
        return true;
    }

    private void setFieldValue(String name, Object value) throws SecurityException, NoSuchFieldException, CopyException, IllegalArgumentException, IllegalAccessException, ValidationError {
        Field field = this.getClass().getDeclaredField(name);
        field.setAccessible(true);
        if (DataProperty.class.isAssignableFrom(field.getType())) {
            DataProperty dataProperty = (DataProperty) field.get(this);
            dataProperty.populate(value);
        } else {
            Object convertedValue = convertToCorrectType(name, field.getType(), value);
            field.set(this, convertedValue);
        }
    }

    public DataProperty findDataProperty(String name) throws Exception {
        DataProperty dataProp = null;
        PropertyDescriptor prop = Properties.getProperty(this, name);
        Object obj = Properties.getPropertyValue(name, this);
        dataProp = (DataProperty) obj;
        return dataProp;
    }

    private Object convertToCorrectType(String name, Class propertyType, Object value) throws CopyException, ValidationError {
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        Object retVal = null;
        if (value instanceof DataProperty) {
            value = ((DataProperty) value).value();
        }
        if (value == null) return retVal;
        try {
            String type = propertyType.getName();
            if (type.equals("int")) {
                if (value == null) value = 0; else if (value.equals("")) value = "0";
                retVal = new Integer(round(value.toString()));
            } else if (type.equals("short")) {
                if (value == null) value = 0; else if (value.equals("")) value = "0";
                retVal = new Short(round(value.toString()));
            } else if (type.equals("long")) {
                if (value == null) value = 0; else if (value.equals("")) value = "0";
                retVal = new Long(round(value.toString()));
            } else if (type.equals("float")) {
                if (value == null) value = 0; else if (value.equals("")) value = "0";
                retVal = new Float(value.toString());
            } else if (type.equals("double")) {
                if (value == null) value = 0; else if (value.equals("")) value = "0";
                retVal = new Double(value.toString());
            } else if (type.equals("boolean")) {
                if (value == null) value = "false";
                if (value.equals("")) value = "false";
                retVal = new Boolean(value.toString());
            } else if (type.equals("java.lang.String")) {
                if (value == null) value = "";
                retVal = value.toString();
            } else if (type.equals("java.util.Date")) {
                if (value == null) value = "";
                try {
                    retVal = format.parse(value.toString());
                } catch (ParseException e) {
                    throw new ValidationError(name, "Not a Valid Date: " + value.toString());
                }
            } else {
                if (value == null) retVal = null; else if (propertyType.isAssignableFrom(value.getClass())) retVal = value; else if (value instanceof List) {
                    System.out.println("TODO: What to do with list in PropertyObject.convertToCorrectType(String name,Class propertyType, Object value)");
                } else throw new CopyException("The value '" + value + "' (Data type: '" + value.getClass().getCanonicalName() + "') could not be put in declared property '" + name + "' (Data type: '" + type + "') of '" + this.getClass().getCanonicalName() + "' because they are not compatible types");
            }
        } catch (NumberFormatException e) {
            throw new ValidationError(name, "Not a Valid Number: " + value);
        }
        return retVal;
    }

    private String round(String value) {
        if (value.equals("")) value = "0";
        Double num = new Double(value);
        return "" + num.longValue();
    }

    public int size() {
        return propNames().size();
    }

    public boolean isEmpty() {
        if (propNames().size() == 0) return true;
        return false;
    }

    public boolean containsKey(Object key) {
        try {
            findPropValue(key.toString());
        } catch (PropertyNotFoundException e) {
            return false;
        }
        return true;
    }

    public boolean containsValue(Object obj) {
        List props = propNames();
        for (int i = 0; i < props.size(); i++) {
            String name = props.get(i).toString();
            try {
                Object data = findPropValue(name);
                if (obj.equals(data)) return true;
            } catch (Exception e) {
            }
        }
        return false;
    }

    public Object get(Object key) {
        Object value = null;
        try {
        } catch (Exception e) {
        }
        return value;
    }

    public Object put(Object key, Object value) {
        set(key.toString(), value);
        return value;
    }

    public Object remove(Object obj) {
        return objProps.remove(obj);
    }

    public void putAll(Map map) {
        try {
            copyProps(map);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clear() {
        objProps.clear();
    }

    public Set keySet() {
        return new LinkedHashSet(propNames());
    }

    public Collection values() {
        ArrayList list = new ArrayList();
        List props = propNames();
        for (int i = 0; i < props.size(); i++) {
            String name = props.get(i).toString();
            try {
                Object data = findPropValue(name);
                list.add(data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public Set entrySet() {
        return null;
    }

    protected boolean hasOnlyDeclaredProperties() {
        if (!declaredChecked) {
            declaredChecked = true;
            Annotation declaredProps = this.getClass().getAnnotation(DeclaredPropsOnly.class);
            if (declaredProps == null) declaredOnly = false; else declaredOnly = true;
        }
        return declaredOnly;
    }

    public String parse(String str) {
        String result = str;
        ArrayList<String> paramerterizedNames = new ArrayList<String>();
        char chars[] = str.toCharArray();
        boolean inParam = false;
        String curParamName = "";
        int symbolLocation = -1;
        for (int i = 0; i < chars.length; i++) {
            if (inParam) {
                if (chars[i] == '}') {
                    inParam = false;
                    paramerterizedNames.add(curParamName);
                    curParamName = "";
                    continue;
                }
                curParamName += chars[i];
            } else {
                if (chars[i] == '$') {
                    symbolLocation = i;
                } else if (chars[i] == '{') {
                    if (i - 1 == symbolLocation) {
                        inParam = true;
                        curParamName = "";
                    }
                }
            }
        }
        for (int i = 0; i < paramerterizedNames.size(); i++) {
            String param = paramerterizedNames.get(i);
            Object newValue = envValue(param);
            if (newValue == null) result = result.replace("${" + param + "}", ""); else result = result.replace("${" + param + "}", newValue.toString());
        }
        return result;
    }

    private Object envValue(String param) {
        Object value = "";
        try {
            value = get(param);
        } catch (PropertyNotFoundException e) {
            value = Props.get(param);
            if (value == null) value = "NOT_DEFINED";
        }
        return value;
    }

    public boolean hasAnnotation(String prop, Class annotation) throws PropertyNotFoundException {
        try {
            Field field = this.getClass().getDeclaredField(prop);
            Annotation desiredAnnotation = field.getAnnotation(annotation);
            if (desiredAnnotation == null) return false;
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean isPrimaryKey(String name) throws PropertyNotFoundException {
        if (hasAnnotation(name, PK.class)) return true; else return false;
    }

    public void isAutoIncrement(boolean value) throws PropertyNotFoundException {
        isAutoIncrement = value;
    }

    public boolean isAutoIncrement(String name) throws PropertyNotFoundException {
        if (hasAnnotation(name, Auto.class)) return true;
        if (isAutoIncrement) return true;
        return false;
    }

    public boolean isAnnotationMaped(String name) throws PropertyNotFoundException {
        if (hasAnnotation(name, Mapto.class)) return true; else return false;
    }

    public boolean isNullable(String propName) throws PropertyNotFoundException {
        if (hasAnnotation(propName, Nullable.class)) return true; else return false;
    }

    public String annotationMap(String name) throws PropertyNotFoundException {
        if (hasAnnotation(name, Mapto.class)) {
            Mapto mapTo = null;
            try {
                mapTo = (Mapto) this.getClass().getDeclaredField(name).getAnnotation(Mapto.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (mapTo == null) return null;
            return mapTo.value();
        } else {
            return null;
        }
    }

    public boolean propertyExists(String property) {
        try {
            findPropValue(property);
        } catch (PropertyNotFoundException e) {
            return false;
        }
        return true;
    }

    public boolean anyFieldHasAnnotation(Class annotationClass) throws Exception {
        Field fields[] = this.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            if (hasAnnotation(fields[i].getName(), annotationClass)) return true;
        }
        return false;
    }

    public String toString() {
        String str = "";
        str += "\n\n--Dump from " + this.getClass().getCanonicalName() + "--\n\n";
        str += "--Declared Properties: \n";
        try {
            List list = Properties.getFullProperties(this);
            for (int i = 0; i < list.size(); i++) {
                PropertyDescriptor prop = (PropertyDescriptor) list.get(i);
                String name = prop.getName();
                Object value = Properties.getPropertyValue(prop, this);
                str += "Property: " + name + " = " + value + "\n";
            }
            str += "\n\n--Field Properties: \n";
            try {
                Field fields[] = this.getClass().getDeclaredFields();
                for (int i = 0; i < fields.length; i++) {
                    if (hasAnnotation(fields[i].getName(), Prop.class)) {
                        String name = fields[i].getName();
                        Field field = this.getClass().getDeclaredField(name);
                        field.setAccessible(true);
                        Object value = field.get(this);
                        str += "Property: " + name + " = " + value + "\n";
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            str += "\n\n--Dynamic Properties: \n";
            Map map = this.dynamicProperties();
            Iterator propNames = map.keySet().iterator();
            while (propNames.hasNext()) {
                String name = (String) propNames.next();
                Object value = map.get(name);
                str += "Property: " + name + " = " + value + "\n";
            }
        } catch (Exception e) {
            str += "Error dumping object data: " + e;
        }
        return str;
    }
}
