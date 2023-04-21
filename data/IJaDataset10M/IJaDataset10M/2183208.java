package ssii2009.mips.lib;

import xdevs.kernel.modeling.Atomic;
import xdevs.kernel.modeling.Port;

/**
 *
 * @author Jose Roldan Ramirez
 * @author José L. Risco Martín
 */
public class RegisterMdr extends Atomic {

    public static final String inClkName = "clk";

    public static final String inRegWriteName = "RegWrite";

    public static final String inInName = "in";

    public static final String outOutName = "out";

    protected Port<Integer> clk = new Port<Integer>(RegisterMdr.inClkName);

    protected Port<Integer> regWrite = new Port<Integer>(RegisterMdr.inRegWriteName);

    protected Port<Object> in = new Port<Object>(RegisterMdr.inInName);

    protected Port<Integer> out = new Port<Integer>(RegisterMdr.outOutName);

    protected Double delayRead;

    protected Double delayWrite;

    protected Integer valueAtClk;

    protected Integer valueAtRegWrite;

    protected Object valueAtIn;

    protected Integer valueAtOut;

    public RegisterMdr(String name, Double delayRead, Double delayWrite) {
        super(name);
        super.addInport(clk);
        super.addInport(regWrite);
        super.addInport(in);
        super.addOutport(out);
        this.delayRead = delayRead;
        this.delayWrite = delayWrite;
        valueAtClk = null;
        valueAtIn = null;
        valueAtOut = 0;
        super.holdIn("Read", delayRead);
    }

    public RegisterMdr(String name) {
        this(name, 0.0, 0.0);
    }

    @Override
    public void deltint() {
        super.passivate();
    }

    @Override
    public void deltext(double e) {
        super.resume(e);
        if (!in.isEmpty()) {
            valueAtIn = in.getValue();
        }
        Integer tempValueAtRegWrite = regWrite.getValue();
        if (tempValueAtRegWrite != null) {
            valueAtRegWrite = tempValueAtRegWrite;
        }
        Integer tempValueAtClk = clk.getValue();
        if (tempValueAtClk != null) {
            if (valueAtRegWrite != null && valueAtRegWrite == 1 && valueAtClk != null && valueAtClk == 1 && tempValueAtClk == 0) {
                if (valueAtIn != null) {
                    valueAtOut = (Integer) valueAtIn;
                }
                super.holdIn("Write", delayWrite);
            }
            valueAtClk = tempValueAtClk;
        }
    }

    @Override
    public void lambda() {
        if (valueAtOut != null) {
            out.setValue(valueAtOut);
        }
    }
}
