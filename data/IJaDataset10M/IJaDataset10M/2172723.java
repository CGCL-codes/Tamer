package backend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import backend.core.persistent.AbstractONDEXPersistent;
import backend.core.searchable.LuceneEnv;

public abstract class AbstractArguments {

    protected Map<String, List<Object>> options = new HashMap<String, List<Object>>();

    ;

    protected LuceneEnv lenv;

    protected AbstractONDEXPersistent penv;

    /**
	 * Returns the actual indexed ondex graph enviroment.
	 * 
	 * @return indexed enviroment
	 */
    public LuceneEnv getIndexedEnv() {
        return lenv;
    }

    /**
	 * Sets the indexed ondex graph enviroment. 
	 * 
	 * @param lenv - LuceneEnv
	 */
    public void setIndexedEnv(LuceneEnv lenv) {
        this.lenv = lenv;
    }

    /**
	 * Adds additional options for one mapping as a name-value[s] pair.
	 * 
	 * @param name - name of option
	 * @param value - value for option
	 */
    public void addOption(String name, Object value) {
        List<Object> list = options.get(name);
        if (list == null) {
            list = new ArrayList<Object>(1);
            options.put(name, list);
        }
        list.add(value);
    }

    /**
	 * Returns all options as a Map.
	 * 
	 * @return Map containing options
	 */
    public Map<String, List<Object>> getOptions() {
        return options;
    }

    public Object getUniqueValue(String name) {
        List<Object> values = getOptions().get(name);
        if (values == null || values.size() == 0) {
            return null;
        }
        if (values.size() == 1) {
            return values.get(0);
        } else {
            System.err.println("Multiple values found when one was expected");
            return values.get(0);
        }
    }

    public List<Object> getObjectValueList(String name) {
        return getOptions().get(name);
    }

    public void setPersistentEnv(AbstractONDEXPersistent penv) {
        this.penv = penv;
    }

    public AbstractONDEXPersistent getPersistentEnv() {
        return penv;
    }
}
