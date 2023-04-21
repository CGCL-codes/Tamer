package com.iver.utiles;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Vector;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.ValidationException;
import com.iver.utiles.xmlEntity.generate.Property;
import com.iver.utiles.xmlEntity.generate.XmlTag;

/**
 * Adaptador de las llamadas a sus m�todos sobre los generados por castor para
 * generar despu�s los XML.
 *
 * @author Vicente Caballero Navarro
 */
public class XMLEntity {

    private Vector whiteAttrList;

    private XmlTag xmltag;

    /**
	 * Crea un nuevo XMLEntity.
	 *
	 * @param tag
	 *            DOCUMENT ME!
	 */
    public XMLEntity(XmlTag tag) {
        xmltag = tag;
    }

    /**
	 * Crea un nuevo XMLEntity.
	 */
    public XMLEntity() {
        xmltag = new XmlTag();
    }

    /**
	 * A�ade una propiedad con un String como clave y un String como valor.
	 *
	 * @param key
	 *            clave.
	 * @param value
	 *            valor.
	 * @param matters, if false this property will not take effect to the
	 * result of toHashCode() method
	 * @see toHashCode()
	 */
    public void putProperty(String key, String value, boolean matters) {
        if ((key == null)) {
            return;
        }
        Property p = new Property();
        p.setKey(key);
        p.setValue(value);
        putProperty(p, matters);
    }

    public void putProperty(String key, String value) {
        putProperty(key, value, true);
    }

    /**
	 * A�ade una propiedad con un String como clave y un Object como valor.
	 *
	 * @param key
	 *            clave.
	 * @param value
	 *            valor.
	 * @param matters, if false this property will not take effect to the
	 * result of toHashCode() method
	 * @see toHashCode()
	 */
    public void putProperty(String key, Object value, boolean matters) {
        String ret = "";
        if (key == null) {
            return;
        }
        Class valueClass = value.getClass();
        if (valueClass.isArray()) {
            Class compType = valueClass.getComponentType();
            if (compType == byte.class) {
                byte[] array = (byte[]) value;
                if (!(array.length == 0)) {
                    ret += ("" + array[0]).replaceAll("[,]", "\\\\,");
                    for (int i = 1; i < array.length; i++) {
                        ret += (" ," + ("" + array[i]).replaceAll("[,]", "\\\\,"));
                    }
                }
            } else if (compType == short.class) {
                short[] array = (short[]) value;
                if (!(array.length == 0)) {
                    ret += ("" + array[0]).replaceAll("[,]", "\\\\,");
                    for (int i = 1; i < array.length; i++) {
                        ret += (" ," + ("" + array[i]).replaceAll("[,]", "\\\\,"));
                    }
                }
            } else if (compType == int.class) {
                int[] array = (int[]) value;
                if (!(array.length == 0)) {
                    ret += ("" + array[0]).replaceAll("[,]", "\\\\,");
                    for (int i = 1; i < array.length; i++) {
                        ret += (" ," + ("" + array[i]).replaceAll("[,]", "\\\\,"));
                    }
                }
            } else if (compType == long.class) {
                long[] array = (long[]) value;
                if (!(array.length == 0)) {
                    ret += ("" + array[0]).replaceAll("[,]", "\\\\,");
                    for (int i = 1; i < array.length; i++) {
                        ret += (" ," + ("" + array[i]).replaceAll("[,]", "\\\\,"));
                    }
                }
            } else if (compType == float.class) {
                float[] array = (float[]) value;
                if (!(array.length == 0)) {
                    ret += ("" + array[0]).replaceAll("[,]", "\\\\,");
                    for (int i = 1; i < array.length; i++) {
                        ret += (" ," + ("" + array[i]).replaceAll("[,]", "\\\\,"));
                    }
                }
            } else if (compType == double.class) {
                double[] array = (double[]) value;
                if (!(array.length == 0)) {
                    ret += ("" + array[0]).replaceAll("[,]", "\\\\,");
                    for (int i = 1; i < array.length; i++) {
                        ret += (" ," + ("" + array[i]).replaceAll("[,]", "\\\\,"));
                    }
                }
            } else if (compType == boolean.class) {
                boolean[] array = (boolean[]) value;
                if (!(array.length == 0)) {
                    ret += ("" + array[0]).replaceAll("[,]", "\\\\,");
                    for (int i = 1; i < array.length; i++) {
                        ret += (" ," + ("" + array[i]).replaceAll("[,]", "\\\\,"));
                    }
                }
            } else if (compType == String.class) {
                String[] array = (String[]) value;
                if (!(array.length == 0)) {
                    if (array[0] == null) {
                        array[0] = "null";
                    }
                    ret += array[0].replaceAll("[,]", "\\\\,");
                    for (int i = 1; i < array.length; i++) {
                        if (array[i] == null) {
                            array[i] = "null";
                        }
                        array[i] = array[i].replaceAll("[,]", "\\\\,");
                        ret += (" ," + array[i]);
                    }
                }
            } else {
                Object[] array = (Object[]) value;
                if (!(array.length == 0)) {
                    ret += ("" + array[0]).replaceAll("[,]", "\\\\,");
                    for (int i = 1; i < array.length; i++) {
                        array[i] = ("" + array[i]).replaceAll("[,]", "\\\\,");
                        ret += (" ," + array[i]);
                    }
                }
            }
        } else {
            ret = value.toString();
        }
        Property p = new Property();
        p.setKey(key);
        p.setValue(ret);
        putProperty(p, matters);
    }

