package plugins;

import general.NotFoundException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import contents.Content;
import contents.ContentDao;
import apis.ContentAPI;
import apis.EngineAPI;
import users.SNU;

/**
 * @author Alvaro
 *
 */
public class PluginAccionBorrarContenido implements IPlugin {

    /**
	 * 
	 */
    private static final long serialVersionUID = 5972619843186445067L;

    private static Logger log = Logger.getLogger(PluginAccionBorrarContenido.class.getName());

    private static PluginAccionBorrarContenido instance = null;

    private PluginAccionBorrarContenido() {
    }

    public static PluginAccionBorrarContenido getInstance() {
        if (instance == null) {
            instance = new PluginAccionBorrarContenido();
        }
        return instance;
    }

    @Override
    public int executeAction() {
        log.debug("Ejecutando PluginAccionBorrarContenido");
        return 0;
    }

    @Override
    public boolean feasible(Connection conn, SNU pSNU) {
        return true;
    }

    @Override
    public int executeAction(Connection conn, SNU pSNU) {
        log.debug("Ejecutando PluginAccionBorrarContenido para SNU: " + pSNU.getIdSNU());
        List<Content> contentList = ContentAPI.getAllContentsNoDeletedOfAUser(conn, pSNU);
        Content oldContent = ContentAPI.removeOlderContent(contentList, conn);
        return oldContent.getIdContent();
    }
}
