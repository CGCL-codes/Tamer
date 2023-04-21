package org.nakedobjects.application.tracker;

import org.nakedobjects.applib.annotation.Named;

public interface IssuesRepository {

    public abstract Issue[] issuesAssignedToMe();

    public abstract Issue findIssuesById(@Named("ID") int number);

    public abstract Issue searchFor(@Named("Priority") Priority priority);
}
