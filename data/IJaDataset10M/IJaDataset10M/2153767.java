package org.ztemplates.web;

import org.zclasspath.ZIClassRepository;
import org.ztemplates.actions.ZIActionApplicationContext;
import org.ztemplates.render.ZIRenderApplicationContext;
import org.ztemplates.web.script.zscript.ZIJavaScriptRepository;

public interface ZIApplicationService extends ZIService {

    public static final String SPRING_NAME = "ZIApplicationService";

    public ZIClassRepository getClassRepository();

    public ZIJavaScriptRepository getJavaScriptRepository();

    public ZIRenderApplicationContext getRenderApplicationContext();

    public ZIActionApplicationContext getActionApplicationContext();
}
