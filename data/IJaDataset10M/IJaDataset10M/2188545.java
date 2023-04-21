package cleanj.lang;

import java.io.PrintStream;

public abstract class CleanDescriptorRecord extends CleanDescriptor {

    public final boolean eval(CleanVM vm, Object data, Object o) throws CleanException {
        return false;
    }

    public final boolean apply(CleanVM vm, Object data, Object o) throws CleanException {
        throw new CleanException("this operation is not supported.");
    }

    public final Object create_array(int size, CleanVM vm) throws CleanException {
        throw new CleanException("this operation is not supported.");
    }
}
