package org.ladybug.comm;

import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.JComponent;
import org.ladybug.cfg.Configuration;
import org.ladybug.core.AbstractItemContainer;
import org.ladybug.core.AbstractNamedItem;
import org.ladybug.core.items.Feature;
import org.ladybug.core.items.Release;
import org.ladybug.core.items.Task;
import org.ladybug.core.items.UserStory;
import org.ladybug.core.users.User;
import org.ladybug.db.Database;
import org.ladybug.db.DatabaseDriver;
import org.ladybug.db.DatabaseImpl;
import org.ladybug.events.Event;
import org.ladybug.events.EventDispatcher;
import org.ladybug.events.EventGenerator;
import org.ladybug.events.EventListener;
import org.ladybug.gui.toolbox.customcomponents.CustomComponentFactory;
import org.ladybug.gui.toolbox.icons.FilebasedIcon;
import org.ladybug.gui.toolbox.progressbar.ExpensiveOperationEventGenerator;
import org.ladybug.log.LogEngine;
import org.ladybug.utils.Constants;
import org.ladybug.utils.ErrorCode;
import org.ladybug.utils.LadybugException;
import org.ladybug.utils.Reason;
import org.ladybug.utils.Result;
import org.ladybug.utils.UniqueNumberGenerator;
import org.ladybug.utils.validation.ValidateArgument;
import org.ladybug.utils.validation.ValidationTemplate;

/**
 * The actual RMI server implementation.
 * 
 * @author Aurelian Pop
 */
public class ServerImpl implements RemoteServer, LocalServer {

    private static final long serialVersionUID = 1L;

    private static final Configuration CFG = Configuration.getInstance();

    private static final Result<String> PONG = new Result<String>("pong");

    private static Registry rmiRegistry = null;

    private final transient MyDispatchEventListener eventListener = new MyDispatchEventListener();

    private final transient Database db = new DatabaseImpl(Constants.DB_NAME, DatabaseDriver.valueOf(CFG.server.database.driver.toString()));

    private final transient ExecutorService threadPool = Executors.newCachedThreadPool();

    private final int rmiPort = CFG.server.rmiPort.toInt();

    private final Map<Long, RemoteClient> remoteClients = Collections.synchronizedMap(new HashMap<Long, RemoteClient>());

    private final JComponent dbIcon = CustomComponentFactory.createFixedImageComponent(new FilebasedIcon(Constants.ICON_DATABASE, 32).getImage());

    private final transient ExpensiveOperationEventGenerator expensiveOpEventGenerator = new ExpensiveOperationEventGenerator(dbIcon, false);

    private boolean serverStarted = false;

    @Override
    public Result<String> ping() throws RemoteException {
        LogEngine.trace("[PING]");
        return PONG;
    }

    @Override
    public synchronized Result<Long> register(final RemoteClient client) throws RemoteException {
        LogEngine.trace("[REGISTER]");
        ValidateArgument.isNotNull(client, ValidationTemplate.NOT_NULL, "client");
        Long registrationID = null;
        synchronized (remoteClients) {
            if (remoteClients.containsValue(client)) {
                for (final Entry<Long, RemoteClient> entry : remoteClients.entrySet()) {
                    if (entry.getValue().equals(client)) {
                        registrationID = entry.getKey();
                        break;
                    }
                }
                LogEngine.debug("Regained connection with client " + registrationID + " ...", null);
            } else {
                do {
                    registrationID = Long.valueOf(UniqueNumberGenerator.getUniqueId());
                } while (remoteClients.containsKey(registrationID));
                LogEngine.debug("Registering client " + registrationID + " ...", null);
                remoteClients.put(registrationID, client);
            }
        }
        return new Result<Long>(registrationID);
    }

    @Override
    public synchronized Result<Void> unregister(final Long registrationID) throws RemoteException {
        LogEngine.trace("[UNREGISTER]");
        ValidateArgument.isNotNull(registrationID, ValidationTemplate.NOT_NULL, "registrationID");
        synchronized (remoteClients) {
            if (remoteClients.containsKey(registrationID)) {
                LogEngine.debug("Removing client " + registrationID + " ...", null);
                remoteClients.remove(registrationID);
            }
        }
        return new Result<Void>();
    }

