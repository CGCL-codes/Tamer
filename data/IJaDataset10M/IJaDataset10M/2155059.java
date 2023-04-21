package net.sf.extcos.filter;

import java.util.Set;
import net.sf.extcos.resource.Resource;

public interface BlacklistManager {

    Set<Resource> newResultSet();

    void blacklist(Resource resource);
}
