package cn.ekuma.epos.swing.calc;

/**
 *
 * @author Administrator
 */
public class ChangeSignCommand extends AbstractCommand {

    @Override
    public String getCommStyle() {
        return COMM_SIGN;
    }

    @Override
    public boolean isSuportReCalc() {
        return true;
    }

    @Override
    public CalcLogic clone() {
        return new ChangeSignCommand();
    }

    @Override
    public double calc(double inData) {
        return 0 - inData;
    }
}
