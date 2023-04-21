package simpleorm.simplets.componentframework;

/** An error message to the user. */
public class HValidationException extends RuntimeException {

    HComponent component;

    String componentName;

    public HValidationException() {
        super();
    }

    public HValidationException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public HValidationException(String arg0) {
        super(arg0);
    }

    public HValidationException(Throwable arg0) {
        super(arg0);
    }

    public HComponent getComponent() {
        return component;
    }

    public void setComponent(HComponent component) {
        this.component = component;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }
}
