package org.cloudlet.web.mvp.shared;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.google.gwt.user.client.TakesValue;
import com.google.gwt.user.client.ui.HasName;
import com.google.inject.Inject;
import com.google.inject.Provider;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public final class SimpleActivityMapper implements ActivityMapper, HasName {

    private final Map<String, Provider<Activity>> placeToActivity = new HashMap<String, Provider<Activity>>();

    private final Logger logger = Logger.getLogger(getClass().getName());

    private String name;

    @Inject
    SimpleActivityMapper() {
    }

    public <T extends Place> void addBinding(Class<T> place, Provider<? extends Activity> activityProvider) {
        String clazz = place.getName();
        if (null != placeToActivity.put(clazz, (Provider<Activity>) activityProvider)) {
            logger.warning("绑定了重复的place=" + clazz);
        }
        logger.finest("bind layout \"" + name + "\" with " + clazz);
    }

    @Override
    public Activity getActivity(Place place) {
        String clazz = place.getClass().getName();
        Activity activity;
        Provider<Activity> provider = placeToActivity.get(clazz);
        if (provider == null) {
            return null;
        }
        activity = provider.get();
        if (activity instanceof TakesValue<?>) {
            ((TakesValue) activity).setValue(place);
        }
        if (activity instanceof HasName) {
            ((HasName) activity).setName(getName());
        }
        return activity;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
}
