package net.solarnetwork.central.dras.support;

import java.util.LinkedHashMap;
import java.util.Map;
import net.solarnetwork.central.dras.dao.ProgramFilter;
import net.solarnetwork.util.SerializeIgnore;

/**
 * Implementation of {@link ProgramFilter}.
 * 
 * @author matt
 * @version $Revision: 1490 $
 */
public class SimpleProgramFilter implements ProgramFilter {

    private Long userId;

    private String name;

    @Override
    @SerializeIgnore
    public Map<String, ?> getFilter() {
        Map<String, Object> f = new LinkedHashMap<String, Object>(1);
        if (name != null) {
            f.put("name", name);
        }
        if (userId != null) {
            f.put("userId", userId);
        }
        return f;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Long getUserId() {
        return userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
