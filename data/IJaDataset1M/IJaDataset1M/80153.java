package com.google.gwt.maeglin89273.game.ashinyballonthecross.client.core.creation.shape;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.FixtureDef;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.maeglin89273.game.ashinyballonthecross.client.utility.ASBOTXConfigs;
import com.google.gwt.maeglin89273.game.mengine.physics.Point;
import com.google.gwt.maeglin89273.game.mengine.physics.Vector;
import com.google.gwt.maeglin89273.game.mengine.physics.CoordinateConverter;
import com.google.gwt.user.client.Random;

/**
 * @author Maeglin Liao
 *
 */
public class Polygon extends PhysicalShape {

    private Vector[] vertices;

    /**
	 * 
	 * @param creator
	 * @param p
	 */
    public Polygon(Point p) {
        this(0, null, p, 0, generateRandomInscribedPolygonVertices(3 + Random.nextInt(6), 12 + Random.nextInt(4)), ASBOTXConfigs.Color.getRandomShapeBorderColor());
    }

    /**
	 * 
	 * @param creator
	 * @param p
	 * @param angle
	 * @param vertices
	 * @param color
	 */
    public Polygon(ShapesController controller, Point p, double angle, Vector[] vertices, CssColor color) {
        this(Math.round(vertices.length * 2.5f), controller, p, angle, vertices, color);
    }

    /**
	 * 
	 * @param creator
	 * @param contentPower
	 * @param p
	 * @param angle
	 * @param vertices
	 * @param color
	 */
    private Polygon(int contentPower, ShapesController controller, Point p, double angle, Vector[] vertices, CssColor color) {
        super(contentPower, controller, p, 0, 0, angle, color);
        if (this.isVerified()) {
            this.vertices = vertices;
            PolygonShape shape = new PolygonShape();
            FixtureDef fixtureDef = new FixtureDef();
            Vec2[] vs = new Vec2[vertices.length];
            for (int i = 0; i < vs.length; i++) {
                vs[i] = CoordinateConverter.vectorPixelToWorld(vertices[i]);
            }
            shape.set(vs, vs.length);
            fixtureDef.shape = shape;
            fixtureDef.friction = 0.7f;
            fixtureDef.density = 1.5f;
            fixtureDef.restitution = 0.6f;
            aabb = body.createFixture(fixtureDef).getAABB();
        }
    }

    @Override
    public void draw(Context2d context) {
        context.save();
        context.setStrokeStyle(borderColor);
        context.setLineWidth(1.25);
        context.translate(getX(), getY());
        context.rotate(getAngle());
        context.beginPath();
        context.moveTo(vertices[0].getVectorX(), vertices[0].getVectorY());
        for (int i = 1; i < vertices.length; i++) {
            context.lineTo(vertices[i].getVectorX(), vertices[i].getVectorY());
        }
        context.closePath();
        context.stroke();
        context.restore();
    }

    @Override
    public int hashCode() {
        return 2;
    }

    public static Vector[] generateRandomInscribedPolygonVertices(int verticesCount, double circumscribedCircleRadius) {
        if (verticesCount > 8) {
            throw new IllegalArgumentException("too many vertices!");
        } else if (verticesCount < 3) {
            throw new IllegalArgumentException("too few vertices!");
        }
        Point g = new Point(0, 0);
        double theta = 2 * Math.PI / verticesCount;
        double angle = 0;
        Point[] verticesP = new Point[verticesCount];
        Vector[] vertices = new Vector[verticesCount];
        for (int c = 0; c < vertices.length; c++) {
            angle = theta * Random.nextDouble() + c * theta;
            verticesP[c] = new Point(circumscribedCircleRadius, angle, true);
            g.translate(verticesP[c].getX(), verticesP[c].getY());
        }
        g.setPosition(g.getX() / verticesCount, g.getY() / verticesCount);
        for (int i = 0; i < vertices.length; i++) {
            vertices[i] = new Vector(verticesP[i].getX() - g.getX(), verticesP[i].getY() - g.getY());
        }
        return vertices;
    }
}
