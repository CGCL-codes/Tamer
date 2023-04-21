package org.lcx.scrumvision.unuse;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.mylyn.tasks.core.ITask.PriorityLevel;
import org.lcx.scrumvision.core.IScrumVisionClient;
import org.lcx.scrumvision.core.IScrumVisionConstants;
import org.lcx.scrumvision.core.ScrumVisionAttribute;
import org.lcx.scrumvision.core.ScrumVisionCorePlugin;
import org.lcx.scrumvision.core.IScrumVisionConstants.TASK_PRIORITY;
import org.lcx.scrumvision.core.IScrumVisionConstants.TASK_STATUS;
import org.lcx.scrumvision.core.model.ScrumVisionTaskKey.Key;
import com.google.gdata.data.spreadsheet.ListEntry;

/**
 * @author Laurent Carbonnaux
 */
public class ScrumVisionTask {

    private final Map<Key, String> valueByKey = new HashMap<Key, String>();

    private String taskId;

    private String repositoryUrl;

    private String url;

    public ScrumVisionTask(String repositoryUrl, ListEntry task) {
        this.setTaskId(task.getCustomElements().getValue(ScrumVisionAttribute.TASKID.getSvKey()));
        this.setUrl(repositoryUrl + IScrumVisionClient.TASK_URL_PREFIX + this.getTaskId());
        this.setRepositoryUrl(repositoryUrl);
        this.updateFromFeed(task);
    }

    public String getConnectorKind() {
        return ScrumVisionCorePlugin.CONNECTOR_KIND;
    }

    public boolean isLocal() {
        return false;
    }

    public void setPriorityFromFeed(String priority) {
        setPriority(PriorityLevel.getDefault().toString());
        if (priority != null && TASK_PRIORITY.valueOf(priority) != null) {
            String p = TASK_PRIORITY.valueOf(priority).getPriority();
            if (p != null) setPriority(p);
        }
    }

    private void setPriority(String string) {
    }

    /**
	 * Update the task when task list is created
	 * @param task
	 */
    public void updateFromFeed(ListEntry task) {
        Set<String> tags = task.getCustomElements().getTags();
        for (String tag : tags) {
            valueByKey.put(Key.fromKey(tag), task.getCustomElements().getValue(tag));
        }
        String summary = task.getCustomElements().getValue(ScrumVisionAttribute.SUMMARY.getSvKey());
        if (isLevelInSummary()) {
            String[] levels = new String[tags.size()];
            for (String tag : tags) {
                if (tag.startsWith(ScrumVisionAttribute.LN.getSvKey())) {
                    String l = tag.substring(ScrumVisionAttribute.LN.getSvKey().length());
                    int level = Integer.parseInt(l);
                    levels[level] = task.getCustomElements().getValue(tag);
                }
            }
            summary = this.getLongSummary(levels, summary);
        }
        this.setPriorityFromFeed(task.getCustomElements().getValue(ScrumVisionAttribute.PRIORITY.getSvKey()));
        String status = task.getCustomElements().getValue(ScrumVisionAttribute.STATUS.getSvKey());
        this.setStatus(status);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public Map<Key, String> getValueByKey() {
        return valueByKey;
    }

    public String getRepositoryUrl() {
        return repositoryUrl;
    }

    public void setRepositoryUrl(String repositoryUrl) {
        this.repositoryUrl = repositoryUrl;
    }

    public String getLongSummary(String[] levels, String summary) {
        IPreferenceStore store = ScrumVisionCorePlugin.getDefault().getPreferenceStore();
        String sl = "";
        int i = 0;
        for (String s : levels) {
            if (s != null) {
                i++;
                String key = IScrumVisionConstants.PREF_LEVEL_NOT_IN_SUMMARY + this.getRepositoryUrl() + i;
                if (store.getBoolean(key)) {
                    sl = sl + s + " - ";
                }
            }
        }
        sl = sl + summary;
        return sl;
    }

    /**
	 * @return the isLevelNotInSummary
	 */
    public static boolean isLevelInSummary() {
        boolean isLevelInSummary = !(ScrumVisionCorePlugin.getDefault().getPreferenceStore().getBoolean(IScrumVisionConstants.PREF_LEVEL_NOT_IN_SUMMARY));
        return isLevelInSummary;
    }

    /**
	 * @return the status
	 */
    public String getStatus() {
        return valueByKey.get(Key.STATUS);
    }

    /**
	 * @param status the status to set
	 */
    public void setStatus(String status) {
        if (status != null && status.equalsIgnoreCase(TASK_STATUS.COMPLETED.toString())) {
            this.setCompleted(true);
        } else {
            this.setCompleted(false);
        }
    }

    private void setCompleted(boolean b) {
    }
}
