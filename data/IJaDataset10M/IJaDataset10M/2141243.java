package net.sf.doolin.context;

/**
 * The Class ApplicationTest.
 */
public class MyApplication extends AbstractApplication {

    private boolean started = false;

    private String name;

    /**
	 * {@inheritDoc}.
	 * 
	 * @param arguments
	 *            the arguments
	 */
    @Override
    public void start(String[] arguments) {
        started = true;
    }

    /**
	 * Checks if is started.
	 * 
	 * @return true, if is started
	 */
    public boolean isStarted() {
        return started;
    }

    /**
	 * Gets the name.
	 * 
	 * @return the name
	 */
    public String getName() {
        return name;
    }

    /**
	 * Sets the name.
	 * 
	 * @param name
	 *            the new name
	 */
    public void setName(String name) {
        this.name = name;
    }
}
