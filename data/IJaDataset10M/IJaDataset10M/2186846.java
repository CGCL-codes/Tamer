package ac.jp.u_tokyo.SyncLib.language;

import java.io.IOException;
import java.util.HashMap;

public class ObjCreation extends Const {

    String _ref;

    public ObjCreation(int line, String ref) {
        super(line);
        _ref = ref;
    }

    @Override
    public void write(Appendable sb, ConstMapper constMap) throws ReferenceNotFoundException, IOException {
        sb.append("(new ").append(_ref).append("())");
    }
}
