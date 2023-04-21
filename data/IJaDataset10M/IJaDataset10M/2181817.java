package uk.ac.lkl.migen.system.util.gwt;

public class GwtTimerFactory implements TimerFactory {

    @Override
    public Timer getTimer(Runnable runnable) {
        return new TimerImpl(runnable);
    }

    class TimerImpl implements Timer {

        private com.google.gwt.user.client.Timer internalTimer = null;

        public TimerImpl(final Runnable runnable) {
            this.internalTimer = new com.google.gwt.user.client.Timer() {

                @Override
                public void run() {
                    runnable.run();
                }
            };
        }

        @Override
        public void schedule(int period) {
            internalTimer.scheduleRepeating(period);
        }

        @Override
        public void cancel() {
            internalTimer.cancel();
        }
    }
}