    /**
	 * A�ade una propiedad con un String como clave y un double como valor.
	 * La propiedad a�adida afecta al valor calculado por toHashCode()
	 *
	 * @param key
	 *            clave.
	 * @param value
	 *            valor.
	 * @see toHashCode()
	 */
    public void putProperty(String key, Object value) {
        putProperty(key, value, true);
    }

    /**
	 * A�ade una propiedad con un String como clave y un entero como valor.
	 *
	 * @param key
	 *            clave.
	 * @param value
	 *            valor.
	 * @param matters, if false this property will not take effect to the
	 * result of toHashCode() method
	 * @see toHashCode()
	 */
    public void putProperty(String key, int value, boolean matters) {
        if (key == null) {
            return;
        }
        Property p = new Property();
        p.setKey(key);
        p.setValue(new Integer(value).toString());
        putProperty(p, matters);
    }

    /**
	 * A�ade una propiedad con un String como clave y un double como valor.
	 * La propiedad a�adida afecta al valor calculado por toHashCode()
	 *
	 * @param key
	 *            clave.
	 * @param value
	 *            valor.
	 * @see toHashCode()
	 */
    public void putProperty(String key, int value) {
        putProperty(key, value, true);
    }

    /**
	 * A�ade una propiedad con un String como clave y un long como valor.
	 *
	 * @param key
	 *            clave.
	 * @param value
	 *            valor.
	 * @param matters, if false this property will not take effect to the
	 * result of toHashCode() method
	 * @see toHashCode()
	 */
    public void putProperty(String key, long value, boolean matters) {
        if (key == null) {
            return;
        }
        Property p = new Property();
        p.setKey(key);
        p.setValue(new Long(value).toString());
        putProperty(p, matters);
    }

    /**
	 * A�ade una propiedad con un String como clave y un double como valor.
	 * La propiedad a�adida afecta al valor calculado por toHashCode()
	 *
	 * @param key
	 *            clave.
	 * @param value
	 *            valor.
	 * @see toHashCode()
	 */
    public void putProperty(String key, long value) {
        putProperty(key, value, true);
    }

    /**
	 * A�ade una propiedad con un String como clave y un boolean como valor.
	 *
	 * @param key
	 *            clave.
	 * @param value
	 *            valor.
	 * @param matters, if false this property will not take effect to the
	 * result of toHashCode() method
	 * @see toHashCode()
	 */
    public void putProperty(String key, boolean value, boolean matters) {
        if (key == null) {
            return;
        }
        Property p = new Property();
        p.setKey(key);
        p.setValue(new Boolean(value).toString());
        putProperty(p, matters);
    }

    /**
	 * A�ade una propiedad con un String como clave y un double como valor.
	 * La propiedad a�adida afecta al valor calculado por toHashCode()
	 *
	 * @param key
	 *            clave.
	 * @param value
	 *            valor.
	 * @see toHashCode()
	 */
    public void putProperty(String key, boolean value) {
        putProperty(key, value, true);
    }