    @Override
    public synchronized Result<Void> create(final AbstractNamedItem newItem) throws RemoteException {
        LogEngine.trace("[CREATE]");
        ValidateArgument.isNotNull(newItem, ValidationTemplate.NOT_NULL, "newItem");
        Result<Void> result = new Result<Void>(Reason.UNKNOWN);
        if (newItem instanceof Release) {
            result = db.insertRelease((Release) newItem);
        }
        if (newItem instanceof Feature) {
            result = db.insertFeature((Feature) newItem);
        }
        if (newItem instanceof UserStory) {
            result = db.insertUserStory((UserStory) newItem);
        }
        if (newItem instanceof Task) {
            result = db.insertTask((Task) newItem);
        }
        if (newItem instanceof User) {
            result = db.insertUser((User) newItem);
        }
        if (result.getErrorCode() == ErrorCode.SUCCESS) {
            resyncClients();
        }
        return result;
    }

    @Override
    public synchronized Result<List<AbstractNamedItem>> read(final AbstractNamedItem itemType, final AbstractItemContainer<?> containingItem, final boolean deleted) throws RemoteException {
        LogEngine.trace("[READ]");
        Result<List<AbstractNamedItem>> result = new Result<List<AbstractNamedItem>>(new LinkedList<AbstractNamedItem>());
        final List<AbstractNamedItem> tempList = new LinkedList<AbstractNamedItem>();
        if (itemType instanceof Release) {
            final Result<List<Release>> releases = db.readReleases(deleted);
            tempList.addAll(releases.getReturnValue());
            result = new Result<List<AbstractNamedItem>>(tempList, releases.getErrorCode(), releases.getReason());
        }
        if (itemType instanceof Feature) {
            final Result<List<Feature>> features = db.readFeatures((Release) containingItem, deleted);
            tempList.addAll(features.getReturnValue());
            result = new Result<List<AbstractNamedItem>>(tempList, features.getErrorCode(), features.getReason());
        }
        if (itemType instanceof UserStory) {
            final Result<List<UserStory>> userStories = db.readUserStories((Feature) containingItem, deleted);
            tempList.addAll(userStories.getReturnValue());
            result = new Result<List<AbstractNamedItem>>(tempList, userStories.getErrorCode(), userStories.getReason());
        }
        if (itemType instanceof Task) {
            final Result<List<Task>> tasks = db.readTasks((UserStory) containingItem, deleted);
            tempList.addAll(tasks.getReturnValue());
            result = new Result<List<AbstractNamedItem>>(tempList, tasks.getErrorCode(), tasks.getReason());
        }
        if (itemType instanceof User) {
            final Result<List<User>> users = db.readUsers(deleted);
            tempList.addAll(users.getReturnValue());
            result = new Result<List<AbstractNamedItem>>(tempList, users.getErrorCode(), users.getReason());
        }
        return result;
    }

    @Override
    public synchronized Result<Void> update(final AbstractNamedItem oldItem, final AbstractNamedItem newItem) throws RemoteException {
        LogEngine.trace("[UPDATE]");
        ValidateArgument.isNotNull(oldItem, ValidationTemplate.NOT_NULL, "oldItem");
        ValidateArgument.isNotNull(newItem, ValidationTemplate.NOT_NULL, "newItem");
        Result<Void> result = new Result<Void>(Reason.UNKNOWN);
        if (oldItem.equals(newItem)) {
            reprioritizeItems(oldItem, newItem);
        }
        if (oldItem instanceof Release && newItem instanceof Release) {
            result = db.updateRelease((Release) oldItem, (Release) newItem);
        }
        if (oldItem instanceof Feature && newItem instanceof Feature) {
            result = db.updateFeature((Feature) oldItem, (Feature) newItem);
        }
        if (oldItem instanceof UserStory && newItem instanceof UserStory) {
            result = db.updateUserStory((UserStory) oldItem, (UserStory) newItem);
        }
        if (oldItem instanceof Task && newItem instanceof Task) {
            result = db.updateTask((Task) oldItem, (Task) newItem);
        }
        if (oldItem instanceof User && newItem instanceof User) {
            result = db.updateUser((User) oldItem, (User) newItem);
        }
        if (result.getErrorCode() == ErrorCode.SUCCESS) {
            resyncClients();
        }
        return result;
    }

