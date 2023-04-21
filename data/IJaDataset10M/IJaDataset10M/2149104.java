package xml.filter;

import java.util.HashMap;
import java.util.Map;

public class DefaultFilter extends StackFilter {

    public DefaultFilter(Map map) {
        this.push(new EnvironmentFilter());
        this.push(new SystemFilter());
        this.push(new MapFilter(map));
    }
}
