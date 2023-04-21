package kanjitori.abstraction;

import java.util.Map;

/**
 *
 * @author Pirx
 */
public interface Parameterizable {

    public void setParams(Map<String, String> map);

    public Map<String, String> getParams();
}
