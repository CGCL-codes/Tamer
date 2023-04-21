package com.hcs.application;

import android.app.Activity;
import android.app.Application;

/**
 * 该类用于存放应用正在运行的Activity实例
 * @author zhongming.meng
 * @since 2011-06-15
 */
public class ActivityContainerApp extends Application {

    private Activity layer1;

    private Activity layer2;

    private Activity layer3;

    public Activity getLayer1() {
        return layer1;
    }

    public void setLayer1(Activity layer1) {
        this.layer1 = layer1;
    }

    public Activity getLayer2() {
        return layer2;
    }

    public void setLayer2(Activity layer2) {
        this.layer2 = layer2;
    }

    public Activity getLayer3() {
        return layer3;
    }

    public void setLayer3(Activity layer3) {
        this.layer3 = layer3;
    }
}
