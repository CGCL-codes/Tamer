package net.sourceforge.mandalore.aspect.base;

/**
 * Created by IntelliJ IDEA.
 * User: ctoth
 * Date: Sep 5, 2006
 * Time: 15:05:09 PM
 */
public interface PersistenceUIAspect<E> extends UIAspect<E> {

    String persist();
}
