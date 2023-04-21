package de.jmulti.proc;

import com.jstatcom.engine.gauss.GaussLoadTypes;
import com.jstatcom.model.JSCData;
import com.jstatcom.model.JSCInt;
import com.jstatcom.model.JSCNArray;
import com.jstatcom.model.JSCTypeDef;
import com.jstatcom.model.JSCTypes;
import com.jstatcom.model.SymbolTable;
import com.jstatcom.ts.TSDateRange;
import com.jstatcom.util.FArg;

public class ARIMAHanRissPCall extends GaussPCall {

    public static final JSCTypeDef ALL_LAGS_CRIT = new JSCTypeDef("ALL_LAGS_CRIT", JSCTypes.NARRAY, "information criteria for all (pmax+1)^2 lag combinations: p~q~AIC~HQ~SC");

    public static final JSCTypeDef LAGS_OPT = new JSCTypeDef("LAGS_OPT", JSCTypes.NARRAY, "3 rows with optimal lags for AIC, HQ, SC, each row is p~q~AIC~HQ~SC");

    private int h;

    private int d;

    private int pqmax;

    private JSCNArray allResults;

    private JSCNArray optLags;

    private JSCNArray y;

    private JSCNArray det;

    private TSDateRange range;

    private String yName;

    public ARIMAHanRissPCall(int h, int pqmax, int d, JSCNArray y, JSCNArray det, String yName, TSDateRange range) {
        setName("Hannan-Rissanen Model Selection for ARIMA");
        this.h = h;
        this.d = d;
        this.pqmax = pqmax;
        this.y = y;
        this.det = det;
        this.yName = yName;
        this.range = range;
    }

    @Override
    protected void runCode() {
        SymbolTable local = getSymbolTable();
        if (local != null) {
            local.get(ALL_LAGS_CRIT).clear();
            local.get(LAGS_OPT).clear();
        }
        allResults = (JSCNArray) ALL_LAGS_CRIT.getInstance();
        optLags = (JSCNArray) LAGS_OPT.getInstance();
        engine().load("arima, uni", GaussLoadTypes.LIB);
        engine().call("hannan_rissanen_uni", new JSCData[] { y, det, new JSCInt("h", h), new JSCInt("pmax", pqmax), new JSCInt("d", d) }, new JSCData[] { optLags, allResults });
        if (local != null) {
            local.get(ALL_LAGS_CRIT).setJSCData(allResults);
            local.get(LAGS_OPT).setJSCData(optLags);
        }
    }

    @Override
    protected void finalCode() {
        output.append("OPTIMAL LAGS FROM HANNAN-RISSANEN MODEL SELECTION\n");
        output.append("(Hannan & Rissanen, 1982, Biometrika 69)\n\n");
        output.append(FArg.sprintf("%-30s %s \n", "original variable:", yName));
        output.append(FArg.sprintf("%-30s %s \n", "order of differencing (d):", d));
        range = range.addPeriodsToStart(h + pqmax + d);
        output.append(range.format("adjusted sample range:", 31) + "\n\n");
        output.append("optimal lags p, q (searched all combinations where max(p,q) <= " + pqmax + ")\n");
        output.append(FArg.sprintf("%-25s p=%i, q=%i\n", "Akaike Info Criterion:", optLags.intAt(0, 0), optLags.intAt(0, 1)));
        output.append(FArg.sprintf("%-25s p=%i, q=%i\n", "Hannan-Quinn Criterion:", optLags.intAt(1, 0), optLags.intAt(1, 1)));
        output.append(FArg.sprintf("%-25s p=%i, q=%i\n", "Schwarz Criterion:", optLags.intAt(2, 0), optLags.intAt(2, 1)));
    }
}
