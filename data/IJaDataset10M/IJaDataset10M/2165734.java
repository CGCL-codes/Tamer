package org.objectwiz.plugin.methodinvocation;

import org.objectwiz.core.Application;

/**
 * Helper methods for {@link MethodInvocationFacet}.
 *
 * @author Benoit Del Basso <benoit.delbasso at helmet.fr>
 */
public class MethodInvocationHelper {

    private MethodInvocationFacet facet;

    public static MethodInvocationHelper require(Application application) {
        return new MethodInvocationHelper(application.requireFacet(MethodInvocationFacet.class));
    }

    private MethodInvocationHelper(MethodInvocationFacet facet) {
        this.facet = facet;
    }

    public MethodInvocationFacet getFacet() {
        return facet;
    }

    public ObjectMethod requireMethodByFullName(String methodFullName) {
        ObjectMethod method = getMethodByFullName(methodFullName);
        if (method == null) throw new IllegalArgumentException("Method not found: " + methodFullName);
        return method;
    }

    public ObjectMethod getMethodByFullName(String methodFullName) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
