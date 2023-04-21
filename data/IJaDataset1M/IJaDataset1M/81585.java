package org.osmius.dao.hibernate;

import org.osmius.dao.OsmNTimeguardDao;
import org.osmius.dao.OsmUserDao;
import org.osmius.model.OsmNTimeguard;
import org.osmius.model.OsmUser;
import org.osmius.model.OsmNGuard;
import org.joda.time.DateTime;
import java.util.List;
import java.util.Date;

public class OsmNTimeguardDaoHibernate extends BaseDaoHibernate implements OsmNTimeguardDao {

    private OsmUserDao osmUserDao;

    public void setOsmUserDao(OsmUserDao osmUserDao) {
        this.osmUserDao = osmUserDao;
    }

    public OsmNTimeguard getOsmNTimeguard(Long idnTimeguard) {
        OsmNTimeguard osmNTimeguard = (OsmNTimeguard) getHibernateTemplate().load(OsmNTimeguard.class, idnTimeguard);
        if (osmNTimeguard == null) {
            log.warn("osmNTimeguard  : '" + idnTimeguard + "' not found...");
        }
        return osmNTimeguard;
    }

    public List getOsmNTimeguardsByUser(String idnUser) {
        List data = getHibernateTemplate().find("from OsmNTimeguard where osmUser.idnUser=? order by osmNGuard.idnGuard, dtiIni, dtiFini", idnUser);
        return data;
    }

    public void removeOsmNTimeguards(String idnUser) {
        getHibernateTemplate().bulkUpdate("delete OsmNTimeguard where osmUser.idnUser=?", idnUser);
    }

    public void saveOrUpdateOsmNTimeguards(OsmUser osmUser, String[] arrDays, String[] arrFrom, String[] arrTo) {
        for (int i = 0; i < arrDays.length; i++) {
            String arrDay = arrDays[i];
            if (arrDay.equals("")) {
                continue;
            }
            String[] _days = arrDay.split("-");
            for (int j = 0; j < _days.length; j++) {
                String day = _days[j];
                Date dateFrom = decodeDay(day, arrFrom[i]);
                Date dateTo = decodeDay(day, arrTo[i]);
                OsmNTimeguard osmNTimeguard = new OsmNTimeguard();
                osmNTimeguard.setDtiIni(dateFrom);
                osmNTimeguard.setDtiFini(dateTo);
                osmNTimeguard.setOsmUser(osmUser);
                getHibernateTemplate().save(osmNTimeguard);
            }
        }
    }

    private Date decodeDay(String day, String time) {
        DateTime date = null;
        String[] _time = time.split(":");
        if (day.equals("MO")) {
            date = new DateTime(2000, 5, 1, (_time[0].equals("24") ? 23 : Integer.parseInt(_time[0])), (_time[0].equals("24") ? 59 : Integer.parseInt(_time[1])), (_time[0].equals("24") && _time[1].equals("00") ? 59 : 0), 0);
        } else if (day.equals("TU")) {
            date = new DateTime(2000, 5, 2, (_time[0].equals("24") ? 23 : Integer.parseInt(_time[0])), (_time[0].equals("24") ? 59 : Integer.parseInt(_time[1])), (_time[0].equals("24") && _time[1].equals("00") ? 59 : 0), 0);
        } else if (day.equals("WE")) {
            date = new DateTime(2000, 5, 3, (_time[0].equals("24") ? 23 : Integer.parseInt(_time[0])), (_time[0].equals("24") ? 59 : Integer.parseInt(_time[1])), (_time[0].equals("24") && _time[1].equals("00") ? 59 : 0), 0);
        } else if (day.equals("TH")) {
            date = new DateTime(2000, 5, 4, (_time[0].equals("24") ? 23 : Integer.parseInt(_time[0])), (_time[0].equals("24") ? 59 : Integer.parseInt(_time[1])), (_time[0].equals("24") && _time[1].equals("00") ? 59 : 0), 0);
        } else if (day.equals("FR")) {
            date = new DateTime(2000, 5, 5, (_time[0].equals("24") ? 23 : Integer.parseInt(_time[0])), (_time[0].equals("24") ? 59 : Integer.parseInt(_time[1])), (_time[0].equals("24") && _time[1].equals("00") ? 59 : 0), 0);
        } else if (day.equals("SA")) {
            date = new DateTime(2000, 5, 6, (_time[0].equals("24") ? 23 : Integer.parseInt(_time[0])), (_time[0].equals("24") ? 59 : Integer.parseInt(_time[1])), (_time[0].equals("24") && _time[1].equals("00") ? 59 : 0), 0);
        } else if (day.equals("SU")) {
            date = new DateTime(2000, 5, 7, (_time[0].equals("24") ? 23 : Integer.parseInt(_time[0])), (_time[0].equals("24") ? 59 : Integer.parseInt(_time[1])), (_time[0].equals("24") && _time[1].equals("00") ? 59 : 0), 0);
        }
        return date.toDate();
    }

