package org.openamf;

import java.io.Serializable;

/**
 * AMF Body
 *
 * @author Jason Calabrese <jasonc@missionvi.com>
 * @author Pat Maddox <pergesu@users.sourceforge.net>
 * @see AMFHeader
 * @see AMFMessage
 * @version $Revision: 1.19 $, $Date: 2003/09/20 01:05:24 $
 */
public class AMFBody implements Serializable {

    protected String target;

    protected String serviceName;

    protected String serviceMethodName;

    protected String response;

    protected Object value;

    protected byte type;

    /**
     * Unknow object type
     */
    public static final byte DATA_TYPE_UNKNOWN = -1;

    /**
     * Number object type
     */
    public static final byte DATA_TYPE_NUMBER = 0;

    /**
     * Boolean object type
     */
    public static final byte DATA_TYPE_BOOLEAN = 1;

    /**
     * String object type
     */
    public static final byte DATA_TYPE_STRING = 2;

    /**
     * Object object type
     */
    public static final byte DATA_TYPE_OBJECT = 3;

    /**
     * Movie clip object type
     */
    public static final byte DATA_TYPE_MOVIE_CLIP = 4;

    /**
     * NULL object type
     */
    public static final byte DATA_TYPE_NULL = 5;

    /**
     * Undefined object type
     */
    public static final byte DATA_TYPE_UNDEFINED = 6;

    /**
     * Reference object type
     */
    public static final byte DATA_TYPE_REFERENCE_OBJECT = 7;

    /**
     * Mixed Array Object type
     */
    public static final byte DATA_TYPE_MIXED_ARRAY = 8;

    /**
     * Object end type
     */
    public static final byte DATA_TYPE_OBJECT_END = 9;

    /**
     * Array Object type
     */
    public static final byte DATA_TYPE_ARRAY = 10;

    /**
     * Date object type
     */
    public static final byte DATA_TYPE_DATE = 11;

    /**
     * Long String object type
     */
    public static final byte DATA_TYPE_LONG_STRING = 12;

    /**
     * General Object type
     */
    public static final byte DATA_TYPE_AS_OBJECT = 13;

    /**
     * RecordSet object type
     */
    public static final byte DATA_TYPE_RECORDSET = 14;

    /**
     * XML Document object type
     */
    public static final byte DATA_TYPE_XML = 15;

    /**
     * Custom class object type
     */
    public static final byte DATA_TYPE_CUSTOM_CLASS = 16;

    /**
     * AMF body with unknown type
     *
     * @param target
     * @param response
     * @param value
     */
    public AMFBody(String target, String response, Object value) {
        this(target, response, value, DATA_TYPE_UNKNOWN);
    }

    /**
     * AMF Body constructor
     *
     * @param target
     * @param response
     * @param value
     * @param type
     */
    public AMFBody(String target, String response, Object value, byte type) {
        this.response = response;
        this.value = value;
        this.type = type;
        setTarget(target);
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
        int dotIndex = target.lastIndexOf('.');
        if (dotIndex > 0) {
            this.serviceName = target.substring(0, dotIndex);
            this.serviceMethodName = target.substring(dotIndex + 1);
        }
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getServiceMethodName() {
        return serviceMethodName;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    /**
     * Returns object type
     *
     * @return
     */
    public byte getType() {
        return type;
    }

    /**
     * Sets object type
     *
     * @param type
     */
    public void setType(byte type) {
        this.type = type;
    }

    /**
     * Returns String description of object type
     *
     * @param type object type
     * @return
     */
    public static String getObjectTypeDescription(byte type) {
        switch(type) {
            case DATA_TYPE_UNKNOWN:
                return "UNKNOWN";
            case DATA_TYPE_NUMBER:
                return "NUMBER";
            case DATA_TYPE_BOOLEAN:
                return "BOOLEAN";
            case DATA_TYPE_STRING:
                return "STRING";
            case DATA_TYPE_OBJECT:
                return "OBJECT";
            case DATA_TYPE_MOVIE_CLIP:
                return "MOVIECLIP";
            case DATA_TYPE_NULL:
                return "NULL";
            case DATA_TYPE_UNDEFINED:
                return "UNDEFINED";
            case DATA_TYPE_REFERENCE_OBJECT:
                return "REFERENCE";
            case DATA_TYPE_MIXED_ARRAY:
                return "MIXED_ARRAY";
            case DATA_TYPE_OBJECT_END:
                return "OBJECT_END";
            case DATA_TYPE_ARRAY:
                return "ARRAY";
            case DATA_TYPE_DATE:
                return "DATE";
            case DATA_TYPE_LONG_STRING:
                return "LONG_STRING";
            case DATA_TYPE_AS_OBJECT:
                return "AS_OBJECT";
            case DATA_TYPE_RECORDSET:
                return "RECORDSET";
            case DATA_TYPE_XML:
                return "XML";
            case DATA_TYPE_CUSTOM_CLASS:
                return "CUSTOM_CLASS";
            default:
                return "UNKNOWN: 0x" + Integer.toBinaryString(type);
        }
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("[AMFBody: {serviceName=");
        sb.append(getServiceName());
        sb.append(", serviceMethodName=");
        sb.append(getServiceMethodName());
        sb.append(", response=");
        sb.append(getResponse());
        sb.append(", type=");
        sb.append(getObjectTypeDescription(type));
        sb.append(", value=");
        sb.append(getValue());
        sb.append("}]");
        return sb.toString();
    }
}
