package org.guestshome.actions.guests;

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
 * <p>Description: Action class invoked by "guest contributions" functionality.</p>
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
public class GuestContributionsAction extends Action {

    /**
   * Return a list of GuestContributions objects, encoded in JSON format 
   * @throws Throwable in case of errors
   */
    public void getGuestContributions() throws Throwable {
        EntityManager em = null;
        try {
            int peopleId = Integer.parseInt(request.getParameter("peopleId"));
            ListCommand lc = ExtUtils.getListCommand(GuestContribution.class, this);
            em = EntityManagerProvider.getInstance().getEntityManager();
            ListResponse res = BusinessObjectsFacade.getInstance().getGuestContributionsBO().getGuestContributions(username, em, lc, peopleId);
            ExtUtils.sendListResponse(res, this);
        } catch (Throwable ex) {
            ExtUtils.sendErrorResponse(ex.getMessage(), response);
            throw ex;
        } finally {
            if (em != null) EntityManagerProvider.getInstance().releaseEntityManager(em);
        }
    }

    /**
   * Insert a GuestContributions object, expressed in the request as JSON-like object.
   * Send back the same object (filled with other values), in JSON format.
   * @throws Throwable in case of errors
   */
    public void insertGuestContribution() throws Throwable {
        EntityManager em = null;
        try {
            int peopleId = Integer.parseInt(request.getParameter("peopleId"));
            em = EntityManagerProvider.getInstance().getEntityManager();
            Person person = BusinessObjectsFacade.getInstance().getGuestsBO().getGuest(username, em, peopleId);
            GuestContribution vo = ExtUtils.getValueObject(GuestContribution.class, this);
            vo.setContributor(BusinessObjectsFacade.getInstance().getContributorsBO().getContributor(username, em, vo.getContributorId()));
            vo.setPerson(person);
            vo.setPersonId(person.getId());
            BusinessObjectsFacade.getInstance().getGuestContributionsBO().insertGuestContribution(username, em, vo);
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
   * Update a list of GuestContributions objects, expressed in the request as JSON-like objects.
   * Send back the same objects (filled with other values, e.g. ROW_VERSION), in JSON format.
   * @throws Throwable in case of errors
   */
    public void updateGuestContributions() throws Throwable {
        EntityManager em = null;
        try {
            List<GuestContribution> vos = ExtUtils.getValueObjects(GuestContribution.class, this);
            em = EntityManagerProvider.getInstance().getEntityManager();
            for (GuestContribution vo : vos) {
                vo.setContributor(BusinessObjectsFacade.getInstance().getContributorsBO().getContributor(username, em, vo.getContributorId()));
                vo.setPerson(BusinessObjectsFacade.getInstance().getGuestsBO().getGuest(username, em, vo.getPersonId()));
            }
            vos = BusinessObjectsFacade.getInstance().getGuestContributionsBO().updateGuestContributions(username, em, vos);
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
   * Logically delete a list of GuestContributions objects, expressed in the request as JSON-like objects.
   * Send back the deleting result, expressed in JSON format.
   * @throws Throwable in case of errors
   */
    public void deleteGuestContributions() throws Throwable {
        EntityManager em = null;
        try {
            List<GuestContribution> vos = ExtUtils.getValueObjects(GuestContribution.class, this);
            em = EntityManagerProvider.getInstance().getEntityManager();
            for (GuestContribution vo : vos) {
                vo.setContributor(BusinessObjectsFacade.getInstance().getContributorsBO().getContributor(username, em, vo.getContributorId()));
                vo.setPerson(BusinessObjectsFacade.getInstance().getGuestsBO().getGuest(username, em, vo.getPersonId()));
            }
            BusinessObjectsFacade.getInstance().getGuestContributionsBO().deleteGuestContributions(username, em, vos);
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
