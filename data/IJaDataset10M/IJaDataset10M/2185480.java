package powermock.examples.suppress.method;

/**
 * This example demonstrates PowerMock abilities to suppress methods.
 */
public class ExampleWithEvilMethod {

    private final String message;

    public ExampleWithEvilMethod(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message + getEvilMessage();
    }

    private String getEvilMessage() {
        System.loadLibrary("evil.dll");
        return "evil!";
    }
}
