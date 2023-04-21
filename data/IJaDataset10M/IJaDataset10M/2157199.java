package org.nakedobjects.nof.reflect.java.reflect;

public class DescriptiveMethods {

    private final MemberHelper nameMethod;

    private final MemberHelper descriptionMethod;

    public DescriptiveMethods(MemberHelper nameMethod, MemberHelper descriptionMethod) {
        this.nameMethod = nameMethod;
        this.descriptionMethod = descriptionMethod;
    }

    public final MemberHelper getDescriptionMethod() {
        return descriptionMethod;
    }

    public final MemberHelper getNameMethod() {
        return nameMethod;
    }
}
