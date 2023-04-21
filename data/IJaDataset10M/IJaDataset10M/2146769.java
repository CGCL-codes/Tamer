package org.xmlvm.iphone;

import java.util.ArrayList;
import java.util.Set;
import org.xmlvm.XMLVMSkeletonOnly;
import org.xmlvm.iphone.internal.renderer.UISegmentedControlRenderer;

@XMLVMSkeletonOnly
public class UISegmentedControl extends UIControl {

    private int selection = -1;

    private ArrayList<String> titles = new ArrayList<String>();

    private int style = UISegmentedControlStyle.Plain;

    private UIColor tintColor = null;

    private boolean momentary = false;

    public UISegmentedControl() {
        this(CGRect.Zero());
    }

    public UISegmentedControl(CGRect rect) {
        super(rect);
        xmlvmSetRenderer(new UISegmentedControlRenderer(this));
    }

    public UISegmentedControl(ArrayList items) {
        if (items == null || items.isEmpty()) {
            return;
        }
        if (items.get(0) instanceof String) {
            for (int i = 0; i < items.size(); i++) {
                insertSegmentWithTitle((String) items.get(i), i, false);
            }
        } else if (items.get(0) instanceof UIImage) {
            for (int i = 0; i < items.size(); i++) {
                insertSegmentWithImage((UIImage) items.get(i), i, false);
            }
        }
    }

    public void setTitle(String title, int index) {
        titles.set(index, title);
        setNeedsDisplay();
    }

    public String titleForSegmentAtIndex(int index) {
        return titles.get(index);
    }

    public void setImage(UIImage image, int index) {
    }

    public UIImage imageForSegmentAtIndex(int index) {
        return null;
    }

    public final void insertSegmentWithTitle(String title, int index, boolean animated) {
        titles.add(index, title);
        if (selection >= index) {
            selection++;
            fireEventValueChanged();
        }
        setNeedsDisplay();
    }

    public final void insertSegmentWithImage(UIImage img, int index, boolean animated) {
        insertSegmentWithTitle("IMG", index, animated);
    }

    public int numberOfSegments() {
        return titles.size();
    }

    public void removeAllSegments() {
        titles.clear();
        selection = -1;
        setNeedsDisplay();
    }

    public void removeSegmentAtIndex(int index, boolean animated) {
        titles.remove(index);
        if (selection > index || selection < 0) {
            selection--;
            fireEventValueChanged();
        } else if (selection == index) {
            selection = -1;
            fireEventValueChanged();
        }
        setNeedsDisplay();
    }

    public int getSelectedSegmentIndex() {
        return selection;
    }

    public void setSelectedSegmentIndex(int index) {
        if (index != selection) {
            selection = index;
            fireEventValueChanged();
            setNeedsDisplay();
        }
    }

    public int getSegmentedControlStyle() {
        return style;
    }

    public void setSegmentedControlStyle(int uiSegmentedControlStyle) {
        this.style = uiSegmentedControlStyle;
        setNeedsDisplay();
    }

    public UIColor getTintColor() {
        return tintColor;
    }

    public void setTintColor(UIColor tintColor) {
        this.tintColor = tintColor;
    }

    public boolean isMomentary() {
        return momentary;
    }

    public void setMomentary(boolean momentary) {
        this.momentary = momentary;
    }

    @Override
    public void touchesEnded(Set<UITouch> touches, UIEvent event) {
        UITouch t = touches.iterator().next();
        CGPoint p = t.locationInView(this);
        CGRect r = this.getBounds();
        int newselection = (int) (p.x / r.size.width * (numberOfSegments()));
        if (newselection < 0 || newselection >= numberOfSegments()) return;
        raiseEvent(UIControlEvent.TouchUpInside);
        if (newselection != selection) {
            setSelectedSegmentIndex(newselection);
        }
    }

    private void fireEventValueChanged() {
        raiseEvent(UIControlEvent.ValueChanged);
    }
}
