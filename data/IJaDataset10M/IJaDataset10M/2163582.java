package org.thechiselgroup.choosel.workbench.client.init;

import java.util.List;
import java.util.Map;
import com.google.gwt.user.client.Window;

public class DefaultWindowLocation implements WindowLocation {

    @Override
    public String getParameter(String name) {
        return Window.Location.getParameter(name);
    }

    @Override
    public Map<String, List<String>> getParameterMap() {
        return Window.Location.getParameterMap();
    }
}
