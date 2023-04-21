package ch.jester.db.persister.impl;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceFactory;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.ComponentContext;
import ch.jester.common.components.InjectedLogFactoryComponentAdapter;
import ch.jester.commonservices.api.bundle.IActivationContext;
import ch.jester.commonservices.api.persistency.IDaoService;
import ch.jester.commonservices.api.persistency.IDaoServiceFactory;
import ch.jester.commonservices.api.persistency.IPrivateContextDaoService;
import ch.jester.commonservices.api.persistency.IEntityObject;
import ch.jester.commonservices.util.ServiceUtility;
import ch.jester.dao.ICategoryDao;
import ch.jester.dao.IPlayerDao;
import ch.jester.dao.IRoundDao;
import ch.jester.dao.ITournamentDao;

/**
 * Implementiert eine ServiceFactory und stellt die DAO Implementierungen zur Verfügung.
 * Die Klasse wird als Komponente mit DS gestartet und registriert sich selbst als Factory für die entsprechenden
 * DAO Services.
 * Die Factory gibt immer eine neue Service Instanz zurück. Damit wird das DS/OSGI Caching pro Bundle umgangen, so dass
 * der DAO Service ansich nicht Threadsafe sein muss.
 *  
 *
 */
public class PersisterFactory extends InjectedLogFactoryComponentAdapter<Object> implements ServiceFactory, IDaoServiceFactory {

    private ServiceUtility mServiceUtility;

    private HashMap<String, Class<?>> mServiceInterfaceRegistry = new HashMap<String, Class<?>>();

    private HashMap<Class<?>, Class<?>> mDaoObjectClassRegistry = new HashMap<Class<?>, Class<?>>();

    private IActivationContext<?> mActivationContext;

    public PersisterFactory() {
    }

    @Override
    public Object getService(Bundle bundle, ServiceRegistration registration) {
        String objectClass = ((String[]) registration.getReference().getProperty("objectClass"))[0];
        Class<?> clz = mServiceInterfaceRegistry.get(objectClass);
        try {
            return clz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends IDaoService<?>> T getDaoServiceByServiceInterface(Class<T> objectClass) {
        Class<?> clz = mServiceInterfaceRegistry.get(objectClass.getCanonicalName());
        try {
            return (T) clz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends IEntityObject> IDaoService<T> getDaoServiceByEntity(Class<T> objectClass) {
        Class<?> clz = mServiceInterfaceRegistry.get(objectClass.getCanonicalName());
        if (clz == null) {
            clz = mDaoObjectClassRegistry.get(objectClass);
        }
        if (clz != null) {
            try {
                return (IDaoService<T>) clz.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return (IDaoService<T>) new GenericPersister<T>(objectClass);
    }

    @Override
    public void ungetService(Bundle bundle, ServiceRegistration registration, Object service) {
    }

    @Override
    public void addServiceHandling(Class<?> pInterfaceClassName, Class<?> class1) {
        mServiceInterfaceRegistry.put(pInterfaceClassName.getName(), class1);
        mServiceUtility.registerServiceFactory(pInterfaceClassName, this);
        Type actualTypeArgument = ((ParameterizedType) class1.getGenericSuperclass()).getActualTypeArguments()[0];
        Class<?> mClz = (Class<?>) actualTypeArgument;
        mDaoObjectClassRegistry.put(mClz, class1);
    }

    @Override
    public void start(ComponentContext pComponentContext) {
        mActivationContext = Activator.getDefault().getActivationContext();
        mServiceUtility = mActivationContext.getServiceUtil();
        addServiceHandling(IPlayerDao.class, DBPlayerPersister.class);
        addServiceHandling(ITournamentDao.class, DBTournamentPersister.class);
        addServiceHandling(ICategoryDao.class, DBCategoryPersister.class);
        addServiceHandling(IRoundDao.class, DBRoundPersister.class);
    }

    @Override
    public <T extends IEntityObject> void registerDaoService(Class<T> pClass, Class<IDaoService<T>> pServiceClass) {
        mDaoObjectClassRegistry.put(pClass, pServiceClass);
    }

    @Override
    public <T extends IEntityObject> IPrivateContextDaoService<T> adaptPrivate(IDaoService<T> pService) {
        if (!(pService instanceof GenericPersister)) {
            throw new IllegalArgumentException("Argument must be instance of GenericPersister");
        }
        return new PrivateContextDaoServiceAdapter<T>((GenericPersister<T>) pService);
    }
}
