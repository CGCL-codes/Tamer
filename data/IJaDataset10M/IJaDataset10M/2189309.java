package org.nakedobjects.plugins.dnd.viewer.border;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.plugins.dnd.Canvas;
import org.nakedobjects.plugins.dnd.Click;
import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.ContentDrag;
import org.nakedobjects.plugins.dnd.Drag;
import org.nakedobjects.plugins.dnd.DragStart;
import org.nakedobjects.plugins.dnd.Feedback;
import org.nakedobjects.plugins.dnd.FocusManager;
import org.nakedobjects.plugins.dnd.InternalDrag;
import org.nakedobjects.plugins.dnd.KeyboardAction;
import org.nakedobjects.plugins.dnd.UserActionSet;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewAreaType;
import org.nakedobjects.plugins.dnd.ViewAxis;
import org.nakedobjects.plugins.dnd.ViewDrag;
import org.nakedobjects.plugins.dnd.ViewSpecification;
import org.nakedobjects.plugins.dnd.ViewState;
import org.nakedobjects.plugins.dnd.Viewer;
import org.nakedobjects.plugins.dnd.Workspace;
import org.nakedobjects.plugins.dnd.viewer.drawing.Bounds;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;
import org.nakedobjects.plugins.dnd.viewer.drawing.Padding;
import org.nakedobjects.plugins.dnd.viewer.drawing.Size;

public abstract class AbstractViewDecorator implements View {

    protected View wrappedView;

    protected AbstractViewDecorator(final View wrappedView) {
        this.wrappedView = wrappedView;
        wrappedView.setView(this);
    }

    public void addView(final View view) {
        wrappedView.addView(view);
    }

    public Consent canChangeValue() {
        return wrappedView.canChangeValue();
    }

    public boolean canFocus() {
        return wrappedView.canFocus();
    }

    public boolean contains(final View view) {
        return wrappedView.contains(view);
    }

    public boolean containsFocus() {
        return wrappedView.containsFocus();
    }

    public void contentMenuOptions(final UserActionSet menuOptions) {
        wrappedView.contentMenuOptions(menuOptions);
    }

    public final void debug(final DebugString debug) {
        debug.append("Decorator: ");
        debug.indent();
        debugDetails(debug);
        debug.appendln("set size", getSize());
        debug.appendln("maximum", getMaximumSize());
        debug.appendln("required", getRequiredSize(Size.ALL));
        debug.appendln("padding", getPadding());
        debug.appendln("baseline", getBaseline());
        debug.appendln();
        debug.unindent();
        wrappedView.debug(debug);
    }

    protected void debugDetails(final DebugString debug) {
        final String name = getClass().getName();
        debug.appendln(name.substring(name.lastIndexOf('.') + 1));
    }

    public void debugStructure(final DebugString debug) {
        wrappedView.debugStructure(debug);
    }

    public void dispose() {
        wrappedView.dispose();
    }

    public void drag(final ContentDrag contentDrag) {
        wrappedView.drag(contentDrag);
    }

    public void drag(final InternalDrag drag) {
        wrappedView.drag(drag);
    }

    public void dragCancel(final InternalDrag drag) {
        wrappedView.dragCancel(drag);
    }

    public View dragFrom(final Location location) {
        return wrappedView.dragFrom(location);
    }

    public void dragIn(final ContentDrag drag) {
        wrappedView.dragIn(drag);
    }

    public void dragOut(final ContentDrag drag) {
        wrappedView.dragOut(drag);
    }

    public Drag dragStart(final DragStart drag) {
        return wrappedView.dragStart(drag);
    }

    public void dragTo(final InternalDrag drag) {
        wrappedView.dragTo(drag);
    }

    public void draw(final Canvas canvas) {
        wrappedView.draw(canvas);
    }

    public void drop(final ContentDrag drag) {
        wrappedView.drop(drag);
    }

    public void drop(final ViewDrag drag) {
        wrappedView.drop(drag);
    }

    public void editComplete(boolean moveFocus, boolean toNextField) {
        wrappedView.editComplete(moveFocus, toNextField);
    }

    public void entered() {
        wrappedView.entered();
    }

    public void exited() {
        wrappedView.exited();
    }

    public void firstClick(final Click click) {
        wrappedView.firstClick(click);
    }

    public void focusLost() {
        wrappedView.focusLost();
    }

    public void focusReceived() {
        wrappedView.focusReceived();
    }

    public Location getAbsoluteLocation() {
        return wrappedView.getAbsoluteLocation();
    }

    public int getBaseline() {
        return wrappedView.getBaseline();
    }

    public Bounds getBounds() {
        return new Bounds(getLocation(), getSize());
    }

