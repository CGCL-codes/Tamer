package musite.ui.task;

import cytoscape.task.Task;
import cytoscape.task.ui.JTaskConfig;
import cytoscape.task.util.TaskManager;
import java.awt.Frame;

/**
 *
 * @author Jianjiong Gao
 */
public final class TaskUtil {

    public static void execute(Task task) {
        execute(task, new TaskConfigBuilder().build());
    }

    public static void execute(Task task, Frame container) {
        execute(task, new TaskConfigBuilder().onwer(container).build());
    }

    public static void execute(Task task, JTaskConfig config) {
        TaskManager.executeTask(task, config);
    }

    public static class TaskConfigBuilder {

        private Frame onwer = musite.Musite.getDesktop();

        private boolean displayCloseButton = true;

        private boolean displayCancelButton = false;

        private boolean displayStatus = true;

        private boolean autoDispose = true;

        private boolean displayTimeElapsed = true;

        private boolean displayTimeRemaining = false;

        private boolean modal = true;

        private int millisToPopup = 100;

        public JTaskConfig build() {
            JTaskConfig config = new JTaskConfig();
            config.setOwner(onwer);
            config.displayCloseButton(displayCloseButton);
            config.displayCancelButton(displayCancelButton);
            config.displayStatus(displayStatus);
            config.setAutoDispose(autoDispose);
            config.displayTimeElapsed(displayTimeElapsed);
            config.displayTimeRemaining(displayTimeRemaining);
            config.setModal(modal);
            config.setMillisToPopup(millisToPopup);
            return config;
        }

        public TaskConfigBuilder onwer(Frame frame) {
            this.onwer = frame;
            return this;
        }

        public TaskConfigBuilder displayCloseButton(boolean displayCloseButton) {
            this.displayCloseButton = displayCloseButton;
            return this;
        }

        public TaskConfigBuilder displayCancelButton(boolean displayCancelButton) {
            this.displayCancelButton = displayCancelButton;
            return this;
        }

        public TaskConfigBuilder displayStatus(boolean displayStatus) {
            this.displayStatus = displayStatus;
            return this;
        }

        public TaskConfigBuilder autoDispose(boolean autoDispose) {
            this.autoDispose = autoDispose;
            return this;
        }

        public TaskConfigBuilder displayTimeElapsed(boolean displayTimeElapsed) {
            this.displayTimeElapsed = displayTimeElapsed;
            return this;
        }

        public TaskConfigBuilder displayTimeRemaining(boolean displayTimeRemaining) {
            this.displayTimeRemaining = displayTimeRemaining;
            return this;
        }

        public TaskConfigBuilder modal(boolean modal) {
            this.modal = modal;
            return this;
        }

        public TaskConfigBuilder millisToPopup(int millisToPopup) {
            this.millisToPopup = millisToPopup;
            return this;
        }
    }
}
