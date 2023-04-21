package com.pn.action;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import com.kn.core.struts.base.BaseAction;
import com.pn.bo.LoaiVatTuBean;
import com.pn.bo.VatTuBean;
import com.pn.bo.VatTuGiaNgayBean;
import com.pn.services.LoaiVatTuServices;
import com.pn.services.VatTuServices;
import com.pn.utils.ExcelWriterUtils;
import com.pn.utils.StringUtils;
import com.pn.utils.UIMessages;

public class VattuAction extends BaseAction {

    private static final long serialVersionUID = 1L;

    private Long vatTuId;

    private Long loaiVatTuId;

    private String maLoaiVatTu;

    private String tenLoaiVatTu;

    private String maVatTu;

    private String tenVatTu;

    private String donViTinh;

    private Double giaMua;

    private String ngayHieuLuc;

    private Double giaMuaDuBao;

    private String ghiChu;

    private Boolean vatTuTam;

    private Boolean isNgungSuDung;

    private VatTuServices vatTuServices;

    private LoaiVatTuServices loaiVatTuServices;

    private List<LoaiVatTuBean> loaiVatTuList;

    private List<VatTuBean> vatTuList;

    private List<VatTuGiaNgayBean> vatTuGiaNgayList;

    private InputStream inputStream;

    private File upload;

    private String uploadContentType;

    private String uploadFileName;

    private static final String URL_REDIRECT = "/page/com/pn/action/VattuAction/process.action";

    private String url;

    private String searchText;

    private Long option;

    private Integer numOfRow;

    public String process() {
        try {
            if (url != null && url.trim().length() > 0) {
                url = "";
            } else {
                this.clearActionErrors();
                this.clear();
            }
            if (!hasActionErrors()) {
                this.clearErrorsAndMessages();
                loaiVatTuList = loaiVatTuServices.getAllLoaiVatTu();
                numOfRow = vatTuServices.getCountAllVatTu();
                String pageNum = this.getRequest().getParameter("d-49653-p");
                if (pageNum == null) {
                    pageNum = "1";
                }
                vatTuList = vatTuServices.getAllVatTu(new Integer(pageNum));
            }
        } catch (Exception e) {
            addActionError(UIMessages.VIEW_ERROR);
            return SUCCESS;
        }
        return SUCCESS;
    }

    public String viewExport() {
        try {
            if (url != null && url.trim().length() > 0) {
                url = "";
            } else {
                this.clearActionErrors();
                this.clear();
            }
            if (!hasActionErrors()) {
                this.clearErrorsAndMessages();
                vatTuList = vatTuServices.getAllVatTu();
            }
        } catch (Exception e) {
            addActionError(UIMessages.VIEW_ERROR);
            return SUCCESS;
        }
        return "viewExport";
    }

    private LoaiVatTuBean getLoaiVatTuByMa(String maLoaiVatTu) {
        if (loaiVatTuList == null || loaiVatTuList.size() == 0) return null;
        for (LoaiVatTuBean bean : loaiVatTuList) {
            if (bean.getMaPhanLoai().equalsIgnoreCase(maLoaiVatTu)) {
                return bean;
            }
        }
        return null;
    }

    public String searchVatTu() {
        if (searchText != null && searchText.trim().length() > 0) {
            try {
                if (searchText.contains(",")) {
                    String[] arr = searchText.split(",");
                    searchText = arr[0].trim();
                }
                if (option == 1) {
                    vatTuList = vatTuServices.searchVatTuByMa(searchText);
                }
                if (option == 2) {
                    vatTuList = vatTuServices.searchVatTuByTen(searchText);
                }
            } catch (Exception e) {
                addActionError(UIMessages.VIEW_ERROR);
                return SUCCESS;
            }
        }
        numOfRow = vatTuList.size();
        searchText = "";
        return SUCCESS;
    }

    public String displayInput() {
        try {
            this.clearErrorsAndMessages();
            String newMaTuDong = vatTuServices.getMaTuDongMoi();
            if (newMaTuDong != null && newMaTuDong.trim().length() > 0) {
                maVatTu = newMaTuDong;
            } else {
                maVatTu = UIMessages.MA_VAT_TU_BAN_DAU;
            }
            clear();
        } catch (Exception e) {
            addActionError(UIMessages.VIEW_ERROR);
            return INPUT;
        }
        return INPUT;
    }

