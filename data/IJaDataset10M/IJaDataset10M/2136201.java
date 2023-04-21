package org.bs.mdi.core;

import java.lang.ref.WeakReference;
import java.util.*;

/**
 * The MessageDispatcher manages communication between various parts of the framework.
 */
public class MessageDispatcher {

    /**
	 * Indicates that a new document window has been created.
	 */
    public static final int WINDOW_CREATED = 1;

    /**
	 * Indicates that a document window has been opened.
	 */
    public static final int WINDOW_OPENED = 2;

    /**
	 * Indicates that a document window has been closed.
	 */
    public static final int WINDOW_CLOSED = 3;

    /**
	 * Indicated that a document window has been selected/activated.
	 */
    public static final int WINDOW_SELECTED = 4;

    /**
	 * Indicates that the application has just been launched.
	 */
    public static final int APP_INIT = 5;

    /**
	 * Indicates that the application is shutting down.
	 */
    public static final int APP_QUIT = 6;

    /**
	 * Indicates that the user has issued a command, for example clicked a menu entry.
	 */
    public static final int COMMAND_ISSUED = 7;

    /**
	 * Indicates that the current selection has been changed.
	 */
    public static final int SELECTION_CHANGED = 8;

    /**
	 * Indicates that the UndoManager registered an Action.
	 */
    public static final int ACTION_OCCURRED = 9;

    /**
	 * Indicates that the UndoManager just performed an undo operation.
	 */
    public static final int ACTION_UNDONE = 10;

    /**
	 * Indicates that the UndoManager just performed a redo operation.
	 */
    public static final int ACTION_REDONE = 11;

    /**
	 * Indicates that the FileIOManager has registered a new FileLoader. 
	 */
    public static final int IO_LOADER_REGISTERED = 12;

    /**
	 * Indicates that the FileIOManager has registered a new FileSaver. 
	 */
    public static final int IO_SAVER_REGISTERED = 13;

    /**
	 * Indicates that the FileIOManager has registered a new FileExporter. 
	 */
    public static final int IO_EXPORTER_REGISTERED = 14;

    /**
	 * Indicates that another document has been selected.
	 */
    public static final int DOCUMENT_SELECTED = 15;

    /**
	 * Indicates that a document has been saved.
	 */
    public static final int DOCUMENT_SAVED = 16;

    /**
	 * Indicates that a documents dirty status has been changed.
	 */
    public static final int DOCUMENT_DIRTY = 17;

    /**
	 * This is the last message ID reserved by the MDI framework. Custom messages may
	 * use id's > LAST_RESERVED.
	 */
    public static final int LAST_RESERVED = 255;

    List processors = new LinkedList();

    /**
	 * Creates a new message dispatcher.
	 * Note that there is a default dispatcher which can be accessed using
	 * {@link Application#getMessageDispatcher}.
	 */
    public MessageDispatcher() {
    }

    /**
	 * Register a message processor so that it will be notified of future
	 * messages.
	 * @param processor	the message processor
	 */
    public void registerProcessor(MessageProcessor processor) {
        for (Iterator i = processors.iterator(); i.hasNext(); ) {
            WeakReference ref = (WeakReference) i.next();
            MessageProcessor p = (MessageProcessor) ref.get();
            if (processor == p) return;
        }
        processors.add(new WeakReference(processor));
    }

    /**
	 * Unregister a message processor so that it will no longer be notified
	 * of any messages.
	 * @param processor	the message processor
	 */
    public void unregisterProcessor(MessageProcessor processor) {
        for (Iterator i = processors.iterator(); i.hasNext(); ) {
            WeakReference ref = (WeakReference) i.next();
            MessageProcessor p = (MessageProcessor) ref.get();
            if (processor == p) {
                i.remove();
                break;
            }
        }
    }

    /**
	 * Counts the registered MessageProcessors.
	 * @return	the number of registered MessageProcessors
	 */
    public int countProcessors() {
        return processors.size();
    }

    /**
	 * Unregisters all MessageProcessors, so that nobody will be notified of
	 * future messages.
	 */
    public void unregisterAllProcessors() {
        processors.clear();
    }

    /**
	 * Dispatches a message. All registered MessageProcessors will be notified of
	 * this message. Note that it cannot be guaranteed that a particular MessageProcessor 
	 * will handle the message. It is possible that none, just one or all of the registered
	 * MessageProcessors will react.  
	 * @param source	the source where this message originated from
	 * @param type	the message type
	 * @param argument	an argument
	 */
    public void dispatch(Object source, int type, Object argument) {
        for (Iterator i = processors.iterator(); i.hasNext(); ) {
            WeakReference ref = (WeakReference) i.next();
            MessageProcessor p = (MessageProcessor) ref.get();
            if (p == null) i.remove(); else p.processMessage(source, type, argument);
        }
    }
}
