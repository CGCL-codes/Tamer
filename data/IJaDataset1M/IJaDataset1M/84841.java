package com.ogprover.prover_protocol.cp.thmstatement;

import java.util.ArrayList;
import java.util.Vector;
import com.ogprover.main.OpenGeoProver;
import com.ogprover.polynomials.XPolynomial;
import com.ogprover.prover_protocol.cp.auxiliary.PointSetRelationshipManager;
import com.ogprover.prover_protocol.cp.geoconstruction.GeoConstruction;
import com.ogprover.prover_protocol.cp.geoconstruction.Line;
import com.ogprover.prover_protocol.cp.geoconstruction.LineThroughTwoPoints;
import com.ogprover.prover_protocol.cp.geoconstruction.Point;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for statement about n collinear points (n>=3)</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class CollinearPoints extends PositionThmStatement {

    /**
	 * <i><b>
	 * Version number of class in form xx.yy where
	 * xx is major version/release number and yy is minor
	 * release number.
	 * </b></i>
	 */
    public static final String VERSION_NUM = "1.00";

    /**
	 * Constructor method.
	 * 
	 * @param pointList	List of points that make the statement about collinear points.
	 */
    public CollinearPoints(ArrayList<Point> pointList) {
        if (pointList == null || pointList.size() < 3) {
            OpenGeoProver.settings.getLogger().error("There should be at least three points for statement about collinear points.");
            return;
        }
        this.geoObjects = new Vector<GeoConstruction>();
        for (Point p : pointList) this.geoObjects.add(p);
    }

    /**
	 * @see com.ogprover.prover_protocol.cp.thmstatement.ThmStatement#getAlgebraicForm()
	 */
    @Override
    public XPolynomial getAlgebraicForm() {
        XPolynomial tempCond = null;
        XPolynomial tempAddend = null;
        XPolynomial bestCondition = null;
        int degreeOfBestCondition = 0;
        for (GeoConstruction geoCons : this.consProtocol.getConstructionSteps()) {
            if (!(geoCons instanceof Line)) continue;
            Line l = (Line) geoCons;
            Vector<Point> lPoints = l.getPoints();
            ArrayList<Point> pointsFromLine = new ArrayList<Point>();
            PointSetRelationshipManager manager = null;
            tempCond = new XPolynomial();
            for (GeoConstruction gc : this.geoObjects) {
                Point p = (Point) gc;
                if (lPoints.indexOf(p) >= 0) pointsFromLine.add(p);
            }
            if (pointsFromLine.size() >= 2) {
                int numPointsLeft = this.geoObjects.size() - pointsFromLine.size();
                for (GeoConstruction gc : this.geoObjects) {
                    Point p = (Point) gc;
                    if (pointsFromLine.indexOf(p) >= 0) continue;
                    manager = new PointSetRelationshipManager(l, p, PointSetRelationshipManager.MANAGER_TYPE_STATEMENT);
                    tempAddend = manager.retrieveInstantiatedCondition();
                    if (numPointsLeft == 1) {
                        tempCond.addPolynomial(tempAddend);
                        break;
                    }
                    tempCond.addPolynomial(tempAddend.clone().multiplyByPolynomial(tempAddend));
                    numPointsLeft--;
                    if (numPointsLeft == 0) break;
                }
                int tempDegree = tempCond.getPolynomialDegree();
                if (bestCondition == null || tempDegree < degreeOfBestCondition || (tempDegree == degreeOfBestCondition && tempCond.getTerms().size() < bestCondition.getTerms().size())) {
                    bestCondition = tempCond;
                    degreeOfBestCondition = tempDegree;
                }
            }
        }
        boolean isOnePointLeft = (this.geoObjects.size() == 3);
        for (int ii1 = 0, jj1 = this.geoObjects.size(); ii1 < jj1; ii1++) {
            Point p1 = (Point) this.geoObjects.get(ii1);
            for (int ii2 = 0, jj2 = this.geoObjects.size(); ii2 < jj2; ii2++) {
                Point p2 = (Point) this.geoObjects.get(ii2);
                if (p2.getGeoObjectLabel().equals(p1.getGeoObjectLabel())) continue;
                Line l = new LineThroughTwoPoints("tempLine", p1, p2);
                tempCond = new XPolynomial();
                for (int ii = 0, jj = this.geoObjects.size(); ii < jj; ii++) {
                    Point p = (Point) this.geoObjects.get(ii);
                    if (p.getGeoObjectLabel().equals(p1.getGeoObjectLabel()) || p.getGeoObjectLabel().equals(p2.getGeoObjectLabel())) continue;
                    tempAddend = l.instantiateConditionFromBasicElements(p);
                    if (isOnePointLeft) {
                        tempCond.addPolynomial(tempAddend);
                        break;
                    }
                    tempCond.addPolynomial(tempAddend.clone().multiplyByPolynomial(tempAddend));
                }
                int tempDegree = tempCond.getPolynomialDegree();
                if (bestCondition == null || tempDegree < degreeOfBestCondition || (tempDegree == degreeOfBestCondition && tempCond.getTerms().size() < bestCondition.getTerms().size())) {
                    bestCondition = tempCond;
                    degreeOfBestCondition = tempDegree;
                }
            }
        }
        return bestCondition;
    }

    /**
	 * @see com.ogprover.prover_protocol.cp.thmstatement.ElementaryThmStatement#isValid()
	 */
    @Override
    public boolean isValid() {
        if (!super.isValid()) return false;
        if (this.geoObjects.size() < 3) {
            OpenGeoProver.settings.getLogger().error("There should be at least three points.");
            return false;
        }
        return true;
    }

    /**
	 * @see com.ogprover.prover_protocol.cp.thmstatement.ThmStatement#getStatementDesc()
	 */
    @Override
    public String getStatementDesc() {
        StringBuilder sb = new StringBuilder();
        sb.append("Points ");
        boolean bFirst = true;
        for (GeoConstruction geoCons : this.geoObjects) {
            if (!bFirst) sb.append(", "); else bFirst = false;
            sb.append(geoCons.getGeoObjectLabel());
        }
        sb.append(" are collinear");
        return sb.toString();
    }
}
