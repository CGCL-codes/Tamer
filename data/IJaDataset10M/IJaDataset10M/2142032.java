package com.sun.tools.jdi;

import com.sun.jdi.*;
import java.util.*;

public class ClassObjectReferenceImpl extends ObjectReferenceImpl implements ClassObjectReference {

    private ReferenceType reflectedType;

    ClassObjectReferenceImpl(VirtualMachine vm, long ref) {
        super(vm, ref);
    }

    public ReferenceType reflectedType() {
        if (reflectedType == null) {
            try {
                JDWP.ClassObjectReference.ReflectedType reply = JDWP.ClassObjectReference.ReflectedType.process(vm, this);
                reflectedType = vm.referenceType(reply.typeID, reply.refTypeTag);
            } catch (JDWPException exc) {
                throw exc.toJDIException();
            }
        }
        return reflectedType;
    }

    byte typeValueKey() {
        return JDWP.Tag.CLASS_OBJECT;
    }

    public String toString() {
        return "instance of " + referenceType().name() + "(reflected class=" + reflectedType().name() + ", " + "id=" + uniqueID() + ")";
    }
}
