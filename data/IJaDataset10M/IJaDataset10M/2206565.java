package com.hyper9.simdk.stubs.dao;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.hyper9.simdk.stubs.ArrayOf;
import com.hyper9.simdk.stubs.JAXBAnnotated;

/**
 * An array of boolean values.
 * 
 * @author akutz
 * 
 */
@XmlType(name = "ArrayOfBoolean")
@JAXBAnnotated
public class ArrayOfBoolean extends ArrayOf<Boolean> {

    private static final long serialVersionUID = -6359754650080436269L;

    @Override
    @XmlElement(name = "boolean", namespace = "http://www.w3.org/2001/XMLSchema")
    public List<Boolean> getData() {
        return super.getData();
    }
}
