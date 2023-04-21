package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mirza
 */
public class Pegawai extends DatabaseModel {

    private String nip;

    private String namaPegawai;

    private String jenisKelamin;

    private String alamat;

    private String contactPerson;

    private String tanggalDiterima;

    private String jabatan;

    private String statusKerja;

    public Pegawai() {
        super();
    }

    @Override
    public boolean insertTransaction() {
        try {
            String sql = "insert into pegawai(`nip`,`nama_pegawai`,`jenis_kelamin`,`alamat`,`contact_person`,`tanggal_diterima`,`jabatan`,`status_kerja`) values('" + nip + "','" + namaPegawai + "','" + jenisKelamin + "','" + alamat + "','" + contactPerson + "','" + tanggalDiterima + "','" + jabatan + "','" + statusKerja + "')";
            super.statement.execute(sql);
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(Barang.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public boolean updateTransaction() {
        try {
            String sql = "update pegawai set nama_pegawai = '" + namaPegawai + "', jenis_kelamin = '" + jenisKelamin + "',alamat = '" + alamat + "',contact_person = '" + contactPerson + "',tanggal_diterima = '" + tanggalDiterima + "',jabatan = '" + jabatan + "',status_kerja = '" + statusKerja + "' where nip = '" + nip + "'";
            super.statement.execute(sql);
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(Barang.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public boolean deleteTransaction() {
        try {
            String sql = "delete from pegawai where nip = '" + nip + "'";
            super.statement.execute(sql);
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(Barang.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public ResultSet getData(String filter) {
        try {
            String sql = "select * from pegawai " + filter;
            return super.statement.executeQuery(sql);
        } catch (SQLException ex) {
            Logger.getLogger(Suplier.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * @param nip the nip to set
     */
    public void setNip(String nip) {
        this.nip = nip;
    }

    /**
     * @param namaPegawai the namaPegawai to set
     */
    public void setNamaPegawai(String namaPegawai) {
        this.namaPegawai = namaPegawai;
    }

    /**
     * @param jenisKelamin the jenisKelamin to set
     */
    public void setJenisKelamin(String jenisKelamin) {
        this.jenisKelamin = jenisKelamin;
    }

    /**
     * @param alamat the alamat to set
     */
    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    /**
     * @param contactPerson the contactPerson to set
     */
    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    /**
     * @param tanggalDiterima the tanggalDiterima to set
     */
    public void setTanggalDiterima(String tanggalDiterima) {
        this.tanggalDiterima = tanggalDiterima;
    }

    /**
     * @param jabatan the jabatan to set
     */
    public void setJabatan(String jabatan) {
        this.jabatan = jabatan;
    }

    /**
     * @param statusKerja the statusKerja to set
     */
    public void setStatusKerja(String statusKerja) {
        this.statusKerja = statusKerja;
    }
}
