package de.creatronix.artist3k.jbpl;

import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.taskmgmt.def.AssignmentHandler;
import org.jbpm.taskmgmt.exe.Assignable;

public class NappyAssignmentHandler implements AssignmentHandler {

    /**
     *
     */
    private static final long serialVersionUID = 1124431342660540499L;

    public void assign(Assignable assignable, ExecutionContext arg1) throws Exception {
        assignable.setActorId("papa");
    }
}
