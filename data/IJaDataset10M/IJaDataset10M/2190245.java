package com.ogprover.prover_protocol.cp.auxiliary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import com.ogprover.polynomials.UXVariable;
import com.ogprover.polynomials.XPolynomial;
import com.ogprover.prover_protocol.cp.geoconstruction.AngleBisector;
import com.ogprover.prover_protocol.cp.geoconstruction.Circle;
import com.ogprover.prover_protocol.cp.geoconstruction.CircleWithCenterAndPoint;
import com.ogprover.prover_protocol.cp.geoconstruction.CircleWithCenterAndRadius;
import com.ogprover.prover_protocol.cp.geoconstruction.Line;
import com.ogprover.prover_protocol.cp.geoconstruction.LineThroughTwoPoints;
import com.ogprover.prover_protocol.cp.geoconstruction.Point;
import com.ogprover.prover_protocol.cp.ndgcondition.NDGCondition;
import com.ogprover.prover_protocol.cp.thmstatement.CollinearPoints;
import com.ogprover.prover_protocol.cp.thmstatement.ConcyclicPoints;
import com.ogprover.prover_protocol.cp.thmstatement.FourHarmonicConjugatePoints;
import com.ogprover.prover_protocol.cp.thmstatement.LinearCombinationOfOrientedSegments;
import com.ogprover.prover_protocol.cp.thmstatement.PointOnSetOfPoints;
import com.ogprover.prover_protocol.cp.thmstatement.SegmentsOfEqualLengths;
import com.ogprover.prover_protocol.cp.thmstatement.TwoInversePoints;
import com.ogprover.prover_protocol.cp.thmstatement.TwoParallelLines;
import com.ogprover.prover_protocol.cp.thmstatement.TwoPerpendicularLines;

/**
* <dl>
* <dt><b>Class description:</b></dt>
* <dd>Class for checking whether certain points' positions 
*     of four points correspond to specified NDG condition</dd>
* </dl>
* 
* @version 1.00
* @author Ivan Petrovic
*/
public class FourPointsPositionChecker extends PointsPositionChecker {

    /**
	 * <i><b>
	 * Version number of class in form xx.yy where
	 * xx is major version/release number and yy is minor
	 * release number.
	 * </b></i>
	 */
    public static final String VERSION_NUM = "1.00";

    /**
	 * Constructor method
	 * 
	 * @param ndgCond	NDG condition associated to this points position checker
	 */
    public FourPointsPositionChecker(NDGCondition ndgCond) {
        this.initializePointsPositionChecker(ndgCond);
    }

    /**
	 * Check if points are collinear.
	 * 
	 * @param A		First point
	 * @param B		Second point
	 * @param C		Third point
	 * @param D		Fourth point
	 * @return		True if condition is met, false otherwise
	 */
    private boolean checkFourCollinearPoints(Point A, Point B, Point C, Point D) {
        this.clearAuxCP();
        this.auxiliaryCP.addGeoConstruction(A);
        this.auxiliaryCP.addGeoConstruction(B);
        this.auxiliaryCP.addGeoConstruction(C);
        this.auxiliaryCP.addGeoConstruction(D);
        ArrayList<Point> pointList = new ArrayList<Point>();
        pointList.add(A);
        pointList.add(B);
        pointList.add(C);
        pointList.add(D);
        this.auxiliaryCP.addThmStatement(new CollinearPoints(pointList));
        XPolynomial statementPoly = this.auxiliaryCP.getTheoremStatement().getAlgebraicForm();
        if (statementPoly != null && statementPoly.matchesNDGCPolynomial(this.ndgCond.getPolynomial())) {
            StringBuilder sb = new StringBuilder();
            sb.append("Points ");
            sb.append(A.getGeoObjectLabel());
            sb.append(", ");
            sb.append(B.getGeoObjectLabel());
            sb.append(", ");
            sb.append(C.getGeoObjectLabel());
            sb.append(" and ");
            sb.append(D.getGeoObjectLabel());
            sb.append(" are not collinear");
            Vector<Point> pointsV = new Vector<Point>();
            pointsV.add(A);
            pointsV.add(B);
            pointsV.add(C);
            pointsV.add(D);
            this.ndgCond.addNewText(pointsV, sb.toString());
            return true;
        }
        return false;
    }

