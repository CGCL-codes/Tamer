package ch.bbv.dog.remote.ejb3;

import java.util.List;
import java.util.Map;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ch.bbv.application.ComponentException;
import ch.bbv.application.ComponentImpl;
import ch.bbv.dog.DataObject;
import ch.bbv.dog.DataObjectHandler;
import ch.bbv.dog.LightweightObject;
import ch.bbv.dog.remote.DataObjectRemoteHandler;
import ch.bbv.dog.updates.DataObjectUpdate;
import ch.bbv.dog.updates.DogUpdate;
import ch.bbv.dog.updates.PostRetrieveFunctor;
import ch.bbv.utilities.ReflectUtilities;

/**
 * The EJB client is used to retrieve and store data objects via an EJB on a
 * Java EE application server.
 * 
 * @author UeliKurmann
 * @author ZlatkoFranjcic
 * @version $Revision: 1.2 $
 */
public class EjbClient extends ComponentImpl implements DataObjectHandler {

    private static final String PROPERTY_TIMESTAMP = "timestamp";

    private static final String PROPERTY_ID = "id";

    private static final String EJB_NAME = "EjbServer/remote";

    private DataObjectRemoteHandler handler;

    /**
   * Logger of the instances of the class.
   */
    private static Log log = LogFactory.getLog(EjbClient.class);

    /**
   * Initializes the EJB client.
   * 
   * @param name
   *          the component name for this EJB client
   */
    public EjbClient(String name) {
        super(name);
        try {
            initialize();
            Context jndiContext = new InitialContext();
            Object ref = jndiContext.lookup(EJB_NAME);
            handler = (DataObjectRemoteHandler) PortableRemoteObject.narrow(ref, DataObjectRemoteHandler.class);
        } catch (javax.naming.NamingException e) {
            log.fatal(e);
        } catch (ComponentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize() throws ComponentException {
        super.initialize();
    }

    public void closeDatasource(String datasource) {
    }

    public void openDatasource(String source, boolean readonly) {
    }

    public void remove(String datasource, Class<? extends DataObject> clazz, DataObject root) {
        handler.remove(datasource, clazz, root);
    }

    public void remove(String datasource, Class<? extends DataObject> clazz, Integer id) {
        handler.remove(datasource, clazz, id);
    }

    public DataObject retrieve(String datasource, Class<? extends DataObject> clazz, Integer id) {
        DataObject dataObject = handler.retrieve(datasource, clazz, id);
        PostRetrieveFunctor.createUpdates(dataObject);
        return dataObject;
    }

    public List<DataObject> retrieve(String datasource, Class<? extends DataObject> clazz, String constraints) {
        List<DataObject> dataObjectList = handler.retrieve(datasource, clazz, constraints);
        PostRetrieveFunctor.createUpdatesForList(dataObjectList);
        return dataObjectList;
    }

    public LightweightObject retrieveLw(String datasource, Class<? extends LightweightObject> clazz, Integer id) {
        return handler.retrieveLw(datasource, clazz, id);
    }

    public List<LightweightObject> retrieveLw(String datasource, Class<? extends LightweightObject> clazz, String constraints) {
        return handler.retrieveLw(datasource, clazz, constraints);
    }

    public List<DataObjectUpdate> store(String datasource, Class<? extends DataObject> clazz, DataObject root) {
        DogUpdate dogUpdate = new DogUpdate(root);
        Map<DataObjectUpdate, DataObject> updatesToDataObjects = dogUpdate.mapUpdatesToDataobjects();
        List<DataObjectUpdate> updates = handler.store(datasource, dogUpdate);
        postStore(updates, updatesToDataObjects);
        return updates;
    }

    /**
   * Copies the id and timestamp from the updates of stored objects back to the
   * corresponding local data object.
   * 
   * @param updates
   *          the update objects representing the stored objects
   * @param updatesToDataObjects
   *          a map containing the update-to-data-object mapping
   */
    private void postStore(List<DataObjectUpdate> updates, Map<DataObjectUpdate, DataObject> updatesToDataObjects) {
        for (DataObjectUpdate update : updates) {
            DataObject dataObject = updatesToDataObjects.get(update);
            ReflectUtilities.setProperty(dataObject, PROPERTY_ID, update.getId());
            ReflectUtilities.setProperty(dataObject, PROPERTY_TIMESTAMP, update.getTimestamp());
            dataObject.setModified(false);
            dataObject.getUpdate().clearUpdates();
        }
    }
}
