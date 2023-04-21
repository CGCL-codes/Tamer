package net.bos.groupware.addressbook;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author <a href="mailto:vecchio.fabrizio@payroll.it">Vecchio Fabrizio</a>
 */
public class CacheDirectory implements Directory {

    /** */
    protected Directory _wrappedDir;

    /** */
    protected List<Contact> _cache;

    /** */
    protected Integer _contactCount = null;

    /** Creates a new instance of CacheDirectory */
    public CacheDirectory(Directory wrappedDir) {
        _wrappedDir = wrappedDir;
    }

    /**
     *
     */
    public int getContactCount() {
        if (_contactCount == null) {
            _contactCount = _wrappedDir.getContactCount();
            _cache = new ArrayList<Contact>(getContactCount());
            for (int i = 0; i < _contactCount; i++) _cache.add(null);
        }
        return _contactCount;
    }

    /**
     *
     */
    public Contact getContactAt(int index) {
        Contact contact = _cache.get(index);
        if (contact == null) {
            contact = _wrappedDir.getContactAt(index);
            _cache.set(index, contact);
        }
        return contact;
    }

    /**
     *
     */
    public void cache() {
        for (int i = 0; i < getContactCount(); i++) getContactAt(i);
    }

    /**
     *
     */
    public void clear() {
        _cache.clear();
        _contactCount = null;
    }
}
