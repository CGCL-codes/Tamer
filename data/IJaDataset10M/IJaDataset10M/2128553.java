package org.jmlspecs.jml6.boogie.translator.factory;

public class BoogieUnknownTranslatorType extends RuntimeException {

    @SuppressWarnings("unchecked")
    Class clazz;

    @SuppressWarnings("unchecked")
    BoogieUnknownTranslatorType(Class clazz) {
        super("Factory does not know how to handle the translator of type: " + clazz.getName());
        this.clazz = clazz;
    }
}
