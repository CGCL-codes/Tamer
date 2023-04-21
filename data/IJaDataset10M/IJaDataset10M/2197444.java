package org.jquantlib.cashflow;

import java.util.List;
import org.jquantlib.util.DefaultObservable;
import org.jquantlib.util.Observable;
import org.jquantlib.util.Observer;

public abstract class FloatingRateCouponPricer implements Observer, Observable {

    private final DefaultObservable delegatedObservable = new DefaultObservable(this);

    public abstract double swapletPrice();

    public abstract double swapletRate();

    public abstract double capletPrice(double effectiveCap);

    public abstract double capletRate(double effectiveCap);

    public abstract double floorletPrice(double effectiveFloor);

    public abstract double floorletRate(double effectiveFloor);

    public abstract void initialize(FloatingRateCoupon coupon);

    @Override
    public void update() {
        notifyObservers();
    }

    @Override
    public void addObserver(final Observer observer) {
        delegatedObservable.addObserver(observer);
    }

    @Override
    public int countObservers() {
        return delegatedObservable.countObservers();
    }

    @Override
    public void deleteObserver(final Observer observer) {
        delegatedObservable.deleteObserver(observer);
    }

    @Override
    public void deleteObservers() {
        delegatedObservable.deleteObservers();
    }

    @Override
    public List<Observer> getObservers() {
        return delegatedObservable.getObservers();
    }

    @Override
    public void notifyObservers() {
        delegatedObservable.notifyObservers();
    }

    @Override
    public void notifyObservers(final Object arg) {
        delegatedObservable.notifyObservers(arg);
    }
}
