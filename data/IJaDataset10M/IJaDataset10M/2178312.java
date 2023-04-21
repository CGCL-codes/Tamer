package com.jeantessier.classreader.impl;

import java.io.*;
import org.apache.log4j.*;
import com.jeantessier.classreader.*;

public class AnnotationDefault_attribute extends Attribute_info implements com.jeantessier.classreader.AnnotationDefault_attribute {

    private ElementValue elementValue;

    public AnnotationDefault_attribute(ConstantPool constantPool, Visitable owner, DataInput in) throws IOException {
        this(constantPool, owner, in, new ElementValueFactory());
    }

    public AnnotationDefault_attribute(ConstantPool constantPool, Visitable owner, DataInput in, ElementValueFactory elementValueFactory) throws IOException {
        super(constantPool, owner);
        int byteCount = in.readInt();
        Logger.getLogger(getClass()).debug("Attribute length: " + byteCount);
        elementValue = elementValueFactory.create(constantPool, in);
    }

    public ElementValue getElemementValue() {
        return elementValue;
    }

    public String getAttributeName() {
        return AttributeType.ANNOTATION_DEFAULT.getAttributeName();
    }

    public void accept(Visitor visitor) {
        visitor.visitAnnotationDefault_attribute(this);
    }
}
