package org.rapla.server;

import java.util.Date;
import org.rapla.entities.RaplaType;
import org.rapla.framework.RaplaException;
import org.rapla.storage.dbrm.EntityList;

public interface RemoteStorage {

    void authenticate(String username, String password) throws RaplaException;

    boolean canChangePassword() throws RaplaException;

    void changePassword(String username, String oldPassword, String newPassword) throws RaplaException;

    EntityList getResources() throws RaplaException;

    /** returns the time on the server */
    long getServerTime() throws RaplaException;

    /** delegates the corresponding method in the StorageOperator. */
    EntityList getReservations(Date start, Date end) throws RaplaException;

    EntityList getEntityRecursive(Object id) throws RaplaException;

    String refresh(String clientRepoVersion) throws RaplaException;

    void restartServer() throws RaplaException;

    void dispatch(String xml) throws RaplaException;

    String createIdentifier(RaplaType raplaType) throws RaplaException;
}
