package client.manager;

import java.util.HashMap;
import client.game.entity.EntityManagerFactory;
import client.game.entity.IEntity;
import client.game.state.WorldStatesMachine;
import com.jme.scene.Node;
import com.jmex.game.state.BasicGameState;
import com.jmex.game.state.GameStateManager;

/**
  * <code>EntityManager</code> es responsable de administrar
  * todas las <code>IEntity</code> en el mundo del juego.
  * <p>
  * <code>EntityManager</code> es responsable de todos los aspectos 
  * de administracion de entidades, incluido la creacion, obtencion y destruccion
  * de las mismas
  * <p>
  * <code>EntityManager</code> Mantiene todas las entidades por su ID (identidicacion unica).
  * Esto permite multiples entidades con el mismo tipo
  * <p>
  * <code>EntityManager</code> es un Singleton.
  * @author Santiago Michielotto, Guillermo Fiorini (Javadoc)
  * @version Created: 20-11-2008
 */
public class EntityManager {

    /**
	 * El numero de identificacion unica de la entidad actual del lado del cliente
	 */
    private static int currentId;

    /** 
	 * Obtiene el ID de la entidad actual de las entidades del juego
	 * @return la identidad actual de las entidades del juego.
	 */
    public Integer getCurrentId() {
        return currentId;
    }

    /** 
	 * Aplica un ID actual al <code>EntityManager</code>.
	 * @param theCurrentId to apply to the EntityManager singleton.
	 */
    public void setCurrentId(Integer theCurrentId) {
        currentId = theCurrentId;
    }

    /**
	 * La instacia unica del <code>EntityManager</code>.
	 */
    private static EntityManager instance;

    /**
	 * El <code>HashMap</code> de pares de IDs y <code>IEntity</code>.
	 */
    private HashMap<String, IEntity> hash;

    /** 
	 * Obtiene el <code>HasMap</code> de entidades del juego.
	 * @return el <code>HasMap</code> de entidades del juego.
	 */
    public HashMap<String, IEntity> getHash() {
        return hash;
    }

    /** 
	 * Cambia el <code>HashMap</code> de entidades al <code>EntityManager</code>.
	 * @param theHash <code>HashMap</code> a aplicar en el singleton.
	 */
    public void setHash(HashMap<String, IEntity> theHash) {
        hash = theHash;
    }

    /**
	 * Constructor de <code>EntityManager</code>.
	 */
    protected EntityManager() {
        this.hash = new HashMap<String, IEntity>();
    }

    /** 
	 * Obtiene la instancia unica del <code>EntityManager</code>.
	 * @return la instancia del <code>EntityManager<code>.
	 */
    public static EntityManager getInstance() {
        if (EntityManager.instance == null) {
            EntityManager.instance = new EntityManager();
        }
        return EntityManager.instance;
    }

    /** 
	 * Obtiene una <code>IEntity<code> especifica del juego.
	 * @param entity La <code>IEntity<code> a obtener.
	 * @return 
	 */
    public IEntity getEntity(IEntity entity) {
        return hash.get(entity.getId());
    }

    /** 
	 * Obtiene una <code>IEntity<code> especifica del juego.
	 * @param id <code>String<code> El identificador unico de la entidad.
	 * @return 
	 */
    public IEntity getEntity(String id) {
        return hash.get(id);
    }

    /**
	 * Quita una entidad.
	 * @param entity La <code>IEntity<code> a destruir.
	 */
    public void removeEntity(IEntity entity) {
        if (entity != null) {
            Node root = ((BasicGameState) GameStateManager.getInstance().getChild(WorldStatesMachine.activeState)).getRootNode();
            root.getChild(entity.getId());
        }
        hash.remove(entity.getId());
    }

    /**
	 * Quita una entidad.
	 * @param id <code>String</code> El identificador unico de la entidad a destruir.
	 */
    public void removeEntity(String id) {
        hash.remove(id);
    }

    /**
	 * Registra una entidad dada.
	 * @param entity La <code>IEntity</code> a registrar.
	 */
    public void registerEntity(IEntity entity) {
        if (hash.containsKey(entity.getId())) {
            return;
        }
        hash.put(entity.getId(), entity);
    }

    /**
	 * Crea una entidad en el mundo.
	 * @return La recientemente creada <code>IEntity</code>.
	 */
    public IEntity createEntity(String factoryId, String id) {
        if (!hash.containsKey(id)) {
            IEntity entity = EntityManagerFactory.getInstance().create(factoryId, id);
            if (entity != null) {
                registerEntity(entity);
                return entity;
            }
        }
        return null;
    }

    /**
	 * Quita todas las entidades del mundo.
	 */
    public void removeAll() {
        hash.clear();
    }
}
