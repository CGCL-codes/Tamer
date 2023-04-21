package com.xucia.jsponic.datasource;

import java.util.List;
import com.xucia.jsponic.data.ObjectId;

public interface DataSourceCanHaveOrphans extends DataSource {

    public List<ObjectId> getOrphans();

    public void purgeOrphans();
}
