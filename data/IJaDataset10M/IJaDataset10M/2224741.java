package wii.edu.core.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.hibernate.Session;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import wii.edu.core.model.DataKelulusan;
import wii.edu.core.model.Fakultas;
import wii.edu.core.model.Mahasiswa;
import wii.edu.core.model.ProgramStudi;

/**
 *
 * @author Hendro
 */
public class KelulusanDAO {

    /** Creates a new instance of DataKelulusanDAO */
    public KelulusanDAO() {
    }

    public DataKelulusan getDataKelulusan(long id) throws Exception {
        HibernateUtil.beginTransaction();
        Session session = HibernateUtil.getSession();
        DataKelulusan dataKelulusan = null;
        try {
            dataKelulusan = (DataKelulusan) session.load(DataKelulusan.class, id);
        } catch (Exception ex) {
            throw ex;
        }
        return dataKelulusan;
    }

    public JSONObject getAllDataKelulusanJSONObject() throws Exception {
        List list = new MahasiswaDAO().getAllMahasiswaLulus();
        JSONObject root = new JSONObject();
        JSONArray dataKelulusan = new JSONArray();
        for (int a = 0; a < list.size(); a++) {
            Mahasiswa mhs = (Mahasiswa) list.get(a);
            DataKelulusan data = mhs.getDataKelulusan();
            if (data != null) {
                JSONObject jsonUser = new JSONObject();
                jsonUser.put("id", mhs.getId());
                jsonUser.put("noInduk", mhs.getNomorInduk());
                jsonUser.put("nama", mhs.getNama());
                jsonUser.put("jurusan", mhs.getFakultas().getNama() + " / " + mhs.getProgdi().getNama());
                SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
                jsonUser.put("tglUjian", format.format(data.getTglUjian()));
                jsonUser.put("tglYudisium", format.format(data.getTglYudisium()));
                jsonUser.put("judulSkripsi", data.getJudulSkripsi());
                jsonUser.put("nilai", data.getNilai());
                jsonUser.put("predikat", data.getPredikat());
                dataKelulusan.add(jsonUser);
            }
        }
        root.put("daftarDataKelulusan", dataKelulusan);
        return root;
    }

    public JSONObject getAllDataKelulusanJSONObject(int start, int limit) throws Exception {
        List list = new MahasiswaDAO().getAllMahasiswaLulus(start, limit);
        List list_all = new MahasiswaDAO().getAllMahasiswaLulus();
        JSONObject root = new JSONObject();
        JSONArray dataKelulusan = new JSONArray();
        for (int a = 0; a < list.size(); a++) {
            Mahasiswa mhs = (Mahasiswa) list.get(a);
            DataKelulusan data = mhs.getDataKelulusan();
            if (data != null) {
                JSONObject jsonUser = new JSONObject();
                jsonUser.put("id", mhs.getId());
                jsonUser.put("noInduk", mhs.getNomorInduk());
                jsonUser.put("nama", mhs.getNama());
                jsonUser.put("jurusan", mhs.getFakultas().getNama() + " / " + mhs.getProgdi().getNama());
                SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
                jsonUser.put("tglUjian", format.format(data.getTglUjian()));
                jsonUser.put("tglYudisium", format.format(data.getTglYudisium()));
                jsonUser.put("judulSkripsi", data.getJudulSkripsi());
                jsonUser.put("nilai", data.getNilai());
                jsonUser.put("predikat", data.getPredikat());
                dataKelulusan.add(jsonUser);
            }
        }
        root.put("total", list_all.size());
        root.put("daftarDataKelulusan", dataKelulusan);
        return root;
    }

