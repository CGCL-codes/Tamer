package apollo.datamodel;

import java.util.*;
import java.io.IOException;
import java.lang.String;
import apollo.gui.event.*;
import apollo.gui.*;
import apollo.dataadapter.*;

public abstract class AbstractLazySequence extends AbstractSequence implements LazySequenceI {

    LazyLoadControlledObject llco;

    CacheSequenceLoader cacher;

    public AbstractLazySequence(String id, Controller c) {
        super(id);
        setController(c);
        setCacher(new CacheSequenceLoader(this));
    }

    class LazyLoadControlledObject extends NonWindowControlledObject {

        public LazyLoadControlledObject(Controller c) {
            super(c);
        }

        public void fireLazyLoadEvent(int type) {
            controller.handleLazyLoadEvent(new LazyLoadEvent(this, getDisplayId(), type, LazyLoadEvent.SEQUENCE));
        }
    }

    public abstract SequenceI getSubseq(int start, int end);

    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        LazyLoadControlledObject tmpl = llco;
        CacheSequenceLoader tmpc = cacher;
        llco = null;
        cacher = null;
        out.defaultWriteObject();
        llco = tmpl;
        cacher = tmpc;
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        setController(Config.getController());
        setCacher(new CacheSequenceLoader(this));
    }

    public void setController(Controller c) {
        llco = new LazyLoadControlledObject(c);
    }

    public void setCacher(CacheSequenceLoader csl) {
        cacher = csl;
    }

    public CacheSequenceLoader getCacher() {
        return cacher;
    }

    protected String getResiduesImpl(int start, int end) {
        return cacher.getResidues(start, end);
    }

    protected String getResiduesImpl(int start) {
        return cacher.getResidues(start, length);
    }

    public String getResiduesFromSource(int low, int high) {
        llco.fireLazyLoadEvent(LazyLoadEvent.BEFORE_LOAD);
        String seq = getResiduesFromSourceImpl(low, high);
        llco.fireLazyLoadEvent(LazyLoadEvent.AFTER_LOAD);
        return seq;
    }

    protected abstract String getResiduesFromSourceImpl(int low, int high);
}
