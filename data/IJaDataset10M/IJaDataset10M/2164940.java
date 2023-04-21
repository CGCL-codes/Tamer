package org.usixml.admin.action;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.log.Log;
import org.usixml.admin.entity.Organization;

/**
 *
 * @author htmfilho
 */
@Stateless
@Name("organizationAction")
public class OrganizationActionBean implements OrganizationActionLocal {

    @In
    private Organization organization;

    @PersistenceContext
    private EntityManager em;

    @Logger
    private Log log;

    public String add() {
        em.persist(organization);
        log.info("#{organization.name} persisted.");
        return "/index.xhtml";
    }
}