    public JSONObject getAllDataKelulusanJSONObject(Fakultas fak) throws Exception {
        List list = new MahasiswaDAO().getAllMahasiswaLulus(fak);
        JSONObject root = new JSONObject();
        JSONArray dataKelulusan = new JSONArray();
        for (int a = 0; a < list.size(); a++) {
            Mahasiswa mhs = (Mahasiswa) list.get(a);
            DataKelulusan data = mhs.getDataKelulusan();
            if (data != null) {
                JSONObject jsonUser = new JSONObject();
                jsonUser.put("id", mhs.getId());
                jsonUser.put("noInduk", mhs.getNomorInduk());
                jsonUser.put("nama", mhs.getNama());
                jsonUser.put("jurusan", mhs.getFakultas().getNama() + " / " + mhs.getProgdi().getNama());
                SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
                jsonUser.put("tglUjian", format.format(data.getTglUjian()));
                jsonUser.put("tglYudisium", format.format(data.getTglYudisium()));
                jsonUser.put("judulSkripsi", data.getJudulSkripsi());
                jsonUser.put("nilai", data.getNilai());
                jsonUser.put("predikat", data.getPredikat());
                dataKelulusan.add(jsonUser);
            }
        }
        root.put("daftarDataKelulusan", dataKelulusan);
        return root;
    }

    public JSONObject getAllDataKelulusanJSONObject(Fakultas fak, int start, int limit) throws Exception {
        List list = new MahasiswaDAO().getAllMahasiswaLulus(fak, start, limit);
        List list_all = new MahasiswaDAO().getAllMahasiswaLulus(fak);
        JSONObject root = new JSONObject();
        JSONArray dataKelulusan = new JSONArray();
        for (int a = 0; a < list.size(); a++) {
            Mahasiswa mhs = (Mahasiswa) list.get(a);
            DataKelulusan data = mhs.getDataKelulusan();
            if (data != null) {
                JSONObject jsonUser = new JSONObject();
                jsonUser.put("id", mhs.getId());
                jsonUser.put("noInduk", mhs.getNomorInduk());
                jsonUser.put("nama", mhs.getNama());
                jsonUser.put("jurusan", mhs.getFakultas().getNama() + " / " + mhs.getProgdi().getNama());
                SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
                jsonUser.put("tglUjian", format.format(data.getTglUjian()));
                jsonUser.put("tglYudisium", format.format(data.getTglYudisium()));
                jsonUser.put("judulSkripsi", data.getJudulSkripsi());
                jsonUser.put("nilai", data.getNilai());
                jsonUser.put("predikat", data.getPredikat());
                dataKelulusan.add(jsonUser);
            }
        }
        root.put("total", list_all.size());
        root.put("daftarDataKelulusan", dataKelulusan);
        return root;
    }

    public JSONObject getAllDataKelulusanJSONObject(Fakultas fak, ProgramStudi prog, int start, int limit) throws Exception {
        List list = new MahasiswaDAO().getAllMahasiswaLulus(fak, prog, start, limit);
        List list_all = new MahasiswaDAO().getAllMahasiswaLulus(fak, prog);
        JSONObject root = new JSONObject();
        JSONArray dataKelulusan = new JSONArray();
        for (int a = 0; a < list.size(); a++) {
            Mahasiswa mhs = (Mahasiswa) list.get(a);
            DataKelulusan data = mhs.getDataKelulusan();
            if (data != null) {
                JSONObject jsonUser = new JSONObject();
                jsonUser.put("id", mhs.getId());
                jsonUser.put("noInduk", mhs.getNomorInduk());
                jsonUser.put("nama", mhs.getNama());
                jsonUser.put("jurusan", mhs.getFakultas().getNama() + " / " + mhs.getProgdi().getNama());
                SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
                jsonUser.put("tglUjian", format.format(data.getTglUjian()));
                jsonUser.put("tglYudisium", format.format(data.getTglYudisium()));
                jsonUser.put("judulSkripsi", data.getJudulSkripsi());
                jsonUser.put("nilai", data.getNilai());
                jsonUser.put("predikat", data.getPredikat());
                dataKelulusan.add(jsonUser);
            }
        }
        root.put("total", list_all.size());
        root.put("daftarDataKelulusan", dataKelulusan);
        return root;
    }
}
