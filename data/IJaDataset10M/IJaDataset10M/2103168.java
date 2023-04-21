package net.sf.contrail.core.storage.provider;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import net.sf.contrail.core.Identifier;
import net.sf.contrail.core.utils.ContrailAction;
import net.sf.contrail.core.utils.ContrailTask;
import net.sf.contrail.core.utils.ContrailTaskTracker;
import net.sf.contrail.core.utils.IResult;
import net.sf.contrail.core.utils.Logging;
import net.sf.contrail.core.utils.SignalHandler;
import net.sf.contrail.core.utils.Signals;
import net.sf.contrail.core.utils.TaskUtils;
import net.sf.contrail.core.utils.ContrailTask.Operation;

/**
 * A convenient base class for implementing providers.
 * This class implements the nasty, complicated concurrency aspect.
 * All that needs to be implemented are the doXXXX methods that just 
 * implement the actual storage functions.   
 * 
 */
public abstract class AbstractStorageProvider implements IStorageProvider {

    private HashSet<Identifier> _identifiersOfInterest = new HashSet<Identifier>();

    private ContrailTaskTracker _tracker = new ContrailTaskTracker();

    public abstract class Session implements IStorageProvider.Session {

        private ContrailTaskTracker.Session _trackerSession = _tracker.beginSession();

        protected abstract boolean exists(Identifier path) throws IOException;

        protected abstract void doStore(Identifier path, byte[] byteArray) throws IOException;

        protected abstract byte[] doFetch(Identifier path) throws IOException;

        protected abstract void doDelete(Identifier path) throws IOException;

        protected abstract void doClose() throws IOException;

        protected abstract void doFlush() throws IOException;

        protected abstract Collection<Identifier> doList(Identifier path) throws IOException;

        @Override
        public void flush() throws IOException {
            try {
                _trackerSession.awaitCompletion(IOException.class);
            } finally {
                doFlush();
            }
        }

        @Override
        public void close() throws IOException {
            try {
                _trackerSession.awaitCompletion(IOException.class);
            } finally {
                try {
                    _trackerSession.close();
                } catch (Throwable t) {
                    Logging.warning(t);
                }
                doClose();
            }
        }

        @Override
        public IResult<Collection<Identifier>> listChildren(final Identifier path) {
            ContrailTask<Collection<Identifier>> action = new ContrailTask<Collection<Identifier>>(path, Operation.LIST) {

                protected void run() throws IOException {
                    setResult(doList(path));
                }
            };
            _trackerSession.submit(action);
            return action;
        }

        @Override
        public IResult<byte[]> fetch(final Identifier path) {
            ContrailTask<byte[]> action = new ContrailTask<byte[]>(path, Operation.READ) {

                protected void run() throws IOException {
                    setResult(doFetch(path));
                }
            };
            _trackerSession.submit(action);
            return action;
        }

        @Override
        public void store(final Identifier identifier, final IResult<byte[]> content) {
            _trackerSession.submit(new ContrailAction(identifier, Operation.WRITE) {

                protected void run() {
                    try {
                        doStore(identifier, content.get());
                    } catch (Throwable t) {
                        setError(t);
                    }
                }
            });
        }

        @Override
        public void delete(final Identifier path) {
            _trackerSession.submit(new ContrailAction(path, Operation.DELETE) {

                protected void run() throws IOException {
                    try {
                        doDelete(path);
                    } finally {
                        boolean signal = false;
                        synchronized (_identifiersOfInterest) {
                            if (_identifiersOfInterest.contains(path)) signal = true;
                        }
                        if (signal) {
                            Signals.signal(path);
                        }
                    }
                }
            });
        }

        /**
		 * Stores the given contents at the given location if the file 
		 * does not already exist.  Otherwise does nothing.
		 * 
		 * @param _waitMillis
		 * 		if the file already exists and parameter is greater than zero   
		 * 		then wait the denoted number of milliseconds for the file to be 
		 * 		deleted.
		 * 
		 * @return 
		 * 		true if the file was created, false if the file already exists 
		 * 		and was not deleted within the wait period.
		 */
        @Override
        public IResult<Boolean> create(final Identifier path_, final IResult<byte[]> source_, final long waitMillis_) {
            ContrailTask<Boolean> task = new ContrailTask<Boolean>(path_, Operation.CREATE) {

                final Identifier _path = path_;

                final IResult<byte[]> _source = source_;

                final long _waitMillis = waitMillis_;

                final long _start = System.currentTimeMillis();

                boolean _locked = false;

                SignalHandler signalHandler = new SignalHandler() {

                    public void signal(Identifier signal) {
                        process();
                    }
                };

                {
                    Signals.register(_path, signalHandler);
                    new ContrailAction() {

                        public void run() {
                            process();
                        }
                    }.submit();
                }

                protected void run() throws Exception {
                }

                protected synchronized void process() {
                    try {
                        if (isDone()) {
                            return;
                        }
                        if (!getLock()) {
                            return;
                        }
                        if (!doCreate()) {
                            return;
                        }
                        terminate();
                    } catch (Throwable t) {
                        setError(t);
                        terminate();
                    } finally {
                        checkTimeOut();
                    }
                }

                private boolean getLock() {
                    if (!_locked) {
                        synchronized (_identifiersOfInterest) {
                            if (!_identifiersOfInterest.contains(_path)) {
                                _identifiersOfInterest.add(_path);
                                _locked = true;
                            }
                        }
                    }
                    return _locked;
                }

                private void terminate() {
                    if (getResult() == null) setResult(false);
                    Signals.unregister(path_, signalHandler);
                    releaseLock();
                    done();
                }

                private void checkTimeOut() {
                    long millisRemaining = _waitMillis - (System.currentTimeMillis() - _start);
                    if (millisRemaining <= 0) {
                        terminate();
                    }
                }

                private boolean doCreate() throws IOException {
                    try {
                        if (!exists(_path)) {
                            doStore(_path, _source.get());
                            setResult(true);
                            return true;
                        }
                    } catch (Throwable t) {
                        if (!isCancelled()) TaskUtils.throwSomething(t, IOException.class);
                    }
                    return false;
                }

                private void releaseLock() {
                    if (_locked) {
                        synchronized (_identifiersOfInterest) {
                            _identifiersOfInterest.remove(_path);
                        }
                        Signals.signal(_path);
                    }
                }
            };
            return task;
        }
    }
}
