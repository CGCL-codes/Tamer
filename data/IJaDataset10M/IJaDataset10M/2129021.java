package com.ogprover.prover_protocol.cp.thmstatement;

import java.util.Vector;
import com.ogprover.main.OpenGeoProver;
import com.ogprover.polynomials.XPolynomial;
import com.ogprover.prover_protocol.cp.auxiliary.DoubleSignedAreaOfPolygon;
import com.ogprover.prover_protocol.cp.geoconstruction.GeoConstruction;
import com.ogprover.prover_protocol.cp.geoconstruction.Point;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for statement about linear combination of 
*     double signed polygon areas.</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class LinearCombinationOfDoubleSignedPolygonAreas extends DimensionThmStatement {

    /**
	 * <i><b>
	 * Version number of class in form xx.yy where
	 * xx is major version/release number and yy is minor
	 * release number.
	 * </b></i>
	 */
    public static final String VERSION_NUM = "1.00";

    /**
	 * Polygons of linear combination - each is represented by vector of ordered vertices
	 */
    private Vector<Vector<Point>> polygons = null;

    /**
	 * Coefficients of linear combination
	 */
    private Vector<Double> coefficients = null;

    /**
	 * @param polygons the polygons to set
	 */
    public void setPolygons(Vector<Vector<Point>> polygons) {
        this.polygons = polygons;
    }

    /**
	 * @return the polygons
	 */
    public Vector<Vector<Point>> getPolygons() {
        return polygons;
    }

    /**
	 * @param coefficients the coefficients to set
	 */
    public void setCoefficients(Vector<Double> coefficients) {
        this.coefficients = coefficients;
    }

    /**
	 * @return the coefficients
	 */
    public Vector<Double> getCoefficients() {
        return coefficients;
    }

    /**
	 * Constructor method.
	 * 
	 * @param polygons		Polygons of linear combination; each represented 
	 *                      by vector of ordered vertices
	 * @param coefficients	Coefficients of linear combination
	 */
    public LinearCombinationOfDoubleSignedPolygonAreas(Vector<Vector<Point>> polygons, Vector<Double> coefficients) {
        this.polygons = polygons;
        this.coefficients = coefficients;
        this.geoObjects = new Vector<GeoConstruction>();
        for (Vector<Point> vertices : this.polygons) {
            for (Point P : vertices) {
                if (this.geoObjects.indexOf(P) < 0) this.geoObjects.add(P);
            }
        }
    }

    /**
	 * @see com.ogprover.prover_protocol.cp.thmstatement.ElementaryThmStatement#isValid()
	 */
    @Override
    public boolean isValid() {
        if (!super.isValid()) return false;
        if (this.polygons == null || this.coefficients == null || this.polygons.size() != this.coefficients.size()) {
            OpenGeoProver.settings.getLogger().error("There must be equal number of polygons and coefficients.");
            return false;
        }
        return true;
    }

    /**
	 * @see com.ogprover.prover_protocol.cp.thmstatement.ThmStatement#getAlgebraicForm()
	 */
    @Override
    public XPolynomial getAlgebraicForm() {
        XPolynomial statementPoly = new XPolynomial();
        for (int ii = 0, jj = this.polygons.size(); ii < jj; ii++) {
            DoubleSignedAreaOfPolygon area = new DoubleSignedAreaOfPolygon(this.polygons.get(ii));
            statementPoly.addPolynomial(area.getAreaPolynomial().multiplyByRealConstant(this.coefficients.get(ii).doubleValue()));
        }
        return statementPoly;
    }

    /**
	 * @see com.ogprover.prover_protocol.cp.thmstatement.ThmStatement#getStatementDesc()
	 */
    @Override
    public String getStatementDesc() {
        StringBuilder sb = new StringBuilder();
        sb.append("Linear combination of double signed polygon areas: ");
        for (int ii = 0; ii < this.coefficients.size(); ii++) {
            if (ii > 0 && this.coefficients.get(ii) > 0) sb.append("+");
            sb.append(this.coefficients.get(ii));
            sb.append("*");
            for (Point vertex : this.polygons.get(ii)) sb.append(vertex.getGeoObjectLabel());
        }
        sb.append(" equals zero");
        return sb.toString();
    }
}
