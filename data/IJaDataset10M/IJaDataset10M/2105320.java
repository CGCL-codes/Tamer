package com.gempukku.animator;

import com.gempukku.animator.composite.ContainerAnimated;
import com.gempukku.animator.utils.DrawUtilities;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class AnimatorComponent extends JPanel {

    private final Animator _animator;

    private final AnimatedCallbackImpl _animatedCallbackImpl = new AnimatedCallbackImpl();

    private ContainerAnimated _containerAnimated = new ContainerAnimated();

    private GraphicsQuality _graphicsQuality = GraphicsQuality.BEST;

    private long _lastStoppedAtFrame = 0L;

    private long _animationStartTime;

    private boolean _running;

    private boolean _displayFPS = false;

    private Color _displayFPSColor = Color.BLACK;

    private int _fps;

    private int _framesCounter;

    private long _second;

    private final List<MouseEvent> _mouseEventBuffer = new ArrayList<MouseEvent>();

    private final AnimatedMouseListenerStack animatedMouseListenerStack = new AnimatedMouseListenerStack();

    private int _displayFPSHorizontal = SwingConstants.CENTER;

    private int _displayFPSVertical = SwingConstants.CENTER;

    private boolean _mouseDownInComponent = false;

    public AnimatorComponent() {
        this(Animator.getAnimator());
    }

    public AnimatorComponent(Animator animator) {
        _animator = animator;
        setDoubleBuffered(false);
        setOpaque(true);
        AnimatorMouseListener animatorMouseListener = new AnimatorMouseListener();
        this.addMouseListener(animatorMouseListener);
        this.addMouseMotionListener(animatorMouseListener);
        addAncestorListener(new AnimatorAncestorListener());
    }

    public boolean isUserInputEnabled() {
        return _containerAnimated.isUserInputEnabled();
    }

    public void setUserInputEnabled(boolean userInputEnabled) {
        _containerAnimated.setUserInputEnabled(userInputEnabled);
    }

    public boolean isDisplayFPS() {
        return _displayFPS;
    }

    public void setDisplayFPS(boolean displayFPS) {
        this._displayFPS = displayFPS;
    }

    public void setDisplayFPSColor(Color color) {
        _displayFPSColor = color;
    }

    public void setDisplayFPSHorizontal(int displayFPSHorizontal) {
        _displayFPSHorizontal = displayFPSHorizontal;
    }

    public void setDisplayFPSVertical(int displayFPSVertical) {
        _displayFPSVertical = displayFPSVertical;
    }

    public void setGraphicsQuality(GraphicsQuality graphicsQuality) {
        this._graphicsQuality = graphicsQuality;
    }

    public void addAnimated(Animated animated) {
        _containerAnimated.addAnimated(animated);
    }

    public void removeAnimated(Animated animated) {
        _containerAnimated.removeAnimated(animated);
    }

    public synchronized void startAnimator() {
        if (!_running) {
            doStartAnimator();
        }
    }

    private void doStartAnimator() {
        _animationStartTime = System.currentTimeMillis();
        _running = true;
    }

    public synchronized void stopAnimator() {
        if (_running) {
            doStopAnimator();
        }
    }

    private void doStopAnimator() {
        _lastStoppedAtFrame = _lastStoppedAtFrame + (System.currentTimeMillis() - _animationStartTime);
        _running = false;
    }

    public synchronized boolean isRunning() {
        return this._running;
    }

    private void processAnimatorMouseEvent(MouseEvent evt, AnimatorComponentCallback animatorComponentCallback) {
        int eventId = evt.getID();
        int x = evt.getX();
        int y = evt.getY();
        AnimatedMouseEvent animatedMouseEvent = new AnimatedMouseEvent(this, animatorComponentCallback, evt.getButton(), evt.getClickCount(), evt.getModifiers(), evt.getModifiersEx(), x, y, evt.isPopupTrigger());
        switch(eventId) {
            case MouseEvent.MOUSE_CLICKED:
                animatedMouseListenerStack.processMouseClicked(animatedMouseEvent);
                break;
            case MouseEvent.MOUSE_DRAGGED:
                animatedMouseListenerStack.processMouseDragged(animatedMouseEvent);
                break;
            case MouseEvent.MOUSE_ENTERED:
                animatedMouseListenerStack.processMouseEntered(animatedMouseEvent);
                break;
            case MouseEvent.MOUSE_EXITED:
                animatedMouseListenerStack.processMouseExited(animatedMouseEvent);
                break;
            case MouseEvent.MOUSE_MOVED:
                animatedMouseListenerStack.processMouseMoved(animatedMouseEvent);
                break;
            case MouseEvent.MOUSE_PRESSED:
                animatedMouseListenerStack.processMousePressed(animatedMouseEvent);
                _mouseDownInComponent = true;
                break;
            case MouseEvent.MOUSE_RELEASED:
                animatedMouseListenerStack.processMouseReleased(animatedMouseEvent);
                _mouseDownInComponent = false;
                break;
        }
    }

    public void paintComponent(Graphics gr) {
        AnimatorComponentCallbackImpl callback = new AnimatorComponentCallbackImpl();
        for (MouseEvent evt : _mouseEventBuffer) {
            this.processAnimatorMouseEvent(evt, callback);
        }
        Cursor cursorToSet = callback.getCursor();
        if (cursorToSet == null) {
            cursorToSet = Cursor.getDefaultCursor();
        }
        this.setCursor(cursorToSet);
        _mouseEventBuffer.clear();
        this.animatedMouseListenerStack.removeAllListeners();
        Graphics2D g = (Graphics2D) gr;
        _graphicsQuality.applyQuality(g);
        super.paintComponent(g);
        long timeFromStart = getAnimationFrame();
        SimpleGraphicsInfo displayInfo = new SimpleGraphicsInfo();
        _containerAnimated.paintAnimated(g, displayInfo, timeFromStart, _animatedCallbackImpl);
        if (this._displayFPS) {
            long paintingSecond = System.currentTimeMillis() / 1000;
            if (paintingSecond != _second) {
                _fps = _framesCounter;
                _second = paintingSecond;
                _framesCounter = 0;
            }
            _framesCounter++;
            DrawUtilities.drawLabel(g, new Rectangle(getSize()), this._fps + " fps", new Font("Arial", Font.BOLD, 30), _displayFPSColor, _displayFPSHorizontal, _displayFPSVertical);
        }
        if (animatedMouseListenerStack.isMouseInside()) {
            Point2D mousePosition = animatedMouseListenerStack.getMousePosition();
            if (_mouseDownInComponent) {
                _mouseEventBuffer.add(new MouseEvent(this, MouseEvent.MOUSE_DRAGGED, System.currentTimeMillis(), 0, (int) mousePosition.getX(), (int) mousePosition.getY(), 0, false));
            } else {
                _mouseEventBuffer.add(new MouseEvent(this, MouseEvent.MOUSE_MOVED, System.currentTimeMillis(), 0, (int) mousePosition.getX(), (int) mousePosition.getY(), 0, false));
            }
        }
    }

    public long getAnimationFrame() {
        if (_running) {
            return _lastStoppedAtFrame + (System.currentTimeMillis() - _animationStartTime);
        } else {
            return _lastStoppedAtFrame;
        }
    }

    private class AnimatorMouseListener implements MouseListener, MouseMotionListener {

        @Override
        public void mouseClicked(MouseEvent evt) {
            _mouseEventBuffer.add(evt);
        }

        @Override
        public void mousePressed(MouseEvent evt) {
            _mouseEventBuffer.add(evt);
        }

        @Override
        public void mouseReleased(MouseEvent evt) {
            _mouseEventBuffer.add(evt);
        }

        @Override
        public void mouseEntered(MouseEvent evt) {
            _mouseEventBuffer.add(evt);
        }

        @Override
        public void mouseExited(MouseEvent evt) {
            _mouseEventBuffer.add(evt);
        }

        @Override
        public void mouseDragged(MouseEvent evt) {
            _mouseEventBuffer.add(evt);
        }

        @Override
        public void mouseMoved(MouseEvent evt) {
            _mouseEventBuffer.add(evt);
        }
    }

    private class AnimatedCallbackImpl implements Animated.AnimatedCallback {

        @Override
        public void registerAnimatedMouseListener(AnimatedMouseListener listener) {
            animatedMouseListenerStack.addAnimatedMouseListenerToTop(listener);
        }
    }

    private class AnimatorAncestorListener implements AncestorListener {

        @Override
        public void ancestorAdded(AncestorEvent evt) {
            _animator.registerComponent(AnimatorComponent.this);
        }

        @Override
        public void ancestorRemoved(AncestorEvent evt) {
            _animator.unregisterComponent(AnimatorComponent.this);
        }

        @Override
        public void ancestorMoved(AncestorEvent evt) {
        }
    }

    private class SimpleGraphicsInfo implements DisplayInfo {

        @Override
        public Dimension getDisplaySize() {
            return getSize();
        }

        @Override
        public void resizeTo(Dimension dimension) {
            if (!getPreferredSize().equals(dimension)) {
                setPreferredSize(dimension);
                revalidate();
            }
        }
    }

    private class AnimatorComponentCallbackImpl implements AnimatorComponentCallback {

        private Cursor _cursor;

        @Override
        public void setCursor(Cursor cursor) {
            _cursor = cursor;
        }

        public Cursor getCursor() {
            return _cursor;
        }
    }
}
