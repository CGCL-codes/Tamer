package ch.ideenarchitekten.vip.gui.widget.resize;

import java.awt.Label;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import junit.framework.TestCase;
import ch.ideenarchitekten.vip.gui.ResizeBoard;
import ch.ideenarchitekten.vip.gui.widget.GraphicObject;
import ch.ideenarchitekten.vip.gui.widget.GraphicObjectFactory;
import ch.ideenarchitekten.vip.logic.event.GraphicObjectData;

/**
 * UnitTest f�r die Klasse TopRightHandle.
 * @author $LastChangedBy: martinschaub $
 * @version $LastChangedRevision: 340 $
 */
public class ZZTestTopRightHandle extends TestCase {

    private TopRightHandle m_underTest;

    private GraphicObject m_graphicObject;

    public void setUp() {
        m_graphicObject = GraphicObjectFactory.getInstance().newInstance(null, 1, GraphicObjectData.OBJECT_TYPE_RECTANGLE, null);
        m_graphicObject.setOriginAndCorner(new Point(100, 100), new Point(200, 200));
        m_underTest = new TopRightHandle(m_graphicObject);
        assertNotNull(m_underTest);
    }

    /**
	 * Test der Methode getBounds mit korrekten Testdaten.
	 */
    public void testGetBoundsNormal() {
        Rectangle bounds = m_graphicObject.getBounds();
        final Rectangle dest = new Rectangle(bounds.x + bounds.width, bounds.y - ResizeBoard.DEFAULTRECTSIZE, ResizeBoard.DEFAULTRECTSIZE, ResizeBoard.DEFAULTRECTSIZE);
        assertEquals(dest, m_underTest.getRectangle());
    }

    /**
	 * Der Grenzwert der Gr�sse und Position eines Objektes wird f�r 
	 * diesen Testfall verwendet.
	 */
    public void testGetBoundsZero() {
        final Point corner = new Point(0, 0);
        final Point origin = new Point(0, 0);
        m_graphicObject.setOriginAndCorner(origin, corner);
        final Rectangle dest = new Rectangle(origin.x, origin.y - ResizeBoard.DEFAULTRECTSIZE, ResizeBoard.DEFAULTRECTSIZE, ResizeBoard.DEFAULTRECTSIZE);
        assertEquals(dest, m_underTest.getRectangle());
    }

    /**
	 * Testet ob ein Objekt korrekt resized wird.  (Resize Methode)
	 */
    public void testResize() {
        final int dx = 10;
        final int dy = 20;
        final Rectangle bounds = m_graphicObject.getBounds();
        final Point dragPoint = new Point(bounds.x + bounds.width + dx, bounds.y + dy);
        Rectangle correctBounds = new Rectangle(bounds.x, bounds.y + dy, bounds.width + dx, bounds.height - dy);
        m_underTest.startResize();
        MouseEvent event = new MouseEvent(new Label(), 1, 1, 0, dragPoint.x, dragPoint.y, 1, true);
        m_underTest.dragResize(event);
        assertEquals(correctBounds, m_graphicObject.getBounds());
    }
}
