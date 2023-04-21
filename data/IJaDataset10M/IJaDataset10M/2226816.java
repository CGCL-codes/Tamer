package org.charvolant.tmsnet.client;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.StringWriter;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.charvolant.tmsnet.AbstractModel;
import org.charvolant.tmsnet.TMSClientPreferences;
import org.charvolant.tmsnet.TMSNetError;
import org.charvolant.tmsnet.model.Directory;
import org.charvolant.tmsnet.model.Event;
import org.charvolant.tmsnet.model.FileEntry;
import org.charvolant.tmsnet.model.PVRState;
import org.charvolant.tmsnet.model.Station;
import org.charvolant.tmsnet.model.Timer;
import org.charvolant.tmsnet.protocol.TMSNetInterface;
import org.charvolant.tmsnet.resources.ResourceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A client for the TMSNetServer TAP.
 * <p>
 * The client is responsible for managing the communications with the TAP,
 * getting status information and generally keeping things in line.
 *
 * @author Doug Palmer &lt;doug@charvolant.org&gt;
 *
 */
public class TMSNetClient extends AbstractModel implements PropertyChangeListener, Transactable, Connectable {

    /** The logger for the class */
    private static final Logger logger = LoggerFactory.getLogger(TMSNetClient.class);

    /** A progress change */
    public static final String EVENT_PROGRESS = "progress";

    /** We're starting a big update */
    public static final String EVENT_UPDATING = "updating";

    /** We've finished a big update */
    public static final String EVENT_UPDATED = "updated";

    /** There's been an error */
    public static final String EVENT_ERROR = "error";

    /** The current configuration */
    private TMSClientPreferences preferences;

    /** The JAXB context for loading/saving state */
    private JAXBContext context;

    /** The interface to the server */
    private TMSNetInterface intf;

    /** The state model of the system */
    private PVRState state;

    /** Current transaction */
    private Transaction<Transactable> current;

    /** The list of things that can be undone/committed */
    private List<Transaction<Transactable>> executed;

    /**
   * Construct a new client with a pre-defined set of preferences.
   * 
   * @param preferences The preferences to use
   *
   * @throws Exception if there is a problem constructing the client.
   */
    public TMSNetClient(TMSClientPreferences preferences) throws Exception {
        this.context = JAXBContext.newInstance("org.charvolant.tmsnet.client:org.charvolant.tmsnet.command:org.charvolant.tmsnet.model:org.charvolant.tmsnet.protocol");
        this.preferences = preferences;
        this.state = new PVRState();
        this.executed = new ArrayList<Transaction<Transactable>>(16);
    }

    /**
   * Construct a new client with the stored peference set.
   *
   * @throws Exception if there is a problem constructing the client
   */
    public TMSNetClient() throws Exception {
        this(TMSClientPreferences.getInstance());
    }

    /**
   * Get the server address, stored in the preferences.
   * 
   * @return The server address
   */
    protected InetSocketAddress getServer() {
        String server = this.preferences.getServer();
        int port = this.preferences.getPort();
        return new InetSocketAddress(server, port);
    }

    /**
   * Get the interface.
   *
   * @return the interface
   */
    public TMSNetInterface getIntf() {
        return this.intf;
    }

    /**
   * Get the preferences.
   *
   * @return the preferences
   */
    public TMSClientPreferences getPreferences() {
        return this.preferences;
    }

    /**
   * Get the PVR state.
   *
   * @return the state
   */
    @Override
    public PVRState getState() {
        return this.state;
    }

    /**
   * Get the list of executed but not committed transactions.
   *
   * @return the executed transactions
   */
    public List<Transaction<Transactable>> getExecuted() {
        return this.executed;
    }

    /**
   * Get the current label.
   * <p>
   * The current label is a message key that describes where a transaction has got to.
   * 
   * @return The current label or null for none
   * 
   * @see org.charvolant.tmsnet.client.Transaction#getLabel()
   */
    public String getLabel() {
        if (this.current == null) return null;
        return this.current.getLabel();
    }

    /**
   * Get the current progress in a task.
   * 
   * @return The progress as a percentage (0 for N/A)
   * 
   * @see org.charvolant.tmsnet.client.Transaction#getProgress()
   */
    public int getProgress() {
        if (this.current == null) return 0;
        return this.current.getProgress();
    }

