package zcatalog;

/**
 * An observer for the an object name.
 * @author Alessandro Zigliani
 * @version 0.9
 * @since ZCatalog 0.9
 */
public interface ObjectNameObserver {

    /** notify the name of the observed object has changed */
    public void notifyObjectName(String objName);
}
