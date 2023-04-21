package org.diamondspin;

import java.util.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.swing.*;
import com.merl.diamondtouch.*;

public class DSPanel extends JPanel implements Cloneable, MouseListener, MouseMotionListener, DSElement {

    protected BufferedImage PanelImage = null;

    protected Graphics2D PanelGraphics = null;

    protected Point Center = new Point(0, 0);

    public static final int CORNER_SIZE = DiamondTouch.getFingerSize();

    /**
   * State of the window.
  **/
    public static final int WAITING = 0;

    public static final int DRAGGING = 1;

    public static final int ZOOMING_BR = 2;

    public static final int ZOOMING_TL = 3;

    public static final int ROTATING = 4;

    protected int Mode = WAITING;

    public int getMode() {
        return Mode;
    }

    public void setMode(int Mode_Arg) {
        Mode = Mode_Arg;
    }

    /**
   * everything is diplayed in a DSContainer. We work in a centralized architecture
  **/
    protected DSContainer DSContainer1 = null;

    public DSContainer getContainer() {
        return DSContainer1;
    }

    /**
   * The last component concerned by a mouse event. usefull for mouseEnter and mouseExit
  **/
    protected Component LastComponent = null;

    /**
   * The clicked component because it is this one which should receive the mouserelease!
  **/
    protected Component ClickedComponent = null;

    /**
   * Distance to the center of the circular layout 0.0-1.0
  **/
    protected float D = 0.5f;

    /**
   * @return the distance to the center of the circular layout
  **/
    public float getD() {
        return D;
    }

    /**
   * change the distance to the center of the circular layout
  **/
    public void setD(float D_Arg) {
        D = D_Arg;
    }

    /**
   * Angle from the right horitontal direction. Like the trigonometric circle ! 0-2PI
  **/
    protected float Alpha = 0f;

    /**
   * @return the angle from the right horitontal direction. Like the trigonometric circle ! 0-2PI
  **/
    public float getAlpha() {
        return Alpha;
    }

    /**
   * change the angle from the right horitontal direction. Like the trigonometric circle ! 0-2PI
  **/
    public void setAlpha(float Alpha_Arg) {
        Alpha = Alpha_Arg;
    }

    protected boolean HasActivePopup = false;

    public boolean getHasActivePopup() {
        return HasActivePopup;
    }

    public void setHasActivePopup(boolean arg) {
        HasActivePopup = arg;
    }

    protected int ShareMode = 0;

    public int getShareMode() {
        return ShareMode;
    }

    public void setShareMode(int arg) {
        ShareMode = arg;
    }

    protected int IDOwner = -1;

    public void setIDOwner(int ID_Arg) {
        IDOwner = ID_Arg;
    }

    public int getIDOwner() {
        return IDOwner;
    }

    /**
   * Angle around its center ! 0-2PI
  **/
    protected float Beta = 0f;

    /**
   * @return the angle around its center
  **/
    public float getBeta() {
        return Beta;
    }

    /**
   * change the angle around its center
  **/
    public void setBeta(float Beta_Arg) {
        Beta = Beta_Arg;
    }

    /**
   * Scale factor to resize the element at low level (pixel by pixel). 1.0 most of the time.
  **/
    protected float Scale = 1f;

    /**
   * @return the scale factor to resize the element at low level (pixel by pixel).
  **/
    public float getScale() {
        return Scale;
    }

    /**
   * change the scale factor to resize the element at low level (pixel by pixel).
  **/
    public void setScale(float Scale_Arg) {
        Scale = Scale_Arg;
    }

    protected boolean Corners = false;

    /**
   * say if the component is resizable & rotable
  **/
    public void setCorners(boolean Corner_Arg) {
        Corners = Corner_Arg;
    }

    /**
   * @return if this element is rotable
  **/
    public boolean isCorners() {
        return Corners;
    }

    /**
   * Even if a document has its own scale, it can be affected by a global scale effect (blackhole, etc.)
   * Like Alpha which is also dependant of the global angle to know where the element is on the screen
   * Min - 1.0
  **/
    protected float ScaleCorrection = 1.0f;

    /**
   * Change the location of this Panel
  **/
    public void setLocation(float D_Arg, float Alpha_Arg, float Scale_Arg) {
        D = D_Arg;
        Alpha = Alpha_Arg;
        Scale = Scale_Arg;
    }

    /**
   * if the component is active or not
  **/
    private boolean Active = false;

    /**
   * @return if the component is active or not (with shadow =  is selected)
  **/
    public boolean isActive() {
        return Active;
    }

    /**
   * change if the component is active or not (with shadow =  is selected)
  **/
    public void setActive(boolean Active_Arg) {
        Active = Active_Arg;
    }

    /**
   * Where the mouse cursor grabbed the window (0,0 only if the user clicks in the first
   * pixel of the component !)
  **/
    protected Point2D.Float DTopLeftCorner = null;

    public boolean isShowing() {
        return true;
    }

    /**
   * Simulate the element has been dragged. uses the coordinates given as grab point
   * in the local coordinate system
  **/
    public boolean grabElement(float DX_Arg, float DY_Arg) {
        Mode = DRAGGING;
        DTopLeftCorner = new Point2D.Float(DX_Arg, DY_Arg);
        return true;
    }

    /**
   * I'm a DSElement so I can dispatch DSElement events
  **/
    private Vector DSElementListenerList = new Vector();

    public void addDSElementListener(DSElementListener DSElementListener_Arg) {
        if (!DSElementListenerList.contains(DSElementListener_Arg)) DSElementListenerList.add(DSElementListener_Arg);
    }

    public void removeDSElementListener(DSElementListener DSElementListener_Arg) {
        DSElementListenerList.remove(DSElementListener_Arg);
    }

    public void removeAllDSElementListener() {
        DSElementListenerList.removeAllElements();
    }

    /**
   * Fire the events to the suscribers
  **/
    public void fireDSElementMovedEvent(float Distance_Arg, float Angle_Arg) {
        for (int i = DSElementListenerList.size() - 1; i >= 0; i--) {
            DSElementListener DSElementListener1 = (DSElementListener) DSElementListenerList.elementAt(i);
            DSElementListener1.elementMoved(this, Distance_Arg, Angle_Arg);
        }
    }

