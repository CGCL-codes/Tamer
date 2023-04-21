package fr.cnes.sitools.tasks;

import java.util.logging.Level;
import org.restlet.Context;
import org.restlet.data.Method;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.security.User;
import fr.cnes.sitools.common.SitoolsSettings;
import fr.cnes.sitools.common.application.ContextAttributes;
import fr.cnes.sitools.common.application.SitoolsApplication;
import fr.cnes.sitools.common.model.Response;
import fr.cnes.sitools.common.resource.SitoolsParameterizedResource;
import fr.cnes.sitools.plugins.resources.model.ResourceModel;
import fr.cnes.sitools.plugins.resources.model.ResourceParameter;
import fr.cnes.sitools.server.Consts;
import fr.cnes.sitools.tasks.business.Task;
import fr.cnes.sitools.tasks.business.TaskManager;
import fr.cnes.sitools.tasks.model.TaskModel;
import fr.cnes.sitools.tasks.model.TaskResourceModel;
import fr.cnes.sitools.tasks.model.TaskRunTypeAdministration;
import fr.cnes.sitools.tasks.model.TaskRunTypeUserInput;

/**
 * Utils Class to run Tasks Synchronously or Asynchronously
 * 
 * 
 * @author m.gond (AKKA Technologies)
 */
public final class TaskUtils {

    /** PARENT_APPLICATION */
    public static final String PARENT_APPLICATION = "ParentApplication";

    /** Dataset_application */
    public static final String TASK = "Task";

    /** SITOOLS_SETTINGS */
    public static final String SITOOLS_SETTINGS = ContextAttributes.SETTINGS;

    /** LOG FOLDER */
    public static final String LOG_FOLDER = "LOG_FOLDER";

    /** THE BODY CONTENT */
    public static final String BODY_CONTENT = "BODY_CONTENT";

    /** TIMESTAMP_PATTERN */
    private static final String TIMESTAMP_PATTERN = "Starter.orderTimestampPattern";

    /**
   * Private constructor
   */
    private TaskUtils() {
        super();
    }

    /**
   * Gets the DataSetApplication contained in the Context
   * 
   * @param context
   *          the Context
   * @return the DataSetApplication contained in the Context
   */
    public static SitoolsParameterizedResource getDataSetApplication(Context context) {
        return (SitoolsParameterizedResource) context.getAttributes().get(PARENT_APPLICATION);
    }

    /**
   * Gets the Task contained in the Context
   * 
   * @param context
   *          the Context
   * @return the Task contained in the Context
   */
    public static Task getTask(Context context) {
        return (Task) context.getAttributes().get(TASK);
    }

    /**
   * Gets the DatabaseRequestParameters contained in the Context
   * 
   * @param context
   *          the Context
   * @return the DatabaseRequestParameters contained in the Context
   */
    public static SitoolsSettings getSitoolsSettings(Context context) {
        return (SitoolsSettings) context.getAttributes().get(SITOOLS_SETTINGS);
    }

    /**
   * Gets the timestamp pattern
   * 
   * @return the timestamp pattern
   */
    public static String getTimestampPattern() {
        return SitoolsSettings.getInstance().getString(TIMESTAMP_PATTERN);
    }

