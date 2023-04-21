package wii.edu.core.dao;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import wii.edu.core.model.Angkatan;

/**
 *
 * @author Hendro
 */
public class AngkatanDAO {

    /** Creates a new instance of AngkatanDAO */
    public AngkatanDAO() {
    }

    public Angkatan getAngkatan(long id) throws Exception {
        HibernateUtil.beginTransaction();
        Session session = HibernateUtil.getSession();
        Angkatan angkatan = null;
        try {
            angkatan = (Angkatan) session.load(Angkatan.class, id);
        } catch (Exception ex) {
            throw ex;
        }
        return angkatan;
    }

    public List getAllAngkatan() throws Exception {
        HibernateUtil.beginTransaction();
        List list = new ArrayList();
        Session session = HibernateUtil.getSession();
        try {
            list = session.createQuery("FROM Angkatan").list();
        } catch (Exception ex) {
            throw ex;
        }
        return list;
    }

    public List getAllAngkatan(int start, int limit) throws Exception {
        HibernateUtil.beginTransaction();
        List list = new ArrayList();
        Session session = HibernateUtil.getSession();
        try {
            list = session.createQuery("FROM Angkatan").setFirstResult(start).setMaxResults(limit).list();
        } catch (Exception ex) {
            throw ex;
        }
        return list;
    }

    public JSONObject getAllAngkatanJSONObject() throws Exception {
        List list = getAllAngkatan();
        JSONObject root = new JSONObject();
        JSONArray angkatan = new JSONArray();
        for (int a = 0; a < list.size(); a++) {
            Angkatan angk = (Angkatan) list.get(a);
            JSONObject jsonUser = new JSONObject();
            jsonUser.put("id", angk.getId());
            jsonUser.put("nama", angk.getNama());
            jsonUser.put("keterangan", angk.getKeterangan());
            jsonUser.put("biaya", angk.getBiayaPerSks());
            angkatan.add(jsonUser);
        }
        root.put("daftarAngkatan", angkatan);
        return root;
    }

    public JSONObject getAllAngkatanJSONObject(int start, int limit) throws Exception {
        List list = getAllAngkatan(start, limit);
        List list_all = getAllAngkatan();
        JSONObject root = new JSONObject();
        JSONArray angkatan = new JSONArray();
        for (int a = 0; a < list.size(); a++) {
            Angkatan angk = (Angkatan) list.get(a);
            JSONObject jsonUser = new JSONObject();
            jsonUser.put("id", angk.getId());
            jsonUser.put("nama", angk.getNama());
            jsonUser.put("keterangan", angk.getKeterangan());
            jsonUser.put("biaya", angk.getBiayaPerSks());
            angkatan.add(jsonUser);
        }
        root.put("total", list_all.size());
        root.put("daftarAngkatan", angkatan);
        return root;
    }
}
