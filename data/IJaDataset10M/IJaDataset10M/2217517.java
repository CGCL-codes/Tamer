package lang2.parser;

import java.io.OutputStreamWriter;
import lang2.parser.SourceGenerator;
import lang2.vm.Value;
import lang2.vm.op.Function;
import lang2.vm.op.Variable;
import org.gocha.text.IndentStackWriter;

/**
 * @author gocha
 */
public class VRFunctionLog extends VRFunction {

    public VRFunctionLog() {
    }

    public VRFunctionLog(VRFunctionLog src, boolean deep) {
        super(src, deep);
    }

    @Override
    public Value deepClone() {
        return new VRFunctionLog(this, true);
    }

    protected static IndentStackWriter log = null;

    public static void setLog(IndentStackWriter log) {
        VRFunctionLog.log = log;
        sgen = null;
    }

    public static IndentStackWriter log() {
        if (log == null) {
            log = new IndentStackWriter(new OutputStreamWriter(System.out));
        }
        return log;
    }

    private void println(Object x) {
        log().println(x);
    }

    private void println(String x) {
        log().println(x);
    }

    private void println(double x) {
        log().println(x);
    }

    private void println(float x) {
        log().println(x);
    }

    private void println(long x) {
        log().println(x);
    }

    private void println(int x) {
        log().println(x);
    }

    private void println(char x) {
        log().println(x);
    }

    private void println(boolean x) {
        log().println(x);
    }

    private void println() {
        log().println();
    }

    private void flush() {
        log().flush();
    }

    private void template(String template, Object... values) {
        log().template(template, values);
    }

    private void incLevel() {
        log().incLevel();
    }

    private void decLevel() {
        log().decLevel();
    }

    protected static SourceGenerator sgen = null;

    protected static SourceGenerator sourceGenerator() {
        if (sgen == null) {
            sgen = new SourceGenerator(log());
        }
        return sgen;
    }

    @Override
    protected Value createFunction() {
        println("VRFunctionLog.createFunction(){");
        incLevel();
        flush();
        println("Лог");
        incLevel();
        Value res = super.createFunction();
        decLevel();
        println("Результат");
        incLevel();
        sourceGenerator().go(res);
        decLevel();
        println();
        decLevel();
        println("}");
        flush();
        return res;
    }

    @Override
    protected void exchangeVarRef(Value scope, Variable var, ExternalVariables extVars) {
        println("exchangeVarRef(scope,var,extVars){");
        incLevel();
        flush();
        super.exchangeVarRef(scope, var, extVars);
        decLevel();
        println("}");
        flush();
    }
}
