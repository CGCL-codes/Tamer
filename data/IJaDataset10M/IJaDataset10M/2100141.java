package shag.demo;

/**
 * Test bean for some of the demos in this package.
 */
public class DemoBean {

    public DemoBean(String dummy, boolean bozo, int moron) {
        _dummy = dummy;
        _bozo = bozo;
        _moron = moron;
    }

    public String getDummy() {
        return (_dummy);
    }

    public boolean isBozo() {
        return (_bozo);
    }

    public int getMoron() {
        return (_moron);
    }

    public boolean isOne() {
        return (_moron == 1);
    }

    public boolean isTwo() {
        return (_moron == 2);
    }

    public boolean isThree() {
        return (_moron == 3);
    }

    public void setDummy(String dummy) {
        _dummy = dummy;
    }

    public void setBozo(boolean bozo) {
        _bozo = bozo;
    }

    public void setMoron(int moron) {
        _moron = moron;
    }

    public void setOne(boolean one) {
        if (one) _moron = 1; else _moron = 0;
    }

    public void setTwo(boolean two) {
        if (two) _moron = 2; else _moron = 0;
    }

    public void setThree(boolean three) {
        if (three) _moron = 3; else _moron = 0;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return getDummy();
    }

    private String _dummy;

    private boolean _bozo;

    private int _moron;
}
