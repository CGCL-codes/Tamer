package com.jeantessier.classreader.impl;

import java.io.*;
import org.apache.log4j.*;
import com.jeantessier.classreader.*;

public class AnnotationElementValue extends ElementValue implements com.jeantessier.classreader.AnnotationElementValue {

    private Annotation annotation;

    public AnnotationElementValue(ConstantPool constantPool, DataInput in) throws IOException {
        super(constantPool);
        annotation = new Annotation(constantPool, in);
        Logger.getLogger(getClass()).debug("Annotation: " + annotation);
    }

    public Annotation getAnnotation() {
        return annotation;
    }

    public char getTag() {
        return ElementValueType.ANNOTATION.getTag();
    }

    public void accept(Visitor visitor) {
        visitor.visitAnnotationElementValue(this);
    }
}
