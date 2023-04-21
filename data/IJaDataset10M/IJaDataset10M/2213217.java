package fitlibrary.specify.mapTraverse;

import java.util.HashMap;
import java.util.Map;
import fitlibrary.object.DomainFixtured;

public class Empty implements DomainFixtured {

    @SuppressWarnings({ "rawtypes" })
    public Map getEmptyMap() {
        return new HashMap();
    }
}
