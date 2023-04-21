package org.polepos.framework;

import java.lang.reflect.*;
import org.polepos.*;

/**
 * an implementation of a circuit for a team
 */
public abstract class DriverBase extends Driver implements Cloneable {

    private Car mCar;

    private TurnSetup mSetup;

    private long _checkSum;

    private int _bulkId;

    private int _testId;

    private int _objectCount;

    private int _commitInterval;

    protected final Car car() {
        return mCar;
    }

    /**
	 * take a seat in a car.
	 */
    @Override
    public void takeSeatIn(Car car, TurnSetup setup) throws CarMotorFailureException {
        mCar = car;
        mSetup = setup;
        _checkSum = 0;
    }

    /**
	 * Called just before one of the specific benchmark calls are issued.
	 * Normally opens the database.
	 */
    @Override
    public abstract void prepare() throws CarMotorFailureException;

    /**
     * Called after the lap so that the driver can clean up any files it
     * created and close any resources it opened. 
     */
    @Override
    public abstract void backToPit();

    protected TurnSetup setup() {
        return mSetup;
    }

    public void addToCheckSum(CheckSummable checkSummable) {
        addToCheckSum(checkSummable.checkSum());
    }

    /**
     * Collecting a checksum to make sure every team does a complete job  
     */
    public synchronized void addToCheckSum(long l) {
        _checkSum += l;
    }

    @Override
    public long checkSum() {
        return _checkSum;
    }

    public DriverBase clone() {
        try {
            return (DriverBase) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void circuitCompleted() {
    }

    public Runnable prepareLap(final Lap lap) {
        return new Runnable() {

            private final Method method = prepareMethod();

            @Override
            public void run() {
                if (method == null) {
                    return;
                }
                try {
                    method.invoke(DriverBase.this, (Object[]) null);
                } catch (Exception e) {
                    if (Settings.DEBUG) {
                        throw new RuntimeException(e);
                    }
                    e.printStackTrace();
                }
            }

            private Method prepareMethod() {
                try {
                    return DriverBase.this.getClass().getDeclaredMethod(lap.name(), (Class[]) null);
                } catch (Exception e) {
                    if (Settings.DEBUG) {
                        throw new RuntimeException(e);
                    }
                    e.printStackTrace();
                }
                return null;
            }
        };
    }

    @Override
    public boolean canRunLap(Lap lap) {
        return true;
    }

    public boolean supportsConcurrency() {
        return true;
    }

    public void copyStateFrom(DriverBase masterDriver) {
    }

    public void bulkId(int id) {
        _bulkId = id;
    }

    protected void initializeTestId(int count) {
        _objectCount = count;
        _testId = _bulkId * count;
        _commitInterval = setup().getCommitInterval();
    }

    protected void initializeTestIdD(int count) {
        _objectCount = count;
        _testId = _bulkId * count;
        _commitInterval = setup().getCommitInterval();
    }

    protected int nextTestId() {
        _objectCount--;
        if (_objectCount < 0) {
            outOfObjectCount();
        }
        return ++_testId;
    }

    private void outOfObjectCount() {
        throw new IllegalStateException(" Out of _objectCount. Did you call initializeTestId ?");
    }

    protected int selectCount() {
        return setup().getSelectCount();
    }

    protected int objectCount() {
        return setup().getObjectCount();
    }

    protected int updateCount() {
        return setup().getUpdateCount();
    }

    protected int depth() {
        return setup().getDepth();
    }

    protected int reuse() {
        return setup().getReuse();
    }

    protected boolean doCommit() {
        if (_objectCount == 0) {
            return true;
        }
        if (_objectCount < 0) {
            outOfObjectCount();
        }
        return (_objectCount % _commitInterval) == 0;
    }

    protected boolean hasMoreTestIds() {
        return _objectCount > 0;
    }
}