    /**
	 * Check if points are concyclic.
	 * 
	 * @param A		First point
	 * @param B		Second point
	 * @param C		Third point
	 * @param D		Fourth point
	 * @return		True if condition is met, false otherwise
	 */
    private boolean checkFourConcyclicPoints(Point A, Point B, Point C, Point D) {
        Map<String, UXVariable> xVarMap = new HashMap<String, UXVariable>();
        xVarMap.put(A.getX().toString(), A.getX());
        xVarMap.put(B.getX().toString(), B.getX());
        xVarMap.put(C.getX().toString(), C.getX());
        xVarMap.put(D.getX().toString(), D.getX());
        Map<String, UXVariable> yVarMap = new HashMap<String, UXVariable>();
        yVarMap.put(A.getY().toString(), A.getY());
        yVarMap.put(B.getY().toString(), B.getY());
        yVarMap.put(C.getY().toString(), C.getY());
        yVarMap.put(D.getY().toString(), D.getY());
        if (xVarMap.size() <= 2 || yVarMap.size() <= 2) return false;
        this.clearAuxCP();
        this.auxiliaryCP.addGeoConstruction(A);
        this.auxiliaryCP.addGeoConstruction(B);
        this.auxiliaryCP.addGeoConstruction(C);
        this.auxiliaryCP.addGeoConstruction(D);
        ArrayList<Point> pointList = new ArrayList<Point>();
        pointList.add(A);
        pointList.add(B);
        pointList.add(C);
        pointList.add(D);
        this.auxiliaryCP.addThmStatement(new ConcyclicPoints(pointList));
        XPolynomial statementPoly = this.auxiliaryCP.getTheoremStatement().getAlgebraicForm();
        if (statementPoly != null && statementPoly.matchesNDGCPolynomial(this.ndgCond.getPolynomial())) {
            StringBuilder sb = new StringBuilder();
            sb.append("Points ");
            sb.append(A.getGeoObjectLabel());
            sb.append(", ");
            sb.append(B.getGeoObjectLabel());
            sb.append(", ");
            sb.append(C.getGeoObjectLabel());
            sb.append(" and ");
            sb.append(D.getGeoObjectLabel());
            sb.append(" are not concyclic");
            Vector<Point> pointsV = new Vector<Point>();
            pointsV.add(A);
            pointsV.add(B);
            pointsV.add(C);
            pointsV.add(D);
            this.ndgCond.addNewText(pointsV, sb.toString());
            return true;
        }
        return false;
    }

    /**
	 * Check if segment AB is equal to segment CD.
	 * 
	 * @param A		First point
	 * @param B		Second point
	 * @param C		Third point
	 * @param D		Fourth point
	 * @return		True if condition is met, false otherwise
	 */
    private boolean checkEqualSegments(Point A, Point B, Point C, Point D) {
        this.clearAuxCP();
        this.auxiliaryCP.addGeoConstruction(A);
        this.auxiliaryCP.addGeoConstruction(B);
        this.auxiliaryCP.addGeoConstruction(C);
        this.auxiliaryCP.addGeoConstruction(D);
        this.auxiliaryCP.addThmStatement(new SegmentsOfEqualLengths(A, B, C, D));
        XPolynomial statementPoly = this.auxiliaryCP.getTheoremStatement().getAlgebraicForm();
        if (statementPoly != null && statementPoly.matchesNDGCPolynomial(this.ndgCond.getPolynomial())) {
            StringBuilder sb = new StringBuilder();
            sb.append("Segment with endpoints ");
            sb.append(A.getGeoObjectLabel());
            sb.append(" and ");
            sb.append(B.getGeoObjectLabel());
            sb.append(" and segment with endpoints ");
            sb.append(C.getGeoObjectLabel());
            sb.append(" and ");
            sb.append(D.getGeoObjectLabel());
            sb.append(" are not of same lengths");
            Vector<Point> pointsV = new Vector<Point>();
            pointsV.add(A);
            pointsV.add(B);
            pointsV.add(C);
            pointsV.add(D);
            this.ndgCond.addNewText(pointsV, sb.toString());
            return true;
        }
        return false;
    }

