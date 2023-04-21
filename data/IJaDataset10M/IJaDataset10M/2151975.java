package mpmetr.core;

class SystemTime {

    private static final Long ZERO_TIME = System.currentTimeMillis();

    public Long getTime() {
        return System.currentTimeMillis() - ZERO_TIME;
    }
}
