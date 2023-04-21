package geomss.geom;

import java.util.List;
import javax.measure.unit.Unit;
import javax.measure.quantity.Length;
import javax.measure.quantity.Dimensionless;
import javolution.context.ObjectFactory;
import javolution.util.FastTable;
import javolution.xml.XMLFormat;
import javolution.xml.stream.XMLStreamException;
import javolution.xml.XMLSerializable;
import jahuwaldt.js.param.Parameter;
import geomss.geom.nurbs.NurbsCurve;
import geomss.geom.nurbs.NurbsCurveTrans;

/**
*  A {@link GeomTransform} object that refers to a {@link LineSegment} object
*  and maskerades as a LineSegment object itself.
*
*  <p>  Modified by:  Joseph A. Huwaldt   </p>
*
*  @author Joseph A. Huwaldt    Date:  January 15, 2012
*  @version January 20, 2012
**/
public final class LineSegTrans extends LineSegment implements GeomTransform<LineSegment> {

    /**
	*  The transformation represented by this transformation element.
	**/
    private GTransform _TM;

    /**
	*  The object that is the child of this transform.
	**/
    private LineSegment _child;

    /**
 	* Returns a 3D {@link LineSegTrans} instance holding the specified {@link LineSegment}
	* and {@link GTransform}.
 	*
 	* @param child  The LineSegment that is the child of this transform element
	*               (may not be <code>null</code>).
 	* @param transform  The transform held by this transform element
	*               (may not be <code>null</code>).
 	* @return the transform element having the specified values.
 	*/
    public static LineSegTrans newInstance(LineSegment child, GTransform transform) {
        if (child == null) throw new NullPointerException(RESOURCES.getString("paramNullErr").replace("<NAME/>", "child"));
        if (transform == null) throw new NullPointerException(RESOURCES.getString("paramNullErr").replace("<NAME/>", "transform"));
        if (child.getPhyDimension() != 3) throw new DimensionException(RESOURCES.getString("dimensionNot3").replace("<TYPE/>", "vector").replace("<DIM/>", String.valueOf(child.getPhyDimension())));
        LineSegTrans obj = FACTORY.object();
        obj._TM = transform;
        obj._child = child;
        return obj;
    }

    /**
	*  Returns the transformation represented by this transformation element.
	**/
    public GTransform getTransform() {
        return _TM;
    }

    /**
	*  Sets the transformation represented by this transformation element.
	*
	*  @param transform  The transform to set this transform element to
	*                    (may not be <code>null</code>).
	**/
    public void setTransform(GTransform transform) {
        if (transform == null) throw new NullPointerException(RESOURCES.getString("paramNullErr").replace("<NAME/>", "transform"));
        _TM = transform;
    }

    /**
	*  Returns the child object transformed by this transform element.
	**/
    public LineSegment getChild() {
        return _child;
    }

    /**
	*  Return a copy of the child object transformed by this transformation.
	**/
    public LineSegment copyToReal() {
        GeomPoint p0 = getStart().copyToReal();
        GeomPoint p1 = getEnd().copyToReal();
        LineSeg L = LineSeg.valueOf(p0, p1);
        return copyState(L);
    }

    /**
	 *  Return the starting point of the line segment.
	 **/
    public GeomPoint getStart() {
        return _child.getStart().getTransformed(_TM);
    }

    /**
	 *  Return the end point of the line segment.
	 **/
    public GeomPoint getEnd() {
        return _child.getEnd().getTransformed(_TM);
    }

    /**
	 *  Return the dimensional derivative vector for this line segment.
	 *  The length of this vector is the length of the line segment,
	 *  the origin is at the start point and the end of the vector is the line end.
	 **/
    public GeomVector<Length> getDerivativeVector() {
        return _child.getDerivativeVector().getTransformed(_TM);
    }

    /**
	 *  Get unit direction vector for the line segment.
	 **/
    public GeomVector<Dimensionless> getUnitVector() {
        return _child.getUnitVector().getTransformed(_TM);
    }

