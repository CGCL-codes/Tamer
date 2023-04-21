package cartago;

import java.lang.reflect.*;

/**
 * Default implementation of operations (based on artifact class methods) 
 * 
 * @author aricci
 *
 */
public class ArtifactOpMethod implements IArtifactOp {

    private Method method;

    private Artifact artifact;

    public ArtifactOpMethod(Artifact artifact, Method method) {
        this.method = method;
        this.artifact = artifact;
        method.setAccessible(true);
    }

    public void exec(Object[] actualParams) throws Exception {
        method.invoke(artifact, actualParams);
    }

    public int getNumParameters() {
        return method.getParameterTypes().length;
    }

    public String getName() {
        return method.getName();
    }

    public boolean isVarArgs() {
        return method.isVarArgs();
    }

    public Method getMethod() {
        return method;
    }
}
