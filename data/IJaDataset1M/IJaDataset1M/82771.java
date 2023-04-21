package com.googlecode.mgwt.examples.showcase.client;

import cn.ekuma.epos.online.pda.client.activities.MainActivity;
import cn.ekuma.epos.online.pda.client.activities.MainPlace;
import cn.ekuma.epos.online.pda.client.activities.product.ProductActivity;
import cn.ekuma.epos.online.pda.client.activities.product.ProductPlace;
import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import com.googlecode.mgwt.examples.showcase.client.activities.AboutActivity;
import com.googlecode.mgwt.examples.showcase.client.activities.AboutPlace;
import com.googlecode.mgwt.examples.showcase.client.activities.ShowCaseListActivity;
import com.googlecode.mgwt.examples.showcase.client.activities.animation.AnimationActivity;
import com.googlecode.mgwt.examples.showcase.client.activities.animation.AnimationPlace;
import com.googlecode.mgwt.examples.showcase.client.activities.animationdone.AnimationDissolvePlace;
import com.googlecode.mgwt.examples.showcase.client.activities.animationdone.AnimationDoneActivity;
import com.googlecode.mgwt.examples.showcase.client.activities.animationdone.AnimationFadePlace;
import com.googlecode.mgwt.examples.showcase.client.activities.animationdone.AnimationFlipPlace;
import com.googlecode.mgwt.examples.showcase.client.activities.animationdone.AnimationPopPlace;
import com.googlecode.mgwt.examples.showcase.client.activities.animationdone.AnimationSlidePlace;
import com.googlecode.mgwt.examples.showcase.client.activities.animationdone.AnimationSlideUpPlace;
import com.googlecode.mgwt.examples.showcase.client.activities.animationdone.AnimationSwapPlace;
import com.googlecode.mgwt.examples.showcase.client.places.HomePlace;

/**
 * @author Daniel Kurka
 * 
 */
public class PhoneActivityMapper implements ActivityMapper {

    private final ClientFactory clientFactory;

    public PhoneActivityMapper(ClientFactory clientFactory) {
        this.clientFactory = clientFactory;
    }

    @Override
    public Activity getActivity(Place place) {
        if (place instanceof HomePlace) {
            return new ShowCaseListActivity(clientFactory);
        }
        if (place instanceof MainPlace) {
            return new MainActivity(clientFactory);
        }
        if (place instanceof ProductPlace) {
            return new ProductActivity(clientFactory);
        }
        if (place instanceof AboutPlace) {
            return new AboutActivity(clientFactory);
        }
        if (place instanceof AnimationPlace) {
            return new AnimationActivity(clientFactory);
        }
        if (place instanceof AnimationSlidePlace || place instanceof AnimationSlideUpPlace || place instanceof AnimationDissolvePlace || place instanceof AnimationFadePlace || place instanceof AnimationFlipPlace || place instanceof AnimationPopPlace || place instanceof AnimationSwapPlace || place instanceof AnimationSwapPlace) {
            return new AnimationDoneActivity(clientFactory);
        }
        return new ShowCaseListActivity(clientFactory);
    }
}
