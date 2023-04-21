package pyrasun.eio.helpers;

import pyrasun.eio.EIOWorkerFactory;
import pyrasun.eio.EIOEvent;
import pyrasun.eio.EIOWorker;

/**
 * An abstract factory class for creating standard Socket read/write/process
 * workers.  Mostly automates the bookkeeping required by the EIOWorkerFactory
 * inteface.
 */
public abstract class EIOSocketWorkerFactory implements EIOWorkerFactory {

    private EIOEvent[] supportedTypes = new EIOEvent[] { EIOEvent.READ, EIOEvent.WRITE, EIOEvent.PROCESS };

    public EIOSocketWorkerFactory() {
    }

    public EIOWorker createWorker(EIOEvent type) throws IllegalArgumentException {
        if (type.equals(EIOEvent.READ)) {
            return (createReader());
        } else if (type.equals(EIOEvent.WRITE)) {
            return (createWriter());
        } else if (type.equals(EIOEvent.PROCESS)) {
            return (createProcessor());
        } else {
            throw new IllegalArgumentException("ERROR: This Factory does not support " + type + "' workers");
        }
    }

    public abstract EIOWorker createReader();

    public abstract EIOWorker createWriter();

    public abstract EIOWorker createProcessor();

    public EIOEvent[] supportedWorkerTypes() {
        return (supportedTypes);
    }
}
