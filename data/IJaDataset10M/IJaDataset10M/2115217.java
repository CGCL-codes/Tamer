package com.halware.nakedide.eclipse.ext.builders.checkers;

import java.util.HashMap;
import java.util.Map;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import com.halware.nakedide.eclipse.ext.builders.Constants;

public abstract class AbstractChecker {

    /**
     * 
     * @param type
     * @param markerId - as per {@link MarkerConstants}.
     * @param message
     * 
     * @throws JavaModelException
     * @throws CoreException
     */
    protected void createProblemMarker(IType type, int markerId, String message) throws JavaModelException, CoreException {
        IMarker marker;
        marker = type.getUnderlyingResource().createMarker(Constants.PROBLEM_MARKER);
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put(IMarker.MESSAGE, message);
        attributes.put(IMarker.SEVERITY, new Integer(org.eclipse.core.resources.IMarker.SEVERITY_WARNING));
        attributes.put(IMarker.LINE_NUMBER, 1);
        attributes.put(MarkerConstants.ATTR_ID_KEY, markerId);
        marker.setAttributes(attributes);
    }

    /**
     * 
     * @param method
     * @param markerId - as per {@link MarkerConstants}
     * @param message
     * 
     * @throws JavaModelException
     * @throws CoreException
     */
    protected void createProblemMarker(IMethod method, int markerId, String message) throws JavaModelException, CoreException {
        IMarker marker;
        marker = method.getUnderlyingResource().createMarker(Constants.PROBLEM_MARKER);
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put(IMarker.MESSAGE, message);
        attributes.put(IMarker.SEVERITY, IMarker.SEVERITY_WARNING);
        attributes.put(MarkerConstants.ATTR_ID_KEY, markerId);
        attributes.put(MarkerConstants.ATTR_METHOD_KEY, method);
        int line = getLineNumber(method);
        if (line > 0) {
            attributes.put(IMarker.LINE_NUMBER, line);
        }
        marker.setAttributes(attributes);
    }

    protected int getLineNumber(IMethod method) throws JavaModelException {
        int lines = 0;
        String targetsource = method.getDeclaringType().getCompilationUnit().getSource();
        String sourceuptomethod = targetsource.substring(0, method.getNameRange().getOffset());
        char[] chars = new char[sourceuptomethod.length()];
        sourceuptomethod.getChars(0, sourceuptomethod.length(), chars, 0);
        for (int j = 0; j < chars.length; j++) {
            if (chars[j] == '\n') {
                lines++;
            }
        }
        return lines + 1;
    }
}
