package org.ccnx.ccn.impl.repo;

import java.io.IOException;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import org.ccnx.ccn.CCNHandle;
import org.ccnx.ccn.CCNInterestListener;
import org.ccnx.ccn.config.SystemConfiguration;
import org.ccnx.ccn.impl.CCNFlowControl;
import org.ccnx.ccn.impl.support.Log;
import org.ccnx.ccn.impl.support.ConcurrencyUtils.Waiter;
import org.ccnx.ccn.io.content.ContentDecodingException;
import org.ccnx.ccn.profiles.CommandMarker;
import org.ccnx.ccn.protocol.ContentName;
import org.ccnx.ccn.protocol.ContentObject;
import org.ccnx.ccn.protocol.Interest;
import org.ccnx.ccn.protocol.SignedInfo.ContentType;

/**
 * Handle repo specialty start/end protocol
 * 
 * Needs to be able to handle multiple clients. Currently due to limitations in close,
 * to do this requires that clients above close their streams in order when multiple
 * streams are using the same FC.
 * 
 * Intended to handle the repo ack protocol. This is currently unused until we find
 * a workable way to do it.
 * 
 * @see CCNFlowControl
 * @see RepositoryInterestHandler
 */
public class RepositoryFlowControl extends CCNFlowControl implements CCNInterestListener {

    protected HashSet<Interest> _writeInterests = new HashSet<Interest>();

    protected boolean localRepo = false;

    protected Queue<Client> _clients = new ConcurrentLinkedQueue<Client>();

    /**
	 * Handles packets received from the repository after the start write request.  It's looking
	 * for a RepoInfo packet indicating a repository has responded.
	 */
    public Interest handleContent(ContentObject co, Interest interest) {
        Interest interestToReturn = null;
        if (Log.isLoggable(Log.FAC_REPO, Level.INFO)) Log.info(Log.FAC_REPO, "handleContent: got potential repo message: {0}", co.name());
        if (co.signedInfo().getType() != ContentType.DATA) return interestToReturn;
        RepositoryInfo repoInfo = new RepositoryInfo();
        try {
            repoInfo.decode(co.content());
            switch(repoInfo.getType()) {
                case INFO:
                    for (Client client : _clients) {
                        if (client._name.isPrefixOf(co.name())) {
                            if (Log.isLoggable(Log.FAC_REPO, Level.FINE)) Log.fine(Log.FAC_REPO, "Marked client {0} initialized", client._name);
                            synchronized (this) {
                                client._initialized = true;
                                notifyAll();
                            }
                        }
                    }
                    break;
                default:
                    break;
            }
        } catch (ContentDecodingException e) {
            Log.info(Log.FAC_REPO, "ContentDecodingException parsing RepositoryInfo: {0} from content object {1}, skipping.", e.getMessage(), co.name());
        }
        return interestToReturn;
    }

    /**
	 * Preserves information about our clients
	 */
    protected class Client {

        protected ContentName _name;

        protected Shape _shape;

        protected boolean _initialized = false;

        public Client(ContentName name, Shape shape) {
            _name = name;
            _shape = shape;
        }

        public ContentName name() {
            return _name;
        }

        public Shape shape() {
            return _shape;
        }
    }

    /**
	 * @param handle a CCNHandle - if null one is created
	 * @throws IOException if library is null and a new CCNHandle can't be created
	 */
    public RepositoryFlowControl(CCNHandle handle) throws IOException {
        super(handle);
    }

    /**
	 * constructor to allow the repo flow controller to set the scope for the start write interest
	 * 
	 * @param handle a CCNHandle - if null, one is created
	 * @param local boolean to determine if a general start write, or one with the scope set to one.
	 * 			A scope set to one will limit the write to a repo on the local device
	 * @throws IOException
	 */
    public RepositoryFlowControl(CCNHandle handle, boolean local) throws IOException {
        super(handle);
        localRepo = local;
    }

    /**
	 * @param name		an initial namespace for this stream
	 * @param handle	a CCNHandle - if null one is created
	 * @throws IOException if handle is null and a new CCNHandle can't be created
	 */
    public RepositoryFlowControl(ContentName name, CCNHandle handle) throws IOException {
        super(name, handle);
    }