    public List getAssignedUsers(Long idnGuard) {
        List data = getHibernateTemplate().find(" select distinct(ont.osmUser) from OsmNTimeguard ont where ont.osmNGuard.idnGuard=?", idnGuard);
        return data;
    }

    public List getNotAssignedUsers(Long idnGuard) {
        String client = getClient();
        String role = getRole();
        List data = null;
        if (role.equals("ROOT")) {
            data = getHibernateTemplate().find("from OsmUser where idnUser not in (select ont.osmUser.idnUser from OsmNTimeguard ont where ont.osmNGuard.idnGuard=?) and idnUser<>'OSMINTRL' ", idnGuard);
        } else {
            data = getHibernateTemplate().find("from OsmUser u where u.idnUser not in (select ont.osmUser.idnUser from OsmNTimeguard ont where ont.osmNGuard.idnGuard=?) and u.osmRole.idnRole<>'ROOT' and u.osmClient.idnClient=? and u.idnUser<>'OSMINTRL' ", new Object[] { idnGuard, client });
        }
        return data;
    }

    public void saveOsmNTimeguards(OsmNGuard osmNGuard, String[] arrDays, String[] arrFrom, String[] arrTo, String[] arrUsers) {
        for (int h = 0; h < arrUsers.length; h++) {
            String userID = arrUsers[h];
            for (int i = 0; i < arrDays.length; i++) {
                String arrDay = arrDays[i];
                if (arrDay.equals("")) {
                    continue;
                }
                String[] _days = arrDay.split("-");
                for (int j = 0; j < _days.length; j++) {
                    String day = _days[j];
                    Date dateFrom = decodeDay(day, arrFrom[i]);
                    Date dateTo = decodeDay(day, arrTo[i]);
                    OsmNTimeguard osmNTimeguard = new OsmNTimeguard();
                    osmNTimeguard.setDtiIni(dateFrom);
                    osmNTimeguard.setDtiFini(dateTo);
                    osmNTimeguard.setOsmUser(osmUserDao.getUser(userID));
                    osmNTimeguard.setOsmNGuard(osmNGuard);
                    getHibernateTemplate().save(osmNTimeguard);
                }
            }
        }
    }

    public void updateOsmTimeGuards(OsmNGuard osmNGuard, String[] arrDays, String[] arrFrom, String[] arrTo, String[] arrUsers) {
        getHibernateTemplate().bulkUpdate("delete OsmNTimeguard ont where ont.osmNGuard.idnGuard=? ", osmNGuard.getIdnGuard());
        saveOsmNTimeguards(osmNGuard, arrDays, arrFrom, arrTo, arrUsers);
    }

    public List getOsmNTimeguardsByGuard(String idnGuard) {
        List data = null;
        data = getHibernateTemplate().find("select distinct(ont.osmUser) from OsmNTimeguard ont where ont.osmNGuard.idnGuard=?", new Long(idnGuard));
        if (data.size() > 0) {
            OsmUser osmUser = (OsmUser) data.get(0);
            data = getHibernateTemplate().find("from OsmNTimeguard ont where ont.osmNGuard.idnGuard=? and ont.osmUser.idnUser=?", new Object[] { new Long(idnGuard), osmUser.getIdnUser() });
        }
        return data;
    }

    public void deleteTimeGuards(String[] splittedChecks) {
        StringBuffer delete = new StringBuffer("delete OsmNTimeguard ont where ont.osmNGuard.idnGuard in (");
        Long[] guards = new Long[splittedChecks.length];
        for (int i = 0; i < splittedChecks.length; i++) {
            if (i > 0) delete.append(",");
            delete.append("?");
            guards[i] = new Long(splittedChecks[i]);
        }
        delete.append(")");
        getHibernateTemplate().bulkUpdate(delete.toString(), guards);
    }

    public void removeOsmNTimeguardsByUser(String idnUser) {
        getHibernateTemplate().bulkUpdate("delete OsmNTimeguard ont where ont.osmUser.idnUser = ?", idnUser);
    }
}
