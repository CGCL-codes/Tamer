package org.nvframe.manager;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 
 * @author Nik Van Looy
 */
public class RegisterManager {

    private static final RegisterManager _instance = new RegisterManager();

    public static RegisterManager getInstance() {
        return _instance;
    }

    private static Map<String, Object> register;

    private RegisterManager() {
        register = new LinkedHashMap<String, Object>();
    }

    /**
	 * get an option from the registry
	 * 
	 * @param id the registry key value
	 * @return The registry value
	 */
    public Object getOption(String id) {
        return register.get(id);
    }

    /**
	 * set a registry key - value
	 * 
	 * @param id The registry key
	 * @param ob the registry value
	 */
    public void setOption(String id, Object ob) {
        register.put(id, ob);
    }
}
