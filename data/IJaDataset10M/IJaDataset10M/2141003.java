package com.googlecode.jumpnevolve.graphics;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import com.googlecode.jumpnevolve.math.Circle;
import com.googlecode.jumpnevolve.math.NextShape;
import com.googlecode.jumpnevolve.math.PointLine;
import com.googlecode.jumpnevolve.math.Rectangle;
import com.googlecode.jumpnevolve.math.Shape;
import com.googlecode.jumpnevolve.math.Vector;

/**
 * Zeichnet Objekte in einen OpenGL Grafikkontext.
 * 
 * @author Niklas Fiekas
 */
public class GraphicUtils {

    /**
	 * Zeichnet einen Kreis.
	 * 
	 * @param g
	 *            Grafikkontext
	 * @param circle
	 *            Kreis
	 */
    public static void draw(Graphics g, Circle circle) {
        Rectangle rect = circle.getBoundingRect();
        g.drawOval(rect.x, rect.y, rect.width, rect.height);
    }

    /**
	 * Zeichnet ein Rechteck.
	 * 
	 * @param g
	 *            Grafikkontext
	 * @param rect
	 *            Rechteck
	 */
    public static void draw(Graphics g, Rectangle rect) {
        g.drawRect(rect.x, rect.y, rect.width, rect.height);
    }

    /**
	 * Zeichnet eine Figur.
	 * 
	 * @param g
	 *            Grafikkontext
	 * @param shape
	 *            Figur
	 */
    public static void draw(Graphics g, Shape shape) {
        if (shape instanceof Circle) {
            draw(g, (Circle) shape);
        } else if (shape instanceof Rectangle) {
            draw(g, (Rectangle) shape);
        }
    }

    /**
	 * Zeichnet eine Figur.
	 * 
	 * @param g
	 *            Grafikkontext
	 * @param shape
	 *            Figur
	 */
    public static void draw(Graphics g, NextShape shape) {
        g.draw(shape.toSlickShape());
    }

    /**
	 * Zeichnet eine Figur in einer bestimmten Farbe;
	 * 
	 * @param g
	 *            Der Grafikkontext
	 * @param shape
	 *            Die Figur
	 * @param color
	 *            Die Farbe
	 */
    public static void draw(Graphics g, NextShape shape, Color color) {
        Color c = g.getColor();
        g.setColor(color);
        draw(g, shape);
        g.setColor(c);
    }

    /**
	 * Zeichnet eine Figur in einer bestimmten Farbe;
	 * 
	 * @param g
	 *            Der Grafikkontext
	 * @param shape
	 *            Die Figur
	 * @param color
	 *            Die Farbe
	 */
    public static void draw(Graphics g, Shape shape, Color color) {
        Color c = g.getColor();
        g.setColor(color);
        draw(g, shape);
        g.setColor(c);
    }

    /**
	 * Zeichnet eine Punktlinie in einer bestimmten Farbe
	 * 
	 * @param g
	 *            Der Grafikkontext
	 * @param line
	 *            Die Punktline
	 * @param color
	 *            Die Farbe
	 */
    public static void draw(Graphics g, PointLine line, Color color) {
        Color c = g.getColor();
        g.setColor(color);
        g.drawLine(line.p1.x, line.p1.y, line.p2.x, line.p2.y);
        g.setColor(c);
    }

    public static void drawScaled(Graphics g, Shape shape, float zoom) {
        if (shape instanceof Circle) {
            drawScaled(g, (Circle) shape, zoom);
        } else if (shape instanceof Rectangle) {
            drawScaled(g, (Rectangle) shape, zoom);
        }
    }

    public static void drawScaled(Graphics g, Rectangle rect, float zoom) {
        Rectangle rect2 = new Rectangle(rect.getCenter(), new Vector(rect.width / 2.0f * zoom, rect.height / 2.0f * zoom));
        draw(g, rect2);
    }

    public static void drawScaled(Graphics g, Circle circle, float zoom) {
        draw(g, new Circle(circle.getCenter(), circle.radius * zoom));
    }

    public static void drawScaled(Graphics g, NextShape shape, float zoom) {
        NextShape scaled = shape.scale(zoom);
        draw(g, scaled);
    }

    /**
	 * Zeichnet einen Vektor ausgehend vom Ursprung.
	 * 
	 * @param g
	 *            Grafikkontext
	 * @param vector
	 *            Vektor
	 */
    public static void draw(Graphics g, Vector vector) {
        g.drawLine(0, 0, vector.x, vector.y);
    }

    /**
	 * Zeichnet einen Vektor ausgehend von einem Orsvektor.
	 * 
	 * @param g
	 *            Grafikkontext
	 * @param position
	 *            Ortsvektor
	 * @param vector
	 *            Vektor
	 */
    public static void draw(Graphics g, Vector position, Vector vector) {
        g.drawLine(position.x, position.y, position.x + vector.x, position.y + vector.y);
    }

