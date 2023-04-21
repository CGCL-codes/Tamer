package fr.aliacom.dbmjui.driver;

import java.util.Iterator;
import org.apache.log4j.Logger;

/**
 * @author tom
 *
 * (c) 2001, 2003 Thomas Cataldo
 */
public class DriverIterator implements Iterator {

    private Iterator it;

    private Logger log;

    public DriverIterator(Iterator it, Logger log) {
        this.it = it;
        this.log = log;
    }

    public boolean hasNext() {
        return it.hasNext();
    }

    public Object next() {
        String klass = it.next().toString();
        Object ret = null;
        try {
            ret = Class.forName(klass).newInstance();
        } catch (InstantiationException e) {
            log.fatal(klass, e);
        } catch (IllegalAccessException e) {
            log.fatal(klass, e);
        } catch (ClassNotFoundException e) {
            log.fatal(klass, e);
        }
        return ret;
    }

    public void remove() {
        throw new RuntimeException("Removing from this iterator is not allowed");
    }
}
