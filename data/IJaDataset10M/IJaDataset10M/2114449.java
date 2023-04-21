package filebot.ui.panel.sfv;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import tuned.ui.SwingWorkerPropertyChangeAdapter;

public class Checksum {

    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    public static final String STATE_PROPERTY = "state";

    public static final String PROGRESS_PROPERTY = "progress";

    private Long checksum = null;

    private State state = State.PENDING;

    private ChecksumComputationTask computationTask;

    public static enum State {

        PENDING, INPROGRESS, READY
    }

    public Checksum(long checksum) {
        setChecksum(checksum);
        setState(State.READY);
    }

    public Checksum(String checksumString) {
        this(Long.parseLong(checksumString, 16));
    }

    public Checksum(ChecksumComputationTask computationTask) {
        this.computationTask = computationTask;
        computationTask.addPropertyChangeListener(new ComputationTaskPropertyChangeListener());
    }

    public String getChecksumString() {
        StringBuffer buffer = new StringBuffer(8);
        buffer.append(Long.toHexString(checksum).toUpperCase());
        while (buffer.length() < 8) {
            buffer.insert(0, "0");
        }
        return buffer.toString();
    }

    public Long getChecksum() {
        return checksum;
    }

    public void setChecksum(Long checksum) {
        this.checksum = checksum;
    }

    public State getState() {
        return state;
    }

    private void setState(State state) {
        this.state = state;
        propertyChangeSupport.firePropertyChange(STATE_PROPERTY, null, state);
    }

    public Integer getProgress() {
        return computationTask.getProgress();
    }

    public void cancelComputationTask() {
        if (computationTask == null) return;
        computationTask.cancel(false);
    }

    private class ComputationTaskPropertyChangeListener extends SwingWorkerPropertyChangeAdapter {

        @Override
        public void progress(PropertyChangeEvent evt) {
            propertyChangeSupport.firePropertyChange(PROGRESS_PROPERTY, null, evt.getNewValue());
        }

        @Override
        public void started(PropertyChangeEvent evt) {
            setState(State.INPROGRESS);
        }

        @Override
        public void done(PropertyChangeEvent evt) {
            ChecksumComputationTask task = (ChecksumComputationTask) evt.getSource();
            if (task.isCancelled()) {
                return;
            }
            try {
                checksum = task.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
            setState(State.READY);
            computationTask = null;
        }
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    @Override
    public String toString() {
        switch(state) {
            case PENDING:
                return state.toString();
            case INPROGRESS:
                return state.toString();
            case READY:
                return getChecksumString();
            default:
                return null;
        }
    }
}
