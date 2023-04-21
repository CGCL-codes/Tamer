package org.modyna.modyna.simulation.integration;

import org.modyna.modyna.simulation.Sample;
import org.jfree.data.XYSeries;
import org.jfree.data.XYSeriesCollection;

/**
 * The integrator class combines methods that realize a Runge-Kutta integration
 * of ordinary differential equations (ODEs), that are passed in to the driving
 * method as an object implementing the Integratable interface. The class
 * returns an XYSeriesCollection as an output that contains the final result as
 * well as intermediate results of the integrating step. The algorithm is taken
 * from the book numerical recipes in C. And has been adopted to Java and to the
 * OO paradigm. The algorithm is based on three method. A driver method
 * (visible) that communicates with the outside world and which drives the
 * integration through the incremental steps. An adaptive stepper routine, which
 * chooses the optimal step length for the actual step and obeys the accuracy
 * constraint. The stepper routine is also responsible for proposing a trial
 * step length for the next step. Finally there is the numerical step routine
 * itself, which promotes the function through the given increment h in time.
 * The numerical step routine also calculates an approximation for the numerical
 * error, which is passed back to the adaptive stepper in order to adjust the
 * step length. The numerical step routine is based on the Cash Karp Runge Kutta
 * scheme. (a 4th order RK-scheme)
 * 
 * @author Rupert
 */
public class Integrator {

    static double a2 = 0.2, a3 = 0.3, a4 = 0.6, a5 = 1.0, a6 = 0.875, b21 = 0.2, b31 = 3.0 / 40.0, b32 = 9.0 / 40.0, b41 = 0.3, b42 = -0.9, b43 = 1.2, b51 = -11.0 / 54.0, b52 = 2.5, b53 = -70.0 / 27.0, b54 = 35.0 / 27.0, b61 = 1631.0 / 55296.0, b62 = 175.0 / 512.0, b63 = 575.0 / 13824.0, b64 = 44275.0 / 110592.0, b65 = 253.0 / 4096.0, c1 = 37.0 / 378.0, c3 = 250.0 / 621.0, c4 = 125.0 / 594.0, c6 = 512.0 / 1771.0, dc5 = -277.00 / 14336.0;

    static int MAXSTP = 20000;

    static double SAFETY = 0.9, TINY = 1.0e-9;

    /** epsilon as the target for accuracy */
    private double _eps;

    /**
	 * The step size that has been used to advance the function within the given
	 * accuracy (used to increment time)
	 */
    private double _hDid;

    /** hmin as the minimum allowed stepsize (can be zero) */
    private double _hmin;

    /**
	 * Initial step size for the next step as proposed by the adaptive stepper
	 * and used in the driver routine
	 */
    private double _hNextTry;

    /**
	 * number of steps for which the initial gues for the stepsize h has been
	 * too large
	 */
    private int _nBad;

    /**
	 * On output nok and nbad are the number of good and bad (but retried and
	 * fixed) steps taken
	 */
    private int _nOk;

    /** The Time in the next step */
    private double _tStartForNextStep;

    /**
	 * vector of the approximated numerical errors for the actual step (all
	 * ODEs)
	 */
    private DoubleVector _yErr;

    double dc1 = c1 - 2825.0 / 27648.0, dc3 = c3 - 18575.0 / 48384.0, dc4 = c4 - 13525.0 / 55296.0, dc6 = c6 - 0.25;

    /**
	 * Creates a new instance of Integrator Sets defaults
	 */
    public Integrator() {
        _eps = 1.0e-4;
        _hmin = 0.0;
        _yErr = new DoubleVector();
    }

    /**
	 * Stops integration and throws exception if the choosen stepsize is smaller
	 * than the threshold hmin, because in this case the function can not be
	 * integrated numerically due to limited machine accuracy
	 * 
	 * @param hIn
	 *            automatically choosen stepsize at the beginning of time step
	 * @throws NumericsException
	 *             signals stepsize underflow
	 */
    private void checkForUnderflowInStepSize(double hIn) throws NumericsException {
        if (Math.abs(hIn) <= _hmin) throw new NumericsException("Step size too small in odeint");
        double tnew = _tStartForNextStep + hIn;
        if (tnew == _tStartForNextStep) throw new NumericsException("stepsize underflow in rkqs");
    }