    /**
	 * A�ade una propiedad con un String como clave y un float como valor.
	 *
	 * @param key
	 *            clave.
	 * @param value
	 *            valor.
	 * @param matters, if false this property will not take effect to the
	 * result of toHashCode() method
	 * @see toHashCode()
	 */
    public void putProperty(String key, float value, boolean matters) {
        if (key == null) {
            return;
        }
        Property p = new Property();
        p.setKey(key);
        p.setValue(new Float(value).toString());
        putProperty(p, matters);
    }

    /**
	 * A�ade una propiedad con un String como clave y un double como valor.
	 * La propiedad a�adida afecta al valor calculado por toHashCode()
	 *
	 * @param key
	 *            clave.
	 * @param value
	 *            valor.
	 * @see toHashCode()
	 */
    public void putProperty(String key, float value) {
        putProperty(key, value, true);
    }

    /**
	 * A�ade una propiedad con un String como clave y un double como valor.
	 *
	 * @param key
	 *            clave.
	 * @param value
	 *            valor.
	 * @param matters, if false this property will not take effect to the
	 * result of toHashCode() method
	 * @see toHashCode()
	 */
    public void putProperty(String key, double value, boolean matters) {
        if (key == null) {
            return;
        }
        Property p = new Property();
        p.setKey(key);
        p.setValue(new Double(value).toString());
        putProperty(p, matters);
    }

    /**
	 * A�ade una propiedad con un String como clave y un double como valor.
	 * La propiedad a�adida afecta al valor calculado por toHashCode()
	 *
	 * @param key
	 *            clave.
	 * @param value
	 *            valor.
	 * @see toHashCode()
	 */
    public void putProperty(String key, double value) {
        putProperty(key, value, true);
    }

    /**
	 * Devuelve el String que corresponda a la clave que se pasa como par�metro.
	 *
	 * @param key
	 *            clave
	 *
	 * @return valor.
	 *
	 * @throws NotExistInXMLEntity
	 *             Lanza esta excepci�n si no se encuentra ning�n elemento con
	 *             esa clave.
	 */
    public String getStringProperty(String key) {
        Property[] properties = xmltag.getProperty();
        String res = null;
        boolean exists = false;
        for (int i = 0; i < properties.length; i++) {
            if (properties[i].getKey().compareTo(key) == 0) {
                res = properties[i].getValue();
                exists = true;
            }
        }
        if (exists) {
            return res;
        }
        throw new NotExistInXMLEntity();
    }

    /**
	 * Devuelve el double que corresponda a la clave que se pasa como par�metro.
	 *
	 * @param key
	 *            clave
	 *
	 * @return valor.
	 *
	 * @throws NotExistInXMLEntity
	 *             Lanza esta excepci�n si no se encuentra ning�n elemento con
	 *             esa clave.
	 */
    public double getDoubleProperty(String key) {
        Property[] properties = xmltag.getProperty();
        double res = 0;
        boolean exists = false;
        for (int i = 0; i < properties.length; i++) {
            if (properties[i].getKey().compareTo(key) == 0) {
                res = Double.parseDouble(properties[i].getValue());
                exists = true;
            }
        }
        if (exists) {
            return res;
        }
        throw new NotExistInXMLEntity();
    }

    /**
	 * Devuelve el array de doubles que corresponda a la clave que se pasa como
	 * par�metro.
	 *
	 * @param key
	 *            clave
	 *
	 * @return valor.
	 *
	 * @throws NotExistInXMLEntity
	 *             Lanza esta excepci�n si no se encuentra ning�n elemento con
	 *             esa clave.
	 */
    public double[] getDoubleArrayProperty(String key) {
        Property[] properties = xmltag.getProperty();
        String value = null;
        boolean exists = false;
        for (int i = 0; i < properties.length; i++) {
            if (properties[i].getKey().compareTo(key) == 0) {
                value = properties[i].getValue();
                exists = true;
            }
        }
        if (!exists) {
            throw new NotExistInXMLEntity();
        }
        if (value.compareTo("") == 0) {
            return new double[0];
        }
        String[] aux = (String[]) value.split(" ,");
        double[] ret = new double[aux.length];
        for (int i = 0; i < aux.length; i++) {
            ret[i] = Double.parseDouble(aux[i].replaceAll("\\\\,", ","));
        }
        return ret;
    }

