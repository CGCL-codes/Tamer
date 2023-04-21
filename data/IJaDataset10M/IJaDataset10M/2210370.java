package org.waveprotocol.wave.client.scheduler;

import com.google.gwt.user.client.Command;

/**
 * Interface for command queues.
 *
 */
public interface CommandQueue {

    /**
   * Command queue that uses our {@link ScheduleCommand} scheduler.
   */
    public static final CommandQueue HIGH_PRIORITY = new CommandQueue() {

        /** Schedule */
        public void addCommand(final Command command) {
            SchedulerInstance.getHighPriorityTimer().schedule(new Scheduler.Task() {

                public void execute() {
                    command.execute();
                }
            });
        }
    };

    void addCommand(Command c);
}
