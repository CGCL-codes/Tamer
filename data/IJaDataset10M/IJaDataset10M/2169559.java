package org.tynamo.security.testapp.pages;

import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.tynamo.security.services.SecurityService;
import org.tynamo.security.testapp.services.AlphaService;
import org.tynamo.security.testapp.services.BetaService;
import org.tynamo.security.testapp.services.impl.Invoker;

public class Index {

    @Persist(PersistenceConstants.FLASH)
    private String result;

    @Inject
    private AlphaService alphaService;

    @Inject
    private BetaService betaService;

    @Inject
    private SecurityService securityService;

    public String getStatus() {
        return securityService.isAuthenticated() ? "Authenticated" : "Not Authenticated";
    }

    @RequiresAuthentication
    public void onActionFromComponentMethodInterceptor() {
        result = Invoker.invoke(getClass());
    }

    public void onActionFromBetaServiceInvoke() {
        result = betaService.invoke();
    }

    public void onActionFromAlphaServiceInvoke() {
        result = alphaService.invoke();
    }

    public void onActionFromAlphaServiceRequiresAuthentication() {
        result = alphaService.invokeRequiresAuthentication();
    }

    public void onActionFromAlphaServiceRequiresUser() {
        result = alphaService.invokeRequiresUser();
    }

    public void onActionFromAlphaServiceRequiresGuest() {
        result = alphaService.invokeRequiresGuest();
    }

    public void onActionFromAlphaServiceRequiresRolesUser() {
        result = alphaService.invokeRequiresRolesUser();
    }

    public void onActionFromAlphaServiceRequiresRolesManager() {
        result = alphaService.invokeRequiresRolesManager();
    }

    public void onActionFromAlphaServiceRequiresPermissionsNewsView() {
        result = alphaService.invokeRequiresPermissionsNewsView();
    }

    public void onActionFromAlphaServiceRequiresPermissionsNewsEdit() {
        result = alphaService.invokeRequiresPermissionsNewsEdit();
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }
}
