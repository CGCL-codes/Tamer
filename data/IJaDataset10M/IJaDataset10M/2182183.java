package de.objectcode.time4u.server.ejb;

import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import de.objectcode.time4u.StorePlugin;
import de.objectcode.time4u.server.IServerConnection;
import de.objectcode.time4u.server.ISynchronizeLog;
import de.objectcode.time4u.server.ServerException;
import de.objectcode.time4u.server.SynchronizeLog;
import de.objectcode.time4u.server.api.LoginService;
import de.objectcode.time4u.server.api.PersonService;
import de.objectcode.time4u.server.api.ProjectService;
import de.objectcode.time4u.server.api.WorkItemService;
import de.objectcode.time4u.server.util.PasswordEncoder;
import de.objectcode.time4u.store.IProjectStore;
import de.objectcode.time4u.store.IRepository;
import de.objectcode.time4u.store.IStatisticStore;
import de.objectcode.time4u.store.ISyncStore;
import de.objectcode.time4u.store.IWorkItemStore;
import de.objectcode.time4u.store.UserContext;

public class EJBServerConnection implements IServerConnection {

    final String m_serverName;

    final String m_userId;

    final ServerCommandExecutor m_executor;

    final PersonService m_personService;

    final ProjectService m_projectService;

    final WorkItemService m_workItemService;

    public EJBServerConnection(UserContext userContext, EJBServerConnectionParameters parameters) throws ServerException {
        try {
            EJBServerConnectionParameters param = (EJBServerConnectionParameters) parameters;
            m_serverName = param.getHostname();
            m_userId = userContext.getUserId();
            Properties registerEnv = new Properties();
            registerEnv.setProperty(Context.PROVIDER_URL, m_serverName);
            registerEnv.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
            registerEnv.setProperty(Context.URL_PKG_PREFIXES, "org.jboss.naming:org.jnp.interfaces");
            InitialContext registerCtx = new InitialContext(registerEnv);
            LoginService loginService = (LoginService) registerCtx.lookup("time4u-server/LoginServiceBean/remote");
            if (loginService.getApiVersion() != LoginService.CURRENT_VERSION) {
                throw new ServerException("Server version does not match");
            }
            loginService.registerLogin(userContext.getUserId(), userContext.getPassword());
            Properties env = new Properties();
            env.setProperty(Context.SECURITY_PRINCIPAL, userContext.getUserId());
            env.setProperty(Context.SECURITY_CREDENTIALS, userContext.getPassword());
            env.setProperty(Context.PROVIDER_URL, m_serverName);
            env.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.security.jndi.JndiLoginInitialContextFactory");
            env.setProperty(Context.URL_PKG_PREFIXES, "org.jboss.naming:org.jnp.interfaces");
            InitialContext ctx = new InitialContext(env);
            m_personService = (PersonService) ctx.lookup("time4u-server/PersonServiceBean/remote");
            m_projectService = (ProjectService) ctx.lookup("time4u-server/ProjectServiceBean/remote");
            m_workItemService = (WorkItemService) ctx.lookup("time4u-server/WorkItemServiceBean/remote");
            m_executor = new ServerCommandExecutor();
        } catch (Throwable e) {
            StorePlugin.getDefault().log(e);
            throw new ServerException(e);
        }
    }

    public void changePassword(String newPassword) throws ServerException {
        try {
            m_personService.changeLogin(m_userId, PasswordEncoder.encrypt(newPassword));
        } catch (Throwable e) {
            StorePlugin.getDefault().log(e);
            throw new ServerException(e);
        }
    }

    public void syncProjects(ISynchronizeLog log, IRepository repository) throws ServerException {
        try {
            log.addEntry(SynchronizeLog.Severity.INFO, SynchronizeLog.Scope.PROJECT, SynchronizeLog.State.CONNECT, "Start project synchronize");
            IProjectStore projectStore = repository.getProjectStore();
            ISyncStore syncStore = repository.getSyncStore(m_serverName);
            ProjectTaskSynchronizer synchronizer = new ProjectTaskSynchronizer(m_executor, m_projectService, projectStore, syncStore);
            if (Thread.interrupted()) {
                throw new InterruptedException("User abort");
            }
            synchronizer.pullProjectTasks(log, false);
            if (Thread.interrupted()) {
                throw new InterruptedException("User abort");
            }
            synchronizer.pushProjectTasks(log);
            log.addEntry(SynchronizeLog.Severity.INFO, SynchronizeLog.Scope.PROJECT, SynchronizeLog.State.CONNECT, "Finish project synchronize");
        } catch (Exception e) {
            StorePlugin.getDefault().log(e);
            log.addEntry(SynchronizeLog.Severity.ERROR, SynchronizeLog.Scope.PROJECT, SynchronizeLog.State.CONNECT, e.toString());
            throw new ServerException(e);
        }
    }

