package org.myrobotlab.android;

import org.myrobotlab.service.Proxy;
import android.os.Bundle;

public class ProxyActivity extends ServiceActivity {

    Proxy myService = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.proxy);
        myService = (Proxy) sw.service;
    }

    @Override
    public void attachGUI() {
    }

    @Override
    public void detachGUI() {
    }
}
