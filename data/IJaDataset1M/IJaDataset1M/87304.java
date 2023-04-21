package rotorsim.main;

/**
 * @author Shannon Smith
 */
public class ResourceNotFoundException extends RuntimeException {

    public String resName;

    public ResourceNotFoundException(String resName) {
        this.resName = resName;
    }

    public String getMessage() {
        return "No such resource: " + resName;
    }
}
