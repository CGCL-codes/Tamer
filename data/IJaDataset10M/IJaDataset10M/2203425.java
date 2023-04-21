package org.geometerplus.fbreader.fbreader;

import org.geometerplus.zlibrary.core.options.ZLBooleanOption;
import org.geometerplus.zlibrary.core.options.ZLEnumOption;
import org.geometerplus.zlibrary.core.view.ZLView;

public class ScrollingPreferences {

    private static ScrollingPreferences ourInstance;

    public static ScrollingPreferences Instance() {
        return (ourInstance != null) ? ourInstance : new ScrollingPreferences();
    }

    public enum FingerScrolling {

        byTap, byFlick, byTapAndFlick
    }

    public final ZLEnumOption<FingerScrolling> FingerScrollingOption = new ZLEnumOption<FingerScrolling>("Scrolling", "Finger", FingerScrolling.byTapAndFlick);

    public final ZLBooleanOption VolumeKeysOption = new ZLBooleanOption("Scrolling", "VolumeKeys", true);

    public final ZLEnumOption<ZLView.Animation> AnimationOption = new ZLEnumOption<ZLView.Animation>("Scrolling", "Animation", ZLView.Animation.slide);

    public final ZLBooleanOption HorizontalOption = new ZLBooleanOption("Scrolling", "Horizontal", true);

    public final ZLBooleanOption InvertVolumeKeysOption = new ZLBooleanOption("Scrolling", "InvertVolumeKeys", false);

    private ScrollingPreferences() {
        ourInstance = this;
    }
}
