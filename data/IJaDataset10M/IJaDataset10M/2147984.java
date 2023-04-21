package org.nakedobjects.plugins.htmlviewer.request;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.Map.Entry;
import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.commons.ensure.Assert;
import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAction;
import org.nakedobjects.plugins.htmlviewer.component.Block;
import org.nakedobjects.plugins.htmlviewer.component.ComponentFactory;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.persistence.ConcurrencyException;
import org.nakedobjects.runtime.persistence.adaptermanager.AdapterManager;
import org.nakedobjects.runtime.userprofile.UserProfile;

public class Context {

    private static final Logger LOG = Logger.getLogger(Context.class);

    private final Map actionMap = new HashMap();

    private final ComponentFactory componentFactory;

    private final ObjectHistory history = new ObjectHistory();

    private boolean isValid;

    private int max;

    private final Map<String, CollectionMapping> collectionMap = new HashMap<String, CollectionMapping>();

    private final Map<String, ObjectMapping> objectMap = new HashMap<String, ObjectMapping>();

    private final Map<String, ObjectMapping> serviceMap = new HashMap<String, ObjectMapping>();

    private final Stack<Crumb> crumbs = new Stack<Crumb>();

    private final List<String> messages = new ArrayList<String>();

    private final List<String> warnings = new ArrayList<String>();

    private AuthenticationSession session;

    public Context(final ComponentFactory factory) {
        componentFactory = factory;
        isValid = true;
        clearMessagesAndWarnings();
        LOG.debug(this + " created with " + factory);
    }

    public void setObjectCrumb(final NakedObject object) {
        while (crumbs.size() > 0 && !(crumbs.lastElement() instanceof TaskCrumb)) {
            crumbs.pop();
        }
        final String id = mapObject(object);
        crumbs.push(new ObjectCrumb(id, object));
    }

    public void addCollectionFieldCrumb(final String collectionFieldName) {
        crumbs.push(new ObjectFieldCrumb(collectionFieldName));
    }

    public void addCollectionCrumb(final String id) {
        while (crumbs.size() > 0 && !(crumbs.lastElement() instanceof TaskCrumb)) {
            crumbs.pop();
        }
        crumbs.push(new CollectionCrumb(id, getMappedCollection(id)));
    }

    public void addTaskCrumb(final Task task) {
        while (crumbs.size() > 1 && !(crumbs.lastElement() instanceof ObjectCrumb)) {
            crumbs.pop();
        }
        Assert.assertNotNull(task);
        Assert.assertTrue(!isTask());
        task.init(this);
        crumbs.push(new TaskCrumb(task));
    }

    public void debug(final DebugString debug) {
        debug.startSection("Web Session Context");
        debug.appendAsHexln("hash", hashCode());
        debug.appendln("session", session);
        debug.appendln("is valid", isValid);
        debug.appendln("next id", max);
        debug.appendln("factory", componentFactory);
        debug.appendln("is task", isTask());
        debug.appendln("crumbs (" + crumbs.size() + ")");
        debug.indent();
        for (int i = 0; i < crumbs.size(); i++) {
            final Crumb crumb = (Crumb) crumbs.get(i);
            debug.appendln(i + 1 + ". " + crumb);
            debug.indent();
            crumb.debug(debug);
            debug.unindent();
        }
        debug.unindent();
        debug.startSection("Objects");
        for (String id : objectMap.keySet()) {
            final ObjectMapping object = objectMap.get(id);
            debug.appendln(id + " -> " + object.getOid());
            debug.indent();
            object.debug(debug);
            debug.unindent();
        }
        debug.endSection();
        debug.startSection("Collections");
        for (String id : collectionMap.keySet()) {
            final CollectionMapping coll = collectionMap.get(id);
            debug.appendln(id + " -> collection of " + coll.getElementSpecification().getPluralName());
            coll.debug(debug);
        }
        debug.endSection();
        debug.startSection("Actions");
        debugMap(debug, actionMap);
        debug.endSection();
        debug.startSection("History");
        history.debug(debug);
        debug.endSection();
        debug.endSection();
    }

    private void debugMap(final DebugString debug, final Map map) {
        final Iterator names = map.keySet().iterator();
        while (names.hasNext()) {
            final String name = (String) names.next();
            debug.appendln(name + " -> " + map.get(name));
        }
    }

    public NakedObjectAction getMappedAction(final String id) {
        return (NakedObjectAction) getMappedInstance(actionMap, id);
    }

    public ComponentFactory getComponentFactory() {
        return componentFactory;
    }

