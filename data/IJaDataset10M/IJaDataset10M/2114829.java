package net.sf.dbchanges;

import java.util.Map;
import net.sf.dbchanges.actions.ActionInfo;
import net.sf.dbchanges.actions.Actions;
import net.sf.dbchanges.actions.IAction;
import net.sf.dbchanges.actions.IActions;
import net.sf.dbchanges.actions.InitAction;
import net.sf.dbchanges.baseline.CreateVersionAction;
import net.sf.dbchanges.database.IDatabasesManager;
import net.sf.dbchanges.database.XmlDatabasesManager;
import net.sf.dbchanges.dbupdate.CheckAction;
import net.sf.dbchanges.dbupdate.DbUpdater;
import net.sf.dbchanges.dbupdate.IDbUpdater;
import net.sf.dbchanges.dbupdate.UpdateAction;
import net.sf.dbchanges.fs.Filesystem;
import net.sf.dbchanges.fs.IFilesystem;
import net.sf.dbchanges.plan.IPlanner;
import net.sf.dbchanges.plan.PlanAction;
import net.sf.dbchanges.plan.Planner;
import net.sf.dbchanges.state.IStateManager;
import net.sf.dbchanges.state.XmlStateManager;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;

/**
 * @author olex
 */
public class DbChangesModule {

    public static void bind(ServiceBinder binder) {
        binder.bind(IStateManager.class, XmlStateManager.class);
        binder.bind(IDatabasesManager.class, XmlDatabasesManager.class);
        binder.bind(IFilesystem.class, Filesystem.class);
        binder.bind(IPlanner.class, Planner.class);
        binder.bind(IDbUpdater.class, DbUpdater.class);
        binder.bind(CheckAction.class);
        binder.bind(UpdateAction.class);
        binder.bind(InitAction.class);
        binder.bind(PlanAction.class);
        binder.bind(CreateVersionAction.class);
    }

    public static IActions buildActions(Map<ActionInfo, IAction> extensions) {
        return new Actions(extensions);
    }

    public static void contributeActions(MappedConfiguration<ActionInfo, IAction> extensions, CheckAction check, UpdateAction update, InitAction init, PlanAction plan, CreateVersionAction createVersion) {
        extensions.add(CheckAction.INFO, check);
        extensions.add(UpdateAction.INFO, update);
        extensions.add(InitAction.INFO, init);
        extensions.add(PlanAction.INFO, plan);
        extensions.add(CreateVersionAction.INFO, createVersion);
    }
}
