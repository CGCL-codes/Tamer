package info.monami.osgi.osgi4ami.service;

public interface Service {

    public static final int EVENT_ENABLED = 0;

    public static final int EVENT_DISABLED = 1;

    public static final String SERVICE_SERIAL = null;

    public static final String SERVICE_CATEGORY = null;

    public static final String SERVICE_PROVIDER = null;

    public String getServiceID();

    public String getServiceDescription();

    public void setServiceDescription(String description);

    public int enable();

    public int disable();

    public boolean isEnabled();

    public boolean registerServiceListener(ServiceListener listener);

    public boolean unregisterServiceListener(ServiceListener listener);
}
