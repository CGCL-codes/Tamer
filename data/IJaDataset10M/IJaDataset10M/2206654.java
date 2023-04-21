package org.guestshome.businessobjects.other;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.persistence.EntityManager;
import org.guestshome.commons.Consts;
import org.guestshome.commons.database.DefaultFieldsCallabacks;
import org.guestshome.entities.*;
import org.sqlutils.ListCommand;
import org.sqlutils.ListResponse;
import org.sqlutils.jpa.JPAMethods;
import org.sqlutils.jpa.JPASelectStatement;
import org.sqlutils.logger.Logger;

/**
 * <p>Title: GuestsHome application</p>
 * <p>Description: Business object related to provinces.</p>
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
public class ProvincesBO {

    /**
   * Retrieve a specific province
   * @param code province code 
   * @return province
   * @throws Exception in case of errors
   */
    public Province getProvince(String username, EntityManager em, ProvincePK pk) throws Throwable {
        try {
            Province vo = JPAMethods.find(username, em, Province.class, pk);
            if (vo == null) throw new Exception("No Province found having pk: " + pk);
            return vo;
        } catch (Throwable ex) {
            Logger.error(null, ex.getMessage(), ex);
            throw ex;
        }
    }

    /**
   * Retrieve a list of Provinces. 
   * @return list of Province objects
   * @throws Exception in case of errors
   */
    public ListResponse<Province> getProvinces(String username, EntityManager em, ListCommand lc, String countryCode) throws Throwable {
        try {
            String jpql = "select p from Province p join fetch p.country where p.deleted = ?1 ";
            if (countryCode != null) jpql += " and p.pk.countryCode = ?2";
            JPASelectStatement query = new JPASelectStatement("p", jpql);
            List<Object> params = new ArrayList<Object>();
            params.add(Consts.FLAG_N);
            if (countryCode != null) params.add(countryCode);
            return JPAMethods.executeQuery(em, query, params, lc, new HashMap<String, String>());
        } catch (Throwable ex) {
            Logger.error(null, ex.getMessage(), ex);
            throw ex;
        }
    }

    /**
   * Insert a Province.
   * @param Province to insert
   * @throws Exception in case of errors
   */
    public void insertProvince(String username, EntityManager em, Province vo) throws Throwable {
        try {
            JPAMethods.persist(em, username, DefaultFieldsCallabacks.getInstance(), vo);
        } catch (Throwable ex) {
            Logger.error(null, ex.getMessage(), ex);
            throw ex;
        } finally {
            em.flush();
        }
    }

    /**
   * Update a Province.
   * @param vo Province to update
   * @throws Exception in case of errors
   */
    public Province updateProvince(String username, EntityManager em, Province vo) throws Throwable {
        try {
            return (Province) JPAMethods.merge(em, username, DefaultFieldsCallabacks.getInstance(), vo);
        } catch (Throwable ex) {
            Logger.error(null, ex.getMessage(), ex);
            throw ex;
        } finally {
            em.flush();
        }
    }

    /**
   * Update a list of Provinces.
   * @param vos list of Provinces to update
   * @return list of updated Provinces 
   * @throws Exception in case of errors
   */
    public List<Province> updateProvinces(String username, EntityManager em, List<Province> vos) throws Throwable {
        try {
            Province vo = null;
            for (int i = 0; i < vos.size(); i++) {
                vo = vos.get(i);
                vos.set(i, (Province) JPAMethods.merge(em, username, DefaultFieldsCallabacks.getInstance(), vo));
                em.flush();
            }
            return vos;
        } catch (Throwable ex) {
            Logger.error(null, ex.getMessage(), ex);
            throw ex;
        }
    }

    /**
   * Delete (logically) a Province. 
   * @param vo Province to logically delete
   * @throws Exception in case of errors
   */
    public void deleteProvinces(String username, EntityManager em, List<Province> vos) throws Throwable {
        try {
            for (Province vo : vos) {
                vo.setDeleted(Consts.FLAG_Y);
                JPAMethods.merge(em, username, DefaultFieldsCallabacks.getInstance(), vo);
            }
        } catch (Throwable ex) {
            Logger.error(null, ex.getMessage(), ex);
            throw ex;
        }
    }
}
