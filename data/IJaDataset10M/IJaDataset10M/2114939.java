package org.butu.bot.client;

import org.butu.bot.model.Project;
import org.butu.paged.IPagedService;

public interface IProjectService extends IPagedService<Project> {

    public Project save(Project project);

    public void delete(Project... projects);
}