    /**
   * Set the resource locator.
   * <p>
   * Once set, the resources are loaded for the current locale
   * 
   * @param locator The locator
   */
    public void setLocator(ResourceLocator locator) {
        this.getState().setLocator(locator);
        this.getState().loadResources(this.getPreferences().getLocale());
    }

    /**
   * Convert an object into XML for display.
   * 
   * @param object The object
   * 
   * @return The XML form of the object
   */
    public String xmlText(Object object) {
        try {
            Marshaller marshaller = this.context.createMarshaller();
            StringWriter writer = new StringWriter(256);
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(object, writer);
            return writer.toString();
        } catch (JAXBException ex) {
            this.logger.error("Unable to represent " + object, ex);
            return ex.getMessage();
        }
    }

    /**
   * Is the system idle at the moment?
   * 
   * @return True if we do not have a currently running transaction
   */
    public boolean isIdle() {
        return this.current == null;
    }

    /**
   * Is the current transaction cancellable?
   * 
   * @return True if we have a currently running transaction and it is cancellable
   */
    public boolean isCancellable() {
        return this.current != null && this.current.isCancellable();
    }

    /**
   * Is the system connected at the moment?
   * 
   * @return True if we have reached the idle state and have an interface
   */
    public boolean isConnected() {
        return this.intf != null && this.intf.isConnected();
    }

    /**
   * Is the system commmandable at the moment?
   * 
   * @return True if we are idle and connected
   */
    public boolean isCommandable() {
        return this.isConnected() && this.isIdle();
    }

    /**
   * Is it possible to shut up shop and go home?
   * <p>
   * The client is closable if it is idle and there isn't anything to
   * undo or commit.
   * 
   * @return True if the client is closable
   */
    public boolean isClosable() {
        return this.isIdle() && this.getUndo() == null;
    }

    /**
   * Is there an existing delete file undo for this file?
   * <p>
   * This is used when a deletion hasn't completed, to allow the file to be re-deleted again.
   * 
   * @param file The file
   * 
   * @return True if there is an existing entry for this file
   */
    public boolean hasDeleteUndo(FileEntry file) {
        for (Transaction<Transactable> tr : this.executed) if (tr instanceof DeleteFileTransaction && ((DeleteFileTransaction) tr).getFile() == file) return true;
        return false;
    }

    /**
   * Queue a command for execution.
   * 
   * @param command The command
   * @see Transactable#queue(Object)
   */
    @Override
    public void queue(Object command) {
        if (!this.isConnected()) throw new IllegalStateException("Interface is not connected");
        this.intf.queue(command);
    }

    /**
   * Start executing a transaction.
   * <p>
   * The transaction is ignored if the client is currently busy.
   * Otherwise this is set as the transactions client and the client is executed.
   * 
   * @param transaction The transaction
   */
    protected synchronized void execute(Transaction<Transactable> transaction) {
        if (!this.isCommandable()) return;
        this.current = transaction;
        this.current.setClient(this);
        this.firePropertyChange(this.EVENT_UPDATING, false, true);
        this.current.execute();
    }

    /**
   * Stop the client.
   * <p>
   * This is essentially an emergency stop.
   */
    public void stop() {
        if (this.intf != null) {
            this.intf.removePropertyChangeListener(this);
            this.intf.stop();
        }
    }

    /**
   * Get the current state of the PVR.
   */
    public void retrieveState() {
        this.execute(new GetStateTransaction(this.preferences.getEpgDaysBefore(), this.preferences.getEpgDaysAfter()));
    }

    /**
   * Change channel to a specific channel.
   * 
   * @param channel The channel
   */
    public void changeChannel(Station station) {
        if (!this.isCommandable()) return;
        this.execute(new ChangeChannelTransaction(station));
    }

    /**
   * Add a new timer to record an event.
   * 
   * @param event The event
   */
    public void addTimer(Event event) {
        this.execute(new AddTimerTransaction(event));
    }

    /**
   * Modify a timer.
   * 
   * @param timer The timer
   */
    public void modifyTimer(Timer timer) {
        Timer original = this.state.getTimers().get(timer.getSlot() - 1);
        this.execute(new ModifyTimerTransaction(original.toDescription(), timer.toDescription()));
    }