    /**
   * Fire the events to the suscribers
  **/
    public void fireDSElementResizedEvent(float Scale_Arg) {
        for (int i = DSElementListenerList.size() - 1; i >= 0; i--) {
            DSElementListener DSElementListener1 = (DSElementListener) DSElementListenerList.elementAt(i);
            DSElementListener1.elementResized(this, Scale_Arg);
        }
    }

    /**
   * Fire the events to the suscribers
  **/
    public void fireDSElementDroppedEvent() {
        Vector DSElementListenerListCopy = (Vector) DSElementListenerList.clone();
        for (int i = DSElementListenerListCopy.size() - 1; i >= 0; i--) {
            DSElementListener DSElementListener1 = (DSElementListener) DSElementListenerList.elementAt(i);
            if (DSElementListenerList.contains(DSElementListener1)) DSElementListener1.elementDropped(this);
        }
    }

    /**
   * Fire the events to the suscribers
  **/
    public void fireDSElementRotatedEvent(float Beta_Arg) {
        for (int i = DSElementListenerList.size() - 1; i >= 0; i--) {
            DSElementListener DSElementListener1 = (DSElementListener) DSElementListenerList.elementAt(i);
            DSElementListener1.elementRotated(this, Beta_Arg);
        }
    }

    /**
   * Constructor without title.
   * @param DSContainer_Arg the container in which the Panel exists
  **/
    public DSPanel(DSContainer DSContainer_Arg) {
        super();
        DSContainer1 = DSContainer_Arg;
        Graphics1 = DSContainer_Arg.getGraphics();
        DSContainer_Arg.activateFakeComponent(this);
        setDoubleBuffered(false);
        setFocusable(false);
        setBorder(null);
        setBackground(Color.WHITE);
        setForeground(Color.BLACK);
        setVisible(true);
        setLayout(new FlowLayout());
    }

    /**
   * Overload this annoying function which is not relevant anymore
  **/
    public Container getParent() {
        return DSContainer1;
    }

    /**
   * Overload this annoying function which is not relevant anymore
  **/
    public Point getLocationOnScreen() {
        return new Point(0, 0);
    }

    Graphics Graphics1 = null;

    /**
   * Overload this annoying function which is not relevant anymore
  **/
    public Graphics getGraphics() {
        Graphics1.clipRect(1, 1, 1, 1);
        return Graphics1;
    }

    /**
   * Overload this annoying function which is not relevant anymore
  **/
    public Rectangle getBounds() {
        return new Rectangle(0, 0, getWidth(), getHeight());
    }

    /**
   * Overload this annoying function which is not relevant anymore
  **/
    public Image createImage(int Width_Arg, int Height_Arg) {
        return new BufferedImage(Width_Arg, Height_Arg, BufferedImage.TYPE_INT_RGB);
    }

    /**
   * Overload this annoying function which is not relevant anymore
  **/
    public void validate() {
        super.validate();
    }

    /**
   * Overload this annoying function which is not relevant anymore
  **/
    public void invalidate() {
        super.invalidate();
    }

    /**
   * Overload this annoying function which is not relevant anymore
  **/
    public void doLayout() {
        super.doLayout();
    }

    /**
   * Overload this annoying function which is not relevant anymore
  **/
    public void addNotify() {
        super.addNotify();
    }

    /**
   * Overload this annoying function which is not relevant anymore
  **/
    public void update(Graphics Graphics_Arg) {
        super.update(Graphics_Arg);
    }

    /**
   * change the size of the DSPanel
   * @param Dimension_Arg the dimension of the new size
  **/
    public void setSize(Dimension Dimension_Arg) {
        super.setSize(Dimension_Arg);
    }

    /**
   * change the size of the DSPanel
   * @param Width_Arg the width of the dimension of the panel
   * @param Height_Arg the height of the dimension of the panel
  **/
    public void setSize(int Width_Arg, int Height_Arg) {
        super.setSize(Width_Arg, Height_Arg);
    }

    /**
   * Init the swing component inside this window
  **/
    public void initPanelComponents() throws Exception {
    }

    /**
   * private because recursive
  **/
    private void repair(Component Component_Arg, String Indent_Arg) {
        if (Component_Arg instanceof JComboBox && !(Component_Arg instanceof DSJComboBox)) {
            DSJComboBox DSJComboBox1 = new DSJComboBox(DSContainer1, (JComboBox) Component_Arg);
            DSJComboBox1.setModel(((JComboBox) Component_Arg).getModel());
            Container Container1 = Component_Arg.getParent();
            Component[] ComponentList = Container1.getComponents();
            for (int i = 0; i < ComponentList.length; i++) {
                Component Component2 = ComponentList[i];
                if (Component2 == Component_Arg) {
                    Container1.add(DSJComboBox1, i);
                    Container1.remove(Component_Arg);
                }
            }
            return;
        }
        if ((Component_Arg instanceof JComponent)) {
            ((JComponent) Component_Arg).setAutoscrolls(false);
            ((JComponent) Component_Arg).setToolTipText(null);
        }
        if (Component_Arg instanceof Container) {
            Component[] ComponentList = ((Container) Component_Arg).getComponents();
            for (int i = 0; i < ComponentList.length; i++) {
                Component Component2 = ComponentList[i];
                repair(Component2, Indent_Arg + "| ");
            }
        }
    }

    /**
   * Call the other paint(). The one by default. There are no 2 qualities of repaint for this element.
  **/
    public void paint(Graphics2D Graphics_Arg, int Quality_Arg) {
        paint(Graphics_Arg);
    }

