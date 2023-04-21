package herschel.phs.server.interaction.hifi;

import herschel.share.util.DynamicArray;
import java.util.*;

/**
 * Special case of {@link CusFunction} to cover discontinuous parameter 
 * domains.
 * @author  Albrecht de Jonge
 * @version $Revision: 1.15 $ $Date: 2010/02/22 16:04:02 $ 
 */
public abstract class OtfCusFunction extends CusFunction {

    /** Convenience to create the function and specify the variable and fixed parameters in one go.
     * Amounts to calling the other constructor followed by calls 
     * to {@link #setFixPars} and {@link #setVarPars}  	
     * @param cc The CusCaller for which a function has to be created.
     * @param fixPars passed to <code>setFixPars()</code>
     * @param varPars passed to <code>setVarPars()</code>
     */
    public OtfCusFunction(CusCaller cc, Map fixPars, List varPars) throws SpotFail {
        this(cc);
        setFixPars(fixPars);
        setVarPars(varPars);
    }

    /** Contruct the OtfCusFunction from a CusCaller.
     * @param cc The CusCaller for which a function has to be created.
     */
    public OtfCusFunction(CusCaller cc) throws SpotFail {
        super(cc);
    }

    private static final String SPECIAL = "n_linesperscan";

    /** Mapping function argument to CUS values for n_linesperscan.
     * Index is function argument value,
     * value is the n_linesperscan value corresponding to index
     */
    private List<Long> _nlinesperscanTable;

    private double nyquistStep(String bandName, double loFreqGHz) {
        try {
            CusCaller reader = new CusCaller("CalibrationReader", _cus.getCusDefinition().getCommandSet());
            Map calPars = new HashMap();
            calPars.put("topicname", "beam");
            calPars.put("objectnames_0", "nyquist");
            calPars.put("band", bandName);
            calPars.put("lo_freq", loFreqGHz * 1000);
            Object[] calResult = null;
            calResult = reader.evaluate(calPars);
            _log.log(java.util.logging.Level.INFO, "{0} {1} result {2}", new Object[] { reader, calPars, calResult[0] });
            return (Double) calResult[0];
        } catch (Exception ce) {
            throw new IllegalArgumentException("Cannot obtain nyquist step for band " + bandName + " at " + loFreqGHz + " GHz ", ce);
        }
    }

    private long compute_nlines() throws SpotFail {
        double loFreqGHz = (Double) fixPar("fe_lof_0");
        double stepSizeDegrees = (Double) fixPar("flyCrossStep") / 3600.0;
        double ySizeDegrees = (Double) fixPar("flyY") / 60.0;
        if (((String) fixPar("modeName")).equals("freq")) {
            loFreqGHz = (Double) fixPar("lo_freq1");
        }
        if ((Boolean) fixPar("flyNyquistSel")) {
            String bandName = (String) fixPar("band");
            stepSizeDegrees = nyquistStep(bandName, loFreqGHz);
            _log.info("nyquist step size " + stepSizeDegrees);
        }
        long result = (long) Math.ceil(ySizeDegrees / stepSizeDegrees);
        _log.info("step size " + stepSizeDegrees + " and  ySize=" + ySizeDegrees + " gives " + result + " steps");
        return result;
    }

    private Object fixPar(String name) throws SpotFail {
        Object result = _allPars.get(name);
        if (result == null) throw new SpotFail("Missing parameter " + name);
        return result;
    }

    public void setVarPars(List varPars) throws SpotFail {
        super.setVarPars(varPars);
        if (_allPars == null) {
            throw new IllegalStateException("no allpars");
        }
        if (varPars.contains(SPECIAL)) {
            Object[] range = _cus.parameterRange(SPECIAL);
            _log.info(SPECIAL + " range " + Arrays.asList(range));
            long nlines = compute_nlines();
            _log.info(" nlines is " + nlines);
            _nlinesperscanTable = new ArrayList<Long>();
            for (long i = (Long) range[0]; i <= (Long) range[1]; i++) {
                if (nlines % i == 0) {
                    _nlinesperscanTable.add(i);
                }
            }
            _log.info(SPECIAL + " table is " + _nlinesperscanTable);
        }
    }

    @Override
    public Object fun2Cus(String name, double x) throws SpotFail {
        if (name.equals(SPECIAL)) {
            try {
                Object result;
                result = _nlinesperscanTable.get(Math.round((float) x));
                return result;
            } catch (IndexOutOfBoundsException e) {
                _log.fine("Func arg value " + x + " cannot be mapped back to " + name);
                return new Long(-1);
            }
        }
        return super.fun2Cus(name, x);
    }

    public double cus2Fun(String name, Object x) throws SpotFail {
        if (name.equals(SPECIAL)) {
            int i = _nlinesperscanTable.indexOf(x);
            if (i < 0) {
                _log.info(name + " cannot have value " + x);
            }
            return i;
        }
        return super.cus2Fun(name, x);
    }

    @Override
    protected double[] getRange(String name) {
        if (name.equals(SPECIAL)) {
            return new double[] { 0, _nlinesperscanTable.size() - 1 };
        }
        return super.getRange(name);
    }
}
