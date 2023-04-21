package scrum.client.issues;

import java.util.*;

public abstract class GShowSuspendedIssuesAction extends scrum.client.common.AScrumAction {

    public GShowSuspendedIssuesAction() {
    }

    @Override
    public boolean isExecutable() {
        return true;
    }
}
