package org.inqle.user.domain.workflow;

import java.net.URI;
import java.util.List;
import org.inqle.user.data.rdf.RDF;
import org.inqle.user.data.rdf.persistence.APersistable;

public class Status extends APersistable implements IStatus {

    public static final URI COMPLETED_TASKS = URI.create(RDF.INQLE + "completedTask");

    public void completed(URI completedTaskUri) {
        addAttribute(COMPLETED_TASKS, completedTaskUri);
    }

    public boolean hasCompleted(URI taskUri) {
        List<URI> completedTasks = getUriValues(COMPLETED_TASKS);
        return completedTasks.contains(taskUri);
    }
}
