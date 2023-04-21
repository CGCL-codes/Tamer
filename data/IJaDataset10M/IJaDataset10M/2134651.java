package ucm.si.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 *
 * @author Niko, Jose Antonio, Ivan
 */
public class TLG<S> {

    private TLG<S> ant = null;

    private TreeMap<S, Set<S>> tabla;

    public TLG() {
        this.tabla = new TreeMap<S, Set<S>>();
    }

    public TLG(int tam) {
        this.tabla = new TreeMap<S, Set<S>>();
    }

    public TLG(TLG<S> t) {
        this.tabla = new TreeMap<S, Set<S>>();
        this.ant = t;
    }

    public Set<S> getHijo(S e) {
        Set<S> set;
        if (this.tabla.containsKey(e)) {
            set = this.tabla.get(e);
        } else {
            set = new TreeSet<S>();
        }
        if (ant != null) {
            Set<S> setant = ant.getHijo(e);
            if ((setant != null) && (setant.size() > 0)) {
                set.addAll(setant);
            }
        }
        this.tabla.put(e, set);
        return set;
    }

    public void setArista(S eini, S efin) {
        Set<S> l;
        if (this.tabla.containsKey(eini)) {
            l = this.tabla.get(eini);
            l.add(efin);
        } else {
            l = new TreeSet<S>();
            l.add(efin);
            this.tabla.put(eini, l);
        }
    }

    public void setAristas(S eini, Set<S> c) {
        this.tabla.put(eini, c);
    }

    public void addAristas(S eini, Set<S> c) {
        if (this.tabla.containsKey(eini)) {
            this.tabla.get(eini).addAll(c);
        } else {
            this.tabla.put(eini, c);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TLG<S> other = (TLG<S>) obj;
        if (this.ant != other.ant && (this.ant == null || !this.ant.equals(other.ant))) {
            return false;
        }
        if (this.tabla != other.tabla && (this.tabla == null || !this.tabla.equals(other.tabla))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 31 * hash + (this.ant != null ? this.ant.hashCode() : 0);
        hash = 31 * hash + (this.tabla != null ? this.tabla.hashCode() : 0);
        return hash;
    }

    public int size() {
        return this.tabla.size();
    }
}
