package org.nakedobjects.nof.core.context;

import org.nakedobjects.noa.adapter.NakedObjectLoader;
import org.nakedobjects.noa.persist.NakedObjectPersistor;
import org.nakedobjects.noa.reflect.NakedObjectReflector;
import org.nakedobjects.noa.security.Session;
import org.nakedobjects.nof.core.image.TemplateImageLoader;
import org.nakedobjects.nof.core.util.DebugInfo;
import org.nakedobjects.nof.core.util.DebugString;
import org.nakedobjects.nof.core.util.ToString;
import org.nakedobjects.object.MessageBroker;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.log4j.Logger;

public class NakedObjectsData implements ContextDebug {

    private static final Logger LOG = Logger.getLogger(NakedObjectsData.class);

    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("dd/MM HH:mm:ss,SSS");

    private static int nextId = 1;

    private final int id = nextId++;

    protected NakedObjectLoader objectLoader;

    protected NakedObjectPersistor objectPersistor;

    protected NakedObjectReflector reflector;

    protected MessageBroker messageBroker = new SimpleMessageBroker();

    protected UpdateNotifier updateNotifier = new SimpleUpdateNotifier();

    protected Session session;

    protected TemplateImageLoader templateImageLoader;

    protected long accessTime;

    private String snapshot;

    public String toString() {
        ToString toString = new ToString(this);
        toString.append("context", executionContextId());
        toString.append("objectPersistor", objectPersistor);
        toString.append("session", session);
        toString.append("messageBroker", messageBroker);
        toString.append("objectLoader", objectLoader);
        toString.append("imageLoader", templateImageLoader);
        return toString.toString();
    }

    public String executionContextId() {
        return "#" + id + (session == null ? "" : "/" + session.getUserName());
    }

    public void debug(DebugString debug) {
        debug.appendAsHexln("hash", hashCode());
        debug.appendln("context id", id);
        debug.appendln("accessed", FORMAT.format(new Date(accessTime)));
        debug.appendln("image tmpl", templateImageLoader);
        debug.appendln("reflector", reflector);
        debug.appendln("loader", objectLoader);
        debug.appendln("persistor", objectPersistor);
        debug.appendln("updates", updateNotifier);
        debug.appendln("message broker", messageBroker);
        debug.appendln("session", session);
    }

    public void debugAll(DebugString debug) {
        debug.startSection("Naked Objects Context Snapshot");
        debug.appendln(snapshot);
        debug.endSection();
    }

    private void debug(DebugString debug, Object object) {
        if (object instanceof DebugInfo) {
            DebugInfo d = (DebugInfo) object;
            debug.startSection(d.debugTitle());
            d.debugData(debug);
            debug.endSection();
        } else {
            debug.appendln("no debug for " + object);
        }
    }

    public void takeSnapshot() {
        DebugString debug = new DebugString();
        debug(debug);
        debug.indent();
        debug.appendln();
        debug(debug, objectLoader);
        debug(debug, objectPersistor);
        debug(debug, updateNotifier);
        debug(debug, messageBroker);
        snapshot = debug.toString();
        LOG.debug(snapshot);
    }
}
