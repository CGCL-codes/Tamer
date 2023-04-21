package org.loon.framework.android.game.utils;

import org.loon.framework.android.game.LGameAndroid2DActivity;
import org.loon.framework.android.game.Location;
import org.loon.framework.android.game.core.LSystem;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

/**
 * 
 * Copyright 2008 - 2011
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * 
 * @project loonframework
 * @author chenpeng
 * @email ceponline@yahoo.com.cn
 * @version 0.1.0
 */
public class AdUtils {

    private static Object adObject;

    private static boolean isAdJarExist = true;

    private static final String dealutKeywords = "game play video mobile app buy love down world blog wiki";

    static {
        try {
            Class<?> clazz = Class.forName("com.admob.android.ads.AdView");
            if (clazz == null) {
                isAdJarExist = false;
            }
        } catch (Exception e) {
            isAdJarExist = false;
        }
    }

    public static void createAdView(LGameAndroid2DActivity context, Location lad, int requestInterval) {
        createAdView(context, lad, dealutKeywords, requestInterval, null);
    }

    public static void createAdView(LGameAndroid2DActivity context, Location lad, int requestInterval, com.admob.android.ads.AdListener listener) {
        createAdView(context, lad, dealutKeywords, requestInterval, listener);
    }

    public static void createAdView(LGameAndroid2DActivity context, Location lad, String keywords, int requestInterval) {
        createAdView(context, lad, keywords, requestInterval, null);
    }

    public static void createAdView(final LGameAndroid2DActivity context, final Location lad, final String keywords, final int requestInterval, final com.admob.android.ads.AdListener listener) {
        if (!isAdJarExist) {
            return;
        }
        if (adObject != null) {
            return;
        }
        LSystem.threadUi(new Runnable() {

            public void run() {
                try {
                    if (LSystem.isEmulator()) {
                        com.admob.android.ads.AdManager.setTestDevices(new String[] { com.admob.android.ads.AdManager.TEST_EMULATOR });
                    }
                    com.admob.android.ads.AdManager.setAllowUseOfLocation(true);
                    com.admob.android.ads.AdView adview = new com.admob.android.ads.AdView(context);
                    if (listener != null) {
                        adview.setAdListener(listener);
                    }
                    if (keywords != null) {
                        adview.setKeywords(keywords);
                    }
                    adview.setRequestInterval(requestInterval);
                    adview.setGravity(Gravity.NO_GRAVITY);
                    adview.setVisibility(com.admob.android.ads.AdView.VISIBLE);
                    adview.requestFreshAd();
                    adview.bringToFront();
                    context.addView(adview, lad);
                    adObject = adview;
                } catch (Exception ex) {
                    Log.d("AD", ex.getMessage());
                }
            }
        });
    }

    public static Object getAdViewObject() {
        return adObject;
    }

    public static void hideAd() {
        if (adObject != null) {
            if (adObject instanceof com.admob.android.ads.AdView) {
                final com.admob.android.ads.AdView ad = ((com.admob.android.ads.AdView) adObject);
                if (ad.getVisibility() == View.VISIBLE) {
                    LSystem.post(new Runnable() {

                        public void run() {
                            ad.setVisibility(View.GONE);
                        }
                    });
                }
            }
        }
    }

    public static void showAd() {
        if (adObject != null) {
            if (adObject instanceof com.admob.android.ads.AdView) {
                final com.admob.android.ads.AdView ad = ((com.admob.android.ads.AdView) adObject);
                if (ad.getVisibility() == View.GONE) {
                    LSystem.post(new Runnable() {

                        public void run() {
                            ad.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }
        }
    }

    public static void setPublisherId(final String publisherId) {
        if (isAdJarExist) {
            com.admob.android.ads.AdManager.setPublisherId(publisherId);
        }
    }

    public static void setInterstitialPublisherId(final String publisherId) {
        if (isAdJarExist) {
            com.admob.android.ads.AdManager.setInterstitialPublisherId(publisherId);
        }
    }

    public static boolean isAdJarExist() {
        return isAdJarExist;
    }

    public static void setAdJarExist(boolean isAdJarExist) {
        AdUtils.isAdJarExist = isAdJarExist;
    }
}