    /**
	 * Decreases last time step so that the integration ends exactly at the end
	 * of the integration interval
	 * 
	 * @param h
	 *            automatically choosen step size
	 * @param t
	 *            start value
	 * @param tStart
	 *            start of integration interval is needed for backward in time
	 *            integrations
	 * @param tEnd
	 *            end of integration interval
	 * @return New step size which fits exactly to the integration interval
	 */
    private double correctOvershooting(double h, double t, double tStart, double tEnd) {
        double correctedH = h;
        double advancedTime = t + h;
        if ((advancedTime - tEnd) * (tEnd - tStart) > 0.0) correctedH = tEnd - t;
        return correctedH;
    }

    /**
	 * Decrease step size in the course of the integration if the estimated
	 * accuracy is too small (decision in caller)
	 * 
	 * @param hIn
	 *            automatically choosen step size
	 * @param accuracy
	 *            estimated accuracy
	 * @return decreased step size
	 * @throws NumericsException
	 *             signals step size underflow
	 */
    private double decreaseH(double hIn, double accuracy) throws NumericsException {
        final double PSHRNK = -0.25;
        double decreasedH = SAFETY * hIn * Math.pow(accuracy, PSHRNK);
        decreasedH = (decreasedH >= 0.0 ? Math.max(decreasedH, 0.1 * hIn) : Math.min(decreasedH, 0.1 * hIn));
        checkForUnderflowInStepSize(decreasedH);
        return decreasedH;
    }

    /**
	 * Scaling used to monitor accuracy. This general-purpose choice can be
	 * modified if need be.
	 */
    private DoubleVector determineYScale(DoubleVector yAtT, DoubleVector dydt, double h) {
        DoubleVector yScale = new DoubleVector();
        for (int i = 0; i < dydt.size(); i++) yScale.add(Math.abs(yAtT.get(i)) + Math.abs(dydt.get(i) * h) + TINY);
        return yScale;
    }

    /**
	 * Runge-Kutta driver with adaptive stepsize control. Integrate starting
	 * values ystart[1..nvar] from x1 to x2 with accuracy eps, storing
	 * intermediate results in global variables. h1 should be set as a guessed
	 * 1rst stepsize and ystart is replaced by values at the end of the
	 * integration interval. derivs is the user-supplied routine for calculating
	 * the right-hand side derivative, while rkqs is the name of the stepper
	 * routine to be used.
	 */
    public Sample driveIntegration(DoubleVector yStart, double tStart, double tEnd, double hInitial, Integratable model) throws NumericsException {
        DoubleVector allQuantities[] = new DoubleVector[1];
        _tStartForNextStep = tStart;
        _hNextTry = hInitial * sign(tEnd - tStart);
        _hDid = 0.0;
        DoubleVector yAtT = yStart.copy();
        DefaultSample collectedData = new DefaultSample(model);
        for (int nstp = 1; nstp <= MAXSTP; nstp++) {
            _tStartForNextStep = _tStartForNextStep + _hDid;
            _hNextTry = correctOvershooting(_hNextTry, _tStartForNextStep, tStart, tEnd);
            if ((_tStartForNextStep - tEnd) * (tEnd - tStart) >= 0.0) {
                collectedData.addQuantityVector(_tStartForNextStep, allQuantities[0]);
                return collectedData;
            }
            DoubleVector dydt = model.calculateDerivatives(yAtT, _tStartForNextStep, allQuantities);
            collectedData.addQuantityVector(_tStartForNextStep, allQuantities[0]);
            double hInitialTry = _hNextTry;
            yAtT = rungeKuttaAdaptiveStepper(yAtT, dydt, model);
            if (_hDid == hInitialTry) ++(_nOk); else ++(_nBad);
        }
        throw new NumericsException("Too many steps in routine odeint");
    }

