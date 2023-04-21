package annone.engine.generated.annone.standard;

import java.nio.charset.Charset;
import annone.engine.Engine;
import annone.engine.Pointer;
import annone.engine.Type;

public class CharArrayPointer extends Pointer {

    private static Type getByteArrayType(Engine engine) {
        return engine.getType(engine.getComponent("annone.standard.Array"), new Type[] { engine.getComponent("annone.standard.Integer") }, false);
    }

    private Charset charset;

    private byte[] bytes;

    public CharArrayPointer(Engine engine) {
        super(getByteArrayType(engine));
        bytes = new byte[0];
    }

    public byte[] getBytes() {
        return bytes;
    }
}
