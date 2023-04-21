package kursach2.statistic;

import java.lang.reflect.InvocationTargetException;

/**
 * @author Vsevolod
 * 
 */
public abstract class AParameter implements IParameter {

    protected java.lang.reflect.Method getMethod, setMethod;

    protected Object Parent;

    protected Object Obj;

    protected String name;

    protected double Start, End, Step, Current;

    @Override
    public boolean end() {
        if (Current > End) return true;
        return false;
    }

    @Override
    public boolean freshly() {
        set(Start);
        return true;
    }

    protected static int CountTests;

    @Override
    public int getCountTests() {
        return CountTests;
    }

    @Override
    public Object currentValue() {
        Object rez = null;
        try {
            rez = getMethod.invoke(Parent, Obj);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return rez;
    }

    @Override
    public boolean nextStep() {
        Current += Step;
        if (Current > End) return false;
        try {
            setMethod.invoke(Parent, Current);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public Object get() {
        return Current;
    }

    @Override
    public void set(Object s) {
        Double g;
        if (s instanceof Double) {
            g = (Double) s;
            if (g.compareTo(Start) >= 0 && g.compareTo(End) <= 0) {
                Current = g;
                try {
                    setMethod.invoke(Parent, (int) Current);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                throw new java.lang.IllegalArgumentException(new ClassCastException());
            }
        }
    }

    @Override
    public void setStart(double newstart) {
        Start = newstart;
    }

    @Override
    public void setEnd(double newend) {
        End = newend;
    }
}
