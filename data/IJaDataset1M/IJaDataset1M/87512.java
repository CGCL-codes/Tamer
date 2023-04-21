package de.jmulti.proc;

import com.jstatcom.engine.gauss.GaussLoadTypes;
import com.jstatcom.model.JSCData;
import com.jstatcom.model.JSCInt;
import com.jstatcom.model.JSCNArray;
import com.jstatcom.model.JSCNumber;
import com.jstatcom.model.JSCSArray;
import com.jstatcom.model.JSCString;
import com.jstatcom.model.JSCTypeDef;
import com.jstatcom.model.JSCTypes;
import com.jstatcom.model.SymbolTable;
import com.jstatcom.ts.TS;
import com.jstatcom.ts.TSDate;
import com.jstatcom.ts.TSDateRange;
import com.jstatcom.ts.TSHolder;
import com.jstatcom.util.FArg;

public class ARIMAForecastCall extends GaussPCall {

    private int p = 0;

    private int q = 0;

    private int d = 0;

    private String yName = null;

    private JSCNArray y = null;

    private JSCNArray b = null;

    private JSCNArray resids = null;

    private JSCNArray forecast = null;

    private JSCNArray forecast_stderr = null;

    private SymbolTable local = null;

    private JSCNArray dets;

    private TSDateRange range;

    private double ciLevel = 0.95;

    private int horizon = 1;

    private boolean isPlot = false;

    private TSDate startPlot;

    public static final JSCTypeDef FORECAST = new JSCTypeDef("FORECAST", JSCTypes.NARRAY, "3xh matrix: point forecast | lower CI |upper CI");

    public static final JSCTypeDef FORECAST_STDERR = new JSCTypeDef("FORECAST_STDERR", JSCTypes.NARRAY, "h x 1 array of forecast std. errors");

    public ARIMAForecastCall(int p, int q, int d, JSCNArray y, JSCNArray resids, JSCNArray dets, JSCNArray b, String yName, double ciLevel, int horizon, TSDateRange range, TSDate startPlot, boolean isPlot, SymbolTable local) {
        setName("ARIMA Forecast");
        this.p = p;
        this.q = q;
        this.d = d;
        this.y = y;
        this.b = b;
        this.yName = yName;
        this.local = local;
        this.dets = dets;
        this.range = range;
        this.ciLevel = ciLevel;
        this.horizon = horizon;
        this.resids = resids;
        this.isPlot = isPlot;
        this.startPlot = startPlot;
    }

    @Override
    protected void runCode() {
        if (local != null) {
            local.get(FORECAST).clear();
            local.get(FORECAST_STDERR).clear();
        }
        forecast = (JSCNArray) FORECAST.getInstance();
        forecast_stderr = (JSCNArray) FORECAST_STDERR.getInstance();
        JSCData[] rtns = new JSCData[] { forecast, forecast_stderr };
        engine().load("pgraph, tools, plot, arima, uni, var", GaussLoadTypes.LIB);
        JSCData[] inArgs = new JSCData[] { b, y, new JSCInt("p", p), new JSCInt("d", d), new JSCInt("q", q), dets, resids, new JSCNumber("ciLevel", ciLevel), new JSCInt("horizon", horizon) };
        engine().call("arimaForecast_uni", inArgs, rtns);
        if (local != null) {
            local.get(FORECAST).setJSCData(forecast);
            local.get(FORECAST_STDERR).setJSCData(forecast_stderr);
        }
        if (!isPlot) return;
        String title = "Forecast of " + yName + " (CI " + ciLevel * 100 + "%)";
        setGlobalPgraphSettings();
        TS origTS = TSHolder.getInstance().getTS(yName);
        JSCNArray yOrig = new JSCNArray("yOrig", origTS.values());
        TSDate start = origTS.range().lowerBound();
        int T2 = start.compareTo(range.upperBound()) + 1;
        int T1 = start.compareTo(startPlot) + 1;
        engine().call("plot_forecast", new JSCData[] { yOrig, forecast, new JSCNumber("T1", T1), new JSCNumber("T2", T2), new JSCNArray("selForec", 1), new JSCSArray("ny", new String[] { yName }), new JSCString("tit", title), new JSCNumber("beg", startPlot.doubleValue()), new JSCInt("period", startPlot.subPeriodicity()) }, null);
    }

    @Override
    protected void finalCode() {
        TSDateRange r = new TSDateRange(range.upperBound().addPeriods(1), range.upperBound().addPeriods(horizon));
        output.append("\nforecasted variable: " + yName + " (in levels)\n");
        output.append(r.format("forecast range:", 21) + "\n\n");
        output.append(FArg.sprintf("%-14s %-20s %-20s %-20s %-20s\n", "time", "lower CI", "forecast", "upper CI", "std. err"));
        String[] times = r.timeAxisStringArray();
        for (int i = 0; i < forecast.cols(); i++) output.append(FArg.sprintf("%-14s %- 20.4f %- 20.4f %- 20.4f %- 20.4f\n", times[i], forecast.doubleAt(1, i), forecast.doubleAt(0, i), forecast.doubleAt(2, i), forecast_stderr.doubleAt(i, 0)));
    }
}
