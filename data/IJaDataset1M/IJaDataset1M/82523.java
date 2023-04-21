package com.peterhi.client.ui.widgets;

import org.eclipse.swt.accessibility.Accessible;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.DragDetectListener;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.HelpListener;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.ShellListener;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.GCData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Caret;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Shell;

public class Window {

    private Display display;

    private Shell shell;

    public Window() {
        shell = new Shell();
    }

    public void mainloop() {
        display = Display.getDefault();
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        shell.dispose();
        display.dispose();
    }

    public void addControlListener(ControlListener arg0) {
        shell.addControlListener(arg0);
    }

    public void addDisposeListener(DisposeListener arg0) {
        shell.addDisposeListener(arg0);
    }

    public void addDragDetectListener(DragDetectListener arg0) {
        shell.addDragDetectListener(arg0);
    }

    public void addFocusListener(FocusListener arg0) {
        shell.addFocusListener(arg0);
    }

    public void addHelpListener(HelpListener arg0) {
        shell.addHelpListener(arg0);
    }

    public void addKeyListener(KeyListener arg0) {
        shell.addKeyListener(arg0);
    }

    public void addListener(int arg0, Listener arg1) {
        shell.addListener(arg0, arg1);
    }

    public void addMenuDetectListener(MenuDetectListener arg0) {
        shell.addMenuDetectListener(arg0);
    }

    public void addMouseListener(MouseListener arg0) {
        shell.addMouseListener(arg0);
    }

    public void addMouseMoveListener(MouseMoveListener arg0) {
        shell.addMouseMoveListener(arg0);
    }

    public void addMouseTrackListener(MouseTrackListener arg0) {
        shell.addMouseTrackListener(arg0);
    }

    public void addMouseWheelListener(MouseWheelListener arg0) {
        shell.addMouseWheelListener(arg0);
    }

    public void addPaintListener(PaintListener arg0) {
        shell.addPaintListener(arg0);
    }

    public void addShellListener(ShellListener arg0) {
        shell.addShellListener(arg0);
    }

    public void addTraverseListener(TraverseListener arg0) {
        shell.addTraverseListener(arg0);
    }

    public void changed(Control[] arg0) {
        shell.changed(arg0);
    }

    public void close() {
        shell.close();
    }

    public Point computeSize(int arg0, int arg1, boolean arg2) {
        return shell.computeSize(arg0, arg1, arg2);
    }

    public Point computeSize(int arg0, int arg1) {
        return shell.computeSize(arg0, arg1);
    }

    public Rectangle computeTrim(int arg0, int arg1, int arg2, int arg3) {
        return shell.computeTrim(arg0, arg1, arg2, arg3);
    }

    public void dispose() {
        shell.dispose();
    }

    public boolean dragDetect(Event arg0) {
        return shell.dragDetect(arg0);
    }

    public boolean dragDetect(MouseEvent arg0) {
        return shell.dragDetect(arg0);
    }

    public void drawBackground(GC arg0, int arg1, int arg2, int arg3, int arg4) {
        shell.drawBackground(arg0, arg1, arg2, arg3, arg4);
    }

    public boolean equals(Object obj) {
        return shell.equals(obj);
    }

    public void forceActive() {
        shell.forceActive();
    }

    public boolean forceFocus() {
        return shell.forceFocus();
    }

    public Accessible getAccessible() {
        return shell.getAccessible();
    }

    public Color getBackground() {
        return shell.getBackground();
    }

    public Image getBackgroundImage() {
        return shell.getBackgroundImage();
    }

    public int getBackgroundMode() {
        return shell.getBackgroundMode();
    }

    public int getBorderWidth() {
        return shell.getBorderWidth();
    }

    public Rectangle getBounds() {
        return shell.getBounds();
    }

    public Caret getCaret() {
        return shell.getCaret();
    }

    public Control[] getChildren() {
        return shell.getChildren();
    }

    public Rectangle getClientArea() {
        return shell.getClientArea();
    }

    public Cursor getCursor() {
        return shell.getCursor();
    }

    public Object getData() {
        return shell.getData();
    }

    public Object getData(String arg0) {
        return shell.getData(arg0);
    }

    public Button getDefaultButton() {
        return shell.getDefaultButton();
    }

    public Display getDisplay() {
        return shell.getDisplay();
    }

    public boolean getDragDetect() {
        return shell.getDragDetect();
    }

    public boolean getEnabled() {
        return shell.getEnabled();
    }

    public Font getFont() {
        return shell.getFont();
    }

    public Color getForeground() {
        return shell.getForeground();
    }

    public ScrollBar getHorizontalBar() {
        return shell.getHorizontalBar();
    }

    public Image getImage() {
        return shell.getImage();
    }