    /**
	 * Check if lines AB and CD are parallel.
	 * 
	 * @param A		First point
	 * @param B		Second point
	 * @param C		Third point
	 * @param D		Fourth point
	 * @return		True if condition is met, false otherwise
	 */
    private boolean checkParallelLines(Point A, Point B, Point C, Point D) {
        this.clearAuxCP();
        this.auxiliaryCP.addGeoConstruction(A);
        this.auxiliaryCP.addGeoConstruction(B);
        this.auxiliaryCP.addGeoConstruction(C);
        this.auxiliaryCP.addGeoConstruction(D);
        Line AB = new LineThroughTwoPoints("AB", A, B);
        this.auxiliaryCP.addGeoConstruction(AB);
        Line CD = new LineThroughTwoPoints("CD", C, D);
        this.auxiliaryCP.addGeoConstruction(CD);
        this.auxiliaryCP.addThmStatement(new TwoParallelLines(AB, CD));
        XPolynomial statementPoly = this.auxiliaryCP.getTheoremStatement().getAlgebraicForm();
        if (statementPoly != null && statementPoly.matchesNDGCPolynomial(this.ndgCond.getPolynomial())) {
            StringBuilder sb = new StringBuilder();
            sb.append("Line through points ");
            sb.append(A.getGeoObjectLabel());
            sb.append(" and ");
            sb.append(B.getGeoObjectLabel());
            sb.append(" is not parallel with line through points ");
            sb.append(C.getGeoObjectLabel());
            sb.append(" and ");
            sb.append(D.getGeoObjectLabel());
            Vector<Point> pointsV = new Vector<Point>();
            pointsV.add(A);
            pointsV.add(B);
            pointsV.add(C);
            pointsV.add(D);
            this.ndgCond.addNewText(pointsV, sb.toString());
            return true;
        }
        return false;
    }

    /**
	 * Check if lines AB and CD are perpendicular.
	 * 
	 * @param A		First point
	 * @param B		Second point
	 * @param C		Third point
	 * @param D		Fourth point
	 * @return		True if condition is met, false otherwise
	 */
    private boolean checkPerpendicularLines(Point A, Point B, Point C, Point D) {
        this.clearAuxCP();
        this.auxiliaryCP.addGeoConstruction(A);
        this.auxiliaryCP.addGeoConstruction(B);
        this.auxiliaryCP.addGeoConstruction(C);
        this.auxiliaryCP.addGeoConstruction(D);
        Line AB = new LineThroughTwoPoints("AB", A, B);
        this.auxiliaryCP.addGeoConstruction(AB);
        Line CD = new LineThroughTwoPoints("CD", C, D);
        this.auxiliaryCP.addGeoConstruction(CD);
        this.auxiliaryCP.addThmStatement(new TwoPerpendicularLines(AB, CD));
        XPolynomial statementPoly = this.auxiliaryCP.getTheoremStatement().getAlgebraicForm();
        if (statementPoly != null && statementPoly.matchesNDGCPolynomial(this.ndgCond.getPolynomial())) {
            StringBuilder sb = new StringBuilder();
            sb.append("Line through points ");
            sb.append(A.getGeoObjectLabel());
            sb.append(" and ");
            sb.append(B.getGeoObjectLabel());
            sb.append(" is not perpendicular to line through points ");
            sb.append(C.getGeoObjectLabel());
            sb.append(" and ");
            sb.append(D.getGeoObjectLabel());
            Vector<Point> pointsV = new Vector<Point>();
            pointsV.add(A);
            pointsV.add(B);
            pointsV.add(C);
            pointsV.add(D);
            this.ndgCond.addNewText(pointsV, sb.toString());
            return true;
        }
        return false;
    }

