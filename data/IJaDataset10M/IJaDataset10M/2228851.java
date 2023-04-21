package plugins;

import general.NotFoundException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import groups.Group;
import groups.GroupDao;
import apis.EngineAPI;
import apis.GroupAPI;
import users.SNU;

/**
 * @author Alvaro
 *
 */
public class PluginAccionBorrarGrupo implements IPlugin {

    /**
	 * 
	 */
    private static final long serialVersionUID = 5972619843186445067L;

    private static Logger log = Logger.getLogger(PluginAccionBorrarGrupo.class.getName());

    private static PluginAccionBorrarGrupo instance = null;

    private PluginAccionBorrarGrupo() {
    }

    public static PluginAccionBorrarGrupo getInstance() {
        if (instance == null) {
            instance = new PluginAccionBorrarGrupo();
        }
        return instance;
    }

    @Override
    public int executeAction() {
        log.debug("Ejecutando PluginAccionBorrarGrupo");
        return 0;
    }

    @Override
    public boolean feasible(Connection conn, SNU pSNU) {
        return true;
    }

    @Override
    public int executeAction(Connection conn, SNU pSNU) {
        log.debug("Ejecutando PluginAccionBorrarGrupo para SNU: " + pSNU.getIdSNU());
        List<Group> groupList = GroupAPI.getAllGroupsNoDeletedOfAUser(conn, pSNU);
        Group oldGroup = GroupAPI.removeOlderGroup(groupList, conn);
        return oldGroup.getIdGroup();
    }
}
