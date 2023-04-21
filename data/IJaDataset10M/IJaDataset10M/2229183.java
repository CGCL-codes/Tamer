package wilos.business.webservices.transfertobject;

import java.io.Serializable;
import wilos.model.spem2.task.TaskDescriptor;

/**
 * 
 * @author toine
 */
public class TaskDescriptorTO extends TaskDescriptor implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -5257048255787493710L;

    /** Creates a new instance of TaskDescriptorTO */
    public TaskDescriptorTO() {
    }

    public TaskDescriptorTO(TaskDescriptor myTaskDescriptor) {
        this.setName(myTaskDescriptor.getName());
        this.setPrefix(myTaskDescriptor.getPrefix());
        this.setGuid(myTaskDescriptor.getGuid());
        this.setDescription(myTaskDescriptor.getDescription());
        this.setPresentationName(myTaskDescriptor.getPresentationName());
        this.setInsertionOrder(myTaskDescriptor.getInsertionOrder());
        this.setMainDescription(myTaskDescriptor.getMainDescription());
        this.setKeyConsiderations(myTaskDescriptor.getKeyConsiderations());
        if (myTaskDescriptor.getTaskDefinition() != null) this.setTaskDefinition(new TaskDefinitionTO(myTaskDescriptor.getTaskDefinition()));
        if (this.getTaskDefinition() != null && this.getDescription().length() == 0) this.setDescription(this.getTaskDefinition().getDescription());
    }
}