    /**
	 * Check if par of points (A, B) and (C, D) are in harmonic conjunction.
	 * 
	 * @param A		First point
	 * @param B		Second point
	 * @param C		Third point
	 * @param D		Fourth point
	 * @return		True if condition is met, false otherwise
	 */
    private boolean checkHarmonicConjugatePoints(Point A, Point B, Point C, Point D) {
        this.clearAuxCP();
        this.auxiliaryCP.addGeoConstruction(A);
        this.auxiliaryCP.addGeoConstruction(B);
        this.auxiliaryCP.addGeoConstruction(C);
        this.auxiliaryCP.addGeoConstruction(D);
        this.auxiliaryCP.addThmStatement(new FourHarmonicConjugatePoints(A, B, C, D));
        XPolynomial statementXPoly = ((FourHarmonicConjugatePoints) this.auxiliaryCP.getTheoremStatement()).getXAlgebraicForm();
        XPolynomial statementYPoly = ((FourHarmonicConjugatePoints) this.auxiliaryCP.getTheoremStatement()).getYAlgebraicForm();
        XPolynomial statementPoly = null;
        if (statementXPoly != null && statementYPoly != null) statementPoly = (XPolynomial) statementXPoly.clone().multiplyByPolynomial(statementXPoly).addPolynomial(statementYPoly.clone().multiplyByPolynomial(statementYPoly));
        if ((statementXPoly != null && statementXPoly.matchesNDGCPolynomial(this.ndgCond.getPolynomial())) || (statementYPoly != null && statementYPoly.matchesNDGCPolynomial(this.ndgCond.getPolynomial())) || (statementPoly != null && statementPoly.matchesNDGCPolynomial(this.ndgCond.getPolynomial()))) {
            StringBuilder sb = new StringBuilder();
            sb.append("Pair of points ");
            sb.append(A.getGeoObjectLabel());
            sb.append(" and ");
            sb.append(B.getGeoObjectLabel());
            sb.append(" is not in harmonic conjunction with pair of points ");
            sb.append(C.getGeoObjectLabel());
            sb.append(" and ");
            sb.append(D.getGeoObjectLabel());
            Vector<Point> pointsV = new Vector<Point>();
            pointsV.add(A);
            pointsV.add(B);
            pointsV.add(C);
            pointsV.add(D);
            this.ndgCond.addNewText(pointsV, sb.toString());
            return true;
        }
        return false;
    }

    /**
	 * Check if segments AB and CD are two congruent collinear segments.
	 * This also covers the case of translated point.
	 * 
	 * @param A		First point
	 * @param B		Second point
	 * @param C		Third point
	 * @param D		Fourth point
	 * @return		True if condition is met, false otherwise
	 */
    private boolean checkCongruentCollinearSegments(Point A, Point B, Point C, Point D) {
        this.clearAuxCP();
        this.auxiliaryCP.addGeoConstruction(A);
        this.auxiliaryCP.addGeoConstruction(B);
        this.auxiliaryCP.addGeoConstruction(C);
        this.auxiliaryCP.addGeoConstruction(D);
        Segment segAB = new Segment(A, B);
        Segment segCD = new Segment(C, D);
        Vector<Segment> segments = new Vector<Segment>();
        segments.add(segAB);
        segments.add(segCD);
        Vector<Double> coefficients = new Vector<Double>();
        coefficients.add(new Double(1));
        coefficients.add(new Double(-1));
        this.auxiliaryCP.addThmStatement(new LinearCombinationOfOrientedSegments(segments, coefficients));
        XPolynomial statementPoly = this.auxiliaryCP.getTheoremStatement().getAlgebraicForm();
        if (statementPoly != null && statementPoly.matchesNDGCPolynomial(this.ndgCond.getPolynomial())) {
            StringBuilder sb = new StringBuilder();
            sb.append("Segment with endpoints ");
            sb.append(A.getGeoObjectLabel());
            sb.append(" and ");
            sb.append(B.getGeoObjectLabel());
            sb.append(" is not collinear and congruent with segment with endpoints ");
            sb.append(C.getGeoObjectLabel());
            sb.append(" and ");
            sb.append(D.getGeoObjectLabel());
            Vector<Point> pointsV = new Vector<Point>();
            pointsV.add(A);
            pointsV.add(B);
            pointsV.add(C);
            pointsV.add(D);
            this.ndgCond.addNewText(pointsV, sb.toString());
            return true;
        }
        return false;
    }