    public Image[] getImages() {
        return shell.getImages();
    }

    public int getImeInputMode() {
        return shell.getImeInputMode();
    }

    public Layout getLayout() {
        return shell.getLayout();
    }

    public Object getLayoutData() {
        return shell.getLayoutData();
    }

    public boolean getLayoutDeferred() {
        return shell.getLayoutDeferred();
    }

    public Point getLocation() {
        return shell.getLocation();
    }

    public boolean getMaximized() {
        return shell.getMaximized();
    }

    public Menu getMenu() {
        return shell.getMenu();
    }

    public Menu getMenuBar() {
        return shell.getMenuBar();
    }

    public boolean getMinimized() {
        return shell.getMinimized();
    }

    public Point getMinimumSize() {
        return shell.getMinimumSize();
    }

    public Monitor getMonitor() {
        return shell.getMonitor();
    }

    public Composite getParent() {
        return shell.getParent();
    }

    public Region getRegion() {
        return shell.getRegion();
    }

    public Shell getShell() {
        return shell.getShell();
    }

    public Shell[] getShells() {
        return shell.getShells();
    }

    public Point getSize() {
        return shell.getSize();
    }

    public int getStyle() {
        return shell.getStyle();
    }

    public Control[] getTabList() {
        return shell.getTabList();
    }

    public String getText() {
        return shell.getText();
    }

    public String getToolTipText() {
        return shell.getToolTipText();
    }

    public ScrollBar getVerticalBar() {
        return shell.getVerticalBar();
    }

    public boolean getVisible() {
        return shell.getVisible();
    }

    public int hashCode() {
        return shell.hashCode();
    }

    public void internal_dispose_GC(int arg0, GCData arg1) {
        shell.internal_dispose_GC(arg0, arg1);
    }

    public int internal_new_GC(GCData arg0) {
        return shell.internal_new_GC(arg0);
    }

    public boolean isDisposed() {
        return shell.isDisposed();
    }

    public boolean isEnabled() {
        return shell.isEnabled();
    }

    public boolean isFocusControl() {
        return shell.isFocusControl();
    }

    public boolean isLayoutDeferred() {
        return shell.isLayoutDeferred();
    }

    public boolean isListening(int arg0) {
        return shell.isListening(arg0);
    }

    public boolean isReparentable() {
        return shell.isReparentable();
    }

    public boolean isVisible() {
        return shell.isVisible();
    }

    public void layout() {
        shell.layout();
    }

    public void layout(boolean arg0, boolean arg1) {
        shell.layout(arg0, arg1);
    }

    public void layout(boolean arg0) {
        shell.layout(arg0);
    }

    public void layout(Control[] arg0) {
        shell.layout(arg0);
    }

    public void moveAbove(Control arg0) {
        shell.moveAbove(arg0);
    }

    public void moveBelow(Control arg0) {
        shell.moveBelow(arg0);
    }

    public void notifyListeners(int arg0, Event arg1) {
        shell.notifyListeners(arg0, arg1);
    }

    public void open() {
        shell.open();
    }

    public void pack() {
        shell.pack();
    }

    public void pack(boolean arg0) {
        shell.pack(arg0);
    }

    public void redraw() {
        shell.redraw();
    }

    public void redraw(int arg0, int arg1, int arg2, int arg3, boolean arg4) {
        shell.redraw(arg0, arg1, arg2, arg3, arg4);
    }

    public void removeControlListener(ControlListener arg0) {
        shell.removeControlListener(arg0);
    }

    public void removeDisposeListener(DisposeListener arg0) {
        shell.removeDisposeListener(arg0);
    }

    public void removeDragDetectListener(DragDetectListener arg0) {
        shell.removeDragDetectListener(arg0);
    }

    public void removeFocusListener(FocusListener arg0) {
        shell.removeFocusListener(arg0);
    }

    public void removeHelpListener(HelpListener arg0) {
        shell.removeHelpListener(arg0);
    }

    public void removeKeyListener(KeyListener arg0) {
        shell.removeKeyListener(arg0);
    }

    public void removeListener(int arg0, Listener arg1) {
        shell.removeListener(arg0, arg1);
    }

    public void removeMenuDetectListener(MenuDetectListener arg0) {
        shell.removeMenuDetectListener(arg0);
    }

    public void removeMouseListener(MouseListener arg0) {
        shell.removeMouseListener(arg0);
    }

    public void removeMouseMoveListener(MouseMoveListener arg0) {
        shell.removeMouseMoveListener(arg0);
    }

    public void removeMouseTrackListener(MouseTrackListener arg0) {
        shell.removeMouseTrackListener(arg0);
    }

    public void removeMouseWheelListener(MouseWheelListener arg0) {
        shell.removeMouseWheelListener(arg0);
    }

