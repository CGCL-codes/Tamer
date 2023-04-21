package gemini.basic.dao.impl;

import gemini.basic.dao.DistributorDao;
import gemini.basic.dao.GmnDao;
import gemini.basic.dto.SearchDistributorCriteria;
import gemini.basic.dto.SearchDistributorResult;
import gemini.basic.model.Distributor;
import gemini.basic.model.Person;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 *
 *
 */
public class DistributorDaoImpl extends HibernateDaoSupport implements DistributorDao {

    private GmnDao gmnDao;

    public void setGmnDao(GmnDao gmnDao) {
        this.gmnDao = gmnDao;
    }

    @Override
    public Distributor saveOrUpdate(Distributor distributor, boolean flush) {
        Distributor result = null;
        if (flush) {
            result = (Distributor) gmnDao.saveOrUpdateAndFlush(distributor);
        } else {
            result = (Distributor) gmnDao.saveOrUpdate(distributor);
        }
        return result;
    }

    @Override
    public Distributor getByCode(String code) {
        DetachedCriteria dtCri = DetachedCriteria.forClass(Distributor.class);
        if (code != null && !("".equals(code))) {
            dtCri.add(Restrictions.eq("code", code));
        }
        List<Object> disList = gmnDao.findByCriteria(dtCri);
        Distributor result = null;
        if (disList != null && !disList.isEmpty()) {
            result = (Distributor) disList.get(0);
        }
        return result;
    }

    @Override
    public List<SearchDistributorResult> findDistributor(SearchDistributorCriteria criteria) {
        String lastNameKey = null;
        String firstNameKey = null;
        DetachedCriteria dtCri = DetachedCriteria.forClass(Distributor.class);
        dtCri.createAlias("persons", "ps");
        if (criteria.getDistributorId() != null && !criteria.getDistributorId().isEmpty()) {
            dtCri.add(Restrictions.eq("code", criteria.getDistributorId()));
        }
        if (criteria.getFirstName() != null && !criteria.getFirstName().isEmpty()) {
            if (criteria.isExactly()) {
                dtCri.add(Restrictions.eq("ps.firstName", criteria.getFirstName()));
            } else {
                firstNameKey = criteria.getFirstName().toLowerCase();
            }
        }
        if (criteria.getLastName() != null && !criteria.getLastName().isEmpty()) {
            if (criteria.isExactly()) {
                dtCri.add(Restrictions.eq("ps.lastName", criteria.getLastName()));
            } else {
                lastNameKey = criteria.getLastName().toLowerCase();
            }
        }
        if (criteria.getEmail() != null && !criteria.getEmail().isEmpty()) {
            dtCri.add(Restrictions.eq("ps.email", criteria.getEmail()));
        }
        if (criteria.isSupervisor()) {
            dtCri.add(Restrictions.isNotNull("tab"));
        }
        if (criteria.getPhoneNumber() != null && !criteria.getPhoneNumber().isEmpty()) {
            dtCri.add(Restrictions.eq("ps.phoneNumber", criteria.getPhoneNumber()));
        }
        List<SearchDistributorResult> result = null;
        List<Object> disList = gmnDao.findByCriteria(dtCri);
        if (disList != null && !disList.isEmpty()) {
            result = new ArrayList<SearchDistributorResult>(disList.size());
            SearchDistributorResult rs = null;
            Distributor dis = null;
            Person ps1 = null;
            boolean checkName = true;
            boolean checkAlreadyInList = false;
            for (Object rsObject : disList) {
                dis = (Distributor) rsObject;
                for (Person ps : dis.getPersons()) {
                    ps1 = null;
                    checkName = true;
                    if (firstNameKey != null && !ps.getFirstName().toLowerCase().contains(firstNameKey)) {
                        checkName = false;
                    }
                    if (lastNameKey != null && !ps.getLastName().toLowerCase().contains(lastNameKey)) {
                        checkName = false;
                    }
                    if (checkName) {
                        rs = new SearchDistributorResult();
                        rs.setFullName(ps.getFirstName() + " " + ps.getLastName());
                        rs.setPhoneNumber(ps.getPhoneNumber());
                        rs.setEmail(ps.getEmail());
                        if (ps.getPerson() != null) {
                            ps1 = ps.getPerson();
                        } else if (ps.getPersons() != null && !ps.getPersons().isEmpty()) {
                            ps1 = ps.getPersons().get(0);
                        }
                        if (ps1 != null) {
                            rs.setSpouseFullName(ps1.getFirstName() + " " + ps1.getLastName());
                            rs.setSpousePhoneNumber(ps1.getPhoneNumber());
                            rs.setSpouseEmail(ps1.getEmail());
                        }
                        rs.setDistributorId(dis.getCode());
                        rs.setSupervisor(dis.getTab() != null);
                        for (SearchDistributorResult exRs : result) {
                            if (exRs.getDistributorId().equals(rs.getDistributorId())) {
                                checkAlreadyInList = true;
                                break;
                            }
                        }
                        if (!checkAlreadyInList) {
                            result.add(rs);
                        }
                    }
                }
            }
        }
        return result;
    }

    @Override
    public List<Distributor> getAllDistributors() {
        DetachedCriteria dtCri = DetachedCriteria.forClass(Distributor.class);
        List<Object> distributorsList = gmnDao.findByCriteria(dtCri);
        List<Distributor> result = null;
        if (distributorsList != null && !distributorsList.isEmpty()) {
            result = new ArrayList<Distributor>(distributorsList.size());
            Distributor dis = null;
            for (Object ob : distributorsList) {
                dis = (Distributor) ob;
                result.add(dis);
            }
        }
        return result;
    }
}
