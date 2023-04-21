package br.com.mcampos.ejb.cloudsystem.anoto.page;

import br.com.mcampos.dto.anoto.AnotoPageDTO;
import java.io.Serializable;

public class AnotoPagePK implements Serializable {

    private String pageAddress;

    private Integer formId;

    private Integer padId;

    public AnotoPagePK() {
    }

    public AnotoPagePK(AnotoPageDTO dto) {
        setPageAddress(dto.getPageAddress());
        setFormId(dto.getFormId());
        setPadId(dto.getPadId());
    }

    public AnotoPagePK(AnotoPage entity) {
        setPageAddress(entity.getPageAddress());
        setFormId(entity.getFormId());
        setPadId(entity.getPadId());
    }

    public AnotoPagePK(String apg_id_ch, Integer frm_id_in, Integer pad_id_in) {
        this.pageAddress = apg_id_ch;
        this.formId = frm_id_in;
        this.padId = pad_id_in;
    }

    public boolean equals(Object other) {
        if (other instanceof AnotoPagePK) {
            final AnotoPagePK otherAnotoPagePK = (AnotoPagePK) other;
            final boolean areEqual = (otherAnotoPagePK.pageAddress.equals(pageAddress) && otherAnotoPagePK.formId.equals(formId) && otherAnotoPagePK.padId.equals(padId));
            return areEqual;
        }
        return false;
    }

    public int hashCode() {
        return super.hashCode();
    }

    String getPageAddress() {
        return pageAddress;
    }

    void setPageAddress(String apg_id_ch) {
        this.pageAddress = apg_id_ch;
    }

    Integer getFormId() {
        return formId;
    }

    void setFormId(Integer frm_id_in) {
        this.formId = frm_id_in;
    }

    Integer getPadId() {
        return padId;
    }

    void setPadId(Integer pad_id_in) {
        this.padId = pad_id_in;
    }
}