    /**
	 * Devuelve el Object que corresponda a la clave que se pasa como par�metro.
	 *
	 * @param key
	 *            clave
	 *
	 * @return valor.
	 *
	 * @throws NotExistInXMLEntity
	 *             Lanza esta excepci�n si no se encuentra ning�n elemento con
	 *             esa clave.
	 */
    public Object getObjectProperty(String key) {
        Property[] properties = xmltag.getProperty();
        Object res = null;
        boolean exists = false;
        for (int i = 0; i < properties.length; i++) {
            if (properties[i].getKey().compareTo(key) == 0) {
                res = properties[i].getValue();
                exists = true;
            }
        }
        if (exists) {
            return res;
        }
        throw new NotExistInXMLEntity();
    }

    /**
	 * Devuelve el array de float que corresponda a la clave que se pasa como
	 * par�metro.
	 *
	 * @param key
	 *            clave
	 *
	 * @return valor.
	 *
	 * @throws NotExistInXMLEntity
	 *             Lanza esta excepci�n si no se encuentra ning�n elemento con
	 *             esa clave.
	 */
    public float[] getFloatArrayProperty(String key) {
        Property[] properties = xmltag.getProperty();
        String value = null;
        boolean exists = false;
        for (int i = 0; i < properties.length; i++) {
            if (properties[i].getKey().compareTo(key) == 0) {
                value = properties[i].getValue();
                exists = true;
            }
        }
        if (!exists) {
            throw new NotExistInXMLEntity();
        }
        if (value.compareTo("") == 0) {
            return new float[0];
        }
        String[] aux = (String[]) value.split(" ,");
        float[] ret = new float[aux.length];
        for (int i = 0; i < aux.length; i++) {
            ret[i] = Float.parseFloat(aux[i].replaceAll("\\\\,", ","));
        }
        return ret;
    }

    /**
	 * Devuelve el array de long que corresponda a la clave que se pasa como
	 * par�metro.
	 *
	 * @param key
	 *            clave
	 *
	 * @return valor.
	 *
	 * @throws NotExistInXMLEntity
	 *             Lanza esta excepci�n si no se encuentra ning�n elemento con
	 *             esa clave.
	 */
    public long[] getLongArrayProperty(String key) {
        Property[] properties = xmltag.getProperty();
        String value = null;
        boolean exists = false;
        for (int i = 0; i < properties.length; i++) {
            if (properties[i].getKey().compareTo(key) == 0) {
                value = properties[i].getValue();
                exists = true;
            }
        }
        if (!exists) {
            throw new NotExistInXMLEntity();
        }
        if (value.compareTo("") == 0) {
            return new long[0];
        }
        String[] aux = (String[]) value.split(" ,");
        long[] ret = new long[aux.length];
        for (int i = 0; i < aux.length; i++) {
            ret[i] = Long.parseLong(aux[i].replaceAll("\\\\,", ","));
        }
        return ret;
    }

    /**
	 * Devuelve el array de bytes que corresponda a la clave que se pasa como
	 * par�metro.
	 *
	 * @param key
	 *            clave
	 *
	 * @return valor.
	 *
	 * @throws NotExistInXMLEntity
	 *             Lanza esta excepci�n si no se encuentra ning�n elemento con
	 *             esa clave.
	 */
    public byte[] getByteArrayProperty(String key) {
        Property[] properties = xmltag.getProperty();
        String value = null;
        boolean exists = false;
        for (int i = 0; i < properties.length; i++) {
            if (properties[i].getKey().compareTo(key) == 0) {
                value = properties[i].getValue();
                exists = true;
            }
        }
        if (!exists) {
            throw new NotExistInXMLEntity();
        }
        if (value.compareTo("") == 0) {
            return new byte[0];
        }
        String[] aux = (String[]) value.split(" ,");
        byte[] ret = new byte[aux.length];
        for (int i = 0; i < aux.length; i++) {
            ret[i] = Byte.parseByte(aux[i].replaceAll("\\\\,", ","));
        }
        return ret;
    }

    /**
	 * Devuelve el array de enteros que corresponda a la clave que se pasa como
	 * par�metro.
	 *
	 * @param key
	 *            clave
	 *
	 * @return valor.
	 *
	 * @throws NotExistInXMLEntity
	 *             Lanza esta excepci�n si no se encuentra ning�n elemento con
	 *             esa clave.
	 */
    public int[] getIntArrayProperty(String key) {
        Property[] properties = xmltag.getProperty();
        String value = null;
        boolean exists = false;
        for (int i = 0; i < properties.length; i++) {
            if (properties[i].getKey().compareTo(key) == 0) {
                value = properties[i].getValue();
                exists = true;
            }
        }
        if (!exists) {
            throw new NotExistInXMLEntity();
        }
        if (value.compareTo("") == 0) {
            return new int[0];
        }
        String[] aux = (String[]) value.split(" ,");
        int[] ret = new int[aux.length];
        for (int i = 0; i < aux.length; i++) {
            ret[i] = Integer.parseInt(aux[i].replaceAll("\\\\,", ","));
        }
        return ret;
    }

