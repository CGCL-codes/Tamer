package hu.sztaki.lpds.information.util;

/**
 * @author krisztian karoczkai
 */
public class ServiceDescriptionBean {

    private String type;

    private boolean state;

    private String information;

    /**
 * Constructor
 */
    public ServiceDescriptionBean() {
    }

    /**
 * Constructor
 * @param type type of service (wspgrade,wfi,wfs,storage,repository,gemlcqquery,information,dcibridge)
 * @param state state of service (true = configuration is ok)
 * @param information service text for UI (error descripton)
 */
    public ServiceDescriptionBean(String type, boolean state, String information) {
        this.type = type;
        this.state = state;
        this.information = information;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
