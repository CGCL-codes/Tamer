package net.sourceforge.javamatch.engine.test;

import java.util.*;
import javax.jdo.*;
import net.sourceforge.javamatch.test.*;

public class PersistenceManagerMock extends ExpectationList implements PersistenceManager {

    public void expectEvict(Object objectToEvict) {
        expect("evict: " + objectToEvict);
    }

    public void evict(Object objectToEvict) {
        encounter("evict: " + objectToEvict);
    }

    public boolean isClosed() {
        return false;
    }

    public void close() {
    }

    public Transaction currentTransaction() {
        return null;
    }

    public void evictAll(Object[] os) {
    }

    public void evictAll(Collection c) {
    }

    public void evictAll() {
    }

    public void refresh(Object o) {
    }

    public void refreshAll(Object[] os) {
    }

    public void refreshAll(Collection c) {
    }

    public void refreshAll() {
    }

    public Query newQuery() {
        return null;
    }

    public Query newQuery(Object o) {
        return null;
    }

    public Query newQuery(String s, Object o) {
        return null;
    }

    public Query newQuery(Class c) {
        return null;
    }

    public Query newQuery(Extent e) {
        return null;
    }

    public Query newQuery(Class cl, Collection co) {
        return null;
    }

    public Query newQuery(Class c, String s) {
        return null;
    }

    public Query newQuery(Class cl, Collection co, String s) {
        return null;
    }

    public Query newQuery(Extent e, String s) {
        return null;
    }

    public Extent getExtent(Class s, boolean b) {
        return null;
    }

    public Object getObjectById(Object o, boolean b) {
        return null;
    }

    public Object getObjectId(Object o) {
        return null;
    }

    public Object getTransactionalObjectId(Object o) {
        return null;
    }

    public Object newObjectIdInstance(Class c, String s) {
        return null;
    }

    public void makePersistent(Object o) {
    }

    public void makePersistentAll(Object[] os) {
    }

    public void makePersistentAll(Collection c) {
    }

    public void deletePersistent(Object o) {
    }

    public void deletePersistentAll(Object[] os) {
    }

    public void deletePersistentAll(Collection c) {
    }

    public void makeTransient(Object o) {
    }

    public void makeTransientAll(Object[] os) {
    }

    public void makeTransientAll(Collection c) {
    }

    public void makeTransactional(Object o) {
    }

    public void makeTransactionalAll(Object[] os) {
    }

    public void makeTransactionalAll(Collection c) {
    }

    public void makeNontransactional(Object o) {
    }

    public void makeNontransactionalAll(Object[] os) {
    }

    public void makeNontransactionalAll(Collection c) {
    }

    public void retrieve(Object o) {
    }

    public void retrieveAll(Collection c) {
    }

    public void retrieveAll(Collection c, boolean b) {
    }

    public void retrieveAll(Object[] o) {
    }

    public void retrieveAll(Object[] os, boolean b) {
    }

    public void setUserObject(Object o) {
    }

    public Object getUserObject() {
        return null;
    }

    public PersistenceManagerFactory getPersistenceManagerFactory() {
        return null;
    }

    public Class getObjectIdClass(Class c) {
        return null;
    }

    public void setMultithreaded(boolean b) {
    }

    public boolean getMultithreaded() {
        return false;
    }

    public void setIgnoreCache(boolean b) {
    }

    public boolean getIgnoreCache() {
        return false;
    }
}
