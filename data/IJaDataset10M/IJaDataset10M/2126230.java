package com.ogprover.prover_protocol.cp.thmstatement;

import java.util.ArrayList;
import java.util.Vector;
import com.ogprover.main.OpenGeoProver;
import com.ogprover.polynomials.Variable;
import com.ogprover.polynomials.XPolySystem;
import com.ogprover.polynomials.XPolynomial;
import com.ogprover.prover_protocol.cp.auxiliary.PointSetRelationshipManager;
import com.ogprover.prover_protocol.cp.geoconstruction.GeoConstruction;
import com.ogprover.prover_protocol.cp.geoconstruction.IntersectionPoint;
import com.ogprover.prover_protocol.cp.geoconstruction.Line;
import com.ogprover.prover_protocol.cp.geoconstruction.Point;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for statement about n concurrent lines (n>=3)</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class ConcurrentLines extends PositionThmStatement {

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
	 * @param lineList	List of lines that make the statement about concurrent lines.
	 */
    public ConcurrentLines(ArrayList<Line> lineList) {
        if (lineList == null || lineList.size() < 3) {
            OpenGeoProver.settings.getLogger().error("There should be at least three lines for statement about concurrent lines.");
            return;
        }
        this.geoObjects = new Vector<GeoConstruction>();
        for (Line l : lineList) this.geoObjects.add(l);
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
        IntersectionPoint bestIntersectionPoint = null;
        for (GeoConstruction geoCons : this.consProtocol.getConstructionSteps()) {
            if (!(geoCons instanceof Point)) continue;
            Point P = (Point) geoCons;
            Vector<Line> linesHavingP = new Vector<Line>();
            PointSetRelationshipManager manager = null;
            tempCond = new XPolynomial();
            for (GeoConstruction gc : this.geoObjects) {
                Line l = (Line) gc;
                if (l.getPoints().indexOf(P) >= 0) linesHavingP.add(l);
            }
            if (linesHavingP.size() >= 2) {
                int numLinesLeft = this.geoObjects.size() - linesHavingP.size();
                for (GeoConstruction gc : this.geoObjects) {
                    Line l = (Line) gc;
                    if (linesHavingP.indexOf(l) >= 0) continue;
                    manager = new PointSetRelationshipManager(l, P, PointSetRelationshipManager.MANAGER_TYPE_STATEMENT);
                    tempAddend = manager.retrieveInstantiatedCondition();
                    if (numLinesLeft == 1) {
                        tempCond.addPolynomial(tempAddend);
                        break;
                    }
                    tempCond.addPolynomial(tempAddend.clone().multiplyByPolynomial(tempAddend));
                    numLinesLeft--;
                    if (numLinesLeft == 0) break;
                }
                int tempDegree = tempCond.getPolynomialDegree();
                if (bestCondition == null || tempDegree < degreeOfBestCondition || (tempDegree == degreeOfBestCondition && tempCond.getTerms().size() < bestCondition.getTerms().size())) {
                    bestCondition = tempCond;
                    degreeOfBestCondition = tempDegree;
                }
            }
        }
        boolean isOneLineLeft = (this.geoObjects.size() == 3);
        PointSetRelationshipManager manager = null;
        for (int ii1 = 0, jj1 = this.geoObjects.size(); ii1 < jj1; ii1++) {
            Line l1 = (Line) this.geoObjects.get(ii1);
            for (int ii2 = 0, jj2 = this.geoObjects.size(); ii2 < jj2; ii2++) {
                Line l2 = (Line) this.geoObjects.get(ii2);
                if (l2.getGeoObjectLabel().equals(l1.getGeoObjectLabel())) continue;
                tempCond = new XPolynomial();
                int numOfPolynomialsInSystem = this.consProtocol.getAlgebraicGeoTheorem().getHypotheses().getPolynomials().size();
                IntersectionPoint P = new IntersectionPoint("intersectPoint-" + l1.getGeoObjectLabel() + "." + l2.getGeoObjectLabel(), l1, l2);
                this.consProtocol.addGeoConstruction(P);
                if (P.isValidConstructionStep() == false) {
                    OpenGeoProver.settings.getLogger().error("Failed to validate the construction of intersection point " + P.getGeoObjectLabel());
                    return null;
                }
                P.transformToAlgebraicFormWithOutputPrintFlag(false);
                int numOfDependentCoordinates = 0;
                if (P.getX().getVariableType() == Variable.VAR_TYPE_UX_X) numOfDependentCoordinates++;
                if (P.getY().getVariableType() == Variable.VAR_TYPE_UX_X) numOfDependentCoordinates++;
                for (int ii = 0, jj = this.geoObjects.size(); ii < jj; ii++) {
                    Line l = (Line) this.geoObjects.get(ii);
                    if (l.getGeoObjectLabel().equals(l1.getGeoObjectLabel()) || l.getGeoObjectLabel().equals(l2.getGeoObjectLabel())) continue;
                    manager = new PointSetRelationshipManager(l, P, PointSetRelationshipManager.MANAGER_TYPE_STATEMENT);
                    XPolynomial instantiatedCondition = manager.retrieveInstantiatedCondition();
                    if (instantiatedCondition == null) {
                        OpenGeoProver.settings.getLogger().error("Failed to retrieve the condition for point " + P.getGeoObjectLabel() + " to belong to line " + l.getGeoObjectLabel());
                        return null;
                    }
                    tempAddend = instantiatedCondition;
                    if (isOneLineLeft) {
                        tempCond.addPolynomial(tempAddend);
                        break;
                    }
                    tempCond.addPolynomial(tempAddend.clone().multiplyByPolynomial(tempAddend));
                }
                int tempDegree = tempCond.getPolynomialDegree();
                if (bestCondition == null || tempDegree < degreeOfBestCondition || (tempDegree == degreeOfBestCondition && tempCond.getTerms().size() < bestCondition.getTerms().size())) {
                    bestCondition = tempCond;
                    degreeOfBestCondition = tempDegree;
                    bestIntersectionPoint = P;
                }
                this.consProtocol.getConstructionSteps().remove(P.getIndex());
                l1.getPoints().remove(P);
                l2.getPoints().remove(P);
                XPolySystem system = this.consProtocol.getAlgebraicGeoTheorem().getHypotheses();
                for (int ii = 0, jj = system.getPolynomials().size() - numOfPolynomialsInSystem; ii < jj; ii++) system.removePoly(system.getPolynomials().size() - 1);
                for (int ii = 0; ii < numOfDependentCoordinates; ii++) this.consProtocol.decrementXIndex();
            }
        }
        if (bestIntersectionPoint != null) {
            Line l1 = (Line) bestIntersectionPoint.getFirstPointSet();
            Line l2 = (Line) bestIntersectionPoint.getSecondPointSet();
            Point P = new IntersectionPoint("intersectPoint-" + l1.getGeoObjectLabel() + "." + l2.getGeoObjectLabel(), l1, l2);
            this.consProtocol.addGeoConstruction(P);
            if (P.isValidConstructionStep() == false) {
                OpenGeoProver.settings.getLogger().error("Failed to validate the construction of intersection point " + bestIntersectionPoint.getGeoObjectLabel());
                return null;
            }
            P.transformToAlgebraicForm();
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
            OpenGeoProver.settings.getLogger().error("There should be at least three lines.");
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
        sb.append("Lines ");
        boolean bFirst = true;
        for (GeoConstruction geoCons : this.geoObjects) {
            if (!bFirst) sb.append(", "); else bFirst = false;
            sb.append(geoCons.getGeoObjectLabel());
        }
        sb.append(" are concurrent");
        return sb.toString();
    }
}
