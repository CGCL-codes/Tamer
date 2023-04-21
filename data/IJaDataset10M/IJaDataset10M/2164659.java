package ru.jnano.math.calc.diff.contrl;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class AbstractController implements IControllerRP, Serializable {

    private ArrayList<IControllerRPListener> listeners = new ArrayList<IControllerRPListener>();

    @Override
    public void fireStartResolve() {
        for (IControllerRPListener l : listeners) l.StartResolve();
    }

    @Override
    public void fireFinishResolve() {
        for (IControllerRPListener l : listeners) l.FinishResolve();
    }

    @Override
    public void fireback(int count) {
        for (IControllerRPListener l : listeners) l.back(count);
    }

    @Override
    public void addControllerRPListener(IControllerRPListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeControllerRPListener(IControllerRPListener listener) {
        listeners.remove(listener);
    }
}