    @Override
    public synchronized Result<Void> delete(final AbstractNamedItem item) throws RemoteException {
        LogEngine.trace("[DELETE]");
        ValidateArgument.isNotNull(item, ValidationTemplate.NOT_NULL, "item");
        Result<Void> result = new Result<Void>(Reason.UNKNOWN);
        if (item instanceof Release) {
            result = db.deleteRelease((Release) item);
        }
        if (item instanceof Feature) {
            result = db.deleteFeature((Feature) item);
        }
        if (item instanceof UserStory) {
            result = db.deleteUserStory((UserStory) item);
        }
        if (item instanceof Task) {
            result = db.deleteTask((Task) item);
        }
        if (item instanceof User) {
            result = db.deleteUser((User) item);
        }
        if (result.getErrorCode() == ErrorCode.SUCCESS) {
            resyncClients();
        }
        return result;
    }

    @Override
    public synchronized Result<Void> recover(final AbstractNamedItem item) throws RemoteException {
        LogEngine.trace("[RECOVER]");
        ValidateArgument.isNotNull(item, ValidationTemplate.NOT_NULL, "item");
        Result<Void> result = new Result<Void>(Reason.UNKNOWN);
        if (item instanceof Release) {
            final Release release = (Release) item;
            result = db.recoverRelease(release);
        }
        if (item instanceof Feature) {
            final Feature feature = (Feature) item;
            result = db.recoverFeature(feature);
        }
        if (item instanceof UserStory) {
            final UserStory userStory = (UserStory) item;
            result = db.recoverUserStory(userStory);
        }
        if (item instanceof Task) {
            final Task task = (Task) item;
            result = db.recoverTask(task);
        }
        if (item instanceof User) {
            final User user = (User) item;
            result = db.recoverUser(user);
        }
        if (result.getErrorCode() == ErrorCode.SUCCESS) {
            resyncClients();
        }
        return result;
    }

    @Override
    public synchronized Result<Void> purge(final AbstractNamedItem item) throws RemoteException {
        LogEngine.trace("[PURGE]");
        Result<Void> result = new Result<Void>();
        if (item == null) {
            result = db.purgeDatabase();
        }
        if (item instanceof Release) {
            final Release release = (Release) item;
            if (release.equals(Constants.EMPTY_RELEASE)) {
                result = db.purgeReleasesTable();
            } else {
                result = db.purgeRelease(release);
            }
        }
        if (item instanceof Feature) {
            final Feature feature = (Feature) item;
            if (feature.equals(Constants.EMPTY_FEATURE)) {
                result = db.purgeFeaturesTable();
            } else {
                result = db.purgeFeature(feature);
            }
        }
        if (item instanceof UserStory) {
            final UserStory userStory = (UserStory) item;
            if (userStory.equals(Constants.EMPTY_USERSTORY)) {
                result = db.purgeUserStoriesTable();
            } else {
                result = db.purgeUserStory(userStory);
            }
        }
        if (item instanceof Task) {
            final Task task = (Task) item;
            if (task.equals(Constants.EMPTY_TASK)) {
                result = db.purgeTasksTable();
            } else {
                result = db.purgeTask(task);
            }
        }
        if (item instanceof User) {
            final User user = (User) item;
            if (user.equals(Constants.EMPTY_USER)) {
                result = db.purgeUsersAndAvatarsTable();
            } else {
                result = db.purgeUserAndAvatar(user);
            }
        }
        if (result.getErrorCode() == ErrorCode.SUCCESS) {
            resyncClients();
        }
        return result;
    }

    @Override
    public Result<Boolean> hasDeletedItems() throws RemoteException {
        LogEngine.trace("[HAS DELETED ITEMS]");
        return db.hasDeletedItems();
    }