    public void syncProjectsFull(ISynchronizeLog log, IRepository repository) throws ServerException {
        try {
            log.addEntry(SynchronizeLog.Severity.INFO, SynchronizeLog.Scope.PROJECT, SynchronizeLog.State.CONNECT, "Start project full synchronize");
            IProjectStore projectStore = repository.getProjectStore();
            ISyncStore syncStore = repository.getSyncStore(m_serverName);
            ProjectTaskSynchronizer synchronizer = new ProjectTaskSynchronizer(m_executor, m_projectService, projectStore, syncStore);
            if (Thread.interrupted()) {
                throw new InterruptedException("User abort");
            }
            synchronizer.pullProjectTasks(log, true);
            if (Thread.interrupted()) {
                throw new InterruptedException("User abort");
            }
            synchronizer.pushProjectTasks(log);
            log.addEntry(SynchronizeLog.Severity.INFO, SynchronizeLog.Scope.PROJECT, SynchronizeLog.State.CONNECT, "Finish project full synchronize");
        } catch (Exception e) {
            StorePlugin.getDefault().log(e);
            log.addEntry(SynchronizeLog.Severity.ERROR, SynchronizeLog.Scope.PROJECT, SynchronizeLog.State.CONNECT, e.toString());
            throw new ServerException(e);
        }
    }

    public void syncWorkItems(ISynchronizeLog log, IRepository repository) throws ServerException {
        try {
            log.addEntry(SynchronizeLog.Severity.INFO, SynchronizeLog.Scope.WORKITEM, SynchronizeLog.State.CONNECT, "Start workitem synchronize");
            IWorkItemStore workItemStore = repository.getWorkItemStore();
            ISyncStore syncStore = repository.getSyncStore(m_serverName);
            IStatisticStore statisticStore = repository.getStatisticStore();
            WorkItemSynchronizer synchronizer = new WorkItemSynchronizer(m_executor, m_workItemService, workItemStore, syncStore);
            if (Thread.interrupted()) {
                throw new InterruptedException("User abort");
            }
            if ((syncStore.getDirtyDays().size() == 0) && (statisticStore.getMonthTotals().size() == 0)) {
                synchronizer.pullWorkItems(log);
            } else {
                synchronizer.pushWorkItems(log, false);
            }
            log.addEntry(SynchronizeLog.Severity.INFO, SynchronizeLog.Scope.WORKITEM, SynchronizeLog.State.CONNECT, "Finish workitem synchronize");
        } catch (Exception e) {
            StorePlugin.getDefault().log(e);
            log.addEntry(SynchronizeLog.Severity.ERROR, SynchronizeLog.Scope.WORKITEM, SynchronizeLog.State.CONNECT, e.toString());
            throw new ServerException(e);
        }
    }

    public void syncWorkItemsFull(ISynchronizeLog log, IRepository repository) throws ServerException {
        try {
            log.addEntry(SynchronizeLog.Severity.INFO, SynchronizeLog.Scope.WORKITEM, SynchronizeLog.State.CONNECT, "Start workitem full synchronize");
            IWorkItemStore workItemStore = repository.getWorkItemStore();
            ISyncStore syncStore = repository.getSyncStore(m_serverName);
            IStatisticStore statisticStore = repository.getStatisticStore();
            WorkItemSynchronizer synchronizer = new WorkItemSynchronizer(m_executor, m_workItemService, workItemStore, syncStore);
            if (Thread.interrupted()) {
                throw new InterruptedException("User abort");
            }
            if ((syncStore.getDirtyDays().size() == 0) && (statisticStore.getMonthTotals().size() == 0)) {
                synchronizer.pullWorkItems(log);
            } else {
                synchronizer.pushWorkItems(log, true);
            }
            log.addEntry(SynchronizeLog.Severity.INFO, SynchronizeLog.Scope.WORKITEM, SynchronizeLog.State.CONNECT, "Finish workitem full synchronize");
        } catch (Exception e) {
            StorePlugin.getDefault().log(e);
            log.addEntry(SynchronizeLog.Severity.ERROR, SynchronizeLog.Scope.WORKITEM, SynchronizeLog.State.CONNECT, e.toString());
            throw new ServerException(e);
        }
    }
}
