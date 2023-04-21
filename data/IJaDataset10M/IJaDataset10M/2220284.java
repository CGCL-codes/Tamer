package tripleo.util;

public interface SchedulerListener {

    void handlerError(Exception e);

    void handleSuccess();

    public static class NullListener implements SchedulerListener {

        public void handlerError(Exception e) {
        }

        public void handleSuccess() {
        }
    }
}
