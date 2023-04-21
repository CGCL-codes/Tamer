package org.nakedobjects.nos.client.dnd.basic;

import org.nakedobjects.noa.reflect.Consent;
import org.nakedobjects.noa.reflect.NakedObjectAction.Type;
import org.nakedobjects.nof.core.reflect.Allow;
import org.nakedobjects.nof.core.util.DebugString;
import org.nakedobjects.nos.client.dnd.Canvas;
import org.nakedobjects.nos.client.dnd.Click;
import org.nakedobjects.nos.client.dnd.ContentDrag;
import org.nakedobjects.nos.client.dnd.Drag;
import org.nakedobjects.nos.client.dnd.DragStart;
import org.nakedobjects.nos.client.dnd.Toolkit;
import org.nakedobjects.nos.client.dnd.UserAction;
import org.nakedobjects.nos.client.dnd.UserActionSet;
import org.nakedobjects.nos.client.dnd.View;
import org.nakedobjects.nos.client.dnd.ViewAreaType;
import org.nakedobjects.nos.client.dnd.ViewState;
import org.nakedobjects.nos.client.dnd.Workspace;
import org.nakedobjects.nos.client.dnd.action.AbstractUserAction;
import org.nakedobjects.nos.client.dnd.action.WindowControl;
import org.nakedobjects.nos.client.dnd.drawing.Color;
import org.nakedobjects.nos.client.dnd.drawing.Location;
import org.nakedobjects.nos.client.dnd.drawing.Padding;
import org.nakedobjects.nos.client.dnd.drawing.Size;
import org.nakedobjects.nos.client.dnd.view.simple.AbstractView;

public class MinimizedView extends AbstractView {

    private class CloseWindowControl extends WindowControl {

        public CloseWindowControl(final View target) {
            super(new UserAction() {

                public Consent disabled(final View view) {
                    return Allow.DEFAULT;
                }

                public void execute(final Workspace workspace, final View view, final Location at) {
                    ((MinimizedView) view).close();
                }

                public String getDescription(final View view) {
                    return "Close " + view.getSpecification().getName();
                }

                public String getHelp(final View view) {
                    return null;
                }

                public String getName(final View view) {
                    return "Close view";
                }

                public Type getType() {
                    return USER;
                }
            }, target);
        }

        public void draw(final Canvas canvas) {
            int x = 0;
            int y = 0;
            Color crossColor = Toolkit.getColor("black");
            canvas.drawLine(x + 4, y + 3, x + 10, y + 9, crossColor);
            canvas.drawLine(x + 5, y + 3, x + 11, y + 9, crossColor);
            canvas.drawLine(x + 10, y + 3, x + 4, y + 9, crossColor);
            canvas.drawLine(x + 11, y + 3, x + 5, y + 9, crossColor);
        }
    }

    private class RestoreWindowControl extends WindowControl {

        public RestoreWindowControl(final View target) {
            super(new UserAction() {

                public Consent disabled(final View view) {
                    return Allow.DEFAULT;
                }

                public void execute(final Workspace workspace, final View view, final Location at) {
                    ((MinimizedView) view).restore();
                }

                public String getDescription(final View view) {
                    return "Restore " + view.getSpecification().getName() + " to normal size";
                }

                public String getHelp(final View view) {
                    return null;
                }

                public String getName(final View view) {
                    return "Restore view";
                }

                public Type getType() {
                    return USER;
                }
            }, target);
        }

        public void draw(final Canvas canvas) {
            int x = 0;
            int y = 0;
            canvas.drawRectangle(x + 1, y + 1, WIDTH - 1, HEIGHT - 1, Toolkit.getColor("black"));
            canvas.drawLine(x + 2, y + 2, x + WIDTH - 2, y + 2, Toolkit.getColor("black"));
            canvas.drawLine(x + 2, y + 3, x + WIDTH - 2, y + 3, Toolkit.getColor("black"));
        }
    }

    private static final int BORDER_WIDTH = 5;

    private WindowControl controls[];

    private View iconView;

    private final View viewToMinimize;

    public MinimizedView(final View viewToMinimize) {
        super(viewToMinimize.getContent(), null, null);
        this.viewToMinimize = viewToMinimize;
        iconView = new SubviewIconSpecification().createView(viewToMinimize.getContent(), null);
        iconView.setParent(this);
        controls = new WindowControl[] { new RestoreWindowControl(this), new CloseWindowControl(this) };
    }

    public String debugDetails() {
        DebugString b = new DebugString();
        b.append(super.debugDetails());
        b.appendln("minimized view", viewToMinimize);
        b.appendln();
        b.appendln("icon size", iconView.getSize());
        b.append(iconView);
        return b.toString();
    }

    public void dispose() {
        super.dispose();
        viewToMinimize.dispose();
    }

    public Drag dragStart(final DragStart drag) {
        if (iconView.getBounds().contains(drag.getLocation())) {
            drag.subtract(BORDER_WIDTH, BORDER_WIDTH);
            return iconView.dragStart(drag);
        } else {
            return super.dragStart(drag);
        }
    }