    /**
     * Returns an array of instances of the specified type that are currently known in the current context, ie
     * have been recently seen by the user.
     * 
     * <p>
     * These will be resolved if required, with a transaction created (and ended) if required.
     */
    public NakedObject[] getKnownInstances(final NakedObjectSpecification type) {
        final List<NakedObject> instances = new ArrayList<NakedObject>();
        for (String id : objectMap.keySet()) {
            final NakedObject adapter = getMappedObject(id);
            NakedObjectsContext.getPersistenceSession().resolveImmediately(adapter);
            if (adapter.getSpecification().isOfType(type)) {
                instances.add(adapter);
            }
        }
        final NakedObject[] array = new NakedObject[instances.size()];
        instances.toArray(array);
        return array;
    }

    private String addToMap(final Map map, final Object object) {
        Assert.assertNotNull(object);
        if (map.containsValue(object)) {
            return findExistingId(map, object);
        } else {
            return mapNewObject(map, object);
        }
    }

    private String mapNewObject(final Map map, final Object object) {
        max++;
        final String id = "" + max;
        map.put(id, object);
        final String mapName = map == objectMap ? "object" : (map == collectionMap ? "collection" : "action");
        LOG.debug("add " + object + " to " + mapName + " as #" + id);
        return id;
    }

    private String findExistingId(final Map<String, ?> map, final Object object) {
        for (String id : map.keySet()) {
            if (object.equals(map.get(id))) {
                return id;
            }
        }
        throw new NakedObjectException();
    }

    private Object getMappedInstance(final Map map, final String id) {
        final Object object = map.get(id);
        if (object == null) {
            final String mapName = (map == objectMap) ? "object" : (map == collectionMap ? "collection" : "action");
            throw new ObjectLookupException("No object in " + mapName + " map with id " + id);
        }
        return object;
    }

    public NakedObject getMappedCollection(final String id) {
        final CollectionMapping map = (CollectionMapping) getMappedInstance(collectionMap, id);
        return map.getCollection(this);
    }

    public NakedObject getMappedObject(final String id) {
        final ObjectMapping mappedObject = (ObjectMapping) getMappedInstance(objectMap, id);
        final NakedObject object = mappedObject.getObject();
        if (object.isPersistent() && object.getResolveState().isGhost()) {
            NakedObjectsContext.getPersistenceSession().resolveImmediately(object);
        }
        try {
            mappedObject.checkVersion(object);
        } catch (final ConcurrencyException e) {
            LOG.info("concurrency conflict: " + e.getMessage());
            messages.clear();
            messages.add(e.getMessage());
            messages.add("Reloaded object " + object.titleString());
            updateVersion(object);
        }
        return object;
    }

    public Task getTask(final String taskId) {
        Task task = null;
        for (int i = crumbs.size() - 1; i >= 0; i--) {
            final Object crumb = crumbs.get(i);
            if (crumb instanceof TaskCrumb) {
                final TaskCrumb taskCrumb = (TaskCrumb) crumb;
                final String id = taskCrumb.getTask().getId();
                if (taskId.equals(id)) {
                    task = taskCrumb.getTask();
                    break;
                }
            }
        }
        return task;
    }

    public Request cancelTask(final Task task) {
        if (task != null) {
            endTask(task);
        }
        final Crumb crumb = (Crumb) crumbs.get(crumbs.size() - 1);
        return crumb.changeContext();
    }

    public void invalidate() {
        isValid = false;
    }

    public boolean isValid() {
        return isValid;
    }

    public boolean isLoggedIn() {
        return session != null;
    }

    public String mapAction(final NakedObjectAction action) {
        return addToMap(actionMap, action);
    }

    public String mapObject(final NakedObject adapter) {
        ObjectMapping mapping = null;
        if (adapter.isTransient()) {
            mapping = new TransientObjectMapping(adapter);
        } else {
            mapping = new PersistentObjectMapping(adapter);
        }
        return addToMap(objectMap, mapping);
    }

    public String mapCollection(final NakedObject collection) {
        final CollectionMapping map = new CollectionMapping(this, collection);
        return addToMap(collectionMap, map);
    }

    public void endTask(final Task task) {
        for (int i = crumbs.size() - 1; i >= 0; i--) {
            final Object crumb = crumbs.get(i);
            if (crumb instanceof TaskCrumb) {
                final TaskCrumb taskCrumb = (TaskCrumb) crumb;
                if (taskCrumb.getTask() == task) {
                    crumbs.remove(taskCrumb);
                    return;
                }
            }
        }
        throw new NakedObjectException("No crumb found for " + task);
    }

