package org.jsmg.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Java source class field.
 */
public class Field {

    /**
	 * Name.
	 */
    private String variableName;

    /**
	 * Type.
	 */
    private String fieldType;

    /**
	 * If field is private.
	 */
    private boolean privateModifier;

    /**
	 * If field is protected.
	 */
    private boolean protectedModifier;

    /**
	 * If field is public.
	 */
    private boolean publicModifier;

    /**
	 * If field is static.
	 */
    private boolean staticModifier;

    /**
	 * Annoatations.
	 */
    private List<Annotation> annotations;

    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    public String getVariableName() {
        return variableName;
    }

    public void setType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getFieldType() {
        return fieldType;
    }

    public List<Annotation> getAnnotations() {
        if (annotations == null) {
            annotations = new ArrayList<Annotation>();
        }
        return annotations;
    }

    public boolean isPrivate() {
        return privateModifier;
    }

    public void setPrivate(boolean privateModifier) {
        this.privateModifier = privateModifier;
    }

    public boolean isProtected() {
        return protectedModifier;
    }

    public void setProtected(boolean protectedModifier) {
        this.protectedModifier = protectedModifier;
    }

    public boolean isPublic() {
        return publicModifier;
    }

    public void setPublic(boolean publicModifier) {
        this.publicModifier = publicModifier;
    }

    public boolean isStatic() {
        return staticModifier;
    }

    public void setStatic(boolean staticModifier) {
        this.staticModifier = staticModifier;
    }
}