    public void draw(final Canvas canvas) {
        super.draw(canvas);
        Size size = getSize();
        int width = size.getWidth();
        int height = size.getHeight();
        int left = 3;
        int top = 3;
        boolean hasFocus = containsFocus();
        Color lightColor = hasFocus ? Toolkit.getColor("secondary1") : Toolkit.getColor("secondary2");
        canvas.clearBackground(this, Toolkit.getColor("background.window"));
        canvas.drawRectangle(1, 0, width - 2, height, lightColor);
        canvas.drawRectangle(0, 1, width, height - 2, lightColor);
        for (int i = 2; i < left; i++) {
            canvas.drawRectangle(i, i, width - 2 * i, height - 2 * i, lightColor);
        }
        ViewState state = getState();
        if (state.isActive()) {
            int i = left;
            canvas.drawRectangle(i, top, width - 2 * i, height - 2 * i - top, Toolkit.getColor("active"));
        }
        int bw = controls[0].getLocation().getX() - 3;
        canvas.drawSolidRectangle(bw, top, width - bw - 3, height - top * 2, Toolkit.getColor("secondary3"));
        canvas.drawLine(bw - 1, top, bw - 1, height - top * 2, lightColor);
        for (int i = 0; controls != null && i < controls.length; i++) {
            Canvas controlCanvas = canvas.createSubcanvas(controls[i].getBounds());
            controls[i].draw(controlCanvas);
        }
        Canvas c = canvas.createSubcanvas(iconView.getBounds());
        iconView.draw(c);
    }

    public Size getMaximumSize() {
        Size size = new Size();
        size.extendWidth(BORDER_WIDTH);
        Size iconMaximumSize = iconView.getMaximumSize();
        size.extendWidth(iconMaximumSize.getWidth());
        size.extendHeight(iconMaximumSize.getHeight());
        size.ensureHeight(WindowControl.HEIGHT);
        size.extendHeight(BORDER_WIDTH);
        size.extendHeight(BORDER_WIDTH);
        size.extendWidth(HPADDING);
        size.extendWidth(controls.length * (WindowControl.WIDTH + HPADDING));
        size.extendWidth(BORDER_WIDTH);
        return size;
    }

    public Padding getPadding() {
        return new Padding(BORDER_WIDTH, BORDER_WIDTH, BORDER_WIDTH, BORDER_WIDTH);
    }

    public void layout(final Size maximumSize) {
        Size size = getMaximumSize();
        layoutControls(size.getWidth());
        size.contractWidth(BORDER_WIDTH * 2);
        size.contractWidth(HPADDING);
        size.contractWidth(controls.length * (WindowControl.WIDTH + HPADDING));
        size.contractHeight(BORDER_WIDTH * 2);
        iconView.setLocation(new Location(BORDER_WIDTH, BORDER_WIDTH));
        iconView.setSize(size);
    }

    private void layoutControls(final int width) {
        int widthControl = WindowControl.WIDTH + HPADDING;
        int x = width - BORDER_WIDTH + HPADDING;
        x -= widthControl * controls.length;
        int y = BORDER_WIDTH;
        for (int i = 0; i < controls.length; i++) {
            controls[i].setSize(controls[i].getMaximumSize());
            controls[i].setLocation(new Location(x, y));
            x += widthControl;
        }
    }

    private void restore() {
        Workspace workspace = getWorkspace();
        View[] views = workspace.getSubviews();
        for (int i = 0; i < views.length; i++) {
            if (views[i] == this) {
                viewToMinimize.setParent(workspace);
                workspace.removeView(this);
                workspace.addView(viewToMinimize);
                workspace.invalidateLayout();
                return;
            }
        }
    }

    private void close() {
        Workspace workspace = getWorkspace();
        View[] views = workspace.getSubviews();
        for (int i = 0; i < views.length; i++) {
            if (views[i] == this) {
                viewToMinimize.setParent(workspace);
                workspace.removeView(this);
                workspace.invalidateLayout();
                return;
            }
        }
    }

    public void secondClick(final Click click) {
        restore();
    }

    public ViewAreaType viewAreaType(final Location location) {
        location.subtract(BORDER_WIDTH, BORDER_WIDTH);
        return iconView.viewAreaType(location);
    }

    public void viewMenuOptions(final UserActionSet options) {
        options.add(new AbstractUserAction("Restore") {

            public void execute(final Workspace workspace, final View view, final Location at) {
                restore();
            }
        });
        super.viewMenuOptions(options);
    }

    public void firstClick(final Click click) {
        View button = overControl(click.getLocation());
        if (button == null) {
        } else {
            button.firstClick(click);
        }
    }

    private View overControl(final Location location) {
        for (int i = 0; i < controls.length; i++) {
            WindowControl control = controls[i];
            if (control.getBounds().contains(location)) {
                return control;
            }
        }
        return null;
    }

    public void dragIn(final ContentDrag drag) {
        if (iconView.getBounds().contains(drag.getTargetLocation())) {
            drag.subtract(BORDER_WIDTH, BORDER_WIDTH);
            iconView.dragIn(drag);
        }
    }

    public void dragOut(final ContentDrag drag) {
        if (iconView.getBounds().contains(drag.getTargetLocation())) {
            drag.subtract(BORDER_WIDTH, BORDER_WIDTH);
            iconView.dragOut(drag);
        }
    }

    public View identify(final Location location) {
        if (iconView.getBounds().contains(location)) {
            location.subtract(BORDER_WIDTH, BORDER_WIDTH);
            return iconView.identify(location);
        }
        return this;
    }

    public void drop(final ContentDrag drag) {
        if (iconView.getBounds().contains(drag.getTargetLocation())) {
            drag.subtract(BORDER_WIDTH, BORDER_WIDTH);
            iconView.drop(drag);
        }
    }
}