    /**
   * Execute a task described in the given resource It can be executed either synchronously or asynchronously depending
   * on the runType request and Resource parameter
   * 
   * @param resource
   *          the resource describing the task to execute
   * @param variant
   *          the variant needed for the response
   * @return a representation representing a TaskModel if the task is run asynchronously or the result if run
   *         synchronously
   */
    public static Representation execute(SitoolsParameterizedResource resource, Variant variant) {
        Representation represent = null;
        SitoolsSettings settings = ((SitoolsApplication) resource.getApplication()).getSettings();
        Context taskContext = resource.getContext().createChildContext();
        taskContext.getAttributes().put(TaskUtils.PARENT_APPLICATION, resource.getApplication());
        taskContext.getAttributes().put(TaskUtils.LOG_FOLDER, settings.getStoreDIR(Consts.APP_RESOURCE_LOGS_DIR));
        taskContext.getAttributes().put(ContextAttributes.SETTINGS, settings);
        taskContext.getAttributes().put(ContextAttributes.RESOURCE_ATTACHMENT, resource.getContext().getAttributes().get(ContextAttributes.RESOURCE_ATTACHMENT));
        if (resource.getContext().getAttributes().get(TaskUtils.BODY_CONTENT) != null) {
            taskContext.getAttributes().put(TaskUtils.BODY_CONTENT, resource.getContext().getAttributes().get(TaskUtils.BODY_CONTENT));
        }
        ResourceModel resourceModel = resource.getModel();
        if (resourceModel == null) {
            represent = resource.getRepresentation(new Response(false, "resource.not.found"), variant);
            return represent;
        } else {
            User user = resource.getRequest().getClientInfo().getUser();
            if (user == null) {
                user = new User("anonymous");
            }
            String loggerLvlStr = (String) resource.getRequest().getResourceRef().getQueryAsForm().getFirstValue("loggerLvl");
            Level loggerLvl = null;
            try {
                if (loggerLvlStr != null) {
                    loggerLvl = Level.parse(loggerLvlStr);
                }
            } catch (IllegalArgumentException e) {
                loggerLvl = null;
            }
            boolean persist = false;
            Method method = resource.getRequest().getMethod();
            String rootUrl = null;
            if (user != null && method.equals(Method.PUT) || method.equals(Method.POST) || method.equals(Method.DELETE)) {
                persist = true;
            }
            Task task = TaskManager.getInstance().createTask(taskContext, resource.getRequest(), resource.getResponse(), resourceModel, user, loggerLvl, persist);
            rootUrl = createRootUrl(resource, user);
            task.setRootUrl(rootUrl + "/" + task.getTaskId());
            task.setStatusUrl(task.getRootUrl());
            boolean runSync = false;
            boolean runAsync = false;
            ResourceParameter runTypeParam = resourceModel.getParameterByName(TaskResourceModel.RUN_TYPE_PARAM_NAME_ADMINISTATION);
            TaskRunTypeAdministration taskRunTypeAdministration = TaskRunTypeAdministration.valueOf(runTypeParam.getValue());
            if (method.equals(Method.GET) || method.equals(Method.OPTIONS)) {
                taskRunTypeAdministration = TaskRunTypeAdministration.TASK_FORCE_RUN_SYNC;
            }
            if (taskRunTypeAdministration == TaskRunTypeAdministration.TASK_FORCE_RUN_SYNC) {
                runSync = true;
            } else if (taskRunTypeAdministration == TaskRunTypeAdministration.TASK_FORCE_RUN_ASYNC) {
                runAsync = true;
            } else if (taskRunTypeAdministration == TaskRunTypeAdministration.TASK_DEFAULT_RUN_SYNC || taskRunTypeAdministration == TaskRunTypeAdministration.TASK_DEFAULT_RUN_ASYNC) {
                String runtypeParam = resource.getQuery().getFirstValue(TaskResourceModel.RUN_TYPE_PARAM_NAME_USER_INPUT);
                if (runtypeParam != null && TaskRunTypeUserInput.TASK_RUN_ASYNC.equals(TaskRunTypeUserInput.valueOf(runtypeParam))) {
                    runAsync = true;
                } else if (runtypeParam != null && TaskRunTypeUserInput.TASK_RUN_SYNC.equals(TaskRunTypeUserInput.valueOf(runtypeParam))) {
                    runSync = true;
                } else {
                    if (taskRunTypeAdministration == TaskRunTypeAdministration.TASK_DEFAULT_RUN_SYNC) {
                        runSync = true;
                    } else if (taskRunTypeAdministration == TaskRunTypeAdministration.TASK_DEFAULT_RUN_ASYNC) {
                        runAsync = true;
                    }
                }
            }
            if (runSync) {
                resource.getLogger().info(TaskRunTypeAdministration.TASK_DEFAULT_RUN_SYNC.toString());
                TaskManager.getInstance().runSynchrone(task);
                represent = task.getResult();
            }
            if (runAsync) {
                resource.getLogger().info(TaskRunTypeAdministration.TASK_DEFAULT_RUN_ASYNC.toString());
                TaskModel taskModel = task.getTaskModel();
                TaskManager.getInstance().runAsynchrone(task, taskContext);
                Response response = new Response(true, taskModel, TaskModel.class, "TaskModel");
                represent = resource.getRepresentation(response, variant);
            }
        }
        resource.getResponse().setEntity(represent);
        return represent;
    }

    /**
   * Creates the rootUrl
   * 
   * @param resource
   *          the resource
   * @param user
   *          the user
   * 
   * @return The root url
   */
    private static String createRootUrl(SitoolsParameterizedResource resource, User user) {
        SitoolsSettings settings = ((SitoolsApplication) resource.getApplication()).getSettings();
        String url = settings.getString(Consts.APP_URL) + settings.getString(Consts.APP_USERRESOURCE_ROOT_URL);
        url += "/" + user.getIdentifier();
        url += settings.getString(Consts.APP_TASK_URL);
        return url;
    }
}
