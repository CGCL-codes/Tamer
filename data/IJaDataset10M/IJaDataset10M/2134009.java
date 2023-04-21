package net.sf.doolin.jbars.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class DispatchingBarsModel<T> extends AbstractBarsModel<T> {

    protected static class BarObject<T> {

        private String name;

        private T userObject;

        private BigDecimal value;

        public BarObject(String name, BigDecimal value, T userObject) {
            this.name = name;
            this.value = value;
            this.userObject = userObject;
        }

        public String getName() {
            return this.name;
        }

        public T getUserObject() {
            return this.userObject;
        }

        public BigDecimal getValue() {
            return this.value;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setUserObject(T userObject) {
            this.userObject = userObject;
        }

        public void setValue(BigDecimal value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.format("%.4f %s %s", this.value.doubleValue(), this.name, this.userObject);
        }
    }

    private static final int SCALE = 10;

    private List<BarObject<T>> objects = Collections.synchronizedList(new ArrayList<BarObject<T>>());

    private final AtomicBoolean dispatchSuspended = new AtomicBoolean(false);

    public void addBar(String name) {
        addBar(name, null);
    }

    public void addBar(String name, T userObject) {
        BigDecimal value = this.objects.isEmpty() ? BigDecimal.ONE : BigDecimal.ZERO;
        BarObject<T> object = new BarObject<T>(name, value, userObject);
        this.objects.add(object);
        fireBarModelChanged();
    }

    public void clear() {
        this.objects.clear();
        fireBarModelChanged();
    }

    @Override
    public int getBarCount() {
        return this.objects.size();
    }

    @Override
    public String getBarName(int index) {
        if (index >= this.objects.size()) {
            return "";
        }
        return this.objects.get(index).getName();
    }

    @Override
    public BigDecimal getBarValue(int index) {
        if (index >= this.objects.size()) {
            return BigDecimal.ZERO;
        }
        return this.objects.get(index).getValue();
    }

    @Override
    public T getUserObject(int index) {
        if (index >= this.objects.size()) {
            return null;
        }
        return this.objects.get(index).getUserObject();
    }

    public void resumeDispatch() {
        this.dispatchSuspended.set(false);
    }

    @Override
    public void setBarValue(int index, BigDecimal newValue) {
        suspendFire();
        try {
            BarObject<T> object = this.objects.get(index);
            if (this.objects.size() == 1 && !this.dispatchSuspended.get()) {
                object.setValue(BigDecimal.ONE);
            } else {
                BigDecimal oldValue = object.getValue();
                object.setValue(newValue);
                BigDecimal diff = oldValue.subtract(newValue);
                if (!this.dispatchSuspended.get()) {
                    dispatch(diff, index);
                }
            }
        } finally {
            resumeFire();
        }
        fireAllBarValuesChanged();
    }

    public void suspendDispatch() {
        this.dispatchSuspended.set(true);
    }

    protected void checksum() {
        BigDecimal sum = BigDecimal.ZERO;
        for (BarObject<T> object : this.objects) {
            BigDecimal value = object.getValue();
            sum = sum.add(value);
        }
        sum = sum.setScale(5, RoundingMode.HALF_UP);
        if (sum.compareTo(BigDecimal.ONE) != 0) {
            System.err.format("Total sum (%f) must be equal to 1.0%n", sum);
        }
    }

    protected void dispatch(BigDecimal diff, int index) {
        if (diff.compareTo(BigDecimal.ZERO) == 0) {
            return;
        }
        int n = this.objects.size();
        BigDecimal[] diffPerIndex = new BigDecimal[n];
        BigDecimal totalDiffPerIndex = BigDecimal.ZERO;
        for (int i = 0; i < n; i++) {
            if (i != index) {
                BigDecimal thisValue = this.objects.get(i).getValue();
                BigDecimal thisDiff;
                if (diff.signum() > 0) {
                    thisDiff = diff.min(BigDecimal.ONE.subtract(thisValue));
                } else {
                    thisDiff = diff.max(thisValue.negate());
                }
                diffPerIndex[i] = thisDiff;
                totalDiffPerIndex = totalDiffPerIndex.add(thisDiff);
            }
        }
        BigDecimal[] pDiff = new BigDecimal[n];
        for (int i = 0; i < n; i++) {
            if (i != index) {
                pDiff[i] = diffPerIndex[i].divide(totalDiffPerIndex, SCALE, RoundingMode.HALF_UP);
            }
        }
        for (int i = 0; i < n; i++) {
            if (i != index) {
                BarObject<T> object = this.objects.get(i);
                BigDecimal thisValue = object.getValue();
                BigDecimal thisDiff = pDiff[i].multiply(diff);
                thisValue = thisValue.add(thisDiff);
                thisValue = thisValue.setScale(SCALE, RoundingMode.HALF_UP);
                object.setValue(thisValue);
            }
        }
        checksum();
    }
}