    /**
	 * Zeichnet eine gefüllte Fläche anhand eines Shapes
	 * 
	 * @param g
	 *            Grafikkontext
	 * @param shape
	 *            Das zuzeichnende Shape
	 */
    public static void fill(Graphics g, Shape shape) {
        g.fill(shape.toSlickShape());
    }

    /**
	 * Zeichnet eine gefüllte Fläche anhand eines Shapes in einer bestimmten
	 * Farbe
	 * 
	 * @param g
	 *            Grafikkontext
	 * @param shape
	 *            Shape
	 * @param c
	 *            Die Farbe mit der das Shape gefüllt wird
	 */
    public static void fill(Graphics g, Shape shape, Color c) {
        Color cur = g.getColor();
        g.setColor(c);
        fill(g, shape);
        g.setColor(cur);
    }

    /**
	 * Zeichnet eine gefüllte Fläche anhand eines Shapes
	 * 
	 * @param g
	 *            Grafikkontext
	 * @param shape
	 *            Das zuzeichnende Shape
	 */
    public static void fill(Graphics g, NextShape shape) {
        g.fill(shape.toSlickShape());
    }

    /**
	 * Zeichnet eine gefüllte Fläche anhand eines Shapes in einer bestimmten
	 * Farbe
	 * 
	 * @param g
	 *            Grafikkontext
	 * @param shape
	 *            Shape
	 * @param c
	 *            Die Farbe mit der das Shape gefüllt wird
	 */
    public static void fill(Graphics g, NextShape shape, Color c) {
        Color cur = g.getColor();
        g.setColor(c);
        fill(g, shape);
        g.setColor(cur);
    }

    /**
	 * Zeichnet eine Textur
	 */
    public static void texture(Graphics g, Shape shape, Image image, boolean fit) {
        g.texture(shape.toSlickShape(), image, fit);
    }

    /**
	 * Zeichnet eine Textur
	 */
    public static void texture(Graphics g, NextShape shape, Image image, boolean fit) {
        g.texture(shape.toSlickShape(), image, fit);
    }

    /**
	 * Zeichnet einen String
	 * 
	 * @param position
	 *            Obere linke Ecke des Textes
	 */
    public static void drawString(Graphics g, Vector position, String text) {
        g.drawString(text, position.x, position.y);
    }

    /**
	 * Zeichnet ein Bild in ein Shape
	 * 
	 * ACHTUNG: Das Bild wird bei einem unpassenden Shape verzerrt (z.B.
	 * quadratisches Bild in einem länglichen Rechteck) bzw. über die Grenzen
	 * hinaus gezeichnet (z.B. rechteckiges Bild in einem Kreis)
	 * 
	 * @param shape
	 *            Das Shape
	 * @param image
	 *            Das Bild
	 */
    public static void drawImage(Graphics g, Shape shape, Image image) {
        g.drawImage(image, shape.getLeftEnd(), shape.getUpperEnd(), shape.getRightEnd(), shape.getLowerEnd(), 0, 0, image.getWidth(), image.getHeight());
    }

    /**
	 * Zeichnet ein Bild in ein Shape
	 * 
	 * ACHTUNG: Das Bild wird bei einem unpassenden Shape verzerrt (z.B.
	 * quadratisches Bild in einem länglichen Rechteck) bzw. über die Grenzen
	 * hinaus gezeichnet (z.B. rechteckiges Bild in einem Kreis)
	 * 
	 * @param shape
	 *            Das Shape
	 * @param image
	 *            Das Bild
	 */
    public static void drawImage(Graphics g, NextShape shape, Image image) {
        g.drawImage(image, shape.getLeftEnd(), shape.getUpperEnd(), shape.getRightEnd(), shape.getLowerEnd(), 0, 0, image.getWidth(), image.getHeight());
    }

    /**
	 * Markiert eine Position durch ein Kreuz
	 * 
	 * @param g
	 *            Der Grafikkontext
	 * @param position
	 *            Die Position, die markiert werden soll
	 * @param distance
	 *            Die Größe des Kreuzes, mit dem die Position markiert wird
	 *            (Länge des Kreuzes vom Zentrum in alle Richtungen)
	 */
    public static void markPosition(Graphics g, Vector position, float distance, Color c) {
        Color save = g.getColor();
        g.setColor(c);
        g.drawLine(position.x - distance, position.y, position.x + distance, position.y);
        g.drawLine(position.x, position.y - distance, position.x, position.y + distance);
        g.setColor(save);
    }

    public static void drawLine(Graphics g, Vector vector, Vector vector2) {
        g.drawLine(vector.x, vector.y, vector2.x, vector2.y);
    }
}