    public String displayUpdate() {
        try {
            this.clearErrorsAndMessages();
            if (vatTuList != null && vatTuList.size() > 0) {
                for (VatTuBean bean : vatTuList) {
                    if (bean.getId().intValue() == vatTuId.intValue()) {
                        maVatTu = bean.getMaVatTu();
                        loaiVatTuId = bean.getLoaiVatTuId().getId();
                        maLoaiVatTu = bean.getLoaiVatTuId().getMaPhanLoai();
                        tenLoaiVatTu = bean.getLoaiVatTuId().getTenPhanLoai();
                        tenVatTu = bean.getTenVatTu();
                        donViTinh = bean.getDonViTinh();
                        giaMuaDuBao = bean.getGiaMuaDuBao();
                        ghiChu = bean.getGhiChu();
                        vatTuGiaNgayList = vatTuServices.getVatTuGiaNgayListByVatTuId(vatTuId);
                        VatTuGiaNgayBean giaNgayBean = vatTuServices.getLatestVatTuGiaNgay(vatTuId);
                        if (giaNgayBean != null) {
                            giaMua = giaNgayBean.getGiaMua();
                            ngayHieuLuc = StringUtils.formatDatePerVietnamPattern(giaNgayBean.getNgayHieuLuc());
                        }
                    }
                }
            } else {
                addActionError(UIMessages.VIEW_ERROR);
                return UPDATE;
            }
        } catch (Exception e) {
            addActionError(UIMessages.VIEW_ERROR);
            return UPDATE;
        }
        return UPDATE;
    }

    public String updateGiaNgay() {
        try {
            this.clearErrorsAndMessages();
            if (ngayHieuLuc == null || ngayHieuLuc.trim().length() == 0) {
                addActionError(UIMessages.VAT_TU_ERROR_NGAY_HIEU_LUC);
            }
            if (giaMua == null || giaMua == 0.0) {
                addActionError(UIMessages.VAT_TU_ERROR_GIA_MUA);
            }
            if (!hasActionErrors()) {
                vatTuServices.addOrUpdateVatTuGiaNgay(vatTuId, giaMua, StringUtils.parseDate(ngayHieuLuc));
                this.clearErrorsAndMessages();
                this.clear();
                addActionMessage(UIMessages.UPDATE_GIA_OK);
            } else {
                return UPDATE;
            }
        } catch (Exception e) {
            addActionError(UIMessages.VIEW_ERROR);
        }
        return UPDATE;
    }

    public String save() {
        try {
            checkSave();
            if (!hasActionErrors()) {
                vatTuServices.addVatTu(loaiVatTuId, maVatTu, tenVatTu, donViTinh, vatTuTam, isNgungSuDung, giaMua, StringUtils.parseDate(ngayHieuLuc), giaMuaDuBao, ghiChu);
                auditLogServices.addAuditLog(getUserName(), "", "Tạo thông tin vật tư: " + UIMessages.MA_VAT_TU + " " + maVatTu + ", " + UIMessages.TEN_VAT_TU + " " + tenVatTu + ", " + UIMessages.DON_VI_TINH + " " + donViTinh + ", " + UIMessages.GIA_MUA + " " + giaMua + ", " + UIMessages.NGAY_HIEU_LUC + " " + ngayHieuLuc + ", " + UIMessages.GIA_BAN_DU_BAO + " " + giaMuaDuBao + ", " + UIMessages.GHI_CHU + " " + ghiChu);
                this.clearActionErrors();
                this.clear();
                addActionMessage(UIMessages.SAVE_OK);
            } else {
                return INPUT;
            }
        } catch (Exception e) {
            addActionError(UIMessages.SAVE_ERROR);
            return INPUT;
        }
        setUrl(URL_REDIRECT);
        return REDIRECT;
    }

