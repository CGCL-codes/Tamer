package edu.kgi.jarnac;

import java.util.*;
import org.biospice.analyzer.*;
import org.biospice.datatypes.*;
import edu.caltech.sbw.*;
import edu.kgi.optdemo.sbwservices.BiospiceAnalyze;
import edu.kgi.optdemo.datatypes.SbwMatrix;
import edu.kgi.optdemo.SbwAnalyzer;
import edu.kgi.optdemo.SbwConnectionManager;

/**
 * The Jarnac steady-state analyzer.
 * @author Cameron Wellock (cwellock@kgi.edu)
 */
public class JarnacSteady extends SbwAnalyzer implements Analyzer {

    /**
 * Create the analyzer and connect to SBW.
 */
    public JarnacSteady() {
        super();
        if (Sys.OSIsMac() || Sys.OSIsUnix()) {
            System.setProperty("java.net.preferIPv4Stack", "true");
            System.setProperty("sbw.broker.allow-remote-modules", "true");
        }
    }

    public ParameterValueGroup analyze(ParameterValueGroup params) throws AnalysisException {
        Module m;
        Service s;
        BiospiceAnalyze ba;
        String sbml = null;
        SbwMatrix r = null;
        try {
            SbwConnectionManager.connect();
            m = SBW.getModuleInstance("Jarnac");
            s = m.findServiceByName("biospiceAnalyzeSS");
            ba = (BiospiceAnalyze) s.getServiceObject(BiospiceAnalyze.class);
            sbml = readText((Biodata) params.getParameterValue("model").getValue());
            LinkedList sbwParams = new LinkedList();
            sbwParams.add(sbml);
            List rv = ba.analyze(sbwParams);
            r = toSbwMatrix(rv);
            r.data = transpose(r.data);
        } catch (SBWException e) {
            throw new AnalysisException(e.getMessage() + " " + e.getDetailedMessage(), e);
        }
        ParameterValue p = new ParameterValue(JarnacSimFactory.OUT_P, r);
        ParameterValue[] pv = new ParameterValue[] { p };
        return new ParameterValueGroup(pv);
    }

    /**
 * Transpose a matrix (required because Jarnac and BioSPICE organize their
 * results matrices differently).
 * @param d Matrix of double values to transpose.
 * @return Transposed matrix.
 */
    double[][] transpose(double[][] d) {
        double[][] r = new double[d[0].length][d.length];
        for (int i = 0; i < d.length; i++) for (int j = 0; j < d[0].length; j++) r[j][i] = d[i][j];
        return r;
    }

    protected void finalize() throws Throwable {
        SbwConnectionManager.disconnect();
        super.finalize();
    }
}
