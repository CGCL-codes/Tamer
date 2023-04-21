package com.epicsagaonline.bukkit.permissions;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import com.epicsagaonline.bukkit.EnableError;
import com.epicsagaonline.bukkit.NotFoundError;
import com.epicsagaonline.bukkit.WritableConfiguration;
import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

public class NijikoPermissionManager implements PermissionManager {

    private static final String FILENAME = "config.yml";

    private Permissions perms;

    private PermissionHandler handler;

    private WritableConfiguration source;

    private File sourceFile;

    private long sourceModDate;

    private String defaultGroup;

    private Map<String, String> userCaseMap = new HashMap<String, String>();

    private Map<String, String> groupCaseMap = new HashMap<String, String>();

    /**
	 * 
	 * @param server
	 * @throws FileNotFoundException when PluginManager or it's config.yml isn't
	 *        found.
	 */
    public NijikoPermissionManager(Server server) throws EnableError {
        PluginManager pm = server.getPluginManager();
        try {
            perms = (Permissions) pm.getPlugin("Permissions");
            if (perms == null) {
                throw new EnableError("Permissions plugin doesn't " + "exist on this server. Please make sure Permissions " + "exists in the plugins directory");
            }
            if (!perms.isEnabled()) server.getPluginManager().enablePlugin(perms);
        } catch (ClassCastException e) {
            throw new EnableError("Permissions plugins isn't type " + "com.nijikokun.bukkit.Permissions.Permissions");
        }
        handler = perms.getHandler();
        sourceFile = new File(perms.getDataFolder(), FILENAME);
        sourceModDate = 0;
        source = new WritableConfiguration(sourceFile);
        refreshSource();
    }

    /**
	 * Reload Permissions plugin by issuing an "/rp" chat command 
	 * 
	 * @param server
	 */
    public void reload() {
        perms.getConfiguration().load();
        perms.setupPermissions();
    }

    @SuppressWarnings("unchecked")
    private void refreshSource() {
        if (sourceFile.lastModified() <= sourceModDate) return;
        source.load();
        sourceModDate = sourceFile.lastModified();
        Map<String, ?> users = (Map<String, ?>) source.getProperty("users");
        userCaseMap.clear();
        for (String user : users.keySet()) {
            userCaseMap.put(user.toLowerCase(), user);
        }
        Map<String, ?> groups = (Map<String, ?>) source.getProperty("groups");
        groupCaseMap.clear();
        for (String group : groups.keySet()) {
            groupCaseMap.put(group.toLowerCase(), group);
        }
        for (Map.Entry<String, Map<String, Object>> entry : ((Map<String, Map<String, Object>>) groups).entrySet()) {
            try {
                Map<String, Object> groupEntry = entry.getValue();
                Boolean isDefault = (Boolean) groupEntry.get("default");
                if (isDefault != null && isDefault) {
                    defaultGroup = entry.getKey();
                    break;
                }
            } catch (ClassCastException e) {
                continue;
            }
        }
    }

    private void saveSource() {
        source.save();
        reload();
    }

    private String groupPath(String group) {
        return "groups." + groupCaseMap.get(group.toLowerCase());
    }

    private String userPath(String user) {
        String ret = "users." + userCaseMap.get(user.toLowerCase());
        return ret;
    }

    public VariableContainer getGroupVars(String group) throws NotFoundError {
        return new VariableContainerImpl(groupPath(group));
    }

    public VariableContainer getUserVars(String user) throws NotFoundError {
        return new VariableContainerImpl(userPath(user));
    }

    /**
	 * return true if player exists in the permisisons file 
	 * @param player
	 */
    public boolean hasUser(String name) {
        return source.getProperty(userPath(name)) == null ? false : true;
    }

    /**
	 * Adds the player to the permissions database.  Refuses to overwrite an 
	 *   existing user.
	 * 
	 * @param player
	 * @param group group user belongs to, mandatory
	 * @param permissions optional list of permissions to add for user 
	 * 		  ( can be null or empty )
	 */
    public void addUser(String name, List<String> permissions) {
        refreshSource();
        if (permissions != null && permissions.isEmpty()) permissions = null;
        String path = "users." + name;
        source.setProperty(path + ".group", defaultGroup);
        source.setProperty(path + ".permissions", permissions);
        source.setProperty(path + ".info", null);
        userCaseMap.put(name.toLowerCase(), name);
        saveSource();
    }

    public boolean has(Player player, String perm) {
        return handler.has(player, perm);
    }

    public String getGroup(String playerName) throws NotFoundError {
        String result = handler.getGroup(playerName);
        if (result == null) throw new NotFoundError("User not in file: " + playerName);
        return result;
    }

    private class VariableContainerImpl implements VariableContainer {

        private String path;

        private Map<String, Object> map;

        /**
		 * @throws NotFoundError if user or group doesn't exist
		 */
        public VariableContainerImpl(String userOrGroupPath) throws NotFoundError {
            this.path = userOrGroupPath;
            refreshSource();
            if (source.getProperty(path) == null) throw new NotFoundError("user or group doesn't exist: " + path);
            loadMap();
        }

        @SuppressWarnings("unchecked")
        private void loadMap() {
            if (source.getProperty(path) == null) throw new IllegalStateException("user or group no longer exists: " + path);
            try {
                map = (Map<String, Object>) source.getProperty(path + ".info");
            } catch (ClassCastException e) {
                map = null;
            }
            if (map == null) {
                map = new HashMap<String, Object>();
                source.setProperty(path + ".info", map);
            }
        }

        private void refreshMap() {
            long lastDate = sourceModDate;
            refreshSource();
            if (lastDate == sourceModDate) return;
            loadMap();
        }

        public void set(String variable, Object val) {
            refreshMap();
            map.put(variable, val);
            saveSource();
        }

        public Boolean getBoolean(String variable) {
            refreshMap();
            try {
                return (Boolean) map.get(variable);
            } catch (NullPointerException e) {
                return (Boolean) null;
            }
        }

        public Double getDouble(String variable) {
            refreshMap();
            try {
                return (Double) map.get(variable);
            } catch (NullPointerException e) {
                return null;
            }
        }

        public Integer getInteger(String variable) {
            refreshMap();
            try {
                return (Integer) map.get(variable);
            } catch (NullPointerException e) {
                return null;
            }
        }

        public Object getObject(String variable) {
            refreshMap();
            return map.get(variable);
        }

        public String getString(String variable) {
            refreshMap();
            Object result = map.get(variable);
            if (result == null) return null;
            if (result instanceof String) return (String) result; else return result.toString();
        }
    }
}