    public String update() {
        try {
            checkSave();
            if (!hasActionErrors()) {
                vatTuServices.updateVatTu(vatTuId, maVatTu, tenVatTu, donViTinh, vatTuTam, isNgungSuDung, giaMua, StringUtils.parseDate(ngayHieuLuc), giaMuaDuBao, ghiChu);
                auditLogServices.addAuditLog(getUserName(), "", "Sửa thông tin vật tư: " + UIMessages.MA_VAT_TU + " " + maVatTu + ", " + UIMessages.TEN_VAT_TU + " " + tenVatTu + ", " + UIMessages.DON_VI_TINH + " " + donViTinh + ", " + UIMessages.GIA_MUA + " " + giaMua + ", " + UIMessages.NGAY_HIEU_LUC + " " + ngayHieuLuc + ", " + UIMessages.GIA_BAN_DU_BAO + " " + giaMuaDuBao + ", " + UIMessages.GHI_CHU + " " + ghiChu);
                this.clearActionErrors();
                this.clear();
            } else {
                return UPDATE;
            }
        } catch (Exception e) {
            addActionError(UIMessages.SAVE_ERROR);
            return UPDATE;
        }
        setUrl(URL_REDIRECT);
        return REDIRECT;
    }

    private void checkSave() {
        this.clearErrorsAndMessages();
        if (maVatTu == null || maVatTu.trim().length() == 0) {
            addActionError(UIMessages.VAT_TU_ERROR_MA);
        }
        if (tenVatTu == null || tenVatTu.trim().length() == 0) {
            addActionError(UIMessages.VAT_TU_ERROR_TEN);
        }
        if (giaMua != null && (ngayHieuLuc == null || ngayHieuLuc.trim().length() == 0)) {
            addActionError(UIMessages.VAT_TU_ERROR_NGAY_HIEU_LUC);
        } else if (ngayHieuLuc != null && StringUtils.parseDate(ngayHieuLuc).after(StringUtils.parseDate(StringUtils.now()))) {
            addActionError("Ngày Hiệu Lực Không được sau ngày hiện tại.");
        }
        if (loaiVatTuId == null || loaiVatTuId == -1) {
            addActionError(UIMessages.VAT_TU_ERROR_LOAI);
        }
    }

    public String delete() {
        try {
            if (!hasActionErrors()) {
                vatTuServices.deleteVatTu(vatTuId);
            }
        } catch (Exception e) {
            addActionError(UIMessages.DELETE_ERROR);
        }
        setUrl(URL_REDIRECT);
        return REDIRECT;
    }

    private void clear() {
        loaiVatTuId = new Long(-1);
        tenVatTu = "";
        donViTinh = "";
        ngayHieuLuc = "";
        giaMua = null;
        giaMuaDuBao = null;
        ghiChu = null;
        searchText = "";
    }

    public String download() {
        InputStream bis = null;
        try {
            ByteArrayOutputStream bos = (ByteArrayOutputStream) ExcelWriterUtils.writeVatTuExcel();
            bis = new ByteArrayInputStream(bos.toByteArray());
        } catch (Exception e) {
            addActionError(UIMessages.DOWNLOAD_ERROR);
        }
        setInputStream(bis);
        getResponse().setHeader("Content-Disposition", "attachment;filename=VatTu.xls");
        return DOWNLOAD;
    }

    public String saveExcel() {
        try {
            List<VatTuBean> beanList = parseAndCheckExcel();
            if (beanList != null && beanList.size() > 0 && !hasActionErrors()) {
                for (VatTuBean bean : beanList) {
                    VatTuGiaNgayBean vatTuGiaNgayBean = (VatTuGiaNgayBean) bean.getVatTuGiaNgayList().iterator().next();
                    vatTuServices.addVatTu(bean.getLoaiVatTuId().getId(), bean.getMaVatTu(), bean.getTenVatTu(), bean.getDonViTinh(), bean.getVatTuTam(), bean.getIsNgungSuDung(), vatTuGiaNgayBean.getGiaMua(), vatTuGiaNgayBean.getNgayHieuLuc(), bean.getGiaMuaDuBao(), bean.getGhiChu());
                }
                this.addActionMessage(UIMessages.SAVE_OK);
            } else {
                return INPUT;
            }
        } catch (Exception e) {
            addActionError(UIMessages.READ_FILE_ERROR);
            return INPUT;
        }
        return INPUT;
    }

