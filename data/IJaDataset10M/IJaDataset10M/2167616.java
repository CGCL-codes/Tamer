package ch.kwa.ee.view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.ItemSelectable;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Transparency;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.VolatileImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.JComponent;
import org.apache.log4j.Logger;
import ch.kwa.ee.model.transkription.TranskriptionModelElement;
import ch.unibas.germa.view.tei.TEIView;
import ch.unibas.germa.view.tei.visual.AbstractView;

@SuppressWarnings("serial")
public class SideRibbon extends JComponent implements ViewCompanion, MouseListener, MouseMotionListener, ItemSelectable, ComponentListener {

    public interface IInterested {

        boolean isInterested(AbstractView view);
    }

    private class InterestedInEverything implements IInterested {

        public boolean isInterested(AbstractView view) {
            return (view instanceof MarkerView);
        }
    }

    private IInterested interestDecider = new InterestedInEverything();

    public static final Point2D NOWHERE = new Point2D.Double(Double.MIN_VALUE, Double.MIN_VALUE);

    private static final Cursor HAND_CSR = new Cursor(Cursor.HAND_CURSOR);

    private static final Cursor STD_CSR = new Cursor(Cursor.DEFAULT_CURSOR);

    private static final Cursor INFO_CSR = new Cursor(Cursor.TEXT_CURSOR);

    private Logger logger = Logger.getLogger(SideRibbon.class);

    public static final int ALIGN_LEFT = 0;

    public static final int ALIGN_RIGHT = 1;

    private int vAlign = ALIGN_RIGHT;

    TEIView documentview;

    HashMap<TranskriptionModelElement, ReferencePosition> references = new HashMap<TranskriptionModelElement, ReferencePosition>();

    TranskriptionModelElement selectedElement = null;

    public SideRibbon() {
        super();
        this.setLayout(null);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
    }

    public IInterested getInterestDecider() {
        return interestDecider;
    }

    public void setInterestDecider(IInterested interestDecider) {
        this.interestDecider = interestDecider;
    }

    public int getVAlign() {
        return vAlign;
    }

    public void setVAlign(int valign) {
        this.vAlign = valign;
    }

    @Override
    public Dimension getMaximumSize() {
        return getPreferredSize();
    }

