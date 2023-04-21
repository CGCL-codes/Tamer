package domain;

import java.io.Serializable;

public class ImplementationTask extends AbstractTask implements Serializable {

    private static final long serialVersionUID = 1L;

    private TaskStatus status;

    private Long totalEstimate;

    private Long todoEstimate;

    public ImplementationTask() {
    }

    /**
	 * @param name
	 */
    public ImplementationTask(final String name) {
        this(name, null, null, null);
    }

    /**
	 * @param name
	 * @param status
	 * @param todoEstimate
	 * @param totalEstimate
	 */
    public ImplementationTask(final String name, final TaskStatus status) {
        this(name, status, null, null);
    }

    /**
	 * @param name
	 * @param status
	 * @param todoEstimate
	 */
    public ImplementationTask(final String name, final TaskStatus status, final Long totalEstimate) {
        this(name, status, totalEstimate, totalEstimate);
    }

    /**
	 * @param name
	 * @param status
	 * @param todoEstimate
	 * @param totalEstimate
	 * Use this constructor if you want to explicitely set the todo-estimate to
	 * be different from the total extimate at construction time.
	 */
    public ImplementationTask(final String name, final TaskStatus status, final Long totalEstimate, final Long todoEstimate) {
        this.setName(name);
        this.status = status;
        this.todoEstimate = todoEstimate;
        this.totalEstimate = totalEstimate;
    }

    /**
	 * @return the status
	 */
    public TaskStatus getStatus() {
        return status;
    }

    /**
	 * @param status the status to set
	 */
    public void setStatus(final TaskStatus status) {
        this.status = status;
    }

    /**
	 * @return the totalEstimate
	 */
    public Long getTotalEstimate() {
        return totalEstimate;
    }

    /**
	 * @param totalEstimate the totalEstimate to set
	 */
    public void setTotalEstimate(final Long totalEstimate) {
        this.totalEstimate = totalEstimate;
    }

    /**
	 * @return the todoEstimate
	 */
    public Long getTodoEstimate() {
        return todoEstimate;
    }

    /**
	 * @param todoEstimate the todoEstimate to set
	 */
    public void setTodoEstimate(final Long todoEstimate) {
        this.todoEstimate = todoEstimate;
    }
}
