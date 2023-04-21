package com.googlecode.jazure.sdk.lifecycle;

public abstract class LifeCycles {

    public static LifeCycleWrapper wrapped() {
        return new LifeCycleWrapper() {

            private volatile boolean running;

            @Override
            public boolean isRunning() {
                return running;
            }

            @Override
            public void start(Runnable startCommand) {
                if (running) {
                    throw new IllegalStateException("Already running!");
                }
                startCommand.run();
                running = true;
            }

            @Override
            public void stop(Runnable stopCommand) {
                if (!running) {
                    throw new IllegalStateException("Already stopped!");
                }
                stopCommand.run();
                running = false;
            }
        };
    }

    public static LifeCycledRunnable run(Runnable runnable, LifeCycle lifeCycle) {
        return new LifeCycledRunnable(runnable, lifeCycle);
    }
}
