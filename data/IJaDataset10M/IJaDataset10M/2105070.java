package pt.ips.estsetubal.mig.academicCloud.server.helper;

import java.util.HashMap;
import java.util.Map;
import pt.ips.estsetubal.mig.academicCloud.server.resources.ResourceLocator;
import pt.ips.estsetubal.mig.academicCloud.server.service.ServiceLocator;
import pt.ips.estsetubal.mig.academicCloud.shared.dto.SessionDTO;
import pt.ips.estsetubal.mig.academicCloud.shared.enums.Module;

/**
 * This class stores business information about the current request being
 * processed and provides several services.
 * 
 * <p>
 * There is only one for each request being processed.
 * </p>
 * 
 * @author António Casqueiro
 */
public final class ServerBusinessHelper {

    /**
	 * Store the ServerBusinessHelper instance for each module.
	 */
    private static ThreadLocal<Map<Module, ServerBusinessHelper>> threadLocal = new ThreadLocal<Map<Module, ServerBusinessHelper>>();

    /**
	 * Utility class to get access to services. For more information see
	 * http://java
	 * .sun.com/blueprints/corej2eepatterns/Patterns/ServiceLocator.html
	 */
    private ServiceLocator serviceLocator = null;

    /**
	 * Utility class to get access to resources.
	 */
    private ResourceLocator resourceLocator = null;

    /**
	 * Creation date of the object.
	 */
    private long start;

    /**
	 * Flag to rollback the current transaction.
	 */
    private boolean isToRollback;

    /**
	 * Module.
	 */
    private Module module;

    /**
	 * Reference to the object responsible for managing information from the
	 * current session.
	 */
    private ServerContextInformation contextInformation;

    /**
	 * Constructor.
	 */
    public ServerBusinessHelper(SessionDTO sessionInfo, Module module, boolean automaticTransaction) {
        if (threadLocal.get() == null) {
            Map<Module, ServerBusinessHelper> references = new HashMap<Module, ServerBusinessHelper>();
            threadLocal.set(references);
        }
        Map<Module, ServerBusinessHelper> references = threadLocal.get();
        ServerBusinessHelper helper = references.get(module);
        if (helper != null) {
            throw new IllegalStateException("There is already a ServerBusinessHelper for the " + module.name() + " module!");
        }
        references.put(module, this);
        init(sessionInfo);
        this.module = module;
        if (automaticTransaction) {
            getResourceLocator().getEntityResource().activateAutomaticTransaction();
        }
    }

    private void init(SessionDTO sessionInfo) {
        if (ServerApplicationHelper.getInstance().getConfiguration().isBusinessExecutionTimeTrace()) {
            start = System.currentTimeMillis();
        }
        contextInformation = new ServerContextInformation(sessionInfo);
    }

    /**
	 * Retrieve the ServerBusinessHelper instance from the thread local
	 * variable.
	 * 
	 * @return the helper
	 */
    public static ServerBusinessHelper getInstance(Module module) {
        Map<Module, ServerBusinessHelper> references = threadLocal.get();
        ServerBusinessHelper helper = references.get(module);
        if (helper == null) {
            throw new IllegalStateException("The ServerBusinessHelper is not available for the " + module.name() + " module!");
        }
        return helper;
    }

    /**
	 * Call the close method of the ServerBusinessHelper instance stored in the
	 * thread local variable and clean the thread local variable reference to
	 * it.
	 */
    public void close() {
        closeResources();
        Map<Module, ServerBusinessHelper> references = threadLocal.get();
        references.remove(module);
        if (references.size() == 0) {
            threadLocal.set(null);
        }
    }

    public ServiceLocator getServiceLocator() {
        if (serviceLocator == null) {
            serviceLocator = new ServiceLocator();
        }
        return serviceLocator;
    }

    public ResourceLocator getResourceLocator() {
        if (resourceLocator == null) {
            resourceLocator = new ResourceLocator();
        }
        return resourceLocator;
    }

    public boolean isToRollback() {
        return isToRollback;
    }

    public void setToRollback(boolean isToRollback) {
        this.isToRollback = isToRollback;
    }

    /**
	 * Release resources and measure request processing time.
	 */
    private void closeResources() {
        if (serviceLocator != null) {
            serviceLocator.close();
        }
        if (resourceLocator != null) {
            resourceLocator.close(isToRollback);
        }
        if (ServerApplicationHelper.getInstance().getConfiguration().isBusinessExecutionTimeTrace()) {
            long end = System.currentTimeMillis();
            ServerApplicationHelper.getInstance().getLog().info("Business execution time: " + (end - start));
        }
    }

    public ServerContextInformation getContextInformation() {
        return contextInformation;
    }

    /**
	 * Create a transaction and manage it.
	 */
    public void activateAutomaticTransaction() {
        getResourceLocator().getEntityResource().activateAutomaticTransaction();
    }
}
