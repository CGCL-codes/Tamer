package it.aco.mandragora.vo;

import java.util.Collection;

public class CopyNode31VO extends ValueObject {

    private String idCopyNode31;

    private String idCopyNode3;

    private String description;

    private Collection<CopyNode311VO> copyNode311VOs;

    public String getIdCopyNode31() {
        return idCopyNode31;
    }

    public void setIdCopyNode31(String idCopyNode31) {
        this.idCopyNode31 = idCopyNode31;
    }

    public String getIdCopyNode3() {
        return idCopyNode3;
    }

    public void setIdCopyNode3(String idCopyNode3) {
        this.idCopyNode3 = idCopyNode3;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Collection<CopyNode311VO> getCopyNode311VOs() {
        return copyNode311VOs;
    }

    public void setCopyNode311VOs(Collection<CopyNode311VO> copyNode311VOs) {
        this.copyNode311VOs = copyNode311VOs;
    }
}
