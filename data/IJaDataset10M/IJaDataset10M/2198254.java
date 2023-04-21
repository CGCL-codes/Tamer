package br.guj.chat.dao.hibernate;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import br.guj.chat.dao.ActionDAO;
import br.guj.chat.dao.DAOException;
import br.guj.chat.dao.DAOLocator;
import br.guj.chat.model.action.Action;
import br.guj.chat.model.action.ActionGroup;
import br.guj.chat.model.server.ChatIOException;
import br.guj.chat.model.server.ChatServer;
import br.guj.chat.util.AdvancedProperties;
import br.guj.chat.util.PropertiesFile;

/**
 * A hibernate based action dao class
 * @author Guilherme de Azevedo Silveira
 * @version $Revision: 1.2 $, $Date: 2003/10/29 18:34:10 $
 */
class HibernateActionDAO implements ActionDAO {

    /** the server */
    private ChatServer server = null;

    /** cached map of action groups */
    private HashMap map = new HashMap();

    HibernateDAO dao = null;

    /**
	 * Creates the file action dao object and caches the actions
	 * @param server
	 */
    public HibernateActionDAO(HibernateDAO hibernateDAO, ChatServer server) {
        this.server = server;
        this.dao = hibernateDAO;
    }

    /**
	 * Returns an action group
	 */
    public ActionGroup get(String id) throws DAOException {
        if (!map.containsKey(id)) {
            throw new DAOException("Action group " + id + " not found");
        }
        return (ActionGroup) map.get(id);
    }

    /**
	 * Fast loads all in memory, if not yet loaded
	 */
    public void loadAll() throws DAOException {
        if (map.isEmpty()) {
            File file = getFile();
            if (!file.exists()) {
                throw new ChatIOException("Action file does not exist for server " + server.getID());
            }
            AdvancedProperties p = new PropertiesFile(file);
            int id = p.getInt("Groups");
            for (int i = 1; i != id + 1; i++) {
                ActionGroup g = read(p.getProperty("Group." + i + ".ID"));
                map.put(g.getID(), g);
            }
        }
    }

    /**
	 * Reads an action group from the data source
	 * @param id	the action group
	 * @return ActionGroup	the action group
	 */
    private ActionGroup read(String groupID) throws ChatIOException {
        File file = getFile();
        if (!file.exists()) {
            throw new ChatIOException("Action file does not exist for server " + server.getID());
        }
        AdvancedProperties p = new PropertiesFile(file);
        if (p.getProperty("Group." + groupID) == null) {
            throw new ChatIOException("Action group does not exist for server " + groupID + "/" + server.getID());
        }
        int id = p.getInt("Group." + groupID);
        p = p.getProperties("Group." + id + ".", true);
        String name = p.getProperty("Name");
        String description = p.getProperty("Description", name);
        int list = p.getInt("Actions");
        ActionGroup g = new ActionGroup(groupID, name);
        g.setDescription(description);
        for (int c = 1; c != list + 1; c++) {
            String aid = p.getProperty(c + ".ID");
            String aname = p.getProperty(c + ".Name");
            String amask = p.getProperty(c + ".Mask");
            String atoMask = p.getProperty(c + ".ToMask");
            g.addAction(new Action(aid, aname, amask, atoMask));
        }
        g.setDefaultAction(g.getAction(p.getProperty("Default")));
        return g;
    }

    /**
	 * Returns the file to be used
	 * @return File
	 */
    private File getFile() {
        return new File(DAOLocator.getDAO().getServerDAO().getTemplatePath(server), "actions.txt");
    }

    /**
	 * Returns a list of available action groups
	 */
    public ArrayList getAvailable() throws DAOException {
        return new ArrayList(map.values());
    }

    /**
	 * Returns the id list of available action groups
	 */
    public ArrayList getAvailableID() throws DAOException {
        return new ArrayList(map.keySet());
    }

    /**
	 * Removes an action group
	 */
    public void remove(ActionGroup action) throws DAOException {
        if (map.get(action.getID()) != null) {
            map.remove(action.getID());
            save();
        }
    }

    /**
	 * Saves back all data to the system
	 */
    private void save() throws DAOException {
        PropertiesFile p = new PropertiesFile();
        p.setFile(getFile());
        ArrayList groups = getAvailable();
        p.setProperty("Groups", "" + groups.size());
        for (int z = 0; z != groups.size(); z++) {
            int i = z + 1;
            ActionGroup g = (ActionGroup) groups.get(z);
            String prefix = "Group." + i + ".";
            p.setProperty("Group." + g.getID(), "" + i);
            p.setProperty(prefix + "ID", g.getID());
            p.setProperty(prefix + "Name", g.getName());
            p.setProperty(prefix + "Description", g.getName());
            p.setProperty(prefix + "Default", g.getDefaultAction().getID());
            p.setProperty(prefix + "Actions", "" + g.getActions().size());
            ArrayList actions = g.getActions();
            for (int i2 = 0; i2 != actions.size(); i2++) {
                Action action = (Action) actions.get(i2);
                p.setProperty(prefix + (i2 + 1) + ".ID", action.getID());
                p.setProperty(prefix + (i2 + 1) + ".Name", action.getName());
                p.setProperty(prefix + (i2 + 1) + ".Mask", action.getMask());
                p.setProperty(prefix + (i2 + 1) + ".ToMask", action.getToMask());
            }
        }
        p.save();
    }

    /**
	 * Updates an action group
	 */
    public void update(ActionGroup group) throws DAOException {
        if (map.get(group.getID()) != null) {
            save();
        }
    }

    /**
	 * Returns a map list with all available groups
	 */
    public HashMap getAvailableMap() throws DAOException {
        return map;
    }

    /**
	 * Adds a new action group
	 */
    public void insert(ActionGroup group) throws DAOException {
        map.put(group.getID(), group);
        save();
    }
}
