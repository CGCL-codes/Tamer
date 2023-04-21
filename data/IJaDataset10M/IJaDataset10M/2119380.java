package de.juwimm.cms.model;

import java.util.Collection;
import java.util.Iterator;
import org.apache.log4j.Logger;
import de.juwimm.cms.components.model.*;
import de.juwimm.cms.remote.helper.AuthenticationHelper;
import de.juwimm.cms.util.SequenceHelper;

/**
 * @see de.juwimm.cms.model.UnitHbm
 * @author <a href="mailto:carsten.schalm@juwimm.com">Carsten Schalm</a>
 *         company Juwi|MacMillan Group Gmbh, Walsrode, Germany
 * @version $Id: UnitHbmDaoImpl.java 41 2009-02-27 17:14:45Z skulawik $
 */
public class UnitHbmDaoImpl extends UnitHbmDaoBase {

    private static final Logger log = Logger.getLogger(UnitHbmDaoImpl.class);

    @Override
    public UnitHbm create(UnitHbm unitHbm) {
        if (unitHbm.getUnitId() == null || unitHbm.getUnitId().intValue() == 0) {
            try {
                Integer id = SequenceHelper.getSequenceSession().getNextSequenceNumber("unit.unit_id");
                unitHbm.setUnitId(id);
            } catch (Exception e) {
                log.error("Error creating primary key", e);
            }
        }
        if (unitHbm.getSite() == null) {
            unitHbm.setSite(getUserHbmDao().load(AuthenticationHelper.getUserName()).getActiveSite());
        }
        unitHbm.setLastModifiedDate(System.currentTimeMillis());
        return super.create(unitHbm);
    }

    @Override
    protected String handleToXmlRecursive(int tabdepth, UnitHbm unit) throws Exception {
        boolean isRootUnit = false;
        if (unit.getSite().getRootUnit().equals(unit)) {
            isRootUnit = true;
        }
        StringBuffer sb = new StringBuffer();
        sb.append("<unit id=\"" + unit.getUnitId() + "\" ");
        sb.append("imageId=\"" + unit.getImageId() + "\" logoId=\"" + unit.getLogoId() + "\" ");
        sb.append("isRootUnit=\"" + Boolean.toString(isRootUnit) + "\">");
        sb.append("<![CDATA[").append(unit.getName().trim()).append("]]>\n");
        try {
            Collection personLinks = getPersonToUnitLinkHbmDao().findByUnit(unit.getUnitId());
            Iterator it = personLinks.iterator();
            while (it.hasNext()) {
                PersonToUnitLinkHbm pl = (PersonToUnitLinkHbm) it.next();
                sb.append(pl.toXml(tabdepth + 1));
            }
        } catch (Exception exe) {
        }
        {
            Collection addr = unit.getAddresses();
            Iterator it = addr.iterator();
            while (it.hasNext()) {
                AddressHbm adr = (AddressHbm) it.next();
                sb.append(adr.toXml(tabdepth + 1));
            }
        }
        {
            Collection pers = unit.getPersons();
            Iterator it = pers.iterator();
            while (it.hasNext()) {
                PersonHbm per = (PersonHbm) it.next();
                sb.append(per.toXmlRecursive(tabdepth + 1));
            }
        }
        {
            Collection deps = unit.getDepartments();
            Iterator it = deps.iterator();
            while (it.hasNext()) {
                DepartmentHbm dep = (DepartmentHbm) it.next();
                sb.append(dep.toXmlRecursive(tabdepth + 1));
            }
        }
        {
            Collection ttimes = unit.getTalktimes();
            Iterator it = ttimes.iterator();
            while (it.hasNext()) {
                TalktimeHbm ttime = (TalktimeHbm) it.next();
                sb.append(ttime.toXml(tabdepth + 1));
            }
        }
        sb.append("</unit>\n");
        return sb.toString();
    }

    @SuppressWarnings("unchecked")
    public java.util.Collection findAll(final int transform) {
        return this.findAll(transform, "from de.juwimm.cms.model.UnitHbm as unitHbm");
    }

    @SuppressWarnings("unchecked")
    public java.util.Collection findAll(final int transform, final java.lang.Integer siteId) {
        return this.findAll(transform, "from de.juwimm.cms.model.UnitHbm as unit where unit.site.siteId = ? order by unit.name", siteId);
    }

    @SuppressWarnings("unchecked")
    public java.util.Collection findBySite(final int transform, final java.lang.Integer siteId) {
        return this.findBySite(transform, "from de.juwimm.cms.model.UnitHbm as unit where unit.site.siteId = ?", siteId);
    }

    @SuppressWarnings("unchecked")
    public java.util.Collection findByName(final int transform, final java.lang.Integer siteId, final java.lang.String name) {
        return this.findByName(transform, "from de.juwimm.cms.model.UnitHbm as u where u.site.siteId = ? and u.name like ?", siteId, name);
    }

    @SuppressWarnings("unchecked")
    public java.util.Collection findByUserAndSite(final int transform, final java.lang.String userId, final java.lang.Integer siteId) {
        return this.findByUserAndSite(transform, "select u from de.juwimm.cms.model.UnitHbm as u inner join u.users s where s.userId = ? and u.site.siteId = ?", userId, siteId);
    }
}
