package muc.services;

import java.util.Date;
import java.util.HashMap;
import mappers.VisitMapper;
import database.Database;
import domain.muc.UserPermissions;
import domain.muc.Visit;
import exceptions.DatabaseNotConnectedException;
import exceptions.ServiceOperationException;

/**
 * Represents muc visits service. Stores in-memory bindings between
 * {@link UserPermissions} and {@link Visit} instances in order to provide fast
 * lookup of user latest visits
 * 
 * @author tillias
 * 
 */
public class VisitService {

    /**
     * Creates new instance of service
     * 
     * @param db
     *            {@link Database} object which will be used by service
     * @throws NullPointerException
     *             Thrown if argument passed to constructor is null
     * @throws DatabaseNotConnectedException
     *             Thrown if database passed to constructor is in disconnected
     *             state
     */
    public VisitService(Database db) throws NullPointerException, DatabaseNotConnectedException {
        if (db == null) throw new NullPointerException();
        if (!db.isConnected()) throw new DatabaseNotConnectedException();
        mapper = new VisitMapper(db);
        visitsTable = new HashMap<UserPermissions, Visit>();
    }

    /**
     * Creates new {@link Visit} object for given user, persists it into
     * database and associates it with given user. User is defined by
     * {@link UserPermissions} argument
     * 
     * @param permissions
     *            Specifies user information for which visit will be created and
     *            processed
     * @return New visit that has been created for given user
     * @throws NullPointerException
     *             Thrown if argument passed to this method is null
     * @throws IllegalArgumentException
     *             Thrown if argument passed to this method isn't persistent
     *             domain object
     * @throws ServiceOperationException
     *             Thrown if service is unable to persist new {@link Visit}
     *             object
     */
    public Visit startVisit(UserPermissions permissions) throws NullPointerException, IllegalArgumentException, ServiceOperationException {
        checkArgument(permissions);
        Visit visit = new Visit(permissions);
        if (mapper.save(visit)) {
            setVisit(permissions, visit);
        } else throw new ServiceOperationException("Can't save Visit into database", null);
        return visit;
    }

    /**
     * Finishes {@link Visit} for given user. User is defined by
     * {@link UserPermissions} argument
     * 
     * @param permissions
     *            Specifies user information for which visit will be created and
     *            processed
     * @return Visit that has been finished for given user
     * @throws NullPointerException
     *             Thrown if argument passed to this method is null
     * @throws IllegalArgumentException
     *             Thrown if argument passed to this method isn't persistent
     *             domain object
     * @throws ServiceOperationException
     *             Thrown if service is unable to persist {@link Visit} object
     */
    public Visit finishVisit(UserPermissions permissions) throws NullPointerException, IllegalArgumentException, ServiceOperationException {
        checkArgument(permissions);
        Visit visit = visitsTable.get(permissions);
        if (visit != null) {
            visit.setEndDate(new Date());
            if (!mapper.save(visit)) {
                throw new ServiceOperationException("Can't save Visit into database", null);
            }
        }
        return visit;
    }

    /**
     * Gets current (latest) visit of given user. User is defined by
     * {@link UserPermissions} argument
     * 
     * @param permissions
     *            Specifies user information for which visit will be created and
     *            processed
     * @return Latest visit of given user
     * @throws NullPointerException
     *             Thrown if argument passed to this method is null
     * @throws IllegalArgumentException
     *             Thrown if argument passed to this method isn't persistent
     *             domain object
     */
    public Visit getVisit(UserPermissions permissions) throws NullPointerException, IllegalArgumentException {
        checkArgument(permissions);
        return visitsTable.get(permissions);
    }

    private void checkArgument(UserPermissions permissions) throws NullPointerException, IllegalArgumentException {
        if (permissions == null) throw new NullPointerException();
        if (!permissions.isPersistent()) throw new IllegalArgumentException("Permissions argument isn't persistent domain object");
    }

    private void setVisit(UserPermissions permissions, Visit visit) {
        if (permissions != null && visit != null) {
            boolean ok = permissions.isPersistent() && visit.isPersistent();
            if (ok) {
                visitsTable.put(permissions, visit);
            }
        }
    }

    HashMap<UserPermissions, Visit> visitsTable;

    VisitMapper mapper;
}