    public Content getContent() {
        return wrappedView.getContent();
    }

    public FocusManager getFocusManager() {
        return wrappedView.getFocusManager();
    }

    public int getId() {
        return wrappedView.getId();
    }

    public Location getLocation() {
        return wrappedView.getLocation();
    }

    public Padding getPadding() {
        return wrappedView.getPadding();
    }

    public View getParent() {
        return wrappedView.getParent();
    }

    public Size getRequiredSize(final Size maximumSize) {
        return wrappedView.getRequiredSize(maximumSize);
    }

    public Size getMaximumSize() {
        return wrappedView.getMaximumSize();
    }

    public Size getSize() {
        return wrappedView.getSize();
    }

    public ViewSpecification getSpecification() {
        return wrappedView.getSpecification();
    }

    public ViewState getState() {
        return wrappedView.getState();
    }

    public View[] getSubviews() {
        return wrappedView.getSubviews();
    }

    public View getView() {
        return wrappedView.getView();
    }

    public ViewAxis getViewAxis() {
        return wrappedView.getViewAxis();
    }

    public Viewer getViewManager() {
        return wrappedView.getViewManager();
    }

    public Feedback getFeedbackManager() {
        return wrappedView.getFeedbackManager();
    }

    public Workspace getWorkspace() {
        return wrappedView.getWorkspace();
    }

    public boolean hasFocus() {
        return wrappedView.hasFocus();
    }

    public View identify(final Location mouseLocation) {
        return wrappedView.identify(mouseLocation);
    }

    public void invalidateContent() {
        wrappedView.invalidateContent();
    }

    public void invalidateLayout() {
        wrappedView.invalidateLayout();
    }

    public void keyPressed(final KeyboardAction key) {
        wrappedView.keyPressed(key);
    }

    public void keyReleased(final int keyCode, final int modifiers) {
        wrappedView.keyReleased(keyCode, modifiers);
    }

    public void keyTyped(final char keyCode) {
        wrappedView.keyTyped(keyCode);
    }

    public void layout(final Size maximumSize) {
        wrappedView.layout(maximumSize);
    }

    public void limitBoundsWithin(final Size size) {
        wrappedView.limitBoundsWithin(size);
    }

    public void markDamaged() {
        wrappedView.markDamaged();
    }

    public void markDamaged(final Bounds bounds) {
        wrappedView.markDamaged(bounds);
    }

    public void mouseDown(final Click click) {
        wrappedView.mouseDown(click);
    }

    public void mouseMoved(final Location at) {
        wrappedView.mouseMoved(at);
    }

    public void mouseUp(final Click click) {
        wrappedView.mouseUp(click);
    }

    public void objectActionResult(final NakedObject result, final Location at) {
        wrappedView.objectActionResult(result, at);
    }

    public View pickupContent(final Location location) {
        return wrappedView.pickupContent(location);
    }

    public View pickupView(final Location location) {
        return wrappedView.pickupView(location);
    }

    public void print(final Canvas canvas) {
        wrappedView.print(canvas);
    }

    public void refresh() {
        wrappedView.refresh();
    }

    public void removeView(final View view) {
        wrappedView.removeView(view);
    }

    public void replaceView(final View toReplace, final View replacement) {
        wrappedView.replaceView(toReplace, replacement);
    }

    public void secondClick(final Click click) {
        wrappedView.secondClick(click);
    }

    public void setBounds(final Bounds bounds) {
        wrappedView.setBounds(bounds);
    }

    public void setFocusManager(final FocusManager focusManager) {
        wrappedView.setFocusManager(focusManager);
    }

    public void setLocation(final Location point) {
        wrappedView.setLocation(point);
    }

    public void setParent(final View view) {
        wrappedView.setParent(view);
    }

    public void setMaximumSize(final Size size) {
        wrappedView.setMaximumSize(size);
    }

    public void setSize(final Size size) {
        wrappedView.setSize(size);
    }

    public void setView(final View view) {
        wrappedView.setView(view);
    }

    public View subviewFor(final Location location) {
        return wrappedView.subviewFor(location);
    }

    public void thirdClick(final Click click) {
        wrappedView.thirdClick(click);
    }

    @Override
    public String toString() {
        final String name = getClass().getName();
        return name.substring(name.lastIndexOf('.') + 1) + "/" + wrappedView;
    }

    public void update(final NakedObject object) {
        wrappedView.update(object);
    }

    public void updateView() {
        wrappedView.updateView();
    }

    public ViewAreaType viewAreaType(final Location mouseLocation) {
        return wrappedView.viewAreaType(mouseLocation);
    }

    public void viewMenuOptions(final UserActionSet menuOptions) {
        wrappedView.viewMenuOptions(menuOptions);
    }
}
