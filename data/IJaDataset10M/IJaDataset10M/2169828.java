package org.eclipse.epsilon.egl.symmetric_ao.tasks.superimpose;

import java.util.HashMap;
import java.util.Map;
import org.apache.tools.ant.Project;

/**
 * A registry of artifact handlers to make managing them easier.
 * 
 * @author zschaler
 * 
 */
public class ArtifactHandlerRegistry {

    private static Map<Project, ArtifactHandlerRegistry> s_mpSingleton = new HashMap<Project, ArtifactHandlerRegistry>();

    /**
	 * There's only one registry per project.
	 */
    public static ArtifactHandlerRegistry getInstance(Project project) {
        ArtifactHandlerRegistry ahrTentative = s_mpSingleton.get(project);
        if (ahrTentative == null) {
            ahrTentative = new ArtifactHandlerRegistry();
            s_mpSingleton.put(project, ahrTentative);
        }
        return ahrTentative;
    }

    private static final String JAVA15_HANDLER = "java15";

    private static final String XML_HANDLER = "xml";

    private Map<String, ArtifactHandler> mpRegistry = new HashMap<String, ArtifactHandler>();

    private ArtifactHandlerRegistry() {
        super();
        register(JAVA15_HANDLER, new Java15Handler());
        register(XML_HANDLER, new XmlHandler());
    }

    public ArtifactHandler getHandler(String handlerID) {
        return mpRegistry.get(handlerID);
    }

    public void register(String handlerID, ArtifactHandler handler) {
        mpRegistry.put(handlerID, handler);
    }
}
