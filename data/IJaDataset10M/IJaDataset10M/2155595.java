package org.dspace.eperson.dao;

import java.util.ArrayList;
import java.util.List;
import org.dspace.authorize.AuthorizeException;
import org.dspace.authorize.AuthorizeManager;
import org.dspace.core.Constants;
import org.dspace.core.Context;
import org.dspace.core.LogManager;
import org.dspace.eperson.EPerson;
import org.dspace.eperson.EPerson.EPersonMetadataField;
import org.dspace.uri.ObjectIdentifier;
import org.dspace.uri.ObjectIdentifierService;
import org.dspace.uri.IdentifierException;
import org.dspace.uri.dao.ObjectIdentifierStorageException;

/**
 * @author James Rutherford
 */
public class EPersonDAOCore extends EPersonDAO {

    public EPersonDAOCore(Context context) {
        super(context);
    }

    public EPerson create() throws AuthorizeException {
        if (!AuthorizeManager.isAdmin(context)) {
            throw new AuthorizeException("You must be an admin to create an EPerson");
        }
        EPerson eperson = childDAO.create();
        ObjectIdentifier oid = ObjectIdentifierService.mint(context, eperson);
        update(eperson);
        log.info(LogManager.getHeader(context, "create_eperson", "eperson_id=" + eperson.getID()));
        return eperson;
    }

    public EPerson retrieve(int id) {
        EPerson eperson = (EPerson) context.fromCache(EPerson.class, id);
        if (eperson == null) {
            eperson = childDAO.retrieve(id);
        }
        return eperson;
    }

    public EPerson retrieve(EPersonMetadataField field, String value) {
        if ((field != EPersonMetadataField.EMAIL) && (field != EPersonMetadataField.NETID)) {
            throw new IllegalArgumentException(field + " isn't allowed here");
        }
        if (value == null || "".equals(value)) {
            return null;
        }
        return childDAO.retrieve(field, value);
    }

    public void update(EPerson eperson) throws AuthorizeException {
        try {
            if (!context.ignoreAuthorization() && ((context.getCurrentUser() == null) || !eperson.equals(context.getCurrentUser()))) {
                AuthorizeManager.authorizeAction(context, eperson, Constants.WRITE);
            }
            ObjectIdentifier oid = eperson.getIdentifier();
            if (oid == null) {
                oid = ObjectIdentifierService.mint(context, eperson);
            }
            oidDAO.update(eperson.getIdentifier());
            log.info(LogManager.getHeader(context, "update_eperson", "eperson_id=" + eperson.getID()));
            childDAO.update(eperson);
        } catch (ObjectIdentifierStorageException e) {
            log.error("caught exception: ", e);
            throw new RuntimeException(e);
        }
    }

    public void delete(int id) throws AuthorizeException {
        try {
            if (!AuthorizeManager.isAdmin(context)) {
                throw new AuthorizeException("You must be an admin to delete an EPerson");
            }
            EPerson eperson = retrieve(id);
            context.removeCached(eperson, id);
            oidDAO.delete(eperson.getIdentifier());
            log.info(LogManager.getHeader(context, "delete_eperson", "eperson_id=" + id));
            childDAO.delete(id);
        } catch (ObjectIdentifierStorageException e) {
            log.error("caught exception: ", e);
            throw new RuntimeException(e);
        }
    }

    public List<EPerson> search(String query) {
        return search(query, -1, -1);
    }

    public List<EPerson> search(String query, int offset, int limit) {
        if (limit == 0) {
            return new ArrayList<EPerson>();
        }
        if (query == null || "".equals(query)) {
            List<EPerson> epeople = getEPeople();
            if ((offset > -1) || (limit > -1)) {
                int toIndex = epeople.size();
                if (offset < 0) {
                    offset = 0;
                }
                if (limit != -1) {
                    if ((offset + limit) <= epeople.size()) {
                        toIndex = offset + limit;
                    }
                }
                return epeople.subList(offset, toIndex);
            } else {
                return epeople;
            }
        }
        return childDAO.search(query, offset, limit);
    }
}
