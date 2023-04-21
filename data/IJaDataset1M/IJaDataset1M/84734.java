package com.rbnb.api;

final class StreamPlugInListener extends com.rbnb.api.StreamDHListener {

    /**
     * For Monitor mode, this is the next event to post to the client
     * <p>
     *
     * @author John Wilson
     *
     * @since V2.0
     * @version 11/18/2005
     */
    private Serializable newestEvent = null;

    StreamPlugInListener(StreamParent parentI, Rmap requestI, NotificationFrom sourceI) throws java.lang.InterruptedException {
        super(parentI, requestI, sourceI);
        if (requestI instanceof DataRequest) {
            DataRequest dr = (DataRequest) requestI;
            if (dr.getNrepetitions() != 1) {
                throw new java.lang.IllegalStateException("PlugIns do not yet support requests with repetition " + "(such as for monitor or subscription requests).");
            }
        }
    }

    StreamPlugInListener(StreamParent parentI, DataRequest requestI, PlugInHandler plugInI) throws java.lang.InterruptedException {
        this(parentI, requestI, (NotificationFrom) plugInI);
    }

    public String toString() {
        return new String("StreamPlugInListener:" + super.toString());
    }

    public final synchronized void accept(Serializable eventI, Rmap matchI) throws java.lang.InterruptedException {
        if (eventI == null) {
            return;
        }
        if (true) {
            newestEvent = eventI;
            notifyAll();
        } else {
            try {
                post(eventI);
            } catch (com.rbnb.api.AddressException e) {
                stop();
            } catch (com.rbnb.api.SerializeException e) {
                stop();
            } catch (java.io.IOException e) {
                stop();
            }
        }
    }

    final void createWorking() throws com.rbnb.api.AddressException, com.rbnb.api.SerializeException, java.io.IOException, java.lang.InterruptedException {
        if ((getAnotification() != null) && (getAnotification().size() == 1)) {
            getOriginal().setName(((AwaitNotification) getAnotification().firstElement()).getName());
            Rmap me = getOriginal().getChildAt(0);
            getOriginal().removeChildAt(0);
            while (me.getNchildren() > 0) {
                Rmap child = me.getChildAt(0);
                me.removeChildAt(0);
                if (me.getTrange() != null) {
                    if (child.getTrange() == null) {
                        child.setTrange(me.getTrange());
                    } else {
                        child.setTrange(me.getTrange().add(child.getTrange()));
                    }
                }
                if (child.getFrange() == null) {
                    child.setFrange(me.getFrange());
                }
                getOriginal().addChild(child);
            }
        } else {
            throw new java.lang.IllegalStateException("Software error:\n" + this + " is in a bad state.");
        }
    }

    private boolean isMonitorMode() {
        DataRequest dr = ((getBaseRequest() instanceof DataRequest) ? ((DataRequest) getBaseRequest()) : null);
        if ((dr != null) && (dr.getGapControl())) {
            return true;
        }
        return false;
    }

    public final void post(Serializable serializableI) throws com.rbnb.api.AddressException, com.rbnb.api.SerializeException, java.io.InterruptedIOException, java.io.IOException, java.lang.InterruptedException {
        Serializable serializable;
        Rmap eos = null, me = ((Rmap) getSource()).newInstance(), highest, child;
        me.setParent(null);
        if (serializableI instanceof EndOfStream) {
            eos = (Rmap) serializableI;
            highest = new Rmap();
            if (eos.compareNames(".") != 0) {
                highest.setName(eos.getName());
            }
            eos.setName(null);
            highest.setTrange(eos.getTrange());
            eos.setTrange(null);
            highest.setFrange(eos.getFrange());
            eos.setFrange(null);
            while (eos.getNchildren() > 0) {
                child = eos.getChildAt(0);
                eos.removeChildAt(0);
                highest.addChild(child);
            }
        } else {
            highest = (Rmap) serializableI;
        }
        if (highest != null) {
            me.addChild(highest);
        }
        if (eos != null) {
            eos.addChild(me);
            serializable = eos;
        } else {
            serializable = me;
        }
        if (isMonitorMode()) {
            setWaiting(true);
            serializable = new RSVP(0, serializable);
        }
        super.post(serializable);
    }

    private void processEvent() throws AddressException, java.io.InterruptedIOException, InterruptedException, java.io.IOException, SerializeException {
        DataRequest original = (DataRequest) getOriginal();
        boolean isStreaming = original.getNrepetitions() > 1;
        boolean doRequest = true;
        DataRequest request;
        if (isStreaming) {
            request = (DataRequest) (original.clone());
            request.setRepetitions(1, 0.0);
            if (request.getMode() == DataRequest.FRAMES) {
                request.setMode(DataRequest.CONSOLIDATED);
                request.setReference(DataRequest.NEWEST);
                request.getChildAt(0).setTrange(new TimeRange(0, 0.0));
                request.getChildAt(0).setFrange(null);
            } else {
            }
        } else request = original;
        while (true) {
            if (doRequest) {
                ((PlugInHandler) getSource()).initiateRequest(request, getNBO().getRequestOptions());
                doRequest = false;
            }
            if (getTerminateRequested()) {
                break;
            }
            synchronized (this) {
                if ((!getWaiting()) && (newestEvent != null)) {
                    if (isStreaming) {
                        if (newestEvent instanceof EndOfStream) {
                            newestEvent = ((Rmap) newestEvent).getChildAt(0);
                            ((Rmap) newestEvent).getParent().removeChildAt(0);
                        }
                        TimeRange tr = ((Rmap) newestEvent).summarize().getTrange();
                        if (tr != null) {
                            post(newestEvent);
                            request = (DataRequest) request.duplicate();
                            request.setRepetitions(1, 0.0);
                            request.setMode(DataRequest.CONSOLIDATED);
                            double magicOffset;
                            if (original.getGapControl()) {
                                request.setReference(DataRequest.AFTER);
                                magicOffset = 2e-7;
                            } else {
                                request.setReference(DataRequest.ABSOLUTE);
                                request.setRelationship(original.getIncrement() > 0 ? DataRequest.GREATER : DataRequest.LESS);
                                magicOffset = 0;
                            }
                            request.getChildAt(0).setTrange(new TimeRange(tr.getTime() + magicOffset, 0.0));
                            request.getChildAt(0).setFrange(null);
                        } else {
                            Thread.sleep(100);
                        }
                        doRequest = true;
                    } else {
                        post(newestEvent);
                    }
                    newestEvent = null;
                    continue;
                }
                wait(TimerPeriod.NORMAL_WAIT);
            }
        }
    }

    public final void run() {
        getDoor().setIdentification(toString());
        try {
            if ((getSource() instanceof ClientHandler) && !((ClientHandler) getSource()).allowAccess(getNBO())) {
                post(new EndOfStream(EndOfStream.REASON_EOD));
                setEOS(true);
                return;
            } else {
                processEvent();
            }
        } catch (java.io.InterruptedIOException e) {
        } catch (java.lang.InterruptedException e) {
        } catch (java.lang.Exception e) {
            try {
                getNBO().asynchronousException(e);
            } catch (java.lang.Exception e1) {
            }
            setEOS(true);
        } finally {
            if (getThread() != null) {
                ((ThreadWithLocks) getThread()).clearLocks();
            }
        }
        if (!getTerminateRequested()) {
            try {
                stop();
            } catch (java.lang.Exception e) {
            }
            if (getThread() != null) {
                ((ThreadWithLocks) getThread()).clearLocks();
            }
        }
        setTerminateRequested(false);
        setThread(null);
    }
}
