package ch.ideenarchitekten.vip.gui.widget;

import java.awt.*;
import ch.ideenarchitekten.vip.config.*;
import ch.ideenarchitekten.vip.gui.*;
import ch.ideenarchitekten.vip.logic.event.*;

/**
 * Diese Klasse rep�sentiert ein Pfeilobjekt.
 * @author $LastChangedBy: jogeli $
 * @version $LastChangedRevision: 400 $
 * Date: $LastChangedDate: 2007-01-01 11:07:30 +0100 (Mo, 01 Jan 2007) 
 */
public class ArrowObject extends AbstractLineObject {

    /**
	 * Aktuelle Version dieser Klasse.
	 */
    private static final long serialVersionUID = 20070501L;

    /**
	 * Wie viele Pixel von der L�nge des Pfeils f�r die Spitze verwende werden.
	 */
    public static final int LENGTH = 20;

    /**
	 * Die Abgrenzung des ArrowObject muss gr�sser sein als die Eckpunkte,
	 * da die Pfeilspitzen �ber diese Ecken herausragen k�nnen.
	 */
    public static final int BOUNDSADDING = (int) (LENGTH * 1.5);

    /**
	 * Faktor, der die Abgrenzung multipliziert und somit
	 * die H�he und Breite des Panels vergr�ssert.
	 */
    public static final int BOUNDSFACTOR = 3;

    /**
	 * Initialisert einen Pfeil.
	 * @param board Board auf dem sich dieses Objekt befindet.
	 * @param objectID Eindeutige ObjektID dieses Objektes.
	 */
    public ArrowObject(ObjectBoard board, int objectID) {
        super(board, objectID);
    }

    /**
	 * Zeichnet das gew�nschte Objekt ohne Text.
	 * @param g Auf dieses Graphics gemahlt werden soll.
	 */
    public void paintObject(Graphics g) {
        Point from = getOrigin();
        Point location = getLocation();
        Point to = getCorner();
        from.x -= location.x;
        to.x -= location.x;
        from.y -= location.y;
        to.y -= location.y;
        int thikness = Integer.parseInt(Config.getInstance().getConstant("LineWidth").substring(0, 1));
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setStroke(new BasicStroke(thikness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.setColor(getForeground());
        g2d.drawLine(from.x, from.y, to.x, to.y);
        Point mainLine = new Point(to.x - from.x, to.y - from.y);
        double lineLength = abs(mainLine);
        if (from.equals(to)) {
            g2d.drawLine(to.x, to.y, to.x - LENGTH, to.y);
            g2d.drawLine(to.x, to.y, to.x, to.y - LENGTH);
        } else {
            Point intersection = new Point();
            intersection.x = (int) (mainLine.x * (1 - LENGTH / lineLength));
            intersection.y = (int) (mainLine.y * (1 - LENGTH / lineLength));
            Point left = new Point(mainLine.y, -mainLine.x);
            Point right = new Point(-mainLine.y, mainLine.x);
            double smallLine = abs(left);
            to.x -= from.x;
            to.y -= from.y;
            lineLength = abs(to);
            to.x = (int) (to.x * ((lineLength + thikness) / lineLength));
            to.y = (int) (to.y * ((lineLength + thikness) / lineLength));
            to.x += from.x;
            to.y += from.y;
            left.x = (int) (left.x * (LENGTH / smallLine));
            left.y = (int) (left.y * (LENGTH / smallLine));
            g2d.drawLine(to.x, to.y, from.x + intersection.x + left.x, from.y + intersection.y + left.y);
            right.x = (int) (right.x * (LENGTH / smallLine));
            right.y = (int) (right.y * (LENGTH / smallLine));
            g2d.drawLine(to.x, to.y, from.x + intersection.x + right.x, from.y + intersection.y + right.y);
            g2d.setStroke(new BasicStroke(1));
            drawText(g);
        }
    }

    /**
	 * F�r den Pfeil muss ein gewisses Minimum an Platz existieren. 
	 * Wird die Gr�sse �ber setCorner ver�ndert, so muss �berpr�ft werden, dass
	 * dieses Minimum existiert.
	 * @param origin Punkt der zusammen mit corner ein Rechteck aufspannt.
	 * @param corner Punkt der zusammen mit origin ein Rechteck aufspannt.
	 */
    public void setOriginAndCorner(Point origin, Point corner) {
        super.setOriginAndCorner(origin, corner);
        Rectangle r = new Rectangle();
        r.x = Math.min(origin.x, corner.x) - BOUNDSADDING;
        r.y = Math.min(origin.y, corner.y) - BOUNDSADDING;
        r.width = Math.max(origin.x, corner.x) - r.x + BOUNDSADDING * 2;
        r.height = Math.max(origin.y, corner.y) - r.y + BOUNDSADDING * 2;
        setBounds(r);
    }

    /**
	 * Es wird ermittelt, wie die Bounds angepasst werden muss.
	 * @param r neue Bounds.
	 */
    public void setBoundsAndPoints(Rectangle r) {
        setBounds(r);
        r.x += BOUNDSADDING;
        r.y += BOUNDSADDING;
        r.width -= BOUNDSFACTOR * BOUNDSADDING;
        r.height -= BOUNDSFACTOR * BOUNDSADDING;
        if (r.width > 0 && r.height > 0) {
            Point origin = getOrigin();
            Point corner = getCorner();
            if (origin.x <= corner.x && origin.y <= corner.y) {
                origin.x = r.x;
                origin.y = r.y;
                corner.x = r.x + r.width;
                corner.y = r.y + r.height;
            } else if (origin.x < corner.x && origin.y > corner.y) {
                origin.x = r.x;
                origin.y = r.y + r.height;
                corner.x = r.x + r.width;
                corner.y = r.y;
            } else if (origin.x > corner.x && origin.y > corner.y) {
                origin.x = r.x + r.width;
                origin.y = r.y + r.height;
                corner.x = r.x;
                corner.y = r.y;
            } else if (origin.x > corner.x && origin.y < corner.y) {
                origin.x = r.x + r.width;
                origin.y = r.y;
                corner.x = r.x;
                corner.y = r.y + r.height;
            } else {
                assert false;
            }
            setOriginAndCorner(origin, corner);
        }
    }

    /**
	 * Gibt zur�ck, um was f�r einen Objekttypen es sich handelt. Dies muss f�r fast alle
	 * anwendungen transparent sein. Momentan ist nur bekannt, dass dies f�r das Kopieren verwendet wird.
	 * @return der Objekttyp.
	 */
    public int getDataType() {
        return GraphicObjectData.OBJECT_TYPE_ARROW;
    }

    /**
	 * Rechnet den Betrag eines Vektors(Punkt) aus.
	 * @param p1 Der Punkt, von welchem der Betrag gerechnet werden soll.
	 * @return der Betrag
	 */
    private double abs(Point p1) {
        return Math.sqrt(Math.pow(p1.x, 2) + Math.pow(p1.y, 2));
    }
}
