package com.em.validation.client.model.composed;

public class ComposedTestClass {

    @ComposedConstraint
    @ComposedSingleViolationConstraint
    @CyclicalComposedConstraintPart1
    @CyclicalComposedConstraintPart2
    private String composedString = "";

    public String getComposedString() {
        return composedString;
    }

    public void setComposedString(String composedString) {
        this.composedString = composedString;
    }
}
