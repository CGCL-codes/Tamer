package com.google.gwt.maps.client.streetview;

import com.google.gwt.ajaxloader.client.ArrayHelper;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.maps.client.JsBeanTestCase;

/**
 * Tests setters / getters for typos in JSNI methods.
 */
public class StreetviewUserPhotoOptionsTest extends JsBeanTestCase {

    public void testFields() {
        loadApi(new Runnable() {

            public void run() {
                StreetviewUserPhotosOptions opts = StreetviewUserPhotosOptions.newInstance();
                JsArrayString array = ArrayHelper.toJsArrayString("picasa", "flickr");
                opts.setPhotoRepositories(array).setPhotoRepositories(array);
                assertSame(array, getJso(opts, "photoRepositories"));
            }
        });
    }
}
