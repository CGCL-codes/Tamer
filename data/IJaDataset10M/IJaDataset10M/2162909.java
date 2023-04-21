package org.sadhar.sipp.perekamananggaranbiayaunitnonrapatkerja;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Yohan Hardinugroho
 */
public interface PerekamanAnggaranBiayaUnitNonRapatKerjaDAO {

    public List<Map> listStatusPengajuan2(String tahun, String kodeUnit, String isRevisi, String isSudahDiajukan);

    public List<Map> listAnggaranBiayaRapatKerja2(String isRapat, String tahun, String kodeUnit, String isRevisi, String isSudahDiajukan);

    public List<Map> listStatusPengajuan(String tahun, String kodeUnit);

    public double countStatusPengajuan(String tahun, String kodeUnit);

    public String selectIsDikunci(String tahun, String kodeUnit, String isRevisi) throws Exception;

    public double totalAnggaranNonRapat(String tahun, String kodeUnit, String isRevisi, String isDiajukan);

    public double totalAnggaranRapat(String tahun, String kodeUnit, String isRevisi, String isDiajukan);

    public List<Map> listAnggaranBiayaRapatKerja(String isRapat, String tahun, String kodeUnit);

    public List<Map> listAnggaranBiayaRapatKerjaForButtonKirim(String isRapat, String tahun, String kodeUnit, String isRevisi, String isSudahDiajukan);

    public String tahunKelompokAnggaranYangDigunakan(String tahunAnggaran);

    public List<Map> listKelompokBiaya(String tahun, String namaPosBiaya);

    public List<Map> cekPosBiayaKhususUnit(int kodeKelompokBiaya, String kodeUnitKerja);

    public void delete(String tahun, String kodeUnit, int kodePosBiaya, int kodeItemBiayaBarang, String isRevisi);

    public List<Map> listNamaItemFromItemBiaya(int kodePosBiaya);

    public List<Map> listDetailFromItemBiaya(String tahun, int kodeItem);

    public List<Map> listDetailJenisBarang(int kodeklasifikasi, String jenisBarang);

    public List<Map> listDetailBarang(int idJenisBarang, String namaBarang);

    public List<Map> listHargaBarang(String tahun, int idBarang);

    public List<Map> listKendaraanBarangInvenstasi(String searchKey);

    public void insert(String tahun, String kodeUnit, int kodePosBiaya, int kodeItemBiayaBarang, double harga, int kuantitas, String isRevisi, String isDiajukan, int kodeSubItemBiayaBarang);

    public int cek(String tahun, String kodeUnit, int kodePosBiaya, int kodeItemBiayaBarang, String isRevisi);

    public List<Map> listDetailsIfItemBiaya(String tahun, String kodeUnit, int kodePosBiaya);

    public List<Map> listDetailsIfBarang(String tahun, String kodeUnit, int kodePosBiaya);

    public List<Map> listDetailsIfItemBiayaRev(String tahun, String kodeUnit, int kodePosBiaya);

    public List<Map> listDetailsIfBarangRev(String tahun, String kodeUnit, int kodePosBiaya);

    public List<Map> listDetailsKendaraanRev(String tahun, String kodeUnit, int kodePosBiaya);

    public List<Map> listDetailsKendaraan(String tahun, String kodeUnit, int kodePosBiaya);

    public void updateIsDiajukan(String tahun, String kodeUnit);

    public int cekKunciAnggaranBiaya(String tahun, String kodeUnit);

    public void insertKunciAnggaranBiaya(String tahun, String kodeUnit);

    public void updateKunciAnggaranBiaya(String tahun, String kodeUnit);

    public boolean isDikunci(String tahun, String kodeUnit, int kodeJenisAnggaran);
}
