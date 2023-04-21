package org.az.gsm.web.client.managed.activity;

import org.az.gsm.common.client.model.PersonProxy;
import org.az.gsm.common.client.model.SkillProxy;
import org.az.gsm.common.client.model.SkillRefProxy;
import org.az.gsm.web.client.managed.request.ApplicationEntityTypesProcessor;
import org.az.gsm.web.client.managed.request.ApplicationRequestFactory;
import org.az.gsm.web.client.managed.ui.PersonListView;
import org.az.gsm.web.client.managed.ui.SkillListView;
import org.az.gsm.web.client.managed.ui.SkillRefListView;
import org.az.gsm.web.client.scaffold.place.ProxyListPlace;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceController;
import com.google.inject.Inject;

public final class ApplicationMasterActivities implements ActivityMapper {

    protected PlaceController placeController;

    protected ApplicationRequestFactory requests;

    @Inject
    public ApplicationMasterActivities(final ApplicationRequestFactory requests, final PlaceController placeController) {
        this.requests = requests;
        this.placeController = placeController;
    }

    @Override
    public Activity getActivity(final Place place) {
        if (!(place instanceof ProxyListPlace)) {
            return null;
        }
        final ProxyListPlace listPlace = (ProxyListPlace) place;
        return new ApplicationEntityTypesProcessor<Activity>() {

            @Override
            public void handlePerson(final PersonProxy isNull) {
                setResult(new PersonListActivity(requests, PersonListView.instance(), placeController));
            }

            @Override
            public void handleSkill(final SkillProxy isNull) {
                setResult(new SkillListActivity(requests, SkillListView.instance(), placeController));
            }

            @Override
            public void handleSkillRef(final SkillRefProxy isNull) {
                setResult(new SkillRefListActivity(requests, SkillRefListView.instance(), placeController));
            }
        }.process(listPlace.getProxyClass());
    }
}