    private List<VatTuBean> parseAndCheckExcel() throws Exception {
        this.clearActionErrors();
        if (getUpload() == null) {
            addActionError(UIMessages.READ_FILE_ERROR);
            return null;
        }
        POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(getUpload()));
        HSSFWorkbook wb = new HSSFWorkbook(fs);
        HSSFSheet sheet = wb.getSheetAt(0);
        HSSFRow headerRow = null;
        HSSFCell headerCell = null;
        if (sheet != null && sheet.rowIterator() != null) {
            headerRow = (HSSFRow) sheet.rowIterator().next();
        }
        if (headerRow != null && headerRow.cellIterator() != null) {
            headerCell = (HSSFCell) headerRow.cellIterator().next();
        }
        HSSFRow row = null;
        int startRow = -1;
        int startColumn = -1;
        List<VatTuBean> list = new ArrayList<VatTuBean>();
        if (headerCell != null && headerCell.getCellType() == HSSFCell.CELL_TYPE_STRING && (headerCell.getStringCellValue().equalsIgnoreCase(UIMessages.LOAI_VAT_TU))) {
            startRow = headerCell.getRowIndex() + 1;
            startColumn = headerCell.getColumnIndex();
        }
        String specialText = "";
        if (startRow > -1 && startRow > -1) {
            for (; startRow <= sheet.getLastRowNum(); startRow++) {
                row = sheet.getRow(startRow);
                VatTuBean bean = new VatTuBean();
                String maLoaiVatTu = getCellValue(row.getCell(startColumn));
                if (loaiVatTuList == null || maLoaiVatTu == null || maLoaiVatTu.trim().length() == 0) {
                    addActionError(UIMessages.LOAI_VAT_TU_ERROR_NOT_EXIST);
                    return null;
                }
                LoaiVatTuBean loaiVatTuBean = getLoaiVatTuByMa(maLoaiVatTu);
                bean.setLoaiVatTuId(loaiVatTuBean);
                if (bean.getLoaiVatTuId() == null) {
                    addActionError(UIMessages.LOAI_VAT_TU_ERROR_NOT_EXIST);
                    addActionError(UIMessages.RE_CHECK_FILE_ERROR);
                    return null;
                }
                String maVT = getCellValue(row.getCell(startColumn + 1));
                if ("".equals(maVT.trim())) {
                    addActionError(UIMessages.VAT_TU_ERROR_MA);
                    addActionError(UIMessages.RE_CHECK_FILE_ERROR);
                    return null;
                }
                if (StringUtils.isChildInRoot(specialText, maVT)) {
                    addActionError(UIMessages.VAT_TU_ERROR_MA_DUPLICATE + " " + maVT);
                    addActionError(UIMessages.RE_CHECK_FILE_ERROR);
                    return null;
                }
                specialText = specialText + maVT + "=";
                bean.setMaVatTu(maVT);
                bean.setTenVatTu(getCellValue(row.getCell(startColumn + 2)));
                bean.setDonViTinh(getCellValue(row.getCell(startColumn + 3)));
                VatTuGiaNgayBean vatTuGiaNgayBean = new VatTuGiaNgayBean();
                String giamua = getCellValue(row.getCell(startColumn + 4));
                if (giamua == null || giamua.trim().length() == 0) {
                    addActionError(UIMessages.VAT_TU_ERROR_GIA_MUA);
                    addActionError(UIMessages.RE_CHECK_FILE_ERROR);
                    return null;
                }
                if (!StringUtils.isNumber(giamua)) {
                    addActionError(UIMessages.VAT_TU_ERROR_GIA_MUA_NUM);
                    addActionError(UIMessages.RE_CHECK_FILE_ERROR);
                    return null;
                }
                vatTuGiaNgayBean.setGiaMua(Double.parseDouble(giamua));
                String ngayHieuLuc = getCellValue(row.getCell(startColumn + 5));
                if (ngayHieuLuc == null || ngayHieuLuc.trim().length() == 0 || StringUtils.parseDate(ngayHieuLuc) == null) {
                    ngayHieuLuc = StringUtils.now();
                }
                if (ngayHieuLuc != null && StringUtils.parseDate(ngayHieuLuc) != null && StringUtils.parseDate(ngayHieuLuc).after(StringUtils.parseDate(StringUtils.now()))) {
                    addActionError("Ngày Hiệu Lực Không được sau ngày hiện tại.");
                    addActionError(UIMessages.RE_CHECK_FILE_ERROR);
                    return null;
                }
                vatTuGiaNgayBean.setNgayHieuLuc(StringUtils.parseDate(ngayHieuLuc));
                Set<VatTuGiaNgayBean> set = new HashSet<VatTuGiaNgayBean>();
                set.add(vatTuGiaNgayBean);
                bean.setVatTuGiaNgayList(set);
                list.add(bean);
            }
        }
        return list;
    }

    private static String getCellValue(HSSFCell cell) {
        if (cell != null && cell.toString().trim().length() > 0) return cell.toString();
        return "";
    }

    public void setMaVatTu(String maVatTu) {
        this.maVatTu = maVatTu;
    }

    public String getMaVatTu() {
        return maVatTu;
    }

    public void setTenVatTu(String tenVatTu) {
        this.tenVatTu = tenVatTu;
    }

    public String getTenVatTu() {
        return tenVatTu;
    }

    public void setDonViTinh(String donViTinh) {
        this.donViTinh = donViTinh;
    }

    public String getDonViTinh() {
        return donViTinh;
    }

    public Double getGiaMua() {
        return giaMua;
    }

    public void setGiaMua(Double giaMua) {
        this.giaMua = giaMua;
    }

    public void setNgayHieuLuc(String ngayHieuLuc) {
        this.ngayHieuLuc = ngayHieuLuc;
    }

    public String getNgayHieuLuc() {
        return ngayHieuLuc;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setUpload(File upload) {
        this.upload = upload;
    }

    public File getUpload() {
        return upload;
    }

    public void setUploadContentType(String uploadContentType) {
        this.uploadContentType = uploadContentType;
    }

    public String getUploadContentType() {
        return uploadContentType;
    }

    public void setUploadFileName(String uploadFileName) {
        this.uploadFileName = uploadFileName;
    }

    public String getUploadFileName() {
        return uploadFileName;
    }

    public void setVatTuId(Long vatTuId) {
        this.vatTuId = vatTuId;
    }

    public Long getVatTuId() {
        return vatTuId;
    }

    public void setVatTuServices(VatTuServices vatTuServices) {
        this.vatTuServices = vatTuServices;
    }

    public VatTuServices getVatTuServices() {
        return vatTuServices;
    }

    public void setLoaiVatTuList(List<LoaiVatTuBean> loaiVatTuList) {
        this.loaiVatTuList = loaiVatTuList;
    }

    public List<LoaiVatTuBean> getLoaiVatTuList() {
        return loaiVatTuList;
    }

    public void setVatTuList(List<VatTuBean> vatTuList) {
        this.vatTuList = vatTuList;
    }

    public List<VatTuBean> getVatTuList() {
        return vatTuList;
    }

    public void setVatTuGiaNgayList(List<VatTuGiaNgayBean> vatTuGiaNgayList) {
        this.vatTuGiaNgayList = vatTuGiaNgayList;
    }

    public List<VatTuGiaNgayBean> getVatTuGiaNgayList() {
        return vatTuGiaNgayList;
    }

    public void setLoaiVatTuServices(LoaiVatTuServices loaiVatTuServices) {
        this.loaiVatTuServices = loaiVatTuServices;
    }

    public LoaiVatTuServices getLoaiVatTuServices() {
        return loaiVatTuServices;
    }

    public void setVatTuTam(Boolean vatTuTam) {
        this.vatTuTam = vatTuTam;
    }

    public Boolean getVatTuTam() {
        return vatTuTam;
    }

    public void setIsNgungSuDung(Boolean isNgungSuDung) {
        this.isNgungSuDung = isNgungSuDung;
    }

    public Boolean getIsNgungSuDung() {
        return isNgungSuDung;
    }

    public void setLoaiVatTuId(Long loaiVatTuId) {
        this.loaiVatTuId = loaiVatTuId;
    }

    public Long getLoaiVatTuId() {
        return loaiVatTuId;
    }

    public void setMaLoaiVatTu(String maLoaiVatTu) {
        this.maLoaiVatTu = maLoaiVatTu;
    }

    public String getMaLoaiVatTu() {
        return maLoaiVatTu;
    }

    public void setTenLoaiVatTu(String tenLoaiVatTu) {
        this.tenLoaiVatTu = tenLoaiVatTu;
    }

    public String getTenLoaiVatTu() {
        return tenLoaiVatTu;
    }

    public void setGiaMuaDuBao(Double giaMuaDuBao) {
        this.giaMuaDuBao = giaMuaDuBao;
    }

    public Double getGiaMuaDuBao() {
        return giaMuaDuBao;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setOption(Long option) {
        this.option = option;
    }

    public Long getOption() {
        return option;
    }

    public Integer getNumOfRow() {
        return numOfRow;
    }

    public void setNumOfRow(Integer numOfRow) {
        this.numOfRow = numOfRow;
    }
}