    /**
   * Paint the Panel ... and hopefully every swing component which has been added in it.
   * @param Graphics_Arg a graphic context on the general component
  **/
    public void paint(Graphics2D Graphics_Arg) {
        if (!isVisible() || getWidth() <= 0 || getHeight() <= 0) return;
        if (PanelImage == null || PanelImage.getWidth(null) != getWidth() || PanelImage.getHeight(null) != getHeight()) {
            PanelImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
            PanelGraphics = (Graphics2D) PanelImage.getGraphics();
        }
        AffineTransform AffineTransform2 = Graphics_Arg.getTransform();
        AffineTransform AffineTransform1 = DSContainer1.transformTable(D, Alpha, Beta, Scale, DSContainer1.getActiveView().getAngle(), true);
        Graphics_Arg.setTransform(AffineTransform1);
        RenderingHints RenderingHints1 = Graphics_Arg.getRenderingHints();
        RenderingHints QualityHints = new RenderingHints(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        Graphics_Arg.setRenderingHints(QualityHints);
        super.paint(PanelGraphics);
        Graphics_Arg.drawImage(PanelImage, -getWidth() / 2, -getHeight() / 2, null);
        QualityHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Graphics_Arg.setRenderingHints(QualityHints);
        if (isActive() && Mode != DRAGGING) paintShadow(Graphics_Arg);
        Graphics_Arg.setColor(Color.black);
        if (DSContainer1.isDragObject(this)) {
            int ID = DSContainer1.getIDForDragObject(this);
            Graphics_Arg.setColor(DSContainer1.getUserColor(ID));
            Graphics_Arg.drawRect(-getWidth() / 2 - 1, -getHeight() / 2 - 1, getWidth() + 1, getHeight() + 1);
        }
        if (Corners && isActive() && Mode != DRAGGING) paintCorners(Graphics_Arg);
        if (Selected) drawHilite(Graphics_Arg);
        Graphics_Arg.setTransform(AffineTransform2);
        Graphics_Arg.setRenderingHints(RenderingHints1);
    }

    /**
   * @return an image representation of this component
  **/
    public Image getImage() {
        BufferedImage BufferedImage1 = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D Graphics1 = (Graphics2D) BufferedImage1.getGraphics();
        super.paint(Graphics1);
        return BufferedImage1;
    }

    /**
   * Paint the shadow around the Panel (when it is selected). This method has been re-rited with gradient paint instead of
   * a set of transparent lines.
   * @param Graphics_Arg a graphic context on the general component
  **/
    protected void paintShadow(Graphics2D Graphics_Arg) {
        try {
            int ID = DSContainer1.getIDForDragObject(this);
            Color Color3 = DSContainer1.getUserColor(ID);
            Paint Paint1 = Graphics_Arg.getPaint();
            Color Color1 = new Color(Color3.getRed(), Color3.getGreen(), Color3.getBlue(), 196);
            Color Color2 = new Color(Color3.getRed(), Color3.getGreen(), Color3.getBlue(), 0);
            GradientPaint GradientPaint1 = new GradientPaint(-getWidth() / 2, getHeight() / 2, Color1, -getWidth() / 2, getHeight() / 2 + DSContainer.SHADOW_SIZE, Color2, false);
            Graphics_Arg.setPaint(GradientPaint1);
            Graphics_Arg.fillRect(-getWidth() / 2 + DSContainer.SHADOW_SIZE, getHeight() - getHeight() / 2, getWidth() - DSContainer.SHADOW_SIZE, DSContainer.SHADOW_SIZE);
            GradientPaint1 = new GradientPaint(getWidth() / 2, 0, Color1, getWidth() / 2 + DSContainer.SHADOW_SIZE, 0, Color2, false);
            Graphics_Arg.setPaint(GradientPaint1);
            Graphics_Arg.fillRect(getWidth() - getWidth() / 2, -getHeight() / 2 + DSContainer.SHADOW_SIZE, DSContainer.SHADOW_SIZE, getHeight() - DSContainer.SHADOW_SIZE);
            GradientPaint1 = new GradientPaint(-getWidth() / 2 + DSContainer.SHADOW_SIZE, getHeight() - getHeight() / 2, Color1, -getWidth() / 2 + DSContainer.SHADOW_SIZE / 2, getHeight() - getHeight() / 2 + DSContainer.SHADOW_SIZE / 2, Color2, false);
            Graphics_Arg.setPaint(GradientPaint1);
            Graphics_Arg.fillRect(-getWidth() / 2, getHeight() - getHeight() / 2, DSContainer.SHADOW_SIZE, DSContainer.SHADOW_SIZE);
            GradientPaint1 = new GradientPaint(getWidth() - getWidth() / 2, -getHeight() / 2 + DSContainer.SHADOW_SIZE, Color1, getWidth() - getWidth() / 2 + DSContainer.SHADOW_SIZE / 2, -getHeight() / 2 + DSContainer.SHADOW_SIZE / 2, Color2, false);
            Graphics_Arg.setPaint(GradientPaint1);
            Graphics_Arg.fillRect(getWidth() - getWidth() / 2, -getHeight() / 2, DSContainer.SHADOW_SIZE, DSContainer.SHADOW_SIZE);
            GradientPaint1 = new GradientPaint(getWidth() - getWidth() / 2, getHeight() - getHeight() / 2, Color1, getWidth() - getWidth() / 2 + DSContainer.SHADOW_SIZE / 2, getHeight() - getHeight() / 2 + DSContainer.SHADOW_SIZE / 2, Color2, false);
            Graphics_Arg.setPaint(GradientPaint1);
            Graphics_Arg.fillRect(getWidth() - getWidth() / 2, getHeight() - getHeight() / 2, DSContainer.SHADOW_SIZE, DSContainer.SHADOW_SIZE);
            Graphics_Arg.setPaint(Paint1);
        } catch (Exception Exception1) {
            Exception1.printStackTrace();
        }
    }

    /**
   * take a point in the general coordinate system and transform it in a local coordinate system
   * @param Point_Arg the point to transform
   * @return the point transformed. (0,0) = the center of the Panel
  **/
    private Point inversePoint(Point Point_Arg) {
        Point TranformedPoint = new Point(0, 0);
        AffineTransform AffineTransform1 = DSContainer1.transformTable(D, Alpha, Beta, Scale, DSContainer1.getTableAngle(), true);
        try {
            AffineTransform1.inverseTransform(Point_Arg, TranformedPoint);
        } catch (NoninvertibleTransformException NITE1) {
            NITE1.printStackTrace();
            return null;
        }
        return TranformedPoint;
    }

    /**
   * Construct a new mouse event with new source, transformed location, and Event type
   * @param MouseEvent_Arg an initial MouseEvent to use as model to create a new one
   * @param Component_Arg the new source
   * @param Point_Arg the new point
   * @param ID_Arg the new type of event
   * @return the new MouseEvent
  **/
    private MouseEvent convertMouseEvent(MouseEvent MouseEvent_Arg, Component Component_Arg, Point Point_Arg, int ID_Arg) {
        MouseEvent MouseEvent1 = new MouseEvent(Component_Arg, ID_Arg, MouseEvent_Arg.getWhen(), MouseEvent_Arg.getModifiers(), (int) Point_Arg.getX(), (int) Point_Arg.getY(), MouseEvent_Arg.getClickCount(), MouseEvent_Arg.isPopupTrigger(), MouseEvent_Arg.getButton());
        return MouseEvent1;
    }

    /**
   * Construct a new mouse event with new source and transformed location
   * @param MouseEvent_Arg an initial MouseEvent to use as model to create a new one
   * @param Component_Arg the new source
   * @param Point_Arg the new point
   * @return the new MouseEvent
  **/
    private MouseEvent convertMouseEvent(MouseEvent MouseEvent_Arg, Component Component_Arg, Point Point_Arg) {
        MouseEvent MouseEvent1 = new MouseEvent(Component_Arg, MouseEvent_Arg.getID(), MouseEvent_Arg.getWhen(), MouseEvent_Arg.getModifiers(), (int) Point_Arg.getX(), (int) Point_Arg.getY(), MouseEvent_Arg.getClickCount(), MouseEvent_Arg.isPopupTrigger(), MouseEvent_Arg.getButton());
        return MouseEvent1;
    }

    /**
   * Convert a MouseEvent and dispatch it
   * @param MouseEvent_Arg an initial MouseEvent dispatch in sub-components
  **/
    protected MouseEvent redispatchEvent(MouseEvent MouseEvent_Arg) {
        Point LocInWindow = inversePoint(new Point(MouseEvent_Arg.getX(), MouseEvent_Arg.getY()));
        LocInWindow.translate(getWidth() / 2, getHeight() / 2);
        Component Component1 = SwingUtilities.getDeepestComponentAt(this, (int) LocInWindow.getX(), (int) LocInWindow.getY());
        Point ComponentPoint = null;
        ComponentPoint = SwingUtilities.convertPoint(this, LocInWindow, Component1);
        if (MouseEvent_Arg.getID() == MouseEvent.MOUSE_PRESSED && (LocInWindow.getX() < 0 || LocInWindow.getX() > getWidth() || LocInWindow.getY() < 0 || LocInWindow.getY() > getHeight())) return null;
        MouseEvent_Arg.consume();
        if (ClickedComponent != null && MouseEvent_Arg.getID() == MouseEvent.MOUSE_RELEASED) Component1 = ClickedComponent;
        if (ClickedComponent != null && MouseEvent_Arg.getID() == MouseEvent.MOUSE_DRAGGED) Component1 = ClickedComponent;
        if (MouseEvent_Arg.getID() == MouseEvent.MOUSE_PRESSED) ClickedComponent = Component1;
        if (Component1 != LastComponent && LastComponent != null) {
            Point LastComponentPoint = SwingUtilities.convertPoint(this, LocInWindow, LastComponent);
            MouseEvent MouseEvent2 = convertMouseEvent(MouseEvent_Arg, LastComponent, LastComponentPoint, MouseEvent.MOUSE_EXITED);
            LastComponent.dispatchEvent(MouseEvent2);
        }
        if (Component1 != LastComponent && Component1 != null) {
            ComponentPoint = SwingUtilities.convertPoint(this, LocInWindow, Component1);
            MouseEvent MouseEvent2 = convertMouseEvent(MouseEvent_Arg, Component1, ComponentPoint, MouseEvent.MOUSE_ENTERED);
            Component1.dispatchEvent(MouseEvent2);
        }
        LastComponent = Component1;
        if (Component1 == null) return null;
        MouseEvent MouseEvent1 = convertMouseEvent(MouseEvent_Arg, Component1, ComponentPoint);
        ComponentPoint = SwingUtilities.convertPoint(this, LocInWindow, Component1);
        if (Component1 instanceof javax.swing.JScrollBar && MouseEvent_Arg.getID() == MouseEvent.MOUSE_DRAGGED) {
            MouseEvent1 = convertMouseEvent(MouseEvent_Arg, Component1, new Point(Math.abs(ComponentPoint.x), Math.abs(ComponentPoint.y)));
        }
        try {
            if (MouseEvent1.getID() == MouseEvent.MOUSE_DRAGGED && Component1 instanceof JList && Component1 == ClickedComponent) {
            }
            Component1.dispatchEvent(MouseEvent1);
            DSContainer1.repaint();
        } catch (IllegalComponentStateException Exception1) {
            Exception1.printStackTrace();
        }
        return MouseEvent1;
    }

    public boolean isMouseTarget(MouseEvent MouseEvent_Arg) {
        float TableAngle = DSContainer1.getTableAngle();
        Point2D MousePosition = new Point();
        AffineTransform AffineTransform1;
        AffineTransform1 = getContainer().transformTable(D, Alpha, Beta, Scale, TableAngle, true);
        try {
            AffineTransform1.inverseTransform(new Point(MouseEvent_Arg.getX(), MouseEvent_Arg.getY()), MousePosition);
        } catch (NoninvertibleTransformException NITE1) {
            NITE1.printStackTrace();
        }
        if ((int) MousePosition.getX() >= -getWidth() / 2 && (int) MousePosition.getY() >= -getHeight() / 2 && (int) MousePosition.getX() <= getWidth() / 2 && (int) MousePosition.getY() <= getHeight() / 2) {
            return true;
        } else {
            return false;
        }
    }

    /**
   * Handle a mouse button press event
  **/
    public void mousePressed(MouseEvent MouseEvent_Arg) {
        float TableAngle = DSContainer1.getTableAngle();
        Point2D MousePosition = new Point();
        AffineTransform AffineTransform1;
        AffineTransform1 = getContainer().transformTable(D, Alpha, Beta, Scale, TableAngle, true);
        try {
            AffineTransform1.inverseTransform(new Point(MouseEvent_Arg.getX(), MouseEvent_Arg.getY()), MousePosition);
        } catch (NoninvertibleTransformException NITE1) {
            NITE1.printStackTrace();
        }
        int CornerSizeUnscalled;
        CornerSizeUnscalled = (int) ((float) CORNER_SIZE / Scale);
        if ((int) MousePosition.getX() >= -getWidth() / 2 && (int) MousePosition.getY() >= -getHeight() / 2 && (int) MousePosition.getX() <= getWidth() / 2 && (int) MousePosition.getY() <= getHeight() / 2) {
            MouseEvent MouseEvent1 = redispatchEvent(MouseEvent_Arg);
            if (!isValid()) {
                validate();
                DSContainer1.repaint();
            }
            setMode(DRAGGING);
            getContainer().setActionMode(DSContainer.MOVE_ELEMENT, DSContainer.getCurrentUser());
            if (Mode == DRAGGING) {
                double MouseX = MouseEvent_Arg.getX() - (int) getContainer().getCenter().getX();
                double MouseY = MouseEvent_Arg.getY() - (int) getContainer().getCenter().getY();
                double DMouse = (float) Math.sqrt(MouseX * MouseX + MouseY * MouseY);
                double AlphaM = (float) Math.atan2(-MouseY, MouseX) - TableAngle;
                Alpha = (float) (Alpha % (Math.PI * 2));
                double Gamma = Math.atan2(-MouseY, MouseX);
                Gamma = Gamma - Alpha - TableAngle;
                Gamma = Gamma % (Math.PI * 2);
                DMouse = DMouse / getContainer().getRadius();
                float Dx = (float) (Math.sin(Gamma) * DMouse);
                double DBorder = Math.sqrt(DMouse * DMouse - Dx * Dx);
                float Dy = (float) (getD() - DBorder);
                DTopLeftCorner = new Point2D.Float(Dx, Dy);
                if (getContainer().getPercentFace() == 1.0) {
                    Point2D CenterPosition = new Point();
                    AffineTransform1.transform(new Point(0, 0), CenterPosition);
                    CenterPosition.setLocation(CenterPosition.getX() - getContainer().getCenter().getX(), CenterPosition.getY() - getContainer().getCenter().getY());
                    DTopLeftCorner = new Point2D.Float((float) (CenterPosition.getX() - MouseX), (float) (CenterPosition.getY() - MouseY));
                }
            }
            if (MouseEvent1 != null) {
                MouseEvent_Arg.consume();
                return;
            }
        } else if (Corners && isActive() && (int) MousePosition.getX() >= -getWidth() / 2 - CornerSizeUnscalled && (int) MousePosition.getY() >= getHeight() / 2 - CornerSizeUnscalled && (int) MousePosition.getX() <= -getWidth() / 2 + CornerSizeUnscalled && (int) MousePosition.getY() <= getHeight() / 2 + CornerSizeUnscalled) {
            setMode(ROTATING);
            MouseEvent_Arg.consume();
        } else if (Corners && isActive() && (int) MousePosition.getX() >= -getWidth() / 2 - CornerSizeUnscalled && (int) MousePosition.getY() >= -getHeight() / 2 - CornerSizeUnscalled && (int) MousePosition.getX() <= -getWidth() / 2 + CornerSizeUnscalled && (int) MousePosition.getY() <= -getHeight() / 2 + CornerSizeUnscalled) {
            float RealScale = getContainer().getScale(D, Alpha + DSContainer1.getTableAngle(), Scale);
            ScaleCorrection = (float) (Scale / RealScale);
            Scale = RealScale;
            setMode(ZOOMING_TL);
            MouseEvent_Arg.consume();
        } else if (Corners && isActive() && (int) MousePosition.getX() >= getWidth() / 2 - CornerSizeUnscalled && (int) MousePosition.getY() >= getHeight() / 2 - CornerSizeUnscalled && (int) MousePosition.getX() <= getWidth() / 2 + CornerSizeUnscalled && (int) MousePosition.getY() <= getHeight() / 2 + CornerSizeUnscalled) {
            float RealScale = getContainer().getScale(D, Alpha + DSContainer1.getTableAngle(), Scale);
            ScaleCorrection = (float) (Scale / RealScale);
            Scale = RealScale;
            setMode(ZOOMING_BR);
            MouseEvent_Arg.consume();
        }
    }

    /**
   * Handle a mouse button release event
  **/
    public void mouseReleased(MouseEvent MouseEvent_Arg) {
        if (Mode == DRAGGING) {
            Mode = WAITING;
            fireDSElementDroppedEvent();
            if (MouseEvent_Arg != null) MouseEvent_Arg.consume();
            return;
        }
        if (Mode == ZOOMING_BR || Mode == ZOOMING_TL) {
            Mode = WAITING;
            fireDSElementResizedEvent(Scale);
            MouseEvent_Arg.consume();
            return;
        }
        if (Mode == ROTATING) {
            Mode = WAITING;
            fireDSElementRotatedEvent(Beta);
            MouseEvent_Arg.consume();
            return;
        }
        MouseEvent MouseEvent1 = null;
        if (MouseEvent_Arg != null) MouseEvent1 = redispatchEvent(MouseEvent_Arg);
        if (!isValid()) {
            validate();
            DSContainer1.repaint();
        }
        if (MouseEvent1 != null && !MouseEvent1.isConsumed()) {
            MouseEvent_Arg.consume();
        }
    }

    /**
   * Handle a mouse cursor move event.
  **/
    public void mouseMoved(MouseEvent MouseEvent_Arg) {
        MouseEvent MouseEvent1 = redispatchEvent(MouseEvent_Arg);
        if (MouseEvent1 != null && !MouseEvent1.isConsumed()) MouseEvent_Arg.consume();
    }

    /**
   * Handle a mouse cursor drag event or more the frame if in DRAGGING MODE
  **/
    public void mouseDragged(MouseEvent MouseEvent_Arg) {
        float TableAngle = DSContainer1.getTableAngle();
        if (Mode == DRAGGING && getContainer().getPercentFace() == 1.0) {
            int MouseX = MouseEvent_Arg.getX() - (int) getContainer().getCenter().getX();
            int MouseY = MouseEvent_Arg.getY() - (int) getContainer().getCenter().getY();
            float NewPosX = (float) MouseX + (float) DTopLeftCorner.getX();
            float NewPosY = (float) MouseY + (float) DTopLeftCorner.getY();
            D = (float) Math.sqrt(NewPosX * NewPosX + NewPosY * NewPosY) / getContainer().getRadius();
            Alpha = -TableAngle + (float) Math.atan2(-NewPosY, NewPosX);
            if (D < 0) D = 0;
            if (MouseEvent_Arg != null) {
                MouseEvent_Arg.consume();
                fireDSElementMovedEvent(D, Alpha);
            }
            return;
        } else if (Mode == DRAGGING) {
            int MouseX = MouseEvent_Arg.getX() - (int) getContainer().getCenter().getX();
            int MouseY = MouseEvent_Arg.getY() - (int) getContainer().getCenter().getY();
            double DMouse = Math.sqrt(MouseX * MouseX + MouseY * MouseY);
            DMouse = DMouse / getContainer().getRadius();
            double Coeff = DTopLeftCorner.getX() / DMouse;
            if (Coeff < -1) Coeff = -1;
            if (Coeff > 1) Coeff = 1;
            float Beta = (float) Math.asin(Coeff);
            Alpha = -TableAngle + (float) Math.atan2(-MouseY, MouseX) - Beta;
            D = (float) (DTopLeftCorner.getY() + DMouse * Math.cos(Beta));
            if (D < 0) D = 0;
            if (MouseEvent_Arg != null) {
                MouseEvent_Arg.consume();
                fireDSElementMovedEvent(D, Alpha);
            }
            return;
        } else if (getMode() == ROTATING) {
            Point2D MousePosition = new Point();
            AffineTransform AffineTransform1;
            AffineTransform1 = getContainer().transformTable(D, Alpha, Beta, Scale, TableAngle, true);
            try {
                AffineTransform1.inverseTransform(new Point(MouseEvent_Arg.getX(), MouseEvent_Arg.getY()), MousePosition);
            } catch (NoninvertibleTransformException e) {
                e.printStackTrace();
            }
            float Angle = (float) Math.atan2(MousePosition.getX(), MousePosition.getY());
            float AngleFlat = (float) Math.atan2(-getWidth(), getHeight());
            setBeta(getBeta() + (AngleFlat - Angle));
            if (MouseEvent_Arg != null) {
                MouseEvent_Arg.consume();
                fireDSElementMovedEvent(D, Alpha);
            }
            return;
        } else if (getMode() == ZOOMING_BR) {
            int CornerSizeUnscalled = (int) ((float) CORNER_SIZE / Scale);
            float MinScale = ((float) CORNER_SIZE * 2) / getWidth();
            MinScale = Math.max(MinScale, ((float) CORNER_SIZE * 2) / getHeight());
            Point2D MousePosition = new Point();
            AffineTransform AffineTransform1;
            int DeformationMode = getContainer().getDeformationMode();
            getContainer().setDeformationMode(DSContainer.NORMAL);
            AffineTransform1 = getContainer().transformTable(D, Alpha, Beta, Scale, TableAngle, true);
            try {
                AffineTransform1.inverseTransform(new Point(MouseEvent_Arg.getX(), MouseEvent_Arg.getY()), MousePosition);
            } catch (NoninvertibleTransformException e) {
                e.printStackTrace();
            }
            if (MousePosition.getX() > 0) {
                float Ratio1 = (float) MousePosition.getX() * 2 / getWidth();
                float Ratio2 = (float) MousePosition.getY() * 2 / getHeight();
                float Ratio = Math.max(Ratio1, Ratio2);
                Scale = getContainer().getScale(D, Alpha + DSContainer1.getTableAngle(), Scale) * Ratio;
                Scale = Math.max(Scale, MinScale);
                fireDSElementResizedEvent(Scale);
                MouseEvent_Arg.consume();
            }
            getContainer().setDeformationMode(DeformationMode);
            return;
        } else if (getMode() == ZOOMING_TL) {
            int DeformationMode = getContainer().getDeformationMode();
            getContainer().setDeformationMode(DSContainer.NORMAL);
            Point2D MousePosition = new Point();
            double MouseX = MouseEvent_Arg.getX() - getContainer().getWidth() / 2;
            double MouseY = -1 * (MouseEvent_Arg.getY() - getContainer().getHeight() / 2);
            double DMouse = (float) Math.sqrt(MouseX * MouseX + MouseY * MouseY);
            double AlphaM = (float) Math.atan2(MouseY, MouseX) - DSContainer1.getTableAngle();
            AffineTransform AffineTransform1;
            AffineTransform1 = getContainer().transformTable(D, Alpha, Beta, Scale, TableAngle, true);
            try {
                AffineTransform1.inverseTransform(new Point(MouseEvent_Arg.getX(), MouseEvent_Arg.getY()), MousePosition);
            } catch (NoninvertibleTransformException e) {
                e.printStackTrace();
            }
            if (MousePosition.getX() < 0) {
                double Ratio = MousePosition.getX() * -2 / getWidth();
                Scale = (float) (Scale * Ratio);
                fireDSElementResizedEvent(Scale);
                MouseEvent_Arg.consume();
            }
            getContainer().setDeformationMode(DeformationMode);
            return;
        } else {
            MouseEvent MouseEvent1 = null;
            try {
                MouseEvent1 = redispatchEvent(MouseEvent_Arg);
            } catch (IllegalComponentStateException Exception1) {
                Exception1.printStackTrace();
            }
            if (MouseEvent1 != null && !MouseEvent1.isConsumed()) MouseEvent_Arg.consume();
        }
    }

    /**
   * Unused callbacks
  **/
    public void mouseClicked(MouseEvent MouseEvent_Arg) {
        MouseEvent MouseEvent1 = redispatchEvent(MouseEvent_Arg);
        if (MouseEvent1 != null && !MouseEvent1.isConsumed()) MouseEvent_Arg.consume();
    }

    public void mouseEntered(MouseEvent MouseEvent_Arg) {
        MouseEvent MouseEvent1 = redispatchEvent(MouseEvent_Arg);
        if (MouseEvent1 != null && !MouseEvent1.isConsumed()) MouseEvent_Arg.consume();
    }

    public void mouseExited(MouseEvent MouseEvent_Arg) {
        MouseEvent MouseEvent1 = redispatchEvent(MouseEvent_Arg);
        if (MouseEvent1 != null && !MouseEvent1.isConsumed()) MouseEvent_Arg.consume();
    }

    public void setText(String text_arg) {
    }

    public void appendText(String text_arg) {
    }

    public void insertText(String text_arg) {
    }

    public void backspace() {
    }

    public void navigate(int direction_arg) {
    }

    public boolean hasPermissions(int ID_Arg) {
        return false;
    }

    /**
     * to get a clone of the TFNImage
    **/
    public Object clone() {
        try {
            DSPanel mag = (DSPanel) super.clone();
            return mag;
        } catch (CloneNotSupportedException CloneNotSupportedException1) {
            CloneNotSupportedException1.printStackTrace();
            return null;
        }
    }

    public int getX() {
        double tempD = D * getContainer().getRadius();
        float TableAngle = getContainer().getActiveView().getAngle();
        double tempAlpha = Alpha + TableAngle;
        double tempX = tempD * Math.cos(tempAlpha);
        return (int) tempX;
    }

    public int getY() {
        double tempD = D * getContainer().getRadius();
        float TableAngle = getContainer().getActiveView().getAngle();
        double tempAlpha = Alpha + TableAngle;
        double tempY = tempD * Math.sin(tempAlpha);
        return (int) tempY;
    }

    /**
     * move the variable center of gravity of this element
     *
     * @param X_Arg The new coordinate of the center in this frame coordinate space
     * @param Y_Arg The new coordinate of the center in this frame coordinate space
    **/
    private void moveCenterTo(int X_Arg, int Y_Arg) {
        float TableAngle = getContainer().getActiveView().getAngle();
        double D2 = Math.sqrt(X_Arg * X_Arg + Y_Arg * Y_Arg);
        D2 = D2 / getContainer().getRadius();
        Alpha = -TableAngle + (float) Math.atan2(-Y_Arg, X_Arg);
        D = (float) D2;
        if (D < 0) D = 0;
    }

    public boolean doesContain(float x, float y) {
        return false;
    }

    public float getAbsScale() {
        return 1.0f;
    }

    public Point2D getXY() {
        return null;
    }

    protected int Height;

    protected int Width;

    public boolean splitVertical(Point touchPoint) {
        return false;
    }

    public boolean getsTopLeft(Point touchPoint) {
        return false;
    }

    public void moveUnderFinger(Point touchPoint) {
        float TableAngle = getContainer().getActiveView().getAngle();
        int MouseX = touchPoint.x - (int) getContainer().getCenter().getX();
        int MouseY = touchPoint.y - (int) getContainer().getCenter().getY();
        double D2 = Math.sqrt(MouseX * MouseX + MouseY * MouseY);
        D2 = D2 / getContainer().getRadius();
        Alpha = -TableAngle + (float) Math.atan2(-MouseY, MouseX);
        D = (float) D2;
        if (D < 0) D = 0;
    }

    public Point2D transformPointToGlobal(Point2D beforeTransform) {
        Point2D afterTransform = new Point();
        AffineTransform at = getContainer().transformTable(D, Alpha, Beta, Scale, getContainer().getTableAngle(), true);
        at.transform(beforeTransform, afterTransform);
        return (new Point2D.Float((float) afterTransform.getX(), (float) afterTransform.getY()));
    }

    public void setReadOnly(boolean arg) {
    }

    public void paintCorners(Graphics2D Graphics_Arg) {
        int CornerSizeInv = (int) ((float) CORNER_SIZE / Scale);
        int CornerSize = CornerSizeInv;
        int W = getWidth();
        int H = getHeight();
        if (getMode() == ZOOMING_TL) {
            Graphics_Arg.setColor(new Color(255, 0, 0, 128));
            CornerSize = 2 * CornerSizeInv;
        } else Graphics_Arg.setColor(Color.RED.darker());
        if (getMode() == ZOOMING_TL || getMode() == WAITING) {
            Graphics_Arg.fillRect(-W / 2 - CornerSize, -H / 2 - CornerSize, 2 * CornerSize, CornerSize);
            Graphics_Arg.fillRect(-W / 2 - CornerSize, -H / 2, CornerSize, CornerSize);
            Graphics_Arg.setColor(Color.BLACK);
            Graphics_Arg.drawLine(-W / 2, -H / 2 + CornerSize, -W / 2 - CornerSize, -H / 2 + CornerSize);
            Graphics_Arg.drawLine(-W / 2 - CornerSize, -H / 2 + CornerSize, -W / 2 - CornerSize, -H / 2 - CornerSize);
            Graphics_Arg.drawLine(-W / 2 - CornerSize, -H / 2 - CornerSize, -W / 2 + CornerSize, -H / 2 - CornerSize);
            Graphics_Arg.drawLine(-W / 2 + CornerSize, -H / 2 - CornerSize, -W / 2 + CornerSize, -H / 2);
            Graphics_Arg.drawLine(-W / 2 - 2, -H / 2 - 2, -W / 2 - CornerSize + 2, -H / 2 - CornerSize + 2);
            Graphics_Arg.drawLine(-W / 2 - CornerSize + 2, -H / 2 - CornerSize + 2, -W / 2 - CornerSize + 2, -H / 2 - CornerSize + 2 + CornerSize / 2);
            Graphics_Arg.drawLine(-W / 2 - CornerSize + 2, -H / 2 - CornerSize + 2, -W / 2 - CornerSize + 2 + CornerSize / 2, -H / 2 - CornerSize + 2);
        }
        CornerSize = CornerSizeInv;
        if (getMode() == ZOOMING_BR) {
            Graphics_Arg.setColor(new Color(255, 0, 0, 128));
            CornerSize = 2 * CornerSizeInv;
        } else Graphics_Arg.setColor(Color.RED.darker());
        if (getMode() == ZOOMING_BR || getMode() == WAITING) {
            Graphics_Arg.fillRect(W / 2, H / 2 - CornerSize, CornerSize, 2 * CornerSize);
            Graphics_Arg.fillRect(W / 2 - CornerSize, H / 2, CornerSize, CornerSize);
            Graphics_Arg.setColor(Color.BLACK);
            Graphics_Arg.drawLine(W / 2, H / 2 - CornerSize, W / 2 + CornerSize, H / 2 - CornerSize);
            Graphics_Arg.drawLine(W / 2 + CornerSize, H / 2 - CornerSize, W / 2 + CornerSize, H / 2 + CornerSize);
            Graphics_Arg.drawLine(W / 2 + CornerSize, H / 2 + CornerSize, W / 2 - CornerSize, H / 2 + CornerSize);
            Graphics_Arg.drawLine(W / 2 - CornerSize, H / 2 + CornerSize, W / 2 - CornerSize, H / 2);
            Graphics_Arg.drawLine(W / 2 + 2, H / 2 + 2, W / 2 + CornerSize - 2, H / 2 + CornerSize - 2);
            Graphics_Arg.drawLine(W / 2 + CornerSize - 2, H / 2 + CornerSize - 2, W / 2 + CornerSize - 2, H / 2 + CornerSize - 2 - CornerSize / 2);
            Graphics_Arg.drawLine(W / 2 + CornerSize - 2, H / 2 + CornerSize - 2, W / 2 + CornerSize - 2 - CornerSize / 2, H / 2 + CornerSize - 2);
        }
        CornerSize = CornerSizeInv;
        if (getMode() == ROTATING) {
            Graphics_Arg.setColor(new Color(255, 0, 255, 128));
            CornerSize = 2 * CornerSizeInv;
        } else Graphics_Arg.setColor(Color.MAGENTA.darker());
        if (getMode() == ROTATING || getMode() == WAITING) {
            Graphics_Arg.fillRect(-W / 2 - CornerSize, H / 2 - CornerSize, CornerSize, 2 * CornerSize);
            Graphics_Arg.fillRect(-W / 2, H / 2, CornerSize, CornerSize);
            Graphics_Arg.setColor(Color.BLACK);
            Graphics_Arg.drawArc(-W / 2 - CornerSize * 2 / 3, H / 2 - CornerSize / 3, CornerSize, CornerSize, 90, 270);
            Graphics_Arg.drawLine(-W / 2, H / 2 - CornerSize / 3, -W / 2 - CornerSize / 3, H / 2 - 2 * CornerSize / 3);
            Graphics_Arg.drawLine(-W / 2, H / 2 - CornerSize / 3, -W / 2 - CornerSize / 3, H / 2);
            Graphics_Arg.drawLine(-W / 2, H / 2 - CornerSize, -W / 2 - CornerSize, H / 2 - CornerSize);
            Graphics_Arg.drawLine(-W / 2 - CornerSize, H / 2 - CornerSize, -W / 2 - CornerSize, H / 2 + CornerSize);
            Graphics_Arg.drawLine(-W / 2 - CornerSize, H / 2 + CornerSize, -W / 2 + CornerSize, H / 2 + CornerSize);
            Graphics_Arg.drawLine(-W / 2 + CornerSize, H / 2 + CornerSize, -W / 2 + CornerSize, H / 2);
        }
    }

    public void drawHilite(Graphics2D Graphics_Arg) {
        Graphics_Arg.setColor(getContainer().getUserColor(IDOwner));
        Graphics_Arg.drawRect(-getWidth() / 2, -getHeight() / 2, getWidth(), getHeight());
    }

    public void setSelected(boolean arg) {
        Selected = arg;
    }

    public boolean isSelected() {
        return Selected;
    }

    protected boolean Selected = false;

    public boolean caresAboutKeyboards() {
        return false;
    }

    public void paintPileBorder(Graphics2D gfx_arg, Color color_arg, int borderW) {
    }

    public boolean isMouseInBorder(MouseEvent MouseEvent_Arg, int borderW) {
        int x = MouseEvent_Arg.getX();
        int y = MouseEvent_Arg.getY();
        float TableAngle = DSContainer1.getTableAngle();
        Point2D MousePosition = new Point();
        AffineTransform AffineTransform1;
        AffineTransform1 = getContainer().transformTable(D, Alpha, Beta, Scale, TableAngle, true);
        try {
            AffineTransform1.inverseTransform(new Point((int) x, (int) y), MousePosition);
        } catch (NoninvertibleTransformException NITE1) {
            NITE1.printStackTrace();
        }
        if (((int) MousePosition.getX() <= -getWidth() / 2 && (int) MousePosition.getY() >= -getHeight() / 2 && (int) MousePosition.getX() >= -getWidth() / 2 - borderW && (int) MousePosition.getY() <= getHeight() / 2) || ((int) MousePosition.getX() >= getWidth() / 2 && (int) MousePosition.getY() >= -getHeight() / 2 && (int) MousePosition.getX() <= getWidth() / 2 + borderW && (int) MousePosition.getY() <= getHeight() / 2) || ((int) MousePosition.getX() >= -getWidth() / 2 && (int) MousePosition.getY() <= -getHeight() / 2 && (int) MousePosition.getX() <= getWidth() / 2 && (int) MousePosition.getY() >= -getHeight() / 2 - borderW) || ((int) MousePosition.getX() >= -getWidth() / 2 && (int) MousePosition.getY() >= getHeight() / 2 && (int) MousePosition.getX() <= getWidth() / 2 && (int) MousePosition.getY() <= getHeight() / 2 + borderW)) return true; else return false;
    }
}
