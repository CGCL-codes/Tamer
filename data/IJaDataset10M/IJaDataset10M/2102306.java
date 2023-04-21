package com.doculibre.intelligid.wicket.panels;

import javax.servlet.ServletContext;
import common.Assert;
import wicket.authorization.strategies.role.Roles;
import wicket.markup.html.panel.Panel;
import wicket.model.IModel;
import wicket.model.LoadableDetachableModel;
import wicket.protocol.http.WebApplication;
import com.doculibre.intelligid.entites.UtilisateurIFGD;
import com.doculibre.intelligid.wicket.components.UsefullComponent;
import com.doculibre.intelligid.wicket.components.protocol.http.FGDWebSession;

@SuppressWarnings("serial")
public class BaseFGDPanel extends Panel implements UsefullComponent {

    public BaseFGDPanel(String id, IModel model) {
        super(id, model);
    }

    public BaseFGDPanel(String id) {
        super(id);
    }

    public UtilisateurIFGD getUtilisateurCourant() {
        FGDWebSession session = (FGDWebSession) FGDWebSession.get();
        return session.getUtilisateurCourant();
    }

    public boolean hasAnyRole(Roles roles) {
        return getUtilisateurCourant() != null && getUtilisateurCourant().getRoles().hasAnyRole(roles);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void assertType(Object object, Class type) {
        Assert.verify(object != null, "L'objet ne doit pas être à Null");
        Assert.verify(object.getClass().equals(type), "L'objet '" + object.toString() + "' de type '" + object.getClass().getSimpleName() + "' devrait être de type '" + type.getSimpleName() + "'");
    }

    public IModel getServletContextModel() {
        return new LoadableDetachableModel() {

            protected Object load() {
                return ((WebApplication) getApplication()).getWicketServlet().getServletContext();
            }
        };
    }

    public String getRealPathOf(String relativePath) {
        ServletContext context = ((WebApplication) getApplication()).getWicketServlet().getServletContext();
        return context.getRealPath(relativePath);
    }
}