    /**
	 * Check if point D is on angle bisector of angle &lt;ABC.
	 * 
	 * @param A		First point
	 * @param B		Second point
	 * @param C		Third point
	 * @param D		Fourth point
	 * @return		True if condition is met, false otherwise
	 */
    private boolean checkAngleBisectorPoint(Point A, Point B, Point C, Point D) {
        this.clearAuxCP();
        this.auxiliaryCP.addGeoConstruction(A);
        this.auxiliaryCP.addGeoConstruction(B);
        this.auxiliaryCP.addGeoConstruction(C);
        this.auxiliaryCP.addGeoConstruction(D);
        StringBuilder nameSB = new StringBuilder("tempAngBis_");
        nameSB.append(A.getGeoObjectLabel());
        nameSB.append(B.getGeoObjectLabel());
        nameSB.append(C.getGeoObjectLabel());
        Line angBis = new AngleBisector(nameSB.toString(), A, B, C);
        this.auxiliaryCP.addGeoConstruction(angBis);
        this.auxiliaryCP.addThmStatement(new PointOnSetOfPoints(angBis, D));
        XPolynomial statementPoly = this.auxiliaryCP.getTheoremStatement().getAlgebraicForm();
        if (statementPoly != null && statementPoly.matchesNDGCPolynomial(this.ndgCond.getPolynomial())) {
            StringBuilder sb = new StringBuilder();
            sb.append("Point ");
            sb.append(D.getGeoObjectLabel());
            sb.append(" is not on angle bisector of angle with vertex ");
            sb.append(B.getGeoObjectLabel());
            sb.append(" and two points from different rays ");
            sb.append(A.getGeoObjectLabel());
            sb.append(" and ");
            sb.append(C.getGeoObjectLabel());
            Vector<Point> pointsV = new Vector<Point>();
            pointsV.add(A);
            pointsV.add(B);
            pointsV.add(C);
            pointsV.add(D);
            this.ndgCond.addNewText(pointsV, sb.toString());
            return true;
        }
        return false;
    }

    /**
	 * Check if points C and D are on circle with center A and one its point B.
	 * 
	 * @param A		First point
	 * @param B		Second point
	 * @param C		Third point
	 * @param D		Fourth point
	 * @return		True if condition is met, false otherwise
	 */
    private boolean checkTwoPointsOnCircle(Point A, Point B, Point C, Point D) {
        this.clearAuxCP();
        this.auxiliaryCP.addGeoConstruction(A);
        this.auxiliaryCP.addGeoConstruction(B);
        this.auxiliaryCP.addGeoConstruction(C);
        this.auxiliaryCP.addGeoConstruction(D);
        Circle k = new CircleWithCenterAndPoint("tempCircle", A, B);
        this.auxiliaryCP.addGeoConstruction(k);
        this.auxiliaryCP.addThmStatement(new PointOnSetOfPoints(k, C));
        XPolynomial statementPolyC = this.auxiliaryCP.getTheoremStatement().getAlgebraicForm();
        this.auxiliaryCP.addThmStatement(new PointOnSetOfPoints(k, D));
        XPolynomial statementPolyD = this.auxiliaryCP.getTheoremStatement().getAlgebraicForm();
        XPolynomial statementPoly = null;
        if (statementPolyC != null && statementPolyD != null) statementPoly = (XPolynomial) statementPolyC.clone().multiplyByPolynomial(statementPolyC).addPolynomial(statementPolyD.clone().multiplyByPolynomial(statementPolyD));
        if (statementPoly != null && statementPoly.matchesNDGCPolynomial(this.ndgCond.getPolynomial())) {
            StringBuilder sb = new StringBuilder();
            sb.append("Points ");
            sb.append(C.getGeoObjectLabel());
            sb.append(" and ");
            sb.append(D.getGeoObjectLabel());
            sb.append(" are not together on circle with center ");
            sb.append(A.getGeoObjectLabel());
            sb.append(" and one point on it ");
            sb.append(B.getGeoObjectLabel());
            Vector<Point> pointsV = new Vector<Point>();
            pointsV.add(A);
            pointsV.add(B);
            pointsV.add(C);
            pointsV.add(D);
            this.ndgCond.addNewText(pointsV, sb.toString());
            return true;
        }
        return false;
    }

