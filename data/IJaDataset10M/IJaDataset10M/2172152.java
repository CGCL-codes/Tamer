package org.fulworx.core.security;

import org.restlet.data.ChallengeResponse;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.Parameter;
import org.restlet.data.Request;
import org.restlet.util.Series;
import com.noelios.restlet.authentication.AuthenticationHelper;

public class FulworxHTTPSAuthenticationHelper extends AuthenticationHelper {

    public FulworxHTTPSAuthenticationHelper() {
        super(ChallengeScheme.CUSTOM, false, true);
    }

    public void formatCredentials(StringBuilder stringBuilder, ChallengeResponse challengeResponse, Request request, Series<Parameter> parameters) {
    }
}
