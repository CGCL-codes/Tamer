package org.jcvi.vics.model.tasks.prokAnnotation;

import org.jcvi.vics.model.tasks.Event;
import org.jcvi.vics.model.tasks.TaskParameter;
import org.jcvi.vics.model.user_data.Node;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: tsafford
 * Date: Dec 15, 2008
 * Time: 3:03:13 PM
 */
public class ProkaryoticAnnotationBulkTask extends ProkaryoticAnnotationTask {

    public static final transient String TASK_NAME = "prokAnnotationBulkTask";

    public static final transient String DISPLAY_NAME = "Prokaryotic Annotation Bulk Pipeline";

    public static final transient String PARAM_genomeListFile = "genomeListFile";

    public ProkaryoticAnnotationBulkTask(Set<Node> inputNodes, String owner, List<Event> events, Set<TaskParameter> taskParameterSet) {
        super(inputNodes, owner, events, taskParameterSet);
        setDefaultValues();
    }

    public ProkaryoticAnnotationBulkTask() {
        super();
        setDefaultValues();
    }
}
