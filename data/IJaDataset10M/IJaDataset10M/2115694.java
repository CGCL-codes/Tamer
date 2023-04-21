package net.abhijat.se.process.integration.jmx;

public abstract class AbstractMBean implements LifeCycleEventListener {

    protected static final org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(AbstractMBean.class);

    private String name;

    private int state;

    private LifeCycleEventManager eventManager;

    public AbstractMBean() {
        this.eventManager = new LifeCycleEventManager();
        this.eventManager.addListener(this);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public int getState() {
        return this.state;
    }

    public String getStateString() {
        return String.valueOf(getState());
    }

    public void jbossInternalLifecycle(String arg0) throws Exception {
        if (log.isInfoEnabled()) {
            log.info("[jbossInternalLifecycle()] firing event named:" + arg0);
        }
        eventManager.fireNamedEvent(arg0);
    }
}
