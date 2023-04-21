package org.hypergraphdb.type;

import org.hypergraphdb.HGHandle;
import org.hypergraphdb.HGPersistentHandle;
import org.hypergraphdb.HyperGraph;
import org.hypergraphdb.HGException;
import org.hypergraphdb.IncidenceSetRef;
import org.hypergraphdb.LazyRef;
import java.io.*;

/**
 * 
 * <p>
 * This type implementation handles values as serializable Java blobs. 
 * </p>
 *
 * @author Borislav Iordanov
 *
 */
public class SerializableType implements HGAtomType {

    /**
     * <p>An <code>ObjectInputStream</code> that uses the thread context class loader to
     * resolve classes.</p>
     * 
     * @author Borislav Iordanov
     *
     */
    public static class SerInputStream extends ObjectInputStream {

        private HyperGraph graph;

        public SerInputStream() throws IOException {
        }

        public SerInputStream(HyperGraph graph) throws IOException {
            super();
            this.graph = graph;
        }

        public SerInputStream(InputStream in, HyperGraph graph) throws IOException {
            super(in);
            this.graph = graph;
        }

        @Override
        protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
            try {
                return super.resolveClass(desc);
            } catch (Exception ex) {
                ClassLoader cl = graph == null ? null : graph.getConfig().getClassLoader();
                if (cl == null) cl = Thread.currentThread().getContextClassLoader();
                if (cl != null) return cl.loadClass(desc.getName()); else if (ex instanceof IOException) throw (IOException) ex; else throw (ClassNotFoundException) ex;
            }
        }
    }

    private HyperGraph hg;

    public SerializableType() {
    }

    public void setHyperGraph(HyperGraph hg) {
        this.hg = hg;
    }

    public Object make(HGPersistentHandle handle, LazyRef<HGHandle[]> targetSet, IncidenceSetRef incidenceSet) {
        try {
            ByteArrayInputStream in = new ByteArrayInputStream(hg.getStore().getData(handle));
            ObjectInputStream objectIn = new SerInputStream(in, this.hg);
            return objectIn.readObject();
        } catch (Exception ex) {
            throw new HGException(ex);
        }
    }

    public void release(HGPersistentHandle handle) {
        hg.getStore().removeData(handle);
    }

    public HGPersistentHandle store(Object instance) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ObjectOutputStream objectOut = new ObjectOutputStream(out);
            objectOut.writeObject(instance);
            objectOut.flush();
            return hg.getStore().store(out.toByteArray());
        } catch (IOException ex) {
            throw new HGException(ex);
        }
    }

    public boolean subsumes(Object general, Object specific) {
        return false;
    }
}
