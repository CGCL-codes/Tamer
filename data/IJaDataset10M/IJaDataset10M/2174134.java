package pedro.system;

import java.util.*;

public class Context extends HashMap {

    public Context() {
    }

    public Object getProperty(String propertyName) {
        return (get(propertyName));
    }

    public String[] getRegisteredPropertyNames() {
        ArrayList propertyNames = new ArrayList();
        propertyNames.addAll(keySet());
        String[] results = (String[]) propertyNames.toArray(new String[0]);
        Arrays.sort(results);
        return results;
    }

    public void setProperty(String propertyName, Object propertyValue) {
        put(propertyName, propertyValue);
    }
}
