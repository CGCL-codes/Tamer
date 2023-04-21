package org.ourgrid.matchers;

import org.easymock.IArgumentMatcher;
import org.easymock.classextension.EasyMock;
import br.edu.ufcg.lsd.commune.identification.DeploymentID;

public class StartWorkErrorMessageMatcher implements IArgumentMatcher {

    private final DeploymentID mgID;

    private final String problematicDir;

    private final boolean playpenError;

    public StartWorkErrorMessageMatcher(DeploymentID mgID, String problematicDir, boolean playpenError) {
        this.problematicDir = problematicDir.replace("\\\\", "\\").replace("\\", "\\\\");
        this.mgID = mgID;
        this.playpenError = playpenError;
    }

    public void appendTo(StringBuffer arg0) {
    }

    public boolean matches(Object arg0) {
        if (arg0.getClass() != String.class) {
            return false;
        }
        String anotherMessage = (String) arg0;
        String pattern = "The client [" + mgID + "] tried to start the work of this Worker, " + "but the " + (this.playpenError ? "playpen" : "storage") + " directory ";
        return anotherMessage.startsWith(pattern) && anotherMessage.endsWith("cannot be created.");
    }

    public static String eqMatcher(DeploymentID mgID, String problematicDir, boolean playpenError) {
        EasyMock.reportMatcher(new StartWorkErrorMessageMatcher(mgID, problematicDir, playpenError));
        return null;
    }
}
