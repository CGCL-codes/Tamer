package whitecat.example;

import whitecat.core.annotations.PUBLICROLE;
import whitecat.core.annotations.ROLE;
import whitecat.core.role.IRole;
import whitecat.core.role.descriptors.annotation.AnnotationEventDescriptor;
import whitecat.core.role.descriptors.annotation.AnnotationRoleDescriptor;
import whitecat.core.role.descriptors.annotation.AnnotationTaskDescriptor;

/**
 * An example of role built with annotations, that can be used to provide a role
 * descriptor automatically.
 * 
 * @author Luca Ferrari - cat4hire (at) sourceforge.net
 * 
 */
@ROLE()
@PUBLICROLE(roleInterface = "whitecat.example.AnnotatedPublicRoleInterface", roleAnnotation = "whitecat.example.ExampleRoleAnnotation")
@AnnotationRoleDescriptor(aim = "Example AIM", name = "Example NAME", keywords = "keyword1, keyword2, keyword 3")
public class AnnotatedRoleExample implements IRole, AnnotatedPublicRoleInterface {

    /**
	 * A simple task. Does nothing on the value, returns it as it was provided.
	 */
    @AnnotationTaskDescriptor(aim = "Task AIM", name = "Task NAME", keywords = "taks1", taskID = "id1")
    public int exampleTask1(final int value) {
        return value;
    }

    /**
	 * Another task.
	 * 
	 * @return a string
	 */
    @AnnotationTaskDescriptor(aim = "Task2 AIM", name = "Task2 NAME", keywords = "taks2", taskID = "id2")
    public String exampleTask2() {
        return "Task2";
    }

    /**
	 * A task that is a subtask of task 2.
	 */
    @AnnotationTaskDescriptor(aim = "Task3 AIM", name = "Task3 NAME", keywords = "taks3", taskID = "id3", addToTaskID = "id2")
    @AnnotationEventDescriptor(aim = "Event AIM", name = "Event NAME", issuing = true, receiving = false)
    public String exampleTask3() {
        System.out.println("Executing task 3");
        return "Task3";
    }
}
