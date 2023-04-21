package streamcruncher.api;

import java.util.List;
import java.util.Map;
import streamcruncher.api.artifact.RowSpec;

public interface CustomStore {

    public void init(String queryName, Map<String, RowSpec> sourceTblAliasAndRowSpec, String whereClause);

    public void startBatch();

    public void added(String alias, Long id, Object[] data);

    public void removed(String alias, Long id, Object[] data);

    public List<Object[]> endBatch();

    public void destroy();
}
