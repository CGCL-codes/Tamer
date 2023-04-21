package gov.sns.tools.beam.tests;

import java.io.PrintWriter;
import gov.sns.tools.beam.CorrelationMatrix;
import gov.sns.tools.beam.PhaseMatrix;
import gov.sns.tools.beam.PhaseVector;
import gov.sns.tools.beam.TraceXalUnitConverter;
import gov.sns.tools.beam.Twiss;
import gov.sns.xal.model.probe.EnvelopeProbe;
import gov.sns.xal.model.probe.traj.EnvelopeProbeState;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Tests Twiss parameter conversion.
 * 
 * @author Craig McChesney
 */
public class TestUnitConverter extends TestCase {

    private static final double f = 402.5e+6;

    private static final double ER = 939.3014e+6;

    private static final double W = 2.5e+6;

    private static final double I = 0.025;

    private static final Twiss t3dX = new Twiss(-2.3849, 0.21384, 13.0);

    private static final Twiss t3dY = new Twiss(1.9242, 0.16734, 13.0);

    private static final Twiss t3dZ = new Twiss(-0.1704, 1.23696, 555.695);

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        return new TestSuite(TestUnitConverter.class);
    }

    public void testConvertTwiss() {
        TraceXalUnitConverter conv = TraceXalUnitConverter.newConverter(f, ER, W);
        Twiss xalX = conv.traceToXalTransverse(t3dX);
        Twiss xalY = conv.traceToXalTransverse(t3dY);
        Twiss xalZ = conv.traceToXalLongitudinal(t3dZ);
        PrintWriter pw = new PrintWriter(System.out);
        pw.println("X plane");
        xalX.printOn(pw);
        pw.println();
        pw.println("Y plane");
        xalY.printOn(pw);
        pw.println();
        pw.println("Z plane");
        xalZ.printOn(pw);
        pw.flush();
        PhaseMatrix pm = conv.correlationMatrixFromT3d(t3dX, t3dY, t3dZ);
        pw.println();
        pw.println("correlation matrix");
        pm.print(pw);
        pw.flush();
    }

    public void testOffCenter() {
        TraceXalUnitConverter conv = TraceXalUnitConverter.newConverter(f, ER, W);
        PhaseVector xalCentroid = new PhaseVector(0.001, 0, -0.001, 0, 0.0005, 0);
        PhaseVector centroid = conv.xalToTraceCoordinates(xalCentroid);
        System.out.println("t3d centroid: " + centroid);
        PhaseMatrix pm = conv.correlationMatrixFromT3d(t3dX, t3dY, t3dZ, centroid);
        PrintWriter pw = new PrintWriter(System.out);
        pw.println();
        pw.println("off center correlation matrix");
        pm.print(pw);
        pw.flush();
    }

    public void testProbeEditor() {
        EnvelopeProbe myProbe = new EnvelopeProbe();
        TraceXalUnitConverter uc = TraceXalUnitConverter.newConverter(f, ER, W);
        PhaseVector pv = new PhaseVector(0.001, 0, -0.001, 0, 0.0005, 0);
        Twiss twissX = new Twiss(-2.3849, 0.21384, 13.0);
        Twiss twissY = new Twiss(1.9242, 0.16734, 13.0);
        Twiss twissZ = new Twiss(-0.1704, 1.23696, 555.695);
        CorrelationMatrix cm = uc.correlationMatrixFromT3d(twissX, twissY, twissZ);
        PrintWriter pw = new PrintWriter(System.out);
        pw.println();
        pw.println("test probe editor correlation matrix");
        cm.print(pw);
        pw.flush();
        EnvelopeProbeState envProbeState = (EnvelopeProbeState) myProbe.createProbeState();
        envProbeState.setBeamCurrent(I / 1.e3);
        envProbeState.setCorrelation(cm);
        envProbeState.setBunchFrequency(f / 1.0e6);
        myProbe.applyState(envProbeState);
    }
}
