package com.ecmdeveloper.plugin.model.tasks;

import com.ecmdeveloper.plugin.core.model.tasks.TaskResult;
import com.ecmdeveloper.plugin.core.model.tasks.classes.IRefreshClassDescriptionTask;
import com.ecmdeveloper.plugin.model.ClassDescription;
import com.ecmdeveloper.plugin.model.ContentEngineConnection;
import com.ecmdeveloper.plugin.util.PluginLog;

/**
 * @author Ricardo.Belfor
 *
 */
public class RefreshClassDescriptionTask extends BaseTask implements IRefreshClassDescriptionTask {

    private ClassDescription[] classDescriptions;

    public RefreshClassDescriptionTask(ClassDescription[] classDescriptions) {
        this.classDescriptions = classDescriptions;
    }

    @Override
    public ClassDescription[] getClassDescriptions() {
        return classDescriptions;
    }

    @Override
    protected Object execute() throws Exception {
        for (ClassDescription classDescription : classDescriptions) {
            try {
                classDescription.refresh();
            } catch (Exception e) {
                PluginLog.error(e);
            }
        }
        fireTaskCompleteEvent(TaskResult.COMPLETED);
        return null;
    }

    @Override
    protected ContentEngineConnection getContentEngineConnection() {
        if (classDescriptions.length == 0) {
            throw new IllegalArgumentException();
        }
        return (ContentEngineConnection) classDescriptions[0].getObjectStore().getConnection();
    }
}
