package org.hip.vif.web.tasks;

import java.util.HashMap;
import java.util.Map;
import org.hip.vif.core.interfaces.IPluggableTask;
import org.hip.vif.web.controller.TaskManager;
import org.hip.vif.web.interfaces.ITaskConfiguration;
import org.hip.vif.web.interfaces.ITaskSet;
import org.hip.vif.web.util.UseCaseHelper;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

/**
 * Registry for all forwarding tasks.<br />
 * Tasks in bundles can forward to registered tasks as follows: 
 * <pre>sendEvent(ForwardTaskRegistry.ForwardQuestionShow.class);</pre>
 * 
 * @author Luthiger
 * Created: 28.09.2011
 */
public enum ForwardTaskRegistry {

    INSTANCE;

    public static final String FORWARD_REQUEST_LIST = "showRequestList";

    public static final String FORWARD_QUESTION_SHOW = "showQuestion";

    public static final String FORWARD_GROUP_ADMIN_PENDING = "showGroupAdminsPending";

    public static final String FORWARD_RATING_FORM = "showRatingForm";

    public static final String FORWARD_PWCHNGE_FORM = "changePWForm";

    private Map<String, String> targetRegistry = new HashMap<String, String>();

    private Map<String, Class<? extends IPluggableTask>> taskRegistry = new HashMap<String, Class<? extends IPluggableTask>>();

    /**
	 * Singleton constructor,
	 * fills the task manager with all forwarding tasks.
	 */
    private ForwardTaskRegistry() {
        taskRegistry.put(FORWARD_REQUEST_LIST, ForwardRequestsList.class);
        taskRegistry.put(FORWARD_QUESTION_SHOW, ForwardQuestionShow.class);
        taskRegistry.put(FORWARD_GROUP_ADMIN_PENDING, ForwardGroupAdminPending.class);
        taskRegistry.put(FORWARD_RATING_FORM, ForwardRatingForm.class);
        taskRegistry.put(FORWARD_PWCHNGE_FORM, ForwardPWChangeForm.class);
        TaskManager.INSTANCE.addForumTaskSet(new ForwardTaskSet());
    }

    /**
	 * Returns the task class that is registered with the specified alias. <br />
	 * This method is public, as clients have to call it.
	 * 
	 * @param inAlias String
	 * @return IPluggableTask class
	 */
    public Class<? extends IPluggableTask> getTask(String inAlias) {
        return taskRegistry.get(inAlias);
    }

    /**
	 * Returns the target class that is registered with the specified alias. <br />
	 * This method is package friendly, as only the <code>ForwardTask</code> has to call it.
	 * 
	 * @param inAlias String
	 * @return String the target's fully qualified class name
	 */
    String getTarget(String inAlias) {
        return targetRegistry.get(inAlias);
    }

    /**
	 * Registeres the specified target class with the specified alias.
	 * 
	 * @param inAlias String
	 * @param inTarget IPluggableTask class
	 */
    public void registerTarget(String inAlias, Class<? extends IPluggableTask> inTarget) {
        targetRegistry.put(inAlias, UseCaseHelper.createFullyQualifiedTaskName(inTarget));
    }

    /**
	 * Unregisteres the target task with the specified alias. 
	 * 
	 * @param inAlias String
	 */
    public void unregisterTarget(String inAlias) {
        targetRegistry.remove(inAlias);
    }

    /**
	 * The task set of all forwarding tasks is configured here.
	 */
    private static class ForwardTaskSet implements ITaskSet {

        final Bundle bundle = FrameworkUtil.getBundle(getClass());

        @Override
        public ITaskConfiguration[] getTaskConfigurations() {
            return new ITaskConfiguration[] { new ITaskConfiguration() {

                @Override
                public String getTaskName() {
                    return ForwardRequestsList.class.getName();
                }

                @Override
                public Bundle getBundle() {
                    return bundle;
                }
            }, new ITaskConfiguration() {

                @Override
                public String getTaskName() {
                    return ForwardQuestionShow.class.getName();
                }

                @Override
                public Bundle getBundle() {
                    return bundle;
                }
            }, new ITaskConfiguration() {

                @Override
                public String getTaskName() {
                    return ForwardGroupAdminPending.class.getName();
                }

                @Override
                public Bundle getBundle() {
                    return bundle;
                }
            }, new ITaskConfiguration() {

                @Override
                public String getTaskName() {
                    return ForwardRatingForm.class.getName();
                }

                @Override
                public Bundle getBundle() {
                    return bundle;
                }
            }, new ITaskConfiguration() {

                @Override
                public String getTaskName() {
                    return ForwardPWChangeForm.class.getName();
                }

                @Override
                public Bundle getBundle() {
                    return bundle;
                }
            } };
        }
    }

    public static class ForwardRequestsList extends ForwardTask {

        @Override
        protected String getAlias() {
            return FORWARD_REQUEST_LIST;
        }
    }

    public static class ForwardQuestionShow extends ForwardTask {

        @Override
        protected String getAlias() {
            return FORWARD_QUESTION_SHOW;
        }
    }

    public static class ForwardGroupAdminPending extends ForwardTask {

        @Override
        protected String getAlias() {
            return FORWARD_GROUP_ADMIN_PENDING;
        }
    }

    public static class ForwardRatingForm extends ForwardTask {

        @Override
        protected String getAlias() {
            return FORWARD_RATING_FORM;
        }
    }

    public static class ForwardPWChangeForm extends ForwardTask {

        @Override
        protected String getAlias() {
            return FORWARD_PWCHNGE_FORM;
        }
    }
}