    /**
	 * Devuelve el array de boolean que corresponda a la clave que se pasa como
	 * par�metro.
	 *
	 * @param key
	 *            clave
	 *
	 * @return valor.
	 *
	 * @throws NotExistInXMLEntity
	 *             Lanza esta excepci�n si no se encuentra ning�n elemento con
	 *             esa clave.
	 */
    public boolean[] getBooleanArrayProperty(String key) {
        Property[] properties = xmltag.getProperty();
        String value = null;
        boolean exists = false;
        for (int i = 0; i < properties.length; i++) {
            if (properties[i].getKey().compareTo(key) == 0) {
                value = properties[i].getValue();
                exists = true;
            }
        }
        if (!exists) {
            throw new NotExistInXMLEntity();
        }
        if (value.compareTo("") == 0) {
            return new boolean[0];
        }
        String[] aux = (String[]) value.split(" ,");
        boolean[] ret = new boolean[aux.length];
        for (int i = 0; i < aux.length; i++) {
            ret[i] = Boolean.valueOf(aux[i].replaceAll("\\\\,", ",")).booleanValue();
        }
        return ret;
    }

    /**
	 * Devuelve el array de String que corresponda a la clave que se pasa como
	 * par�metro.
	 *
	 * @param key
	 *            clave
	 *
	 * @return valor.
	 *
	 * @throws NotExistInXMLEntity
	 *             Lanza esta excepci�n si no se encuentra ning�n elemento con
	 *             esa clave.
	 */
    public String[] getStringArrayProperty(String key) {
        Property[] properties = xmltag.getProperty();
        String value = null;
        boolean exists = false;
        for (int i = 0; i < properties.length; i++) {
            if (properties[i].getKey().compareTo(key) == 0) {
                value = properties[i].getValue();
                exists = true;
            }
        }
        if (!exists) {
            throw new NotExistInXMLEntity();
        }
        if (value.compareTo("") == 0) {
            return new String[0];
        }
        String[] aux = (String[]) value.split(" ,");
        for (int i = 0; i < aux.length; i++) {
            aux[i] = aux[i].replaceAll("\\\\,", ",");
        }
        return aux;
    }

    /**
	 * Devuelve el boolean que corresponda a la clave que se pasa como
	 * par�metro.
	 *
	 * @param key
	 *            clave
	 *
	 * @return valor.
	 *
	 * @throws NotExistInXMLEntity
	 *             Lanza esta excepci�n si no se encuentra ning�n elemento con
	 *             esa clave.
	 */
    public boolean getBooleanProperty(String key) {
        Property[] properties = xmltag.getProperty();
        boolean res = false;
        boolean exists = false;
        for (int i = 0; i < properties.length; i++) {
            if (properties[i].getKey().compareTo(key) == 0) {
                res = (boolean) Boolean.valueOf(properties[i].getValue()).booleanValue();
                exists = true;
            }
        }
        if (exists) {
            return res;
        }
        throw new NotExistInXMLEntity();
    }

    /**
	 * Devuelve el entero que corresponda a la clave que se pasa como par�metro.
	 *
	 * @param key
	 *            clave
	 *
	 * @return valor.
	 *
	 * @throws NotExistInXMLEntity
	 *             Lanza esta excepci�n si no se encuentra ning�n elemento con
	 *             esa clave.
	 */
    public int getIntProperty(String key) {
        Property[] properties = xmltag.getProperty();
        int res = 0;
        boolean exists = false;
        for (int i = 0; i < properties.length; i++) {
            if (properties[i].getKey().compareTo(key) == 0) {
                res = Integer.parseInt(properties[i].getValue());
                exists = true;
            }
        }
        if (exists) {
            return res;
        }
        throw new NotExistInXMLEntity();
    }