    /**
	 * Check if point D is on circle with center A and radius equal to segment BC.
	 * 
	 * @param A		First point
	 * @param B		Second point
	 * @param C		Third point
	 * @param D		Fourth point
	 * @return		True if condition is met, false otherwise
	 */
    private boolean checkPointOnCircle(Point A, Point B, Point C, Point D) {
        this.clearAuxCP();
        this.auxiliaryCP.addGeoConstruction(A);
        this.auxiliaryCP.addGeoConstruction(B);
        this.auxiliaryCP.addGeoConstruction(C);
        this.auxiliaryCP.addGeoConstruction(D);
        Circle k = new CircleWithCenterAndRadius("tempCircle", A, B, C);
        this.auxiliaryCP.addGeoConstruction(k);
        this.auxiliaryCP.addThmStatement(new PointOnSetOfPoints(k, D));
        XPolynomial statementPoly = this.auxiliaryCP.getTheoremStatement().getAlgebraicForm();
        if (statementPoly != null && statementPoly.matchesNDGCPolynomial(this.ndgCond.getPolynomial())) {
            StringBuilder sb = new StringBuilder();
            sb.append("Point ");
            sb.append(D.getGeoObjectLabel());
            sb.append(" is not on circle with center ");
            sb.append(A.getGeoObjectLabel());
            sb.append(" and radius equal to segment with endpoints ");
            sb.append(B.getGeoObjectLabel());
            sb.append(" and ");
            sb.append(C.getGeoObjectLabel());
            Vector<Point> pointsV = new Vector<Point>();
            pointsV.add(A);
            pointsV.add(B);
            pointsV.add(C);
            pointsV.add(D);
            this.ndgCond.addNewText(pointsV, sb.toString());
            return true;
        }
        return false;
    }

    /**
	 * Check if points C and D are two inverse points with respect to circle
	 * with center A one one point from it B.
	 * 
	 * @param A		First point
	 * @param B		Second point
	 * @param C		Third point
	 * @param D		Fourth point
	 * @return		True if condition is met, false otherwise
	 */
    private boolean checkInversePoints(Point A, Point B, Point C, Point D) {
        this.clearAuxCP();
        this.auxiliaryCP.addGeoConstruction(A);
        this.auxiliaryCP.addGeoConstruction(B);
        this.auxiliaryCP.addGeoConstruction(C);
        this.auxiliaryCP.addGeoConstruction(D);
        Circle k = new CircleWithCenterAndPoint("tempCircle", A, B);
        this.auxiliaryCP.addGeoConstruction(k);
        this.auxiliaryCP.addThmStatement(new TwoInversePoints(C, D, k));
        XPolynomial statementXPoly = ((TwoInversePoints) this.auxiliaryCP.getTheoremStatement()).getXAlgebraicForm();
        XPolynomial statementYPoly = ((TwoInversePoints) this.auxiliaryCP.getTheoremStatement()).getYAlgebraicForm();
        XPolynomial statementPoly = null;
        if (statementXPoly != null && statementYPoly != null) statementPoly = (XPolynomial) statementXPoly.clone().multiplyByPolynomial(statementXPoly).addPolynomial(statementYPoly.clone().multiplyByPolynomial(statementYPoly));
        if ((statementXPoly != null && statementXPoly.matchesNDGCPolynomial(this.ndgCond.getPolynomial())) || (statementYPoly != null && statementYPoly.matchesNDGCPolynomial(this.ndgCond.getPolynomial())) || (statementPoly != null && statementPoly.matchesNDGCPolynomial(this.ndgCond.getPolynomial()))) {
            StringBuilder sb = new StringBuilder();
            sb.append("Points ");
            sb.append(C.getGeoObjectLabel());
            sb.append(" and ");
            sb.append(D.getGeoObjectLabel());
            sb.append(" are not two inverse points with respect to circle with center ");
            sb.append(A.getGeoObjectLabel());
            sb.append(" and one point from it ");
            sb.append(B.getGeoObjectLabel());
            Vector<Point> pointsV = new Vector<Point>();
            pointsV.add(A);
            pointsV.add(B);
            pointsV.add(C);
            pointsV.add(D);
            this.ndgCond.addNewText(pointsV, sb.toString());
            return true;
        }
        return false;
    }