    /**
	 * @param name		an initial namespace for this stream
	 * @param handle	a CCNHandle - if null one is created
	 * @param local boolean to determine if a general start write, or one with the scope set to one.
	 * 			A scope set to one will limit the write to a repo on the local device
	 * @throws IOException if handle is null and a new CCNHandle can't be created
	 */
    public RepositoryFlowControl(ContentName name, CCNHandle handle, boolean local) throws IOException {
        super(name, handle);
        localRepo = local;
    }

    /**
	 * @param name		an initial namespace for this stream
	 * @param handle	a CCNHandle - if null one is created
	 * @param shape		shapes are not currently implemented and may be deprecated. The only currently defined
	 * 					shape is "Shape.STREAM"
	 * @throws IOException	if handle is null and a new CCNHandle can't be created
	 * @see	CCNFlowControl
	 */
    public RepositoryFlowControl(ContentName name, CCNHandle handle, Shape shape) throws IOException {
        super(name, handle);
    }

    /**
	 * @param name		an initial namespace for this stream
	 * @param handle	a CCNHandle - if null one is created
	 * @param shape		shapes are not currently implemented and may be deprecated. The only currently defined
	 * 					shape is "Shape.STREAM"
	 * @param local boolean to determine if a general start write, or one with the scope set to one.
	 * 			A scope set to one will limit the write to a repo on the local device
	 * @throws IOException	if handle is null and a new CCNHandle can't be created
	 * @see	CCNFlowControl
	 */
    public RepositoryFlowControl(ContentName name, CCNHandle handle, Shape shape, boolean local) throws IOException {
        super(name, handle);
        localRepo = local;
    }

    @Override
    public void startWrite(ContentName name, Shape shape) throws IOException {
        if (Log.isLoggable(Log.FAC_REPO, Level.INFO)) Log.info(Log.FAC_REPO, "RepositoryFlowControl.startWrite called for name {0}, shape {1}", name, shape);
        Client client = new Client(name, shape);
        _clients.add(client);
        ContentName repoWriteName = new ContentName(name, CommandMarker.COMMAND_MARKER_REPO_START_WRITE.getBytes(), Interest.generateNonce());
        Interest writeInterest = new Interest(repoWriteName);
        if (localRepo || SystemConfiguration.FC_LOCALREPOSITORY) {
            writeInterest.scope(1);
        }
        _handle.expressInterest(writeInterest, this);
        synchronized (this) {
            _writeInterests.add(writeInterest);
        }
        try {
            new Waiter(getTimeout()) {

                @Override
                protected boolean check(Object o, Object check) throws Exception {
                    return ((Client) check)._initialized;
                }
            }.wait(this, client);
        } catch (Exception e) {
        }
        synchronized (this) {
            if (!client._initialized) {
                _clients.remove();
                Log.warning(Log.FAC_REPO, "No response from a repository, cannot add name space : " + name);
                throw new IOException("No response from a repository for " + name);
            }
        }
    }

    /**
	 * Handle acknowledgement packet from the repo
	 * @param name
	 */
    public void ack(ContentName name) {
        synchronized (_holdingArea) {
            if (Log.isLoggable(Log.FAC_REPO, Level.FINE)) Log.fine(Log.FAC_REPO, "Handling ACK {0}", name);
            if (_holdingArea.get(name) != null) {
                ContentObject co = _holdingArea.get(name);
                if (Log.isLoggable(Log.FAC_REPO, Level.FINE)) Log.fine(Log.FAC_REPO, "CO {0} acked", co.name());
                _holdingArea.remove(co.name());
                if (_holdingArea.size() < _capacity) _holdingArea.notify();
            }
        }
    }

    /**
	 * Called after close has completed a flush
	 */
    @Override
    public void afterClose() throws IOException {
        try {
            _clients.remove();
        } catch (NoSuchElementException nse) {
        }
        super.afterClose();
        cancelInterests();
    }

    /**
	 * Cancel any outstanding interests on close.
	 * TODO - since the flow controller may be used by multiple streams we probably want to use Clients to decide
	 * what interests to cancel.
	 */
    public void cancelInterests() {
        for (Interest writeInterest : _writeInterests) {
            _handle.cancelInterest(writeInterest, this);
        }
    }

    /**
	 * Help users determine what type of flow controller this is.
	 */
    public SaveType saveType() {
        if (SystemConfiguration.FC_LOCALREPOSITORY) return SaveType.LOCALREPOSITORY;
        if (localRepo) return SaveType.LOCALREPOSITORY; else return SaveType.REPOSITORY;
    }
}