    public Crumb[] getCrumbs() {
        final int size = crumbs.size();
        final Crumb[] taskList = new Crumb[size];
        for (int i = 0; i < crumbs.size(); i++) {
            taskList[i] = (Crumb) crumbs.get(i);
        }
        return taskList;
    }

    public boolean[] isLinked() {
        final int size = crumbs.size();
        final boolean[] isLinked = new boolean[size];
        for (int i = size - 1; i >= 0; i--) {
            final boolean isTask = crumbs.elementAt(i) instanceof TaskCrumb;
            isLinked[i] = i != size - 1;
            if (isTask) {
                break;
            }
        }
        return isLinked;
    }

    public List<String> getMessages() {
        return messages;
    }

    public String getMessage(int i) {
        return messages.get(i);
    }

    public List<String> getWarnings() {
        return warnings;
    }

    public String getWarning(int i) {
        return warnings.get(i);
    }

    public void setMessagesAndWarnings(final List<String> messages, final List<String> warnings) {
        this.messages.clear();
        this.messages.addAll(messages);
        this.warnings.clear();
        this.warnings.addAll(warnings);
    }

    public void clearMessagesAndWarnings() {
        messages.clear();
        warnings.clear();
    }

    private boolean isTask() {
        final int index = crumbs.size() - 1;
        return index >= 0 && crumbs.get(index) instanceof TaskCrumb;
    }

    public Request changeContext(final int id) {
        while (crumbs.size() - 1 > id) {
            crumbs.pop();
        }
        final Crumb c = (Crumb) crumbs.lastElement();
        return c.changeContext();
    }

    public void setSession(final AuthenticationSession currentSession) {
        this.session = currentSession;
    }

    public AuthenticationSession getSession() {
        return session;
    }

    public void purge() {
        actionMap.clear();
        clearMessagesAndWarnings();
        final Map newCollectionMap = new HashMap();
        final Map newObjectMap = new HashMap();
        final Enumeration e = history.elements();
        while (e.hasMoreElements()) {
            final HistoryEntry entry = (HistoryEntry) e.nextElement();
            if (entry.type == HistoryEntry.OBJECT) {
                final Object item = objectMap.get(entry.id);
                newObjectMap.put(entry.id, item);
                LOG.debug("copied object map " + entry.id + " for " + item);
                ((ObjectMapping) item).updateVersion();
            } else if (entry.type == HistoryEntry.COLLECTION) {
                final CollectionMapping coll = (CollectionMapping) collectionMap.get(entry.id);
                newCollectionMap.put(entry.id, coll);
                LOG.debug("copied collection map for " + coll);
                final Enumeration e1 = coll.elements();
                while (e1.hasMoreElements()) {
                    final String id1 = (String) e1.nextElement();
                    final Object item = objectMap.get(id1);
                    newObjectMap.put(id1, item);
                    LOG.debug("copied object map " + id1 + " for " + item);
                    ((ObjectMapping) item).updateVersion();
                }
            }
        }
        collectionMap.clear();
        collectionMap.putAll(newCollectionMap);
        objectMap.clear();
        objectMap.putAll(newObjectMap);
        objectMap.putAll(serviceMap);
    }

    public void restoreAllObjectsToLoader() {
        final Set oidSet = objectMap.entrySet();
        for (final Iterator it = oidSet.iterator(); it.hasNext(); ) {
            final ObjectMapping mapping = (ObjectMapping) ((Entry) it.next()).getValue();
            mapping.restoreToLoader();
        }
    }

    public void listHistory(final Context context, final Block navigation) {
        history.listObjects(context, navigation);
    }

    public void addObjectToHistory(final String idString) {
        history.addObject(idString);
    }

    public void addCollectionToHistory(final String idString) {
        history.addCollection(idString);
    }

    public void init() {
        AdapterManager adapterManager = NakedObjectsContext.getPersistenceSession().getAdapterManager();
        List<Object> services = getUserProfile().getPerspective().getServices();
        for (Object service : services) {
            NakedObject serviceAdapter = adapterManager.adapterFor(service);
            if (serviceAdapter == null) {
                LOG.warn("unable to find service: " + service + "; skipping");
                continue;
            }
            mapObject(serviceAdapter);
        }
        serviceMap.putAll(objectMap);
    }

    public void updateVersion(final NakedObject adapter) {
        if (adapter.isTransient()) {
            return;
        }
        final String id = mapObject(adapter);
        if (id != null) {
            final ObjectMapping mapping = new PersistentObjectMapping(adapter);
            objectMap.put(id, mapping);
        }
    }

    private static UserProfile getUserProfile() {
        return NakedObjectsContext.getUserProfile();
    }
}
