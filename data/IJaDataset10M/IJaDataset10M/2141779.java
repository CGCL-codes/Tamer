package com.ibm.realtime.flexotask.scheduling.reactive;

import com.ibm.realtime.flexotask.FileDescriptor;
import com.ibm.realtime.flexotask.scheduling.ConnectionDriver;
import com.ibm.realtime.flexotask.scheduling.FlexotaskController;
import com.ibm.realtime.flexotask.scheduling.NonAllocatingFifo;
import com.ibm.realtime.flexotask.scheduling.NonAllocatingFifoElement;
import com.ibm.realtime.flexotask.template.FlexotaskTemplateElement;
import com.ibm.realtime.flexotask.template.FlexotaskValidationException;
import com.ibm.realtime.flexotask.timing.FlexotaskTimingData;
import com.ibm.realtime.flexotask.timing.reactive.ClockAnnotation;
import com.ibm.realtime.flexotask.util.ESystem;

/**
 * Represents one runnable element (connection or task) for the ReactiveScheduler
 */
public class ReactiveRunnableElement extends NonAllocatingFifoElement {

    private static final boolean DEBUG = false;

    private FlexotaskTemplateElement templateElement;

    private Runnable toRun;

    private FileDescriptor parameter;

    private ReactiveRunnableElement[] successors;

    private ReactiveRunnableElement[] predecessors;

    private boolean moving;

    private long period;

    /**
	 * Make a new element for a non-source task or for a connection
	 * @param element the template element for this runnable element
	 * @param toRun the Runnable for this runnable element
	 */
    public ReactiveRunnableElement(FlexotaskTemplateElement element, Runnable toRun) {
        this.templateElement = element;
        this.toRun = toRun;
    }

    /**
	 * Make a new element for a source task, which must actually react to input
	 * @param element the template element for this runnable element
	 * @param toRun the Runnable for this runnable element
	 * @param parameter the parameter being passed to the task.  Either task must have a ClockAnnotation or this parameter
	 *   must be a FileDescriptor
	 * @throws FlexotaskValidationException if the parameter is null or of the wrong type
	 */
    public ReactiveRunnableElement(FlexotaskTemplateElement element, Runnable toRun, Object parameter) throws FlexotaskValidationException {
        this(element, toRun);
        if (parameter instanceof FileDescriptor) {
            this.parameter = (FileDescriptor) parameter;
        } else {
            FlexotaskTimingData timing = element.getTimingData();
            if (timing instanceof ClockAnnotation) {
                period = ((ClockAnnotation) timing).getPeriod();
            } else {
                throw new FlexotaskValidationException("Source task " + element.getName() + " has neither a FileDescriptor parameter nor a clock annotation; " + "one or the other is needed for the reactive scheduler");
            }
        }
    }

    /**
	 * Add a predecessor, which is always a connection feeding one of this task's input ports.
	 * Connections don't have predecessors
	 * @param connectionElement the task element
	 */
    public void addPredecessor(ReactiveRunnableElement connectionElement) {
        if (predecessors == null) {
            predecessors = new ReactiveRunnableElement[] { connectionElement };
        } else {
            int oldlen = predecessors.length;
            ReactiveRunnableElement[] newArray = new ReactiveRunnableElement[oldlen + 1];
            System.arraycopy(predecessors, 0, newArray, 0, oldlen);
            predecessors = newArray;
            predecessors[oldlen] = connectionElement;
        }
    }

    /**
	 * Add a successor, which is always a task connected by one hop in the forward direction.
	 * Connections don't have successors.
	 * @param taskElement the task element
	 */
    public void addSuccessor(ReactiveRunnableElement taskElement) {
        if (successors == null) {
            successors = new ReactiveRunnableElement[] { taskElement };
        } else {
            int oldlen = successors.length;
            ReactiveRunnableElement[] newArray = new ReactiveRunnableElement[oldlen + 1];
            System.arraycopy(successors, 0, newArray, 0, oldlen);
            successors = newArray;
            successors[oldlen] = taskElement;
        }
    }

    /**
	 * @return the file descriptor if this is a source task with a file descriptor or -1 if not
	 */
    public int getFileDescriptor() {
        return parameter == null ? -1 : parameter.getFileDescriptor();
    }

    /**
	 * Get the period (if zero, this task is not periodic)
	 * @return the specified period
	 */
    public long getPeriod() {
        return period;
    }

    /**
	 * @return the template element for this element
	 */
    public FlexotaskTemplateElement getTemplate() {
        return templateElement;
    }

    /**
	 * Used to avoid adding successors more than once (note that when there are no output ports, there will never be
	 *   successors and this will always return false, but that is only a very minor inefficiency and won't cause incorrect
	 *   results)
	 * @return true if this node already has successors
	 */
    public boolean hasSuccessors() {
        return successors != null;
    }

    /** Run this element */
    public void run(NonAllocatingFifo runQueue) {
        if (toRun instanceof ConnectionDriver) {
            ConnectionDriver driver = (ConnectionDriver) toRun;
            if (moving) {
                if (DEBUG) {
                    ESystem.err.print("Moving on ");
                    ESystem.err.println(driver.toString());
                }
                driver.move();
            } else {
                if (DEBUG) {
                    ESystem.err.print("Copying on ");
                    ESystem.err.println(driver.toString());
                }
                driver.copy();
            }
        } else {
            if (predecessors != null) {
                if (DEBUG) {
                    ESystem.err.print("Running ");
                    ESystem.err.print(predecessors.length);
                    ESystem.err.println(" predecessors");
                }
                for (int i = 0; i < predecessors.length; i++) {
                    predecessors[i].run(runQueue);
                }
            }
            FlexotaskController controller = (FlexotaskController) toRun;
            if (DEBUG) {
                ESystem.err.print("Running ");
                ESystem.err.println(controller.toString());
            }
            controller.run();
            controller.collect();
        }
        if (successors == null) {
            return;
        }
        for (int i = 0; i < successors.length; i++) {
            ReactiveRunnableElement successor = successors[i];
            for (ReactiveRunnableElement elem = (ReactiveRunnableElement) runQueue.getFirst(); elem != null; elem = (ReactiveRunnableElement) elem.getNext()) {
                if (elem == successor) {
                    runQueue.remove(elem);
                }
            }
            runQueue.add(successor);
        }
    }

    /**
	 * Sets this element to be a moving connection (only called on connections)
	 */
    public void setMoving() {
        moving = true;
    }
}
