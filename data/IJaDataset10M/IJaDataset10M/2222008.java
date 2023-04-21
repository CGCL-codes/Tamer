package com.xiledsystems.AlternateJavaBridgelib.components.altbridge;

import com.xiledsystems.AlternateJavaBridgelib.components.events.EventDispatcher;
import android.view.View;
import android.widget.SlidingDrawer;

public class DrawerSlider extends AndroidViewComponent {

    private final SlideDrawer view;

    public DrawerSlider(ComponentContainer container, int resId) {
        super(container);
        SlidingDrawer temp = (SlidingDrawer) container.$context().findViewById(resId);
        view = new SlideDrawer(container.$context(), temp.getHandle().getId(), temp.getContent().getId());
    }

    @Override
    public View getView() {
        return view;
    }

    public void setDrawerDirection(int direction) {
        view.setDrawerDirection(direction);
    }

    @Override
    public void postAnimEvent() {
        EventDispatcher.dispatchEvent(this, "AnimationMiddle");
    }
}