    @Override
    public synchronized boolean startServer() {
        if (!isStarted()) {
            expensiveOpEventGenerator.triggerStartProgress("Starting server...");
            boolean stubOK = false;
            boolean dbOK = false;
            String errorMessage = null;
            try {
                expensiveOpEventGenerator.triggerUpdateProgress(33, "Binding to port " + rmiPort + "...", null);
                LogEngine.inform("Starting server on port " + rmiPort, null);
                if (rmiRegistry == null) {
                    rmiRegistry = LocateRegistry.createRegistry(rmiPort);
                }
                final RemoteServer stub = (RemoteServer) UnicastRemoteObject.exportObject(this, 0);
                rmiRegistry.rebind(Constants.PRODUCT_NAME, stub);
                stubOK = true;
                expensiveOpEventGenerator.triggerUpdateProgress(66, "Opening database...", null);
                db.open();
                dbOK = true;
                expensiveOpEventGenerator.triggerUpdateProgress(100, "Done", null);
                LogEngine.inform("Server started on port " + rmiPort, null);
                EventDispatcher.getInstance().addListener(eventListener);
                serverStarted = true;
            } catch (final RemoteException e) {
                errorMessage = "Could not start RMI server on port " + rmiPort + "!";
                LogEngine.warn(errorMessage, e);
            } catch (final LadybugException e) {
                errorMessage = "Could not open database!";
                LogEngine.warn(errorMessage, e);
            }
            if (!isStarted()) {
                if (stubOK) {
                    try {
                        UnicastRemoteObject.unexportObject(this, true);
                    } catch (final NoSuchObjectException ignored) {
                    }
                }
                if (dbOK) {
                    db.close();
                }
                EventDispatcher.getInstance().removeListener(eventListener);
                threadPool.shutdown();
            }
            expensiveOpEventGenerator.triggerEndProgress(errorMessage);
        }
        return isStarted();
    }

    @Override
    public synchronized boolean stopServer() {
        if (isStarted()) {
            try {
                serverStarted = false;
                LogEngine.inform("Stopping server...", null);
                UnicastRemoteObject.unexportObject(this, true);
                db.close();
                EventDispatcher.getInstance().removeListener(eventListener);
                threadPool.shutdown();
                LogEngine.inform("Server stopped", null);
            } catch (final NoSuchObjectException e) {
                LogEngine.debug("Unknown problem when stopping server", e);
            }
        }
        return !isStarted();
    }

    @Override
    public synchronized boolean isStarted() {
        return serverStarted;
    }

    private void resyncClients() {
        synchronized (remoteClients) {
            for (final Map.Entry<Long, RemoteClient> entry : remoteClients.entrySet()) {
                threadPool.execute(new ResyncThread(entry));
            }
        }
    }

    private void reprioritizeItems(final AbstractNamedItem oldItem, final AbstractNamedItem newItem) {
        final int lowPriority = Math.min(oldItem.getPriority(), newItem.getPriority());
        final int highPriority = Math.max(oldItem.getPriority(), newItem.getPriority());
        final int delta = (int) Math.signum(oldItem.getPriority() - newItem.getPriority());
        if (oldItem instanceof Release && newItem instanceof Release) {
            db.reprioritizeReleases((Release) oldItem, lowPriority, highPriority, delta);
        }
        if (oldItem instanceof Feature && newItem instanceof Feature) {
            db.reprioritizeFeatures((Feature) oldItem, lowPriority, highPriority, delta);
        }
        if (oldItem instanceof UserStory && newItem instanceof UserStory) {
            db.reprioritizeUserStories((UserStory) oldItem, lowPriority, highPriority, delta);
        }
        if (oldItem instanceof Task && newItem instanceof Task) {
            db.reprioritizeTasks((Task) oldItem, lowPriority, highPriority, delta);
        }
    }

    private class MyDispatchEventListener implements EventListener {

        private final Event[] listenedEvents = new Event[] { Event.SHUTDOWN };

        @Override
        public Event[] getListenedEvents() {
            return listenedEvents;
        }

        @Override
        public void update(final Event event, final EventGenerator generator) {
            if (event == Event.SHUTDOWN) {
                stopServer();
            }
        }
    }

    private class ResyncThread implements Runnable {

        private final Map.Entry<Long, RemoteClient> entry;

        public ResyncThread(final Map.Entry<Long, RemoteClient> entry) {
            this.entry = entry;
        }

        @Override
        public void run() {
            try {
                entry.getValue().resync();
            } catch (final RemoteException e) {
                try {
                    unregister(entry.getKey());
                } catch (RemoteException ignored) {
                    ignored = null;
                }
            }
        }
    }
}
