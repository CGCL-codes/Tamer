package com.ee.bruscar.configuration.hint;

public class FieldHint implements Hint {

    private String name;

    private String id;

    private String ref;

    private ModelHint modelHint;

    public ModelHint getModelHint() {
        return modelHint;
    }

    public void setModelHint(ModelHint modelHint) {
        this.modelHint = modelHint;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    private String hintClass;

    private String value;

    private String modelRef;

    public String getModelRef() {
        return modelRef;
    }

    public void setModelRef(String modelRef) {
        this.modelRef = modelRef;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getHintClass() {
        return hintClass;
    }

    public void setHintClass(String hintClass) {
        this.hintClass = hintClass;
    }
}
