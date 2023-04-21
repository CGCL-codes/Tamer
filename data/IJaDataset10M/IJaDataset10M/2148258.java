package org.sempere.commons.filter;

import org.apache.commons.lang.*;
import org.sempere.commons.base.*;
import org.sempere.commons.encryption.*;
import org.slf4j.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import static javax.servlet.http.HttpServletResponse.*;

/**
 * Filter that checks user authorization to access a web resource.<br/>
 * This filter relies on commons-security classes to achieve this job.
 *
 * @author sempere
 */
public abstract class AuthorizationFilter extends AbstractFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizationFilter.class);

    private Encrypter encrypter;

    public AuthorizationFilter() {
    }

    @Override
    protected void initInternal() {
        LOGGER.debug("About to create encrypter instance from class [{}].", this.getEncrypterClass());
        this.encrypter = ReflectionHelper.newInstance(this.getEncrypterClass());
        LOGGER.debug("Encrypter instance from class [{}] created successfully.", this.getEncrypterClass());
    }

    @Override
    protected void destroyInternal() {
        LOGGER.debug("About to destroy encrypter instance from class [{}].", this.getEncrypterClass());
        this.encrypter = null;
        LOGGER.debug("Encrypter instance from class [{}] destroyed successfully.", this.getEncrypterClass());
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        LOGGER.debug("in doFilter");
        if (this.isValid(request)) {
            LOGGER.debug("User request is valid in term of authorizations.");
            chain.doFilter(request, response);
        } else {
            LOGGER.debug("User request is invalid in term of authorizations.");
            ((HttpServletResponse) response).setStatus(SC_FORBIDDEN);
        }
    }

    /**
	 * Return the current token stored in the client request (the token should be encrypted using the encrypter's encrypt method)
	 *
	 * @param request the client request
	 * @return String
	 */
    public abstract String getCurrentToken(ServletRequest request);

    /**
	 * Return the expected token (the token should be encrypted using the encrypter's encrypt method)
	 *
	 * @param request the client request
	 * @return String
	 */
    public abstract String getExpectedToken(ServletRequest request);

    /**
	 * Return the encrypter class to be used
	 *
	 * @return String
	 */
    public abstract String getEncrypterClass();

    protected final boolean isValid(ServletRequest request) {
        String expectedToken = this.getExpectedToken(request);
        if (!StringUtils.isBlank(expectedToken)) {
            String expectedClearToken = this.getEncrypter().decrypt(expectedToken);
            String currentClearToken = this.getEncrypter().decrypt(this.getCurrentToken(request));
            return expectedClearToken.equals(currentClearToken);
        }
        return true;
    }

    public Encrypter getEncrypter() {
        return encrypter;
    }
}
