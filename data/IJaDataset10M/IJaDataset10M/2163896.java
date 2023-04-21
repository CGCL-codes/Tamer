package net.sourceforge.jpp.processor;

import java.util.List;
import net.sourceforge.jpp.model.TypeName;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.ElementKind;
import javax.lang.model.type.TypeMirror;
import net.sourceforge.jpp.model.MethodData;
import net.sourceforge.jpp.annotation.TypeKind;
import net.sourceforge.jpp.model.TypeData;

public interface Modeler {

    TypeName newTypeName(TypeElement type);

    TypeName newTypeName(String name, boolean indexed);

    TypeName newTypeName(String name);

    TypeName newTypeName(TypeMirror mirror);

    TypeName[] newTypeNames(List<? extends TypeMirror> values);

    TypeKind getKind(ElementKind kind);

    MethodData[] listMethods(TypeElement type);

    MethodData[] listMethods(String typeName);

    MethodData getInit(TypeElement type);

    TypeData buildTypeData(TypeElement type);

    TypeData buildTypeData(String typeName);

    TypeData newTypeData(String typeName);

    TypeData newTypeData(String typeName, TypeElement type, String nameSuffix);

    void inheritType(TypeData base, TypeData derived);
}
