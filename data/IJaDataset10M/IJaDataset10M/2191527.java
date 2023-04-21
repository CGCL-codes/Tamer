package com.googlecode.sarasvati.load.properties;

import java.beans.PropertyDescriptor;
import com.googlecode.sarasvati.load.SarasvatiLoadException;

public interface PropertyMutator {

    void setPropertyDescriptor(PropertyDescriptor pd);

    void setTarget(Object target);

    Object getCurrentValue() throws SarasvatiLoadException;

    void setValue(Object value) throws SarasvatiLoadException;

    void setFromText(String text) throws SarasvatiLoadException;
}
