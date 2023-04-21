package org.zamia.instgraph.sim.ref;

import org.zamia.SourceLocation;
import org.zamia.ZamiaException;
import org.zamia.instgraph.IGStaticValue;
import java.math.BigInteger;

/**
 * @author Guenter Bartsch
 */
public class IGSignalChangeRequest extends IGSimRequest {

    private BigInteger fTime;

    private IGStaticValue fValue;

    private IGSignalDriver fSignalDriver;

    private SourceLocation fLocation;

    public IGSignalChangeRequest(IGSimProcess aProcess, BigInteger aTime, IGStaticValue aValue, IGSignalDriver aSignalDriver, SourceLocation aLocation) {
        super(aProcess);
        fTime = aTime;
        fValue = aValue;
        fSignalDriver = aSignalDriver;
        fLocation = aLocation;
    }

    @Override
    public void execute(IGSimRef aSim) throws ZamiaException {
        if (isCanceled()) {
            return;
        }
        fSignalDriver.setNextValue(fValue, fLocation);
    }

    public BigInteger getTime() {
        return fTime;
    }

    public IGSignalDriver getDriver() {
        return fSignalDriver;
    }

    @Override
    public String toString() {
        return "@ " + fTime + ": " + fSignalDriver + "<=" + fValue;
    }
}
