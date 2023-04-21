package jsr166.contrib.uncontended;

import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;

class ReadWriteLockedList<E> extends ReadWriteLockedCollection<E, List<E>> implements List<E> {

    public ReadWriteLockedList(List<E> base, ReadWriteLock rwLock) {
        super(base, rwLock);
    }

    public void add(int arg0, E arg1) {
        rwLock.writeLock().lock();
        try {
            base.add(arg0, arg1);
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    public boolean addAll(int arg0, Collection<? extends E> arg1) {
        rwLock.writeLock().lock();
        try {
            return base.addAll(arg0, arg1);
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    public E get(int arg0) {
        rwLock.readLock().lock();
        try {
            return base.get(arg0);
        } finally {
            rwLock.readLock().unlock();
        }
    }

    public int indexOf(Object arg0) {
        rwLock.readLock().lock();
        try {
            return base.indexOf(arg0);
        } finally {
            rwLock.readLock().unlock();
        }
    }

    public int lastIndexOf(Object arg0) {
        rwLock.readLock().lock();
        try {
            return base.lastIndexOf(arg0);
        } finally {
            rwLock.readLock().unlock();
        }
    }

    public ListIterator<E> listIterator() {
        rwLock.readLock().lock();
        try {
            return new ReadWriteLockedListIterator<E>(base.listIterator(), rwLock);
        } finally {
            rwLock.readLock().unlock();
        }
    }

    public ListIterator<E> listIterator(int arg0) {
        rwLock.readLock().lock();
        try {
            return new ReadWriteLockedListIterator<E>(base.listIterator(arg0), rwLock);
        } finally {
            rwLock.readLock().unlock();
        }
    }

    public E remove(int arg0) {
        rwLock.writeLock().lock();
        try {
            return base.remove(arg0);
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    public E set(int arg0, E arg1) {
        rwLock.writeLock().lock();
        try {
            return base.set(arg0, arg1);
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    public List<E> subList(int arg0, int arg1) {
        rwLock.readLock().lock();
        try {
            return new ReadWriteLockedList<E>(base.subList(arg0, arg1), rwLock);
        } finally {
            rwLock.readLock().unlock();
        }
    }
}
