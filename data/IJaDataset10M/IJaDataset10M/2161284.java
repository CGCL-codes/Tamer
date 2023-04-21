package com.androinject.android;

import java.io.InputStream;
import android.app.Application;
import com.androinject.android.factory.AndroidXmlComponentFactory;
import com.androinject.core.ComponentFactory;
import com.androinject.core.ComponentFactoryProvider;

public class AndroInjectApplication extends Application implements ComponentFactoryProvider {

    private ComponentFactory factory;

    public void onCreate(int resId) {
        super.onCreate();
        this.factory = new AndroidXmlComponentFactory(this, resId);
    }

    public void onCreate(InputStream inputStream) {
        super.onCreate();
        this.factory = new AndroidXmlComponentFactory(this, inputStream);
    }

    public ComponentFactory getComponentFactory() {
        return this.factory;
    }
}
