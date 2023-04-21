package org.az.gsm.web.client.managed.activity;

import org.az.gsm.common.client.model.SkillProxy;
import org.az.gsm.web.client.managed.request.ApplicationRequestFactory;
import org.az.gsm.web.client.scaffold.place.ProxyListPlace;
import org.az.gsm.web.client.scaffold.place.ProxyPlace;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.place.shared.Place;
import com.google.gwt.requestfactory.shared.EntityProxyId;

public class SkillEditActivityWrapper extends SkillEditActivityWrapper_Roo_Gwt {

    private final EntityProxyId<SkillProxy> proxyId;

    public SkillEditActivityWrapper(final ApplicationRequestFactory requests, final View<?> view, final Activity activity, final EntityProxyId<org.az.gsm.common.client.model.SkillProxy> proxyId) {
        this.requests = requests;
        this.view = view;
        this.wrapped = activity;
        this.proxyId = proxyId;
    }

    public Place getBackButtonPlace() {
        return (proxyId == null) ? new ProxyListPlace(SkillProxy.class) : new ProxyPlace(proxyId, ProxyPlace.Operation.DETAILS);
    }

    public String getBackButtonText() {
        return "Cancel";
    }

    public Place getEditButtonPlace() {
        return null;
    }

    public String getTitleText() {
        return (proxyId == null) ? "New Skill" : "Edit Skill";
    }

    public boolean hasEditButton() {
        return false;
    }

    @Override
    public String mayStop() {
        return wrapped.mayStop();
    }

    @Override
    public void onCancel() {
        wrapped.onCancel();
    }

    @Override
    public void onStop() {
        wrapped.onStop();
    }

    public interface View<V extends org.az.gsm.web.client.scaffold.place.ProxyEditView<SkillProxy, V>> extends View_Roo_Gwt<V> {
    }
}
