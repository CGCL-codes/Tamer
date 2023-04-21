package ytex.kernel.tree;

import java.util.List;
import java.util.Map;

public class QueryMappingInfo {

    String query;

    Map<String, Object> queryArgs;

    List<NodeMappingInfo> nodeTypes;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Map<String, Object> getQueryArgs() {
        return queryArgs;
    }

    public void setQueryArgs(Map<String, Object> queryArgs) {
        this.queryArgs = queryArgs;
    }

    public List<NodeMappingInfo> getNodeTypes() {
        return nodeTypes;
    }

    public void setNodeTypes(List<NodeMappingInfo> nodeTypes) {
        this.nodeTypes = nodeTypes;
    }
}