    /**
	*  Returns the number of child-elements that make up this geometry element.
	*  This implementation returns the size in the child curve.
	**/
    public int size() {
        return _child.size();
    }

    /**
	* Returns the number of physical dimensions of the geometry element.
	*/
    public int getPhyDimension() {
        return _child.getPhyDimension();
    }

    /**
	* Calculate a point on the curve for the given parameteric distance
	* along the curve, <code>p(s)</code>.
	*
	* @param s  parametric distance to calculate a point for (0.0 to 1.0 inclusive).
	* @return calculated point or <code>null</code> if the parameter is not in a valid range.
	**/
    public GeomPoint getRealPoint(double s) {
        return _TM.transform(_child.getRealPoint(s));
    }

    /**
	* Calculate all the derivatives from <code>0</code> to <code>grade</code> with respect
	* to parametric distance on the curve for the given parameteric distance along the curve,
	* <code>d^{grade}p(s)/d^{grade}s</code>.
	* Example:  1st derivative (grade = 1), this returns <code>[p(s), dp(s)/ds]</code>;
	*			2nd derivative (grade = 2), this returns <code>[p(s), dp(s)/ds, d^2p(s)/d^2s]</code>; etc.<br>
	*
	* @param s     Parametric distance to calculate derivatives for (0.0 to 1.0 inclusive).
	* @param grade The maximum grade to calculate the derivatives for (1=1st derivative, 2=2nd derivative, etc)
	* @return A list of derivatives up to the specified grade of the curve at the specified parametric position.
	* @throws IllegalArgumentException if the grade is < 0.
	**/
    public List<GeomVector<Length>> getSDerivatives(double s, int grade) {
        LineSegment trans = copyToReal();
        return trans.getSDerivatives(s, grade);
    }

    /**
	*  Return a new curve that is identical to this one, but with the 
	*  parameterization reversed.
	**/
    public LineSegTrans reverse() {
        return LineSegTrans.newInstance(_child.reverse(), _TM);
    }

    /**
	*  Return the coordinate point representing the
	*  minimum bounding box corner of this geometry element (e.g.: min X, min Y, min Z).
	*
	*  @return The minimum bounding box coordinate for this geometry element.
	*  @throws IndexOutOfBoundsException if this list contains no elements.
	**/
    public GeomPoint getBoundsMin() {
        LineSegment trans = copyToReal();
        GeomPoint minPoint = trans.getBoundsMin();
        return minPoint;
    }

    /**
	*  Return the coordinate point representing the
	*  maximum bounding box corner (e.g.: max X, max Y, max Z).
	*
	*  @return The maximum bounding box coordinate for this geometry element.
	*  @throws IndexOutOfBoundsException if this list contains no elements.
	**/
    public GeomPoint getBoundsMax() {
        LineSegment trans = copyToReal();
        GeomPoint maxPoint = trans.getBoundsMax();
        return maxPoint;
    }

    /**
	*  Returns transformed version of this element.  The returned object implements
	*  {@link GeomTransform} and contains this element as a child.
	**/
    public LineSegTrans getTransformed(GTransform transform) {
        return LineSegTrans.newInstance(this, transform);
    }

    /**
	*  Returns the unit in which this curve is stated.
	**/
    public Unit<Length> getUnit() {
        return _child.getUnit();
    }

    /**
 	* Returns the equivalent to this curve but stated in the 
 	* specified unit. 
 	*
 	* @param  unit the length unit of the curve to be returned.
 	* @return an equivalent to this curve but stated in the 
 	* 		specified unit.
 	* @throws ConversionException if the the input unit is not a length unit.
 	*/
    public LineSegTrans to(Unit<Length> unit) {
        if (getUnit().equals(unit)) return this;
        return LineSegTrans.newInstance(_child.to(unit), _TM);
    }

