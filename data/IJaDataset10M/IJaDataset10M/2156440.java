package com.common.to;

public class ServiceTO extends BaseTO {

    /**
	 * 
	 */
    private static final long serialVersionUID = 8956554620793340205L;

    private String name;

    public ServiceTO(String pName) {
        super();
        this.name = pName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return name;
    }
}
