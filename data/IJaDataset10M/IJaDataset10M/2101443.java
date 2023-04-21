package com.x3.monitoring.dao.mysql;

import com.x3.monitoring.dao.KegiatanPemerintahDAO;
import com.x3.monitoring.entity.KegiatanPemerintah;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Hendro Steven
 */
public class KegiatanPemerintahDAOImpl implements KegiatanPemerintahDAO {

    private Connection conn;

    public KegiatanPemerintahDAOImpl(Connection conn) {
        this.conn = conn;
    }

    public void insert(KegiatanPemerintah kp) throws Exception {
        String sql = "INSERT INTO kegiatan_pemerintah(nama,jalan,kota,tahun,kelurahan_id,kecamatan_id," + "sumber_dana_id,bidang_usaha_pemerintah_id,nilai_investasi) " + "VALUES(?,?,?,?,?,?,?,?,?)";
        PreparedStatement ps = null;
        try {
            ps = this.conn.prepareStatement(sql);
            ps.setString(1, kp.getNama());
            ps.setString(2, kp.getJalan());
            ps.setString(3, kp.getKota());
            ps.setString(4, kp.getTahun());
            ps.setInt(5, kp.getKelurahan().getId());
            ps.setInt(6, kp.getKecamatan().getId());
            ps.setInt(7, kp.getSumberDana().getId());
            ps.setInt(8, kp.getBidang().getId());
            ps.setDouble(9, kp.getNilaiInvestasi());
            ps.execute();
        } catch (Exception ex) {
            throw new Exception("Insert KegiatanPemerintah Gagal!!\nProblems:\n" + ex.getMessage());
        }
    }

    public void update(KegiatanPemerintah kp) throws Exception {
        String sql = "UPDATE kegiatan_pemerintah SET nama=?,jalan=?,kota=?,tahun=?,kelurahan_id=?," + "kecamatan_id=?,sumber_dana_id=?,bidang_usaha_pemerintah_id=?,nilai_investasi=? WHERE id=?";
        PreparedStatement ps = null;
        try {
            ps = this.conn.prepareStatement(sql);
            ps.setString(1, kp.getNama());
            ps.setString(2, kp.getJalan());
            ps.setString(3, kp.getKota());
            ps.setString(4, kp.getTahun());
            ps.setInt(5, kp.getKelurahan().getId());
            ps.setInt(6, kp.getKecamatan().getId());
            ps.setInt(7, kp.getSumberDana().getId());
            ps.setInt(8, kp.getBidang().getId());
            ps.setDouble(9, kp.getNilaiInvestasi());
            ps.setInt(10, kp.getId());
            ps.execute();
        } catch (Exception ex) {
            throw new Exception("Update KegiatanPemerintah Gagal!!\nProblems:\n" + ex.getMessage());
        }
    }

    public void delete(int id) throws Exception {
        String sql = "DELETE FROM kegiatan_pemerintah WHERE id=?";
        PreparedStatement ps = null;
        try {
            ps = this.conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.execute();
        } catch (Exception ex) {
            throw new Exception("Delete KegiatanPemerintah Gagal!!\nProblems:\n" + ex.getMessage());
        }
    }

    public KegiatanPemerintah get(int id) throws Exception {
        KegiatanPemerintah kp = null;
        String sql = "SELECT id,nama,jalan,kota,tahun,kelurahan_id,kecamatan_id,sumber_dana_id,bidang_usaha_pemerintah_id,nilai_investasi FROM " + "kegiatan_pemerintah WHERE id=?";
        PreparedStatement ps = null;
        try {
            ps = this.conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                kp = new KegiatanPemerintah();
                kp.setId(rs.getInt(1));
                kp.setNama(rs.getString(2));
                kp.setJalan(rs.getString(3));
                kp.setKota(rs.getString(4));
                kp.setTahun(rs.getString(5));
                kp.setKelurahan(new KelurahanDAOImpl(conn).get(rs.getInt(6)));
                kp.setKecamatan(new KecamatanDAOImpl(conn).get(rs.getInt(7)));
                kp.setSumberDana(new SumberDanaDAOImpl(conn).get(rs.getInt(8)));
                kp.setBidang(new BidangUsahaPemerintahDAOImpl(conn).get(rs.getInt(9)));
                kp.setNilaiInvestasi(rs.getDouble(10));
            }
        } catch (Exception ex) {
            throw new Exception("Getting KegiatanPemerintah Gagal!!\nProblems:\n" + ex.getMessage());
        }
        return kp;
    }

    public List<KegiatanPemerintah> gets() throws Exception {
        List<KegiatanPemerintah> list = new ArrayList<KegiatanPemerintah>();
        String sql = "SELECT id,nama,jalan,kota,tahun,kelurahan_id,kecamatan_id,sumber_dana_id,bidang_usaha_pemerintah_id,nilai_investasi FROM " + "kegiatan_pemerintah";
        PreparedStatement ps = null;
        try {
            ps = this.conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                KegiatanPemerintah kp = new KegiatanPemerintah();
                kp.setId(rs.getInt(1));
                kp.setNama(rs.getString(2));
                kp.setJalan(rs.getString(3));
                kp.setKota(rs.getString(4));
                kp.setTahun(rs.getString(5));
                kp.setKelurahan(new KelurahanDAOImpl(conn).get(rs.getInt(6)));
                kp.setKecamatan(new KecamatanDAOImpl(conn).get(rs.getInt(7)));
                kp.setSumberDana(new SumberDanaDAOImpl(conn).get(rs.getInt(8)));
                kp.setBidang(new BidangUsahaPemerintahDAOImpl(conn).get(rs.getInt(9)));
                kp.setNilaiInvestasi(rs.getDouble(10));
                list.add(kp);
            }
        } catch (Exception ex) {
            throw new Exception("Getting KegiatanPemerintah Gagal!!\nProblems:\n" + ex.getMessage());
        }
        return list;
    }

    public List<KegiatanPemerintah> gets(String sql) throws Exception {
        List<KegiatanPemerintah> list = new ArrayList<KegiatanPemerintah>();
        PreparedStatement ps = null;
        try {
            ps = this.conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                KegiatanPemerintah kp = new KegiatanPemerintah();
                kp.setId(rs.getInt(1));
                kp.setNama(rs.getString(2));
                kp.setJalan(rs.getString(3));
                kp.setKota(rs.getString(4));
                kp.setTahun(rs.getString(5));
                kp.setKelurahan(new KelurahanDAOImpl(conn).get(rs.getInt(6)));
                kp.setKecamatan(new KecamatanDAOImpl(conn).get(rs.getInt(7)));
                kp.setSumberDana(new SumberDanaDAOImpl(conn).get(rs.getInt(8)));
                kp.setBidang(new BidangUsahaPemerintahDAOImpl(conn).get(rs.getInt(9)));
                kp.setNilaiInvestasi(rs.getDouble(10));
                list.add(kp);
            }
        } catch (Exception ex) {
            throw new Exception("Getting KegiatanPemerintah Gagal!!\nProblems:\n" + ex.getMessage());
        }
        return list;
    }
}
