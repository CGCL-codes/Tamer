package net.aequologica.openmozart;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import net.aequologica.openmozart.model.Root;

public class Anagrams implements Collection<String> {

    public static final String __ID__ = "$Id: AnagramsBean.java,v 1.2 2004/10/01 14:17:14 Administrator Exp $";

    private static final Logger log = Logger.getLogger(Anagrams.class.getName());

    private static Anagrams _instance = null;

    private String _root;

    private Vector<Integer> _shuffle = new Vector<Integer>();

    private ArrayList<String> _anagrams = new ArrayList<String>();

    public static synchronized Anagrams getInstance() {
        if (_instance == null) {
            _instance = new Anagrams();
        }
        return _instance;
    }

    private void shuffle() {
        synchronized (_shuffle) {
            Random generator = new Random();
            Vector<Integer> sequenz = new Vector<Integer>();
            _shuffle.clear();
            int count = 1;
            for (int i = 2; i <= _root.length(); i++) {
                count *= i;
            }
            for (int i = 0; i < count; i++) {
                sequenz.add(new Integer(i));
            }
            for (int i = 0; i < count; i++) {
                int siz = sequenz.size();
                int num = 0;
                if (siz > 2) {
                    num = (int) (Math.round(generator.nextFloat() * siz) % siz);
                }
                Integer obj = null;
                if (num < siz) {
                    obj = sequenz.elementAt(num);
                }
                if (obj != null) {
                    _shuffle.add(obj);
                    sequenz.removeElementAt(num);
                }
            }
        }
    }

    private void turn_left(StringBuffer buff, int level) {
        for (int i = 0; i < level; i++) {
            char tmp = buff.charAt(0);
            for (int c = 0; c < (level - 1); c++) {
                buff.setCharAt(c, buff.charAt(c + 1));
            }
            buff.setCharAt(level - 1, tmp);
            if (level > 2) {
                turn_left(buff, level - 1);
            } else {
                String result = buff.toString();
                _anagrams.add(result);
            }
        }
    }

    public String getRoot() {
        return _root;
    }

    public void setRoot(String root) {
        if (root == null) {
            _root = root;
            _anagrams.clear();
            return;
        }
        if ((root.length() < 1) || (root.length() > 6)) {
            return;
        }
        if (_root != null) {
            if (_root.equals(root)) {
                shuffle();
                return;
            }
        }
        _root = root;
        _anagrams.clear();
        StringBuffer buff = new StringBuffer(_root);
        turn_left(buff, _root.length());
        shuffle();
        EntityManager em = null;
        try {
            em = EMF.get().createEntityManager();
            Root _root = em.find(Root.class, new Long(1L));
            if (_root == null) {
                _root = new Root();
                _root.setName(root);
            } else {
                _root.setName(root);
            }
            em.merge(_root);
        } catch (Exception e) {
            log.log(Level.WARNING, _root, e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public int size() {
        return _anagrams.size();
    }

    public boolean isEmpty() {
        return _anagrams.isEmpty();
    }

    public boolean contains(Object p0) {
        return _anagrams.contains(p0);
    }

    public Object[] toArray() {
        return _anagrams.toArray();
    }

    public boolean add(String p0) {
        return _anagrams.add(p0);
    }

    public boolean remove(Object p0) {
        return _anagrams.remove(p0);
    }

    public void clear() {
        _anagrams.clear();
    }

    public boolean equals(Object p0) {
        return _anagrams.equals(p0);
    }

    public int hashCode() {
        return _anagrams.hashCode();
    }

    private class ShuffledIterator implements Iterator<String> {

        Iterator<Integer> i = _shuffle.iterator();

        public boolean hasNext() {
            return i.hasNext();
        }

        public String next() {
            return _anagrams.get(i.next());
        }

        public void remove() {
        }
    }

    public Iterator<String> iterator() {
        return new ShuffledIterator();
    }

    @Override
    public boolean addAll(Collection<? extends String> c) {
        return _anagrams.addAll(c);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return _anagrams.containsAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return _anagrams.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return _anagrams.retainAll(c);
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return _anagrams.toArray(a);
    }
}
