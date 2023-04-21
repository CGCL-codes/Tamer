package scrum.client.project;

import java.util.*;

public abstract class GSetRequirementCleanAction extends scrum.client.common.AScrumAction {

    protected scrum.client.project.Requirement requirement;

    public GSetRequirementCleanAction(scrum.client.project.Requirement requirement) {
        this.requirement = requirement;
    }

    @Override
    public boolean isExecutable() {
        return true;
    }

    @Override
    public String getId() {
        return ilarkesto.core.base.Str.getSimpleName(getClass()) + '_' + ilarkesto.core.base.Str.toHtmlId(requirement);
    }
}
