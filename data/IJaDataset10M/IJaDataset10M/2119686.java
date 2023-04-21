package sisc.ser;

import java.io.*;
import sisc.data.Expression;
import sisc.data.Symbol;
import sisc.env.SymbolicEnvironment;
import sisc.env.DelegatingSymEnv;
import sisc.util.InternedValue;
import sisc.interpreter.Context;
import sisc.interpreter.AppContext;

public class JavaDeserializer extends DeserializerImpl {

    private JavaDeserializer(AppContext ctx, ObjectInput i) throws IOException {
        super(ctx, i);
    }

    public static Deserializer create(ObjectInput i) throws IOException {
        return (i instanceof NestedObjectInputStream) ? ((NestedObjectInputStream) i).getDeserializerInstance() : new JavaDeserializer(Context.currentAppContext(), i);
    }

    protected Object readObjectIOExceptionOnly() throws IOException {
        try {
            return readObject();
        } catch (ClassNotFoundException cnf) {
            throw new IOException(cnf.toString());
        }
    }

    public Expression readExpression() throws IOException {
        boolean isLibraryReference = readBoolean();
        if (isLibraryReference) {
            String libName = readUTF();
            int epid = readInt();
            return resolveLibraryBinding(new LibraryBinding(libName, epid));
        } else {
            Object o = readObjectIOExceptionOnly();
            return (Expression) ((o instanceof InternedValue) ? ((InternedValue) o).readResolve() : o);
        }
    }

    public Expression readInitializedExpression() throws IOException {
        return readExpression();
    }

    public SymbolicEnvironment readSymbolicEnvironment() throws IOException {
        Expression e = readExpression();
        return (e instanceof Symbol) ? new DelegatingSymEnv((Symbol) e) : (SymbolicEnvironment) e;
    }

    public Class readClass() throws IOException {
        return (Class) readObjectIOExceptionOnly();
    }
}