    /**
	 * Devuelve el long que corresponda a la clave que se pasa como par�metro.
	 *
	 * @param key
	 *            clave
	 *
	 * @return valor.
	 *
	 * @throws NotExistInXMLEntity
	 *             Lanza esta excepci�n si no se encuentra ning�n elemento con
	 *             esa clave.
	 */
    public long getLongProperty(String key) {
        Property[] properties = xmltag.getProperty();
        long res = 0;
        boolean exists = false;
        for (int i = 0; i < properties.length; i++) {
            if (properties[i].getKey().compareTo(key) == 0) {
                res = Long.valueOf(properties[i].getValue()).longValue();
                exists = true;
            }
        }
        if (exists) {
            return res;
        }
        throw new NotExistInXMLEntity();
    }

    /**
	 * Devuelve el float que corresponda a la clave que se pasa como par�metro.
	 *
	 * @param key
	 *            clave
	 *
	 * @return valor.
	 *
	 * @throws NotExistInXMLEntity
	 *             Lanza esta excepci�n si no se encuentra ning�n elemento con
	 *             esa clave.
	 */
    public float getFloatProperty(String key) {
        Property[] properties = xmltag.getProperty();
        float res = 0;
        boolean exists = false;
        for (int i = 0; i < properties.length; i++) {
            if (properties[i].getKey().compareTo(key) == 0) {
                res = Float.valueOf(properties[i].getValue()).floatValue();
                exists = true;
            }
        }
        if (exists) {
            return res;
        }
        throw new NotExistInXMLEntity();
    }

    /**
	 * A�ade el nombre con clave name y valor el String que se pasa como
	 * par�metro.
	 *
	 * @param name
	 *            nombre.
	 */
    public void setName(String name) {
        Property p = new Property();
        p.setKey("name");
        p.setValue(name);
        xmltag.addProperty(p);
    }

    /**
	 * Devuelve el nombre.
	 *
	 * @return nombre.
	 */
    public String getName() {
        Property p = getProperty("name");
        return (String) p.getValue();
    }

    /**
	 * Devuelve la clase que implementa.
	 *
	 * @return clase.
	 */
    public Class getImplementingClass() {
        Property p = getProperty("class");
        Object ob = p.getValue();
        return (Class) ob;
    }

    /**
	 * A�ade un hijo al XMLEntity.
	 *
	 * @param entity
	 *            xml para a�adir.
	 */
    public void addChild(XMLEntity entity) {
        xmltag.addXmlTag(entity.getXmlTag());
    }

    /**
	 * Devuelve un hijo a partir de un indice.
	 *
	 * @param i
	 *            indice.
	 *
	 * @return hijo.
	 */
    public XMLEntity getChild(int i) {
        return new XMLEntity(xmltag.getXmlTag(i));
    }

    /**
	 * Devuelve el n�mero de hijos que contiene el XMLEntity.
	 *
	 * @return n�mero de hijos.
	 */
    public int getChildrenCount() {
        return xmltag.getXmlTagCount();
    }

    /**
	 * Devuelve el Property a partir de la clave.
	 *
	 * @param key
	 *            clave.
	 *
	 * @return Property.
	 */
    private Property getProperty(String key) {
        Property[] ps = xmltag.getProperty();
        for (int i = 0; i < ps.length; i++) {
            if (ps[i].getKey().compareTo(key) == 0) {
                return ps[i];
            }
        }
        return null;
    }

    /**
	 * Devuelve el xmltag.
	 *
	 * @return xmltag.
	 */
    public XmlTag getXmlTag() {
        return xmltag;
    }

    /**
	 * A�ade el property que se pasa como par�metro.
	 *
	 * @param p
	 *            property.
	 */
    private void putProperty(Property p, boolean matters) {
        if (!matters) {
            getWhiteAttrList().add(p.getKey());
        }
        for (int i = 0; i < xmltag.getPropertyCount(); i++) {
            if (xmltag.getProperty(i).getKey().compareTo(p.getKey()) == 0) {
                xmltag.getProperty(i).setValue(p.getValue());
                return;
            }
        }
        xmltag.addProperty(p);
    }

    private Vector getWhiteAttrList() {
        if (whiteAttrList == null) {
            whiteAttrList = new Vector();
        }
        return whiteAttrList;
    }

    public boolean contains(String key) {
        Property[] properties = xmltag.getProperty();
        boolean exists = false;
        for (int i = 0; i < properties.length; i++) {
            if (properties[i].getKey().compareTo(key) == 0) {
                exists = true;
            }
        }
        return exists;
    }

