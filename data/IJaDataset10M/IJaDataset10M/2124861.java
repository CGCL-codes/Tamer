package polyglot.ext.jl5.types;

import java.util.List;
import polyglot.types.Name;
import polyglot.types.ParsedClassType;
import polyglot.types.TypeObject;

public interface JL5ParsedClassType extends ParsedClassType {

    void addEnumConstant(EnumInstance ei);

    List<EnumInstance> enumConstants();

    EnumInstance enumConstantNamed(Name name);

    void addAnnotationElem(AnnotationElemInstance ai);

    List<AnnotationElemInstance> annotationElems();

    AnnotationElemInstance annotationElemNamed(Name name);

    void annotations(List annotations);

    List annotations();

    List<TypeVariable> typeVariables();

    boolean hasTypeVariable(Name name);

    boolean isGeneric();

    boolean equivalentImpl(TypeObject arg2);
}