    @Override
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension((int) (80.0 * documentview.getScale()), (int) (documentview.getPreferredSize().getHeight()));
    }

    public void note(AbstractView view) {
        if (interestDecider.isInterested(view)) {
            final TranskriptionModelElement element = (TranskriptionModelElement) view.getModel();
            logger.debug("button added");
            ((MarkerView) view).getCompanions().add(this);
            ReferencePosition pos = new ReferencePosition();
            pos.setPosition(NOWHERE);
            pos.setElement(element);
            pos.setMarker((MarkerView) view);
            references.put(element, pos);
        }
    }

    public TEIView getDocumentview() {
        return documentview;
    }

    public void setDocumentview(TEIView documentview) {
        if (this.documentview != null) this.documentview.removeComponentListener(this);
        this.documentview = documentview;
        this.documentview.addComponentListener(this);
    }

    public void notePaint(AbstractView view, double x, double y, double width, double height) {
        TranskriptionModelElement elem = (TranskriptionModelElement) view.getModel();
        ReferencePosition pos = references.get(elem);
        pos.setPosition(new Point2D.Double(10, y));
        this.repaint();
    }

    private AffineTransform getTrans() {
        AffineTransform imgtrans = new AffineTransform();
        imgtrans.scale(documentview.getScale(), documentview.getScale());
        return imgtrans;
    }

    private VolatileImage createVolatileImage(int width, int height, int transparency) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsConfiguration gc = ge.getDefaultScreenDevice().getDefaultConfiguration();
        VolatileImage image = null;
        image = gc.createCompatibleVolatileImage(width, height, transparency);
        int valid = image.validate(gc);
        if (valid == VolatileImage.IMAGE_INCOMPATIBLE) {
            image = this.createVolatileImage(width, height, transparency);
            return image;
        }
        return image;
    }

    VolatileImage buffer;

    private static final double height = 20;

    @Override
    public void paint(Graphics g) {
        Rectangle clip = g.getClipBounds();
        if (buffer == null) {
            buffer = createVolatileImage((int) Math.ceil(clip.getWidth()), (int) Math.ceil(clip.getHeight()), Transparency.OPAQUE);
        }
        do {
            final int valid = buffer.validate(getGraphicsConfiguration());
            if (valid == VolatileImage.IMAGE_INCOMPATIBLE || (clip.getWidth() != buffer.getWidth() || clip.getHeight() != buffer.getHeight())) {
                buffer = createVolatileImage((int) Math.ceil(clip.getWidth()), (int) Math.ceil(clip.getHeight()), Transparency.OPAQUE);
            }
            Graphics2D graphics = buffer.createGraphics();
            try {
                graphics.translate(-clip.x, -clip.y);
                if (getFont() != null) graphics.setFont(getFont());
                graphics.clipRect(clip.x, clip.y, clip.width, clip.height);
                graphics.setBackground(getBackground());
                graphics.clearRect(clip.x, clip.y, clip.width, clip.height);
                final AffineTransform trans = getTrans();
                graphics.transform(trans);
                graphics.setColor(getBackground());
                graphics.fillRect(0, 0, this.getWidth(), this.getHeight());
                graphics.setColor(Color.BLUE);
                final Iterator<ReferencePosition> it = references.values().iterator();
                while (it.hasNext()) {
                    final ReferencePosition ref = it.next();
                    if (ref.getPosition().getY() > 0.1 && ref.getElement().getReference() != null && ref.getElement().getReference().getText() != null) {
                        final String txt = ref.getElement().getReference().getText();
                        final Rectangle2D sb = graphics.getFont().getStringBounds(txt, graphics.getFontRenderContext());
                        switch(vAlign) {
                            case ALIGN_LEFT:
                                {
                                    graphics.drawString(txt, 10.0f, (float) (ref.getPosition().getY() + sb.getHeight()));
                                    break;
                                }
                            default:
                                {
                                    graphics.drawString(txt, (float) (((double) getWidth() / documentview.getScale()) - (sb.getWidth() + 2)), (float) (ref.getPosition().getY() + sb.getHeight()));
                                }
                        }
                    }
                }
                g.drawImage(buffer, clip.x, clip.y, this);
            } finally {
                graphics.dispose();
            }
        } while (buffer.contentsLost());
    }

    public void mouseClicked(MouseEvent e) {
        ReferencePosition pos = findReferenceAt(e.getPoint());
        if (pos != null) {
            TranskriptionModelElement pointedAt = pos.getElement();
            if (pointedAt != null && pointedAt.getReference() != null && pointedAt.getReference().getPointer() != null) {
                selectedElement = pointedAt;
                fireSelectionTaken();
            }
        }
    }

    private ReferencePosition findReferenceAt(Point p) {
        Point2D point = new Point2D.Double(p.x, p.y);
        try {
            final AffineTransform imgtrans = getTrans();
            point = imgtrans.inverseTransform(point, null);
        } catch (NoninvertibleTransformException ex) {
            ex.printStackTrace();
        }
        final Iterator<ReferencePosition> it = references.values().iterator();
        while (it.hasNext()) {
            final ReferencePosition ref = it.next();
            if (point.getY() > ref.getPosition().getY() && point.getY() < ref.getPosition().getY() + (height * documentview.getScale())) {
                return ref;
            }
        }
        return null;
    }

    public void mouseEntered(MouseEvent arg0) {
    }

    public void mouseExited(MouseEvent arg0) {
        this.setCursor(STD_CSR);
        if (currentmark != null) {
            currentmark.visible = false;
            currentmark = null;
            documentview.repaint();
        }
    }

    public void mousePressed(MouseEvent arg0) {
    }

    public void mouseReleased(MouseEvent arg0) {
    }

    ArrayList<ItemListener> itemListeners = new ArrayList<ItemListener>();

    public void addItemListener(ItemListener listener) {
        itemListeners.add(listener);
    }

    public Object[] getSelectedObjects() {
        return new TranskriptionModelElement[] { selectedElement };
    }

    public TranskriptionModelElement getSelectedElement() {
        return selectedElement;
    }

    public void removeItemListener(ItemListener listener) {
        itemListeners.remove(listener);
    }

    @SuppressWarnings("unchecked")
    protected void fireSelectionTaken() {
        ItemEvent e = new ItemEvent(this, 0, selectedElement, ItemEvent.SELECTED);
        ArrayList listeners = (ArrayList<ItemListener>) itemListeners.clone();
        Iterator<ItemListener> it = listeners.iterator();
        while (it.hasNext()) {
            it.next().itemStateChanged(e);
        }
    }

    public void mouseDragged(MouseEvent arg0) {
    }

    private MarkerView currentmark = null;

    public void mouseMoved(MouseEvent e) {
        ReferencePosition pointedAt = findReferenceAt(e.getPoint());
        if (pointedAt != null) {
            if ((pointedAt.getElement().getReference() != null) && (pointedAt.getElement().getReference().getPointer() != null)) this.setCursor(HAND_CSR); else this.setCursor(INFO_CSR);
            if (currentmark != pointedAt.getMarker()) {
                currentmark = pointedAt.getMarker();
                currentmark.visible = true;
                documentview.repaint();
            }
        } else {
            this.setCursor(STD_CSR);
            if (currentmark != null) {
                currentmark.visible = false;
                currentmark = null;
                documentview.repaint();
            }
        }
    }

    public void componentHidden(ComponentEvent e) {
    }

    public void componentMoved(ComponentEvent e) {
    }

    public void componentResized(ComponentEvent e) {
        revalidate();
    }

    public void componentShown(ComponentEvent e) {
    }
}
