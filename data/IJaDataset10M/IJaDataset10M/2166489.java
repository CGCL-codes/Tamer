package org.berlin.pino.dev.analy.metric.antlr;

public class TypeBuilder {

    private final ClassInfo info;

    public TypeBuilder(ClassInfo info) {
        this.info = info;
    }

    public void addField(String name, Type type, Visibility visibility, boolean isGlobal, boolean isFinal) {
        boolean isPrivate = Visibility.PRIVATE == visibility;
        FieldInfo fieldInfo = new FieldInfo(info, name, type, isFinal, isGlobal, isPrivate);
        info.addField(fieldInfo);
    }

    public String getName() {
        return info.getName();
    }
}