    public int getPropertyCount() {
        return xmltag.getPropertyCount();
    }

    public String getPropertyValue(int index) {
        return xmltag.getProperty(index).getValue();
    }

    public String getPropertyName(int index) {
        return xmltag.getProperty(index).getKey();
    }

    /**
	 * Removes a property of this XML-Entity.
	 *
	 * @param p
	 *            property.
	 */
    public void remove(String key) {
        Property[] properties = xmltag.getProperty();
        for (int i = 0; i < properties.length; i++) {
            if (properties[i].getKey().compareTo(key) == 0) {
                Property[] newProperties = new Property[properties.length - 1];
                int k = 0;
                for (int j = 0; j < newProperties.length; j++) {
                    if (j == i) k = 1;
                    newProperties[j] = properties[j + k];
                }
                xmltag.setProperty(newProperties);
                return;
            }
        }
    }

    /**
	 * Elimina el hijo n del XMLEntity.
	 *
	 * @param indice
	 *            del hijo a eliminar.
	 */
    public void removeChild(int index) {
        xmltag.removeXmlTag(index);
    }

    /**
	 * Elimina todos los hijos de XMLEntity.
	 *
	 */
    public void removeAllChildren() {
        xmltag.removeAllXmlTag();
    }

    public boolean equals(Object obj) {
        if (obj instanceof XMLEntity) {
            XMLEntity other = (XMLEntity) obj;
            Property[] thisProperties;
            Property[] otherProperties;
            thisProperties = xmltag.getProperty();
            otherProperties = other.xmltag.getProperty();
            if (thisProperties.length != otherProperties.length) return false;
            for (int i = 0; i < thisProperties.length; i++) {
                if (!thisProperties[i].getKey().equals(otherProperties[i].getKey())) return false;
                if (!thisProperties[i].getValue().equals(otherProperties[i].getValue())) return false;
            }
            if (this.getChildrenCount() != other.getChildrenCount()) return false;
            for (int i = 0; i < this.getChildrenCount(); i++) {
                if (!this.getChild(i).equals(other.getChild(i))) return false;
            }
            return true;
        }
        return false;
    }

    /**
	 * <p>
	 * <code>hash()</code> method is used to know if this <b>XMLEntity</b>
	 * can be considered as changed. At the loading of the <b>XMLEntity</b> tree
	 * you can invoke this method. The result of it is a <code>int</code> hash
	 * value calculated from the contents of this and its children. Then, if you
	 * store this value you can recognize if the <b>XMLEntity</b> has changed
	 * since the last time you called <code>hash()</code> just by comparing
	 * the previous calculated value to the new calculated one.<br>
	 * </p>
	 * <p>
	 * Doing so, you can automatically detect if your persistent data has changed
	 * and you are required to save it.<br>
	 * </p>
	 * <p>
	 * <b>Notice</b> that you can mark the properties that you are using during
	 * the session but you don't care about the values between sessions as
	 * properties which don't matter to detect if your XMLEntity has changed.<br>
	 * </p>
	 *
	 */
    public long hash() {
        long result = 17;
        if (xmltag.getName() != null) {
            char[] name = xmltag.getName().toCharArray();
            for (int i = 0; i < name.length; i++) {
                result = 37 + result + (int) name[i];
            }
        }
        Property[] properties = xmltag.getProperty();
        for (int i = 0; i < properties.length; i++) {
            String strKey = properties[i].getKey();
            if (whiteAttrList != null && whiteAttrList.contains(strKey)) continue;
            char[] key = strKey.toCharArray();
            for (int j = 0; j < key.length; j++) {
                result = 37 + result + (int) key[j];
            }
            if (properties[i] != null) {
                String value = properties[i].getValue();
                char[] chars = (value != null) ? value.toCharArray() : new char[0];
                for (int j = 0; j < chars.length; j++) {
                    result = 37 + result + (int) chars[j];
                }
            } else {
                result += 37;
            }
        }
        for (int i = 0; i < this.getChildrenCount(); i++) {
            result = 37 + result + this.getChild(i).hash();
        }
        return result;
    }

