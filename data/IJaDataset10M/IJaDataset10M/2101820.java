package compiler;

import net.sf.beezle.mork.classfile.Bytecodes;
import net.sf.beezle.mork.classfile.Code;

public class Program implements Bytecodes {

    private final Block body;

    private final Declarations[] scopes;

    public Program(Declarations[] scopes, Block body) {
        this.scopes = scopes;
        this.body = body;
    }

    public Code translate() {
        Code result;
        int i;
        result = new Code();
        result.locals = 1;
        for (i = 0; i < scopes.length; i++) {
            result.locals = scopes[i].allocate(result.locals);
        }
        body.translate(result);
        result.emit(RETURN);
        return result;
    }
}
