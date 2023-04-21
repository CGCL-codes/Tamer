package org.jcompany.control.jsf.filter;

import java.io.IOException;
import javax.faces.context.FacesContext;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import org.jboss.seam.contexts.Contexts;
import org.jboss.seam.core.Manager;
import org.jboss.seam.core.Pages;
import org.jboss.seam.web.RedirectFilter;
import org.jcompany.control.PlcConstants.PlcJsfConstants;

/**
 * Classe que verifica se  existe o par�metro propagaConversacaoPlc na URL e se tiver atacha o Conversation ID na URL,
 * ou seja Propaga a conversa��o. Somente para Redirects
 * 
 * @author Pedro Henrique
 *
 */
public class PlcRedirectFilter extends RedirectFilter {

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        chain.doFilter(request, wrapResponse((HttpServletResponse) response));
    }

    private static ServletResponse wrapResponse(HttpServletResponse response) {
        return new HttpServletResponseWrapper(response) {

            @Override
            public void sendRedirect(String url) throws IOException {
                if (Contexts.isEventContextActive()) {
                    if (!url.startsWith("http:") && !url.startsWith("https:") && spreadsConversationPlc(url)) {
                        String viewId = getViewId(url);
                        if (viewId != null) {
                            url = Pages.instance().encodePageParameters(FacesContext.getCurrentInstance(), url, viewId);
                        }
                        url = Manager.instance().appendConversationIdFromRedirectFilter(url, viewId);
                    }
                }
                super.sendRedirect(url);
            }
        };
    }

    /**
	 * Verifica se tem o par�metro na Url que indica que vai propagar a conversa��o
	 * 
	 */
    protected static boolean spreadsConversationPlc(String url) {
        return url != null && url.contains(PlcJsfConstants.PLC_IND_SPREADS_CONVERSATION);
    }
}