    /**
	 * Devuelve el primer hijo que el valor de su propieda 'key'
	 * es igual a 'value'
	 *
	 * @param key propiedad a comparar
	 * @param value valor a comparar
	 * @return XMLEntity hijo o null si no se encuentra
	 */
    public XMLEntity firstChild(String key, String value) {
        int num = this.getChildrenCount();
        XMLEntity child;
        for (int i = 0; i < num; i++) {
            child = this.getChild(i);
            try {
                if (child.getStringProperty(key).equals(value)) {
                    return child;
                }
            } catch (NotExistInXMLEntity e) {
            }
        }
        return null;
    }

    /**
	 * Devuelve el indice del primer hijo que el valor de su propieda 'key'
	 * es igual a 'value'
	 *
	 * @param key propiedad a comparar
	 * @param value valor a comparar
	 * @return int indice del hijo o -1 si no se encuentra
	 */
    public int firstIndexOfChild(String key, String value) {
        int num = this.getChildrenCount();
        XMLEntity child;
        for (int i = 0; i < num; i++) {
            try {
                child = this.getChild(i);
                if (child.getStringProperty(key).equals(value)) {
                    return i;
                }
            } catch (NotExistInXMLEntity e) {
            }
        }
        return -1;
    }

    /**
	 * Devuelve un iterador sobre los hijos que cumplen la condicion
	 * que el valor de su propiedad 'key' es igual a 'value'
	 *
	 * El iterador no permite eliminacion
	 *
	 * @param key nombre de la propidedad
	 * @param value valor de la propiedad
	 * @return
	 */
    public Iterator findChildren(String key, String value) {
        return new XMLEntityIterator(this, key, value);
    }

    public String toString() {
        StringWriter buffer = new StringWriter();
        Marshaller m;
        try {
            m = new Marshaller(buffer);
        } catch (IOException e4) {
            e4.printStackTrace();
            return null;
        }
        m.setEncoding("ISO-8859-1");
        try {
            m.marshal(this.getXmlTag());
        } catch (MarshalException e2) {
            e2.printStackTrace();
            return null;
        } catch (ValidationException e3) {
            e3.printStackTrace();
            return null;
        }
        return buffer.toString();
    }

    public static XMLEntity parse(String data) throws MarshalException, ValidationException {
        StringReader reader = new StringReader(data);
        XmlTag tag;
        tag = (XmlTag) XmlTag.unmarshal(reader);
        return new XMLEntity(tag);
    }
}

class XMLEntityIterator implements Iterator {

    private int lastIndex;

    private int lastHasNextIndex;

    private XMLEntity entity;

    private String key;

    private String value;

    public XMLEntityIterator(XMLEntity entity, String key, String value) {
        this.entity = entity;
        this.key = key;
        this.value = value;
        this.lastIndex = -1;
        this.lastHasNextIndex = -1;
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }

    public boolean hasNext() {
        if (entity.getChildrenCount() == 0 || entity.getChildrenCount() <= this.lastIndex) {
            return false;
        }
        int num = this.entity.getChildrenCount();
        XMLEntity child;
        for (int i = this.lastIndex + 1; i < num; i++) {
            child = this.entity.getChild(i);
            try {
                if (child.getStringProperty(key).equals(value)) {
                    this.lastHasNextIndex = i;
                    return true;
                }
            } catch (NotExistInXMLEntity e) {
            }
        }
        return false;
    }

    public Object next() {
        if (entity.getChildrenCount() == 0 || entity.getChildrenCount() <= this.lastIndex) {
            throw new NoSuchElementException();
        }
        XMLEntity child;
        if (this.lastHasNextIndex > -1) {
            if (this.entity.getChildrenCount() > this.lastHasNextIndex) {
                child = this.entity.getChild(this.lastHasNextIndex);
                try {
                    if (child.getStringProperty(key).equals(value)) {
                        this.lastIndex = this.lastHasNextIndex;
                        this.lastHasNextIndex = -1;
                        return child;
                    }
                } catch (NotExistInXMLEntity e) {
                }
            }
        }
        int num = this.entity.getChildrenCount();
        for (int i = this.lastIndex + 1; i < num; i++) {
            child = this.entity.getChild(i);
            try {
                if (child.getStringProperty(key).equals(value)) {
                    this.lastIndex = i;
                    this.lastHasNextIndex = -1;
                    return child;
                }
            } catch (NotExistInXMLEntity e) {
            }
        }
        this.lastHasNextIndex = -1;
        throw new NoSuchElementException();
    }
}
