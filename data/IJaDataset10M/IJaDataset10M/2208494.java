package annone.local.linker;

import java.io.DataOutput;
import java.io.IOException;
import annone.local.OpCode;

class NullExpression extends Expression {

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeByte(OpCode.NULL);
    }
}
