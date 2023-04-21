package stamina.kernel.environment.event;

public interface HttpBeanNewHeaderListener extends EnvironmentEventListener {

    void httpBeanNewHeader(HttpBeanEvent e);
}