    /**
	*  Return a copy of this curve converted to the specified number
	*  of physical dimensions.  If the number of dimensions is greater than
	*  this element, then zeros are added to the additional dimensions.
	*  If the number of dimensions is less than this element, then
	*  the extra dimensions are simply dropped (truncated).  If
	*  the new dimensions are the same as the dimension of this element,
	*  then this element is simply returned.
	*
	*  @param newDim  The dimension of the curve to return.
	*  @return This curve converted to the new dimensions.
	**/
    public LineSegTrans toDimension(int newDim) {
        if (getPhyDimension() == newDim) return this;
        return LineSegTrans.newInstance(_child.toDimension(newDim), _TM);
    }

    /**
	 *  Return a transformed NURBS curve representation of this line segment.
	 *  An exact representation is returned and the tolerance parameter
	 *  is ignored.
	 *
	 *  @param tol  Ignored.  May pass <code>null</code>.
	 *  @return A transformed NURBS curve that exactly represents this line segment.
	 **/
    public NurbsCurve toNurbs(Parameter<Length> tol) {
        NurbsCurve curve = NurbsCurveTrans.newInstance(_child.toNurbs(tol), _TM);
        copyState(curve);
        return curve;
    }

    /**
	* Returns a copy of this LineSegTrans instance  
	* {@link javolution.context.AllocatorContext allocated} 
	* by the calling thread (possibly on the stack).
	*	
	* @return an identical and independant copy of this point.
	*/
    public LineSegTrans copy() {
        return LineSegTrans.copyOf(this);
    }

    /**
	* Returns a copy of this LineSegTrans instance  
	* {@link javolution.context.AllocatorContext allocated} 
	* by the calling thread (possibly on the stack).
	*	
	* @return an identical and independant copy of this point.
	**/
    public Object clone() {
        return LineSegTrans.copyOf(this);
    }

    /**
 	* Compares this LineSegTrans against the specified object for strict 
 	* equality.
	*
 	* @param  obj the object to compare with.
 	* @return <code>true</code> if this transform is identical to that
 	* 		transform; <code>false</code> otherwise.
	**/
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if ((obj == null) || (obj.getClass() != this.getClass())) return false;
        LineSegTrans that = (LineSegTrans) obj;
        if (!this._TM.equals(that._TM)) return false;
        if (!this._child.equals(that._child)) return false;
        return super.equals(obj);
    }

    /**
 	* Returns the hash code for this parameter.
 	* 
 	* @return the hash code value.
 	*/
    public int hashCode() {
        int hash = 7;
        hash = hash * 31 + _TM.hashCode();
        hash = hash * 31 + _child.hashCode();
        return hash;
    }

    /**
 	* Holds the default XML representation. For example:
 	*/
    protected static final XMLFormat<LineSegTrans> XML = new XMLFormat<LineSegTrans>(LineSegTrans.class) {

        @Override
        public LineSegTrans newInstance(Class<LineSegTrans> cls, InputElement xml) throws XMLStreamException {
            return FACTORY.object();
        }

        @SuppressWarnings("unchecked")
        @Override
        public void read(InputElement xml, LineSegTrans V) throws XMLStreamException {
            V._TM = xml.getNext();
            V._child = xml.getNext();
        }

        @Override
        public void write(LineSegTrans V, OutputElement xml) throws XMLStreamException {
            xml.add(V._TM);
            xml.add(V._child);
        }
    };

    private static final ObjectFactory<LineSegTrans> FACTORY = new ObjectFactory<LineSegTrans>() {

        @Override
        protected LineSegTrans create() {
            return new LineSegTrans();
        }
    };

    /**
	*  Recycles a <code>LineSegTrans</code> instance immediately (on the stack when executing in a StackContext).
	**/
    public static void recycle(LineSegTrans instance) {
        FACTORY.recycle(instance);
    }

    @SuppressWarnings("unchecked")
    private static LineSegTrans copyOf(LineSegTrans original) {
        LineSegTrans o = FACTORY.object();
        o._TM = original._TM;
        o._child = original._child.copy();
        return o;
    }

    /**
	*  Do not allow the default constructor to be used except by subclasses.
	**/
    private LineSegTrans() {
    }
}
