package org.fcrepo.server.storage.types;

/**
 * @author Sandy Payette
 */
@Deprecated
@SuppressWarnings("deprecation")
public class DSBindingMap {

    public String dsBindMapID = null;

    public String dsBindMechanismPID = null;

    public String dsBindMapLabel = null;

    public String state = null;

    public DSBinding[] dsBindings = new DSBinding[0];

    public DSBindingMap() {
    }
}
