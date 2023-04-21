package genomancer.ivy.das.client.modelimpl;

import genomancer.ivy.das.model.Das1TypeI;
import genomancer.ivy.das.model.Das1TypesResponseI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Das1TypesResponse extends Das1Response implements Das1TypesResponseI {

    List<Das1TypeI> types;

    Map<String, Das1TypeI> id2type = new HashMap<String, Das1TypeI>();

    public Das1TypesResponse(String request_url, String version, List<Das1TypeI> types) {
        super(request_url, version);
        this.types = types;
        for (Das1TypeI type : types) {
            id2type.put(type.getID(), type);
        }
    }

    public Das1TypeI getType(String id) {
        return id2type.get(id);
    }

    public List<Das1TypeI> getTypes() {
        return types;
    }
}
