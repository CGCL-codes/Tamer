package com.agileprojectassistant.client.project;

import com.agileprojectassistant.shared.ProjectProxy;

public class AbstractProjectFactory {

    public static AbstractProject createAbstractProject(ProjectProxy proxy) {
        AbstractProject project = null;
        switch(proxy.getType()) {
            case AGILE:
                project = new AgileProject(proxy);
                break;
        }
        return project;
    }
}
