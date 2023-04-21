package slider;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.JComponent;
import javax.swing.JSlider;
import javax.swing.event.ChangeListener;
import javax.swing.event.MouseInputAdapter;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSliderUI;

/**
 * Open source code from the internet. Code available online at http://www2.gol.com/users/tame/swing/examples/JSliderExamples1.html
 * @version 1.0 09/08/99
 */
public class BasicMThumbSliderUI extends BasicSliderUI implements MThumbSliderAdditional {

    MThumbSliderAdditionalUI additonalUi;

    MouseInputAdapter mThumbTrackListener;

    public static ComponentUI createUI(JComponent c) {
        return new BasicMThumbSliderUI((JSlider) c);
    }

    public BasicMThumbSliderUI() {
        super(null);
    }

    public BasicMThumbSliderUI(JSlider b) {
        super(b);
    }

    public void installUI(JComponent c) {
        additonalUi = new MThumbSliderAdditionalUI(this);
        additonalUi.installUI(c);
        mThumbTrackListener = createMThumbTrackListener((JSlider) c);
        super.installUI(c);
    }

    public void uninstallUI(JComponent c) {
        super.uninstallUI(c);
        additonalUi.uninstallUI(c);
        additonalUi = null;
        mThumbTrackListener = null;
    }

    protected MouseInputAdapter createMThumbTrackListener(JSlider slider) {
        return additonalUi.trackListener;
    }

    protected TrackListener createTrackListener(JSlider slider) {
        return null;
    }

    protected ChangeListener createChangeListener(JSlider slider) {
        return additonalUi.changeHandler;
    }

    protected void installListeners(JSlider slider) {
        slider.addMouseListener(mThumbTrackListener);
        slider.addMouseMotionListener(mThumbTrackListener);
        slider.addFocusListener(focusListener);
        slider.addComponentListener(componentListener);
        slider.addPropertyChangeListener(propertyChangeListener);
        slider.getModel().addChangeListener(changeListener);
    }

    protected void uninstallListeners(JSlider slider) {
        slider.removeMouseListener(mThumbTrackListener);
        slider.removeMouseMotionListener(mThumbTrackListener);
        slider.removeFocusListener(focusListener);
        slider.removeComponentListener(componentListener);
        slider.removePropertyChangeListener(propertyChangeListener);
        slider.getModel().removeChangeListener(changeListener);
    }

    protected void calculateGeometry() {
        super.calculateGeometry();
        additonalUi.calculateThumbsSize();
        additonalUi.calculateThumbsLocation();
    }

    protected void calculateThumbLocation() {
    }

    Rectangle zeroRect = new Rectangle();

    public void paint(Graphics g, JComponent c) {
        Rectangle clip = g.getClipBounds();
        thumbRect = zeroRect;
        super.paint(g, c);
        int thumbNum = additonalUi.getThumbNum();
        Rectangle[] thumbRects = additonalUi.getThumbRects();
        for (int i = thumbNum - 1; 0 <= i; i--) {
            if (clip.intersects(thumbRects[i])) {
                thumbRect = thumbRects[i];
                paintThumb(g);
            }
        }
    }

    public void scrollByBlock(int direction) {
    }

    public void scrollByUnit(int direction) {
    }

    public Rectangle getTrackRect() {
        return trackRect;
    }

    public Dimension getThumbSize() {
        return super.getThumbSize();
    }

    public int xPositionForValue(int value) {
        return super.xPositionForValue(value);
    }

    public int yPositionForValue(int value) {
        return super.yPositionForValue(value);
    }
}
