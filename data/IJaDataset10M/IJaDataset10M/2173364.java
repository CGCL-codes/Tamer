package com.pn.action;

import java.util.List;
import com.kn.core.struts.base.BaseAction;
import com.pn.bo.LoaiVatTuBean;
import com.pn.services.LoaiVatTuServices;
import com.pn.utils.UIMessages;

public class PhanLoaiVatTuAction extends BaseAction {

    private static final long serialVersionUID = 1L;

    private Long phanLoaiId;

    private String maPhanLoai;

    private String tenPhanLoai;

    private String ghiChu;

    private List<LoaiVatTuBean> loaiVatTuList;

    private LoaiVatTuServices loaiVatTuServices;

    private static final String URL_REDIRECT = "/page/com/pn/action/PhanLoaiVatTuAction/process.action";

    private String url;

    public String process() {
        try {
            if (url != null && url.trim().length() > 0) url = ""; else this.clearActionErrors();
            if (!hasActionErrors()) {
                this.clearErrorsAndMessages();
                loaiVatTuList = loaiVatTuServices.getAllLoaiVatTu();
            }
        } catch (Exception e) {
            addActionError(UIMessages.VIEW_ERROR);
        }
        return SUCCESS;
    }

    public String displayInput() {
        try {
            this.clearErrorsAndMessages();
            String newMaTuDong = loaiVatTuServices.getMaTuDongMoi();
            if (newMaTuDong != null && newMaTuDong.trim().length() > 0) {
                maPhanLoai = newMaTuDong;
            } else {
                maPhanLoai = UIMessages.MA_PHAN_LOAI_BAN_DAU;
            }
            clear();
        } catch (Exception e) {
            addActionError(UIMessages.VIEW_ERROR);
        }
        return INPUT;
    }

    public String displayUpdate() {
        try {
            this.clearActionErrors();
            if (loaiVatTuList != null && loaiVatTuList.size() > 0) {
                for (LoaiVatTuBean bean : loaiVatTuList) {
                    if (bean.getId().intValue() == phanLoaiId.intValue()) {
                        maPhanLoai = bean.getMaPhanLoai();
                        tenPhanLoai = bean.getTenPhanLoai();
                        ghiChu = bean.getGhiChu();
                    }
                }
            } else {
                addActionError(UIMessages.VIEW_ERROR);
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
                loaiVatTuServices.addLoaiVatTu(maPhanLoai, tenPhanLoai, ghiChu);
                auditLogServices.addAuditLog(getUserName(), "", "Tạo thông tin phân loại vật tư: " + UIMessages.MA_PHAN_LOAI + " " + maPhanLoai + ", " + UIMessages.TEN_PHAN_LOAI + " " + tenPhanLoai + ", " + UIMessages.GHI_CHU + " " + ghiChu);
                addActionMessage(UIMessages.SAVE_OK);
                clear();
            } else {
                return INPUT;
            }
        } catch (Exception e) {
            addActionError(UIMessages.SAVE_ERROR);
            return INPUT;
        }
        return displayInput();
    }

    public String update() {
        try {
            checkSave();
            if (!hasActionErrors()) {
                loaiVatTuServices.updateLoaiVatTu(phanLoaiId, maPhanLoai, tenPhanLoai, ghiChu);
                auditLogServices.addAuditLog(getUserName(), "", "Sửa thông tin phân loại vật tư: " + UIMessages.MA_PHAN_LOAI + " " + maPhanLoai + ", " + UIMessages.TEN_PHAN_LOAI + " " + tenPhanLoai + ", " + UIMessages.GHI_CHU + " " + ghiChu);
                this.addActionMessage(UIMessages.SAVE_OK);
                clear();
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

    public String delete() {
        try {
            checkDelete();
            if (!hasActionErrors()) {
                loaiVatTuServices.deleteLoaiVatTu(phanLoaiId);
            }
        } catch (Exception e) {
            addActionError(UIMessages.DELETE_ERROR);
        }
        setUrl(URL_REDIRECT);
        return REDIRECT;
    }

    private void checkSave() {
        clearErrorsAndMessages();
        if (maPhanLoai == null || maPhanLoai.trim().length() == 0) {
            addActionError(UIMessages.PHAN_LOAI_ERROR_MA);
        }
        if (tenPhanLoai == null || tenPhanLoai.trim().length() == 0) {
            addActionError(UIMessages.PHAN_LOAI_ERROR_TEN);
        }
    }

    private void checkDelete() {
        clearErrorsAndMessages();
        if (phanLoaiId == null || phanLoaiId == 0) {
            addActionError(UIMessages.PHAN_LOAI_ERROR_CHON);
        }
    }

    private void clear() {
        tenPhanLoai = "";
        ghiChu = "";
    }

    public void setMaPhanLoai(String maPhanLoai) {
        this.maPhanLoai = maPhanLoai;
    }

    public String getMaPhanLoai() {
        return maPhanLoai;
    }

    public void setTenPhanLoai(String tenPhanLoai) {
        this.tenPhanLoai = tenPhanLoai;
    }

    public String getTenPhanLoai() {
        return tenPhanLoai;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setPhanLoaiId(Long phanLoaiId) {
        this.phanLoaiId = phanLoaiId;
    }

    public Long getPhanLoaiId() {
        return phanLoaiId;
    }

    public void setLoaiVatTuList(List<LoaiVatTuBean> loaiVatTuList) {
        this.loaiVatTuList = loaiVatTuList;
    }

    public List<LoaiVatTuBean> getLoaiVatTuList() {
        return loaiVatTuList;
    }

    public void setLoaiVatTuServices(LoaiVatTuServices loaiVatTuServices) {
        this.loaiVatTuServices = loaiVatTuServices;
    }

    public LoaiVatTuServices getLoaiVatTuServices() {
        return loaiVatTuServices;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public String getGhiChu() {
        return ghiChu;
    }
}
