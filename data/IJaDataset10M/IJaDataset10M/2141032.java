package am.ik.calc;

public class CalcAdd implements Calc {

    @Override
    public Integer execute(Integer arg1, Integer arg2) {
        return arg1 + arg2;
    }
}
