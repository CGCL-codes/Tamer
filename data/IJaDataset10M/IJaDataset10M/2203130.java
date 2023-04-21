package polyglot.ext.jl5.types;

import java.util.List;
import polyglot.types.ProcedureDef;
import polyglot.types.Ref;
import polyglot.types.Type;

public interface JL5ProcedureDef extends ProcedureDef {

    void setTypeVariableTypes(List<Ref<? extends Type>> pTypes);

    List<Ref<? extends Type>> typeVariableTypes();
}
