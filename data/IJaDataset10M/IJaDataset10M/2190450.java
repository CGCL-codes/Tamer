package xdoclet.loader;

/**
 * @created   7. april 2002
 */
public class SubTaskDefinition {

    public final String name;

    public final String implementationClass;

    public final String parentTaskClass;

    public SubTaskDefinition(String name, String implementationClass, String parentTaskClass) {
        this.name = name;
        this.implementationClass = implementationClass;
        this.parentTaskClass = parentTaskClass;
    }
}
