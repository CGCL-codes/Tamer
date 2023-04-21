package org.guestshome.actions.other;

import java.util.List;
import javax.persistence.EntityManager;
import org.extutils.web.Action;
import org.guestshome.businessobjects.BusinessObjectsFacade;
import org.guestshome.businessobjects.other.*;
import org.guestshome.commons.database.EntityManagerProvider;
import org.guestshome.entities.*;
import org.sqlutils.ListCommand;
import org.sqlutils.ListResponse;
import org.sqlutils.ext.ExtUtils;

/**
 * <p>Title: GuestsHome application</p>
 * <p>Description: Action class invoked by "Release types" functionality related to guest reception.</p>
 * <p>Copyright: Copyright (C) 2009 Informatici senza frontiere</p>
 *
 * This application is free software; you can redistribute it and/or
 * modify it under the terms of the (LGPL) Lesser General Public
 * License as published by the Free Software Foundation;
 *
 *                GNU LESSER GENERAL PUBLIC LICENSE
 *                 Version 2.1, February 1999
 *
 * This application is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the Free
 * Software Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 * @author Mauro Carniel
 * @version 1.0
 *
 */
public class ReleaseTypesAction extends Action {

    /**
	 * Return a list of ReleaseType objects, encoded in JSON format 
	 * @throws Throwable in case of errors
	 */
    public void getReleaseTypes() throws Throwable {
        EntityManager em = null;
        try {
            ListCommand lc = ExtUtils.getListCommand(ReleaseType.class, this);
            em = EntityManagerProvider.getInstance().getEntityManager();
            ListResponse res = BusinessObjectsFacade.getInstance().getReleaseTypesBO().getReleaseTypes(username, em, lc);
            ExtUtils.sendListResponse(res, this);
        } catch (Throwable ex) {
            ExtUtils.sendErrorResponse(ex.getMessage(), response);
            throw ex;
        } finally {
            if (em != null) EntityManagerProvider.getInstance().releaseEntityManager(em);
        }
    }

    /**
	 * Insert a ReleaseType object, expressed in the request as JSON-like object.
	 * Send back the same object (filled with other values), in JSON format.
	 * @throws Throwable in case of errors
	 */
    public void insertReleaseType() throws Throwable {
        EntityManager em = null;
        try {
            ReleaseType vo = ExtUtils.getValueObject(ReleaseType.class, this);
            em = EntityManagerProvider.getInstance().getEntityManager();
            BusinessObjectsFacade.getInstance().getReleaseTypesBO().insertReleaseType(username, em, vo);
            em.getTransaction().commit();
            ExtUtils.sendValueObject(vo, this);
        } catch (Throwable ex) {
            ExtUtils.sendErrorResponse(ex.getMessage(), response);
            throw ex;
        } finally {
            if (em != null) EntityManagerProvider.getInstance().releaseEntityManager(em);
        }
    }

    /**
	 * Insert a list of ReleaseType objects, expressed in the request as JSON-like object.
	 * Send back the same object (filled with other values), in JSON format.
	 * @throws Throwable in case of errors
	 */
    public void insertReleaseTypes() throws Throwable {
        EntityManager em = null;
        try {
            List<ReleaseType> vos = ExtUtils.getValueObjects(ReleaseType.class, this);
            em = EntityManagerProvider.getInstance().getEntityManager();
            for (ReleaseType vo : vos) BusinessObjectsFacade.getInstance().getReleaseTypesBO().insertReleaseType(username, em, vo);
            em.getTransaction().commit();
            ExtUtils.sendList(vos, this);
        } catch (Throwable ex) {
            ExtUtils.sendErrorResponse(ex.getMessage(), response);
            throw ex;
        } finally {
            if (em != null) EntityManagerProvider.getInstance().releaseEntityManager(em);
        }
    }

    /**
	 * Update a list of ReleaseType objects, expressed in the request as JSON-like objects.
	 * Send back the same objects (filled with other values, e.g. ROW_VERSION), in JSON format.
	 * @throws Throwable in case of errors
	 */
    public void updateReleaseTypes() throws Throwable {
        EntityManager em = null;
        try {
            List<ReleaseType> vos = ExtUtils.getValueObjects(ReleaseType.class, this);
            em = EntityManagerProvider.getInstance().getEntityManager();
            vos = BusinessObjectsFacade.getInstance().getReleaseTypesBO().updateReleaseTypes(username, em, vos);
            em.getTransaction().commit();
            ExtUtils.sendList(vos, this);
        } catch (Throwable ex) {
            ExtUtils.sendErrorResponse(ex.getMessage(), response);
            throw ex;
        } finally {
            if (em != null) EntityManagerProvider.getInstance().releaseEntityManager(em);
        }
    }

    /**
	 * Logically delete a list of ReleaseType objects, expressed in the request as JSON-like objects.
	 * Send back the deleting result, expressed in JSON format.
	 * @throws Throwable in case of errors
	 */
    public void deleteReleaseTypes() throws Throwable {
        EntityManager em = null;
        try {
            List<ReleaseType> vos = ExtUtils.getValueObjects(ReleaseType.class, this);
            em = EntityManagerProvider.getInstance().getEntityManager();
            BusinessObjectsFacade.getInstance().getReleaseTypesBO().deleteReleaseTypes(username, em, vos);
            em.getTransaction().commit();
            ExtUtils.sendResponse(null, response);
        } catch (Throwable ex) {
            ExtUtils.sendErrorResponse(ex.getMessage(), response);
            throw ex;
        } finally {
            if (em != null) EntityManagerProvider.getInstance().releaseEntityManager(em);
        }
    }
}
