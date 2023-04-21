package storm.definition.field.annotation;

import storm.definition.field.annotation.option.OptionAnnotationReader;
import storm.definition.field.annotation.type.TypeAnnotationReader;
import storm.description.field.FieldDescription;

public abstract class AnnotationReader {

    TypeAnnotationReader typeAnnotationReader;

    OptionAnnotationReader optionAnnotationReader;

    public abstract void readAnnotations(FieldDescription field, Class parent) throws Throwable;

    public TypeAnnotationReader getTypeAnnotationReader() {
        return typeAnnotationReader;
    }

    public void setTypeAnnotationReader(TypeAnnotationReader typeAnnotationReader) {
        this.typeAnnotationReader = typeAnnotationReader;
    }

    public OptionAnnotationReader getOptionAnnotationReader() {
        return optionAnnotationReader;
    }

    public void setOptionAnnotationReader(OptionAnnotationReader optionAnnotationReader) {
        this.optionAnnotationReader = optionAnnotationReader;
    }
}