    /**
   * Delete a timer.
   * 
   * @param timer The timer
   */
    public void deleteTimer(Timer timer) {
        this.execute(new DeleteTimerTransaction(timer));
    }

    /**
   * Fetch a directory.
   * 
   * @param directory The directory
   */
    public void fetchDirectory(Directory directory) {
        this.execute(new FetchDirectoryTransaction(directory));
    }

    /**
   * Play a recording.
   * 
   * @param recording The recording to play
   */
    public void playRecording(FileEntry recording) {
        this.execute(new StartPlayTransaction(recording));
    }

    /**
   * Stop playing a recording.
   */
    public void stopPlayRecording() {
        if (this.state.getPlayInfo() == null) return;
        this.execute(new StopPlayTransaction());
    }

    /**
   * Start recording.
   */
    public void startRecording() {
        this.execute(new StartRecordingTransaction());
    }

    /**
   * Stop recording.
   * 
   * @param model The recording to stop
   */
    public void stopRecording(Timer model) {
        this.execute(new StopRecordingTransaction(model.getSlot()));
    }

    /**
   * Set the duration of a recording
   * 
   * @param slot The recording slot
   * @param duration The new duration in minutes
   */
    public void recordingDuration(int slot, int duration) {
        this.execute(new SetRecordingDurationTransaction(slot, duration));
    }

    /**
   * Delete a file
   * 
   * @param file The file to delete
   */
    public void deleteFile(FileEntry file) {
        this.execute(new DeleteFileTransaction(file));
    }

    /**
   * Rename a file
   * 
   * @param file The file to delete
   * @param name The new base name
   */
    public void renameFile(FileEntry file, String name) {
        this.execute(new RenameFileTransaction(file, name));
    }

    /**
   * Save a file.
   * <p>
   * If the file entry contains multiple files, each file will be
   * saved with a suitable extension.
   * 
   * @param file The PVR file to save
   * @param save The file to save to
   */
    public void saveFile(FileEntry file, File save) {
        this.execute(new FetchFileTransaction(file, save));
    }

    /**
   * Send a file to the PVR.
   * <p>
   * The file will be transferred into the remote directory
   * with the same name as the local file.
   * 
   * @param dir The target directory
   * @param send The file to send
   */
    public void sendFile(Directory dir, File send) {
        this.execute(new SendFileTransaction(dir, send));
    }

    /**
   * Create a directory on the PVR
   * 
   * @param dir The target directory
   * @param name The directory name
   */
    public void createDirectory(Directory dir, String name) {
        this.execute(new CreateDirectoryTransaction(dir, name));
    }

    /**
   * Update the transient state of the model.
   */
    public void update() {
        if (!this.isCommandable()) return;
        this.logger.debug("Update");
        this.execute(new UpdateTransaction());
    }

    /**
   * Refresh the state of the model.
   */
    public void refresh() {
        if (!this.isCommandable()) return;
        this.logger.debug("Refresh");
        this.execute(new GetStateTransaction());
    }

    /**
   * Connect to the PVR via an interface.
   */
    @Override
    public void connect() {
        InetSocketAddress server = this.getServer();
        this.state.setName(server.getHostName());
        this.intf = new TMSNetInterface(server);
        this.intf.addPropertyChangeListener(this);
        this.intf.start();
    }

    /**
   * Disconnect the PVR interface.
   */
    @Override
    public void disconnect() {
        if (this.intf != null) {
            this.intf.removePropertyChangeListener(this);
            this.intf.stop();
            this.intf = null;
        }
    }

    /**
   * Get the transaction to undo.
   * 
   * @return The transaction on top of the undo stack, or null if the stack is empty
   */
    public Transaction<Transactable> getUndo() {
        return this.executed.isEmpty() ? null : this.executed.get(this.executed.size() - 1);
    }

    /**
   * Undo the last operation.
   */
    public void undo() {
        Transaction<Transactable> last;
        if (!this.isCommandable() || this.executed.isEmpty()) return;
        synchronized (this) {
            last = this.executed.remove(this.executed.size() - 1);
            this.logger.debug("Undo " + last);
            this.current = last;
            this.current.setClient(this);
        }
        this.firePropertyChange(this.EVENT_UPDATING, false, true);
        this.current.rollback();
    }

