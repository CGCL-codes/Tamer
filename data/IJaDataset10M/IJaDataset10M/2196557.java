package sagex.remote;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import sagex.SageAPI;
import sagex.util.ILog;
import sagex.util.LogProvider;

/**
 * Accepts RemoteRequest object and returns a RemoteResponse object.
 * 
 * The RemoteRequest is decoded and passed off to the SageAPI. The response from
 * sage is then placed into the RemoteResponse.
 * 
 * All native sage objects are converted into RemoteObjectRef objects, so they
 * are never sent back directly. All object arrays that are not serializable are
 * also sent back as RemoteObjectRef objects. Serializable Objects and Arrays
 * are sent back in full.
 * 
 * Local Object References are stored in a WeakHashMap so there is the change
 * that objects in the Map will disappear before they are accessed. This will
 * have undetermined results in the calling application.
 * 
 * @author seans
 * 
 */
public abstract class AbstractRPCHandler implements IRCPHandler {

    protected ILog log = LogProvider.getLogger(this.getClass());

    /**
	 * Simple object reference for tracking when a reference has been accessed.
	 * @author sean
	 */
    private class ObjRef {

        private long accessed;

        private Object object;

        public ObjRef(Object o) {
            this.object = o;
            this.accessed = System.currentTimeMillis();
        }
    }

    private Map<String, ObjRef> objectRefs = new HashMap<String, ObjRef>();

    public AbstractRPCHandler() {
        RemoteObjectReaper.getInstance().manageObjects(this);
    }

    public void handleRPCCall(RemoteRequest request, RemoteResponse response) {
        try {
            Object oArr[] = request.getParameters();
            if (oArr != null && oArr.length > 0) {
                for (int i = 0; i < oArr.length; i++) {
                    Object o = oArr[i];
                    if (o == null) continue;
                    if (o.getClass().isArray() && RemoteObjectRef.class.isAssignableFrom(o.getClass().getComponentType())) {
                        log.debug("Converting Remote Object Reference Array into a Sage Array.");
                        Object oo[] = (Object[]) o;
                        if (oo.length > 0) {
                            oArr[i] = getReference((RemoteObjectRef) oo[0]);
                        } else {
                            oArr[i] = new Object[0];
                        }
                    } else if (o instanceof RemoteObjectRef) {
                        log.debug("Converting Remote Object Reference into a Sage Reference.");
                        RemoteObjectRef ref = (RemoteObjectRef) o;
                        Object oref = getReference(ref);
                        if (oref.getClass().isArray() && ref.getIndex() != -1) {
                            oArr[i] = ((Object[]) oref)[ref.getIndex()];
                        } else {
                            oArr[i] = oref;
                        }
                    }
                }
            }
            Object oreply = null;
            if (request.getContext() != null) {
                oreply = SageAPI.call(request.getContext(), request.getCommand(), request.getParameters());
            } else {
                oreply = SageAPI.call(request.getCommand(), request.getParameters());
            }
            Object finalReply = null;
            RemoteObjectRef replyRef = null;
            if (oreply != null) {
                if (oreply instanceof Vector && ((Vector) oreply).size() > 0 && !isSerializable(((Vector) oreply).get(0))) {
                    Vector v = (Vector) oreply;
                    Vector newV = new Vector(v.size());
                    for (Object o : v) {
                        RemoteObjectRef ror = new RemoteObjectRef();
                        setReference(ror, o);
                        newV.add(ror);
                    }
                    oreply = newV;
                }
                if (oreply.getClass().isArray()) {
                    if (oreply.getClass().getComponentType().isPrimitive() || Serializable.class.isAssignableFrom(oreply.getClass().getComponentType())) {
                        finalReply = oreply;
                    } else {
                        System.out.println("Converting Sage Object Array into a Remote Object Reference Array.");
                        replyRef = new RemoteObjectRef(((Object[]) oreply));
                        finalReply = replyRef.getRemoteObjectReferenceArray();
                    }
                } else {
                    if (isSerializable(oreply)) {
                        finalReply = oreply;
                    } else {
                        System.out.println("Converting Sage Object into a Remote Object Reference.");
                        replyRef = new RemoteObjectRef();
                        finalReply = replyRef;
                    }
                }
            }
            if (replyRef != null) {
                setReference(replyRef, oreply);
            }
            response.setData(finalReply);
        } catch (Throwable t) {
            log.warn(String.format("----------- Sage Handling of a Remote Command Failed: %s ---------\n", request), t);
            response.setError(404, "Command Failed: " + (request != null ? request.getCommand() : ""), t);
        }
    }

    protected boolean isSerializable(Object oreply) {
        if (oreply == null) return false;
        if (oreply.getClass().isPrimitive() || Serializable.class.isAssignableFrom(oreply.getClass())) {
            return true;
        }
        return false;
    }

    public Object getReference(RemoteObjectRef ref) {
        if (ref == null) throw new RuntimeException("RemoteObjectRef is null");
        try {
            ObjRef r = objectRefs.get(ref.getId());
            if (r == null) {
                throw new RuntimeException("No object reference for " + ref.getId());
            }
            r.accessed = System.currentTimeMillis();
            return r.object;
        } catch (Throwable t) {
            throw new RuntimeException("Invalid Object Reference: " + ref.getId() + "; It may be that the reference has been cleaned up.", t);
        }
    }

    public void setReference(RemoteObjectRef ref, Object o) {
        log.debug("Adding Object Reference: " + ref.getId());
        objectRefs.put(ref.getId(), new ObjRef(o));
    }

    /**
	 * Iterates the object references, and cleans out any objects that have not been accessed in the maxexpiry time
	 * 
	 * @param maxexpiry expire objects that have not been accessed since this many ms
	 */
    public void cleanObjectReferences(long maxexpiry) {
        int removed = 0;
        for (Iterator<Map.Entry<String, ObjRef>> i = objectRefs.entrySet().iterator(); i.hasNext(); ) {
            Map.Entry<String, ObjRef> me = i.next();
            if (System.currentTimeMillis() - me.getValue().accessed > maxexpiry) {
                i.remove();
                removed++;
            }
        }
        if (removed > 0) {
            log.info("Removed " + removed + " stale remote objects.");
        }
    }
}