    /**
	 * DEPRECATED !! XYSeriesCollection still used in testdrivers only !!
	 * Runge-Kutta driver with adaptive stepsize control. Integrate starting
	 * values ystart[1..nvar] from x1 to x2 with accuracy eps, storing
	 * intermediate results in global variables. h1 should be set as a guessed
	 * ?rst stepsize, . and ystart is replaced by values at the end of the
	 * integration interval. derivs is the user-supplied routine for calculating
	 * the right-hand side derivative, while rkqs is the name of the stepper
	 * routine to be used.
	 */
    public XYSeriesCollection driveIntegrationOld(DoubleVector yStart, double tStart, double tEnd, double hInitial, Integratable model) throws NumericsException {
        DoubleVector allQuantities[] = new DoubleVector[1];
        _tStartForNextStep = tStart;
        _hNextTry = hInitial * sign(tEnd - tStart);
        _hDid = 0.0;
        DoubleVector yAtT = yStart.copy();
        XYSeriesCollection collectedData = initializeXYSeriesCollection(model);
        for (int nstp = 1; nstp <= MAXSTP; nstp++) {
            _tStartForNextStep = _tStartForNextStep + _hDid;
            _hNextTry = correctOvershooting(_hNextTry, _tStartForNextStep, tStart, tEnd);
            if ((_tStartForNextStep - tEnd) * (tEnd - tStart) >= 0.0) {
                model.calculateDerivatives(yAtT, _tStartForNextStep, allQuantities);
                storeIntermediateResult(_tStartForNextStep, collectedData, allQuantities[0]);
                return collectedData;
            }
            DoubleVector dydt = model.calculateDerivatives(yAtT, _tStartForNextStep, allQuantities);
            storeIntermediateResult(_tStartForNextStep, collectedData, allQuantities[0]);
            double hInitialTry = _hNextTry;
            yAtT = rungeKuttaAdaptiveStepper(yAtT, dydt, model);
            if (_hDid == hInitialTry) ++(_nOk); else ++(_nBad);
        }
        throw new NumericsException("Too many steps in routine odeint");
    }

    /**
	 * returned the scaled accuracy evaluated over all n ODEs
	 * 
	 * @parameter yErr the error of the RK-CK step
	 * @parameter yScale the relative requirements of accuracy for the different
	 *            ODEs as typically provided by the driver
	 * @parameter the overall numeric accuracy
	 */
    private double evaluateAccuracy(DoubleVector yScale, double eps) {
        double errmax = 0.0;
        for (int i = 0; i < yScale.size(); i++) errmax = Math.max(errmax, Math.abs(_yErr.get(i) / yScale.get(i)));
        errmax /= eps;
        return errmax;
    }

    /**
	 * Get target accuracy epsilon
	 * 
	 * @return target accuracy epsilon
	 */
    public double getEpsilon() {
        return _eps;
    }

    /**
	 * Cumulated number of integration steps that were successful with regard to
	 * the number target accuracy
	 * 
	 * @return number of steps
	 */
    public int getNumberOfSuccessfulSteps() {
        return _nOk;
    }

    /**
	 * Cumulated number of integration steps that did not match the target
	 * accuracy and which led to reduced time steps
	 * 
	 * @return number of failed steps
	 */
    public int getNumberOfUnsuccessfulButFixedSteps() {
        return _nBad;
    }

    /**
	 * Truncation error has been o.k., h may be increased. Do not increase more
	 * than a factor of 5.0.
	 * 
	 * @param hIn
	 *            automatically choosen step size
	 * @param accuracy
	 *            estimated accuracy
	 * @return double new increased step size
	 */
    private double increaseH(double hIn, double accuracy) {
        final double PGROW = -0.2;
        double increasedH = (SAFETY * hIn * Math.pow(accuracy, PGROW));
        increasedH = (increasedH >= 0.0 ? Math.min(increasedH, 5.0 * hIn) : Math.max(increasedH, 5.0 * hIn));
        return increasedH;
    }

    /**
	 * DEPRECATED !! Used for testing purposes only
	 * 
	 * @param model
	 * @return
	 */
    private XYSeriesCollection initializeXYSeriesCollection(Integratable model) {
        XYSeriesCollection collectedData = new XYSeriesCollection();
        java.util.Vector modelQuantityNames = model.getQuantityNames();
        for (int i = 0; i < modelQuantityNames.size(); i++) {
            XYSeries xySeries = new XYSeries((String) modelQuantityNames.get(i));
            collectedData.addSeries(xySeries);
        }
        return collectedData;
    }

