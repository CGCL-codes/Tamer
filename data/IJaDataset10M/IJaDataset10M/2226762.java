package com.medsol.infra.acegi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.acegisecurity.context.HttpSessionContextIntegrationFilter;
import org.acegisecurity.context.SecurityContext;
import org.springframework.util.StringUtils;
import com.medsol.common.util.PropertiesUtil;

/**
 * Class overridden to set MedSolSecurityContextImpl as the default security context
 * for the entire application. This class will hold the context information throughout
 * the session so that this can be used to switch between databases
 * @author Vinay.Puthenveettil
 *
 */
public class MedSolHttpSessionContextIntegrationFilter extends HttpSessionContextIntegrationFilter {

    private Class<MedSolSecurityContextImpl> context = MedSolSecurityContextImpl.class;

    private static final String DEPLOY_MULTIPLE_CONTEXT = PropertiesUtil.getMainProperty("medsol.deploy.multiple.context", "false");

    @Override
    public SecurityContext generateNewContext() throws ServletException {
        try {
            if (isMedSolMultipleContextEnabled()) {
                return (SecurityContext) this.context.newInstance();
            } else {
                return super.generateNewContext();
            }
        } catch (InstantiationException ie) {
            throw new ServletException(ie);
        } catch (IllegalAccessException iae) {
            throw new ServletException(iae);
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        super.doFilter(request, response, chain);
        if (isMedSolMultipleContextEnabled()) {
            HttpSession httpSession = ((HttpServletRequest) request).getSession(false);
            if (httpSession != null && httpSession.getAttribute(ACEGI_SECURITY_CONTEXT_KEY) != null) {
                SecurityContext context = (SecurityContext) httpSession.getAttribute(ACEGI_SECURITY_CONTEXT_KEY);
                if (!(context instanceof MedSolSecurityContextImpl)) {
                    httpSession.setAttribute(ACEGI_SECURITY_CONTEXT_KEY, new MedSolSecurityContextImpl(context));
                }
            }
        }
    }

    public static boolean isMedSolMultipleContextEnabled() {
        if (DEPLOY_MULTIPLE_CONTEXT.equalsIgnoreCase("true")) {
            return true;
        }
        return false;
    }

    public static List<String> getDatabaseList() {
        List<String> databaseList = new ArrayList<String>();
        String databases = PropertiesUtil.getMainProperty("jdbc.databases");
        if (StringUtils.hasText(databases)) {
            for (StringTokenizer tokenizer = new StringTokenizer(databases, ","); tokenizer.hasMoreTokens(); ) {
                databaseList.add((String) tokenizer.nextElement());
            }
        }
        return databaseList;
    }
}
