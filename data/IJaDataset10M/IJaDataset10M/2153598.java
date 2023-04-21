package org.xmlvm.common.wp7.objects;

import org.xmlvm.commondevice.objects.CommonDeviceView;
import Compatlib.System.Object;
import Compatlib.System.String;
import Compatlib.System.Windows.RoutedEventHandler;
import Compatlib.System.Windows.Size;
import Compatlib.System.Windows.UIElement;
import Compatlib.System.Windows.Controls.Image;
import Compatlib.System.Windows.Controls.Panel;
import Compatlib.System.Windows.Controls.Primitives.ButtonBase;
import Compatlib.System.Windows.Input.ManipulationCompletedEventArgs;
import Compatlib.System.Windows.Input.ManipulationDeltaEventArgs;
import Compatlib.System.Windows.Input.ManipulationStartedEventArgs;
import Compatlib.System.Windows.Media.Stretch;
import android.graphics.Rect;
import android.internal.Assert;
import android.view.MotionEvent;
import android.view.View;

/**
 *
 */
public class WP7View extends Object implements CommonDeviceView {

    private UIElement element;

    private View androidView;

    public WP7View(View view) {
        this.androidView = view;
        setElement(new Panel());
    }

    public UIElement getElement() {
        return element;
    }

    public void setElement(UIElement element) {
        this.element = element;
        this.element.ManipulationStarted.__add(new RoutedEventHandler(this, new String("OnManipulationStarted")));
        this.element.ManipulationDelta.__add(new RoutedEventHandler(this, new String("OnManipulationDelta")));
        this.element.ManipulationCompleted.__add(new RoutedEventHandler(this, new String("OnManipulationCompleted")));
    }

    @Override
    public Rect getFrame() {
        return WP7View.toRectangle(element.getDesiredSize());
    }

    @Override
    public void setFrame(Rect frame) {
        element.Measure(WP7View.toSize(frame));
        element.xmlvmSetXY(frame.top, frame.left);
    }

    @Override
    public void setHidden(boolean b) {
    }

    @Override
    public void setNeedsDisplay() {
        element.InvalidateArrange();
    }

    @Override
    public void setBackgroundColor(int bcolor) {
    }

    @Override
    public void addSubview(CommonDeviceView metricsView) {
        if (element instanceof Panel) {
            ((Panel) element).getChildren().Add(((WP7View) metricsView).getElement());
        } else {
            Assert.NOT_IMPLEMENTED();
        }
    }

    @Override
    public void insertSubview(CommonDeviceView metricsView, int idx) {
        if (element instanceof Panel) {
            ((Panel) element).getChildren().Insert(idx, ((WP7View) metricsView).getElement());
        } else {
            Assert.NOT_IMPLEMENTED();
        }
    }

    @Override
    public void removeFromSuperview() {
        if (element.getVisualParent() instanceof Panel) {
            ((Panel) element.getVisualParent()).getChildren().Remove(element);
        } else {
            Assert.NOT_IMPLEMENTED();
        }
    }

    @Override
    public void setContentMode(int mode) {
        if (element instanceof Image) {
            switch(mode) {
                case CommonDeviceView.CENTER:
                    break;
                case CommonDeviceView.SCALE_ASPECT_FILL:
                case CommonDeviceView.SCALE_ASPECT_FIT:
                    ((Image) element).setStretch(Stretch.Uniform);
                    break;
                case CommonDeviceView.SCALE_TO_FILL:
                    ((Image) element).setStretch(Stretch.Fill);
                    break;
            }
        } else {
            Assert.NOT_IMPLEMENTED();
        }
    }

    public void OnManipulationStarted(Object sender, ManipulationStartedEventArgs args) {
        boolean event = xmlvmTouchesEvent(MotionEvent.ACTION_DOWN, (int) args.getManipulationOrigin().getX(), (int) args.getManipulationOrigin().getY());
        args.setHandled(event);
    }

    public void OnManipulationDelta(Object sender, ManipulationDeltaEventArgs args) {
        int x = (int) (args.getDeltaManipulation().getTranslation().getX() + args.getManipulationOrigin().getX());
        int y = (int) (args.getDeltaManipulation().getTranslation().getY() + args.getManipulationOrigin().getY());
        boolean event = xmlvmTouchesEvent(MotionEvent.ACTION_MOVE, x, y);
        args.setHandled(event);
    }

    public void OnManipulationCompleted(Object sender, ManipulationCompletedEventArgs args) {
        boolean event = xmlvmTouchesEvent(MotionEvent.ACTION_UP, (int) args.getManipulationOrigin().getX(), (int) args.getManipulationOrigin().getY());
        args.setHandled(event);
    }

    public boolean xmlvmTouchesEvent(int action, int x, int y) {
        if (action == MotionEvent.ACTION_UP && androidView.getOnClickListener() != null) {
            androidView.getOnClickListener().onClick(androidView);
        }
        MotionEvent motionEvent = new MotionEvent(action, x, y);
        if (androidView.onTouchEvent(motionEvent)) {
            return true;
        }
        if (androidView.getOnTouchListener() != null && androidView.getOnTouchListener().onTouch(androidView, motionEvent)) {
            return true;
        }
        if (element instanceof ButtonBase) {
            return false;
        }
        if (androidView.getParent() != null && (androidView.getParent() instanceof View)) {
            return ((WP7View) ((View) androidView.getParent()).xmlvmGetViewHandler().getContentView()).xmlvmTouchesEvent(action, x, y);
        }
        return false;
    }

    public static Rect toRectangle(Size desiredSize) {
        return new Rect(0, 0, (int) desiredSize.getWidth(), (int) desiredSize.getHeight());
    }

    public static Size toSize(Rect frame) {
        return new Size(frame.right - frame.left, frame.bottom - frame.top);
    }
}
