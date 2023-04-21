package controller;

import model.JpaPengajuan;
import model.JpaMahasiswa;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import model.Mahasiswa;
import model.Pengajuan;

/**
 *
 * @author yosua
 */
public class ControllerPengajuan {

    HttpServletRequest request;

    public ControllerPengajuan(HttpServletRequest request) {
        this.request = request;
    }

    /**
     * Melakukan list pengajuan berdasarkan nrp dengan me-remove objek yang nilai status bernilai "Daftar"
     */
    public void setListStatusByNrp() {
        String nrp = request.getParameter("nrp");
        JpaPengajuan listPengajuan = new JpaPengajuan();
        HttpSession session = request.getSession();
        List<Pengajuan> list = new ArrayList<Pengajuan>();
        list = listPengajuan.getAllPengajuanByNrp(nrp);
        Iterator itr = list.listIterator();
        if (itr.hasNext()) {
            Pengajuan pengajuan = (Pengajuan) itr.next();
            if (pengajuan.getStatuspengajuan().equals("Daftar")) {
                itr.remove();
            }
            session.setAttribute("list_pengajuan_by_nrp", list);
        } else {
            request.setAttribute("pesan", "Anda belum memiliki record Status Beasiswa");
        }
    }

    /**
     * Melakukan list pengajuan berdasarkan nrp
     */
    public void setListPengajuanByNrp() {
        Pengajuan pengajuan = new Pengajuan();
        String nrp = request.getParameter("nrp");
        JpaPengajuan listPengajuan = new JpaPengajuan();
        HttpSession session = request.getSession();
        List<Pengajuan> list = new ArrayList<Pengajuan>();
        list = listPengajuan.getAllPengajuanByNrp(nrp);
        session.setAttribute("list_pengajuan_by_nrp", list);
    }

    /**
     * Melakukan list seluruh pengajuan yang ada dalam db
     */
    public void setListPengajuan() {
        Pengajuan pengajuan = new Pengajuan();
        JpaPengajuan listPengajuan = new JpaPengajuan();
        HttpSession session = request.getSession();
        List<Pengajuan> list = new ArrayList<Pengajuan>();
        list = listPengajuan.getAllPengajuan();
        session.setAttribute("list_all_pengajuan", list);
    }

    /**
     * Melakukan list pengajuan berdasarkan idbeasiswa
     */
    public void setListPengajuanByIdBeasiswa() {
        Pengajuan pengajuan = new Pengajuan();
        String idbeasiswa = request.getParameter("pilih");
        String sort = request.getParameter("pilih_sort");
        JpaPengajuan listPengajuan = new JpaPengajuan();
        HttpSession session = request.getSession();
        List<Pengajuan> list = new ArrayList<Pengajuan>();
        if (sort.equals("ipk")) {
            list = listPengajuan.getAllPengajuanByIdBeasiswaOrderByIpk(idbeasiswa);
        } else if (sort.equals("gaji")) {
            list = listPengajuan.getAllPengajuanByIdBeasiswaOrderByGaji(idbeasiswa);
        } else {
            list = listPengajuan.getAllPengajuan();
        }
        session.setAttribute("list_all_pengajuan", list);
    }

    /**
     * Fungsi untuk melakukan pencarian objek berdasarkan
     * idpengajuan kemudian menyimpan dalam session pengajuan
     */
    public void setPengajuan() {
        String idPengajuan = request.getParameter("idpengajuan");
        JpaPengajuan cariPengajuan = new JpaPengajuan();
        Pengajuan pengajuan = new Pengajuan();
        pengajuan = cariPengajuan.findPengajuanById(idPengajuan);
        HttpSession session = request.getSession();
        session.setAttribute("pengajuan", pengajuan);
    }

    /**
     * Fungsi automasi untuk status pengajuan yang telah diterima menjadi kadaluarsa
     * fungsi ini berjalan otomatis setiap admin ingin melakukan rekapitulasi
     */
    public void setStatusPenerima() {
        JpaPengajuan listPengajuan = new JpaPengajuan();
        JpaMahasiswa aturPengajuan = new JpaMahasiswa();
        HttpSession session = request.getSession();
        List<Pengajuan> list = new ArrayList<Pengajuan>();
        list = listPengajuan.getAllPengajuan();
        Iterator itr = list.listIterator();
        Date tglSaatIni = null;
        Calendar currentDate = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String dateNow = formatter.format(currentDate.getTime());
        try {
            tglSaatIni = (Date) formatter.parse(dateNow);
        } catch (ParseException ex) {
            Logger.getLogger(ControllerPengajuan.class.getName()).log(Level.SEVERE, null, ex);
        }
        while (itr.hasNext()) {
            Pengajuan pengajuan = (Pengajuan) itr.next();
            Mahasiswa mahasiswa = new Mahasiswa();
            mahasiswa.setNrp(pengajuan.getNrp().getNrp());
            mahasiswa = aturPengajuan.findMahasiswaByNrp(mahasiswa.getNrp());
            if (tglSaatIni.after(pengajuan.getIdbeasiswa().getTanggalhabis()) == true) {
                if (pengajuan.getStatuspengajuan().equals("Terima")) {
                    pengajuan.setStatuspengajuan("Berakhir");
                    mahasiswa.setStatuspenerima("Tidak Sedang Menerima Beasiswa");
                    try {
                        listPengajuan.edit(pengajuan);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    itr.remove();
                }
            }
            if (tglSaatIni.before(pengajuan.getIdbeasiswa().getTanggalhabis()) == true) {
                if (pengajuan.getStatuspengajuan().equals("Berakhir")) {
                    pengajuan.setStatuspengajuan("Terima");
                    mahasiswa.setStatuspenerima("Sedang Menerima Beasiswa");
                    try {
                        listPengajuan.edit(pengajuan);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            try {
                aturPengajuan.edit(mahasiswa);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Fungsi yang digunakan dalam rekapitulasi untuk merubah status penerima beasiswa
     */
    public void setAturStatus() {
        Mahasiswa mahasiswa = new Mahasiswa();
        String nrp = request.getParameter("nrp");
        String idpengajuan = request.getParameter("idpengajuan");
        JpaMahasiswa aturStatus = new JpaMahasiswa();
        JpaPengajuan aturPengajuan = new JpaPengajuan();
        mahasiswa = aturStatus.findMahasiswaByNrp(nrp);
        Pengajuan pengajuan = new Pengajuan();
        pengajuan = aturPengajuan.findPengajuanById(idpengajuan);
        if (mahasiswa.getStatuspenerima().equals("Sedang Menerima Beasiswa")) {
            pengajuan.setStatuspengajuan("Daftar");
            mahasiswa.setStatuspenerima("Tidak Sedang Menerima Beasiswa");
            try {
                aturPengajuan.edit(pengajuan);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            mahasiswa.setStatuspenerima("Sedang Menerima Beasiswa");
            pengajuan.setStatuspengajuan("Terima");
            try {
                aturPengajuan.edit(pengajuan);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            aturStatus.edit(mahasiswa);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
