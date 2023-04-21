package reasoning;

import org.tzi.use.parser.SemanticException;
import org.tzi.use.parser.Symtable;
import org.tzi.use.uml.ocl.type.Type;
import org.tzi.use.uml.ocl.value.VarBindings;

public class AdaptorSymtable extends Symtable {

    public AdaptorSymtable() {
        super();
    }

    public AdaptorSymtable(VarBindings globalBindings) {
        super(globalBindings);
    }

    public void add(String name, Type type) throws SemanticException {
        this.add(name, type, new SrcEMF(name, type.toString()));
    }
}