    public void removePaintListener(PaintListener arg0) {
        shell.removePaintListener(arg0);
    }

    public void removeShellListener(ShellListener arg0) {
        shell.removeShellListener(arg0);
    }

    public void removeTraverseListener(TraverseListener arg0) {
        shell.removeTraverseListener(arg0);
    }

    public void scroll(int arg0, int arg1, int arg2, int arg3, int arg4, int arg5, boolean arg6) {
        shell.scroll(arg0, arg1, arg2, arg3, arg4, arg5, arg6);
    }

    public void setActive() {
        shell.setActive();
    }

    public void setBackground(Color arg0) {
        shell.setBackground(arg0);
    }

    public void setBackgroundImage(Image arg0) {
        shell.setBackgroundImage(arg0);
    }

    public void setBackgroundMode(int arg0) {
        shell.setBackgroundMode(arg0);
    }

    public void setBounds(int arg0, int arg1, int arg2, int arg3) {
        shell.setBounds(arg0, arg1, arg2, arg3);
    }

    public void setBounds(Rectangle arg0) {
        shell.setBounds(arg0);
    }

    public void setCapture(boolean arg0) {
        shell.setCapture(arg0);
    }

    public void setCaret(Caret arg0) {
        shell.setCaret(arg0);
    }

    public void setCursor(Cursor arg0) {
        shell.setCursor(arg0);
    }

    public void setData(Object arg0) {
        shell.setData(arg0);
    }

    public void setData(String arg0, Object arg1) {
        shell.setData(arg0, arg1);
    }

    public void setDefaultButton(Button arg0) {
        shell.setDefaultButton(arg0);
    }

    public void setDragDetect(boolean arg0) {
        shell.setDragDetect(arg0);
    }

    public void setEnabled(boolean arg0) {
        shell.setEnabled(arg0);
    }

    public boolean setFocus() {
        return shell.setFocus();
    }

    public void setFont(Font arg0) {
        shell.setFont(arg0);
    }

    public void setForeground(Color arg0) {
        shell.setForeground(arg0);
    }

    public void setImage(Image arg0) {
        shell.setImage(arg0);
    }

    public void setImages(Image[] arg0) {
        shell.setImages(arg0);
    }

    public void setImeInputMode(int arg0) {
        shell.setImeInputMode(arg0);
    }

    public void setLayout(Layout arg0) {
        shell.setLayout(arg0);
    }

    public void setLayoutData(Object arg0) {
        shell.setLayoutData(arg0);
    }

    public void setLayoutDeferred(boolean arg0) {
        shell.setLayoutDeferred(arg0);
    }

    public void setLocation(int arg0, int arg1) {
        shell.setLocation(arg0, arg1);
    }

    public void setLocation(Point arg0) {
        shell.setLocation(arg0);
    }

    public void setMaximized(boolean arg0) {
        shell.setMaximized(arg0);
    }

    public void setMenu(Menu arg0) {
        shell.setMenu(arg0);
    }

    public void setMenuBar(Menu arg0) {
        shell.setMenuBar(arg0);
    }

    public void setMinimized(boolean arg0) {
        shell.setMinimized(arg0);
    }

    public void setMinimumSize(int arg0, int arg1) {
        shell.setMinimumSize(arg0, arg1);
    }

    public void setMinimumSize(Point arg0) {
        shell.setMinimumSize(arg0);
    }

    public boolean setParent(Composite arg0) {
        return shell.setParent(arg0);
    }

    public void setRedraw(boolean arg0) {
        shell.setRedraw(arg0);
    }

    public void setRegion(Region arg0) {
        shell.setRegion(arg0);
    }

    public void setSize(int arg0, int arg1) {
        shell.setSize(arg0, arg1);
    }

    public void setSize(Point arg0) {
        shell.setSize(arg0);
    }

    public void setTabList(Control[] arg0) {
        shell.setTabList(arg0);
    }

    public void setText(String arg0) {
        shell.setText(arg0);
    }

    public void setToolTipText(String arg0) {
        shell.setToolTipText(arg0);
    }

    public void setVisible(boolean arg0) {
        shell.setVisible(arg0);
    }

    public Point toControl(int arg0, int arg1) {
        return shell.toControl(arg0, arg1);
    }

    public Point toControl(Point arg0) {
        return shell.toControl(arg0);
    }

    public Point toDisplay(int arg0, int arg1) {
        return shell.toDisplay(arg0, arg1);
    }

    public Point toDisplay(Point arg0) {
        return shell.toDisplay(arg0);
    }

    public String toString() {
        return shell.toString();
    }

    public boolean traverse(int arg0) {
        return shell.traverse(arg0);
    }

    public void update() {
        shell.update();
    }
}
