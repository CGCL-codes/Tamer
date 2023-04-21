package com.doculibre.intelligid.webservices.plugin.base;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.doculibre.intelligid.webservices.plugin.model.AddinException;
import com.doculibre.intelligid.webservices.plugin.model.AddinUserRequest;

/**
 * Sous-classe de tous les services du plugins fonctionnant avec un utilisateur,
 * mais sans fichier électronique en particulier
 * 
 * @author francisbaril
 * 
 */
@SuppressWarnings("serial")
public abstract class BaseUserAddinService extends AbstractAddinService<AddinUserRequest> {

    @Override
    protected AddinUserRequest newAddinRequest(HttpServletRequest request, HttpServletResponse response) throws AddinException {
        return new AddinUserRequest(request, response, checkPassword());
    }

    protected boolean checkPassword() {
        return false;
    }
}
