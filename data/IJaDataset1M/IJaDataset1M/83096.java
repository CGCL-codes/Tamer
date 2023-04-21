package com.aytul.janissary.element;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Yalim Aytul
 * 
 * <p>
 * This class holds the application data for a scenario.
 * </p>
 */
public class Scenario {

    /**
     * <p>
     * Empty Constructor.
     * </p>
     */
    public Scenario() {
    }

    public Object getRequestGroup(int index) {
        return request_group.get(index);
    }

    public void addRequestGroup(RequestGroup requestgroup) {
        request_group.add(requestgroup);
    }

    /**
	 * @return Returns the name.
	 */
    public String getName() {
        return name;
    }

    /**
	 * @param name The name to set.
	 */
    public void setName(String name) {
        this.name = name;
    }

    /**
	 * @return Returns the request_group.
	 */
    public List getRequestGroups() {
        return request_group;
    }

    /**
	 * @param request_group The request_group to set.
	 */
    public void setRequestGroups(List request_group) {
        this.request_group = request_group;
    }

    private String name;

    private List request_group = new ArrayList();
}
