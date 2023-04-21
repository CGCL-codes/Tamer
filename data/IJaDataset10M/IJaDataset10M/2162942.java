package ie.dkit.java3Demulation.objects3d;

import java.util.ArrayList;
import java.util.Collection;
import ie.dkit.java3Demulation.drawer.Line3DDrawable;
import ie.dkit.java3Demulation.transforming.Transformable;

/**
 * @author Sebastian Ruehl
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */
public class Line3D implements Transformable, Line3DDrawable {

    private Point3D m_PointA;

    private Point3D m_PointB;

    private Point3D m_RotationPoint;

    private String name;

    public Line3D() {
        m_PointA = new Point3D(-10, 0, 0);
        m_PointA.setName("Point A");
        m_PointB = new Point3D(10, 0, 0);
        m_PointB.setName("Point B");
        name = "Line3D";
    }

    /**
	 * @param m_a
	 * @param m_b
	 */
    public Line3D(Point3D m_a, Point3D m_b) {
        super();
        m_PointA = m_a;
        m_PointB = m_b;
    }

    /**
	 * @return the a
	 */
    public Point3D getPointA() {
        return m_PointA;
    }

    /**
	 * @param a
	 *            the a to set
	 */
    public void setPointA(Point3D a) {
        m_PointA = a;
    }

    /**
	 * @return the b
	 */
    public Point3D getPointB() {
        return m_PointB;
    }

    /**
	 * @param b
	 *            the b to set
	 */
    public void setPointB(Point3D b) {
        m_PointB = b;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<Point3D> getPoints() {
        Collection<Point3D> returnValue = new ArrayList<Point3D>();
        returnValue.add(m_PointA);
        returnValue.add(m_PointB);
        return returnValue;
    }

    public Point3D getRotationPoint() {
        return m_RotationPoint;
    }

    public void setRotationPoint(Point3D point3D) {
        m_RotationPoint = point3D;
    }

    public Collection<Line3D> getLines() {
        Collection<Line3D> returnValue = new ArrayList<Line3D>();
        returnValue.add(this);
        return returnValue;
    }
}
