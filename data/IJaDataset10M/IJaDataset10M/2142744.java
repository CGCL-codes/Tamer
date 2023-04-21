package ua.org.hatu.daos.gwt.client;

import ua.org.hatu.daos.gwt.client.activity.TestCreationActivity;
import ua.org.hatu.daos.gwt.client.activity.TestActivity;
import ua.org.hatu.daos.gwt.client.activity.SignInActivity;
import ua.org.hatu.daos.gwt.client.activity.SignUpActivity;
import ua.org.hatu.daos.gwt.client.place.AdminPlace;
import ua.org.hatu.daos.gwt.client.place.SignInPlace;
import ua.org.hatu.daos.gwt.client.place.SignUpPlace;
import ua.org.hatu.daos.gwt.client.place.TestPlace;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;

/**
 * @author zeus (alex.pogrebnyuk@gmail.com)
 * @author dmytro (pogrebniuk@gmail.com)
 */
public class ContentActivityMapper implements ActivityMapper {

    private ClientFactory clientFactory;

    public ContentActivityMapper(ClientFactory clientFactory) {
        this.clientFactory = clientFactory;
    }

    @Override
    public Activity getActivity(Place place) {
        if (place instanceof SignUpPlace) {
            return new SignUpActivity(clientFactory);
        } else if (place instanceof SignInPlace) {
            return new SignInActivity(clientFactory);
        } else if (place instanceof AdminPlace) {
            return new TestCreationActivity(clientFactory);
        } else if (place instanceof TestPlace) {
            return new TestActivity(clientFactory, ((TestPlace) place).getToken());
        }
        return null;
    }
}
