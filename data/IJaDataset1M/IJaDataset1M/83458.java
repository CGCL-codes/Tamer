package com.jmex.xml.types;

import java.net.URI;

public class SchemaAnyURI implements SchemaType {

    protected URI value;

    protected boolean isempty;

    protected boolean isnull;

    public SchemaAnyURI() {
        setEmpty();
    }

    public SchemaAnyURI(SchemaAnyURI newvalue) {
        value = newvalue.value;
        isempty = newvalue.isempty;
        isnull = newvalue.isnull;
    }

    public SchemaAnyURI(URI newvalue) {
        setValue(newvalue);
    }

    public SchemaAnyURI(String newvalue) {
        parse(newvalue);
    }

    public SchemaAnyURI(SchemaType newvalue) {
        assign(newvalue);
    }

    public URI getValue() {
        return value;
    }

    public void setValue(URI newvalue) {
        if (newvalue == null) {
            isempty = true;
            isnull = true;
        } else {
            isnull = false;
            isempty = false;
            value = newvalue;
        }
    }

    public void parse(String s) {
        String newvalue = SchemaNormalizedString.normalize(SchemaNormalizedString.WHITESPACE_COLLAPSE, s);
        if (newvalue == null) setNull(); else if (newvalue.length() == 0) setEmpty(); else {
            setNull();
            try {
                value = URI.create(newvalue);
            } catch (IllegalArgumentException e) {
                throw new StringParseException(e);
            }
            isnull = false;
            isempty = false;
        }
    }

    public void assign(SchemaType newvalue) {
        if (newvalue == null || newvalue.isNull()) setNull(); else if (newvalue.isEmpty()) setEmpty(); else {
            if (newvalue instanceof SchemaString) parse(newvalue.toString()); else if (newvalue instanceof SchemaAnyURI) setValue(((SchemaAnyURI) newvalue).value); else throw new TypesIncompatibleException(newvalue, this);
        }
    }

    public void setNull() {
        isnull = true;
        isempty = true;
        value = null;
    }

    public void setEmpty() {
        parse("http://www.altova.com/language_select.html");
    }

    public int hashCode() {
        return value.hashCode();
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof SchemaAnyURI)) return false;
        return value.equals(((SchemaAnyURI) obj).value);
    }

    public Object clone() {
        return new SchemaAnyURI(this);
    }

    public String toString() {
        if (isempty || isnull || value == null) return "";
        return value.toString();
    }

    public int length() {
        return value.toString().length();
    }

    public boolean booleanValue() {
        return (value != null && value.toString().length() != 0);
    }

    public boolean isEmpty() {
        return isempty;
    }

    public boolean isNull() {
        return isnull;
    }

    public int compareTo(Object obj) {
        return compareTo((SchemaAnyURI) obj);
    }

    public int compareTo(SchemaAnyURI obj) {
        return toString().compareTo(obj.toString());
    }
}
