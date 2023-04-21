package net.sf.doolin.gui.field.support;

public interface FieldSupportFactory {

    <T extends FieldSupport> T getFieldSupport(Class<T> fieldSupportClass);

    <T extends FieldSupport> void registerFieldSupport(Class<T> fieldSupportClass, Class<? extends T> fieldSupportImplClass);
}