    /**
   * Commit the undoable transactions
   */
    public void commit() {
        if (!this.isCommandable() || this.executed.isEmpty()) return;
        synchronized (this) {
            this.current = new CompositeTransaction(this.executed, TransactionLifecycle.EXECUTED);
            this.executed = new ArrayList<Transaction<Transactable>>();
        }
        this.firePropertyChange(this.EVENT_UPDATING, false, true);
        this.current.setClient(this);
        this.current.commit();
    }

    /**
   * Roll back the undoable transactions
   */
    public void rollback() {
        if (!this.isCommandable() || this.executed.isEmpty()) return;
        synchronized (this) {
            this.current = new CompositeTransaction(this.executed, TransactionLifecycle.EXECUTED);
            this.executed = new ArrayList<Transaction<Transactable>>();
        }
        this.firePropertyChange(this.EVENT_UPDATING, false, true);
        this.current.setClient(this);
        this.current.rollback();
    }

    /**
   * Cancel the current transaction.
   */
    public void cancelTransaction() {
        if (this.current != null && this.current.isCancellable()) this.current.cancel();
    }

    /**
   * Provide some information about an error.
   * 
   * @param The error message
   * @see org.charvolant.tmsnet.client.Transactable#reportError(org.charvolant.tmsnet.TMSNetError)
   */
    @Override
    public void reportError(TMSNetError error) {
        this.logger.warn("Protocol error " + error);
        this.firePropertyChange(this.EVENT_ERROR, null, error);
    }

    /**
   * Provide some information about an error
   *
   * @param ex The source exception
   * @see org.charvolant.tmsnet.client.Transactable#reportError(java.lang.Exception)
   */
    @Override
    public void reportError(Exception ex) {
        this.logger.warn("Error " + ex);
        this.logger.debug("Report exception ", ex);
        this.firePropertyChange(this.EVENT_ERROR, null, ex.getMessage());
    }

    /**
   * Process a command response from the interface.
   * <p>
   * Responses are treated as events and fed into the current transaction for
   * handling.
   * <p>
   * The event has {@link PropertyChangeEvent#getOldValue()} set to the original
   * command that caused the response and {@link PropertyChangeEvent#getNewValue()}
   * set to the response.
   * 
   * @param evt The event
   * 
   * @throws Exception if unable to process the response
   */
    protected void processCommandResponse(PropertyChangeEvent evt) throws Exception {
        if (this.current != null) this.current.event(evt.getNewValue());
    }

    /**
   * Process a state change from the interface.
   * <p>
   * Responses are treated as events and fed into the current transaction for
   * handling.
   * 
   * @param evt The event
   * 
   * @throws Exception if unable to process the response
   */
    protected void processInterfaceState(PropertyChangeEvent evt) throws Exception {
        if (this.current != null) this.current.event(evt.getNewValue());
    }

    /**
   * Recieve notification of a transaction change.
   * <p>
   * We need to see if the transaction has completed, indicate progress, etc. etc. etc.
   *
   * @see org.charvolant.tmsnet.client.Transactable#transactionChanged()
   */
    @Override
    public void transactionChanged() {
        if (this.current != null) {
            if (this.current.isStopped() || this.current.isIdle() || this.current.isError()) {
                Transaction<Transactable> old;
                this.logger.debug("Completed transaction " + this.current);
                synchronized (this) {
                    old = this.current;
                    this.current = null;
                }
                if (old.isIdle()) this.executed.add(old);
                if (old.isError()) {
                    this.disconnect();
                }
            }
            this.firePropertyChange(this.current == null ? this.EVENT_UPDATED : this.EVENT_PROGRESS, false, true);
        }
    }

    /**
   * Respond to a property change.
   *
   * @param evt The event
   * 
   * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
   */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        try {
            if (evt.getSource() == this.intf && evt.getPropertyName().equals(TMSNetInterface.COMMAND)) this.processCommandResponse(evt);
            if (evt.getSource() == this.intf && evt.getPropertyName().equals(TMSNetInterface.STATE)) this.processInterfaceState(evt);
        } catch (Exception ex) {
            this.logger.warn("Unable to process change " + evt, ex);
        }
    }
}
