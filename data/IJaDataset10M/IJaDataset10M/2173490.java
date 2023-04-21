package pl.wcislo.sbql4j.java.model.runtime;

import java.util.Collections;
import java.util.List;
import pl.wcislo.sbql4j.lang.types.ENVSType;
import pl.wcislo.sbql4j.lang.types.Executable;

public abstract class MethodBinder implements ENVSType, Executable {

    private Object owner;

    public MethodBinder(Object owner) {
        this.owner = owner;
    }

    public abstract boolean isApplicableTo(String methodName, Object... params);

    public abstract Object execute(Object... params) throws Exception;

    public Object getOwner() {
        return owner;
    }

    @Override
    public List<? extends ENVSType> nested() {
        return Collections.EMPTY_LIST;
    }
}
