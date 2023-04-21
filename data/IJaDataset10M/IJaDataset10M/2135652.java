package aurora.database.actions;

import uncertain.composite.CompositeMap;
import uncertain.composite.DynamicObject;
import uncertain.composite.TextParser;
import uncertain.exception.BuiltinExceptionFactory;
import uncertain.logging.ILogger;
import uncertain.ocm.IConfigurable;
import uncertain.proc.AbstractEntry;
import uncertain.proc.ProcedureRunner;
import aurora.database.service.BusinessModelService;
import aurora.database.service.DatabaseServiceFactory;
import aurora.database.service.ServiceOption;
import aurora.database.service.SqlServiceContext;

public abstract class AbstractModelAction extends AbstractEntry {

    String mModel;

    DatabaseServiceFactory mServiceFactory;

    BusinessModelService mService;

    ILogger mLogger;

    CompositeMap mEntryConfig;

    public AbstractModelAction(DatabaseServiceFactory svcFactory) {
        this.mServiceFactory = svcFactory;
    }

    protected void transferServiceOption(ServiceOption option, String key) {
        if (mEntryConfig != null) option.getObjectContext().put(key, mEntryConfig.get(key));
    }

    protected void prepareServiceOption(ServiceOption option) {
    }

    protected void prepareRun(CompositeMap context_map) throws Exception {
        if (mModel == null) throw BuiltinExceptionFactory.createAttributeMissing(this, "model");
        SqlServiceContext context = (SqlServiceContext) DynamicObject.cast(context_map, SqlServiceContext.class);
        mService = mServiceFactory.getModelService(TextParser.parse(mModel, context_map), context_map);
        mLogger = DatabaseServiceFactory.getLogger(context_map);
        ServiceOption option = ServiceOption.createInstance();
        prepareServiceOption(option);
        context.setServiceOption(option);
    }

    public BusinessModelService getService() {
        return mService;
    }

    public DatabaseServiceFactory getServiceFactory() {
        return mServiceFactory;
    }

    public abstract void run(ProcedureRunner runner) throws Exception;

    /**
     * @return the model
     */
    public String getModel() {
        return mModel;
    }

    /**
     * @param model the model to set
     */
    public void setModel(String model) {
        this.mModel = model;
    }

    protected ILogger getLogger() {
        return mLogger;
    }

    public void beginConfigure(CompositeMap config) {
        super.beginConfigure(config);
        mEntryConfig = config;
    }

    public String getName() {
        return super.getName() + "[" + getModel() + "]";
    }
}
