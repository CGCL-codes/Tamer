package cn.jsprun.domain;

public class CampaignsId implements java.io.Serializable {

    private static final long serialVersionUID = -8889734683601616627L;

    private Integer id;

    private Byte type;

    public CampaignsId() {
    }

    public CampaignsId(Integer id, Byte type) {
        this.id = id;
        this.type = type;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Byte getType() {
        return this.type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public boolean equals(Object other) {
        if ((this == other)) return true;
        if ((other == null)) return false;
        if (!(other instanceof CampaignsId)) return false;
        CampaignsId castOther = (CampaignsId) other;
        return ((this.getId() == castOther.getId()) || (this.getId() != null && castOther.getId() != null && this.getId().equals(castOther.getId()))) && ((this.getType() == castOther.getType()) || (this.getType() != null && castOther.getType() != null && this.getType().equals(castOther.getType())));
    }

    public int hashCode() {
        int result = 17;
        result = 37 * result + (getId() == null ? 0 : this.getId().hashCode());
        result = 37 * result + (getType() == null ? 0 : this.getType().hashCode());
        return result;
    }
}
