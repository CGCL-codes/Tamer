package wii.edu.core.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.hibernate.Session;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import wii.edu.core.model.BidangStudi;
import wii.edu.core.model.Dosen;
import wii.edu.core.model.Fakultas;
import wii.edu.core.model.Distrik;

/**
 *
 * @author Hendro
 */
public class DosenDAO {

    /** Creates a new instance of DosenDAO */
    public DosenDAO() {
    }

    public Dosen getDosen(long id) throws Exception {
        HibernateUtil.beginTransaction();
        Session session = HibernateUtil.getSession();
        Dosen dosen = null;
        try {
            dosen = (Dosen) session.load(Dosen.class, id);
        } catch (Exception ex) {
            throw ex;
        }
        return dosen;
    }

    public Dosen getDosen(String username) throws Exception {
        HibernateUtil.beginTransaction();
        Session session = HibernateUtil.getSession();
        Dosen dosen = null;
        try {
            dosen = (Dosen) session.createQuery("SELECT dsn FROM Dosen dsn WHERE dsn.nomorPegawai= :input").setParameter("input", username).uniqueResult();
        } catch (Exception ex) {
            throw ex;
        }
        return dosen;
    }

    public List getListDosen() throws Exception {
        HibernateUtil.beginTransaction();
        List list = new ArrayList();
        Session session = HibernateUtil.getSession();
        try {
            list = session.createQuery("FROM Dosen").list();
        } catch (Exception ex) {
            throw ex;
        }
        return list;
    }

    public List getListDosen(Distrik distrik) throws Exception {
        HibernateUtil.beginTransaction();
        List list = new ArrayList();
        Session session = HibernateUtil.getSession();
        try {
            list = session.createQuery("FROM Dosen WHERE distrik = :input").setParameter("input", distrik).list();
        } catch (Exception ex) {
            throw ex;
        }
        return list;
    }

    public List getListDosen(int start, int limit) throws Exception {
        HibernateUtil.beginTransaction();
        List list = new ArrayList();
        Session session = HibernateUtil.getSession();
        try {
            list = session.createQuery("FROM Dosen").setFirstResult(start).setMaxResults(limit).list();
        } catch (Exception ex) {
            throw ex;
        }
        return list;
    }

    public JSONObject getAllDosenJSONObject() throws Exception {
        List list = getListDosen();
        JSONObject root = new JSONObject();
        JSONArray dosen = new JSONArray();
        for (int a = 0; a < list.size(); a++) {
            Dosen dos = (Dosen) list.get(a);
            Distrik prov = dos.getDistrik();
            JSONObject jsonUser = new JSONObject();
            jsonUser.put("id", dos.getId());
            jsonUser.put("userName", dos.getNomorPegawai());
            jsonUser.put("password", dos.getPassword());
            jsonUser.put("nama", dos.getNama());
            jsonUser.put("alamat", dos.getJalan());
            jsonUser.put("subDistrik", dos.getSubDistrik());
            jsonUser.put("distrik", prov.getId());
            jsonUser.put("kodepos", dos.getKodePos());
            jsonUser.put("telpon", dos.getTelepon());
            jsonUser.put("handphone", dos.getHandphone());
            jsonUser.put("email", dos.getEmail());
            jsonUser.put("jenisKelamin", dos.getJenisKelamin());
            jsonUser.put("pendidikanTerakhir", dos.getPendidikanTerakhir());
            jsonUser.put("isLogin", dos.getIsLogin() == 1 ? true : false);
            dosen.add(jsonUser);
        }
        root.put("daftarDosen", dosen);
        return root;
    }

    public JSONObject getAllDosenJSONObject(int start, int limit) throws Exception {
        List list = getListDosen(start, limit);
        List list_all = getListDosen();
        JSONObject root = new JSONObject();
        JSONArray dosen = new JSONArray();
        for (int a = 0; a < list.size(); a++) {
            Dosen dos = (Dosen) list.get(a);
            Distrik prov = dos.getDistrik();
            JSONObject jsonUser = new JSONObject();
            jsonUser.put("id", dos.getId());
            jsonUser.put("userName", dos.getNomorPegawai());
            jsonUser.put("password", dos.getPassword());
            jsonUser.put("nama", dos.getNama());
            jsonUser.put("alamat", dos.getJalan());
            jsonUser.put("subDistrik", dos.getSubDistrik());
            jsonUser.put("distrik", prov.getId());
            jsonUser.put("kodepos", dos.getKodePos());
            jsonUser.put("telpon", dos.getTelepon());
            jsonUser.put("handphone", dos.getHandphone());
            jsonUser.put("email", dos.getEmail());
            jsonUser.put("jenisKelamin", dos.getJenisKelamin());
            jsonUser.put("pendidikanTerakhir", dos.getPendidikanTerakhir());
            jsonUser.put("isLogin", dos.getIsLogin() == 1 ? true : false);
            dosen.add(jsonUser);
        }
        root.put("total", list_all.size());
        root.put("daftarDosen", dosen);
        return root;
    }

    public boolean nopegIsUsed(String nomorPegawai) throws Exception {
        HibernateUtil.beginTransaction();
        Session session = HibernateUtil.getSession();
        Dosen dosen = null;
        boolean isUsed = false;
        try {
            dosen = (Dosen) session.createQuery("SELECT dsn FROM Dosen dsn WHERE dsn.nomorPegawai= :input").setParameter("input", nomorPegawai).uniqueResult();
            if (dosen != null) {
                isUsed = true;
            }
        } catch (Exception ex) {
            throw ex;
        }
        return isUsed;
    }

    public Dosen login(String username, String password) throws Exception {
        HibernateUtil.beginTransaction();
        Session session = HibernateUtil.getSession();
        Dosen dosen = null;
        try {
            dosen = (Dosen) session.createQuery("SELECT dsn FROM Dosen dsn WHERE dsn.nomorPegawai= :input").setParameter("input", username).uniqueResult();
        } catch (Exception ex) {
            throw ex;
        }
        return dosen;
    }

    public int logout(Dosen dosen) throws Exception {
        int result = 0;
        HibernateUtil.beginTransaction();
        Session session = HibernateUtil.getSession();
        try {
            dosen.setIsLogin(0);
            session.update(dosen);
            result = 1;
        } catch (Exception ex) {
            throw ex;
        }
        return result;
    }

    public Iterator count() throws Exception {
        HibernateUtil.beginTransaction();
        Session session = HibernateUtil.getSession();
        Iterator iter = null;
        try {
            iter = session.createQuery("SELECT dsn.jenisKelamin, COUNT(*) FROM Dosen dsn GROUP BY dsn.jenisKelamin").iterate();
        } catch (Exception ex) {
            throw ex;
        }
        return iter;
    }
}