    /**
	 * Fifth-order Runge-Kutta step with monitoring of local truncation error to
	 * ensure accuracy and adjust stepsize. Input are the dependent variable
	 * vector y[1..n] and its derivative dydx[1..n] at the starting value of the
	 * independent variable x. Also input are the stepsize to be attempted htry,
	 * the required accuracy eps, and the vector yscal[1..n] against which the
	 * error is scaled. On output, y and x are replaced by their new values,
	 * hdid is the stepsize that was actually accomplished, and hnext is the
	 * estimated next stepsize. derivs is the user-supplied routine that
	 * computes the right-hand side derivatives.
	 */
    public DoubleVector rungeKuttaAdaptiveStepper(DoubleVector yAtTStart, DoubleVector dydtAtTStart, Integratable model) throws NumericsException {
        DoubleVector yAtTEnd;
        DoubleVector yScale = determineYScale(yAtTStart, dydtAtTStart, _hNextTry);
        double accuracy = 0.0;
        for (; ; ) {
            yAtTEnd = stepFithOrderCashKarpRungeKutta(yAtTStart, dydtAtTStart, _tStartForNextStep, _hNextTry, model);
            accuracy = evaluateAccuracy(yScale, _eps);
            if (accuracy <= 1.0) break;
            _hNextTry = decreaseH(_hNextTry, accuracy);
        }
        _hDid = _hNextTry;
        _hNextTry = increaseH(_hNextTry, accuracy);
        return yAtTEnd;
    }

    /**
	 * Mathematical signum function
	 * 
	 * @param value
	 *            input value
	 * @return 0 if 0, -1 if negative, +1 if positive
	 */
    int sign(double value) {
        if (value < 0) return -1;
        if (value > 0) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
	 * Given values for n variables yAtT and their derivatives dydtAtT known at
	 * time t, use the fifth-order Cash-Karp Runge-Kutta method to advance the
	 * solution over an interval h and return the incremented variables. Also
	 * return an estimate of the local truncation error in yout using the
	 * embedded fourth-order method. The user supplies the routine
	 * calculateDerivatives, which returns derivatives dydtAtT.
	 */
    public DoubleVector stepFithOrderCashKarpRungeKutta(DoubleVector yAtT, DoubleVector dydtAtT, double t, double h, Integratable model) {
        DoubleVector yVectorAtTPlusDt = new DoubleVector();
        DoubleVector ak2 = model.calculateDerivatives(yAtT.add(dydtAtT.multiply(b21 * h)), t + a2 * h);
        DoubleVector temp = dydtAtT.multiply(b31);
        temp = temp.add(ak2.multiply(b32));
        temp = yAtT.add(temp.multiply(h));
        DoubleVector ak3 = model.calculateDerivatives(temp, t + a3 * h);
        temp = dydtAtT.multiply(b41);
        temp = temp.add(ak2.multiply(b42));
        temp = temp.add(ak3.multiply(b43));
        temp = yAtT.add(temp.multiply(h));
        DoubleVector ak4 = model.calculateDerivatives(temp, t + a4 * h);
        temp = dydtAtT.multiply(b51);
        temp = temp.add(ak2.multiply(b52));
        temp = temp.add(ak3.multiply(b53));
        temp = temp.add(ak4.multiply(b54));
        temp = yAtT.add(temp.multiply(h));
        DoubleVector ak5 = model.calculateDerivatives(temp, t + a5 * h);
        temp = dydtAtT.multiply(b61);
        temp = temp.add(ak2.multiply(b62));
        temp = temp.add(ak3.multiply(b63));
        temp = temp.add(ak4.multiply(b64));
        temp = temp.add(ak5.multiply(b65));
        temp = yAtT.add(temp.multiply(h));
        DoubleVector ak6 = model.calculateDerivatives(temp, t + a6 * h);
        temp = dydtAtT.multiply(c1);
        temp = temp.add(ak3.multiply(c3));
        temp = temp.add(ak4.multiply(c4));
        temp = temp.add(ak6.multiply(c6));
        yVectorAtTPlusDt = yAtT.add(temp.multiply(h));
        temp = dydtAtT.multiply(dc1);
        temp = temp.add(ak3.multiply(dc3));
        temp = temp.add(ak4.multiply(dc4));
        temp = temp.add(ak5.multiply(dc5));
        temp = temp.add(ak6.multiply(dc6));
        _yErr = temp.multiply(h);
        return yVectorAtTPlusDt;
    }

    /**
	 * DEPRECATED !! For testing purposes only
	 * 
	 * @param t
	 * @param collection
	 * @param quantities
	 */
    private void storeIntermediateResult(double t, XYSeriesCollection collection, DoubleVector quantities) {
        for (int i = 0; i < quantities.size(); i++) {
            collection.getSeries(i).add(t, quantities.get(i));
        }
    }
}
