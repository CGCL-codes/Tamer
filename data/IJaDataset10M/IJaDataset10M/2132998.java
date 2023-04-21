package br.gov.frameworkdemoiselle.internal.factory;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import br.gov.frameworkdemoiselle.internal.proxy.HttpSessionProxy;

public class HttpSessionFactory {

    @Produces
    @Default
    @SessionScoped
    public HttpSession create(final HttpServletRequest request) {
        return new HttpSessionProxy(request.getSession());
    }
}
