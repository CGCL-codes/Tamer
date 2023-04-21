package jhomenet.hw.driver;

public abstract class HardwareDriverException extends Exception {

    public HardwareDriverException(Exception e) {
        super(e);
    }

    public HardwareDriverException(String message) {
        super(message);
    }
}
