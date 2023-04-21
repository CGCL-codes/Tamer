package $package;

/**
 * A typical simple backing bean, that is backed to <code>helloworld.jsp</code>
 * 
 * @author <a href="mailto:matzew@apache.org">Matthias We�endorf</a> 
 */
public class HelloWorldBacking {

    private String name;

    /**
     * default empty constructor
     */
    public HelloWorldBacking() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Method that is backed to a submit button of a form.
     */
    public String send() {
        return ("success");
    }
}
