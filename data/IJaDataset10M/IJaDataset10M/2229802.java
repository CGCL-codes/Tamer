package org.nomadpim.module.schedule.ui.component.tasktree;

import java.util.List;
import org.nomadpim.core.entity.IEntity;

public interface ITaskTreeEntry {

    List<ITaskTreeEntry> getChildren();

    int getFinished();

    int getTaskCount();

    String getDescription();

    IEntity getObject();
}
