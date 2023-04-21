package org.kablink.teaming.remoteapplication;

import java.io.Writer;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.kablink.teaming.security.accesstoken.AccessToken.BinderAccessConstraints;

public interface RemoteApplicationManager {

    public void executeSessionScopedRenderableAction(Map<String, String> params, Long applicationId, HttpServletRequest request, Writer out) throws RemoteApplicationException;

    public void executeSessionScopedRenderableAction(Map<String, String> params, Long applicationId, Long binderId, BinderAccessConstraints binderAccessConstraints, HttpServletRequest request, Writer out) throws RemoteApplicationException;

    public String executeRequestScopedNonRenderableAction(Map<String, String> params, Long applicationId) throws RemoteApplicationException;

    public String executeRequestScopedNonRenderableAction(Map<String, String> params, Long applicationId, Long binderId, BinderAccessConstraints binderAccessConstraints) throws RemoteApplicationException;
}