    /**
	 * Method that checks if some position of four given points can
	 * generate polynomial for attached NDG condition. If can, text of 
	 * this NDG condition is populated with appropriate readable
	 * form that describes specific position of these four points
	 * that generates this NDG condition. The text actually corresponds 
	 * to the negation of attached NDG condition.
	 * 
	 * @param pointList		List with four points
	 * @return		        True if some position generates attached NDG condition,
	 * 				        false otherwise
	 * 
	 * @see com.ogprover.prover_protocol.cp.auxiliary.PointsPositionChecker#checkPositions(java.util.Vector)
	 * 
	 */
    public boolean checkPositions(Vector<Point> pointList) {
        if (pointList == null || pointList.size() != 4) return false;
        boolean checkResult = false;
        boolean singleCheckResult = false;
        Point A = pointList.get(0).clone();
        Point B = pointList.get(1).clone();
        Point C = pointList.get(2).clone();
        Point D = pointList.get(3).clone();
        singleCheckResult = this.checkFourCollinearPoints(A, B, C, D);
        checkResult = (checkResult || singleCheckResult);
        singleCheckResult = this.checkFourConcyclicPoints(A, B, C, D);
        checkResult = (checkResult || singleCheckResult);
        singleCheckResult = this.checkEqualSegments(A, B, C, D);
        if (!singleCheckResult) {
            singleCheckResult = this.checkEqualSegments(A, C, B, D);
            if (!singleCheckResult) {
                singleCheckResult = this.checkEqualSegments(A, D, B, C);
            }
        }
        checkResult = (checkResult || singleCheckResult);
        singleCheckResult = this.checkParallelLines(A, B, C, D);
        if (!singleCheckResult) {
            singleCheckResult = this.checkParallelLines(A, C, B, D);
            if (!singleCheckResult) {
                singleCheckResult = this.checkParallelLines(A, D, B, C);
            }
        }
        checkResult = (checkResult || singleCheckResult);
        singleCheckResult = this.checkPerpendicularLines(A, B, C, D);
        if (!singleCheckResult) {
            singleCheckResult = this.checkPerpendicularLines(A, C, B, D);
            if (!singleCheckResult) {
                singleCheckResult = this.checkPerpendicularLines(A, D, B, C);
            }
        }
        checkResult = (checkResult || singleCheckResult);
        singleCheckResult = this.checkHarmonicConjugatePoints(A, B, C, D);
        if (!singleCheckResult) {
            singleCheckResult = this.checkHarmonicConjugatePoints(A, C, B, D);
            if (!singleCheckResult) {
                singleCheckResult = this.checkHarmonicConjugatePoints(A, D, B, C);
            }
        }
        checkResult = (checkResult || singleCheckResult);
        singleCheckResult = this.checkCongruentCollinearSegments(A, B, C, D);
        if (!singleCheckResult) {
            singleCheckResult = this.checkCongruentCollinearSegments(A, C, B, D);
            if (!singleCheckResult) {
                singleCheckResult = this.checkCongruentCollinearSegments(A, D, B, C);
            }
        }
        checkResult = (checkResult || singleCheckResult);
        singleCheckResult = this.checkAngleBisectorPoint(C, A, B, D);
        if (!singleCheckResult) {
            singleCheckResult = this.checkAngleBisectorPoint(A, B, C, D);
            if (!singleCheckResult) {
                singleCheckResult = this.checkAngleBisectorPoint(B, C, A, D);
                if (!singleCheckResult) {
                    singleCheckResult = this.checkAngleBisectorPoint(D, A, B, C);
                    if (!singleCheckResult) {
                        singleCheckResult = this.checkAngleBisectorPoint(A, B, D, C);
                        if (!singleCheckResult) {
                            singleCheckResult = this.checkAngleBisectorPoint(B, D, A, C);
                            if (!singleCheckResult) {
                                singleCheckResult = this.checkAngleBisectorPoint(D, A, C, B);
                                if (!singleCheckResult) {
                                    singleCheckResult = this.checkAngleBisectorPoint(A, C, D, B);
                                    if (!singleCheckResult) {
                                        singleCheckResult = this.checkAngleBisectorPoint(C, D, A, B);
                                        if (!singleCheckResult) {
                                            singleCheckResult = this.checkAngleBisectorPoint(D, B, C, A);
                                            if (!singleCheckResult) {
                                                singleCheckResult = this.checkAngleBisectorPoint(B, C, D, A);
                                                if (!singleCheckResult) {
                                                    singleCheckResult = this.checkAngleBisectorPoint(C, D, B, A);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        checkResult = (checkResult || singleCheckResult);
        singleCheckResult = this.checkTwoPointsOnCircle(A, B, C, D);
        if (!singleCheckResult) {
            singleCheckResult = this.checkTwoPointsOnCircle(B, A, C, D);
            if (!singleCheckResult) {
                singleCheckResult = this.checkTwoPointsOnCircle(A, C, B, D);
                if (!singleCheckResult) {
                    singleCheckResult = this.checkTwoPointsOnCircle(C, A, B, D);
                    if (!singleCheckResult) {
                        singleCheckResult = this.checkTwoPointsOnCircle(A, D, B, C);
                        if (!singleCheckResult) {
                            singleCheckResult = this.checkTwoPointsOnCircle(D, A, B, C);
                            if (!singleCheckResult) {
                                singleCheckResult = this.checkTwoPointsOnCircle(B, C, A, D);
                                if (!singleCheckResult) {
                                    singleCheckResult = this.checkTwoPointsOnCircle(C, B, D, A);
                                    if (!singleCheckResult) {
                                        singleCheckResult = this.checkTwoPointsOnCircle(B, D, A, C);
                                        if (!singleCheckResult) {
                                            singleCheckResult = this.checkTwoPointsOnCircle(D, B, A, C);
                                            if (!singleCheckResult) {
                                                singleCheckResult = this.checkTwoPointsOnCircle(C, D, A, B);
                                                if (!singleCheckResult) {
                                                    singleCheckResult = this.checkTwoPointsOnCircle(D, C, A, B);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        checkResult = (checkResult || singleCheckResult);
        singleCheckResult = this.checkPointOnCircle(A, B, C, D);
        if (!singleCheckResult) {
            singleCheckResult = this.checkPointOnCircle(A, B, D, C);
            if (!singleCheckResult) {
                singleCheckResult = this.checkPointOnCircle(A, C, D, B);
                if (!singleCheckResult) {
                    singleCheckResult = this.checkPointOnCircle(B, A, C, D);
                    if (!singleCheckResult) {
                        singleCheckResult = this.checkPointOnCircle(B, A, D, C);
                        if (!singleCheckResult) {
                            singleCheckResult = this.checkPointOnCircle(B, C, D, A);
                            if (!singleCheckResult) {
                                singleCheckResult = this.checkPointOnCircle(C, A, B, D);
                                if (!singleCheckResult) {
                                    singleCheckResult = this.checkPointOnCircle(C, A, D, B);
                                    if (!singleCheckResult) {
                                        singleCheckResult = this.checkPointOnCircle(C, B, D, A);
                                        if (!singleCheckResult) {
                                            singleCheckResult = this.checkPointOnCircle(D, A, B, C);
                                            if (!singleCheckResult) {
                                                singleCheckResult = this.checkPointOnCircle(D, A, C, B);
                                                if (!singleCheckResult) {
                                                    singleCheckResult = this.checkPointOnCircle(D, B, C, A);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        checkResult = (checkResult || singleCheckResult);
        singleCheckResult = this.checkInversePoints(A, B, C, D);
        if (!singleCheckResult) {
            singleCheckResult = this.checkInversePoints(B, A, C, D);
            if (!singleCheckResult) {
                singleCheckResult = this.checkInversePoints(A, C, B, D);
                if (!singleCheckResult) {
                    singleCheckResult = this.checkInversePoints(C, A, B, D);
                    if (!singleCheckResult) {
                        singleCheckResult = this.checkInversePoints(A, D, B, C);
                        if (!singleCheckResult) {
                            singleCheckResult = this.checkInversePoints(D, A, B, C);
                            if (!singleCheckResult) {
                                singleCheckResult = this.checkInversePoints(B, C, A, D);
                                if (!singleCheckResult) {
                                    singleCheckResult = this.checkInversePoints(C, B, A, D);
                                    if (!singleCheckResult) {
                                        singleCheckResult = this.checkInversePoints(B, D, A, C);
                                        if (!singleCheckResult) {
                                            singleCheckResult = this.checkInversePoints(D, B, A, C);
                                            if (!singleCheckResult) {
                                                singleCheckResult = this.checkInversePoints(C, D, A, B);
                                                if (!singleCheckResult) {
                                                    singleCheckResult = this.checkInversePoints(D, C, A, B);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        checkResult = (checkResult || singleCheckResult);
        return checkResult;
    }
}
