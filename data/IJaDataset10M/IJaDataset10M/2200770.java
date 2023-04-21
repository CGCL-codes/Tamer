package remote.motecontrol.client;

import java.util.Observable;
import java.util.Observer;
import remote.protocol.motecontrol.MsgCommand;
import remote.protocol.motecontrol.MsgConfirm;
import remote.protocol.motecontrol.MsgRequest;
import remote.protocol.motecontrol.MsgStatus;

/**
 * @author zept
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SimpleMoteStatus {

    public static final short UNKNOWN = MsgStatus.UNKNOWN;

    public static final short UNAVAILABLE = MsgStatus.UNAVAILABLE;

    public static final short STOPPED = MsgStatus.STOPPED;

    public static final short RUNNING = MsgStatus.RUNNING;

    public static final short PROGRAMMING = MsgStatus.PROGRAMMING;

    public static final short INVALID = 60;

    public static final short REQ_STATUS = 61;

    public static final short REQ_START = 62;

    public static final short REQ_STOP = 63;

    public static final short REQ_RESET = 64;

    public static final short REQ_PROGRAM = 65;

    public static final short REQ_CANCELPROGRAMMING = 66;

    protected SimpleMote mote;

    protected short status = 0;

    protected short lastCommand = -1;

    protected short lastResult = -1;

    protected ExposedObservable eventDispatcher = new ExposedObservable();

    protected class ObserverWrapper implements Observer {

        protected SimpleMoteStatusListener listener;

        protected SimpleMote mote;

        public ObserverWrapper(SimpleMote mote, SimpleMoteStatusListener listener) {
            super();
            this.listener = listener;
            this.mote = mote;
        }

        public void update(Observable o, Object arg) {
            this.listener.simpleMoteStatusChange(this.mote);
        }
    }

    protected SimpleMoteStatus(SimpleMote mote) {
        this.mote = mote;
    }

    public short getStatus() {
        return status;
    }

    public boolean isReady() {
        return status == RUNNING || status == PROGRAMMING || status == REQ_PROGRAM || status == STOPPED || status == UNKNOWN;
    }

    public void addChangeListener(SimpleMoteStatusListener listener) {
        eventDispatcher.addWrappedObserver(listener, new ObserverWrapper(this.mote, listener));
    }

    public void removeChangeListener(SimpleMoteStatusListener listener) {
        eventDispatcher.deleteWrappedObserver(listener);
    }

    protected void setStatus(short status) {
        this.status = status;
        this.fireChangeEvent();
    }

    public short getLastCommand() {
        return lastCommand;
    }

    public short getLastResult() {
        return lastResult;
    }

    protected void update(MsgConfirm confirm) {
        this.lastResult = confirm.getResult().getValue();
        this.lastCommand = confirm.getCommand().getValue();
        this.status = confirm.getStatus().getValue();
        this.fireChangeEvent();
    }

    protected void update(MsgRequest request) {
        switch(request.getCommand().getValue()) {
            case MsgCommand.PROGRAM:
                this.status = REQ_PROGRAM;
                break;
            case MsgCommand.CANCELPROGRAMMING:
                this.status = REQ_CANCELPROGRAMMING;
                break;
            case MsgCommand.RESET:
                this.status = REQ_RESET;
                break;
            case MsgCommand.START:
                this.status = REQ_START;
                break;
            case MsgCommand.STATUS:
                this.status = REQ_STATUS;
                break;
            case MsgCommand.STOP:
                this.status = REQ_STOP;
                break;
            default:
                this.status = INVALID;
        }
        this.fireChangeEvent();
    }

    protected void fireChangeEvent() {
        this.eventDispatcher.setChanged();
        this.eventDispatcher.notifyObservers();
    }
}
