package org.or5e.web.action;

import java.util.HashMap;
import java.util.Map;

public class NavigationAction extends BaseAction {

    private static final long serialVersionUID = -432704025590891949L;

    private static Map<String, String> pageMapping = new HashMap<String, String>();

    static {
        pageMapping.put("video", "dContent/VideoHome.jsp");
    }

    private String navigateTo = null;

    private String targetPage = null;

    public final String getTargetPage() {
        return targetPage;
    }

    public final void setTargetPage(String targetPage) {
        this.targetPage = targetPage;
    }

    public final String getNavigateTo() {
        return navigateTo;
    }

    public final void setNavigateTo(String navigateTo) {
        this.navigateTo = navigateTo;
    }

    @Override
    public String execute() throws Exception {
        targetPage = pageMapping.get(this.navigateTo);
        return super.execute();
    }
}
