package polr.client.ui.base.skin.simple;

import polr.client.ui.base.Button;
import polr.client.ui.base.Component;
import polr.client.ui.base.ScrollBar;
import polr.client.ui.base.Skin;
import polr.client.ui.base.Slider;
import polr.client.ui.base.Theme;
import polr.client.ui.base.skin.ScrollBarAppearance;
import polr.client.ui.base.skin.SkinUtil;
import polr.client.ui.base.skin.simple.SimpleArrowButton;

/**
 * 
 * @author davedes
 */
public class SimpleScrollBarAppearance extends SimpleContainerAppearance implements ScrollBarAppearance {

    public void install(Component comp, Skin skin, Theme theme) {
        SkinUtil.installFont(comp, ((SimpleSkin) skin).getFont());
        SkinUtil.installColors(comp, theme.getPrimary1(), theme.getForeground());
    }

    public Button createScrollButton(ScrollBar bar, int direction) {
        Button btn = createSimpleScrollButton(bar, direction);
        return btn;
    }

    public Slider createSlider(ScrollBar bar, int orientation) {
        Slider slider = new Slider(orientation);
        return slider;
    }

    /**
	 * A utility method to create a scroll button based on the given scroll
	 * bar's orientation, size and direction. This will set the button's
	 * dimensions to the width or height (based on orientation) of the given
	 * scroll bar.
	 * 
	 * 
	 * 
	 * @param bar
	 *            the scroll bar parent
	 * @param direction
	 *            the direction the bar will scroll, either ScrollBar.INCREMENT
	 *            or ScrollBar.DECREMENT.
	 * @return a new SimpleArrowButton based on the given parameters
	 */
    protected Button createSimpleScrollButton(ScrollBar bar, int direction) {
        float angle = SimpleArrowButton.getScrollButtonAngle(bar, direction);
        int orientation = bar.getOrientation();
        float size = 0f;
        if (orientation == ScrollBar.HORIZONTAL) {
            size = bar.getHeight();
        } else size = bar.getWidth();
        if (size == 0) size = ScrollBar.DEFAULT_SIZE;
        Button btn = new SimpleArrowButton(angle);
        btn.setSize(size, size);
        return btn;
    }
}
